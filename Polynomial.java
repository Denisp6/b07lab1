import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//(i) one field representing the coefficients
public class Polynomial {
    private double[] coefficients;
    private int[] exponents;
//(ii) It has a no-argument constructor
    public Polynomial() {
        coefficients = new double[]{0};
        exponents = new int[]{0};
    }
//(iii) constructor that takes an array of double
    public Polynomial(double[] coeff, int[] exp) {
        coefficients = new double[coeff.length];
        exponents = new int[exp.length];
        for (int i = 0; i < coeff.length; i++) {
            coefficients[i] = coeff[i];
            exponents[i] = exp[i];
        }
    }
// NEW CONSTRUCTOR) Initializes poly from file
    public Polynomial(File file) throws IOException {
        Scanner input = new Scanner(file);
        String poly = input.nextLine();
        input.close();

        //initialize size of coeff and exp array 
        String[] terms = poly.split("(?=[+-])"); //comeback to this...figure out how to split w/o deleting +-
        //?=[] means split before the + and -. **Useful to remember!!!
        coefficients = new double[terms.length];
        exponents = new int[terms.length];

        for (int i = 0; i < terms.length; i++) {
            //use .contains() to find a specific char
            //find the x, term before it is coeff, term after is exp
            if (terms[i].contains("x")) {
                //use .indexOf to find position of specific char
                int xindex = terms[i].indexOf("x");
                //use .substring(start index, end index) to seperate coeff and exp
                String coeffb4x = terms[i].substring(0, xindex);
                String expftrx = terms[i].substring(xindex + 1, terms[i].length());

                //initialize coeff array
                //use .equals("string") to see if string contains an exact of...
                //Case 1: coefficient is -1
                if (coeffb4x.equals("-")) {
                    coefficients[i] = -1;
                //Case 2: coeff is 1
                } else if (coeffb4x.equals("") || coeffb4x.equals("+")) {
                    coefficients[i] = 1;
                //Case 3: other...Come back to this figure out how to convert to double/int
                } else {
                    // use datatype.parsedatatype() to convert str to datatype
                    coefficients[i] = Double.parseDouble(coeffb4x);
                }
                //initialize exp array
                if (expftrx.equals("")) { // no exponent/exp = 1
                    exponents[i] = 1;
                } else {
                    exponents[i] = Integer.parseInt(expftrx);
                }
            }
        }
    }
//(iv) method named add that takes one argument ------------COME BACK TO THIS LATER
    public Polynomial add(Polynomial poly) {
        //New array to fit the sum of polys
        double[] newcoeff = new double[coefficients.length + poly.coefficients.length]; //coeffs are non-zero so this is max size
        int[] newexp = new int[newcoeff.length];
        //Put original poly into new array to prep for terms to add in 2nd poly
        int count = 0;
        for (int i = 0; i < coefficients.length; i++) {
            newcoeff[count] = coefficients[i];
            newexp[count] = exponents[i];
            count++;
        }
        //check terms of 2nd poly needed to be sum
        for (int i = 0; i < poly.coefficients.length; i++) {
            int j = 0;
            while (j < count && newexp[j] != poly.exponents[i]) { //either j reaches the end of newexp and finds nothing or matches
                j++;
            }
            if (j < count) {
                //matching exponent was found so while loop didnt iterate fully
                newcoeff[j] += poly.coefficients[i];
            } else {
                //j = count means no match was found so we append to newcoeff and newexp
                newcoeff[count] = poly.coefficients[i];
                newexp[count] = poly.exponents[i];
                count++;
            }
        }
        //redo arrays bc count++ counts actual size of array needed but we initialize to coeff.len + poly.coeff.len at the start so we may have extra empty spots
        double[] summedcoeff = new double[count];
        int[] summedexp = new int[count];
        for (int i = 0; i < count; i++) {
            summedcoeff[i] = newcoeff[i];
            summedexp[i] = newexp[i];
        }
        return new Polynomial(summedcoeff, summedexp);
    } 
//(v) It has a method named evaluate that takes one argument
    public double evaluate(double x) {
        double eval = 0;
        for (int i = 0; i < coefficients.length; i++) {
            eval +=  coefficients[i] * Math.pow(x,exponents[i]);
        }
        return eval;
    }
//(vi) It has a method named hasRoot that takes one argument
    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
///Tutorial 2 Updates:
// i) It has a method named multiply with 1 arg poly
    public Polynomial multiply(Polynomial poly) {
        //New array to fit the product of polys
        double[] newcoeff = new double[coefficients.length * poly.coefficients.length]; 
        int[] newexp = new int[newcoeff.length];
        int count = 0;

        //multiply each term of p1 w p2
        for (int i = 0; i < coefficients.length; i++) {
            for (int j = 0; j < poly.coefficients.length; j++) {
                double multipliedCoeff = coefficients[i] * poly.coefficients[j];
                int multipliedExp = exponents[i] + poly.exponents[j];

                //iterrate thru newexp/coeff to collect like terms/append new ones
                int k = 0;
                while (k < count && newexp[k] != multipliedExp) { //first itteration should automatically go to else bc k = 0 = count
                    k++;
                }
                if (k < count) { //if exp DOES match then we collect like terms, k++ doesnt occur and we go to if case to collect like terms
                    newcoeff[k] += multipliedCoeff;
                } else { //curent exp dne so append
                    newcoeff[count] = multipliedCoeff;
                    newexp[count] = multipliedExp;
                    count++; //increase size of newcoeff and newexp in iteration
                }
            }
        }
        //same with add method we need to cut up to count entries of array bc of any unused entries from start
        double[] fmdcoeff = new double[count];
        int[] fmdexp = new int[count];
        for (int i = 0; i < count; i++) {
            fmdcoeff[i] = newcoeff[i];
            fmdexp[i] = newexp[i];
        }
        return new Polynomial(fmdcoeff, fmdexp);
    }
// ii) It has a method saveToFile with 1 arg String
    public void saveToFile(String filename) throws IOException {
        FileWriter output = new FileWriter(filename, false);
        //make the string
        String str = "";
        for (int i = 0; i < coefficients.length; i++) {
            double c = coefficients[i];
            int e = exponents[i];
    
            if (str.length() > 0 && c > 0) { //positive C and make sure + isnt first char so we add + 
                str += "+";
            }
            if (c == 1 && e != 0) {//coeff is 1 and exp != 0 so we dont have to add a number just skip

            } else if (c == -1 && e != 0) {//coeff is -1 and exp !=0 so we can just add neg sign 
                str += "-"; 
            } else {
                str += c; //cases outside of 1/-1 so just add c with its respective sign if neccesary
            }
            if (e > 0) { //exponents
                str += "x";
                if (e > 1) {
                    str += e;
                }
            }
        }
        output.write(str);
        output.close();
    }
}
