package NumeralSystemConverter;

import java.util.ArrayList;

public abstract class Converter {

    /**
     * Checks if the given integer is a power of two.
     * <p>
     * This method converts the integer to its binary representation and removes all '0's.
     * If only one '1' remains in the binary string, the number is a power of two.
     * </p>
     *
     * @param base The integer to check.
     * @return true if {@code base} is a power of two; false otherwise.
     */
    private static boolean isPowerOfTwo(int base){
        String baseBinary = Integer.toBinaryString(base);
        baseBinary = baseBinary.replaceAll("0", "");
        return baseBinary.length() == 1;
    }

    /**
     * Checks if the specified number string is valid in the given numeral system base.
     *<p>
     * This method verifies that each character in the {@code number} string is compatible
     * with the specified {@code base}. Digits outside the range of the base and characters
     * other than commas or periods will result in a false return value.
     *</p>
     *
     * @param number The number string to check for validity in the given numeral system.
     * @param base The numeral system base (e.g.: 2 for binary, 10 for decimal).
     * @return true if {@code number} is valid in the numeral system of {@code base}; false otherwise.
     */
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

    /**
     * Strips leading zeros from both parts of the provided number string, before and after the comma.
     * <p>
     * This method first normalizes the input number using {@link #normalizeNumber(String)}. Then, it splits
     * the number string into the part before the comma and the part after the comma (if any). Leading zeros
     * are removed from each part independently. If both parts are blank after stripping, the method returns "0".
     * </p>
     *
     * @param number The number string to normalize and strip of leading zeros.
     * @return The normalized number string without leading zeros in each part, or "0" if the input
     *         consists entirely of zeros.
     */
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

    /**
     * Normalizes the provided number string by:
     * <p>
     * - Replacing all commas with dots if there is only one dot in the string. <br>
     * - Removing all commas if there are multiple dots in the string.<br>
     * - Removing underscores and spaces.<br>
     * </p>
     *
     * <p>
     * This ensures the number string is in a more consistent format.
     * </p>
     *
     * @param number The number string to normalize.
     * @return A normalized version of the number string.
     */
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

    /**
     * Converts a number from a specified base to decimal (base 10), supporting both
     * whole numbers and fractional parts.
     * <p>
     * This method takes a number in a given base and converts it to its decimal equivalent.
     * It handles numbers with or without decimal points and can provide a step-by-step breakdown
     * of the conversion process if requested.
     * </p>
     *
     * @param number The number string to be converted, which may include digits and a decimal point.
     * @param base The base of the input number, which must be greater than 1.
     * @param withSteps A boolean flag indicating whether to include detailed conversion steps in the output.
     * @return A string representation of the conversion result, which can be formatted to include
     *         the detailed steps if requested. Returns "ERROR" if the number is not valid in the given base
     *         or "Number is Invalid" if more than one decimal point is found.
     *
     * @throws IllegalArgumentException if the base is less than or equal to 1.
     * @throws NumberFormatException if the input number contains invalid characters for the specified base.
     */
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

    /**
     * Converts a number from a specified base to its decimal (base 10) equivalent.
     * <p>
     * This method processes the input number as a string, calculates its decimal
     * value based on the given base, and constructs a detailed step-by-step
     * explanation of the conversion process if {@code withSteps} is true.
     * The conversion is performed by iterating over each digit, determining
     * its numeric value, calculating the corresponding power of the base,
     * and summing the results.
     * </p>
     *
     * @param number The number string to be converted, represented in the specified base.
     * @param base The base of the input number (e.g., 2 for binary, 16 for hexadecimal).
     * @param withSteps A boolean indicating whether to return the step-by-step
     *                  conversion process or just the final result.
     * @return A string containing the step-by-step conversion details if
     *         {@code withSteps} is true, otherwise the decimal equivalent of the number.
     */
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

    /**
     * Converts a number from a specified base to decimal (base 10) after the decimal point.
     * <p>
     * This method takes a number in a given base and converts it to its decimal equivalent,
     * supporting fractional parts by treating digits after the decimal as negative powers of the base.
     * It can optionally provide a step-by-step breakdown of the conversion process.
     * </p>
     *
     * @param number The number string to be converted, which may include digits and a decimal point.
     * @param base The base of the input number, which must be greater than 1.
     * @param withSteps A boolean flag indicating whether to include detailed conversion steps in the output.
     * @return A {@link Conversion} object containing the decimal representation of the number
     *         and, if requested, the detailed steps of the conversion process.
     *
     * @throws IllegalArgumentException if the base is less than or equal to 1.
     * @throws NumberFormatException if the input number contains invalid characters for the specified base.
     */
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
}
