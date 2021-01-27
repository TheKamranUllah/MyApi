package com.kamranullah.jsondataviavolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    private LayoutInflater inflater;
    private List<myData> dataList;
    private Context context;

    public myAdapter(Context theContext, List<myData> dataList) {
        this.inflater = LayoutInflater.from(theContext);
        this.dataList = dataList;
        this.context = theContext;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custome_layout, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        myData ourData = dataList.get(position);

        holder.name.setText(ourData.getName());
        holder.field_email.setText(ourData.getField_email());
        holder.field_url.setText(ourData.getField_url());
try {
    Picasso.get().load(ourData.getField_logo()).placeholder(R.drawable.placeholder).into(holder.logo_image);
}
catch (IllegalArgumentException e){e.printStackTrace();}
catch (NullPointerException e){e.printStackTrace();}
catch (RuntimeException e){e.printStackTrace();}
   
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView logo_image;
        private TextView name, field_url, field_email;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            logo_image = itemView.findViewById(R.id.logo_image);
            name = itemView.findViewById(R.id.name);
            field_url = itemView.findViewById(R.id.field_url);
            field_email = itemView.findViewById(R.id.email_field);
        }
    }
}
