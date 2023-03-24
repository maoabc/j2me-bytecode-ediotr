package main;

import classfile.ClassFile;
import classfile.Code;
import classfile.JavaOpcode;
import io.BufferedInputStream;
import util.Utils;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.file.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;

public class Search extends List implements Runnable{
    public Image image;
    String path,search;
    public boolean isBreak=false,isSearchCode;
    public Search(String path,String search,Image image){
        super("Path:\n"+path,List.IMPLICIT);
        this.image=image;
        this.path=path;
        isSearchCode=false;
        String[] s= Utils.splitString(search.trim()," ");
        if(JavaOpcode.toCode(s[0]) != 0)
            isSearchCode=true;
        this.search=search;
        new Thread(this).start();
    }


    private InputStream open(String path) throws IOException{
        FileConnection fc = (FileConnection) Connector.open("file://"+path,Connector.READ);
        InputStream is=fc.openInputStream();
        fc.close();
        return is;
    }


    public String search(String path,String str) {
        try{
        DataInputStream dis=new DataInputStream( new BufferedInputStream( open(path) ) );
        if(isSearchCode){
            ClassFile cf=new ClassFile();
            cf.read(dis);
            Code cd=new Code(cf);
            cd.load(str);
            String search=cd.getString();
            int cont=cf.getMethodsCount();
            for(int i=0; i<cont;i++){
                String src=i2b(cf.getMethodCode(i) );
                if( src.indexOf(search) != -1 
)
                    return path;
            }
        }
        if( Utils.isIn( dis , str ) ){
            return path;
        }
        }catch(IOException e){}
        return null;
    }
    private static String i2b(int[] a){
        byte[] b=new byte[a.length];
        for(int i=0;i<a.length;i++)
            b[i]=(byte)a[i];
        return new String(b);
    }
    public  String[] list(String path){
        try{
            FileConnection fc=(FileConnection)Connector.open("file://"+path,Connector.READ);
            if(fc.exists()){
                Enumeration en=fc.list();
                Vector vc=new Vector(0,1);
                while(en.hasMoreElements()){
                    vc.addElement(en.nextElement());
                }
                String[] s=new String[(int)vc.size()];
                vc.copyInto(s);
                return s;
            }
        }catch(IOException e){
            return null;
        }
        return null;
    }
    public void search(String path){
        String temp="";
        try{
            String[] str=list(path);
            for(int i=0;i<str.length;i++){
                if(isBreak)
                    break;
                temp=path+str[i];
                if(isDirectory(temp))
                    search(temp);
                else
                {
                    if(temp.endsWith(".class")){
                        setTitle(temp.substring(this.path.length()) );
                        String s=search(temp,search);
                        if(s != null)
                    
                            append(s.substring(this.path.length()),image);        
                    }
                }
            }
        }catch(Exception e){
            append(e.getMessage() ,null);
        }
    }
    public boolean isDirectory(String path){
        return path.endsWith("/");
    }
    public void run(){
        search(path);
        setTitle(search);
    }

}