package com.charlesq.java.graph;
import java.util.*;

public class Prim extends MST
{
    private int [] colors;  /* value 0 indicates UNDISCOVERED, value 1 indicates DISCOVERED; 
                               value 2 indicates EXPLORED */   
    public Prim(final Graph g)
    {
        super(g);
        colors = new int [g.V()];
        Arrays.fill(colors, 0);
    }
    public void run()
    {
        run(0);/* start from vertice 0 */
    }
    public void run(int s)
    {
        mstree.clear();
        if (colors[s] != 0)
            return;
        scan(s); 
        while(!pq.isEmpty())
        {
            /* extract a minimal-weight edge from the queue */
            Edge eg = pq.poll(); 
            /* no endpoint is undiscoverd, drop the edge */
            if (colors[eg.o()] != 0 && colors[eg.e()] != 0)
                continue;
            /* add the edge to the spanning tree set 
               scan the undiscovered vertex. 
             */ 
            mstree.add(eg);
            w += eg.w();
            s = colors[eg.o()] == 0? eg.o(): eg.e();
            scan(s);
        }
        
    }
    private void scan(int v)
    {
        int ep;
        if (colors[v] != 0)
            return;
        colors[v] = 1; /*mark as discovered */
        List<Edge> adj = g.Adj(v);
        for (Edge eg: adj)
        {
           pq.add(eg);
        }
        colors[v] = 2;
    }
    
}
