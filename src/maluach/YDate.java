/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author orr
 */
public class YDate
{

    //חמה לבנה מאדים כוכב צדק נגה שבתאי
    //ראשון שני שלישי רביעי חמישי שישי שבת
    //חלם כצנש ראה רשי מסכת ברכות דף נט עמוד ב דיבור המתחיל שבתאי על חלוקת הכוכבים לימות השבוע

    static final int SUNDAY = 0;//Sun - Sunne in old english
    static final int MONDAY = 1;//Moon - M?na in old english
    static final int TUESDAY = 2;//Mars - T?w in old english
    static final int WEDNESDAY = 3;//Mercury - W?den in old english
    static final int THURSDAY = 4;//Jupiter - ?unor in old english
    static final int FRIDAY = 5;//Venus - frig in old english
    static final int SATURDAY = 6;//Saturn - S?tern in old english

    public static class GregorianDate
    {
        static final int DAYS_IN_400 = 146097;
        static final int DAYS_IN_4 = 1461;
        static final int DAYS_BEGINNING_1600 = 1957451;// days since beginning up to year 1.1.1600 (14 in tevet, 5360)
        static final int JULIAN_DAY_OFFSET=347997;
        static final int[] HUNDRED_OFFSET ={0,36525,36525*2-1,36525*3-2};
        private int year; // year 1 is the first year.
        private int month; // range 1..12, 13 - Adar I, 14 - Adar II
        private int day; // range 1..30
        private int year_length; // days in year - 365,366
        private int year_first_day; // 1.1.year from the beginning
        private int day_in_year; // days after year beginning. first day in year  is 0
        private boolean valid;
        GregorianDate(int year,int month,int day)
        {
            if (year>=1600 && year< 2300)
            {
                valid=true;
                this.year=year;
                this.month=month;
                this.day=day;
                this.year_first_day=days_until_year(this.year);
                this.year_length=isLeap(this.year)?366:365;
                this.day_in_year=calculateDayInYear(this.year_length,this.month,this.day);
            }
            else
            {
                valid=false;
            }
        }
        GregorianDate(int days)
        {
            if (days>=DAYS_BEGINNING_1600)
            {
                valid=true;
                boolean hdate_method=true;
                int gd_day;
                int gd_month;
                int gd_year;
                if (hdate_method)
                {
                    int l, n, i, j;
                    l = days+347997 + 68569;
                    n = (4 * l) / 146097;
                    l = l - (146097 * n + 3) / 4;
                    i = (4000 * (l + 1)) / 1461001;	/* that's 1,461,001 */
                    l = l - (1461 * i) / 4 + 31;
                    j = (80 * l) / 2447;
                    gd_day = l - (2447 * j) / 80;
                    l = j / 11;
                    gd_month = j + 2 - (12 * l);
                    gd_year = 100 * (n - 49) + i + l;
                }
                else
                {
                    days-=DAYS_BEGINNING_1600;
                    gd_year=1600+400*((days)/DAYS_IN_400);
                    days=days%DAYS_IN_400;
                    int h=(days*4)/146097;
                    if (h==0)
                    {
                        h=(days*4)/1461;
                        gd_year+=h;
                        days=days-(h*1461+3)/4;
                    }
                    else
                    {
                        gd_year+=100*h;
                        //h;
                        //gd_year+=h;
                    }
                    //gd_year=;
                    gd_month=1;
                    gd_day=1;
                }
                this.year=gd_year;
                this.month=gd_month;
                this.day=gd_day;
                this.year_first_day=days_until_year(this.year);
                this.year_length=isLeap(this.year)?366:365;
                this.day_in_year=calculateDayInYear(this.year_length,this.month,this.day);
                
            }
            else
            {
                valid=false;
            }
        }
        public int dayInMonth()
        {
            return this.day;
        }
        public int dayInYear()
        {
            return this.day_in_year;
        }
        public int year()
        {
            return this.year;
        }
        public int daysSinceBeginning()
        {
            return year_first_day+day_in_year;
        }
        public int JulianDay()
        {
            return daysSinceBeginning()+JULIAN_DAY_OFFSET;
        }
        private static final int[][] months_days_offsets=
        {
            {0 , 31 , 59 , 90 , 120 , 151 , 181 , 212 , 243 , 273 , 304 , 334 , 365 },
            {0 , 31 , 60 , 91 , 121 , 152 , 182 , 213 , 244 , 274 , 305 , 335 , 366 }
        };
        private void setMonthDay(int days)
        {
            int mo_year_t=isLeap(this.year)?1:0;
            int m=days*2/61;
            if (months_days_offsets[mo_year_t][m]>days)
                m--;
            else if (months_days_offsets[mo_year_t][m+1]<=days)
                m++;
            this.month=m+1;
            this.day=days-months_days_offsets[mo_year_t][m]+1;
        }
        private static int calculateDayInYear(int year_length,int month,int day)
        {
            int mo_year_t=year_length-365;
            return months_days_offsets[mo_year_t][month-1]+day-1;
        }
        private static int calculateDayInYear(boolean leap_year,int month,int day)
        {
            return months_days_offsets[leap_year?1:0][month-1]+day-1;
        }
        public static boolean isLeap(int year)
        {
            if (year % 400 == 0)
            {
                return true;
            }
            else
            {
                if (year % 4 == 0 && year % 100 != 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        public static int days_until_year(int year)
        {
            
            int years_since_1600=year-1600;
            int year_first_day=DAYS_BEGINNING_1600;
            year_first_day+=DAYS_IN_400*(years_since_1600/400);
            year=years_since_1600%400;
            
            year_first_day+=HUNDRED_OFFSET[year/100];
            if (year/100==0)
            {
                year_first_day+=((year+3)/4)+year*365;
                return year_first_day;
            }
            year=year%100;
            //if the assumption (-1)/4 ==0 is incorrect (like in python), use the following code.
            if ((-1)/4!=0)
                year_first_day+=(((year-1<0)?0:year-1)/4)+year*365;
            else
                year_first_day+=((year-1)/4)+year*365;
            return year_first_day;
        }
                
        
    }
    public static class JewishDate
    {

        
        static final int HOUR = 1080;
        static final int DAY = (24 * HOUR);
        static final int WEEK = (7 * DAY);
        static final int MONTH = 29 * DAY + HP(12, 793);
        static final int MOLAD = MONDAY * DAY + HP(5, 204);
        static final int MONTHS_IN_19Y = 235;//12*12+7*13
        static final int MOLAD_ZAKEN_ROUNDING = 6*HOUR;
        static final int TKUFA = 91 * DAY + 7 * HOUR + 540;
        
        static final int M_ID_TISHREI = 0;
        static final int M_ID_CHESHVAN = 1;
        static final int M_ID_KISLEV = 2;
        static final int M_ID_TEVET = 3;
        static final int M_ID_SHEVAT = 4;
        static final int M_ID_ADAR = 5;
        static final int M_ID_ADAR_I = 6;
        static final int M_ID_ADAR_II = 7;
        static final int M_ID_NISAN = 8;
        static final int M_ID_IYAR = 9;
        static final int M_ID_SIVAN = 10;
        static final int M_ID_TAMMUZ = 11;
        static final int M_ID_AV = 12;
        static final int M_ID_ELUL = 13;


        static final int[] MONTHS_DIVISION =
        {
            12, 12, 13, 12, 12, 13, 12, 13, 12, 12, 13, 12, 12, 13, 12, 12, 13, 12, 13
        };

        static int HP(int h, int p)
        {
            return h * HOUR + p;
        }

        private int year; // year 1 is the first year.
        private int month; // range 1..12,or 1..13 in leap year
        
        private int day; // range 1..30
        private long year_molad_parts; // parts since the beginning
        private int year_length; // days in year - 353,354,355,383,384,385
        private int year_first_day; // first day of rosh hashana from the beginning
        private int day_in_year; // days after year beginning. first day in year  is 0
        private boolean valid;
        JewishDate(int year,int month,int day)
        {
            if (year>=4119 && year<7001)
            {
                this.valid=true;
                this.year=year;
                this.month=month;
                this.day=day;
                this.year_molad_parts=parts_since_beginning(year);
                this.year_first_day=days_until_year(year,this.year_molad_parts);
                this.year_length=days_until_year(year+1,parts_since_beginning(year+1))-this.year_first_day;
                this.day_in_year=calculateDayInYear(this.year_length,month,day);
            }
            else
            {
                this.valid=false;
            }
        }
        JewishDate(int days)
        {
            long orig_parts = (long)days * DAY;
            long parts = orig_parts - MOLAD;
            int months = (int) (parts / MONTH);
            parts = (parts % MONTH);
            int years = 1;//first year was year one.
            years += 19 * (months / MONTHS_IN_19Y);
            months = months % MONTHS_IN_19Y;
            int year_in_19 = ((months + 1) * 19 - 2) / 235;
            years += year_in_19;
            months = months - (235 * (year_in_19) + 1) / 19;
            parts += months * MONTH;
            this.year_molad_parts=orig_parts-parts;
            this.year_first_day=days_until_year(years,this.year_molad_parts);
            int next_year_day=days_until_year(years+1,parts_since_beginning(years+1));
            int months_in_year;
            if (days>=this.year_first_day && days < next_year_day)
            {
                this.year=years;
                this.year_length=next_year_day-this.year_first_day;
            }
            else
            {

                if (days<this.year_first_day)
                {
                    this.year=years-1;
                    months_in_year=calculateYearMonths(this.year);
                    this.year_molad_parts-=months_in_year*MONTH;
                    next_year_day=this.year_first_day;
                    this.year_first_day=days_until_year(this.year,this.year_molad_parts);
                    this.year_length=next_year_day-this.year_first_day;
                }
                else
                {
                    
                    if (days>=next_year_day)
                    {
                        this.year=years+1;
                        months_in_year=calculateYearMonths(years);
                        this.year_molad_parts+=months_in_year*MONTH;
                        this.year_first_day=next_year_day;
                        this.year_length=days_until_year(this.year+1,parts_since_beginning(this.year+1))-this.year_first_day;
                    }
                    else
                    {
// should not get here
                    }
                }
            }
            this.day_in_year=days-this.year_first_day;
            setMonthDay(this.day_in_year);
            
        }
        public int ShmitaOrdinal()
        {
            return 1+(this.year-4117)%7;//4116 was a shmita year
        }
        /**
         * find out how many days passed from the last sun blessing
         *
         * @return number of days, modulu by 10227
         */
        public int TkufotCycle()//when this method return 0, we need to do sun blessing in nissan.
        {
            //10227=number of days in 28 years when year=365.25 days
            return (daysSinceBeginning()-1503540)%10227;
            //this date (1503540) is 19 in Nisan, 4117, wednesday
            //there is 112 tkofut in 28 year (4*28) or in 10227 days
            //you can find out which tkufa by tkufa=TkufotCycle()*112/10227
            //and that tkufa started at tkufa*10227/112
            //which is actually 1461/16 or 16/1461
        }
        public int sfiratHaomer()
        {
            int before_fisrt_omer_day=calculateDayInYearByMonthId(this.year_length,M_ID_NISAN,15);//pessah night
            int omer=this.day_in_year-before_fisrt_omer_day;
            if (omer < 0 || omer > 49) //if omer ==0 then its the night before hasfira
            {
                return -1;
            }
            return omer;
        }
        public int dayInMonth()
        {
            return this.day;
        }
        public int dayInYear()
        {
            return this.day_in_year;
        }
        public int year()
        {
            return this.year;
        }
        public static int monthFromIDByYear(int year,int monthId)
        {
            return monthFromIDByYearMonths(calculateYearMonths(year),monthId);
        }
        public static int monthFromIDByYearLength(int year_length,int monthId)
        {
            return monthFromIDByYearMonths((year_length>355)?13:12,monthId);
        }
        public static int monthFromIDByYearMonths(int year_months,int monthId)
        {
            if (monthId<M_ID_ADAR)
                return monthId+1;
            if (year_months>12)//leap year
            {

                if (monthId>=M_ID_ADAR_I)
                    return monthId;
                return 7;//adar II if monthId==M_ID_ADAR
            }
            else
            {
                if (monthId>=M_ID_NISAN)
                    return monthId-1;
                return 6; // regular adar
            }
        }
        public int monthID()
        {
            int m=this.month;
            if (this.year_length>355)//leap year
            {
                if (m>5)//if Adar or after
                    ++m;//skip regular Adar
            }
            else
            {
                if (m>6)//if Nisan or after
                    m+=2;//skip Adar I+II
            }
            return m-1;
        }
        public String monthName(boolean Heb)
        {
            final String[][] months =
            {
                {"תשרי", "חשוון", "כסלו", "טבת",
                "שבט", "אדר",
                "אדר א'",
                "אדר ב'",
                "ניסן", "אייר",
                "סיוון", "תמוז", "אב", "אלול"},
                {"Tishrei", "Cheshvan", "Kislev", "Tevet",
                "Shevat", "Adar",
                "Adar I",
                "Adar II",
                "Nisan", "Iyar",
                "Sivan", "Tammuz", "Av", "Elul"},
            };
            int mID=monthID();
            return months[Heb?0:1][mID];
        }
        public int daysSinceBeginning()
        {
            return year_first_day+day_in_year;
        }
        private static final int[][] months_days_offsets=
        {
            {0 , 30 , 59 , 88 , 117 , 147 , 176 , 206 , 235 , 265 , 294 , 324 , 353 },
            {0 , 30 , 59 , 89 , 118 , 148 , 177 , 207 , 236 , 266 , 295 , 325 , 354 },
            {0 , 30 , 60 , 90 , 119 , 149 , 178 , 208 , 237 , 267 , 296 , 326 , 355 },
            {0 , 30 , 59 , 88 , 117 , 147 , 177 , 206 , 236 , 265 , 295 , 324 , 354 , 383 },
            {0 , 30 , 59 , 89 , 118 , 148 , 178 , 207 , 237 , 266 , 296 , 325 , 355 , 384 },
            {0 , 30 , 60 , 90 , 119 , 149 , 179 , 208 , 238 , 267 , 297 , 326 , 356 , 385 }
        };
        private void setMonthDay(int days)
        {
            int mo_year_t=mo_year_type(this.year_length);
            int m=days*2/59;
            if (months_days_offsets[mo_year_t][m]>days)
                m--;
            else if (months_days_offsets[mo_year_t][m+1]<=days)
                m++;
            this.month=m+1;
            this.day=days-months_days_offsets[mo_year_t][m]+1;
        }
        private static int mo_year_type(int year_length)
        {
            return ((year_length%10)-3)+3*(year_length>355?1:0); //0 hasera,1 kesidra,2 melea,3 meuberet hasera,4 meuberet kesidra,5 meuberet melea
        }
        private static int calculateDayInYear(int year_length,int month,int day)
        {
            int mo_year_t=mo_year_type(year_length);
            return months_days_offsets[mo_year_t][month-1]+day-1;
        }
        public static int calculateDayInYearByMonthId(int year_length,int month_id,int day)
        {
            int month=monthFromIDByYearLength(year_length, month_id);
            return calculateDayInYear(year_length,month,day);
        }


        public int monthLength()
        {
            return monthLengthInYear(this.year_length,this.month);
        }
        private static int monthLengthInYear(int year_length,int month)
        {
            int mo_year_t=mo_year_type(year_length);
            return months_days_offsets[mo_year_t][month]-months_days_offsets[mo_year_t][month-1];
        }

        /**
         * checks how many months are in a certain year
         *
         * @param year a hebrew year
         * @return the number of months in this year
         */
        public static int calculateYearMonths(int year)
        {
            /*
             the loop:
             for x in range(0,19):
             print (235*(x+1)+1)/19-(235*x+1)/19
             gives exactly:
             12,12,13,12,12,13,12,13,12,12,13,12,12,13,12,12,13,12,13
             which is the 19-years period month's division
             */
            //return (235 * year + 1) / 19 - (235 * (year - 1) + 1) / 19;
            return MONTHS_DIVISION[(year-1)%19];
        }

        public static long parts_since_beginning(int year)
        {
            /*
             the loop:
             for x in range(0,19):
             print (235*(x+1)+1)/19-(235*x+1)/19
             gives exactly:
             12,12,13,12,12,13,12,13,12,12,13,12,12,13,12,12,13,12,13
             which is the 19-years period month's division
             */
            int months = (235 * (year - 1) + 1) / 19;//first year was year one.
            long parts = MOLAD + MONTH * (long) months;
            return parts;
        }
        public static int days_until_year(int year,long parts)
        {
            int days = (int)((parts + MOLAD_ZAKEN_ROUNDING) / DAY);
            int parts_mod = (int) (parts % DAY);
            int year_type = ((year - 1) * 7 + 1) % 19;
            /* this magic gives us the following array:
             1, 8,15, 3,10,17, 5,12, 0, 7,14, 2, 9,16, 4,11,18, 6,13
             now if we compare it with the 235 months division:
             12,12,13,12,12,13,12,13,12,12,13,12,12,13,12,12,13,12,13
             we can see that all the leap years # >=12 and all the regular years # <12
             also, all the years that comes after a leap year have a number < 7
             */
            int week_day = (days % 7);
            if (parts_mod<DAY-MOLAD_ZAKEN_ROUNDING)
            {
                if (year_type < 12)//regular year (non leap)
                {
                    if (week_day == TUESDAY && parts_mod >= HP(9, 204))
                    {
                        return days + 2;//we need to add 2 because Wednesday comes next (ADU)
                    }
                }
                if (year_type < 7)//a year after a leap year
                {
                    if (week_day == MONDAY && parts_mod >= HP(15, 589))
                    {
                        return days + 1;//we need to add only 1..
                    }
                }
            }
            if (week_day == SUNDAY || week_day == WEDNESDAY || week_day == FRIDAY)
            {
                ++days;
            }
            return days;
        }

        public static int days_to_year(int days)
        {
            long orig_parts = (long)days * DAY;
            long parts = orig_parts - MOLAD;
            int months = (int) (parts / MONTH);
            parts = (parts % MONTH);
            int years = 1;//first year was year one.
            years += 19 * (months / MONTHS_IN_19Y);
            months = months % MONTHS_IN_19Y;
            int year_in_19 = ((months + 1) * 19 - 2) / 235;
            years += year_in_19;
            months = months - (235 * (year_in_19) + 1) / 19;
            parts += months * MONTH;
            int estimated_year_length=353;
            if (calculateYearMonths(years)==13)
                estimated_year_length=383;
            long year_molad_parts=orig_parts-parts;
            int estimated_first_year_day=(int)((year_molad_parts)/DAY);
            if (estimated_first_year_day+2 <= days  && days < estimated_first_year_day +estimated_year_length)
            {
                return years;
            }
            int year_first_day=days_until_year(years,year_molad_parts);
            if (days<year_first_day)
                return years-1;
            int next_year_day=days_until_year(years+1,parts_since_beginning(years+1));
            if (days>=next_year_day)
                return years+1;
            return years;
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
    JewishDate hd;
    GregorianDate gd;

    private YDate()
    {
        Calendar cal = Calendar.getInstance();
        Date d = new Date();

        //long t = d.getTime(); //milliseconds since 1.1.70 00:00 GMT+
        cal.setTime(d);
        int gd_day = cal.get(Calendar.DAY_OF_MONTH);
        int gd_mon = cal.get(Calendar.MONTH) + 1;
        int gd_year = cal.get(Calendar.YEAR);
        gd=new GregorianDate(gd_year, gd_mon, gd_day);
        hd= new JewishDate(gd.daysSinceBeginning());

    }
    public static YDate getNow()
    {
        return new YDate();
    }

}
