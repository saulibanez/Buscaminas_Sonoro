package es.urjc.mov.sibanez.minersonoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Winner extends AppCompatActivity {

    Button volver, salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winner);

        volver = (Button) findViewById(R.id.btn2);
        volver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aceptar = new Intent(Winner.this, MainActivity.class);
                startActivity(aceptar);
            }
        });

        salir = (Button) findViewById(R.id.out);
        salir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                finish();
            }
        });
    }
}