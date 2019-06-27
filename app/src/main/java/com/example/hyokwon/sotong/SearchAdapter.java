package com.example.hyokwon.sotong;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static com.example.hyokwon.sotong.URLS.server;

/**
 * Created by Administrator on 2017-08-07.
 */

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    String pname;

    public SearchAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.lect1_item, null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) convertView.findViewById(R.id.mLect);

            viewHolder.Name = (TextView) convertView.findViewById(R.id.mName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


/*        INDAT ind2 = new INDAT();
        try {
            Log.d("디버그", "1단계 del");
            String res = ind2.execute(server + "/getPfsrNAME.php", viewHolder.label.getText().toString(), null, null, null, null).get();
            Log.d("디버그", "2단계 del");
            BufferedReader reader = new BufferedReader(new StringReader(res));
            Log.d("디버그", "3단계 del");
            String line;
            while ((line = reader.readLine()) != null) {
                pname=line;
                //Toast.makeText(evalActivity.this, line, Toast.LENGTH_SHORT).show();
            }
            Log.d("디버그", "4단계 del");
        } catch (Exception e) {
            Log.e("디버그", "ERROR set-del", e);
        }
        */

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label.setText(list.get(position));
        viewHolder.Name.setText("");

        return convertView;
    }

    class ViewHolder {
        public TextView label;
        public TextView Name;
    }

}
