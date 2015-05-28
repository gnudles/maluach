/**
 *
 * @author orr
 */
package maluach;

import javax.microedition.lcdui.*;

public class PreferencesForm extends Form implements DisplaySave, CommandCheck, ScreenView
{

    private static final String errortitle = "������ ������";
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
        super("������");
        FieldLatitude = new TextField("��� ���� (��)", null, 12, TextField.DECIMAL);
        FieldLatitude.setLayout(TextField.LAYOUT_RIGHT);
        FieldLongitude = new TextField("��� ���� (��)", null, 12, TextField.DECIMAL);
        FieldLongitude.setLayout(TextField.LAYOUT_RIGHT);
        //FieldTimeZone = new TextField("���� ���", null, 4, TextField.DECIMAL);
        FieldShabatTime = new TextField("��� ����� ��� ���� �����", null, 3, TextField.NUMERIC);
        FieldShabatTime.setLayout(TextField.LAYOUT_RIGHT);
        LabelCity = new StringItem("�� ����", null);
        LabelCity.setLayout(StringItem.LAYOUT_RIGHT);
        FontSizes = new ChoiceGroup("���� ���", Choice.EXCLUSIVE);
        FontSizes.append("���", null);
        FontSizes.append("����", null);
        super.append(LabelCity);
        super.append(FieldLatitude);
        super.append(FieldLongitude);
        //super.append(FieldTimeZone);
        super.append(FieldShabatTime);
        super.append(FontSizes);

        updateFromPreferences();


        c_openCitySelector = new ScreenCommand(CityList.getInstance(),null,"��� ���", 3);
        c_getGeoLocation = new Command("��� ����", Command.SCREEN, 3);
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
            errorString = "���� ���� ����� -90 �� 90" + "\n";
        }
        if (Math.abs(longitude) > 180)
        {
            errorString += "���� ���� ����� -180 �� 180" + "\n";
        }
        /*if (Math.abs(timezone) > 12)
        {
            errorString += "���� ��� ����� -12 �� 12" + "\n";
        }
        if (Math.abs(longitude / 15.0 - timezone) > timeZoneOffset)
        {
            errorString += "���� ���� ���� ���� ������ �����" + "\n";
        }*/
        if (FieldShabatTime.getString().equals(""))
        {
            errorString += "��� ����� ��� ���" + "\n";
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
                maluach.showAlert("���� �����", "������ ���� ���� ������ �����", AlertType.ERROR);
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
