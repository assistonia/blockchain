package com.example.javatest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ID_Card extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);

        // 현재 시간
        LocalTime now = LocalTime.now();

        // 현재시간 출력
        System.out.println(now);  // 06:20:57.008731300

        // 포맷 정의하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");

        // 포맷 적용하기
        String formatedNow = now.format(formatter);

        Button go_main = findViewById(R.id.go_main_ID);
        go_main.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(ID_Card.this, MainActivity.class);
              startActivity(intent);

              finish();
        }
        }
        );


        Button Dron1 = findViewById(R.id.dron1);

        Dron1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),formatedNow+" 드론 배송 시작!", Toast.LENGTH_SHORT).show();
        }
        });


        Button Dron2 = findViewById(R.id.dron2);

        Dron2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"배송불가 드론입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button Dron3 = findViewById(R.id.dron3);

        Dron3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"배송불가 드론입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button Dron4 = findViewById(R.id.dron4);

        Dron4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),formatedNow+" 드론 배송 시작!", Toast.LENGTH_SHORT).show();
            }
        });
    }








    private void Drone_online(){
        // 소리
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ringtone.play();
        // 토스트 (Toast)
        Handler handler = new Handler(Looper.getMainLooper());
        new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast t = Toast.makeText(getApplicationContext(), "배송 시작" , Toast.LENGTH_SHORT);
                        t.show();
                    }
                };
            }
        }





