package com.humeyramercan.mybestlandscapephotos.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.humeyramercan.mybestlandscapephotos.R;
import com.humeyramercan.mybestlandscapephotos.adapter.CustomAdapter;
import com.humeyramercan.mybestlandscapephotos.modal.Landscape;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        SQLiteDatabase database;
        ListView listView;
        ArrayList <Landscape> landscapeList;
        CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        landscapeList=new ArrayList<>();
        listView=findViewById(R.id.listView);

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("info","old");
                intent.putExtra("landscape",landscapeList.get(i));
                startActivity(intent);
            }
        });
    }

    public void getData(){
        customAdapter=new CustomAdapter(this,landscapeList);
        try {
            database=openOrCreateDatabase("Landscapes",MODE_PRIVATE,null);
            Cursor cursor=database.rawQuery("SELECT * FROM landscapes",null);
            int imageIndex = cursor.getColumnIndex("image");
            int placeIndex = cursor.getColumnIndex("place");
            int dateIndex = cursor.getColumnIndex("date");
            int noteIndex = cursor.getColumnIndex("note");
            while (cursor.moveToNext()){
                String place=cursor.getString(placeIndex);
                String date=cursor.getString(dateIndex);
                String note = cursor.getString(noteIndex);
                byte[] bytes=cursor.getBlob(imageIndex);
                Landscape landscape=new Landscape(date,note,place,bytes);
                landscapeList.add(landscape);
            }
            customAdapter.notifyDataSetChanged();
            cursor.close();
        }catch (Exception e){
            e.printStackTrace(); }
        listView.setAdapter(customAdapter);
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