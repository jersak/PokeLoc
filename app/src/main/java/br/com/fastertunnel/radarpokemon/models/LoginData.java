package br.com.fastertunnel.radarpokemon.models;

/**
 * Created by Fuzi on 26/07/2016.
 */
public class LoginData {
    String user;
    String senha;

    public LoginData(String user, String senha) {
        this.user = user;
        this.senha = senha;
    }

    public String getUser() {
        return user;
    }

    public String getSenha() {
        return senha;
    }
}
