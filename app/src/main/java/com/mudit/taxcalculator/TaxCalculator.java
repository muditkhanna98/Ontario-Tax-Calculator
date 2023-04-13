package com.mudit.taxcalculator;

public class TaxCalculator {
    private double[] ontarioBrackets = {0, 48430, 96860, 150000, 220000};
    private double[] ontarioRates = {0.0505, 0.0915, 0.1116, 0.1216, 0.1316};
    private double[] federalBrackets = {0, 49020, 98040, 151978, 216511};
    private double[] federalRates = {0.15, 0.205, 0.26, 0.29, 0.33};
    private double[] surtaxRates = {0.20, 0.36, 0.41, 0.44};
    private static final double RRSP_LIMIT = 27570;

    public double calculateTax(double income, double rrsp) {
        // Deduct RRSP contribution from income
        double taxableIncome = income - Math.min(rrsp, RRSP_LIMIT);

        // Calculate federal tax
        double federalTax = calculateFederalTax(taxableIncome);

        // Calculate Ontario tax
        double ontarioTax = calculateOntarioTax(taxableIncome);

        // Calculate surtax if applicable
        double surtax = calculateSurtax(income);

        // Calculate total tax
        double totalTax = federalTax + ontarioTax + surtax;

        return totalTax;
    }

    private double calculateFederalTax(double income) {
        double federalTax = 0;
        for (int i = 0; i < federalBrackets.length; i++) {
            if (income > federalBrackets[i]) {
                double incomeInBracket = Math.min(income - federalBrackets[i], federalBrackets[i + 1] - federalBrackets[i]);
                federalTax += incomeInBracket * federalRates[i];
            } else {
                break;
            }
        }
        return federalTax;
    }

    private double calculateOntarioTax(double income) {
        double ontarioTax = 0;
        for (int i = 0; i < ontarioBrackets.length; i++) {
            if (income > ontarioBrackets[i]) {
                double incomeInBracket = Math.min(income - ontarioBrackets[i], ontarioBrackets[i + 1] - ontarioBrackets[i]);
                ontarioTax += incomeInBracket * ontarioRates[i];
            } else {
                break;
            }
        }
        return ontarioTax;
    }

    private double calculateSurtax(double income) {
        double surtax = 0;
        double baseTax = calculateOntarioTax(income);

        if (baseTax > 0) {
            for (int i = 0; i < surtaxRates.length; i++) {
                double surtaxIncome = baseTax * surtaxRates[i];
                if (income > surtaxIncome) {
                    surtax += surtaxIncome * (surtaxRates[i] - surtaxRates[i - 1]);
                } else {
                    surtax += (income - baseTax) * (surtaxRates[i] - surtaxRates[i - 1]);
                    break;
                }
            }
        }
        return surtax;
    }
}




