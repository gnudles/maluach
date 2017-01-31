/* This is free and unencumbered software released into the public domain.
 */
package maluach;


import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.pim.*;

public abstract class DateShowers extends Canvas implements CommandCheck
{
    protected static final Command c_jumpToday = new Command("���� �����", Command.SCREEN, 2);
    protected static final Command c_showDayTimes = new Command("��� �����", Command.SCREEN, 3);
    protected static final Command c_showMeida=new Command("����",Command.SCREEN,1);
    protected static final Command c_showLimud=new Command("����� ����",Command.SCREEN,3);
    protected static final Command c_showMolad=new Command("���� �����",Command.SCREEN,5);

    protected DateShowers()
    {
        addCommand(c_jumpToday);
        addCommand(c_showDayTimes);
        addCommand(c_showMeida);
        addCommand(c_showLimud);
        addCommand(c_showMolad);
    }

    protected static YDate s_dateToday = YDate.getNow();
    protected YDate m_dateCursor;
    public static YDate getToday()
    {
        return s_dateToday;
    }
    final public YDate getCursor()
    {
        return m_dateCursor;
    }

    public boolean Execute(Command c)
    {
        if (c == c_jumpToday)
        {
            jumpToday();
            return true;
        }
        if (c == c_showDayTimes)
        {
            ShowTime();
            return true;
        }
        /*if (c==c_showParasha)
        {
            showParasha();
            return true;
        }*/
        if (c==c_showMeida)
        {
            showInformation();
            return true;
        }
        if (c==c_showMolad)
        {
            showMolad();
            return true;
        }
        if (c==c_showLimud)
        {
            showLimud();
            return true;
        }
        return false;
    }
    protected void showInformation()
    {
        String lstr=parasha();
        if (lstr.length()>0)
        {
            
            lstr+="\n";
        }
        String omer=SfiratOmer();
        lstr+=omer;
        if (omer.length()>0)
            lstr+="\n";
        if (m_dateCursor.hd.dayInMonth()==1 || m_dateCursor.hd.dayInMonth()==30)
            lstr+="��� ���\n";
        else
        {
            if (m_dateCursor.hd.dayInMonth()>=23 && m_dateCursor.hd.dayInWeek()==7)
                lstr+="������ ����\n";
        }
        lstr+="��� "+m_dateCursor.hd.ShmitaTitle()+"\n";
        int day_tkufa=m_dateCursor.hd.dayInTkufa();
        int day_mazal=m_dateCursor.hd.dayInMazal();
        lstr+="��� "+Integer.toString(day_tkufa+1)+" �" +m_dateCursor.hd.TkufaName(true)+"\n";
        if (day_tkufa==0)
            lstr+="����� ����� "+m_dateCursor.hd.TkufaBeginning(new TzDstManager())+"\n";
        if (day_tkufa==59 && m_dateCursor.hd.TkufaType()==YDate.JewishDate.M_ID_TISHREI)
            lstr+="������� ���� �� �� ���� ���\"�\n";
        if (m_dateCursor.hd.dayInYear()==36 ) // 7 in chesvan
            lstr+="������� ���� �� �� ���� ���� �����\n";
        lstr+="��� "+Integer.toString(day_mazal+1)+" �" +m_dateCursor.hd.MazalName(true)+"\n";
        if (day_tkufa!=0 && day_mazal == 0)
            lstr+="����� ��� "+m_dateCursor.hd.MazalBeginning(new TzDstManager())+"\n";
        lstr+="���� ���� ���� "+Integer.toString((10227-m_dateCursor.hd.TkufotCycle())%10227)+" ���\n";
        if (lstr.length()>0)
            maluach.showAlert("����", lstr, AlertType.INFO);
    }
    protected void showMolad()
    {
        String lstr;
        lstr=m_dateCursor.hd.MoladString(new TzDstManager());
        maluach.showAlert("���� �����", lstr, AlertType.INFO);
    }
    
    protected void showLimud()
    {
        maluach.showAlert("�� ����", DailyLimud.getDafYomi(m_dateCursor.hd.daysSinceBeginning(), true), AlertType.INFO);
    }
    protected String parasha()
    {
        String il_parasha=Parasha.GetParashaFor(m_dateCursor.hd,false);
        String lstr=il_parasha;
        String shabat_str=Parasha.parshiot4(m_dateCursor);
        if (shabat_str.length()>0)
            lstr="��� "+shabat_str+", "+lstr;
        String diasp=Parasha.GetParashaFor(m_dateCursor.hd,true);
        if (diasp.length()>0 && !il_parasha.equals(diasp))
            lstr+="\n���\"� "+diasp;
        return lstr;
    }
    protected String SfiratOmer()
    {
        int om=m_dateCursor.hd.sfiratHaomer();
        String lstr="";
        if (om>0)
        {
            lstr="���� "+Format.HebIntString(om,true) + " ���� (���� �����)";
            
        }
        om+=1;
        if (om<50 && om>0)
        {
            lstr+="\n���� "+Format.HebIntString(om,true) + " ����";
        }
        return lstr;
    }
    final public void jumpToday()
    {
        jumpTo(s_dateToday);
    }
    abstract public void jumpTo(YDate date);
    public void ShowTime()
    {
        int y=m_dateCursor.gd.year();
        if (y<1900 || y >= 2100)
        {
            maluach.showAlert("�����", "�� ���� ����� ����� ���� ���� ������", AlertType.INFO);
        }
        else
        {
            TimesForm.getInstance().OnShow(m_dateCursor);
            maluach.getInstance().PushScreen((Displayable)TimesForm.getInstance());
        }
    }
}
