package com.codepath.apps.twitterprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
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
    SwipeRefreshLayout swipeContainer;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        //TODO:data binding required

        client = TwitterApp.getRestClient(this);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching data...");
                populateHomeTimeline(1);
            }


        });



        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Init tweetlist
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);
        //Recycler view setup
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);


        populateHomeTimeline(1);
    }



    public void populateHomeTimeline(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess()"+"\n"+json.toString());

                JSONArray jsonArray = json.jsonArray;
                try{
                    adapter.clear();
                    adapter.addAll(TweetModel.fromJsonArray(jsonArray));
                    swipeContainer.setRefreshing(false);
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