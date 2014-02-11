import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runner.RunWith;
import org.junit.runner.Result;
import org.junit.Assert;
import org.junit.runner.notification.Failure;
import org.junit.runner.JUnitCore;
@RunWith (Suite.class)
@SuiteClasses({
    TestSearch.class,
    TestMST.class,
 
})

class GraphTests
{
    public void GraphTests()
    {
    }
    public static void main(String[] args)
    {
        Result result = JUnitCore.runClasses(GraphTests.class); 
        for (Failure failure : result.getFailures()) 
        {
            System.out.println(failure.toString());
        }
        if (result.wasSuccessful())
            System.out.println("All tests passed ");
        else
        {
            System.out.println("failed " + result.getFailureCount() + " Tests");
        }
    } 
}
