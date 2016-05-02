package com.danteros.retropostmixera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText et_Login, et_Pass;
    private ProgressDialog pDialog;
    String strLogin;
    String strPass;
    Button btn_enter;
    private Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://service.4invest.net")
            .build();
    private Login interFace = retrofit.create(Login.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Login = (EditText) findViewById(R.id.et_Login);
        et_Pass = (EditText) findViewById(R.id.et_Pass);
        btn_enter = (Button) findViewById(R.id.btn_enter);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Подключение к серверу");
                pDialog.setTitle("Ожидайте");
                pDialog.show();

                strLogin = et_Login.getText().toString();
                strPass = et_Pass.getText().toString();

                RequestBody login = RequestBody.create(MediaType.parse("form-data"), strLogin);
                RequestBody password = RequestBody.create(MediaType.parse("form-data"), strPass);

                Call<Object> call = interFace.login(login,password);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        pDialog.dismiss();
                        Map<String,String> map = gson.fromJson(response.body().toString(),Map.class);
                        for (Map.Entry e : map.entrySet()){
                            if (e.getValue().equals(true)){
                                Intent intent = new Intent(MainActivity.this,ChartActivity.class);
                                startActivity(intent);
                                System.out.println(e.getKey()+ " " + e.getValue()+ " ");
                            }else if (e.getValue().equals(false)){
                                Toast.makeText(MainActivity.this,"Данные не верны",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        pDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Подключение не выполнено",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}



