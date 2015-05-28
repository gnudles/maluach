
package maluach;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Hdate
{

    static final int HOUR = 1080;
    static final int DAY = (24 * HOUR);
    static final int WEEK = (7 * DAY);

    static int M(int h, int p)
    {
        return h * HOUR + p;
    }
    static final int MONTH = DAY + M(12, 793);
    /** The number of day in the hebrew month (1..30). */
    private int hd_day;
    /** The number of the hebrew month 1..14 (1 - tishre, 13 - adar 1, 14 - adar 2). */
    private int hd_mon;
    /** The number of the hebrew year. */
    private int hd_year;
    /** The number of the day in the month. (1..31) */
    private int gd_day;
    /** The number of the month 1..12 (1 - jan). */
    private int gd_mon;
    /** The number of the year. */
    private int gd_year;
    /** The day of the week 1..7 (1 - sunday). */
    private int hd_dw;
    /** The length of the year in days. */
    private int hd_size_of_year;
    /** The week day of Hebrew new year. */
    private int hd_new_year_dw;
    /** The number type of year. */
    private int hd_year_type;
    /** The Julian day number */
    private int hd_jd;
    /** The number of days passed since 1 tishrey */
    private int hd_days;


    static final int[] year_types =
    {
        1, 0, 0, 2, 0, 3, 4, 0, 5, 0, 6, 7,
        8, 0, 9, 10, 0, 11, 0, 0, 12, 0, 13, 14
    };
    static final int[] hd_month_size =
    {
        30/*tishrey*/, 29, 30/*kislev*/, 29, 30, 29/*adar*/, 30/*nissan*/, 29, 30, 29, 30, 29, /*adar1 */ 30,/*adar2*/ 29
    };
    byte month_holydays[] = null;
    static final String[] day_names =
    {
        "ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת"
    };

    public Hdate()
    {
        Calendar todays = Calendar.getInstance();
        todays.setTime(new Date());
        gd_day = todays.get(Calendar.DAY_OF_MONTH);
        gd_mon = todays.get(Calendar.MONTH) + 1;
        gd_year = todays.get(Calendar.YEAR);
        gdate2jd();
        calc_hdate();
    }

    public void Set(Hdate newdate)
    {
        if (newdate.hd_mon != hd_mon || newdate.hd_year != hd_year)
        {
            month_holydays = null;
        }
        hd_day = newdate.hd_day;
        hd_mon = newdate.hd_mon;
        hd_year = newdate.hd_year;
        gd_day = newdate.gd_day;
        gd_mon = newdate.gd_mon;
        gd_year = newdate.gd_year;
        hd_dw = newdate.hd_dw;
        hd_size_of_year = newdate.hd_size_of_year;
        hd_new_year_dw = newdate.hd_new_year_dw;
        hd_year_type = newdate.hd_year_type;
        hd_jd = newdate.hd_jd;
        hd_days = newdate.hd_days;

    }
    public int get_hd_new_year_dw()
    {
        return hd_new_year_dw;
    }
    public int get_hd_year_type()
    {
        return hd_year_type;
    }
    public int get_day_in_week()
    {
        return hd_dw;
    }

    public String get_day_in_week_name()
    {
        return day_names[hd_dw - 1];
    }

    public int get_hd_day_in_month()
    {
        return hd_day;
    }

    public int get_hd_month()
    {
        return hd_mon;
    }

    public int get_hd_month_nice()
    {
        if (hd_mon <= 12)
        {
            return hd_mon;
        }
        else
        {
            return 6;
        }
        /*if (hd_size_of_year <360)
        {
        return hd_mon;
        }
        else
        {
        if (hd_mon <=12)
        {
        if (hd_mon> 6)
        return hd_mon+1;
        else return hd_mon;
        }
        else
        return hd_mon-7;

        }*/
    }

    public int get_hd_jd()
    {
        return hd_jd;
    }

    public int get_hd_year_size()
    {
        return hd_size_of_year;
    }

    public int get_gd_year_size()
    {
        if (gd_year % 400 == 0)
        {
            return 366;
        }
        if (gd_year % 100 == 0)
        {
            return 365;
        }
        if (gd_year % 4 == 0)
        {
            return 366;
        }
        return 365;
    }

    public int get_hd_year()
    {
        return hd_year;
    }

    public int get_gd_year()
    {
        return gd_year;
    }

    public int get_gd_month()
    {
        return gd_mon;
    }

    public int get_gd_day()
    {
        return gd_day;
    }
    public int get_hd_weeks()
    {
        return ((hd_days - 1) + (hd_new_year_dw - 1)) / 7 + 1;
    }

    public static int get_year_type(int size_of_year, int new_year_dw)
    {
        /* Only 14 combinations of size and week day are posible */
        int doffset;
        /* convert size and first day to 1..24 number */
        /* 2,3,5,7 -> 1,2,3,4 */
        /* 353, 354, 355, 383, 384, 385 -> 0, 1, 2, 3, 4, 5 */
        doffset = (new_year_dw + 1) / 2;
        doffset = doffset + 4 * ((size_of_year % 10 - 3) + (size_of_year / 10 - 35));
        /* some combinations are imposible */
        return year_types[doffset - 1];
    }

    public final void gdate2jd()
    {
        int a = (14 - gd_mon) / 12;
        int y = gd_year + 4800 - a;
        int m = gd_mon + 12 * a - 3;
        hd_jd = gd_day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
    }

    public static int days_from_3744(int hebrew_year)
    {
        int years_from_3744;
        int molad_3744;
        int leap_months;
        int leap_left;
        int num_months;
        int parts;
        int days;
        int parts_left_in_week;
        int parts_left_in_day;
        int week_day;
        /* Start point for calculation is Molad new year 3744 (16BC) */
        years_from_3744 = hebrew_year - 3744;
        molad_3744 = M(1 + 6, 779);	/* Molad 3744 + 6 hours in parts */

        /* Time in months */
        leap_months = (years_from_3744 * 7 + 1) / 19;	/* Number of leap months */
        leap_left = (years_from_3744 * 7 + 1) % 19;	/* Months left of leap cycle */
        num_months = years_from_3744 * 12 + leap_months;	/* Total Number of months */

        /* Time in parts and days */
        parts = num_months * MONTH + molad_3744;	/* Molad This year + Molad 3744 - corections */
        days = num_months * 28 + parts / DAY - 2;	/* 28 days in month + corections */

        /* Time left for round date in corections */
        parts_left_in_week = parts % WEEK;	/* 28 % 7 = 0 so only corections counts */
        parts_left_in_day = parts % DAY;
        week_day = parts_left_in_week / DAY;

        /* Special cases of Molad Zaken */
        if ((leap_left < 12 && week_day == 3
             && parts_left_in_day >= M(9 + 6, 204))
            || (leap_left < 7 && week_day == 2
                && parts_left_in_day >= M(15 + 6, 589)))
        {
            days++;
            week_day++;
        }
        /* ADU */

        if (week_day == 1 || week_day == 4 || week_day == 6)
        {
            days++;
        }
        return days;

    }

    public int get_hd_month_size()
    {
        return get_hd_month_size(hd_mon, hd_size_of_year);


    }

    public static int get_hd_month_size(int month, int size_of_year)
    {

        int extradays = size_of_year % 10 - 3;


        if (extradays == 0)
        {
            if (month == 3)
            {
                return 29;


            }

        }
        else if (extradays == 2) //regular year
        {
            if (month == 2)
            {
                return 30;


            }
        }
        return hd_month_size[month - 1];


    }

    public int get_hd_prev_month_size()
    {


        if (hd_mon == 7 && hd_size_of_year / 10 == 38)
        {
            return 29;
        }
        if (hd_mon == 1)
        {
            return 29;
        }
        if (hd_mon == 13)
        {
            return 30;
        }
        return get_hd_month_size(hd_mon - 1, hd_size_of_year);

    }

    public void hdate_to_jd()//calc jd and hd variables from hd_year hd_mon hd_day
    {
        int _days_from_3744;
        /* Calculate days since 1,1,3744 */
        _days_from_3744 = days_from_3744(hd_year);
        hd_days = _days_from_3744;
        hd_size_of_year = days_from_3744(hd_year + 1) - _days_from_3744;
        if (hd_size_of_year > 380)
        {
            if (hd_mon == 6)
            {
                hd_mon = 14;
            }
        }
        else
        {
            if (hd_mon > 12)
            {
                hd_mon = 6;
            }
        }
        if (hd_day == 30)
        {
            if (get_hd_month_size() == 29)
            {
                hd_day = 29;
            }
        }
        int day = hd_day;
        int month = hd_mon;
        /* Adjust for leap year */
        if (month == 13)
        {
            month = 6;
        }
        if (month == 14)
        {
            month = 6;
            day += 30;
        }
        day = _days_from_3744 + (59 * (month - 1) + 1) / 2 + day;

        /* length of year */

        /* Special cases for this year */
        if (hd_size_of_year % 10 > 4 && month > 2) /* long Heshvan */ {
            day++;
        }
        if (hd_size_of_year % 10 < 4 && month > 3) /* short Kislev */ {
            day--;
        }
        if (hd_size_of_year > 365 && month > 6) /* leap year */ {
            day += 30;
        }

        /* adjust to julian */
        hd_jd = day + 1715118;
        hd_days = day - hd_days;
        hd_dw = (hd_jd + 1) % 7 + 1;
        hd_new_year_dw = (hd_jd - hd_days + 2) % 7 + 1;
        hd_year_type = get_year_type(hd_size_of_year, hd_new_year_dw);
    }

    public void change_hd_year(int year)
    {
        if (year >= 0 && year < 7000)
        {
            hd_year = year;//changes hd_year
            hdate_to_jd();//fix hd_mon,hd_day. set hd_jd,hd_size_of_year,hd_days,hd_dw,hd_new_year_dw,hd_year_type
            jd2gdate();//
        }

    }

    public void change_hd_jd(int jd)
    {
        if (jd > 347644 && jd < 2904343)
        {
            hd_jd = jd;
            jd2gdate();
            calc_hdate();
        }
    }

    public void change_gd_year(int year)
    {
        if (year >= -3760)
        {
            if (year >= 3239)
            {
                if (year == 3239)
                {
                    boolean calc = false;
                    if (gd_mon < 9)
                    {
                        calc = true;
                    }
                    if (gd_mon == 9)
                    {
                        if (gd_day < 20)
                        {
                            calc = true;
                        }
                    }
                    if (calc)
                    {
                        gd_year = 3239;
                        gdate2jd();
                        calc_hdate();
                    }
                }
                return;
            }
            gd_year = year;
            gdate2jd();
            calc_hdate();
        }
        else
        {
            boolean calc = false;
            if (year == -3761)
            {
                if (gd_mon == 9)
                {
                    if (gd_day >= 20)
                    {
                        calc = true;

                    }
                }
                else if (gd_mon > 9)
                {
                    calc = true;
                }
                if (calc)
                {
                    gd_year = -3761;
                    gdate2jd();
                    calc_hdate();
                }
            }
        }
    }

    public int get_gd_month_size()
    {
        if (gd_mon != 2)
        {
            int mon;
            if (gd_mon > 7)
            {
                mon = gd_mon - 7;
            }
            else
            {
                mon = gd_mon;
            }
            return 30 + mon % 2;
        }
        else
        {
            if (gd_year % 400 == 0)
            {
                return 29;
            }
            if (gd_year % 100 == 0)
            {
                return 28;
            }
            if (gd_year % 4 == 0)
            {
                return 29;
            }
            return 28;
        }

    }

    public void change_gd_month_next()
    {
        if (gd_year == 3239)
        {
            if (gd_mon == 9)
            {
                return;
            }
            if (gd_mon == 8 && gd_day > 19)
            {
                return;
            }
        }
        if (gd_mon == 12)
        {
            gd_mon = 1;
        }
        else
        {
            gd_mon++;
        }
        if (gd_day > 28)
        {
            int msize = get_gd_month_size();
            if (gd_day > msize)
            {
                gd_day = msize;
            }
        }
        gdate2jd();
        calc_hdate();
    }

    public void change_gd_month_prev()
    {
        if (gd_year == -3761)
        {
            if (gd_mon == 9)
            {
                return;
            }
            if (gd_mon == 10 && gd_day < 20)
            {
                return;
            }
        }
        if (gd_mon == 1)
        {
            gd_mon = 12;
        }
        else
        {
            gd_mon--;
        }
        if (gd_day > 28)
        {
            int msize = get_gd_month_size();
            if (gd_day > msize)
            {
                gd_day = msize;
            }
        }
        gdate2jd();
        calc_hdate();
    }

    public void change_gd_day_prev()
    {
        if (gd_year == -3761)
        {
            if (gd_mon == 9)
            {
                if (gd_day == 20)
                {
                    return;
                }
            }
        }
        if (gd_day == 1)
        {
            gd_day = get_gd_month_size();
        }
        else
        {
            gd_day--;
        }
        gdate2jd();
        calc_hdate();
    }

    public void change_gd_day_next()
    {
        if (gd_year == 3239)
        {
            if (gd_mon == 9)
            {
                if (gd_day == 19)
                {
                    return;
                }
            }
        }
        if (gd_day == get_gd_month_size())
        {
            gd_day = 1;
        }
        else
        {
            gd_day++;
        }
        gdate2jd();
        calc_hdate();
    }

    public void change_hd_month_next()
    {
        if (hd_mon == 12)
        {
            hd_mon = 1;
        }
        else
        {
            if (hd_mon == 14)
            {
                hd_mon = 7;
            }
            else
            {
                if (hd_mon == 5 && hd_size_of_year > 380)
                {
                    hd_mon = 13;
                }
                else
                {
                    hd_mon++;
                }
            }
        }
        hdate_to_jd();
        jd2gdate();
    }

    public void change_hd_month_prev()
    {
        if (hd_mon == 1)
        {
            hd_mon = 12;
        }
        else
        {
            if (hd_size_of_year > 380 && hd_mon == 7)
            {
                hd_mon = 14;
            }
            else
            {
                if (hd_mon == 13)
                {
                    hd_mon = 5;
                }
                else
                {
                    hd_mon--;
                }
            }
        }
        hdate_to_jd();
        jd2gdate();
    }

    public void change_hd_day_prev()
    {
        if (hd_day == 1)
        {
            hd_day = get_hd_month_size();
        }
        else
        {
            hd_day--;
        }
        hdate_to_jd();
        jd2gdate();
    }

    public void change_hd_day_next()
    {
        if (hd_day == 30 || (hd_day == 29 && get_hd_month_size() == 29))
        {
            hd_day = 1;
        }
        else
        {
            hd_day++;
        }
        hdate_to_jd();
        jd2gdate();
    }

    private void calc_hdate()
    {
        int days;
        int jd_tishrey1, jd_tishrey1_next_year;
        /* Guess Hebrew year is Gregorian year + 3760 */
        hd_year = gd_year + 3760;

        jd_tishrey1 = days_from_3744(hd_year) + 1715119;
        jd_tishrey1_next_year = days_from_3744(hd_year + 1) + 1715119;

        /* Check if computed year was underestimated */


        if (jd_tishrey1_next_year <= hd_jd)
        {
            hd_year = hd_year + 1;
            jd_tishrey1 = jd_tishrey1_next_year;
            jd_tishrey1_next_year = days_from_3744(hd_year + 1) + 1715119;


        }

        hd_size_of_year = jd_tishrey1_next_year - jd_tishrey1;

        /* days into this year, first month 0..29 */
        days = hd_jd - jd_tishrey1;
        hd_days = days + 1;

        /* last 8 months allways have 236 days */


        if (days >= (hd_size_of_year - 236)) /* in last 8 months */ {
            days = days - (hd_size_of_year - 236);
            hd_mon = days * 2 / 59;
            hd_day = days - (hd_mon * 59 + 1) / 2 + 1;

            hd_mon = hd_mon + 4 + 1;

            /* if leap */


            if (hd_size_of_year > 355 && hd_mon <= 6)
            {
                hd_mon = hd_mon + 8;


            }
        }
        else /* in 4-5 first months */ {
            /* Special cases for this year */
            if (hd_size_of_year % 10 > 4 && days == 59) /* long Heshvan (day 30 of Heshvan) */ {
                hd_mon = 1;
                hd_day = 30;


            }
            else if (hd_size_of_year % 10 > 4 && days > 59) /* long Heshvan */ {
                hd_mon = (days - 1) * 2 / 59;
                hd_day = days - (hd_mon * 59 + 1) / 2;


            }
            else if (hd_size_of_year % 10 < 4 && days > 87) /* short kislev */ {
                hd_mon = (days + 1) * 2 / 59;
                hd_day = days - (hd_mon * 59 + 1) / 2 + 2;


            }
            else /* regular months */ {
                hd_mon = days * 2 / 59;
                hd_day = days - (hd_mon * 59 + 1) / 2 + 1;


            }

            hd_mon = hd_mon + 1;


        }

        hd_dw = (hd_jd + 1) % 7 + 1;
        hd_new_year_dw = (jd_tishrey1 + 1) % 7 + 1;
        hd_year_type = get_year_type(hd_size_of_year, hd_new_year_dw);
    }
    static String[][] digits =
    {
        {
            " ", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט"
        },
        {
            "ט", "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ"
        },
        {
            " ", "ק", "ר", "ש", "ת"
        }
    };

    public static String get_int_string(int n, boolean geresh)
    {
        String hnum = "";


        boolean hundreds = false;
        /* sanity checks */


        if (n < 1 || n >= 10000)
        {
            return hnum;
        }
        if (n >= 1000)
        {
            hnum = hnum + digits[0][n / 1000];
            n %= 1000;
            hnum = hnum + "'";
            hundreds = true;
        }
        while (n >= 400)
        {
            hnum = hnum + digits[2][4];
            n -= 400;
        }
        if (n >= 100)
        {
            hnum = hnum + digits[2][n / 100];
            n %= 100;
        }
        if (n >= 10)
        {
            if (n == 15 || n == 16)
            {
                n -= 9;
            }
            hnum = hnum + digits[1][n / 10];
            n %= 10;
        }
        if (n > 0)
        {
            hnum = hnum + digits[0][n];
        }
        if (geresh)
        {
            int len = hnum.length();
            if (hundreds)
            {
                len -= 2;
            }
            if (len < 2)
            {
                hnum += "'";
            }
            else
            {
                hnum = hnum.substring(0, hnum.length() - 1) + "\"" + hnum.substring(hnum.length() - 1, hnum.length());
            }
        }
        return hnum;


    }
    final static String[] months =
    {
        "תשרי", "חשוון", "כסלו", "טבת",
        "שבט", "אדר", "ניסן", "אייר",
        "סיוון", "תמוז", "אב", "אלול", "אדר א'",
        "אדר ב'"
    };
    final static String[] gdmonths =
    {
        "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"
    };

    String get_hd_month_name()
    {
        return months[hd_mon - 1];
    }

    String get_gd_month_name()
    {
        return gdmonths[gd_mon - 1];
    }

    String getDayString()
    {
        return get_int_string(hd_day, true) + " ב" + months[hd_mon - 1] + " " + get_int_string(hd_year, true);
    }

    String getgDayString()
    {
        return Integer.toString(gd_day) + " ב" + gdmonths[gd_mon - 1] + " " + Integer.toString(gd_year);
    }
    final static String[] holydaystr =
    {
        "", "א' ראש השנה",
        "ב' ראש השנה",//2
        "צום גדליה",//3
        "יום כיפור",//4
        "סוכות",//5
        "חול המועד סוכות",
        "הושענא רבה",
        "שמחת תורה",
        "חנוכה",
        "צום י' בטבת",//10
        "ט\"ו בשבט",
        "תענית אסתר",
        "פורים",
        "שושן פורים",
        "פסח",//15
        "חול המועד פסח",
        "יום העצמאות",
        "ל\"ג בעומר",
        "ערב שבועות",
        "שבועות",//20
        "צום י\"ז בתמוז",
        "צום ט' באב",
        "ט\"ו באב",
        "יום השואה",
        "יום הזיכרון",//25
        "יום ירושלים",
        "שמיני עצרת",
        "שביעי של פסח",
        "שמיני של פסח",
        "",//30//Shavuot II
        "",//Sukkot II
        "",//Pesach II
        "יום המשפחה",
        "יום הזיכרון לנופלים",
        "יום הזיכרון ליצחק רבין",//35
        "יום ז'בוטינסקי",
        "ערב יום כיפור",//37
        "א' חנוכה",
        "ב' חנוכה",
        "ג' חנוכה",
        "ד' חנוכה",
        "ה' חנוכה",
        "ו' חנוכה",
        "ז' חנוכה",
       
        "ח' חנוכה",
        "ערב סוכות",
        //47 erev pesach
        "ערב פסח",
            "ערב ראש השנה",
    "אסרו חג",
        "פסח שני",
        "פורים קטן"
           
    };

    String getHolyday()
    {
        get_holydays();
        return holydaystr[month_holydays[hd_day - 1]];
    }

    int get_month_week_offset()
    {
        return (7 + hd_dw - (hd_day % 7)) % 7;
    }
    static final byte[][] taanit =
    {
        {
            3, 1, 3
        }/* Tishrey */,
        {
        }/* Heshvan */,
        {
        }/* Kislev */,
        {
        }/* Tevet */,
        {
        }/* Shvat */,
        {
            13, -2, 12
        }/* Adar */,
        {
        }/* Nisan */,
        {
        }/* Iyar */,
        {
        }/* Sivan */,
        {
            17, 1, 21
        }/* Tamuz */,
        {
            9, 1, 22
        }/* Av */,
        {
        }/* Elul */,
        {
        }/* Adar 1 */,
        {
            13, -2, 12
        }/* Adar 2 */

    };
    static final byte[][] holydays_table =
    {
        { /* Tishrey */

            1, 2, 0, 0, 0, 0, 0, 0, 37, 4,
            0, 0, 0, 46, 5, 6, 6, 6, 6, 6,
            7, 27, 49, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Heshvan */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Kislev */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 38, 39, 40, 41, 42, 43
        },
        { /* Tevet */

            44, 45, 0, 0, 0, 0, 0, 0, 0, 10,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Shvat */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 11, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 33
        },
        { /* Adar */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 13, 14, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Nisan */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 47, 15, 16, 16, 16, 16, 16,
            28, 49, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Iyar */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 50, 0, 0, 0, 18, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 26, 0, 0
        },
        { /* Sivan */

            0, 0, 0, 0, 19, 20, 49, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Tamuz */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Av */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 23, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Elul */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 48
        },
        { /* Adar 1 */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 51, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        { /* Adar 2 */

            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 13, 14, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        }
    };
    
    byte[] get_holydays()
    {
        if (month_holydays != null)
        {
            return month_holydays;
        }
        int offset = get_month_week_offset();
        month_holydays = new byte[30];
        byte[] curmonth = holydays_table[hd_mon - 1];
        System.arraycopy(curmonth, 0, month_holydays, 0, 30);
        int i;
        byte taanitval[] = taanit[hd_mon - 1];
        for (i = 0; i < taanitval.length / 3; i += 3)
        {
            if (((taanitval[i] + offset - 1)) % 7 == 6)
            {
                month_holydays[taanitval[i] - 1 + taanitval[i + 1]] = taanitval[i + 2];
            }
            else
            {
                month_holydays[taanitval[i] - 1] = taanitval[i + 2];
            }
        }
        if (hd_mon == 2 /* heshvan */)
        {
            if (gd_year >= 1997)
            {
                int dayweek = (12 - 1 + offset) % 7;
                int rabinday = 11;
                if (dayweek == 5)//on friday
                {
                    rabinday--;
                }
                month_holydays[rabinday] = 35;
            }
        }
        else if (hd_mon == 8 /* Iyar */)
        { //yom azmaut
            if (gd_year >= 1948)
            {
                int dayweek = (5 - 1 + offset) % 7;
                int azmaday;
                if (gd_year >= 2004)
                {

                    if (dayweek == 6)//on saturday
                    {
                        azmaday = 2;

                    }
                    else if (dayweek == 5)//on friday
                    {
                        azmaday = 3;

                    }
                    else if (dayweek == 1)//on mondayday
                    {
                        azmaday = 5;

                    }
                    else
                    {
                        azmaday = 4;

                    }

                }
                else
                {
                    if (dayweek == 6)//on saturday
                    {
                        azmaday = 2;

                    }
                    else if (dayweek == 5)//on friday
                    {
                        azmaday = 3;

                    }
                    else
                    {
                        azmaday = 4;

                    }
                }
                month_holydays[azmaday] = 17;
                month_holydays[azmaday - 1] = 25;
            }
        }
        else if (hd_mon == 7 /* nissan */)//yom hashoa
        {
            if (gd_year >= 1958)
            {
                int dayweek = (27 - 1 + offset) % 7;
                int shoaday = 26;
                if (dayweek == 5)//friday
                {
                    shoaday--;
                }
                else
                {
                    if (dayweek == 0)//sunday
                    {
                        shoaday++;
                    }
                }
                month_holydays[shoaday] = 24;
            }
        }
        else if (hd_mon == 4 /* tevet */)
        {
            if (get_hd_prev_month_size() == 29)
            {
                month_holydays[0] = 43;
                month_holydays[1] = 44;
                month_holydays[2] = 45;
            }
        }
        return month_holydays;
    }

    static final int[] omer_offsets={
        -15,15,44
    };
    public int get_sfirat_haomer()
    {
        if (hd_mon>=7 && hd_mon<=9)
        {
            int om=hd_day+omer_offsets[hd_mon-7];
            if (om<0 || om>49) //if om ==0 then its the night before hasfira
                return -1;
            return om;
        }
        else
            return -1;
    }
    public boolean moveday(int delta)
    {
        if ((hd_jd + delta) <= 347644 || (hd_jd + delta) >= 2904343)
        {
            return false;
        }
        hd_jd += delta;
        jd2gdate();
        int prevmon, prevyear;
        prevmon = hd_mon;
        prevyear = hd_year;
        calc_hdate();
        if (prevmon != hd_mon || prevyear != hd_year)
        {
            month_holydays = null;
        }
        return true;
    }

    private void jd2gdate()
    {
        int l, n, i, j;
        l = hd_jd + 68569;
        n = (4 * l) / 146097;
        l = l - (146097 * n + 3) / 4;
        i = (4000 * (l + 1)) / 1461001;	/* that's 1,461,001 */
        l = l - (1461 * i) / 4 + 31;
        j = (80 * l) / 2447;
        gd_day =
        l - (2447 * j) / 80;
        l = j / 11;
        gd_mon = j + 2 - (12 * l);
        gd_year = 100 * (n - 49) + i + l;

    }
}
//bad days to start new things from ha'ari
/*
nisan: 7,9,11,16,21,24
iyar: 5,7,15,22
sivan: 1,6,9,26
tamuz: 14,15,17,20,29
av: 9,10,19,20,22,27
elul:9,17,28,29
tishrei: 6,10,28
heshvan: 7,11,15,21
kislev: 1,8
tevet: 1,2,4,6,7,11,17,20,24,25,26,27
shvat: 9,17,18,24,25,26
adar I,II: 3,15,17,18,28

*/