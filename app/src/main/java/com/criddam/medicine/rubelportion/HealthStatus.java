package com.criddam.medicine.rubelportion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.criddam.medicine.R;
import com.criddam.medicine.database.OfflineInformation;
import com.criddam.medicine.others.Keys;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HealthStatus extends AppCompatActivity {

    LineChart chart;
    FloatingActionButton fab;
    List<Helthdata> datalist;

    Sqlitdata db;
//    private  SharedPreferences  sharedPreferences =  getApplicationContext().getSharedPreferences(Keys.USER_INFO, Context.MODE_PRIVATE);
  //  SharedPreferences.Editor editor = sharedPreferences.edit();
    private static final String TAG = "HealthStatus";

    RecyclerView recyclerView;



    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthstatus);
        datalist=new ArrayList<>();
        db= new Sqlitdata(this);
        chart=findViewById(R.id.chart);
        chart.animateX(1000);
        chartdraw(40,60);
        fab=findViewById(R.id.favbtn);
        recyclerView=findViewById(R.id.recyclerviewhelth);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Healthiput.class));

            }
        });


        Customadapter customadapter = new Customadapter(HealthStatus.this,datalist);

        recyclerView.setAdapter(customadapter);
    }

    private void chartdraw(int count,int range){

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry>yVals2 = new ArrayList<>();
        ArrayList<Entry>yVals3 = new ArrayList<>();
        ArrayList<Entry>yVals4 = new ArrayList<>();
        ArrayList<Entry>yVals5 = new ArrayList<>();
        Cursor cursor = db.getdata();
        if(cursor.getCount()!=0){

            int i=0;
            while (cursor.moveToNext()){

                int temperature = cursor.getInt(1);
                yVals1.add(new Entry(i, temperature));

                int weight = cursor.getInt(2);
                yVals2.add(new Entry(i,weight));

                int pulse =cursor.getInt(3);
                yVals3.add(new Entry(i,pulse));

                int systolic =cursor.getInt(4);
                yVals4.add(new Entry(i,systolic));

                int diastolic = cursor.getInt(5);
                yVals5.add(new Entry(i,diastolic));

                String date = cursor.getString(6);

                Helthdata data = new Helthdata(temperature,weight,pulse,systolic,diastolic,date);
                datalist.add(data);


                i++;


            }
        }


        /*for(int i=0;i<count;i++){
            float val = 50;
            yVals1.add(new Entry(i, val));
        }*/


      /*  yVals1.add(new Entry(1, 50));
        yVals1.add(new Entry(2, 40));
        yVals1.add(new Entry(3, 120));
        yVals1.add(new Entry(4, 70));
        yVals1.add(new Entry(5, 30));
        yVals1.add(new Entry(6, 0));
        yVals1.add(new Entry(7, 90));*/

        LineDataSet set1;
        LineDataSet set2;
        LineDataSet set3;
        LineDataSet set4;
        LineDataSet set5;


        set1= new LineDataSet(yVals1,"Temparature");
        set2= new LineDataSet(yVals2,"weight");
        set3= new LineDataSet(yVals3,"pulse");
        set4= new LineDataSet(yVals4,"Systolic");
        set5= new LineDataSet(yVals5,"Diastolic");


        set2.setLineWidth(3);
        set2.setColor(Color.parseColor("#196fe0"));

        set3.setLineWidth(2);
        set3.setColor(Color.parseColor("#e04e19"));
        set1.setLineWidth(5);
        set1.setColor(Color.parseColor("#9932a8"));

        set4.setLineWidth(3);
        set4.setColor(Color.parseColor("#65ccdb"));

        set5.setLineWidth(4);
        set5.setColor(Color.parseColor("#edc945"));


        LineData data = new LineData(set1,set2,set3,set4,set5);
        chart.setData(data);


    }


}