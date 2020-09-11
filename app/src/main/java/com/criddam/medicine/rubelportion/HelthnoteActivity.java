package com.criddam.medicine.rubelportion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.criddam.medicine.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HelthnoteActivity extends AppCompatActivity {

    Notedb db;
    List<String> datalist;
    FloatingActionButton fab;
    SharedPreferences pref ;
    String[]data = new String[1000];
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helthnote);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        db= new Notedb(this);
        datalist= new ArrayList<>();
        fab=findViewById(R.id.fav_addnote);
        recyclerView=findViewById(R.id.helthnote_recylerview);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NoteeditorActivity.class));
            }
        });

        getnote();
    }

    private void getnote() {

        Cursor cursor = db.getdata();

      /*  if(cursor.getCount()!=0){
            int i=0;
            while (cursor.moveToNext()){
                datalist.add(cursor.getString(1));

                Toast.makeText(this, "data is "+ cursor.getString(1), Toast.LENGTH_SHORT).show();

            }
        }*/
/*

      int counter = pref.getInt("increment",0);
        Toast.makeText(this, "counter is "+counter, Toast.LENGTH_SHORT).show();

*/


        /*for(int i=0;i<counter;i++){
            data[i]=pref.getString("key"+counter,null);
            Toast.makeText(this, data[i], Toast.LENGTH_SHORT).show();
        }*/

      /*if(counter!=0){
          for(int i=1;i<=counter;i++){

              datalist.add(pref.getString("key"+i,null));
          Toast.makeText(this, datalist.get(i), Toast.LENGTH_SHORT).show();
          }
      }*/

    }
}