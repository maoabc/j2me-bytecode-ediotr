package mpe;

import classfile.JavaOpcode;
import util.Arrays;
import util.Utils;

import java.util.*;

public class Document {

    public Vector data;
    public boolean inSelection = false,isShow=false;
    int[] lab;
    public int curX,  curY,  stringOffset,  vPosition;
    public int selAnchorX,  selAnchorY,  selX1,  selY1,  selX2,  selY2;
    private int maxLines;
    public Document(String str, int maxL) {
        maxLines = maxL;

        data = new Vector();
            String[] initialCode = Utils.splitString(str, "\n");
            for (int i = 0; i < initialCode.length; i++) {
                data.addElement(initialCode[i]);
            }
            data.setElementAt("  "+data.elementAt(0).toString(),0);
setLabel();
labelLoad();
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

    public void Insert(String str) {
        String[] s = Utils.splitString(str, "\n");
        if (inSelection) {
            Backspace();
        }
        if (curX == 0) {
            if (s.length > 1) {
                String temp = data.elementAt(globalY()).toString();
                data.setElementAt(s[0], globalY());
                for (int i = 1; i < s.length - 1; i++) {
                    curY++;
                    data.insertElementAt(s[i], globalY());
                }
                curY++;
                data.insertElementAt(s[s.length - 1] + temp, globalY());
            } else {
                data.setElementAt(s[0] + data.elementAt(globalY()).toString(), globalY());
            }
            setCursor(s[s.length - 1].length(), globalY());
        } else if (curX == data.elementAt(globalY()).toString().length()) {
            data.setElementAt(data.elementAt(globalY()).toString() + s[0], globalY());
            if (s.length > 1) {
                for (int i = 1; i < s.length; i++) {
                    curY++;
                    data.insertElementAt(s[i], globalY());
                }
            }
            setCursor(data.elementAt(globalY()).toString().length(), globalY());
        } else {
            String temp = data.elementAt(globalY()).toString();
            if (s.length > 1) {
                data.setElementAt(temp.substring(0, curX) + s[0], globalY());
                for (int i = 1; i < s.length - 1; i++) {
                    curY++;
                    data.insertElementAt(s[i], globalY());
                }
                curY++;
                data.insertElementAt(s[s.length - 1] + temp.substring(curX), globalY());
                setCursor(s[s.length - 1].length(), globalY());
            } else {
                data.setElementAt(temp.substring(0, curX) + s[0] + temp.substring(curX), globalY());
                setCursor(temp.substring(0, curX).length() + s[0].length(), globalY());
            }
        }
        if(isShow)
            labelLoad();
    }

    public void Backspace() {
        if (inSelection) {
            calcSelection();
            setCursor(selX1, selY1);
            for (int i = selY1; i <= selY2; i++) {
                if (selY1 == selY2) {
                    data.setElementAt(data.elementAt(selY1).toString().substring(0, selX1) + data.elementAt(selY1).toString().substring(selX2), selY1);
                } else if (i == selY1) {
                    if (selX1 == 0) {
                        data.removeElementAt(i);
                        selY2--;
                        i--;
                    } else {
                        data.setElementAt(data.elementAt(i).toString().substring(0, selX1), i);
                    }
                } else if (i == selY2) {
                    if (selX2 == data.elementAt(i).toString().length()) {
                        data.removeElementAt(i);
                        selY2--;
                        i--;
                    } else {
                        data.setElementAt(data.elementAt(i).toString().substring(selX2), i);
                    }
                } else {
                    data.removeElementAt(i);
                    selY2--;
                    i--;
                }
            }
            endSelection();
        } else if (curX != 0 || globalY() != 0) {
            if (curX > 0) {
                data.setElementAt(Utils.stringDelete(data.elementAt(globalY()).toString(), --curX), globalY());
            } else {
                curY--;
                curX = data.elementAt(globalY()).toString().length();
                data.setElementAt(data.elementAt(globalY()).toString() + data.elementAt(globalY() + 1).toString(), globalY());
                data.removeElementAt(globalY() + 1);
                if (curY < 0 && vPosition > 0) {
                    curY = 0;
                    vPosition--;
                }
            }
        }
        if(isShow)
            labelLoad();
    }

    public String Copy() {
        String buffer = "";
        if (inSelection) {
            calcSelection();
            if (selY1 == selY2) {
                buffer = data.elementAt(selY1).toString().substring(selX1, selX2);
            } else {
                for (int i = selY1; i <= selY2; i++) {
                    if (i == selY1) {
                        buffer = data.elementAt(i).toString().substring(selX1) + "\n";
                    } else if (i == selY2) {
                        buffer += data.elementAt(i).toString().substring(0, selX2);
                    } else {
                        buffer += data.elementAt(i).toString() + "\n";
                    }
                }
            }
        }

        return buffer;
    }

    public String Cut() {
        String buffer = "";
        if (inSelection) {
            buffer = Copy();
            Backspace();
        }
        return buffer;
    }

    public void setCursor(int x, int y) {
        /*if (y >= data.size()) {
            y = data.size() - 1;
        }
        if (x >= data.elementAt(y).toString().length()) {
            x = data.elementAt(y).toString().length() - 1;
        }*/
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

    public void startSelection() {
        inSelection = true;
        selAnchorX = curX;
        selAnchorY = globalY();
    }

    public void endSelection() {
        inSelection = false;
    }

    public void calcSelection() {
        if (selAnchorY == globalY()) {
            selX1 = selAnchorX < curX ? selAnchorX : curX;
            selX2 = selAnchorX < curX ? curX : selAnchorX;
            selY1 = selY2 = globalY();
        } else {
            selX1 = selAnchorY < globalY() ? selAnchorX : curX;
            selX2 = selAnchorY < globalY() ? curX : selAnchorX;
            selY1 = selAnchorY < globalY() ? selAnchorY : globalY();
            selY2 = selAnchorY < globalY() ? globalY() : selAnchorY;
        }
    }

    public void setMaxLines(int maxL) {
        maxLines = maxL;
        setCursor(curX, curY);
    }

    public void labelLoad(){
        int[] lab=new int[data.size()];
        int labl=0;
        int labelCount=0;
        for(int i=0;i<lab.length;i++){
            if(i>0)
            {
                String s=data.elementAt(i-1).toString().trim();
                String[] str=null;
                int index=s.indexOf(' '); 
                if(index>-1)
                {
                    String st=s.substring(0,index);
                    index=JavaOpcode.getOpcodeLengthNew(JavaOpcode.toCode(st));
                    switch(index){
                        case JavaOpcode.TABLESWITCH:
                            try{
                            int temp0=labl;
                            for(;labl<temp0+4;labl++){
                                if((i+labl-labelCount)%4==0)
                                    break;
                            }
                            str=Utils.splitString(s," ");
                            if(str.length>2){
                                str=Utils.splitString(str[2],",");
                                s=str[0].substring(0,str[0].indexOf(':'));
                                int low=Integer.parseInt(s);
                                s=str[str.length-1].substring(0,str[str.length-1].indexOf(':'));
                                int high=Integer.parseInt(s);
                                labl+=4*(high-low+4);
                            }
                            }catch(Exception e){}
                        break;
                        case JavaOpcode.LOOKUPSWITCH:
                            int temp=labl;
                            for(;labl<temp+4;labl++){
                              if((i+labl-labelCount)%4==0)
                                  break;
                            }
                            str=Utils.splitString(s," ");
                            if(str.length>2){
                                str=Utils.splitString(str[2],",");
                                if(str!=null){
                                    labl+=8*(str.length+1);
                                }
                            }
                        break;
                        default:
                            labl+=index-1;
                    }
                }else if(s.startsWith("Label") ){
                    labelCount++;
                }
            }
            lab[i]=labl+i-labelCount;
        }
        this.lab=lab;
    }
    

    private void setLabel(){
        labelLoad();
        Hashtable hash=new Hashtable();
        for(int i=0;i<data.size();i++)
        {
            if(data.elementAt(i).toString().trim().indexOf(' ')>-1){
                String[] str=Utils.splitString(data.elementAt(i).toString().trim()," ");
                int code=JavaOpcode.toCode(str[0]);
                if(JavaOpcode.isBranchInstruction(code)){
                    int offset=Integer.parseInt(str[1]);
                    int jump=lab[i]+offset;
                    String s="Label"+jump;
                    data.setElementAt("  "+str[0]+" "+s,i);
                    hash.put(s,new Integer(jump));
                }else if(code==JavaOpcode.TABLESWITCH || code==JavaOpcode.LOOKUPSWITCH){
                    String temp=str[0];
                    int offset=Integer.parseInt(str[1]);
                    int jump=lab[i]+offset;
                    String s="Label"+jump;
                    temp+=" "+s+" ";
hash.put(s,new Integer(jump));
                    str=Utils.splitString(str[2],",");
                    for(int k=0;k<str.length;k++){
                        int j=str[k].indexOf(':');
                        temp+=str[k].substring(0,j)+":";
                        offset=Integer.parseInt(str[k].substring(j+1));
                        jump=lab[i]+offset;
                        s="Label"+jump;
                        temp+=s+",";
                        hash.put( s , new Integer(jump) );
                    }
                    data.setElementAt("  "+temp.substring(0,temp.length()-1),i);
                }
            }   
        }
        if( ! hash.isEmpty()){
            int[] a=new int[hash.size()];
            int i=0;
            Enumeration e=hash.keys();
            while(e.hasMoreElements()){
                String key=(String) e.nextElement();
                a[i++]=Integer.parseInt( hash.get(key).toString());
            }
            Arrays.sort(a);
            int labelCount=0;
            for(int k=0;k<a.length;k++) {
                int jump=a[k];
                String s="Label"+jump+":";
                jump=Utils.binarySearch(lab,jump);
                data.insertElementAt(s,jump+labelCount++);
            }
        }
    }


    private void removeLabel(){
        for(int i=0;i<data.size();i++)
        {
            String str=data.elementAt(i).toString().trim();
            if(str.startsWith("Label") ){
                data.removeElementAt(i);
            }
        }
    }




    public void setOffset(){
        labelLoad();
        Hashtable hash=new Hashtable();
        for(int i=0;i<data.size();i++)
        {
            String str=data.elementAt(i).toString().trim();
            if(str.startsWith("Label") ){
                hash.put(str,new Integer(lab[i]));
            }
        }
        if( ! hash.isEmpty()){
            for(int i=0;i<data.size();i++){
                if(data.elementAt(i).toString().trim().indexOf(' ')>-1){
                    String[] str=Utils.splitString(data.elementAt(i).toString().trim()," ");
                    int code=JavaOpcode.toCode(str[0]);
                    if(JavaOpcode.isBranchInstruction(code)){
                        String key=str[1]+":";
                        int jump=Integer.parseInt(hash.get(key).toString());
                        int start=lab[i];
                        String s=str[0]+" "+String.valueOf(jump-start);
                        data.setElementAt(s,i);
                    }else if(code==JavaOpcode.TABLESWITCH || code==JavaOpcode.LOOKUPSWITCH){
                        String temp=str[0];
                        String key=str[1]+":";
                        int jump=Integer.parseInt(hash.get(key).toString());
                        int start=lab[i];
                        temp+=" "+String.valueOf(jump-start)+" ";
                        str=Utils.splitString(str[2],",");
                        for(int j=0;j<str.length;j++){
                            String s=str[j];
                            int n=s.indexOf(':');
                            temp+=s.substring(0,n)+":";
                            key=s.substring(n+1)+":";
                            jump=Integer.parseInt(hash.get(key).toString());
                            temp+=String.valueOf(jump-start)+",";
                        }
                        data.setElementAt(temp.substring(0,temp.length()-1),i);
                    }
                }
            }
            removeLabel();
        }
    }

}
