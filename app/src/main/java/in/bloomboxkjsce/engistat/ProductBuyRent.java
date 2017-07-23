package in.bloomboxkjsce.engistat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Aniket on 10-07-2017.
 */
public class ProductBuyRent extends AppCompatActivity {

    private ImageView mImageView;
    private Button mBuy;
    private Button mRent;
    private boolean isBuy;
    private TextView mName;
    private String pathName;
    private String productName;
    private int resourceID;
    int quantity;
    int basePrice = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImageView = (ImageView) findViewById(R.id.image);
        mBuy = (Button) findViewById(R.id.buy);
        mRent = (Button) findViewById(R.id.rent);
        mName = (TextView) findViewById(R.id.name);
        mName.setText(productName);

        //GET EXTRAS FROM ACTIVITY
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                productName = null;
            } else {
                productName = extras.getString("PRODUCT_NAME");
                isBuy = extras.getBoolean("IS_BUY");
                basePrice = extras.getInt("PRODUCT_PRICE");
                pathName = extras.getString("PRODUCT_PATH");
                resourceID = extras.getInt("IMAGE");
            }
        } else {
            productName = (String) savedInstanceState.getSerializable("PRODUCT_NAME");
            basePrice = (int) savedInstanceState.getSerializable("PRODUCT_PRICE");
            isBuy = false;
        }

        final Order order = new Order(FirebaseAuth.getInstance().getCurrentUser().getUid()
                ,productName
                ,FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                ,quantity
                ,calculatePrice()
                ,true
                ,false);

        final long currentTime = System.currentTimeMillis(); //Using as orderID

        if (isBuy) {  //To write in buy database

            //Since cannot pass bitmap from previous activity
            final long ONE_MEGABYTE = 1024*1024;
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(
                    "gs://engistat-9a8f7.appspot.com").child(pathName);


            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mImageView.setImageBitmap(bitmap);
                }
            });

            mBuy = (Button) findViewById(R.id.buy);
            mBuy.setVisibility(View.VISIBLE);
            mRent.setVisibility(View.GONE);
            mBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseDatabase.getInstance().getReference().child("orders").child("buy")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int count = 0;
                                    //COUNT ISSUED PRODUCTS
                                    Iterable<DataSnapshot> iterable =  dataSnapshot.getChildren();
                                    for(DataSnapshot ds : iterable){
                                        if(ds.child("userID").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            count = count + (int) ds.child("quantity").getValue();
                                        }
                                    }
                                    if(count>3){
                                        Toast.makeText(ProductBuyRent.this,"You cannot have more than 3 products",Toast.LENGTH_SHORT).show();
                                    }else{
                                        //WRITE ORDER FOR CURRENT USER

                                        FirebaseDatabase.getInstance().getReference().child("orders").child("buy")
                                                .child(currentTime + "").setValue(order);

                                        Toast.makeText(ProductBuyRent.this, "Your Order is successfully placed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    Intent intent = new Intent(ProductBuyRent.this, ProductsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            mImageView.setImageResource(resourceID);
            mRent.setVisibility(View.VISIBLE);
            mBuy.setVisibility(View.GONE);
            mRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("orders").child("rent")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int count = 0;
                            Iterable<DataSnapshot> iterable =  dataSnapshot.getChildren();
                            for(DataSnapshot ds : iterable){
                                if(ds.child("userID").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    count = count + (int) ds.child("quantity").getValue();
                                }
                            }
                            if(count>3){
                                Toast.makeText(ProductBuyRent.this,"You cannot have more than 3 products",Toast.LENGTH_SHORT).show();
                            }else{
                                //WRITE ORDER FOR CURRENT USER if issued products is less than 3
                                FirebaseDatabase.getInstance().getReference().child("orders").child("rent")
                                        .child(currentTime + "").setValue(order);
                                Toast.makeText(ProductBuyRent.this, "Your Order is successfully placed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Intent intent = new Intent(ProductBuyRent.this, ProductsActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    public void increment(View view){
        quantity=quantity+1;
        display(quantity);
    }

    public void decrement(View view){
        quantity=quantity-1;
        if(quantity<1)
        {
            quantity=1;
            Toast.makeText(this,"You cannot order less than 1 product",Toast.LENGTH_SHORT).show();
        }
        display(quantity);
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private int calculatePrice(){
        int price = 0;
        price = price + basePrice*quantity;
        return price;
    }
}
