package main;

import classfile.*;
import constedit.ConstantEditor;
import io.ArrayOutputStream;
import io.BufferedInputStream;
import mpe.LangResources;
import mpe.MPE;

import java.io.*;
import java.util.Enumeration;
import javax.microedition.io.file.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;


public class Main extends MIDlet implements CommandListener,Runnable {
    private AttributeInfo.CodeAttribute.ExceptionEntry[] ace;
    public LangResources lr=new LangResources("/lang/zh-cn.lang");
    public List methodList=new List(lr.get("60"),List.IMPLICIT);
    public List fieldList=new List(lr.get("61"),List.IMPLICIT);
    public List mainList;
    private Form sear,sm,about,cp;
    TextField ptf,stf,mtf;
    public Display display=Display.getDisplay(this);
    private Command code=new Command(lr.get("62"),Command.OK,0);
    private Command ok=new Command(lr.get("27"),Command.OK,0); 
    private Command exception=new Command(lr.get("64"),Command.SCREEN,0);
    private Command max=new Command(lr.get("65"),Command.SCREEN,0);
    private Command add,add0,remove,remove0,cmdAbout,aback;
    private Command mainabout=new Command(lr.get("66"),Command.SCREEN,0);
    private Command save=new Command(lr.get("67"),Command.SCREEN,0);
    private Command back=new Command(lr.get("29"),Command.BACK,0);
    private Command desc=new Command(lr.get("63"),Command.SCREEN,0);
    private Alert alert;
    public static final String[] imagNames={"/res/back.png","/res/disk.png","/res/dir.png","/res/class.png","/res/field.png","/res/method.png","/res/other.png","/res/inter.png","/res/zip.png"};
    Image[] image;
    private Command desc0;
    public String path="";
    String currDirName="" ;
    public String buffer = "";
    FileBrowser file;
    public MPE mpe;
    Search mysc;
    ZipSearch zip;
    public boolean isSearchM, isSearchDir, isSave=true;
    SearchMethod searchMethod;
    public ClassFile classFile;
    public Setting setting;
    public int methodIndex;
    int aCount;
    int fileSize;
    int fieldIndex;
    public Main() {
                    JavaOpcode.initOpcode();
                    image=new Image[imagNames.length];
                    for(int i=0;i<imagNames.length;i++)
                        image[i]=getImage(imagNames[i]);
                    mainList=new List("Main",List.IMPLICIT,new String[]{"常量池","字段","方法","搜索"},new Image[]{image[3], image[4],image[5],image[6]});
        setting=SettingsManager.getSetting() ;
        currDirName=setting.path;
        file=new FileBrowser();
        add=new Command(lr.get("68"),Command.SCREEN,0);
        remove=new Command(lr.get("69"),Command.SCREEN,0);
        mainList.setSelectCommand(ok);
        mainList.addCommand(save);
        mainList.addCommand(mainabout);
        mainList.addCommand(back);
        mainList.setCommandListener(this);

                    methodList.setSelectCommand(code);
                    methodList.addCommand(exception);
                    methodList.addCommand(max);
methodList.addCommand(desc);
                    methodList.addCommand(add);
                    methodList.addCommand(remove);
                    methodList.addCommand(back);
                    methodList.addCommand(save);
                    methodList.setCommandListener(this);

                    add0=new Command(lr.get("70"),Command.SCREEN,0);
                    remove0=new Command(lr.get("71"),Command.SCREEN,0);
                   desc0=new Command(lr.get("63"),Command.OK,0);
                    fieldList.setSelectCommand(desc0);
                    fieldList.addCommand(add0);
                    fieldList.addCommand(remove0);
                    fieldList.addCommand(back);
                    fieldList.setCommandListener(this);
    }
    private void showAlert(){
        alert=new Alert("",lr.get("72"),null,AlertType.INFO);
        aback=new Command(lr.get("6"),Command.BACK,0);
        alert.addCommand(ok);
        alert.addCommand(aback);
        alert.setCommandListener(this);
        display.setCurrent(alert);
    }
    private Image getImage(String str){
        Image image=null;
        try{
            image=Image.createImage(str);
        }catch(OutOfMemoryError out){
            out.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
    return image;
    }
    public void showAbout(){
        if(aCount<Constant.msg.length){
            about=new Form(Constant.title[aCount]);
            cmdAbout=new Command(lr.get("73"),Command.OK,0);
            about.append(Constant.msg[aCount++]);
            about.addCommand(cmdAbout);
            about.addCommand(back);
            about.setCommandListener(this);
            display.setCurrent(about);
        }
     }


    public void startApp() throws MIDletStateChangeException
    {
        display.setCurrent(file);
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean arg0){
        setting.path=currDirName;
        SettingsManager.saveSetting(setting) ;
    }
    public void commandAction(Command c, Displayable d)
    {
        if(d==mainList){
            if(c==ok){
                int i=mainList.getSelectedIndex();
                if(i==0)
                    new ConstantEditor(this, classFile.getConstantPoolElements() ) ;
                else if(i==1){
                    updateField();
                    display.setCurrent(fieldList);
                }else if(i==2){
                    updateMethod();
                    display.setCurrent(methodList);
                }else if(i==3){
                    sm=new Form(lr.get("74") );
                    mtf=new TextField(lr.get("36"),setting.searchMethod,500,0);
                    sm.append(mtf);
                    sm.addCommand(ok);
                    sm.addCommand(back);
                    sm.setCommandListener(this);
                    display.setCurrent(sm);
                }
            }else if(c==back){
                if(isSave){
                    if( ! isSearchDir){
                        methodList.deleteAll();
                        isSearchM=false;
                        display.setCurrent(file);
                    }else{
                        isSearchM=false;
                        display.setCurrent(mysc);
                    }
                }else{
                    showAlert();
                }
            }else if(c==save){
            isSearchM=false;
            isSave=true;
            methodList.deleteAll();
            writeFile(path);
            if( ! isSearchDir){
            display.setCurrent(file);
            }else{
                display.setCurrent(mysc);
                }
            }else if(c==mainabout){
                showAbout();
            }
        }else if(d==fieldList){
            if(c==desc0){
                fieldIndex=fieldList.getSelectedIndex();
                FieldEditor fe=new FieldEditor(classFile.getFieldFlags(fieldIndex),classFile.getFieldName(fieldIndex),classFile.getFieldDescriptor(fieldIndex));
                display.setCurrent(fe);   
            }else if(c==add0){
                FieldEditor fe=new FieldEditor();
                display.setCurrent(fe);
            }else if(c==remove0){
                isSave=false;
                fieldIndex=fieldList.getSelectedIndex();
                classFile.removeField(fieldIndex);
                updateField();
            }else if(c==back){
                display.setCurrent(mainList);
            }
        }else if(d==methodList){
        if(c==save)
        {
            isSearchM=false;
            isSave=true;
            methodList.deleteAll();
            writeFile(path);
            if( ! isSearchDir){
                display.setCurrent(file);
            }else{
                display.setCurrent(mysc);
            }
        }else if(c==back)

        {
            methodList.deleteAll();
            isSearchM=false;
            display.setCurrent(mainList);
        }else if(c==code)
       {
            if(!isSearchM)
                methodIndex=methodList.getSelectedIndex();
            else
                methodIndex=searchMethod.getIndex(methodList.getSelectedIndex());
            if(methodIndex != -1){
                int[] code=classFile.getMethodCode(methodIndex);
                if( code != null ){
                    Disasm ds=new Disasm(classFile);
                    String asmCode=ds.disasm(code);
                    mpe=new MPE(this,asmCode);
                }
            }
        }else if(c==exception)
        {
            loadException();
        }else if(c==desc){
            if(!isSearchM)
                methodIndex=methodList.getSelectedIndex();
            else
                methodIndex=searchMethod.getIndex(methodList.getSelectedIndex());
            if(methodIndex != -1){
                MethodEditor me=new MethodEditor(classFile.getMethodFlags(methodIndex),classFile.getMethodName(methodIndex),classFile.getMethodDescriptor(methodIndex));
                display.setCurrent(me);
            }
        }else if(c==remove){
            if(!isSearchM)
                methodIndex=methodList.getSelectedIndex();
            else
                methodIndex=searchMethod.getIndex(methodList.getSelectedIndex());
            if(methodIndex != -1 && ! isSearchM){
                classFile.removeMethod(methodIndex);
                isSave=false;
                updateMethod();
            }
         }else if(c==add){
            if(methodIndex != -1  && ! isSearchM){
                MethodEditor me=new MethodEditor();
                display.setCurrent(me);
            }
        }else if(c==max){
            if(!isSearchM)
                methodIndex=methodList.getSelectedIndex();
            else
                methodIndex=searchMethod.getIndex(methodList.getSelectedIndex());
            if(methodIndex != -1){
                int maxs=classFile.getMethodMaxStack(methodIndex);
                int maxl=classFile.getMethodMaxLocals(methodIndex);
                display.setCurrent(new MaxEditor(maxs,maxl));
            }
        }

        }else if(d==sear){
                if(c==back){
                    ptf=null;
                    stf=null;
                    sear=null;
                    display.setCurrent(file);
                }else if(c==ok){
                    if(ptf.getString().toLowerCase().endsWith(".zip") ){
                        zip=new ZipSearch(ptf.getString(), stf.getString() );
                        setting.search=stf.getString();
                        zip.addCommand(back);
                        zip.setCommandListener(this);
                        display.setCurrent(zip);
                    }else{
                        isSearchDir=true;
                        mysc=new Search( ptf.getString(), stf.getString(),image[3]);
                        setting.search=stf.getString();
                        mysc.setSelectCommand(ok);
                        mysc.addCommand(back);
                        mysc.setCommandListener(this);
                        display.setCurrent(mysc);
                        stf=null;
                    }
                }
            }else if(d==mysc){
                if(c==back){
                    isSearchDir=false;
                    mysc.isBreak=true;
                    display.setCurrent(file);
                    ptf=null;
                    sear=null;
                    mysc=null;
                }else if(c==ok){
                    if(mysc.getSelectedIndex() != -1){
                        path=ptf.getString()+mysc.getString(mysc.getSelectedIndex());
                        open(path);
                    }
                }
            }else if(d==sm){
                if(c==ok){
                    isSearchM=true;
                    searchMethod=new SearchMethod(this,mtf.getString());
                    setting.searchMethod=mtf.getString();
                    mtf=null;
                    sm=null;
                    methodList.setTitle(lr.get("44"));
                    display.setCurrent(methodList);
                }else if(c==back){
                    isSearchM=false;
                    display.setCurrent(mainList);
                    mtf=null;
                    sm=null;
                    }
            }else if(d==alert){
                if(c==ok){
                    isSave=true;
                    isSearchM=false;
                    methodList.deleteAll();
                    writeFile(path);
                    if( ! isSearchDir){
                        display.setCurrent(file);
                    }else{
                        display.setCurrent(mysc);
                    }
                    alert=null;
                    aback=null;
                }else if(c==aback){
                    isSave=true;
                    alert=null;
                    aback=null;
                    if( ! isSearchDir){
                        display.setCurrent(file);
                    }else{
                        display.setCurrent(mysc);
                    }
                }
            }else if(d==about){
                if(c==cmdAbout){
                    showAbout();
                }else if(c==back){
                    aCount=0;
                    cmdAbout=null;
                    about=null;
                    display.setCurrent(mainList);
                }
            }else if(d==zip){
                if(c==back){
                    zip.isBreak=true;
                    file.showCurrDir();
                    display.setCurrent(file);
                    zip=null;
                }
            }else if(d == cp ){
                if( c==back )
                    display.setCurrent(mainList);
            }
    }
    public void loadException(){
        if(!isSearchM)
            methodIndex=methodList.getSelectedIndex();
        else
            methodIndex=searchMethod.getIndex(methodList.getSelectedIndex());
        if(methodIndex != -1){
            Except excep = new Except();
            ace=classFile.getMethodExceptionTable(methodIndex);
            if(ace != null){
                for(int i=0;i<ace.length;i++)
                    excep.append( ( ace[i].catchType>0 ? classFile.getClassInfo(ace[i].catchType) : "all" ), null  );
            }
            display.setCurrent(excep);
        }
    }
    public void run()
    {
        Code cd=new Code(classFile);
        try
        {
            cd.load(mpe.getString());
        }catch(IOException e)
        {
            display.setCurrent(new Alert("","erorr: "+cd.count,null,AlertType.ERROR));
        }
        mpe=null;
        isSave=false;
        int[] code=cd.getCode();
        classFile.setMethodCode(methodIndex,code);
    }

    public final void open(final String path)
    {
        classFile=new ClassFile();
        new Thread(new Runnable(){
            public void run(){
                InputStream is=null;
                FileConnection fc=null;
                try{
    //                long s=System.currentTimeMillis();
                    fc = (FileConnection) Connector.open("file://"+path);
                    fileSize=(int) fc.fileSize();
                    is=fc.openInputStream();
                    DataInputStream dis=new DataInputStream(new BufferedInputStream(is,16*1024) );
                    classFile.read(dis);
                    is.close();
                    fc.close();
                    dis.close();
                    mainList.setTitle(path.substring(path.lastIndexOf('/')+1) );
  //                  mainList.setTitle(""+(System.currentTimeMillis() -s ) );
                    display.setCurrent(mainList);
                }catch(Exception e){
                    display.setCurrent(new Alert("",e.getMessage(),null,AlertType.ERROR));
                }finally{
                    try{
                        fc.close();
                        is.close();
                    }catch(Exception e){}
                }
            }
        }).start();
    }
   private void updateMethod(){
       Thread t = new Thread(new Runnable(){
           public void run(){
               methodList.setTitle(classFile.getClassName() );
               methodList.deleteAll();
               String[] str=classFile.getMethodNames();
               for(int i=0;i<str.length;i++)
                   methodList.append(str[i],image[5]);
           }
       });
        t.start();
        try{ t.join(); }catch(Exception e) {}
   }
   private void updateField(){
       Thread t = new Thread(new Runnable(){
           public void run(){
               fieldList.deleteAll();
               String[] str=classFile.getFieldsNames();
               for(int i=0;i<str.length;i++)
                   fieldList.append(str[i],image[4]);
           }
       });
        t.start();
        try{ t.join(); }catch(Exception e) {}
   }
  

    public final void writeFile(final  String path)
    {
        new Thread(new Runnable(){
            public void run(){
                FileConnection fc=null;
                OutputStream os=null;
                try{
//                    long s=System.currentTimeMillis(); 
                    fc = (FileConnection) Connector.open("file://"+path);
                    ArrayOutputStream aos=new ArrayOutputStream(16*1024);
                    DataOutputStream dos=new DataOutputStream( aos );
                    classFile.write(dos);
                    dos.flush();
                    if(!fc.exists())
                        fc.create();
                    else{
                       if(aos.size() < fileSize ){
                           fc.delete();
                           fc.create();
                       }
                    }
                    os=fc.openOutputStream();
                    os.write(aos.getBufArray(), 0, aos.size() );
                    os.close();
                    dos.close();
                    fc.close();
                    classFile=null;
                    display.setCurrent( new Alert(null,lr.get("75")+path,null,AlertType.CONFIRMATION) );
//                    display.setCurrent( new Alert(null,""+(System.currentTimeMillis() - s),null,AlertType.CONFIRMATION) );
                }catch(Exception e){
                    display.setCurrent( new Alert(null,e.getMessage(),null,AlertType.ERROR) );
                }finally{
                    try{
                        os.close();
                        fc.close();
                    }catch(Exception e){}
                }
            }
        }).start();
    }    
    
    public class FileBrowser extends List implements CommandListener, Runnable {
        private Enumeration files;
        
        private Command exit   = new Command(lr.get("6"),   Command.EXIT, 1);
        private Command up     = new Command(lr.get("29"), Command.BACK, 0);
        private Command open   = new Command(lr.get("27"),     Command.OK, 0);
        private Command sc     = new Command(lr.get("74"), Command.SCREEN, 0);
        String temp="";
        
        public FileBrowser()
        {
            super(lr.get("76"), List.IMPLICIT);
            setSelectCommand(open);
            if(mpe==null)
                addCommand(sc);
            temp=currDirName;
            addCommand(exit);
            addCommand(up);
            setCommandListener(this);
            showCurrDir();
        }

        public void run() {
            try
            {
                deleteAll();
                if ("/".equals(currDirName))
                {
                    files = FileSystemRegistry.listRoots();
                    while (files.hasMoreElements())
                    {
                        String s=(String)files.nextElement();
                        if(s.equals("E:/") || s.equals("C:/"))
                            append(s, image[1]);
                        else
                            append(s,image[2]);
                    }
                } else 
                {
                    FileConnection fc = (FileConnection) Connector.open("file:///"+currDirName,Connector.READ);
                files = fc.list();
                fc.close();
                append("..", image[0]);
                while (files.hasMoreElements())
                    {
                        String s = (String)files.nextElement();
                        if (s.endsWith("/"))
                            append(s, image[2]);
                        else if(mpe==null && s.endsWith(".class") )
                            append(s, image[3]);
                        else if(mpe !=null && s.endsWith(".i"))
                            append(s, image[7]);
                        else if(mpe==null && s.toLowerCase().endsWith(".zip"))
                            append(s,image[8]);
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        private void traverseDirectory(String s) {
            if (currDirName.equals("/") && ! s.equals(".."))
            {
                currDirName = s;
            } else if (s.equals(".."))
            {
                int i = currDirName.lastIndexOf('/', currDirName.length() - 2);
                if (i != -1)
                {
                    currDirName = currDirName.substring(0, i + 1);
                } else
                {
                    currDirName = "/";
                }
            } else
            {
                currDirName = currDirName + s;
            }
        }
        private void showCurrDir() {
            Thread t = new Thread(this);
            t.start();
            try { t.join(); } catch (Exception e) {}
        }

        public void commandAction(Command c, Displayable d) {
            if (c.equals(open)) {
                if(getSelectedString().endsWith("/")||getSelectedString().endsWith(".."))
                {
                    traverseDirectory(getSelectedString());
                    showCurrDir();
                }else if(mpe==null  && getSelectedString().endsWith(".class") ){
                    path = "/"+currDirName + getSelectedString();
                    open(path);
                }else if(mpe==null  && getSelectedString().toLowerCase().endsWith(".zip") ){
                    sc();
                    path = "/"+currDirName + getSelectedString();
                    ptf=new TextField(lr.get("77"),path,128,0);
                    stf=new TextField(lr.get("36"),setting.search,50000,0);
                    sear.append(ptf);
                    sear.append(stf);
                    display.setCurrent(sear);
                }else if(mpe != null && getSelectedString().endsWith(".i") ){
                    String ph = "/"+currDirName + getSelectedString();
                    currDirName=temp;
                    mpe.open(ph);
                }
            }else if(c.equals(exit))
            {
                if(mpe==null){
                   destroyApp(true);
                   notifyDestroyed();
                }else{
                   currDirName=temp;
                   mpe.display.setCurrent(mpe.w);
                }
            }else if(c.equals(up)){
                traverseDirectory("..");
                showCurrDir();
            }else if(c.equals(sc)){
                sc();
                    path = "/"+currDirName + getSelectedString();
                if(path.endsWith("/") || path.toLowerCase().endsWith(".zip") ){
                    ptf=new TextField(lr.get("77"),path,128,0);
                }else{
                ptf=new TextField(lr.get("77"),path.substring(0,path.lastIndexOf('/')+1),128,0);
                }
                stf=new TextField(lr.get("36"),setting.search,50000,0);
                sear.append(ptf);
                sear.append(stf);
                display.setCurrent(sear);
            }   
        }

        private String getSelectedString() {
            return getString(getSelectedIndex());
        }
    }

private class Except extends List implements CommandListener{
    private Command ok=new Command(lr.get("78"),Command.OK,0);
    private Command addException=new Command(lr.get("79"),Command.SCREEN,0);

    private Command remove=new Command(lr.get("80"),Command.SCREEN,0);
    private Command back=new Command(lr.get("29"),Command.BACK,0);


    int index=-1;
    public Except()
    {
        super(lr.get("81"),List.IMPLICIT);
        setSelectCommand(ok);
        addCommand(addException);
        addCommand(remove);
        addCommand(back);
        setCommandListener(this);
    }
    public void commandAction(Command c,Displayable d)
    {
        if(c.equals(ok))
        {
            index=getSelectedIndex();
        if(index != -1)
            display.setCurrent(new ExceptionForm(true));
        }else if(c.equals(back))
        {
            display.setCurrent(methodList);
        }else if(c.equals(addException)){
            display.setCurrent(new ExceptionForm(false));
        }else if(c.equals(remove)){
            index=getSelectedIndex();
        if(index != -1){
            isSave=false;
            classFile.removeMethodExceptionTable(methodIndex,index);
            loadException();
            }
        }
    }
private class ExceptionForm extends Form implements
CommandListener{
    Command ok,back;
    public TextField start,end,hand,catchType;
    boolean b;
    public ExceptionForm(boolean b)
    {
        super(lr.get("81"));
        ok=new Command(lr.get("27"),Command.OK,0);
        back=new Command(lr.get("29"),Command.BACK,0);
        this.b=b;
        if(this.b){
        start=new TextField("startPC",ace[index].startPC+"",50,2);
        end=new TextField("endPC",ace[index].endPC+"",50,2);
        hand=new TextField("handlerPC",ace[index].handlerPC+"",50,2);
        catchType=new TextField("catchType",getString(index),500, (getString(index).equals("all") ?0x20000 : 4) );
        }else{
        start=new TextField("startPC","",50,2);
        end=new TextField("endPC","",50,2);
        hand=new TextField("handlerPC","",50,2);
        catchType=new TextField("catchType","java/lang/Exception",500,4);
        }
        append(start);
        append(end);
        append(hand);
        append(catchType);
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
    }
    public void commandAction(Command c,Displayable d)
    {
        if(c.equals(back))
        {
            loadException();
        }else if(c.equals(ok)&&b){
            try{
                ace[index].startPC=Integer.parseInt(start.getString());
                ace[index].endPC=Integer.parseInt(end.getString());
                ace[index].handlerPC=Integer.parseInt(hand.getString());
                ace[index].catchType=(catchType.getString().equals("all") ? ace[index].catchType : classFile.addToConstantPool(new Clazz(catchType.getString())) );
                isSave=false;
            }catch(Exception e){}
            loadException();
        }else if(c.equals(ok) && !b){
            try{
                AttributeInfo.CodeAttribute.ExceptionEntry exc=new AttributeInfo.CodeAttribute.ExceptionEntry(Integer.parseInt(start.getString()),Integer.parseInt(end.getString()),Integer.parseInt(hand.getString()),classFile.addToConstantPool(new Clazz(catchType.getString())));
                classFile.addMethodExceptionTable(methodIndex,exc);
                isSave=false;
            }catch(Exception e){}
            loadException();
        }
    }
}
}
public void sc(){
sear=new Form("搜索");
                sear.addCommand(ok);
                sear.addCommand(back);
                sear.setCommandListener(this);
}

class FieldEditor extends Form implements CommandListener{
    ChoiceGroup flags;
    private Command back=new Command(lr.get("29"),Command.BACK,0);
    private Command ok=new Command(lr.get("27"),Command.OK,0);
    TextField tn,td;
    boolean isadd;
    FieldEditor(boolean[] b,String name,String desc){
        super(lr.get("83"));
isadd=false;
        flags= new ChoiceGroup("AccessFlags", Choice.MULTIPLE, new String[]{"public", "private", "protected", "static","final","volatile","transient"}, null);
        for(int i=0;i<b.length;i++)
            flags.setSelectedIndex(i,b[i]);
        tn=new TextField("Name",name,500,0);
        td=new TextField("Descriptor",desc,500,0);
        append(flags);
        append(tn);
        append(td);
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
    }
    FieldEditor(){
        super(lr.get("70"));
        isadd=true;
        flags= new ChoiceGroup("AccessFlags", Choice.MULTIPLE, new String[]{"public", "private", "protected", "static","final","volatile","transient"}, null);
        for(int i=0;i<flags.size();i++)
            flags.setSelectedIndex(i,false);
        tn=new TextField("Name","myField",500,0);
        td=new TextField("Descriptor","Ljava/lang/String;",500,0);
        append(flags);
        append(tn);
        append(td);
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
    }

    public int getFlags(){
        boolean[] b=new boolean[flags.size()];
        flags.getSelectedFlags(b);
        int ac=0;
        for(int i=0;i<b.length;i++){
            if(b[i]){
                switch(i){
                    case 0:
                        ac|=1;
                        break;
                    case 1:
                        ac|=2;
                        break;
                    case 2:
                        ac|=4;
                        break;
                    case 3:
                        ac|=8;
                        break;
                    case 4:
                        ac|=0x10;
                        break;
                    case 5:
                        ac|=0x40;
                        break;
                    case 6:
                        ac|=0x80;
                        break;
                }
            }
        }
    return ac;
    }
    public void commandAction(Command c,Displayable d){
     if(c==ok && ! isadd){
          classFile.setFieldOther(fieldIndex,getFlags(),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(tn.getString())),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(td.getString())));
display.setCurrent(fieldList);
        isSave=false;
        }else if(c==ok && isadd){
          FieldInfo fi=new FieldInfo(getFlags(),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(tn.getString())),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(td.getString())),0,(AttributeInfo[])null);
classFile.addField(fi);
updateField();
display.setCurrent(fieldList);
        isSave=false;
        }else if(c==back){
            display.setCurrent(fieldList);
        }
    }
}

class MethodEditor extends Form implements CommandListener{
    ChoiceGroup flags;
    boolean isadd;
    private Command back=new Command(lr.get("29"),Command.BACK,0);
    private Command ok=new Command(lr.get("27"),Command.OK,0);
    TextField tn,td;
    MethodEditor(boolean[] b,String name,String desc){
        super(lr.get("84"));
        isadd=false;
        flags= new ChoiceGroup("AccessFlags", Choice.MULTIPLE, new String[]{"public", "private", "protected", "static","final","synchronized","native","abstract"}, null);
            for(int i=0;i<b.length;i++)
                flags.setSelectedIndex(i,b[i]);
        tn=new TextField("Name",name,500,0);
        td=new TextField("Descriptor",desc,500,0);
        append(flags);
        append(tn);
        append(td);
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
    }

    MethodEditor(){
        super(lr.get("68"));
        isadd=true;
        flags= new ChoiceGroup("AccessFlags", Choice.MULTIPLE, new String[]{"public", "private", "protected", "static","final","synchronized","native","abstract"}, null);
            for(int i=0;i<flags.size();i++)
                flags.setSelectedIndex(i,false);
        tn=new TextField("Name","myMethod",500,0);
        td=new TextField("Descriptor","()V",500,0);
        append(flags);
        append(tn);
        append(td);
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
    }


    public int getFlags(){
        boolean[] b=new boolean[flags.size()];
        flags.getSelectedFlags(b);
        int ac=0;
        for(int i=0;i<b.length;i++){
            if(b[i]){
                switch(i){
                    case 0:
                        ac|=1;
                        break;
                    case 1:
                        ac|=2;
                        break;
                    case 2:
                        ac|=4;
                        break;
                    case 3:
                        ac|=8;
                        break;
                    case 4:
                        ac|=0x10;
                        break;
                    case 5:
                        ac|=0x20;
                        break;
                    case 6:
                        ac|=0x0100;
                        break;
                    case 7:
                        ac|=0x0400;
                        break;
                }
            }
        }
    return ac;
    }
    public void commandAction(Command c,Displayable d){
     if(c==ok && ! isadd){
          classFile.setMethodOther(methodIndex,getFlags(),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(tn.getString())),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(td.getString())));
display.setCurrent(methodList);
        isSave=false;
        }else if(c==ok && isadd){
            AttributeInfo[] attr=new AttributeInfo[2];
            attr[0]=new AttributeInfo.ExceptionsAttribute(classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info("Exceptions")));
             attr[1]=new AttributeInfo.CodeAttribute(classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info("Code")));
            MethodInfo mi=new MethodInfo(getFlags(),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(tn.getString())),classFile.addToConstantPool(new ConstantPoolInfo.Utf8Info(td.getString())),2,attr);
        classFile.addMethod(mi);
        updateMethod();
        display.setCurrent(methodList);
        isSearchM=false;
        isSave=false;
        }else if(c==back){
            display.setCurrent(methodList);
        }
    }
}

class MaxEditor extends Form implements CommandListener{
    private Command back=new Command(lr.get("29"),Command.BACK,0);
    private Command ok=new Command(lr.get("27"),Command.OK,0);
    TextField ts,tl;
    MaxEditor(int maxs,int maxl){
        super(lr.get("85"));
        ts=new TextField("MaxStack:",""+maxs,500,2);
        tl=new TextField("MaxLocals:",""+maxl,500,2);
        append(ts);
        append(tl);
        addCommand(ok);
        addCommand(back);
        setCommandListener(this);
    }
    public void commandAction(Command c,Displayable d){
        if(c==ok){
            try{
                classFile.setMethodMaxStack(methodIndex,Integer.parseInt(ts.getString()));
                classFile.setMethodMaxLocals(methodIndex,Integer.parseInt(tl.getString()));
                isSave=false;
                display.setCurrent(methodList);
            }catch(Exception e){}
        }else if(c==back){
            display.setCurrent(methodList);
        }
    }
}


}
