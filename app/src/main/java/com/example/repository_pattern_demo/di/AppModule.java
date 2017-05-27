package com.example.repository_pattern_demo.di;

import android.content.Context;

import com.example.repository_pattern_demo.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
public class AppModule {

    private App mApp;

    public AppModule(App application) {
        mApp = application;
    }

    @Provides
    @Singleton
    App providesApp() {
        return mApp;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApp.getApplicationContext();
    }

}
