package be.intec.utils;

import be.intec.models.Student;
import org.jeasy.random.EasyRandom;

public class MainApp {

    public static void main(String[] args) {
        EasyRandom random=new EasyRandom();
        Student student =random.nextObject(Student.class);
        student.setAge(Math.abs(student.getAge()));

//        if(!student.getEmail().contains("@")){
//            student.setEmail(student.getEmail().substring(0,student.getEmail().length()/2)+"@"+
//                    student.getEmail().substring(student.getEmail().length()/2,student.getEmail().length()));
//        }

        System.out.println(student);
    }
}
