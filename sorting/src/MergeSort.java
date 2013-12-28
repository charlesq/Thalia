package com.charlesq.java.sorting;
public class MergeSort extends Sort
{
    private int [] b;
    public MergeSort(int []numbers)
    {
        super (numbers);
    }
    public void sort()
    {
        if (size != 0)
        {
            b = new int [size];
            sort(0, size-1);    
 
        }
    }
    private void sort(int start, int end)
    {
       /* terminate if only one element resides in the subarray */
       if (start >= end)
          return;
       
       int m = (start + end)/2; 
       /*divide into two sub arrays */
       sort(start, m);
       sort(m + 1, end);
       /* merge */
       merge(start, end);
    }
    private void merge(int start, int end)
    {
        int m = (start + end)/2, i = m +1, j = 0;
        while(start <= m && i <= end)
            b[j++] = numbers[start] < numbers[i]? numbers[start++]: numbers[i++];
        while(start <= m)
            b[j++] = numbers[start++];
        while(i <= end)
            b[j++] = numbers[i++];
        while( j > 0)
            numbers[end--] = b[--j];   
    }
}

