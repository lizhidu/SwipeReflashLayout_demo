package com.example.dulzh.swipereflashlayout_demo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements LoadMoreListView.OnLoadMoreListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private LoadMoreListView loadMoreListView;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout layout_swipe_refresh;
    ArrayList<String> arrayList;
    int curNum = 0;
    private Handler mHandler = new Handler();
    private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        layout_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        loadMoreListView = (LoadMoreListView) findViewById(R.id.listview);

        layout_swipe_refresh.setOnRefreshListener(this);
        //        loadMoreListView.setNoMoreToLoad(false);
        loadMoreListView.setOnScrollListener(this);
        loadMoreListView.setOnLoadMoreListener(this);

        for (int i = 0; i < 20; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("img", R.mipmap.photo);
            listItem.put("text", "Item " + i);
            mData.add(listItem);
        }
        myAdapter = new MyAdapter(this, mData);
        loadMoreListView.setAdapter(myAdapter);

    }

    /**
     * 模拟下拉刷新时获取新数据
     * simulate getting new data when pull to refresh
     */
    private void getNewTopData() {
        Map<String, Object> listItem = new HashMap<>();
        listItem.put("img", R.mipmap.ic_launcher);
        listItem.put("text", "New Top Item " + mData.size());
        mData.add(0, listItem);
    }

    /**
     * 模拟上拉加载更多时获得更多数据
     * simulate load more data to bottom
     */
    private void getNewBottomData() {
        int size = mData.size();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("img", R.mipmap.ic_launcher);
            listItem.put("text", "New Bottom Item " + (size + i));
            mData.add(listItem);


        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_swipe_refresh.setRefreshing(true); //请求开始的时候
                getNewTopData();
                myAdapter.notifyDataSetChanged();
                layout_swipe_refresh.setRefreshing(false); // xx 请求结束的时候
            }
        }, 1000);


    }

    @Override
    public void onLoadMore(AbsListView view) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                curNum++;
                if (curNum <= 2) {
                    getNewBottomData();
                    myAdapter.notifyDataSetChanged();
                    loadMoreListView.onLoadMoreComplete();  //false
                    loadMoreListView.setNoMoreToLoad(false); //有可加载数据
                } else {
                    loadMoreListView.setNoMoreToLoad(true); //数据全部加载完成

                }
            }
        }, 1000);

    }

    //－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
    public static class MyAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Map<String, Object>> mData;

        public MyAdapter(Context context, ArrayList<Map<String, Object>> mData) {
            this.context = context;
            this.mData = mData;
        }


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = null;
            ViewHolder viewHolder = null;
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_listview_content, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_content);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_photo);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(mData.get(position).get("text").toString());
            viewHolder.imageView.setImageResource((Integer) mData.get(position).get("img"));
            return convertView;
        }

        static class ViewHolder {
            TextView textView;
            ImageView imageView;
        }
    }
}
