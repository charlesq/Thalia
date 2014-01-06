package com.charlesq.java.graph;
import java.util.PriorityQueue;
import java.util.ArrayList; 
import java.util.List;
/* mst proivdes the base class for prim and kruskal algorithms
 */
public abstract class MST
{
    int w;/*agregate weights of edges in the minimal spanning tree/forest */
    PriorityQueue<Edge> pq;/* used by both prim and kruskal algorithms */
    List<Edge> mstree;    /* stores edges of minimal spanning tree*/     
    final Graph g;
    public MST(final Graph g)
    {
        this.g = g;
        pq = new PriorityQueue<Edge>();
        mstree = new ArrayList<Edge>();
    } 
    public abstract void run(int s);
    public abstract void run();
    public Object [] mst() { return mstree.toArray();}
    public int w() { return w;}
}
