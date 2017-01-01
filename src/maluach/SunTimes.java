/**
 *
 * @author orr
 */

package maluach;
//using noaa calculation
public class SunTimes
{

    private double my_t;
    private double my_L0;
    private double my_M;
    private double my_C;
    private double my_O;
    private double my_v;
    private double my_ec;
    private double my_R;
    private double my_omega;
    private double my_lambda;
    private double my_e0;
    private double my_e;
    private double my_alpha;
    private double my_theta;

    public double getTheta()
    {
        return my_theta;
    }
    public double getR()
    {
        return my_R;
    }
    
    public SunTimes(double jd)
    {

        my_t = calcTimeJulianCent(jd);
        calcGeomMeanLongSun();//my_L0
        calcGeomMeanAnomalySun();//my_M
        calcSunEqOfCenter();//my_C
        calcSunTrueLong();//my_O
        calcSunTrueAnomaly();//my_v
        calcEccentricityEarthOrbit();//my_ec
        calcSunRadVector();//my_R
        calcSunApparentLong();//my_lambda,my_omega
        calcMeanObliquityOfEcliptic();//my_e0
        calcObliquityCorrection();//my_e
        calcSunRtAscension();//my_alpha
        calcSunDeclination();//my_theta

    }
    


    
    public static double radToDeg(double angleRad)
    {
        return (180.0 * angleRad / Math.PI);
    }

    public static double degToRad(double angleDeg)
    {
        return (Math.PI * angleDeg / 180.0);
    }

    public static double calcTimeJulianCent(double jd)
    {
        double t = (jd - 2451545.0) / 36525.0;
        return t;
    }

    private void calcGeomMeanLongSun()
    {
        my_L0 = 280.46646 + my_t * (36000.76983 + 0.0003032 * my_t);
        while (my_L0 > 360.0)
        {
            my_L0 -= 360.0;
        }
        while (my_L0 < 0.0)
        {
            my_L0 += 360.0;
        }
        // in degrees
    }
    private void calcSunRadVector()
    {
        my_R = (1.000001018 * (1 - my_ec * my_ec)) / (1 + my_ec * Math.cos(degToRad(my_v)));// in AUs
    }

    private void calcGeomMeanAnomalySun()
    {
        my_M = 357.52911 + my_t * (35999.05029 - 0.0001537 * my_t);
        // in degrees
    }

    private void calcEccentricityEarthOrbit()
    {
        my_ec = 0.016708634 - my_t * (0.000042037 + 0.0000001267 * my_t);
        // unitless
    }

    private void calcSunEqOfCenter()
    {
        double mrad = degToRad(my_M);
        double sinm = Math.sin(mrad);
        double sin2m = Math.sin(mrad + mrad);
        double sin3m = Math.sin(mrad + mrad + mrad);
        my_C = sinm * (1.914602 - my_t * (0.004817 + 0.000014 * my_t)) + sin2m * (0.019993 - 0.000101 * my_t) + sin3m * 0.000289;
        // in degrees
    }

    private void calcSunTrueLong()
    {
        my_O = my_L0 + my_C;
        // in degrees
    }

    private void calcSunTrueAnomaly()
    {

        my_v = my_M + my_C;
        // in degrees
    }

    /*private void calcSunRadVector()
    {
    my_R = (1.000001018 * (1 - my_ec * my_ec)) / (1 + my_ec * Math.cos(degToRad(my_v)));
    // in AUs
    }*/
    private void calcSunApparentLong()
    {
        my_omega = 125.04 - 1934.136 * my_t;
        my_lambda = my_O - 0.00569 - 0.00478 * Math.sin(degToRad(my_omega));
    }

    private void calcMeanObliquityOfEcliptic()
    {
        double seconds = 21.448 - my_t * (46.8150 + my_t * (0.00059 - my_t * (0.001813)));
        my_e0 = 23.0 + (26.0 + (seconds / 60.0)) / 60.0;
        // in degrees
    }

    private void calcObliquityCorrection()
    {
        my_omega = 125.04 - 1934.136 * my_t;
        my_e = my_e0 + 0.00256 * Math.cos(degToRad(my_omega));
        // in degrees
    }

    private void calcSunRtAscension()
    {
        double tananum = (Math.cos(degToRad(my_e)) * Math.sin(degToRad(my_lambda)));
        double tanadenom = (Math.cos(degToRad(my_lambda)));
        my_alpha = radToDeg(Trig.atan2(tananum, tanadenom));
        // in degrees
    }

    private void calcSunDeclination()
    {
        double sint = Math.sin(degToRad(my_e)) * Math.sin(degToRad(my_lambda));
        my_theta = radToDeg(Trig.asin(sint));
        // in degrees
    }

    public double getSunDeclination()
    {
        return my_theta;
    }

    public double calcEquationOfTime()
    {

        double y = Math.tan(degToRad(my_e) / 2.0);
        y *= y;
        double sin2l0 = Math.sin(2.0 * degToRad(my_L0));
        double sinm = Math.sin(degToRad(my_M));
        double cos2l0 = Math.cos(2.0 * degToRad(my_L0));
        double sin4l0 = Math.sin(4.0 * degToRad(my_L0));
        double sin2m = Math.sin(2.0 * degToRad(my_M));

        double Etime = y * sin2l0 - 2.0 * my_ec * sinm + 4.0 * my_ec * y * sinm * cos2l0
                       - 0.5 * y * y * sin4l0 - 1.25 * my_ec * my_ec * sin2m;
        return radToDeg(Etime) * 4.0;	// in minutes of time
    }
   /* 	function calcSun(riseSetForm, latLongForm, index, index2) 
	{
		if(index2 != 0)
		{
			setLatLong(latLongForm, index2);
		}
		var latitude = getLatitude(latLongForm);
		var longitude = getLongitude(latLongForm);
		var indexRS = riseSetForm.mos.selectedIndex
		if (isValidInput(riseSetForm, indexRS, latLongForm)) 
		{
			if((latitude >= -90) && (latitude < -89.8))
			{
				alert("All latitudes between 89.8 and 90 S\n will be set to -89.8.");
				latLongForm["latDeg"].value = -89.8;
				latitude = -89.8;
			}
			if ((latitude <= 90) && (latitude > 89.8))
			{
		            alert("All latitudes between 89.8 and 90 N\n will be set to 89.8.");
				latLongForm["latDeg"].value = 89.8;
				latitude = 89.8;
			}

	//*****	Get calc date/time			

//			var julDay = calcJulianDay(indexRS, 
//				parseFloat(riseSetForm["day"].value), 
//				isLeapYear(riseSetForm["year"].value));

			var zone = parseFloat(latLongForm["hrsToGMT"].value);
			var daySavings = YesNo[index].value;  // = 0 (no) or 60 (yes)

			if(zone > 12 || zone < -12.5)
			{
				alert("The offset must be between -12.5 and 12.  \n Setting \"Off-Set\"=0");
				zone = "0";
				latLongForm["hrsToGMT"].value = zone;
			}

			var ss = parseFloat(riseSetForm["secs"].value);
			var mm = parseFloat(riseSetForm["mins"].value);
			var hh = parseFloat(riseSetForm["hour"].value) - (daySavings/60);
			if(riseSetForm.ampm[1].checked) 
			{
				hh += 12;
			}
			while (hh > 23) 
			{
				hh -= 24;
			}
			riseSetForm["hour"].value = hh + (daySavings/60);
			if (mm > 9) 
			{
				riseSetForm["mins"].value = mm;
			}
			else 
			{
				riseSetForm["mins"].value = "0" + mm;
			}
			if (ss > 9) 
			{
				riseSetForm["secs"].value = ss;
			}
			else 
			{
				riseSetForm["secs"].value = "0" + ss;
			}
			riseSetForm.ampm[2].checked = 1;

// timenow is GMT time for calculation
			timenow = hh + mm/60 + ss/3600 + zone;	// in hours since 0Z
			//alert("timenow = " + timenow);


			var JD = (calcJD(parseFloat(riseSetForm["year"].value), indexRS + 1, parseFloat(riseSetForm["day"].value)));
			var dow = calcDayOfWeek(JD);
			var doy = calcDayOfYear(indexRS + 1, parseFloat(riseSetForm["day"].value), isLeapYear(riseSetForm["year"].value));
			var T = calcTimeJulianCent(JD + timenow/24.0); 
			//var L0 = calcGeomMeanLongSun(T);
			//var M = calcGeomMeanAnomalySun(T);
			//var e = calcEccentricityEarthOrbit(T);
			//var C = calcSunEqOfCenter(T);
			//var O = calcSunTrueLong(T);
			//var v = calcSunTrueAnomaly(T);
			var R = calcSunRadVector(T);
			//var lambda = calcSunApparentLong(T);
			//var epsilon0 = calcMeanObliquityOfEcliptic(T);
			//var epsilon = calcObliquityCorrection(T);
			var alpha = calcSunRtAscension(T);
			var theta = calcSunDeclination(T);
			var Etime = calcEquationOfTime(T);

			var eqTime = Etime;
			var solarDec = theta; // in degrees
			var earthRadVec = R;

			riseSetForm["eqTime"].value = (Math.floor(100*eqTime))/100;
			riseSetForm["solarDec"].value = (Math.floor(100*(solarDec)))/100;

			var solarTimeFix = eqTime - 4.0 * longitude + 60.0 * zone;
			var trueSolarTime = hh * 60.0 + mm + ss/60.0 + solarTimeFix;
			// in minutes

			while (trueSolarTime > 1440)
			{
				trueSolarTime -= 1440;
			}

			//var hourAngle = calcHourAngle(timenow, longitude, eqTime);
			var hourAngle = trueSolarTime / 4.0 - 180.0;
			// Thanks to Louis Schwarzmayr for finding our error,
			// and providing the following 4 lines to fix it:
			if (hourAngle < -180) 
			{
			  hourAngle += 360.0;
			}
			// alert ("Hour Angle = " + hourAngle);

			var haRad = degToRad(hourAngle);

			var csz = Math.sin(degToRad(latitude)) * 
				Math.sin(degToRad(solarDec)) + 
				Math.cos(degToRad(latitude)) * 
				Math.cos(degToRad(solarDec)) * Math.cos(haRad);
			if (csz > 1.0) 
			{
				csz = 1.0;
			} else if (csz < -1.0) 
			{ 
				csz = -1.0; 
			}
			var zenith = radToDeg(Math.acos(csz));

			var azDenom = ( Math.cos(degToRad(latitude)) *
					Math.sin(degToRad(zenith)) );
			if (Math.abs(azDenom) > 0.001) {
				azRad = (( Math.sin(degToRad(latitude)) * 
					Math.cos(degToRad(zenith)) ) - 
					Math.sin(degToRad(solarDec))) / azDenom;
				if (Math.abs(azRad) > 1.0) {
					if (azRad < 0) {
						azRad = -1.0;
					} else {
						azRad = 1.0;
					}
				}

				var azimuth = 180.0 - radToDeg(Math.acos(azRad));

				if (hourAngle > 0.0) {
					azimuth = -azimuth;
				}
			} else {
				if (latitude > 0.0) {
					azimuth = 180.0;
				} else { 
					azimuth = 0.0;
				}
			}
			if (azimuth < 0.0) {
				azimuth += 360.0;
			}

			exoatmElevation = 90.0 - zenith;
			if (exoatmElevation > 85.0) {
				refractionCorrection = 0.0;
			} else {
				te = Math.tan (degToRad(exoatmElevation));
				if (exoatmElevation > 5.0) {
					refractionCorrection = 58.1 / te - 0.07 / (te*te*te) +
						0.000086 / (te*te*te*te*te);
				} else if (exoatmElevation > -0.575) {
					refractionCorrection = 1735.0 + exoatmElevation *
						(-518.2 + exoatmElevation * (103.4 +
						exoatmElevation * (-12.79 + 
						exoatmElevation * 0.711) ) );
				} else {
					refractionCorrection = -20.774 / te;
				}
				refractionCorrection = refractionCorrection / 3600.0;
			}

			solarZen = zenith - refractionCorrection;
					
			if(solarZen < 108.0) { // astronomical twilight	
			  riseSetForm["azimuth"].value = (Math.floor(100*
azimuth))/100;
			  riseSetForm["elevation"].value = (Math.floor(100*
(90.0 - solarZen)))/100;
			  if (solarZen < 90.0) {
				riseSetForm["coszen"].value = 
(Math.floor(10000.0*(Math.cos(degToRad(solarZen)))))/10000.0;
			  } else {
				riseSetForm["coszen"].value = 0.0;
			  }
			} else {  // do not report az & el after astro twilight
			  riseSetForm["azimuth"].value = "dark";
			  riseSetForm["elevation"].value = "dark";
			  riseSetForm["coszen"].value = 0.0;
			}

		//***********Conv lat and long
			convLatLong(latLongForm);
		} else {			// end of IF ISVALIDINPUT
			riseSetForm["azimuth"].value = "error";
			riseSetForm["elevation"].value = "error";
			riseSetForm["eqTime"].value = "error";
			riseSetForm["solarDec"].value = "error";
			riseSetForm["coszen"].value = "error";

			// alert("Invalid Input");
		}
	}*/
}
