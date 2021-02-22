package com.example.bolkaruidemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    Context context;
    JSONArray jsonArray;
    private final String IMG_URL = "https://cdn1.bolkarapp.com/uploads/dp/";
    private static final String TAG = "RVAdapter";

    public RVAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.members_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject charsIndi = jsonArray.getJSONObject(position);

            holder.name.setText(charsIndi.getString("n"));

            String imgURL = IMG_URL + charsIndi.getString("u") + ".jpg";
            Glide.with(context)
                    .asBitmap()
                    .load(imgURL)
                    .into(holder.image);

            holder.image.setClipToOutline(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image, mic, hostBadge;
        TextView name, post;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.memberImage);
            name = itemView.findViewById(R.id.memberName);
        }
    }
}
