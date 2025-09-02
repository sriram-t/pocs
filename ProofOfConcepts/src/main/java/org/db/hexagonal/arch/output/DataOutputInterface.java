package org.db.hexagonal.arch.output;

public interface DataOutputInterface {
    public void addData(String string);
    public void removeData(String string);
    public void updateData(String string);
}
