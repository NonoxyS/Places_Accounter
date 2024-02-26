package com.example.placesaccounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.placesaccounter.db.DbManager;
import com.example.placesaccounter.listAdapter.MainAdapter;
import com.example.placesaccounter.listAdapter.ModelRoom;
import com.example.placesaccounter.listAdapter.OnClickListener;

public class MainActivity extends AppCompatActivity {
    private DbManager dbManager;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;
    private ImageButton img_btn_Main, img_btn_Second, getImg_btn_Third, getImg_btn_Fourth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        OnClickListener clickListener = new OnClickListener() { // Test Listener
            @Override
            public void onItemClick(ModelRoom modelRoom, int position) {
                Toast.makeText(getApplicationContext(), "Была выбрана комната: " + modelRoom.getRoom_number(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        dbManager = new DbManager(this);
        mainAdapter = new MainAdapter(this, clickListener);
        rcView = findViewById(R.id.rcView);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDb();
        mainAdapter.updateAdapter(dbManager.readFromDb());
    }

    public void goToSecondActivity(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);

    }

    public void goToThirdActivity(View v) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    public void goToFourthActivity(View v) {
        Intent intent = new Intent(this, FourthActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDb();
    }
}