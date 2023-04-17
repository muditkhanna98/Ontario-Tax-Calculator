package com.mudit.taxcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {
    private TextView headingTextView;
    private EditText incomeEditText;
    private TextView rrspTextView;
    private SeekBar rrspSeekBar;
    private TextView rrsp_multiplierText;
    private TextView ontarioTaxTextView;
    private TextView federalTaxTextView;
    private TextView surtaxTextView;
    private TextView rrspContributionTv;

    private SharedPreferences sharedPreferences;
    private double rrspMultiplier;
    private double rrspDeduction;
    private TaxCalculator taxCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headingTextView = findViewById(R.id.headingTextView);
        incomeEditText = findViewById(R.id.incomeEditText);
        rrspTextView = findViewById(R.id.rrspTextView);
        rrspSeekBar = findViewById(R.id.rrsp_seekbar);
        rrsp_multiplierText = findViewById(R.id.rrsp_multiplierText);
        rrspContributionTv = findViewById(R.id.rrspContributionTv);
        ontarioTaxTextView = findViewById(R.id.ontarioTaxTextView);
        federalTaxTextView = findViewById(R.id.federalTaxTextView);
        surtaxTextView = findViewById(R.id.surtaxTextView);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        taxCalculator = new TaxCalculator();

        rrspSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calculate the RRSP deduction based on the SeekBar progress
                Double income = Double.parseDouble(incomeEditText.getText().toString());
                rrspMultiplier = (double) progress / 1000.0;
                if (income < 27230) {
                    rrspMultiplier = (double) progress / 1000.0;
                    rrspDeduction = rrspMultiplier * income;
                } else {

                    rrspDeduction = rrspMultiplier * 27230;
                }
                updateTaxOutput(rrspDeduction, rrspMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
        fetchSharedPreferences();
    }

    public void updateTaxOutput(double rrspDeduction, double rrspMultiplier) {
        Double income = Double.parseDouble(incomeEditText.getText().toString());
        Double ontarioTax = taxCalculator.calculateOntarioTax(income, rrspDeduction);
        Double federalTax = taxCalculator.calculateFederalTax(income, rrspDeduction);
        Double surtax = taxCalculator.calculateSurtax(income, rrspDeduction, ontarioTax);

        rrsp_multiplierText.setText("RRSP Multiplier: " + String.format("%.2f", rrspMultiplier));
        ontarioTaxTextView.setText("Ontario tax: " + String.format("%.2f", ontarioTax));
        federalTaxTextView.setText("Federal tax: " + String.format("%.2f", federalTax));
        surtaxTextView.setText("Surtax: " + String.format("%.2f", surtax));
        rrspContributionTv.setText("RRSP Contribution: " + String.format("%.2f", rrspDeduction));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("income", String.format("%.2f", income));
        editor.putString("multiplier", "RRSP Multiplier: " + String.format("%.2f", rrspMultiplier));
        editor.putString("ontarioTax", "Ontario tax: " + String.format("%.2f", ontarioTax));
        editor.putString("federalTax", "Federal tax: " + String.format("%.2f", federalTax));
        editor.putString("surtax", "Surtax: " + String.format("%.2f", surtax));
        editor.putString("rrsDeduction", "RRSP Contribution: " + String.format("%.2f", rrspDeduction));
        editor.apply();
    }

    public void fetchSharedPreferences() {
        String income = sharedPreferences.getString("income", "0.0");
        String multiplier = sharedPreferences.getString("multiplier", "RRSP Multiplier: ");
        String ontarioTax = sharedPreferences.getString("ontarioTax", "Ontario tax: ");
        String federalTax = sharedPreferences.getString("federalTax", "Federal Tax: ");
        String surtax = sharedPreferences.getString("surtax", "Surtax: ");
        String rrspDeduction = sharedPreferences.getString("rrspDeduction", "RRSP Contribution: ");

        rrsp_multiplierText.setText(multiplier);
        incomeEditText.setText(income);
        ontarioTaxTextView.setText(ontarioTax);
        federalTaxTextView.setText(federalTax);
        surtaxTextView.setText(surtax);
        rrspContributionTv.setText(rrspDeduction);

        if (multiplier != "RRSP Multiplier: ") {
            String[] multiplierArr = multiplier.split(":");
            int progValue = (int) (Double.parseDouble(multiplierArr[1].trim()) * 1000);
            rrspSeekBar.setProgress(progValue, true);
        } else {
            rrspSeekBar.setProgress(0, true);
        }
    }
}