package NumeralSystemConverter;

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
                if (!Character.isDigit(newChar) || Character.isLetter(newChar)) {return false;}
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
        String strippedNumberPreComma = splitNumber[0].replaceFirst("^0+", "");
        String strippedNumberPostComma = splitNumber[1].replaceFirst("^0+", "");

        return (strippedNumberPreComma.isBlank() && strippedNumberPostComma.isBlank()) ? "0" : strippedNumberPreComma+","+strippedNumberPostComma;
    }

    /**
     * Normalizes the provided number string by:
     * <p>
     * - Replacing all periods with commas if there is only one comma in the string. <br>
     * - Removing all periods if there are multiple commas in the string.<br>
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
        // Check the count of commas in the string.
        int commaCount = number.split(",").length - 1;

        // If there is only one comma, replace all periods with commas.
        if (commaCount == 1) {
            number = number.replaceAll("\\.", ",");
        } else if (commaCount > 1) {
            // If there are multiple commas, remove all periods.
            number = number.replaceAll("\\.", "");
        }

        // Remove underscores and spaces.
        number = number.replaceAll("_", "");
        number = number.replaceAll(" ", "");

        return number;
    }




}
