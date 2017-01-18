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
        YDate dateCursor= (YDate)param;
        SunCalc sc = new SunCalc(SunMonPosition.calcJD(dateCursor.gd.dayInMonth(), dateCursor.gd.month(), dateCursor.gd.year()),
                MaluachPreferences.GetLatitude(), -MaluachPreferences.GetLongitude(), new TzDstManager());
        AlotHashahar.setString(Format.Min2Str(sc.correctTz(sc.getDawn())));
        super.append(AlotHashahar);
        Sheyakir.setString(Format.Min2Str(sc.correctTz(sc.getRecognize())));
        super.append(Sheyakir);
        Zriha.setString(Format.Min2Str(sc.correctTz(sc.getSunrise())));
        super.append(Zriha);
        
        SofKshMGA.setString(Format.Min2Str(sc.correctTz(sc.getEndTimeKriatShmaMGA())));
        super.append(SofKshMGA);
        SofTfilaMGA.setString(Format.Min2Str(sc.correctTz(sc.getEndTimeShahritMGA())));
        super.append(SofTfilaMGA);
        
        SofKsh.setString(Format.Min2Str(sc.correctTz(sc.getEndTimeKriatShma())));
        super.append(SofKsh);
        SofTfila.setString(Format.Min2Str(sc.correctTz(sc.getEndTimeShahrit())));
        super.append(SofTfila);
        
        if (dateCursor.hd.dayInMonth()==14 && dateCursor.hd.monthID()==YDate.JewishDate.M_ID_NISAN)
        {
            AchilatHametz.setString(Format.Min2Str(sc.correctTz(sc.getEndTimeShahritMGA())));
            super.append(AchilatHametz);
            BiurHametz.setString(Format.Min2Str(sc.correctTz(sc.getBiurHametz())));
            super.append(BiurHametz);
        }
        
        HatzotHayom.setString(Format.Min2Str(sc.correctTz(sc.getNoon())));
        super.append(HatzotHayom);
        MinhaGdola.setString(Format.Min2Str(sc.correctTz(sc.getMinhaGdola())));
        super.append(MinhaGdola);
        MinhaKtana.setString(Format.Min2Str(sc.correctTz(sc.getMinhaKtana())));
        super.append(MinhaKtana);
        PlagMinha.setString(Format.Min2Str(sc.correctTz(sc.getPlagMinha())));
        super.append(PlagMinha);
        if (dateCursor.hd.dayInWeek()==6)
        {
            KnisatShabat.setString(Format.Min2Str(sc.correctTz(sc.getSunset()-MaluachPreferences.GetKnisatShabat())));
            super.append(KnisatShabat);
        }
        Shkia.setString(Format.Min2Str(sc.correctTz(sc.getSunset())));
        super.append(Shkia);
        TzetHakohavim.setString(Format.Min2Str(sc.correctTz(sc.getVisibleStars())));
        super.append(TzetHakohavim);

        if (dateCursor.hd.dayInWeek()==7)
        {
            TzetShabat.setString(Format.Min2Str(sc.correctTz(sc.getSunset()+40)));
            super.append(TzetShabat);
        }
        TzetHakohavim_ratam.setString(Format.Min2Str(sc.correctTz(sc.getRabenuTam())));
        super.append(TzetHakohavim_ratam);
        HatzotLayla.setString(Format.Min2Str(sc.correctTz(sc.getNoon()+12*60)));
        super.append(HatzotLayla);
    }
}
