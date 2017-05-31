package com.example.repository_pattern_demo.ui;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.repository_pattern_demo.data.model.User;
import com.example.repository_pattern_demo.databinding.LiLoadMoreBinding;
import com.example.repository_pattern_demo.databinding.LiUserBinding;

import java.util.ArrayList;
import java.util.List;

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
class GithubUsersAdapter extends RecyclerView.Adapter<GithubUsersAdapter.BaseHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_LOAD_MORE = 1;

    private boolean isLoadMoreEnabled;
    private List<User> mUsers = new ArrayList<>(0);
    private RequestManager mGlide;
    private LoadMoreListener mLoadMoreListener;

    public GithubUsersAdapter(RequestManager glide, LoadMoreListener loadMoreListener) {
        mGlide = glide;
        isLoadMoreEnabled = true;
        mLoadMoreListener = loadMoreListener;
    }

    public void addUsers(List<User> users) {
        int start = mUsers.size() - 1;
        mUsers.addAll(users);
        notifyItemRangeInserted(start, users.size());
    }

    public void setUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public void setIsLoadMoreEnabled(boolean isLoadMoreEnabled) {
        this.isLoadMoreEnabled = isLoadMoreEnabled;
    }

    @Override
    public int getItemCount() {
        return mUsers.size() == 0 ? 0 : mUsers.size() + (isLoadMoreEnabled ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return position < mUsers.size() ? TYPE_USER : TYPE_LOAD_MORE;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_USER:
                return new UserHolder(LiUserBinding.inflate(inflater, parent, false));
            case TYPE_LOAD_MORE:
                return new LoadMoreHolder(LiLoadMoreBinding.inflate(inflater, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.bind(position);
    }

    public class UserHolder extends BaseHolder {

        private LiUserBinding mBinding;

        public UserHolder(LiUserBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            itemView.setOnClickListener(v -> Toast.makeText(itemView.getContext(), "Soon! ... Maybe!", Toast.LENGTH_SHORT).show());
        }

        @Override
        public void bind(int position) {
            User user = mUsers.get(position);
            mGlide.load(user.avatar_url())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(mBinding.userImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(itemView.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mBinding.userImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            mBinding.userName.setText(user.login());
        }

    }

    public class LoadMoreHolder extends BaseHolder {

        LiLoadMoreBinding mBinding;

        public LoadMoreHolder(LiLoadMoreBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            if (mLoadMoreListener != null)
                mLoadMoreListener.loadMore(mUsers.get(mUsers.size() - 1).id());
        }

    }

    public abstract class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }

        public void bind(int position) {

        }

    }

    public interface LoadMoreListener {
        void loadMore(long lastId);
    }

}
