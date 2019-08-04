package activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import charts_activities.ReportsBarChart;
import charts_activities.ReportsPieChartActivity;
import charts_activities.ReportsRadarChart;
import com.aru.expapp.R;
import database.DBcatch;
import global.GlobalProperties;
import models.Settings;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = HomeActivity.this;

    private ArrayList<Settings> getSettings;
    private DBcatch dbCatch;
    private Settings settings;
    private int user_id;
    boolean pieChart, barChart, radarChart;

    private CardView appCardViewReports;
    private CardView appCardViewBills;
    private CardView appCardViewCategories;
    private CardView appCardViewAccount;
    private CardView appCardViewScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_home) : This will set activity_home.xml layout as UI of your app.
         * it will helps system to easily access the layout from resources/layout directory.
         */
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        initObjects();
        initViews();
        initListeners();
    }

    private void initObjects() {
        dbCatch = new DBcatch(activity);
        settings = new Settings();

        user_id  = ((GlobalProperties) this.getApplication()).getUserTokenVariable();
    }

    private void initViews() {
        appCardViewReports = (CardView) findViewById(R.id.appCardViewReports);
        appCardViewBills = (CardView) findViewById(R.id.appCardViewBills);
        appCardViewCategories = (CardView) findViewById(R.id.appCardViewCategories);
        appCardViewAccount = (CardView) findViewById(R.id.appCardViewAccount);
        appCardViewScan = (CardView) findViewById(R.id.appCardViewScan);
    }


    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCardViewReports.setOnClickListener(this);
        appCardViewBills.setOnClickListener(this);
        appCardViewCategories.setOnClickListener(this);
        appCardViewAccount.setOnClickListener(this);
        appCardViewScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCardViewReports:
                redirectOnReports();
                break;
            case R.id.appCardViewBills:
                redirectOnBills();
                break;
            case R.id.appCardViewCategories:
                redirectOnCategories();
                break;
            case R.id.appCardViewAccount:
                redirectOnProfile();
                break;
            case R.id.appCardViewScan:
                redirectOnScan();
                break;
        }
    }

    private void redirectOnReports(){
        setUpView();
        if(pieChart){
            Intent intentUserInter = new Intent(getApplicationContext(), ReportsPieChartActivity.class);
            startActivity(intentUserInter);
        }if(barChart){
            Intent intentUserInter = new Intent(getApplicationContext(), ReportsBarChart.class);
            startActivity(intentUserInter);
        }if(radarChart){
            Intent intentUserInter = new Intent(getApplicationContext(), ReportsRadarChart.class);
            startActivity(intentUserInter);
        }if(!radarChart && !barChart && !pieChart){
            Toast toast = Toast.makeText(HomeActivity.this, getString(R.string.error_select_type), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
        }
    }
    private void redirectOnBills(){
        Intent intentUserInter = new Intent(activity, BillsListActivity.class);
        startActivity(intentUserInter);
    }

    private void redirectOnCategories(){
        Intent intentUserInter = new Intent(getApplicationContext(), CategoriesActivity.class);
        startActivity(intentUserInter);
    }

    private void redirectOnProfile(){
        Intent intentUserInter = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intentUserInter);
    }
    private void redirectOnScan(){
       Intent intentUserInter = new Intent(getApplicationContext(), DocumentScannerActivity.class);
       startActivity(intentUserInter);
    }
    @Override
    public void onBackPressed() {
        Intent intentUserInter = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentUserInter);
    }

    private void setUpView(){
        getSettings = dbCatch.getSettingsByID(user_id);
        for (Settings s : getSettings) {
            Log.d("GET SETTINGS", "The Settings extracted settings " + String.valueOf(getSettings.size()));
            Log.d("GET SETTINGS", " Settings IF " + s.getId()+ "Pie Chart " + s.isPieChart() + " /Bar Chart " + s.isBarChart()
                    + " /Radar Chart " + s.isRadarChart());
            pieChart = s.isPieChart();
            barChart = s.isBarChart();
            radarChart = s.isRadarChart();
        }
    }
}
