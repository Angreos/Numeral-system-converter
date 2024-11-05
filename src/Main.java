import NumeralSystemConverter.ConverterApp;
import NumeralSystemConverter.Converter;

public class Main {
    public static void main(String[] args) {
        // Description
        System.out.println("""
                This application was made by Andreas Schmidt.
                https://github.com/Angreos
                
                With this you can convert numbers from one numeral system to another.
                For example, you can convert 1110 in binary to decimal which would give you 14.
                """);

        // starts the Application
        ConverterApp.run();
    }
}
