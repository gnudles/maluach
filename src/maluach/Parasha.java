/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import maluach.YDate.JewishDate;

/**
 *
 * @author orr
 */
public class Parasha
{

    final static boolean[][][] _join_flags =
    {
        {
            {
                true, true, true, true, false, true, true
            }, /* 1 be erez israel */
            {
                true, true, true, true, false, true, false
            }, /* 2 */
            {
                true, true, true, true, false, true, true
            }, /* 3 */
            {
                true, true, true, false, false, true, false
            }, /* 4 */
            {
                true, true, true, true, false, true, true
            }, /* 5 */
            {
                false, true, true, true, false, true, false
            }, /* 6 */
            {
                true, true, true, true, false, true, true
            }, /* 7 */
            {
                false, false, false, false, false, true, true
            }, /* 8 */
            {
                false, false, false, false, false, false, false
            }, /* 9 */
            {
                false, false, false, false, false, true, true
            }, /* 10 */
            {
                false, false, false, false, false, false, false
            }, /* 11 */
            {
                false, false, false, false, false, false, false
            }, /* 12 */
            {
                false, false, false, false, false, false, true
            }, /* 13 */
            {
                false, false, false, false, false, true, true
            }
        }, /* 14 */
        {
            {
                true, true, true, true, false, true, true
            }, /* 1 in diaspora */
            {
                true, true, true, true, false, true, false
            }, /* 2 */
            {
                true, true, true, true, true, true, true
            }, /* 3 */
            {
                true, true, true, true, false, true, false
            }, /* 4 */
            {
                true, true, true, true, true, true, true
            }, /* 5 */
            {
                false, true, true, true, false, true, false
            }, /* 6 */
            {
                true, true, true, true, false, true, true
            }, /* 7 */
            {
                false, false, false, false, true, true, true
            }, /* 8 */
            {
                false, false, false, false, false, false, false
            }, /* 9 */
            {
                false, false, false, false, false, true, true
            }, /* 10 */
            {
                false, false, false, false, false, true, false
            }, /* 11 */
            {
                false, false, false, false, false, true, false
            }, /* 12 */
            {
                false, false, false, false, false, false, true
            }, /* 13 */
            {
                false, false, false, false, true, true, true
            }  /* 14 */

        }
    };
    final static int[] double_reading =
    {
        22, 27, 29, 32, 39, 42, 51
    };
    final static String[] parashot =
    {
        "",
        //1-8
        "בראשית", "נח", "לך-לך", "וירא", "חיי-שרה", "תולדות", "ויצא", "וישלח",
        //9-17
        "וישב", "מקץ", "ויגש", "ויחי", "שמות", "וארא", "בא", "בשלח", "יתרו",
        //18-25
        "משפטים", "תרומה", "תצוה", "כי-תשא", "ויקהל", "פקודי", "ויקרא", "צו",
        //26-33
        "שמיני", "תזריע", "מצורע", "אחרי-מות", "קדושים", "אמור", "בהר", "בחקותי",
        //34-41
        "במדבר", "נשא", "בהעלותך", "שלח", "קרח", "חקת", "בלק", "פנחס",
        //42-49
        "מטות", "מסעי", "דברים", "ואתחנן", "עקב", "ראה", "שופטים", "כי-תצא",
        //50-54
        "כי-תבוא", "נצבים", "וילך", "האזינו", "וזאת הברכה"
    };
    final static String[] special_shabat =
    {
        "שקלים",
        "זכור",
        "פרה",
        "החודש",
        "הגדול"
    };
    private static final int SHABAT_SHKALIM = 0;
    private static final int SHABAT_ZAKHOR = 1;
    private static final int SHABAT_PARA = 2;
    private static final int SHABAT_HACHODESH = 3;
    private static final int SHABAT_HAGADOL = 4;

    static String parshiot4(YDate h)
    {
        YDate tweaked = YDate.createFrom(h);
        if (h.hd.dayInWeek() == 7)
        {
            tweaked.seekBy(6);
            if (tweaked.hd.monthID() == JewishDate.M_ID_NISAN) //maybe shabat hachodesh or shabat hagadol
            {
                if (tweaked.hd.dayInMonth() <= 7)
                {
                    return special_shabat[SHABAT_HACHODESH];
                }
                if (h.hd.dayInMonth() < 15 && h.hd.dayInMonth() > 7)
                {
                    return special_shabat[SHABAT_HAGADOL];
                }
            }
            if (tweaked.hd.monthID() == JewishDate.M_ID_ADAR
                || tweaked.hd.monthID() == JewishDate.M_ID_ADAR_II)//adar or adar II
            {
                if (tweaked.hd.dayInMonth() <= 7)
                {
                    return special_shabat[SHABAT_SHKALIM];
                }
                if (h.hd.dayInMonth() < 14 && h.hd.dayInMonth() > 7)
                {
                    return special_shabat[SHABAT_ZAKHOR];
                }
                if (h.hd.dayInMonth() > 16)
                {
                    return special_shabat[SHABAT_PARA];
                }
            }
        }
        return "";
    }

    static String GetParashaFor(JewishDate h, boolean diaspora)
    {
        int pnum = GetParashaInt(h, diaspora);
        if (pnum >= 55)
        {
            pnum -= 55;
            return "פרשת " + parashot[double_reading[pnum]] + ", " + parashot[double_reading[pnum] + 1];
        }
        if (parashot[pnum].length() > 0)
        {
            return "פרשת " + parashot[pnum];
        }
        return "";
    }

    static int GetParashaInt(JewishDate h, boolean diaspora)
    {
        final boolean[][] join_flags = _join_flags[diaspora ? 1 : 0];
        int hd_mon_id = h.monthID();
        int hd_day = h.dayInMonth();
        if (hd_mon_id == JewishDate.M_ID_TISHREI)
        {
            if (hd_day == 22)
            {
                return 54;		/* simhat tora  */

            }
            if (hd_day == 23 && diaspora)
            {
                return 54;
            }
        }
        /* if not shabat return none */
        if (h.dayInWeek() != 7)
        {
            return 0;
        }

        int reading;
        int hd_weeks = h.week();
        int hd_new_year_dw = h.yearWeekDay();
        int hd_year_type = h.getYearTypeWeekDayLength();
        int hd_size_of_year = h.yearLength();
        switch (hd_weeks)
        {
            case 1:
                if (hd_new_year_dw == 7)
                {
                    /* Rosh hashana */
                    return 0;
                }
                else if ((hd_new_year_dw == 2) || (hd_new_year_dw == 3))
                {
                    return 52;
                }
                else /* if (h->hd_new_year_dw == 5) */

                {
                    return 53;
                }

            case 2:
                if (hd_new_year_dw == 5)
                {
                    /* Yom kippur */
                    return 0;
                }
                else
                {
                    return 53;
                }

            case 3:
                /* Succot */
                return 0;
            case 4:
                if (hd_new_year_dw == 7)
                {
                    /* Simhat tora in israel */
                    if (!diaspora)
                    {
                        return 54;
                    }
                    /* Not simhat tora in diaspora */
                    return 0;
                }
                else
                {
                    return 1;
                }
            default:
                /* simhat tora on week 4 bereshit too */
                reading = hd_weeks - 3;

                /* was simhat tora on shabat ? */
                if (hd_new_year_dw == 7)
                {
                    reading = reading - 1;
                }

                /* no joining */
                if (reading < 22)
                {
                    return reading;
                }

                /* pesach */
                if ((hd_mon_id == JewishDate.M_ID_NISAN) && (hd_day > 14))
                {
                    if (diaspora && (hd_day <= 22))
                        return 0;
                    if (!diaspora && (hd_day < 22))
                        return 0;

                }

                /* Pesach allways removes one */
                if (((hd_mon_id == JewishDate.M_ID_NISAN) && (hd_day > 21)) || (hd_mon_id > JewishDate.M_ID_NISAN))
                {
                    reading--;
                    /* on diaspora, shmini of pesach may fall on shabat if next new year is on shabat */
                    if (diaspora && (((hd_new_year_dw + hd_size_of_year) % 7) == 2))
                    {
                        reading--;
                    }

                }
                /* on diaspora, shavot may fall on shabat if next new year is on shabat */
                if (diaspora
                    && ((hd_mon_id > JewishDate.M_ID_SIVAN) || (hd_mon_id == JewishDate.M_ID_SIVAN && hd_day >= 7))
                    && ((hd_new_year_dw + hd_size_of_year) % 7) == 0)
                {
                    if (hd_mon_id == JewishDate.M_ID_SIVAN && hd_day == 7)
                    {
                        return 0;
                    }
                    else
                    {
                        reading--;
                    }
                }
                /* joining */
                if (join_flags[hd_year_type - 1][0] && (reading >= 22))
                {
                    if (reading == 22)
                    {
                        return 55;
                    }
                    else
                    {
                        reading++;
                    }
                }
                if (join_flags[hd_year_type - 1][1] && (reading >= 27))
                {
                    if (reading == 27)
                    {
                        return 56;
                    }
                    else
                    {
                        reading++;
                    }
                }
                if (join_flags[hd_year_type - 1][2] && (reading >= 29))
                {
                    if (reading == 29)
                    {
                        return 57;
                    }
                    else
                    {
                        reading++;
                    }
                }
                if (join_flags[hd_year_type - 1][3] && (reading >= 32))
                {
                    if (reading == 32)
                    {
                        return 58;
                    }
                    else
                    {
                        reading++;
                    }
                }

                if (join_flags[hd_year_type - 1][4] && (reading >= 39))
                {
                    if (reading == 39)
                    {
                        return 59;
                    }
                    else
                    {
                        reading++;
                    }
                }
                if (join_flags[hd_year_type - 1][5] && (reading >= 42))
                {
                    if (reading == 42)
                    {
                        return 60;
                    }
                    else
                    {
                        reading++;
                    }
                }
                if (join_flags[hd_year_type - 1][6] && (reading >= 51))
                {
                    if (reading == 51)
                    {
                        return 61;
                    }
                    else
                    {
                        reading++;
                    }
                }
                break;
        }

        return reading;

    }
}
