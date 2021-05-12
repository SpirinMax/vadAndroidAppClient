package entites;

public class TypeRequests {
    private int iconResourse;
    private String name;
    private int iconCheck;

    public TypeRequests(int iconResourse, String name, int iconCheck) {
        this.iconResourse = iconResourse;
        this.name = name;
        this.iconCheck = iconCheck;
    }

    public int getIconResourse() {
        return iconResourse;
    }

    public void setIconResourse(int iconResourse) {
        this.iconResourse = iconResourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconCheck() {
        return iconCheck;
    }

    public void setIconCheck(int iconCheck) {
        this.iconCheck = iconCheck;
    }
}
