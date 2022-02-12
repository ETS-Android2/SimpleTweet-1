package com.codepath.apps.twitterprogram.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterprogram.R;
import com.codepath.apps.twitterprogram.models.TweetModel;

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

        public TweetViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO:remove this as binding later

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvActualID = itemView.findViewById(R.id.tvActualID);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvUpdateTime = itemView.findViewById(R.id.tvUpdateTime);
        }

        public void bind(TweetModel tweet) {


            Glide.with(context).load(tweet.user.publicImageURL).into(ivProfileImage);
            tvActualID.setText("@"+tweet.user.name);
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvUpdateTime.setText(tweet.getFormattedTimeStamp());

        }

    }
}
