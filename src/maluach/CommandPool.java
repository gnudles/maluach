/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import javax.microedition.lcdui.Command;

/**
 *
 * @author orr
 */
public class CommandPool
{

    static private Command c_exit;
    static private Command c_back;
    static private Command c_select;
    static private Command c_save;


    static public void Initiate()
    {
        c_exit = new Command("יציאה", Command.EXIT, 0);
        c_back = new Command("חזור", Command.BACK, 0);
        c_select = new Command("בחר", Command.OK, 1);
        c_save = new Command("שמור", Command.OK, 2);
    }

    static public Command getC_exit()
    {
        return c_exit;
    }

    static public Command getC_back()
    {
        return c_back;
    }

    static public Command getC_save()
    {
        return c_save;
    }

    static public Command getC_select()
    {
        return c_select;
    }

}
