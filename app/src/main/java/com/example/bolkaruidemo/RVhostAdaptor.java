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

public class RVhostAdaptor extends RecyclerView.Adapter<RVhostAdaptor.HostViewHolder> {

    Context context;
    JSONObject data;
    JSONArray speakers;
    String host = "Host", mod = "Moderator", speaker = "Speaker";
    private final String IMG_URL = "https://cdn1.bolkarapp.com/uploads/dp/";
    private static final String TAG = "RVhostAdaptor";

    RVhostAdaptor(Context context, JSONObject data) {
        this.context = context;
        this.data = data;
        try {
            speakers = data.getJSONArray("speakers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RVhostAdaptor.HostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.non_members_view, parent, false);
        return new HostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVhostAdaptor.HostViewHolder holder, int position) {
        try {
            Log.d(TAG, "onBindViewHolder: " + data);

            if(position == 0) {
                holder.name.setText(data.getJSONObject("host").getString("n"));

                if (!data.getJSONObject("host").getBoolean("mic")) {
                    holder.mic.setVisibility(View.VISIBLE);
                } else {
                    holder.mic.setVisibility(View.INVISIBLE);
                }

                holder.post.setText(host);

                holder.loop.setVisibility(View.VISIBLE);

                holder.hostBadge.setVisibility(View.VISIBLE);

                String imgURL = IMG_URL + data.getJSONObject("host").getString("u") + ".jpg";
                Glide.with(context)
                        .asBitmap()
                        .load(imgURL)
                        .into(holder.image);
            }
            else {
                speakers = data.getJSONArray("speakers");

                JSONObject charsIndi = speakers.getJSONObject(position - 1);

                holder.name.setText(charsIndi.getString("n"));

                if (!charsIndi.getBoolean("mic")) {
                    holder.mic.setVisibility(View.VISIBLE);
                } else {
                    holder.mic.setVisibility(View.INVISIBLE);
                }

                if (charsIndi.getBoolean("mod")) {
                    holder.post.setText(mod);
                } else {
                    holder.post.setText(speaker);
                }

                String imgURL = IMG_URL + charsIndi.getString("u") + ".jpg";
                Glide.with(context)
                        .asBitmap()
                        .load(imgURL)
                    .into(holder.image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return speakers.length() + 1;
    }

    public class HostViewHolder extends RecyclerView.ViewHolder {

        ImageView image, mic, hostBadge, loop;
        TextView name, post;

        public HostViewHolder(@NonNull View itemView) {
            super(itemView);

            loop = itemView.findViewById(R.id.loop);
            image = itemView.findViewById(R.id.nonMemberImage);
            mic = itemView.findViewById(R.id.mic);
            hostBadge = itemView.findViewById(R.id.hostBadge);
            name = itemView.findViewById(R.id.nonMemberName);
            post = itemView.findViewById(R.id.post);
        }
    }
}
