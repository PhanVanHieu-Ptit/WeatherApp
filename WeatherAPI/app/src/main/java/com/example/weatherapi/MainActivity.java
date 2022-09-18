package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText edtSearch;
    Button btnSearch, btnChangActivity;
    TextView txtName, txtCountry, txtTemp,txtStatus, txtHumidty, txtCloud, txtWind, txtDay;
    ImageView imgIcon;
    String City ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        GetCurrentWeatherData("Saigon");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSearch.getText().toString();
                if (city.equals("")){
                    City = "Saigon";
                }
                else{
                    City = city;
                }
                GetCurrentWeatherData(City);
            }
        });
        btnChangActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("name",city);
                startActivity(intent);

            }
        });
    }
    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=5588376f106282563e5fbea08f1a150b";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String day = jsonObject.getString("dt");
                    String name = jsonObject.getString("name");
                    txtName.setText("Ten thanh pho: "+ name);
                    long l = Long.valueOf(day);
                    Date date = new Date(l*1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm::ss");
                    String Day = simpleDateFormat.format(date);

                    txtDay.setText(Day);
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");

                    Picasso.with(MainActivity.this).load("http://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);
                    txtStatus.setText(status);

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String nhietdo = jsonObjectMain.getString("temp");
                    String doam = jsonObjectMain.getString("humidity");

                    Double a = Double.valueOf(nhietdo);
                    String Nhietdo = String.valueOf(a.intValue());
                    txtTemp.setText(Nhietdo+" C");
                    txtHumidty.setText(doam+"%");

                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String gio = jsonObjectWind.getString("speed");
                    txtWind.setText(gio+"m/s");

                    JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                    String may = jsonObjectClouds.getString("all");
                    txtCloud.setText(may+"%");

                    JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                    String country = jsonObjectSys.getString("country");
                    txtCountry.setText("Quoc gia: "+ country);
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
    private void AnhXa() {
        edtSearch = (EditText) findViewById(R.id.edittextSearch);
        btnChangActivity = (Button) findViewById(R.id.buttonChangeActivity);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        txtName = (TextView) findViewById(R.id.textviewName);
        txtCountry = (TextView) findViewById(R.id.textviewCountry);
        txtTemp = (TextView) findViewById(R.id.textviewTemp);
        txtStatus = (TextView) findViewById(R.id.textviewTrangthai);
        txtHumidty = (TextView) findViewById(R.id.textviewDoam);
        txtCloud = (TextView) findViewById(R.id.textviewMay);
        txtWind = (TextView) findViewById(R.id.textviewGio);
        txtDay = (TextView) findViewById(R.id.textviewDay);
        imgIcon= (ImageView) findViewById(R.id.imageIcon);
    }
}