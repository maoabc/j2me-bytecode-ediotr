package classfile;

import java.io.*;

public abstract class ConstantPoolInfo {

    public static final int CLASS = 7;
    public static final int FIELDREF = 9;
    public static final int METHODREF = 10;
    public static final int INTERFACEMETHODREF = 11;
    public static final int STRING = 8;
    public static final int INTEGER = 3;
    public static final int FLOAT = 4;
    public static final int LONG = 5;
    public static final int DOUBLE = 6;
    public static final int NAMEANDTYPE = 12;
    public static final int UTF8 = 1;

    public abstract int getTag();

    public abstract void write(DataOutputStream out) throws IOException;

    public abstract boolean equals(Object obj);

    public static ConstantPoolInfo construct(DataInputStream in) throws IOException {
        int tag = in.readUnsignedByte();
        switch (tag) {
            case CLASS:
                return new ClassInfo(in);
            case FIELDREF:
                return new FieldRefInfo(in);
            case METHODREF:
                return new MethodRefInfo(in);
            case INTERFACEMETHODREF:
                return new InterfaceMethodRefInfo(in);
            case STRING:
                return new StringInfo(in);
            case INTEGER:
                return new IntegerInfo(in);
            case FLOAT:
                return new FloatInfo(in);
            case LONG:
                return new LongInfo(in);
            case DOUBLE:
                return new DoubleInfo(in);
            case NAMEANDTYPE:
                return new NameAndTypeInfo(in);
            case UTF8:
                return new Utf8Info(in);
        }
        return null;
    }

    public static class ClassInfo extends ConstantPoolInfo {

        private final int nameIndex;
        private final int hashCode;

        ClassInfo(DataInputStream in) throws IOException {
            nameIndex = in.readUnsignedShort();
            hashCode = (this.getClass().hashCode() * 31) ^ (nameIndex * 37);
        }

        ClassInfo(int val) {
            nameIndex = val;
            hashCode = (this.getClass().hashCode() * 31) ^ (nameIndex * 37);
        }

        public int getTag() {
            return CLASS;
        }

        public int getNameIndex() {
            return nameIndex;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(CLASS);
            out.writeShort(nameIndex);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof ClassInfo)) {
                return false;
            }

            return getNameIndex() == ((ClassInfo) obj).getNameIndex();
        }

        public String toString() {
            return "Class_Info: name=" + getNameIndex();
        }
    }

    abstract static class RefInfo extends ConstantPoolInfo {

        private final int classIndex;
        private final int nameAndTypeIndex;
        private final int hashCode;

        RefInfo(DataInputStream in) throws IOException {
            classIndex = in.readUnsignedShort();
            nameAndTypeIndex = in.readUnsignedShort();
            hashCode = (this.getClass().hashCode() * 31) ^ (classIndex * 37) ^ (nameAndTypeIndex * 41);
        }

        RefInfo(int classIndex, int nameAndTypeIndex) {
            this.classIndex = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
            hashCode = (this.getClass().hashCode() * 31) ^ (classIndex * 37) ^ (nameAndTypeIndex * 41);
        }

        public int getClassIndex() {
            return classIndex;
        }

        public int getNameAndTypeIndex() {
            return nameAndTypeIndex;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(getTag());
            out.writeShort(classIndex);
            out.writeShort(nameAndTypeIndex);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof RefInfo)) {
                return false;
            }

            if (getTag() != ((RefInfo) obj).getTag()) {
                return false;
            }

            return (getClassIndex() == ((RefInfo) obj).getClassIndex()) && (getNameAndTypeIndex() == ((RefInfo) obj).getNameAndTypeIndex());
        }
    }

    public static class FieldRefInfo extends RefInfo {

        FieldRefInfo(DataInputStream in) throws IOException {
            super(in);
        }

        FieldRefInfo(int classIndex, int nameAndTypeIndex) {
            super(classIndex, nameAndTypeIndex);
        }

        public int getTag() {
            return FIELDREF;
        }

        public String toString() {
            return "FieldRef_Info: class=" + getClassIndex() + " , nameAndType=" + getNameAndTypeIndex();
        }
    }

    public static class MethodRefInfo extends RefInfo {

        MethodRefInfo(DataInputStream in) throws IOException {
            super(in);
        }

        MethodRefInfo(int classIndex, int nameAndTypeIndex) {
            super(classIndex, nameAndTypeIndex);
        }

        public int getTag() {
            return METHODREF;
        }

        public String toString() {
            return "MethodRef_Info: class=" + getClassIndex() + " , nameAndType=" + getNameAndTypeIndex();
        }
    }

    public static class InterfaceMethodRefInfo extends MethodRefInfo {

        InterfaceMethodRefInfo(DataInputStream in) throws IOException {
            super(in);
        }

        InterfaceMethodRefInfo(int classIndex, int nameAndTypeIndex) {
            super(classIndex, nameAndTypeIndex);
        }

        public int getTag() {
            return INTERFACEMETHODREF;
        }

        public String toString() {
            return "InterfaceMethodRef_Info: class=" + getClassIndex() + " , nameAndType=" + getNameAndTypeIndex();
        }
    }

    public static class StringInfo extends ConstantPoolInfo {

        private final int stringIndex;
        private final int hashCode;

        StringInfo(DataInputStream in) throws IOException {
            stringIndex = in.readUnsignedShort();
            hashCode = (this.getClass().hashCode() * 31) ^ (stringIndex * 37);
        }

        StringInfo(int val) {
            stringIndex = val;
            hashCode = (this.getClass().hashCode() * 31) ^ (stringIndex * 37);
        }

        public int getTag() {
            return STRING;
        }

        public int getStringIndex() {
            return stringIndex;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(STRING);
            out.writeShort(stringIndex);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof StringInfo)) {
                return false;
            }

            return getStringIndex() == ((StringInfo) obj).getStringIndex();
        }

        public String toString() {
            return "String_Info: string=" + getStringIndex();
        }
    }

    public static class IntegerInfo extends ConstantPoolInfo {

        private int bytes;
        private final int hashCode;

        IntegerInfo(DataInputStream in) throws IOException {
            bytes = in.readInt();
            hashCode = (this.getClass().hashCode() * 31) ^ (bytes * 37);
        }

        IntegerInfo(int val) {
            bytes = val;
            hashCode = (this.getClass().hashCode() * 31) ^ (bytes * 37);
        }

        public int getTag() {
            return INTEGER;
        }

        public int getBytes() {
            return bytes;
        }
        public void setBytes(int i ){
            bytes=i;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(INTEGER);
            out.writeInt(bytes);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof IntegerInfo)) {
                return false;
            }

            return getBytes() == ((IntegerInfo) obj).getBytes();
        }

        public String toString() {
            return "Integer_Info: " + getBytes();
        }
    }

    public static class FloatInfo extends ConstantPoolInfo {

        private float bytes;
        private final int hashCode;

        FloatInfo(DataInputStream in) throws IOException {
            bytes = in.readFloat();
            hashCode = (this.getClass().hashCode() * 31) ^ (Float.floatToIntBits(bytes) * 37);
        }

        FloatInfo(float val) {
            bytes = val;
            hashCode = (this.getClass().hashCode() * 31) ^ (Float.floatToIntBits(bytes) * 37);
        }

        public int getTag() {
            return FLOAT;
        }

        public float getBytes() {
            return bytes;
        }
        public void setBytes(float f ){
            bytes= f ;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(FLOAT);
            out.writeFloat(bytes);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof FloatInfo)) {
                return false;
            }

            return getBytes() == ((FloatInfo) obj).getBytes();
        }

        public String toString() {
            return "Float_Info: " + getBytes();
        }
    }

    public static class LongInfo extends ConstantPoolInfo {

        private long bytes;
        private final int hashCode;

        LongInfo(DataInputStream in) throws IOException {
            bytes = in.readLong();
            hashCode = (this.getClass().hashCode() * 31) ^ (((int) bytes) * 37) ^ (((int) (bytes >>> 32)) * 41);
        }

        LongInfo(long val) {
            bytes = val;
            hashCode = (this.getClass().hashCode() * 31) ^ (((int) bytes) * 37) ^ (((int) (bytes >>> 32)) * 41);
        }

        public int getTag() {
            return LONG;
        }

        public long getBytes() {
            return bytes;
        }
        public void setBytes(long l ){
            bytes=l;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(LONG);
            out.writeLong(bytes);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof LongInfo)) {
                return false;
            }

            return getBytes() == ((LongInfo) obj).getBytes();
        }

        public String toString() {
            return "Long_Info: " + getBytes();
        }
    }

    public static class DoubleInfo extends ConstantPoolInfo {

        private double bytes;
        private final int hashCode;

        DoubleInfo(DataInputStream in) throws IOException {
            bytes = in.readDouble();
            long longBytes = Double.doubleToLongBits(bytes);
            hashCode = (this.getClass().hashCode() * 31) ^ (((int) longBytes) * 37) ^ (((int) (longBytes >>> 32)) * 41);
        }

        DoubleInfo(double val) {
            bytes = val;
            long longBytes = Double.doubleToLongBits(bytes);
            hashCode = (this.getClass().hashCode() * 31) ^ (((int) longBytes) * 37) ^ (((int) (longBytes >>> 32)) * 41);
        }

        public int getTag() {
            return DOUBLE;
        }

        public double getBytes() {
            return bytes;
        }
        public void setBytes(double d ){
            bytes=d;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(DOUBLE);
            out.writeDouble(bytes);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof DoubleInfo)) {
                return false;
            }

            return getBytes() == ((DoubleInfo) obj).getBytes();
        }

        public String toString() {
            return "Double_Info: " + getBytes();
        }
    }

    public static class NameAndTypeInfo extends ConstantPoolInfo {

        private final int nameIndex;
        private final int descriptorIndex;
        private final int hashCode;

        NameAndTypeInfo(DataInputStream in) throws IOException {
            nameIndex = in.readUnsignedShort();
            descriptorIndex = in.readUnsignedShort();
            hashCode = (this.getClass().hashCode() * 31) ^ (nameIndex * 37) ^ (descriptorIndex * 41);
        }

        NameAndTypeInfo(int nameIndex, int descriptorIndex) {
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
            hashCode = (this.getClass().hashCode() * 31) ^ (nameIndex * 37) ^ (descriptorIndex * 41);
        }

        public int getTag() {
            return NAMEANDTYPE;
        }

        public int getNameIndex() {
            return nameIndex;
        }

        public int getDescriptorIndex() {
            return descriptorIndex;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(NAMEANDTYPE);
            out.writeShort(nameIndex);
            out.writeShort(descriptorIndex);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof NameAndTypeInfo)) {
                return false;
            }

            return (getNameIndex() == ((NameAndTypeInfo) obj).getNameIndex()) && (getDescriptorIndex() == ((NameAndTypeInfo) obj).getDescriptorIndex());
        }

        public String toString() {
            return "NameAndType_Info: descriptor=" + getDescriptorIndex() + " , name=" + getNameIndex();
        }
    }

    public static class Utf8Info extends ConstantPoolInfo {

        private String bytes;
        private final int hashCode;

        Utf8Info(DataInputStream in) throws IOException {
            bytes = in.readUTF();
            hashCode = (this.getClass().hashCode() * 31) ^ (bytes.hashCode() * 37);
        }

        public Utf8Info(String val) {
            bytes = val;
            hashCode = (this.getClass().hashCode() * 31) ^ (bytes.hashCode() * 37);
        }

        public int getTag() {
            return UTF8;
        }

        public String getBytes() {
            return bytes;
        }
        public void setBytes(String s){
            bytes=s;
        }

        public void write(DataOutputStream out) throws IOException {
            out.writeByte(UTF8);
            out.writeUTF(bytes);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Utf8Info)) {
                return false;
            }

            return getBytes().equals(((Utf8Info) obj).getBytes());
        }

        public String toString() {
            return "Utf8_Info: " + getBytes();
        }
    }
}
