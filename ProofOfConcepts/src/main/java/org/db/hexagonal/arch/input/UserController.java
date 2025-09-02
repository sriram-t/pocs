package org.db.hexagonal.arch.input;

public class UserController implements UserInputInterface {
    @Override
    public void addData(String string) {
        System.out.println("Adding data from user to core-service");
    }

    @Override
    public void removeData(String string) {
        System.out.println("Removing data from user to core-service");
    }

    @Override
    public void updateData(String string) {
        System.out.println("Updating data from user to core-service");
    }
}
