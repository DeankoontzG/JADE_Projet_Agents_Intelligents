package bdi;

public class Intention {
    private String name;
    private int priority;

    public Intention(String name){
      this.name = name;
      this.priority = 0;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
