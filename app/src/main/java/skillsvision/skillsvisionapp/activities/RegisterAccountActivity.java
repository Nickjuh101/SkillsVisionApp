package skillsvision.skillsvisionapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import skillsvision.skillsvisionapp.R;
import skillsvision.skillsvisionapp.network.MySingleton;

public class RegisterAccountActivity extends AppCompatActivity {

    private static Holder mHolder = new Holder();

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_DATE_REGEX =
            Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

    String reg_url = "http://192.168.8.107:80/skillsvision/register_user.php";

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

        mHolder.mRegisterAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkEmptyFields()){
                    return;
                } else {
                    final String emailadress = mHolder.mEmailEditText.getText().toString().trim();
                    final String password;
                    final String birthdate;
                    final String location;

                    if (!VALID_EMAIL_ADDRESS_REGEX.matcher(emailadress).matches()){
                        mHolder.mEmailEditText.requestFocus();
                        mHolder.mEmailEditText.setError("The provided emailadress is invalid");
                        return;
                    } else if(mHolder.mPasswordEditText.getText().toString().length() <= 4){
                        mHolder.mPasswordEditText.requestFocus();
                        mHolder.mPasswordEditText.setError("A password should be atleast 4 characters long. \n" +
                                                           "A strong password has the following:\n" +
                                                           " - Atleast 1 lowercase letter\n" +
                                                           " - Atleast 1 uppercase letter\n" +
                                                           " - Atleast 1 number\n" +
                                                           " - Atleast 1 character");
                        return;
                    } else if (!mHolder.mPasswordEditText.getText().toString().equals(mHolder.mConfirmPasswordEditText.getText().toString())){
                        mHolder.mConfirmPasswordEditText.requestFocus();
                        mHolder.mPasswordEditText.setError("Passwords are not identical");
                        mHolder.mConfirmPasswordEditText.setError("Passwords are not identical");
                        return;
                    } else if(!VALID_DATE_REGEX.matcher(mHolder.mBirthdateEditText.getText().toString()).matches()){
                        mHolder.mBirthdateEditText.requestFocus();
                        mHolder.mBirthdateEditText.setError("Field is empty or birthdate should be written as DD-MM-YYYY");
                        return;
                    } else {
                        password = mHolder.mPasswordEditText.getText().toString().trim();
                        /* Parse Date into correct format for database */
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date dateObject = null;
                        birthdate = mHolder.mBirthdateEditText.getText().toString();
                        try {
                            dateObject = formatter.parse(birthdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        location = mHolder.mLocationEditText.getText().toString().trim();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    String message = jsonObject.getString("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERROR", "Error occurred ", error);
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("emailadress", emailadress);
                                params.put("password", password);
                                params.put("birthdate", birthdate);
                                params.put("country", location);
                                return params;
                            }
                        };
                        MySingleton.getInstance(RegisterAccountActivity.this).addToRequestqueue(stringRequest);
                    }
                }
            }
        });
    }

    private Boolean checkEmptyFields(){
        if (mHolder.mEmailEditText.getText().toString().isEmpty() &&
                mHolder.mPasswordEditText.getText().toString().isEmpty() &&
                mHolder.mConfirmPasswordEditText.getText().toString().isEmpty()&&
                mHolder.mBirthdateEditText.getText().toString().isEmpty() &&
                mHolder.mLocationEditText.getText().toString().isEmpty()){
            mHolder.mEmailEditText.setError("Field is empty");
            mHolder.mPasswordEditText.setError("Field is empty");
            mHolder.mConfirmPasswordEditText.setError("Field is empty");
            mHolder.mBirthdateEditText.setError("Field is empty");
            mHolder.mLocationEditText.setError("Field is empty");
            Toast.makeText(RegisterAccountActivity.this, "All fields are empty", Toast.LENGTH_LONG).show();
            mHolder.mEmailEditText.requestFocus();
            return false;
        }else if (mHolder.mPasswordEditText.getText().toString().isEmpty() &&
                mHolder.mConfirmPasswordEditText.getText().toString().isEmpty()&&
                mHolder.mBirthdateEditText.getText().toString().isEmpty() &&
                mHolder.mLocationEditText.getText().toString().isEmpty()){
            mHolder.mEmailEditText.setError(null);
            mHolder.mPasswordEditText.setError("Field is empty");
            mHolder.mConfirmPasswordEditText.setError("Field is empty");
            mHolder.mBirthdateEditText.setError("Field is empty");
            mHolder.mLocationEditText.setError("Field is empty");
            Toast.makeText(RegisterAccountActivity.this, "Some fields are empty", Toast.LENGTH_LONG).show();
            mHolder.mPasswordEditText.requestFocus();
            return false;
        } else if (mHolder.mConfirmPasswordEditText.getText().toString().isEmpty()&&
                mHolder.mBirthdateEditText.getText().toString().isEmpty() &&
                mHolder.mLocationEditText.getText().toString().isEmpty()){
            mHolder.mConfirmPasswordEditText.setError("Field is empty");
            mHolder.mBirthdateEditText.setError("Field is empty");
            mHolder.mLocationEditText.setError("Field is empty");
            mHolder.mEmailEditText.setError(null);
            mHolder.mPasswordEditText.setError(null);
            Toast.makeText(RegisterAccountActivity.this, "Some fields are empty", Toast.LENGTH_LONG).show();
            mHolder.mConfirmPasswordEditText.requestFocus();
            return false;
        }else if (mHolder.mBirthdateEditText.getText().toString().isEmpty() &&
                mHolder.mLocationEditText.getText().toString().isEmpty()){
            mHolder.mBirthdateEditText.setError("Field is empty");
            mHolder.mLocationEditText.setError("Field is empty");
            mHolder.mEmailEditText.setError(null);
            mHolder.mPasswordEditText.setError(null);
            mHolder.mConfirmPasswordEditText.setError(null);
            Toast.makeText(RegisterAccountActivity.this, "Some fields are empty", Toast.LENGTH_LONG).show();
            mHolder.mBirthdateEditText.requestFocus();
            return false;
        }else if (mHolder.mLocationEditText.getText().toString().isEmpty()){
            mHolder.mLocationEditText.setError("Field is empty");
            mHolder.mEmailEditText.setError(null);
            mHolder.mPasswordEditText.setError(null);
            mHolder.mConfirmPasswordEditText.setError(null);
            mHolder.mBirthdateEditText.setError(null);
            Toast.makeText(RegisterAccountActivity.this, "A field is empty", Toast.LENGTH_LONG).show();
            return false;
        } else {
            mHolder.mEmailEditText.setError(null);
            mHolder.mPasswordEditText.setError(null);
            mHolder.mConfirmPasswordEditText.setError(null);
            mHolder.mBirthdateEditText.setError(null);
            mHolder.mLocationEditText.setError(null);
            return true;
        }
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
