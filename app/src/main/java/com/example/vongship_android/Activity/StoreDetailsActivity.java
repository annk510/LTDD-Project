package com.example.vongship_android.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vongship_android.Adapter.ProductAdapter;
import com.example.vongship_android.DTO.Product;
import com.example.vongship_android.R;

import java.util.ArrayList;

public class StoreDetailsActivity extends AppCompatActivity {
    RecyclerView product;
    ArrayList<Product> productArrayList;
    ProductAdapter productAdapter;
    void loadProductRecyclerView(LinearLayoutManager layoutManager){
        product = findViewById(R.id.listStore);
        product.setHasFixedSize(true);
        product.setLayoutManager(layoutManager);
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("1","Bánh Mỳ","35.000 VNĐ","Bánh mỳ Việt, đầy dinh dưỡng",R.drawable.banhmy));
        productArrayList.add(new Product("1","Trà Sữa Trân Châu","50.000 VNĐ","Trà sữa thơm ngon nứt mũi",R.drawable.trasua));
        productArrayList.add(new Product("1","Bánh Ép Huế","15.000 VNĐ","Bánh ép tôm thịt Huế",R.drawable.trsua));
        productArrayList.add(new Product("1","Cà Phê","15.000 VNĐ","Đậm đà hương vị Miền Trung",R.drawable.caphe));
        productArrayList.add(new Product("1","Nước Rau Má","20.000 VNĐ","Nước rau má làm đã cơn khát",R.drawable.rauma));
        productArrayList.add(new Product("1","Bánh Cuốn","25.000 VNĐ","Càng cuốn càng ghiền",R.drawable.banhcuon));
        productAdapter = new ProductAdapter(productArrayList,this,LinearLayoutManager.VERTICAL);
        product.setAdapter(productAdapter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        loadProductRecyclerView(layoutManager);
    }
}
