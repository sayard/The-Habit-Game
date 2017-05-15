package pl.c0.sayard.thehabitgame;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private LatLng position = null;
    private final int PERMISSIONS_REQUEST_CODE = 101;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                position = place.getLatLng();
                mapFragment.getMapAsync(MapActivity.this);
            }

            @Override
            public void onError(Status status) {
            }
        });

        Intent intent = getIntent();
        final int id = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_ID), -1);
        Cursor cursor = getGeoNotificationData(id);
        cursor.moveToFirst();

        final int isGeoNotificationActive = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_GEO_NOTIFICATION_ACTIVE));
        cursor.close();

        Button cancelNotificationButton = (Button) findViewById(R.id.cancel_geo_notification);
        Button setNotificationButton = (Button) findViewById(R.id.set_geo_notification);

        setNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position!=null){
                    double latitude = position.latitude;
                    double longitude = position.longitude;
                    writeToDb(id, latitude, longitude,1);
                    finish();
                }else{
                    Toast.makeText(MapActivity.this, "Please select location with a marker", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGeoNotificationActive == 1){
                    writeToDb(id, null, null, -1);
                    Toast.makeText(MapActivity.this, "Notification cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void writeToDb(int id, Double latitude, Double longitude, int notificationActive) {
        if(id == -1){
            Toast.makeText(this, "Failed to create notification. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_IS_GEO_NOTIFICATION_ACTIVE, notificationActive);
        if(latitude != null)
            contentValues.put(HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LATITUDE, latitude);
        if(longitude != null)
            contentValues.put(HabitContract.HabitEntry.COLUMN_GEON_NOTIFICATION_LONGITUDE, longitude);

        db.update(HabitContract.HabitEntry.TABLE_NAME,
                contentValues,
                HabitContract.HabitEntry._ID + " = " + id,
                null);
    }

    private Cursor getGeoNotificationData(int id) {
        if(id == -1){
            Toast.makeText(this, "Failed to read notification data. Try again later.", Toast.LENGTH_SHORT).show();
            return null;
        }
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                HabitContract.HabitEntry.COLUMN_IS_GEO_NOTIFICATION_ACTIVE
        };
        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                HabitContract.HabitEntry._ID + " = " + id,
                null,
                null,
                null,
                null);
        return cursor;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                position = latLng;
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(new MarkerOptions().position(position).draggable(true));
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                }, PERMISSIONS_REQUEST_CODE);
                return;
            }else{
                googleMap.setMyLocationEnabled(true);
            }
        }
        else{
            googleMap.setMyLocationEnabled(true);
        }
        if(position!=null){
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(position).draggable(true));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15), 2000, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mapFragment.getMapAsync(MapActivity.this);
                return;
        }
    }
}
