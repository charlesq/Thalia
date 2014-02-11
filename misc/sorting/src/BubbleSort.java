package com.charlesq.java.sorting;
public class BubbleSort extends Sort
{
    public BubbleSort(int [] numbers)
    {
        super(numbers);
    } 
    public void sort()
    {
        for (int i = size -1; i != 0; i--)
        {
            for (int j = 0; j < i; j ++)
                if (numbers[j] > numbers[j +1])
                    swap(j, j +1);
        } 
    }
}
