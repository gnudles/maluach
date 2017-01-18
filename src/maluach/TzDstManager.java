/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import java.util.Calendar;
import java.util.Date;
import maluach.YDate.*;

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
            return 0.0f;
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
