import com.charlesq.java.sorting.MergeSort;
class TestMergeSort extends TestSorting
{
    public void instantiate()
    {
        sort = new MergeSort(numbers);
        setName("Merge Sort");
    }
}
