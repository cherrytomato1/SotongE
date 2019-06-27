package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class LastevalActivity extends Activity {
    private ListView feedList = null;
    private ListViewAdapter mAdapter = null;
    TextView name_num;
    String NAME, ID;

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void addItem(String mTitle) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mTitle;
            mListData.add(addInfo);
        }


        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public String getName(int position){
            Log.d("디버그","겟네임1");
            ListData mData = mListData.get(position);
            Log.d("디버그","겟네임 : "+mData.mTitle);
            return mData.mTitle;
        }
        private class ViewHolder {
            public TextView mText;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new ListViewAdapter.ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lectlist_item, null);

                holder.mText = (TextView) convertView.findViewById(R.id.lect1);

                convertView.setTag(holder);
            } else {
                holder = (ListViewAdapter.ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);


            holder.mText.setText(mData.mTitle);
            return convertView;
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lasteval);
        feedList = (ListView) findViewById(R.id.feedback_view);
        name_num = (TextView) findViewById(R.id.namenum);
        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        name_num.setText(NAME + "(" + ID + ")");
        mAdapter = new ListViewAdapter(this);
        feedList.setAdapter(mAdapter);
        mAdapter.addItem("교수님의 피드백 입니다.");
    }
}
