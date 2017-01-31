/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import maluach.YDate.JewishDate;

/**
 *
 * @author orr
 */
public class DailyLimud
{

    static final String[][] masechet_name =
    {
        {
            "ברכות",
            "שבת",
            "עירובין",
            "פסחים",
            "שקלים",
            "יומא",
            "סוכה",
            "ביצה",
            "ראש השנה",
            "תענית",
            "מגילה",
            "מועד קטן",
            "חגיגה",
            "יבמות",
            "כתובות",
            "נדרים",
            "נזיר",
            "סוטה",
            "גיטין",
            "קידושין",
            "בבא קמא",
            "בבא מציעא",
            "בבא בתרא",
            "סנהדרין",
            "מכות",
            "שבועות",
            "עבודה זרה",
            "הוריות",
            "זבחים",
            "מנחות",
            "חולין",
            "בכורות",
            "ערכין",
            "תמורה",
            "כריתות",
            "מעילה",
            "קנים",
            "תמיד",
            "מדות",
            "נדה"
        },
        {
            "Berachot",
            "Shabbat",
            "Eruvin",
            "Pesachim",
            "Shekalim",
            "Yoma",
            "Sukkah",
            "Beitzah",
            "Rosh Hashana",
            "Taanit",
            "Megillah",
            "Moed Katan",
            "Chagigah",
            "Yevamot",
            "Ketubot",
            "Nedarim",
            "Nazir",
            "Sotah",
            "Gitin",
            "Kiddushin",
            "Baba Kamma",
            "Baba Metzia",
            "Baba Batra",
            "Sanhedrin",
            "Makkot",
            "Shevuot",
            "Avodah Zarah",
            "Horayot",
            "Zevachim",
            "Menachot",
            "Chullin",
            "Bechorot",
            "Arachin",
            "Temurah",
            "Keritot",
            "Meilah",
            "Kinnim",
            "Tamid",
            "Midot",
            "Niddah",
        }
    };
    static final int[] masechet_length =
    {
        63, //Berachot
        156, //Shabbat
        104, //Eruvin
        120, //Pesachim
        21, //Shekalim - 12 in old cycles (first seven)
        87, //Yoma
        55, //Sukkah
        39, //Beitzah
        34, //Rosh Hashana
        30, //Taanit
        31, //Megillah
        28, //Moed Katan
        26, //Chagigah
        121, //Yevamot
        111, //Ketubot
        90, //Nedarim
        65, //Nazir
        48, //Sotah
        89, //Gitin
        81, //Kiddushin
        118, //Baba Kamma
        118, //Baba Metzia
        175, //Baba Batra
        112, //Sanhedrin
        23, //Makkot
        48, //Shevuot
        75, //Avodah Zarah
        13, //Horayot
        119, //Zevachim
        109, //Menachot
        141, //Chullin 
        60, //Bechorot
        33, //Arachin 
        33, //Temurah 
        27, //Keritot 
        21, //Meilah
        3, //Kinnim +1
        8, //Tamid +1
        4, //Midot 
        72//Niddah
    };
    static final int[] masechet_begin =
    {
        2, //Berachot
        2, //Shabbat
        2, //Eruvin
        2, //Pesachim
        2, //Shekalim
        2, //Yoma
        2, //Sukkah
        2, //Beitzah
        2, //Rosh Hashana
        2, //Taanit
        2, //Megillah
        2, //Moed Katan
        2, //Chagigah
        2, //Yevamot
        2, //Ketubot
        2, //Nedarim
        2, //Nazir
        2, //Sotah
        2, //Gitin
        2, //Kiddushin
        2, //Baba Kamma
        2, //Baba Metzia
        2, //Baba Batra
        2, //Sanhedrin
        2, //Makkot
        2, //Shevuot
        2, //Avodah Zarah
        2, //Horayot
        2, //Zevachim
        2, //Menachot
        2, //Chullin 
        2, //Bechorot
        2, //Arachin 
        2, //Temurah 
        2, //Keritot 
        2, //Meilah
        23, //Kinnim
        26, //Tamid 
        34, //Midot 
        2//Niddah
    };

    private static final int first_cycle=2075677;
    private static final int eight_cycle=first_cycle+7*2702;
    public static String getDafYomi(int d, boolean Heb)
    {
        //if last daf of Meilah, first daf of Kinnim
        //if last daf of Kinnim, first daf of Tamid
        if (d<first_cycle)
            return "";
        int cycle;
        int offset;
        if (d<eight_cycle)
        {
            cycle = 2702;
            offset=(d-first_cycle)%cycle;
        }
        else
        {
            cycle = 2711;
            offset=(d-eight_cycle)%cycle;
        }
        int daf_counter=0;
        for (int masechet=0;masechet<masechet_length.length;masechet++)
        {
            daf_counter+=masechet_length[masechet];
            if (cycle == 2702 && masechet== 4) //shekalim
            {
                daf_counter -= 9; // 21 - 9 = 12
            }
            if (offset<daf_counter)//found it!
            {
                offset-=(daf_counter-masechet_length[masechet]);
                String page_num;
                if (Heb)
                    page_num=Format.HebIntString(offset+masechet_begin[masechet], false);
                else
                    page_num=String.valueOf(offset+masechet_begin[masechet]);
                String masechet_str= masechet_name[Heb?0:1][masechet];
                if (offset==masechet_length[masechet]-1 && (masechet==35 || masechet== 36))//Meilah Kinnim
                {
                    masechet_str += ", " + masechet_name[Heb?0:1][masechet+1];
                }
                return masechet_str+" "+page_num;
            }
        }
        return "";
    }
}
