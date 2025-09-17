//(i) one field representing the coefficients
public class Polynomial {
    private double[] coefficients;
//(ii) It has a no-argument constructor
    public Polynomial() {
        coefficients = new double[]{0};
    }
//(iii) constructor that takes an array of double
    public Polynomial(double[] coeff) {
        coefficients = new double[coeff.length];
        for (int i = 0; i < coeff.length; i++) {
            coefficients[i] = coeff[i];
        }
    }
//(iv) method named add that takes one argument
    public Polynomial add(Polynomial poly) {
        //find the bigger len poly to create new poly array size
        int newlen = 0;
        if (coefficients.length >= poly.coefficients.length) {
            newlen = coefficients.length;
        } else {
            newlen = poly.coefficients.length;
        }
        double[] sum = new double[newlen];
        for (int i = 0; i < newlen; i++) {
            //NOTE:error if coeff[i] exist but poly.coeff[i] DNE so cant directly add
            double a = 0;
            double b = 0;
            if (i < coefficients.length) {
                a = coefficients[i];
            }
            if (i < poly.coefficients.length) {
                b = poly.coefficients[i];
            }
            sum[i] = a +  b;
        }
        return new Polynomial(sum);
    } 

//(v) It has a method named evaluate that takes one argument
    public double evaluate(double x) {
        double Eval = 0;
        for (int i = 0; i < coefficients.length; i++) {
            Eval +=  coefficients[i] * Math.pow(x,i);
        }
        return Eval;
    }

//(vi) It has a method named hasRoot that takes one argument
    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}