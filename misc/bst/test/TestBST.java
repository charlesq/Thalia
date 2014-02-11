import com.charlesq.java.bst.*;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import junit.framework.*;
import org.hamcrest.core.IsEqual;
import java.util.Random;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Ignore;
import java.util.*;
public class TestBST
{
    int [] numbers;    
    BST bst;
    public static int [] generateArray(int sz)
    {
       int [] nums = new int [sz--]; 
       for (; sz >=0; --sz)
           nums[sz] = sz;
       fisher_yates_shuffle(nums);
       return nums;
    }
    /* made this method static and public, so other program can invoke it */
    public static  void fisher_yates_shuffle(int [] num)
    {
       Random rand = new Random(); 
       for (int i = 1; i < num.length; i ++)
       {
           int j = rand.nextInt(Integer.MAX_VALUE)% (i + 1);
           swap(num, i ,j );
       } 
    }
    public void testSetup()
    {
        int sz = 20;
        System.out.println();
        numbers = generateArray(sz);
    }
    /* swap elements indexed as i and j in the array */
    public static void swap(int [] num, int i, int j)
    {
        if (i == j)
           return;
        int agent = num[i];
        num[i] = num[j];
        num[j] = agent;
    }
    @Before
    public void testBuildBST()
    {
      testSetup();
      if (numbers == null)
          Assert.assertTrue("failed to generate array", false);
      bst = new BST();
      for (int e: numbers)
          bst.add(e);  
      if (bst.size() != numbers.length)
          Assert.assertTrue("bst size is not correct", false);
    }
    @Test
    public void testIsBST()
    {
        Assert.assertTrue("bst tree is not valid", bst.isBST());
    }
    @Test
    public void testInOrderTraversal()
    {
        List<Integer> list = bst.inOrderTraversal();
        int i = 0;
        for (Integer e: list)
               Assert.assertTrue("failed in order traversal", e == i ++); 
    }
    public void printList(List <Integer> l)
    {
       for (Integer i: l)
           System.out.print(i + " ");
       System.out.println();
    }
    @Test
    public void testPreOrderTraversal()
    {
      // bst.preOrderTraversal();
       
        for (int e: numbers)
        {
            bst.remove(e);
            List<Integer> list = bst.preOrderTraversal();
            for (Integer i: list)
            {
                if (i == e)
                    Assert.assertTrue("preOrderTraversal error", false);
            }
        }
    }
    @Test
    public void testPostOrderTraversal()
    {
        for (int e: numbers)
        {
             bst.remove(e);
             List<Integer> list = bst.postOrderTraversal();
             for (Integer i: list)
             {
                 if (i == e)
                     Assert.assertTrue("postOrderTraversal error", false);
             }
         }

    }
    @Test
    public void testRemove()
    {
       List<Integer> list;
       for (int e: numbers)
       {
           bst.remove(e);
           Assert.assertTrue("BST size not correct after deletion", bst.size() == numbers.length -1);
           list = bst.inOrderTraversal(); 
           for (Integer l: list)
              Assert.assertTrue("delete a wrong key", l != e); 
           bst.add(e);
       }
   }
}
