package br.com.fastertunnel.radarpokemon.ui.custom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import br.com.fastertunnel.radarpokemon.R;
import br.com.fastertunnel.radarpokemon.async.LoginTask;
import br.com.fastertunnel.radarpokemon.models.LoginData;
import br.com.fastertunnel.radarpokemon.utils.DataManager;

/**
 * Created by cassioisquierdo on 7/31/16
 */
public class LoginDialog extends Dialog {

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    View mLoginButton;
    View mCancelButton;
    ProgressDialog mProgressDialog;

    public LoginDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_login);

        mUsernameEditText = (EditText) findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(onLoginButtonClicked);
        mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(onCancelButtonClicked);
    }

    View.OnClickListener onLoginButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.empty_fields)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                return;
            }

            tryLogin(username, password);
        }
    };

    View.OnClickListener onCancelButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LoginDialog.this.dismiss();
        }
    };

    private void tryLogin(final String username, final String password) {
        mProgressDialog = ProgressDialog.show(getContext(), getContext().getString(R.string.login),
                getContext().getString(R.string.login_loading), true);

        LoginData loginData = new LoginData(username, password);

        new LoginTask(new LoginTask.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                hideKeyboard();

                DataManager.storeUsernameString(getContext(), username);
                DataManager.storePasswordString(getContext(), password);
                LoginDialog.this.dismiss();
            }

            @Override
            public void onLoginFailed() {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                hideKeyboard();

                Toast.makeText(getContext(), R.string.login_failed_try_again, Toast.LENGTH_SHORT).show();
            }
        }).execute(loginData);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
