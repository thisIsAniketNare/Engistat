package in.bloomboxkjsce.engistat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Aniket on 06-07-2017.
 */
public class ProductsActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private final ArrayList<Product> products = new ArrayList<>();
    private boolean buy = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.products_list);

        final ListView listView = (ListView) findViewById(R.id.list);
        findViewById(R.id.nav_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy = true;
            }
        });
        findViewById(R.id.nav_rent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy = false;
            }
        });

        if(!buy){
            ArrayList<Product> rentList = new ArrayList();
            rentList.add(new Product("Arduino - UNO", R.drawable.arduino));
            rentList.add(new Product("Raspberry Pi", R.drawable.raspberry));
            rentList.add(new Product("MBed", R.drawable.mbed));
            rentList.add(new Product("Development Board", R.drawable.atx_board));
            rentList.add(new Product("Micro - Controller 8051", R.drawable.micro8051));
            ProductAdapter rentAdapter = new ProductAdapter(this, rentList, buy);
            listView.setAdapter(rentAdapter);
        }
        else {
            showProgressDialog();
            FirebaseDatabase.getInstance().getReference().child("products").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                            for (DataSnapshot ds : iterable) {
                                Product product = ds.getValue(Product.class);
                                products.add(product);
                                ProductAdapter adapter = new ProductAdapter(ProductsActivity.this, products, buy);
                                listView.setAdapter(adapter);
                            }
                            hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductsActivity.this, ProductBuyRent.class);
                Product clickedProduct = (Product) parent.getItemAtPosition(position);
                intent.putExtra("PRODUCT_NAME", clickedProduct.getProductName());
                intent.putExtra("IS_BUY",buy);
                if(buy){
                    intent.putExtra("PRODUCT_PRICE",clickedProduct.getProductPrice());
                    intent.putExtra("PRODUCT_PATH",clickedProduct.getPathName());
                }else{
                    intent.putExtra("IMAGE",clickedProduct.getImageResource());
                }
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}