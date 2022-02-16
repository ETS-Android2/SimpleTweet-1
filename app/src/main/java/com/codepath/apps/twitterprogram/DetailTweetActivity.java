package com.codepath.apps.twitterprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterprogram.models.TweetModel;

import org.parceler.Parcels;

public class DetailTweetActivity extends AppCompatActivity {

    TweetModel tweet;

    ImageView ivProfileImage;
    ImageView ivadditionalImage;
    ImageView ivRTImage;

    TextView tvScreenName;
    TextView tvActualID;
    TextView tvBody;
    TextView tvRTMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        tweet = (TweetModel) Parcels.unwrap(getIntent().getParcelableExtra("parcel_tweet"));
        //view setup

        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivadditionalImage = findViewById(R.id.ivadditionalImage);
        ivRTImage = findViewById(R.id.ivRTImage);

        tvScreenName = findViewById(R.id.tvScreenName);
        tvActualID = findViewById(R.id.tvActualID);
        tvBody = findViewById(R.id.tvBody);
        tvRTMarker = findViewById(R.id.tvRTMarker);


        //data binding

        TweetModel decider = tweet;
        if(tweet.retweet_container != null)
        {
            ivRTImage.setVisibility(View.VISIBLE);
            tvRTMarker.setVisibility(View.VISIBLE);
            decider = tweet.retweet_container;
        }
        else
        {
            ivRTImage.setVisibility(View.GONE);
            tvRTMarker.setVisibility(View.GONE);
        }


        Glide.with(this).load(decider.user.publicImageURL).into(ivProfileImage);
        tvScreenName.setText(decider.user.screenName);
        tvActualID.setText("@"+decider.user.name);
        tvBody.setText(decider.body);

        //these won't be used if retweet is not available anyways
        tvRTMarker.setText(tweet.user.screenName+" Retweeted");
        ivRTImage.setImageResource(R.drawable.ic_vector_retweet_gray);

        //aditional image setup

        if(decider.additionalImageURL == null)
        {
            //no image. hide the glide
            ivadditionalImage.setVisibility(View.GONE);
        }
        else
        {
            //has additional media image. load.
            Glide.with(this).load(decider.additionalImageURL).into(ivadditionalImage);
        }

    }
}