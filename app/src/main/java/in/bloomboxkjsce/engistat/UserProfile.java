package in.bloomboxkjsce.engistat;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Aniket on 28-06-2017.
 */


public class UserProfile extends AppCompatActivity {

    String TAG = "EmailPassword";
    private TextView aadhar;
    private TextInputLayout emailId;
    private TextInputLayout mobile;
    private TextView college;
    private TextInputLayout firstName;
    private TextInputLayout lastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        aadhar = (TextView) findViewById(R.id.account_aadhar);
        emailId = (TextInputLayout) findViewById(R.id.account_emailId);
        firstName = (TextInputLayout) findViewById(R.id.account_first_name);
        lastName = (TextInputLayout) findViewById(R.id.account_last_name);
        mobile = (TextInputLayout) findViewById(R.id.account_mobile_number);
        college = (TextView) findViewById(R.id.account_college_name);

        FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                aadhar.setText(user.getAadhar());
                firstName.getEditText().setText(user.getFirstName());
                lastName.getEditText().setText(user.getLastName());
                college.setText(user.getCollegeName());
                mobile.getEditText().setText(user.getMobile());
                emailId.getEditText().setText(user.getEmailId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}