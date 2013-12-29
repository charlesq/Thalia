package com.charlesq.java.sorting;
public class InsertSort extends Sort
{
    public InsertSort(int [] numbers)
    {
        super(numbers);
    }
    public void sort()
    {
        for (int i = 1; i < size; ++i)
            for (int j = i; j > 0; --j)
                if (numbers[j] < numbers[j-1])
                    swap(j-1, j);
    } 
} 
