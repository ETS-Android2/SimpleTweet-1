package com.codepath.apps.twitterprogram.models;


import com.codepath.apps.twitterprogram.helpers.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TweetModel {

    public String body;
    public String createdAt;
    public TwitterUserModel user;
    public long ID;


    public String getFormattedTimeStamp()
    {
        return TimeFormatter.getTimeDifference(this.createdAt);
    }


    public static TweetModel fromJson(JSONObject jsonObject) throws JSONException {
        TweetModel tweet = new TweetModel();

        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = TwitterUserModel.fromJson(jsonObject.getJSONObject("user"));
        tweet.ID = jsonObject.getLong("id");

        return tweet;
    }

    public static List<TweetModel> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<TweetModel> tweets = new ArrayList<>();

        for( int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
