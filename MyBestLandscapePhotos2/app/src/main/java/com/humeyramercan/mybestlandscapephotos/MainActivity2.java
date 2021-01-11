package com.humeyramercan.mybestlandscapephotos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.humeyramercan.mybestlandscapephotos.R;

import java.io.ByteArrayOutputStream;

public class MainActivity2 extends AppCompatActivity {
    Bitmap selectedImage;
    ImageView landscapeImageView;
    EditText dateEditText,placeEditText,noteMultilineText;
    Button saveButton;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        landscapeImageView=findViewById(R.id.landscapeImageView);
        dateEditText=findViewById(R.id.dateEditText);
        placeEditText=findViewById(R.id.placeEditText);
        noteMultilineText=findViewById(R.id.noteMultilineText);
        saveButton=findViewById(R.id.saveButton);
        Intent intent=getIntent();
        String info=intent.getStringExtra("info");

        if(info.matches("new")){
            dateEditText.setText("");
            placeEditText.setText("");
            noteMultilineText.setText("");
            saveButton.setVisibility(View.VISIBLE);
            Bitmap selectImage=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.selectimage);
            landscapeImageView.setImageBitmap(selectImage);
        }else{
            saveButton.setVisibility(View.INVISIBLE);

            int id = intent.getIntExtra("id", 0);
            landscapeImageView.setOnClickListener(null);
            try {
                database = openOrCreateDatabase("Landscapes", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("SELECT * FROM landscapes WHERE id=?", new String[]{String.valueOf(id)});
                int imageIndex = cursor.getColumnIndex("image");
                int placeIndex = cursor.getColumnIndex("place");
                int dateIndex = cursor.getColumnIndex("date");
                int noteIndex = cursor.getColumnIndex("note");

                while (cursor.moveToNext()) {
                    placeEditText.setText(cursor.getString(placeIndex));
                    dateEditText.setText(cursor.getString(dateIndex));
                    noteMultilineText.setText(cursor.getString(noteIndex));

                    byte[] bytes = cursor.getBlob(imageIndex);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    landscapeImageView.setImageBitmap(bitmap);
                }
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void selectedImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
         Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         startActivityForResult(intentToGallery,2);
        }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            Uri imageData=data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    landscapeImageView.setImageBitmap(selectedImage);

                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    landscapeImageView.setImageBitmap(selectedImage);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void save(View view){

        if(selectedImage!=null) {
            Bitmap smallImage = makeSmallerImage(selectedImage, 300);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            byte[] bytes = outputStream.toByteArray();
            String date = dateEditText.getText().toString();
            String place = placeEditText.getText().toString();
            String note = noteMultilineText.getText().toString();
            if(!date.matches("")&& !place.matches("") && !note.matches("")) {

                database = this.openOrCreateDatabase("Landscapes", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS landscapes (id INTEGER PRIMARY KEY,image BLOB,date VARCHAR,place VARCHAR,note VARCHAR) ");

                String sqlString = "INSERT INTO landscapes (image,date,place,note) VALUES (?,?,?,?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindBlob(1, bytes);
                sqLiteStatement.bindString(2, date);
                sqLiteStatement.bindString(3, place);
                sqLiteStatement.bindString(4, note);
                sqLiteStatement.execute();

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else{
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }else {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    public Bitmap makeSmallerImage(Bitmap image,int maximumSize){
        int width=image.getWidth();
        int height=image.getHeight();
        float bitmapRatio= (float) width/ (float) height;

        if(bitmapRatio>1){
            width=maximumSize;
            height=(int) (width/bitmapRatio);

        } else{
            height=maximumSize;
            width= (int)(maximumSize*bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
}