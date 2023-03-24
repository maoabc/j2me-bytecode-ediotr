package classfile;

public class Clazz {

    String name = "";
    boolean bInterface = false;

    public Clazz(String n, boolean i) {
        name = n;
        bInterface = i;
    }

    public Clazz(String nm) {
        this(nm, false);
    }

    public Clazz(Class cls) {
        this(cls.getName(), cls.isInterface());
    }

    public String getName() {
        return name;
    }

    public boolean isInterface() {
        return bInterface;
    }
}
