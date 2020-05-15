package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import food.user.demand.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder{
    public ImageView img_cardOffersHeader;
    public HeaderViewHolder(View itemView) {
        super(itemView);
        img_cardOffersHeader = (ImageView) itemView.findViewById(R.id.img_cardOffersHeader);
    }
}
