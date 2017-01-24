/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;


/**
 *
 * @author orr
 */
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
    private static final String[][] digits =
    {
        {
            " ", "à", "á", "â", "ã", "ä", "å", "æ", "ç", "è"
        },
        {
            "è", "é", "ë", "ì", "î", "ð", "ñ", "ò", "ô", "ö"
        },
        {
            " ", "÷", "ø", "ù", "ú"
        }
    };
    public static String HebIntString(int n, boolean geresh)
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
}
