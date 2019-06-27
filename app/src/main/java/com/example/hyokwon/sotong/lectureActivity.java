package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.hyokwon.sotong.URLS.server;

public class lectureActivity extends Activity {
    String ID, NAME;
    TextView name_num;
    ImageButton btnEXIT;
    Button open;
    Intent itnt;

    private ListView lectList = null;
    private ListViewAdapter mAdapter = null;

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void addItem(Drawable mIcon, String mLect, String mName) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mLect;
            addInfo.mName = mName;
            addInfo.mIcon = mIcon;
            mListData.add(addInfo);
        }


        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }
        public void clear()
        {
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
            public TextView mLect;
            public TextView mName;
            public ImageView mIcon;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lect1_item, null);

                holder.mLect = (TextView) convertView.findViewById(R.id.mLect);
                holder.mName = (TextView) convertView.findViewById(R.id.mName);
                holder.mIcon = (ImageView) convertView.findViewById(R.id.mIcon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);
            if (mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            } else {
                holder.mIcon.setVisibility(View.GONE);
            }
            holder.mName.setText(mData.mName);
            holder.mLect.setText(mData.mTitle);
            return convertView;
        }
    }

    public void btnAddList(View v) {
        itnt = new Intent(getApplicationContext(), lecSerchActivity.class);
        itnt.putExtra("NAME", NAME);
        itnt.putExtra("ID", ID);
        startActivityForResult(itnt, 3000);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /*INDAT ind = new INDAT();        //나중에 합치자
            String res = data.getStringExtra("result");
            try {
                Log.d("디버그", "addsub-lece1단계");
                ind.execute(server + "/newSub.php", ID, null, res, null, null);
                Log.d("디버그", "addsub-lec2단계");

            } catch (Exception e) {
                Log.e("디버그", "ERROR addsub-lec", e);
            }
            mAdapter.addItem(getResources().getDrawable(R.drawable.book), res, "교수이름");
            mAdapter.notifyDataSetChanged();

            */
            setLect();
        }


    }


    public void setLect() {
        INDAT ind = new INDAT();
        INDAT ind2[] = new INDAT[15];
        try {
            Log.d("디버그", "1단계");
            String res = ind.execute(server + "/load_user-subList.php", ID, null, null, null, null).get();
            Log.d("디버그", "2단계");
            BufferedReader reader = new BufferedReader(new StringReader(res));
            Log.d("디버그", "3단계");
            String line, pfname;
            int i = 0;
            mAdapter.clear();
            while ((line = reader.readLine()) != null) {
                ind2[i] = new INDAT();
                pfname = ind2[i++].execute(server + "/getPfsrNAME.php", line, null, null, null, null).get();
                pfname = pfname.replaceAll(System.getProperty("line.separator"), "");
                mAdapter.addItem(getResources().getDrawable(R.drawable.book), line, pfname + " 교수님");
            }
            Log.d("디버그", "4단계");
        } catch (Exception e) {
            Log.e("디버그", "ERROR setlist", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        btnEXIT = (ImageButton) findViewById(R.id.lect_backbtn);
        name_num = (TextView) findViewById(R.id.namenum);
        open = (Button) findViewById(R.id.btn);
        lectList = (ListView) findViewById(R.id.lectlist_view);
        mAdapter = new ListViewAdapter(this);
        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        name_num.setText(NAME + "(" + ID + ")");
        lectList.setAdapter(mAdapter);
        setLect();
        setResult(RESULT_OK, intent);


        btnEXIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position1, long l) {
                final ListData mData = mAdapter.mListData.get(position1);
                AlertDialog.Builder dialog = new AlertDialog.Builder(lectureActivity.this);
                dialog.setTitle("삭제");
                dialog.setMessage("삭제하시겠습니까?");
                dialog.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                INDAT ind = new INDAT();        //나중에 합치자
                                Log.d("디버그", "겟네임2");
                                String subName;

                                try {

                                    Log.d("디버그", "delsub 1단계- :" + mAdapter.getName(position1));
                                    ind.execute(server + "/delSub.php", ID, null, mAdapter.getName(position1), null, null); //어댑터로부터 이름을 받아서 보내기
                                    Log.d("디버그", "delsub-lec2단계");

                                } catch (Exception e) {
                                    Log.e("디버그", "ERROR del-lec", e);
                                }
                                mAdapter.remove(position1);
                            }
                        });
                dialog.setNegativeButton("아니요",null);
                dialog.show();
                return false;

            }
        });
    }
}