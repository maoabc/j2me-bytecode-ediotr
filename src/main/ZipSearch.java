package main;

import io.ArrayInputStream;
import io.ArrayOutputStream;
import jzip.ContDataInputStream;
import jzip.ZipEntry;
import jzip.ZipInputStream;
import util.Utils;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.util.Vector;
import javax.microedition.lcdui.*;

public class ZipSearch extends Form implements Runnable{
    String path,str,src;
    boolean isBreak=false;
    
    public ZipSearch(String path,String str){
        super("搜索中…");
        this.path=path;
        this.str=str;
        new Thread(this).start();
    }
    public InputStream open(String path) throws IOException{
        FileConnection fc = (FileConnection) Connector.open("file://"+path);
        InputStream in = fc.openInputStream();
        fc.close();
        return in;
    }
    public OutputStream write(String path)throws IOException{
        FileConnection fc = (FileConnection) Connector.open("file://"+path);
        if (path.endsWith("/") ){
            if(! fc.exists() )
                fc.mkdir();
            return null;
        }else{
            if (!fc.exists())
                fc.create();
            else{
                fc.delete();
                fc.create();
            }
            OutputStream os = fc.openOutputStream();
            fc.close();
            return os;
        }
    }
    public void run(){
        search(path , str);
        setTitle(str);
    }

    public void unZip(String p) {
        try {
            ZipInputStream zip=new ZipInputStream( new ContDataInputStream( open(p) ) ) ;
            int i=p.lastIndexOf('.');
            p=p.substring(0,i)+"_unpack/";
            write(p);
ZipEntry entry;
            while ((entry=zip.getNextEntry()) != null) {
                if( entry.isDirectory() ){
                    write(p+entry.toString() );
                    continue;
                }
                byte[] buf = new byte[4098];
                OutputStream os=write(p+entry.toString() );
                int num = -1;
                while ((num = zip.read(buf, 0, buf.length)) != -1)
                    os.write(buf, 0, num);
                
                os.close();
            }
            zip.close();
        } catch (Exception ex) {
            append(ex.getMessage() );
        }
    }


    public void search(String p, String src) {
        DataInputStream dis=null;
        OutputStream os=null;
        ZipInputStream zip=null;
        try {
            zip=new ZipInputStream( new ContDataInputStream( open(p) ) ) ;
            int count=0;
            ZipEntry entry;
            append("文件路径: "+p+"\n搜索字符: "+str+"\n当前进度: ");
            int i=p.indexOf('.');
            p=p.substring(0,i)+"_unpack/";
            while ((entry=zip.getNextEntry()) != null) {
                if(isBreak)   //中断线程
                    return;
                if( ! entry.isDirectory() ){
                    String name=entry.toString();
                        setTitle(name );
                    if( name.endsWith(".class") ){
                        append(" "+name +"搜索中…");
                        byte[] buf = new byte[4*1024];
                        int num = -1;
                        ArrayOutputStream baos = new ArrayOutputStream( 8*1024 );
                        while ((num = zip.read(buf, 0, buf.length)) != -1)
                            baos.write(buf, 0, num);
                        buf=baos.getBufArray();
                        num=baos.size();
                        dis=new DataInputStream(new ArrayInputStream(buf, 0 , num ) );
                        boolean b=false;
                        b= Utils.isIn(dis , src);
                        if( b ){
                            write(p);
                            if(name.indexOf('/') != -1){
                                String[] str=Utils.splitString(name,"/");
                                String dir="";
                                for(int j=0;j<str.length-1;j++){
                                    dir+=str[j]+"/";
                                    write(p+dir);
                                }
                            }
                            delete(1);
                            append(" "+name+"解压中…");
                            int temp=num/10,val=0;
                                    String  tr=String.valueOf( num/1024.0f);
                            Gauge g=new Gauge( "0.0" +( num>1024 ? "%总大小: "+ ( tr.length()>5 ? tr.substring(0, 5) : tr ) +"KB" : "%总大小: "+num+"B" ) ,false,temp,0);
                            append(g);
                            os=write(p+name );
                            for(int v=0 ; v < num ; v++ ){
                                os.write( buf[v]);
                                if(v ==val ){
                                    val+=temp;
                                    String t=String.valueOf( ( (float)v*100)/num ) ;
                                    g.setLabel( (t.length()>5 ?t.substring(0,5) : t)  +( num>1024 ? "%总大小: "+ ( tr.length()>5 ? tr.substring(0, 5) : tr ) +"KB" : "%总大小: "+num+"B" )  );

                                    g.setValue(v/10);
                                }
                            }
                            delete(2);
                            os.close();
                            delete(1);
                            append(" "+name +"已解压…");
                            count++;
                        }
                        delete(1);
                    }
                }
            }
            deleteAll();
            append( (count==0?"无搜索结果" : "包含字符: “" + str +"”的文件共"+count +"个，解压路径: "+p) );
            zip.close();
        } catch (Exception ex) {
             deleteAll();
            append(ex.getMessage() );
        }finally{
            try{
                zip.close();
                dis.close();
                os.close();
            }catch(Exception e){}
        }
    }
}