package be.nidel.kinomichi.base;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
    protected int id = -1;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
