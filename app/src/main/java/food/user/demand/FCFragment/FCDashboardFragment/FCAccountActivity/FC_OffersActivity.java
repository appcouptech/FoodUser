package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCOfferActivity.FC_PaymentOfferFragment;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCOfferActivity.FC_RestaurantOfferFragment;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_OffersActivity extends AppCompatActivity {

    private ImageView img_backBtn;
    AppBarLayout mAppBarLayout;
    TabLayout tab_offer;
    ViewPager vp_offer;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_OffersActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_fc__offers);

        FindViewById();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FC_Common.paymentID = (String) bundle.get("id");
        }
        if (FC_Common.paymentID != null && FC_Common.paymentID.contains("2")) {
            adapter.moveToPaymentFragment();
        }

        img_backBtn.setOnClickListener(view -> onBackPressed());

    }

    private void FindViewById() {

        img_backBtn = findViewById(R.id.img_backBtn);
        tab_offer = findViewById(R.id.tab_offer);
        vp_offer = findViewById(R.id.vp_offer);
        setupViewPager(vp_offer);
    }

    private void setupViewPager(ViewPager vp_offer) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vp_offer.setAdapter(adapter);
        tab_offer.setupWithViewPager(vp_offer);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FC_Common.paymentID = "0";
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        Fragment fragment = null;
        ViewPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                fragment = new FC_RestaurantOfferFragment();
            } else if (position == 1) {
                fragment = new FC_PaymentOfferFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "RestaurantOffers";
            } else if (position == 1) {
                title = "PaymentOffers/Coupons";
            }
            return title;
        }

        void moveToPaymentFragment() {
            vp_offer.setCurrentItem(vp_offer.getCurrentItem() + 1);
            notifyDataSetChanged();
        }
    }

}
