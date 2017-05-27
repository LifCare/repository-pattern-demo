package com.example.repository_pattern_demo.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.repository_pattern_demo.BuildConfig;
import com.example.repository_pattern_demo.data.DbOpenHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/*
 * Copyright 2017 Sourabh Verma
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Module
public final class DataModule {

    @Provides
    @Singleton
    public SQLiteOpenHelper provideOpenHelper(Context context) {
        return new DbOpenHelper(context);
    }

    @Provides
    @Singleton
    public SqlBrite provideSqlBrite() {
        SqlBrite.Logger logger = message -> Timber.tag("Database").v(message);
        return new SqlBrite.Builder().logger(logger).build();
    }

    @Provides
    @Singleton
    public BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(BuildConfig.DEBUG);
        return db;
    }

}
