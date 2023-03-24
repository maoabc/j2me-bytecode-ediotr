package constedit;

import main.Main;
import mpe.Constants;
import util.URLEncode;
import util.Utils;

import java.io.*;
import java.util.Vector;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;

public final class ConstantEditor implements CommandListener{
    public static final int MENU_GOTO = -1;
    public static final int MENU_LINEE = 0;
    public static final int MENU_SEARCH = 1;
    public static final int MENU_REPLACE = 3;
    public static final int MENU_WORK = 4;
    public static final int MENU_GOOGLE = 5;
    String buf="";
    int menu;
    TextField tfEdit1,tfEdit2;
    TextBox tb;
    private ChoiceGroup cgSearchVar;
    Command cmdBack = new Command("返回", Command.BACK, 99);
    Command cmdOk = new Command("确定", Command.OK, 0);
    Command cmdk= new Command("清空", Command.SCREEN, 0);
    private Display display;
    private Work w;
    Main main;
    String temp="";
    public boolean isSearch=true ,isKey=false;
    public ConstantEditor(Main main, Vector v){
        this.main=main;
        display=main.display;
        
        w=new Work(this,v);
        showMenu(MENU_WORK);
    }
    private void editor(int index , String str){
            main.isSave=false;
            if(temp.equals("Utf8_Info:" ) ){
                main.classFile.setConstantUtf8(w.doc.lab[index] , str ) ;
                w.doc.data.setElementAt(temp+ " " + str, index);
            }else if(temp.equals("Integer_Info:" ) ){
                try{
                    int val =Integer.parseInt( str ) ;
                    main.classFile.setConstantInt(w.doc.lab[index] , val ) ;
                    w.doc.data.setElementAt(temp+ " " + str, index);
                }catch(Exception e ) { }
            }else if(temp.equals("Float_Info:" ) ){
                try{
                    float f =Float.parseFloat( str ) ;
                    main.classFile.setConstantFloat(w.doc.lab[index] , f ) ;
                    w.doc.data.setElementAt(temp+ " " + str, index);
                }catch(Exception e ) { }
            }else if(temp.equals("Long_Info:" ) ){
                try{
                   long l =Long.parseLong( str ) ;
                    main.classFile.setConstantLong(w.doc.lab[index] , l ) ;
                    w.doc.data.setElementAt(temp+ " " + str, index);
                }catch(Exception e ) { }
            }else if(temp.equals("Double_Info:" ) ){
                try{
                    double dou =Double.parseDouble ( str ) ;
                    main.classFile.setConstantDouble(w.doc.lab[index] , dou ) ;
                    w.doc.data.setElementAt(temp+ " " + str, index);
                }catch(Exception e ) { }
            }
    }

    public void search(int pos , String str){
        isSearch=false;
        isKey=false;
        for (int i =  pos; i < w.doc.data.size() ; i++) {
            if (w.doc.data.elementAt(i).toString().toLowerCase().indexOf(str.toLowerCase()) > -1) {
                isSearch=true;
                isKey=true;
                w.doc.setCursor(0, i);
                break;
            }
        }
        showMenu(MENU_WORK);
    }

    public void commandAction(Command c, Displayable d){
        if( c == cmdOk ){
            switch(menu){
                case MENU_LINEE:
                    editor(w.doc.globalY(), tb.getString() );
                    w.doc.setCursor(w.doc.data.elementAt(w.doc.globalY()).toString().length(), w.doc.globalY());
                    showMenu(MENU_WORK );
                    tb=null;
                    break;
                case MENU_GOOGLE:
                    new Google( tfEdit1.getString() );
                    main.setting.google=tfEdit1.getString();
                    break;
                case MENU_REPLACE:
                    int j=0;
                    isSearch=false;
                    for (int i = cgSearchVar.getSelectedIndex() == 2 ? 0 : w.doc.getCursor(Constants.CUR_Y); i < w.doc.data.size() && i > -1 ; ) {
                        if(tfEdit1.getString().length() ==0 )
                            break;
                        String s=w.doc.data.elementAt(i).toString();
                        String st = s.substring(s.indexOf(" ")+1);
                        if (st.indexOf(tfEdit1.getString()) > -1 ) {
                            isSearch=true;
                            temp=s.substring(0 , s.indexOf(" ") );
                            editor( i , Utils.replace( st , tfEdit1.getString() , tfEdit2.getString() ) );
                             j+=Utils.replaceCount;
                             w.doc.setCursor(0, i);
                        }
                        if (cgSearchVar.getSelectedIndex() == 1) {
                            i--;
                        } else {
                            i++;
                        }
                    }
                    w.count=j;
                    main.setting.codeSearch=tfEdit1.getString();
                    main.setting.codeReplace = tfEdit2.getString();
            
                    showMenu(MENU_WORK);
                    tfEdit1=null;
                    tfEdit2=null;
                    break;
                case MENU_SEARCH:
                    isSearch=false;
                    isKey=false;
                    for (int i = cgSearchVar.getSelectedIndex() == 2 ? 0 : w.doc.getCursor(Constants.CUR_Y); i < w.doc.data.size() && i > -1 ; i = cgSearchVar.getSelectedIndex() == 1 ? i - 1 : i + 1) {
                        if(tfEdit1.getString().length()==0)
                           break;
                        if (w.doc.data.elementAt(i).toString().toLowerCase().indexOf(tfEdit1.getString().toLowerCase()) > -1) {
                            isSearch=true;
                            isKey=true;
                            w.doc.setCursor(0, i);
                            break;
                        }
                    }
                    main.setting.codeSearch = tfEdit1.getString();
                    showMenu(MENU_WORK);
                    tfEdit1=null;
                    break;
                case MENU_GOTO:
                    try{
                        int in = Integer.parseInt(tfEdit1.getString());
                        if (in >= 0 && in < w.doc.data.size()) {
                            w.doc.setCursor(0, in);
                        }
                        main.setting.gotoLine=tfEdit1.getString();
                    }catch(Exception e) { }
                    showMenu(MENU_WORK);
                tfEdit1=null;
                    break;
            }
        }else if ( c== cmdBack ){
            showMenu(MENU_WORK);
                tfEdit1=null;
                tfEdit2=null;
                tb=null;
        }else if ( c== cmdk ){
            tfEdit1.setString("");
        }
    }


    public void showMenu(int id) {
        menu = id;
        Form f ;
        switch (id) {
            case MENU_WORK:
                display.setCurrent(w);
                break;
            case MENU_REPLACE:
                f = new Form("替换");
                tfEdit1 = new TextField("搜索字符",main.setting.codeSearch, 100, TextField.ANY);
                f.append(tfEdit1);
                tfEdit2 = new TextField("替换字符",main.setting.codeReplace, 100, TextField.ANY);
                f.append(tfEdit2);
                cgSearchVar = new ChoiceGroup("方式", ChoiceGroup.EXCLUSIVE);
                cgSearchVar.append("向下", null);
                cgSearchVar.append("向上", null);
                cgSearchVar.append("从开始处", null);
                f.append(cgSearchVar);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_SEARCH:
                f = new Form("搜索");
                tfEdit1 = new TextField("搜索字符",main.setting.codeSearch , 100, TextField.ANY);
                f.append(tfEdit1);
                cgSearchVar = new ChoiceGroup("方式", ChoiceGroup.EXCLUSIVE);
                cgSearchVar.append("向下", null);
                cgSearchVar.append("向上", null);
                cgSearchVar.append("从开始处", null);
                f.append(cgSearchVar);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_GOTO:
                f = new Form("跳转");
                tfEdit1 = new TextField("最大行(" + new Integer(w.doc.data.size()-1).toString() + ")", "", 10, TextField.NUMERIC);
                
                f.append(tfEdit1);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_GOOGLE:

                f = new Form("谷歌翻译");
                String tra=w.doc.data.elementAt(w.doc.globalY() ).toString();
                tfEdit1 = new TextField("翻译:" , ( tra.startsWith("Utf8_Info:") ? tra.substring( tra.indexOf(' ')+1) :  main.setting.google ) , 100000, 0);
                f.append(tfEdit1);
                cgSearchVar = new ChoiceGroup("语言", ChoiceGroup.EXCLUSIVE);
                cgSearchVar.append("英文＞中文", null);
                cgSearchVar.append("中文＞英文", null);
                cgSearchVar.append("俄文＞中文", null);
                f.append(cgSearchVar);
                f.addCommand(cmdOk);
                f.addCommand(cmdk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_LINEE:
                String s=w.doc.data.elementAt(w.doc.globalY() ).toString();
                int i=s.indexOf(" ");
                temp = s.substring(0,i);
                if(temp.equals("Utf8_Info:" ) )
                    tb = new TextBox("编辑行", s.substring(i+1) , 50000, TextField.ANY);
                else if( temp.equals("Integer_Info:" ) || temp.equals("Long_Info:" ) )
                    tb = new TextBox("编辑行", s.substring(i+1) , 5000, 2 );
                else
                    tb = new TextBox("编辑行", s.substring(i+1) , 5000, 5 );
                tb.addCommand(cmdOk);
                tb.addCommand(cmdBack);
                tb.setCommandListener(this);
                display.setCurrent(tb);
                break;
        }
    }

class Google extends Form implements Runnable,CommandListener{
    String translate;
    TextBox tb;
    Command ok = new Command("确定", Command.OK, 1);
    Command back = new Command("返回", Command.BACK, 1);
    Google(String str){
        super("Google");
        translate=str;
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
        display.setCurrent(this);
        new Thread(this).start();
    }
    
    public void commandAction(Command c,Displayable d){
        if(c==back){
            showMenu(MENU_GOOGLE);
            buf="";
        }else if(c==ok){
            tb=new TextBox("",buf,5000,0);
            tb.addCommand(back);
            tb.setCommandListener(this);
            display.setCurrent(tb );
        }
    }

    public void run(){
        setTitle("翻译中…");
        translate(translate);
        setTitle("结果");
    }
    
    private void translate(String str){
        try{
            HttpConnection hc=(HttpConnection)Connector.open("http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&q="+ URLEncode.encode(str)+(cgSearchVar.getSelectedIndex()==0 ? "&langpair=en%7czh-CN" : (cgSearchVar.getSelectedIndex()==1? "&langpair=zh-CN%7cen" : "&langpair=ru%7czh-CN" ) )  );
            hc.setRequestMethod(HttpConnection.GET);
            InputStream is=hc.openInputStream();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
int i;
            while((i=is.read())!=-1)
                baos.write(i);
            byte[] b=baos.toByteArray();
            String s=new String(b,"UTF-8");
            if(s.indexOf("translatedText") != -1 ){
                int index=s.indexOf("},",36);
                buf = s.substring(36,index-1);
                append(str);
                append("\n翻译为:");
            }
            append("\n   "+buf);
            is.close();
            baos.close();
        }catch(Exception e){}
    }
}



}