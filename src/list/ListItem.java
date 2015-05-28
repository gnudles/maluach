/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package list;

/**
 *
 * @author orr
 */
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
