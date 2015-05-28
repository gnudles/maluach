/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maluach;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.*;
/**
 *
 * @author orr
 */
public class CityList extends List implements DisplaySelect,ScreenView {

    private boolean isLoaded;
    private double latitudes[];
    private double longitudes[];
    private short knisat_shabat[];
    private String names[];
    public CityList()
    {
        super("בחירת עיר",IMPLICIT);
        isLoaded=false;
    }

    private void Load(String filename)
    {
InputStream in = CityList.class.getResourceAsStream(filename);
        DataInputStream dis = new DataInputStream(in);
        try
        {
            short nCities = dis.readShort();
            names=new String[nCities];
            latitudes=new double[nCities];
            longitudes=new double[nCities];
            knisat_shabat=new short[nCities];
            for (int i=0; i<nCities;i++)
            {
                names[i]=dis.readUTF();
                append(names[i], null);
                latitudes[i]=dis.readDouble();
                longitudes[i]=dis.readDouble();
                knisat_shabat[i]=dis.readShort();
            }
        }
        catch (IOException ex)
        {
            maluach.showAlert("שגיאה: קובץ פגום", "נסה להתקין את התוכנה מחדש", AlertType.INFO);
        }
        isLoaded=true;
    }
    public void Select()
    {
        int i = getSelectedIndex();
        MaluachPreferences.SetLatitude(latitudes[i]);
        MaluachPreferences.SetLongitude(longitudes[i]);
        MaluachPreferences.SetKnisatShabat(knisat_shabat[i]);
        MaluachPreferences.SetCity(names[i]);
        PreferencesForm.getInstance().updateFromPreferences();
    }

    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
        addCommand(CommandPool.getC_select());
    }

    public void OnShow(Object param)
    {

        if (!isLoaded)
            Load("/data/cities.data");
    }
    static private CityList _instance;
    static public CityList getInstance()
    {
        if (_instance == null)
        {
            _instance = new CityList();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }

}
