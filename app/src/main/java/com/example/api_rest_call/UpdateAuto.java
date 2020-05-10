package com.example.api_rest_call;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateAuto extends AppCompatActivity implements View.OnClickListener{

    Button btnGuardar;
    Button btnLimpiar;
    Button btnInicio;
    TextView txtId;
    EditText edtMarca;
    EditText edtModelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_auto);

        txtId = (TextView) findViewById(R.id.txtId);
        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtModelo = (EditText) findViewById(R.id.edtModelo);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnLimpiar = (Button)findViewById(R.id.btnLimpiar);
        btnInicio = (Button)findViewById(R.id.btnInicio);

        btnGuardar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnInicio.setOnClickListener(this);

        if(getIntent().hasExtra("miId"))
        {
            String miID = getIntent().getStringExtra("miId");
            if(miID != "")
                getVehiculo(miID);
        }
        else
            txtId.setText("");
    }

    @Override
    public void onClick(View v) {
            switch (v.getId())
        {
            case R.id.btnGuardar:
                    guardar(txtId.getText().toString());
                break;
            case R.id.btnLimpiar:
                limpiar();
                break;
            case R.id.btnInicio:
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getVehiculo(String miId) {
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
                    txtId.setText(auto.getId());
                    edtMarca.setText(auto.getMarca());
                    edtModelo.setText(auto.getModelo());

                    // Aviso al base adapter que cambio mi set de datos.
                    // Renderizacion general de mi ListView
                }
                else
                {
                    Toast.makeText(UpdateAuto.this, "Hubo un error con la respuesta",
                            Toast.LENGTH_LONG);
                }
            }
            public void onFailure(Call<Auto> call, Throwable t) {
                // SI el servidor o la llamada no puede ejecutarse, muestro un mensaje de eror:
                Toast.makeText(UpdateAuto.this,"Hubo un error con la llamada a la API", Toast.LENGTH_LONG);

            }
        });
    }

    private void limpiar() {
        if(txtId.getText().toString() == "")
        {
            txtId.setText("");
            edtMarca.setText("");
            edtModelo.setText("");
        }
        else
            getVehiculo(txtId.getText().toString());
    }

    private void guardar(String id){
        if(id.equals(""))
            save();
        else
            update(id);
    }

    private void save() {
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://us-central1-be-tp3-a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AutoService autoService = retrofit.create(AutoService.class);

        Call<Auto> http_call = autoService.saveAuto(edtMarca.getText().toString(),
                                                        edtModelo.getText().toString());

        http_call.enqueue(new Callback<Auto>(){
            public void onResponse(Call<Auto> call, Response<Auto> response) {
                if(response.isSuccessful())
                {
                    String message = "";
                    if(response.isSuccessful())
                        message = "El auto fue guardado correctamente";
                    else
                        message = "Ocurrio algo inesperado.";

                    Toast.makeText(UpdateAuto.this
                            , message
                            , Toast.LENGTH_SHORT).show();
                    limpiar();
                }
            }
            public void onFailure(Call<Auto> call, Throwable t) {
                Toast.makeText(UpdateAuto.this, "Hubo un error con la llamada a la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update(String id){

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://us-central1-be-tp3-a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AutoService autoService = retrofit.create(AutoService.class);

        Call<Void> http_call = autoService.updateAuto(id,
                edtMarca.getText().toString(),
                edtModelo.getText().toString());

        http_call.enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = "";
                    if (response.isSuccessful())
                        message = "El auto fue guardado correctamente";
                    else
                        message = "Ocurrio algo inesperado.";

                    Toast.makeText(UpdateAuto.this
                            , message
                            , Toast.LENGTH_SHORT).show();
                    limpiar();
                }
            }

            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateAuto.this, "Hubo un error con la llamada a la API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
