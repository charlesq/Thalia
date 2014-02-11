import com.charlesq.java.sorting.HeapSort;
public class TestHeapSort extends TestSorting
{
    
    public void instantiate()
    {
        sort = new HeapSort(numbers);
        setName("Heap Sort");
    } 
}
