package com.charlesq.java.graph;
import java.util.*;
/** DFS implements depth First Search and allows action to performed 
  * the moment a vertex is discovered or all neighbors explored.
  *  
  *
  **/
public class DFS
{
    final Graph g;
    Action discovered; /* action to take when discovered */
    Action explored;   /* action to take when fully explored*/      
    int [] vstate; /* 0 denotes undiscovered, 1 denotes discoverd, 2 denotes explored */ 
    public DFS(Graph g)
    {
        this.g = g;
        this.vstate = new int[g.V()];
        Arrays.fill(vstate, 0, g.V(), 0);
    }
    /* this should be invoked before search starts */
    public void setDiscoveredAction(Action ad)
    {
       this.discovered = ad;
    }
    /* this should be invoked before search starts */
    public void setExploredAction(Action ae)
    {
        this.explored = ae;
    }
    /* starts search from vertex v
     * 
     */
    public void search (int v)     
    {
       if (v >= g.V())
           return; 
       if (vstate[v] != 0)
           return; 
       if (discovered != null)
           discovered.act(v);           
       vstate[v] = 1;
       List<Edge> l = g.Adj(v);
       int ep;
       for (Edge edge: l)
       {
           ep = (v == edge.e())? edge.o(): edge.e(); 
           search(ep); 
       }
       vstate[v] = 2;
       if (explored != null)
           explored.act(v);
     }
}
