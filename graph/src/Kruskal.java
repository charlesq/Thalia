package com.charlesq.java.graph;
import java.util.List;
import java.util.Iterator;
public class Kruskal extends MST
{
    private DisjointedSet [] ds; 
    public Kruskal(final Graph g)
    {
        super(g);
    }
    public void run(int s)
    {
        run();
    }
    public void run()
    {
        /* reset the mst weight*/
        w = 0;
        /* establish a disjointed set for each vertex */
        ds = new DisjointedSet[g.V()];
        for (int i = 0; i < g.V(); ++i)
            ds[i] = DisjointedSet.makeSet(i);
        pq.clear();
        mstree.clear(); 
        List<Edge> edges = g.Edges();   
        /* add all edges to the queue */
        Iterator<Edge> it = edges.iterator();
        while(it.hasNext())
        {
           pq.add(it.next());
           it.remove();
        }
        while(!pq.isEmpty())
        {
            /* in each iteration, extract a minimal-weight edge and check if the two endpoints 
               belong to the same set */
            Edge eg = pq.poll();
            /* drop the edge if associated DisjointedSet share the same ancestor */
            if (DisjointedSet.findSet(ds[eg.o()]) == DisjointedSet.findSet(ds[eg.e()]))
                continue;
            /* otherwise, add the edge to the minimal spanning tree and unite the two sets */
            DisjointedSet.makeUnion(ds[eg.o()], ds[eg.e()]);
            mstree.add(eg);
            w += eg.w();
        }
    }
}
