package classfile;

import java.io.*;

public class FieldInfo {

    public int accessFlags;
    public int nameIndex;
    public int descriptorIndex;
    private int attributesCount;
    private AttributeInfo[] attributes;
    public static final int PUBLIC = 0x0001;
    public static final int PRIVATE = 0x0002;
    public static final int PROTECTED = 0x0004;
    public static final int STATIC = 0x0008;
    public static final int FINAL = 0x0010;
    public static final int VOLATILE = 0x0040;
    public static final int TRANSIENT = 0x0080;
    public static final int SYNTHETIC = 0x1000;
    public static final int ENUM = 0x4000;

    public FieldInfo(DataInputStream in, ConstantPoolInfo[] pool) throws IOException {
        accessFlags = in.readUnsignedShort();
        nameIndex = in.readUnsignedShort();
        descriptorIndex = in.readUnsignedShort();

        attributesCount = in.readUnsignedShort();
        attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = AttributeInfo.construct(in, pool);
        }
    }
    public FieldInfo(int accessFlags,int nameIndex,int descriptorIndex,int attributesCount,AttributeInfo[] attributes){
        this.accessFlags=accessFlags;
        this.nameIndex=nameIndex;
        this.descriptorIndex=descriptorIndex;
        this.attributesCount=attributesCount;
        this.attributes = attributes;
    }
    
    public int getDescriptorIndex(){
        return descriptorIndex;
    }
    public int getNameIndex(){
        return nameIndex;
    }

   public boolean[] accessFlags() {
      boolean[] ab=new boolean[7];
      for(int i=0;i<7;i++)
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
      if ((accessFlags & 0x0040) > 0)
         ab[5]=true;
      if ((accessFlags & 0x0080) > 0)
         ab[6]=true;
      return ab;
   }

    public int getAccessFlags(){
        return accessFlags;
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
}