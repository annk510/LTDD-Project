package com.example.vongship_android.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vongship_android.Adapter.CategoriesAdapter;
import com.example.vongship_android.Adapter.StoresAdapter;
import com.example.vongship_android.DTO.Categories;
import com.example.vongship_android.DTO.Store;
import com.example.vongship_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryDetailsActivity extends AppCompatActivity {
    RecyclerView stores;
    public ArrayList<Store> storeArrayList;
    StoresAdapter storesAdapter;

    void loadStoresRecyclerView(LinearLayoutManager layoutManager) {
        stores = findViewById(R.id.listStore);
        stores.setHasFixedSize(true);
        stores.setLayoutManager(layoutManager);
        final TextView txt1=  findViewById(R.id.txt_location_category);
        final Categories categories = (Categories) getIntent().getSerializableExtra("category");
        TextView categoryname = findViewById(R.id.categoryname);
        categoryname.setText(categories.getCategoryName());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Stores").whereEqualTo("categoryid".trim(),categories.getCategoryId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if(task.isSuccessful()){
                         storeArrayList = new ArrayList<>();
                         for (QueryDocumentSnapshot document : task.getResult()) {
                             Store store = new Store();
                             store.setStoreId(document.getId());
                             store.setStoreName(document.getString("storename"));
                             store.setDistance(document.getString("distance"));
                             store.setImage(document.getString("image"));
                             store.setSale(document.getString("sale"));
                             storeArrayList.add(store);
                         }
                         storesAdapter = new StoresAdapter(storeArrayList,CategoryDetailsActivity.this,LinearLayoutManager.VERTICAL);
                         stores.setAdapter(storesAdapter);
                     }
                 }
             }
        );


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        loadStoresRecyclerView(layoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCategory);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.abc);    //logo muốn hiện thị trên action bar
        actionBar.setDisplayUseLogoEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);//của nút quay lại trên toolbar, có cái func ở dưới nữa.

        actionBar.setTitle("");
        TextView location = findViewById(R.id.txt_location_category);
        location.setText(VT());
    }
    public String VT() {
        LocationManager locationManager = (LocationManager) CategoryDetailsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(CategoryDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CategoryDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        Criteria criteria = new Criteria();

        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        double latitude = 0;
        double longitude = 0;
        if(lastLocation!= null){
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        }
        Geocoder geoCoder = new Geocoder(CategoryDetailsActivity.this, Locale.getDefault());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address=null;
        if(matches != null){
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            if(bestMatch != null){
                address= bestMatch.getAddressLine(0);
            }

        }

        return address;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    LocationListener locationListener= new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double longitude= location.getLongitude();
            double lat= location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };

}