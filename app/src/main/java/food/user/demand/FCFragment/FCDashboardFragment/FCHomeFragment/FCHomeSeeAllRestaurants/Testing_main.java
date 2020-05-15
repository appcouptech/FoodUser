package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.R;

public class Testing_main extends RecyclerView.ViewHolder{
    public LoaderTextView txt_restaurantname,txt_cuisines;
    public Testing_main(View itemView) {
        super(itemView);
        txt_restaurantname =  itemView.findViewById(R.id.lt_restaurantname);
        txt_cuisines =  itemView.findViewById(R.id.lt_cuisine);
    }
}
