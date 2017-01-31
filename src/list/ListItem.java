/* This is free and unencumbered software released into the public domain.
 */

package list;

public class ListItem {
    private Object data;
    private ListItem next;
    private ListItem prev;

    public ListItem(Object o)
    {
        data=o;
    }
    public void setNext(ListItem li)
    {
        li.prev=this;
        next=li;
    }
    public ListItem getNext()
    {
        return next;
    }
    public ListItem getPrevious()
    {
        return prev;
    }
    public Object getItem()
    {
        return data;
    }

}
