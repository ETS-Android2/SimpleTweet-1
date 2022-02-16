package com.codepath.apps.twitterprogram.adapters;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterprogram.DetailTweetActivity;
import com.codepath.apps.twitterprogram.R;
import com.codepath.apps.twitterprogram.models.TweetModel;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder>{
    // Pass in the context and list of tweets
    Context context;
    List<TweetModel> tweets;

    public TweetsAdapter(Context context, List<TweetModel> tweets){
        this.context = context;
        this.tweets = tweets;
    }


    public void clear(){
        this.tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TweetModel> list){
        this.tweets.addAll(list);
        notifyDataSetChanged();
    }


    // For each row, inflate the layout
    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);


        return new TweetViewHolder(view);
    }


    // Bind values based on the position of the elements
    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {
        //Get data at the position
        TweetModel tweet = tweets.get(position);
        //Bind the tweet with the viewholder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }




    // Define a ViewHolder

    public class TweetViewHolder extends RecyclerView.ViewHolder {


        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvActualID;
        TextView tvUpdateTime;

        TextView tvRTMarker;
        ImageView ivRTImage;

        RelativeLayout rlTweeterTimelineRecyclerLayout;

        public TweetViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO:remove this as binding later

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvActualID = itemView.findViewById(R.id.tvActualID);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvUpdateTime = itemView.findViewById(R.id.tvUpdateTime);


            tvRTMarker = itemView.findViewById(R.id.tvRTMarker);
            ivRTImage = itemView.findViewById(R.id.ivRTImage);


            rlTweeterTimelineRecyclerLayout = itemView.findViewById(R.id.rlTweeterTimelineRecyclerLayout);

        }

        public void bind(TweetModel tweet) {

            if(tweet.retweet_container == null)
            {
                tvRTMarker.setVisibility(View.GONE);
                ivRTImage.setVisibility(View.GONE);

                //rest
                Glide.with(context).load(tweet.user.publicImageURL).into(ivProfileImage);
                tvActualID.setText("@"+tweet.user.name);
                tvBody.setText(tweet.body);
                tvScreenName.setText(tweet.user.screenName);
                tvUpdateTime.setText(tweet.getFormattedTimeStamp());
            }
            else
            {

                tvRTMarker.setVisibility(View.VISIBLE);
                ivRTImage.setVisibility(View.VISIBLE);

                tvRTMarker.setText(tweet.user.screenName+" Retweeted");
                ivRTImage.setImageResource(R.drawable.ic_vector_retweet_gray);

                //use the retweeted tweet for main body
                Glide.with(context).load(tweet.retweet_container.user.publicImageURL).into(ivProfileImage);
                tvActualID.setText("@"+tweet.retweet_container.user.name);
                tvBody.setText(tweet.retweet_container.body);
                tvScreenName.setText(tweet.retweet_container.user.screenName);
                tvUpdateTime.setText(tweet.retweet_container.getFormattedTimeStamp());
            }

            rlTweeterTimelineRecyclerLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailTweetActivity.class);
                    i.putExtra("parcel_tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });



        }

    }
}
