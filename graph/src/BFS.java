package com.charlesq.java.graph;
import java.util.*;
public class BFS
{
    final Graph g;
    Action discovered; /* Action to take upon discovery */
    Action explored; /* Action to take upon completion of 
                       scanning neighbors */ 
    int [] vstate; /* 0 indicates undiscovered; 1 indicates discovered;
                     2 indicates explored */
    List<Integer> list; /* buffering vertices to explore */     
    public BFS(Graph g)
    {
        this.g = g;
        this.vstate = new int [g.V()];
        Arrays.fill(vstate, 0, g.V(), 0);
        list = new  ArrayList<Integer>();
    }
    public void setAction(Action a, boolean discovery)
    {
        if (discovery)
            this.discovered = a;
        else
             this.explored = a;

    }
    public void search (int v)
    {
        List<Graph.Edge> l;
        Iterator<Integer> vIt;
        int ep;
        if (v >= g.V() || vstate[v] != 0)
            return;
        vstate[v] = 1;
        if(discovered != null)
            discovered.act(v);
        list.add(v);
        vIt = list.iterator();
        while(!list.isEmpty())
        {
           vIt = list.iterator();
           v = vIt.next(); 
           list.remove(0);
           switch(vstate[v])
           {
           case 2:
               break;
           case 1:
               l = g.Adj(v); 
               for (Graph.Edge edge: l)
               {
                   ep = edge.e() == v? edge.o(): edge.e();    
                   if (vstate[ep] == 0)
                   {
                       vstate[ep] = 1;
                       if (discovered != null)
                           discovered.act(ep);
                       list.add(ep);
                   }
               }
               vstate[v] = 2;
               if (explored != null)
                   explored.act(v);
           }
          
        }
    }
}
