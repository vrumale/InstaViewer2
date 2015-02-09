package com.codepath.instaviewer;

import org.json.JSONArray;

/**
 * Created by vrumale on 2/6/2015.
 * Our Data model we only care about JSON values we need
 */
public class InstagramPhoto {
    public String username;
    public String caption;
    public String imageUrl;
    public int likes;
    public String profilePic;
    public long createdTime;
    public int commentCount;
    public JSONArray comments;
}
