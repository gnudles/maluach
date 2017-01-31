/* This is free and unencumbered software released into the public domain.
 */

package list;

public class DynamicIntArray
{

    private static final int DEFAULT_CAPACITY_INCREMENT = 20;
    private static final int DEFAULT_INITIAL_CAPACITY = 50;
    private final int capacityIncrement;
    public int length = 0;
    public int[] array;

    public DynamicIntArray(int initialCapacity, int capacityIncrement)
    {
        this.capacityIncrement = capacityIncrement;
        this.array = new int[initialCapacity];
    }

    public DynamicIntArray()
    {
        this(DEFAULT_CAPACITY_INCREMENT, DEFAULT_INITIAL_CAPACITY);
    }

    public void push(int i)
    {

        if (length == array.length)
        {
            int[] old = array;
            array = new int[length + capacityIncrement];
            System.arraycopy(old, 0, array, 0, length);
        }
        array[length++] = i;

    }

    public void removeElementAt(int offset)
    {
        if (offset >= length)
        {
            throw new ArrayIndexOutOfBoundsException("offset too big");
        }

        if (offset < length)
        {
            System.arraycopy(array, offset + 1, array, offset, length - offset - 1);
            length--;
        }
    }

    public int At(int offset)
    {
        if (offset >= length || offset < 0)
        {
            throw new ArrayIndexOutOfBoundsException("offset too big");
        }
        return array[offset];
    }
    public int pop()
    {
        length--;
        return array[length];
    }
}
