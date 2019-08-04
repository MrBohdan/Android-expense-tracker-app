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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

public class ReportsBarChart extends AppCompatActivity {
    private AppCompatActivity activity = ReportsBarChart.this;
    private TextView set , cancel;
    private AppCompatImageView datePicker;
    private AppCompatTextView total;
    private BarChart barChart;

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
        setContentView(R.layout.activity_bar_chart);
        getSupportActionBar().hide();
        barChart = (BarChart) findViewById(R.id.chartBar);

        initViews();
        initObjects();
        setUpBarChart();
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
    private void showYearDialog()
    {
        final Dialog d = new Dialog(ReportsBarChart.this);
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
                setUpBarChart();
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

    private void setUpBarChart(){
        myYearDaterTotals = dbCatch.getYearDateByUserID(user_id);

        checkSelectedYear = Integer.toString(year);
        searchYear = checkSelectedYear.substring(2);

        ArrayList<BarEntry> barEnties = new ArrayList<>();
        for (Bills b: myYearDaterTotals) {
            Log.d("BILLSCOUNT","The number of bills extracted was " + String.valueOf(myYearDaterTotals.size()));
            Log.d("YEARLYTOTAL","Yearly total for " + b.getDateString() + " was " + b.getAmount());
            if (searchYear.equals(b.getDateString())){
                yearTotalAmount = b.getAmount();
            }
        }

        final List<String> timestampList = new ArrayList<>();
        myMonthlyTotals = dbCatch.getMonthDateByUserID(user_id);

        int index = 0;
        for (Bills b: myMonthlyTotals) {
            Log.d("BILLSCOUNT","The number of bills extracted was " + String.valueOf(myMonthlyTotals.size()));
            Log.d("MONTHYLTOTAL","Monthly total for " + b.getDateString() + " was " + String.valueOf(b.getAmount()));
            String repo = b.getDateString();
            String repo2 = repo.substring(3);
            if (searchYear.equals(repo2)){
                barEnties.add(new BarEntry(index, b.getAmount()));
                timestampList.add(b.getDateString());
                index++;
            }
        }
        BarDataSet barDataSet = new BarDataSet(barEnties, "Date Set1");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(true);

        BarData data = new BarData(barDataSet);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextColor(Color.WHITE);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        //barChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        barChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setLabelCount(Integer.MAX_VALUE, true);
        barChart.animateXY(400, 400);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(timestampList));
        barChart.invalidate();

        barChart.setData(data);
        barChart.invalidate();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelCount(12);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setTextColor(Color.WHITE);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(12f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(4f);

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
}
