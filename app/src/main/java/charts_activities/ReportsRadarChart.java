package charts_activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.aru.expapp.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import controllers.LargeValueFormatter;
import database.DBcatch;
import global.GlobalProperties;
import models.Bills;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportsRadarChart extends AppCompatActivity {

    private static final int NB_QUALITIES = 12;
    private RadarChart radarChart;
    private AppCompatActivity activity = ReportsRadarChart.this;
    private TextView set , cancel;
    private AppCompatImageView datePicker;
    private AppCompatTextView total;

    private Bills bills;
    private ArrayList<Bills> myMonthlyTotals;
    private ArrayList<Bills> myYearDaterTotals;
    private DBcatch dbCatch;
    private String searchYear;
    private String checkSelectedYear;
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private float yearTotalAmount;
    private long convert;
    private int user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_register) : This will set activity_register.xml layout as UI of your app.
         * it will helps system to easily access the layout from resources/layout directory.
         */
        setContentView(R.layout.activity_radar_chart);
        getSupportActionBar().hide();
        radarChart = (RadarChart) findViewById(R.id.chartRadar);

        initViews();
        initObjects();
        setUpRadarChart();
    }

    private void initObjects() {
        bills = new Bills();
        user_id  = ((GlobalProperties) this.getApplication()).getUserTokenVariable();

        dbCatch = new DBcatch(activity);
    }

    private  void  initViews(){
        datePicker = (AppCompatImageView) findViewById(R.id.datePicker);
        total = (AppCompatTextView) findViewById(R.id.total);
        datePicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showYearDialog();
            }
        });

    }
    private void setUpRadarChart() {
        myYearDaterTotals = dbCatch.getYearDateByUserID(user_id);

        checkSelectedYear = Integer.toString(year);
        searchYear = checkSelectedYear.substring(2);

        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        for (Bills b: myYearDaterTotals) {
            Log.d("BILLSCOUNT","The number of bills extracted was " + String.valueOf(myYearDaterTotals.size()));
            Log.d("YEARLYTOTAL","Yearly total for " + b.getDateString() + " was " + b.getAmount());
            if (searchYear.equals(b.getDateString())){
                yearTotalAmount = b.getAmount();
            }
        }

        myMonthlyTotals = dbCatch.getMonthDateByUserID(user_id);
        final List<String> timestampList = new ArrayList<>();

        for (Bills b: myMonthlyTotals) {
            Log.d("BILLSCOUNT","The number of bills extracted was " + String.valueOf(myMonthlyTotals.size()));
            Log.d("MONTHYLTOTAL","Monthly total for " + b.getDateString() + " was " + String.valueOf(b.getAmount()));
            String repo = b.getDateString();
            String repo2 = repo.substring(3);
            if (searchYear.equals(repo2)){
                radarEntries.add(new RadarEntry(b.getAmount()));
                timestampList.add(b.getDateString());
            }
        }

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "Date Set1");
        radarDataSet.setValueFormatter(new LargeValueFormatter());
        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        radarDataSet.setDrawValues(true);
        //radarDataSet.setFillAlpha(180);
        radarDataSet.setFormLineWidth(2f);
        radarDataSet.setDrawHorizontalHighlightIndicator(true);
        radarDataSet.setDrawHighlightCircleEnabled(true);

        RadarData data = new RadarData(radarDataSet);
        //Amount text size
        data.setValueTextSize(12f);
        //Display amount values
        data.setDrawValues(true);
        data.setValueTextColor(Color.WHITE);

        radarChart.setBackgroundColor(Color.TRANSPARENT);
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.WHITE);
        radarChart.setWebAlpha(100);
        radarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(timestampList));
        radarChart.getYAxis().setValueFormatter(new LargeValueFormatter());
        radarChart.setData(data);
        radarChart.invalidate();

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0);
        xAxis.setXOffset(0);
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = radarChart.getYAxis();
        //yAxis.setLabelCount(NB_QUALITIES,false);
        yAxis.setTextSize(9f);
        // Amount Labels
        yAxis.setDrawLabels(true);

        Legend l = radarChart.getLegend();
        l.setTextSize(15f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
        Float PIE = yearTotalAmount;
        convert = PIE.longValue();
        format(convert);
        total.setText("Total: "+"$" + r + " For year: " + year);
    }
    private static String[] suffix = new String[]{"","k", "m", "b", "t"};
    private static int MAX_LENGTH = 4;
    private static String r;
    public static String format(double number) {
        r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }
        return r;
    }


    private void showYearDialog() {
        final Dialog d = new Dialog(ReportsRadarChart.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        set = (TextView) d.findViewById(R.id.button1);
        cancel = (TextView) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);
        //to make layout behind routed corners transparent
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                year = nopicker.getValue();
                setUpRadarChart();
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}
