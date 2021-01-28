package com.kamranullah.jsondataviavolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class OnlineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<myData> theData;
    private static String JSON_URL = "https://www.ggemploi.com/json/employeur";
    private myAdapter adapter;
    private ProgressBar progressBar;

    SQLiteDatabase sqLiteDatabase;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private boolean wificonnection = false;
    private boolean mobileconnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        mToolbar = findViewById(R.id.main_bar_online);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Online Usage");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getBackground().setAlpha(84);

        progressBar = (ProgressBar) findViewById(R.id.progress_circular_online);

        recyclerView = findViewById(R.id.data_container);
        theData = new ArrayList<>();

        isInternetConnection();

        extractData();
    }

    private void extractData()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        myData data = new myData();

                        String TheName = jsonObject.getString("name").toString();
                        String Field_email = jsonObject.getString("field_email").toString();
                        String field_Logo = jsonObject.getString("field_logo").toString();
                        String field_Url = jsonObject.getString("field_url").toString();
                        String id = jsonObject.getString("id").toString();

                        data.setName(TheName);
                        data.setField_email(Field_email);
                        data.setField_logo(field_Logo);
                        data.setField_url(field_Url);
                        data.setId(id);
                        theData.add(data);

                        //Inserting into Sqlite database.
                        puttingDataIntoSQlite(sqLiteDatabase,id, TheName, Field_email, field_Url, field_Logo);

                    }catch (JSONException | IllegalArgumentException | SecurityException| SQLiteException e)
                    {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                adapter = new myAdapter(getApplicationContext(), theData);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("tag", "onErrorResponse: "+ error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void puttingDataIntoSQlite(SQLiteDatabase sqLiteDatabase,String id,
                                      String TheName, String Field_email,String field_Url,String field_Logo)
    {
        SQLiteOpenHelper helper = new myHelper(getApplicationContext());
        sqLiteDatabase = helper.getWritableDatabase();

        ((myHelper) helper).insertData(sqLiteDatabase,id, TheName, Field_email, field_Url, field_Logo);

        sqLiteDatabase.close();
    }


    private void isInternetConnection( )
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            wificonnection = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileconnection = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (wificonnection)
            {
                Toast.makeText(this, "You're mobile is connected to WIFI!", Toast.LENGTH_LONG).show();
            }
            else if (mobileconnection)
            {
                Toast.makeText(this, "You're mobile is connected to  CELLULAR DATA!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            View mview = LayoutInflater.from(OnlineActivity.this).inflate(R.layout.dialog_layout, null);
            TextView Message = mview.findViewById(R.id.dialog_text);
            Button OkBtn = mview.findViewById(R.id.dialog_btn);
            AlertDialog.Builder mbuilder = new AlertDialog.Builder(OnlineActivity.this, R.style.mydialog);
            mbuilder.setView(mview);
            mbuilder.setCancelable(false);
            String message = "Please check you'r internet connection!";
            Message.setText(message);

            final Dialog dialog = mbuilder.create();
            dialog.show();

            OkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
        }
    }
}
