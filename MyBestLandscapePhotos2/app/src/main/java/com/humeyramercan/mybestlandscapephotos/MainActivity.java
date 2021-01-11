package com.humeyramercan.mybestlandscapephotos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        SQLiteDatabase database;
        ListView listView;
        ArrayList <String> placeArray;
        ArrayList<Integer> idArray;
        ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idArray=new ArrayList<>();
        placeArray=new ArrayList<>();

        listView=findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,placeArray);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("info","old");
                intent.putExtra("id",idArray.get(i));
                startActivity(intent);
            }
        });

        getData();
    }


    public void getData(){
        try {
            database=openOrCreateDatabase("Landscapes",MODE_PRIVATE,null);
            Cursor cursor=database.rawQuery("SELECT * FROM landscapes",null);
            int placeIndex=cursor.getColumnIndex("place");
            int idIndex=cursor.getColumnIndex("id");
            while (cursor.moveToNext()){
                placeArray.add(cursor.getString(placeIndex));
                idArray.add(cursor.getInt(idIndex));
            }
            arrayAdapter.notifyDataSetChanged();
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.add_landscape,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_landscape_photo) {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}