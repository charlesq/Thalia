package com.charlesq.java.sorting;
import static java.lang.System.*;
import java.util.*;
public class QuickSort extends Sort{
    public QuickSort(int [] values) 
    {
        super(values);
    }
    int partition(int start, int end)
    {
       int i = start, j = start; 
       while(++j <= end)
       {
           if (numbers[j] <= numbers[start])
           {
               swap(++i, j); 
           }
       }
       swap(i, start);
       return i;
    }
    public void sort()
    {
        if (size > 1)
            sort(0, size -1);
    }
    private void sort(int start, int end)
    {
         if (end <= start)
            return;
         /*randomly pick a pivot element */
         int p = new Random().nextInt(); 
         if (p < 0)
             p = -p;
         p = p % (end - start + 1) + start;
         swap(start, p);
         /* divide */
         p = partition(start, end);
         if (p != start)
             sort(start, p -1);
         if (p != end)
             sort(p + 1, end);
        /* no explicit conquer procedure */
    } 
}
