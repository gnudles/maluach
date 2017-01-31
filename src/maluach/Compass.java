/* This is free and unencumbered software released into the public domain.
 */
package maluach;

/**
 *
 * @author orr
 */
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public final class Compass extends Canvas implements DisplayBack, ScreenView
{

    private double base_rotation;
    private double rotation;
    private final double jerusalemLongitude = 35.23472222222;
    private final double jerusalemLatitude = 31.77694444444;
    private boolean hasSensor;
    private boolean tooClose;
    private float sunElev;
    private float sunAz;
    private float moonElev;
    private float moonAz;
    private String time_now;
    CompassThread compassRun;

    private Compass()
    {
        rotation = 0;
        hasSensor=false;
        this.setFullScreenMode(false);
    }
    public void setSensorAvailable(boolean avail)
    {
        hasSensor=avail;
    }

    public void start()
    {
        compassRun = new CompassThread(this);
        compassRun.start();
    }

    public void updateCoords()
    {
        double lat1=SunTimes.degToRad(MaluachPreferences.GetLatitude());
        double lat2=SunTimes.degToRad(jerusalemLatitude);
        double lon1=SunTimes.degToRad(MaluachPreferences.GetLongitude());
        double lon2=SunTimes.degToRad(jerusalemLongitude);
        double dLon= lon2-lon1;
        if (Math.abs(dLon)+Math.abs(lat1-lat2)<0.004)
        {
            tooClose=true;
        }
        else
            tooClose=false;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
        base_rotation = MicroMath.fast_atan2(y, x);
        
        
        Calendar todays = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        todays.setTime(new Date());
        double deltaT=65;//seconds
        float timezone=TzDstManager.GetTimeZoneAt(new Date());
        double jd=SunMonPosition.calcJD(todays.get(Calendar.DAY_OF_MONTH), todays.get(Calendar.MONTH)+1, todays.get(Calendar.YEAR));
        //jd=SunMonPosition.calcJD(6, 4, todays.get(Calendar.YEAR));
        int hour=todays.get(Calendar.HOUR_OF_DAY);
        int minute=todays.get(Calendar.MINUTE);
        int second=todays.get(Calendar.SECOND);
        double localtime = (hour/*-timezone*/+minute/60.0
                           +(second)/3600.0)/24.0;
        jd+=localtime;
        double TDT=jd+deltaT/3600.0/24.0;
        time_now=Format.Min2Str(((hour+timezone)%24)*60+minute);
        double gmst=SunMonPosition.gMST(jd);
        double lat = MaluachPreferences.GetLatitude()*SunMonPosition.DEG2RAD; // geodetic latitude of observer on WGS84
        double lon = MaluachPreferences.GetLongitude()*SunMonPosition.DEG2RAD; // geodetic latitude of observer on WGS84
        double lmst = SunMonPosition.gMST2LMST(gmst, lon);
        double height = 0.001;
        SunMonPosition.Coor observerCart = SunMonPosition.observer2EquCart(lon, lat, height, gmst);
        SunMonPosition.Coor sunCoor  = SunMonPosition.sunPosition(TDT, lat, lmst*15.*SunMonPosition.DEG2RAD);   // Calculate data for the Sun at given time
        SunMonPosition.Coor moonCoor = SunMonPosition.moonPosition(sunCoor, TDT, observerCart, lmst*15.*SunMonPosition.DEG2RAD);    // Calculate data for the Moon at given time
        
        double elev=sunCoor.alt+SunMonPosition.refraction(sunCoor.alt)*SunMonPosition.DEG2RAD;//radians
        double az=sunCoor.az;//radians
        sunElev=(float)(sunCoor.alt+SunMonPosition.refraction(sunCoor.alt)*SunMonPosition.DEG2RAD);//radians
        sunAz=(float)sunCoor.az;//radians
        moonElev=(float)(moonCoor.alt+SunMonPosition.refraction(moonCoor.alt)*SunMonPosition.DEG2RAD);//radians
        moonAz=(float)moonCoor.az;
        
    }

    public void updateOrientation(float orinet)
    {
        rotation = (orinet * Math.PI / 180);
        repaint();
    }

    protected void paint(Graphics g)
    {

        g.setColor(0x0);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (!hasSensor)
        {
            g.setColor(0xff0000);
            g.drawString("use external compass", 2, 2, Graphics.LEFT|Graphics.TOP);
        }
        g.setColor(0xffffff);

        int minsize = Math.min(getWidth(), getHeight());
        int fonth = g.getFont().getHeight();
        minsize -= 2 * fonth;
        int radius = minsize / 2;
        int left = (getWidth() - minsize) / 2;
        int up = (getHeight() - minsize + fonth) / 2;
        g.drawArc(left, up, minsize, minsize, 0, 360);
        int cx = getWidth() / 2;
        int cy = (getHeight() + fonth) / 2;
        int sinrot = (int) (Math.sin(rotation) * radius);
        int cosrot = (int) (Math.cos(rotation) * radius);
        //north
        int cardirx = -sinrot + cx;
        int cardiry = -cosrot + cy;
        int fontfix = g.getFont().getBaselinePosition() / 2;


        g.drawLine(cx + 4 * (cardirx - cx) / 5, cy + 4 * (cardiry - cy) / 5, cardirx, cardiry);
        g.drawString("N", cx + 3 * (cardirx - cx) / 5, cy + 3 * (cardiry - cy) / 5 - fontfix, Graphics.TOP | Graphics.HCENTER);
        //south
        cardirx = sinrot + cx;
        cardiry = cosrot + cy;
        g.drawLine(cx + 4 * (cardirx - cx) / 5, cy + 4 * (cardiry - cy) / 5, cardirx, cardiry);
        g.drawString("S", cx + 3 * (cardirx - cx) / 5, cy + 3 * (cardiry - cy) / 5 - fontfix, Graphics.TOP | Graphics.HCENTER);
        //west
        cardirx = -cosrot + cx;
        cardiry = sinrot + cy;
        g.drawLine(cx + 4 * (cardirx - cx) / 5, cy + 4 * (cardiry - cy) / 5, cardirx, cardiry);
        g.drawString("W", cx + 3 * (cardirx - cx) / 5, cy + 3 * (cardiry - cy) / 5 - fontfix, Graphics.TOP | Graphics.HCENTER);
        //east
        cardirx = cosrot + cx;
        cardiry = -sinrot + cy;
        g.drawLine(cx + 4 * (cardirx - cx) / 5, cy + 4 * (cardiry - cy) / 5, cardirx, cardiry);
        g.drawString("E", cx + 3 * (cardirx - cx) / 5, cy + 3 * (cardiry - cy) / 5 - fontfix, Graphics.TOP | Graphics.HCENTER);
        //jerusalem
        if(!tooClose)
        {
            cardirx = cx + (int) (Math.sin(base_rotation-rotation) * radius);
            cardiry = cy - (int) (Math.cos(base_rotation-rotation) * radius);
            g.drawLine(cx , cy , cardirx, cardiry);
            g.setColor(0x55ff33);
            g.fillArc(cardirx-5,cardiry-5,11,11,0,360);
            g.setColor(0xffffff);
            g.drawString(time_now, 2, getHeight()-2, Graphics.LEFT|Graphics.BOTTOM);
        }
        else
        {
            g.setColor(0xff0000);
            g.drawString("The entered location is too", 2, getHeight()-4-fonth, Graphics.LEFT|Graphics.BOTTOM);
            g.drawString("close to the Western Wall", 2, getHeight()-2, Graphics.LEFT|Graphics.BOTTOM);
        }
        
        float elev=sunElev;//radians
        float az=sunAz;//radians
        //SunPos sp = new SunPos((int)jd, MaluachPreferences.GetLatitude(), -MaluachPreferences.GetLongitude(), (int)(timezone * 60));
        //sp.calcSunAz();
        //elev=sp.getSunElevation()*SunMonPosition.DEG2RAD;
        
        {//draw sun
            //double az=SunTimes.degToRad(sp.getSunAzimuth());
            //elev=SunTimes.degToRad(elev);
            cardirx = cx + (int) (Math.sin(az - rotation) * radius * Math.cos(elev));
            cardiry = cy - (int) (Math.cos(az - rotation) * radius * Math.cos(elev));
            g.setColor(0xffff00);
            if (elev > -18.0 * SunMonPosition.DEG2RAD && elev < 198.0 * SunMonPosition.DEG2RAD)
            {
                g.fillArc(cardirx - 13, cardiry - 13, 27, 27, 0, 360);
            }
            else
            {
                g.drawArc(cardirx - 13, cardiry - 13, 27, 27, 0, 360);
            }
            g.setColor(0xff0000);
            g.drawString("שמש", cardirx, cardiry - fontfix, Graphics.TOP | Graphics.HCENTER);
        }
        elev=moonElev;//radians
        az=moonAz;//radians
        {
            //double az=SunTimes.degToRad(sp.getSunAzimuth());
            //elev=SunTimes.degToRad(elev);
            cardirx = cx + (int) (Math.sin(az - rotation) * radius * Math.cos(elev));
            cardiry = cy - (int) (Math.cos(az - rotation) * radius * Math.cos(elev));
            g.setColor(0xaaaaaa);
            if (elev > -18.0 * SunMonPosition.DEG2RAD && elev < 198.0 * SunMonPosition.DEG2RAD)
            {
                g.fillArc(cardirx - 13, cardiry - 13, 27, 27, 0, 360);
            }
            else
            {
                g.drawArc(cardirx - 13, cardiry - 13, 27, 27, 0, 360);
            }
            g.setColor(0xff0000);
            g.drawString("ירח", cardirx, cardiry - fontfix, Graphics.TOP | Graphics.HCENTER);
        }
        g.setColor(0xffffff);
        g.fillArc(cx-5,cy-5,11,11,0,360);
    }

    
    public boolean Back()
    {
        compassRun.interrupt();
        return true;
    }

    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
    }

    public void OnShow(Object param)
    {
        updateCoords();
        start();
    }
    static private Compass _instance;
    static public Compass getInstance()
    {

        if (_instance == null)
        {
            _instance = new Compass();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }
}
