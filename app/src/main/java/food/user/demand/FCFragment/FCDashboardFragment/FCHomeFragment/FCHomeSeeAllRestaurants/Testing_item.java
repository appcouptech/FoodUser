package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.VerticalLabelView;
import food.user.demand.R;

public class Testing_item extends RecyclerView.ViewHolder{
   public AC_Textview txt_header;

    public Testing_item(View itemView) {
        super(itemView);
        txt_header = itemView.findViewById(R.id.txt_header);

    }
}
