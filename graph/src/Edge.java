/* Graph class presents a graph structure, in which
 * links/edges associated with a vertex  are grouped in
 * an adjacency list. A link between two vertices 
 * is described with an Edge structure, containing  
 * endpoints and weight fields. 
 *
 * In case of unweighted graph, a link may simply be 
 * marked with a the other vertex. This Graph does not
 * does not support this scheme though.
 *  
 * A vertex in the graph is denoted with an unsigned integer
 * ranging from 0 to V-1, where V is the total number of vertex. 
 * The E field records the number of links in the graph.
 * 
 * A graph object instantiated can be either directional or 
 * unidirectional per constructed invoked.
 */ 
package com.charlesq.java.graph;
import java.util.Comparator;
public class Edge implements Comparable<Edge>
{
    private final int e;
    private final int o;
    private final int w;
    public int e () { return e; } 
    public int o () { return o; }
    public int w () { return w; }
    public Edge(int e, int o, int w)
    {
        this.e = e;
        this.o = o;
        this.w = w;
    }
    public int compareTo(Edge eg)
    {
        if (this == eg)
            return 0;
        if (eg == null)
            return 1;
        if (eg.w() == w)
            return 0;
        if (w < eg.w())
           return -1;
        return 1;
    }
}
