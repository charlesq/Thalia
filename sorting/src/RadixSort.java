package com.charlesq.java.sorting;
public class RadixSort extends Sort
{
    public final int BASE_10 = 10;
    public final int BASE_16 = 16;
    private int base = BASE_16;
    private int [] acc, helper; 
    public RadixSort(int [] numbers)
    {
        super(numbers);
        acc = new int[base];
        helper = new int [size];
    }
    public void sort()
    {
        int denom = 1;
        boolean halt = false;
        while(!halt)
        {
            halt = true;
            setZero(acc);
            for (int i = 0; i < size; i ++)
            {
               int v = numbers[i] / denom % base; 
               ++acc[v];
               if (halt && v > 0)
                   halt = false;
            }
            for (int i = 1; i < base; i ++)
                acc[i] += acc[i-1];
            for (int i = size -1; i >= 0; --i)
                helper[--acc[numbers[i] / denom % base]] = numbers[i]; 
            for (int i = 0; i < size; i ++)
                numbers[i] = helper[i];
            denom *= base;
        }
    }   
    private void setZero(int [] v)
    {
        for (int i = 0; i < v.length; i ++)
            v[i] = 0;
    } 
}
