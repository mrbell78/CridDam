package com.criddam.medicine.rubelportion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.criddam.medicine.R;

public class NoteeditorActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText edt_note;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    int counter =0;

    Notedb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteeditor);
        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("your Note");
        edt_note=findViewById(R.id.note_edit);
        db= new Notedb(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.save:
                savenote();
                return true;
            default:
                return false;
        }
    }

    private void savenote() {
        String notedata = edt_note.getText().toString();
    }
}