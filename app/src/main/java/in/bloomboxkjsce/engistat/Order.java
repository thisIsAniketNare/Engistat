package in.bloomboxkjsce.engistat;

/**
 * Created by Aniket on 23-07-2017.
 */
public class Order {
    public String getProductName() {
        return productName;
    }

    public String getCollegeName() {
        return userID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAmount() {
        return amount;
    }

    String productName;
    String userID;
    int quantity;
    String displayName;
    int amount;
    boolean isPlaced;
    boolean isDelivered;

    public Order(String userID, String productName, String displayName, int amount, int quantity, boolean isPlaced, boolean isDelivered) {
        this.userID = userID;
        this.productName = productName;
        this.displayName = displayName;
        this.amount = amount;
        this.quantity = quantity;
        this.isDelivered = isDelivered;
        this.isPlaced = isPlaced;
    }
}
