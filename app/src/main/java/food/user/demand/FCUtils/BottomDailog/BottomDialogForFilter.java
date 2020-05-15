package food.user.demand.FCUtils.BottomDailog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FC_HomeFragment;
import food.user.demand.FCPojo.FCFilter.CuisineObject;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;
import me.bendik.simplerangeview.SimpleRangeView;

public class BottomDialogForFilter extends BottomSheetDialogFragment implements View.OnClickListener {


    public AC_Textview txt_vegOnly, txt_priceRange, txt_rating, txt_cuisines,txt_vegNonveg;
    SwitchCompat sw_vegNonveg;
    public static FragmentManager fragmentManager;
    private int bottomSheetValue;
    private LinearLayout ll_rating, ll_cuisines, ll_vegOnly, ll_priceRange;
    private RadioButton rbtn_popularity, rbtn_ratingHighToLow, rbtn_deliveryTime, rbtn_costLowToHigh, rbtn_costHighToLow;
    private RecyclerView rv_cuisine;
    private SimpleRangeView srv_range;
    private FragmentActivity activity;
    private AC_Edittext edt_min,edt_max;
    private AC_Textview txt_start ,txt_end,txt_apply,txt_dummy,txt_clearAll;
    private ImageView img_close ;
    AC_Edittext edt_search;
    CuisineAdapter cuisineAdapter;
    ArrayList<CuisineObject> cuisineObjects = new ArrayList<>();
    int check = 0;
    private CheckType checkTypes;
    SharedPreferences sharedpreferences;
    public static BottomDialogForFilter newInstance() {
        return new BottomDialogForFilter();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_for_filter, container,
                false);
       // activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FindViewById(view);
        cuisineRecycler();
        Bundle object = getArguments();
        if (getActivity() instanceof CheckType)
            checkTypes = (CheckType) getActivity();
        if (object != null) {
            FC_Common.typeVeg = object.getString("typeVeg");
            FC_Common.filter_price_min_check = object.getString("filter_price_min");
            FC_Common.filter_price_max_check = object.getString("filter_price_max");
            FC_Common.typePopularity = object.getString("typePopularity");
            FC_Common.cuisinecheck = object.getString("cuisinecheck");
        }
        Log.d("dfgfdgsfdg","bottom"+FC_Common.typeVeg);
        Log.d("dfgfdgsfdg","bottom"+FC_Common.filter_price_min_check);
        Log.d("dfgfdgsfdg","bottom"+FC_Common.filter_price_max_check);
        Log.d("dfgfdgsfdg","bottom"+FC_Common.typePopularity);
        Log.d("dfgfdgsfdg","bottom"+FC_Common.cuisinecheck);
        Log.d("dfgfdgsfdg","bottom"+bottomSheetValue);

        if (bottomSheetValue == 1) {
            txt_vegOnly.setBackgroundColor(getResources().getColor(R.color.white));
            ll_vegOnly.setVisibility(View.VISIBLE);
            ll_priceRange.setVisibility(View.GONE);
            ll_rating.setVisibility(View.GONE);
            ll_cuisines.setVisibility(View.GONE);

        } else if (bottomSheetValue == 2) {
            txt_vegOnly.setBackgroundColor(getResources().getColor(R.color.white));
            ll_vegOnly.setVisibility(View.VISIBLE);
            ll_priceRange.setVisibility(View.GONE);
            ll_rating.setVisibility(View.GONE);
            ll_cuisines.setVisibility(View.GONE);
        } else if (bottomSheetValue == 3) {

            FC_Common.minrange=Integer.parseInt(FC_Common.filter_price_min_check);
            FC_Common.maxrange=Integer.parseInt(FC_Common.filter_price_max_check);
            srv_range.setCount(FC_Common.maxrange);
            Log.d("dfgdfsgfdgfd","minrange1:"+FC_Common.minrange);
            Log.d("dfgdfsgfdgfd","maxrange1:"+FC_Common.maxrange);
            /*txt_start.setText(""+FC_Common.minrange);
            txt_end.setText(""+FC_Common.maxrange);*/
            txt_priceRange.setBackgroundColor(getResources().getColor(R.color.white));
            ll_priceRange.setVisibility(View.VISIBLE);
            ll_vegOnly.setVisibility(View.GONE);
            ll_rating.setVisibility(View.GONE);
            ll_cuisines.setVisibility(View.GONE);

        } else if (bottomSheetValue == 4) {
            txt_rating.setBackgroundColor(getResources().getColor(R.color.white));
            ll_vegOnly.setVisibility(View.GONE);
            ll_priceRange.setVisibility(View.GONE);
            ll_rating.setVisibility(View.VISIBLE);
            ll_cuisines.setVisibility(View.GONE);

        } else if (bottomSheetValue == 5) {
            txt_cuisines.setBackgroundColor(getResources().getColor(R.color.white));
            ll_vegOnly.setVisibility(View.GONE);
            ll_priceRange.setVisibility(View.GONE);
            ll_rating.setVisibility(View.GONE);
            ll_cuisines.setVisibility(View.VISIBLE);

           /* if (FC_Common.cuisinecheck.equalsIgnoreCase("0"))
            {
                CuisineAsync();
            }*/
            CuisineAsync();

            cuisineAdapter = new CuisineAdapter(cuisineObjects);
            rv_cuisine.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rv_cuisine.setAdapter(cuisineAdapter);
        }

        rbtn_popularity.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b) {
                rbtn_ratingHighToLow.setChecked(false);
                rbtn_deliveryTime.setChecked(false);
                rbtn_costLowToHigh.setChecked(false);
                rbtn_costHighToLow.setChecked(false);
                FC_Common.typePopularity="1";
            }

        });

        rbtn_ratingHighToLow.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rbtn_popularity.setChecked(false);
                rbtn_deliveryTime.setChecked(false);
                rbtn_costLowToHigh.setChecked(false);
                rbtn_costHighToLow.setChecked(false);
                FC_Common.typePopularity="2";
            }
        });

        rbtn_deliveryTime.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b) {
                rbtn_popularity.setChecked(false);
                rbtn_ratingHighToLow.setChecked(false);
                rbtn_costLowToHigh.setChecked(false);
                rbtn_costHighToLow.setChecked(false);
                FC_Common.typePopularity="3";
            }
        });

        rbtn_costLowToHigh.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b) {
                rbtn_popularity.setChecked(false);
                rbtn_ratingHighToLow.setChecked(false);
                rbtn_deliveryTime.setChecked(false);
                rbtn_costHighToLow.setChecked(false);
                FC_Common.typePopularity="4";
            }

        });

        rbtn_costHighToLow.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rbtn_popularity.setChecked(false);
                rbtn_ratingHighToLow.setChecked(false);
                rbtn_deliveryTime.setChecked(false);
                rbtn_costLowToHigh.setChecked(false);
                FC_Common.typePopularity="5";
            }
        });

        srv_range.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                int count=i*100;
                txt_start.setText("" + count);
                int x = new Integer(txt_start.getText().toString());
                int y = new Integer(100);
                int div = x / y; //Perform Maths operation
                Log.d("dfvsdxvvsdxc","maxrangecheck"+div);
                FC_Common.minrange=Integer.valueOf(div);
                Log.d("dfvsdxvvsdxc","minrange"+FC_Common.minrange);
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                int count=i*100;
                txt_end.setText("" + count);
                int x = new Integer(txt_end.getText().toString());
                int y = new Integer(100);
                int div = x / y; //Perform Maths operation
                FC_Common.maxrange=Integer.valueOf(div);
                Log.d("dfvsdxvvsdxc","maxrange"+FC_Common.maxrange);
            }
        });
    }
    private void FindViewById(View view) {
        txt_dummy = view.findViewById(R.id.txt_dummy);
        txt_clearAll = view.findViewById(R.id.txt_clearAll);
        edt_search = view.findViewById(R.id.edt_search);
        img_close = view.findViewById(R.id.img_close);
        txt_apply = view.findViewById(R.id.txt_apply);
        sw_vegNonveg = view.findViewById(R.id.sw_vegNonveg);
        txt_vegOnly = view.findViewById(R.id.txt_vegOnly);
        txt_vegNonveg = view.findViewById(R.id.txt_vegNonveg);
        txt_priceRange = view.findViewById(R.id.txt_priceRange);
        txt_rating = view.findViewById(R.id.txt_rating);
        txt_cuisines = view.findViewById(R.id.txt_cuisines);
        ll_rating = view.findViewById(R.id.ll_rating);
        rbtn_popularity = view.findViewById(R.id.rbtn_popularity);
        rbtn_ratingHighToLow = view.findViewById(R.id.rbtn_ratingHighToLow);
        rbtn_deliveryTime = view.findViewById(R.id.rbtn_deliveryTime);
        rbtn_costLowToHigh = view.findViewById(R.id.rbtn_costLowToHigh);
        rbtn_costHighToLow = view.findViewById(R.id.rbtn_costHighToLow);

        ll_cuisines = view.findViewById(R.id.ll_cuisines);
        rv_cuisine = view.findViewById(R.id.rv_cuisine);

        ll_vegOnly = view.findViewById(R.id.ll_vegOnly);
        ll_priceRange = view.findViewById(R.id.ll_priceRange);

        srv_range = view.findViewById(R.id.srv_range);
        txt_start = view.findViewById(R.id.txt_start);
        txt_end = view.findViewById(R.id.txt_end);
        edt_max = view.findViewById(R.id.edt_max);
        edt_min = view.findViewById(R.id.edt_min);

        img_close.setOnClickListener(this);
        txt_vegOnly.setOnClickListener(this);
        sw_vegNonveg.setOnCheckedChangeListener(onCheckedChanged());
        txt_priceRange.setOnClickListener(this);
        txt_rating.setOnClickListener(this);
        txt_cuisines.setOnClickListener(this);

        edt_min.setText(FC_Common.filter_price_min_check);
        edt_max.setText(FC_Common.filter_price_max_check);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("typeveg", Context.MODE_PRIVATE);
        String vegCheck = sharedPreferences.getString("typeVeg","");


      /*  edt_min.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence txt, int i, int i1, int i2) {

                Log.d("dfhgfdgfd","fgfdg"+FC_Common.minrange);


                try {
                    String searchvalue = edt_min.getText().toString().toLowerCase(Locale.getDefault());
                    String searchmax = edt_max.getText().toString().toLowerCase(Locale.getDefault());
                    int d1 = Integer.parseInt(searchvalue);
                    int d2 = Integer.parseInt(searchmax);
                    int retval = Integer.compare(d1, d2);

                    if (retval > 0) {
                        // edt_min.setText(FC_Common.minrange);
                        Log.d("dfhdfgfdgfdgfd", "d1 is greater than d2");
                        edt_min.setText("4545");
                    } else if (retval < 0) {
                        Log.d("dfhdfgfdgfdgfd", "d1 is less than d2");
                        //edt_min.setText("4");
                    } else {
                        //edt_min.setText("5");
                        Log.d("dfhdfgfdgfdgfd", "d1 is equal than d2");

                    }
                } catch ( Exception e) {
                    Log.e("InvalidNumber","Can not parse zero string");
                    return; // Or another exception handling.
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        edt_max.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence txt, int i, int i1, int i2) {


                Log.d("dfhgfdgfd","fgfdg"+FC_Common.maxrange);
                String searchvalue = edt_min.getText().toString().toLowerCase(Locale.getDefault());
                String searchmax = edt_max.getText().toString().toLowerCase(Locale.getDefault());

                int d1 = Integer.parseInt(searchvalue);
                int d2 = Integer.parseInt(searchmax);
                int retval = Integer.compare(d1, d2);

                if (retval > 0) {
                    //edt_max.setText(FC_Common.maxrange);
                    Log.d("dfhdfgfdgfdgfd", "d1 is greater than d2");
                } else if (retval < 0) {
                    Log.d("dfhdfgfdgfdgfd", "d1 is less than d2");
                   // edt_max.setText("4");
                } else {
                   // edt_max.setText("5");
                    Log.d("dfhdfgfdgfdgfd", "d1 is equal than d2");

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });*/
        if (FC_Common.typeVeg.equalsIgnoreCase("1")) {
            sw_vegNonveg.setChecked(true);
            txt_vegNonveg.setText(R.string.veg_only);
        }
        else {
            sw_vegNonveg.setChecked(false);
            txt_vegNonveg.setText(R.string.veg_nonveg);
        }
        Log.d("dfhdfgdfg","dfgdfg"+FC_Common.typePopularity);
        if (FC_Common.typePopularity.equalsIgnoreCase("2")) {
            rbtn_popularity.setChecked(false);
            rbtn_ratingHighToLow.setChecked(true);
            rbtn_deliveryTime.setChecked(false);
            rbtn_costLowToHigh.setChecked(false);
            rbtn_costHighToLow.setChecked(false);
            FC_Common.typePopularity="2";
        }
        else if (FC_Common.typePopularity.equalsIgnoreCase("3")) {
            rbtn_popularity.setChecked(false);
            rbtn_ratingHighToLow.setChecked(false);
            rbtn_deliveryTime.setChecked(true);
            rbtn_costLowToHigh.setChecked(false);
            rbtn_costHighToLow.setChecked(false);
            FC_Common.typePopularity="3";
        }
        else if (FC_Common.typePopularity.equalsIgnoreCase("4")) {
            rbtn_popularity.setChecked(false);
            rbtn_ratingHighToLow.setChecked(false);
            rbtn_deliveryTime.setChecked(false);
            rbtn_costLowToHigh.setChecked(true);
            rbtn_costHighToLow.setChecked(false);
            FC_Common.typePopularity="4";
        }
        else  if (FC_Common.typePopularity.equalsIgnoreCase("5")) {
            rbtn_popularity.setChecked(false);
            rbtn_ratingHighToLow.setChecked(false);
            rbtn_deliveryTime.setChecked(false);
            rbtn_costLowToHigh.setChecked(false);
            rbtn_costHighToLow.setChecked(true);
            FC_Common.typePopularity="5";
        }
        else {
            rbtn_popularity.setChecked(true);
            rbtn_ratingHighToLow.setChecked(false);
            rbtn_deliveryTime.setChecked(false);
            rbtn_costLowToHigh.setChecked(false);
            rbtn_costHighToLow.setChecked(false);
            FC_Common.typePopularity="1";
        }
        cuisineObjects = new ArrayList<>();
        CuisineObject cuisineObject = new CuisineObject();
        cuisineObject.setD_images("");
        cuisineObject.setD_images("");
        cuisineObjects.add(cuisineObject);
        txt_clearAll.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("hotelpricing",FC_Common.hotelpricing);
            bundle.putString("typeVeg","2");
            bundle.putString("filter_price_min",FC_Common.filter_price_min);
            bundle.putString("filter_price_max",FC_Common.filter_price_max);
            bundle.putString("typePopularity","1");
            bundle.putString("cuisinecheck","");


            Fragment homeFragment = new FC_HomeFragment();
            FragmentTransaction fragmentTransactionHome = fragmentManager.beginTransaction();
            homeFragment.setArguments(bundle);
            // fragmentTransactionHome.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fragmentTransactionHome.replace(R.id.fl_fcDashboardFragment, homeFragment);
            fragmentTransactionHome.commit();
            dismiss();
        });
        txt_apply.setOnClickListener(v -> {


            FC_Common.minrange=Integer.parseInt(edt_min.getText().toString().trim());
            FC_Common.maxrange=Integer.parseInt(edt_max.getText().toString().trim());
            Log.d("fghfghfgh","min"+FC_Common.filter_price_min);
            Log.d("fghfghfgh","max"+FC_Common.filter_price_max);

            if (FC_Common.minrange>FC_Common.maxrange)
            {
                Log.d("fghfghfgh","check1"+FC_Common.minrange);
                Log.d("fghfghfgh","check2"+FC_Common.maxrange);
                Utils.toast(getContext(),"Value Mismathces");
            }

            else {
                if (FC_Common.minrange>Integer.parseInt(FC_Common.filter_price_max))
                {
                    Log.d("fghfghfgh","Value"+FC_Common.filter_price_max);
                    Utils.toast(getContext(),"Value Mismathces");
                }
                else {
                    Log.d("fghfghfgh", "checkmax" + FC_Common.filter_price_max);
                    txt_dummy.setText("");
                    for (int i = 0; i < cuisineObjects.size(); i++) {

                        if (cuisineObjects.get(i).getSelected()) {
                            //tv.setText(tv.getText() + "" + CustomAdapter.modelArrayList.get(i).getAnimal()+",");
                            String sdmkldn=""+txt_dummy.getText()+"(,"+cuisineObjects.get(i).getId()+")";
                            //String sdmkldn = "" + txt_submitAddondummy.getText() + "" + ingredientObjects.get(i).getId() + "";
                            String number = sdmkldn;
                            String newNumber = number.replace("(,", "(");
                            Log.d("sfsdsdfsdfs", "number" + number);
                            txt_dummy.setText(newNumber);
                            String text = txt_dummy.getText().toString().trim();
                            String finalnumber = text.replace(")(", ",");
                            txt_dummy.setText(finalnumber);
                            String finalnumber1 = finalnumber.replace("(", "");
                            txt_dummy.setText(finalnumber1);
                            String finalnumber2 = finalnumber1.replace(",)", "");
                            txt_dummy.setText(finalnumber2);
                            FC_Common.cuisinecheck = finalnumber2.replace(")", "");
                            txt_dummy.setText(finalnumber2);

                            Log.d("sdgsvdfsdfsdf", "finalnumberdfs" + finalnumber);
                            Log.d("sdgsvdfsdfsdf", "finalnumberzxc" + finalnumber1);
                            Log.d("sdgsvdfsdfsdf", "finalnumberfsd" + finalnumber2);
                            Log.d("sdgsvdfsdfsdf", "finalnumberxcx" + FC_Common.cuisinecheck);
                        }
                    }


                    Bundle bundle = new Bundle();
                    bundle.putString("hotelpricing",FC_Common.hotelpricing);
                    bundle.putString("typeVeg",FC_Common.typeVeg);
                    bundle.putString("filter_price_min",String.valueOf(FC_Common.minrange));
                    bundle.putString("filter_price_max",String.valueOf(FC_Common.maxrange));
                    bundle.putString("typePopularity",FC_Common.typePopularity);
                    bundle.putString("cuisinecheck",FC_Common.cuisinecheck);

                    Log.d("dfgdfsgfdgfd","minrange2:"+FC_Common.minrange);
                    Log.d("dfgdfsgfdgfd","maxrange2:"+FC_Common.maxrange);

                    Fragment homeFragment = new FC_HomeFragment();
                    FragmentTransaction fragmentTransactionHome = fragmentManager.beginTransaction();
                    homeFragment.setArguments(bundle);
                    // fragmentTransactionHome.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                    fragmentTransactionHome.replace(R.id.fl_fcDashboardFragment, homeFragment);
                    fragmentTransactionHome.commit();
                    dismiss();
                }
            }

        });


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_close:
                dismiss();
                break;
            case R.id.txt_vegOnly:

                ll_vegOnly.setVisibility(View.VISIBLE);
                ll_priceRange.setVisibility(View.GONE);
                ll_rating.setVisibility(View.GONE);
                ll_cuisines.setVisibility(View.GONE);
                txt_vegOnly.setBackgroundColor(getResources().getColor(R.color.white));
                txt_priceRange.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_rating.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_cuisines.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));

                break;

            case R.id.txt_priceRange:

              /*  FC_Common.minrange=Integer.parseInt(FC_Common.filter_price_min_check);
                FC_Common.maxrange=Integer.parseInt(FC_Common.filter_price_max_check);
                edt_min.setText(FC_Common.minrange);
                edt_max.setText(FC_Common.maxrange);*/
               // srv_range.setCount(FC_Common.maxrange);

                Log.d("dfgdfsgfdgfd","minrange"+FC_Common.minrange);
                Log.d("dfgdfsgfdgfd","maxrange"+FC_Common.maxrange);
                /*txt_start.setText(""+FC_Common.minrange);
                txt_end.setText(""+FC_Common.maxrange);*/

                ll_vegOnly.setVisibility(View.GONE);
                ll_priceRange.setVisibility(View.VISIBLE);
                ll_rating.setVisibility(View.GONE);
                ll_cuisines.setVisibility(View.GONE);
                txt_vegOnly.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_priceRange.setBackgroundColor(getResources().getColor(R.color.white));
                txt_rating.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_cuisines.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));

                break;

            case R.id.txt_rating:

                ll_vegOnly.setVisibility(View.GONE);
                ll_priceRange.setVisibility(View.GONE);
                ll_rating.setVisibility(View.VISIBLE);
                ll_cuisines.setVisibility(View.GONE);


                txt_vegOnly.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_priceRange.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_rating.setBackgroundColor(getResources().getColor(R.color.white));
                txt_cuisines.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));

                break;

            case R.id.txt_cuisines:

                ll_vegOnly.setVisibility(View.GONE);
                ll_priceRange.setVisibility(View.GONE);
                ll_rating.setVisibility(View.GONE);
                ll_cuisines.setVisibility(View.VISIBLE);


                txt_vegOnly.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_priceRange.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_rating.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                txt_cuisines.setBackgroundColor(getResources().getColor(R.color.white));
               /* if (FC_Common.cuisinecheck.equalsIgnoreCase("0"))
                {
                    CuisineAsync();
                }*/
                CuisineAsync();

//                cuisinesAdapter = new CuisinesAdapter(getActivity());
//                rv_cuisine.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                rv_cuisine.setAdapter(cuisinesAdapter);

                break;


        }
    }

    public void onclick(FragmentActivity activity, int bottomsheetValue) {
        this.activity = activity;
        bottomSheetValue = bottomsheetValue;
    }
    private CompoundButton.OnCheckedChangeListener onCheckedChanged() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.sw_vegNonveg:
                        setButtonState(isChecked);
                        break;
                }
            }
        };
    }
    private void setButtonState(boolean state) {
        if (state) {
            txt_vegNonveg.setText(R.string.veg_only);
            FC_Common.typeVeg="1";
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("typeVeg", FC_Common.typeVeg);
            editor.apply();
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putString("typeVeg1", FC_Common.typeVeg);
//            editor.apply();

        } else {
            txt_vegNonveg.setText(R.string.veg_nonveg);
            FC_Common.typeVeg="2";
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("typeVeg", FC_Common.typeVeg);
            editor.apply();
           // Utils.toast(getContext(),"sfsd"+FC_Common.typeVeg);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void CuisineAsync() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_CUISNELIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("gfdgvbcxbxcvxcvxc", ">>" + response);
                        Log.d("URL_PRODUCTADDONLIST", ">>" + FC_URL.URL_CUISNELIST);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("ghfghfghf", "fhfgdhfd" + obj);

                            try {
                                if (obj.optString("success").equals("1")) {

                                    JSONArray cuisine = obj.getJSONArray("data");

                                    if (!cuisine.equals("0")) {
                                        cuisineObjects.clear();
                                        for (int k = 0; k < cuisine.length(); k++) {

                                            CuisineObject cuisineModel = new CuisineObject();
                                            JSONObject cuisineitem = cuisine.getJSONObject(k);
                                            cuisineModel.setId(cuisineitem.optString("id"));
                                            cuisineModel.setCuisine_name(cuisineitem.optString("cuisine_name"));
                                            cuisineModel.setStatus(cuisineitem.optString("selected"));
                                            cuisineObjects.add(cuisineModel);
                                            if (cuisineObjects != null) {
                                                Log.d("sdgfsdfsdfsd", "cxg45r3sdfsdfsdfsd");
                                                cuisineAdapter.visibleContentLayout();
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("sdgfsdfsdfsd", "" + e);
//                                women = 1;
//                                Utils.log(getActivity(), "women : " + women);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("sdgfsdfsdfsd", "fdhfdh" + e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Log.d("sdgfsdfsdfsd", "hfdhdf" + error);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("id", FC_Common.hotelid);
                params.put("id", FC_Common.cuisinecheck);
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Utils.log(getContext(), "token_type:12" + FC_Common.token_type);
                Utils.log(getContext(), "access_token:" + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void cuisineRecycler() {

        //ingredientObjects = getModel(false);
        cuisineAdapter = new CuisineAdapter(cuisineObjects);
        rv_cuisine.setAdapter(cuisineAdapter);
        rv_cuisine.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.ViewHolder> {
        private final ArrayList<CuisineObject> cuisineObjects;
        boolean visible;
        CuisineAdapter( ArrayList<CuisineObject> cuisineObjects) {
            this.cuisineObjects = cuisineObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_cusines_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.ll_loadercuisine.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                FC_Common.CuisineName = cuisineObjects.get(position).getCuisine_name();
                holder.txt_dishName.setText(FC_Common.CuisineName);
                holder.chk_dishName.setTag( position);
                //holder.chk_dishName.setText("Checkbox "+position);
                if (cuisineObjects.get(position).getStatus().equalsIgnoreCase("1")) {
                    Log.d("dfgdfgdf","dfgdfgfd"+cuisineObjects.get(position).getSelected());
                    Integer pos = (Integer)  holder.chk_dishName.getTag();
                    //Toast.makeText(getContext(), "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();
                    if(cuisineObjects.get(position).getSelected()){
                        cuisineObjects.get(position).setSelected(false);
                        holder.chk_dishName.setChecked(false);
                        Log.d("Fghfdhfdg","1dfgfdgfd");

                    }else {
                        cuisineObjects.get(position).setSelected(true);
                        holder.chk_dishName.setChecked(true);
                        Log.d("Fghfdhfdg","1dadsadadfgfdgfd");
                    }
                }

                holder.chk_dishName.setOnClickListener(v -> {

                    Integer pos = (Integer)  holder.chk_dishName.getTag();
                    //Toast.makeText(getContext(), "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();
                    if(cuisineObjects.get(pos).getSelected()){
                        cuisineObjects.get(pos).setSelected(false);
                        Log.d("Fghfdhfdg","2dfgfdgfd");
                    }else {
                        cuisineObjects.get(pos).setSelected(true);
                        Log.d("Fghfdhfdg","2dadsadadfgfdgfd");
                    }

                });
            }
        }

        @Override
        public int getItemCount() {
            return cuisineObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return cuisineObjects.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout ll_loadercuisine,ll_content;
            CheckBox chk_dishName;
            AC_Textview txt_dishName;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                chk_dishName = itemView.findViewById(R.id.chk_dishName);
                ll_loadercuisine = itemView.findViewById(R.id.ll_loadercuisine);
                ll_content = itemView.findViewById(R.id.ll_content);
            }
        }
    }
    public interface CheckType {
        void checkTypes(int products);
    }

}