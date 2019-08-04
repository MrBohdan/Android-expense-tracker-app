package charts_activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.aru.expapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import controllers.LargeValueFormatter;
import database.DBcatch;
import global.GlobalProperties;
import models.Bills;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportsPieChartActivity extends AppCompatActivity {

    private AppCompatActivity activity = ReportsPieChartActivity.this;

    private TextView set , cancel;
    private AppCompatImageView datePicker;

    private Bills bills;
    private ArrayList<Bills> myMonthlyTotals;
    private ArrayList<Bills> myYearDaterTotals;

    private DBcatch dbCatch;

    private String searchYear;
    private String checkSelectedYear;
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private float yearTotalAmount;
    private int user_id;
    private long convert;
    private static String r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_pie_chart) : This will set activity_pie_chartrt.xml layout as UI of your app.
         * it will helps system to easily access the layout from resources/layout directory.
         */
        setContentView(R.layout.activity_pie_chart);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        setUpPieChart();
    }

    private void initObjects() {
        bills = new Bills();
        user_id  = ((GlobalProperties) this.getApplication()).getUserTokenVariable();

        dbCatch = new DBcatch(activity);
    }

    private  void  initViews(){
        datePicker = (AppCompatImageView) findViewById(R.id.datePicker);
        datePicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showYearDialog();
            }
        });

    }

    public void showYearDialog()
    {
        final Dialog d = new Dialog(ReportsPieChartActivity.this);
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
                setUpPieChart();
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


    private void setUpPieChart() {
        myYearDaterTotals = dbCatch.getYearDateByUserID(user_id);

        checkSelectedYear = Integer.toString(year);
        searchYear = checkSelectedYear.substring(2);

        List<PieEntry> pieEntries = new ArrayList<>();
        for (Bills b: myYearDaterTotals) {
            Log.d("BILLSCOUNT","The number of bills extracted was " + String.valueOf(myYearDaterTotals.size()));
            Log.d("YEARLYTOTAL","Yearly total for " + b.getDateString() + " was " + b.getAmount());
            if (searchYear.equals(b.getDateString())){
                yearTotalAmount = b.getAmount();
            }
        }

        myMonthlyTotals = dbCatch.getMonthDateByUserID(user_id);
        for (Bills b: myMonthlyTotals) {
            Log.d("BILLSCOUNT","The number of bills extracted was " + String.valueOf(myMonthlyTotals.size()));
            Log.d("MONTHYLTOTAL","Monthly total for " + b.getDateString() + " was " + String.valueOf(b.getAmount()));
            String repo = b.getDateString();
            String repo2 = repo.substring(3);
            if (searchYear.equals(repo2)){
                pieEntries.add(new PieEntry(b.getAmount(),  b.getDateString()));
            }
        }
            PieDataSet dataSet = new PieDataSet(pieEntries, "");
            dataSet.setValueFormatter(new LargeValueFormatter());
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(18);
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            PieData data = new PieData(dataSet);
            PieChart chart = (PieChart) findViewById(R.id.chart);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.CIRCLE);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);
            l.setTextSize(10f);
            l.setWordWrapEnabled(true);
            l.setDrawInside(false);
            l.getCalculatedLineSizes();
            l.setYOffset(10f);
            Float PIE = yearTotalAmount;
            convert = PIE.longValue();
            format(convert);
            chart.setCenterText("Total: " + " $ " + r + "\r\n" +" For year: " + year);
            chart.setCenterTextSize(18f);

            chart.setCenterTextColor(Color.WHITE);
            chart.invalidate();

            chart.setEntryLabelTextSize(15f);
            chart.setHoleColor(Color.TRANSPARENT);
            chart.getDescription().setEnabled(false);
            chart.setExtraBottomOffset(20f);
            chart.setData(data);
            chart.invalidate();
    }

    private static String[] suffix = new String[]{"","k", "m", "b", "t"};
    private static int MAX_LENGTH = 4;

    public static String format(double number) {
        r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }
        return r;
    }
}

