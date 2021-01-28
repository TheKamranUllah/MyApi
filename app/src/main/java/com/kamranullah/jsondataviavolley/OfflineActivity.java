package com.kamranullah.jsondataviavolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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

public class OfflineActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mToolbar;
    private RecyclerView myRecylerView;
    private int myId =0;
    private List<myData> theData;
    private myAdapter madapter;
    private static String JSON_URL = "https://www.ggemploi.com/json/employeur";
    SQLiteDatabase sqLiteDatabase;
    private ProgressBar progressBar;

    private boolean wificonnection = false;
    private boolean mobileconnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        mToolbar = findViewById(R.id.main_bar_offline);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Offline Usage");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getBackground().setAlpha(84);

        progressBar = (ProgressBar) findViewById(R.id.progress_circular_offline);

        myRecylerView = findViewById(R.id.offline_data_container);
        theData = new ArrayList<>();

        toCheckIfDataIsAvailable();

       //  fillDataToArray();

}

    private void toCheckIfDataIsAvailable()
    {

        SQLiteOpenHelper handler = new myHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        String count = "SELECT count(*) FROM MYDATA";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0)
        {
            fillDataToArray();
        }
     else
    {
        isInternetConnection();
        fillDataToArray();
    }


    }

    public void fillDataToArray() {

        ArrayList<myData> arrayList = new ArrayList<myData>();
        SQLiteOpenHelper handler = new myHelper(this);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Reading data from sqlite

        Cursor c = db.query("MYDATA",
                new String[] {"NAME", "FIELD_URL", "FIELD_EMAIL", "LOGO_IMAGE"},
                "id <> ?",
                new String[]{Integer.toString(myId)},
                "NAME", null, null);
        try
        {

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int count = 0; count < c.getCount(); count++) {

                    myData detail = new myData();
                    String name = c.getString(c.getColumnIndex("NAME"));
                    String field_url = c.getString(c.getColumnIndex("FIELD_URL"));
                    String field_email = c.getString(c.getColumnIndex("FIELD_EMAIL"));

                    //ourData.setId(id);
                    detail.setName(name);
                    detail.setField_url(field_url);
                    detail.setField_email(field_email);

                    String logo_image = c.getString(c.getColumnIndex("LOGO_IMAGE"));

                    detail.setField_logo(logo_image);
//fill Arraylist here
                    arrayList.add(detail);
                    c.moveToNext();

                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        c.close();
        db.close();

        myRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        madapter = new myAdapter(OfflineActivity.this, arrayList);
        madapter.notifyDataSetChanged();
        myRecylerView.setAdapter(madapter);
        progressBar.setVisibility(View.INVISIBLE);
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
                Toast.makeText(this, "Please Wait, Data Downloading!", Toast.LENGTH_LONG).show();
                extractData();
            }
            else if (mobileconnection)
            {
                Toast.makeText(this, "Please Wait, Data Downloading!", Toast.LENGTH_LONG).show();
                extractData();
            }
        }
        else
        {
            View mview = LayoutInflater.from(OfflineActivity.this).inflate(R.layout.dialog_layout, null);
            TextView Message = mview.findViewById(R.id.dialog_text);
            Button OkBtn = mview.findViewById(R.id.dialog_btn);
            AlertDialog.Builder mbuilder = new AlertDialog.Builder(OfflineActivity.this, R.style.mydialog);
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

                        String TheName = jsonObject.getString("name").toString();
                        String Field_email = jsonObject.getString("field_email").toString();
                        String field_Logo = jsonObject.getString("field_logo").toString();
                        String field_Url = jsonObject.getString("field_url").toString();
                        String id = jsonObject.getString("id").toString();

                        //Inserting into Sqlite database.
                        puttingDataIntoSQliteOnOffline(sqLiteDatabase,id, TheName, Field_email, field_Url, field_Logo);

                    }catch (JSONException | IllegalArgumentException | SecurityException| SQLiteException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("tag", "onErrorResponse: "+ error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void puttingDataIntoSQliteOnOffline(SQLiteDatabase sqLiteDatabase,String id,
                                      String TheName, String Field_email,String field_Url,String field_Logo)
    {
        SQLiteOpenHelper helper = new myHelper(getApplicationContext());
        sqLiteDatabase = helper.getWritableDatabase();

        ((myHelper) helper).insertData(sqLiteDatabase,id, TheName, Field_email, field_Url, field_Logo);

        sqLiteDatabase.close();

        selfIntent();
    }

    private void selfIntent()
    {
        Intent intent = new Intent(OfflineActivity.this, OfflineActivity.class);
        startActivity(intent);
    }

}
