package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class OrderScheduler {
    public static void main(String[] args) throws IOException {

        OrderCountOpimalization opt = new OrderCountOpimalization();
        char choice;

        //a simple menu to choose the type of algorithm
        System.out.println(" ============MENU============ ");
        System.out.println("-----------------------------");
        System.out.println("1.  Optimize by quantity");
        System.out.println("2.  Optimize by values");
        System.out.println("-----------------------------");
        System.out.println("Choice: ");

        Scanner sc1 = new Scanner(System.in);
        choice =  sc1.nextLine().charAt(0);

        switch (choice) {
            case '1' -> show(opt.findOptimum(args, true));
            case '2' -> show(opt.findOptimum(args, false));
            default -> System.out.println("POMYLKA!");
        }
    }

    //function to show the final result on the console
    public static void show(ArrayList<String> score){
        for(int i = 0; i < score.size(); i++){
            System.out.println(score.get(i));
        }
    }


}

