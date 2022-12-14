package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    String tenThanhPho;
    ImageView imgback;
    TextView txtName;
    ListView lv;

    CustomAdapter customAdapter;
    ArrayList<ThoiTiet> mangthoitiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Anhxa();

        Intent intent =getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua","Du lieu truyen qua: "+ city);
        if (city.equals("")){
            tenThanhPho = "Saigon";
        }
        else{
            tenThanhPho = city;
        }
        Get7DaysData(tenThanhPho);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void Anhxa() {
        imgback = (ImageView) findViewById(R.id.imageviewBack);
        txtName = (TextView) findViewById(R.id.textviewTenthanhpho);
        lv = (ListView) findViewById(R.id.listview);
        mangthoitiet = new ArrayList<ThoiTiet>();
        customAdapter = new CustomAdapter(MainActivity2.this,mangthoitiet);
        lv.setAdapter(customAdapter);

    }

    private void Get7DaysData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast/?q="+data+"&units=metric&cnt=7&appid=5588376f106282563e5fbea08f1a150b";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                    String name = jsonObjectCity.getString("name");

                    txtName.setText(name);

                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                    for(int i=0;i<jsonArrayList.length();i++){
                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        String ngay = jsonObjectList.getString("dt");

                        long l = Long.valueOf(ngay);
                        Date date = new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                        String Day = simpleDateFormat.format(date);

                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                        String max = jsonObjectTemp.getString("temp_max");
                        String min = jsonObjectTemp.getString("temp_min");

                        Double a = Double.valueOf(max);
                        String nhietdoMax = String.valueOf(a.intValue());
                        Double b = Double.valueOf(min);
                        String nhietdoMin = String.valueOf(b.intValue());

                        JSONArray jsonArrayNhietDo = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayNhietDo.getJSONObject(0);
                        String status = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");

                        mangthoitiet.add(new ThoiTiet(Day,status, icon, nhietdoMax, nhietdoMin));
                    }
                    customAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}