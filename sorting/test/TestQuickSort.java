import com.charlesq.java.sorting.QuickSort;
public class TestQuickSort extends TestSorting
{
    
    public void instantiate()
    {
        sort = new QuickSort(numbers);
        setName("Quick Sort");
    } 
}
