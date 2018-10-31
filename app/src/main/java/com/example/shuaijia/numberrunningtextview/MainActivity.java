package com.example.shuaijia.numberrunningtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shuaijia.numberrunningtextview.numberrunningtextview.NumberRunningTextView;

public class MainActivity extends AppCompatActivity {
    NumberRunningTextView run_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        run_tv = findViewById(R.id.run_tv);

        run_tv.setNum("139441232");
    }
}
