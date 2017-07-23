package in.bloomboxkjsce.engistat;

import android.graphics.Bitmap;

/**
 * Created by Aniket on 06-07-2017.
 */
public class Product {

    private String productName;
    private int productPrice;
    private String description;
    private int imageResource;
    private String pathName;

    public Product(){}

    public Product(String productName, int imageResource) {
        this.productName = productName;
        this.imageResource = imageResource;
    }

    public String getPathName() {return pathName;}

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getImageResource(){ return imageResource;}

    public String getDescription(){ return description;}
}
