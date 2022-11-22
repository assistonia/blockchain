package com.example.javatest1;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static NaverMap naverMap;  // 네이버 지도 객체 생성
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;   // FusedLocationSource 권한 요청 코드
    private FusedLocationSource locationSource; // FusedLocationSource 객체 생성
    // 위치 정보 액세스 권한 요청
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,   // GPS와 네트워크를 이용하여 단말기 위치 식별
            Manifest.permission.ACCESS_COARSE_LOCATION // 네트워크를 이용하여 단말기 위치 식별
    };


    // Map 상태
    boolean map_status = true;

    // 현재 날짜 및 시간
    long getStartTime;
    String startTime;
    String login_id;
    String user_data;


    // *********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // 네이버 지도 불러움
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_navermap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_navermap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        // 사용자 gps 권한 설정
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


//        new Thread(new Runnable() {
//        @Override
//        public void run() {
//        runOnUiThread(new Runnable() {
//        @Override
//        public void run() {
//            Space("SPACE", 37.550541, 127.075484);
//        }
//        });
//        }
//        }).start();


        // 운행 종료 버튼
        Button btn_map_end = findViewById(R.id.btn_map_end);
        btn_map_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); // 현재 위치 표시
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.addOnLocationChangeListener(location ->
                Log.d("MapActivity", location.getLatitude() + ", " + location.getLongitude()));  // 현재 위치
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {    // 권한 허용
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);    // 현재 위치 뜸
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
//    @Override
//    public void onBackPressed(){
//        Intent intent =new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }


    //  초기 설정 ***********************************************************************************

//    // GPS 데이터를 이용하여 맵에 경로 나타내는 함수
//    private void showKickBoardPath(String ID, double latitude, double longitude) {
//        Log.d("MapActivity", "ID: " + ID + " Latitude: " + latitude + " Longitude: " + longitude);  // 현재 킥보드 위치
//        if (ID.equals(gps_kickID)) {
//            // 경로 추가
//            coords.add(new LatLng(latitude, longitude));
//            path.setCoords(coords);
//            // 맵에 경로 표시
//            path.setColor(Color.parseColor("#FFD53B"));
//            path.setOutlineColor(Color.BLACK);
//            path.setMap(naverMap);  // 지도에 경로 업데이트
//            Log.d("MapActivity", "GPS Tracking ...");
//        }
//    }


//private static void Space(String ID, double latitude, double longitude) {
//    Log.d("Check", "Latitude: " + latitude + " Longitude: " + longitude);  // 주차장 위치
//    // 주차장 마커
//    if(ID.equals("SPACE")){
//        Marker ParkingSpaceMarker = new Marker();
//        OverlayImage image = OverlayImage.fromResource(R.drawable.intro_icon);
//        ParkingSpaceMarker.setPosition(new LatLng(latitude, longitude));  // 새로운 주차장 위치 설정
//        ParkingSpaceMarker.setIcon(image);    // 주차장 마커 이미지
//        ParkingSpaceMarker.setWidth(80);
//        ParkingSpaceMarker.setHeight(80);
//        ParkingSpaceMarker.setMap(naverMap);  // 지도에 마커 띄움
//        Log.d("Check", "Space Marker: " + latitude + " " + longitude);
//    }
//    }
//
//}
