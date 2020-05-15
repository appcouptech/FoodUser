package food.user.demand.Testing_New;

public class Description {
    private String id;
    private String product_name;
    private String product_description;
    private String category_id;
    private String cuisine_id;
    private String dish_id;
    private String photo;
    private String price;
    private String orignal_price;
    private String discount;
    private String discount_type;
    private String availability;
    private String price_status;
    private String addon_status;
//    private String ingredients;
//    private String preparations;
    private String next_avail_time1;
    private String next_avail_time2;
    private String next_avail_time3;
    private String next_available;
    private String quantity;
    public Description(String id,String product_name,String product_description,String category_id,
                       String cuisine_id,String dish_id,String photo,String price,String orignal_price,String discount,String discount_type,
                       String availability,String price_status,String addon_status,
                       String next_avail_time1,String next_avail_time2,String next_avail_time3,String next_available,String quantity) {
        this.id = id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        this.cuisine_id = cuisine_id;
        this.dish_id = dish_id;
        this.photo = photo;
        this.price = price;
        this.orignal_price = orignal_price;
        this.discount = discount;
        this.discount_type = discount_type;
        this.availability = availability;
        this.price_status = price_status;
        this.addon_status = addon_status;
//        this.ingredients = ingredients;
//        this.preparations = preparations;
        this.next_avail_time1 = next_avail_time1;
        this.next_avail_time2 = next_avail_time2;
        this.next_avail_time3 = next_avail_time3;
        this.next_available = next_available;
        this.quantity = quantity;

    }
    public String getid() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCuisine_id() {
        return cuisine_id;
    }

    public String getDish_id() {
        return dish_id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPrice() {
        return price;
    }

    public String getOrignal_price() {
        return orignal_price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public String getAvailability() {
        return availability;
    }

    public String getPrice_status() {
        return price_status;
    }

    public String getAddon_status() {
        return addon_status;
    }

//    public String getIngredients() {
//        return ingredients;
//    }
//
//    public String getPreparations() {
//        return preparations;
//    }

    public String getNext_avail_time1() {
        return next_avail_time1;
    }

    public String getNext_avail_time2() {
        return next_avail_time2;
    }

    public String getNext_avail_time3() {
        return next_avail_time3;
    }
    public String getnext_available() {
        return next_available;
    }
    public String getquantity() {
        return quantity;
    }

}
