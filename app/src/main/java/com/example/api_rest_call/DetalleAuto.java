package com.example.api_rest_call;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleAuto extends AppCompatActivity implements View.OnClickListener{

    TextView id;
    TextView marca;
    TextView modelo;
    Button btnAtras;
    Button btnModificar;
    Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_auto);

        id = (TextView) findViewById(R.id.txtId);
        marca = (TextView) findViewById(R.id.txtMarca);
        modelo = (TextView) findViewById(R.id.txtModelo);
        btnAtras = (Button) findViewById(R.id.btnBack);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        btnModificar.setOnClickListener(this);
        btnAtras.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);

        if(getIntent().hasExtra("miID"))
        {
            String miID = getIntent().getStringExtra("miID");
            if(miID != "")
                getVehiculo(miID);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId())
        {
            case R.id.btnBack:
                intent = new Intent(v.getContext(), MainActivity.class);
                break;
            case R.id.btnModificar:
                intent = new Intent(v.getContext(), UpdateAuto.class);
                intent.putExtra("miId", id.getText());
                break;
            case R.id.btnEliminar:
                delete(id.getText().toString());
                intent = new Intent(v.getContext(), MainActivity.class);
                break;
            default:
                break;
        }

        startActivity(intent);
    }

    public void getVehiculo(String miId) {
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://us-central1-be-tp3-a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AutoService autoService = retrofit.create(AutoService.class);

        Call<Auto> http_call = autoService.getAuto(miId);

        http_call.enqueue(new Callback<Auto>() {

            public void onResponse(Call<Auto> call, Response<Auto> response) {
                // Si el servidor responde correctamente puedo hacer uso de la respuesta esperada:

                if(response.body() != null) {
                    Auto auto = (Auto) response.body();
                    id.setText(auto.getId());
                    marca.setText(auto.getMarca());
                    modelo.setText(auto.getModelo());

                    // Aviso al base adapter que cambio mi set de datos.
                    // Renderizacion general de mi ListView
                }
                else
                {
                    Toast.makeText(DetalleAuto.this, "Hubo un error con la respuesta",
                            Toast.LENGTH_LONG);
                }
            }
            public void onFailure(Call<Auto> call, Throwable t) {
                // SI el servidor o la llamada no puede ejecutarse, muestro un mensaje de eror:
                Toast.makeText(DetalleAuto.this,"Hubo un error con la llamada a la API", Toast.LENGTH_LONG);

            }
        });
    }

    private void delete(String id)
    {
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://us-central1-be-tp3-a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AutoService autoService = retrofit.create(AutoService.class);

        Call<Void> http_call = autoService.deleteAuto(id);

        http_call.enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(DetalleAuto.this, "El auto fue eliminado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalleAuto.this, "Hubo un error al llamar a la API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
