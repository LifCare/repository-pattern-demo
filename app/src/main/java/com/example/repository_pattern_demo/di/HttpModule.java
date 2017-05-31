package com.example.repository_pattern_demo.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.repository_pattern_demo.BuildConfig;
import com.example.repository_pattern_demo.data.DbOpenHelper;
import com.example.repository_pattern_demo.data.TypeAdapterFactoryImpl;
import com.example.repository_pattern_demo.data.repository.GithubUserRepository;
import com.example.repository_pattern_demo.data.rest.GithubApi;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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
public class HttpModule {

    @Provides
    @Singleton
    Cache providesOkHttpCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(context.getCacheDir(), cacheSize);
    }


    @Provides
    @Singleton
    Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(TypeAdapterFactoryImpl.create());
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    public OkHttpClient providesOkHttpClient(Context context, Cache cache) {
        Stetho.initializeWithDefaults(context);
        HttpLoggingInterceptor debugInterceptor = new HttpLoggingInterceptor();
        debugInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(debugInterceptor)
                .addInterceptor(new ChuckInterceptor(context).showNotification(true))
                .cache(cache).build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public GithubApi providesRestApi(Retrofit retrofit) {
        return retrofit.create(GithubApi.class);
    }

    @Singleton
    @Provides
    public GithubUserRepository providesGithubUserRepository(GithubApi githubApi, BriteDatabase database) {
        return new GithubUserRepository(githubApi, database);
    }

}
