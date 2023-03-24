package classfile;

import java.io.*;
public class MethodInfo {

    public int accessFlags;
    public int nameIndex;
    public int descriptorIndex;
    public int attributesCount;
    public AttributeInfo[] attributes;
    AttributeInfo.CodeAttribute.ExceptionEntry[] ob;

    public static final int PUBLIC = 0x0001;
    public static final int PRIVATE = 0x0002;
    public static final int PROTECTED = 0x0004;
    public static final int STATIC = 0x0008;
    public static final int FINAL = 0x0010;
    public static final int SYNCHRONIZED = 0x0020;
    public static final int BRIDGE = 0x0040;
    public static final int VARARGS = 0x0080;
    public static final int NATIVE = 0x0100;
    public static final int ABSTRACT = 0x0400;
    public static final int STRICT = 0x0800;
    public static final int SYTHETIC = 0x1000;

    public MethodInfo(DataInputStream in, ConstantPoolInfo[] pool) throws IOException {
        accessFlags = in.readUnsignedShort();
        nameIndex = in.readUnsignedShort();
        descriptorIndex = in.readUnsignedShort();

        attributesCount = in.readUnsignedShort();
        attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = AttributeInfo.construct(in, pool);
        }
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeShort(accessFlags);
        out.writeShort(nameIndex);
        out.writeShort(descriptorIndex);

        out.writeShort(attributesCount);
        for (int i = 0; i < attributesCount; i++) {
            attributes[i].write(out);
        }
    }
    public MethodInfo(int access,int nameIndex,int descriptorIndex,int aCount,AttributeInfo[] attributes){
        accessFlags = access;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        attributesCount = aCount;
        this.attributes=attributes;
    }
   public boolean[] accessFlags() {
      boolean[] ab=new boolean[8];
      for(int i=0;i<8;i++)
         ab[i]=false;
      if ((accessFlags & 0x0001) > 0)
         ab[0]=true;
      if ((accessFlags & 0x0002) > 0)
         ab[1]=true;
      if ((accessFlags & 0x0004) > 0)
         ab[2]=true;
      if ((accessFlags & 0x0008) > 0)
         ab[3]=true;
      if ((accessFlags & 0x0010) > 0)
         ab[4]=true;
      if ((accessFlags & 0x0020) > 0)
         ab[5]=true;
      if ((accessFlags & 0x0100) > 0)
         ab[6]=true;
      if ((accessFlags & 0x0400) > 0) 
         ab[7]=true;
      return ab;
   }

    public int getNameIndex() {
        return nameIndex;
    }



    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    public int getMaxStack() {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                return ((AttributeInfo.CodeAttribute) attributes[i]).getMaxStack();
            }
        }
        return 0;
    }


    public void setMaxStack(int maxs) {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                ((AttributeInfo.CodeAttribute) attributes[i]).maxStack=maxs;
            }
        }
    }

    public void setMaxLocals(int maxl) {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                ((AttributeInfo.CodeAttribute) attributes[i]).maxLocals=maxl;
            }
        }
    }


    public int getMaxLocals() {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                return ((AttributeInfo.CodeAttribute) attributes[i]).getMaxLocals();
            }
        }
        return 0;
    }

    public int[] getCode() {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                return ((AttributeInfo.CodeAttribute) attributes[i]).getCode();
            }
        }
        return null;
    }

    public void setCode(int[] code, ClassFile cf) {
        setCode(code, code.length, cf);
    }

    public void setCode(int[] code, int codeLength, ClassFile cf) {
        String descriptor = cf.getConstantPoolUtf8(this.getDescriptorIndex());
        int argLength = cf.getMethodArgLength(descriptor);
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                ((AttributeInfo.CodeAttribute) attributes[i]).setCode(code, codeLength, cf, argLength);
                return;
            }
        }
    }

    public AttributeInfo.CodeAttribute.ExceptionEntry[] getExceptionTable() {
        for (int i = 0; i < attributesCount; i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                return ((AttributeInfo.CodeAttribute) attributes[i]).getExceptionTable();
            }
        }
        return null;
    }
    public void addException(AttributeInfo.CodeAttribute.ExceptionEntry ace, ClassFile cf){
        AttributeInfo.CodeAttribute.ExceptionEntry[] exception=getExceptionTable();
        int count=exception.length;
        ob=new AttributeInfo.CodeAttribute.ExceptionEntry[count+1];
        System.arraycopy(exception,0,ob,0,count);
        ob[count]=ace;
        setExceptionTable(ob,cf);
    }
    public void removeException(int n, ClassFile cf){
        AttributeInfo.CodeAttribute.ExceptionEntry[] exception=getExceptionTable();
        int count = exception == null ? 0 : exception.length;
        if(count==1 ){
            setExceptionTable((AttributeInfo.CodeAttribute.ExceptionEntry[])null,0,cf);
        }else if ( count > 1 ) {
            System.arraycopy(exception, n+1,exception, n,count - n - 1);
            exception[--count]=null;
            setExceptionTable(exception,count,cf);
        }
    }


    public void setExceptionTable(AttributeInfo.CodeAttribute.ExceptionEntry[] exceptionTable, ClassFile cf) {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                ((AttributeInfo.CodeAttribute) attributes[i]).setExceptionTable(exceptionTable, exceptionTable.length, cf);
                return;
            }
        }
    }

    public void setExceptionTable(AttributeInfo.CodeAttribute.ExceptionEntry[] exceptionTable, int exceptionTableLength, ClassFile cf) {
        for (int i = 0; (i < attributesCount); i++) {
            if (attributes[i] instanceof AttributeInfo.CodeAttribute) {
                ((AttributeInfo.CodeAttribute) attributes[i]).setExceptionTable(exceptionTable, exceptionTableLength, cf);
                return;
            }
        }
    }
}
