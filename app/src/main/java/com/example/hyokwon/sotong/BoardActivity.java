package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.hyokwon.sotong.URLS.server;

public class BoardActivity extends Activity {
    private ListView boardList = null;
    private ListViewAdapter mAdapter = null;
    String NAME1, ID, STAT, LECTURE;
    String btext, bid, btitle, bdate;
    LinearLayout mLayout, bar;
    TextView name_num, lectname;
    Button makeBtn;
    ImageButton back;

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void addItem(String mTitle, String mName, String mDate, String mGle) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mTitle;
            addInfo.mDate = mDate;
            addInfo.mName = mName;
            addInfo.mGle = mGle;

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

        public String getName(int position) {
            Log.d("디버그", "겟네임1");
            ListData mData = mListData.get(position);
            Log.d("디버그", "겟네임 : " + mData.mTitle);
            return mData.mTitle;
        }

        private class ViewHolder {
            public TextView mText;
            public TextView title;
            public TextView date;
            public TextView name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.board_item, null);

                holder.mText = (TextView) convertView.findViewById(R.id.gle);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.name = (TextView) convertView.findViewById(R.id.bID);
                holder.date = (TextView) convertView.findViewById(R.id.date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);


            holder.mText.setText(mData.mGle);
            holder.title.setText(mData.mTitle);
            holder.date.setText(mData.mDate);
            holder.name.setText(mData.mName);
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        boardList = (ListView) findViewById(R.id.board_list);
        makeBtn = (Button) findViewById(R.id.make);
        back = (ImageButton) findViewById(R.id.board_backbtn);
        mAdapter = new ListViewAdapter(this);
        boardList.setAdapter(mAdapter);
        name_num = (TextView) findViewById(R.id.namenum);
        lectname = (TextView) findViewById(R.id.let_name);
        mLayout = (LinearLayout) findViewById(R.id.boardlay);
        bar = (LinearLayout) findViewById(R.id.bar);
        Intent intent = getIntent();
        STAT = intent.getStringExtra("STAT");
        NAME1 = intent.getStringExtra("NAME");
        LECTURE = intent.getStringExtra("LECTURE");
        ID = intent.getStringExtra("ID");
        if (STAT.equals("STU")) {
            name_num.setText(NAME1 + "(" + ID + ")");
            makeBtn.setVisibility(View.GONE);
        } else {
            name_num.setText(NAME1 + " 교수님");
            mLayout.setBackgroundResource(R.drawable.probackground);
            bar.setBackgroundColor(Color.rgb(255, 181, 161));
            makeBtn.setBackgroundResource(R.drawable.nemo3);
            boardList.setBackgroundColor(Color.rgb(255, 221, 212));
        }
        lectname.setText(LECTURE);

        INDAT ind = new INDAT();
        try {
            Log.d("디버그", "1단계");
            String res = ind.execute(server + "/load_BOARD-List.php", ID, LECTURE, null, null, null).get();            //결과를 res에 받음
            Log.d("디버그", "2단계");
            BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
            Log.d("디버그", "3단계");
            String line;

            int i = 0;
            while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                Log.d("디버그", line + " 불러옴");
                switch (i) {
                    case 0:
                        bid = line;
                        i++;
                        break;
                    case 1:
                        btitle = line;
                        i++;
                        break;
                    case 2:
                        btext = line;
                        i++;
                        break;
                    case 3:
                        bdate = line;
                        mAdapter.addItem(btitle, bid, bdate, btext);

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

        makeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WriteboardActivity.class);
                i.putExtra("NAME", NAME1);
                i.putExtra("LECTURE", LECTURE);
                i.putExtra("BID","-1");
                Log.d("디버그","bid : "+"-1");
                startActivityForResult(i, 0);
            }
        });
        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ListData mData = mAdapter.mListData.get(position);
                Intent i = new Intent(getApplicationContext(), BoardViewActivity.class);
                i.putExtra("STAT", STAT);
                i.putExtra("BTITLE", mData.mTitle);
                i.putExtra("BNAME", mData.mName);
                i.putExtra("BTEXT", mData.mGle);
                i.putExtra("BDATE", mData.mDate);
                startActivityForResult(i, 2000);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {         //게시판 작성 액티비티에서 받아온 리턴값으로 리스트뷰 항목 추가
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                INDAT ind = new INDAT();
                mAdapter.clear();
                try {
                    Log.d("디버그", "1단계");
                    String res = ind.execute(server + "/load_BOARD-List.php", ID, LECTURE, null, null, null).get();            //결과를 res에 받음
                    Log.d("디버그", "2단계");
                    BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
                    Log.d("디버그", "3단계");
                    String line;

                    int i = 0;
                    while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                        Log.d("디버그", line + " 불러옴");
                        switch (i) {
                            case 0:
                                bid = line;
                                i++;
                                break;
                            case 1:
                                btitle = line;
                                i++;
                                break;
                            case 2:
                                btext = line;
                                i++;
                                break;
                            case 3:
                                bdate = line;
                                mAdapter.addItem(btitle, bid, bdate, btext);

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
            }
        } else if (requestCode == 2000) {

            if (resultCode == RESULT_OK) {

                INDAT ind = new INDAT();
                mAdapter.clear();
                try {
                    Log.d("디버그", "1단계");
                    String res = ind.execute(server + "/load_BOARD-List.php", ID, LECTURE, null, null, null).get();            //결과를 res에 받음
                    Log.d("디버그", "2단계");
                    BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
                    Log.d("디버그", "3단계");
                    String line;

                    int i = 0;
                    while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                        Log.d("디버그", line + " 불러옴");
                        switch (i) {
                            case 0:
                                bid = line;
                                i++;
                                break;
                            case 1:
                                btitle = line;
                                i++;
                                break;
                            case 2:
                                btext = line;
                                i++;
                                break;
                            case 3:
                                bdate = line;
                                mAdapter.addItem(btitle, bid, bdate, btext);

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
            }
        }
    }
}