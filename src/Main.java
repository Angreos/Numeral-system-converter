import NumeralSystemConverter.Colors;
import NumeralSystemConverter.ConverterApp;
import NumeralSystemConverter.Converter;
import NumeralSystemConverter.Conversion;

public class Main {
    public static void main(String[] args) {
        // Description and github link
        System.out.println(Colors.GREEN_BOLD +"This application was made by Andreas."+Colors.RESET+"\nhttps://github.com/Angreos");
        System.out.println("Convert numbers from one numeral system to a different one, with or without steps.");

        // starts the Application
        ConverterApp.run();
    }
}
