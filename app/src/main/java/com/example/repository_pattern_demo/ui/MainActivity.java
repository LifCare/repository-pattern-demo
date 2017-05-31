package com.example.repository_pattern_demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.repository_pattern_demo.App;
import com.example.repository_pattern_demo.R;
import com.example.repository_pattern_demo.data.repository.GithubUserRepository;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements GithubUsersAdapter.LoadMoreListener {

    @Inject GithubUserRepository mGithubUserRepository;

    private RecyclerView mRecyclerView;
    private GithubUsersAdapter mGithubUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) (getApplicationContext())).component().inject(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGithubUsersAdapter = new GithubUsersAdapter(Glide.with(this), this);
        mRecyclerView.setAdapter(mGithubUsersAdapter);
        mGithubUserRepository.query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mGithubUsersAdapter::setUsers, throwable -> {
                    Timber.e(throwable);
                    mGithubUsersAdapter.setIsLoadMoreEnabled(false);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadMore(long lastId) {
        mGithubUserRepository.queryPaginated(lastId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mGithubUsersAdapter::setUsers, throwable -> {
                    Timber.e(throwable);
                    mGithubUsersAdapter.setIsLoadMoreEnabled(false);
                });
    }

}
