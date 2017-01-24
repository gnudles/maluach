/**
 *
 * @author orr
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
    protected static final Command c_jumpToday = new Command("עבור להיום", Command.SCREEN, 2);
    protected static final Command c_showDayTimes = new Command("הצג זמנים", Command.SCREEN, 3);
    protected static final Command c_showMeida=new Command("מידע",Command.SCREEN,1);
    //protected static final Command c_showParasha=new Command("פרשת שבוע",Command.SCREEN,0);
    protected static final Command c_showMolad=new Command("מולד הלבנה",Command.SCREEN,5);
    //protected static final Command c_showOmer=new Command("ספירת העמר",Command.SCREEN,6);
    protected DateShowers()
    {
        addCommand(c_jumpToday);
        addCommand(c_showDayTimes);
        addCommand(c_showMeida);
        //addCommand(c_showParasha);
        addCommand(c_showMolad);
        //addCommand(c_showOmer);
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
        /*if (c==c_showOmer)
        {
            showOmer();
            return true;
        }*/
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
            lstr+="ראש חדש\n";
        else
        {
            if (m_dateCursor.hd.dayInMonth()>=23 && m_dateCursor.hd.dayInWeek()==7)
                lstr+="מברכין החדש\n";
        }
        lstr+="שנה "+m_dateCursor.hd.ShmitaTitle()+"\n";
        int day_tkufa=m_dateCursor.hd.dayInTkufa();
        int day_mazal=m_dateCursor.hd.dayInMazal();
        lstr+="יום "+Integer.toString(day_tkufa+1)+" ל" +m_dateCursor.hd.TkufaName(true)+"\n";
        if (day_tkufa==0)
            lstr+="תחילת תקופה "+m_dateCursor.hd.TkufaBeginning(new TzDstManager())+"\n";
        if (day_tkufa==59 && m_dateCursor.hd.TkufaType()==YDate.JewishDate.M_ID_TISHREI)
            lstr+="מתחילים לומר תן טל ומטר בחו\"ל\n";
        if (m_dateCursor.hd.dayInYear()==36 ) // 7 in chesvan
            lstr+="מתחילים לומר תן טל ומטר בארץ ישראל\n";
        lstr+="יום "+Integer.toString(day_mazal+1)+" ל" +m_dateCursor.hd.MazalName(true)+"\n";
        if (day_tkufa!=0 && day_mazal == 0)
            lstr+="חילוף מזל "+m_dateCursor.hd.MazalBeginning(new TzDstManager())+"\n";
        lstr+="ברכת החמה בעוד "+Integer.toString((10227-m_dateCursor.hd.TkufotCycle())%10227)+" יום\n";
        if (lstr.length()>0)
            maluach.showAlert("מידע", lstr, AlertType.INFO);
    }
    protected void showMolad()
    {
        String lstr;
        /*
        // 19,17,14,11,8,6,3, those are years with 13 months
        final int D=0,H=1,P=2,PART=1080;//days,hours,parts
        final int c_month_parts=(793+PART*12+29*24*PART);
        final int c_week_parts=(7*24*PART);
        final int c_month_parts_mod=c_month_parts%c_week_parts;
        final int [] c_months_in_year=
        {
            12,12,13,12,12,13,12,13,12,12,13,12,12,13,12,12,13,12,13
        };
        final int c_first_molad=1*24*PART+5*PART+204;
        final int[][] c_cycle_offset=
        {
            {0,  0,    0},//
            {4,  8,  876},
            {1, 17,  672},
            {0, 15,  181},
            {4, 23, 1057},
            {2,  8,  853},
            {1,  6,  362},
            {5, 15,  158},
            {4, 12,  747},
            {1, 21,  543},
            {6,  6,  339},
            {5,  3,  928},
            {2, 12,  724},
            {6, 21,  520},
            {5, 19,   29},
            {3,  3,  905},
            {0, 12,  701},
            {6, 10,  210},
            {3, 19,    6},
            {2, 16,  595}
        };
        final int [][] c_month_offset=
        {
            {0,  0,    0},//
            {1, 12,  793},
            {3,  1,  506},
            {4, 14,  219},
            {6,  2, 1012},
            {0, 15,  725},
            {2,  4,  438},
            {3, 17,  151},
            {5,  5,  944},
            {6, 18,  657},
            {1,  7,  370},
            {2, 20,   83},
            {4,  8,  876},
            {5, 21,  589}
        };
        int year=m_dateCursor.hd.year()-1;//we subtract one because we want full years
        
        int cycle=year/19;
        int offset=year%19;
        int mon=m_dateCursor.hd.monthInYear();
        int parts=c_first_molad;
        parts+=(c_cycle_offset[19][D]*cycle*24*PART
                   + c_cycle_offset[19][H]*cycle*PART
                  + c_cycle_offset[19][P]*cycle)
                  % c_week_parts;
        parts+=(c_cycle_offset[offset][D]*24*PART
                   + c_cycle_offset[offset][H]*PART
                  + c_cycle_offset[offset][P])
                  ;
        parts+=(c_month_offset[mon-1][D]*24*PART
                   + c_month_offset[mon-1][H]*PART
                  + c_month_offset[mon-1][P])
                  ;
        parts%= c_week_parts;
        int days=parts/(24*PART);
        parts=parts%(24*PART);
        int hours=parts/(PART);
        parts=parts%(PART);
        
        lstr="המולד לחודש ";
        lstr+=m_dateCursor.hd.monthName(true);
        lstr+=" ";
        lstr+=Format.HebIntString(m_dateCursor.hd.year(),true);
        lstr+=" ביום ";
        lstr+=YDate.day_names[0][days];
        lstr+=" שעה ";
        lstr+=String.valueOf(hours);
        lstr+=" ו ";
        lstr+=String.valueOf(parts);
        lstr+=" חלקים";
        lstr+="\n";
        int minutes=(parts)/(1080/60);
        YDate molad=YDate.createFrom(m_dateCursor);
        
        molad.seekBy(-molad.hd.dayInMonth());
        float dst=0;
        hours=(18+hours+(int)dst)%24;
        lstr+=Format.Min2Str(hours*60+minutes);
        lstr+=" ו ";
        lstr+=String.valueOf(parts%(1080/60));
        lstr+=" חלקים";
        if (dst>0)
            lstr+=" (שעון קיץ)";
                
        
        maluach.showAlert("מולד הלבנה", lstr, AlertType.INFO);
                */
        lstr=m_dateCursor.hd.MoladString(new TzDstManager());
        maluach.showAlert("מולד הלבנה", lstr, AlertType.INFO);
    }
    protected String parasha()
    {
        String lstr=Parasha.GetParashaFor(m_dateCursor.hd);
        String shabat_str=Parasha.parshiot4(m_dateCursor);
        if (shabat_str.length()>0)
            lstr="שבת "+shabat_str+", "+lstr;
        return lstr;
    }
    protected String SfiratOmer()
    {
        int om=m_dateCursor.hd.sfiratHaomer();
        String lstr="";
        if (om>0)
        {
            lstr="היום "+Format.HebIntString(om,true) + " לעמר (מערב אתמול)";
            
        }
        om+=1;
        if (om<50 && om>0)
        {
            lstr+="\nבערב "+Format.HebIntString(om,true) + " לעמר";
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
            maluach.showAlert("שגיאה", "לא ניתן להציג זמנים עבור השנה הנבחרת", AlertType.INFO);
        }
        else
        {
            TimesForm.getInstance().OnShow(m_dateCursor);
            maluach.getInstance().PushScreen((Displayable)TimesForm.getInstance());
        }
    }
}
