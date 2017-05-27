package com.example.repository_pattern_demo;

import android.app.Application;

import com.example.repository_pattern_demo.di.AppComponent;
import com.example.repository_pattern_demo.di.AppModule;
import com.example.repository_pattern_demo.di.DaggerAppComponent;
import com.example.repository_pattern_demo.di.DataModule;
import com.example.repository_pattern_demo.di.HttpModule;

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
public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .httpModule(new HttpModule())
                .build();
    }
}
