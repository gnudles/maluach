/* This is free and unencumbered software released into the public domain.
 */
package maluach;


public class Format
{
    public static String Min2Str(double min)
    {
        int sign=1;
        if (min<0)
        {
            sign=-1;
            min=-min;
        }
        int imin = (int) (min + 0.5);
        imin = imin % (60*24);
        String stime = ((sign==-1)?"-":"") + (imin / 60);
        imin = imin % 60;
        if (imin < 10)
        {
            stime += ":0" + imin;
        }
        else
        {
            stime += ":" + imin;
        }
        return stime;
    }
    private static String getc00String(int x)
    {
        
        char a=(char)('0'+x/10);
        char b=(char)('0'+x%10);
        return (":"+a)+b;
    }
    private static String get00String(int x)
    {
        
        char a=(char)('0'+x/10);
        char b=(char)('0'+x%10);
        return (""+a)+b;
    }
    public static String GDateString(int y,int m,int d)
    {
        String sdate = get00String(d);
        sdate += "."+get00String(m);
        sdate += "."+get00String(y%100);
        return sdate;
    }
    public static String TimeString(int h,int m)
    {
        String stime = get00String(h);
        stime += getc00String(m);
        return stime;
    }
    public static String TimeString(int h,int m,int s)
    {
        String stime = get00String(h);
        stime += getc00String(m);
        stime += getc00String(s);
        return stime;
    }
    public static final String[] alphabeta =
    {
        " ", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט",
        "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ",
        "ק", "ר", "ש", "ת"
    };
    
    
    public static String HebIntSubString(int n, boolean geresh,boolean gereshim)
    {
        final int ONES=0;
        final int TET=9;
        final int TENS=9;
        final int HUNDREDS=18;

        if (n<1000 && n>=1)
        {
            String strstrm="";
            int a,t;
            a=n/100;
            n=n%100;
            while (a>0)
            {
                t=Math.min(a, 4);
                a-=t;
                if (a==0 && n==0 && !strstrm.equals("") && gereshim)
                    strstrm+="\"";
                strstrm+=alphabeta[t+HUNDREDS];
            }
            
            if (n == 15 || n == 16)
            {
                n-=9;
                strstrm+=alphabeta[TET];
            }
            a=n/10;
            n=n%10;
            if (a>0)
            {
                if (n==0 && !strstrm.equals("") && gereshim)
                    strstrm+="\"";
                strstrm+=alphabeta[TENS+a];
            }
            
            if (n>0)
            {
                if (!strstrm.equals("") && gereshim)
                    strstrm+="\"";
                strstrm+=alphabeta[ONES+n];
            }
            if (strstrm.length()==1 && geresh)
                strstrm+="\'";
            return strstrm;
        }
        else
            return String.valueOf(n);

    }
    public static String HebIntString(int n, boolean pg) //pg - prat gadol
    {
        boolean geresh,gereshim;
        int a;
        String out="";
        a=n/1000;
        n=n%1000;
        if ( a>0 && pg )
        {
            out=HebIntSubString(a,true,true);
            if (n==0 || a>10)
                out+=" אלפים ";
        }
        if (n>0)
            out+=HebIntSubString(n,true,true);
        
        return out;
    }

}
