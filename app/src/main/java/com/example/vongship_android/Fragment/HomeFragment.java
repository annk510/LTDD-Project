package com.example.vongship_android.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.vongship_android.Activity.Cart;
import com.example.vongship_android.Activity.FoodDeliveryActivity;
import com.example.vongship_android.Activity.LoginActivity;
import com.example.vongship_android.Activity.MainActivity;
import com.example.vongship_android.Adapter.CategoriesAdapter;
import com.example.vongship_android.Adapter.BannerAdapter;
import com.example.vongship_android.Activity.MapsActivity;

import com.example.vongship_android.Adapter.ProductAdapter;
import com.example.vongship_android.Adapter.SectionsAdapter;
import com.example.vongship_android.Adapter.StoresAdapter;
import com.example.vongship_android.Class.DownloadImageTask;
import com.example.vongship_android.DTO.Categories;
import com.example.vongship_android.DTO.Product;
import com.example.vongship_android.DTO.Section;
import com.example.vongship_android.DTO.Store;
import com.example.vongship_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.vongship_android.R.id.location_click;
import static com.example.vongship_android.R.id.txt_date;

public class HomeFragment extends Fragment {
    LinearLayout location;
    CardView gotoFoodDelivery;
    ArrayList<Section> sectionArrayList;
    SectionsAdapter sectionsAdapter;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        location = root.findViewById(location_click);
        gotoFoodDelivery = root.findViewById(R.id.gotoFoodDelivery);
        ViewPager viewPager = root.findViewById(R.id.viewPager);
        BannerAdapter adapter = new BannerAdapter(getActivity());
        viewPager.setAdapter(adapter);
        envent();
        int permission_fine_loc = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_coarse_loc = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission_fine_loc != PackageManager.PERMISSION_GRANTED
                || permission_coarse_loc != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
        final TextView txtAdress =root.findViewById(R.id.txt_Address);
        final TextView txtDate =root.findViewById(R.id.txt_date);
        if(VT() != null){
            txtAdress.setText(VT());
        }else {
            txtAdress.setText("");
        }


        new DownloadImageTask((ImageView) root.findViewById(R.id.IMGprofile_home))
                .execute("https://firebasestorage.googleapis.com/v0/b/doanltdd-60a15.appspot.com/o/Image%2FprofileImage.jpg?alt=media&token=40d48a63-1ac3-4e2c-946d-4b8515f79c62");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d MMMM, yyyy");
        String currentDateandTime = sdf.format(new Date());
       txtDate.setText(currentDateandTime);

        RecyclerView recyclerView =root.findViewById(R.id.sectionsInHomeFragment);
        sectionsAdapter = loadSections();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sectionsAdapter);

        CardView cart = root.findViewById(R.id.cv_cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Cart.class);
                startActivity(intent);
            }
        });

        return root;
    }
    SectionsAdapter loadSections(){
        sectionArrayList = new ArrayList<>();
        //Section cửa hàng gần đây
        final Section section = new Section();
        section.setHeaderTitle("THỬ QUÁN MỚI");
        final ArrayList<Store> storeArrayList = new ArrayList<>();
        storeArrayList.add(new Store("yLd8mWjPtzORB3aHxDuD","Cà Phê Trung Nguyên","5.6 km","Sale 12%","https://firebasestorage.googleapis.com/v0/b/doanltdd-60a15.appspot.com/o/Image%2Fshopcf1.png?alt=media&token=94d36d69-e67c-473a-930b-8bfd63d6b6d2"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Stores").whereEqualTo("new",true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if(task.isSuccessful()){

                         for (QueryDocumentSnapshot document : task.getResult()) {
                             Store store = new Store();
                             store.setStoreId(document.getId());
                             store.setStoreName(document.getString("storename"));
                             store.setDistance(document.getString("distance"));
                             store.setImage(document.getString("image"));
                             store.setSale(document.getString("sale"));
                             storeArrayList.add(store);
                         }
                     }
                 }
             }
        );
        section.setListContent(storeArrayList);


        final Section section1 = new Section();
        section1.setHeaderTitle("ĐANG KHUYẾN MÃI");
        final ArrayList<Store> storeArrayList1 = new ArrayList<>();
        storeArrayList1.add(new Store("dZ7lThqPIbXULE8avgPs","Bánh Mì 69 Lê Duẩn","1.5 km","Sale 7%","https://firebasestorage.googleapis.com/v0/b/doanltdd-60a15.appspot.com/o/Image%2Fshopbm2.png?alt=media&token=09600cc9-ae17-4009-8db0-f054fa8e3fb9"));
        db.collection("Stores").limit(6).orderBy("distance")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.isSuccessful()){

                             for (QueryDocumentSnapshot document : task.getResult()) {
                                 Store store = new Store();
                                 store.setStoreId(document.getId());
                                 store.setStoreName(document.getString("storename"));
                                 store.setDistance(document.getString("distance"));
                                 store.setImage(document.getString("image"));
                                 store.setSale(document.getString("sale"));
                                 storeArrayList1.add(store);
                             }
                         }
                     }
                 }
        );
        section1.setListContent(storeArrayList1);

        sectionArrayList.add(section);
        sectionArrayList.add(section1);
        sectionsAdapter = new SectionsAdapter(sectionArrayList,getActivity());
        return sectionsAdapter;
    }
    private void makeRequest() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        Intent refresh = new Intent(getContext(), HomeFragment.class);
        startActivity(refresh);
        getActivity().finish();
    }
    public String VT() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Geocoder geoCoder = new Geocoder(getActivity(),Locale.getDefault());
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


    public void envent(){
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        gotoFoodDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodDeliveryActivity.class);
                startActivity(intent);
            }
        });
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