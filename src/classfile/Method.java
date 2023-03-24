package classfile;

/**
 *
 * @author P@bloid
 */
public class Method {

    Clazz type;
    String name = "";
    Clazz classDeclared;
    Clazz[] parameterTypes;

    public Method(Clazz cls, Clazz tp, String nm, Clazz[] param) {
        type = tp;
        name = nm;
        classDeclared = cls;
        parameterTypes = param;
    }

    public Clazz getReturnType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Clazz getDeclaringClass() {
        return classDeclared;
    }

    public Clazz[] getParameterTypes() {
        return parameterTypes;
    }
}
