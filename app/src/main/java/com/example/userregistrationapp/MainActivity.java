package com.example.userregistrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (ImageView)findViewById(R.id.buttonArrow);
        mButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v== mButton){
            Intent intent = new Intent(MainActivity.this,loginActivity.class);
            startActivity(intent);
        }
    }
}
