package com.example.mymap;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_advert_list);
        RecyclerView recyclerView = findViewById(R.id.advertListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // add horizontal line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        AdvertAdapter advertAdapter = new AdvertAdapter();
        recyclerView.setAdapter(advertAdapter);

        DBManager dbManager = new DBManager(this);
        dbManager.open();
        List<Advert> adverts = dbManager.getAllAdverts();

        if (adverts.isEmpty()) {
            TextView emptyTextView = findViewById(R.id.emptyTextView);
            emptyTextView.setText("No adverts found");
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            advertAdapter.setAdverts(adverts);
            advertAdapter.notifyDataSetChanged();
        }
    }
}
