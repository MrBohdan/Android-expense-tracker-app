package activities;

import adapters.CategoryAdapter;
import com.aru.expapp.R;
import models.Category;
import validation.InputValidation;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import database.DBcatch;
import global.GlobalProperties;
import models.Settings;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity  extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private DBcatch dbCatch;
    private User user;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private Category category;
    private String retrivedUserID;
    private Settings settings;
    private int user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_register) : This will set activity_register.xml layout as UI of your app.
         * it will helps system to easily access the layout from resources/layout directory.
         */
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        category = new Category();
        inputValidation = new InputValidation(activity);
        dbCatch = new DBcatch(activity);
        user = new User();
        settings = new Settings();
    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!dbCatch.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            dbCatch.addUser(user);
            user_id = dbCatch.getUser(textInputEditTextEmail.getText().toString().trim()).getId();
            ((GlobalProperties) this.getApplication()).setUserTokenVariable(user_id);
            retrivedUserID = Integer.toString(user_id);
            setDefault();
            settings.setUser_ID(user_id);
            settings.setPieChart(true);
            settings.setBarChart(false);
            settings.setRadarChart(false);
            dbCatch.createSettings(settings);
            emptyInputEditText();
            Intent intentUserInter = new Intent(RegisterActivity.this, SuccessfullyLoginActivity.class);
            startActivity(intentUserInter);


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
    /**
     * This method is to fetch all user records from SQLite
     */
    public void setDefault(){
        if (!dbCatch.checkCategory("Food & Beverages",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Food & Beverages");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("Transportation",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Transportation");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("Accommodation",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Accommodation");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("Education",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Education");
            dbCatch.addCategory(category);
        }  if (!dbCatch.checkCategory("Entertainment",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Entertainment");
            dbCatch.addCategory(category);
        }  if (!dbCatch.checkCategory("Financial Services",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Financial Services");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("Insurance",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Insurance");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("Sport",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Sport");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("Telecommunication",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Telecommunication");
            dbCatch.addCategory(category);
        } if(!dbCatch.checkCategory("Others",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("Others");
            dbCatch.addCategory(category);
        }
    }
}
