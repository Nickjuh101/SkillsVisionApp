package skillsvision.skillsvisionapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import skillsvision.skillsvisionapp.R;

public class RegisterAccountActivity extends AppCompatActivity {

    private static Holder mHolder = new Holder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        /* Initialize the views */
        initViews();

        mHolder.mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivityIntent = new Intent(RegisterAccountActivity.this, StartActivity.class);
                startActivity(registerActivityIntent);
                finish();
            }
        });

    }

    private void initViews(){
        /* Toolbar */
        mHolder.mToolbar = (Toolbar)findViewById(R.id.toolbar_register_account);
        mHolder.mBackButton = (ImageView)findViewById(R.id.toolbar_register_account_back_button);

        /* Fields */
        mHolder.mEmailEditText = (EditText)findViewById(R.id.activity_register_email_field);
        mHolder.mPasswordEditText = (EditText)findViewById(R.id.activity_register_password_field);
        mHolder.mConfirmPasswordEditText = (EditText)findViewById(R.id.activity_register_confirm_password_field);
        mHolder.mBirthdateEditText = (EditText)findViewById(R.id.activity_register_birthdate_field);
        mHolder.mLocationEditText = (EditText)findViewById(R.id.activity_register_location_field);

        /* Button */
        mHolder.mRegisterAccountButton = (Button)findViewById(R.id.activity_register_register_account_button);

    }


    private static class Holder{
        Toolbar mToolbar;
        ImageView mBackButton;
        Button mRegisterAccountButton;
        EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText, mBirthdateEditText, mLocationEditText;
    }
}
