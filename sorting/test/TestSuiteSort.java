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

        TestResult result = new TestResult();
        suite.run(result);
        System.out.println();
        System.out.println("There are in total: " + result.runCount() + " test cases"); 
        if(result.wasSuccessful())
            System.out.println("All tests have passed!");


    } 
}
