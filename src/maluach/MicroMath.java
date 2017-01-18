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
public class MicroMath
{
    
    static long round(double x)
    {
        if (Double.isNaN(x))
                return 0;
        if (x<((double)Long.MIN_VALUE+0.5))
            return Long.MIN_VALUE;
        if (x>=((double)Long.MAX_VALUE-0.5))
            return Long.MAX_VALUE;
        if (x<0)
        {
            return -(long)(0.5-x);
        }
        return (long)(0.5+x);
    }
    /*
     * ====================================================
     * Copyright (C) 1993 by Sun Microsystems, Inc. All rights reserved.
     *
     * Developed at SunSoft, a Sun Microsystems, Inc. business.
     * Permission to use, copy, modify, and distribute this
     * software is freely granted, provided that this notice 
     * is preserved.
     * ====================================================
     *
     */

    /* atan(x)
     * Method
     *   1. Reduce x to positive by atan(x) = -atan(-x).
     *   2. According to the integer k=4t+0.25 chopped, t=x, the argument
     *      is further reduced to one of the following intervals and the
     *      arctangent of t is evaluated by the corresponding formula:
     *
     *      [0,7/16]      atan(x) = t-t^3*(a1+t^2*(a2+...(a10+t^2*a11)...)
     *      [7/16,11/16]  atan(x) = atan(1/2) + atan( (t-0.5)/(1+t/2) )
     *      [11/16.19/16] atan(x) = atan( 1 ) + atan( (t-1)/(1+t) )
     *      [19/16,39/16] atan(x) = atan(3/2) + atan( (t-1.5)/(1+1.5t) )
     *      [39/16,INF]   atan(x) = atan(INF) + atan( -1/t )
     *
     * Constants:
     * The hexadecimal values are the intended ones for the following 
     * constants. The decimal values may be used, provided that the 
     * compiler will convert from decimal to binary accurately enough 
     * to produce the hexadecimal values shown.
     */
    static final double atanhi[] =
    {

        4.63647609000806093515e-01, /* atan(0.5)hi 0x3FDDAC67, 0x0561BB4F */
        7.85398163397448278999e-01, /* atan(1.0)hi 0x3FE921FB, 0x54442D18 */
        9.82793723247329054082e-01, /* atan(1.5)hi 0x3FEF730B, 0xD281F69B */
        1.57079632679489655800e+00, /* atan(inf)hi 0x3FF921FB, 0x54442D18 */

    };

    static final double atanlo[] =
    {

        2.26987774529616870924e-17, /* atan(0.5)lo 0x3C7A2B7F, 0x222F65E2 */
        3.06161699786838301793e-17, /* atan(1.0)lo 0x3C81A626, 0x33145C07 */
        1.39033110312309984516e-17, /* atan(1.5)lo 0x3C700788, 0x7AF0CBBD */
        6.12323399573676603587e-17, /* atan(inf)lo 0x3C91A626, 0x33145C07 */

    };

    static final double aT[] =
    {

        3.33333333333329318027e-01, /* 0x3FD55555, 0x5555550D */
        -1.99999999998764832476e-01, /* 0xBFC99999, 0x9998EBC4 */
        1.42857142725034663711e-01, /* 0x3FC24924, 0x920083FF */
        -1.11111104054623557880e-01, /* 0xBFBC71C6, 0xFE231671 */
        9.09088713343650656196e-02, /* 0x3FB745CD, 0xC54C206E */
        -7.69187620504482999495e-02, /* 0xBFB3B0F2, 0xAF749A6D */
        6.66107313738753120669e-02, /* 0x3FB10D66, 0xA0D03D51 */
        -5.83357013379057348645e-02, /* 0xBFADDE2D, 0x52DEFD9A */
        4.97687799461593236017e-02, /* 0x3FA97B4B, 0x24760DEB */
        -3.65315727442169155270e-02, /* 0xBFA2B444, 0x2C6A6C2F */
        1.62858201153657823623e-02, /* 0x3F90AD3A, 0xE322DA11 */

    };

    static final double one = 1.0,
            huge = 1.0e300;

    static double atan(double x)

    {
        double w, s1, s2, z;
        int ix, hx, id;

        hx = __HI(x);
        ix = hx & 0x7fffffff;
        if (ix >= 0x44100000)
        {	/* if |x| >= 2^66 */

            if (ix > 0x7ff00000
                || (ix == 0x7ff00000 && (__LO(x) != 0)))
            {
                return x + x;		/* NaN */
            }
            if (hx > 0)
            {
                return atanhi[3] + atanlo[3];
            }
            else
            {
                return -atanhi[3] - atanlo[3];
            }
        }
        if (ix < 0x3fdc0000)
        {	/* |x| < 0.4375 */

            if (ix < 0x3e200000)
            {	/* |x| < 2^-29 */

                if (huge + x > one)
                {
                    return x;	/* raise inexact */
                }
            }
            id = -1;
        }
        else
        {
            x = Math.abs(x);
            if (ix < 0x3ff30000)
            {		/* |x| < 1.1875 */

                if (ix < 0x3fe60000)
                {	/* 7/16 <=|x|<11/16 */

                    id = 0;
                    x = (2.0 * x - one) / (2.0 + x);
                }
                else
                {			/* 11/16<=|x|< 19/16 */

                    id = 1;
                    x = (x - one) / (x + one);
                }
            }
            else
            {
                if (ix < 0x40038000)
                {	/* |x| < 2.4375 */

                    id = 2;
                    x = (x - 1.5) / (one + 1.5 * x);
                }
                else
                {			/* 2.4375 <= |x| < 2^66 */

                    id = 3;
                    x = -1.0 / x;
                }
            }
        }
        /* end of argument reduction */
        z = x * x;
        w = z * z;
        /* break sum from i=0 to 10 aT[i]z**(i+1) into odd and even poly */
        s1 = z * (aT[0] + w * (aT[2] + w * (aT[4] + w * (aT[6] + w * (aT[8] + w * aT[10])))));
        s2 = w * (aT[1] + w * (aT[3] + w * (aT[5] + w * (aT[7] + w * aT[9]))));
        if (id < 0)
        {
            return x - x * (s1 + s2);
        }
        else
        {
            z = atanhi[id] - ((x * (s1 + s2) - atanlo[id]) - x);
            return (hx < 0) ? -z : z;
        }
    }

    public static int signFromBit(float value)
    {
        return ((Float.floatToIntBits(value) >> 30) | 1);
    }

    /**
     * @param value A double value.
     * @return -1 if sign bit is 1, 1 if sign bit is 0.
     */
    public static long signFromBit(double value)
    {
        // Returning a long, to avoid useless cast into int.
        return ((Double.doubleToLongBits(value) >> 62) | 1);
    }

    /**
     * If value is not NaN and is outside [-1,1] range, closest value in this
     * range is used.
     *
     * @param value Value in [-1,1].
     * @return Value arcsine, in radians, in [-PI/2,PI/2].
     */
    public static double asinInRange(double value)
    {
        if (value <= -1.0)
        {
            return -Math.PI / 2;
        }
        else if (value >= 1.0)
        {
            return Math.PI / 2;
        }
        else
        {
            return asin(value);
        }
    }

    public static double asin(double x)
    {
        return 2 * atan2(x, 1 + Math.sqrt(1 - x * x));
    }

    public static double acos(double x)
    {
        return 2 * atan2(Math.sqrt(1 - x * x), 1 + x);
    }
    /**
     * @param value Value in [-1,1].
     * @return Value arccosine, in radians, in [0,PI].
     */
    public static double _acos(double value)
    {
        return Math.PI / 2 - asin(value);
    }

    static boolean unsignedLessThan(long left, long right)
    {
        return (left < right) ^ (left < 0) ^ (right < 0);
    }

    static final double tiny = 1.0e-300,
            zero = 0.0,
            pi_o_4 = 7.8539816339744827900E-01, /* 0x3FE921FB, 0x54442D18 */
            pi_o_2 = 1.5707963267948965580E+00, /* 0x3FF921FB, 0x54442D18 */
            pi = 3.1415926535897931160E+00, /* 0x400921FB, 0x54442D18 */
            pi_lo = 1.2246467991473531772E-16; /* 0x3CA1A626, 0x33145C07 */


    static int __HI(double x)
    {
        return (int) (Double.doubleToLongBits(x) >> 32);
    }

    static int __LO(double x)
    {
        return (int) (Double.doubleToLongBits(x) & 0xffffffff);
    }

    static double atan2(double y, double x)
    {
        double z;
        int k, m, hx, hy, ix, iy;
        long lx, ly;
        long lz;

        hx = __HI(x);
        ix = hx & 0x7fffffff;
        lx = __LO(x);
        hy = __HI(y);
        iy = hy & 0x7fffffff;
        ly = __LO(y);
        

        if (Double.isNaN(x)||Double.isNaN(y))	/* x or y is NaN */
        {
            return x + y;
        }
        if ((hx - 0x3ff00000 | lx) == 0)
        {
            return atan(y);   /* x=1.0 */
        }
        m = ((hy >> 31) & 1) | ((hx >> 30) & 2);	/* 2*sign(x)+sign(y) */

        /* when y = 0 */
        if ((iy | ly) == 0)
        {
            switch (m)
            {
                case 0:
                case 1:
                    return y; 	/* atan(+-0,+anything)=+-0 */

                case 2:
                    return pi + tiny;/* atan(+0,-anything) = pi */

                case 3:
                    return -pi - tiny;/* atan(-0,-anything) =-pi */

            }
        }
        /* when x = 0 */
        if ((ix | lx) == 0)
        {
            return (hy < 0) ? -pi_o_2 - tiny : pi_o_2 + tiny;
        }

        /* when x is INF */
        if (ix == 0x7ff00000)
        {
            if (iy == 0x7ff00000)
            {
                switch (m)
                {
                    case 0:
                        return pi_o_4 + tiny;/* atan(+INF,+INF) */

                    case 1:
                        return -pi_o_4 - tiny;/* atan(-INF,+INF) */

                    case 2:
                        return 3.0 * pi_o_4 + tiny;/*atan(+INF,-INF)*/

                    case 3:
                        return -3.0 * pi_o_4 - tiny;/*atan(-INF,-INF)*/

                }
            }
            else
            {
                switch (m)
                {
                    case 0:
                        return zero;	/* atan(+...,+INF) */

                    case 1:
                        return -zero;	/* atan(-...,+INF) */

                    case 2:
                        return pi + tiny;	/* atan(+...,-INF) */

                    case 3:
                        return -pi - tiny;	/* atan(-...,-INF) */

                }
            }
        }
        /* when y is INF */
        if (iy == 0x7ff00000)
        {
            return (hy < 0) ? -pi_o_2 - tiny : pi_o_2 + tiny;
        }

        /* compute y/x */
        k = (iy - ix) >> 20;
        if (k > 60)
        {
            z = pi_o_2 + 0.5 * pi_lo; 	/* |y/x| >  2**60 */
        }
        else if (hx < 0 && k < -60)
        {
            z = 0.0; 	/* |y|/x < -2**60 */
        }
        else
        {
            z = atan(Math.abs(y / x));		/* safe to do y/x */
        }
        switch (m)
        {
            case 0:
                return z;	/* atan(+,+) */

            case 1:
                lz=Double.doubleToLongBits(z);
                lz^= 0x8000000000000000L;
                z=Double.longBitsToDouble(lz);
                return z;	/* atan(-,+) */

            case 2:
                return pi - (z - pi_lo);/* atan(+,-) */

            default: /* case 3 */

                return (z - pi_lo) - pi;/* atan(-,-) */

        }
    }

    /*public static double atan2(double y, double x)
    {
        int sign;
        double d = Math.sqrt(x * x + y * y);
        double z;
        double addit;
        if(y<x)
        {   
            z = y / (d + x);
            sign=1;
            addit=0;
        }
        else
        {
            z = x / (d + y);
            sign=-1;
            addit=Math.PI/2;
        }
        double z2 = z * z;
        double z22 = 2 * z2;
        double last_mult = 2 * z / (1 + z2);
        int k;
        double mult = 1, atan = 1;
        for (k = 1; k <= 45; k++)//precision of 45 iterations is enough
        {
            mult *= k * z22 / ((2 * k + 1) * (1 + z2));
            atan += mult;
        }
        atan= addit+sign * atan * last_mult;

        if (atan > Math.PI)
            atan -=2*Math.PI;
        else
            if (atan<-Math.PI)
                atan+=2*Math.PI;
        return atan;
    }*/
    public static double fast_atan2(double y, double x)
    {
	if ( x == 0.0f )
	{
		if ( y > 0.0f ) return Math.PI/2;
		if ( y == 0.0f ) return 0.0f;
		return -Math.PI/2;
	}
	double atan;
	double z = y/x;
	if ( Math.abs( z ) < 1.0f )
	{
		atan = z/(1.0f + 0.2809f*z*z);
		if ( x < 0.0f )
		{
			if ( y < 0.0f ) return atan - Math.PI;
			return atan + Math.PI;
		}
	}
	else
	{
		atan = Math.PI/2 - z/(z*z + 0.2809f);
		if ( y < 0.0f ) return atan - Math.PI;
	}
	return atan;
    }
    static
    {

    }
}
