package classfile;

import java.util.Vector;
public class Disasm{
    ClassFile cf;
    public Disasm(ClassFile cf)
    {
        this.cf=cf;
    }
    public String disasm(int[] code)
    {
        StringBuffer sb=new StringBuffer();
        int i=0;
        while(i<code.length)
        {
            sb.append("  ");
            switch(JavaOpcode.getOpcodeLengthNew(code,i))
            {
                case 1:
                     
                    sb.append(JavaOpcode.toString(code[i++]));
                    sb.append("\n");
                    break;
                case 2:
                    switch(code[i])
                    {
                        case JavaOpcode.LDC:
                             
                            sb.append(JavaOpcode.toString(code[i++]));
                            sb.append(" ");
                            sb.append(cf.ldc(code[i++]));
                            sb.append("\n");
                            break;
                        case JavaOpcode.NEWARRAY:
                             
                            sb.append(JavaOpcode.toString(code[i++]));
                            sb.append(" ");
                            switch(code[i])
                            {
                                case 0x08:
                                    sb.append("byte");
                                    i++;
                                    break;
                                case 0x05:
                                    sb.append("char");
                                    i++;
                                    break;
                                case 0x9:
                                    sb.append("short");
                                    i++;
                                    break;
                                case 0x0a:
                                    sb.append("int");
                                    i++;
                                    break;
                                case 0x06:
                                    sb.append("float");
                                    i++;
                                    break;
                                case 0x0b:
                                    sb.append("long");
                                    i++;
                                    break;
                                case 0x07:
                                    sb.append("double");
                                    i++;
                                    break;
                                case 0x04:
                                    sb.append("boolean");
                                    i++;
                                    break;
                            }
                            sb.append("\n");
                            break;
                        default:
                             
                            sb.append(JavaOpcode.toString(code[i++]));
                            sb.append(" ");
                            sb.append(String.valueOf(code[i++]));
                            sb.append("\n");
                    }
                    break;
                case 3:
                    if(code[i]==JavaOpcode.LDC_W || code[i]==JavaOpcode.LDC2_W)
                    {
                         
                        sb.append(JavaOpcode.toString(code[i++]));
                        sb.append(" ");
                        int index=(code[i++]<<8)|code[i++];
                        sb.append(cf.ldc(index));
                        sb.append("\n");
                    }else if(JavaOpcode.isField(code[i]))
                    {
                         
                        sb.append(JavaOpcode.toString(code[i++]));
                        sb.append(" ");
                        sb.append(cf.getConstantField((code[i++]<<8)|code[i++]));
                        sb.append("\n");
                    }else if(JavaOpcode.isMethod(code[i]))
                    {
                         
                        sb.append(JavaOpcode.toString(code[i++]));
                        sb.append(" ");
                        sb.append(cf.getConstantMethod((code[i++]<<8)|code[i++]));
                        sb.append("\n");
                    }else if(JavaOpcode.isBranchInstruction(code[i])||code[i]==JavaOpcode.SIPUSH)
                    {
                         
                        sb.append(JavaOpcode.toString(code[i++]));
                        sb.append(" ");
                        sb.append(String.valueOf((short)(code[i++]<<8)|code[i++]));
                        sb.append("\n");
                    }else if(code[i]==JavaOpcode.IINC)
                    {
                         
                        sb.append(JavaOpcode.toString(code[i++]));
                        sb.append(" ");
                        sb.append(String.valueOf(code[i++]));
                        sb.append(" ");
                        sb.append(String.valueOf(code[i++]));
                        sb.append("\n");
                    }else
                    {
                         
                        sb.append(JavaOpcode.toString(code[i++]));
                        sb.append(" ");
                        sb.append(cf.getClassInfo((code[i++]<<8)|code[i++]));
                        sb.append("\n");
                    }
                    break;
                case 5:
                     
                    sb.append(JavaOpcode.toString(code[i++]));
                    sb.append(" ");
                    sb.append(cf.getConstantMethod((code[i++]<<8)|code[i++]));
                    sb.append(" ");
                    sb.append(String.valueOf(code[i]));
                    i+=2;
                    sb.append("\n");
                    break;
                case 4:
                     
                    sb.append(JavaOpcode.toString(code[i++]));
                    sb.append(' ');
                    int index=(code[i++]<<8)|code[i++];
                    sb.append(cf.getClassInfo(index));
                    sb.append(' ');
                    sb.append(String.valueOf(code[i++]));
                    sb.append("\n");
                    break;
                case 6:
                     
                    sb.append(JavaOpcode.toString(code[i++]));
                    sb.append("&"+JavaOpcode.toString(code[i++]));
                    sb.append(' ');
                    int inde=(code[i++]<<8)|code[i++];
                    sb.append(String.valueOf(inde) );
                    sb.append(' ');
                    int add=(code[i++]<<8)|code[i++];
                    sb.append(String.valueOf(add) );
                    sb.append("\n");
                    break;
                case JavaOpcode.TABLESWITCH:
                     
                    sb.append(JavaOpcode.toString(code[i++]));
                    sb.append(' ');
                    int x=tableswitch(sb, code, i);
                    i+=x;
                    break;
                case JavaOpcode.LOOKUPSWITCH:
                     
                    sb.append(JavaOpcode.toString(code[i++]));
                    sb.append(' ');
                    int z=lookupswitch(sb, code, i);
                    i+=z;
                    break;
            }
        }
    return sb.toString();
    }
    public int tableswitch(StringBuffer sb, int[] code,int i)
    {
        int start=i;
        for(;i<start+4;i++)
        {
            if(i%4==0)
            {
                break;
            }
        }
        int defaul = (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
        i+=4;
        sb.append(String.valueOf(defaul));
        sb.append(' ');
        int low = (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
        i+=4;
        int high = (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
        i+=4;
        for(int j=0;j<high-low+1;j++)
        {
            int lable= (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
        i+=4;
            sb.append(String.valueOf(low+j));
            sb.append(":");
            sb.append(String.valueOf(lable));
            if(j<high-low)
                sb.append(',');
        }
    sb.append("\n");
    return i-start;
    }

    public int lookupswitch(StringBuffer sb, int[] code,int i)
    {
        int start=i;
        for(;i<start+4;i++)
        {
            if(i%4==0)
            {
                break;
            }
        }
        int defaul = (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
        i+=4;
        sb.append(String.valueOf(defaul));
        sb.append(' ');
        int npair = (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
        i+=4;
        for(int j=0;j<npair;j++)
        {
            int key= (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
            i+=4;
            sb.append(String.valueOf(key));
            sb.append(':');
            int value= (code[i] << 24) | (code[i +1] << 16) | (code[i +2] << 8) | (code[i +3]);
            i+=4;
            sb.append(String.valueOf(value));
            if(j<npair-1)
                sb.append(',');
        }
    sb.append("\n");
    return i-start;
    }
}