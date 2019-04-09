package Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.LoginActivity;
import com.example.scanmore.R;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    TextView emailView;
    TextView nameView;
    DatabaseHandler db;

    LoginActivity la = LoginActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar();
        db = new DatabaseHandler(this);
        final List<User> users = db.getAllUsers();

        //this method will only get the most current addded user
        User user = la.getActiveUser();

        nameView = findViewById(R.id.profile_name);
        nameView.setText("Name: " + user.getName());
        //nameView.append(user.getName());
        emailView = findViewById(R.id.profile_email);
        emailView.setText("Email: " + user.getEmail());
        //emailView.append(user.getEmail());

    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
