package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import com.aru.expapp.R;


public class SuccessfullyLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = SuccessfullyLoginActivity.this;

    private NestedScrollView nestedScrollView;

    private AppCompatButton appCompatButtonContinue;

    private AppCompatTextView textViewLinkCongratulations;
    private AppCompatTextView textViewLinkCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        appCompatButtonContinue = (AppCompatButton) findViewById(R.id.appCompatButtonContinue);
        textViewLinkCongratulations = (AppCompatTextView) findViewById(R.id.textViewLinkCongratulations);
        textViewLinkCreated = (AppCompatTextView) findViewById(R.id.textViewLinkCreated);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonContinue:
                redirectOnHomeActivity();
                break;
        }
    }

    private void redirectOnHomeActivity(){
        Intent intentUserInter = new Intent(SuccessfullyLoginActivity.this, HomeActivity.class);
        startActivity(intentUserInter);
    }

}