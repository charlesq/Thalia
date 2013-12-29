import com.charlesq.java.sorting.InsertSort;
public class TestInsertSort extends TestSorting
{
    public void instantiate()
    {
        sort = new InsertSort(numbers);
        setName("Insert Sort");
    }
} 
