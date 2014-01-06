package com.charlesq.java.graph;
/* In this package, DisjointedSet is used by Kruskal Algorithms to 
 * to quickly decide whether two vertices share the same ancestor
 * in building a minimal spanning tree.
 * 
 * Three static methods are exposed: makeSet(), findSet and makeUnion. 
 */
class DisjointedSet
{
    private DisjointedSet ancestor;
    private int v;
    private DisjointedSet(int v)
    { 
        ancestor = null;
        this.v = v;
    } 
    /* create th DisjointSet instance associated with a vetex */ 
    public static DisjointedSet makeSet(int v)
    {
        return new DisjointedSet(v);
    }
    /* find ancestor for a DisjointedSet object */
    public static DisjointedSet findSet(DisjointedSet ds)
    {
        if (ds == null)
            return null;
        while(ds.ancestor != null)
        {
            ds = ds.ancestor;   
        }
        return ds;
    }
    /* unite two DisjointedSet objects */
    public static DisjointedSet makeUnion(DisjointedSet ds1, DisjointedSet ds2)
    {
        if (ds1 == null || ds2 == null)
            return ds1 == null? ds2: ds1;
        DisjointedSet ds10 = findSet(ds1), ds20 = findSet(ds2); 
        if (ds10 == ds20)
            return ds10;
        ds10.ancestor = ds20;
        return ds20; 
    }
}
