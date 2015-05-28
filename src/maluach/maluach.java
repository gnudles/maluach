/**
 *
 * @author orr
 */
package maluach;

import textviewer.ScriptedTextViewer;
import java.util.Stack;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

public class maluach extends MIDlet implements CommandListener
{

    CalendarViewer m_calendar;
    DateSelector m_dateSelector;
    ScriptedTextViewer m_scriptedTextViewer;
    PreferencesForm m_defaultPreferences;
    //Birthdays m_birthdays;
    Compass m_compass;
    private Stack s_displays;
    private Display s_mainDisplay;
    private static maluach s_instance;
    private static Alert s_alert;
    boolean paused;

    public static maluach getInstance()
    {
        return s_instance;
    }

    public static void showAlert(String title, String text, AlertType type)
    {
        s_alert.setTitle(title);
        s_alert.setString(text);
        s_alert.setType(type);
        s_instance.s_mainDisplay.setCurrent(s_alert);
    }

    public void PushScreen(Displayable disp)
    {
        s_displays.push(s_mainDisplay.getCurrent());
        s_mainDisplay.setCurrent(disp);
    }

    public maluach()
    {
        s_displays = new Stack();
        CommandPool.Initiate();
        s_alert = new Alert(null);
        s_alert.setTimeout(Alert.FOREVER);
        try
        {
            RecordStore.deleteRecordStore("maluach");
        }
        catch (Exception e)
        {

        }
        MaluachPreferences.initialise("maluach2");
        paused=false;
    }//end constructor

    public void startApp()
    {
        s_instance = this;
        s_mainDisplay = Display.getDisplay(this);
        
        if(!paused)
            s_mainDisplay.setCurrent(CalendarViewer.getInstance());
        paused=false;

        //Make the Canvas the current display.
    }//end startApp

    public void pauseApp()
    {
        paused=true;
    }//end pauseApp

    public void destroyApp(boolean unconditional)
    {
        MaluachPreferences.finalise();
    }

    public void commandAction(Command c, Displayable d)
    {

        if (c == CommandPool.getC_exit())
        {
            destroyApp(true);
            s_mainDisplay.setCurrent(null);
            notifyDestroyed();
            return;
        }
        if (c == CommandPool.getC_back())
        {
            if (d instanceof DisplayBack)
            {
                if (!((DisplayBack) d).Back())
                {
                    return;
                }
            }
            s_mainDisplay.setCurrent((Displayable) s_displays.pop());
            return;
        }
        if (c == CommandPool.getC_select())
        {
            if (d instanceof DisplaySelect)
            {
                ((DisplaySelect) d).Select();
            }
            s_mainDisplay.setCurrent((Displayable) s_displays.pop());
            return;
        }
        if (c == CommandPool.getC_save())
        {
            if (d instanceof DisplaySave)
            {
                if (!((DisplaySave) d).Save())
                {
                    return;
                }
            }
            s_mainDisplay.setCurrent((Displayable) s_displays.pop());
            return;
        }

        if (c instanceof ScreenCommand)
        {
            ((ScreenCommand)c).showScreen();
        }
        if (d instanceof CommandCheck)
        {
            if (((CommandCheck) d).Execute(c)) //if they took the command
            {
                return;
            }
        }
    }
}
