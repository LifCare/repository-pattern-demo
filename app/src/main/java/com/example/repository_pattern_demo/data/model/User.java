package com.example.repository_pattern_demo.data.model;

import android.database.Cursor;
import android.os.Parcelable;

import com.example.epository_pattern_demo.data.db.UserModel;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import rx.functions.Func1;


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
@AutoValue
public abstract class User implements UserModel, Parcelable {

    public static final Factory<User> FACTORY = new Factory<>(AutoValue_User::new);

    public static final Func1<Cursor, User> MAPPER = FACTORY.selectAllMapper()::map;

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder login(String login);
        public abstract Builder id(Long id);
        public abstract Builder avatar_url(String avatar_url);
        public abstract Builder gravatar_id(String gravatar_id);
        public abstract Builder url(String url);
        public abstract Builder html_url(String html_url);
        public abstract Builder followers_url(String followers_url);
        public abstract Builder following_url(String following_url);
        public abstract Builder gists_url(String gists_url);
        public abstract Builder starred_url(String starred_url);
        public abstract Builder subscriptions_url(String subscriptions_url);
        public abstract Builder organizations_url(String organizations_url);
        public abstract Builder repos_url(String repos_url);
        public abstract Builder events_url(String events_url);
        public abstract Builder received_events_url(String received_events_url);
        public abstract Builder type(String type);
        public abstract Builder site_admin(Boolean site_admin);
        public abstract User build();
    }

}
