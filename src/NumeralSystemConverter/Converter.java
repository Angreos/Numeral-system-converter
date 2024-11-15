package NumeralSystemConverter;

import java.util.ArrayList;

public abstract class Converter {

    private static boolean isPowerOfTwo(int base){
        String baseBinary = Integer.toBinaryString(base);
        baseBinary = baseBinary.replaceAll("0", "");
        return baseBinary.length() == 1;
    }

    private static boolean isPartOfNumeralSystem(String number, int base) {
        // Check if the number is null or empty, which makes it invalid
        if (number == null || number.isEmpty()) {return false;}
        for (char c : number.toCharArray()) {
            if (c != ',' && c != '.') {
                // Convert character to numeric value in the specified base
                char newChar = Character.forDigit(Character.getNumericValue(c), base);
                // Check if character is a valid digit in the given base
                if (!Character.isDigit(newChar) && !Character.isLetter(newChar)) {
                    System.err.println("Error: The provided number("+number+") is not part of the base "+base);
                    return false;
                }
            }
        }return true;
    }

    private static String stripFirstZeros(String number){
        number = normalizeNumber(number);
        String[] splitNumber = number.split(",");
        if(splitNumber.length < 2){
            return number;
        }
        String strippedNumberPreComma = splitNumber[0].replaceFirst("^0+", "");
        String strippedNumberPostComma = splitNumber[1].replaceFirst("^0+", "");

        return (strippedNumberPreComma.isBlank() && strippedNumberPostComma.isBlank()) ? "0" : strippedNumberPreComma+","+strippedNumberPostComma;
    }

    private static String normalizeNumber(String number) {
        // Check the count of dots in the string.
        int dotCount = number.split("\\.").length - 1;

        // If there is only one dot, replace all commas with dots.
        if (dotCount == 1) {
            number = number.replaceAll(",", ".");
        } else if (dotCount > 1) {
            // If there are multiple dots, remove all commas.
            number = number.replaceAll(",", "");
        }

        // Remove underscores and spaces.
        number = number.replaceAll("_", "");
        number = number.replaceAll(" ", "");

        return number;
    }

    // Convert to base 10
    public static String convertToBase10(String number, int base, boolean withSteps){
        number = stripFirstZeros(number);
        if(!isPartOfNumeralSystem(number, base)){return "ERROR";}

        String steps = "";
        String result = "";

        String[] splitNumber = number.split("\\.");

        if(splitNumber.length > 2){return "Number is Invalid";}
        else if (splitNumber.length == 2){
            String preComma = splitNumber[0];
            String postComma = splitNumber[1];

            Conversion preCommaConversion = convertToBase10PreComma(preComma, base, withSteps);
            Conversion postCommaConversion = convertToBase10PostComma(postComma, base, withSteps); // change this to actuallty post comma

            steps = preCommaConversion.getSteps() + "\n";
            steps += postCommaConversion.getSteps() + "\n";

            String resultPostComma = postCommaConversion.getResult().split("\\.")[1];

            result =  preCommaConversion.getResult() + "." +resultPostComma;
        }
        else if (splitNumber.length == 1){
            Conversion numberConversion = convertToBase10PreComma(number, base, withSteps);
            steps = numberConversion.getSteps()+"\n";
            result = numberConversion.getResult();
        }
        String resultText = number+"("+base+") -> "+result+"(10)";
        return withSteps ? steps+resultText : result;
    }
    public static String convertToBase10(String number, int base){
        return convertToBase10(number, base, false);
    }

    private static Conversion convertToBase10PreComma(String number, int base, boolean withSteps) {
        int result = 0;
        char[] numberArray = number.toCharArray();
        int i = numberArray.length;

        StringBuilder stepsString = new StringBuilder();
        StringBuilder step1 = new StringBuilder("1. ");
        StringBuilder step2 = new StringBuilder("2. ");
        StringBuilder step3 = new StringBuilder("3. ");
        ArrayList<Integer> step3ArrayList = new ArrayList<Integer>();

        for (char c : numberArray) { // Loop through each character in the number
            i--;

            int numericValue = Character.getNumericValue(c); // Get the numeric value of the character
            int power = (int) Math.pow(base, i); // Calculate the base raised to the current index

            int decimalValue = numericValue * power; // Temporary value for this step
            result += decimalValue; // Accumulate the decimal value from each digit

            if(withSteps){
                step1.append(numericValue).append("*").append(base).append("^").append(i);
                step2.append(numericValue).append("*").append(power);

                if (decimalValue != 0) {
                    step3ArrayList.add(decimalValue); // Add non-zero contributions to the list
                }
                if (i != 0) { // Add a '+' if not the last digit
                    step1.append(" + ");
                    step2.append(" + ");
                }
            }
        }
        if(withSteps){
            step1.append("\n");
            step2.append("\n"); // Add new lines after the calculations
            int listSize = step3ArrayList.size(); // Get the size of the step3 array
            for (int j = 0; j < listSize; j++) { // Concatenate all the contributions
                step3.append(step3ArrayList.get(j));
                if (j != listSize - 1) { // Add a '+' if not the last contribution
                    step3.append(" + ");
                }
            }
            step3.append(" = ").append(result).append(" (10)\n"); // Final result in decimal
            // Concatenate all steps into one string
            stepsString.append(step1);
            stepsString.append(step2);
            stepsString.append(step3);
        }
        return withSteps ? new Conversion(result+"", stepsString.toString()) : new Conversion(result + ""); // Return either steps or result
    }
    private static Conversion convertToBase10PreComma(String number, int base){
        return convertToBase10PreComma(number,base,false);
    }

    private static Conversion convertToBase10PostComma(String number, int base, boolean withSteps){
        int indexPower = 0;
        double countedResult = 0.0;

        StringBuilder steps = new StringBuilder();
        StringBuilder step1 = new StringBuilder("1. ");
        StringBuilder step2 = new StringBuilder("2. ");
        StringBuilder step3 = new StringBuilder("3. ");

        ArrayList<Double> decimalNumbers = new ArrayList<>();

        for(char c : number.toCharArray()){
            indexPower--;
            int numericValue = Character.getNumericValue(c);
            double power = Math.pow(base, indexPower);
            double decimalValue = numericValue*power;

            countedResult += decimalValue;

            if(withSteps){
                step1.append(numericValue).append("*").append(base).append("^").append(indexPower);
                step2.append(numericValue).append("*").append(power);

                if(decimalValue != 0.0){decimalNumbers.add(decimalValue);}

                if (indexPower != number.length()*-1) { // Add a '+' if not the last digit
                    step1.append(" + ");
                    step2.append(" + ");
                }
            }
        }
        if(withSteps){
            for(int i = 0; i < decimalNumbers.size(); i++){
                step3.append(decimalNumbers.get(i));
                if(i != decimalNumbers.size()-1){step3.append(" + ");}
            }
            steps.append(step1).append("\n");
            steps.append(step2).append("\n");
            steps.append(step3).append("\n");
        }
        String result = countedResult+"";

        return withSteps ? new Conversion(result, steps.toString()) : new Conversion(result);
    }
    private static Conversion convertToBase10PostComma(String number, int base){
        return convertToBase10PostComma(number, base, false);
    }

    // Convert from base 10
    public static String convertFromBase10(String number, int base, boolean withSteps){
        String result = "";
        String steps = "";
        number = stripFirstZeros(number);
        System.out.println(number);
        String[] splitNumber = number.split("\\.");
        if(splitNumber.length > 2){return "Number is Invalid";}
        int numPreComma = Integer.parseInt(splitNumber[0]);

        if (splitNumber.length == 2){
            double numPostComma = Double.parseDouble(splitNumber[1]);
            numPostComma = numPostComma/Math.pow(10, ((int)numPostComma+"").length());
            Conversion preComma = convertFromBase10PreComma(numPreComma, base, withSteps);
            Conversion postComma = convertFromBase10PostComma(numPostComma, base, withSteps);

            steps = steps + preComma.getSteps() + "\n";
            steps = steps + postComma.getSteps() + "\n";
            result = preComma.getResult() + "." +(postComma.getResult().split("\\.")[1]);
        } else if (splitNumber.length == 1){
            Conversion preComma = convertFromBase10PreComma(numPreComma, base, withSteps);
            result = preComma.getResult();
            steps = preComma.getSteps();
        }

        String resultText = number+"(10) -> "+result+"("+base+")";
        return withSteps ? steps + "\n" + resultText : result;
    }
    public static String convertFromBase10(String number, int base){
        return convertFromBase10(number, base, false);
    }
    private static Conversion convertFromBase10PreComma(int number, int base, boolean withSteps){
        StringBuilder result = new StringBuilder();
        StringBuilder steps = new StringBuilder();
        while(number != 0)
        {
            int devisionRounded = number/base;
            int mod = number%base;
            if(withSteps){
                steps.append(number).append("/").append(base).append(" = ").append(devisionRounded)
                        .append("\t\t|\t")
                        .append(number).append(" mod(%) ").append(base).append(" = ").append(mod);
                if(mod > 9){
                    char characterInBase = Character.forDigit(mod, base);
                    steps.append(" -> ").append(Character.toUpperCase(characterInBase));
                }
                steps.append("\n");
            }
            number = devisionRounded;
            result.append(mod);
        }
        result.reverse();
        if(result.isEmpty()){
            result.append("0");
        }
        return withSteps ? new Conversion(result.toString(), steps.toString()) : new Conversion(result.toString(), null);
    }

    private static Conversion convertFromBase10PostComma(double numberPostComma, int base, boolean withSteps){
        StringBuilder result = new StringBuilder();
        StringBuilder steps = new StringBuilder();

        while(numberPostComma != 0.0){
            double baseMultipliedNumber = numberPostComma*base;
            int preCommaNumber = (int)baseMultipliedNumber;
            double postCommaNumber = (baseMultipliedNumber-(int)baseMultipliedNumber);
            char numberCharacter = Character.forDigit(preCommaNumber, base);
            numberCharacter = Character.toUpperCase(numberCharacter);

            if(withSteps){
                String coloredNumber = Colors.RED+preCommaNumber+Colors.RESET+"."+((postCommaNumber+"").split("\\.")[1]);
                steps.append(numberPostComma).append(" * ").append(base).append(" = ").append(coloredNumber);
                if(preCommaNumber > 9){
                    steps.append(" -> ").append(Colors.RED).append(numberCharacter).append(Colors.RESET);
                }
                steps.append("\n");
            }
            result.append(numberCharacter);
            numberPostComma = postCommaNumber;
        }
        String finalResult = result.toString();
        if(finalResult.isEmpty()){finalResult = "0";}
        finalResult = "0."+finalResult;

        return withSteps ? new Conversion(finalResult, steps.toString()) : new Conversion(finalResult, null);
    }



}