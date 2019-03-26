package SignUp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.R;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText edtName = findViewById(R.id.input_name);

    EditText edtEmail2 = findViewById(R.id.input_email);

    EditText edtPassword2 = findViewById(R.id.input_password);

    TextView login = findViewById(R.id.link_login);

    Button btnSignup = findViewById(R.id.btn_signup);

    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        databaseHandler = new DatabaseHandler(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {

        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }


        btnSignup.setEnabled(false);


        String name = edtName.getText().toString();
        String email = edtEmail2.getText().toString();
        String password = edtPassword2.getText().toString();

        // TODO: Implement your own signup logic here.

        databaseHandler.addUser(new User(email, password));

        Toast.makeText(SignupActivity.this, "Added User", Toast.LENGTH_SHORT).show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();

                    }
                }, 3000);
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
