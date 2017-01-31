/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import javax.microedition.lcdui.AlertType;
import javax.microedition.location.LocationException;
import javax.microedition.location.Orientation;

/**
 *
 * @author incognito
 */
public class CompassThread extends Thread {

	Compass defcompass;
	public void run() {

// Get an instance of the provider
		try {
                    
			Class.forName("javax.microedition.location.Orientation");
			Orientation myorien;
			float orien, last = 361;//361 is invalid angle, so its a good dummy initialization value.
                        defcompass.setSensorAvailable(true);
			while (true) {
                                myorien = Orientation.getOrientation();
				orien = myorien.getCompassAzimuth();
				if (orien != last) {
					defcompass.updateOrientation(orien);
					last = orien;
				}
				this.sleep(400);
			}
                        
		}
		catch (InterruptedException ex) {
                    defcompass.setSensorAvailable(false);
                }
		catch (Exception ex) {
                    defcompass.setSensorAvailable(false);
			//maluach.showAlert("התראה", "יש להשתמש במצפן חיצוני", AlertType.INFO);
		}
                defcompass.setSensorAvailable(false);

	}

	public CompassThread(Compass compass) {
		defcompass=compass;
	}
}
