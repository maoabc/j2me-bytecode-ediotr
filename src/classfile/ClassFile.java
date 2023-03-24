package classfile;

import util.Util;

import java.io.*;
import java.util.*;

public class ClassFile {

    private int magic;
    private int minorVersion;
    private int majorVersion;
    private int constantPoolCount;
    private ConstantPoolInfo[] constantPool;
    private Hashtable constantPoolMap;
    private int accessFlags;
    private int thisClass;
    private int superClass;
    private int interfacesCount;
    private int[] interfaces ;
    private int fieldsCount;
    private FieldInfo[] fields;
    private int methodsCount;
    private MethodInfo[] methods;
    private int attributesCount;
    private AttributeInfo[] attributes;
    public static final short PUBLIC = (short) 0x0001;
    public static final short FINAL = (short) 0x0010;
    public static final short SUPER = (short) 0x0020;
    public static final short INTERFACE = (short) 0x0200;
    public static final short ABSTRACT = (short) 0x0400;
    public static final int MAX_CONSTANT_POOL_SIZE = 64 * 1024;

    public void read(DataInputStream in) throws IOException {
        readMagic(in);
//         System.out.println("magic");
        readVersion(in);
//         System.out.println("version");
        readConstantPool(in);
//         System.out.println("constpool");
        readAccessFlags(in);
//         System.out.println("accflags");
        readThisClass(in);
//         System.out.println("thisclass");
        readSuperClass(in);
//         System.out.println("superclass");
        readInterfaces(in);
//         System.out.println("interfaces");
        readFields(in, constantPool);
//         System.out.println("fields");
        readMethods(in, constantPool);
//         System.out.println("methods");
        readAttributes(in, constantPool);
//         System.out.println("attributes");
        return;
    }

    public void write(DataOutputStream out) throws IOException {
        writeMagic(out);
//         System.out.println("magic");
        writeVersion(out);
//         System.out.println("version");
        writeConstantPool(out);
//         System.out.println("constpool");
        writeAccessFlags(out);
//         System.out.println("accflags");
        writeThisClass(out);
//         System.out.println("thisclass");
        writeSuperClass(out);
//         System.out.println("superclass");
        writeInterfaces(out);
//         System.out.println("interfaces");
        writeFields(out);
//         System.out.println("fields");
        writeMethods(out);
//         System.out.println("methods");
        writeAttributes(out);
//         System.out.println("attributes");
        return;
    }

    public void update() {
        /* this function will supposibly sync all class info.
        for now I am hoping that by changing to a lower version I can get
        the class loader to ignore half my problems */
        minorVersion = 0;
        majorVersion = 46;
    }

    public String[] getMethodNames() {
        String[] names = new String[methodsCount];
        for (int i = 0; (i < methodsCount); i++) {
            int index = methods[i].getNameIndex();
            names[i] = ((ConstantPoolInfo.Utf8Info) constantPool[index]).getBytes();
        }
        return names;
    }


    public int getMethodsCount(){
        return methodsCount;
    }

    public int[] getMethodsIndex(){
        int[] nameIndex=new int[methodsCount];
        for(int i=0;i<methodsCount;i++){
            nameIndex[i]=i;
        }
        return nameIndex;
    }
    public int[] getMethodCode(int methodIndex) {
        MethodInfo mi = getMethodInfo(methodIndex);
        return mi.getCode();
    }

    public void setMethodCode(int methodIndex, int[] codeBytes) {
        setMethodCode(methodIndex, codeBytes, codeBytes.length);
    }

    public void setMethodCode(int methodIndex, int[] codeBytes, int codeBytesLength) {
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.setCode(codeBytes, codeBytesLength, this);
    }

    public AttributeInfo.CodeAttribute.ExceptionEntry[] getMethodExceptionTable(int methodIndex) {
        MethodInfo mi = getMethodInfo(methodIndex);
        return mi.getExceptionTable();
    }

    public void setMethodExceptionTable(int methodIndex, AttributeInfo.CodeAttribute.ExceptionEntry[] exceptionTable) {
        setMethodExceptionTable(methodIndex, exceptionTable, exceptionTable.length);
    }

    public void setMethodExceptionTable(int methodIndex, AttributeInfo.CodeAttribute.ExceptionEntry[] exceptionTable, int exceptionTableLength) {
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.setExceptionTable(exceptionTable, exceptionTableLength, this);
    }
    public void addMethodExceptionTable(int methodIndex, AttributeInfo.CodeAttribute.ExceptionEntry exceptionTable) {
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.addException(exceptionTable, this);
    }

    public void removeMethodExceptionTable(int methodIndex, int n) {
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.removeException(n, this);
    }

    public String getClassName() {
        if (constantPool[thisClass].getTag() != ConstantPoolInfo.CLASS) {
            throw new Error("thisClass points to non-class constant pool entry");
        }

        int nameIndex = ((ConstantPoolInfo.ClassInfo) constantPool[thisClass]).getNameIndex();

        if (constantPool[nameIndex].getTag() != ConstantPoolInfo.UTF8) {
            throw new Error("thisClass constant pool entry points to non-utf8 constant pool entry");
        }

        return ((ConstantPoolInfo.Utf8Info) constantPool[nameIndex]).getBytes().replace('/', '.');
    }

    public void setClassName(String name) {
        if (constantPool[thisClass].getTag() != ConstantPoolInfo.CLASS) {
            throw new Error("thisClass points to non-class constant pool entry");
        }

        int nameIndex = ((ConstantPoolInfo.ClassInfo) constantPool[thisClass]).getNameIndex();

        if (constantPool[nameIndex].getTag() != ConstantPoolInfo.UTF8) {
            throw new Error("thisClass constant pool entry points to non-utf8 constant pool entry");
        }

        constantPool[nameIndex] = new ConstantPoolInfo.Utf8Info(name.replace('.', '/'));
    }

    /** @return index into constant pool where value is stored */
    public int addToConstantPool(Object o) {
        ConstantPoolInfo cpInfo = null;

        if (o instanceof Field) {
            Field fld = (Field) o;
            String descriptor = getDescriptor(fld.getType());

            ConstantPoolInfo nameInfo = new ConstantPoolInfo.Utf8Info(fld.getName());
            int nameIndex = addToConstantPool(nameInfo);
            ConstantPoolInfo descriptorInfo = new ConstantPoolInfo.Utf8Info(descriptor);
            int descriptorIndex = addToConstantPool(descriptorInfo);
            ConstantPoolInfo nameAndTypeInfo = new ConstantPoolInfo.NameAndTypeInfo(nameIndex, descriptorIndex);
            int nameAndTypeIndex = addToConstantPool(nameAndTypeInfo);

            Clazz cls = ((Field) o).getDeclaringClass();
            int classIndex = addToConstantPool(cls);

            cpInfo = new ConstantPoolInfo.FieldRefInfo(classIndex, nameAndTypeIndex);
        } else if (o instanceof Method) {
            Method mtd = (Method) o;
            Clazz[] params = mtd.getParameterTypes();
            StringBuffer buf = new StringBuffer("(");
            if(params != null){
                for (int i = 0; i < params.length; i++) {
                    buf.append(getDescriptor(params[i]));
                }
            }
            buf.append(")");
            buf.append(getDescriptor(mtd.getReturnType()));
            String descriptor = buf.toString();

            ConstantPoolInfo nameInfo = new ConstantPoolInfo.Utf8Info(mtd.getName());
            int nameIndex = addToConstantPool(nameInfo);
            ConstantPoolInfo descriptorInfo = new ConstantPoolInfo.Utf8Info(descriptor);
            int descriptorIndex = addToConstantPool(descriptorInfo);
            ConstantPoolInfo nameAndTypeInfo = new ConstantPoolInfo.NameAndTypeInfo(nameIndex, descriptorIndex);
            int nameAndTypeIndex = addToConstantPool(nameAndTypeInfo);

            Clazz cls = mtd.getDeclaringClass();
            int classIndex = addToConstantPool(cls);

            if (cls.isInterface()) {
                cpInfo = new ConstantPoolInfo.InterfaceMethodRefInfo(classIndex, nameAndTypeIndex);
            } else {
                cpInfo = new ConstantPoolInfo.MethodRefInfo(classIndex, nameAndTypeIndex);
            }
        } else if (o instanceof Clazz) {
            Clazz cls = (Clazz) o;
            String className = cls.getName().replace('.', '/');
            cpInfo = new ConstantPoolInfo.Utf8Info(className);
            int utf8Index = addToConstantPool(cpInfo);
            cpInfo = new ConstantPoolInfo.ClassInfo(utf8Index);
        } else if (o instanceof String) {
            cpInfo = new ConstantPoolInfo.Utf8Info((String) o);
            int utf8Index = addToConstantPool(cpInfo);
            cpInfo = new ConstantPoolInfo.StringInfo(utf8Index);
        } else if (o instanceof Integer) {
            cpInfo = new ConstantPoolInfo.IntegerInfo(((Integer) o).intValue());
        } else if (o instanceof Float) {
            cpInfo = new ConstantPoolInfo.FloatInfo(((Float) o).floatValue());
        } else if (o instanceof Long) {
            cpInfo = new ConstantPoolInfo.LongInfo(((Long) o).longValue());
        } else if (o instanceof Double) {
            cpInfo = new ConstantPoolInfo.DoubleInfo(((Double) o).doubleValue());
        } else if (o instanceof ConstantPoolInfo) {
            cpInfo = (ConstantPoolInfo) o;
        } else {
            throw new IllegalArgumentException("Invalid Class To Add To Constant Pool");
        }

        int index = searchConstantPool(cpInfo);
        if (index > 0) {
            return index;
        }

        if ((cpInfo instanceof ConstantPoolInfo.DoubleInfo) || (cpInfo instanceof ConstantPoolInfo.LongInfo)) {
            constantPool[constantPoolCount] = cpInfo;
            constantPoolMap.put(cpInfo, new Integer(constantPoolCount));
            constantPool[constantPoolCount + 1] = cpInfo;
            constantPoolCount += 2;
            return constantPoolCount - 2;
        } else {
            constantPool[constantPoolCount] = cpInfo;
            constantPoolMap.put(cpInfo, new Integer(constantPoolCount));
            constantPoolCount++;
            return constantPoolCount - 1;
        }
    }

    public int getMethodMaxStack(int methodIndex) {
        MethodInfo mi = getMethodInfo(methodIndex);
        return mi.getMaxStack();
    }
    public void setMethodOther(int methodIndex,int access,int nameIndex,int des){
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.accessFlags=access;
        mi.nameIndex=nameIndex;
        mi.descriptorIndex=des;
    }
    public void setFieldOther(int fieldIndex,int access,int nameIndex,int des){
        FieldInfo fi = fields[fieldIndex];
        fi.accessFlags=access;
        fi.nameIndex=nameIndex;
        fi.descriptorIndex=des;
    }


    public void setMethodMaxStack(int methodIndex,int maxs) {
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.setMaxStack(maxs);
    }

    public void setMethodMaxLocals(int methodIndex,int maxl) {
        MethodInfo mi = getMethodInfo(methodIndex);
        mi.setMaxLocals(maxl);
    }

    public int getMethodMaxLocals(int methodIndex) {
        MethodInfo mi = getMethodInfo(methodIndex);
        return mi.getMaxLocals();
    }

    protected String getConstantPoolFieldDescriptor(int index) {
        ConstantPoolInfo cpi = constantPool[index];
        //get name and type index from method ref
        index = ((ConstantPoolInfo.FieldRefInfo) cpi).getNameAndTypeIndex();
        cpi = constantPool[index];
        //get descriptor index from name and type
        index = ((ConstantPoolInfo.NameAndTypeInfo) cpi).getDescriptorIndex();
        cpi = constantPool[index];

        return ((ConstantPoolInfo.Utf8Info) cpi).getBytes();
    }

    protected int getFieldLength(String fieldDescriptor) {
        return getFieldLength(fieldDescriptor.charAt(0));
    }

    private int getFieldLength(char ch) {
        switch (ch) {
            case 'V':
                return 0;
            case 'B':
            case 'C':
            case 'F':
            case 'I':
            case 'L':
            case 'S':
            case 'Z':
            case '[':
                return 1;
            case 'D':
            case 'J':
                return 2;
        }
        throw new IllegalStateException();
    }

    protected String getConstantPoolUtf8(int index) {
        return ((ConstantPoolInfo.Utf8Info) constantPool[index]).getBytes();
    }
    public String getFieldDescriptor(int index){
        FieldInfo fi=fields[index];
        index=fi.getDescriptorIndex();
        return getConstantPoolUtf8(index);
    }

    public String getFieldName(int index){
        FieldInfo fi=fields[index];
        index=fi.getNameIndex();
        return getConstantPoolUtf8(index);
    }

    public String getMethodName(int index){
        MethodInfo mi=methods[index];
        index=mi.getNameIndex();
        return getConstantPoolUtf8(index);
    }

    public boolean[] getFieldFlags(int index){
        FieldInfo fi=fields[index];
        return fi.accessFlags();
    }


    public void addField(FieldInfo addfield)
    {
        int     count = fieldsCount;
        FieldInfo[] field = fields;

        if (field.length <= count)
        {
            fields=new FieldInfo[count+1];
            System.arraycopy(field, 0,fields, 0,count);
        }
        // Add the field.
        fields[fieldsCount++] = addfield;
    }
    public void addMethod(MethodInfo addmethod)
    {
        int      methodCount = methodsCount;
        MethodInfo[] method      = methods;
        if (method.length <= methodCount)
        {
            methods = new MethodInfo[methodCount+1];
            System.arraycopy(method, 0,methods, 0,methodCount);
        }
        // Add the method.
        methods[methodsCount++] = addmethod;
    }
    public void removeMethod(int methodIndex)
    {
        int      count=methodsCount;
        MethodInfo[] method      = methods;
        // Shift the method entries.
        System.arraycopy(method, methodIndex+1,
                         method, methodIndex,
                         count - methodIndex - 1);
        // Clear the last entry.
        method[--methodsCount] = null;
    }

    public void removeField(int fieldIndex)
    {
        int     count = fieldsCount;
        FieldInfo[] field      = fields;

        // Shift the field entries.
        System.arraycopy(field, fieldIndex+1,
                         field, fieldIndex,
                         count - fieldIndex - 1);

        // Clear the last entry.
        field[--fieldsCount] = null;
    }

    public String[] getFieldsNames(){
        String[] fieldsNames=new String[fieldsCount];   
        for(int i=0;i<fieldsCount;i++){
            FieldInfo fi=fields[i];
            fieldsNames[i]=getConstantPoolUtf8(fi.getNameIndex());
        }
    return fieldsNames;
    }

    public String getMethodDescriptor(int index){
        MethodInfo mi = getMethodInfo(index);
        index=mi.getDescriptorIndex();
        return getConstantPoolUtf8(index);
    }


    public boolean[] getMethodFlags(int index){
        MethodInfo mi = getMethodInfo(index);
        return mi.accessFlags();
    }

    public String getClassInfo(int index) {
        ConstantPoolInfo cpi = constantPool[index];
        index = ((ConstantPoolInfo.ClassInfo) cpi).getNameIndex();
        return getConstantPoolUtf8(index);
    }

    public void setConstantInt(int index , int val)
    {
        ConstantPoolInfo cpi = constantPool[index];
        if ( cpi instanceof ConstantPoolInfo.IntegerInfo )
            (( ConstantPoolInfo.IntegerInfo ) cpi).setBytes(val);
    }
    public void setConstantLong(int index , long val)
    {
        ConstantPoolInfo cpi = constantPool[index];
        if ( cpi instanceof ConstantPoolInfo.LongInfo )
            ( ( ConstantPoolInfo.LongInfo ) cpi ).setBytes(val);
    }
    public void setConstantFloat(int index , float val)
    {
        ConstantPoolInfo cpi = constantPool[index];
        if ( cpi instanceof ConstantPoolInfo.FloatInfo )
            ( ( ConstantPoolInfo.FloatInfo ) cpi ).setBytes(val);
    }
    public void setConstantDouble(int index , double val)
    {
        ConstantPoolInfo cpi = constantPool[index];
        if ( cpi instanceof ConstantPoolInfo.DoubleInfo )
            ( ( ConstantPoolInfo.DoubleInfo ) cpi ).setBytes(val);
    }
    public void setConstantUtf8(int index , String val)
    {
        ConstantPoolInfo cpi = constantPool[index];
        if ( cpi instanceof ConstantPoolInfo.Utf8Info )
            ( ( ConstantPoolInfo.Utf8Info ) cpi ).setBytes(val);
    }
    public Vector getConstantPoolElements(){
        int constantPoolCount=this.constantPoolCount ;
        Vector v=new Vector(constantPoolCount);
        v.addElement( "null" ) ;
        for ( int i= 1; i <constantPoolCount ; i++)
           v.addElement(constantPool[i].toString() ) ;
        return v;
    }

    public String[] getConstantPoolAllUtf8Info(){
        Vector v=new Vector();
        for ( int i= 1; i <constantPoolCount ; i++){
           if( constantPool[i] instanceof ConstantPoolInfo.Utf8Info )
                v.addElement ( constantPool[i].toString() );
        }
        String[] s=new String[v.size()];
        v.copyInto(s);
        return s;
    }

    protected String getConstantPoolMethodDescriptor(int index) {
        ConstantPoolInfo cpi = constantPool[index];
        //get name and type index from method ref
        index = ((ConstantPoolInfo.MethodRefInfo) cpi).getNameAndTypeIndex();
        cpi = constantPool[index];
        //get descriptor index from name and type
        index = ((ConstantPoolInfo.NameAndTypeInfo) cpi).getDescriptorIndex();
        cpi = constantPool[index];

        return ((ConstantPoolInfo.Utf8Info) cpi).getBytes();
    }
    public String getConstantMethod(int index)
    {
        ConstantPoolInfo cpi = constantPool[index];
        index = ((ConstantPoolInfo.MethodRefInfo) cpi).getNameAndTypeIndex();
        int classIndex=((ConstantPoolInfo.MethodRefInfo) cpi).getClassIndex();
        ConstantPoolInfo cpm=constantPool[classIndex];
        classIndex=((ConstantPoolInfo.ClassInfo) cpm).getNameIndex();
        cpm=constantPool[classIndex];
        cpi = constantPool[index];
        index = ((ConstantPoolInfo.NameAndTypeInfo) cpi).getDescriptorIndex();
        int name=((ConstantPoolInfo.NameAndTypeInfo) cpi).getNameIndex();
        ConstantPoolInfo pname = constantPool[name];
        cpi = constantPool[index];

        return ((ConstantPoolInfo.Utf8Info) cpm).getBytes()+"/"+((ConstantPoolInfo.Utf8Info) pname).getBytes()+((ConstantPoolInfo.Utf8Info) cpi).getBytes();
    }
    public String getConstantField(int index)
    {
        ConstantPoolInfo cpi = constantPool[index];
        index = ((ConstantPoolInfo.FieldRefInfo) cpi).getNameAndTypeIndex();
        int classIndex=((ConstantPoolInfo.FieldRefInfo) cpi).getClassIndex();
        ConstantPoolInfo cpm=constantPool[classIndex];
        classIndex=((ConstantPoolInfo.ClassInfo) cpm).getNameIndex();
        cpm=constantPool[classIndex];
        cpi = constantPool[index];
        index = ((ConstantPoolInfo.NameAndTypeInfo) cpi).getDescriptorIndex();
        int name=((ConstantPoolInfo.NameAndTypeInfo) cpi).getNameIndex();
        ConstantPoolInfo pname = constantPool[name];
        cpi = constantPool[index];

        return ((ConstantPoolInfo.Utf8Info) cpm).getBytes()+"/"+((ConstantPoolInfo.Utf8Info) pname).getBytes()+" "+((ConstantPoolInfo.Utf8Info) cpi).getBytes();
    }
    public String ldc(int index)
    {
        ConstantPoolInfo cpi=constantPool[index];
        if ( cpi instanceof ConstantPoolInfo.StringInfo ) {
            index=((ConstantPoolInfo.StringInfo) cpi).getStringIndex();
            cpi=constantPool[index];
            return Util.replace(((ConstantPoolInfo.Utf8Info) cpi).getBytes(),"\n","^n");
        }else if( cpi instanceof ConstantPoolInfo.IntegerInfo )
            return String.valueOf(((ConstantPoolInfo.IntegerInfo) cpi).getBytes());
        else if(  cpi instanceof ConstantPoolInfo.FloatInfo )
            return String.valueOf(((ConstantPoolInfo.FloatInfo) cpi).getBytes());
        else if (cpi instanceof ConstantPoolInfo.LongInfo )
            return String.valueOf(((ConstantPoolInfo.LongInfo) cpi).getBytes());
        else if( cpi instanceof ConstantPoolInfo.DoubleInfo )
            return String.valueOf(((ConstantPoolInfo.DoubleInfo) cpi).getBytes());
        return "";
    }

    /** @return stack delta caused by an invoke on this method descriptor -- delta = within () - outside () */
    protected int getMethodStackDelta(String methodDescriptor) {
//         System.out.println("methodDescriptor = " + methodDescriptor);
//         int end = methodDescriptor.lastIndexOf(")");
//         int begin = methodDescriptor.lastIndexOf("(", end);
//         String s = methodDescriptor.substring(begin + 1, end);
//         System.out.println("methodDescriptor = " + methodDescriptor);

        int argLength = getMethodArgLength(methodDescriptor);

        int count = 0;
        int delta = 0;
        boolean inReference = false;
        boolean inParameterDescriptor = false;
        char ch;
        for (int i = 0; i < methodDescriptor.length(); i++) {
            ch = methodDescriptor.charAt(i);
            if (ch == ')') {
                return argLength - getFieldLength(methodDescriptor.charAt(i + 1));
            }
        }
        throw new IllegalStateException("Invalid method descriptor");
    }

    /** @return count of arguments -- within () */
    int getMethodArgLength(String methodDescriptor) {
        int count = 0;
        boolean inReference = false;

        for (int i = 0; i < methodDescriptor.length(); i++) {
            char ch = methodDescriptor.charAt(i);
            switch (ch) {
                case '[':
                    while ((ch = methodDescriptor.charAt(++i)) == '[');
                    if (ch != 'L') {
                        count += 1;
                        break;
                    }
                case 'L':
                    while (methodDescriptor.charAt(++i) != ';');
                    count += 1;
                    break;
                case 'B':
                case 'C':
                case 'F':
                case 'I':
                case 'S':
                case 'Z':
                    count += 1;
                    break;
                case 'D':
                case 'J':
                    count += 2;
                    break;
                case ')':
                    return count;
                default:
                    break;
            }
        }
        throw new IllegalStateException("Invalid method descriptor");
    }

    private static String getDescriptor(Clazz cls) {
        if (cls.getName().indexOf('[') > -1) {
            return cls.getName().replace('.', '/');
        } else {
            String c = cls.getName();
            if (c.equals("B")) {
                return "B";
            } else if (c.equals("C")) {
                return "C";
            } else if (c.equals("D")) {
                return "D";
            } else if (c.equals("F")) {
                return "F";
            } else if (c.equals("I")) {
                return "I";
            } else if (c.equals("J")) {
                return "J";
            } else if (c.equals("S")) {
                return "S";
            } else if (c.equals("Z")) {
                return "Z";
            } else if (c.equals("V")) {
                return "V";
            } else {
                return c.replace('.', '/');
            }
        }
    }

    private int searchConstantPool(ConstantPoolInfo query) {
        Integer value = (Integer) (constantPoolMap.get(query));
        if (value != null) {
            return value.intValue();
        } else {
            return -1;
        }

//         for (int i = 1; i < constantPoolCount; i++) {
//             if (constantPool[i].equals(query))
//                 return i;
//         }
//         return -1;
    }

    private MethodInfo getMethodInfo(int methodIndex) {
        return methods[methodIndex];
    }

    private void readMagic(DataInputStream in) throws IOException {
        magic = in.readInt();
        if (magic != 0xCAFEBABE)
            throw new IOException("Invalid classfile magic number" + ": expected 0xCAFEBABE, found 0x" + Integer.toHexString(magic));
    }

    private void writeMagic(DataOutputStream out) throws IOException {
        out.writeInt(magic);
    }

    private void readVersion(DataInputStream in) throws IOException {
        minorVersion = in.readUnsignedShort();
        majorVersion = in.readUnsignedShort();
    }

    private void writeVersion(DataOutputStream out) throws IOException {
        out.writeShort(minorVersion);
        out.writeShort(majorVersion);
    }

    private void readConstantPool(DataInputStream in) throws IOException {
        constantPoolCount = in.readUnsignedShort();
        // be aware that constant pool indices start at 1!! (not 0)
//        constantPool = new ConstantPoolInfo[MAX_CONSTANT_POOL_SIZE];
        constantPool = new ConstantPoolInfo[5*1024];
        constantPoolMap = new Hashtable(5227);
//         constantPool = new ConstantPoolInfo[constantPoolCount];
        for (int i = 1; i < constantPoolCount; i++) {
            ConstantPoolInfo cpInfo = ConstantPoolInfo.construct(in);
            constantPool[i] = cpInfo;
            constantPoolMap.put(cpInfo, new Integer(i));
            if ((constantPool[i] instanceof ConstantPoolInfo.DoubleInfo) ||
                    (constantPool[i] instanceof ConstantPoolInfo.LongInfo)) {
                i++;
                constantPool[i] = constantPool[i - 1];
            }
        }
    }

    private void writeConstantPool(DataOutputStream out) throws IOException {
        out.writeShort(constantPoolCount);
        // be aware that constant pool indices start at 1!! (not 0)
        for (int i = 1; i < constantPoolCount; i++) {
            constantPool[i].write(out);
            if ((constantPool[i] instanceof ConstantPoolInfo.DoubleInfo) ||
                    (constantPool[i] instanceof ConstantPoolInfo.LongInfo)) {
                i++;
            }
        }
    }

    private void readAccessFlags(DataInputStream in) throws IOException {
        accessFlags = in.readUnsignedShort();
    }

    private void writeAccessFlags(DataOutputStream out) throws IOException {
        out.writeShort(accessFlags);
    }

    private void readThisClass(DataInputStream in) throws IOException {
        thisClass = in.readUnsignedShort();
    }

    private void writeThisClass(DataOutputStream out) throws IOException {
        out.writeShort(thisClass);
    }

    private void readSuperClass(DataInputStream in) throws IOException {
        superClass = in.readUnsignedShort();
    }

    private void writeSuperClass(DataOutputStream out) throws IOException {
        out.writeShort(superClass);
    }

    private void readInterfaces(DataInputStream in) throws IOException {
        interfacesCount = in.readUnsignedShort();
        interfaces = new int[interfacesCount];
        for (int i = 0; i < interfacesCount; i++) {
            interfaces[i] = in.readUnsignedShort();
        }
    }

    private void writeInterfaces(DataOutputStream out) throws IOException {
        out.writeShort(interfacesCount);
        for (int i = 0; i < interfacesCount; i++) {
            out.writeShort(interfaces[i]);
        }
    }

    private void readFields(DataInputStream in, ConstantPoolInfo[] pool) throws IOException {
        fieldsCount = in.readUnsignedShort();
        fields = new FieldInfo[fieldsCount];
        for (int i = 0; (i < fieldsCount); i++) {
            fields[i] = new FieldInfo(in, pool);
        }
    }

    private void writeFields(DataOutputStream out) throws IOException {
        out.writeShort(fieldsCount);
        for (int i = 0; (i < fieldsCount); i++) {
            fields[i].write(out);
        }
    }

    private void readMethods(DataInputStream in, ConstantPoolInfo[] pool) throws IOException {
        methodsCount = in.readUnsignedShort();
        methods = new MethodInfo[methodsCount];
        for (int i = 0; (i < methodsCount); i++) {
            methods[i] = new MethodInfo(in, pool);
        }
    }

    private void writeMethods(DataOutputStream out) throws IOException {
        out.writeShort(methodsCount);
        for (int i = 0; (i < methodsCount); i++) {
            methods[i].write(out);
        }
    }

    private void readAttributes(DataInputStream in, ConstantPoolInfo[] pool) throws IOException {
        attributesCount = in.readUnsignedShort();
        attributes = new AttributeInfo[attributesCount];
        for (int i = 0; (i < attributesCount); i++) {
            attributes[i] = AttributeInfo.construct(in, pool);
        }
    }

    private void writeAttributes(DataOutputStream out) throws IOException {
        out.writeShort(attributesCount);
        for (int i = 0; (i < attributesCount); i++) {
            attributes[i].write(out);
        }
    }
}
