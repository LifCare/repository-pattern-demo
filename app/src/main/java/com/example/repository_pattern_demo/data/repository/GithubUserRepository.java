package com.example.repository_pattern_demo.data.repository;

import com.example.repository_pattern_demo.data.model.User;
import com.example.repository_pattern_demo.data.rest.GithubApi;
import com.squareup.sqlbrite.BriteDatabase;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
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
public class GithubUserRepository implements BaseRepository<User> {

    private final GithubApi mGithubApi;
    private final BriteDatabase mDatabase;

    public GithubUserRepository(GithubApi githubApi, BriteDatabase database) {
        this.mGithubApi = githubApi;
        this.mDatabase = database;
    }

    private void bindUser(User.InsertRow insertRow, User user) {
        insertRow.bind(
                user.login(),
                user.id(),
                user.avatar_url(),
                user.gravatar_id(),
                user.url(),
                user.html_url(),
                user.followers_url(),
                user.following_url(),
                user.gists_url(),
                user.starred_url(),
                user.subscriptions_url(),
                user.organizations_url(),
                user.repos_url(),
                user.events_url(),
                user.received_events_url(),
                user.type(),
                user.site_admin()
        );
    }

    private Observable<User> addToDb(User user) {
        User.InsertRow insertRow = new User.InsertRow(mDatabase.getWritableDatabase());
        bindUser(insertRow, user);
        long id = mDatabase.executeInsert(User.TABLE_NAME, insertRow.program);
        return id > 0 ? Observable.just(user) : Observable.error(new SQLException("Failed to insert user"));
    }

    private Observable<List<User>> addToDb(List<User> users) {
        User.InsertRow insertRow = new User.InsertRow(mDatabase.getWritableDatabase());
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (User user : users) {
                bindUser(insertRow, user);
                long id = mDatabase.executeInsert(User.TABLE_NAME, insertRow.program);
                if (id == -1) return Observable.error(new SQLException("Failed to insert user"));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return Observable.just(users);
    }

    private Observable<User> queryFromDb(String username) {
        return mDatabase.createQuery(User.TABLE_NAME, User.FACTORY.selectByLogin(username).statement)
                .mapToOne(User.MAPPER);
    }

    private Observable<List<User>> queryFromDb() {
        return mDatabase.createQuery(User.TABLE_NAME, User.FACTORY.selectAll().statement)
                .mapToList(User.MAPPER);
    }

    private Observable<Integer> removeFromDb(User address) {
        User.DeleteById deleteById = new User.DeleteById(mDatabase.getWritableDatabase());
        deleteById.bind(address.id());
        return Observable.just(mDatabase.executeUpdateDelete(User.TABLE_NAME, deleteById.program));
    }


    @Override
    public Observable<User> add(User item) {
        throw new UnsupportedOperationException("This is not meant to happen.");
    }

    @Override
    public Observable<List<User>> add(List<User> items) {
        throw new UnsupportedOperationException("This is not meant to happen.");
    }

    @Override
    public Observable<User> query(String username) {
        Observable<User> db = queryFromDb(username);
        Observable<User> server = mGithubApi.get(username).doOnNext(this::addToDb);
        return Observable.merge(db, server).distinct();
    }

    @Override
    public Observable<List<User>> query() {
        return queryPaginated(0);
    }

    @Override
    public Observable<List<User>> queryPaginated(long lastId) {
        Observable<List<User>> server = mGithubApi.get(lastId).doOnNext(this::addToDb);
        Observable<List<User>> db = queryFromDb()
                .doOnSubscribe(() -> server.subscribe(users -> { }, Timber::e));
        return db.publish(u -> Observable.merge(u.take(1).filter(users -> users.size() > 0), u.skip(1)));
//        return server;
//        return Observable.combineLatest(db, Observable.concat(null, server),
//                (dbAddress, serverAddress) -> dbAddress.size() == 0 && serverAddress == null ? null : dbAddress)
//                .filter(addresses -> addresses != null)
//                .distinct();
    }

    @Override
    public Observable<User> update(User item) {
        throw new UnsupportedOperationException("This is not meant to happen.");
    }

    @Override
    public Observable<Integer> remove(User item) {
        throw new UnsupportedOperationException("This is not meant to happen.");
    }

}
