/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maluach;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author orr
 */
public class TimesForm extends Form implements ScreenView
{
    TextField AlotHashahar;
    TextField Sheyakir;
    TextField Zriha;
    TextField SofKsh;
    TextField SofTfila;
    TextField SofKshMGA;
    TextField SofTfilaMGA;
    TextField HatzotHayom;
    TextField MinhaGdola;
    TextField MinhaKtana;
    TextField PlagMinha;
    TextField KnisatShabat;
    TextField Shkia;
    TextField TzetHakohavim;
    TextField TzetShabat;
    TextField HatzotLayla;
    TextField AchilatHametz;
    TextField BiurHametz;
    TextField TzetHakohavim_ratam;
    static TimesForm _instance;
    static public TimesForm getInstance()
    {
        if (_instance == null)
        {
            _instance = new TimesForm();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }
    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
    }
    TimesForm()
    {
        super("זמנים");
        AlotHashahar=new TextField("עלות השחר",null,5,TextField.UNEDITABLE );
        AlotHashahar.setLayout(TextField.LAYOUT_RIGHT);
        Sheyakir=new TextField("משיכיר",null,5,TextField.UNEDITABLE );
        Sheyakir.setLayout(TextField.LAYOUT_RIGHT);
        Zriha=new TextField("זריחה",null,5,TextField.UNEDITABLE );
        Zriha.setLayout(TextField.LAYOUT_RIGHT);
        SofKshMGA=new TextField("סוף ק\"ש (מג\"א)",null,5,TextField.UNEDITABLE );
        SofKshMGA.setLayout(TextField.LAYOUT_RIGHT);
        SofTfilaMGA=new TextField("סוף תפילה (מג\"א)",null,5,TextField.UNEDITABLE );
        SofTfilaMGA.setLayout(TextField.LAYOUT_RIGHT);
        SofKsh=new TextField("סוף ק\"ש (גר\"א)",null,5,TextField.UNEDITABLE );
        SofKsh.setLayout(TextField.LAYOUT_RIGHT);
        SofTfila=new TextField("סוף תפילה (גר\"א)",null,5,TextField.UNEDITABLE );
        SofTfila.setLayout(TextField.LAYOUT_RIGHT);
        HatzotHayom=new TextField("חצות היום",null,5,TextField.UNEDITABLE );
        HatzotHayom.setLayout(TextField.LAYOUT_RIGHT);
        MinhaGdola=new TextField("מנחה גדולה",null,5,TextField.UNEDITABLE );
        MinhaGdola.setLayout(TextField.LAYOUT_RIGHT);
        MinhaKtana=new TextField("מנחה קטנה",null,5,TextField.UNEDITABLE );
        MinhaKtana.setLayout(TextField.LAYOUT_RIGHT);
        PlagMinha=new TextField("פלג המנחה",null,5,TextField.UNEDITABLE );
        PlagMinha.setLayout(TextField.LAYOUT_RIGHT);
        KnisatShabat=new TextField("כניסת שבת",null,5,TextField.UNEDITABLE );
        KnisatShabat.setLayout(TextField.LAYOUT_RIGHT);
        Shkia=new TextField("שקיעה",null,5,TextField.UNEDITABLE );
        Shkia.setLayout(TextField.LAYOUT_RIGHT);
        TzetHakohavim=new TextField("צאת הכוכבים",null,5,TextField.UNEDITABLE );
        TzetHakohavim.setLayout(TextField.LAYOUT_RIGHT);
        TzetShabat=new TextField("צאת שבת",null,5,TextField.UNEDITABLE );
        TzetShabat.setLayout(TextField.LAYOUT_RIGHT);
        TzetHakohavim_ratam=new TextField("צאת הכוכבים ר\"ת",null,5,TextField.UNEDITABLE );
        TzetHakohavim_ratam.setLayout(TextField.LAYOUT_RIGHT);
        HatzotLayla=new TextField("חצות הליל",null,5,TextField.UNEDITABLE );
        HatzotLayla.setLayout(TextField.LAYOUT_RIGHT);
        AchilatHametz=new TextField("סוף אכילת חמץ",null,5,TextField.UNEDITABLE );
        AchilatHametz.setLayout(TextField.LAYOUT_RIGHT);
        BiurHametz=new TextField("סוף ביעור חמץ",null,5,TextField.UNEDITABLE );
        BiurHametz.setLayout(TextField.LAYOUT_RIGHT);
        
    }

    public void OnShow(Object param)
    {
        super.deleteAll();
        Hdate dateCursor= (Hdate)param;
        SunCalc sc = new SunCalc(dateCursor.get_hd_jd(), MaluachPreferences.GetLatitude(), -MaluachPreferences.GetLongitude(), (int) (MaluachPreferences.GetTimeZoneAt(dateCursor) * 60));
        AlotHashahar.setString(SunCalc.Min2Str(sc.getDawn()));
        super.append(AlotHashahar);
        Sheyakir.setString(SunCalc.Min2Str(sc.getRecognize()));
        super.append(Sheyakir);
        Zriha.setString(SunCalc.Min2Str(sc.getSunrise()));
        super.append(Zriha);
        
        SofKshMGA.setString(SunCalc.Min2Str(sc.getEndTimeKriatShmaMGA()));
        super.append(SofKshMGA);
        SofTfilaMGA.setString(SunCalc.Min2Str(sc.getEndTimeShahritMGA()));
        super.append(SofTfilaMGA);
        
        SofKsh.setString(SunCalc.Min2Str(sc.getEndTimeKriatShma()));
        super.append(SofKsh);
        SofTfila.setString(SunCalc.Min2Str(sc.getEndTimeShahrit()));
        super.append(SofTfila);
        
        if (dateCursor.get_hd_day_in_month()==14 && dateCursor.get_hd_month()==7)
        {
            AchilatHametz.setString(SunCalc.Min2Str(sc.getEndTimeShahritMGA()));
            super.append(AchilatHametz);
            BiurHametz.setString(SunCalc.Min2Str(sc.getBiurHametz()));
            super.append(BiurHametz);
        }
        
        HatzotHayom.setString(SunCalc.Min2Str(sc.getNoon()));
        super.append(HatzotHayom);
        MinhaGdola.setString(SunCalc.Min2Str(sc.getMinhaGdola()));
        super.append(MinhaGdola);
        MinhaKtana.setString(SunCalc.Min2Str(sc.getMinhaKtana()));
        super.append(MinhaKtana);
        PlagMinha.setString(SunCalc.Min2Str(sc.getPlagMinha()));
        super.append(PlagMinha);
        if (dateCursor.get_day_in_week()==6)
        {
            KnisatShabat.setString(SunCalc.Min2Str(sc.getSunset()-MaluachPreferences.GetKnisatShabat()));
            super.append(KnisatShabat);
        }
        Shkia.setString(SunCalc.Min2Str(sc.getSunset()));
        super.append(Shkia);
        TzetHakohavim.setString(SunCalc.Min2Str(sc.getVisibleStars()));
        super.append(TzetHakohavim);

        if (dateCursor.get_day_in_week()==7)
        {
            TzetShabat.setString(SunCalc.Min2Str(sc.getSunset()+40));
            super.append(TzetShabat);
        }
        TzetHakohavim_ratam.setString(SunCalc.Min2Str(sc.getRabenuTam()));
        super.append(TzetHakohavim_ratam);
        HatzotLayla.setString(SunCalc.Min2Str(sc.getNoon()+12*60));
        super.append(HatzotLayla);
    }
}
