package mpe;

import classfile.JavaOpcode;
import main.Main;
import util.Utils;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;


public class MPE implements CommandListener {

    public static final int MENU_WORKSPACE = -1;
    public static final int MENU_MAIN = 0;
    public static final int MENU_EDIT = 2;
    public static final int MENU_NAVI = 3;
    public static final int MENU_TEMPLATES = 4;
    public static final int MENU_CODE = 5;
    public static final int MENU_SEARCH = 6;
    public static final int MENU_REPLACE = 7;
    public static final int MENU_LINEED = 8;
    public static final int MENU_GOTO = 9;
    public static final int MENU_JVM = 10;
    public static final int MENU_OUT = 11;
    public static final int MENU_ABOUT = 12;
    public static final int MENU_INPUT = 13;
    public int menu;
    public Display display;
    public Workspace w;
    public List mainMenu;
    public LangResources lr;
    private TextField tfEdit1,  tfEdit2,  tfDocTemplate,  tfDocLine;
    public TextBox tbLineEditor;
    private ChoiceGroup cgSearchVar,  cgInterface,  cgFontFace,  cgFontSize,  cgLayout,  cgLineSeparator , flags ;
    private Command cmdBack,  cmdOk,  cmdSave,cmdCopy,cmdup;
    Main m;
    public boolean isSearch=true , isKey=false;
    public MPE(Main m, String str) {
        this.m=m;
        lr =m.lr;
        display = m.display;
        w = new Workspace(this,str.trim());
        w.doc.isShow=m.setting.isShow;
        cmdBack = new Command(lr.get("29"), Command.BACK, 99);
        cmdOk = new Command(lr.get("27"), Command.OK, 0);
        cmdSave = new Command(lr.get("8"), Command.OK, 0);

        showMenu(MENU_WORKSPACE);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == List.SELECT_COMMAND) {
            if (menu == MENU_MAIN) {
                switch (mainMenu.getSelectedIndex()) {
                    case 0:
                        Thread t=new Thread(m);
                        t.start();
                        try{ t.join();}catch( Exception e){ }
                        m.display.setCurrent(m.methodList);
                        w=null;
                        break;
                    case 1:
                        showMenu(MENU_EDIT);
                        break;
                    case 2:
                        showMenu(MENU_NAVI);
                        break;
                    case 3:
                        showMenu(MENU_TEMPLATES);
                        break;
                    case 4:
                        showMenu(MENU_ABOUT);
                        break;
                    case 5:
                        showMenu(MENU_CODE);
                        break;
                    case 6:
                        m.setting.isShow=w.doc.isShow;
                        m.display.setCurrent(m.methodList);
                        m.mpe=null;
                        w=null;
                        break;
                }
            }else if (menu == MENU_EDIT) {
                switch (mainMenu.getSelectedIndex()) {
                    case 0:
                        w.selec=false;
                        w.doc.startSelection();
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 1:
                        m.buffer = w.doc.Copy();
                        w.doc.endSelection();
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 2:
                        m.buffer = w.doc.Cut();
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 3:
                        w.doc.Insert(m.buffer);
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 4:
                        showMenu(MENU_SEARCH);
                        break;
                    case 5:
                        showMenu(MENU_REPLACE);
                        break;
                    case 6:
                        showMenu(MENU_LINEED);
                        break;
                }
            } else if (menu == MENU_NAVI) {
                switch (mainMenu.getSelectedIndex()) {
                    case 0:
                        w.doc.setCursor(0, 0);
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 1:
                        w.doc.setCursor(w.doc.data.elementAt(w.doc.data.size() - 1).toString().length(), w.doc.data.size() - 1);
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 2:
                        w.doc.setCursor(0, w.doc.getCursor(Constants.CUR_Y));
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 3:
                        w.doc.setCursor(w.doc.data.elementAt(w.doc.getCursor(Constants.CUR_Y)).toString().length(), w.doc.getCursor(Constants.CUR_Y));
                        showMenu(MENU_WORKSPACE);
                        break;
                    case 4:
                        showMenu(MENU_GOTO);
                        break;
                }
            }else if(menu==MENU_CODE){
                switch (mainMenu.getSelectedIndex()) {
                    case 0:
                        String ph=m.path.substring(0,m.path.lastIndexOf('.'))+"&"+m.methodIndex+".i";
                        save(ph);
                        break;
                    case 1:
                        
                        display.setCurrent(m.new FileBrowser());
                     
                }
            }
        } else if (c == cmdBack) {
            switch (menu) {
                case MENU_MAIN:
                    showMenu(MENU_WORKSPACE);
                    System.gc();
                    break;
                case MENU_EDIT:
                case MENU_CODE:
                case MENU_NAVI:
                case MENU_ABOUT:
                    showMenu(MENU_MAIN);
                    break;

                case MENU_LINEED:
                    tbLineEditor = null;
                    showMenu(MENU_WORKSPACE);
                    break;
                case MENU_SEARCH:
                case MENU_REPLACE:
                    tfEdit1 = null;
                    tfEdit2 = null;
                    tbLineEditor = null;
                    showMenu(MENU_EDIT);
                    break;
                case MENU_GOTO:
                    showMenu(MENU_NAVI);
                    break;
                case MENU_JVM:
showMenu(MENU_MAIN);
tfEdit1=null;
break;
                case MENU_TEMPLATES:
                    w.doc.isShow=flags.isSelected(0);
                    showMenu(MENU_MAIN);
                    break;
            }
        }else if(c==cmdCopy){
            if(menu==MENU_LINEED){
                int i=tbLineEditor.getCaretPosition();
                tbLineEditor.insert(m.buffer,i);
            }
        } else if (c == cmdOk) {
            Form f;
            switch (menu) {
                case MENU_ABOUT:
                    showMenu(MENU_JVM);
                    break;
                case MENU_SEARCH:
                    f = new Form(lr.get("15"));
                    f.append(lr.get("44"));
                    display.setCurrent(f);
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
                    m.setting.codeSearch=tfEdit1.getString();
                    showMenu(MENU_WORKSPACE);
                    break;
                case MENU_REPLACE:
                    f = new Form(lr.get("16"));
                    f.append(lr.get("45"));
                    display.setCurrent(f);
                    int j=0;
                    isSearch=false;
                    for (int i = cgSearchVar.getSelectedIndex() == 2 ? 0 : w.doc.getCursor(Constants.CUR_Y); i < w.doc.data.size() && i > -1 ;) {
                        if(tfEdit1.getString().length()==0 )
                            break;
                        if (w.doc.data.elementAt(i).toString().indexOf(tfEdit1.getString()) > -1 ) {
                            w.doc.data.setElementAt(Utils.replace(w.doc.data.elementAt(i).toString(), tfEdit1.getString(), tfEdit2.getString() ), i);
                             w.doc.setCursor(0, i);
                            isSearch=true;
                            j+=Utils.replaceCount;
                        }
                        if (cgSearchVar.getSelectedIndex() == 1) {
                            i--;
                        } else {
                            i++;
                        }
                    }
                    w.rcount=j;
                    m.setting.codeSearch=tfEdit1.getString();
                    m.setting.codeReplace = tfEdit2.getString();
                    showMenu(MENU_WORKSPACE);
                    break;
                case MENU_LINEED:
                    w.doc.data.setElementAt("", w.doc.getCursor(Constants.CUR_Y));
                    w.doc.setCursor(0, w.doc.getCursor(Constants.CUR_Y));
                    w.doc.Insert(tbLineEditor.getString());
                    showMenu(MENU_WORKSPACE);
                    break;
                case MENU_GOTO:
                    try{
                        int in = Integer.parseInt(tfEdit1.getString());
                        if (in >= 0 && in < w.doc.data.size()) {
                            w.doc.setCursor(0, in);
                        }
                    }catch(Exception e) { }
                    showMenu(MENU_WORKSPACE);
                    break;
case MENU_TEMPLATES:
                    try{
                        int n = Integer.parseInt(tfEdit1.getString());
                        w.gotoLabel(n);
                        m.setting.offset = tfEdit1.getString();
                    }catch(Exception e) { }
                    w.doc.isShow=flags.isSelected(0);
                    showMenu(MENU_WORKSPACE);
                    break;
                } 
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
        showMenu(MENU_WORKSPACE);
    }


    public void showMenu(int id) {
        menu = id;
        Form f;
        switch (id) {
            case MENU_WORKSPACE:
                display.setCurrent(w);
                break;
            case MENU_MAIN:
                mainMenu = new List(m.classFile.getMethodName(m.methodIndex), List.IMPLICIT);
                mainMenu.append(lr.get("0"), null);
                mainMenu.append(lr.get("1"), null);
                mainMenu.append(lr.get("2"), null);
                mainMenu.append(lr.get("3"), null);
                mainMenu.append(lr.get("5"), null);
                mainMenu.append(lr.get("4"), null);
                mainMenu.append(lr.get("6"), null);
                mainMenu.addCommand(cmdBack);
                mainMenu.setCommandListener(this);
                display.setCurrent(mainMenu);
                break;
            case MENU_EDIT:
                mainMenu = new List(lr.get("1"), List.IMPLICIT);
                mainMenu.append(lr.get("11"), null);
                mainMenu.append(lr.get("12"), null);
                mainMenu.append(lr.get("13"), null);
                mainMenu.append(lr.get("14"), null);
                mainMenu.append(lr.get("15"), null);
                mainMenu.append(lr.get("16"), null);
                mainMenu.append(lr.get("17"), null);
                mainMenu.addCommand(cmdBack);
                mainMenu.setCommandListener(this);
                display.setCurrent(mainMenu);
                break;
            case MENU_NAVI:
                mainMenu = new List(lr.get("2"), List.IMPLICIT);
                mainMenu.append(lr.get("18"), null);
                mainMenu.append(lr.get("19"), null);
                mainMenu.append(lr.get("20"), null);
                mainMenu.append(lr.get("21"), null);
                mainMenu.append(lr.get("30"), null);
                mainMenu.addCommand(cmdBack);
                mainMenu.setCommandListener(this);
                display.setCurrent(mainMenu);
                break;
            case MENU_TEMPLATES:
                 w.doc.labelLoad();
                f = new Form(lr.get("22"));
                tfEdit1 = new TextField( lr.get("87"),m.setting.offset, 10, TextField.NUMERIC);
                flags = new ChoiceGroup(lr.get("52"), Choice.MULTIPLE);
                flags.append(lr.get("53"),null);
                flags.setSelectedIndex(0,w.doc.isShow);
                f.append(tfEdit1);
                f.append(lr.get("88") + new Integer(w.doc.lab[w.doc.globalY()]).toString()+"\n");
                f.append(lr.get("89")+w.doc.lab[w.doc.lab.length-1]+"\n");
                f.append(flags);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_ABOUT:
                f = new Form(lr.get("90"));
                tfEdit1 = new TextField(lr.get("28"),m.setting.jvm, 30, 0);
                f.append(tfEdit1);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_SEARCH:
                f = new Form(lr.get("15"));
                tfEdit1 = new TextField(lr.get("36"), m.setting.codeSearch, 100, TextField.ANY);
                f.append(tfEdit1);
                cgSearchVar = new ChoiceGroup(lr.get("37"), ChoiceGroup.EXCLUSIVE);
                cgSearchVar.append(lr.get("38"), null);
                cgSearchVar.append(lr.get("39"), null);
                cgSearchVar.append(lr.get("40"), null);
                f.append(cgSearchVar);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_REPLACE:
                f = new Form(lr.get("16"));
                tfEdit1 = new TextField(lr.get("36"),m.setting.codeSearch, 100, TextField.ANY);
                f.append(tfEdit1);
                tfEdit2 = new TextField(lr.get("41"),m.setting.codeReplace , 100, TextField.ANY);
                f.append(tfEdit2);
                cgSearchVar = new ChoiceGroup(lr.get("38"), ChoiceGroup.EXCLUSIVE);
                cgSearchVar.append(lr.get("38"), null);
                cgSearchVar.append(lr.get("39"), null);
                cgSearchVar.append(lr.get("40"), null);
                f.append(cgSearchVar);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_LINEED:
                tbLineEditor = new TextBox(lr.get("42"), w.doc.data.elementAt(w.doc.getCursor(Constants.CUR_Y)).toString(), 5000, TextField.ANY);
                cmdCopy = new Command(lr.get("14"), Command.SCREEN, 0);
                tbLineEditor.addCommand(cmdOk);
                tbLineEditor.addCommand(cmdCopy);
                tbLineEditor.addCommand(cmdBack);
                tbLineEditor.setCommandListener(this);
                display.setCurrent(tbLineEditor);
                break;
            case MENU_GOTO:
                f = new Form(lr.get("30"));
                tfEdit1 = new TextField(lr.get("43") + " " + new Integer(w.doc.data.size()-1).toString() + ")","" , 10, TextField.NUMERIC);
                
                f.append(tfEdit1);
                f.addCommand(cmdOk);
                f.addCommand(cmdBack);
                f.setCommandListener(this);
                display.setCurrent(f);
                break;
            case MENU_CODE:
                mainMenu = new List(lr.get("62"), List.IMPLICIT);
                mainMenu.append(lr.get("50"),null);
                mainMenu.append(lr.get("51"),null);
                mainMenu.addCommand(cmdBack);
                mainMenu.setCommandListener(this);
                display.setCurrent(mainMenu);
                break;
            case MENU_JVM:
                    String jvm="";
                    try{
                        InputStream is=this.getClass().getResourceAsStream("/lang/jvm.data");
                        DataInputStream dis=new DataInputStream(is);
                        jvm=dis.readUTF();
                        is.close();
                        dis.close();
                    }catch(IOException e){}
                    String[] str=Utils.splitString(jvm,"\n");
                    jvm=null;
                    int jco= JavaOpcode.toCode(tfEdit1.getString());
                    f=new Form("JVM Instruction");
                    f.append(lr.get("7"));
                    f.append("\n"+str[jco]);
                    str=null;
                    f.addCommand(cmdBack);
                    f.setCommandListener(this);
                    m.setting.jvm=tfEdit1.getString();
                    display.setCurrent(f);
                    break;
        }
    }
    public String getString(){
        w.doc.setOffset();
        StringBuffer temp=new StringBuffer();
        for (int i = 0; i < w.doc.data.size(); i++) {             temp.append(w.doc.data.elementAt(i).toString());
           temp.append("\n");
        }
        return temp.toString().trim();
    }

    public String outCode(){
        StringBuffer temp=new StringBuffer();
        temp.append( "< ClassName: "+m.classFile.getClassName()+" >  <  MethodName: "+m.classFile.getMethodName(m.methodIndex)+" >  < MethodIndex: "+m.methodIndex+" >\n");
        for (int i = 0; i < w.doc.data.size(); i++) {             temp.append(w.doc.data.elementAt(i).toString());
           temp.append("\n");
        }
        return temp.toString().trim();
    }


    public void open(final String fn) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    FileConnection fconn = (FileConnection) Connector.open("file://" + fn);
                        InputStream is = fconn.openInputStream();
                        long l = fconn.fileSize();
                        if (l < Integer.MAX_VALUE) {
                            byte[] b = new byte[(int) l ];
                            is.read(b);
                            String s = Utils.byteArrayToString(b,"UTF-8");
                            b = null;
                            w.addDocument(s);
                        }
                        is.close();
                        fconn.close();
                    } catch (Exception e) {
                    }
                w.repaint();
                display.setCurrent(w);
            }
        }).start();
    }

    public void save(final String fn) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    FileConnection fconn = (FileConnection) Connector.open("file://" + fn);
                    if ( ! fconn.exists())
                        fconn.create();
                    OutputStream os = fconn.openOutputStream();
                    os.write(Utils.encodeUTF8(outCode()));
                    os.close();
                    fconn.close();
                } catch (Exception ex) {
                }
            display.setCurrent(w);
            }
        }).start();
    }

}
