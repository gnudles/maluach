/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Graphics;
import maluach.YDate.*;

public class DateSelector extends DateShowers implements DisplayBack, DisplaySelect,ScreenView,CommandCheck,IntSelect.ValueListen
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
    private static final int BAR_SPACE = 3;
    private static final int FONT_SPACE_TOP = 1;
    private static final int FONT_SPACE_BOTTOM = 1;
    private static final int TOUCH_SENSITIVITY = 3; //the bigger the less sensitive
    private static final int LINE_DATE_TYPE = 0;
    private static final int LINE_YEAR = 1;
    private static final int LINE_MONTH = 2;
    private static final int LINE_DAY = 3;
    private static final int LINE_DAY_DIFF = 4;
    private byte active_line;
    private byte lines;
    //private Hdate m_dateCursor;
    private int last_day_count;
    private int ppx, ppy;
    private boolean ppdrag = false;
    private boolean dragged;
    private boolean show_hebrew;
    private IntSelect.IntValue val_select;
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
    byte onlycursor;///stores changes in the cursor for effective repaint


    private DateSelector()
    {
        super();
        show_hebrew=MaluachPreferences.HebrewInteface();
        onlycursor = -1;
        repainted = false;
        gregorian = false;
        m_dateCursor = YDate.getNow();
        this.setFullScreenMode(false);
        active_line = 0;
        lines = 5;
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

    private void drawline(Graphics g, byte line, int yoff, boolean stop)
    {
        g.setColor(COLOR_TITLE_TEXT);
        String lstr;
        int xoff;
        yoff+=FONT_SPACE_TOP;
        switch (line)
        {
            case LINE_DATE_TYPE:
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
            case LINE_YEAR:
                xoff = (getWidth() - field_width[1]) / 2;
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
            case LINE_MONTH:
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
            case LINE_DAY:
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
            case LINE_DAY_DIFF:
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
            g.drawString(fields[line], getWidth() - 4, yoff + 1 + FONT_SPACE_TOP, Graphics.TOP | Graphics.RIGHT);
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
            fonth = g.getFont().getHeight()+ FONT_SPACE_TOP+FONT_SPACE_BOTTOM;
            linespacing = fonth + BAR_SPACE;
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
        else
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
        // end of function, we need to reset this
        repainted = false;

    }

    private void changefield(byte dir)
    {
        switch (active_line)
        {
            case LINE_DATE_TYPE:
                gregorian = !gregorian;
                break;
            case LINE_YEAR:
                if (gregorian)
                {
                    m_dateCursor.setByGregorianYearMonthDay(m_dateCursor.gd.year()+dir,m_dateCursor.gd.month(),m_dateCursor.gd.dayInMonth());
                }
                else
                {
                    m_dateCursor.setByHebrewYearMonthIdDay(m_dateCursor.hd.year()+dir,m_dateCursor.hd.monthID(),m_dateCursor.hd.dayInMonth());
                }
                break;
            case LINE_MONTH:
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
            case LINE_DAY:
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
            case LINE_DAY_DIFF:
                m_dateCursor.seekBy(dir);
                break;

        }
    }
    
    public void ValueChanged(int value)
    {
        if (active_line == LINE_DAY_DIFF)
        {
            m_dateCursor.setByDays(value + last_day_count);
        }
        else if (active_line == LINE_YEAR)
        {
            if (gregorian)
            {
                int month=m_dateCursor.gd.month();
                int dayinmonth=m_dateCursor.gd.dayInMonth();
                m_dateCursor.setByGregorianYearMonthDay(value,month,dayinmonth);
            }
            else
            {
                int month_id=m_dateCursor.hd.monthID();
                int dayinmonth=m_dateCursor.hd.dayInMonth();
                m_dateCursor.setByHebrewYearMonthIdDay(value,month_id,dayinmonth);
            }
        }
    }
    int getSelectedField()
    {
        switch(active_line)
        { 
            case LINE_YEAR:
                if (gregorian)
                    return m_dateCursor.gd.year();
                return m_dateCursor.hd.year();
            case LINE_MONTH:
                if (gregorian)
                    return m_dateCursor.gd.month();
                return m_dateCursor.hd.monthInYear();
            case LINE_DAY:
                if (gregorian)
                    return m_dateCursor.gd.dayInMonth();
                return m_dateCursor.hd.dayInMonth();
            case LINE_DAY_DIFF:
                return m_dateCursor.gd.daysSinceBeginning()- last_day_count;
            default:
                return 0;
        }
        
    }
    private void openIntSelect(int val)
    {
        val_select=new IntSelect.IntValue(val,this);
        maluach.getInstance().PushScreen((Displayable)new IntSelect((active_line==LINE_YEAR)?"שנה":"הפרש ימים",(active_line==LINE_YEAR)?5:9,val_select));
    }
    final public void keyPressed(int realkeyCode)
    {

        if (realkeyCode >= KEY_NUM0 && realkeyCode <= KEY_NUM9)
        {
            if (active_line == LINE_YEAR || active_line == LINE_DAY_DIFF)
            {
                openIntSelect(realkeyCode-KEY_NUM0);
            }
            return;
        }
        if (realkeyCode == KEY_POUND)
        {
            openIntSelect(0);

            return;
        }
        if (realkeyCode == KEY_STAR)
        {
            openIntSelect(getSelectedField());
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
            changefield(dir);
            repainted = true;
            repaint();
            return;
        }
        if (keyCode == FIRE)
        {
            firepress();
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
            changefield(dir);
            repainted = true;
            repaint();
        }
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
                openIntSelect(getSelectedField());
                break;
        }
    }

    protected void pointerPressed(int x, int y)
    {
        dragged = false;
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

        int mody = (y - 1) / linespacing;
        if (active_line == (byte) mody)
        {
            firepress();
        }

    }

    public boolean Back()
    {
        return true;
    }

    public void Select()
    {
        CalendarViewer.getInstance().jumpTo(GetDate());
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
