import com.charlesq.java.sorting.Sort;
import junit.framework.*;
import com.charlesq.java.sorting.Sort;
import java.util.*;
import org.junit.Test;
import junit.framework.*;

import static org.junit.Assert.*;

public abstract class TestSorting extends TestCase
{
    int [] numbers;
    Sort sort;
    static TestResult testResult;
    public void setUp()
    {
        int sz = 30;
        generateArray(sz);
        instantiate();
        System.out.println();
        System.out.println("****** run (" + getName() + ") test case **********");
        printArray("Prior to sort:"); 
    }
    public static void setTestResult(TestResult tr)
    {
       testResult = tr; 
    }
    public void runTest()
    {
        sort.sort();
        printArray("Post Sort:");
        if (!isSorted())
        {
            /* if testResult is non-null, 
               will test suite exits on failure */
            if (testResult != null)
                testResult.stop();
            assertTrue(false);
        }
        
    }
    abstract public void instantiate();
    private boolean isSorted()
    {
        for (int i =1; i < numbers.length; i ++)
        {
            if (numbers[i-1] > numbers[i])
                return false;
        }
        return true;

    }
    public void tearDown()
    {
        System.out.println("**************************************");
    }
    void printArray(String comment)
    {
        System.out.println(comment);
        for (int e : numbers)
        {
            System.out.print(e);
            System.out.print(" ");
        }
        System.out.println();
    }

    private void  generateArray(int sz)
    {
        if (sz == 0)
            return;
        numbers = new int[sz];
        for (int i = 0; i < sz; i ++)
            numbers[i] = i;
        fisher_yates_shuffle();
        return;
    } 
    private void fisher_yates_shuffle()
    {
        int j;
        Random rdm = new Random();
        for (int i = 1; i < numbers.length; i ++) 
        {
            j = rdm.nextInt(Integer.MAX_VALUE) % (i +1);
     
            /* j is always positive. 
            if (j < 0)
                j = -j;
            */
            swap(i, j); 
        }
    }
    private void swap(int i, int j)
    {
      
        if (i != j)
        {
            int tmp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = tmp;
        }
    } 
}
