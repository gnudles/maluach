/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

/**
 *
 * @author orr
 */
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
        base_rotation = SunTimes.fast_atan2(y, x);
        
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
        }
        else
        {
            g.setColor(0xff0000);
            g.drawString("The entered location is too", 2, getHeight()-4-fonth, Graphics.LEFT|Graphics.BOTTOM);
            g.drawString("close to the Western Wall", 2, getHeight()-2, Graphics.LEFT|Graphics.BOTTOM);
        }
        Hdate nowdate=new Hdate();
        SunPos sp = new SunPos(nowdate.get_hd_jd(), MaluachPreferences.GetLatitude(), -MaluachPreferences.GetLongitude(), (int) (MaluachPreferences.GetTimeZoneAt(nowdate) * 60));
        sp.calcSunAz();
        double elev=sp.getSunElevation();
        if (elev > -18.0 && elev < 198.0)
        {
            double az=SunTimes.degToRad(sp.getSunAzimuth());
            elev=SunTimes.degToRad(elev);
            cardirx = cx + (int) (Math.sin(az-rotation) * radius*Math.cos(elev));
            cardiry = cy - (int) (Math.cos(az-rotation) * radius*Math.cos(elev));
            g.setColor(0xffff00);
            g.fillArc(cardirx-13,cardiry-13,27,27,0,360);
            g.setColor(0xff0000);
            g.drawString("SUN", cardirx, cardiry -fontfix, Graphics.TOP | Graphics.HCENTER);
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
