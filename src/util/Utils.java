package util;

import java.io.*;
import java.util.Vector;
import javax.microedition.io.file.*;
import javax.microedition.io.Connector;

public class Utils {
    public static int replaceCount=0;
    public static String byteArrayToString(byte[] bytes, String encoding) {
        char[] map = "\u0402\u0403\u201A\u0453\u201E\u2026\u2020\u2021\u20AC\u2030\u0409\u2039\u040A\u040C\u040B\u040F\u0452\u2018\u2019\u201C\u201D\u2022\u2013\u2014\uFFFD\u2122\u0459\u203A\u045A\u045C\u045B\u045F\u00A0\u040E\u045E\u0408\u00A4\u0490\u00A6\u00A7\u0401\u00A9\u0404\u00AB\u00AC\u00AD\u00AE\u0407\u00B0\u00B1\u0406\u0456\u0491\u00B5\u00B6\u00B7\u0451\u2116\u0454\u00BB\u0458\u0405\u0455\u0457\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u0419\u041A\u041B\u041C\u041D\u041E\u041F\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042A\u042B\u042C\u042D\u042E\u042F\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u0439\u043A\u043B\u043C\u043D\u043E\u043F\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044A\u044B\u044C\u044D\u044E\u044F".toCharArray();

        if (encoding.equals("UTF-8")) {
            encoding = "ISO-8859-1";
        }
        try {
            return decodeUTF8(bytes, false);
        } catch (UTFDataFormatException udfe) {
        }

        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            chars[i] = (b >= 0) ? (char) b : map[b + 128];
        }

        return new String(chars);
    }

    public static byte[] encodeUTF8(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ret;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c != '\u0000' && c < '\u0080') {
                baos.write(c);
            } else if (c == '\u0000' || (c >= '\u0080' && c < '\u0800')) {
                baos.write((byte) (0xc0 | (0x1f & (c >> 6))));
                baos.write((byte) (0x80 | (0x3f & c)));
            } else {
                baos.write((byte) (0xe0 | (0x0f & (c >> 12))));
                baos.write((byte) (0x80 | (0x3f & (c >> 6))));
                baos.write((byte) (0x80 | (0x3f & c)));
            }
        }
        ret = baos.toByteArray();

        return ret;
    }

    private static String decodeUTF8(byte[] data, boolean gracious) throws UTFDataFormatException {
        byte a, b, c;
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < data.length; i++) {
            try {
                a = data[i];
                if ((a & 0x80) == 0) {
                    ret.append((char) a);
                } else if ((a & 0xe0) == 0xc0) {
                    b = data[i + 1];
                    if ((b & 0xc0) == 0x80) {
                        ret.append((char) (((a & 0x1F) << 6) | (b & 0x3F)));
                        i++;
                    } else {
                        throw new UTFDataFormatException("Illegal 2-byte group");
                    }
                } else if ((a & 0xf0) == 0xe0) {
                    b = data[i + 1];
                    c = data[i + 2];
                    if (((b & 0xc0) == 0x80) && ((c & 0xc0) == 0x80)) {
                        ret.append((char) (((a & 0x0F) << 12) | ((b & 0x3F) << 6) | (c & 0x3F)));
                        i += 2;
                    } else {
                        throw new UTFDataFormatException("Illegal 3-byte group");
                    }
                } else if (((a & 0xf0) == 0xf0) || ((a & 0xc0) == 0x80)) {
                    throw new UTFDataFormatException("Illegal first byte of a group");
                }
            } catch (UTFDataFormatException udfe) {
                if (gracious) {
                    ret.append("?");
                } else {
                    throw udfe;
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                if (gracious) {
                    ret.append("?");
                } else {
                    throw new UTFDataFormatException("Unexpected EOF");
                }
            }
        }

        return ret.toString();
    }

    public static String[] splitString(String str, String delim) {
        if (str == null) {
            return null;
        } else if (str.equals("") || delim == null || delim.length() == 0) {
            return new String[]{str};
        }
        String[] s;
        Vector v = new Vector();
        int pos, newpos;

        pos = 0;
        newpos = str.indexOf(delim, pos);

        while (newpos != -1) {
            v.addElement(str.substring(pos, newpos));
            pos = newpos + delim.length();
            newpos = str.indexOf(delim, pos);
        }
        v.addElement(str.substring(pos));
        s = new String[v.size()];
        v.copyInto(s);
        return s;
    }

    public static String stringDelete(String str, int startIndex, int endIndex) {
        if (endIndex < str.length()) {
            return str.substring(0, startIndex).concat(str.substring(endIndex));
        } else {
            return str.substring(0, str.length() - 1);
        }
    }

    public static String stringDelete(String str, int index) {
        return stringDelete(str, index, index + 1);
    }

    public static String stringInsert(String str, String ins, int index) {
        if (index > 0) {
            return str.substring(0, index) + ins + str.substring(index);
        } else {
            return ins + str;
        }
    }

    public static String stringReplace(String str, String replace, int index) {
        return stringInsert(stringDelete(str, index, index + replace.length()), replace, index);
    }
    public static String replace(String source, String from, String to)
    {
        if (source==null || from == null || to == null)
        {
            return null;
        }
        StringBuffer bf = new StringBuffer();
        int index = -1 , i=0;
        while ((index = source.indexOf(from)) != -1)
        {
            bf.append(source.substring(0, index) );
            bf.append(to);
            source = source.substring(index + from.length());
            index = -1;
            i++;
        }
        replaceCount=i;
        bf.append(source);
        return bf.toString();
    }

    public static int binarySearch(int[] a, int key){
        int low = 0;
        int high = a.length - 1;
        while(low <= high) {
            int mid = (low + high)>>>1;
            int midVal = a[mid];
            if(midVal < key)
                low = mid + 1;
            else if(midVal > key)
                high = mid - 1;
            else
                return mid;
        }
        return -1;
    }

    public static void maoPao(int[] x) {
        for(int i =0; i <x.length; i++) {
            for(int j =i +1; j <x.length; j++) {
                if(x[i] >x[j]) {
                    int temp =x[i];
                    x[i] =x[j];
                    x[j] = temp;
                }
            }
        }
    }

    public static boolean isIn(DataInputStream dis,String str) {
         try{
             if( dis.readInt() !=0xcafebabe )
            throw new IOException("Invalid classFile");
            dis.skip(6);
            while (true){
                switch( dis.readUnsignedByte() ){
                    case 3:
                    case 4:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        dis.skip(4);
                        break;
                    case 5:
                    case 6:
                        dis.skip(8);
                        break;
                    case 7:
                    case 8:
                        dis.skip(2);
                        break;
                    case 1:
                        String s=dis.readUTF();
                        if (s.toLowerCase().indexOf(str.toLowerCase() ) != -1){
                            dis.close();
                            return true;
                        }
                        break;
                    default:
                        dis.close();
                        return false;
                }
            }
        }catch(Exception e){return false;}
    }



}
