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
    protected static final Command c_showParasha=new Command("פרשת שבוע",Command.SCREEN,0);
    protected static final Command c_showOmer=new Command("ספירת העמר",Command.SCREEN,6);
    protected DateShowers()
    {
        addCommand(c_jumpToday);
        addCommand(c_showDayTimes);
        addCommand(c_showParasha);
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
        if (c==c_showOmer)
        {
            showOmer();
            return true;
        }
        return false;
    }
    protected void showParasha()
    {
            String lstr=Parasha.GetParashaFor(m_dateCursor);
            String shabat_str=Parasha.parshiot4(m_dateCursor);
            if (shabat_str.length()>0)
                lstr="שבת "+shabat_str+", "+lstr;
            if (lstr.length()>0)
                maluach.showAlert("פרשה", lstr, AlertType.INFO);
    }
    protected void showOmer()
    {
        int om=m_dateCursor.get_sfirat_haomer();
        String lstr="";
        if (om>0)
        {
            lstr="היום "+Hdate.get_int_string(om,true) + " לעמר (מערב אתמול)";
            
        }
        om+=1;
        if (om<50 && om>0)
        {
            lstr+="\nבערב "+Hdate.get_int_string(om,true) + " לעמר";
        }
        if (lstr.length()>0)
                maluach.showAlert("ספירת העמר", lstr, AlertType.INFO);
    }
    final public void jumpToday()
    {

    
        jumpTo(s_dateToday);
    }
    abstract public void jumpTo(Hdate date);
    public void ShowTime()
    {
        /*SunCalc sc = new SunCalc(m_dateCursor.get_hd_jd(), MaluachPreferences.GetLatitude(), -MaluachPreferences.GetLongitude(), (int) (MaluachPreferences.GetTimeZoneAt(m_dateCursor) * 60));
        String lstr = "עלות השחר " + SunCalc.Min2Str(sc.getDawn());
        lstr += "\nטלית ותפילין " + SunCalc.Min2Str(sc.getRecognize());
        lstr += "\nזריחה " + SunCalc.Min2Str(sc.getSunrise());
        lstr += "\nסוף ק\"ש " + SunCalc.Min2Str(sc.getEndTimeKriatShma());
        lstr += "\nסוף תפילה " + SunCalc.Min2Str(sc.getEndTimeShahrit());
        lstr += "\nחצות היום " + SunCalc.Min2Str(sc.getNoon());
        lstr += "\nמנחה גדולה " + SunCalc.Min2Str(sc.getMinhaGdola());
        lstr += "\nמנחה קטנה " + SunCalc.Min2Str(sc.getMinhaKtana());
        lstr += "\nפלג המנחה " + SunCalc.Min2Str(sc.getPelegMinha());
        if (m_dateCursor.get_day_in_week()==6)
            lstr += "\nכניסת שבת " + SunCalc.Min2Str(sc.getSunset()-MaluachPreferences.GetKnisatShabat());
        lstr += "\nשקיעה " + SunCalc.Min2Str(sc.getSunset());
        lstr += "\nצאת הכוכבים " + SunCalc.Min2Str(sc.getVisibleStars());
        if (m_dateCursor.get_day_in_week()==7)
            lstr += "\nצאת שבת " + SunCalc.Min2Str(sc.getSunset()+40);
        lstr += "\nחצות הליל " + SunCalc.Min2Str(sc.getNoon()+12*60);
        maluach.showAlert("זמני היום", lstr, AlertType.INFO);*/
        TimesForm.getInstance().OnShow(m_dateCursor);
        maluach.getInstance().PushScreen((Displayable)TimesForm.getInstance());
    }
}
