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
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class Graph
{
    int V; 
    int E;
    List<Edge> [] Adj;  
    ArrayList<Edge>  Edges;
    @SuppressWarnings("unchecked")
    public Graph(String fn, boolean directed) 
    {
       Scanner sc = null;
       try 
       {
           sc = new Scanner(new FileInputStream(fn)); 
           /* read number of vertices */
       }
       catch(FileNotFoundException e)
       {
           System.out.println("File not found exception");
       }
       if (sc.hasNextInt())
           V = sc.nextInt(); 
       else
       {
           System.out.println("Error in reading file " + fn);
           return; 
       }
       Edges = new ArrayList<Edge>();
       /* read number of edges in the graph */
       E = sc.nextInt();
       Adj = new ArrayList [V];
       for (int i = 0; i < V; i ++)
           Adj[i] = new ArrayList<Edge> ();
       int n = E, ep1, ep2, w;
       /* read edge parameters and instantiate Edge object/establish adjancy */
       while(n-- != 0)
       {
           ep1 = sc.nextInt();
           ep2 = sc.nextInt();
           w   = sc.nextInt();  
           Edge e = new Edge(ep1, ep2, w); 
           Edges.add(e);
           Adj[ep1].add(e);
           if (!directed)
               Adj[ep2].add(e);
       }
    }
    public Graph( String fn)
    {
        this (fn, false);
    } 
    @SuppressWarnings("unchecked")
    private void init(Graph g)
    {
       V = g.V(); 
       E = g.E();
       Adj = new ArrayList [V];
       Edges = new ArrayList<Edge>();
       List<Edge> l;
       Iterator<Edge> it;
       Edge e;
       while(--V >= 0)
       {
           l = Adj[V]; 
           it = l.iterator(); 
           while(it.hasNext())
           {
               e = it.next();
               Edge edge = new Edge(e.e(), e.o(), e.w());
               Edges.add(edge);
               Adj[e.e()].add(edge);
               Adj[e.o()].add(edge);
           }
       }
       V = g.V();
    }
    public Graph (Graph g)
    {
        init(g);
    }
    @SuppressWarnings("unchecked")
    public Graph(Graph g, boolean transpose)
    {
        if (!transpose)  
            init(g);
        V = g.V();
        E = g.E();
        Adj = new ArrayList [V];
        List<Edge> l;
        Iterator<Edge> it;
        Edge e;
        Edges = new ArrayList<Edge> ();
        while(--V >= 0)
        {
            l = Adj[V];
            it = l.iterator();
            while(it.hasNext())
            {
                e = it.next();
                Edge edge = new Edge(e.o(), e.e(),  e.w());
                Adj[e.e()].add(edge);
                Edges.add(edge);
            }
        }
    }
    public int V() { return V; } 
    public int E() { return E; } 
    public final List<Edge> Adj(int v) { return Adj[v]; } 
    public ArrayList<Edge> Edges() { return Edges; }
}
