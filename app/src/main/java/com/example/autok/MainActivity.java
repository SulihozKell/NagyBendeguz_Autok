package com.example.autok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnKereses, btnUjFelvetele;
    private EditText keresesItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btnKereses.setOnClickListener(v -> {
            String item = keresesItem.getText().toString().trim();
            if (!item.isEmpty()) {
                Intent keresesre = new Intent(MainActivity.this, SearchResultActivity.class);
                keresesre.putExtra("itemKulcs", item);
                startActivity(keresesre);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "A mező nem lehet üres!", Toast.LENGTH_SHORT).show();
            }
        });

        btnUjFelvetele.setOnClickListener(v -> {
            Intent felvetelre = new Intent(MainActivity.this, InsertActivity.class);
            startActivity(felvetelre);
            finish();
        });
    }

    private void init() {
        btnKereses = findViewById(R.id.btn_kereses);
        btnUjFelvetele = findViewById(R.id.btn_uj_felvetele);
        keresesItem = findViewById(R.id.kereses_item);
    }
}