package br.com.fastertunnel.pokeloc.async;

import android.os.AsyncTask;
import android.os.StrictMode;

import br.com.fastertunnel.pokeloc.manager.PokeManager;
import br.com.fastertunnel.pokeloc.models.LoginData;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcCredentialProvider;

import okhttp3.OkHttpClient;

/**
 * Created by Fuzi on 26/07/2016
 */
public class LoginTask extends AsyncTask<LoginData, Void, PokemonGo> {

    public interface LoginCallback {
        void onLoginSuccess();

        void onLoginFailed();
    }

    LoginCallback mCallback;

    public LoginTask(LoginCallback callback) {
        mCallback = callback;
    }

    @Override
    protected PokemonGo doInBackground(LoginData... loginDatas) {
        if (loginDatas == null || loginDatas.length == 0 || loginDatas[0] == null) {
            return null;
        }

        LoginData loginData = loginDatas[0];

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient httpClient = new OkHttpClient();
            PtcCredentialProvider provider = new PtcCredentialProvider(httpClient, loginData.getUser(), loginData.getSenha());
            return new PokemonGo(provider, httpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(PokemonGo go) {
        super.onPostExecute(go);

        if (mCallback == null) {
            return;
        }

        if (go == null) {
            mCallback.onLoginFailed();
        } else {
            PokeManager.getInstance().setPokemonGo(go);
            mCallback.onLoginSuccess();
        }
    }
}
