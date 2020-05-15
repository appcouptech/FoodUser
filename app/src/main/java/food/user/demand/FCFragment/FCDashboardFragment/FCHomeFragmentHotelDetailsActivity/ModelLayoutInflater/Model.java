package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.ModelLayoutInflater;

public class Model{

    public static final int MAIN_TITLE = 0;
    public static final int SUB_TITLE = 1;
    public static final int DESCRIPTION = 2;

    public int type;
    public String data;

    public Model(int type, String data) {

        this.type = type ;
        this.data = data ;


    }
}
