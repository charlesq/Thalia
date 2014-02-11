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
public class TestMST
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
       System.out.println("Testing Minimal Spanning Tree Algorithms");
   }
   @Test
   public void testPrim()
   {
       System.out.println("*******Prim Algorithm *******");
       MST prim = new Prim(g);
       System.out.println("Produced Minimal Spanning trees ");
       /* make sure forests will be discovered */
       for (int i = 0; i < g.V(); i ++)
       {
           /* returns one tree in each run */
           prim.run(i); 
           Object [] obs = prim.mst(); 
           if (obs.length != 0)
           {
               System.out.print("{ ");
               for (Object o: obs)
               {
                   printEdge((Edge)o);
               }
               System.out.print("}");
           }
       }

       System.out.println("\nTotal weights are " + prim.w());
   }
   @Test
   public void testKruskal()
   {
      System.out.println("*********Kruskal Algorithm ******");
      MST kruskal = new Kruskal(g);
      kruskal.run();
      System.out.println("Produced Minimal Spanning trees ");
      Object [] obs = kruskal.mst();
      if (obs.length != 0)
      {
          System.out.print("{ ");
          for (Object o: obs)
          {
              printEdge((Edge)o);
          }
          System.out.print("}");
      }
      System.out.println("\nTotal weights are " + kruskal.w()); 

 
   }
   void printEdge(Edge eg)
   {
        System.out.print("(" +  eg.e());
        System.out.print(", " + eg.o() + ") "); 
   }
}
