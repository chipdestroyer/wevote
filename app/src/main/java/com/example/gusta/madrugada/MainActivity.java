package com.example.gusta.madrugada;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private static Usuario usuario = null;
    private EditText etUsername, etSenha;
    private Button btnLogin, btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ObjectMapper objMp = new ObjectMapper();
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID).toUpperCase();

        RequestBody formBody = new FormBody.Builder()
                .add("idAndroid", android_id)
                .build();
        final Request rq = new Request.Builder()
                .url("http://192.168.0.140/login.php")
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // watch cat /var/www/html/cadastrar.php
                    //
                    Response res = client.newCall(rq).execute();
                    if (res.isSuccessful()) {
                        usuario = objMp.readValue(res.body().string(), Usuario.class);
                        proxTela();
                        //TODO: Programar registro com dialog
                    }
                } catch (Exception e) {

                    final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                    ad.setMessage("Novo Usuario")
                            .setCancelable(false)
                            .setView(R.layout.fragment_dialogo_inscricao);
                    ad.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText cxTxt =
                                    (EditText) ((AlertDialog) dialog).findViewById(R.id.editText);
                            if (cxTxt.getText().toString().trim().equals("")) {
                                return;
                            }
                            RequestBody formBody = new FormBody.Builder()
                                    .add("androidID", android_id)
                                    .add("mobile", "android")
                                    .add("name", cxTxt.getText().toString())
                                    .build();
                            final Request rq = new Request.Builder()
                                    .url("http://192.168.0.140/cadastrar.php")
                                    .post(formBody)
                                    .build();
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Response r = client.newCall(rq).execute();
                                        String j = r.body().string();
                                        usuario = objMp.readValue(j, Usuario.class);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            t.start();
                            try {
                                t.join();
                            } catch (InterruptedException u) {
                                u.printStackTrace();
                            }


                        }
                    });
                    ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (usuario == null) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ad.setMessage("Nome não pode ser vazio");
                                        final AlertDialog dialog = ad.create();
                                        dialog.show();
                                    }
                                });
                            } else {
                                proxTela();
                            }
                        }
                    });
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog dialog = ad.create();
                            dialog.show();
                        }
                    });
                }
            }
        }).start();

    }

    public void proxTela() {
        Intent i = new Intent(this, ListaProjetos.class);
        startActivity(i);
        finish();
    }

    public static Usuario getUsuario() {
        return usuario;
    }
}
