/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maluach;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 *
 * @author orr
 */
public class Parasha {
    final static boolean[][] join_flags =
		{
			{true, true, true, true, false, true, true}, /* 1 be erez israel */
			{true, true, true, true, false, true, false}, /* 2 */
			{true, true, true, true, false, true, true}, /* 3 */
			{true, true, true, false, false, true, false}, /* 4 */
			{true, true, true, true, false, true, true}, /* 5 */
			{false, true, true, true, false, true, false}, /* 6 */
			{true, true, true, true, false, true, true}, /* 7 */
			{false, false, false, false, false, true, true}, /* 8 */
			{false, false, false, false, false, false, false}, /* 9 */
			{false, false, false, false, false, true, true}, /* 10 */
			{false, false, false, false, false, false, false}, /* 11 */
			{false, false, false, false, false, false, false}, /* 12 */
			{false, false, false, false, false, false, true}, /* 13 */
			{false, false, false, false, false, true, true}  /* 14 */
		};
        final static int[] double_reading=
        {
            22,27,29,32,39,42,51
        };
	final static String[] parashot=
    {
        "",
        //1-8
	"בראשית","נח","לך-לך","וירא","חיי-שרה","תולדות","ויצא","וישלח", 
        //9-17
	"וישב","מקץ","ויגש","ויחי","שמות","וארא","בא","בשלח","יתרו",
        //18-25
	"משפטים","תרומה","תצוה","כי-תשא","ויקהל","פקודי","ויקרא","צו",
        //26-33
	"שמיני","תזריע","מצורע","אחרי-מות","קדושים","אמור","בהר","בחקותי",
        //34-41
	"במדבר","נשא","בהעלותך","שלח","קרח","חקת","בלק","פנחס",
        //42-49
	"מטות","מסעי","דברים","ואתחנן","עקב","ראה","שופטים","כי-תצא",
        //50-54
	"כי-תבוא","נצבים","וילך","האזינו","וזאת הברכה"
	};
        final static String[] special_shabat={
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
        static String parshiot4(Hdate h)
        {
            Hdate tweaked=new Hdate();
            tweaked.Set(h);
            if (h.get_day_in_week()==7)
            {
                tweaked.moveday(6);
                if (tweaked.get_hd_month()==7) //maybe shabat hachodesh or shabat hagadol
                {
                    if (tweaked.get_hd_day_in_month()<=7)
                        return special_shabat[SHABAT_HACHODESH];
                    if (h.get_hd_day_in_month()<15 && h.get_hd_day_in_month()>7)
                        return special_shabat[SHABAT_HAGADOL];
                }
                if (tweaked.get_hd_month()==6 || tweaked.get_hd_month()==14)//adar or adar II
                {
                    if (tweaked.get_hd_day_in_month()<=7)
                        return special_shabat[SHABAT_SHKALIM];
                    if (h.get_hd_day_in_month()<14 && h.get_hd_day_in_month()>7)
                        return special_shabat[SHABAT_ZAKHOR];
                    if (h.get_hd_day_in_month()>16)
                        return special_shabat[SHABAT_PARA];
                }
            }
            return "";
        }
        static String GetParashaFor(Hdate h)
        {
            int pnum=GetParashaInt(h);
            if (pnum>=55)
            {
                pnum-=55;
                return parashot[double_reading[pnum]]+", "+parashot[double_reading[pnum]+1];
            }
            return parashot[pnum];
        }
	static int GetParashaInt(Hdate h)
	{
            int hd_mon=h.get_hd_month();
            int hd_day=h.get_hd_day_in_month();
            if (hd_mon == 1)
		if (hd_day == 22 ) return 54;		/* simhat tora  */
            /* if not shabat return none */
            if (h.get_day_in_week() != 7)
            {
		return 0;
            }
            
            int reading;
            int hd_weeks=h.get_hd_weeks();
            int hd_new_year_dw=h.get_hd_new_year_dw();
            int hd_year_type=h.get_hd_year_type();
	switch (hd_weeks)
	{
	case  1:
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

	case  2:
		if (hd_new_year_dw == 5)
		{
			/* Yom kippur */
			return 0;
		}
		else
		{
			return 53;
		}

	case  3:
		/* Succot */
		return 0;
	case  4:
		if (hd_new_year_dw == 7)
		{
			/* Simhat tora in israel */
			return 54;
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
			reading = reading - 1;
		
		/* no joining */
		if (reading < 22)
		{
			return reading;
		}
		
		/* pesach */
		if ((hd_mon == 7) && (hd_day > 14))
		{
			if (hd_day < 22)
				return 0;
		}
		
		/* Pesach allways removes one */
		if (((hd_mon == 7) && (hd_day > 21)) || (hd_mon > 7 && hd_mon < 13))
		{
			reading--;

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
