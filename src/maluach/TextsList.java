/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import javax.microedition.lcdui.*;
import textviewer.ScriptedTextViewer;
/**
 *
 * @author orr
 */
public class TextsList extends List implements ScreenView,CommandCheck
{
    public TextsList()
    {
    	super("�����",IMPLICIT);
        textCommands=new ScreenCommand[10];
        textCommands[append("�����", null)]=new ScreenCommand(Psalms.getInstance(), null ,"",0);
        textCommands[append("���� ����� - ����", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/mazon" ,"", 0);
        textCommands[append("���� ����� - �����", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/mazon_ash" ,"", 0);
        textCommands[append("����� ���� - ����", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/shahar" ,"", 0);
        textCommands[append("��� ������ �����", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/korbanot" ,"", 0);
        textCommands[append("����� ��� ��� �����", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/kriat" ,"", 0);
        textCommands[append("����� �����", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/tfilat_amida" ,"", 0);
        textCommands[append("��� ������", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/shirhashirim" ,"", 0);
        textCommands[append("���� ������", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/perekshira" ,"", 0);
        textCommands[append("���� ���", null)]=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/man_beshalach" ,"", 0);
    }
    public void OnShow(Object param)
    {

    }
    final private ScreenCommand textCommands[];

    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
    }
    public boolean Execute(Command c)
    {
        if (c==List.SELECT_COMMAND)
        {
            int i = getSelectedIndex();
            textCommands[i].showScreen();
            return true;
        }
        return false;
    }
    static private TextsList _instance;
    static public TextsList getInstance()
    {
        if (_instance == null)
        {
            _instance = new TextsList();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }
}
