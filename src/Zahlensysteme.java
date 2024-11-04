import java.util.ArrayList;
import java.util.Scanner;

public abstract class Zahlensysteme {
    public static void start(){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("Welche Zahl?");
            String zahl = scanner.next();
            System.out.println("Im welchen Zahlensystem ist die Zahl(Basis)?");
            int basis = scanner.nextInt();
            System.out.println("Zu welchen Zahlensystem soll es umgewandelt werden(Basis)?");
            int basisTo = scanner.nextInt();
            boolean steps;

            while(true){
                System.out.println("Soll es mit rechenschritten sein? \n(y = yes/ n = no)");
                String withSteps = scanner.next();
                if(withSteps.equalsIgnoreCase("y")){
                    steps = true;
                    break;
                } else if (withSteps.equalsIgnoreCase("n")) {
                    steps = false;
                    break;
                }else{
                    System.out.println("error: not y or n");
                }
            }

            if(steps){
                System.out.println(convert(zahl, basis, basisTo, true));
            }else{
                System.out.println(zahl+"("+basis+") -> " + convert(zahl,basis,basisTo,false)+"("+basisTo+")");
            }

            boolean repeat;
            while(true){
                System.out.println("\n\nEine andere Zahl umwandeln?? \n(y = yes/ n = no)");
                String again = scanner.next();
                if(again.equalsIgnoreCase("y")){
                    repeat = true;
                    break;
                } else if (again.equalsIgnoreCase("n")) {
                    repeat = false;
                    break;
                }else{
                    System.out.println("error: not y or n");
                }
            }
            if(!repeat){break;}
        }
        scanner.close();
    }

    public static String convert(String zahl, int base, int baseto, boolean withSteps) {
        zahl = stripFirstZeros(zahl);
        String result = "";
        if(!isValidNumberInBase(zahl,base)){return "Zahl ist nicht im richtigen Zahlensystem";}
        if(!withSteps) {
            if (base == baseto) {
                result = zahl;
            } else if (baseto == 10) {
                result = convertToTen(zahl, base, false);
            } else if (base == 10) {
                result = convertFromTen(zahl, baseto, false);
            }
            else if (isPowerOfTwo(base, baseto)){
                result = groupingConvert(zahl, base, baseto, false);
            }
            else {
                result = convertFromTen(convertToTen(zahl, base, false), baseto, false);
            }
        }
        else{
            if(base == baseto){return "Die Basis ist gleich";}
            String start = zahl+"("+base+")  zu  Basis " + baseto + ": \n";
            result += start;

            String steps = "\n";
            if(baseto == 10){
                steps += convertToTen(zahl, base, true);
            }else if(base == 10){
                steps += convertFromTen(zahl, baseto, true);
            }else if(isPowerOfTwo(base, baseto)){
                steps += groupingConvert(zahl, base, baseto, true);
            }
            else{
                steps += convertToTen(zahl, base, true)+"\n";
                steps += convertFromTen(convertToTen(zahl, base, false), baseto, true);
            }
            result += steps;

            String end = "\n"+zahl+"("+base+") -> " + (convert(zahl, base, baseto,false)) + "("+baseto+")";
            result += end;
            return result;
        }

        return result;
    }

    // ---------------------------

    private static String convertToTen(String zahl, int base, boolean withSteps){
        int result = 0;
        char[] numberArray = zahl.toCharArray();
        int i = numberArray.length;
        String steps = "";
        String step1 = "";
        String step2 = "";
        String step3 = "";

        ArrayList<Integer> step3Array = new ArrayList<Integer>();

        for(char c : numberArray){
            i--;
            int numericValue = Character.getNumericValue(c);
            int potenziert = (int)Math.pow(base, i);

            result += (numericValue * potenziert);

            step1 +=  numericValue + "*" + base + "^"+i;
            step2 +=  numericValue + "*" + potenziert;

            int step3Num = numericValue * (potenziert);
            if(step3Num != 0){step3Array.add(step3Num);}
            if(i != 0){
                step1 += " + ";
                step2 += " + ";
            }
        }
        step1 += "\n"; step2 +="\n";


        int listSize = step3Array.size();
        for(int j=0; j<listSize; j++){
            step3 += step3Array.get(j);
            if(j != listSize-1){
                step3 += " + ";
            }
        }

        step3 += " = " + result + "(10)\n";

        steps += step1;
        steps += step2;
        steps += step3;
        return withSteps ? steps : result+"";
    }
    private static String convertToTen(int zahl, int base, boolean withSteps){
        return convertToTen(zahl+"", base, withSteps);
    }

    private static String convertFromTen(String zahl, int base, boolean withSteps){
        String result = "";
        int num = Integer.parseInt(zahl);

        String steps = "";

        while(num != 0){
            steps += num+"/"+base+"\t=\t"+(num/base)+"\t|\t" + num+" mod "+base+" = "+(num%base);
            char numCharacter = Character.toUpperCase(Character.forDigit(num%base, base));
            steps += (num%base > 9) ? " -> "+numCharacter+"\n" : "\n";
            result = (numCharacter + result);
            num = num /base;
        }

        return withSteps ? steps : result;
    }
    private static String convertFromTen(int zahl, int base, boolean withSteps){
        return convertFromTen("" + zahl, base, withSteps);
    }

    private static boolean isPowerOfTwo(int base, int baseTo){
        String baseBinary = Integer.toBinaryString(base);
        String baseToBinary = Integer.toBinaryString(baseTo);
        baseBinary = baseBinary.replaceAll("0", "");
        baseToBinary = baseToBinary.replaceAll("0", "");
        return baseBinary.length() == 1 && baseToBinary.length() == 1;
    }
    public static boolean isValidNumberInBase(String zahl, int base){

        for(char c : zahl.toCharArray()){
            char newChar = Character.forDigit(Character.getNumericValue(c),base);
            boolean isPartOfSystem =  Character.isDigit(newChar) || Character.isLetter(newChar);
            if(!isPartOfSystem){return false;}
        }

        return true;
    }


    private static String groupingConvert(String zahl, int base, int baseTo, boolean withSteps){
        char[] charArray = zahl.toCharArray();
        int a = 0;
        int power = (int)  (Math.log(baseTo)/Math.log(2));
        String tmp ="";
        String result = "";
        String start = "";
        String steps = "";
        for(int i = charArray.length-1; i>= 0; i--){
            tmp = charArray[i]+tmp;
            a++;
            if(a == power || i == 0){
                start = (i==0 ? tmp:"_"+tmp) + start;
                String ten = Zahlensysteme.convertToTen(tmp, base, false);
                String fromTen = Zahlensysteme.convertFromTen(ten, baseTo, false);
                if(withSteps){
                    steps += tmp + "("+base+") zu Basis 10 -> "+ten+"(10)\n" ;
                    steps += "\t\t\t\t\t"+ten+"(10) -> "+ fromTen + "("+baseTo+")" + "\n";
                }
                result = fromTen + result;

                tmp = "";
                a = 0;
            }
        }
        start += "\n";
        if(withSteps){
            result = start+"\n"+steps+"\n";
        }
        return result;
    }
    private static String stripFirstZeros(String zahl){
        char[] zahlArray = zahl.toCharArray();
        boolean foundNumber = false;
        zahl = "";
        for(char c : zahlArray){
            if(!foundNumber && c == '0'){continue;}
            else {
                foundNumber= true;
                zahl += c;
            }
        }
        return zahl;
    }
    private static String[] KommaSplit(String zahl){
        return zahl.split(",");
    }

    public static String convertKommaToBaseFromTen(String zahl, int base, int baseTo){
        String[] split = KommaSplit(zahl);
        String vorKomma = split[0];
        String nachKomma = split[1];
        int enoughLength = (int)Math.ceil((Math.log(Math.pow(base,nachKomma.length()))/Math.log(baseTo)));
        String kommaErgebnis = "";

        double rechenZahl;
        try {
            rechenZahl = (Integer.parseInt(nachKomma)) / Math.pow(10, nachKomma.length());
        }catch(Exception e){
            return "ERROR: Zahl ist nicht in Basis 10";
        }

        int i = 0;
        boolean notZero = false;
        while(rechenZahl != 0.0 && (i < enoughLength || !notZero)){
            i++;
            rechenZahl = rechenZahl*baseTo;
            int floor = (int)Math.floor(rechenZahl);
            if (floor != 0){notZero = true;}
            kommaErgebnis += Character.forDigit(floor, baseTo);
            rechenZahl = rechenZahl - floor;
        }

        return "0"+","+kommaErgebnis;

    }


}// class end