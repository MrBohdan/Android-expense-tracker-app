package activities;

import android.util.Log;
import com.aru.expapp.R;
import org.opencv.android.OpenCVLoader;
import validation.InputValidation;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.facebook.stetho.Stetho;
import database.DBcatch;
import global.GlobalProperties;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DBcatch dbCatch;
    private int user_id;

    //To set constant admin
    private String email_admin = "admin@admin.com";
    private String password_admin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_login) : This will set activity_login.xml layout as UI of your app.
         * it will helps system to easily access the layout from resources/layout directory.
         */
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        //chrome://inspect/#devices
        Stetho.initializeWithDefaults(this);
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        /**
         * initialize TextInputLayout view
         */
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        /**
         * initialize TextInputEditText view
         */
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        dbCatch = new DBcatch(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (dbCatch.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {

            DBcatch dbCatch = new DBcatch(LoginActivity.this);
            /**
             * 1) Get user USER_ID and pass to GlobalProperties
             */
            user_id = dbCatch.getUser(textInputEditTextEmail.getText().toString().trim()).getId();
            ((GlobalProperties) this.getApplication()).setUserTokenVariable(user_id);
            /**
             * 2)Redirection to HomeActivity interface
             */
            Intent intentUserInter = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intentUserInter);

        } else if (textInputEditTextEmail.getText().toString().trim().equals(email_admin) &&
                textInputEditTextPassword.getText().toString().trim().equals(password_admin)) {
            /**
             * 1) Get admin EMAIL and pass to HomeActivity
             * 1.1) Redirection to UsersListActivity interface
             */
            Intent accountsIntent = new Intent(activity, UsersListActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);

        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intentUserInter = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentUserInter);
    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

}
