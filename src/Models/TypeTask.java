package Models;

public class TypeTask {
    private String name;
    private  String icon;
    private String color;

    public TypeTask(String name, String icon,String color) {
        this.name = name;
        this.icon = icon;
        this.color =color;
    }

    public TypeTask() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
