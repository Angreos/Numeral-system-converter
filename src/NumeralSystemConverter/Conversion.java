package NumeralSystemConverter;

public class Conversion {
    private String steps;
    private String result;

    public Conversion(String pResult){
        this.result = pResult;
    }

    public Conversion(String pResult, String pSteps){
        this.result = pResult;
        this.steps = pSteps;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
