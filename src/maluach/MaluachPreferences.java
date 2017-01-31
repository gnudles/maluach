/* This is free and unencumbered software released into the public domain.
 */

package maluach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class MaluachPreferences
{

    static private RecordStore rs = null;
    static private double latitude;
    static private double longitude;
    //static private float timezone;
    static private String city;
    static private short knisatshabat;
    static private byte textv_bigfont;

    static public boolean hasLocationAPI()
    {
        try
        {
            Class.forName("javax.microedition.location.LocationProvider");
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    static public void initialise(String db)
    {
        try
        {

            rs = RecordStore.openRecordStore(db, true);
            if (rs.getNumRecords() >= 1)
            {
                setFromRecord();
                return;
            }
        }
        catch (RecordStoreException ex)
        {
        }
        catch (IOException ex)
        {
        }
        setDefaultValues();

    }
    static public boolean HebrewInteface()
    {
        return System.getProperty( "microedition.locale" ).startsWith("he");
    }
    static public void setFromLAPI()
    {
        LocationAPI locationer = new LocationAPI();
        locationer.start();
    }

    static public void setDefaultValues()
    {
        latitude = 31.768318;
        longitude = 35.213711;
        city = "ירושלים";
        knisatshabat = 40;
        textv_bigfont=0;
        
    }

    static public void setFromRecord()
            throws RecordStoreException, IOException
    {

        ByteArrayInputStream bis = new ByteArrayInputStream(rs.getRecord(1));
        DataInputStream dis = new DataInputStream(bis);
        latitude = dis.readDouble();
        longitude = dis.readDouble();
        //timezone = dis.readFloat();
        knisatshabat = dis.readShort();
        city = dis.readUTF();
        textv_bigfont = dis.readByte();
        bis.close();
        dis.close();
        
        

    }

    static private void saveToRecord()
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeDouble(latitude);
            dos.writeDouble(longitude);
            dos.writeShort(knisatshabat);
            dos.writeUTF(city);
            dos.writeByte(textv_bigfont);
            dos.flush();
            byte[] buffer = bos.toByteArray();
            if (rs.getNumRecords() == 0)
            {
                rs.addRecord(buffer, 0, buffer.length);
            }
            else
            {
                rs.setRecord(1, buffer, 0, buffer.length);
            }
            bos.close();
            dos.close();
        }
        catch (RecordStoreException ex)
        {
        }
        catch (IOException ex)
        {
        }

    }

    static public void finalise()
    {
        if (rs != null)
        {
            try
            {
                saveToRecord();
                rs.closeRecordStore();
            }
            catch (RecordStoreException ex)
            {
            }
        }
    }


    static public double GetLatitude()
    {
        return latitude;
    }

    static public void SetLatitude(double new_latitude)
    {
        latitude = new_latitude;
    }

    static public double GetLongitude()
    {
        return longitude;
    }

    static public void SetLongitude(double new_longitude)
    {
        longitude = new_longitude;
    }
    
    static public void SetBigFont(byte new_isBig)
    {
        textv_bigfont=new_isBig;
    }
    static public byte GetBigFont()
    {
        return textv_bigfont;
    }

    static public String GetCity()
    {
        return city;
    }

    static public void SetCity(String cityname)
    {
        city = cityname;
    }

    static public short GetKnisatShabat()
    {
        return knisatshabat;
    }

    static public void SetKnisatShabat(short new_knisatshabat)
    {
        knisatshabat = new_knisatshabat;
    }
    static public boolean isDiaspora()
    {
        return false;
    }
}
