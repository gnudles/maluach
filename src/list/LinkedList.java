/* This is free and unencumbered software released into the public domain.
 */

package list;


public class LinkedList {
    private ListItem first;
    private ListItem last;

    public void addItem(Object o)
    {
        if(last==null)//list is empty
        {
            first=new ListItem(o);
            last=first;
        }
        else
        {
            last.setNext(new ListItem(o));
            last=last.getNext();
        }
    }
    public ListItem getFirst()
    {
        return first;
    }
    public ListItem getLast()
    {
        return last;
    }
}
