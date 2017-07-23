package in.bloomboxkjsce.engistat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Aniket on 06-07-2017.
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    private boolean isBuy;

    public ProductAdapter (Context context, ArrayList<Product> products, boolean isBuy) {
        super(context, 0, products);
        this.isBuy = isBuy;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Product currentProduct = getItem(position);

        if(isBuy) {
            TextView productName = (TextView) listView.findViewById(R.id.product_name);
            productName.setText(currentProduct.getProductName() + "\n" + currentProduct.getProductPrice());

            final ImageView productImage = (ImageView) listView.findViewById(R.id.product_image);

            final long ONE_MEGABYTE = 1024 * 1024;

            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(
                        "gs://engistat-9a8f7.appspot.com").child(currentProduct.getPathName());


            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        productImage.setImageBitmap(bitmap);
                    }
            });
        }
        else{
            TextView productName = (TextView) listView.findViewById(R.id.product_name);
            productName.setText(currentProduct.getProductName());

            ImageView productImage = (ImageView) listView.findViewById(R.id.product_image);
            productImage.setImageResource(currentProduct.getImageResource());
        }

        return listView;
    }
}
