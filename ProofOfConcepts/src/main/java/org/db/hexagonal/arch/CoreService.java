package org.db.hexagonal.arch;

import org.db.hexagonal.arch.input.UserController;
import org.db.hexagonal.arch.input.UserInputInterface;
import org.db.hexagonal.arch.output.DataOutputInterface;
import org.db.hexagonal.arch.output.MySQLJPARepository;

public class CoreService  {
    UserInputInterface userInputInterface = new UserController();
    DataOutputInterface dataOutputInterface = new MySQLJPARepository();

    public  void implementCore(String[] args) {
        userInputInterface.addData("assa");
        System.out.println("IMPLEMENT CORE BUSINESS LOGIC");
        dataOutputInterface.addData("assas");
    }
}
