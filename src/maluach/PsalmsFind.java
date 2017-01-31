/* This is free and unencumbered software released into the public domain.
 */

package maluach;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.*;
import list.LinkedList;
import list.ListItem;
import textviewer.ScriptedTextViewer;

public class PsalmsFind extends Form implements CommandCheck
{
    private TextField tfkeyword;
    public PsalmsFind()
    {
        super("חיפוש");
        InitCommands();
        setCommandListener(maluach.getInstance());
        tfkeyword = new TextField("משפט לחיפוש", "", 30, TextField.ANY);
        tfkeyword.setLayout(TextField.LAYOUT_RIGHT);
        tfkeyword.setInitialInputMode("UCB_HEBREW");
        super.append(tfkeyword);
    }
    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
        addCommand(new Command("חפש", Command.SCREEN, 1));
    }
    public boolean Execute(Command c)
    {
        String str=tfkeyword.getString();
        if (str.length()==0)
            return false;
        char [] text=str.toCharArray();
        byte [] keywords=new byte[text.length];
        for (int i=0;i<text.length;i++)
        {
            if(text[i] < 256)
            {
                keywords[i]=(byte)text[i];
            }
            else
            {
                keywords[i]=(byte)(text[i]-1488);
                keywords[i]+=224;
            }
        }
        byte [] textBuffer;
        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] aoBuffer = new byte[512];
        int slength;
        LinkedList chapterslist = new LinkedList();
        String chapterstr;
        int num_chaps=0;
        for (int chap=1;chap<=150;chap++)
        {
            chapterstr="/data/psalms/p"+String.valueOf(chap);
            in = getClass().getResourceAsStream(chapterstr);
            
            out.reset();
            int nBytesRead;
            try
            {
                while ((nBytesRead = in.read(aoBuffer)) > 0)
                {
                    out.write(aoBuffer, 0, nBytesRead);
                }
                in.close();
            }
            catch (IOException ex)
            {
                return false;
            }
            textBuffer = out.toByteArray();
            byte[] senitized=new byte[ textBuffer.length];
            int senisize=0;
            for (int i=0;i<textBuffer.length;i++)
            {
                if (textBuffer[i]=='(')
                {
                    do
                    {
                        i++;
                    }
                    while (textBuffer[i]!=' ');
                    i++;
                }
                if(!(textBuffer[i]>=-80 && textBuffer[i]<-32))//not nikud
                {
                    senitized[senisize]=textBuffer[i];
                    ++senisize;
                }
            }
            slength=senisize-keywords.length+1;
            boolean found;
            for (int v=0;v<slength;v++)
            {
                found=true;
                for (int u=0;u<keywords.length;u++)
                {
                    if (senitized[u+v]!=keywords[u])
                    {
                        found=false;
                        break;
                    }
                }
                if (found)
                {
                    chapterslist.addItem(chapterstr);
                    num_chaps++;
                    break;
                }
            }
        }
        if (num_chaps==0)
        {
            maluach.showAlert("תם החיפוש", "לא נמצאו תוצאות", AlertType.INFO);
            return false;
        }
        String [] found_chapters=new String[num_chaps];
        ListItem cur_chap_item=chapterslist.getFirst();
        for (int i=0;i<num_chaps;i++)
        {
            found_chapters[i]=(String)cur_chap_item.getItem();
            cur_chap_item=cur_chap_item.getNext();
        }
        ScriptedTextViewer.getInstance().OnShow(found_chapters);
        maluach.getInstance().PushScreen((Displayable)ScriptedTextViewer.getInstance());
        return true;
    }
    
}
