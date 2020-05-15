package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.VerticalLabelView;
import food.user.demand.FCViews.VerticalTextView;
import food.user.demand.R;

class ItemViewHolder extends RecyclerView.ViewHolder{
    AC_Textview  txt_offer,txt_cards;
    VerticalLabelView vv_offer;
    ItemViewHolder(View itemView) {
        super(itemView);
        txt_offer = itemView.findViewById(R.id.txt_offer);
        txt_cards = itemView.findViewById(R.id.txt_cards);
        vv_offer = itemView.findViewById(R.id.vv_offer);
    }
}