package be.nidel.kinomichi.base;

public abstract class BaseEntity {
    protected int id = -1;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
