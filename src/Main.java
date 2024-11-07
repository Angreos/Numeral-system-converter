import NumeralSystemConverter.ConverterApp;
import NumeralSystemConverter.Converter;

public class Main {
    public static void main(String[] args) {
        // Description
        System.out.println("""
                This application was made by Andreas.
                https://github.com/Angreos
                
                With this you can convert numbers from one numeral system to different one.
                For example, you can convert 1110 from binary to base 10 which would give you 14.
                Works with decimal numbers.
                """);

        // starts the Application

        // ConverterApp.run();


        String number = "F.1";
        String numberConverted = Converter.convertToBase10(number, 16, true);
        System.out.println(numberConverted);

    }
}
