/* This is free and unencumbered software released into the public domain.
 */

package maluach;

import java.util.Calendar;
import java.util.Date;

public class YDate
{
    public interface TimeZoneProvider {

        public float getOffset(Date d); //offset in hours
    }
    static final int EPOCH_DAY=2092591;//1.1.1970
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
    static final int JULIAN_DAY_OFFSET=347997;
    
    public static final String[][] day_names =
    {
        {"ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת"},
        {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"},
        {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"},
    };
    public static final String[][] zodiac_names =
    {
        {"טלה", "שור", "תאומים", "סרטן", "אריה", "בתולה", "מאזנים", "עקרב", "קשת", "גדי", "דלי", "דגים"},
        {"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces"}
    };
    /*
    fire: Aries Leo Sagittarius
    earth: Taurus Virgo Capricorn
    wind: Gemini Libra Aquarius
    water: Cancer Scorpio Pisces
       fire doesn't connect with water
       earth doesn't connect with wind
    */
    public static final String[][] four_elements_names =
    {
        {"אש", "עפר", "רוח", "מים"},
        {"fire", "earth", "wind", "water"}
    };
    public static final String[][] star_names =
    {
        {"כוכב", "לבנה", "שבתאי", "צדק", "מאדים", "חמה", "נגה"},
        {"Mercury","Moon","Saturn","Jupiter","Mars","Sun","Venus"}
    };
    public static final class GregorianDate
    {
        static final int DAYS_IN_400 = 146097;
        static final int DAYS_IN_4 = 1461;
        static final int DAYS_OF_1600 = 1957451;// days since beginning up to year 1.1.1600 (14 in tevet, 5360)
        static final int DAYS_OF_2300 = 2213121;
        
        
        static final int[] HUNDRED_OFFSET ={0,36525,36525*2-1,36525*3-2};
        private int year; // year 1 is the first year.
        private int month; // range 1..12, 1 January, February
        private int day; // range 1..31
        private int year_length; // days in year - 365,366
        private int year_first_day; // 1.1.year from the beginning
        private int day_in_year; // days after year beginning. first day in year  is 0
        private boolean valid;
        GregorianDate(GregorianDate o)
        {
            this.valid=o.valid;
            this.year=o.year;
            this.month=o.month;
            this.day=o.day;
            this.year_first_day=o.year_first_day;
            this.year_length=o.year_length;
            this.day_in_year=o.day_in_year;
        }
        GregorianDate(int year,int month,int day)
        {
            valid=setByYearMonthDay(year, month, day);
        }
        private boolean setByYearMonthDay(int year, int month,int  day)
        {
            if (year>=1600 && year< 2300)
            {
                this.year=year;
                if (month>12)
                    month=12;
                else if (month<1)
                    month=1;
                this.month=month;
                this.year_first_day=days_until_year(this.year);
                this.year_length=isLeap(this.year)?366:365;
                int month_length=monthLength();
                if (day>month_length)
                    day=month_length;
                else if (day<1)
                    day=1;
                this.day=day;
                this.day_in_year=calculateDayInYear(this.year_length,this.month,this.day);
                return true;
            }
            else
                return false;
        }
        private boolean setByDays(int days)
        {
            if (days>=DAYS_OF_1600 && days<DAYS_OF_2300)
            {
                int gd_day;
                int gd_month;
                int gd_year;
                {
                    int l, n, i, j;
                    l = days+ 68569 + JULIAN_DAY_OFFSET;
                    n = (4 * l) / DAYS_IN_400;
                    l = l - (DAYS_IN_400 * n + 3) / 4;
                    i = (4000 * (l + 1)) / 1461001;	/* that's 1,461,001 */
                    l = l - (DAYS_IN_4 * i) / 4 + 31;
                    j = (80 * l) / 2447;
                    gd_day = l - (2447 * j) / 80;
                    l = j / 11;
                    gd_month = j + 2 - (12 * l);
                    gd_year = 100 * (n - 49) + i + l;
                }

                /*{
                    days-=DAYS_OF_1600;
                    gd_year=1600+400*((days)/DAYS_IN_400);
                    days=days%DAYS_IN_400;
                    int h=(days*4)/DAYS_IN_400;
                    if (h==0)
                    {
                        h=(days*4)/DAYS_IN_4;
                        gd_year+=h;
                        days=days-(h*DAYS_IN_4+3)/4;
                    }
                    else
                    {
                        gd_year+=100*h;
                        //TODO:!!
                        //h;
                        //gd_year+=h;
                    }
                    //gd_year=;
                    gd_month=1;
                    gd_day=1;
                }*/
                this.year=gd_year;
                this.month=gd_month;
                this.day=gd_day;
                this.year_first_day=days_until_year(this.year);
                this.year_length=isLeap(this.year)?366:365;
                this.day_in_year=calculateDayInYear(this.year_length,this.month,this.day);
                return true;
            }
            else
            {
                return false;
            }
        }
        GregorianDate(int days)
        {
            valid=setByDays(days);
        }
        String dayString(boolean Heb)
        {
            String s=Integer.toString(this.day);
            if(Heb)
                s+= " ב";
            else
                s+= " in ";
            s+= monthName(Heb) + " " + Integer.toString(this.year);
            return s;
        }
        public int dayInWeek()
        {
            return daysSinceBeginning()%7+1;
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
        public int month()
        {
            return this.month;
        }
        public int daysSinceBeginning()
        {
            return year_first_day+day_in_year;
        }
        public int yearFirstDay()
        {
            return this.year_first_day;
        }
        public int monthFirstDay()
        {
            return year_first_day+day_in_year-day+1;
        }
        public int previousMonthLength()
        {
            if (this.month==1)//December is always 31 days
                return 31;
            int mo_year_t=year_length-365;
            return months_days_offsets[mo_year_t][month-1]-months_days_offsets[mo_year_t][month-2];
        }
        public double JulianDay()
        {
            return daysSinceBeginning()+JULIAN_DAY_OFFSET-0.5;
        }
        public String monthName(boolean Heb)
        {
            final String[][] months =
            {
                {"ינואר", "פברואר", "מרס", 
                 "אפריל", "מאי", "יוני", "יולי", 
                 "אוגוסט", "ספטמבר", "אוקטובר", 
                 "נובמבר", "דצמבר"},
                {"January", 
                 "February",
                 "March",
                 "April",
                 "May",
                 "June",
                 "July",
                 "August",
                 "September",
                 "October",
                 "November",
                 "December"},
                {"Jan", 
                 "Feb",
                 "Mar",
                 "Apr",
                 "May",
                 "Jun",
                 "Jul",
                 "Aug",
                 "Sep",
                 "Oct",
                 "Nov",
                 "Dec"},
            };
            return months[Heb?0:1][this.month-1];
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
        public static int calculateDayInYear(boolean leap_year,int month,int day)
        {
            return months_days_offsets[leap_year?1:0][month-1]+day-1;
        }
        public int monthLength()
        {
            int mo_year_t=year_length-365;
            return months_days_offsets[mo_year_t][month]-months_days_offsets[mo_year_t][month-1];
        }
        public int yearLength()
        {
            return this.year_length;
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
            int year_first_day=DAYS_OF_1600;
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
    public static final class JewishDate
    {
        static final int DAYS_OF_4119=1504084;
        static final int DAYS_OF_TKUFA_CYCLE_4117=1503540;
        static final int DAYS_OF_6001=2191466;
        static final int N_YEAR_TYPES = 14;
        static final int HOUR = 1080;
        static final int DAY = (24 * HOUR);
        static final int WEEK = (7 * DAY);
        static final int MONTH = 29 * DAY + HP(12, 793);
        static final int MOLAD = MONDAY * DAY + HP(5, 204);
        static final int MONTHS_IN_19Y = 235;//12*12+7*13
        static final int MOLAD_ZAKEN_ROUNDING = 6*HOUR;
        static final int TKUFA = 91 * DAY + 7 * HOUR + 540;
        static final int MAZAL = 30 * DAY + 10 * HOUR + 540;
        
        public static final int M_ID_TISHREI = 0;
        public static final int M_ID_CHESHVAN = 1;
        public static final int M_ID_KISLEV = 2;
        public static final int M_ID_TEVET = 3;
        public static final int M_ID_SHEVAT = 4;
        public static final int M_ID_ADAR = 5;
        public static final int M_ID_ADAR_I = 6;
        public static final int M_ID_ADAR_II = 7;
        public static final int M_ID_NISAN = 8;
        public static final int M_ID_IYAR = 9;
        public static final int M_ID_SIVAN = 10;
        public static final int M_ID_TAMMUZ = 11;
        public static final int M_ID_AV = 12;
        public static final int M_ID_ELUL = 13;


        static final int[] MONTHS_DIVISION =
        {
            12, 12, 13, 12, 12, 13, 12, 13, 12, 12, 13, 12, 12, 13, 12, 12, 13, 12, 13
        };

        static int HP(int h, int p)
        {
            return h * HOUR + p;
        }

        private int year=0; // year 1 is the first year.
        private int month; // range 1..12,or 1..13 in leap year
        
        private int day; // range 1..30
        private long year_molad_parts; // parts since the beginning
        private int year_length; // days in year - 353,354,355,383,384,385
        private int year_first_day; // first day of rosh hashana from the beginning
        private int day_in_year; // days after year beginning. first day in year  is 0
        private boolean valid;
        JewishDate(JewishDate o)
        {
            this.valid=o.valid;
            this.year=o.year;
            this.month=o.month;
            this.day=o.day;
            this.year_molad_parts=o.year_molad_parts;
            this.year_first_day=o.year_first_day;
            this.year_length=o.year_length;
            this.day_in_year=o.day_in_year;
        }
        JewishDate(int year,int month,int day)
        {
            if (year>=4119 && year<7001)
            {
                this.valid=true;
                this.year=year;
                this.month=month;
                this.day=day;
                calculateYearVariables();
                this.day_in_year=calculateDayInYear(this.year_length,month,day);
            }
            else
            {
                this.valid=false;
            }
        }
        private void calculateYearVariables()
        {
            this.year_molad_parts=parts_since_beginning(year);
            this.year_first_day=days_until_year(year,this.year_molad_parts);
            this.year_length=days_until_year(year+1,parts_since_beginning(year+1))-this.year_first_day;
        }
        private void setByYearMonthIdDay(int year,int month_id,int day)
        {
            if (year>=4119 && year<7001)
            {
                this.valid=true;
                if (this.year!=year)
                {
                    this.year=year;
                    calculateYearVariables();
                }
                this.month=monthFromIDByYearLength(this.year_length,month_id);
                int month_length=monthLength();
                this.day=Math.min(month_length,day);
                this.day_in_year=calculateDayInYear(this.year_length,this.month,this.day);
                
            }
        
        }
        private boolean setByDays(int days)
        {
            if (!(days>=DAYS_OF_4119 && days<DAYS_OF_6001))
            {
                return false;
            }
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
            return true;
        }
        JewishDate(int days)
        {
            valid=setByDays(days);
        }
        public String dayString(boolean Heb)
        {
            if (!Heb)
                return Integer.toString(this.day) + " in " + monthName(Heb) + " " + Integer.toString(this.year);
            else
                return Format.HebIntString(this.day, false)+ " ב" + monthName(Heb) + " " + Format.HebIntString(this.year,true);
        }
        public int NumberOfShabbats()
        {
            int year_diw=year_first_day%7;
            int diy=YDate.getNext(YDate.SATURDAY,year_diw)-year_diw;
            return (year_length-(diy)+6)/7;
        }
        public int ShmitaOrdinal()//unfortunatly we don't have Yovel.
        {
            final int yovel_year=6000;//to be determined
            if (this.year>=yovel_year)
            {
                return 1+(this.year-yovel_year+49)%50;
            }
            return 1+(this.year-4117)%7;//4116 was a shmita year
        }
        public String ShmitaTitle()
        {
            final String [] titles={
            "ראשונה (מעשר שני)",
            "שניה (מעשר שני)",
            "שלישית (מעשר עני)",
            "רביעית (מעשר שני וביעור מעשרות)",
            "חמישית (מעשר שני)",
            "שישית (מעשר עני)",
            "שביעית (שמיטה וביעור מעשרות)",
            "יובל"
            };
            if (ShmitaOrdinal()==50)
                return titles[7];
            return titles[(ShmitaOrdinal()-1)%7];
        }
        public int dayInMazal()
        {
            int d = (TkufotCycle()+1) * DAY-1;
            d = d % MAZAL;
            return (int)(d/DAY);
        }
        public int dayInTkufa()
        {
            int d = (TkufotCycle()+1) * DAY-1;
            d = d % TKUFA;
            return (int)(d/DAY);
        }
        public long MazalParts()
        {
            long mazal_parts=((long)daysSinceBeginning()+1-DAYS_OF_TKUFA_CYCLE_4117)*(long)DAY-1;
            mazal_parts=mazal_parts-(mazal_parts % MAZAL);
            mazal_parts+=(long)DAYS_OF_TKUFA_CYCLE_4117*DAY;
            return mazal_parts;
        }
        public long TkufaParts()
        {
            long tkufa_parts=((long)daysSinceBeginning()+1-DAYS_OF_TKUFA_CYCLE_4117)*(long)DAY-1;
            tkufa_parts=tkufa_parts-(tkufa_parts % TKUFA);
            tkufa_parts+=(long)DAYS_OF_TKUFA_CYCLE_4117*DAY;
            return tkufa_parts;
        }
        public int MazalType()
        {
            int d = (TkufotCycle()+1) * DAY-1;
            return (d / MAZAL)%12;
        }
        public int TkufaType()
        {
            int d = (TkufotCycle()+1) * DAY-1;
            d = d / TKUFA;
            return (M_ID_NISAN+(d%4)*3)%14;
        }
        public String MazalName(boolean Heb)
        {
            int mazal=MazalType();
            if (Heb)
                return "מזל "+zodiac_names[0][mazal] +" ("+four_elements_names[0][mazal%4]+")";
            else
                return "Zodiac. "+zodiac_names[1][mazal] +" ("+four_elements_names[1][mazal%4]+")";
        }
        public String TkufaName(boolean Heb)
        {
            if (Heb)
                return "תקופת "+monthNameByID(TkufaType(),Heb);
            else
                return monthNameByID(TkufaType(),Heb)+" Period";
        }
        public String MazalBeginning(TimeZoneProvider tz)
        {
            Date m=partsToUTC(MazalParts());
            return FormatUTC(m,tz,true);
        }
        public String TkufaBeginning(TimeZoneProvider tz)
        {
            long parts=TkufaParts();
            Date m=partsToUTC(parts);
            return FormatUTC(m,tz,true)+"\nמוסיפים "+((TkufaType()%M_ID_NISAN==M_ID_TEVET)?"60":"30")+" דקות לפני ואחרי."
                    +"\nתחילת תקופה ב"+starForHour(parts, true);
        }
        /**
         * find out how many days passed from the last sun blessing
         *
         * @return number of days, modulo by 10227
         */
        public int TkufotCycle()//when this method return 0, we need to do sun blessing in nissan.
        {
            return TkufotCycle(daysSinceBeginning());
        }
        static public int TkufotCycle(int days)//when this method return 0, we need to do sun blessing in nissan.
        {
            //10227=number of days in 28 years when year=365.25 days
            return (days-DAYS_OF_TKUFA_CYCLE_4117)%10227;
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
        String starForHour(long parts,boolean Heb)
        {
            int hour=(int)((parts/HOUR)%7);
            return star_names[Heb?0:1][hour];
        }
        public long MoladParts()
        {
            return year_molad_parts+(month-1)*MONTH;
        }
        public Date partsToUTC(long parts)
        {
            int days=(int)(parts/DAY);
            int single_parts=(int)(parts%DAY);
            int hours=(int)(single_parts/HOUR);
            single_parts=single_parts%HOUR;
            //we subtract 1 day but we add 18 hours. I mean 16 hours . I mean 15:39
            long millis=(long)(days-EPOCH_DAY-1)*3600L*24*1000L;
            final int offset_utc=15*60+39;// or you can use 16*60 instead
            millis+=(hours*3600L*1000L+single_parts*10000L/3+offset_utc*60L*1000L);
            return new Date(millis);
        }
        String dayPartName(int minutes)
        {
            if (minutes>23*60 || minutes<3*60)
                return "לילה";
            if (minutes<5*60)
                return "לפנות בוקר";
            if (minutes<11*60)
                return "בוקר";
            if (minutes<15*60)
                return "צהריים";
            if (minutes<17*60)
                return "אחה\"צ";
            return "ערב";
        }
        public String FormatUTC(Date t,TimeZoneProvider tz, boolean Heb)
        {
            String lstr;
            int utc_minute_offset=(int)(tz.getOffset(t)*60);
            
            String clock_type="UTC"+(utc_minute_offset>=0?"+":"-")+String.valueOf(Math.abs(utc_minute_offset))+"MIN";
            long millis=t.getTime()+utc_minute_offset*60000L+(EPOCH_DAY-DAYS_OF_4119)*(24*60*60000L);//DAYS_OF_4119 is monday
            int minutes=(int)((millis/60000L)%(24*60));
            lstr=Format.Min2Str(minutes);
            String day_part_name=dayPartName(minutes);
            lstr+=" ("+day_names[Heb?0:1][(int)((millis/(24*60*60000))+1)%7] +" "+day_part_name+ ")";
            lstr+=" ("+clock_type+ ")";
            return lstr;
        }
        public String MoladString(TimeZoneProvider tz)
        { 
            long parts=MoladParts();
            int days=(int)(parts/DAY);
            int single_parts=(int)(parts%DAY);
            int hours=(int)(single_parts/HOUR);
            single_parts=single_parts%HOUR;
            String lstr="המולד לחודש ";
            lstr+=monthName(true);
            lstr+=" ";
            lstr+=Format.HebIntString(year(),true);
            lstr+=" ביום ";
            lstr+=day_names[0][days%7];
            lstr+=" שעה ";
            lstr+=String.valueOf(hours);
            lstr+=" ו ";
            lstr+=String.valueOf(single_parts);
            lstr+=" חלקים";
            lstr+="\n";
            //int minutes=(single_parts)/(1080/60);
            Date m=partsToUTC(parts);
            lstr+=FormatUTC(m,tz,true);
            lstr+="\nהמולד ב"+starForHour(parts, true);
            return lstr;
        }
        public int dayInMonth()//starts from one
        {
            return this.day;
        }
        public int dayInWeek()//starts from one
        {
            return daysSinceBeginning()%7+1;
        }
        public String dayInWeekName(boolean Heb)
        {
            return day_names[Heb?0:1][daysSinceBeginning()%7];
        }
        public int dayInYear()//starts from zero
        {
            return this.day_in_year;
        }
        public int monthInYear()
        {
            return this.month;
        }
        public int year()
        {
            return this.year;
        }
        public int yearLength()
        {
            return this.year_length;
        }
        public int yearFirstDay()
        {
            return this.year_first_day;
        }
        public int monthFirstDay()
        {
            int mo_year_t=mo_year_type(this.year_length);
            return this.year_first_day+months_days_offsets[mo_year_t][this.month-1];
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
        public static int monthID(int months_in_year,int month)
        {
            if (months_in_year==13)//leap year
            {
                if (month>5)//if Adar or after
                    ++month;//skip regular Adar
            }
            else
            {
                if (month>6)//if Nisan or after
                    month+=2;//skip Adar I+II
            }
            return month-1;
        }
        public int monthID()
        {
            return monthID(calculateYearMonths(year),this.month);
        }
        public String monthNameByID(int mID,boolean Heb)
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
            return months[Heb?0:1][mID];
        }
        public String monthName(boolean Heb)
        {
            return monthNameByID(monthID(),Heb);
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

        /**
         * Return Hebrew year type based on size and first week day of year.
         * p - pshuta  350 +
         * m - meuberet 380 +
         * h - hasera + 3
         * k - kesidra + 4
         * s - shlema (melea) + 5
         * year type | year length | Tishery 1 day of week
         * | 1       | 353         | 2  ph2
         * | XXXXX   | 353         | 3  ph3 impossible
         * | XXXXX   | 353         | 5  ph5 impossible
         * | 2       | 353         | 7  ph7
         * | XXXXX   | 354         | 2  pk2 impossible
         * | 3       | 354         | 3  pk3
         * | 4       | 354         | 5  pk5
         * | XXXXX   | 354         | 7  pk7 impossible
         * | 5       | 355         | 2  ps2
         * | XXXXX   | 355         | 3  ps3 impossible
         * | 6       | 355         | 5  ps5
         * | 7       | 355         | 7  ps7
         * | 8       | 383         | 2  mh2
         * | XXXXX   | 383         | 3  mh3 impossible
         * | 9       | 383         | 5  mh5
         * |10       | 383         | 7  mh7
         * | XXXXX   | 384         | 2  mk2 impossible
         * |11       | 384         | 3  mk3
         * | XXXXX   | 384         | 5  mk5 impossible
         * | XXXXX   | 384         | 7  mk7 impossible
         * |12       | 385         | 2  ms2
         * | XXXXX   | 385         | 3  ms3 impossible
         * |13       | 385         | 5  ms5
         * |14       | 385         | 7  ms7
         *
         * @param size_of_year Length of year in days
         * @param year_first_dw First week day of year (1..7)
         * @return A number for year type (1..14)
         */
        public static int ld_year_type (int size_of_year, int year_first_dw)
        {
            final int[] year_type_map =
                    {1, 0, 0, 2, 0, 3, 4, 0, 5, 0, 6, 7,
                            8, 0, 9, 10, 0, 11, 0, 0, 12, 0, 13, 14};

            /* the year cannot start at days 1 4 6, so we are left with 2,3,5,7.
               and the possible lengths are 353 354 355 383 384 385...
               so we have 24 combinations, but only 14 are possible. (see table above)
            */
            /* 2,3,5,7 -> 0,1,2,3 */
            int offset = (year_first_dw - 1) / 2;
            return year_type_map[4 * mo_year_type(size_of_year)+offset];
        }
        public int getYearTypeWeekDayLength()
        {
            return ld_year_type(this.year_length, yearWeekDay());
        }
        public int yearWeekDay()//1- sunday,7-saturday
        {
            return this.year_first_day%7+1;
        }
        public int week()
        {
            return ((this.day_in_year) + (this.year_first_day%7)) / 7 + 1;
        }
        private static int mo_year_type(int year_length)
        {
            //0 hasera,1 kesidra,2 melea,3 meuberet hasera,4 meuberet kesidra,5 meuberet melea
            return ((year_length%10)-3)+(year_length-350)/10;
        }
        private static int calculateDayInYear(int year_length,int month,int day)//0..385
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
        public int previousMonthLength()
        {
            if (this.month==1)//Elul is always 29 days
                return 29;
            return monthLengthInYear(this.year_length,this.month-1);
        }
        public static int monthLengthInYear(int year_length,int month)
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
        public static int calculateYearLength(int year)
        {
            return calculateYearFirstDay(year+1)-calculateYearFirstDay(year);
        }
        public static int calculateYearFirstDay(int year)
        {
            return days_until_year(year,parts_since_beginning(year));
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
    }

    public JewishDate hd;
    
    public GregorianDate gd;
    YDateAnnual events=null;
    public void setMaintainEvents(boolean maintain,boolean diaspora)
    {
        if (maintain)
        {
            if (events==null)
            {
                events=new YDateAnnual(hd.year,hd.year_length,hd.year_first_day,diaspora);
            }
        }
        else
        {
            events=null;
        }
    }
    public YDateAnnual events()
    {
        return events;
    }
    private void maintainEvents()
    {
        if (events!=null)
        {
            if (events.year()!=hd.year())
            {
                events=new YDateAnnual(hd.year,hd.year_length,hd.year_first_day,events.diaspora());
            }
        }
    }
    private static boolean commonRange(int days)
    {
        if (days<JewishDate.DAYS_OF_6001 && days>=GregorianDate.DAYS_OF_1600)
            return true;
        return false;
    }
    public boolean seekBy(int offset)
    {
        int days=hd.daysSinceBeginning()+offset;
        return setByDays(days);
    }
    public boolean setByDays(int days)
    {
        if (commonRange(days))
        {
            gd.setByDays(days);
            hd.setByDays(days);
            maintainEvents();
            return true;
        }
        return false;
    }
    public void setByHebrewYearMonthIdDay(int year,int month_id,int day)
    {
        JewishDate new_hd=new JewishDate(hd);
        new_hd.setByYearMonthIdDay(year, month_id, day);
        int days=new_hd.daysSinceBeginning();
        if (commonRange(days) && new_hd.valid)
        {
            hd=new_hd;
            gd.setByDays(days);
            maintainEvents();
        }
    }
    public void setByDate(Date d)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int gd_day = cal.get(Calendar.DAY_OF_MONTH);
        int gd_mon = cal.get(Calendar.MONTH) + 1;
        int gd_year = cal.get(Calendar.YEAR);
        setByGregorianYearMonthDay(gd_year,gd_mon,gd_day);

    }
    public void setByGregorianYearMonthDay(int year,int month,int day)
    {
        if (gd.year()!= year || gd.month()!=month || gd.dayInMonth()!=day )
        {
            GregorianDate new_gd=new GregorianDate(gd);
            new_gd.setByYearMonthDay(year, month, day);
            int days=new_gd.daysSinceBeginning();
            if (commonRange(days) && new_gd.valid)
            {
                gd=new_gd;
                hd.setByDays(days);
                maintainEvents();
            }
        }
    }
    private static final byte INIT_JD=0;
    private static final byte INIT_JD_MID=1;
    private static final byte INIT_GD=2;
    private YDate(short year,byte mon,byte day, byte init)
    {
        switch(init)
        {
            case INIT_JD:
                hd= new JewishDate(year, mon , day);
            case INIT_JD_MID:
                if (init==INIT_JD_MID)
                    hd= new JewishDate(year,JewishDate.monthFromIDByYear(year, mon) , day);
                gd=new GregorianDate(hd.daysSinceBeginning());
                break;
            default:
                gd=new GregorianDate(year, mon, day);
                hd= new JewishDate(gd.daysSinceBeginning());
        }
    }

    private YDate(JewishDate hd, GregorianDate gd, YDateAnnual events)
    {
        this.gd=new GregorianDate(gd);
        this.hd=new JewishDate(hd);
        this.events=events;
    }
    public static YDate createFrom(YDate other)
    {
        return new YDate(other.hd,other.gd,other.events);
    }
    public static YDate createFrom(Date d)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int gd_day = cal.get(Calendar.DAY_OF_MONTH);
        int gd_mon = cal.get(Calendar.MONTH) + 1;
        int gd_year = cal.get(Calendar.YEAR);
        //long t = d.getTime(); //milliseconds since 1.1.70 00:00 GMT+
        return new YDate((short)gd_year,(byte)gd_mon,(byte)gd_day,INIT_GD);
    }
    public static YDate createFromJewishMonthId(int year,int month_id,int day)
    {
        return new YDate((short)year,(byte)month_id,(byte)day,INIT_JD_MID);
    }
    public static YDate createFromJewish(int year,int month,int day)
    {
        return new YDate((short)year,(byte)month,(byte)day,INIT_JD);
    }
    public static YDate createFromGregorian(int year,int month,int day)
    {
        return new YDate((short)year,(byte)month,(byte)day,INIT_GD);
    }
    
    public static YDate getNow()
    {
        Date d = new Date();
        return createFrom(d);
    }
    public static int getNext(int diw,int days) // return the upcoming diw (or today if it's that diw)
    {
        int diff=(diw-days%7+7)%7;
        return (days+diff);
    }
    public static int getPrevious(int diw,int days)
    {
        return getNext( diw, days-6);
    }
    public static Date toDate(int days,float hour)//hour in utc
    {
        long millis=(long)(days-EPOCH_DAY)*3600L*24*1000L;
        millis+=(hour*3600L*1000L);
        return new Date(millis);
    }
    public static int JdtoDays(double jd)//hour in utc
    {
        return (int)(jd+0.51-JULIAN_DAY_OFFSET);
    }
    public static String getTimeString(Date d, boolean seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        if (seconds)
            return Format.TimeString(hour, min,sec);
        return Format.TimeString(hour, min);
    }

}
