package com.nimap.machinetest.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nimap.machinetest.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> mItems;

    public HomeDataAdapter(Context context, ArrayList<HashMap<String, String>> mItems) {
        this.context = context;
        this.mItems = mItems;
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home, parent, false);
        return new HomeDataAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final HashMap<String, String> home = mItems.get(position);
        holder.txt_Title.setText(home.get("title"));
        holder.txt_Short_desc.setText(home.get("description"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatprev = new SimpleDateFormat("dd/MM/yyyy");
        Date End = null;
        Date Start = null;
        try {
            End = dateFormatprev.parse(Objects.requireNonNull(home.get("endDate")));
            Start = dateFormatprev.parse(Objects.requireNonNull(home.get("startDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert End != null;
        assert Start != null;
        long different = End.getTime() - Start.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        holder.txt_End_Tym.setText(elapsedDays + "");

        holder.txt_Collection.setText(context.getResources().getString(R.string.Rs) +" "+home.get("collectedValue"));
        holder.txt_Collection_Total.setText(context.getResources().getString(R.string.Rs)+" "+home.get("totalValue"));
        RequestOptions requestOptions1 = new RequestOptions().placeholder(R.drawable.thumbnail_bg).error(R.drawable.thumbnail_bg);
        Glide.with(context).setDefaultRequestOptions(requestOptions1).load(home.get("mainImageURL")).into(holder.ImgVideoThumbnail);
        holder.txt_total_per.setText(100 + "%");

    /*   //to calculate the total Collection in %
     if (!Objects.requireNonNull(home.get("collectedValue")).equalsIgnoreCase("")) {
            double amount = Double.parseDouble(Objects.requireNonNull(home.get("collectedValue")));
            double total = Double.parseDouble(Objects.requireNonNull(home.get("totalValue")));
            double res = (amount /total) * 100;
            double roundOff = Math.round(res);
            holder.txt_total_per.setText(roundOff+"%");

        }*/
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Title, txt_Short_desc, txt_Start_Tym, txt_End_Tym, txt_Collection,
                txt_Collection_Total, txt_total_per;
        LinearLayout main;
        ImageView ImgVideoThumbnail;

        MyViewHolder(View view) {
            super(view);
            txt_Title = view.findViewById(R.id.txt_Title);
            main = view.findViewById(R.id.main);
            ImgVideoThumbnail = view.findViewById(R.id.ImgVideoThumbnail);
            txt_Short_desc = view.findViewById(R.id.txt_Short_desc);
            txt_Start_Tym = view.findViewById(R.id.txt_Start_Tym);
            txt_End_Tym = view.findViewById(R.id.txt_End_Tym);
            txt_Collection = view.findViewById(R.id.txt_Collection);
            txt_Collection_Total = view.findViewById(R.id.txt_Collection_Total);
            txt_total_per = view.findViewById(R.id.txt_total_per);
        }
    }

}
