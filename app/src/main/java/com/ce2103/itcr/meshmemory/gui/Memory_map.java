package com.ce2103.itcr.meshmemory.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ce2103.itcr.meshmemory.*;
import com.ce2103.itcr.meshmemory.datastructures.DoublyLinkedList;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Memory_map extends AppCompatActivity {

    String [] a = Master.cliente.getBytesArray();

    public int getAmountOfParts(String[] pArray){
        int parts=1;
        int count =0;
        String current=pArray[0];
        for(int i=0; i<pArray.length; i++){
            if(pArray[i].equals(current)){
                count++;
            }else{
                current=pArray[i];
                count=1;
                parts++;
            }
        }

        return parts;
    }

    public int[] getArrayOfParts(String[] pArray, int pParts){
        int[] array = new int[pParts];
        String current = pArray[0];
        int counter=0;
        int part=0;
        for(int i=0; i < pArray.length; i++){
            if(i==(pArray.length)-1){
                array[part]=counter+1;
                return array;
            }
            if(pArray[i].equals(current)){
                System.out.println("c++");
                counter++;
            }else{
                array[part]=counter;
                current=pArray[i];
                System.out.println("current "+current);
                part++;
                counter=1;
                System.out.println("not c++");
            }
        }/*

        int[] array = new int[pParts];
        String current = pArray[0];
        int currentPart = 1;
        int count = 0;
        for(int i=0; i<pArray.length; i++){
            System.out.println("Current "+ current);
            if(pArray[i].equals(current)){
                count++;
            }else{
                array[currentPart-1] = count;
                count = 0;
                current = pArray[i];
                currentPart++;
            }
        }*/
        return array;
    }


    public String[] getArrayOfParts(String[] pArray, int[] pIntArray){
        String[] res= new String[pIntArray.length];
        String current = pArray[0];
        int currentIndex=0;
        int part=0;
        for(int i=0; i<pIntArray.length; i++){
            res[i]=pArray[currentIndex];
            currentIndex+= pIntArray[i];
        }
        return res;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_map);

        PieChart nodMemorychart=(PieChart)findViewById(R.id.pieNodeGraph);
        String [] bytesArray = Master.cliente.getBytesArray();
        ArrayList<String> nodesnumber = new ArrayList<>();
        ArrayList<Entry> nodesbytes = new ArrayList<>();


        //se sustituye por el llamado a datos de nodos
        int parts = getAmountOfParts(bytesArray);
        System.out.println("Parts "+parts);
        int[] arrayOfInt = getArrayOfParts(bytesArray,parts);
        for(int i=0; i<arrayOfInt.length; i++){
            System.out.print(arrayOfInt[i]);
        }System.out.println();

        String[] arrayOfUUID=getArrayOfParts(bytesArray, arrayOfInt);
        for(int i=0; i<arrayOfUUID.length; i++){
            System.out.print(arrayOfUUID[i]);
        }System.out.println();

        for(int i=0; i< arrayOfUUID.length; i++){
            nodesbytes.add(new Entry(arrayOfInt[i], i));
            System.out.println(" "+arrayOfInt[i]+" "+i+" "+arrayOfUUID[i]);
            nodesnumber.add(arrayOfUUID[i]);
        }

        PieDataSet dataset = new PieDataSet(nodesbytes,"UUID of bytes");
        PieData data = new PieData(nodesnumber, dataset);
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        nodMemorychart.setDescription("Memory Map");
        nodMemorychart.setData(data);
        nodMemorychart.setTouchEnabled(true);
        nodMemorychart.animateY(5000);
    }
}
