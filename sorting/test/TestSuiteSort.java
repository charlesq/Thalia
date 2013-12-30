import org.junit.runner.notification.Failure;
import org.junit.runner.Result;
import junit.framework.*;
public class TestSuiteSort 
{
    public static void main(String[] args) 
    { 
        TestSuite suite = new TestSuite();
        suite.addTest(new TestQuickSort());
        suite.addTest(new TestHeapSort());
        suite.addTest(new TestBubbleSort());
        suite.addTest(new TestMergeSort());
        suite.addTest(new TestRadixSort());
        suite.addTest(new TestInsertSort());
        TestResult result = new TestResult();
        TestSorting.setTestResult(result);
        suite.run(result);
        System.out.println();
        System.out.println("Has run " +  result.runCount() + " test cases"); 
        if(result.wasSuccessful())
            System.out.println("All tests have passed!");
        else
            System.out.println("failed !!!");


    } 
}
