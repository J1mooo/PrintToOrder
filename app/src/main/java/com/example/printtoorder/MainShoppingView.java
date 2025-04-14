package com.example.printtoorder;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainShoppingView extends AppCompatActivity {

    private static final String TAG = MainShoppingView.class.getName();
    private static final String PREFKEY = Login.class.getPackage().toString();

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<Item> mItems;
    private ShoppingAdapter mAdapter;
    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shopping_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 88){
            finish();
        }
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if (user == null){
            Log.d(TAG, "onCreate: User not authenticated!");
            finish();
        } else {
            Log.d(TAG, "onCreate: User is autheticated!");
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItems = new ArrayList<>();

        mAdapter = new ShoppingAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        
        initalizeData();

    }

    private void initalizeData() {
        String[] itemsTitle = getResources().getStringArray(R.array.item_names);
        String[] itemsDescription = getResources().getStringArray(R.array.item_descriptions);

        mItems.clear();

        for (int i = 0; i < itemsTitle.length; i++) {
            mItems.add(new Item(itemsTitle[i], itemsDescription[i]));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.logout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Log.d(TAG, "onOptionsItemSelected: Log out clicked");
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else {
            super.onOptionsItemSelected(item);
        }

        return true;
    }
}