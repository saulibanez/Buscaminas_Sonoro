package es.urjc.mov.sibanez.minersonoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Loser extends AppCompatActivity {

    Button volver, salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loser);

        volver = (Button) findViewById(R.id.btn3);
        volver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aceptar = new Intent(Loser.this, MainActivity.class);
                startActivity(aceptar);
            }
        });

        salir = (Button) findViewById(R.id.out2);
        salir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                finish();
            }
        });
    }
}
