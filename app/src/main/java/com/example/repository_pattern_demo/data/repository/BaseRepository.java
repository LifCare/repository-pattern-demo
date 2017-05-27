package com.example.repository_pattern_demo.data.repository;

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
public interface BaseRepository<T> {
    Observable<T> add(T item);
    Observable<List<T>> add(List<T> items);
    Observable<T> query(long id);
    Observable<List<T>> query();
    Observable<T> update(T item);
    Observable<Integer> remove(T item);
}