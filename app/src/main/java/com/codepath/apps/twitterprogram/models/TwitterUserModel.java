package com.codepath.apps.twitterprogram.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TwitterUserModel {

    public String name;
    public String screenName;
    public String publicImageURL;


    public static TwitterUserModel fromJson(JSONObject jsonObject) throws JSONException {
        TwitterUserModel user = new TwitterUserModel();

        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.publicImageURL = jsonObject.getString("profile_image_url_https");

        return user;
    }


}
