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
    protected static final Command c_jumpToday = new Command("���� �����", Command.SCREEN, 2);
    protected static final Command c_showDayTimes = new Command("��� �����", Command.SCREEN, 3);
    protected static final Command c_showParasha=new Command("���� ����",Command.SCREEN,0);
    protected static final Command c_showMolad=new Command("���� �����",Command.SCREEN,5);
    protected static final Command c_showOmer=new Command("����� ����",Command.SCREEN,6);
    protected DateShowers()
    {
        addCommand(c_jumpToday);
        addCommand(c_showDayTimes);
        addCommand(c_showParasha);
        addCommand(c_showMolad);
        addCommand(c_showOmer);
    }
    protected static Hdate s_dateToday = new Hdate();
    protected Hdate m_dateCursor;
    public static Hdate getToday()
    {
        return s_dateToday;
    }
    final public Hdate getCursor()
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
        if (c==c_showParasha)
        {
            showParasha();
            return true;
        }
        if (c==c_showMolad)
        {
            showMolad();
            return true;
        }
        if (c==c_showOmer)
        {
            showOmer();
            return true;
        }
        return false;
    }
    protected void showMolad()
    {
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
            //{5, 21,  589}
        };
        int year=m_dateCursor.get_hd_year()-1;//we subtract one because we want full years
        
        int cycle=year/19;
        int offset=year%19;
        int mon=m_dateCursor.get_hd_month();//1..14 (1 - tishre
        // 2-heshvan,3-kislev,4-tevet,5-shvat,6-adar..., 13 - adar 1, 14 - adar 2).
        if (c_months_in_year[offset]==13)
        {
            if (mon>=13)
                mon-=(13-6);
            else if(mon>=6)
                mon++;
        }
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
        
        String lstr="����� ����� ";
        lstr+=m_dateCursor.get_hd_month_name();
        lstr+=" ";
        lstr+=Hdate.get_int_string(m_dateCursor.get_hd_year(),true);
        lstr+=" ���� ";
        lstr+=Hdate.day_names[days];
        lstr+=" ��� ";
        lstr+=String.valueOf(hours);
        lstr+=" � ";
        lstr+=String.valueOf(parts);
        lstr+=" �����";
        lstr+="\n";
        int minutes=(parts)/(1080/60);
        hours=(18+hours)%24;
        lstr+=SunCalc.Min2Str(hours*60+minutes);
        lstr+=" � ";
        lstr+=String.valueOf(parts%(1080/60));
        lstr+=" �����";
        maluach.showAlert("���� �����", lstr, AlertType.INFO);
    }
    protected void showParasha()
    {
        String lstr=Parasha.GetParashaFor(m_dateCursor);
        String shabat_str=Parasha.parshiot4(m_dateCursor);
        if (shabat_str.length()>0)
            lstr="��� "+shabat_str+", "+lstr;
        if (lstr.length()>0)
            maluach.showAlert("����", lstr, AlertType.INFO);
    }
    protected void showOmer()
    {
        int om=m_dateCursor.get_sfirat_haomer();
        String lstr="";
        if (om>0)
        {
            lstr="���� "+Hdate.get_int_string(om,true) + " ���� (���� �����)";
            
        }
        om+=1;
        if (om<50 && om>0)
        {
            lstr+="\n���� "+Hdate.get_int_string(om,true) + " ����";
        }
        if (lstr.length()>0)
                maluach.showAlert("����� ����", lstr, AlertType.INFO);
    }
    final public void jumpToday()
    {

    
        jumpTo(s_dateToday);
    }
    abstract public void jumpTo(Hdate date);
    public void ShowTime()
    {
        /*SunCalc sc = new SunCalc(m_dateCursor.get_hd_jd(), MaluachPreferences.GetLatitude(), -MaluachPreferences.GetLongitude(), (int) (MaluachPreferences.GetTimeZoneAt(m_dateCursor) * 60));
        String lstr = "���� ���� " + SunCalc.Min2Str(sc.getDawn());
        lstr += "\n���� ������� " + SunCalc.Min2Str(sc.getRecognize());
        lstr += "\n����� " + SunCalc.Min2Str(sc.getSunrise());
        lstr += "\n��� �\"� " + SunCalc.Min2Str(sc.getEndTimeKriatShma());
        lstr += "\n��� ����� " + SunCalc.Min2Str(sc.getEndTimeShahrit());
        lstr += "\n���� ���� " + SunCalc.Min2Str(sc.getNoon());
        lstr += "\n���� ����� " + SunCalc.Min2Str(sc.getMinhaGdola());
        lstr += "\n���� ���� " + SunCalc.Min2Str(sc.getMinhaKtana());
        lstr += "\n��� ����� " + SunCalc.Min2Str(sc.getPelegMinha());
        if (m_dateCursor.get_day_in_week()==6)
            lstr += "\n����� ��� " + SunCalc.Min2Str(sc.getSunset()-MaluachPreferences.GetKnisatShabat());
        lstr += "\n����� " + SunCalc.Min2Str(sc.getSunset());
        lstr += "\n��� ������� " + SunCalc.Min2Str(sc.getVisibleStars());
        if (m_dateCursor.get_day_in_week()==7)
            lstr += "\n��� ��� " + SunCalc.Min2Str(sc.getSunset()+40);
        lstr += "\n���� ���� " + SunCalc.Min2Str(sc.getNoon()+12*60);
        maluach.showAlert("���� ����", lstr, AlertType.INFO);*/
        TimesForm.getInstance().OnShow(m_dateCursor);
        maluach.getInstance().PushScreen((Displayable)TimesForm.getInstance());
    }
}
