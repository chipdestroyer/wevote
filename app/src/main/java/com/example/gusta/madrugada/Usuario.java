package com.example.gusta.madrugada;

/**
 * Created by gusta on 08/03/2017.
 */

public class Usuario {
    private String idAndroid;
    private String nome;

    public Usuario() {
        this("", "");
    }

    public Usuario(String idAndroid, String nome) {
        this.idAndroid = idAndroid;
        this.nome = nome;
    }

    public String getIdAndroid() {
        return idAndroid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
