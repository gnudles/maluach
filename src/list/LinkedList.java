/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package list;

/**
 *
 * @author orr
 */
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
