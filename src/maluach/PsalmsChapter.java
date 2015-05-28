/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import textviewer.ScriptedTextViewer;

/**
 *
 * @author orr
 */
public class PsalmsChapter extends Form implements CommandCheck
{

    private TextField tfchapter;

    public PsalmsChapter()
    {
        super("בחר פרק");
        InitCommands();
        setCommandListener(maluach.getInstance());
        tfchapter = new TextField("", "", 3, TextField.NUMERIC);
        tfchapter.setLayout(TextField.LAYOUT_RIGHT);
        super.append(tfchapter);
    }

    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
        addCommand(new Command("בחר", Command.SCREEN, 1));
    }

    public boolean Execute(Command c)
    {
        String[] psalms_files = new String[150];
        for (int i = 0; i < 150; i++)
        {
            psalms_files[i] = ("/data/psalms/p" + String.valueOf(i + 1));
        }
        int index=Integer.parseInt(tfchapter.getString())-1;
        
        if (index>149)
            index=149;
        else if (index<0)
            index=0;
            
        ScriptedTextViewer.getInstance().LoadMany(psalms_files,index);
        maluach.getInstance().PushScreen((Displayable)ScriptedTextViewer.getInstance());
        return true;
    }

    

}
