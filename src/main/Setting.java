package main;

import java.io.*;
import javax.microedition.rms.*;

public class Setting{
    public String path="/" , search="" ,searchMethod="", gotoLine="" , offset="" , codeSearch="" , codeReplace="" ,jvm="",google="";
    public boolean isShow=false;
    public Setting(){}
    
    public byte[] toByteArray( ) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] b = null;
        try {
                    dos.writeUTF(path);
                    dos.writeUTF(search);
                    dos.writeUTF(searchMethod);
                    dos.writeUTF(gotoLine);
                    dos.writeUTF(offset);
                    dos.writeUTF(codeSearch);
                    dos.writeUTF(codeReplace);
                    dos.writeUTF(jvm);
                    dos.writeUTF(google);
                    dos.writeBoolean(isShow);
            b=baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    public Setting(byte[] data) {
        DataInputStream dis= new DataInputStream(new ByteArrayInputStream(data));
        try {
            path = dis.readUTF();
            search = dis.readUTF();
            searchMethod = dis.readUTF();
            gotoLine = dis.readUTF();
            offset = dis.readUTF();
            codeSearch = dis.readUTF();
            codeReplace = dis.readUTF();
            jvm = dis.readUTF();
            google = dis.readUTF();
            isShow = dis.readBoolean();
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}