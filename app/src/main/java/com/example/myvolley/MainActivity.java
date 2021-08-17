package com.example.myvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //api
    private static final String URL_DATA = "https://api.github.com/search/users?q=language:java+location:nairobi";
    //declare the recycler view
    private RecyclerView recyclerView;
    //declare the adapter
    private DevelopersAdapter myAdapter;
    //declare the Developers List
    private List<DeveloperList> developerLists;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the recycler view
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initialize the developers list...THIS LIST WILL HOLD THE CONTENTS OF OUR REMOTE JSON AND PASS IT TO RECYCLERVIEW
        developerLists = new ArrayList<>();

        loadUrlData();


    }

    private void loadUrlData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("res","Response:" + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        DeveloperList developers = new DeveloperList(jo.getString("login"),
                                jo.getString("html_url"), jo.getString("avatar_url"));
                        developerLists.add(developers);
                        Log.d("res","developers"+ developers);
                    }
                    myAdapter = new DevelopersAdapter(developerLists, getApplicationContext());
                    recyclerView.setAdapter(myAdapter);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("Tag", response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error"+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}