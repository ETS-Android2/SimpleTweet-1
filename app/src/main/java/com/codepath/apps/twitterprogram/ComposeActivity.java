package com.codepath.apps.twitterprogram;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.twitterprogram.models.TweetModel;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public final int MAX_TWEET_LENGTH = 280;

    public static final String TAG = ComposeActivity.class.getName();


    EditText etCompose;
    TextInputLayout tila;
    Button btnTweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tila = findViewById(R.id.tila);


        btnTweet.setEnabled(false);

        // Set click listening function on button

        tila.setHintEnabled(true);

        btnTweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String tweetContent = etCompose.getText().toString();
                //require an error?
                if (tweetContent.isEmpty()) {

                }
                else if (tweetContent.length() > MAX_TWEET_LENGTH){

                }
                else
                {
                    Toast.makeText(ComposeActivity.this, tweetContent , Toast.LENGTH_LONG).show();
                    client.publicTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to publishing tweet");
                            try {
                                TweetModel tweet = TweetModel.fromJson(json.jsonObject,false);
                                Log.i(TAG,"Published tweet says: "+ tweet);

                                Intent data = new Intent();
                                data.putExtra("parcel_tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, data);

                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to publishing tweet",throwable);
                        }
                    });
                }
                // Make an API call to Twitter for publishing the tweet
            }
        });



        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    btnTweet.setEnabled(false);
                    return;
                }

                if (tweetContent.length() > MAX_TWEET_LENGTH){
                    btnTweet.setEnabled(false);
                    return;
                }

                btnTweet.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}