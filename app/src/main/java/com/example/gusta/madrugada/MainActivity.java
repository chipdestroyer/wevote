package com.example.gusta.madrugada;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AsyncResponse, View.OnClickListener {

    EditText etUsername, etSenha;
    Button btnLogin, btnCadastrar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etSenha = (EditText) findViewById(R.id.etSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap postData = new HashMap();
                postData.put("mobile", "android");
                postData.put("txtUsername", etUsername.getText().toString());
                postData.put("txtPassword", etSenha.getText().toString());
                PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData);

                try {
                    MainActivity.this.processFinish(task.execute("http://192.168.1.140/login.php").get());
                    //Toast.makeText(MainActivity.this, task.execute("http://192.168.1.140/login.php").getStatus()+"" ,Toast.LENGTH_LONG).show();

                    System.out.println(task.get());

                }catch(Exception e){
                    e.printStackTrace();
                }


            }
        });
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap postData = new HashMap();
                postData.put("mobile", "android");
                postData.put("txtUsername", etUsername.getText().toString());
                postData.put("txtPassword", etSenha.getText().toString());
                PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData);
                if (!etSenha.getText().toString().trim().equals(""))
                    task.execute("http://192.168.1.140/cadastrar.php");
            }
        });
        /*     etUsername = (EditText) findViewById(R.id.etUsername);
        etSenha = (EditText) findViewById(R.id.etSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);*/
}

    @Override
    public void processFinish(String result) {
    if(result.equals("success")){
        Intent in =  new Intent(this, SubActivity.class);
        startActivity(in);
    }
    else
    {
        Toast.makeText(this, "failed" , Toast.LENGTH_LONG).show();
    }
}
    @Override
    public void onClick(View v) {
        String btnID = v.getResources().getResourceName(v.getId());


        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtUsername", etUsername.getText().toString());
        postData.put("txtPassword", etSenha.getText().toString());
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        if(btnID =="com.example.gusta.madrugada:id/btnLogin") {
            task.execute("http://192.168.1.140/login.php");
        }else {
            task.execute("http://192.168.1.140/cadastrar.php");
        }
    }

    //public void cadastrar(View v){
        //HashMap postData = new HashMap();
       // postData.put("mobile", "android");
        //postData.put("txtUsername", etUsername.getText().toString());
        //postData.put("txtPassword", etSenha.getText().toString());
        //PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        //task.execute("http://192.168.1.55/cadastrar.php");
   // }
}
