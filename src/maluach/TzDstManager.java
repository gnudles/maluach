/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import maluach.YDate.*;

/*

Rule	Zion	1940	only	-	Jun	 1	0:00	1:00	D
Rule	Zion	1942	only	-	Nov	 1	0:00	0	S
Rule	Zion	1943	only	-	Apr	 1	2:00	1:00	D
Rule	Zion	1943	only	-	Nov	 1	0:00	0	S
Rule	Zion	1944	only	-	Nov	 1	0:00	0	S
Rule	Zion	1944	only	-	Apr	 1	0:00	1:00	D
Rule	Zion	1945	only	-	Apr	16	0:00	1:00	D
Rule	Zion	1945	only	-	Nov	 1	2:00	0	S
Rule	Zion	1946	only	-	Apr	16	2:00	1:00	D
Rule	Zion	1946	only	-	Nov	 1	0:00	0	S

Rule	Zion	1948	only	-	May	23	0:00	2:00	DD
Rule	Zion	1948	only	-	Sep	 1	0:00	1:00	D
Rule	Zion	1948	only	-	Nov	 1	2:00	0	S
Rule	Zion	1949	only	-	May	 1	0:00	1:00	D
Rule	Zion	1949	only	-	Nov	 1	2:00	0	S
Rule	Zion	1950	only	-	Apr	16	0:00	1:00	D
Rule	Zion	1950	only	-	Sep	15	3:00	0	S
Rule	Zion	1951	only	-	Apr	 1	0:00	1:00	D
Rule	Zion	1951	only	-	Nov	11	3:00	0	S
Rule	Zion	1952	only	-	Apr	20	2:00	1:00	D
Rule	Zion	1952	only	-	Oct	19	3:00	0	S
Rule	Zion	1953	only	-	Apr	12	2:00	1:00	D
Rule	Zion	1953	only	-	Sep	13	3:00	0	S
Rule	Zion	1954	only	-	Jun	13	0:00	1:00	D
Rule	Zion	1954	only	-	Sep	12	0:00	0	S
Rule	Zion	1955	only	-	Jun	11	2:00	1:00	D
Rule	Zion	1955	only	-	Sep	11	0:00	0	S
Rule	Zion	1956	only	-	Jun	 3	0:00	1:00	D
Rule	Zion	1956	only	-	Sep	30	3:00	0	S
Rule	Zion	1957	only	-	Apr	29	2:00	1:00	D
Rule	Zion	1957	only	-	Sep	22	0:00	0	S

Rule	Zion	1974	only	-	Jul	 7	0:00	1:00	D
Rule	Zion	1974	only	-	Oct	13	0:00	0	S
Rule	Zion	1975	only	-	Apr	20	0:00	1:00	D
Rule	Zion	1975	only	-	Aug	31	0:00	0	S

Rule	Zion	1985	only	-	Apr	14	0:00	1:00	D
Rule	Zion	1985	only	-	Sep	15	0:00	0	S
Rule	Zion	1986	only	-	May	18	0:00	1:00	D
Rule	Zion	1986	only	-	Sep	 7	0:00	0	S
Rule	Zion	1987	only	-	Apr	15	0:00	1:00	D
Rule	Zion	1987	only	-	Sep	13	0:00	0	S
Rule	Zion	1988	only	-	Apr	10	0:00	1:00	D
Rule	Zion	1988	only	-	Sep	 4	0:00	0	S
Rule	Zion	1989	only	-	Apr	30	0:00	1:00	D
Rule	Zion	1989	only	-	Sep	 3	0:00	0	S
Rule	Zion	1990	only	-	Mar	25	0:00	1:00	D
Rule	Zion	1990	only	-	Aug	26	0:00	0	S
Rule	Zion	1991	only	-	Mar	24	0:00	1:00	D
Rule	Zion	1991	only	-	Sep	 1	0:00	0	S
Rule	Zion	1992	only	-	Mar	29	0:00	1:00	D
Rule	Zion	1992	only	-	Sep	 6	0:00	0	S
Rule	Zion	1993	only	-	Apr	 2	0:00	1:00	D
Rule	Zion	1993	only	-	Sep	 5	0:00	0	S

Rule	Zion	1994	only	-	Apr	 1	0:00	1:00	D
Rule	Zion	1994	only	-	Aug	28	0:00	0	S
Rule	Zion	1995	only	-	Mar	31	0:00	1:00	D
Rule	Zion	1995	only	-	Sep	 3	0:00	0	S

Rule	Zion	1996	only	-	Mar	15	0:00	1:00	D
Rule	Zion	1996	only	-	Sep	16	0:00	0	S
Rule	Zion	1997	only	-	Mar	21	0:00	1:00	D
Rule	Zion	1997	only	-	Sep	14	0:00	0	S
Rule	Zion	1998	only	-	Mar	20	0:00	1:00	D
Rule	Zion	1998	only	-	Sep	 6	0:00	0	S
Rule	Zion	1999	only	-	Apr	 2	2:00	1:00	D
Rule	Zion	1999	only	-	Sep	 3	2:00	0	S

Rule	Zion	2000	only	-	Apr	14	2:00	1:00	D
Rule	Zion	2000	only	-	Oct	 6	1:00	0	S
Rule	Zion	2001	only	-	Apr	 9	1:00	1:00	D
Rule	Zion	2001	only	-	Sep	24	1:00	0	S
Rule	Zion	2002	only	-	Mar	29	1:00	1:00	D
Rule	Zion	2002	only	-	Oct	 7	1:00	0	S
Rule	Zion	2003	only	-	Mar	28	1:00	1:00	D
Rule	Zion	2003	only	-	Oct	 3	1:00	0	S
Rule	Zion	2004	only	-	Apr	 7	1:00	1:00	D
Rule	Zion	2004	only	-	Sep	22	1:00	0	S
*/
/**
 *
 * @author orr
 */
public class TzDstManager implements TimeZoneProvider
{

    static int year;
    static Date summer_beginning;
    static Date summer_end;

    public float getOffset(Date d)
    {
        return GetTimeZoneAt(d);
    }

    static public float GetDSTAtIL(Date d)//d in UTC.
    {

        YDate _yd = YDate.createFrom(d);
        if (_yd.gd.year() < 2005)
        {
            final int BC=0;
            final int AD=1;
            TimeZone timezone = TimeZone.getTimeZone("Asia/Jerusalem");
            Calendar cal=Calendar.getInstance();
            cal.setTimeZone(timezone);
            cal.setTime(d);
            int ms=cal.get(Calendar.HOUR_OF_DAY)*3600000+cal.get(Calendar.MINUTE)*60000
                   +cal.get(Calendar.SECOND)*1000+cal.get(Calendar.MILLISECOND);
            return (timezone.getOffset(AD,cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.DAY_OF_WEEK),
                    ms
                    )/1000L)/3600.0f;
        }

        if (_yd.gd.year() != year)
        {
            year = _yd.gd.year();

            if (_yd.gd.year() < 2013)
            {
                int diy = GregorianDate.calculateDayInYear(GregorianDate.isLeap(_yd.gd.year()), 4, 1);
                int lastfriday = YDate.getPrevious(YDate.FRIDAY, diy + _yd.gd.yearFirstDay());
                summer_beginning = YDate.toDate(lastfriday, 0);
                _yd.setByDays(lastfriday + 200);

                diy = JewishDate.calculateDayInYearByMonthId(_yd.hd.yearLength(), JewishDate.M_ID_TISHREI, 9);
                int lastsunday = YDate.getPrevious(YDate.SUNDAY, diy + _yd.hd.yearFirstDay());
                summer_end = YDate.toDate(lastsunday - 1, 23);
            }
            else
            {
                int diy = GregorianDate.calculateDayInYear(GregorianDate.isLeap(_yd.gd.year()), 3, 31);
                int lastsunday = YDate.getPrevious(YDate.SUNDAY, diy + _yd.gd.yearFirstDay());
                summer_beginning = YDate.toDate(lastsunday - 2, 0);//friday
                diy = GregorianDate.calculateDayInYear(GregorianDate.isLeap(_yd.gd.year()), 10, 31);
                lastsunday = YDate.getPrevious(YDate.SUNDAY, diy + _yd.gd.yearFirstDay());
                summer_end = YDate.toDate(lastsunday - 1, 23);
            }
        }

        if (d.getTime() < summer_beginning.getTime())
        {
            return 0.0f;
        }
        if (d.getTime() < summer_end.getTime())
        {
            return 1.0f;
        }
        return 0.0f;
    }
    /*static public float GetDSTAt(YDate date)
     {
     final float DST=1.0f;
     final float NODST=0.0f;
     if (date.gd.year()>2005)
     {
     if (date.gd.year()<2013)
     {
     int gd_m=date.gd.month();
     if (gd_m>=4 && gd_m<=8)
     return DST;//summer
     if (gd_m<3 || gd_m>=11)
     return NODST;
     if(gd_m==3)
     {
     int gd_d=date.gd.dayInMonth();
     int doff=(33+date.gd.dayInWeek()-gd_d)%7;
     if (gd_d>31-doff)
     return DST;//summer
     return NODST;
     }
     int hd_m=date.hd.monthInYear();
     if (hd_m>=11)//av elul, tammuz in leap year
     return DST;//summer
     if (hd_m<=4 && hd_m >=2)
     return NODST;
     if (hd_m ==1)
     {
     int hd_d=date.hd.dayInMonth();
     if (hd_d>=9)
     return NODST;
     int doff=(15+date.hd.dayInWeek()-hd_d)%7;
     if (hd_d<9-doff)
     return DST;//summer
     }
     return NODST;
     }
     else
     {
     int gd_m=date.gd.month();
     if (gd_m>=4 && gd_m<=9)
     return DST;//summer
     if (gd_m<3 || gd_m>=11)
     return NODST;
     int gd_d=date.gd.dayInMonth();
     int week_day=date.gd.dayInWeek();
     int last_weekday=((31-gd_d)+(week_day-1))%7+1;
     if(gd_m==3)
     {
     if (31-1-last_weekday>gd_d)//friday before last sunday
     return NODST;
     return DST;//summer
     }
     else if (gd_m==10)
     {
     if (31+1-last_weekday>gd_d)//last sunday
     return DST;//summer
     return NODST;
     }
     }
     }
     return NODST;
     }*/

    static public float GetTimeZoneAt(Date date)
    {
        return 2.0f + GetDSTAtIL(date);
    }
    /*static public float GetTimeZoneAt(YDate date)
     {
     return 2.0f+GetDSTAt(date);
     }*/
}
