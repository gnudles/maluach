/**
 *
 * @author orr
 */
package maluach;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import textviewer.ScriptedTextViewer;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class CalendarViewer extends DateShowers implements ScreenView

{

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
    /**the column of the cursor in the table*/
    private short cursor_x;
    /**the row of the cursor in the table*/
    private short cursor_y;
    private short m_newCursorX = 0;
    private short m_newCursorY = 0;
    

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

    private CalendarViewer()
    {
        super();
        this.setFullScreenMode(false);

        m_dateCursor = new Hdate();
        cursor_x = (short) (7 - m_dateCursor.get_day_in_week());
        cursor_y = (short) ((m_dateCursor.get_hd_day_in_month() + cursor_x - 1) / 7);
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

    final public void jumpTo(Hdate date)
    {
        m_dateCursor.Set(date);
        cursor_x = (short) (7 - m_dateCursor.get_day_in_week());
        cursor_y = (short) ((m_dateCursor.get_hd_day_in_month() + cursor_x - 1) / 7);
        repaint();
    }

    private void drawcursor(Graphics g)
    {

        int x = m_tableX + (m_cellWidth + 2) * cursor_x - 1;
        int w = (m_cellWidth + 2);
        int y = m_tableY + (cursor_y + 1) * (m_cellHeight + 2) - 1;
        int h = m_cellHeight + 2;
        g.drawLine(x, y, x + w, y);
        g.drawLine(x, y + h, x + w, y + h);
        g.drawLine(x, y + 1, x, y + h - 1);
        g.drawLine(x + w, y + 1, x + w, y + h - 1);
    }

    private void drawstring(Graphics g)
    {
        int hy = m_tableHeight + m_tableY, gy;
        g.setColor(COLOR_TEXT_DATE_GRE);
        g.drawString(m_dateCursor.getHolyday(), getWidth() / 2, hy + 2 * m_tableFont.getHeight(), Graphics.TOP | Graphics.HCENTER);
        gy = hy + m_tableFont.getHeight();

        g.setColor(COLOR_TEXT_DATE_HEB);
        g.drawString(m_dateCursor.getDayString(), getWidth() / 2, hy, Graphics.TOP | Graphics.HCENTER);
        g.setColor(COLOR_TEXT_DATE_GRE);
        g.drawString(m_dateCursor.getgDayString(), getWidth() / 2, gy, Graphics.TOP | Graphics.HCENTER);


    }

    final public void paint(Graphics g)
    {
        g.setFont(m_tableFont);
        if (clearcursor)
        {
            g.setColor(COLOR_BACKGROUND);
            drawcursor(g);
            g.fillRect(0, m_tableHeight + m_tableY, getWidth(), m_tableFont.getHeight() * 3);
            cursor_x = m_newCursorX;
            cursor_y = m_newCursorY;
            g.setColor(COLOR_CURSOR);
            drawcursor(g);

            drawstring(g);
            return;
        }

        byte[] holydays = m_dateCursor.get_holydays();
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(55, 130, 180);

        int yoffset = (m_cellHeight - m_tableFont.getBaselinePosition()) / 2;
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
            //g.drawString("cc", 0, 6, 0);
            x += m_cellWidth + CELL_MARGINS;
        }




        g.setColor(COLOR_CURSOR);
        drawcursor(g);
        boolean same_month = false;
        if (m_dateCursor.get_hd_month() == s_dateToday.get_hd_month() && m_dateCursor.get_hd_year() == s_dateToday.get_hd_year())
        {
            same_month = true;
        }
        g.setColor(COLOR_TEXT_DAY);
        int y = m_tableY + m_cellHeight + CELL_MARGINS;
        hd_offset = m_dateCursor.get_month_week_offset();
        hd_month_size = m_dateCursor.get_hd_month_size();
        int hd_prev_month_size = 0;
        int cellid = 6 - hd_offset;
        if (hd_offset != 0)
        {
            hd_prev_month_size = m_dateCursor.get_hd_prev_month_size();
        }

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
                    if (holydays[cellid] != 0)
                    {
                        g.setColor(COLOR_CELL_HOLIDAY);
                    }
                    else
                    {
                        if (cellid==0 || cellid ==29)//rosh hodesh
                        {
                            g.setColor(COLOR_CELL_MONTH);
                        }
                        else
                            g.setColor(COLOR_CELL);
                    }
                    g.fillRect(x + 1, y + 1, m_cellWidth - 1, m_cellHeight - 1);
                    if (same_month && (cellid + 1 == s_dateToday.get_hd_day_in_month()))
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
        m_newCursorX = (short) (7 - m_dateCursor.get_day_in_week());
        m_newCursorY = (short) ((m_dateCursor.get_hd_day_in_month() + m_newCursorX - 1) / 7);
        if (monthchanged)
        {
            cursor_x = m_newCursorX;
            cursor_y = m_newCursorY;
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
        int month = m_dateCursor.get_hd_month();
        keyCode = getGameAction(keyCode);
        if (keyCode == UP)
        {
            if (m_dateCursor.moveday(-7))
            {
                movecursor(m_dateCursor.get_hd_month() != month);
            }
            return;
        }
        if (keyCode == DOWN)
        {
            if (m_dateCursor.moveday(7))
            {
                movecursor(m_dateCursor.get_hd_month() != month);
            }
            return;
        }
        if (keyCode == LEFT)
        {
            if (m_dateCursor.moveday(1))
            {
                movecursor(m_dateCursor.get_hd_month() != month);
            }
            return;
        }
        if (keyCode == RIGHT)
        {
            if (m_dateCursor.moveday(-1))
            {
                movecursor(m_dateCursor.get_hd_month() != month);
            }
            return;
        }
        if (keyCode == FIRE)
        {
            showParasha();
        }

    }//end keyPressed

    protected void pointerPressed(int x, int y)
    {

        if (m_tableX < x && (m_tableY + m_cellHeight + CELL_MARGINS) < y && x < m_tableX + m_tableWidth && y < m_tableY + m_tableHeight)
        {
            int cellx = (x - m_tableX) / (m_cellWidth + CELL_MARGINS);
            int celly = (y - m_tableY) / (m_cellHeight + CELL_MARGINS) - 1;
            int month = m_dateCursor.get_hd_month();
            m_dateCursor.moveday((celly - cursor_y - 1) * m_columns + m_rows - cellx + cursor_x + 1);
            movecursor(m_dateCursor.get_hd_month() != month);
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
        //bereshit //c_openHumashBereshit=new ScreenCommand(ScriptedTextViewer.getInstance(), "/data/bereshit" ,"בראשית", 12);
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
