package andyroo.agility;


public class Obstacle {
    public enum Type {
        TIGHTROPE, HANDHOLDS, GAP, LEDGE, EDGE
    }

    private int id;
    private Type type;
    private int[] bounds;

    public Obstacle(int id, Type type) {
        this.id = id;
        this.type = type;
    }

    public Obstacle(int id, Type type, int[] bounds) {
        this.id = id;
        this.type = type;
        this.bounds = bounds;
    }

    public Obstacle(Obstacle o) {
        this.id = o.id;
        this.type = o.type;
        this.bounds = o.bounds;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public int[] getBounds() {
        return bounds;
    }

    public String getAction() {
        if (type == Type.TIGHTROPE || type == Type.HANDHOLDS)
            return "Cross";
        if (type == Type.GAP || type == Type.LEDGE || type == Type.EDGE)
            return "Jump";

        return null;
    }
}
