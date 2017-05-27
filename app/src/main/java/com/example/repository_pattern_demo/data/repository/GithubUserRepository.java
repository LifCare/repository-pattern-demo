package com.example.repository_pattern_demo.data.repository;

import com.example.repository_pattern_demo.data.model.User;
import com.example.repository_pattern_demo.data.rest.GithubApi;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

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
public class GithubUserRepository implements BaseRepository<User> {

    private final GithubApi githubApi;
    private final BriteDatabase database;

    public GithubUserRepository(GithubApi githubApi, BriteDatabase database) {
        this.githubApi = githubApi;
        this.database = database;
    }

    @Override
    public Observable<User> add(User item) {
        return null;
    }

    @Override
    public Observable<List<User>> add(List<User> items) {
        return null;
    }

    @Override
    public Observable<User> query(long id) {
        return null;
    }

    @Override
    public Observable<List<User>> query() {
        return null;
    }

    @Override
    public Observable<User> update(User item) {
        return null;
    }

    @Override
    public Observable<Integer> remove(User item) {
        return null;
    }

}
