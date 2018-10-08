package skillsvision.skillsvisionapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import skillsvision.skillsvisionapp.R;

public class StartActivity extends AppCompatActivity {

    /** The view holder for this class **/
    private static Holder mHolder = new Holder();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        initViews();

        mHolder.mGoToRegisterActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivityIntent = new Intent(StartActivity.this, RegisterAccountActivity.class);
                startActivity(registerActivityIntent);
                finish();
            }
        });
        mHolder.mGoToLoginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginActivityIntent);
                finish();
            }
        });

    }

    /* Initialize all Views*/
    private void initViews(){
        mHolder.mGoToRegisterActivityButton = (Button)findViewById(R.id.activity_start_register_button);
        mHolder.mGoToLoginActivityButton = (Button)findViewById(R.id.activity_start_login_button);
        mHolder.mSkillsVisionLogo = (ImageView)findViewById(R.id.activity_start_logo);
    }

    /* Holder for this activity*/
    private static class Holder {
        Button mGoToRegisterActivityButton;
        Button mGoToLoginActivityButton;
        ImageView mSkillsVisionLogo;
    }



}
