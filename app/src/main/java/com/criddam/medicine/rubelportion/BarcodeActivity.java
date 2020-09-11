package com.criddam.medicine.rubelportion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.criddam.medicine.R;
import com.criddam.medicine.medicineEntry.fragments.MedicineEntryTwoFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeActivity extends AppCompatActivity {
    String value = null;
    TextView tv_show;
    AlertDialog.Builder aldialog;
    Mainapi apicall;
    Bundle bundle;
    String qrvalu = null;
    private static final String TAG = "BarcodeActivity";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Object MedicineEntryTwoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        aldialog= new AlertDialog.Builder(this);
        bundle= new Bundle();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        qrscanner();




    }

    private void uploadqrdata() {
        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl("http://sales.criddam.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apicall= retrofit.create(Mainapi.class);
        Call<ResponseBody> call= apicall.sendQrdata(value,126);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(BarcodeActivity.this, "error response code "+response.code(), Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(BarcodeActivity.this, "probaly ok "+ response.code(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: ......................details response "+ response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        /*call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(BarcodeActivity.this, "something wrong "+ response.code(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(BarcodeActivity.this, "response code"+ response.code(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: ........................ "+ response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: .................. reason of error "+ t.getMessage());

            }
        });*/
    }

    private void qrscanner() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);

        intentIntegrator.initiateScan();

        tv_show=findViewById(R.id.tv_show);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("never here");
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result

            value= scanResult.getContents();
         //   tv_show.setText(value);
            //Toast.makeText(this, scanResult.getContents().toString(), Toast.LENGTH_SHORT).show();


            aldialog.setMessage(value);
            aldialog.setPositiveButton(getString(R.string.qrconsent), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //uploadqrdata();
                    editor.putString("qrkeyfinal",value);
                    editor.commit();

                    //Toast.makeText(BarcodeActivity.this, "the value is "+ value, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });

            aldialog.setNegativeButton(getString(R.string.qrconsentno), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    qrscanner();
                }
            });

            AlertDialog alrt = aldialog.create();
            alrt.setTitle("drug scan");
            alrt.show();


        }
// else continue with any other code you need in the method
    }
}