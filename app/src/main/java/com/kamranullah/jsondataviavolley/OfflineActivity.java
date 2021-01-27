package com.kamranullah.jsondataviavolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OfflineActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mToolbar;
    private RecyclerView myRecylerView;
    private int myId =0;
    private List<myData> theData;
    private myAdapter madapter;

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


        myRecylerView = findViewById(R.id.offline_data_container);
        theData = new ArrayList<>();
        isInternetConnection();

         fillDataToArray();

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
            Toast.makeText(this, "NOT CONNECTED TO INTERNET!", Toast.LENGTH_LONG).show();
        }
    }



}
