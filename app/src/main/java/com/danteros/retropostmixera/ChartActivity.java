package com.danteros.retropostmixera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Gson gson = new GsonBuilder().create();
    String LOG_D = "log";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getItems();
    }

    private void getItems() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://service.4invest.net")
                .build();
        final Chart interFace = retrofit.create(Chart.class);
        Call<ContentItem> call = interFace.getContentItems();
        call.enqueue(new Callback<ContentItem>() {
            @Override
            public void onResponse(Call<ContentItem> call, Response<ContentItem> response) {
                LineChart lineChart = (LineChart) findViewById(R.id.chart);
                lineChart.setMaxVisibleValueCount(-1);

                ContentItem contentitem = response.body();
                List<ContentItem.Items> items = contentitem.items;

                ArrayList<Entry>  entries = new ArrayList<>();
                ArrayList<String> labdate = new ArrayList<String>();

                int i =0;
                Entry entry;
                for (ContentItem.Items item : items) {
                    Date dateObj = new Date(item.getDate() * 1000);
                    labdate.add(String.valueOf(dateObj));
                    entry = new Entry((float) item.getProfit(), i++);
                    entry.setData(item);
                    entries.add(entry);
                }

                LineDataSet dataset = new LineDataSet(entries, "Profit");
                LineData data = new LineData(labdate, dataset);
                MyMarkerView marker = new MyMarkerView(ChartActivity.this, R.layout.marker);
                lineChart.setMarkerView(marker);
                lineChart.setDescription("Description");
                lineChart.setData(data);
                dataset.setDrawFilled(true);
            }

            @Override
            public void onFailure(Call<ContentItem> call, Throwable t) {
                Toast.makeText(ChartActivity.this,"Подключение не выполнено",Toast.LENGTH_LONG).show();
            }
        });
    }
    public class MyMarkerView extends MarkerView {
            private TextView tvProfit,tvMonthProfit,tvTotalProfit;
            public MyMarkerView (Context context, int layoutResource) {
                super(context, layoutResource);
                tvProfit = (TextView) findViewById(R.id.tvProfit);
                tvMonthProfit = (TextView) findViewById(R.id.tvMonthProfit);
                tvTotalProfit = (TextView) findViewById(R.id.tvTotalProfit);
            }

            @Override
            public void refreshContent(Entry e, Highlight highlight) {
                ContentItem.Items item = (ContentItem.Items)e.getData();
                tvProfit.setText("Profit: " + item.getProfit());
                tvMonthProfit.setText("MonthProfit: " + item.getProfit_month());
                tvTotalProfit.setText("TotalProfit: " + item.getTotal_profit());
            }

        @Override
        public int getXOffset(float xpos) {
            return 0;
        }

        @Override
        public int getYOffset(float ypos) {
            return 0;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clExit) {
            Intent intent = new Intent(ChartActivity.this,MainActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
