/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maluach;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

/**
 *
 * @author orr
 */
public class ScreenCommand extends Command {
    public ScreenCommand(ScreenView a_screen, Object a_param,String label ,int priority)
    {
        super(label,Command.SCREEN,priority);
        screen=a_screen;
        param=a_param;
    }
    protected Object param;
    protected ScreenView screen;
    public void showScreen()
    {
        screen.OnShow(param);
        maluach.getInstance().PushScreen((Displayable)screen);
    }

}
