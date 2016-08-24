package com.example.reader20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class UserImfor extends AppCompatActivity {
    private ImageView user_Imfor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_imfor);
        user_Imfor= (ImageView) findViewById(R.id.user_Imfor);
    }

}
