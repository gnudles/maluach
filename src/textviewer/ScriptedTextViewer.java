/* This is free and unencumbered software released into the public domain.
 */

package textviewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;
import maluach.CommandPool;
import maluach.DisplayBack;
import list.DynamicIntArray;
import maluach.*;

public class ScriptedTextViewer extends Canvas implements ScreenView,DisplayBack
{

    private Image m_imageFont;
    
    private final byte[] m_fontCharShiftBig =
    {
        0, 7, 6, 0, 0, 0, 0, 6, 5, 5, 0, 0, 6, 4, 6, 0,//first line in the font
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 0, 0, 0, 0,//0
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//@
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 6, 0, 0,//P
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//`
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 7, 5, 0, 0,//p
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -1, 5, -2, -1, -1, 3, -1,//nikud
        7, -1, -1, 7, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0,
        1, 3, 4, 4, 3, 6, 5, 3, 3, 7, 3, 4, 4, 3, 2, 7,
        4, 3, 3, 4, 3, 3, 3, 3, 4, 1, 1, 0, 0, 0, 0, 0
    };
    private final byte[] m_fontCharWidthBig =
    {
        7, 2, 6, 0, 0, 0, 0, 2, 5, 5, 0, 0, 4, 6, 2, 0,//first line in the font
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 0, 0, 0,//0
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//@
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0, 0,//P
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//`
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 2, 6, 0, 0,//p
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 7, 0,//nikud
        2, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        13, 10, 8, 9, 11, 5, 5, 11, 10, 5, 9, 10, 9, 10, 12, 5,
        7, 10, 11, 9, 9, 12, 11, 10, 9, 14, 13, 0, 0, 0, 0, 0
    };
        private final byte[] m_fontCharShiftSmall =
    {
        1, 5, 4, 1, 2, 1, 2, 5, 4, 3, 2, 2, 3, 3, 4, 2,//first line in the font
        2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 4, 3, 2, 2, 2, 2,//0
        2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2,//@
        2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 4, 2, 3, 2, 1,//P
        3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 2,//`
        2, 2, 3, 2, 2, 2, 2, 1, 2, 2, 2, 3, 5, 3, 2, 0,//p
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -1, 4, -2, -1, -1, 2, -1,//nikud
        2, -1, -1, 3, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0,
        2, 2, 3, 2, 3, 4, 3, 2, 2, 4, 3, 3, 3, 2, 2, 4,
        3, 2, 2, 3, 2, 2, 2, 2, 2, 3, 1, 0, 0, 0, 0, 0
    };
    private final byte[] m_fontCharWidthSmall =
    {
        3, 1, 3, 8, 7, 8, 7, 1, 3, 3, 7, 7, 3, 4, 2, 6,//first line in the font
        7, 5, 8, 7, 7, 7, 7, 7, 7, 7, 2, 3, 7, 7, 7, 5,//0
        7, 7, 7, 7, 7, 7, 7, 7, 7, 5, 6, 7, 7, 7, 7, 7,//@
        7, 7, 7, 7, 7, 7, 7, 10, 7, 7, 7, 3, 6, 3, 7, 8,//P
        3, 6, 6, 6, 6, 6, 6, 6, 6, 5, 4, 6, 5, 7, 6, 6,//`
        6, 6, 5, 6, 6, 6, 6, 8, 6, 6, 6, 5, 1, 5, 7, 0,//p
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 6, 0,//nikud
        1, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        6, 6, 4, 6, 5, 3, 4, 6, 6, 3, 6, 5, 5, 6, 6, 3,
        4, 6, 6, 5, 6, 6, 6, 6, 5, 5, 7, 0, 0, 0, 0, 0
    };
    private byte[] m_fontCharShift;
    private byte[] m_fontCharWidth;
    final private int charSpacing=1;
    /** the height of our image font*/
    private int m_charWidth;
    private int m_fontHeight;
    private byte[] m_textBuffer;
    private DynamicIntArray m_lineBreaks;
    //private static Font m_defaultFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    /** the number of lines that can be viewed on the screen.*/
    private int m_textLines;
    private int m_topVisibleLine;
    private String [] m_filesList;
    private int m_filesListIndex;
    byte isBig;
    final public void ReInitFont(byte big)
    {
        isBig=big;
        try
        {
            // load the bitmap font
            if (big!=0)
            {
                m_imageFont = Image.createImage("/data/hebfontb.png");
                m_charWidth = 15;
                m_fontHeight = 25;
                m_fontCharShift=m_fontCharShiftBig;
                m_fontCharWidth=m_fontCharWidthBig;
            }
            else
            {
                m_imageFont = Image.createImage("/data/hebfont.png");
                m_charWidth = 10;
                m_fontHeight = 17;
                m_fontCharShift=m_fontCharShiftSmall;
                m_fontCharWidth=m_fontCharWidthSmall;
            }
            m_textLines = getHeight() / (m_fontHeight+1);
        }
        catch (Exception ex)
        {
            m_imageFont = null;
        }
    }
    public ScriptedTextViewer()
    {
        ReInitFont(MaluachPreferences.GetBigFont());
        
    }

    private void FormatToLines()
    {

        int sBegin = 0, sEnd = 0, lastSpace = 0;
        int TextWidth = 0;
        int LineIndex = 1;
        m_lineBreaks = new DynamicIntArray();
        m_lineBreaks.push(0);
        short c;
        short prevc=0;
        while (true)
        {

            if (m_textBuffer[sEnd] == ' ')// space
            {
                lastSpace = sEnd;
            }
            c = m_textBuffer[sEnd];
            if (c >= 32)
            {
                c -= 32;
            }
            else if (c < 0)
            {
                c += 256 - 32;
            }
            TextWidth += m_fontCharWidth[c];
            if ((c==164 && m_fontCharShift[prevc]==-2))//handle jerusalem hirik
            {
                TextWidth+=charSpacing;
            }

            if (m_textBuffer[sEnd] == '\n')
            {
                sBegin = sEnd + 1;
                m_lineBreaks.push(sBegin);
                LineIndex++;
                TextWidth = 0;
            }
            else
            {
                if (TextWidth >= getWidth() - 2 * charSpacing)
                {
                    if (lastSpace == sBegin)
                    {
                        sEnd--;
                        sBegin = sEnd;
                    }
                    else
                    {
                        sEnd = lastSpace;
                        sBegin = sEnd+1;
                    }
                    lastSpace = sBegin;
                    m_lineBreaks.push(sBegin);
                    LineIndex++;
                    TextWidth = 0;
                }
                else
                {
                    if (m_fontCharShift[c] >= 0  || (c==164 && m_fontCharShift[prevc]==-2)) //second part to handle jerusalem hirik
                        TextWidth += charSpacing;
                }
            }
            sEnd++;
            if (sEnd == m_textBuffer.length)
            {
                m_lineBreaks.push(sEnd);
                break;
            }
            prevc=c;
        }
    }

    public int LoadFile(String filename)
    {
        m_topVisibleLine = 0;
        InputStream in = getClass().getResourceAsStream(filename);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] aoBuffer = new byte[512];
        int nBytesRead;
        try
        {
            while ((nBytesRead = in.read(aoBuffer)) > 0)
            {
                out.write(aoBuffer, 0, nBytesRead);
            }
            in.close();
        }
        catch (IOException ex)
        {
            return -1;
        }
        m_textBuffer = out.toByteArray();
        FormatToLines();
        repaint();
        return 0;
    }

    public int LoadMany(String[] filenames,int index)
    {
        m_filesListIndex=index;
        m_filesList=filenames;
        return LoadFile(filenames[index]);
    }
    private void drawTextLine(Graphics g, int BufferIndex, int ScreenIndex)
    {
        
        int x = getWidth() - charSpacing;
        int y = ScreenIndex * (m_fontHeight+1);

        int prevShift = 0;
        int curShift=0;
        int curWidth=0;
        short c;
        short prevc=0;
        int i;

        
        for (i = m_lineBreaks.At(BufferIndex); i < m_lineBreaks.At(BufferIndex + 1); i++)
        {

            c = m_textBuffer[i];
            if (c >= 32 || c < 0)
            {
                if (c >= 32)
                {
                    c -= 32;
                }
                else
                {
                    c += 256 - 32;
                }
                if (c==202-32 )//if holam haser
                {
                	if (i+1<m_textBuffer.length)
                		if (m_textBuffer[i+1]==204-256)
                		{
                			m_textBuffer[i+1]=202-256;
                			m_textBuffer[i]=204-256;
                			c=204-32;
                		}
                }
                if (m_fontCharShift[c] < 0)
                {

                    if (c==(196-32) && m_fontCharShift[prevc]==-2)//jerusalem hirik
                    {
                        g.drawRegion(m_imageFont, ((c % 16)) * m_charWidth , ((c / 16)) * m_fontHeight, m_charWidth, m_fontHeight, Sprite.TRANS_NONE, x+m_charWidth/2, y, Graphics.TOP | Graphics.RIGHT);
                        x -=  charSpacing;
                    }
                    else
                    {
                        g.drawRegion(m_imageFont, ((c % 16)) * m_charWidth, ((c / 16)) * m_fontHeight, m_charWidth, m_fontHeight, Sprite.TRANS_NONE, x + prevShift - 1 + curWidth + charSpacing, y, Graphics.TOP | Graphics.RIGHT);
                    }

                }
                else
                {
                    curWidth = m_fontCharWidth[c];
                    curShift = m_fontCharShift[c];

                    g.drawRegion(m_imageFont, ((c % 16)) * m_charWidth + curShift, ((c / 16)) * m_fontHeight, curWidth, m_fontHeight, Sprite.TRANS_NONE, x, y, Graphics.TOP | Graphics.RIGHT);
                    if (c!=202-32 )//if not holam haser
                    {
                        prevShift = curShift;
                        
                    }
                    x -= curWidth + charSpacing;
                }
                prevc=c;
                
            }

        }
    }

    final public void paint(Graphics g)
    {
        g.setColor(0x0);
        g.fillRect(0, 0, getWidth(), getHeight());
        int i;
        for (i = 0; i < m_textLines; i++)
        {
            if (i+ m_topVisibleLine < m_lineBreaks.length - 1)
            {
                drawTextLine(g, i + m_topVisibleLine, i);
            }
            else
            {
                break;
            }
        }
    }

    final public void keyPressed(int keyCode)
    {
        keyCode = getGameAction(keyCode);
        boolean needRepaint = false;
        int filechange=0;
        switch (keyCode)
        {
            case UP:
                if (m_topVisibleLine > 0)
                {
                    m_topVisibleLine--;
                    needRepaint = true;
                }
                else
                    filechange=-1;
                break;
            case DOWN:
                if (m_topVisibleLine + m_textLines < m_lineBreaks.length - 2)
                {
                    m_topVisibleLine++;
                    needRepaint = true;
                }
                else
                    filechange=1;
                break;
            case RIGHT://prev page
                if (m_topVisibleLine > 0)
                {
                    int position = m_topVisibleLine / m_textLines;
                    if (m_topVisibleLine % m_textLines == 0)
                    {
                        position--;
                    }
                    m_topVisibleLine = position * m_textLines;
                    needRepaint = true;
                }
                else
                {
                    filechange=-1;
                }
                break;
            case LEFT://next page
                int position = m_topVisibleLine / m_textLines;
                if (m_topVisibleLine % m_textLines == 0)
                {
                    if (m_topVisibleLine + m_textLines < m_lineBreaks.length - 2)
                    {
                        m_topVisibleLine += m_textLines;
                        needRepaint = true;
                    }
                    else
                    {
                        filechange=1;
                    }
                }
                else
                {
                    m_topVisibleLine = (position + 1) * m_textLines;
                    needRepaint = true;
                }
                break;
        }
        if (needRepaint)
        {
            repaint();
        }
        if (filechange!=0)
        {
            if (m_filesList!=null)
            {
                m_filesListIndex+=filechange;
                if (m_filesListIndex==m_filesList.length)
                {
                    m_filesListIndex--;
                    return;
                }
                if (m_filesListIndex==-1)
                {
                    m_filesListIndex=0;
                    return;
                }
                LoadFile(m_filesList[m_filesListIndex]);
            }
        }
            

    }

    protected void pointerPressed(int x, int y)
    {
    }

    protected void pointerDragged(int x, int y)
    {
    }
    private void InitCommands()
    {
        addCommand(CommandPool.getC_back());
    }

    static private ScriptedTextViewer _instance;
    static public ScriptedTextViewer getInstance()
    {

        if (_instance == null)
        {
            _instance = new ScriptedTextViewer();
            _instance.InitCommands();
            _instance.setCommandListener(maluach.getInstance());
        }
        return _instance;
    }

    public void OnShow(Object param)
    {
        if (isBig!=MaluachPreferences.GetBigFont())
        {
            ReInitFont((byte)(1-isBig));
        }
        m_filesList=null;
        if (param instanceof String)
            LoadFile((String)param);
        else if(param instanceof String []) 
        {
            LoadMany((String[])param,0);
        }
    }

    public boolean Back()
    {
        m_textBuffer=null;
        m_lineBreaks=null;
        return true;
    }
}
