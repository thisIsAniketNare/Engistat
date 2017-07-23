package in.bloomboxkjsce.engistat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Aniket on 14-07-2017.
 */
public class RequestListActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_request_list);

        final ListView listView = (ListView) findViewById(R.id.request_list);
        final ArrayList<Order> products = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("orders").child("rent").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        for(DataSnapshot ds : iterable){
                            Order product = ds.getValue(Order.class);
                            products.add(product);
                            //Add a custom adapter to display request list to admin
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
