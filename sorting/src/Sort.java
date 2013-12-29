package com.charlesq.java.sorting;
import static java.lang.System.*;
public abstract class Sort
{
    int [] numbers;  
    int size;
    public Sort(int [] values)
    {
        numbers = null;
        size = 0;
        if (values != null)
        {
            numbers = values;
            size = values.length; 
        }
    }
    public abstract void sort();
    public void printSubarray( int i, int j)
    {
        while(i <= j)
        {
           System.out.print(numbers[i++]);
           System.out.print(" ");
        }
        System.out.println();
    }
    public void printArray()
    {
       for (int e : numbers)
       {
          System.out.print(e);
          System.out.print(' ');
        }
        System.out.println();
    }
    /* alternatively use java.util.Arrays.swap() */
    void swap(int i, int j)
    {
        if (i == j)
            return;
        int tmp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = tmp; 
    }
}
