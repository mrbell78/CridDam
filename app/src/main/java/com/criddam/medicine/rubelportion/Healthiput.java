package com.criddam.medicine.rubelportion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.criddam.medicine.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Healthiput extends AppCompatActivity {

    EditText systoic,diastolic,pulse,weight,temperutere;
    Button btn_submite;
    private static final String TAG = "Healthiput";
    Mainapi mainapi;

    Sqlitdata db;

    int systolicvalue=0,diastolicvalue=0,pulsevalue=0,weightvalue=0,tempareturevalue=0;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthiput);
        db = new Sqlitdata(this);

        systoic = findViewById(R.id.systolic);
        diastolic=findViewById(R.id.diastolic);
        pulse=findViewById(R.id.pulse);
        weight= findViewById(R.id.weight);
        temperutere=findViewById(R.id.temperature);

        btn_submite=findViewById(R.id.submit);
        mDialog=new ProgressDialog(this);
        mDialog.setMessage("uploading please wait.....");

        btn_submite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(systoic.getText().toString())&& !TextUtils.isEmpty(diastolic.getText().toString())&& !TextUtils.isEmpty(pulse.getText().toString())&&
                !TextUtils.isEmpty(weight.getText().toString()) && !TextUtils.isEmpty(temperutere.getText().toString())
                ){
                    mDialog.show();
                    try {
                        systolicvalue = Integer.parseInt(systoic.getText().toString());
                        diastolicvalue = Integer.parseInt(diastolic.getText().toString());
                        pulsevalue = Integer.parseInt(pulse.getText().toString());
                        tempareturevalue = Integer.parseInt(temperutere.getText().toString());
                        weightvalue = Integer.parseInt(weight.getText().toString());

                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        db.insertData(tempareturevalue,weightvalue,pulsevalue,systolicvalue,diastolicvalue,formattedDate);

                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }

                    Retrofit retrofit  = new Retrofit.Builder()
                            .baseUrl("http://sales.criddam.com/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    mainapi = retrofit.create(Mainapi.class);
                    Call<ResponseBody> call = mainapi.createPost(pulsevalue,tempareturevalue,systolicvalue,diastolicvalue,weightvalue,0);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(!response.isSuccessful()){
                                Toast.makeText(Healthiput.this, "something wrong", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: ..................something wrong "+ response.code());
                            }
                            Log.d(TAG, "onResponse: ...............response code "+ response.code());
                            Toast.makeText(Healthiput.this, "upload success full", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),HealthStatus.class));
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }else {
                    Toast.makeText(Healthiput.this, "Please fill up all the field ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}