package classfile;

import io.ArrayOutputStream;
import util.Util;

import java.io.*;
public class Code{
    ClassFile cf;
    public int count;
    ArrayOutputStream bos;
    public Code(ClassFile cf)
    {
        bos=new ArrayOutputStream(1024);  //初始容量
        this.cf=cf;
        count=0;
     }
     public void load(String s)throws IOException
     {
        DataOutputStream dos=new DataOutputStream(bos);
         String[] str= Util.splitString(s.trim(),"\n");
         for(int i=0;i<str.length;i++)
         {
             String[] bc=Util.splitString(str[i].trim()," ");
             int code=JavaOpcode.toCode(bc[0].trim());
             switch(JavaOpcode.getOpcodeLengthNew(code))
             {
                 case 1:
                     dos.writeByte(code);
                     count++;
                     break;
                 case 3:
                     if(JavaOpcode.isField(code))
                     {
                         dos.writeByte(code);
                         loadField(dos, bc);
                     }else if(JavaOpcode.isMethod(code))
                     {
                         dos.writeByte(code);
                         loadMethod(dos, bc);
                     }else if(JavaOpcode.isBranchInstruction(code))
                     {
                         dos.writeByte(code);
                         dos.writeShort(Integer.parseInt(bc[1].trim()));
                     }else if(code==JavaOpcode.IINC)
                     {
                         dos.writeByte(code);
                         dos.writeByte(Integer.parseInt(bc[1].trim()));
                         dos.writeByte(Integer.parseInt(bc[2].trim()));
                     }else if(code==JavaOpcode.LDC_W)
                     {
                         dos.writeByte(code);
                         if(str[i].lastIndexOf('"') !=-1)
                         {
                             int st=str[i].indexOf('"');
                             int l=str[i].lastIndexOf('"');
                             int ind=cf.addToConstantPool(Util.replace(str[i].substring(st+1,l),"^n","\n"));
                             dos.writeShort(ind);
                         }else
                         {
                             if(bc[1].lastIndexOf('.') !=-1)
                             {
                                 Float f=new Float(Float.parseFloat(bc[1].trim()));
                                 int a=cf.addToConstantPool(f);
                                 dos.writeShort(a);
                             }else
                             {
                                 Integer in=new Integer(Integer.parseInt(bc[1].trim()));
                                 int b=cf.addToConstantPool(in);
                                 dos.writeShort(b);
                             }
                         }
                     }else if(code==JavaOpcode.LDC2_W)
                     {
                         dos.writeByte(code);
                         if(str[i].lastIndexOf('"') !=-1)
                         {
                             int st=str[i].indexOf('"');
                             int l=str[i].lastIndexOf('"');
                             int ind=cf.addToConstantPool(Util.replace(str[i].substring(st+1,l),"^n","\n"));
                             dos.writeShort(ind);
                         }else
                         {
                             if(bc[1].lastIndexOf('.') !=-1)
                             {
                                 Double b=new Double(Double.parseDouble(bc[1].trim()));
                                 int c=cf.addToConstantPool(b);
                                 dos.writeShort(c);
                             }else
                             {
                                 Long l=new Long(Long.parseLong(bc[1].trim()));
                                 int d=cf.addToConstantPool(l);
                                 dos.writeShort(d);
                             }
                         }
                     }else if(code==JavaOpcode.SIPUSH)
                     {
                         dos.writeByte(code);
                         dos.writeShort(Integer.parseInt(bc[1].trim()));
                     }else
                     {
                         dos.writeByte(code);
                         Clazz cl=new Clazz(bc[1].trim());
                         int other=cf.addToConstantPool(cl);
                         dos.writeShort(other);
                     }
                     count+=3;
                     break;
                 case 2:
                     if(code==JavaOpcode.NEWARRAY)
                     {
                         dos.writeByte(code);
                         if(bc[1].trim().equals("byte"))
                         {
                             dos.writeByte(8);
                         }else if(bc[1].trim().equals("char"))
                         {
                             dos.writeByte(5);
                         }else if(bc[1].trim().equals("short"))
                         {
                             dos.writeByte(9);
                         }else if(bc[1].trim().equals("int"))
                         {
                             dos.writeByte(10);
                         }else if(bc[1].trim().equals("float"))
                         {
                             dos.writeByte(6);
                         }else if(bc[1].trim().equals("long"))
                         {
                             dos.writeByte(11);
                         }else if(bc[1].trim().equals("double"))
                         {
                             dos.writeByte(7);
                         }else if(bc[1].trim().equals("boolean"))
                         {
                             dos.writeByte(4);
                         }
                     }else if(code==JavaOpcode.LDC)
                     {
                         dos.writeByte(code);
                         if(str[i].lastIndexOf('"') !=-1)
                         {
                             int st=str[i].indexOf('"');
                             int l=str[i].lastIndexOf('"');
                             int ind=cf.addToConstantPool(Util.replace(str[i].substring(st+1,l),"^n","\n"));
                             dos.writeByte(ind);
                         }else
                         {
                             if(bc[1].lastIndexOf('.') !=-1)
                             {
                                 Float f=new Float(Float.parseFloat(bc[1].trim()));
                                 int a=cf.addToConstantPool(f);
                                 dos.writeByte(a);
                             }else
                             {
                                 Integer in=new Integer(Integer.parseInt(bc[1].trim()));
                                 int b=cf.addToConstantPool(in);
                                 dos.writeByte(b);
                             }
                         }
                     }else
                     {
                         dos.writeByte(code);
                         dos.writeByte(Integer.parseInt(bc[1].trim()));
                     }
                     count+=2;
                     break;
                 case 5:
                     dos.writeByte(code);
                     loadInterface(dos, bc);
                     count+=5;
                     break;
                 case 6:
                     dos.writeByte(code);
                     dos.writeByte(JavaOpcode.IINC);
                         dos.writeShort(Integer.parseInt(bc[1]));
                         dos.writeShort(Integer.parseInt(bc[2]));
                     count+=6;
                     break;
                 case JavaOpcode.TABLESWITCH:
                     dos.writeByte(code);
                     count++;
                     tableswitch(dos ,bc);
                     break;
                 case JavaOpcode.LOOKUPSWITCH:
                     dos.writeByte(code);
                     count++;
                     lookupswitch(dos, bc);
                     break;
                 case 4:
                     dos.writeByte(code);
                     Clazz cl=new Clazz(bc[1]);
                     int index=cf.addToConstantPool(cl);
                     dos.writeShort(index);
                     dos.writeByte(Integer.parseInt(bc[2].trim()));
                     count+=4;
                     break;
             }
         }
         dos.close();
     }
     void tableswitch(DataOutputStream dos, String[] str)throws IOException
     {
         int start=count;
         for(;count<start+4;count++)
         {
             if(count%4==0)
                 break;
             dos.writeByte(0);
         }
         int defaul=Integer.parseInt(str[1].trim());
         dos.writeInt(defaul);
         str=Util.splitString(str[2].trim(),",");
int index=str[0].indexOf(':');
String s=str[0].substring(0,index);
         int low=Integer.parseInt(s);
index=str[str.length-1].indexOf(':');
s=str[str.length-1].substring(0,index);
         int high=Integer.parseInt(s);
         dos.writeInt(low);
         dos.writeInt(high);
         for(int i=0;i<str.length;i++)
         {
index=str[i].indexOf(':');
s=str[i].substring(index+1);
             index=Integer.parseInt(s);
             dos.writeInt(index);
         }
         count+=4*(high-low+4);
     }
     void lookupswitch(DataOutputStream dos, String[] str)throws IOException
     {
         int start=count;
         for(;count<start+4;count++)
         {
             if(count%4==0)
                 break;
             dos.writeByte(0);
         }
         int defaul=Integer.parseInt(str[1]);
         dos.writeInt(defaul);
         count+=4;
         str=Util.splitString(str[2].trim(),",");
         int len=str.length;
         dos.writeInt(len);
         count+=4;
         for(int i=0;i<len;i++)
         {
             String s=str[i];
             int in=s.indexOf(':');
             int key=Integer.parseInt(s.substring(0,in));
             int value=Integer.parseInt(s.substring(in+1));
             dos.writeInt(key);
             dos.writeInt(value);
             count+=8;
         }
     }

    void loadField(DataOutputStream dos, String[] str)throws IOException
    {
        int i=str[1].lastIndexOf('/');
        Clazz cls=new Clazz(str[1].substring(0,i));
        String name=str[1].substring(i+1).trim();
        Clazz type=new Clazz(str[2].trim());
        Field fd=new Field(type,name,cls);
        i=cf.addToConstantPool(fd);
        dos.writeShort(i);
     }
    void loadMethod(DataOutputStream dos, String[] str)throws IOException{
        MethodAnaly ma=new MethodAnaly(str[1].trim());
        Clazz cls=new Clazz(ma.getClazz());
        String name=ma.getName();
        Clazz ret=new Clazz(ma.getReturn());
        String[] pa=splitParam(ma.getParam());
        Clazz[] param=new Clazz[pa.length];
        for(int i=0;i<pa.length;i++)
            param[i]=new Clazz(pa[i]);
        Method md=new Method(cls,ret,name,param);
        int index=cf.addToConstantPool(md);
        dos.writeShort(index);
    }
    void loadInterface(DataOutputStream dos, String[] str)throws IOException{
        MethodAnaly ma=new MethodAnaly(str[1].trim());
        Clazz cls=new Clazz(ma.getClazz(),true);
        String name=ma.getName();
        Clazz ret=new Clazz(ma.getReturn());
        String[] pa=splitParam(ma.getParam());
        Clazz[] param=new Clazz[pa.length];
        for(int i=0;i<pa.length;i++)
            param[i]=new Clazz(pa[i]);
        Method md=new Method(cls,ret,name,param);
        int index=cf.addToConstantPool(md);
        dos.writeShort(index);
        dos.writeByte(Integer.parseInt(str[2].trim()));
        dos.writeByte(0);
    }
    
    
    public static String[] splitParam(String s)
    {
        String str=Util.addBlank(s);
        return Util.splitString(str," ");
    }
    public int[] getCode(){
        byte[] b=bos.getBufArray();
        int[] code=new int[bos.size()];
        for(int i=0;i<code.length;i++)
        {
            code[i]=b[i] & 0xff;
        }
        try
        {
            bos.close();
        }catch(Exception e){}
        return code;
    }
    public String getString(){
        String s =bos.toString();
        try
        {
            bos.close();
        }catch(Exception e){}
        return s;
    }

}