/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import maluach.YDate.JewishDate;

public class DailyLimud
{

    //(*)DAF YOMI SHAS
    //(*)yerushalmi
    //HALACH YOMIT - 3 SEIFIM + KITZUR SHULCHAN ARUCH 1613 PER DAY SINCE NINTH CYCLE. NINTH CYCLE STARTS 16 KISLEV 5750
    //RAMBAM YOMI - 3 PRAKIM/PEREK
    //HASHLAMAT MISHNAIOT - ONE MISHNA PER DAY
    //(*)MISHNA YOMIT - 2 MISHNAS PER DAY - 2096 DAYS PER CYCLE, FIRST CYCLE AT 1 SIVAV 5707
    //MISHNA CHAPTER - ONE PEREK PER DAY
    //MISHNA BRURA - DAF YOMI/AMUD YOMI
    //TANACH - PEREK YOMI
    //TANIA - 
    /** number of chapters in each masechet.
     * 
     */
    static final byte[] mishna_prakim
            = {
                9, 8, 7, 9, 10, 11, 5, 5, 4, 3, 4, 24, 10, 10, 8, 8, 5, 5, 4, 4, 4, 3, 3, 16, 13, 11, 9, 9, 9, 4, 10, 10, 10, 11, 3, 8, 8, 5, 6, 3, 14, 13, 12, 9, 9, 7, 6, 6, 7, 5, 3, 30, 18, 14, 12, 10, 10, 10, 6, 5, 4, 4, 3
            };
    /**
     * number of mishnayot in each masechet.
     */
    static final short[] mishna_mishnayot
            = {
                57, 69, 53, 77, 89, 101, 40, 57, 38, 35, 39, 139, 96, 89, 52, 61, 53, 42, 35, 34, 33, 24, 23, 128, 111, 90, 60, 67, 75, 47, 79, 101, 86, 71, 34, 62, 74, 50, 108, 20, 101, 93, 74, 73, 50, 35, 43, 38, 34, 34, 15, 254, 134, 115, 96, 92, 71, 79, 54, 32, 26, 22, 28
            };
    /**
     * number of mishnayot in each seder.
     */
    static final short[] seder_mishnayot 
            = {
                655, 681, 578, 685, 590, 1003
            };
/**
 * number of mishnayot in each chapter in each masechet.
 */
    static final byte[][] mishnayot_in_prakim
            = {
                {
                    5, 8, 6, 7, 5, 8, 5, 8, 5
                },//Berachot

                {
                    6, 8, 8, 11, 8, 11, 8, 9
                },//Pe'ah

                {
                    4, 5, 6, 7, 11, 12, 8
                },//Demai

                {
                    9, 11, 7, 9, 8, 9, 8, 6, 10
                },//Kil'ayim

                {
                    8, 10, 10, 10, 9, 6, 7, 11, 9, 9
                },//Shevi'it

                {
                    10, 6, 9, 13, 9, 6, 7, 12, 7, 12, 10
                },//Terumot

                {
                    8, 8, 10, 6, 8
                },//Ma'aserot

                {
                    7, 10, 13, 12, 15
                },//Ma'aser Sheni

                {
                    9, 8, 10, 11
                },//Challah

                {
                    9, 17, 9
                },//Orlah

                {
                    11, 11, 12, 5
                },//Bikkurim

                {
                    11, 7, 6, 2, 4, 10, 4, 7, 7, 6, 6, 6, 7, 4, 3, 8, 8, 3, 6, 5, 3, 6, 5, 5
                },//Shabbat

                {
                    10, 6, 9, 11, 9, 10, 11, 11, 4, 15
                },//Eruvin

                {
                    7, 8, 8, 9, 10, 6, 13, 8, 11, 9
                },//Pesachim

                {
                    7, 5, 4, 9, 6, 6, 7, 8
                },//Shekalim

                {
                    8, 7, 11, 6, 7, 8, 5, 9
                },//Yoma

                {
                    11, 9, 15, 10, 8
                },//Sukkah

                {
                    10, 10, 8, 7, 7
                },//Beitzah

                {
                    9, 8, 9, 9
                },//Rosh Hashana

                {
                    7, 10, 9, 8
                },//Ta'anit

                {
                    11, 6, 6, 10
                },//Megillah

                {
                    10, 5, 9
                },//Mo'ed Katan

                {
                    8, 7, 8
                },//Chagigah

                {
                    4, 10, 10, 13, 6, 6, 6, 6, 6, 9, 7, 6, 13, 9, 10, 7
                },//Yevamot

                {
                    10, 10, 9, 12, 9, 7, 10, 8, 9, 6, 6, 4, 11
                },//Ketubot

                {
                    4, 5, 11, 8, 6, 10, 9, 7, 10, 8, 12
                },//Nedarim

                {
                    7, 10, 7, 7, 7, 11, 4, 2, 5
                },//Nazir

                {
                    9, 6, 8, 5, 5, 4, 8, 7, 15
                },//Sotah

                {
                    6, 7, 8, 9, 9, 7, 9, 10, 10
                },//Gittin

                {
                    10, 10, 13, 14
                },//Kiddushin

                {
                    4, 6, 11, 9, 7, 6, 7, 7, 12, 10
                },//Baba Kamma

                {
                    8, 11, 12, 12, 11, 8, 11, 9, 13, 6
                },//Baba Metzia

                {
                    6, 14, 8, 9, 11, 8, 4, 8, 10, 8
                },//Baba Batra

                {
                    6, 5, 8, 5, 5, 6, 11, 7, 6, 6, 6
                },//Sanhedrin

                {
                    10, 8, 16
                },//Makkot

                {
                    7, 5, 11, 13, 5, 7, 8, 6
                },//Shevu'ot

                {
                    14, 10, 12, 12, 7, 3, 9, 7
                },//Eduyot

                {
                    9, 7, 10, 12, 12
                },//Avodah Zarah

                {
                    18, 16, 18, 22, 23, 11
                },//Avot

                {
                    5, 7, 8
                },//Horayot

                {
                    4, 5, 6, 6, 8, 7, 6, 12, 7, 8, 8, 6, 8, 10
                },//Zevachim

                {
                    4, 5, 7, 5, 9, 7, 6, 7, 9, 9, 9, 5, 11
                },//Menachot

                {
                    7, 10, 7, 7, 5, 7, 6, 6, 8, 4, 2, 5
                },//Chullin

                {
                    7, 9, 4, 10, 6, 12, 7, 10, 8
                },//Bechorot

                {
                    4, 6, 5, 4, 6, 5, 5, 7, 8
                },//Arachin

                {
                    6, 3, 5, 4, 6, 5, 6
                },//Temurah

                {
                    7, 6, 10, 3, 8, 9
                },//Keritot

                {
                    4, 9, 8, 6, 5, 6
                },//Me'ilah

                {
                    4, 5, 9, 3, 6, 4, 3
                },//Tamid

                {
                    9, 6, 8, 7, 4
                },//Middot

                {
                    4, 5, 6
                },//Kinnim

                {
                    9, 8, 8, 4, 11, 4, 6, 11, 8, 8, 9, 8, 8, 8, 6, 8, 17, 9, 10, 7, 3, 10, 5, 17, 9, 9, 12, 10, 8, 4
                },//Kelim

                {
                    8, 7, 7, 3, 7, 7, 6, 6, 16, 7, 9, 8, 6, 7, 10, 5, 5, 10
                },//Oholot

                {
                    6, 5, 8, 11, 5, 8, 5, 10, 3, 10, 12, 7, 12, 13
                },//Nega'im

                {
                    4, 5, 11, 4, 9, 5, 12, 11, 9, 6, 9, 11
                },//Parah

                {
                    9, 8, 8, 13, 9, 10, 9, 9, 9, 8
                },//Tahorot

                {
                    8, 10, 4, 5, 6, 11, 7, 5, 7, 8
                },//Mikva'ot

                {
                    7, 7, 7, 7, 9, 14, 5, 4, 11, 8
                },//Niddah

                {
                    6, 11, 8, 10, 11, 8
                },//Machshirin

                {
                    6, 4, 3, 7, 12
                },//Zavim

                {
                    5, 8, 6, 7
                },//Tevul Yom

                {
                    5, 4, 5, 8
                },//Yadayim

                {
                    6, 10, 12
                }//Uktzin

            };
    static final String[][] seder_name
            = {
                {
                    "�����",
                    "����",
                    "����",
                    "������",
                    "�����",
                    "�����"
                },
                {
                    "Zeraim",
                    "Moed",
                    "Nashim",
                    "Nezikin",
                    "Kadashim",
                    "Tahorot"
                }

            };
    /**
     * number of masechtot in each seder.
     */
    static final byte[] seder_masechet
            = {
                11, 12, 7, 10, 11, 12
            };
    static final String[][] mishna_name
            = {
                {
                    //Zeraim
                    "�����",//0
                    "���",//1
                    "����",//2
                    "�����",//3
                    "������",//4
                    "������",//5
                    "������",//6
                    "���� ���",//7
                    "���",//8
                    "����",//9
                    "������",//10
                    //Moed
                    "���",//11
                    "�������",//12
                    "�����",//13
                    "�����",//14
                    "����",//15
                    "����",//16
                    "����",//17
                    "��� ����",//18
                    "�����",//19
                    "�����",//20
                    "���� ���",//21
                    "�����",//22
                    //Nashim
                    "�����",//23
                    "������",//24
                    "�����",//25
                    "����",//26
                    "����",//27
                    "�����",//28
                    "�������",//29
                    //Nezikin
                    "��� ���",//30
                    "��� �����",//31
                    "��� ����",//32
                    "�������",//33
                    "����",//34
                    "������",//35
                    "�����",//36
                    "����� ���",//37
                    "����",//38
                    "������",//39
                    //Kadashim
                    "�����",//40
                    "�����",//41
                    "�����",//42
                    "������",//43
                    "�����",//44
                    "�����",//45
                    "������",//46
                    "�����",//47
                    "����",//48
                    "����",//49
                    "����",//50
                    //Tahorot
                    "����",//51
                    "�����",//52
                    "�����",//53
                    "���",//54
                    "�����",//55
                    "������",//56
                    "���",//57
                    "�������",//58
                    "����",//59
                    "���� ���",//60
                    "����",//61
                    "������",//62
                },
                {
                    "Berachot",
                    "Pe'ah",
                    "Demai",
                    "Kil'ayim",
                    "Shevi'it",
                    "Terumot",
                    "Ma'aserot",
                    "Ma'aser Sheni",
                    "Challah",
                    "Orlah",
                    "Bikkurim",
                    "Shabbat",
                    "Eruvin",
                    "Pesachim",
                    "Shekalim",
                    "Yoma",
                    "Sukkah",
                    "Beitzah",
                    "Rosh Hashana",
                    "Ta'anit",
                    "Megillah",
                    "Mo'ed Katan",
                    "Chagigah",
                    "Yevamot",
                    "Ketubot",
                    "Nedarim",
                    "Nazir",
                    "Sotah",
                    "Gittin",
                    "Kiddushin",
                    "Baba Kamma",
                    "Baba Metzia",
                    "Baba Batra",
                    "Sanhedrin",
                    "Makkot",
                    "Shevu'ot",
                    "Eduyot",
                    "Avodah Zarah",
                    "Avot",
                    "Horayot",
                    "Zevachim",
                    "Menachot",
                    "Chullin",
                    "Bechorot",
                    "Arachin",
                    "Temurah",
                    "Keritot",
                    "Me'ilah",
                    "Tamid",
                    "Middot",
                    "Kinnim",
                    "Kelim",
                    "Oholot",
                    "Nega'im",
                    "Parah",
                    "Tahorot",
                    "Mikva'ot",
                    "Niddah",
                    "Machshirin",
                    "Zavim",
                    "Tevul Yom",
                    "Yadayim",
                    "Uktzin"
                }

            };


    private static final int mishnayomit_first_cycle = 2084329;


    public static String MishnaYomit(int d, boolean show_seder, boolean Heb) {
        if (d < mishnayomit_first_cycle) {
            return "";
        }
        int cycle;
        int offset;
        cycle = 2096;
        offset = (d - mishnayomit_first_cycle) % cycle;
        offset *= 2;
        int i = 0;
        int mishna_mas = 0; //current masechet
        int mishna_mas_max = 0; //the next masechet after the last in the current seder
        int chapters = 0;
        int seder = 0;
        int last_added = 0;
        while (seder < 6) {
            last_added = seder_mishnayot[seder];
            mishna_mas_max += seder_masechet[seder];
            if (i + last_added > offset) {
                break;
            }
            mishna_mas = mishna_mas_max;
            i += last_added;
            seder++;
        }
        while (mishna_mas < mishna_mas_max) {
            last_added = mishna_mishnayot[mishna_mas];
            if (i + last_added > offset) {
                break;
            }
            i += last_added;
            mishna_mas++;
        }
        while (chapters < mishna_prakim[mishna_mas]) {
            last_added = mishnayot_in_prakim[mishna_mas][chapters];
            if (i + last_added > offset) {
                break;
            }
            i += last_added;
            chapters++;
        }
        int misnayot_in_perek = last_added;
        String out_str = "";
        if (show_seder) {
            out_str = seder_name[Heb ? 0 : 1][seder] + " ";
        }
        out_str += mishna_name[Heb ? 0 : 1][mishna_mas];
        if (Heb) {
            out_str += " ��� " + Format.HebIntSubString(chapters + 1, false, false)
                    + " ���� " + Format.HebIntSubString((offset - i) + 1, false, false);
        }
        else {
            out_str += " Chapter " + String.valueOf(chapters + 1)
                    + " Mishna " + String.valueOf(offset - i + 1);
        }
        out_str += ", ";
        if ((offset - i) + 1 == misnayot_in_perek) {
            if (chapters + 1 == mishna_prakim[mishna_mas]) {
                if (show_seder) {
                    if (mishna_mas + 1 == mishna_mas_max) {
                        out_str += seder_name[Heb ? 0 : 1][seder + 1] + " ";
                    }
                }

                out_str += mishna_name[Heb ? 0 : 1][mishna_mas + 1] + " ";
                if (Heb) {
                    out_str += "��� � ���� �";
                }
                else {
                    out_str += "Chapter 1 Mishna 1";
                }

            }
            else {
                if (Heb) {
                    out_str += "��� " + Format.HebIntSubString(chapters + 2, false, false)
                            + " ���� �";
                }
                else {
                    out_str += "Chapter " + String.valueOf(chapters + 2)
                            + " Mishna 1 ";
                }
            }
        }
        else {
            if (Heb) {
                out_str += Format.HebIntSubString(offset - i + 2, false, false);
            }
            else {
                out_str += String.valueOf(offset - i + 2);

            }
        }
        return out_str;

    }
    
        /**
     * the index for the name of each Bavli masechet in the strings array of mishna_name
     */
static final int[] bavli_name_index
            = {
                
                    0,
                    11,
                    12,
                    13,
                    14,
                    15,
                    16,
                    17,
                    18,
                    19,
                    20,
                    21,
                    22,
                    23,
                    24,
                    25,
                    26,
                    27,
                    28,
                    29,
                    30,
                    31,
                    32,
                    33,
                    34,
                    35,
                    37,
                    39,
                    40,
                    41,
                    42,
                    43,
                    44,
                    45,
                    46,
                    47,
                    50,
                    48,
                    49,
                    57
                };
/**
 * number of pages in each Bavli masechet.
 */
    static final int[] bavli_masechet_length
            = {
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
    /**
     * the first page of each masechet in the Bavli's Talmud.
     */
    static final int[] bavli_masechet_begin // page start. (usually Daf Bet)
            = {
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

    private static final int bavli_first_cycle = 2075677; //in days since beginning
    private static final int bavli_cycle_length_old = 2702;//the length of each of the first seven cycles
    private static final int bavli_old_cycles = 7;//the length of each of the first seven cycles
    private static final int bavli_cycle_length_new = 2711;//the new length of each cycle.
    private static final int bavli_eighth_cycle = bavli_first_cycle + bavli_old_cycles * bavli_cycle_length_old;
    
    public static String getBavliDafYomi(int d, boolean Heb) {
        //if last daf of Meilah, first daf of Kinnim
        //if last daf of Kinnim, first daf of Tamid
        if (d < bavli_first_cycle) {
            return "";
        }
        int cycle;
        int offset;
        if (d < bavli_eighth_cycle) {
            cycle = bavli_cycle_length_old;
            offset = (d - bavli_first_cycle) % cycle;
        }
        else {
            cycle = bavli_cycle_length_new;
            offset = (d - bavli_eighth_cycle) % cycle;
        }
        int daf_counter = 0;
        for (int masechet = 0; masechet < bavli_masechet_length.length; masechet++) {
            daf_counter += bavli_masechet_length[masechet];
            if (cycle == bavli_cycle_length_old && masechet == 4) //shekalim
            {
                daf_counter -= 9; // 21 - 9 = 12
            }
            if (offset < daf_counter)//found it!
            {
                offset -= (daf_counter - bavli_masechet_length[masechet]);
                String page_num;
                if (Heb) {
                    page_num = Format.HebIntString(offset + bavli_masechet_begin[masechet], false);
                }
                else {
                    page_num = String.valueOf(offset + bavli_masechet_begin[masechet]);
                }
                String masechet_str = mishna_name[Heb ? 0 : 1][bavli_name_index[masechet]];
                if (offset == bavli_masechet_length[masechet] - 1 && (masechet == 35 || masechet == 36))//Meilah Kinnim
                {
                    masechet_str += ", " + mishna_name[Heb ? 0 : 1][bavli_name_index[masechet+1]];
                }
                return masechet_str + " " + page_num;
            }
        }
        return "";
    }

    /**
     * indices for mishna name array.
     */
    private final static int masechtotYerushalmiIndex[] = {0, 1, 2, 3, 4,
        5, 6, 7, 8, 9, 10, 11, 12, 13,
        17, 18, 15, 16, 19, 14, 20, 22, 21,
        23, 24, 27, 25, 26, 28, 29, 30, 31,
        32, 33, 34, 35, 37, 39, 57};
    /**
     * number of pages in each masechta.
     */
    private final static int[] yerushalmi_masechet_length = {
        68, 37, 34, 44, 31, 59, 26, 33, 28, 20, 13, 92, 65, 71, 22, 22, 42, 26, 26, 33, 34, 22,
        19, 85, 72, 47, 40, 47, 54, 48, 44, 37, 34, 44, 9, 57, 37, 19, 13};
    private static final int yerushalmi_first_cycle = 2096275;//15 shevat 5740 (Febuary, 2, 1980) - days since beginning.
    private static final int yerushalmi_cycle_length = 1554;
    private final static JewishDate yerushalmi_jd = new JewishDate(yerushalmi_first_cycle);

    public static String getYerushalmiDafYomi(int d, boolean Heb) {
        if (d < yerushalmi_first_cycle) {
            return "";
        }
        JewishDate jd = new JewishDate(d);
        if (jd.isKippurDay() || jd.isNineAv()) {
            return "";
        }

        int offset = d - yerushalmi_first_cycle;
        // subtract the number of nine in Av and Kippur day from cycle begin till now.
        offset -= countNoLimudDays(yerushalmi_jd, jd);
        offset %= yerushalmi_cycle_length;
        int daf_counter = 0;
        for (int masechet = 0; masechet < yerushalmi_masechet_length.length; masechet++) {
            daf_counter += yerushalmi_masechet_length[masechet];
            if (offset < daf_counter)//found it!
            {
                offset -= (daf_counter - yerushalmi_masechet_length[masechet]);
                String page_num;
                //Daf in yerushalmi starts from Alef, not from Bet as Bavli.
                if (Heb) {
                    page_num = Format.HebIntString(offset + 1, false);
                }
                else {
                    page_num = String.valueOf(offset + 1);
                }
                String masechet_str = mishna_name[Heb ? 0 : 1][masechtotYerushalmiIndex[masechet]];
                return masechet_str + " " + page_num;
            }
        }
        return "";

    }

    private static int countNoLimudDays(JewishDate jd_from, JewishDate jd_to) {
        if (jd_to.daysSinceBeginning() <= jd_from.daysSinceBeginning()) {
            return 0;
        }

        int res = 0;
        if (jd_to.year() == jd_from.year()) {
            int kipp = 9; // kippur day in year
            if (jd_to.dayInYear() > kipp && jd_from.dayInYear() <= kipp) {
                res++;
            }
            int nine_av = jd_to.nineAvDayInYear(); // nine av
            if (jd_to.dayInYear() > nine_av && jd_from.dayInYear() <= nine_av) {
                res++;
            }
        }
        else {
            res += (jd_to.year() - jd_from.year() - 1) * 2;
            {
                int kipp_to = 9; // kippur
                if (jd_to.dayInYear() > kipp_to) {
                    res++;
                }
                int nine_av_to = jd_to.nineAvDayInYear(); //nine av
                if (jd_to.dayInYear() > nine_av_to) {
                    res++;
                }
            }
            {
                int kipp_from = 9; // kippur
                if (jd_from.dayInYear() <= kipp_from) {
                    res++;
                }
                int nine_av_from = jd_from.nineAvDayInYear(); //nine av
                if (jd_from.dayInYear() <= nine_av_from) {
                    res++;
                }
            }
        }
        return res;
    }
}
