package mpe;

import classfile.JavaOpcode;
import util.Utils;

import java.util.*;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Workspace extends Canvas {

    private int inputKey = -1,  selChar = -1,  charMode = 2,  index;
    public static int maxLines;
    int cont=0,rcount = 0 ;
    boolean error=false;
    private Font font;
    private String[] layout;
    private int inCharTable = 0;
    private TermInputThread t;
    private MPE mpe;
    Document doc;
    boolean selec=true;

    public Workspace(MPE mpe,String str) {
        this.mpe = mpe;
        font = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN, Font.SIZE_SMALL);
        loadLayout(Constants.layoutLat);
        setDocument(str);
        sizeChanged(getWidth(), getHeight());
    }

    protected void paint(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Подсветка текущей строки
        g.setColor(0xd8ebf5);
        g.fillRect(0, doc.curY * font.getHeight(), getWidth(), font.getHeight() + 1);

        g.setFont(font);
        final int numBarW = font.stringWidth(new Integer(doc.data.size()).toString()) + 12;

        if (font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) > doc.stringOffset + getWidth() - numBarW - 12) {
            doc.stringOffset = font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) - getWidth() + numBarW + 12;
        } else if (font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) < doc.stringOffset) {
            doc.stringOffset = font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX);
        }

        // Выделение
        if (doc.inSelection) {
            doc.calcSelection();
            g.setColor(0xb0c5e3);
            if (doc.selY1 == doc.selY2) {
                g.fillRect(font.substringWidth(doc.data.elementAt(doc.selY1).toString(), 0, doc.selX1) + numBarW - doc.stringOffset, (doc.selY1 - doc.vPosition) * font.getHeight(), font.substringWidth(doc.data.elementAt(doc.selY1).toString(), doc.selX1, doc.selX2 - doc.selX1), font.getHeight());
            } else {
                for (int i = doc.selY1 >= doc.vPosition ? doc.selY1 : doc.vPosition; i <= doc.selY2 && i < doc.vPosition + maxLines; i++) {
                    if (i == doc.selY1) {
                        g.fillRect(font.substringWidth(doc.data.elementAt(doc.selY1).toString(), 0, doc.selX1) + numBarW - doc.stringOffset, (doc.selY1 - doc.vPosition) * font.getHeight(), font.stringWidth(doc.data.elementAt(doc.selY1).toString().substring(doc.selX1)), font.getHeight());
                    } else if (i == doc.selY2) {
                        g.fillRect(numBarW - doc.stringOffset, (doc.selY2 - doc.vPosition) * font.getHeight(), font.substringWidth(doc.data.elementAt(doc.selY2).toString(), 0, doc.selX2), font.getHeight());
                    } else {
                        g.fillRect(numBarW - doc.stringOffset, (i - doc.vPosition) * font.getHeight(), font.stringWidth(doc.data.elementAt(i).toString()), font.getHeight());
                    }
                }
            }
        }

        // Строки
        g.setColor(0x000000);
        for (int i = 0; doc.data.size() > (i + doc.vPosition)&& i<maxLines ; i++) {
            g.drawString(doc.data.elementAt(i + doc.vPosition).toString(), numBarW - doc.stringOffset + 1, i * font.getHeight() + 1, Graphics.LEFT + Graphics.TOP);
        }
        g.setColor(0xe0e0e0);
        g.fillRect(0, 0, numBarW, getHeight());
        g.setColor(0x000000);
        for (int i = 0;doc.data.size()>(i+doc.vPosition)&& i<maxLines ; i++) {
                g.drawString( (doc.isShow?doc.lab[i+doc.vPosition] : (i+doc.vPosition) )+" ", numBarW, i * font.getHeight() + 1, Graphics.RIGHT + Graphics.TOP);
        }

        // Скролл-бар
        int wsHeight = getHeight() - font.getHeight();
        if (doc.data.size() > maxLines) {
            g.setColor(0xc8c8c8);
            g.fillRect(getWidth() - 3, 0, 3, wsHeight);
            g.setColor(0x000000);
            final int barH = wsHeight - (wsHeight * (doc.data.size() - maxLines) / doc.data.size());
            g.fillRect(getWidth() - 3, (wsHeight - barH) * doc.vPosition / (doc.data.size() - maxLines), 3, barH);
        }

        // Ввод символа
        g.setColor(0x00aff0);
        g.fillRect(0, getHeight() - font.getHeight(), getWidth(), font.getHeight());
        g.setColor(0x000000);
        if (inputKey > -1 && charMode != 2) {
            int leftPos = 3;
            for (int i = 0; i < layout[charMode * 9 + inputKey].length(); i++) {
                g.drawString(layout[charMode * 9 + inputKey].substring(i, i + 1), leftPos, getHeight(), Graphics.BOTTOM + Graphics.LEFT);
                if (selChar == i) {
                    g.drawRect(leftPos - 3, getHeight() - font.getHeight(), font.stringWidth(layout[charMode * 9 + inputKey].substring(i, i + 1)) + 6, font.getHeight() - 1);
                }
                leftPos += font.stringWidth(layout[charMode * 9 + inputKey].substring(i, i + 1)) + 6;
            }
        } else {
            g.drawString(doc.inSelection ? mpe.lr.get("25") : mpe.lr.get("23") + ": " + new Integer(doc.globalY() ).toString() +";"+ mpe.lr.get("24") + ": " + new Integer(doc.curX ).toString(), 1, getHeight(), Graphics.BOTTOM + Graphics.LEFT);
            g.drawString(charMode == 0 ? "abc" : (charMode == 1 ? "ABC" : "快捷"), getWidth(), getHeight(), Graphics.BOTTOM + Graphics.RIGHT);
        }

        // Рисуем курсор
        g.setColor(0xff0000);
        g.drawLine(font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) + numBarW - doc.stringOffset, doc.curY * font.getHeight(), font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) + numBarW - doc.stringOffset, doc.curY * font.getHeight() + font.getHeight());
        if(error){
            error=false;
            String str="错误偏移！";
            g.setColor(0xdfffff);
            g.fillRect(numBarW, 0 ,font.stringWidth(str) + 3,font.getHeight() +1 ) ;
            g.setColor(0x0000ff);
            g.drawString(str,numBarW+2 , 3 ,Graphics.TOP|Graphics.LEFT);
        }
        if(rcount > 0 ){
            String st="替换: " + rcount + "次" ;
            rcount = 0 ;
            g.setColor(0xdfffff);
            g.fillRect(numBarW, 0 ,font.stringWidth(st) + 3,font.getHeight() +1 ) ;
            g.setColor(0x0000ff);
            g.drawString(st,numBarW+2 , 3 ,Graphics.TOP|Graphics.LEFT);
        }
        if(! mpe.isSearch ){
            String sx="找不到: " + mpe.m.setting.codeSearch ;
            mpe.isSearch = true ;
            g.setColor(0xdfffff);
            g.fillRect(numBarW, 0 ,font.stringWidth(sx) + 4 ,font.getHeight() +1 ) ;
            g.setColor(0x0000ff);
            g.drawString(sx,numBarW+1 , 3 ,Graphics.TOP|Graphics.LEFT);
        }

        if (inCharTable > 0) {
            CharTable.draw(g, font, getWidth(), getHeight(), inCharTable - 1);
        }
    }

    protected void keyPressed(int keyCode) {
        if (inCharTable > 0) {
            switch (keyCode) {
                case -1:
                case 50:
                    CharTable.up();
                    break;
                case -3:
                case 52:
                    CharTable.left();
                    break;
                case -4:
                case 54:
                    CharTable.right();
                    break;
                case -2:
                case 56:
                    CharTable.down();
                    break;
                case -5:
                case 53:
                    doc.Insert(CharTable.select());
                    CharTable.reset();
                    inCharTable = 0;
                    break;
                case Canvas.KEY_STAR:
                case 48:
                    CharTable.reset();
                    inCharTable = 0;
                    break;
            }
        } else {
            if (keyCode == -1) {
                // UP
                doc.moveCursor(1);
            } else if (keyCode == -3) {
                // LEFT
                if(doc.globalY() == 0 && doc.curX==0)
                        doc.setCursor(doc.data.elementAt(doc.data.size() - 1).toString().length(), doc.data.size() - 1);
                else
                    doc.moveCursor(0);
            } else if (keyCode == -4) {
                // RIGHT
                if(doc.globalY() == doc.data.size()-1 && doc.curX== doc.data.elementAt(doc.data.size() - 1).toString().length())
                    doc.setCursor( 0, 0);
                else
                    doc.moveCursor(2);
            } else if (keyCode == -2) {
                // DOWN
                doc.moveCursor(3);
            } else if (keyCode == -7 || keyCode == 4) {
                // Правый софт
                mpe.showMenu(MPE.MENU_MAIN);
            } else if (keyCode == -5) {
                if(doc.curX==doc.data.elementAt(doc.globalY()).toString().length() )
                    doc.Insert("\n  ");
                else
                    doc.Insert("\n");
            } else if (keyCode == Canvas.KEY_POUND) {
                if (charMode < 2) {
                    charMode++;
                } else {
                    charMode = 0;
                }
                mpe.isKey=false;
            } else if (keyCode == Canvas.KEY_STAR) {
                inCharTable = 1;
            } else if (keyCode == 48) {
                if(mpe.isKey)
                    mpe.search(doc.globalY()+1 , mpe.m.setting.codeSearch);
                else
                    branch();
            }else if(keyCode==-6){
                    mpe.showMenu(MPE.MENU_LINEED);
                    mpe.isKey=false;
            }else if ((keyCode>=49 && keyCode <= 57) || keyCode==-50) {
                if (charMode == 2) {
                    if(keyCode==54){
                        for(int i=0;i<11;i++)
                            doc.moveCursor(3);
                    }else if(keyCode==52){
                        for(int i=0;i<11;i++)
                            doc.moveCursor(1);
                    }else if(keyCode==55){
                        mpe.showMenu(mpe.MENU_REPLACE);
                    }else if(keyCode==56){
                        mpe.showMenu(mpe.MENU_SEARCH);                                  }else if(keyCode==53){
                        mpe.showMenu(mpe.MENU_TEMPLATES);                                  }else if(keyCode==57){
                        mpe.showMenu(mpe.MENU_ABOUT);                                  }else if(keyCode==49){
                        if(selec){
                            selec=false;
                            doc.startSelection();
                        }else{
                            selec=true;
                            mpe.m.buffer = doc.Copy();
                            doc.endSelection();
                        }
                        
                 }else if(keyCode==50){
                        selec=true;
                        mpe.m.buffer = doc.Cut();
                 }else if(keyCode==51){
                        if( ! doc.inSelection)
                        doc.Insert(mpe.m.buffer);
                 }else if(keyCode==-50){
if(cont++%2==0)
                        if( ! doc.inSelection)
                            doc.Insert(mpe.m.buffer);
                 }
                } else {
                    if (inputKey == keyCode - 49) {
                        // Если мы повторно нажали на ту же кнопку
                        mpe.isKey=false;
                        selChar = selChar < layout[charMode * 9 + inputKey].length() - 1 ? selChar + 1 : 0;

                        doc.Backspace();
                        if (keyCode == 49 && selChar == 1) {
                            doc.Insert("\n");
                        } else {
                            doc.Insert(layout[charMode * 9 + inputKey].substring(selChar, selChar + 1));
                        }
                    } else {
                        inputKey = keyCode - 49;
                        selChar = 0;
                        if (keyCode == 49 && selChar == 1) {
                            doc.Insert("\n");
                        } else {
                            doc.Insert(layout[charMode * 9 + inputKey].substring(selChar, selChar + 1));
                        }
                    }

                    killTimer();
                    createTimer();
                }
            } else if ( keyCode == -1 || keyCode == -8 || keyCode == 8 || keyCode == 1 || keyCode == -10) {
                selec=true;
                doc.Backspace();
            } else if (keyCode >= 32 && keyCode <= 48 || keyCode >= 57 && keyCode <= 126) {
                doc.Insert("" + (char) keyCode);
            } else if (keyCode == 10) {
                doc.Insert("\n");
            }

            if (keyCode < 48 || keyCode > 57) {
                inputKey = -1;
                selChar = -1;
            }
        }

        repaint();
    }

    public void setDocument(String str) {
        doc=new Document(str, maxLines);
    }

    public void addDocument(String str) {
        doc.data=new Vector();
        String[] code= Utils.splitString(str,"\n");
        for(int i=1;i<code.length;i++)
            doc.data.addElement(code[i]);
        doc.labelLoad();
    }

    public Document getDocument() {
        return doc;
    }


    protected void keyRepeated(int key) {
        keyPressed(key);
    }

    private void loadLayout(String[] l) {
        layout = new String[36];
        for (int i = 0; i < 18; i++) {
            layout[i] = Constants.layoutLat[i];
            layout[i + 18] = l[i];
        }
    }

    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
        setFullScreenMode(true);
        maxLines = (int) Math.floor(getHeight() / font.getHeight()) - 1;
        doc.setMaxLines(maxLines);
    }

    private void createTimer() {
        t = new TermInputThread();
        t.start();
    }

    private void killTimer() {
        if (t != null) {
            t.kill();
            t = null;
        }
    }

    private class TermInputThread extends Thread {

        final Object killtoken = "killtoken";

        public void kill() {
            synchronized (killtoken) {
                this.interrupt();
            }
        }

        public void run() {
            try {
                Thread.sleep(600);
                inputKey = -1;
                selChar = -1;
                repaint();
            } catch (Exception ex) {
            }
        }
    }
    public void gotoLabel(int tem){
        int n=doc.lab[doc.globalY()];
        error=true;
        tem+=n;
        n=doc.globalY();
        if(Utils.binarySearch(doc.lab,tem) != -1){
            n=Utils.binarySearch(doc.lab,tem);
            error=false;
        }
        if (n >= 0 && n < doc.data.size()) {
            doc.setCursor(0, n);
        }
        mpe.showMenu(MPE.MENU_WORKSPACE);
    }
    private void branch(){
        String temp=doc.data.elementAt(doc.globalY()).toString().trim();
        String s="";
        int n=temp.indexOf(' ');
        if(n>-1){
            int i= JavaOpcode.toCode(temp.substring(0,n));
            if(JavaOpcode.isBranchInstruction(i))
            {
                error=true;
                s=temp.substring(n+1)+":";
                for(int j=0;j<doc.data.size();j++){
                        if(doc.data.elementAt(j).toString().indexOf(s) > -1) {
                        error=false;
                        doc.setCursor(0, j);
                        break;
                    }
                }
            }
        }
    }

}
