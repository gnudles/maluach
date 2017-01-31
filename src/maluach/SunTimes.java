//using noaa calculation

package maluach;

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
        my_alpha = radToDeg(MicroMath.atan2(tananum, tanadenom));
        // in degrees
    }

    private void calcSunDeclination()
    {
        double sint = Math.sin(degToRad(my_e)) * Math.sin(degToRad(my_lambda));
        my_theta = radToDeg(MicroMath.asin(sint));
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
}
