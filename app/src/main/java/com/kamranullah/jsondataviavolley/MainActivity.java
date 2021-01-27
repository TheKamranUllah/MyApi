package com.kamranullah.jsondataviavolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mToolbar;
    private Button onlineButton, offlineButton, resetDatabase;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        mToolbar.getBackground().setAlpha(84);



        SQLiteOpenHelper helper = new myHelper(this);
        db = helper.getWritableDatabase();
        onlineButton = (Button) findViewById(R.id.online_button);
        offlineButton = (Button) findViewById(R.id.offline_button);
        resetDatabase = (Button) findViewById(R.id.reset_database);

        onlineButton.getBackground().setAlpha(84);
        offlineButton.getBackground().setAlpha(84);
        resetDatabase.getBackground().setAlpha(84);

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OnlineActivity.class);
                startActivity(intent);
            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OfflineActivity.class);
                startActivity(intent);
            }
        });

        resetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //Deleting all records
              db.delete("MYDATA", "id != ?", new String[] {Integer.toString(0)});
                Toast.makeText(MainActivity.this, "All Records Deleted", Toast.LENGTH_LONG).show();
            }
        });

    }

}
