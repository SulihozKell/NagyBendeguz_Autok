package com.example.autok;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;

public class InsertActivity extends AppCompatActivity {
    private EditText felvetelGyarto, felvetelModell, felvetelUzembeHelyezesEve;
    private Button btnFelvetel, felvetelVisszaMain;
    private DBHelper adatbazis;
    private boolean joGyarto, joModell, joEv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        init();

        felvetelGyarto.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String gyarto = felvetelGyarto.getText().toString().trim();
                Cursor gyartoEredmeny = adatbazis.keresesGyarto(gyarto);
                if (gyartoEredmeny.getCount() != 0) {
                    felvetelGyarto.setTextColor(Color.RED);
                    joGyarto = false;
                    disableButton();
                }
                else {
                    felvetelGyarto.setTextColor(Color.GREEN);
                    joGyarto = true;
                    disableButton();
                }
            }
            else {
                felvetelGyarto.setTextColor(Color.BLACK);
            }
        });

        felvetelModell.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String modell = felvetelModell.getText().toString().trim();
                Cursor modellEredmeny = adatbazis.keresesModell(modell);
                if (modellEredmeny.getCount() != 0) {
                    felvetelModell.setTextColor(Color.RED);
                    joModell = false;
                    disableButton();
                }
                else {
                    felvetelModell.setTextColor(Color.GREEN);
                    joModell = true;
                    disableButton();
                }
            }
            else {
                felvetelModell.setTextColor(Color.BLACK);
            }
        });

        felvetelUzembeHelyezesEve.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String uzembeHelyezesEveString = felvetelUzembeHelyezesEve.getText().toString()
                        .trim();
                try {
                    int uzembeHelyezesEve = Integer.parseInt(uzembeHelyezesEveString);
                    if (uzembeHelyezesEve >= 1900 && uzembeHelyezesEve <= LocalDate.now().getYear())
                    {
                        joEv = true;
                        disableButton();
                    }
                    else {
                        joEv = false;
                        disableButton();
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Az üzembehelyezés évének " +
                            "számnak kell lennie", Toast.LENGTH_SHORT).show();
                    joEv = false;
                    disableButton();
                }
            }
        });

        btnFelvetel.setOnClickListener(v -> {
            String gyarto = felvetelGyarto.getText().toString().trim();
            String modell = felvetelModell.getText().toString().trim();
            String uzembeHelyezesEveString = felvetelUzembeHelyezesEve.getText().toString().trim();
            if (gyarto.isEmpty() || modell.isEmpty() || uzembeHelyezesEveString.isEmpty()) {
                Toast.makeText(getApplicationContext(), "A mezők értéke nem lehet üres!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    int uzembeHelyezesEve = Integer.parseInt(uzembeHelyezesEveString);
                    if (uzembeHelyezesEve >= 1900 && uzembeHelyezesEve <= LocalDate.now().getYear())
                    {
                        if (adatbazis.keresesModell(modell).getCount() == 0 &&
                                adatbazis.keresesGyarto(gyarto).getCount() == 0) {
                            if (adatbazis.felvetel(gyarto, modell, uzembeHelyezesEve)) {
                                Toast.makeText(getApplicationContext(), "Sikeres felvétel!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Sikertelen felvétel!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            btnFelvetel.setEnabled(false);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Az üzembehelyezés évének " +
                                "1900 után és a mai dátum között kell lennie.", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Az üzembehelyezés évének " +
                            "számnak kell lennie", Toast.LENGTH_SHORT).show();
                }
            }
        });

        felvetelVisszaMain.setOnClickListener(v -> {
            Intent mainre = new Intent(InsertActivity.this, MainActivity.class);
            startActivity(mainre);
            finish();
        });
    }

    private void init() {
        felvetelGyarto = findViewById(R.id.felvetel_gyarto);
        felvetelModell = findViewById(R.id.felvetel_modell);
        felvetelUzembeHelyezesEve = findViewById(R.id.felvetel_uzembe_helyezes_eve);
        btnFelvetel = findViewById(R.id.btn_felvetel);
        felvetelVisszaMain = findViewById(R.id.felvetel_vissza_main);
        adatbazis = new DBHelper(InsertActivity.this);
        btnFelvetel.setEnabled(false);
        joGyarto = false;
        joModell = false;
        joEv = false;
    }

    private void disableButton() {
        btnFelvetel.setEnabled(joGyarto && joModell && joEv);
    }
}