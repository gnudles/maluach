/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;
import maluach.YDate.*;
/**
 *
 * @author orr
 */
public class YDateAnnual
{

    final static String[] holydays_str =
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
        "(יום המשפחה)",
        "יום הזכרון ליצחק רבין",//49

    };

    /**
     * Return Hebrew year type based on size and first week day of year.
     * year type | year length | Tishery 1 day of week
     * | 1       | 353         | 2  ph2
     * | 2       | 353         | 7  ph7
     * | 3       | 354         | 3  pk3
     * | 4       | 354         | 5  pk5
     * | 5       | 355         | 2  ps2
     * | 6       | 355         | 5  ps5
     * | 7       | 355         | 7  ps7
     * | 8       | 383         | 2  mh2
     * | 9       | 383         | 5  mh5
     * |10       | 383         | 7  mh7
     * |11       | 384         | 3  mk3
     * |12       | 385         | 2  ms2
     * |13       | 385         | 5  ms5
     * |14       | 385         | 7  ms7
     * 
     * @param size_of_year Length of year in days
     * @param year_first_dw First week day of year 
     * @return A number for year type (1..14)
     */
    static final int [][] event_db= 
    {// month_id,day,array index,# of days,jump/dhia(if #_days==1)
        {JewishDate.M_ID_TISHREI,1,1,2,1},//two days of rosh hashana
        {JewishDate.M_ID_TISHREI,3,3,1,1},//zom gdalia, dhia
        {JewishDate.M_ID_TISHREI,9,4,2,1},//yom kippur
        {JewishDate.M_ID_TISHREI,14,6,2,1},//sukkot
        {JewishDate.M_ID_TISHREI,16,9,5,0},//hol hamoed sukkot
        {JewishDate.M_ID_TISHREI,21,10,1,0},//hoshana raba
        {JewishDate.M_ID_TISHREI,22,13,1,0},//shmini azeret simhat_tora
        {JewishDate.M_ID_TISHREI,23,39,1,0},//isru hag
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
    static final int [][] event_db_diaspora= 
    {// month_id,day,array index,# of days,jump/dhia(if #_days==1)
        {JewishDate.M_ID_TISHREI,16,8,1,0},//sukkot II
        {JewishDate.M_ID_TISHREI,22,11,1,0},//shmini azeret
        {JewishDate.M_ID_TISHREI,23,12,1,0},//simhat_tora
        {JewishDate.M_ID_NISAN,16,30,1,0},//Pesah II
        {JewishDate.M_ID_NISAN,22,33,1,0},//Shmi'ni Pesah
        {JewishDate.M_ID_SIVAN,7,38,1,0},//Shavu'ot II
    };
    static int get_year_ld_type (int size_of_year, int year_first_dw)
    {
            /* Only 14 combinations of size and week day are posible */
            final int[] year_types =
                    {1, 0, 0, 2, 0, 3, 4, 0, 5, 0, 6, 7,
                    8, 0, 9, 10, 0, 11, 0, 0, 12, 0, 13, 14};

            int offset;

            /* convert size and first day to 1..24 number */
            /* 2,3,5,7 -> 1,2,3,4 */
            /* 353, 354, 355, 383, 384, 385 -> 0, 1, 2, 3, 4, 5 */
            offset = (year_first_dw + 1) / 2;
            offset = offset + 4 * ((size_of_year % 10 - 3) + (size_of_year / 10 - 35));

            /* some combinations are imposible */
            return year_types[offset - 1];
    }
    
    static int [][][] annual_events = new int [2][14][];//[diaspora][year_type][day_in_year]
    YDateAnnual(int year, int year_length, int year_first_day,boolean diaspora)
    {
        
        int year_ld_t= get_year_ld_type(year_length,year_first_day%7+1);
        initialize_year(diaspora,year_ld_t,year_length,year_first_day);

    }
    static void expandDB(int year_length,int year_first_day,final int [][] evdb, int [] year_events)
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
                int idx = evdb[ev][IDX_IDX];
                for (int l = 0; l < evdb[ev][IDX_LEN]; ++l)
                {
                    year_events[diy + l] = idx;
                    idx += evdb[ev][IDX_JMP];
                }
            }
        }
    }
    static void initialize_year(boolean diaspora,int year_ld_t,int year_length,int year_first_day)
    {
        if (annual_events[diaspora?1:0][year_ld_t-1]==null)
        {
            annual_events[diaspora?1:0][year_ld_t-1]=new int [year_length];
            expandDB(year_length,year_first_day,event_db, annual_events[diaspora?1:0][year_ld_t-1]);
            if (diaspora)
                expandDB(year_length,year_first_day,event_db_diaspora, annual_events[diaspora?1:0][year_ld_t-1]);
        }
    }

}
