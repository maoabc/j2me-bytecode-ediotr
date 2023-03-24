package constedit;

import mpe.Constants;
import util.IntArray;

import java.util.*;

public class ConstantPoolDocument {

    public Vector data ;
    public boolean inSelection = false ;
    public int curX,  curY,  stringOffset,  vPosition;
    public int selAnchorX,  selAnchorY,  selX1,  selY1,  selX2,  selY2;
    private int maxLines;
    int lab[];

    public ConstantPoolDocument(Vector v, int maxL) {
        maxLines = maxL;

        int j=v.size();

        lab=new int[j];

        data = v ;
        for (int i = 0; i < j ; i++) {
            lab[i]=i;
        }
        curX = stringOffset = vPosition = 0;
        curY = 0;
    }

    public void moveCursor(int dir) {
        /* 0 - Left
         * 1 - Up
         * 2 - Right
         * 3 - Down
         */
        switch (dir) {
            case 0:
                if (curX > 0) {
                    curX--;
                    break;
                } else if (globalY() > 0) {
                    curX = data.elementAt(globalY() - 1).toString().length();
                }
            // Выделение
            case 1:
                if (globalY() > 0) {
                    curY--;
                    if (curY < 0 && vPosition > 0) {
                        curY = 0;
                        vPosition--;
                    }
                    if (curX > data.elementAt(globalY()).toString().length()) {
                        curX = data.elementAt(globalY()).toString().length();
                    }
                }
                break;
            case 2:
                if (curX < data.elementAt(globalY()).toString().length()) {
                    curX++;
                    break;
                } else if (data.size() > globalY() + 1 && globalY() < data.size()) {
                    curX = 0;
                }
            case 3:
                if (globalY() < data.size() - 1) {
                    curY++;
                    if (curY == maxLines) {
                        curY = maxLines - 1;
                        vPosition++;
                    }
                    if (curX > data.elementAt(globalY()).toString().length()) {
                        curX = data.elementAt(globalY()).toString().length();
                    }
                }
                break;
        }
    }


    public void setCursor(int x, int y) {
        curX = x;
        if (y < vPosition) {
            curY = 0;
            vPosition = y;
        } else if (y > vPosition + maxLines - 1) {
            curY = maxLines - 1;
            vPosition = y - maxLines + 1;
        } else {
            curY = y - vPosition;
        }
    }

    public int getCursor(int coord) {
        return coord == Constants.CUR_X ? curX : globalY();
    }

    public final int globalY() {
        return curY + vPosition;
    }



    public void setMaxLines(int maxL) {
        maxLines = maxL;
        setCursor(curX, curY);
    }
    
    public void filterString ( ) {
        setCursor(0,0);
        int temp=data.size();
        Vector v=new Vector();
        IntArray ia=new IntArray(256);
        for( int i = 0 ; i < temp ; i++ ){
            String s = data.elementAt(i).toString() ;
            if ( s.startsWith ( "String_Info:" ) ) {
                int j = s.indexOf( "=" ) ;
                s = s.substring(j+1) ;
                j = Integer.parseInt ( s ) ;
                v.addElement( data.elementAt(j) ) ;
                ia.add(j);
            }
        }
        int k = v.size();
        if( k >0){
            data = v;
            this.lab= ia.toIntArray() ;
        }
    }
    
    public void filter(){
        setCursor(0,0);
        Vector v=new Vector();
        int temp=data.size();
        IntArray ia=new IntArray();
        for(int i =0; i < temp ; i++ ){
            String s = data.elementAt(i).toString() ;
            if ( 
                s.startsWith( "Utf8_Info" )
                || s.startsWith( "Integer_Info" )
                || s.startsWith( "Long_Info" )
                || s.startsWith( "Float_Info" )
                || s.startsWith( "Double_Info" ) ){
                v.addElement( s );
                ia.add(i);
            }
        }
        int k=v.size();
        if( k>0 ){
            data = v ;
            this.lab = ia.toIntArray() ;
        }
    }

}
