/* This is free and unencumbered software released into the public domain.
 */
package maluach;


import javax.microedition.lcdui.*;
import textviewer.ScriptedTextViewer;

public class Psalms extends List implements CommandCheck,ScreenView {

    final static short [] tikun_klali={16,32,41,42,59,77,90,105,137,150};
    final static short [] week_split={1,30,51,73,90,107,120,151};
    public Psalms()
    {
        super("������",IMPLICIT);
        append("���", null);
        append("��� �����", null);
        append("��� ���", null);
        append("��� �����", null);
        append("��� �����", null);
        append("��� �����", null);
        append("��� ����", null);
        append("��� ���", null);
        /*append("��� �����", null);
        append("��� ���", null);
        append("��� �����", null);
        append("��� �����", null);
        append("��� �����", null);
        */
        append("����� �����", null);
        //append("����� �����", null);
        //append("���", null);
        //append("���� ����", null);
        //append("����� ������", null);
        append("��� ���", null);
        append("�����", null);
        
    }


    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
    }

    static private Psalms _instance;
    static public Psalms getInstance()
    {
        if (_instance == null)
        {
            _instance = new Psalms();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }

    public boolean Execute(Command c)
    {
        if (c==List.SELECT_COMMAND)
        {
            int selected=getSelectedIndex();
            if (selected==9)//select Chapter
            {
                
                maluach.getInstance().PushScreen((Displayable)new PsalmsChapter());
                return true;
            }
            else if (selected==10) //find/search
            {
                maluach.getInstance().PushScreen((Displayable)new PsalmsFind());
                return true;
            }
            
            String [] psalms_files = null;
            if (selected==0)//tikun klali
            {
                psalms_files=new String[150];
                for (int i=0;i<150;i++)
                {
                    psalms_files[i]=("/data/psalms/p"+String.valueOf(i+1));
                }
            }
            else
            {
                if (selected>=1 && selected<=7)//week days
                {
                    
                    int end=week_split[selected];
                    int i=week_split[selected-1];
                    int b=0;
                    psalms_files=new String[end-i];
                    for (;i<end;i++)
                    {
                        psalms_files[b]=("/data/psalms/p"+String.valueOf(i));
                        b++;
                    }
                }
                else
                    if (selected==8)//tikun klali
                    {
                        psalms_files=new String[tikun_klali.length+2];
                        psalms_files[0]=("/data/psalms/kishur");
                        for (int i=0;i<tikun_klali.length;i++)
                        {
                            psalms_files[i+1]=("/data/psalms/p"+String.valueOf(tikun_klali[i]));
                        }
                        psalms_files[tikun_klali.length+1]=("/data/psalms/sium");
                    }
            }
            ScriptedTextViewer.getInstance().OnShow(psalms_files);
            maluach.getInstance().PushScreen((Displayable)ScriptedTextViewer.getInstance());
            return true;
        }
        return false;
    }

    public void OnShow(Object param)
    {
    }

}
