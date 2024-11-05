package NumeralSystemConverter;

import java.util.Objects;
import java.util.Scanner;

public abstract class ConverterApp {

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        do{

            System.out.println("What number do you want to convert?");
            String zahl = scanner.next();
            System.out.println("In what numeral system is the number(base)?");
            int basis = scanner.nextInt();
            System.out.println("To what numeral system do you want to convert(base)?");
            int basisTo = scanner.nextInt();
            boolean steps;
            switch (loopAskIfWithSteps(scanner)){
                case 0:
                    steps = false;break;
                case 1:
                    steps = true;break;
                case -1:
                    System.out.println("Quitting..");
                    break;
                default:
                    System.out.println("Error occured, quitting..");
                    break;
            }

            // converter logic here

            // repeat
            break;
        }while (true);
        scanner.close();
        System.out.println("Converter app: exiting");
    }// end

    private static byte loopAskIfWithSteps(Scanner scanner){
        String withSteps;
        boolean isYes;
        boolean isNo;
        do {
            System.out.println("Convert with steps? \n('y' = yes | 'n' = no | 'quit' to quit)");
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
