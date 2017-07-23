package in.bloomboxkjsce.engistat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Aniket on 28-06-2017.
 */
public class MyAccount extends AppCompatActivity {

    private Intent myAccountToSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        myAccountToSignin = new Intent(MyAccount.this, LoginActivity.class);

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAccountToProfile = new Intent(MyAccount.this,UserProfile.class);
                startActivity(myAccountToProfile);
            }
        });


        findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginActivity().signOut();
                startActivity(myAccountToSignin);
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if firebaseUser is signed in (non-null) and update UI accordingly.
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(myAccountToSignin);
        }

    }
    // [END on_start_check_user]


    @Override
    protected void onRestart() {
        super.onRestart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(myAccountToSignin);
        }
    }

}
