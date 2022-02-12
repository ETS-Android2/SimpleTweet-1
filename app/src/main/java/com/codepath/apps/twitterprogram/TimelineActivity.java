package com.codepath.apps.twitterprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterprogram.adapters.TweetsAdapter;
import com.codepath.apps.twitterprogram.models.TweetModel;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    TwitterClient client;
    RecyclerView rvTweets;
    List<TweetModel> tweets;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        //TODO:data binding required

        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Init tweetlist
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);
        //Recycler view setup
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);

        client = TwitterApp.getRestClient(this);
        populateHomeTimeline();
    }

    private void populateHomeTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess()"+"\n"+json.toString());

                JSONArray jsonArray = json.jsonArray;
                try{
                    tweets.addAll(TweetModel.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e){
                    Log.e(TAG, "Json exeption", e);
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"OnFailure()"+response, throwable);
            }
        });
    }
}