package com.bistu.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        data.add("fly");
        data.add("Shot");
        data.add("月光");
        data.add("Been Through");
        data.add("夜的第七章");
        data.add("超市");


        //定义适配器，用以显示
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                data);
        ListView listview = (ListView)findViewById(R.id.list_view);
        listview.setAdapter(adapter);

        //点击歌曲，实现跳转播放
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this,Play.class);
                //获取歌曲名
                String a = data.get(position);
                intent.putExtra("name",a);
                intent.putExtra("index",String.valueOf(position));
                startActivity(intent);
            }
        });

    }



}