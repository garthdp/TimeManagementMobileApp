package com.example.taskwavepart1;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class graphTest extends AppCompatActivity {
    LineChart lineChart;
    int[] colorClassArray = new int[]{Color.RED, Color.CYAN, Color.GREEN};
    public static ArrayList<Timesheet> arrTimeJava = new ArrayList<>();
    public static int min;
    public static int max;
    private DatePickerDialog datePickerDialog1;
    private DatePickerDialog datePickerDialog2;
    private Button btnDate1;
    private Button btnDate2;
    private Button btnFilter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graph_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /*
        Code attribution
        Title = "002 Line Chart : MP Android Chart Tutorial"
        Website link = https://www.youtube.com/watch?v=yrbgN2UvKGQ&list=PLFh8wpMiEi89LcBupeftmAcgDKCeC24bJ&index=2
        Author = Sarthi Technology
        Usage = Watched video on how to do line chart
        */
        /*
        Code attribution
        Title = "Pop Up Date Picker Android Studio Tutorial"
        Website link = https://www.youtube.com/watch?v=qCoidM98zNk
        Author = Code with cal
        Usage = Watched video on how to do a date picker in java for android
        */

        initDatePicker1();
        initDatePicker2();
        btnDate1 = findViewById(R.id.btnFilterDate1);
        btnDate2 = findViewById(R.id.btnFilterDate2);
        btnFilter = findViewById(R.id.btnFilter);
        lineChart = findViewById(R.id.lineChart);
        Button btnDate1 = findViewById(R.id.btnFilterDate1);
        Button btnDate2 = findViewById(R.id.btnFilterDate2);
        Log.d("Tag", arrTimeJava.toString());
    }

    private void initDatePicker1() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnDate1.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog1 = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnDate2.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        String newDay = "";
        String newMonth = "";
        if (day < 10){
            newDay = "0"+day;
        }
        else {
            newDay = String.valueOf(day);
        }
        if (month < 10){
            newMonth = "0"+month;
        }
        else {
            newMonth = String.valueOf(month);
        }
        return newDay + "/" + newMonth + "/" + year;
    }
    public void btnDate1(View view) {
        datePickerDialog1.show();
    }

    public void btnDate2(View view) {
        datePickerDialog2.show();
    }

    public void btnFilter(View view) {
        ArrayList<Entry> dataMin = new ArrayList<>();
        ArrayList<Entry> dataMax = new ArrayList<>();
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        ArrayList<workDay> days = new ArrayList<workDay>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if(!btnDate1.getText().equals("Date") && !btnDate2.getText().equals("Date")){
            LocalDate dateStart = LocalDate.parse(btnDate1.getText(), formatter);
            LocalDate dateFinish = LocalDate.parse(btnDate2.getText(), formatter);

            /*
            Code attribution
            Title = "Number of Days between Two Dates in Java"
            Website link = https://howtodoinjava.com/java/date-time/calculate-days-between-dates/
            Author = Lokesh Gupta
            Usage = Learnt how to get number of days between two dates
            */

            long duration = ChronoUnit.DAYS.between(dateStart, dateFinish);
            for (int i = 0; i < duration; i++){
                LocalDate currentDate = dateFinish.minusDays(i);
                String formattedDate = currentDate.format(formatter);
                days.add(new workDay(formattedDate, 0));
            }
            Collections.reverse(days);
            for(int i = 0; i < days.size(); i++){
                int totalTimesheetHours = 0;
                for(int j = 0; j < arrTimeJava.size(); j++){
                    Log.d("DateCheck", days.get(i).date + " " + arrTimeJava.get(j).getDate());
                    if (Objects.equals(days.get(i).date, arrTimeJava.get(j).getDate())){
                        int startTimeTotal = (Integer.parseInt(arrTimeJava.get(j).getStartTime().substring(0, 2)) * 60) + Integer.parseInt(arrTimeJava.get(j).getStartTime().substring(3, 5));
                        int endTimeTotal = (Integer.parseInt(arrTimeJava.get(j).getEndTime().substring(0, 2)) * 60) + Integer.parseInt(arrTimeJava.get(j).getEndTime().substring(3, 5));
                        totalTimesheetHours += endTimeTotal - startTimeTotal;
                    }
                }
                totalTimesheetHours /= 60;
                days.get(i).setHours(totalTimesheetHours);
                dataVals.add(new Entry(i, days.get(i).hours));
                dataMin.add(new Entry(i, min));
                dataMax.add(new Entry(i, max));
            }
        }
        if (!dataVals.isEmpty()){
            makeGraph(dataVals, dataMin, dataMax);
        }
    }

    public void makeGraph(ArrayList<Entry> values, ArrayList<Entry> minHours, ArrayList<Entry> maxHours){
        LineDataSet lineDataSet = new LineDataSet(values, "Recorded Hours");
        LineDataSet lineDataSet1 = new LineDataSet(minHours, "Min Hours");
        LineDataSet lineDataSet2 = new LineDataSet(maxHours, "Max Hours");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setCircleColor(Color.RED);
        lineDataSet2.setColor(Color.parseColor("#07910a"));
        lineDataSet2.setCircleColor(Color.parseColor("#07910a"));

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }


    public void btnPast(View view) {
        ArrayList<Entry> dataMin = new ArrayList<>();
        ArrayList<Entry> dataMax = new ArrayList<>();
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        ArrayList<workDay> days = new ArrayList<workDay>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        today = LocalDate.parse(today.format(formatter), formatter);
        for(int i = 0; i <31; i++){
            LocalDate currentDate = today.minusDays(i);
            String formattedDate = currentDate.format(formatter);
            days.add(new workDay(formattedDate, 0));
        }
        Collections.reverse(days);
        for(int i = 0; i < days.size(); i++){
            int totalTimesheetHours = 0;
            for(int j = 0; j < arrTimeJava.size(); j++){
                Log.d("DateCheck", days.get(i).date + " " + arrTimeJava.get(j).getDate());
                if (Objects.equals(days.get(i).date, arrTimeJava.get(j).getDate())){
                    int startTimeTotal = (Integer.parseInt(arrTimeJava.get(j).getStartTime().substring(0, 2)) * 60) + Integer.parseInt(arrTimeJava.get(j).getStartTime().substring(3, 5));
                    int endTimeTotal = (Integer.parseInt(arrTimeJava.get(j).getEndTime().substring(0, 2)) * 60) + Integer.parseInt(arrTimeJava.get(j).getEndTime().substring(3, 5));
                    totalTimesheetHours += endTimeTotal - startTimeTotal;
                }
            }
            totalTimesheetHours /= 60;
            days.get(i).setHours(totalTimesheetHours);
            dataVals.add(new Entry(i, days.get(i).hours));
            dataMin.add(new Entry(i, min));
            dataMax.add(new Entry(i, max));
        }
        if (!dataVals.isEmpty()){
            makeGraph(dataVals, dataMin, dataMax);
        }
    }

    public void btnBack(View view) {
        Intent intent = new Intent(this, timesheets.class);
        startActivity(intent);
    }

    class workDay{
        private String date;
        private int hours;
        public workDay(String date, int hoursWorked){
            this.hours = hoursWorked;
            this.date = date;
        }

        public int getHours() {
            return hours;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }
    }
}