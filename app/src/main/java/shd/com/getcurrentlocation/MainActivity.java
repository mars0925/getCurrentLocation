package shd.com.getcurrentlocation;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;
    Button bt;
    TextView t_location;
    TextView t_lon;
    TextView t_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.b_getlocation);
        t_location = findViewById(R.id.t_location);
        t_lon = findViewById(R.id.t_lon);
        t_lat = findViewById(R.id.t_lat);

        client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lon = location.getLongitude();
                            double lat = location.getLatitude();
                            t_lat.setText(String.valueOf(lat));
                            t_lon.setText(String.valueOf(lon));
                            t_location.setText(location.toString());

                            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                            getCityName(myCoordinates);
                        }
                    }
                });
            }
        });
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},123);
    }

    /*座標轉地址*/
    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();

            Log.e("mylog", "Complete Address: " + addresses.toString());
            Log.e("mylog", "Address: " + address);
            Log.e("國家", "國家: " + addresses.get(0).getCountryCode());
            Log.e("縣市", "縣市: " + addresses.get(0).getAdminArea());
            Log.e("鄉鎮市區", "鄉鎮市區: " + addresses.get(0).getLocality());
            Log.e("地址", "地址: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }
}
