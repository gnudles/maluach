/**
 *
 * @author orr
 */

package maluach;

import javax.microedition.lcdui.AlertType;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;

public class LocationAPI extends Thread
{

    Coordinates c=null;

    public void run()
    {
// Get an instance of the provider

        try
        {
            Criteria cr = new Criteria();
            cr.setHorizontalAccuracy(500);
            LocationProvider lp = LocationProvider.getInstance(cr);

// Request the location, setting a one-minute timeout

            Location l = lp.getLocation(60);
            c = l.getQualifiedCoordinates();
        }
        catch (LocationException ex)
        {
            maluach.showAlert("הפעולה נכשלה","מיקומך לא ידוע",AlertType.ERROR);
        }
        catch (InterruptedException ex)
        {
            return;
        }
        if (c != null)
        {
            MaluachPreferences.SetLatitude(c.getLatitude());
            MaluachPreferences.SetLongitude(c.getLongitude());
            MaluachPreferences.SetCity("נתוני מכשיר");
            maluach.showAlert("הצלחה","מיקומך כוון",AlertType.INFO);
            PreferencesForm.getInstance().updateFromPreferences();
        }
    }
    public LocationAPI()
    {
    }
    public boolean isSuccess()
    {
        return (c != null);
    }
}
