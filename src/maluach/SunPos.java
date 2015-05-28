/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author orr
 */
public class SunPos
{
    private double latitude, longitude;
    private int JD;
    private int timezone;
    private double azimuth;
    private double elevation;
    public double getSunAzimuth()
    {
        return azimuth;
    }
    public double getSunElevation()
    {
        return elevation;//if elev is less then 18, then its too dark...
    }
    SunPos(int JD, double latitude, double longitude, int timezone)
    {
        this.timezone = timezone;
        this.JD = JD;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void calcSunAz(double localtime)
 
    {
        SunTimes st = new SunTimes(JD + (localtime - timezone) / (60.0 * 24.0));
        double eqTime = st.calcEquationOfTime();
        double theta = st.getTheta();
        double solarTimeFix = eqTime - 4.0 * longitude - timezone;
        double trueSolarTime = localtime + solarTimeFix;
        while (trueSolarTime > 1440)
        {
            trueSolarTime -= 1440;
        }
        double hourAngle = trueSolarTime / 4.0 - 180.0;
        if (hourAngle < -180)
        {
            hourAngle += 360.0;
        }
        double haRad = SunTimes.degToRad(hourAngle);
        double csz = Math.sin(SunTimes.degToRad(latitude)) * Math.sin(SunTimes.degToRad(theta)) + Math.cos(SunTimes.degToRad(latitude)) * Math.cos(SunTimes.degToRad(theta)) * Math.cos(haRad);
        if (csz > 1.0)
        {
            csz = 1.0;
        }
        else if (csz < -1.0)
        {
            csz = -1.0;
        }
        double zenith = SunTimes.radToDeg(SunTimes.acos(csz));
        double azDenom = (Math.cos(SunTimes.degToRad(latitude)) * Math.sin(SunTimes.degToRad(zenith)));
        double azimuth;
        if (Math.abs(azDenom) > 0.001)
        {
            double azRad = ((Math.sin(SunTimes.degToRad(latitude)) * Math.cos(SunTimes.degToRad(zenith))) - Math.sin(SunTimes.degToRad(theta))) / azDenom;
            if (Math.abs(azRad) > 1.0)
            {
                if (azRad < 0)
                {
                    azRad = -1.0;
                }
                else
                {
                    azRad = 1.0;
                }
            }
            azimuth = 180.0 - SunTimes.radToDeg(SunTimes.acos(azRad));
            if (hourAngle > 0.0)
            {
                azimuth = -azimuth;
            }
        }
        else
        {
            if (latitude > 0.0)
            {
                azimuth = 180.0;
            }
            else
            {
                azimuth = 0.0;
            }
        }
        if (azimuth < 0.0)
        {
            azimuth += 360.0;
        }
        double exoatmElevation = 90.0 - zenith;

// Atmospheric Refraction correction
        double refractionCorrection;
        if (exoatmElevation > 85.0)
        {
            refractionCorrection = 0.0;
        }
        else
        {
            double te = Math.tan(SunTimes.degToRad(exoatmElevation));
            if (exoatmElevation > 5.0)
            {
                refractionCorrection = 58.1 / te - 0.07 / (te * te * te) + 0.000086 / (te * te * te * te * te);
            }
            else if (exoatmElevation > -0.575)
            {
                refractionCorrection = 1735.0 + exoatmElevation * (-518.2 + exoatmElevation * (103.4 + exoatmElevation * (-12.79 + exoatmElevation * 0.711)));
            }
            else
            {
                refractionCorrection = -20.774 / te;
            }
            refractionCorrection = refractionCorrection / 3600.0;
        }

        double solarZen = zenith - refractionCorrection;

        this.azimuth=azimuth;
        this.elevation = 90.0-solarZen;

        
        /*
         * if ((output) && (solarZen > 108.0) ) {
         * document.getElementById("azbox").value = "dark"
         * document.getElementById("elbox").value = "dark" } else if (output) {
         * document.getElementById("azbox").value = Math.floor(azimuth*100
         * +0.5)/100.0 document.getElementById("elbox").value =
         * Math.floor((90.0-solarZen)*100+0.5)/100.0 if
         * (document.getElementById("showae").checked) {
         * showLineGeodesic("#ffff00", azimuth) } } return (azimuth)
         */
    
    }
        public void calcSunAz()
    {
        Calendar todays = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        todays.setTime(new Date());

        double localtime = todays.get(Calendar.HOUR_OF_DAY) * 60+timezone;
        localtime += todays.get(Calendar.MINUTE);
        calcSunAz(localtime);
    }
        
}
