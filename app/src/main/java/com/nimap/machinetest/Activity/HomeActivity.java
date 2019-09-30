package com.nimap.machinetest.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nimap.machinetest.Adapter.HomeDataAdapter;
import com.nimap.machinetest.Util.ConnectionDetector;
import com.nimap.machinetest.API.Constant;
import com.nimap.machinetest.Util.PrefrenceServices;
import com.nimap.machinetest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HomeActivity extends Activity {
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView RecyclerView;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefrenceServices.init(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setContentView(R.layout.activity_main);
        swipeContainer = findViewById(R.id.swipeContainer);
        RecyclerView = findViewById(R.id.RecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setNestedScrollingEnabled(false);
        swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.black, R.color.blue);
        swipeContainer.setOnRefreshListener(this::ShowHomePageData);
        ShowHomePageData();

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (!isInternetPresent) {
            HomePageData(PrefrenceServices.getInstance().getHomePageData());
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void ShowHomePageData() {
        String url = Constant.BASE_URL;
        showpDialog();
        swipeContainer.setRefreshing(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            HomePageData(response);
            PrefrenceServices.getInstance().setHomePageDateData(response);
        }, error -> {
            hidepDialog();
            String message = "";
            if (error instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
            } else if (error instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
            } else if (error instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            }else {
                message = "Please Check Your Internet Connection";
            }
            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(HomeActivity.this));
        requestQueue.add(stringRequest);
    }

    private void HomePageData(String response) {
        boolean responseSuccess;
        final ArrayList<HashMap<String, String>> listHomeData = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (object.getString("Status").equals("200")) {
                responseSuccess = true;
                JSONObject object1 = new JSONObject(object.getString("data"));
                try {
                    JSONArray jArrayValue = new JSONArray(object1.getString("Records"));
                    for (int i = 0; i < jArrayValue.length(); i++) {
                        JSONObject jObjectsValue = jArrayValue.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", jObjectsValue.getString("Id"));
                        map.put("title", jObjectsValue.getString("title"));
                        map.put("description", jObjectsValue.getString("shortDescription"));
                        map.put("mainImageURL", jObjectsValue.getString("mainImageURL"));
                        map.put("collectedValue", jObjectsValue.getString("collectedValue"));
                        map.put("totalValue", jObjectsValue.getString("totalValue"));
                        map.put("endDate", jObjectsValue.getString("endDate"));
                        map.put("startDate", jObjectsValue.getString("startDate"));
                        listHomeData.add(map);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                responseSuccess = false;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
            responseSuccess = false;
        }
        if (responseSuccess) {
            HomeDataAdapter homeAdapter = new HomeDataAdapter(HomeActivity.this, listHomeData);
            RecyclerView.setAdapter(homeAdapter);
            hidepDialog();
        } else {
            RecyclerView.setAdapter(null);
            hidepDialog();
        }


    }

    private void showpDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
            pDialog.setContentView(R.layout.progress_bar);
            Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce)
            finish();
        Toast.makeText(this, R.string.back_double_press, Toast.LENGTH_SHORT).show();
        doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
