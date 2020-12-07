package tn.esprit.setoutlife.entities;

public class Task {
    String name;
    String description;
    String color;

    public Task() { }

    public Task(String name,String description,String color) {
        this.name=name;
        this.description=description;
        this.color=color;
    }

    public Task(String name,String description ) {
        this.name=name;
        this.description=description;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
