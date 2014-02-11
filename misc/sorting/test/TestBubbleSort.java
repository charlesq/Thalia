import com.charlesq.java.sorting.BubbleSort;
class TestBubbleSort extends TestSorting
{
    public void instantiate()
    {
        sort = new BubbleSort(numbers);
        setName("Bubble Sort");
    }

} 
