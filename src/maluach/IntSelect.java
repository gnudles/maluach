/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;


/**
 *
 * @author orr
 */
public class IntSelect extends Form implements ScreenView,DisplaySelect
{
    public static interface ValueListen
    {
        public void ValueChanged(int value);
    }
    public static final class IntValue
    {
        int m_value;
        ValueListen m_vl;

        public IntValue(ValueListen vl)
        {
            m_value=0;
            m_vl=vl;
        }
        public IntValue(int new_value,ValueListen vl)
        {
            m_value=new_value;
            m_vl=vl;
        }
        void setValue(int new_value)
        {
            m_value=new_value;
            if (m_vl!=null)
                m_vl.ValueChanged(m_value);
        }
        int value()
        {
            return m_value;
        }
        
    }
    private TextField tf_int;
    IntValue pval;
    static int m_value;
    static boolean m_selected;
    public IntSelect(String title,int maxSize,IntValue val)
    {
        super(title);
        InitCommands();
        setCommandListener(maluach.getInstance());
        String vstr="";
        if (val!=null)
            vstr=Integer.toString(val.value());
        tf_int = new TextField("", vstr, maxSize, TextField.NUMERIC);
        tf_int.setLayout(TextField.LAYOUT_RIGHT);
        super.append(tf_int);
        pval=val;

    }

    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
        addCommand(CommandPool.getC_select());
    }

    public void Select()
    {
        if (tf_int.getString().length()>0)
        {
            m_value=Integer.parseInt(tf_int.getString());
            if (pval!=null)
                pval.setValue(m_value);
            m_selected=true;
        }
    }
    public static boolean selected()
    {
        return m_selected;
    }
    public static int value()
    {
        return m_value;
    }

    public void OnShow(Object param)
    {
        m_selected=false;
    }

    
}
