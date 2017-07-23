package com.sup3rd3v3l0p3r.teamvetor.minyongsbobzip;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    FragmentManager fragmentManager;
    Context context;
    double lon,lat;
    GpsInfo gpsInfo;
    MarkerOptions markerOptions;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationPermissionChecker();

        fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context = getApplicationContext();
        gpsInfo = new GpsInfo(context);
        markerOptions  = new MarkerOptions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }

    public void refresh(View v) {
        gpsInfo.getLocation();
        lat = gpsInfo.lat;
        lon = gpsInfo.lon;
        Toast.makeText(getApplicationContext(),""+lat+"  "+lon,Toast.LENGTH_SHORT).show();

        LatLng SEOUL = new LatLng(lat,lon);
        markerOptions.position(SEOUL);
        markerOptions.title("내위치");
        markerOptions.snippet("내위치 일 수도?");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(30));

    }

    public void LocationPermissionChecker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)//권한 비허용
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)//권한 비허용
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplication(),"ACCESS_FINE_LOCATION 권한 허가 ㄳ",Toast.LENGTH_SHORT).show();
                } else {//권한 거부
                    Toast.makeText(getApplication(),"ACCESS_FINE_LOCATION 권한 거부 ㄴㄴ",Toast.LENGTH_SHORT).show();
                }
                return;

            case 2:
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplication(),"ACCESS_COARSE_LOCATION 권한 허가 ㄳ",Toast.LENGTH_SHORT).show();
                } else {//권한 거부
                    Toast.makeText(getApplication(),"ACCESS_COARSE_LOCATION 권한 거부 ㄴㄴ",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}
