/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class IntSelect extends Form implements CommandCheck,ScreenView
{
    public final class IntValue
    {
        int value;

        public IntValue()
        {
            value=0;
        }
        public IntValue(int new_value)
        {
            value=new_value;
        }
        void setValue(int new_value)
        {
            value=new_value;
        }
        int value()
        {
            return value;
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
        addCommand(new Command("בחר", Command.SCREEN, 1));
    }

    public boolean Execute(Command c)
    {
        m_value=Integer.parseInt(tf_int.getString());
        if (pval!=null)
            pval.setValue(m_value);
        m_selected=true;
        return true;
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
