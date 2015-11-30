package com.example.dulzh.swipereflashlayout_demo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private LoadMoreListView loadMoreListView;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            String str = "dlzh" + i;
            arrayList.add(str);
        }
        myAdapter = new MyAdapter(this,arrayList);
        loadMoreListView = (LoadMoreListView) findViewById(R.id.list);
        loadMoreListView.setAdapter(myAdapter);

    }


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
