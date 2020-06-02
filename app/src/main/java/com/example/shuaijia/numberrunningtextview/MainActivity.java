package com.example.shuaijia.numberrunningtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shuaijia.numberrunningtextview.numberrunningtextview.NumberRunningTextView;

public class MainActivity extends AppCompatActivity {
     NumberRunningTextView run_tv;
    int s = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        run_tv = findViewById(R.id.run_tv);

        run_tv.setNum(s + "");

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = s + 1;
                run_tv.setContent(s + "");
            }
        });
    }
}
