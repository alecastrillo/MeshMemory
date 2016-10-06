package com.ce2103.itcr.meshmemory.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ce2103.itcr.meshmemory.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Memory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        PieChart memorychart=(PieChart)findViewById(R.id.piegraph);
        int a=49;
        //se sustituye por el llamado a datos de nodos
        ArrayList<Entry> nodesbytes = new ArrayList<>();
        nodesbytes.add(new Entry(a,0));
        nodesbytes.add(new Entry(21,1));
        nodesbytes.add(new Entry(32,2));

        PieDataSet dataset = new PieDataSet(nodesbytes,"Number of calls");
        ArrayList<String> nodesnumber = new ArrayList<>();
        //Se sustituye por datos de los nodos
        nodesnumber.add("Node 1");
        nodesnumber.add("Node 2");
        nodesnumber.add("Node 3");


        PieData data = new PieData(nodesnumber, dataset);
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        memorychart.setDescription("Memory Map");
        memorychart.setData(data);
        memorychart.setTouchEnabled(true);
        memorychart.animateY(5000);
    }
}
