package com.bistu.secondexperiment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private MyDatabaseHelper_user dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //创建dphelper来管理数据库
        dbHelper = new MyDatabaseHelper_user(this, "note.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EditText ed = findViewById(R.id.ed_3);

        // 查询Book表中所有的数据
        Cursor cursor = db.rawQuery("select * from note",null);

        //移至第一行数据
        if (cursor.moveToFirst())
        {
            //定义链表来存储日期信息
            List<String> data = new ArrayList<String>();
            do{
                data.add(cursor.getString(cursor.getColumnIndex("date")));
            }while(cursor.moveToNext());

            //定义适配器，用以显示
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    data);
            ListView listview = (ListView)findViewById(R.id.list_view);
            listview.setAdapter(adapter);

            //点击数据行，则跳转到查看页面，并且将日期信息通过intent传送到下一个界面
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListActivity.this,WriteActivity.class);
                    //获取日期
                    String a = data.get(position);
                    intent.putExtra("date",a);
                    startActivity(intent);
                }
            });
            }

        //如果数据库中没有数据，则跳转到输入界面
        else{
            Toast.makeText(this, "您还没有创建日记，为您自动跳转至写入界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListActivity.this,WriteActivity.class);
            startActivity(intent);
        }
        cursor.close();
    }
    public void insert(View view) {
        Intent intent = new Intent(ListActivity.this,WriteActivity.class);
        startActivity(intent);
    }
}