package com.sup3rd3v3l0p3r.teamvetor.minyongsbobzip;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    FragmentManager fragmentManager;
    Context context;
    double lon, lat;
    GpsInfo gpsInfo;
    MarkerOptions markerOptions;
    GoogleMap googleMap;
    TextView textView;

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
        markerOptions = new MarkerOptions();
        textView = (TextView) findViewById(R.id.asdf);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        gpsInfo.getLocation();
        lat = gpsInfo.lat;
        lon = gpsInfo.lon;

        LatLng NowLocation = new LatLng(lat, lon);
        Log.i("TAG", lat + "  " + lon);
        markerOptions.position(NowLocation);
        markerOptions.title("내위치");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(NowLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    public void refresh(View v) {
        googleMap.clear();

        gpsInfo.getLocation();
        lat = gpsInfo.lat;
        lon = gpsInfo.lon;

        LatLng NowLocation = new LatLng(lat, lon);
        Log.i("TAG", lat + "  " + lon);
        markerOptions.position(NowLocation);
        markerOptions.title("내위치");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(NowLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        final Call<List<Repo>> repos = (Call<List<Repo>>) service.listRepos(lat+","+lon,"치킨",1000,"AIzaSyAE78Um1ZuE9quVniPZSiLEz0Cw9dOPGuk");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> user = response.body();
                for (int i = 0; i < user.size(); i++)
                    Log.i("TAG", "" + user.get(i).getVicinity());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable throwable) {
                Log.i("TAG", "실패함 : "+throwable);
            }
        });
    }

    public interface GitHubService {
        @GET("maps/api/place/nearbysearch/json")
        Call<List<Repo>> listRepos(@Query("location") String location,@Query("keyword")String keyword,@Query("radius")int radius,@Query("key")String key);
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplication(), "ACCESS_FINE_LOCATION 권한 허가 ㄳ", Toast.LENGTH_SHORT).show();
                } else {//권한 거부
                    Toast.makeText(getApplication(), "ACCESS_FINE_LOCATION 권한 거부 ㄴㄴ", Toast.LENGTH_SHORT).show();
                }
                return;

            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplication(), "ACCESS_COARSE_LOCATION 권한 허가 ㄳ", Toast.LENGTH_SHORT).show();
                } else {//권한 거부
                    Toast.makeText(getApplication(), "ACCESS_COARSE_LOCATION 권한 거부 ㄴㄴ", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
