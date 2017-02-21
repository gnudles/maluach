/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import maluach.YDate.JewishDate;


public class TorahReading
{
    //based on shulhan aruch ORACH HAIM SIMAN 428 SEIF 4
    final static int[] double_reading =
    {
        22, 27, 29, 32, 39, 42, 51
    };
    /* outside israel!:
    *  joining   year type      1   2   3   4   5   6   7
    * 22 - Vayakhel Pekudei     V   V   V   V   V   X   V
    * 27 - Tazria Metzora       V   V   V   V   V   V   V
    * 29 - Achrei-Mot Kedoshim  V   V   V   V   V   V   V
    * 32 - Behar Bechukotai     V   V   V   V   V   V   V
    **39 - Chukat Balak         X   X   V   X   V   X   X
    * 42 - Matot Mas'ei         V   V   V   V   V   V   V
    * 51 - Nitzavim Vayelech    V   X   V   X   V   X   V
    * Compatible with Israel    +   +   -   -   -   +   -
    *  joining   year type      8   9   10  11  12  13  14
    * 22 - Vayakhel Pekudei     X   X   X   X   X   X   X
    * 27 - Tazria Metzora       X   X   X   X   X   X   X
    * 29 - Achrei-Mot Kedoshim  X   X   X   X   X   X   X
    * 32 - Behar Bechukotai     X   X   X   X   X   X   X
    **39 - Chukat Balak         V   X   X   X   X   X   V
    * 42 - Matot Mas'ei         V   X   V   V   V   X   V
    * 51 - Nitzavim Vayelech    V   X   V   X   X   V   V
    * Compatible with Israel    -   +   +   -   -   +   -
    * In Israel:
    *  joining   year type      3   4   5   7   8   11  12  14
    * 22 - Vayakhel Pekudei     V   V   V   V   X   X   X   X
    * 27 - Tazria Metzora       V   V   V   V   X   X   X   X
    * 29 - Achrei-Mot Kedoshim  V   V   V   V   X   X   X   X
    * 32 - Behar Bechukotai     V   X   V   V   X   X   X   X
    **39 - Chukat Balak         X   X   X   X   X   X   X   X
    * 42 - Matot Mas'ei         V   V   V   V   V   X   X   V
    * 51 - Nitzavim Vayelech    V   X   V   V   V   X   X   V
    you can obtain the joining in israel by copying the joining outside IL and removing Chukat Balak joining.
    except year type 4,11,12 where you should remove Behar Bechukotai in 4 Matot Mas'ei in 11 12
    */
    final static byte[][][] SidraJoin=
    {
    { //Diaspora
        {1,1,1,1,0,1,1},//1
        {1,1,1,1,0,1,0},//2
        {1,1,1,1,1,1,1},//3
        {1,1,1,1,0,1,0},//4
        {1,1,1,1,1,1,1},//5
        {0,1,1,1,0,1,0},//6
        {1,1,1,1,0,1,1},//7
        {0,0,0,0,1,1,1},//8
        {0,0,0,0,0,0,0},//9
        {0,0,0,0,0,1,1},//10
        {0,0,0,0,0,1,0},//11
        {0,0,0,0,0,1,0},//12
        {0,0,0,0,0,0,1},//13
        {0,0,0,0,1,1,1} //14
    },
    {
        {1,1,1,1,0,1,1},//1
        {1,1,1,1,0,1,0},//2
        {1,1,1,1,0,1,1},//3
        {1,1,1,0,0,1,0},//4
        {1,1,1,1,0,1,1},//5
        {0,1,1,1,0,1,0},//6
        {1,1,1,1,0,1,1},//7
        {0,0,0,0,0,1,1},//8
        {0,0,0,0,0,0,0},//9
        {0,0,0,0,0,1,1},//10
        {0,0,0,0,0,0,0},//11
        {0,0,0,0,0,0,0},//12
        {0,0,0,0,0,0,1},//13
        {0,0,0,0,0,1,1} //14
    }
    };
    final static String[][] sidra =
    {
        {"",
        //1-8
        "בראשית", "נח", "לך-לך", "וירא", "חיי-שרה", "תולדות", "ויצא", "וישלח",
        //9-17
        "וישב", "מקץ", "ויגש", "ויחי", "שמות", "וארא", "בא", "בשלח", "יתרו",
        //18-25
        "משפטים", "תרומה", "תצוה", "כי-תשא", "ויקהל", "פקודי", "ויקרא", "צו",
        //26-33
        "שמיני", "תזריע", "מצורע", "אחרי-מות", "קדושים", "אמור", "בהר", "בחקותי",
        //34-41
        "במדבר", "נשא", "בהעלותך", "שלח-לך", "קרח", "חקת", "בלק", "פנחס",
        //42-49
        "מטות", "מסעי", "דברים", "ואתחנן", "עקב", "ראה", "שופטים", "כי-תצא",
        //50-54
        "כי-תבוא", "נצבים", "וילך", "האזינו", "וזאת הברכה"},
        {
            "",
        //1-8
        "Bereshit", "Noach", "Lech-Lecha", "Vayera", "Chayei-Sarah", "Toldot", "Vayetze", "Vayishlach",
        //9-17
        "Vayeshev", "Miketz", "Vayigash", "Vayechi", "Shemot", "Vaera", "Bo", "Beshalach", "Yitro",
        //18-25
        "Mishpatim", "Terumah", "Tetzaveh", "Ki-Tisa", "Vayakhel", "Pekudei", "Vayikra", "Tzav",
        //26-33
        "Shemini", "Tazria", "Metzora", "Achrei-Mot", "Kedoshim", "Emor", "Behar", "Bechukotai",
        //34-41
        "Bamidbar", "Naso", "Beha\'alotcha", "Shelach-Lecha", "Korach", "Chukat", "Balak", "Pinchas",
        //42-49
        "Matot", "Mas\'ei", "Devarim", "Vaetchanan", "Ekev", "Re\'eh", "Shoftim", "Ki-Tetze",
        //50-54
        "Ki-Tavo", "Nitzavim", "Vayelech", "Ha\'azinu", "Vezot Habracha"
        }
            
    };

    final static String[] special_shabat =
    {
        "שקלים",
        "זכור",
        "פרה",
        "החודש",
        "הגדול",
        "שירה"
    };
    private static final int SHABAT_SHKALIM = 0;
    private static final int SHABAT_ZAKHOR = 1;
    private static final int SHABAT_PARA = 2;
    private static final int SHABAT_HACHODESH = 3;
    private static final int SHABAT_HAGADOL = 4;
    private static final int SHABAT_SHIRA = 5;
    public static String parshiot4(YDate h)
    {
        YDate tweaked = YDate.createFrom(h);
        if (getShabbatBereshit(h.hd.yearLength(),h.hd.yearFirstDay())+15*7 == h.hd.daysSinceBeginning())
            return special_shabat[SHABAT_SHIRA];
        if (h.hd.dayInWeek() == 7)
        {
            tweaked.seekBy(6);
            if (tweaked.hd.monthID() == YDate.JewishDate.M_ID_NISAN) //maybe shabat hachodesh or shabat hagadol
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
            if (tweaked.hd.monthID() == YDate.JewishDate.M_ID_ADAR
                || tweaked.hd.monthID() == YDate.JewishDate.M_ID_ADAR_II)//adar or adar II
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
    static final int HOL_DAY=0;
    static final int HOL_DAY_MONDAY_THURSDAY=1;
    static final int SHABBAT_DAY=(1<<1);
    static final int ROSH_HODESH=(1<<2);
    static final int REGALIM=(1<<3);
    static final int REGALIM_DIASPORA=(1<<4);
    static final int CHANUKKAH=(1<<5);
    static final int PURIM=(1<<6);
    static final int SHOSHAN_PURIM=(1<<7);
    static final int TAANIT=(1<<8);
    static final int EREV_ROSH_HODESH=(1<<9);
    static final int NINE_AV=(1<<10);
    static final int KIPPUR=(1<<11);
    static final int ROSH_HASHANA=(1<<12);

    static int getDayType(JewishDate h)
    {
        byte [] events=YDateAnnual.getEvents(h.yearLength(), h.yearFirstDay(),false);
        int ev=events[h.dayInYear()];
        
        boolean rosh=(h.dayInMonth()==1 || h.dayInMonth()==30);
        boolean erev_rosh=(h.dayInMonth()==29);
        boolean chanukkah=(ev>=14 && ev<=21);
        boolean purim=(ev==25);
        boolean shoshan_purim=(ev==26);
        boolean sheni_hamishi=(h.dayInWeek() == 2 || h.dayInWeek() == 5);
        boolean shabbat=(h.dayInWeek() == 7);
        boolean four_taaniot=(ev==3 || ev==22 || ev==24 || ev==40 );
        boolean regalim=((ev>=29 && ev<=32)||(ev==37)||(ev==13) || (ev>=7 && ev<=11));
        boolean regalim_diasp=(ev==39 || ev==12 || ev==38 || ev==33 );
        boolean nine_av=(ev==41);
        boolean kippur=(ev==5);
        boolean rosh_hashana=(ev==1 || ev==2);
        
        int type=0;
        type+=rosh?ROSH_HODESH:0;
        type+=four_taaniot?TAANIT:0;
        type+=shabbat?SHABBAT_DAY:0;
        type+=chanukkah?CHANUKKAH:0;
        type+=nine_av?NINE_AV:0;
        type+=kippur?KIPPUR:0;
        type+=rosh_hashana?ROSH_HASHANA:0;
        type+=regalim?REGALIM:0;
        type+=(type==0 && sheni_hamishi)?HOL_DAY_MONDAY_THURSDAY:0;
        type+=erev_rosh?EREV_ROSH_HODESH:0;
        type+=purim?PURIM:0;
        type+=shoshan_purim?SHOSHAN_PURIM:0;
        type+=regalim_diasp?REGALIM_DIASPORA:0;
        
        
        return type;
    }
    public static String GetSidra(JewishDate h, boolean diaspora, boolean force)
    {
        int diy=h.dayInYear();
        int ydiw=h.yearFirstDay()%7;
        int simhat_torah=diaspora?23:22;
        int succot=15;
        int day_type=getDayType(h);
        int pnum = 0;
        if (diy+1==simhat_torah)
        {
            pnum=54;
        }
        else
        {
            byte [] sidra_array=calculateSidraArray(h.yearLength(),h.yearFirstDay(), diaspora);
            if ( (day_type & SHABBAT_DAY) != 0 )
                pnum=sidra_array[diy/7];
            
            if (( (day_type & HOL_DAY_MONDAY_THURSDAY) != 0  && (!(diaspora && (day_type & REGALIM_DIASPORA) != 0))) || force)
            {
                if (diy+1<=simhat_torah && diy+1>=succot)
                {
                    pnum=54;
                }
                else
                {
                    int sat=YDate.getNext(YDate.SATURDAY, diy+ydiw)-ydiw;
                    while (pnum==0)
                    {
                        pnum = sidra_array[sat/7];
                        if (pnum==0 && (sat/7)==2)
                            pnum=54;
                        sat+=7;
                    }
                }
            }
        }
        String lstr="";
        if (pnum < 0)
        {
            pnum = -pnum;
            lstr+= "פרשת " + sidra[0][pnum] + ", " + sidra[0][pnum + 1];
        }
        else
        {
            if (pnum != 0)
            {
                lstr+= "פרשת " + sidra[0][pnum];
            }
        }
        if ( (day_type & PURIM) != 0)
        {
            lstr+="\nבערי הפרזות ויבא עמלק";
        }
        if ( (day_type & SHOSHAN_PURIM) != 0)
        {
            lstr+="\nבערים המוקפות ויבא עמלק";
        }
        if ( (day_type & TAANIT) != 0)
        {
            lstr+="\nויחל משה";
        }
        if ( (day_type) == ROSH_HODESH)
        {
            lstr+="\nקריאה לר\"ח";
        }
        return lstr;
    }
    static byte [][][] sidra_reading = new byte [2][JewishDate.N_YEAR_TYPES][];//[diaspora][year_type][shabat]
    
    private static int getNextJoinPointer(byte[] joining, int jp)
    {
        for (;jp<joining.length;++jp)
            if (joining[jp]==1)
                break;
        return jp;
    }
    private static int getJoin(int jp)
    {
        if (jp>=double_reading.length)
            return 55;
        return double_reading[jp];
    }
    private static byte[] calculateSidraArray(int year_length, int year_first_day, boolean diaspora)
    {
        
        int year_diw=year_first_day%7; // can be only 1 2 4 6 (+1 = 2 3 5 7)
        int ldt=YDate.JewishDate.ld_year_type(year_length, year_diw+1);
        if (sidra_reading[diaspora?0:1][ldt-1] != null)
            return sidra_reading[diaspora?0:1][ldt-1];
        byte[] joining=SidraJoin[diaspora?0:1][ldt-1];
        
        int s=0;
        
        int diy=YDate.getNext(YDate.SATURDAY,year_diw)-year_diw;
        int shabats=(year_length-(diy)+6)/7;
        shabats++; // one for the next year
        byte [] reading=new byte[shabats];
        sidra_reading[diaspora?0:1][ldt-1]=reading;
        if ((year_diw>>2) == 0) //if monday or tuesday - pat bag
        {
            reading[s]=52;//Vayelech
            ++s;
            reading[s]=53;//Ha'azinu
            ++s;
            reading[s]=0;//none
            ++s;
            diy+=21;
        }
        else
        {
            if (year_diw==YDate.SATURDAY)
            {
                reading[s]=0;//none
                ++s;
                diy+=7;
            }
            reading[s]=53;//Ha'azinu
            ++s;
            reading[s]=0;//none
            ++s;
            reading[s]=0;//none
            ++s;
            diy+=21;
        }
        int pesah_day = YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_NISAN, 15);
        int pesah_length = diaspora?8:7;
        int azeret_day=50+pesah_day;
        int azeret_length = diaspora?2:1;
        int tr=1;
        //now s points to shabat bereshit
        int jp=0;
        jp=getNextJoinPointer(joining,jp);
        int next_join=getJoin(jp);
        while (s<shabats)
        {
            if ((diy>=pesah_day && diy<pesah_day+pesah_length)
                || (diy>= azeret_day && diy<azeret_day+azeret_length))
            {
                reading[s]=0;//none
                ++s;
                diy+=7;
            }
            else
            {
                if (tr==next_join)
                {
                    reading[s]=(byte)(-tr);
                    ++s;
                    diy+=7;
                    tr+=2;
                    jp=getNextJoinPointer(joining,jp+1);
                    next_join=getJoin(jp);
                }
                else
                {
                    reading[s]=(byte)tr;
                    ++tr;
                    ++s;
                    diy+=7;
                }
            }
        }
        return reading;
    }
    public static int getShabbatBereshit(int year_length, int year_first_day)
    {
        int bereshit_saturday=year_first_day;
        bereshit_saturday+=YDate.JewishDate.calculateDayInYearByMonthId(year_length, JewishDate.M_ID_TISHREI, 23);
        bereshit_saturday=YDate.getNext(YDate.SATURDAY, bereshit_saturday);
        return bereshit_saturday;
    }
}
