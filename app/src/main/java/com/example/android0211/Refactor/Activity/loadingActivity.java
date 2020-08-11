package com.example.android0211.Refactor.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.android0211.R;

public class loadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        try{
            Thread.sleep(1500);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, loginActivity.class));
        finish();
    }
}
