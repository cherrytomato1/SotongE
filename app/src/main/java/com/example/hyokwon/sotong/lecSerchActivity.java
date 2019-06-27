package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.hyokwon.sotong.URLS.server;

public class lecSerchActivity extends Activity {


    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    ImageButton back;
    TextView name_num;
    String NAME, ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letsrch);

        Log.d("디버그","서치넘어오기 성공");
        editSearch = (EditText) findViewById(R.id.etSer);
        listView = (ListView) findViewById(R.id.lectList);
        back = (ImageButton) findViewById(R.id.srch_backbtn);
        name_num = (TextView) findViewById(R.id.namenum) ;
        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        name_num.setText(NAME + "(" + ID + ")");
        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList(list);

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("디버그","온아이템 리스너 발생");
                String result = (String)adapterView.getAdapter().getItem(i);

                INDAT ind2 = new INDAT();
                Intent intn = getIntent();
                intn.putExtra("result",result);
                try {
                    Log.d("디버그", "addsub-lece1단계");
                    ind2.execute(server + "/newSub.php", ID, null, result, null, null);
                    Log.d("디버그", "addsub-lec2단계");

                } catch (Exception e) {
                    Log.e("디버그", "ERROR addsub-lec", e);
                }
                setResult(RESULT_OK,intn);
                finish();
            }
        });

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(List<String> list) {
        INDAT ind = new INDAT();
        try {
            Log. d("디버그","1단계");
            String res= ind.execute(server+"/load_subList.php",null,null,null,null, null).get();
            Log. d("디버그","2단계");
            BufferedReader reader = new BufferedReader(new StringReader(res));
            Log. d("디버그","3단계");
            String line;
            while((line = reader.readLine())!= null) {

                list.add(line);
            }
            Log. d("디버그","4단계");
        }catch (Exception e){
            Log.e("디버그","ERROR setlist",e);
        }
        INDAT ind2 = new INDAT();
        try {
            Log. d("디버그","1단계 del");
            String res= ind2.execute(server+"/load_user-subList.php",ID,null,null,null, null).get();
            Log. d("디버그","2단계 del");
            BufferedReader reader = new BufferedReader(new StringReader(res));
            Log. d("디버그","3단계 del");
            String line;
            while((line = reader.readLine())!= null) {

                list.remove(line);
            }
            Log. d("디버그","4단계 del");
        }catch (Exception e){
            Log.e("디버그","ERROR set-del",e);
        }
    }
}