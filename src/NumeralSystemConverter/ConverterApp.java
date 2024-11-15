package NumeralSystemConverter;

import java.awt.*;
import java.util.Scanner;

public abstract class ConverterApp {

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        do{

            System.out.println("What number do you want to convert?");
            String zahl = scanner.next();
            if(zahl.equalsIgnoreCase("q")){
                break;
            }
            System.out.println(Colors.BLACK_BACKGROUND_BRIGHT + "In what numeral system is the number(base)?"+Colors.RESET);
            int base = scanner.nextInt();
            System.out.println(Colors.BLACK_BACKGROUND_BRIGHT + "To what numeral system do you want to convert(base)?"+Colors.RESET);
            int baseTo = scanner.nextInt();
            switch (loopAskIfWithSteps(scanner)){
                case 0:
                    if(baseTo == 10){
                        System.out.println(Converter.convertToBase10(zahl, base));
                    }else if(base == 10){
                        System.out.println(Converter.convertFromBase10(zahl, baseTo));
                    }
                    break;
                case 1:
                    if(baseTo == 10){
                        System.out.println(Converter.convertToBase10(zahl, base, true));
                    }else if(base == 10){
                        System.out.println(Converter.convertFromBase10(zahl, baseTo, true));
                    }
                    break;
                case -1:
                    System.out.println("Quitting..");
                    break;
                default:
                    System.out.println("Error occured, quitting..");
                    break;
            }

            // converter logic here

            // repeat
        }while (true);
        scanner.close();
        System.out.println("Converter app: exiting");
    }// end

    private static byte loopAskIfWithSteps(Scanner scanner){
        String yesText = Colors.GREEN+"'y' = yes"+Colors.RESET;
        String noText = Colors.RED+"'n' = no"+Colors.RESET;
        String quitText = Colors.RED_BACKGROUND_BRIGHT+"'quit' to quit"+Colors.RESET;

        String withSteps;
        boolean isYes;
        boolean isNo;
        do {
            System.out.println("Convert with steps? \n("+ yesText +" | "+ noText +" | "+ quitText +" )");
            withSteps = scanner.next();
            isYes = withSteps.strip().equalsIgnoreCase("y");
            isNo = withSteps.strip().equalsIgnoreCase("n");
            if (isYes) {
                return 1;
            } else if (isNo) {
                return 0;
            } else if(withSteps.strip().equalsIgnoreCase("quit")){
                return -1;
            } else {
                System.out.println("error: not y or n");
            }
        } while (!isNo && !isYes);
        return -2;
    }// end

}// Class end
