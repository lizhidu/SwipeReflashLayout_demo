package com.example.dulzh.swipereflashlayout_demo;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by dulzh on 11/30/15.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private OnScrollListener mScrollListenerDelegate;
    private OnLoadMoreListener mOnLoadMoreListener;

    private AdapterDataSetObserver mDataSetObserver = new AdapterDataSetObserver();

    private boolean mIsLoadMore = false;
    private boolean mNoMoreLoad = false;
    private int mCurrentLoadState;

    private View mLoadMoreFooterView;
    private ProgressBar mLoadingProgressBar;
    private TextView mNoMoreTextView;

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        ListAdapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerDataSetObserver(mDataSetObserver);
            if (adapter.isEmpty()) {
                mLoadMoreFooterView.setVisibility(GONE);
            } else {
                mLoadMoreFooterView.setVisibility(VISIBLE);
            }
        }
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            ListAdapter adapter = getAdapter();

            mLoadMoreFooterView.setVisibility(VISIBLE);
            if (adapter.isEmpty()) {
//                mNoMoreTextView.setText(R.string.empty);
                mNoMoreTextView.setText("当前数据加载失败！");

            } else {
//                mNoMoreTextView.setText(R.string.no_more);
                mNoMoreTextView.setText("已无加载数据！");

            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mLoadMoreFooterView.setVisibility(View.GONE);
        }
    }

    private void init() {
        final Context c = getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        mLoadMoreFooterView = inflater.inflate(R.layout.footer_load_more, null);

        mLoadingProgressBar = (ProgressBar) mLoadMoreFooterView.findViewById(R.id.progressbar_loading);
        mNoMoreTextView = (TextView) mLoadMoreFooterView.findViewById(R.id.tv_no_more);

        this.addFooterView(mLoadMoreFooterView, null, false);
        mLoadMoreFooterView.setVisibility(GONE);
        mNoMoreTextView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);

        super.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE ) {
            view.invalidateViews();
        }

        mCurrentLoadState = scrollState;
        if (mScrollListenerDelegate != null) {
            mScrollListenerDelegate.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.mScrollListenerDelegate = l;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        this.mOnLoadMoreListener = l;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollListenerDelegate != null) {
            mScrollListenerDelegate.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mOnLoadMoreListener != null) {
            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
            if (!mIsLoadMore && loadMore
                    && mCurrentLoadState != SCROLL_STATE_IDLE
                    && !mNoMoreLoad) {
                mIsLoadMore = true;
                mOnLoadMoreListener.onLoadMore(view);
            }
        }
    }

    public void onLoadMoreComplete() {
        mIsLoadMore = false;
    }

    public void setNoMoreToLoad(boolean noMoreToLoad) {
        mNoMoreLoad = noMoreToLoad;
        if (noMoreToLoad) {
            mLoadingProgressBar.setVisibility(GONE);
            mNoMoreTextView.setVisibility(VISIBLE);
        } else {
            mLoadingProgressBar.setVisibility(VISIBLE);
            mNoMoreTextView.setVisibility(GONE);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(AbsListView view);
    }
}

