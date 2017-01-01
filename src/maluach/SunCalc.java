/**
 *
 * @author orr
 */
package maluach;

import java.util.Calendar;
import java.util.Date;

public class SunCalc
{

    private double latitude, longitude;
    private double JD;
    int timezone;
    private double noonEqTime, noonSolarDec, firstHourAngle, noonmin, risemin, setmin;

    public SunCalc(double JD, double latitude, double longitude, int timezone)
    {
        this.JD = JD;
        this.timezone=timezone;
        this.latitude = latitude;
        this.longitude = longitude;
        noonmin = calcSolNoonUTC(JD, longitude);
        SunTimes st = new SunTimes(JD + noonmin / 1440.0);

        // *** First pass to approximate sunrise (using solar noon)

        noonEqTime = st.calcEquationOfTime();

        noonSolarDec = st.getSunDeclination();
        firstHourAngle = SunTimes.radToDeg(calcHourAngle(latitude, noonSolarDec));
        calcSunUTC(false);
        calcSunUTC(true);

        noonmin += timezone;
        risemin += timezone;
        setmin += timezone;

    }
    /*
     * static public double ElevationAdjustment(double elevation) { double
     * earthRadius = 6356.9; double elevationAdjustment =
     * Math.toDegrees(SunTimes.acos(earthRadius/ (earthRadius + (elevation /
     * 1000)))); return elevationAdjustment;
    }
     */

    public double getNoon()
    {
        return noonmin;
    }

    public double getSunset()
    {
        return setmin;
    }

    public double getSunrise()
    {
        return risemin;
    }
    public double calculteBySunPos(int minutes_time,double angle_time,boolean rise)
    {
        double rise_set;
        if (rise)
            rise_set=risemin;
        else 
            rise_set=setmin;
        float calc_time=(float)rise_set +minutes_time;
        SunPos sp = new SunPos((int)this.JD,this.latitude, this.longitude, this.timezone);
        sp.calcSunAz(calc_time);
        double elev=sp.getSunElevation();
        double new_elev;
        float distance=32;
        int iters=0;
        int sign=1;
        if (elev >angle_time)
            sign*=-1;
        if (!rise)
            sign*=-1;
        while(true)
        {
            iters++;
            if (iters==30)
                return rise_set +minutes_time;
            calc_time+=distance*sign;
            sp.calcSunAz(calc_time);
            new_elev=sp.getSunElevation();
            if (!rise)
                sign=-1;
            else
                sign=1;
            if (new_elev >angle_time)
                sign*=-1;
            distance/=2;
            if (Math.abs(new_elev-angle_time)<0.1)
                break;
        }
        return calc_time;
    }
    public double getDawn()// 72 minutes before the sunrise
    {  
        int minutes_time=-72;
        double angle_time=-16;
        return calculteBySunPos(minutes_time,angle_time,true);
    }

    public double getRecognize()// 50 minutes before the sunrise
    {
        int minutes_time=-50;
        double angle_time=-11.5;
        return calculteBySunPos(minutes_time,angle_time,true);
    }

    public double getEndTimeKriatShma()// 3 day-hours (shaot zmaniot) after the sunrise
    {
        return risemin + (noonmin - risemin) / 2.0;//
    }

    public double getEndTimeShahrit()// 4 day-hours (shaot zmaniot) after the sunrise
    {
        return risemin + 2 * (noonmin - risemin) / 3.0;//
    }
    public double getEndTimeKriatShmaMGA()// 3 day-hours (shaot zmaniot) after the sunrise
    {
        return (risemin-72)+3*((setmin +72) - (risemin-72))/12.0 ;
    }

    public double getEndTimeShahritMGA()// 4 day-hours (shaot zmaniot) after the sunrise
    {
        return (risemin-72)+4*((setmin +72) - (risemin-72))/12.0 ;
    }

    public double getMinhaGdola()// half day-hour (shaot zmaniot) after the noon
    {
        return noonmin + 1 * (setmin - noonmin) / 12.0;//
    }

    public double getMinhaKtana()// 3.5 day-hour (shaot zmaniot) after the noon
    {
        return noonmin + 7 * (setmin - noonmin) / 12.0;//
    }

    public double getPlagMinha()// 3.5 day-hour (shaot zmaniot) after the noon
    {
        return noonmin + 19 * (setmin - noonmin) / 24.0;//
    }

    public double getVisibleStars()// 24 minutes after the sunset
    {
        return setmin + 1 * (setmin - noonmin) / 15.0;//
    }
    public double getBiurHametz()
    {
        return (risemin-72)+5*((setmin +72) - (risemin-72))/12.0;//
    }
    public double getRabenuTam()
    {
        int minutes_time=(int)(1.2*(setmin - noonmin) / 12.0); //72 minutes is 1.2 hours
        double angle_time=-16;
        return calculteBySunPos(minutes_time,angle_time,false);
    }

    public static boolean isLeapYear(int year)
    {
        if (year % 400 == 0)
        {
            return true;
        }
        if (year % 100 == 0)
        {
            return false;
        }
        if (year % 4 == 0)
        {
            return true;
        }
        return false;
    }

    public static int calcDayOfYear(int mn, int dy, boolean isl)
    {
        int k = (isl) ? 1 : 2;
        int doy = (275 * mn) / 9 - k * ((mn + 9) / 12) + dy - 30;
        return doy;
    }

    public static double calcHourAngle(double lat, double solarDec)//for sunset multiply by -1
    {
        double latRad = SunTimes.degToRad(lat);
        double sdRad = SunTimes.degToRad(solarDec);

        double HAarg = (Math.cos(SunTimes.degToRad(90.833)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad));

        double HA = (Trig.acos(Math.cos(SunTimes.degToRad(90.833)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad)));

        return HA;		// in radians
    }

    public static double calcSolNoonUTC(double jd, double longitude)
    {
        // First pass uses approximate solar noon to calculate eqtime
        SunTimes st = new SunTimes(jd + longitude / 360.0);
        double eqTime = st.calcEquationOfTime();

        double solNoonUTC = 720 + (longitude * 4) - eqTime; // min
        st = new SunTimes(jd - 0.5 + solNoonUTC / 1440.0);
        eqTime = st.calcEquationOfTime();
        // var solarNoonDec = calcSunDeclination(newt);
        solNoonUTC = 720 + (longitude * 4) - eqTime; // min

        return solNoonUTC;
    }

    private void calcSunUTC(boolean sunset)
    {


        //double noonmin = calcSolNoonUTC(JD, longitude);


        // *** Find the time of solar noon at the location, and use
        //     that declination. This is better than start of the
        //     Julian day


        double delta = longitude;
        if (sunset)
        {
            delta += firstHourAngle;
        }
        else
        {
            delta -= firstHourAngle;
        }
        double timeDiff = 4 * delta;	// in minutes of time
        double timeUTC = 720 + timeDiff - noonEqTime;	// in minutes

        // alert("eqTime = " + eqTime + "\nsolarDec = " + solarDec + "\ntimeUTC = " + timeUTC);

        // *** Second pass includes fractional jday in gamma calc

        SunTimes st = new SunTimes(JD + timeUTC / 1440.0);

        double eqTime = st.calcEquationOfTime();
        double solarDec = st.getSunDeclination();
        double hourAngle = calcHourAngle(latitude, solarDec);
        if (sunset)
        {
            hourAngle *= -1;
        }
        delta = longitude - SunTimes.radToDeg(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes

        // alert("eqTime = " + eqTime + "\nsolarDec = " + solarDec + "\ntimeUTC = " + timeUTC);
        if (sunset)
        {
            setmin = timeUTC;
        }
        else
        {
            risemin = timeUTC;
        }


    }

    public static String Min2Str(double min)
    {
        int imin = (int) (min + 0.5);
        String stime = "" + (imin / 60);
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
}
