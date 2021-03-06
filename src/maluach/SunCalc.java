//using noaa calculation
package maluach;

import java.util.Calendar;
import java.util.Date;

public class SunCalc
{

    private double latitude, longitude;
    private double JD;
    YDate.TimeZoneProvider timezone;
    private double noonEqTime, noonSolarDec, firstHourAngle, noonmin, risemin, setmin;
    private double risemin_16, setmin_16, setmin_8, setmin_6;
    private double risemin_11,risemin_19;

    public SunCalc(double JD, double latitude, double longitude, YDate.TimeZoneProvider timezone)
    {
        double sun_alt_angle=90.833;
        this.JD = JD;
        this.timezone = timezone;

        this.latitude = latitude;
        this.longitude = longitude;
        noonmin = calcSolNoonUTC(JD, longitude);
        SunTimes st = new SunTimes(JD + noonmin / 1440.0);

        // *** First pass to approximate sunrise (using solar noon)

        noonEqTime = st.calcEquationOfTime();

        noonSolarDec = st.getSunDeclination();
        firstHourAngle = SunTimes.radToDeg(calcHourAngle(latitude, noonSolarDec,sun_alt_angle));
        risemin=calcSunUTC(false,sun_alt_angle);
        setmin=calcSunUTC(true,sun_alt_angle);
        risemin_16=calcSunUTC(false,106.01);
        setmin_16=calcSunUTC(true,106.01);
        setmin_8=calcSunUTC(true,98.5);
        setmin_6=calcSunUTC(true,95.95);
        risemin_11=calcSunUTC(false,101.5);
        risemin_19=calcSunUTC(false,109.75);

    }
    /*
     * static public double ElevationAdjustment(double elevation) { double
     * earthRadius = 6356.9; double elevationAdjustment =
     * Math.toDegrees(SunTimes.acos(earthRadius/ (earthRadius + (elevation /
     * 1000)))); return elevationAdjustment;
    }
     */
    public double correctTz(double t)
    {
        Date d=YDate.toDate(YDate.JdtoDays((int)this.JD), (float)(t/60.0));
        return t+timezone.getOffset(d)*60.0;
    }
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
    public double getDawn()// 72 minutes before the sunrise, 16 degrees
    {  
        return risemin_16;
    }
    public double getDawn90()// 90 minutes before the sunrise, 19.75 degrees
    {  
        return risemin_19;
    }
    

    public double getRecognize()// 50 minutes before the sunrise, 11.5 degrees
    {
        return risemin_11;
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

    public double getMinhaGdolaZmanit()// half day-hour (shaot zmaniot) after the noon
    {
        return noonmin + 1 * (setmin - noonmin) / 12.0;//
    }
    public double getMinhaGdola()// half hour after the noon
    {
        return noonmin + 30;
    }

    public double getMinhaKtana()// 3.5 day-hour (shaot zmaniot) after the noon
    {
        return noonmin + 7 * (setmin - noonmin) / 12.0;//
    }

    public double getPlagMinha()// 3.5 day-hour (shaot zmaniot) after the noon
    {
        return noonmin + 19 * (setmin - noonmin) / 24.0;//
    }
    public double getVisibleStars()// 8.5 degrees
    {
        return setmin_8;
    }
    public double getVisibleStarsGeonim()// 5.95 degrees
    {
        return setmin_6;
    }
    
    public double getVisibleStarsZmaniot24()// 24*3/4 minutes zmaniot after the sunset
    {
        return setmin + (setmin - noonmin) / 20.0;//(24.0*3/4/60)/6=1/20
    }
    public double getVisibleStarsZmaniot18()// 18*3/4 minutes zmaniot after the sunset
    {
        return setmin + (setmin - noonmin)*(18.0*3/4/60)/6;
    }
    public double getVisibleStarsZmaniot225()// 22.5*3/4 minutes zmaniot after the sunset
    {
        return setmin + (setmin - noonmin)*(22.5*3/4/60)/6;
    }
    public double getBiurHametz()
    {
        return (risemin-72)+5*((setmin +72) - (risemin-72))/12.0;//
    }
    public double getRabenuTam()
    {
        //int minutes_time=(int)(1.2*(setmin - noonmin) / 12.0); //72 minutes is 1.2 hours
        return setmin_16;
    }

    public static int calcDayOfYear(int mn, int dy, boolean isl)
    {
        int k = (isl) ? 1 : 2;
        int doy = (275 * mn) / 9 - k * ((mn + 9) / 12) + dy - 30;
        return doy;
    }

    public static double calcHourAngle(double lat, double solarDec,double alt_angle)//for sunset multiply by -1
    {
        double latRad = SunTimes.degToRad(lat);
        double sdRad = SunTimes.degToRad(solarDec);

        double HAarg = (Math.cos(SunTimes.degToRad(alt_angle)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad));

        double HA = (MicroMath.acos(HAarg));

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

    private double calcSunUTC(boolean sunset,double alt_angle)
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
        double hourAngle = calcHourAngle(latitude, solarDec,alt_angle);
        if (sunset)
        {
            hourAngle *= -1;
        }
        delta = longitude - SunTimes.radToDeg(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes

        // alert("eqTime = " + eqTime + "\nsolarDec = " + solarDec + "\ntimeUTC = " + timeUTC);
        return timeUTC;

    }


}
