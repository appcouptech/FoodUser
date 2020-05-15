package food.user.demand.FCPojo.FCCartActivityObject;

public class IngredientObject {

    private String d_images ;
    private String d_info ;
    private String id ;
    private String ingredient_name ;
    private String status ;
    private boolean isSelected;


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

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
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


}
