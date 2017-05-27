package com.example.repository_pattern_demo.data.rest;

import com.example.repository_pattern_demo.data.model.User;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

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
public interface GithubApi {

    @GET("users")
    Observable<List<User>> get();

}
