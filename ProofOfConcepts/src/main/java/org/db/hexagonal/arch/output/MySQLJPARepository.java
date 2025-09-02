package org.db.hexagonal.arch.output;

public class MySQLJPARepository implements DataOutputInterface {
    @Override
    public void addData(String string) {
        System.out.println("Adding data to database");
    }

    @Override
    public void removeData(String string) {
        System.out.println("Removing data to database");
        System.out.println(string);
    }

    @Override
    public void updateData(String string) {
        System.out.println("Updating data to database");
        System.out.println(string);
    }
}
