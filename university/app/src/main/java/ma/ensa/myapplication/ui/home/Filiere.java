package ma.ensa.myapplication.ui.home;

public class Filiere {
    private static int lastId = 0;
    private int id;
    private String code;
    private String name;

    public Filiere(String code, String name) {
        this.id = ++lastId;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nCode: " + code + "\n\nName: " + name + "\n";
    }
}
