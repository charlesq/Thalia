package com.charlesq.java.bst;
import java.util.*;
import java.lang.*;


/* BST is a simple binary search tree class ; at each node, left/right child 
   and parent node information are preserved. 
   
   Besides basic operation operations to add and remove a key, find 
   successor/precessor node, more complex operations include in-order
   /pre-order/post-order traversals, binary search tree 
   property verifying,  using  iteration rather than recursion.
   Note that recursion approach is less hard.

   As no field in a node tracks  depth/level information and no node rotate methods
   are definded, there is no guarantee that a balanced tree will result by adding and
   removing keys. 
*/
public class BST
{
    private Node root;
    private int size;
    public BST()
    {
    }
    /* return number of keys in the binary search 
       tree
     */ 
    public int size()
    {
        return size;
    }
    /*  Add a new key to the tree.
     *  @param v, the key to insert
     *  @return true if tree structure is modified
     *          false otherwise
     */
    public boolean add(int v)
    {
        Node nd = new Node (null, v);
        Node n = root;
        while(n!= null)
        {
            if (n.compareTo(nd) == 0)
                return false;
            if (n.compareTo(nd) > 0)
            {
                 if (n.left == null)
                 {
                     n.left = nd;
                     nd.parent = n;
                     ++size;
                     return true;
                 }
                 n = n.left;
                 continue;
            }
            if (n.right == null)
            {
                n.right = nd;
                nd.parent = n;
                ++size;
                return true;
            } 
            n = n.right;
        }
        root = nd;
        ++size;
        return true;
    }
   /*
    Identify n's successor in the tree
    @param: n is the node to work on    
    @return: null if n has no successor in
             the tree, or the succeeding node.
    */
    Node successor(Node n)
    {
       if (n == null)
           return null;
       if (n.right != null)
           n = n.right;  
       else
       {
           while (n != null)
           {
               if (n.parent == null)
                   return null;
               if (n.parent.right != n)
               {
                   n = n.parent;
                   break;
               }
               n = n.parent;
           }
       }
       if (n == null)
           return null;
       while(n.left != null)
           n = n.left;
       return n;
    }
    Node predecessor(Node n)
    {
        if (n == null)
            return n;
        if (n.left != null)
            n = n.left;
        else
        {
            while(n != null)
            {
                if (n.parent == null)
                    return null;
                if (n.parent.left != n)
                {
                    n = n.parent;
                    break;
                }
                n = n.parent;
            }
        }
        if (n == null)
            return null;
        while(n.right != null)
           n = n.right;
        return n;
    }
    /* Delete a node with the specified key value.
     * @param v the key value
     * @return true is tree structure is modified
     *         false otherwise.
     */
    public boolean remove (int v)
    {
        Node nd = root;
        Node parent, succ;
        /* search the tree for the node with the key */
        while(nd != null)
        {
             if (nd.value == v)
                 break; 
             parent = nd;    
             if (nd.value < v)
                 nd = nd.right;
             else
                 nd = nd.left;
        }
        /* case 1:if not found, do nothing and return false*/
        if (nd == null)
            return false;
        --size;
        /* case 2: nd either a leaf or has only one child */
        if (nd.left == null || nd.right == null)
        {
            /* case 2.1 if nd is root, make the child as new root */
            if (nd == root)
            {
               if (nd.left != null)
               {
                  root = nd.left; 
                  root.parent = null;
                  return true;
               }
               if (nd.right != null)
               {
                  root = root.right; 
                  root.parent = null;
                  return true;
               }
               root = null;
               return true;
            } 
            /* case 2.2: nd is a leaf node, substitute it with the child */
            Node c = nd.left != null? nd.left: nd.right;
            if (nd.parent.left == nd)
            {
                  nd.parent.left = c;
                  if (c != null)
                      c.parent = nd.parent;
            }
            else
            {
                 nd.parent.right = c ; 
                 if (c != null)
                     c.parent = nd.parent;
            } 
            return true;
        }
        /* case 3: nd has two children */
        /* sucessor can not be null and has at most a right child */
        succ = successor(nd);
      
        /* case 3 step one: take care of succ's parent and decendents */
        if (succ.parent.left == succ)
        {
            succ.parent.left = succ.right; 
            if (succ.right != null)
               succ.right.parent = succ.parent;
        }
        else
        {
            succ.parent.right = succ.right;
            if (succ.right != null)
                succ.right.parent = succ.parent;
        }
        /* case 3 step two: set succ's parent to nd's parent */ 
        succ.parent = nd.parent;
        /* case 3 step three:  fix nd's parent's new child  */
        if (nd == root)
        {
            root = succ;
        }
        else
        {
            if (nd.parent.left == nd)
                nd.parent.left = succ;
            else
                nd.parent.right = succ;
        }
        /* case 3 step four:  set succ's new children */
        succ.left = nd.left;
        succ.right = nd.right; 
        /* case 3 step five: set nd'children's new parent  */
        if (nd.left != null)
            nd.left.parent = succ;
        if (nd.right != null)
            nd.right.parent = succ;
        return true;
    }
    /* Worker method for isBST, this is a recursive procedure.
     * @param: n is the node to check  
     * @param: ceiling should be bigger than or equal to all keys in
               subtree rooted at n
     *         if n is non null.
     * @param: floor shoube be less than all keys in subtree rooted 
               at n; 
       @return false if the afore-mentioned conditions are not meet
               true otherwise.
     */
    boolean validateBST(Node  n, int ceiling, int floor)
    {
        if (n == null)
            return true; 
        if (n.value < floor || n.value > ceiling)
            return false;
        return validateBST(n.left, n.value, floor) && 
               validateBST(n.right, ceiling, n.value);
    }
    /* verify the tree is a valid BST 
     * @param: 
       @return: true if the tree is a valid binary search tree 
                false if key at any node violate the rule
     */ 
    public void print(Node n)
    {
        if (n == null)
            return;
        System.out.print("( " + "value: " + n.value + " ");
        if (n.left != null)
            System.out.print(", left: " + n.left.value);
        if (n.right != null)
            System.out.print(", right:" + n.right.value);
        if (n.parent != null)
        {
            System.out.print(", parent: ");
            System.out.print( n.parent.value);
        }
        System.out.println(" )");
    }
    public boolean isBST()
    {
        return validateBST(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }
    public final List <Integer> inOrderTraversal()
    {
        List<Integer>  list = new ArrayList<Integer>(size);
        Node n = root;
        boolean  visitedLeft = false; /* a flag used to decide whether to go uppper */
        
        while(n != null)
        {
           if (n.left == null || visitedLeft)
           {
               /*case 1 : n has no left child or was visited, visit its key */
               list.add(n.value); 
               visitedLeft = false;
               /* case 1.1 consider n's right child, go for it */
               if (n.right != null)
               {
                   n = n.right;
                   continue;
               }
               /* case 1.2 n's right is null, check parent */
               if (n.parent != null)
               {
                    /* case 1.2.2: recursively go  up until current node is not parent's right child
                       similar to case 1.2.1
                    */ 
                    while (n == n.parent.right)
                    {
                        n = n.parent;
                        /* if n's parent is null, returned to root, 
                           traversal is over */
                        if (n.parent == null)
                           break; 
                    }
                    visitedLeft = true; 
                    n = n.parent;
                    continue; 
               }
               /*case 1: n is root, break*/
               n = n.parent;
               continue;
           }
           /* case 2: n has unvisited left child , continue to dive down*/ 
           n = n.left; 
        }
        return list;
    } 
    public List<Integer>  preOrderTraversal()
    {
        boolean  visited = false, childVisited = false;
        List<Integer> list = new ArrayList<Integer>(size());
        Node n = root;
        while(n != null)
        {
            if (visited)
            {
                visited = false;
                if (n.right != null && !childVisited)
                {
                    n = n.right;
                    continue;
                }
                childVisited = false;
                /* if n's parent is null, traversal is complete */
                if (n.parent == null)
                    return list;
                /* @ if n is parent's right child, go to grand parent */
                if (n == n.parent.right)
                {
                    n = n.parent.parent;
                    childVisited = true;
                    visited = true;
                    continue;
                }
                n = n.parent;
                visited = true;
                continue;
            }
            list.add(n.value); 
            if (n.left != null)
            {
                n = n.left;
                continue;
            }
            visited = true; 
        }
        return list;
    }
    public List<Integer> postOrderTraversal()
    {
        List<Integer> list = new ArrayList<Integer> (size());
        boolean visitedRight = false, visitedLeft = false;  
        Node n = root;
        while(n != null)
        {
            if (visitedLeft || n.left == null)
            {
                if (visitedRight || n.right == null)
                {
                   visitedRight = false;
                   visitedLeft = false;
                   list.add(n.value);
                   if (n.parent == null)
                       return list; 
                   if (n.parent.left == n)
                   {
                       visitedLeft = true;
                       if (n.parent.right != null)
                       {
                           n = n.parent.right;
                           continue;
                       }
                       n = n.parent; 
                       continue;
                   }
                   n = n.parent; 
                   visitedLeft = true;
                   visitedRight = true; 
                   continue; 
                }
                n = n.right; 
                continue;
            }
            n = n.left;
        }
        return list;
    }
    
} 

