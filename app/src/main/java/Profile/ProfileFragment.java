package Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scanmore.Database.DatabaseHandler;
import com.example.scanmore.Database.User;
import com.example.scanmore.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {

TextView textView;

DatabaseHandler db;

    public ProfileFragment(){}


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new DatabaseHandler(getActivity());
        final List<User> users = db.getAllUsers();

        final User user1 = users.get(0);


        return view;
    }

}
