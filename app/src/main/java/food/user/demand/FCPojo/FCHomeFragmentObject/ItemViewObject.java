package food.user.demand.FCPojo.FCHomeFragmentObject;

import androidx.palette.graphics.Palette;

public class ItemViewObject {

    private String d_images ;
    private String d_info ;
    private String id ;
    private String partner_id ;
    private String category_name ;
    private String image ;
    private String status ;
    public Palette posterPalette;

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

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Palette getPosterPalette() {
        return posterPalette;
    }
    public void setPosterPalette(Palette posterPalette) {
        this.posterPalette = posterPalette;
    }
}
