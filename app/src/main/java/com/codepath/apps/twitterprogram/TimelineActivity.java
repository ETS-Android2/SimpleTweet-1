package com.codepath.apps.twitterprogram;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitterprogram.adapters.TweetsAdapter;
import com.codepath.apps.twitterprogram.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterprogram.models.TweetModel;
import com.codepath.apps.twitterprogram.models.TwitterUserModel;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    private static final int REQUEST_CODE = 20;

    ActivityResultLauncher<Intent> actreslauncher;

    TwitterClient client;
    RecyclerView rvTweets;
    List<TweetModel> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    ActionBar actionBar;
    TwitterUserModel currentUser;

    Context self_context;

    FloatingActionButton floatButton;

    private EndlessRecyclerViewScrollListener scrollListener;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        self_context = this;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);


        //TODO:data binding required

        client = TwitterApp.getRestClient(this);

        actionBar = getSupportActionBar();






        actreslauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                            TweetModel tweet = (TweetModel) Parcels.unwrap(data.getParcelableExtra("parcel_tweet"));
                            adapter.addFirst(tweet);

                            rvTweets.scrollToPosition(0);
                        }
                    }
                });





        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(R.color.twitter_blue);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching data...");
                populateHomeTimeline();
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



        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
                Log.i(TAG,"Load more: "+page);
            }
        };

        rvTweets.addOnScrollListener(scrollListener);

        floatButton = findViewById(R.id.fabCompose);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self_context, ComposeActivity.class);
                actreslauncher.launch(intent);
                //showEditDialog();
            }
        });


        populateHomeTimeline();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment editNameDialogFragment = ComposeDialogFragment.newInstance();
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }


    //menu options setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void loadNextDataFromApi(int page){
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



    public void populateHomeTimeline() {
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