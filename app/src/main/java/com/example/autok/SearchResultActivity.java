package com.example.autok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends AppCompatActivity {
    private Button searchVisszaMain;
    private TextView gyartoOsszesAutoja;
    private DBHelper adatbazis;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        init();

        if (extras != null) {
            String item = extras.getString("itemKulcs");
            gyartoOsszesAutoja.setText(item);
            Cursor kiir = adatbazis.kereses(item);
            StringBuilder stringBuilder = new StringBuilder();
            if (kiir.getCount() != 0) {
                while (kiir.moveToNext()) {
                    stringBuilder.append("Gyártó: ").append(kiir.getString(0))
                            .append(System.lineSeparator());
                    stringBuilder.append("Modell: ").append(kiir.getString(1))
                            .append(System.lineSeparator());
                    stringBuilder.append("Üzembehelyezés éve: ").append(kiir.getString(2))
                            .append(System.lineSeparator());
                    stringBuilder.append(System.lineSeparator());
                }
            }
            else {
                stringBuilder.append("Nem található rekord a következő adattal: ").append(item);
            }
            gyartoOsszesAutoja.setText(stringBuilder.toString());
        }
        else {
            Toast.makeText(getApplicationContext(), "Érvénytelen adat!", Toast.LENGTH_SHORT)
                    .show();
        }

        searchVisszaMain.setOnClickListener(v -> {
            Intent mainre = new Intent(SearchResultActivity.this, MainActivity.class);
            startActivity(mainre);
            finish();
        });
    }

    private void init() {
        searchVisszaMain = findViewById(R.id.search_vissza_main);
        gyartoOsszesAutoja = findViewById(R.id.gyarto_osszes_autoja);
        adatbazis = new DBHelper(SearchResultActivity.this);
        extras = getIntent().getExtras();
    }
}