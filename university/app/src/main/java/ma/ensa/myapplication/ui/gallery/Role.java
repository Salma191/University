package ma.ensa.myapplication.ui.gallery;

public class Role {
    private static int lastId = 0;
    private int id;
    private String name;


    public Role(String name) {
        this.id = ++lastId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n" + name + "\n";
    }
}
