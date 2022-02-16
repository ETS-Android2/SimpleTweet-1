package com.codepath.apps.twitterprogram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterprogram.adapters.TweetsAdapter;
import com.codepath.apps.twitterprogram.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterprogram.models.TweetModel;
import com.codepath.apps.twitterprogram.models.TwitterUserModel;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    ActionBar actionBar;
    TwitterUserModel currentUser;

    private EndlessRecyclerViewScrollListener scrollListener;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);





        //TODO:data binding required

        client = TwitterApp.getRestClient(this);

        actionBar = getSupportActionBar();
        loadCurrentUserOnActionBar();

        actionBar.setIcon(R.drawable.ic_launcher_twitter_round);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(R.color.twitter_blue);

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTweets.addItemDecoration(dividerItemDecoration);


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
                Log.i(TAG,"Load more: "+page);
            }
        };

        rvTweets.addOnScrollListener(scrollListener);


        populateHomeTimeline(1);
    }

    public void loadCurrentUserOnActionBar()
    {
        client.getVerificationCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject JSONobj = json.jsonObject;
                try {
                   currentUser = TwitterUserModel.fromJson(JSONobj);
                    actionBar.setSubtitle("@"+currentUser.screenName+"'s Home Timeline");
                } catch (JSONException e) {
                    Log.e(TAG,"Failed json", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"OnFailure()"+response, throwable);
            }
        });
    }

    public void loadNextDataFromApi(int offset){
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        client.getNextHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess():Next page"+"\n"+json.toString());

                JSONArray jsonArray = json.jsonArray;
                try{
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
        }, tweets.get(tweets.size()-1).ID-1);

        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
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