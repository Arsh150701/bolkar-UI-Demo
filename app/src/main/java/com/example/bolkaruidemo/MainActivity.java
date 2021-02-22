package com.example.bolkaruidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String ROOM_API = "https://api.bolkarapp.com/live/room.json";
    private final String IMG_URL = "https://cdn1.bolkarapp.com/uploads/dp/";
    JSONObject room, data;
    JSONArray  speakers, membersarr;
    RecyclerView nonMembers, members;
    Toolbar customToolbar;
    ImageView hostimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostimg = findViewById(R.id.hostimg);
        customToolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getjson();
    }

    public void getjson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ROOM_API,
                response -> {
                    try {
                        room = new JSONObject(response);
                        data = room.getJSONObject("data");
                        String title = room.getJSONObject("data").getString("topic");
                        customToolbar.setTitle(title);

                        String hostimgURL = IMG_URL + data.getJSONObject("host").getString("u") + ".jpg";
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(hostimgURL)
                                .into(hostimg);

                        membersarr = room.getJSONObject("data").getJSONArray("members");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        nonMembers = findViewById(R.id.nonMembers);
                        members = findViewById(R.id.members);

                        RVhostAdaptor rvhost = new RVhostAdaptor(getApplicationContext(), data);
                        nonMembers.setAdapter(rvhost);
                        nonMembers.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                        RVAdapter rvamembers = new RVAdapter(getApplicationContext(), membersarr);
                        members.setAdapter(rvamembers);
                        members.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
                    }
                },
                error -> {
                    Log.d(TAG, "getjson: " + error);
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.world:
                Toast.makeText(this, "People icon pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.hostimg:
                Toast.makeText(this, "Host pic clicked", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}