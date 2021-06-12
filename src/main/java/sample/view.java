package sample;

import java.util.Scanner;

public class view {

    public static Scanner scanner = new Scanner(System.in);

    public static String scanStr(){

        return scanner.nextLine();
    }

    public static int scanInt(){
        return Integer.parseInt(scanner.nextLine());
    }

    public static void print(Object ... objs){
        for(Object obj: objs){
            System.out.println(obj);
        }
    }
}
