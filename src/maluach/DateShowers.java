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
import maluach.YDate.JewishDate;

public abstract class DateShowers extends Canvas implements CommandCheck
{
    protected static final Command c_jumpToday = new Command("���� �����", Command.SCREEN, 2);
    protected static final Command c_showDayTimes = new Command("��� �����", Command.SCREEN, 3);
    protected static final Command c_showMeida=new Command("����",Command.SCREEN,1);
    protected static final Command c_showYearMeida=new Command("������ ���",Command.SCREEN,2);
    protected static final Command c_showLimud=new Command("����� ����",Command.SCREEN,3);
    protected static final Command c_showMolad=new Command("���� �����",Command.SCREEN,5);

    protected DateShowers()
    {
        addCommand(c_jumpToday);
        addCommand(c_showDayTimes);
        addCommand(c_showMeida);
        addCommand(c_showYearMeida);
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
        if (c==c_showYearMeida)
        {
            showYearInformation();
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
        if ((m_dateCursor.hd.dayInMonth()==1 && m_dateCursor.hd.dayInYear()!=0) || m_dateCursor.hd.dayInMonth()==30)
            lstr+="��� ���\n";
        else
        {
            if (m_dateCursor.hd.dayInMonth()>=23 && m_dateCursor.hd.dayInWeek()==7)
                lstr+="������ ����\n";
        }
        if (m_dateCursor.hd.dayInMonth()==16 && m_dateCursor.hd.monthID()==JewishDate.M_ID_TISHREI &&
            (m_dateCursor.hd.ShmitaOrdinal()-1)%7 == 0 )
        {
            lstr+="��� ����\n";
        }
        if (TorahReading.getShabbatBereshit(m_dateCursor.hd.yearLength(),m_dateCursor.hd.yearFirstDay())+15*7-4 == m_dateCursor.hd.daysSinceBeginning())
        {
            lstr+="����� ���� ��� ����� ���� ���� �����\n";
        }
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
        if (m_dateCursor.hd.monthID()==JewishDate.M_ID_NISAN)
        {
            lstr+="���� �������\n";
            if (m_dateCursor.hd.dayInMonth()<=13)
            {
                lstr+="������ �������\n";
            }
        }
        if (lstr.length()>0)
            maluach.showAlert("����", lstr, AlertType.INFO);
    }
    protected void showYearInformation()
    {   
        int year=m_dateCursor.hd.year();
        String lstr=Format.HebIntString(year, true);
        lstr+="\n��� "+m_dateCursor.hd.ShmitaTitle();
        int year_first_day=m_dateCursor.hd.yearFirstDay();
        int year_length=m_dateCursor.hd.yearLength();
        int year28=(year-4117)%28;
        lstr+="\n��� "+String.valueOf(year28+1)+" ������ �\"� ����";
        if(year28==0)
        {
            lstr+="\n���� ���� ����";
            int day_in_year=(10227-JewishDate.TkufotCycle(year_first_day)%10227);
            JewishDate hama=new JewishDate(year_first_day+
                                           day_in_year);
            lstr+=" �"+Format.HebIntString(hama.dayInMonth(), false)+" "+hama.monthName(true);
            
        }
        int nineteen_cycle=(year-1)/19 + 1;
        int nineteen_y=(year-1)%19 + 1;
        lstr+="\n��� "+Format.HebIntString(nineteen_y, false)+" ������ "+Format.HebIntString(nineteen_cycle, false);
        
        lstr+="\n���� ����� "+String.valueOf(m_dateCursor.hd.NumberOfShabbats());
        lstr+="\n���� ���� ���� "+String.valueOf(year_length);
        String yearsign=m_dateCursor.hd.yearSign();
        lstr+="\n���� ��� "+yearsign;
        int yeziat_mizraim=year-JewishDate.YEZIAT_MIZRAIM;
        int horban_bait_rishon=year-JewishDate.HORBAN_BAIT_RISHON;
        int minian_shtarot=year-JewishDate.MINIAN_SHTAROT;
        int horban_bait_sheni=year-JewishDate.HORBAN_BAIT_SHENI;
        lstr+="\n"+yeziat_mizraim+" ("+Format.HebIntString(yeziat_mizraim, true)+") ������ ����� ����� ����";
        lstr+="\n"+horban_bait_rishon+" ("+Format.HebIntString(horban_bait_rishon, true)+") ������ ��� �����";
        lstr+="\n"+horban_bait_sheni+" ("+Format.HebIntString(horban_bait_sheni, true)+") ������ ��� ���";
        lstr+="\n"+minian_shtarot+" ("+Format.HebIntString(minian_shtarot, true)+") ����� �����";
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
        String lstr;
        lstr="�� ���� "+DailyLimud.getDafYomi(m_dateCursor.hd.daysSinceBeginning(), true);
        lstr+="\n���� ����� "+DailyLimud.MishnaYomit(m_dateCursor.hd.daysSinceBeginning(),true, true);
        maluach.showAlert("����� ����", lstr, AlertType.INFO);
    }
    protected String parasha()
    {
        String il_parasha=TorahReading.GetSidra(m_dateCursor.hd,false,false);
        String diasp=TorahReading.GetSidra(m_dateCursor.hd,true,false);
        
        String lstr="";
        if ((diasp.length()==0 || !il_parasha.equals(diasp))&& il_parasha.length()>0)
            lstr+="���\"� ";
        lstr+=il_parasha;
        String shabat_str=TorahReading.parshiot4(m_dateCursor);
        if (shabat_str.length()>0)
            lstr="��� "+shabat_str+", "+lstr;
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
            lstr="���� "+Format.HebIntString(om,false) + " ���� (���� �����)";
            
        }
        om+=1;
        if (om<50 && om>0)
        {
            lstr+="\n���� "+Format.HebIntString(om,false) + " ����";
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
