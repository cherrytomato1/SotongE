package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.hyokwon.sotong.URLS.server;

public class feedListActivity extends Activity {

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    String subnm, fid, fdate ,ID, LECN, NAME ,fstate;
    TextView name;

    private class ViewHolder {
        public TextView mText;
        public TextView mName;
        public TextView mDate;
        public ImageView mIcon;
        public TextView mStat;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void addItem(Drawable mIcon, String mTitle, String mDate, String mName, String mStat) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mTitle;
            addInfo.mDate = mDate;
            addInfo.mName = mName;
            addInfo.mIcon = mIcon;
            addInfo.mStat = mStat;
            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void clear() {
            mListData.clear();
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.opt_item, null);
                holder.mStat = (TextView) convertView.findViewById(R.id.mStat);
                holder.mText = (TextView) convertView.findViewById(R.id.mTitle);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);
                holder.mName = (TextView) convertView.findViewById(R.id.mName);
                holder.mIcon = (ImageView) convertView.findViewById(R.id.mIcon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);
            if(mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            } else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.mText.setText(mData.mTitle);
            holder.mStat.setText(mData.mStat);
            holder.mDate.setText(mData.mDate);
            holder.mName.setText(mData.mName);
            return convertView;
        }
    }
    public void BACKbtn(View v) {
        finish();
    }
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedlist);
        mListView = (ListView) findViewById(R.id.mList);
        ArrayList arrayList = new ArrayList<String>();
        INDAT ind = new INDAT();
        Intent itt = getIntent();
        ID = itt.getStringExtra("ID");
        LECN = itt.getStringExtra("LECTURE");
        NAME = itt.getStringExtra("NAME");

        name= (TextView) findViewById(R.id.namenum);
        name.setText(NAME+" 교수님");

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);



        try {
            Log.d("디버그", "1단계");
            String res = ind.execute(server + "/load_FEED-List.php", ID, LECN, null, null, null).get();            //결과를 res에 받음
            Log.d("디버그", "2단계");
            BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
            Log.d("디버그", "3단계");
            String line;
            subnm = "";
            fid = "";
            fdate = "123123";
            int i = 0;
            while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                Log.d("디버그", line + " 불러옴");
                switch (i) {
                    case 0:
                        subnm = line;
                        i++;
                        break;
                    case 1:
                        fid = line;
                        i++;
                        break;
                    case 2:
                        fdate = line;
                        i++;
                        break;
                    case 3:

                        switch(line)
                        {
                            case "1" : fstate = "안읽음"; break;
                            case "2" : fstate = "읽음"; break;
                            case "3" : fstate = "완료"; break;
                            default: fstate= "없음"; break;
                        }
                        mAdapter.addItem(getResources().getDrawable(R.drawable.avatar), subnm, fdate, fid, fstate);

                        i = 0;
                        break;
                    default:
                        break;
                }
            }
            Log.d("디버그", "4단계!!!!");
        } catch (Exception e) {
            Log.e("디버그", "ERROR setlist", e);
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ListData mData = mAdapter.mListData.get(position);

                Intent intnt = new Intent(getApplicationContext(),ViewevalActivity.class);

                intnt.putExtra("sNAME", mData.mTitle);
                intnt.putExtra("fDATE", mData.mDate);
                intnt.putExtra("fID", mData.mName);
                intnt.putExtra("NAME", NAME);
                intnt.putExtra("ID", ID);

                startActivityForResult(intnt,0);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            INDAT ind2 = new INDAT();
            mAdapter.clear();
            try {
                Log.d("디버그", "1단계");
                String res = ind2.execute(server + "/load_FEED-List.php", ID, LECN, null, null, null).get();            //결과를 res에 받음
                Log.d("디버그", "2단계");
                BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
                Log.d("디버그", "3단계");
                String line;
                subnm = "";
                fid = "";
                fdate = "123123";
                int i = 0;
                while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                    Log.d("디버그", line + " 불러옴");
                    switch (i) {
                        case 0:
                            subnm = line;
                            i++;
                            break;
                        case 1:
                            fid = line;
                            i++;
                            break;
                        case 2:
                            fdate = line;
                            i++;
                            break;
                        case 3:

                            switch(line)
                            {
                                case "1" : fstate = "안읽음"; break;
                                case "2" : fstate = "읽음"; break;
                                case "3" : fstate = "완료"; break;
                                default: fstate= "없음"; break;
                            }
                            mAdapter.addItem(getResources().getDrawable(R.drawable.avatar), subnm, fdate, fid, fstate);

                            i = 0;
                            break;
                        default:
                            break;
                    }
                }
                Log.d("디버그", "4단계!!!!");
            } catch (Exception e) {
                Log.e("디버그", "ERROR setlist", e);
            }

        } else {   // RESULT_CANCEL
        }
//        } else if (requestCode == REQUEST_ANOTHER) {
//            ...
    }

}