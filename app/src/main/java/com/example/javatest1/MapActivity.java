package com.example.javatest1;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.Arrays;

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

    Double drone_latitude;
    Double drone_longitude;


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

        Button go_main_map = findViewById(R.id.btn_map_end);
        go_main_map.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                           startActivity(intent);

                                           finish();
                                       }
                                   }
        );




    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        //시작카메라 위치지정 함수
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(37.551236, 127.074184),  // 위치 지정
                15                           // 줌 레벨
        );
        
        naverMap.setCameraPosition(cameraPosition);
        naverMap.setLocationSource(locationSource); // 현재 위치 표시
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.addOnLocationChangeListener(location ->
                Log.d("MapActivity", location.getLatitude() + ", " + location.getLongitude())

        );  // 현재 위치


    drone_latitude=37.552158;
    drone_longitude=127.073335;



//        마커 위치 찍어주기 테스트용임 (경도, 위도 ,넣고싶은 글자)
        showstartSpace(drone_latitude, drone_longitude, "출발지점");

        showfinishSpace(37.549687 ,127.075113, "도착지점");


        make_path();

    }

    //이동경로
    private static void make_path() {

        PathOverlay path = new PathOverlay();
        path.setCoords(Arrays.asList(
                new LatLng(37.552158, 127.073335),
                new LatLng(37.549687 ,127.075113)
        ));
        path.setMap(naverMap);
    }



    private static void showstartSpace( double latitude, double longitude,String Inserttext) {

        Marker SpaceMarker = new Marker();
        OverlayImage image = OverlayImage.fromResource(R.drawable.ic_baseline_place_24_darkred);
        SpaceMarker.setPosition(new LatLng(latitude, longitude));
        SpaceMarker.setIcon(image);
        SpaceMarker.setWidth(80);
        SpaceMarker.setHeight(80);
        SpaceMarker.setMap(naverMap);  // 지도에 마커 띄움
        SpaceMarker.setCaptionText(Inserttext);
        Log.d("ParkingCheck", "New Parking Space Marker: " + latitude + " " + longitude);
    }

    private static void showfinishSpace( double latitude, double longitude,String Inserttext) {

        Marker SpaceMarker = new Marker();
        OverlayImage image = OverlayImage.fromResource(R.drawable.ic_baseline_place_24_blue);
        SpaceMarker.setPosition(new LatLng(latitude, longitude));
        SpaceMarker.setIcon(image);
        SpaceMarker.setWidth(80);
        SpaceMarker.setHeight(80);
        SpaceMarker.setMap(naverMap);  // 지도에 마커 띄움
        SpaceMarker.setCaptionText(Inserttext);
        Log.d("ParkingCheck", "New Parking Space Marker: " + latitude + " " + longitude);
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



//    private void setMark(Marker marker, double lat, double lng, int resourceID)
//    {
//        //원근감 표시
//        marker.setIconPerspectiveEnabled(true);
//        //아이콘 지정
//        marker.setIcon(OverlayImage.fromResource(resourceID));
//        //마커의 투명도
//        marker.setAlpha(0.8f);
//        //마커 위치
//        marker.setPosition(new LatLng(lat, lng));
//        //마커 표시
//        marker.setMap(naverMap);
//    }

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
//        Log.d("MapActivity", "ID: " + ID + " Latitude: " + latitude + " Longitude: " + longitude);  // 현재 위치
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
//    Log.d("Check", "Latitude: " + latitude + " Longitude: " + longitude);  //  위치
//    //  마커
//    if(ID.equals("SPACE")){
//        Marker ParkingSpaceMarker = new Marker();
//        OverlayImage image = OverlayImage.fromResource(R.drawable.intro_icon);
//        ParkingSpaceMarker.setPosition(new LatLng(latitude, longitude));  // 새로운  위치 설정
//        ParkingSpaceMarker.setIcon(image);    // 주차장 마커 이미지
//        ParkingSpaceMarker.setWidth(80);
//        ParkingSpaceMarker.setHeight(80);
//        ParkingSpaceMarker.setMap(naverMap);  // 지도에 마커 띄움
//        Log.d("Check", "Space Marker: " + latitude + " " + longitude);
//    }
//    }
//
//}

