package com.charlesq.java.sorting;

public class HeapSort extends Sort
{
   public HeapSort(int [] values)
    {
        super(values);
    } 
    public void sort()
    {
        heapify();

        for (int i = size -1; i>= 0; i --)
        {
            swap(0, i); 
            sift_down(0, i);
        }

    }
    private void heapify()
    {
        for (int i = size >>> 1; i >= 0; i --)
        {
            sift_down(i, size);
        }
    }
    private void sift_down(int i, int len)
    {
        int l = i  << 1 + 1, r = l + 1;
        if (l >= len)
            return;
        if (r >= len)
        {
            if (numbers[i] >= numbers[l])
                return;
            swap(i, l);
            return;
        }
        l = numbers[l] >= numbers[r]?l: r;   
        if (numbers[l] <= numbers[i])
            return;
        swap(l, i);
        sift_down(l, len);
    } 
}


 

