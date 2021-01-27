package com.kamranullah.jsondataviavolley;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class myHelper extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    private static final String DB_Name = "VolleyData";
    private static final int DB_Version = 1;
    private Context context;
    myHelper(Context context)
    {
        super (context, DB_Name, null, DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {


        sqLiteDatabase.execSQL("CREATE TABLE MYDATA("
                +"id TEXT PRIMARY KEY, "
                +"NAME TEXT, "
                +"FIELD_URL TEXT, "
                +"FIELD_EMAIL TEXT, "
                +"LOGO_IMAGE TEXT);");

        Log.d(LOGCAT,"DATA Created");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {

        String query;
        query = "DROP TABLE IF EXISTS MYDATA";
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
//785 PAGE IN BOOK


    }


    public void insertData(SQLiteDatabase sqLiteDatabase,String id, String Name,String FieldEmail,String FieldUrl, String Logo)
    {
        ContentValues ourValues = new ContentValues();
        ourValues.put("id", id);
        if (Name.equals(""))
        {
            Name = "Not Available";
            ourValues.put("NAME", Name);
        }else
        {
            ourValues.put("NAME", Name);
        }

        if (FieldUrl.equals(""))
        {
            FieldUrl = "No Url";
            ourValues.put("FIELD_URL", FieldUrl);
        }
        else
        {
            ourValues.put("FIELD_URL", FieldUrl);
        }

        if (FieldEmail.equals(""))
        {
            FieldEmail = "No Email";
            ourValues.put("FIELD_EMAIL", FieldEmail);
        }else
        {
            ourValues.put("FIELD_EMAIL", FieldEmail);
        }

        if (Logo.equals(""))
        {
            Logo = "drawable://"+ R.drawable.alert;
            ourValues.put("LOGO_IMAGE", Logo);
        }else
        {
            ourValues.put("LOGO_IMAGE", Logo);
        }

        //Reading from SQLite
        Cursor cursor = sqLiteDatabase.query("MYDATA",
            new String[] {"id", "NAME"},
            "id = ?",
            new String[]{id},
            null, null, null);

        if (cursor.moveToFirst())
        {
            String iD = cursor.getString(cursor.getColumnIndex("id"));
            if (iD.equals(id))
            {
                //If record already exist, don't insert update

                sqLiteDatabase.update("DRINK",
                        ourValues,
                        "_id = ?",
                         new String[] {iD});
                Toast.makeText(context, "Sqlite Updated: "+iD, Toast.LENGTH_SHORT).show();

            }else
                {
                    //If Record doesn't exist insert it

                    long rowInserted = sqLiteDatabase.insert("MYDATA", null, ourValues);
                    if(rowInserted != -1)
                        Toast.makeText(context, "Name: " + Name+ " Image: "+ Logo, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                }
        }else
            {
                long rowInserted = sqLiteDatabase.insert("MYDATA", null, ourValues);
                if(rowInserted != -1)
                    Toast.makeText(context, "New row added, Name: " + Name+ " Image: "+ Logo, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
            }
     cursor.close();
     sqLiteDatabase.close();

    }

}