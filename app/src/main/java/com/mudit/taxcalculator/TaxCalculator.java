package com.mudit.taxcalculator;

public class TaxCalculator {
    private final double[] ontarioBrackets = {0, 48430, 96860, 150000, 220000};
    private final double[] ontarioRates = {0.0505, 0.0915, 0.1116, 0.1216, 0.1316};
    private final double[] federalBrackets = {0, 49020, 98040, 151978, 216511};
    private final double[] federalRates = {0.15, 0.205, 0.26, 0.29, 0.33};
    private final double[] surtaxRates = {0.20, 0.36, 0.48};

    public double calculateFederalTax(double income, double rrsp) {
        double taxableIncome = income - rrsp;
        double federalTax = 0;
        for (int i = 0; i < federalBrackets.length; i++) {
            if (taxableIncome > federalBrackets[i]) {
                double incomeInBracket = Math.min(taxableIncome - federalBrackets[i], federalBrackets[i + 1] - federalBrackets[i]);
                federalTax += incomeInBracket * federalRates[i];
            } else {
                break;
            }
        }
        return federalTax;
    }

    public double calculateOntarioTax(double income, double rrsp) {
        double taxableIncome = income - rrsp;
        double ontarioTax = 0.0;
        for (int i = 0; i < ontarioBrackets.length; i++) {
            if (i == ontarioBrackets.length - 1 || taxableIncome <= ontarioBrackets[i + 1]) {
                double incomeInBracket = taxableIncome - ontarioBrackets[i];
                ontarioTax += incomeInBracket * ontarioRates[i];
                break;
            } else {
                double incomeInBracket = ontarioBrackets[i + 1] - ontarioBrackets[i];
                ontarioTax += incomeInBracket * ontarioRates[i];
            }
        }
        return ontarioTax;
    }

    public double calculateSurtax(double income, double rrsp, double ontarioTax) {
        double taxableIncome = income - rrsp;
        double surtax = 0.0;
        double surtaxThreshold = 484930;
        if (taxableIncome > surtaxThreshold) {
            double surtaxRate = 0.0;
            for (int i = 0; i < surtaxRates.length; i++) {
                surtaxRate = surtaxRates[i];
                if (ontarioTax <= surtaxThreshold * surtaxRate) {
                    break;
                }
            }
            surtax = (ontarioTax - surtaxThreshold * surtaxRate) * surtaxRate;
        }
        return surtax;
    }

}




