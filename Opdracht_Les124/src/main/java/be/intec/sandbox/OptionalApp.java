package be.intec.sandbox;

import java.util.Optional;

public class OptionalApp {

    public static void main(String[] args) {

        //   Long number = sum(10L, -5L);
        Optional<Long> oNumber = Optional.ofNullable(sum(10L, -5L));


        // System.out.println(number);
        System.out.println(oNumber);

        if(oNumber.isPresent()){
            System.out.println("The number is NOT null..");
        }

        if(oNumber.isEmpty()){
            System.out.println("The number is surely NULL, bad code..");
        }

        System.out.println("Do more important work here..");

    }


    public static Long sum(Long n1, Long n2) {
        if (n1 < 0 || n2 < 0) {
            return null;
        } else {
            return n1 + n2;
        }
    }


}
