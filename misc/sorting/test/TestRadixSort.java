import com.charlesq.java.sorting.RadixSort;
public class TestRadixSort extends TestSorting
{
    
    public void instantiate()
    {
        sort = new RadixSort(numbers);
        setName("Radix Sort");
    } 
}
