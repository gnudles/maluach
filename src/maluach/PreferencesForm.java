/**
 *
 * @author orr
 */
package maluach;

import javax.microedition.lcdui.*;

public class PreferencesForm extends Form implements DisplaySave, CommandCheck, ScreenView
{

    private static final String errortitle = "נתונים שגויים";
    //private static final float timeZoneOffset = (float) 2.2;
    private TextField FieldLongitude;
    private TextField FieldLatitude;
    //private TextField FieldTimeZone;
    private TextField FieldShabatTime;
    private StringItem LabelCity;
    private ChoiceGroup FontSizes;

    private ScreenCommand c_openCitySelector;
    private Command c_getGeoLocation;

    public PreferencesForm()
    {
        super("הגדרות");
        FieldLatitude = new TextField("קוי רוחב (צפ)", null, 12, TextField.DECIMAL);
        FieldLatitude.setLayout(TextField.LAYOUT_RIGHT);
        FieldLongitude = new TextField("קוי אורך (מז)", null, 12, TextField.DECIMAL);
        FieldLongitude.setLayout(TextField.LAYOUT_RIGHT);
        //FieldTimeZone = new TextField("אזור זמן", null, 4, TextField.DECIMAL);
        FieldShabatTime = new TextField("זמן כניסת שבת לפני שקיעה", null, 3, TextField.NUMERIC);
        FieldShabatTime.setLayout(TextField.LAYOUT_RIGHT);
        LabelCity = new StringItem("שם מקום", null);
        LabelCity.setLayout(StringItem.LAYOUT_RIGHT);
        FontSizes = new ChoiceGroup("גודל כתב", Choice.EXCLUSIVE);
        FontSizes.append("קטן", null);
        FontSizes.append("גדול", null);
        super.append(LabelCity);
        super.append(FieldLatitude);
        super.append(FieldLongitude);
        //super.append(FieldTimeZone);
        super.append(FieldShabatTime);
        super.append(FontSizes);

        updateFromPreferences();


        c_openCitySelector = new ScreenCommand(CityList.getInstance(),null,"בחר עיר", 3);
        c_getGeoLocation = new Command("מצא אותי", Command.SCREEN, 3);
        addCommand(c_getGeoLocation);
        addCommand(c_openCitySelector);
    }

    /**
     * 
     */
    public final void updateFromPreferences()
    {
        FieldLatitude.setString(Double.toString(MaluachPreferences.GetLatitude()));
        FieldLongitude.setString(Double.toString(MaluachPreferences.GetLongitude()));
        //FieldTimeZone.setString(Float.toString(MaluachPreferences.GetTimeZone()));
        FieldShabatTime.setString(Integer.toString(MaluachPreferences.GetKnisatShabat()));
        LabelCity.setText(MaluachPreferences.GetCity());
        FontSizes.setSelectedIndex(MaluachPreferences.GetBigFont(), true);
    }

    public boolean Save()
    {
        double latitude = Double.parseDouble(FieldLatitude.getString());
        double longitude = Double.parseDouble(FieldLongitude.getString());
        //float timezone = Float.parseFloat(FieldTimeZone.getString());
        String errorString = "";
        if (Math.abs(latitude) > 90)
        {
            errorString = "קווי רוחב בטווח -90 עד 90" + "\n";
        }
        if (Math.abs(longitude) > 180)
        {
            errorString += "קווי אורך בטווח -180 עד 180" + "\n";
        }
        /*if (Math.abs(timezone) > 12)
        {
            errorString += "אזור זמן בטווח -12 עד 12" + "\n";
        }
        if (Math.abs(longitude / 15.0 - timezone) > timeZoneOffset)
        {
            errorString += "אזור הזמן אינו תואם למיקום שצוין" + "\n";
        }*/
        if (FieldShabatTime.getString().equals(""))
        {
            errorString += "שדה כניסת שבת ריק" + "\n";
        }
        if (!errorString.equals(""))
        {
            maluach.showAlert(errortitle, errorString, AlertType.ERROR);
            return false;
        }
        MaluachPreferences.SetLatitude(latitude);
        MaluachPreferences.SetLongitude(longitude);
        //MaluachPreferences.SetTimeZone(timezone);
        MaluachPreferences.SetKnisatShabat(Short.parseShort(FieldShabatTime.getString()));
        MaluachPreferences.SetBigFont((byte)FontSizes.getSelectedIndex());
        LabelCity.setText(MaluachPreferences.GetCity());
        updateFromPreferences();
        return true;
    }

    public boolean Execute(Command c)
    {
        if (c == c_getGeoLocation)
        {
            if (MaluachPreferences.hasLocationAPI())
            {
                MaluachPreferences.setFromLAPI();
            }
            else
            {
                maluach.showAlert("בעית תמיכה", "המכשיר אינו תומך בזיהוי מיקום", AlertType.ERROR);
            }
            return true ;
        }
        return false;

    }

    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
        addCommand(CommandPool.getC_save());
    }

    public void OnShow(Object param)
    {
    }
    static private PreferencesForm _instance;
    static public PreferencesForm getInstance()
    {
        if (_instance == null)
        {
            _instance = new PreferencesForm();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }

}
