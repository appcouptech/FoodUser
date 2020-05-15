package food.user.demand.FCPojo.FCCartActivityObject;

public class AddonObject {

    private String d_images ;
    private String d_info ;
    private String id ;
    private String addon_name ;
    private String price ;
    private String sequence ;
    private String status ;
    private boolean isSelected;
    private String currency;
    private String addon_category_id;

    public String getD_images() {
        return d_images;
    }

    public void setD_images(String d_images) {
        this.d_images = d_images;
    }

    public String getD_info() {
        return d_info;
    }

    public void setD_info(String d_info) {
        this.d_info = d_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getAddon_name() {
        return addon_name;
    }

    public void setAddon_name(String addon_name) {
        this.addon_name = addon_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddon_category_id() {
        return addon_category_id;
    }

    public void setAddon_category_id(String addon_category_id) {
        this.addon_category_id = addon_category_id;
    }
}
