package SignUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.LoginActivity;
import com.example.scanmore.R;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText edtName;
    EditText edtEmail2;
    EditText edtPassword2;
    TextView login;
    Button btnSignup;
    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtName = findViewById(R.id.input_name);
        edtEmail2 = findViewById(R.id.input_email);
        edtPassword2 = findViewById(R.id.input_password);
        login = findViewById(R.id.link_login);
        btnSignup = findViewById(R.id.btn_signup);
        databaseHandler = new DatabaseHandler(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    onSignupFailed();
                    return;
                }
                else {
                    signup();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", edtEmail2.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }

            }
        });
        //should go back to log in

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //end of onCreate method

    }
    public void signup() {
        Log.d(TAG, "Signup");
        btnSignup.setEnabled(false);
        String name = edtName.getText().toString();
        String email = edtEmail2.getText().toString();
        String password = edtPassword2.getText().toString();
        // TODO: Implement your own signup logic here.
        databaseHandler.addUser2(new User(name, email, password));
        Toast.makeText(SignupActivity.this, "Added User", Toast.LENGTH_SHORT).show();

        /*edtName.setText("");
        edtEmail2.setText("");
        edtPassword2.setText(""); */
    }
    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }

      @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

//checks if the inputs are valid
    public boolean validate() {
        boolean valid = true;
        String name = edtName.getText().toString();
        String email = edtEmail2.getText().toString();
        String password = edtPassword2.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            edtName.setError("at least 3 characters");
            valid = false;
        } else {
            edtName.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail2.setError("enter a valid email address");
            valid = false;
        } else {
            edtEmail2.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword2.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edtPassword2.setError(null);
        }
        return valid;
    }

}
