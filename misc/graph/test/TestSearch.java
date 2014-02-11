import com.charlesq.java.graph.*;
import org.junit.Test; 
import org.junit.Before;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runner.RunWith;
import org.junit.runner.Result;
import org.junit.Assert;
import org.junit.runner.notification.Failure;
import org.junit.runner.JUnitCore;
public class TestSearch
{
   private class PrintVertex implements Action
   {
       String str;
       PrintVertex(String s)
       {
           str = s;
       } 
       public void act(int v)
       {
           System.out.println(str + "vertex " + v);
       }
   } 
   Graph g;  
   @Before 
   public void constructGraph()
   {
       g = new Graph("graph.txt");
       if (g.V() == 0)
           Assert.assertTrue("failed to read from " + "graph.txt", false);
   }
   @Test
   public void testDFS()
   {
       System.out.println("*******Testing depth first search*******");
       DFS dfs = new DFS(g);
       Action discovered = new PrintVertex("Discovered ");
       Action explored = new PrintVertex("Explored ");
       dfs.setDiscoveredAction(discovered);
       dfs.setExploredAction(explored);  
       /* make sure forests will be discovered */
       for (int i = 0; i < g.V(); i ++)
           dfs.search(i); 
   }
   @Test
   public void testBFS()
   {
      System.out.println("*********Testing breadth first search******");
      BFS bfs = new BFS(g);
      Action discovered = new PrintVertex("Discovered ");
      Action explored = new PrintVertex("Explored " );
      bfs.setAction (discovered, true);
      bfs.setAction(explored, false);
      for (int i = 0; i < g.V(); i ++)
          bfs.search(i);
   }
}
