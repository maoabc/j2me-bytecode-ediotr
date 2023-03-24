package constedit;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import java.util.Vector;

public class Work extends Canvas {

    public static int maxLines;
    private Font font;
    boolean isfilter= false ;
    int mod=0, count = 0;
    private ConstantEditor ce ;
    ConstantPoolDocument doc ;

    public Work(ConstantEditor ce ,Vector v) {
        this.ce = ce;

        font = Font.getFont(Font.FACE_SYSTEM , Font.STYLE_PLAIN,  Font.SIZE_SMALL );
        createDocument(v);
        sizeChanged(getWidth(), getHeight());
    }

    protected void paint(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());


        g.setColor(0xd8ebf5);
        g.fillRect(0, doc.curY * font.getHeight(), getWidth(), font.getHeight() + 1);

        g.setFont(font);
        final int numBarW = font.stringWidth(new Integer(doc.data.size()).toString()) + 8 ;

        if (font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) > getDocument().stringOffset + getWidth() - numBarW - 12) {
            doc.stringOffset = font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) - getWidth() + numBarW + 12;
        } else if (font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) < getDocument().stringOffset) {
            doc.stringOffset = font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX);
        }

        g.setColor(0x000000);
        for (int i = 0; doc.data.size() > (i + doc.vPosition) && i < maxLines; i++) {
            g.drawString(doc.data.elementAt(i + doc.vPosition).toString(), numBarW - doc.stringOffset + 1, i * font.getHeight() + 1, Graphics.LEFT + Graphics.TOP);
        }

        // Номера строк
        g.setColor(0xe0e0e0);
        g.fillRect(0, 0, numBarW, getHeight());
        g.setColor(0x000000);
        for (int i = 0; doc.data.size() > (i + doc.vPosition) && i < maxLines; i++) {
            g.drawString( new Integer( i + doc.vPosition ).toString()+" ", numBarW, i * font.getHeight() + 1, Graphics.RIGHT + Graphics.TOP);
        }
        //}

        // Скролл-бар
        int wsHeight = getHeight() - font.getHeight();
        if (doc.data.size() > maxLines) {
            g.setColor(0xc8c8c8);
            g.fillRect(getWidth() - 3, 0, 3, wsHeight);
            g.setColor(0x000000);
            final int barH = wsHeight - (wsHeight * (doc.data.size() - maxLines) / doc.data.size());
            g.fillRect(getWidth() - 3, (wsHeight - barH) * doc.vPosition / (doc.data.size() - maxLines), 3, barH);
        }


        g.setColor(0x00aff0);
        g.fillRect(0, getHeight() - font.getHeight(), getWidth(), font.getHeight());
        g.setColor(0x000000);
        g.drawString("行: " + new Integer(doc.globalY() ).toString() + "; 列: " + new Integer(doc.curX ).toString(), 1, getHeight(), Graphics.BOTTOM + Graphics.LEFT);
            g.drawString( mod== 1 ? "过滤1" : ( mod == 2 ? "过滤2" : "常量池"), getWidth(), getHeight(), Graphics.BOTTOM + Graphics.RIGHT);

        // Рисуем курсор
        g.setColor(0xff0000);
        g.drawLine(font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) + numBarW - doc.stringOffset, doc.curY * font.getHeight(), font.substringWidth(doc.data.elementAt(doc.globalY()).toString(), 0, doc.curX) + numBarW - doc.stringOffset, doc.curY * font.getHeight() + font.getHeight());
          //替换
        if(count > 0 ){
            String str="替换：" + count + "次" ;
            count = 0 ;
            g.setColor(0xdfffff);
            g.fillRect(numBarW, 0 ,font.stringWidth(str) + 3,font.getHeight() +1 ) ;
            g.setColor(0x0000ff);
            g.drawString(str,numBarW+1 , 3 ,Graphics.TOP|Graphics.LEFT);
        }
        //搜索
        if( ! ce.isSearch ){
            ce.isSearch=true;
            String tr="找不到: " + ce.main.setting.codeSearch ;
            g.setColor(0xdfffff);
            g.fillRect(numBarW, 0 ,font.stringWidth(tr)+4 ,font.getHeight() +1 ) ;
            g.setColor(0x0000ff);
            g.drawString(tr,numBarW+1 , 3 ,Graphics.TOP|Graphics.LEFT);
        }
    }

    protected void keyPressed(int keyCode) {
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
        }else if( keyCode == 49 ){
                doc.setCursor(0,0);
        }else if( keyCode == 50 ){
                ce.showMenu(ConstantEditor.MENU_GOOGLE);
        }else if( keyCode == 51 ){   
                doc.setCursor(doc.data.elementAt(doc.data.size() - 1).toString().length(), doc.data.size() - 1);
        }else if( keyCode == 52 ){
            for(int i=0;i<11;i++)
                doc.moveCursor(1);
        }else if(keyCode==54){
            for(int i=0;i<11;i++)
                doc.moveCursor(3);
        }else if(keyCode==55){
                ce.showMenu(ConstantEditor.MENU_REPLACE);
        }else if(keyCode==56){
                ce.showMenu(ConstantEditor.MENU_SEARCH);
        }else if( ce.isKey && keyCode==57){
                ce.search(doc.globalY()+1 , ce.main.setting.codeSearch );
        }else if(keyCode==48){
                ce.showMenu(ConstantEditor.MENU_GOTO);
        }else if(keyCode == -5 || keyCode == 53 || keyCode == -10 || keyCode == -6 ){
            String s=doc.data.elementAt(doc.globalY()).toString();
            if ( 
                s.startsWith( "Utf8_Info" )
                || s.startsWith( "Integer_Info" )
                || s.startsWith( "Long_Info" )
                || s.startsWith( "Float_Info" )
                || s.startsWith( "Double_Info" ) )
                ce.showMenu(ConstantEditor.MENU_LINEE);
        }else if (keyCode == Canvas.KEY_POUND) {
            if( mod == 0 ){
                mod = 1;
                doc.filter();
            }
        } else if (keyCode == Canvas.KEY_STAR) {
            if( mod == 0 ){
                mod = 2;
                doc.filterString();
            }
        } else if (keyCode == -7 || keyCode == 4) {
            ce.main.display.setCurrent(ce.main.mainList);
        }
        repaint();
    }

    public void createDocument(Vector v) {
        doc=new ConstantPoolDocument(v, maxLines);
    }

    public ConstantPoolDocument getDocument() {
        return doc;
    }



    protected void keyRepeated(int key) {
        keyPressed(key);
    }

    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
        setFullScreenMode(true);
        maxLines = (int) Math.floor(getHeight() / font.getHeight()) - 1;
        doc.setMaxLines(maxLines);
    }

}
