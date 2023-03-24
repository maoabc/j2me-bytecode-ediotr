package classfile;

public class MethodAnaly{
    String returnType="";
    String param="";
    String methodName="";
    String className="";
    public MethodAnaly(String s){
        int i=s.lastIndexOf(')');
        returnType=s.substring(i+1);
        s=s.substring(0,i);
        i=s.lastIndexOf( '(' );
        param=s.substring(i+1);
        s=s.substring(0,i);
        i=s.lastIndexOf('/');
        methodName=s.substring(i+1);
        className=s.substring(0,i);
    }
    public String getReturn(){
        return returnType;
    }
    public String getParam(){
        return param;
    }
    public String getName(){
        return methodName;
    }
    public String getClazz(){
        return className;
    }
}