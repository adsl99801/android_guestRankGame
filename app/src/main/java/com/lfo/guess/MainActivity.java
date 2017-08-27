package com.lfo.guess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bone on 2014/4/24.
 */
public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.butStart)
    Button butStart;
    @BindView(R.id.textView2)
    TextView textView2;
    private int round = 10;
    private Context mContext;
    private Players players;
    private String[] spinnerlist = {"5局", "10局", "20局", "30局"};//setspinner
    private ArrayAdapter<String> listAdapter;//setspinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleHandler.Companion.getInstance().init(this);
        String tag=this.getClass().getName().toString();
        GoogleHandler.Companion.getInstance().sendAnalytics(tag,tag,"Activity");


        setContentView(R.layout.startpage);
        ButterKnife.bind(this);
        mContext = this;

        players = players.getInstance();


        setListener();//setListener()->  setspinner();
        setspinner();


    }


    private void setListener() {
        final MainActivity activity = this;
        butStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("round", round);
                intent.putExtras(bundle);
                startActivity(intent);
            }


        });

        spinner = (Spinner) findViewById(R.id.spinner);
    }


    private void setspinner() {

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerlist);
        spinner.setAdapter(listAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(mContext, "你選的是" + spinnerlist[position], Toast.LENGTH_SHORT).show();
                switch (position) {

                    case 0:
                        round = 5;
                        break;
                    case 1:
                        round = 10;
                        break;
                    case 2:
                        round = 20;
                        break;
                    case 3:
                        round = 30;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

}


class Player {
    int point = 100;//為了使用bundle傳遞所以要 implements Serializable
    int wincount = 0;
    int lostcount = 0;
    int condition = 0;//一段連勝或是一段連敗
    int lastcondition = 0;//上一場win=1 ;ost=0
    String name = "無名英雄";
    String strpoint = "0";

    public int getpoint() {
        return this.point;
    }

    public String getstrpoint() {
        return String.valueOf(this.point);
    }

    public String getname() {
        return this.name;
    }


}

class Players {

    private static Players _instance;//存放實體
    ArrayList<HashMap<String, String>> lisviewArrayList = new ArrayList<HashMap<String, String>>();

    private Players() {

    }//創建實體

    public static Players getInstance() {//取得實體
        if (_instance == null) {//假如沒有實體 就創建他  否則用舊得
            _instance = new Players();
        }
        return _instance;
    }

    public void addplayer(String name, int point) {
        Player player = new Player();
        player.name = name;

        player.strpoint = String.valueOf(point);
        HashMap map = new HashMap();
        map.put("name", name);

        map.put("point", player.strpoint);
        lisviewArrayList.add(map);
        orderthisArrayList();

    }


    public void orderthisArrayList() {
        Collections.sort(lisviewArrayList, new MapComparator("point"));
    }


}

class MapComparator implements Comparator {
    private String keyName;

    public MapComparator(String keyName) {
        this.keyName = keyName;
    }

    public int compare(Object o1, Object o2) {
        Map m1 = (Map) o1;
        Map m2 = (Map) o2;
        Comparable value1 = (Comparable) m2.get(keyName);
        return value1.compareTo(m1.get(keyName));
    }
}
