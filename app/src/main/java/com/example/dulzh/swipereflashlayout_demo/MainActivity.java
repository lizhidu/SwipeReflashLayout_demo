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
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements LoadMoreListView.OnLoadMoreListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private LoadMoreListView loadMoreListView;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout layout_swipe_refresh;
    ArrayList<String> arrayList;
    int curNum = 0;
    private Handler mHandler = new Handler();
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

        arrayList = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            String str = "dlzh" + i;
            arrayList.add(str);
        }
        myAdapter = new MyAdapter(this, arrayList);
        loadMoreListView.setAdapter(myAdapter);

    }

    @Override
    public void onLoadMore(AbsListView view) {

        loadMoreListView.setNoMoreToLoad(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onRefresh() {
        curNum++;
        String str = new String("dlzh_new"+curNum++);
        arrayList.add(str);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myAdapter.notifyDataSetChanged();
                layout_swipe_refresh.setRefreshing(false);
            }
        }, 2000);


    }

//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
    public static class MyAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> arrayList;

        public MyAdapter(Context context, ArrayList<String> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
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
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(arrayList.get(position));
            return convertView;
        }

        static class ViewHolder {
            TextView textView;
        }
    }
}
