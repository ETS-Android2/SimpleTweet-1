package com.codepath.apps.twitterprogram.models;


import com.codepath.apps.twitterprogram.helpers.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class TweetModel {

    public String body;
    public String createdAt;
    public String additionalImageURL;
    public TwitterUserModel user;
    public TweetModel retweet_container;
    public long ID;

    public TweetModel(){

    }

    public String getFormattedTimeStamp()
    {
        return TimeFormatter.getTimeDifference(this.createdAt);
    }


    public static TweetModel fromJson(JSONObject jsonObject, boolean retweettrack) throws JSONException {
        TweetModel tweet = new TweetModel();

        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = TwitterUserModel.fromJson(jsonObject.getJSONObject("user"));
        tweet.ID = jsonObject.getLong("id");
        try {
            if(retweettrack)
                tweet.retweet_container = fromJson(jsonObject.getJSONObject("retweeted_status"), false);
        }
        catch(JSONException e){
            if (e.getMessage().equals("No value for retweeted_status"))
            {
                tweet.retweet_container = null;
            }
            else
            {
                throw e;
            }
        }

        //complex system to pickup the images url beforehand
        try{
            JSONObject extended_entities_object = jsonObject.getJSONObject("extended_entities");
            JSONArray media_array = extended_entities_object.getJSONArray("media");
            for(int i = 0; i < media_array.length(); i++)
            {
                JSONObject current_json = media_array.getJSONObject(i);
                String type = current_json.getString("type");

                if(type.equals("photo"))
                {
                    tweet.additionalImageURL = current_json.getString("media_url_https");
                }
            }
        }
        catch(JSONException e){
            if (e.getMessage().equals("No value for extended_entities"))
            {
                tweet.additionalImageURL = null;
            }
            else if (e.getMessage().equals("No value for media"))
            {
                tweet.additionalImageURL = null;
            }
            else
            {
                throw e;
            }
        }

        return tweet;
    }

    public static List<TweetModel> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<TweetModel> tweets = new ArrayList<>();

        for( int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i), true));
        }
        return tweets;
    }
}
