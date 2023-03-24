package classfile;

/**
 *
 * @author P@bloid
 */
public class Field {

    Clazz type;
    String name = "";
    Clazz classDeclared = null;

    public Field(Clazz tp, String nm, Clazz cls) {
        type = tp;
        name = nm;
        classDeclared = cls;
    }

    public Clazz getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Clazz getDeclaringClass() {
        return classDeclared;
    }
}
