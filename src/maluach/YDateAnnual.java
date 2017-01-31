/* This is free and unencumbered software released into the public domain.
 */
package maluach;
import maluach.YDate.*;

public class YDateAnnual
{

    final static String[] events_str =
    {
        "",//0 -reserved for none
        "א' ראש השנה",
        "ב' ראש השנה",//2
        "צום גדליה",//3
        "ערב יום כיפור",//4
        "יום כיפור",//5
        "ערב סוכות",
        "סוכות",
        "יו\"ט שני של סוכות",//Sukkot II
        "חול המועד סוכות",
        "הושענא רבה",//10
        "שמיני עצרת",
        "שמחת תורה",
        "שמיני עצרת שמחת תורה",
        "א' חנוכה",
        "ב' חנוכה",
        "ג' חנוכה",//16
        "ד' חנוכה",
        "ה' חנוכה",
        "ו' חנוכה",
        "ז' חנוכה",
        "ח' חנוכה",//21
        "צום י' בטבת",
        "ט\"ו בשבט",
        "תענית אסתר",
        "פורים",//25
        "שושן פורים",
        "פורים קטן",
        "ערב פסח",//28
        "פסח",
        "יו\"ט שני של פסח",//Pesach II
        "חול המועד פסח",//31
        "שביעי של פסח",
        "שמיני של פסח",
        "פסח שני",
        "ל\"ג בעומר",//35
        "ערב שבועות",
        "שבועות",
        "יו\"ט שני של שבועות",//38//Shavuot II
        "אסרו חג",
        "צום י\"ז בתמוז",//40
        "צום ט' באב",
        "ט\"ו באב",
        "ערב ראש השנה",
        "יום השואה",
        "יום הזיכרון",
        "יום העצמאות",//46
        "יום ירושלים",
        "יום המשפחה",
        "יום הזכרון ליצחק רבין",//49
        "שחרור בעל התניא ממאסר",//50

    };

    static final short EV_NONE=0;
    static final short EV_YOM_TOV=1;
    static final short EV_HOL_HAMOED=2;
    static final short EV_ISRU_HAG=3;
    static final short EV_EREV_YOM_TOV=4;
    static final short EV_MIRACLE=5;
    static final short EV_CHASIDIC=6;
    static final short EV_GOOD_DAYS=7;
    static final short EV_NATIONAL=8;
    static final short EV_TZOM=16;
    static final short EV_REGALIM=32;
    static final short EV_MEMORIAL=64;
    static final short EV_HORBAN=128;
    static final short[] events_type =
    {
        EV_NONE,//0 -reserved for none
        EV_YOM_TOV,
        EV_YOM_TOV,//2
        EV_TZOM|EV_HORBAN,//3
        EV_EREV_YOM_TOV,//4
        EV_TZOM|EV_YOM_TOV,//5
        EV_EREV_YOM_TOV,
        EV_YOM_TOV|EV_REGALIM,
        EV_YOM_TOV|EV_REGALIM,//Sukkot II
        EV_HOL_HAMOED|EV_REGALIM,
        EV_HOL_HAMOED|EV_REGALIM,//10
        EV_YOM_TOV|EV_REGALIM,
        EV_YOM_TOV|EV_REGALIM,
        EV_YOM_TOV|EV_REGALIM,
        EV_MIRACLE,
        EV_MIRACLE,
        EV_MIRACLE,//16
        EV_MIRACLE,
        EV_MIRACLE,
        EV_MIRACLE,
        EV_MIRACLE,
        EV_MIRACLE,//21
        EV_TZOM|EV_HORBAN,
        EV_GOOD_DAYS,
        EV_TZOM|EV_MIRACLE,
        EV_MIRACLE,//25
        EV_MIRACLE,
        EV_MIRACLE,
        EV_EREV_YOM_TOV,//28
        EV_YOM_TOV|EV_REGALIM,
        EV_YOM_TOV|EV_REGALIM,//Pesach II
        EV_HOL_HAMOED|EV_REGALIM,//31
        EV_YOM_TOV|EV_REGALIM,
        EV_YOM_TOV|EV_REGALIM,
        EV_REGALIM,
        EV_GOOD_DAYS,//35
        EV_EREV_YOM_TOV,
        EV_YOM_TOV|EV_REGALIM,
        EV_YOM_TOV|EV_REGALIM,//38//Shavuot II
        EV_ISRU_HAG,
        EV_TZOM|EV_HORBAN,//40
        EV_TZOM|EV_HORBAN,
        EV_GOOD_DAYS,
        EV_EREV_YOM_TOV,
        EV_NATIONAL|EV_MEMORIAL,
        EV_NATIONAL|EV_MEMORIAL,
        EV_NATIONAL,//46
        EV_NATIONAL,
        EV_NATIONAL,
        EV_NATIONAL|EV_MEMORIAL,//49
        EV_CHASIDIC,//50 - 19 kislev

    };


    static final byte [][] event_db= 
    {// month_id,day,array index,# of days,jump/dhia(if #_days==1)
        {JewishDate.M_ID_TISHREI,1,1,2,1},//two days of rosh hashana
        {JewishDate.M_ID_TISHREI,3,3,1,1},//zom gdalia, dhia
        {JewishDate.M_ID_TISHREI,9,4,2,1},//yom kippur
        {JewishDate.M_ID_TISHREI,14,6,2,1},//sukkot
        {JewishDate.M_ID_TISHREI,16,9,5,0},//hol hamoed sukkot
        {JewishDate.M_ID_TISHREI,21,10,1,0},//hoshana raba
        {JewishDate.M_ID_TISHREI,22,13,1,0},//shmini azeret simhat_tora
        {JewishDate.M_ID_TISHREI,23,39,1,0},//isru hag
        {JewishDate.M_ID_KISLEV,19,50,1,0},//Baal HaTania got out of prison
        {JewishDate.M_ID_KISLEV,25,14,8,1},//Chanukah
        {JewishDate.M_ID_TEVET,10,22,1,0},//Tzom Asara B'Tevet
        {JewishDate.M_ID_SHEVAT,15,23,1,0},//Tu B'Shvat
        {JewishDate.M_ID_ADAR,13,24,1,-2},//taanit ester
        {JewishDate.M_ID_ADAR,14,25,2,1},//Purim+Shushan
        {JewishDate.M_ID_ADAR_I,14,27,2,0},//Purim katan - two days
        {JewishDate.M_ID_ADAR_II,13,24,1,-2},//taanit ester
        {JewishDate.M_ID_ADAR_II,14,25,2,1},//Purim+Shushan
        {JewishDate.M_ID_NISAN,14,28,2,1},//Erev Pesah+Pesah
        {JewishDate.M_ID_NISAN,16,31,5,0},//Hol Ha'moed Pesah
        {JewishDate.M_ID_NISAN,21,32,1,0},//Shvi'i Pesah
        {JewishDate.M_ID_NISAN,22,39,1,0},//isru hag
        {JewishDate.M_ID_IYAR,14,34,1,0},//Pesah Sheni
        {JewishDate.M_ID_IYAR,18,35,1,0},//Lag Ba'Omer
        {JewishDate.M_ID_SIVAN,5,36,2,1},//Erev Shavu'ot+Shavu'ot
        {JewishDate.M_ID_SIVAN,7,39,1,0},//isru hag
        {JewishDate.M_ID_TAMMUZ,17,40,1,1},//Tzom 17 Tamuz
        {JewishDate.M_ID_AV,9,41,1,1},//Tzom 9 Av
        {JewishDate.M_ID_AV,15,42,1,0},//15 Av
        {JewishDate.M_ID_ELUL,29,43,1,0},//Erev Rosh Hashana
    };
    static final byte [][] event_db_diaspora= 
    {// month_id,day,array index,# of days,jump/dhia(if #_days==1)
        {JewishDate.M_ID_TISHREI,16,8,1,0},//sukkot II
        {JewishDate.M_ID_TISHREI,22,11,1,0},//shmini azeret
        {JewishDate.M_ID_TISHREI,23,12,1,0},//simhat_tora
        {JewishDate.M_ID_NISAN,16,30,1,0},//Pesah II
        {JewishDate.M_ID_NISAN,22,33,1,0},//Shmi'ni Pesah
        {JewishDate.M_ID_SIVAN,7,38,1,0},//Shavu'ot II
    };
/*
    holocaust day (44) in 27 Nisan 1951 (5711). if on friday, move it backward. if on sunday after 1997 move it afterward.
    memorial day (45) in 4 Iyar 1951 (5711).
    family day (48) in 30 Shevat since 1973 (5733).
    
    */
    
    private byte [] current_year_events;
    private boolean diaspora;
    private int year;
    public int year()
    {
        return this.year;
    }
    public boolean diaspora()
    {
        return this.diaspora;
    }
    public String getYearEventForDay(JewishDate d)
    {
        return events_str[current_year_events[d.dayInYear()]];
    }
    public short getYearEventTypeForDay(JewishDate d)
    {
        return events_type[current_year_events[d.dayInYear()]];
    }
    static public short getEventType(int event_id)
    {
        return events_type[event_id];
    }
    public byte [] getYearEvents()
    {
        return current_year_events;
    }
    private static byte [] cloneArray(byte [] arr)
    {
        byte [] new_arr=new byte[arr.length];
        for (int i=0;i<arr.length;++i)
        {
            new_arr[i]=arr[i];
        }
        return new_arr;
    }
    public YDateAnnual(int year, int year_length, int year_first_day,boolean diaspora)
    {
        this.year=year;
        this.diaspora=diaspora;
        int year_ld_t= JewishDate.ld_year_type(year_length,year_first_day%7+1);
        initialize_year(diaspora,year_ld_t,year_length,year_first_day);
        current_year_events=cloneArray(annual_events[diaspora?1:0][year_ld_t-1]);
        addNationalEvents(current_year_events,year, year_length, year_first_day);
    }
    private static void addNationalEvents(byte [] year_events,int year, int year_length, int year_first_day)
    {
        //Holocaust day
        if (year >= 5718)//1958
        {
            int day_in_year = YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_NISAN, 27);
            int dayweek = (day_in_year + year_first_day) % 7;
            if (dayweek == YDate.FRIDAY)//friday
            {
                day_in_year--;
            }
            else if (dayweek == YDate.SUNDAY)//sunday
            {

                day_in_year++;
            }
            year_events[day_in_year] = 44;
        }
        //Yom Azma'ut and Yom HaZikaron
        if (year >= 5708)//1948
        {
            int day_in_year = YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_IYAR, 5);
            int dayweek = (day_in_year + year_first_day) % 7;

            if (dayweek == YDate.SATURDAY)//on saturday
            {
                day_in_year -= 2;

            }
            else if (dayweek == YDate.FRIDAY)//on friday
            {
                day_in_year -= 1;

            }
            else if (dayweek == YDate.MONDAY && year >= 5764)//on monday (2004) then Yom HaZikaron is on sunday, and that's no good...
            {
                day_in_year += 1;

            }
            year_events[day_in_year - 1] = 45;//Yom HaZikaron
            year_events[day_in_year] = 46;//Yom Azma'ut
        }
        //Jerusalem day
        if (year >= 5728)//1968
	{
            int day_in_year = YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_IYAR, 28);
            year_events[day_in_year] = 47;
	}
        //Family day
        if (year >= 5733)//1973
	{
            int day_in_year = YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_SHEVAT, 30);
            year_events[day_in_year] = 48;
	}
        //Rabin's Day   
        if (year >= 5758)//cheshvan 1997
        {
            int day_in_year = YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_CHESHVAN, 12);
            int dayweek = (day_in_year + year_first_day) % 7;
            if (dayweek == YDate.FRIDAY)
            {
                day_in_year--;
            }
            year_events[day_in_year] = 49;
        }
    }
    static byte [][][] annual_events = new byte [2][JewishDate.N_YEAR_TYPES][];//[diaspora][year_type][day_in_year]
    public static String getEventForDay(JewishDate d,boolean diaspora)
    {
        return events_str[getEvents(d, diaspora)[d.dayInYear()]];
    }
    public static byte [] getEvents(JewishDate d,boolean diaspora)
    {
        int year_ld_t= JewishDate.ld_year_type(d.yearLength(),d.yearFirstDay()%7+1);
        return initialize_year(diaspora,year_ld_t,d.yearLength(),d.yearFirstDay());
    }
    public static byte [] getEvents(int year, int year_length, int year_first_day,boolean diaspora)
    {
        int year_ld_t= JewishDate.ld_year_type(year_length,year_first_day%7+1);
        return initialize_year(diaspora,year_ld_t,year_length,year_first_day);
    }
    private static void expandDB(int year_length,int year_first_day,final byte [][] evdb, byte [] year_events)
    {
        boolean leap=year_length>355;
        final int IDX_M_ID = 0;
        final int IDX_DAY = 1;
        final int IDX_IDX = 2;
        final int IDX_LEN = 3;
        final int IDX_JMP = 4;
        for (int ev = 0; ev < evdb.length; ++ev)
        {
            int m_id = evdb[ev][IDX_M_ID];
            if (m_id == JewishDate.M_ID_ADAR && leap)
            {
                continue;
            }
            if ((m_id == JewishDate.M_ID_ADAR_I || m_id == JewishDate.M_ID_ADAR_II) && !leap)
            {
                continue;
            }
            int diy = JewishDate.calculateDayInYearByMonthId(year_length, m_id, evdb[ev][IDX_DAY]);
            if (evdb[ev][IDX_LEN] == 1)
            {
                if (evdb[ev][IDX_JMP] != 0)// dhia
                {
                    if ((year_first_day + diy) % 7 == YDate.SATURDAY)
                    {
                        diy += evdb[ev][IDX_JMP];
                    }
                }
                year_events[diy] = evdb[ev][IDX_IDX];
            }
            else
            {
                byte idx = evdb[ev][IDX_IDX];
                for (int l = 0; l < evdb[ev][IDX_LEN]; ++l)
                {
                    year_events[diy + l] = idx;
                    idx += evdb[ev][IDX_JMP];
                }
            }
        }
    }
    private static byte[] initialize_year(boolean diaspora,int year_ld_t,int year_length,int year_first_day)
    {
        if (annual_events[diaspora?1:0][year_ld_t-1]==null)
        {
            annual_events[diaspora?1:0][year_ld_t-1]=new byte [year_length];
            expandDB(year_length,year_first_day,event_db, annual_events[diaspora?1:0][year_ld_t-1]);
            if (diaspora)
                expandDB(year_length,year_first_day,event_db_diaspora, annual_events[diaspora?1:0][year_ld_t-1]);
        }
        return annual_events[diaspora?1:0][year_ld_t-1];
    }

    //bad days to start new things from ha'ari
/*
nisan: 7,9,11,16,21,24
iyar: 5,7,15,22
sivan: 1,6,9,26
tammuz: 14,15,17,20,29
av: 9,10,19,20,22,27
elul:9,17,28,29
tishrei: 6,10,28
cheshvan: 7,11,15,21
kislev: 1,8
tevet: 1,2,4,6,7,11,17,20,24,25,26,27
shevat: 9,17,18,24,25,26
adar I,II: 3,15,17,18,28

*/
}
