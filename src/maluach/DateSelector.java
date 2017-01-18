/**
 *
 * @author orr
 */
package maluach;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Graphics;
import maluach.YDate.*;

public class DateSelector extends DateShowers implements DisplayBack, DisplaySelect,ScreenView,CommandCheck
{

    private static final int COLOR_BACKGROUND = 0xffffff;
    private static final int COLOR_BORDER = 0x4080c1;
    private static final int COLOR_CELL_INTEREST = 0x52adec;
    private static final int COLOR_CELL_SELECTED = 0xff8019;
    private static final int COLOR_CELL_DESCRIPT = 0x0998cd;
    //private static final int COLOR_UNSELECTED_TEXT = 0x000000;
    //private static final int COLOR_SELECTED_TEXT = 0xff6600;
    private static final int COLOR_TITLE_TEXT = 0xffffff;
    private static final int COLOR_TITLE_CELL = 0x1f608e;
    private static final int SPACE = 3;
    private static final int TOUCH_SENSITIVITY = 3; //the bigger the less sensitive
    private byte active_line;
    private byte lines;
    //private Hdate m_dateCursor;
    private int last_day_count;
    private int ppx, ppy;
    private boolean ppdrag = false;
    private boolean dragged;
    private boolean show_hebrew;
    final private String[] fields =
    {
        "סוג תאריך", "שנה", "חודש", "יום", "הפרש ימים"
    };
    final private int[] field_width =
    {
        0, 0, 0, 0, 0
    };
    final private boolean[] gregsplit =
    {
        false, false, true, false, false
    };
    final private boolean[] hebsplit =
    {
        false, true, true, true, false
    };
    int fonth;
    int linespacing;
    boolean gregorian;
    boolean repainted;
    byte keyboardkey;
    int keyboardnum;
    byte onlycursor;///stores changes in the cursor for effective repaint
    byte keyboardmove;//for repaint
    boolean clearkeyboard;
    boolean keyboardpressed;
    int keyboardCellw;
    final private char[] keyboardKeys =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '×', '»'
    };

    private DateSelector()
    {
        super();
        show_hebrew=MaluachPreferences.HebrewInteface();
        clearkeyboard = false;
        onlycursor = -1;
        keyboardmove = -1;
        keyboardkey = -1;
        repainted = false;
        gregorian = false;
        m_dateCursor = YDate.getNow();
        this.setFullScreenMode(false);
        active_line = 0;
        lines = 5;
        keyboardCellw = (getWidth() - keyboardKeys.length - 2) / keyboardKeys.length;
    }

    public void SetBeginingCursor(YDate cursor)
    {
        m_dateCursor=YDate.createFrom(cursor);
        last_day_count = m_dateCursor.gd.daysSinceBeginning();
    }

    public void jumpTo(YDate cursor)
    {
        m_dateCursor=YDate.createFrom(cursor);
        repaint();
    }

    public YDate GetDate()
    {
        return m_dateCursor;
    }

    private void keyboardpress(byte key)
    {
        if (key != keyboardkey)
        {
            keyboardmove = keyboardkey;
            keyboardkey = key;
        }
        keyboardpress();
    }

    private void keyboardpress()
    {
        keyboardpressed = true;
        if (keyboardkey == 11)//undo
        {
            if (keyboardnum == 0)
            {
                keyboardkey = -1;
                keyboardmove = -1;
                repainted = true;
                repaint();
                return;
            }
            keyboardnum /= 10;
        }
        else if (keyboardkey == 12)//ok
        {
            if (active_line == 4)
            {
                m_dateCursor.setByDays(keyboardnum + last_day_count);
            }
            else if (gregorian)
            {
                int month=m_dateCursor.gd.month();
                int dayinmonth=m_dateCursor.gd.dayInMonth();
                m_dateCursor.setByGregorianYearMonthDay(keyboardnum,month,dayinmonth);
            }
            else
            {
                int month_id=m_dateCursor.hd.monthID();
                int dayinmonth=m_dateCursor.hd.dayInMonth();
                m_dateCursor.setByHebrewYearMonthIdDay(keyboardnum,month_id,dayinmonth);
            }
            keyboardkey = -1;
            keyboardmove = -1;
        }
        else if (keyboardkey == 10)//minus
        {
            keyboardnum *= -1;
        }
        else
        {
            if (Math.abs(keyboardnum) < 10000000)
            {
                if (keyboardnum < 0)
                {
                    keyboardnum = keyboardnum * 10 - keyboardkey;
                }
                else
                {
                    keyboardnum = keyboardnum * 10 + keyboardkey;
                }
            }
        }
        repainted = true;
        repaint();
    }

    private void drawline(Graphics g, byte line, int yoff, boolean stop)
    {
        g.setColor(COLOR_TITLE_TEXT);
        String lstr;
        int xoff;
        switch (line)
        {
            case 0:
                if (gregorian)
                {
                    lstr = "לועזי";
                }
                else
                {
                    lstr = "עברי";
                }
                g.drawString(lstr, (getWidth() - field_width[0]) / 2, yoff, Graphics.TOP | Graphics.HCENTER);
                if (stop)
                {
                    break;
                }
                else
                {
                    yoff += linespacing;
                }
            case 1:
                xoff = (getWidth() - field_width[1]) / 2;
                if (active_line == 1 && keyboardkey != -1)
                {
                    lstr = "" + keyboardnum;
                }
                else
                {
                    if (gregorian)
                    {
                        lstr = "" + m_dateCursor.gd.year();
                    }
                    else
                    {
                        lstr = "" + m_dateCursor.hd.year();
                    }
                }
                if (!gregorian)
                {
                    g.drawString(Format.HebIntString(m_dateCursor.hd.year(), true), (getWidth() - field_width[1]) * 3 / 4, yoff, Graphics.TOP | Graphics.HCENTER);
                    xoff /= 2;
                }
                g.drawString(lstr, xoff, yoff, Graphics.TOP | Graphics.HCENTER);
                if (stop)
                {
                    break;
                }
                else
                {
                    yoff += linespacing;
                }
            case 2:
                if (gregorian)
                {
                    lstr = "" + m_dateCursor.gd.month();
                }
                else
                {
                    lstr = "" + m_dateCursor.hd.monthInYear();
                }
                g.drawString(lstr, (getWidth() - field_width[2]) / 4, yoff, Graphics.TOP | Graphics.HCENTER);
                if (gregorian)
                {
                    lstr = "" + m_dateCursor.gd.monthName(show_hebrew);
                }
                else
                {
                    lstr = "" + m_dateCursor.hd.monthName(show_hebrew);
                }
                g.drawString(lstr, (getWidth() - field_width[2]) * 3 / 4, yoff, Graphics.TOP | Graphics.HCENTER);
                if (stop)
                {
                    break;
                }
                else
                {
                    yoff += linespacing;
                }
            case 3:
                xoff = (getWidth() - field_width[3]) / 2;
                if (gregorian)
                {
                    lstr = Integer.toString(m_dateCursor.gd.dayInMonth());
                }
                else
                {
                    g.drawString(Format.HebIntString(m_dateCursor.hd.dayInMonth(), true), (getWidth() - field_width[3]) * 3 / 4, yoff, Graphics.TOP | Graphics.HCENTER);
                    xoff /= 2;
                    lstr = Integer.toString(m_dateCursor.hd.dayInMonth());
                }
                g.drawString(lstr, xoff, yoff, Graphics.TOP | Graphics.HCENTER);
                if (stop)
                {
                    break;
                }
                else
                {
                    yoff += linespacing;
                }
            case 4:
                if (active_line == 4 && keyboardkey != -1)
                {
                    lstr = Integer.toString(keyboardnum);
                }
                else
                {
                    lstr = Integer.toString(m_dateCursor.gd.daysSinceBeginning()- last_day_count);
                }
                g.drawString(lstr, (getWidth() - field_width[4]) / 2, yoff, Graphics.TOP | Graphics.HCENTER);
        }
    }

    private int paintbox(Graphics g, byte line, int yoff)
    {

        if (!repainted)
        {
            g.setColor(COLOR_BORDER);
            g.drawRect(1, yoff, getWidth() - 3, fonth + 1);
            g.setColor(COLOR_TITLE_CELL);
            if (field_width[line] == 0)
            {
                field_width[line] = g.getFont().stringWidth(fields[line]) + 4;
            }
            int boxw = field_width[line];
            g.fillRect(getWidth() - boxw - 2, yoff + 1, boxw, fonth);
            g.setColor(COLOR_TITLE_TEXT);
            g.drawString(fields[line], getWidth() - 4, yoff + 1, Graphics.TOP | Graphics.RIGHT);
        }
        //
        boolean split = (gregorian) ? gregsplit[line] : hebsplit[line];
        int dewidth = getWidth() - field_width[line] - 4;
        if (line == active_line)
        {
            g.setColor(COLOR_CELL_SELECTED);
        }
        else
        {
            g.setColor(COLOR_CELL_INTEREST);
        }
        if (split)
        {
            byte modu = (byte) (dewidth % 2);
            dewidth /= 2;
            g.fillRect(2, yoff + 1, dewidth, fonth);
            g.setColor(COLOR_CELL_DESCRIPT);
            g.fillRect(2 + dewidth, yoff + 1, dewidth + modu, fonth);
        }
        else
        {
            g.fillRect(2, yoff + 1, dewidth, fonth);
        }
        //
        return 0;
    }

    final public void paint(Graphics g)
    {
        if (!repainted)
        {
            fonth = g.getFont().getBaselinePosition() + SPACE;
            linespacing = fonth + SPACE;
            keyboardkey = -1;
            g.setColor(COLOR_BACKGROUND);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        int yoff;
        if (onlycursor != -1)
        {
            paintbox(g, onlycursor, 1 + linespacing * onlycursor);
            drawline(g, onlycursor, 2 + linespacing * onlycursor, true);
            paintbox(g, active_line, 1 + linespacing * active_line);
            drawline(g, active_line, 2 + linespacing * active_line, true);
            onlycursor = -1;
        }
        else if (keyboardkey == -1)
        {
            yoff = 1;
            byte i;
            for (i = 0; i < fields.length; i++)
            {
                paintbox(g, i, yoff);
                yoff += linespacing;
            }
            drawline(g, (byte) 0, 2, false);
        }
        yoff = 2 + linespacing * lines;

        //keyboard stuff
        
        //TODO... replace keyboard with IntSelect
        if (keyboardkey != -1)
        {

            byte ikey;

            int xoff = 1;
            if (keyboardpressed)
            {
                paintbox(g, active_line, 1 + linespacing * active_line);
                drawline(g, active_line, 2 + linespacing * active_line, true);
                keyboardpressed = false;
            }
            if (keyboardmove != -1)
            {
                int temp;
                g.setColor(COLOR_CELL_INTEREST);
                temp = xoff + (keyboardCellw + 1) * keyboardmove;
                g.fillRect(temp, yoff, keyboardCellw, fonth);
                g.setColor(COLOR_TITLE_TEXT);
                g.drawChar(keyboardKeys[keyboardmove], temp + keyboardCellw / 2, yoff, Graphics.TOP | Graphics.HCENTER);
                g.setColor(COLOR_CELL_SELECTED);
                temp = xoff + (keyboardCellw + 1) * keyboardkey;
                g.fillRect(temp, yoff, keyboardCellw, fonth);
                g.setColor(COLOR_TITLE_TEXT);
                g.drawChar(keyboardKeys[keyboardkey], temp + keyboardCellw / 2, yoff, Graphics.TOP | Graphics.HCENTER);
                keyboardmove = -1;
            }
            else
            {
                g.setColor(COLOR_CELL_INTEREST);
                for (ikey = 0; ikey < keyboardKeys.length; ikey++)
                {
                    if (ikey == keyboardkey)
                    {
                        g.setColor(COLOR_CELL_SELECTED);
                        g.fillRect(xoff, yoff, keyboardCellw, fonth);
                    }
                    else
                    {
                        g.setColor(COLOR_CELL_INTEREST);
                        g.fillRect(xoff, yoff, keyboardCellw, fonth);
                    }
                    g.setColor(COLOR_TITLE_TEXT);
                    g.drawChar(keyboardKeys[ikey], xoff + keyboardCellw / 2, yoff, Graphics.TOP | Graphics.HCENTER);
                    xoff += keyboardCellw + 1;
                }

            }
            clearkeyboard = true;

        }
        else if (clearkeyboard)
        {
            g.setColor(COLOR_BACKGROUND);
            g.fillRect(0, yoff, getWidth(), fonth);
            clearkeyboard = false;
        }
        // end of function, we need to reset this
        repainted = false;

    }

    private void changefield(byte dir)
    {
        switch (active_line)
        {
            case 0:
                gregorian = !gregorian;
                break;
            case 1:
                if (gregorian)
                {
                    m_dateCursor.setByGregorianYearMonthDay(m_dateCursor.gd.year()+dir,m_dateCursor.gd.month(),m_dateCursor.gd.dayInMonth());
                }
                else
                {
                    m_dateCursor.setByHebrewYearMonthIdDay(m_dateCursor.hd.year()+dir,m_dateCursor.hd.monthID(),m_dateCursor.hd.dayInMonth());
                }
                break;
            case 2:
                if (gregorian)
                {
                    int month=m_dateCursor.gd.month();
                    month+=dir;
                    if (month>12)
                        month=1;
                    else if(month==0)
                        month=12;
                    m_dateCursor.setByGregorianYearMonthDay(m_dateCursor.gd.year(),month,m_dateCursor.gd.dayInMonth());
                }
                else
                {
                    int month=m_dateCursor.hd.monthInYear();
                    int num_months=JewishDate.calculateYearMonths(m_dateCursor.hd.year());
                    month+=dir;
                    if (month>num_months)
                        month=1;
                    else if(month==0)
                        month=num_months;
                    int monthID=JewishDate.monthID(num_months,month);
                    m_dateCursor.setByHebrewYearMonthIdDay(m_dateCursor.hd.year(),monthID,m_dateCursor.hd.dayInMonth());
                }
                break;
            case 3:
                if (gregorian)
                {
                    int dayInMonth=m_dateCursor.gd.dayInMonth();
                    dayInMonth+=dir;
                    int month_length=m_dateCursor.gd.monthLength();
                    if (dayInMonth>month_length)
                        dayInMonth=1;
                    else if(dayInMonth==0)
                        dayInMonth=month_length;
                    m_dateCursor.setByGregorianYearMonthDay(m_dateCursor.gd.year(),
                            m_dateCursor.gd.month(),dayInMonth);
                }
                else
                {
                    int dayInMonth=m_dateCursor.hd.dayInMonth();
                    dayInMonth+=dir;
                    int month_length=m_dateCursor.hd.monthLength();
                    if (dayInMonth>month_length)
                        dayInMonth=1;
                    else if(dayInMonth==0)
                        dayInMonth=month_length;
                    int monthId=m_dateCursor.hd.monthID();
                    m_dateCursor.setByHebrewYearMonthIdDay(m_dateCursor.hd.year(),monthId,dayInMonth);
                }

                break;
            case 4:
                m_dateCursor.seekBy(dir);
                break;

        }
    }

    final public void keyPressed(int realkeyCode)
    {

        if (realkeyCode >= KEY_NUM0 && realkeyCode <= KEY_NUM9)
        {
            if (keyboardkey == -1)
            {
                keyboardnum = 0;
            }
            if (active_line == 1 || active_line == 4)
            {
                keyboardpress((byte) (realkeyCode - KEY_NUM0));
            }
            return;
        }
        if (realkeyCode == KEY_POUND)
        {
            if (keyboardkey != -1)
            {
                keyboardpress((byte) 12);
            }

            return;
        }
        if (realkeyCode == KEY_STAR)
        {
            if (keyboardkey != -1)
            {
                keyboardpress((byte) 10);
            }
            return;
        }

        int keyCode = getGameAction(realkeyCode);
        if (keyCode == UP || keyCode == DOWN)
        {
            if (keyCode == UP)
            {
                onlycursor = active_line;
                active_line--;

            }
            else
            {
                onlycursor = active_line;
                active_line++;
            }
            keyboardkey = -1;
            active_line = (byte) ((active_line + lines) % lines);
            repainted = true;
            repaint();
            return;
        }
        if (keyCode == LEFT || keyCode == RIGHT)
        {
            byte dir;
            if (keyCode == LEFT)
            {
                dir = -1;
            }
            else
            {
                dir = 1;
            }
            if (keyboardkey != -1)
            {
                keyboardmove = keyboardkey;
                keyboardkey += dir;
                if (keyboardkey == keyboardKeys.length)
                {
                    keyboardkey = 0;
                }
                else if (keyboardkey == -1)
                {
                    keyboardkey = (byte) (keyboardKeys.length - 1);
                }

            }
            else
            {
                changefield(dir);
            }
            repainted = true;
            repaint();
            return;
        }
        if (keyCode == FIRE)
        {
            if (keyboardkey == -1)
            {
                firepress();
            }
            else
            {
                keyboardpress();
            }
            return;

        }


    }//end keyPressed

    public void keyRepeated(int keyCode)
    {

        if (keyCode == LEFT || keyCode == RIGHT)
        {
            byte dir;
            if (keyCode == LEFT)
            {
                dir = -1;
            }
            else
            {
                dir = 1;
            }
            if (keyboardkey == -1)
            {
                changefield(dir);
            }
            repainted = true;
            repaint();
            return;
        }
    }

    private boolean keyboardOn()
    {
        return (keyboardkey != -1);
    }

    public boolean keyboardselectpressed()
    {
        if (keyboardOn())
        {
            keyboardpress();
            return true;
        }
        return false;
    }

    public boolean keyboardbackpressed()
    {
        if (keyboardOn())
        {

            keyboardpress((byte) 11);

            return true;
        }
        return false;
    }
    private void showInfo()
    {
        String lstr;
        lstr = "יום בשבוע: " + m_dateCursor.hd.dayInWeekName(show_hebrew);
        lstr += "\nימים בשנה: ";
        if (gregorian)
        {
            lstr += m_dateCursor.gd.yearLength();
        }
        else
        {
            lstr += m_dateCursor.hd.yearLength();
        }
        lstr += "\nימים בחודש: ";
        if (gregorian)
        {
            lstr += m_dateCursor.gd.monthLength();
        }
        else
        {
            lstr += m_dateCursor.hd.monthLength();
        }
        maluach.showAlert("מידע", lstr, AlertType.INFO);
        
    }
    private void firepress()
    {
        String lstr = null;
        switch (active_line)
        {
            case 0:
                gregorian = !gregorian;
                repainted = true;
                repaint();
                break;
            case 3:
            case 2:
                showInfo();
                repaint();
                break;
            case 1:
            case 4:
                keyboardkey = 0;
                keyboardnum = 0;
                repainted = true;
                repaint();
                break;
        }
    }

    protected void pointerPressed(int x, int y)
    {
        dragged = false;
        if (keyboardkey == -1)
        {
            int mody = (y - 1) / linespacing;
            if (mody >= 0 && mody < lines)
            {
                if (active_line != (byte) mody)
                {

                    onlycursor = active_line;
                    active_line = (byte) mody;
                    repainted = true;
                    repaint();
                }
                ppdrag = true;
                ppx = x;
                ppy = y;
            }
        }


    }

    protected void pointerDragged(int x, int y)
    {

        if (ppdrag)
        {

            if (Math.abs(y - ppy) > linespacing)
            {
                ppdrag = false;
                return;
            }
            if (ppx == x)
            {
                return;
            }
            byte dir = 0;
            if (ppx > x + TOUCH_SENSITIVITY)
            {
                dir = -1;
            }
            if (ppx < x - TOUCH_SENSITIVITY)
            {
                dir = 1;

            }
            if (dir != 0)
            {
                changefield(dir);
                dragged = true;
                ppx = x;
                repainted = true;
                repaint();
            }


        }
    }

    protected void pointerReleased(int x, int y)
    {
        ppdrag = false;
        if (dragged)
        {
            return;
        }

        if (keyboardkey == -1)
        {
            int mody = (y - 1) / linespacing;
            if (active_line == (byte) mody)
            {
                firepress();
            }

        }
        else
        {
            if (y <= 2 + linespacing * (lines + 1) && y > linespacing * (lines))
            {
                x -= 1;
                x /= (keyboardCellw + 1);
                if (x >= 0 && x < keyboardKeys.length)
                {
                    keyboardpress((byte) x);
                }
            }
        }
    }

    public boolean Back()
    {
        return !keyboardbackpressed();

    }

    public void Select()
    {
        if (!keyboardselectpressed())
        {
            CalendarViewer.getInstance().jumpTo(GetDate());
        }
    }
    private Command c_setCursor;
    private Command c_showInfo;
    private void InitCommands()
    {
        c_setCursor = new Command("קבע יחוס", Command.ITEM, 4);
        addCommand(c_setCursor);
        c_showInfo = new Command("מידע מאפיין", Command.ITEM, 6);
        addCommand(c_showInfo);
        addCommand(CommandPool.getC_back());

        addCommand(CommandPool.getC_select());
    }
    static private DateSelector _instance;

    static public DateSelector getInstance()
    {

        if (_instance == null)
        {
            _instance = new DateSelector();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());

        }
        
        return _instance;
    }

    public void OnShow(Object param)
    {
        SetBeginingCursor(CalendarViewer.getInstance().getCursor());
    }

    public boolean Execute(Command c)
    {
        if (c==c_setCursor)
        {
            last_day_count = m_dateCursor.gd.daysSinceBeginning();
            repaint();
            return true;
        }
        else if (c==c_showInfo)
        {
            showInfo();
            repaint();
            return true;
        }
        return super.Execute(c);
    }
}
