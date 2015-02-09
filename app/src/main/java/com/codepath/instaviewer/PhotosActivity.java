package com.codepath.instaviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    private SwipeRefreshLayout swipeContainer;
    public static final String CLIENT_ID = "734b42e761664c7ea88bb12acc7d1b56"; //PUT IN XML AND ENCRYPT
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        photos = new ArrayList<InstagramPhoto>();

        //Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);

        fetchPopularPhotos();
        //Setup refresh Listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                if(swipeContainer != null) {
                    swipeContainer.setRefreshing(false);
                }
                if(aPhotos != null) {
                    aPhotos.notifyDataSetChanged();
                }

                //Send out API request to Popular photos
                fetchPopularPhotos();

                swipeContainer.setRefreshing(false);
                //fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
    /*Send out network request
    Client ID: 734b42e761664c7ea88bb12acc7d1b56
    	- Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
	- Response
		Type: { "Data"  = >  [x]  ==> "type } ("image" or video"}
		Caption: { "Data"  = >  [x]  ==> "caption"  ==> "text" } ("image" or videro")
		ImageURL : { "Data"  = >  [x]  ==> "images"  ==> "standard_resolution" ==> "url "}
		Author Name{ "Data"  = >  [x]  ==> "user"  ==> "username" }

    */
    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create new http client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler(){
            //onSuccess (worked , 200)
            // Response root is dictionary so we use JSONObject else we use JsonArray
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Expecting a JSON object
                //Type: { "Data"  = >  [x]  ==> "type } ("image" or video"}
                //Caption: { "Data"  = >  [x]  ==> "caption"  ==> "text" } ("image" or videro")
                //ImageURL : { "Data"  = >  [x]  ==> "images"  ==> "standard_resolution" ==> "url "}
                //Author Name{ "Data"  = >  [x]  ==> "user"  ==> "username" }
                // Iterate each of the photo items and decode the iteem into a java Object
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data"); // array of posts
                    photos.clear();
                    //iterate the array
                    for(int i=0; i< photosJSON.length(); i++) {
                        //get the json object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        //Author Name: {"Data"  = >  [x]  ==> "user"  ==> "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.likes = photoJSON.getJSONObject("likes").getInt("count");
                        photo.profilePic = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.createdTime = photoJSON.getLong("created_time");
                        photo.comments = new JSONArray();
                        photo.comments = photoJSON.getJSONObject("comments").getJSONArray("data");
                        photo.commentCount = photo.comments.length();
                       /* if(photo.commentCount > 0) {
                            ListView lvComments = (ListView) findViewById(R.id.lvComments);
                            ArrayAdapter<JSONArray> aComments = new ArrayAdapter<JSONArray>(getBaseContext(),  android.R.layout.simple_list_item_1, (java.util.List<JSONArray>) photo.comments);
                           aComments.notifyDataSetChanged();

                        }*/
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
                //Log.i("DEBUG", response.toString());
            }

            //onFailure

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Do Somthing
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
