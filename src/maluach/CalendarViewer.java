/* This is free and unencumbered software released into the public domain.
 */
package maluach;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import textviewer.ScriptedTextViewer;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class CalendarViewer extends DateShowers implements ScreenView

{

    private class Cursor
    {
        public short x;
        public short y;
        Cursor(short x,short y)
        {
            this.x=x;
            this.y=y;
        }
        Cursor()
        {
            this.x=(short)0;
            this.y=(short)0;
        }
        void set(Cursor c)
        {
            this.x=c.x;
            this.y=c.y;
        }
    }
    private static final int COLOR_BACKGROUND = 0xffffff;
    private static final int COLOR_CELL = 0x49a3ff;
    private static final int COLOR_CELL_MONTH = 0x2993d4;
    private static final int COLOR_CELL_HOLIDAY = 0x007dac;
    private static final int COLOR_CELL_GARY = 0xdbf1f8;
    private static final int COLOR_CURSOR = 0xff8019;
    private static final int COLOR_BORDER = 0x4080c1;
    private static final int COLOR_BORDER_SAT = 0x7fb8ff;
    private static final int COLOR_TEXT_HEADER = 0xddeeff;
    private static final int COLOR_TEXT_DAY = 0xffffff;
    private static final int COLOR_TEXT_OTHER_DAY = 0xaaaaaa;
    private static final int COLOR_TEXT_TODAY = 0xffde00;
    private static final int COLOR_TEXT_DATE_HEB = 0x1e6875;
    private static final int COLOR_TEXT_DATE_GRE = 0x1e752c;
    private static final short CELL_PADDING = 1; // size text cell padding
    private static final short DOUBLE_CELL_PADDING = 2 * CELL_PADDING; // double cell padding helper for computation
    private final short CELL_MARGINS = 2;
    private final short m_rows = 6, m_columns = 7;
    //private short[] m_cellType = new short[m_rows * m_columns];
    private short m_tableWidth, m_tableHeight;
    private short m_tableX, m_tableY;
    private short m_cellWidth, m_cellHeight;
    //int[] Colors = new int[8];
    private Font m_tableFont;
    private Cursor cursor;
    private Cursor newCursor;
    

    private String[] Header =
    {
        "א", "ב", "ג", "ד", "ה", "ו", "ש"
    };
    private String[] Days =
    {
        "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "יא", "יב", "יג", "יד", "טו", "טז", "יז", "יח", "יט", "כ", "כא", "כב", "כג", "כד", "כה", "כו", "כז", "כח", "כט", "ל"
    };
    private int hd_offset, hd_month_size;
    private boolean clearcursor = false;

    private static final int getColorForEvent(int type, boolean rosh_hodesh)
    {
        if (type==0)
        {
             if (rosh_hodesh)
                return COLOR_CELL_MONTH;
             return COLOR_CELL;
        }
        int c=COLOR_CELL_HOLIDAY;
        if (rosh_hodesh)
            c^=COLOR_CELL-COLOR_CELL_MONTH;
        if ((type & YDateAnnual.EV_TZOM) == YDateAnnual.EV_TZOM)
            c^=0x002000;
        if ((type & YDateAnnual.EV_NATIONAL) ==YDateAnnual.EV_NATIONAL)
            c^=0x400040;
        if ((type & YDateAnnual.EV_MEMORIAL) ==YDateAnnual.EV_MEMORIAL)
            c^=0x041230;
        if ((type & YDateAnnual.EV_GOOD_DAYS) ==YDateAnnual.EV_GOOD_DAYS)
            c^=0x880044;
        if ((type & YDateAnnual.EV_MIRACLE) ==YDateAnnual.EV_MIRACLE)
            c^=0x042008;
        if ((type & YDateAnnual.EV_REGALIM) ==YDateAnnual.EV_REGALIM)
            c^=0x016002;
        if ((type & YDateAnnual.EV_YOM_TOV) ==YDateAnnual.EV_YOM_TOV)
            c^=0x804200;
        
        return c;
    }
    private void setCursorXY(Cursor c)
    {
        c.x = (short) (7 - m_dateCursor.hd.dayInWeek());
        c.y = (short) ((m_dateCursor.hd.dayInMonth()+ c.x - 1) / 7);
        if (m_dateCursor.hd.monthFirstDay()%7==YDate.SUNDAY)
            c.y++;
    }
    private CalendarViewer()
    {
        super();
        cursor=new Cursor();
        newCursor=new Cursor();
        this.setFullScreenMode(false);

        m_dateCursor = YDate.getNow();
        m_dateCursor.setMaintainEvents(true, false);
        setCursorXY(cursor);
        m_cellHeight = (short) (getHeight() / 11);
        m_cellWidth = (short) ((getWidth() - 15) / 7);
        m_tableWidth = (short) ((m_cellWidth + 2) * 7 - 1);
        m_tableFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        if ((m_tableFont.stringWidth("XX") > m_cellWidth - 1 - DOUBLE_CELL_PADDING) || (m_tableFont.getHeight() > m_cellHeight - 1 - DOUBLE_CELL_PADDING))
        {
            m_tableFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
            if ((m_tableFont.stringWidth("XX") > m_cellWidth - 1 - DOUBLE_CELL_PADDING) || (m_tableFont.getHeight() > m_cellHeight - 1 - DOUBLE_CELL_PADDING))
            {
                m_tableFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            }
        }
        m_tableX = (short) ((getWidth() - m_tableWidth) / 2);
        m_tableHeight = (short) ((m_cellHeight + 2) * 7 - 1);
        m_tableY = 2;


    }

    final public void jumpTo(YDate date)
    {
        m_dateCursor=YDate.createFrom(date);
        m_dateCursor.setMaintainEvents(true, false);
        setCursorXY(cursor);
        repaint();
    }

    private void drawcursor(Graphics g)
    {

        int x = m_tableX + (m_cellWidth + 2) * cursor.x - 1;
        int w = m_cellWidth + 2;
        int y = m_tableY + (cursor.y + 1) * (m_cellHeight + 2) - 1;
        int h = m_cellHeight + 2;
        g.drawLine(x, y, x + w, y);
        g.drawLine(x, y + h, x + w, y + h);
        g.drawLine(x, y + 1, x, y + h - 1);
        g.drawLine(x + w, y + 1, x + w, y + h - 1);
    }

    private void drawstring(Graphics g)
    {
        boolean heb=MaluachPreferences.HebrewInteface();
        int hy = m_tableHeight + m_tableY, gy;
        g.setColor(COLOR_TEXT_DATE_GRE);
        String il_event = m_dateCursor.events().getYearEventForDay(m_dateCursor.hd);
        if (il_event.length()>0)
            g.drawString(il_event, getWidth() / 2, hy + 2 * m_tableFont.getHeight(), Graphics.TOP | Graphics.HCENTER);
        String diasp_event = YDateAnnual.getEventForDay(m_dateCursor.hd,true);
        if (!diasp_event.equals(il_event) && diasp_event.length()>0)
            g.drawString("בחו\"ל "+diasp_event, getWidth() / 2, hy + 3 * m_tableFont.getHeight(), Graphics.TOP | Graphics.HCENTER);
        gy = hy + m_tableFont.getHeight();

        g.setColor(COLOR_TEXT_DATE_HEB);
        g.drawString(m_dateCursor.hd.dayString(heb), getWidth() / 2, hy, Graphics.TOP | Graphics.HCENTER);
        g.setColor(COLOR_TEXT_DATE_GRE);
        g.drawString(m_dateCursor.gd.dayString(heb), getWidth() / 2, gy, Graphics.TOP | Graphics.HCENTER);


    }

    final public void paint(Graphics g)
    {
        g.setFont(m_tableFont);
        if (clearcursor)
        {
            g.setColor(COLOR_BACKGROUND);
            drawcursor(g);
            g.fillRect(0, m_tableHeight + m_tableY, getWidth(), m_tableFont.getHeight() * 4);
            cursor.set(newCursor);
            g.setColor(COLOR_CURSOR);
            drawcursor(g);

            drawstring(g);
            return;
        }


        byte[] events = m_dateCursor.events().getYearEvents();
        int events_offset=m_dateCursor.hd.dayInYear()-m_dateCursor.hd.dayInMonth()+1;
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(55, 130, 180);

        int yoffset = (m_cellHeight - m_tableFont.getBaselinePosition()-1) / 2;
        int i, j;
        int x = m_tableX;
        //draw the header
        for (i = 0; i < m_columns; i++)
        {
            if (i == 0)
            {
                g.setColor(180, 200, 230);
            }
            else
            {
                g.setColor(149, 189, 194);
            }
            g.drawRect(x, m_tableY, m_cellWidth, m_cellHeight);
            g.setColor(124, 174, 181);
            g.fillRect(x + 1, m_tableY + 1, m_cellWidth - 1, m_cellHeight - 1);
            g.setColor(COLOR_TEXT_HEADER);

            g.drawString(Header[6 - i], x + m_cellWidth / 2, m_tableY + yoffset, Graphics.TOP | Graphics.HCENTER);
            x += m_cellWidth + CELL_MARGINS;
        }


        g.setColor(COLOR_CURSOR);
        drawcursor(g);
        boolean same_month = false;
        if (m_dateCursor.hd.monthInYear()== s_dateToday.hd.monthInYear() && m_dateCursor.hd.year()== s_dateToday.hd.year())
        {
            same_month = true;
        }
        g.setColor(COLOR_TEXT_DAY);
        int y = m_tableY + m_cellHeight + CELL_MARGINS;
        hd_offset = m_dateCursor.hd.monthFirstDay()%7;
        hd_month_size = m_dateCursor.hd.monthLength();
        int hd_prev_month_size = 0;
        int cellid = 6 - hd_offset;
        if (hd_offset == 0)
        {
            cellid-=7;
        }
        hd_prev_month_size = m_dateCursor.hd.previousMonthLength();

        for (j = 0; j < m_rows; j++)
        {
            x = m_tableX;
            for (i = 0; i < m_columns; i++)
            {

                if (i == 0)
                {

                    g.setColor(COLOR_BORDER_SAT);
                }
                else
                {
                    g.setColor(COLOR_BORDER);
                }
                
                g.drawRect(x, y, m_cellWidth, m_cellHeight);

                g.setColor(COLOR_CELL_GARY);
                g.fillRect(x + 1, y + 1, m_cellWidth - 1, m_cellHeight - 1);


                if (cellid >= 0 && cellid < hd_month_size)
                {
                    
                    int type=YDateAnnual.getEventType(events[cellid+events_offset]);
                    g.setColor(getColorForEvent(type,(cellid==0 || cellid ==29)));
                    
                    g.fillRect(x + 1, y + 1, m_cellWidth - 1, m_cellHeight - 1);
                    if (same_month && (cellid + 1 == s_dateToday.hd.dayInMonth()))
                    {
                        g.setColor(COLOR_TEXT_TODAY);
                        g.drawString(Days[cellid], x + m_cellWidth / 2, y + yoffset, Graphics.TOP | Graphics.HCENTER);


                    }
                    else
                    {
                        g.setColor(COLOR_TEXT_DAY);
                        g.drawString(Days[cellid], x + m_cellWidth / 2, y + yoffset, Graphics.TOP | Graphics.HCENTER);

                    }

                }
                else
                {
                    g.setColor(COLOR_TEXT_OTHER_DAY);
                    if (cellid >= 0)
                    {
                        g.drawString(Days[cellid - hd_month_size], x + m_cellWidth / 2, y + yoffset, Graphics.TOP | Graphics.HCENTER);
                    }
                    else
                    {
                        g.drawString(Days[cellid + hd_prev_month_size], x + m_cellWidth / 2, y + yoffset, Graphics.TOP | Graphics.HCENTER);
                    }
                    g.setColor(COLOR_TEXT_DAY);
                }
                x += m_cellWidth + CELL_MARGINS;
                cellid--;
            }
            cellid += 2 * m_columns;
            y += m_cellHeight + CELL_MARGINS;
        }
        drawstring(g);
    }

    private void movecursor(boolean monthchanged)
    {
        
        setCursorXY(newCursor);
        if (monthchanged)
        {
            cursor.set(newCursor);
            this.repaint();
        }
        else
        {
            clearcursor = true;
            this.repaint();
            this.serviceRepaints();
            clearcursor = false;
        }
    }

    final public void keyPressed(int keyCode)
    {
        int month = m_dateCursor.hd.monthInYear();
        keyCode = getGameAction(keyCode);
        if (keyCode == UP)
        {
            if (m_dateCursor.seekBy(-7))
            {
                movecursor(m_dateCursor.hd.monthInYear() != month);
            }
            return;
        }
        if (keyCode == DOWN)
        {
            if (m_dateCursor.seekBy(7))
            {
                movecursor(m_dateCursor.hd.monthInYear() != month);
            }
            return;
        }
        if (keyCode == LEFT)
        {
            if (m_dateCursor.seekBy(1))
            {
                movecursor(m_dateCursor.hd.monthInYear() != month);
            }
            return;
        }
        if (keyCode == RIGHT)
        {
            if (m_dateCursor.seekBy(-1))
            {
                movecursor(m_dateCursor.hd.monthInYear() != month);
            }
            return;
        }
        if (keyCode == FIRE)
        {
            showInformation();
        }

    }//end keyPressed

    protected void pointerPressed(int x, int y)
    {

        if (m_tableX < x && (m_tableY + m_cellHeight + CELL_MARGINS) < y && x < m_tableX + m_tableWidth && y < m_tableY + m_tableHeight)
        {
            int cellx = (x - m_tableX) / (m_cellWidth + CELL_MARGINS);
            int celly = (y - m_tableY) / (m_cellHeight + CELL_MARGINS) - 1;
            int month = m_dateCursor.hd.monthInYear();
            m_dateCursor.seekBy((celly - cursor.y - 1) * m_columns + m_rows - cellx + cursor.x + 1);
            movecursor(m_dateCursor.hd.monthInYear() != month);
        }


    }
//commands
    private ScreenCommand c_openCompass;
    private ScreenCommand c_openTexts;
    
    private ScreenCommand c_openDaySelector;
    private ScreenCommand c_openPreferences;
    
    //bereshit //private ScreenCommand c_openHumashBereshit;

    private void InitCommands()
    {
        c_openCompass=new ScreenCommand(Compass.getInstance(),null, "כיוון תפילה", 4);
/*        c_wakeUpPrayer=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/shahar" ,"ברכות השחר", 10);
        c_birkatMazon=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/mazon" ,"ברכת המזון", 11);
        c_thilim=new ScreenCommand(Psalms.getInstance(), null ,"תהילים",12);*/
        c_openDaySelector=new ScreenCommand(DateSelector.getInstance(),null,"בחר תאריך",1);
        c_openPreferences =new ScreenCommand(PreferencesForm.getInstance(),null,"הגדרות",9);

        c_openTexts=new ScreenCommand(TextsList.getInstance(),null,"כתבים",10);
        addCommand(CommandPool.getC_exit());
        addCommand(c_openDaySelector);

        /*addCommand(c_wakeUpPrayer);
        addCommand(c_birkatMazon);
        addCommand(c_thilim);*/
        addCommand(c_openCompass);
        addCommand(c_openPreferences);
        addCommand(c_openTexts);

        //bereshit //addCommand(c_openHumashBereshit);
    }

    public void OnShow(Object param)
    {
        
    }
    static private CalendarViewer _instance;
    static public CalendarViewer getInstance()
    {
        if (_instance == null)
        {
            _instance = new CalendarViewer();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }
}
