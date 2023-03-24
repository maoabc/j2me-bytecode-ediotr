package util;

import java.util.Vector;

public class Util {
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

    public static String delete(String str, int startIndex, int endIndex) {
        if (endIndex < str.length()) {
            return str.substring(0, startIndex).concat(str.substring(endIndex));
        } else {
            return str;
        }
    }

    public static String delete(String str, int index) {
        return delete(str, index, index + 1);
    }

    public static String insert(String str, String ins, int index) {
        if (index > 0) {
            return str.substring(0, index) + ins + str.substring(index);
        } else {
            return ins + str;
        }
    }

    public static String replace(String source, String from, String to)
    {
        if (source == null || from == null || to == null)
        {
            return null;
        }
        StringBuffer bf = new StringBuffer();
        int index = -1;
        while ((index = source.indexOf(from)) != -1)
        {
            bf.append(source.substring(0, index) );
            bf.append(to);
            source = source.substring(index + from.length());
            index = -1;
        }
        bf.append(source);
        return bf.toString();
    }
    public static String addBlank(String str)
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < str.length() ; i++)
        {
            char ch = str.charAt(i);
            if(i<str.length()-1){
                char c=str.charAt(i+1);
                if((ch == ';')||(ch == 'B')||(ch == 'I')||(ch == 'J')||(ch == 'Z')||(ch == 'C')||(ch == 'D')||(ch == 'S')||(ch == 'F'))
                {
                    if((c == 'L')||(c == 'B')||(c == 'I')||(c == 'J')||(c == 'Z')||(c == 'C')||(c == 'D')||(c == 'S')||(c == 'F')||(c == '['))
                    {
                        sb.append(ch);
                        sb.append(' ');
                    }else
                    {
                        sb.append(ch);
                    }
                }else
                {
                    sb.append(ch);
                }
            }else
            {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String toHexString( int[] b ){
        StringBuffer sb=new StringBuffer(b.length);
        for(int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[ i ] );
            if(hex.length() == 1)
                sb.append(0);
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
