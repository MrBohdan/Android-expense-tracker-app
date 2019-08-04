package activities;

import com.aru.expapp.R;
import validation.InputValidation;
import adapters.UsersAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import database.DBcatch;
import global.GlobalProperties;
import models.Settings;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private final AppCompatActivity activity = SettingsActivity.this;
    final Context context = this;

    private Switch simpleSwitchPieChart, simpleSwitchBarChart, simpleSwitchRadarChart;
    private AppCompatTextView textViewName, textViewEmail, appCompatTextViewCancelLink;
    private AppCompatImageView textViewEdit, textViewEditPass;
    private AppCompatButton appCompatButtonModify;
    private LinearLayout linerLayout;
    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextPassword, textInputEditTextConfirmPassword;
    private ArrayList<User> getUser;
    private ArrayList<Settings> getSettings;
    private Dialog dialog;
    private List<User> listUsers;
    private String checkEmail, checkEmailUsed;
    private UsersAdapter usersAdapter;
    private InputValidation inputValidation;
    private DBcatch dbCatch;
    private Settings settings;
    private User user;
    private int user_id, settingsID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_home) : This will set activity_home.xml layout as UI of your app.
         * it will helps system to easily access the layout from resources/layout directory.
         */
        setContentView(R.layout.activity_preferences);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        setUpDialogEM();
        setUpDialogPass();
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        dbCatch = new DBcatch(activity);
        listUsers = new ArrayList<>();
        usersAdapter = new UsersAdapter(listUsers);
        user = new User();
        settings = new Settings();

        user_id = ((GlobalProperties) this.getApplication()).getUserTokenVariable();
        setUpSettings();
    }

    private void initViews() {
        linerLayout = (LinearLayout) findViewById(R.id.linerLayout);
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        textViewEmail = (AppCompatTextView) findViewById(R.id.textViewEmail);
        textViewEdit = (AppCompatImageView) findViewById(R.id.textViewEdit);
        textViewEditPass = (AppCompatImageView) findViewById(R.id.textViewEditPass);
        simpleSwitchPieChart = (Switch) findViewById(R.id.simpleSwitchPieChart);
        simpleSwitchBarChart = (Switch) findViewById(R.id.simpleSwitchBarChart);
        simpleSwitchRadarChart = (Switch) findViewById(R.id.simpleSwitchRadarChart);

    }

    private void setUpDialogEM() {
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_edit_prof_popup);
                dialog.setTitle("Edit Bill");
                //-
                initDialogViews();
                for (User u : getUser) {
                    Log.d("GET USER", "The User extracted user " + String.valueOf(getUser.size()));
                    Log.d("GET USER", "Email " + u.getEmail() + " /Name " + u.getName());
                    textInputEditTextName.setText(u.getName());
                    textInputEditTextEmail.setText(u.getEmail());
                    checkEmailUsed = textViewEmail.getText().toString().trim();
                    // if button is clicked, close the custom dialog
                    appCompatButtonModify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            postDataToSQLite();
                        }
                    });
                    // if button is clicked, close the custom dialog
                    appCompatTextViewCancelLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                //to make layout behind routed corners transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

            private void initDialogViews() {
                textInputLayoutName = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutName);
                textInputEditTextName = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextName);

                textInputLayoutEmail = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutEmail);
                textInputEditTextEmail = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextEmail);

                appCompatButtonModify = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonModify);
                appCompatTextViewCancelLink = (AppCompatTextView) dialog.findViewById(R.id.appCompatTextViewCancelLink);
            }
        });
    }

    private void setUpDialogPass() {
        textViewEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_settings_change_password_popup);
                dialog.setTitle("Edit Bill");
                //-
                initDialogViewsPass();

                // if button is clicked, close the custom dialog
                appCompatButtonModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postPassToSQL();
                    }
                });
                // if button is clicked, close the custom dialog
                appCompatTextViewCancelLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //to make layout behind routed corners transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

            private void initDialogViewsPass() {
                textInputLayoutPassword = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutPassword);
                textInputLayoutConfirmPassword = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutConfirmPassword);

                textInputEditTextPassword = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextPassword);
                textInputEditTextConfirmPassword = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextConfirmPassword);

                appCompatButtonModify = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonModify);
                appCompatTextViewCancelLink = (AppCompatTextView) dialog.findViewById(R.id.appCompatTextViewCancelLink);
            }
        });
    }

    private void setUpSettings() {
        getUser = dbCatch.getUserByID(user_id);
        for (User u : getUser) {
            Log.d("GET USER", "The User extracted user " + String.valueOf(getUser.size()));
            Log.d("GET USER", "Email " + u.getEmail() + " /Name " + u.getName()
                    + " /Password" + u.getPassword());
            textViewName.setText(u.getName());
            textViewEmail.setText(u.getEmail());
        }
        getSettings = dbCatch.getSettingsByID(user_id);
        for (Settings s : getSettings) {
            Log.d("GET SETTINGS", "The Settings extracted settings " + String.valueOf(getSettings.size()));
            Log.d("GET SETTINGS", " Settings IF " + s.getId() + "Pie Chart " + s.isPieChart() + " /Bar Chart " + s.isBarChart()
                    + " /Radar Chart " + s.isRadarChart());
            settingsID = s.getId();
            simpleSwitchPieChart.setChecked(s.isPieChart());
            simpleSwitchBarChart.setChecked(s.isBarChart());
            simpleSwitchRadarChart.setChecked(s.isRadarChart());
        }
        simpleSwitchPieChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitchPieChart.isChecked()) {
                    simpleSwitchBarChart.setChecked(false);
                    simpleSwitchRadarChart.setChecked(false);
                }
                SettingsActivity.this.updateSettings();
            }
        });
        simpleSwitchBarChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitchBarChart.isChecked()) {
                    simpleSwitchPieChart.setChecked(false);
                    simpleSwitchRadarChart.setChecked(false);
                }
                SettingsActivity.this.updateSettings();
            }
        });
        simpleSwitchRadarChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitchRadarChart.isChecked()) {
                    simpleSwitchPieChart.setChecked(false);
                    simpleSwitchBarChart.setChecked(false);
                }
                SettingsActivity.this.updateSettings();
            }
        });
    }

    private void updateSettings() {
        settings.setId(settingsID);
        settings.setUser_ID(user_id);
        if (simpleSwitchPieChart.isChecked()) {
            settings.setPieChart(true);
            settings.setBarChart(false);
            settings.setRadarChart(false);
        }if (simpleSwitchBarChart.isChecked()) {
            settings.setPieChart(false);
            settings.setBarChart(true);
            settings.setRadarChart(false);
        }if (simpleSwitchRadarChart.isChecked()) {
            settings.setPieChart(false);
            settings.setBarChart(false);
            settings.setRadarChart(true);
        }
        dbCatch.updateSettings(settings);
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        checkEmail = textInputEditTextEmail.getText().toString().trim();
        if (checkEmail.equals(checkEmailUsed)) {
            postDataToSQL();
        } else if (!dbCatch.checkUser(textInputEditTextEmail.getText().toString().trim())) {
            postDataToSQL();
        } else {
            // Toast message to show error message that record already exists
            Toast toast = Toast.makeText(SettingsActivity.this, getString(R.string.error_email_exists), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
        }
    }

    private void postPassToSQL() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        user.setId(user_id);
        user.setPassword(textInputEditTextPassword.getText().toString().trim());
        dbCatch.updateUserPassword(user);
        Toast toast = Toast.makeText(SettingsActivity.this, getString(R.string.error_password_changed), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(20);
        toast.show();
        Intent intentUserInter = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(intentUserInter);
    }

    private void postDataToSQL() {
        user.setId(user_id);
        user.setName(textInputEditTextName.getText().toString().trim());
        user.setEmail(textInputEditTextEmail.getText().toString().trim());
        Toast toast = Toast.makeText(SettingsActivity.this, getString(R.string.error_info_changed), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(20);
        toast.show();
        dbCatch.updateUser(user);
        Intent intentUserInter = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(intentUserInter);
    }

    /**
     * This method is to empty all input edit text
     */

    @Override
    public void onBackPressed() {
        Intent intentUserInter = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intentUserInter);
    }
}
