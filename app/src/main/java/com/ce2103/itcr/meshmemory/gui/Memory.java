package com.ce2103.itcr.meshmemory.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ce2103.itcr.meshmemory.R;
import com.ce2103.itcr.meshmemory.datastructures.DoublyLinkedList;
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
        DoublyLinkedList list = Manager.servidor.getListNodes();
        Object[] array = list.arrayVisualize();
        ArrayList<String> nodesnumber = new ArrayList<>();
        ArrayList<Entry> nodesbytes = new ArrayList<>();
        Manager.servidor.getListNodes();
        //se sustituye por el llamado a datos de nodos
        System.out.println("BOOL "+array[0]+"   lenght "+array.length+" leee2 "+((int[])array[1])[0]);
        boolean available = (boolean)array[0];

        System.out.print("Array: ");
        for(int i=0; i<((int[])array[1]).length; i++){
            System.out.print(" "+((int[])array[1])[i]+" ");
        }
        System.out.println();

        for(int i=0; i< ((int[])array[1]).length; i++){
            System.out.println(((int[])array[1])[i]);
            if(available){
                System.out.println(" i "+i);
                nodesbytes.add(new Entry(((int[])array[1])[i],i));
                nodesnumber.add("Available");
                available = !available;
            }else{
                System.out.println(" i "+i);
                nodesbytes.add(new Entry(((int[])array[1])[i],i));
                nodesnumber.add("Occupied");
                available =!available;
            }
        }

        PieDataSet dataset = new PieDataSet(nodesbytes,"Number of bytes");
        PieData data = new PieData(nodesnumber, dataset);
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        memorychart.setDescription("Memory Map");
        memorychart.setData(data);
        memorychart.setTouchEnabled(true);
        memorychart.animateY(5000);
    }
}
