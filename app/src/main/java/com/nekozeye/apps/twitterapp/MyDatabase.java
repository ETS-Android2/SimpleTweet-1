package com.nekozeye.apps.twitterapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nekozeye.apps.twitterapp.models.SampleModel;
import com.nekozeye.apps.twitterapp.models.SampleModelDao;

@Database(entities={SampleModel.class}, version=1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract SampleModelDao sampleModelDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
