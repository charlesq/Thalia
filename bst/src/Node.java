package com.charlesq.java.bst;
import java.lang.*;
public class Node implements Comparable<Node>
{
    Node parent;
    Node left;
    Node right; 
    int value;
   
    public Node (Node parent,int value)
    {
        this.parent = parent;
        this.value = value; 
    } 
    public Node (int value)
    {
        this.value = value;
    }
    public int compareTo(Node another)
    {
        return this.value - another.value;
    }
}
