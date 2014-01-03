package com.charlesq.java.graph;
/* Many graph algorithms, such as PRIM Minimal Spanning Tree algorithm, etc, 
 * performs some actionis upon discovery or completion of exploring. 
 *
 * Interface Action is created to service this purpose and method  act() takes
 * only the vertex designation argumment. 
 * 
 * Concrete class implementing this interface may be passed to the algorithm 
 * class at runtime.
 * 
 */

public interface Action 
{
    void act(int v);
    
}
