package com.codepath.apps.twitterprogram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.twitterprogram.models.TweetModel;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link ComposeDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ComposeDialogFragment extends DialogFragment {

    EditText etCompose;
    TextInputLayout tila;
    Button btnTweet;


    public final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = ComposeDialogFragment.class.getName();

    TwitterClient client;

    public ComposeDialogFragment() {
        // Required empty public constructor
    }

    public static ComposeDialogFragment newInstance() {
        ComposeDialogFragment frag = new ComposeDialogFragment();
        return frag;
    }



    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_compose_dialog, container);
    }



    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getRestClient(view.getContext());

        etCompose = view.findViewById(R.id.etCompose);
        btnTweet = view.findViewById(R.id.btnTweet);
        tila = view.findViewById(R.id.tila);

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
                    client.publicTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess to publishing tweet");
                            try {
                                TweetModel tweet = TweetModel.fromJson(json.jsonObject,false);

                                // Close the dialog and return back to the parent activity
                                dismiss();

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

    }
}