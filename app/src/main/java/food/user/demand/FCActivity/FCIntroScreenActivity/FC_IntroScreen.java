package food.user.demand.FCActivity.FCIntroScreenActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import food.user.demand.FCLogin.FC_Login;
import food.user.demand.R;

public class FC_IntroScreen extends AppCompatActivity {

    FC_IntroPreferenceManager fc_introPreferenceManager ;
    ViewPager view_pager;
    LinearLayout ll_bars;
    Button btn_next,btn_skip;
    int[] screens;
    TextView[] bottomBars;
    MyViewPagerAdapter myvpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        FindViewByID();

        myvpAdapter = new MyViewPagerAdapter();
        view_pager.setAdapter(myvpAdapter);

        fc_introPreferenceManager = new FC_IntroPreferenceManager(this);
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener);
        ColoredBars(0);

        btn_next.setOnClickListener(view -> {

            Intent login = new Intent(FC_IntroScreen.this, FC_Login.class);
            startActivity(login);

        });

        btn_skip.setOnClickListener(view -> {

            Intent login = new Intent(FC_IntroScreen.this, FC_Login.class);
            startActivity(login);

        });

    }

    private void FindViewByID() {

        view_pager =  findViewById(R.id.view_pager);
        ll_bars =  findViewById(R.id.ll_bars);
        btn_next = findViewById(R.id.btn_next);
        btn_skip = findViewById(R.id.btn_skip);


        screens = new int[]{
                R.layout.activity_fc__intro_screen_one,
                R.layout.activity_fc__intro_screen_two,
                R.layout.activity_fc__intro_screen_three,
        };
    }

    private void ColoredBars(int thisScreen) {
        int[] colorsInactive = getResources().getIntArray(R.array.dot_on_page_not_active);
        int[] colorsActive = getResources().getIntArray(R.array.dot_on_page_active);
        bottomBars = new TextView[screens.length];

        ll_bars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new TextView(this);
            bottomBars[i].setTextSize(100);
            bottomBars[i].setText(Html.fromHtml("&#175"));
            ll_bars.addView(bottomBars[i]);
            bottomBars[i].setTextColor(colorsInactive[thisScreen]);
        }
        if (bottomBars.length > 0)
            bottomBars[thisScreen].setTextColor(colorsActive[thisScreen]);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            ColoredBars(position);
            if (position == screens.length - 1) {
                btn_next.setVisibility(View.VISIBLE);
                btn_skip.setVisibility(View.VISIBLE);
            }else {
                btn_next.setVisibility(View.GONE);
                btn_skip.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
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
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        Button btn_next,btn_skip;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }
}
