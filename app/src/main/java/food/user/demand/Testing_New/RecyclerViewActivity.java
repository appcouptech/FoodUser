package food.user.demand.Testing_New;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCActivity.FCCartActivity.FC_CartActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FCSearchFragment.FC_SearchHotelDetailsFragment;
import food.user.demand.FCPojo.FCHomeFragmentObject.TestObject;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class RecyclerViewActivity extends AppCompatActivity  implements View.OnClickListener{
    private RecyclerView rv_recommended,rv_menuItem;
    AC_Textview txt_restaurantNameHeader,txt_restaurantTime;
    private LinearLayoutManager linearLayoutManager;
    private List<Object> menulist;
    private ImageView img_backBtn, img_search;
    private MenuItemAdapter menuItemAdapter;
    Handler handler;
    LinearLayout ll_viewCartLayoutBtn,ll_menuMainLayout,ll_viewCart,ll_contentMainLayout,ll_menu,ll_menuList,ll_toolbarTextLayout,ll_hotelDetails,ll_hotelTiming,ll_restaurantInfo,ll_vegNonVegType;
    String check="false";
    LoaderTextView lt_hotelDetails,lt_hotelTiming,lt_restaurantInfo;
    private NestedScrollView nsv_hotelsDetails ;
    private MultiViewTypeAdapter multiViewTypeAdapter;
    public static FragmentManager fragmentManagerHotelDetailsActiviy;
    AC_Textview txt_costLimit,txt_personLimit,txt_day,txt_timing,txt_phone,txt_restaurantName,txt_cuisines,txt_rating,txt_currency,txt_deliveryEstimation,txt_addAddress;
    Context context;
    private Rect scrollBounds = new Rect();
    private View view_scroll ;
    private FrameLayout fl_menuLayout;
    private int MenuTypecounter = 0;
    View parentLayout;
    Snackbar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc__hotel_details);
        context=RecyclerViewActivity.this;
        fragmentManagerHotelDetailsActiviy = getSupportFragmentManager();
        FindViewById();
        rv_recommended=findViewById(R.id.rv_recommended);
        // Set Layout Manager
        linearLayoutManager=new LinearLayoutManager(this);
        rv_recommended.setLayoutManager(linearLayoutManager);
        // Limiting the size
        rv_recommended.setHasFixedSize(true);

        // Initialize list items
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               // multiViewTypeAdapter.visibleContentLayout();

                Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_menu);
                ll_menu.startAnimation(animSlideUp1);
                ll_menu.setVisibility(View.VISIBLE);

            }
        }, 3000);

        nsv_hotelsDetails.getHitRect(scrollBounds);

        nsv_hotelsDetails.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (view_scroll != null) {

                if (view_scroll.getLocalVisibleRect(scrollBounds)) {
                    ll_toolbarTextLayout.setVisibility(View.GONE);
                } else {
                    ll_toolbarTextLayout.setVisibility(View.VISIBLE);
                    txt_restaurantNameHeader.setText(FC_Common.restaurant_name);
                    txt_restaurantTime.setText(FC_Common.delivery_estimation);
                }
            }

        });
    }
    private void init(){
        menulist =new ArrayList<>();
        // Initiating Adapter
        multiViewTypeAdapter =new MultiViewTypeAdapter(RecyclerViewActivity.this);
        rv_recommended.setAdapter(multiViewTypeAdapter);
        // Adding some demo data(Call &amp; Description Objects).
        // You can get them from your data server
        menulist.add(new Call("John","9:30 AM"));
        menulist.add(new Call("Rob","9:40 AM"));
        menulist.add(new Call("Mia","10:40 AM"));
        menulist.add(new Call("Scott","10:45 AM"));
        // Set items to adapter
        multiViewTypeAdapter.menuFeed(menulist);
        multiViewTypeAdapter.notifyDataSetChanged();
//        check="false";
//        multiViewTypeAdapter.visibleContentLayout(check);

        AllRestaurantList();

        handler = new Handler();
        int delay = 5000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (MenuTypecounter > 5) {
                    MenuTypecounter = 0;
                    //AllRestaurantList();
                    Log.d("dgsdgsdgsd","sdfgsdfsdfsd");
                }
                else {
                    //snackBar("Please Try Again");
                }
                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }
    private void FindViewById() {

        ll_viewCart = findViewById(R.id.ll_viewCart);
        ll_contentMainLayout = findViewById(R.id.ll_contentMainLayout);
        ll_toolbarTextLayout = findViewById(R.id.ll_toolbarTextLayout);
        rv_menuItem = findViewById(R.id.rv_menuItem);
        lt_restaurantInfo = findViewById(R.id.lt_restaurantInfo);
        ll_restaurantInfo = findViewById(R.id.ll_restaurantInfo);
        lt_hotelDetails = findViewById(R.id.lt_hotelDetails);
        ll_vegNonVegType = findViewById(R.id.ll_vegNonVegType);
        ll_hotelDetails = findViewById(R.id.ll_hotelDetails);
        lt_hotelTiming = findViewById(R.id.lt_hotelTiming);
        ll_hotelTiming = findViewById(R.id.ll_hotelTiming);
        parentLayout = findViewById(android.R.id.content);
        lt_hotelDetails.setVisibility(View.VISIBLE);
        ll_hotelDetails.setVisibility(View.GONE);
        lt_hotelTiming.setVisibility(View.VISIBLE);
        ll_hotelTiming.setVisibility(View.GONE);
        lt_restaurantInfo.setVisibility(View.VISIBLE);
        ll_restaurantInfo.setVisibility(View.GONE);

        txt_restaurantNameHeader = findViewById(R.id.txt_restaurantNameHeader);
        txt_restaurantTime = findViewById(R.id.txt_restaurantTime);
        ll_menuList = findViewById(R.id.ll_menuList);
        rv_recommended = findViewById(R.id.rv_recommended);
        ll_menu = findViewById(R.id.ll_menu);
        txt_restaurantName = findViewById(R.id.txt_restaurantName);
        txt_phone = findViewById(R.id.txt_phone);
        fl_menuLayout = findViewById(R.id.fl_menuLayout);
        txt_timing = findViewById(R.id.txt_timing);
        txt_day = findViewById(R.id.txt_day);
        txt_personLimit = findViewById(R.id.txt_personLimit);
        txt_costLimit = findViewById(R.id.txt_costLimit);
        txt_cuisines = findViewById(R.id.txt_cuisines);
        txt_rating = findViewById(R.id.txt_rating);
        txt_currency = findViewById(R.id.txt_currency);
        txt_deliveryEstimation = findViewById(R.id.txt_deliveryEstimation);
        txt_addAddress = findViewById(R.id.txt_addAddress);
        nsv_hotelsDetails = findViewById(R.id.nsv_hotelsDetails);
        view_scroll = findViewById(R.id.view_scroll);
        ll_menu.setVisibility(View.GONE);
        ll_menu.setOnClickListener(this);


        ll_menuMainLayout = findViewById(R.id.ll_menuMainLayout);
        ll_viewCartLayoutBtn = findViewById(R.id.ll_viewCartLayoutBtn);
        img_backBtn = findViewById(R.id.img_backBtn);
        img_search = findViewById(R.id.img_search);


        img_backBtn.setOnClickListener(this);
        ll_menu.setOnClickListener(this);
        ll_viewCartLayoutBtn.setOnClickListener(this);
        img_search.setOnClickListener(this);

        /*callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.SUB_TITLE, ""));
        callSMSList.add(new Model(Model.DESCRIPTION, ""));
        callSMSList.add(new Model(Model.SUB_TITLE, ""));
        callSMSList.add(new Model(Model.DESCRIPTION, ""));
        callSMSList.add(new Model(Model.SUB_TITLE, ""));
        callSMSList.add(new Model(Model.DESCRIPTION, ""));*/
    }

    private void AllRestaurantList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTLIST,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            FC_Common.hotelid = jsonObject.getString("id");
                            FC_Common.restaurant_name = jsonObject.getString("restaurant_name");
                            FC_Common.restaurant_cuisine = jsonObject.getString("cuisines");
                            FC_Common.dish_id = jsonObject.getString("dish_id");
                            FC_Common.restaurant_logo = jsonObject.getString("restaurant_logo");
                            FC_Common.restaurant_phone = jsonObject.getString("restaurant_phone");
                            FC_Common.rating = jsonObject.getString("rating");
                            FC_Common.delivery_estimation = jsonObject.getString("delivery_estimation");
                            FC_Common.cost_limit = jsonObject.getString("cost_limit");
                            FC_Common.restaurant_address = jsonObject.getString("restaurant_address");
                            FC_Common.currency = jsonObject.getString("currency");
                            FC_Common.restaurant_status = jsonObject.getString("restaurant_status");
                            FC_Common.person_limit = jsonObject.getString("person_limit");
                            FC_Common.opens = jsonObject.getString("opens");

                            if (FC_Common.dish_id.equalsIgnoreCase("1"))
                            {
                                ll_vegNonVegType.setVisibility(View.GONE);
                            }
                            else {
                                ll_vegNonVegType.setVisibility(View.VISIBLE);
                            }

                            lt_hotelDetails.setVisibility(View.GONE);
                            ll_hotelDetails.setVisibility(View.VISIBLE);
                            lt_hotelTiming.setVisibility(View.GONE);
                            ll_hotelTiming.setVisibility(View.VISIBLE);
                            lt_restaurantInfo.setVisibility(View.GONE);
                            ll_restaurantInfo.setVisibility(View.VISIBLE);
                            txt_restaurantName.setText(FC_Common.restaurant_name);
                            txt_phone.setText(FC_Common.restaurant_phone);
                            txt_cuisines.setText(FC_Common.restaurant_cuisine);
                            txt_rating.setText(FC_Common.rating);
                            txt_deliveryEstimation.setText(FC_Common.delivery_estimation);
                            txt_currency.setText(FC_Common.currency);
                            txt_addAddress.setText(FC_Common.restaurant_address);
                            txt_costLimit.setText(FC_Common.cost_limit);
                            txt_day.setText(FC_Common.restaurant_status);
                            txt_personLimit.setText("For "+FC_Common.person_limit);
                            txt_timing.setText(FC_Common.opens);
                            JSONArray jsonArray = jsonObject.getJSONArray("category");
                            JSONArray dataArray = jsonObject.getJSONArray("recommended");
                            Log.d("dfhdfhgdfhg","sdgsdfgDfhgfdgdf"+jsonArray);
                            menulist.clear();
                            for (int x = 0; x < dataArray.length(); x++) {
                                //JSONObject object = jsonArray.getJSONObject(x);
                                JSONObject product = dataArray.getJSONObject(x);

                                menulist.add(new Recommended(product.getString("id"),
                                        product.getString("product_name"),
                                        product.getString("product_description"),
                                        product.getString("category_id"),
                                        product.getString("cuisine_id"),
                                        product.getString("dish_id"),
                                        product.getString("photo"),
                                        product.getString("price"),
                                        product.getString("orignal_price"),
                                        product.getString("discount"),
                                        product.getString("discount_type"),
                                        product.getString("availability"),
                                        product.getString("price_status"),
                                        product.getString("addon_status"),
                                        product.getString("next_avail_time1"),
                                        product.getString("next_avail_time2"),
                                        product.getString("next_avail_time3"),
                                        product.getString("next_available"),
                                        product.getString("quantity")));
                                multiViewTypeAdapter.menuFeed(menulist);
                                multiViewTypeAdapter.notifyDataSetChanged();

                                check="true";
                                multiViewTypeAdapter.visibleContentLayout(check);


                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONArray dataArray3 = object.getJSONArray("products");
                                Log.d("xcvbxcbxcb","dataArray3"+dataArray3);
                                String name = object.getString("category_name");
                                Log.d("dfhdfhgdfhg", "object" + name);
                                Log.d("dfhdfhgdfhg","dfgfd"+name.length());
                                // if (name.length() > 1) {
                                Log.d("fdgdgdfg", "xvxcbcdfgdfgfd");
                                menulist.add(new Header(object.getString("category_name")));
                                multiViewTypeAdapter.menuFeed(menulist);
                                multiViewTypeAdapter.notifyDataSetChanged();

//                                }
//                                else {
                                Log.d("fdgdgdfg","dataArray3"+dataArray3);
                                Log.d("fdgdgdfg", "ddfghet43tcvfgdfgfd"+dataArray3.length());
                                for (int j = 0; j < dataArray3.length(); j++) {
                                    JSONObject product2 = dataArray3.getJSONObject(j);
                                    menulist.add(new Description(product2.getString("id"),
                                            product2.getString("product_name"),
                                            product2.getString("product_description"),
                                            product2.getString("category_id"),
                                            product2.getString("cuisine_id"),
                                            product2.getString("dish_id"),
                                            product2.getString("photo"),
                                            product2.getString("price"),
                                            product2.getString("orignal_price"),
                                            product2.getString("discount"),
                                            product2.getString("discount_type"),
                                            product2.getString("availability"),
                                            product2.getString("price_status"),
                                            product2.getString("addon_status"),
//                                            product2.getString("ingredients"),
//                                            product2.getString("preparations"),
                                            product2.getString("next_avail_time1"),
                                            product2.getString("next_avail_time2"),
                                            product2.getString("next_avail_time3"),
                                            product2.getString("next_available"),
                                            product2.getString("quantity")));
                                    multiViewTypeAdapter.menuFeed(menulist);
                                    Log.d("fdgdgdfg", "dg436vcbrtydfgdfgfd");
                                    multiViewTypeAdapter.notifyDataSetChanged();
                                    // }
//                                        check="true";
//                                        multiViewTypeAdapter.visibleContentLayout(check);

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("dfghdfgdfgdfgvd","34543cvbvcb"+e);
                            //snackBar("Please Try Again");
                            final int counter_menutype = MenuTypecounter++;
                            Utils.log(context, "countervalue" + "A:" + counter_menutype);
                            if (counter_menutype < 5) {
                                AllRestaurantList();
                            }
//                            check="false";
//                            multiViewTypeAdapter.visibleContentLayout(check);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                String error_value = String.valueOf(error);
                //snackBar("Please Try Again");
                final int counter_menutype = MenuTypecounter++;
                Utils.log(context, "countervalue" + "A:" + counter_menutype);
                if (counter_menutype < 5) {
                    AllRestaurantList();
                }
                Log.d("dfghdfgdfgdfgvd","fgr546t4fgbddfgfdg"+error_value);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "19");
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Utils.log(context, "token_type:12" + FC_Common.token_type);
                // Utils.log(context, "access_token:" + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type_dup + " " + FC_Common.access_token_dup);
                return params;
            }
        };
        // request queue
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(RecyclerViewActivity.this).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CALL=1,TYPE_DESCRIPTION=2,TYPE_Header=3,TYPE_RECOMMENDED=4;
        private List<Object> menufeed =new ArrayList();
        boolean visible;

        String check_value="false";
        // Context is not used here but may be required to
        // perform complex operations or call methods from outside
        private Context context;

        // Constructor
        public MultiViewTypeAdapter(Context context){
            this.context=context;
        }

        public void menuFeed(List<Object> menufeed){
            this.menufeed = menufeed;
            //notifyDataSetChanged();
        }

        // We need to override this as we need to differentiate
        // which type viewHolder to be attached
        // This is being called from onBindViewHolder() method
        @Override
        public int getItemViewType(int position) {
            if (menufeed.get(position) instanceof Call) {
                return TYPE_CALL;
            } else if (menufeed.get(position) instanceof Description) {
                return TYPE_DESCRIPTION;
            }
            else if (menufeed.get(position) instanceof Header) {
                return TYPE_Header;
            }
            else if (menufeed.get(position) instanceof Recommended) {
                return TYPE_RECOMMENDED;
            }

            return -1;
        }

        // Invoked by layout manager to replace the contents of the views
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            int viewType=holder.getItemViewType();
            switch (viewType){
                case TYPE_CALL:
                    Call call=(Call)menufeed.get(position);
                    ((CallViewHolder)holder).showCallDetails(call);
                    break;
                case TYPE_DESCRIPTION:
                    Description Description=(Description)menufeed.get(position);
                    ((SMSViewHolder)holder).showSmsDetails(Description);
                    break;
                case TYPE_Header:
                    Header header=(Header)menufeed.get(position);
                    ((HeaderView)holder).showHeader(header);
                    break;
                case TYPE_RECOMMENDED:
                    Recommended recommend=(Recommended) menufeed.get(position);
                    ((RecommendedView)holder).showRecommended(recommend);
                    Log.d("fdhsfdhsfhsfd","dfhfdgdf"+visible);
                        ((RecommendedView)holder).showRecommended(recommend);
                    break;


            }
        }

        @Override
        public int getItemCount(){return menufeed.size();}
        /*void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }*/
        void visibleContentLayout(String check) {
            Log.d("dsfsdgdsgf","gsdfsdf"+check);

            if (check.equalsIgnoreCase("false"))
            {
               // ll_content.setVisibility(View.GONE);
                visible = false;
                check_value="false";
                notifyDataSetChanged();
            }
            else
            {
                //ll_content.setVisibility(View.VISIBLE);
                check_value="true";
                visible = true;
                notifyDataSetChanged();
            }

        }

        // Invoked by layout manager to create new views
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Attach layout for single cell
            int layout = 0;
            RecyclerView.ViewHolder viewHolder;
            // Identify viewType returned by getItemViewType(...)
            // and return ViewHolder Accordingly
            switch (viewType){
                case TYPE_CALL:
                    layout= R.layout.layout_recommended_items;
                    View callsView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new CallViewHolder(callsView);
                    break;
                case TYPE_DESCRIPTION:
                    layout=R.layout.layout_description_items;
                    View smsView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new SMSViewHolder(smsView);
                    break;
                case TYPE_Header:
                    layout=R.layout.layout_sub_title_items;
                    View header = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new HeaderView(header);
                    break;
                case TYPE_RECOMMENDED:
                    layout=R.layout.layout_recommended_items;
                    View recommended = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new RecommendedView(recommended);
                    break;
                default:
                    viewHolder=null;
                    break;
            }
            return viewHolder;
        }

        // First ViewHolder of object type Call
        // Reference to the views for each call items to display desired information
        public class CallViewHolder extends RecyclerView.ViewHolder {

            LoaderTextView lt_loaderView;
            ProgressBar  pb_cart ;
            public CallViewHolder(View itemView) {
                super(itemView);
                // Initiate view
                lt_loaderView=itemView.findViewById(R.id.lt_loaderView);
                pb_cart=itemView.findViewById(R.id.pb_cart);

                //callTimeTextView=(TextView)itemView.findViewById(R.id.callTime);
            }

            public void showCallDetails(Call call){
                // Attach values for each item
                lt_loaderView.setVisibility(View.VISIBLE);

            }

        }

        // Second ViewHolder of object type Description
        // Reference to the views for each call items to display desired information
        public class SMSViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_content ,ll_addBtn;
        ImageView img_VegNonVegType ,img_plus;
        LoaderTextView lt_loaderView;
        AC_Textview txt_restaurantName,txt_currency,txt_price,txt_preparation;
        ProgressBar pb_cart;

            public SMSViewHolder(View itemView) {
                super(itemView);
                // Initiate view
                img_VegNonVegType =  itemView.findViewById(R.id.img_VegNonVegType);
                img_plus =  itemView.findViewById(R.id.img_plus);
                txt_restaurantName =  itemView.findViewById(R.id.txt_restaurantName);
                txt_currency =  itemView.findViewById(R.id.txt_currency);
                txt_price =  itemView.findViewById(R.id.txt_price);
                txt_preparation =  itemView.findViewById(R.id.txt_preparation);
                ll_content =  itemView.findViewById(R.id.ll_content);
                lt_loaderView =  itemView.findViewById(R.id.lt_loaderView);
                pb_cart =  itemView.findViewById(R.id.pb_cart);
                ll_addBtn = itemView.findViewById(R.id.ll_addBtn);


            }
            public void showSmsDetails(Description Description){
                // Attach values for each item
                txt_restaurantName.setText(Description.getProduct_name());
               // txt_currency.setText(Description.getPrice());
                txt_price.setText(Description.getPrice());
                txt_preparation.setText(Description.getProduct_description());
                ll_content.setVisibility(View.VISIBLE);
                lt_loaderView.setVisibility(View.GONE);
                if (Description.getDish_id().equalsIgnoreCase("1")) {
                    img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                }
                else {
                    img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }

                ll_addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ll_viewCart.setVisibility(View.VISIBLE);

                        if (ll_viewCart.getVisibility() != View.VISIBLE) {

                            ll_viewCart.setVisibility(View.VISIBLE);
                            pb_cart.setVisibility(View.VISIBLE);

                            Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                            ll_viewCart.startAnimation(animSlideUp1);
                        }
                    }
                });
            }


        }

        public class HeaderView extends RecyclerView.ViewHolder{

            AC_Textview txt_header;
            public HeaderView(View itemView) {
                super(itemView);
                txt_header =  itemView.findViewById(R.id.txt_header);

            }
            public void showHeader(Header header){
                // Attach values for each item
                //String categoryname   =header.getCategoryname();
                txt_header.setText(header.getCategoryname());
            }
        }


        public class RecommendedView extends RecyclerView.ViewHolder{
            LoaderTextView txt_restaurantname,txt_cuisine;
            LinearLayout ll_content,ll_addBtn,ll_dishimage,ll_tick,ll_close,ll_addAndMinus;
            ImageView img_tick,img_close,img_VegNonVegType,img_plus;
            CircleImageView imgcr_dishImage;
            LoaderTextView lt_loaderView;
            LoaderTextView lt_dishPrice;
            ProgressBar pb_cart ;
            public RecommendedView(View itemView) {
                super(itemView);
                img_plus =  itemView.findViewById(R.id.img_plus);
                ll_addAndMinus =  itemView.findViewById(R.id.ll_addAndMinus);
                ll_content =  itemView.findViewById(R.id.ll_content);
                ll_tick =  itemView.findViewById(R.id.ll_tick);
                ll_close =  itemView.findViewById(R.id.ll_close);
                ll_dishimage =  itemView.findViewById(R.id.ll_dishimage);
                ll_addBtn =  itemView.findViewById(R.id.ll_addBtn);
                lt_loaderView =  itemView.findViewById(R.id.lt_loaderView);
                img_tick =  itemView.findViewById(R.id.img_tick);
                img_close =  itemView.findViewById(R.id.img_close);
                img_VegNonVegType =  itemView.findViewById(R.id.img_VegNonVegType);
                imgcr_dishImage =  itemView.findViewById(R.id.imgcr_dishImage);
                txt_restaurantname =  itemView.findViewById(R.id.lt_restaurantname);
                txt_cuisine =  itemView.findViewById(R.id.lt_cuisine);
                lt_dishPrice =  itemView.findViewById(R.id.lt_dishPrice);
                pb_cart=itemView.findViewById(R.id.pb_cart);


            }
            public void showRecommended(Recommended recommened){
                // Attach values for each item
                //String categoryname   =recommened.getCategoryname();
Log.d("check_value","dfgffdgfd"+check_value);
                lt_loaderView.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                if (FC_Common.restaurant_status.equalsIgnoreCase("OPEN"))
                {
                    //ll_addBtn.setEnabled(true);

                }
                else {
                   // ll_addBtn.setEnabled(false);
                    Log.d("sdgsdgfsdg","dsfgsdfsdf");
                    ll_dishimage.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                    ll_close.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                    ll_tick.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
                }
                txt_restaurantname.setText(recommened.getProduct_name());
                txt_cuisine.setText(recommened.getCuisine_id());
                lt_dishPrice.setText(recommened.getPrice());
                //Picasso.get().load(recommened.getPhoto()).into(imgcr_dishImage);
                Picasso.get().load(recommened.getPhoto())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imgcr_dishImage);
                if (recommened.getDish_id().equalsIgnoreCase("1")) {
                    img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                }
                else {
                    img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }
                ll_addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (FC_Common.restaurant_status.equalsIgnoreCase("OPEN"))
                        {
                            ll_addBtn.setVisibility(View.GONE);
                            ll_addAndMinus.setVisibility(View.VISIBLE);

                        }
                        else {
                            ll_addBtn.setVisibility(View.GONE);
                            ll_addAndMinus.setVisibility(View.VISIBLE);
                            snackBar(FC_Common.restaurant_name+" Currently UnAvailable");
                        }

                    }
                });

                img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ll_viewCart.setVisibility(View.VISIBLE);
                        pb_cart.setVisibility(View.VISIBLE);

                        if (ll_viewCart.getVisibility() != View.VISIBLE) {

                            ll_viewCart.setVisibility(View.VISIBLE);
                            pb_cart.setVisibility(View.VISIBLE);

                            Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                            ll_viewCart.startAnimation(animSlideUp1);
                        }
                    }
                });
            }


        }

    }
    private class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
        private final RecyclerViewActivity activity;

        MenuItemAdapter(RecyclerViewActivity activity) {
            this.activity = activity;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_menu_items, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ll_menuList.setVisibility(View.GONE);
                        ll_menu.setVisibility(View.VISIBLE);
                        ll_contentMainLayout.setBackgroundColor(getResources().getColor(R.color.white));
                        fl_menuLayout.setClickable(false);
                    }
                });
            }
        }
    }

    private void menuAdapter() {

        menuItemAdapter = new MenuItemAdapter(RecyclerViewActivity.this);
        LinearLayoutManager menu = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_menuItem.setLayoutManager(menu);
        rv_menuItem.setAdapter(menuItemAdapter);

        ll_contentMainLayout.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
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
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_backBtn:

                onBackPressed();

                break;

            case R.id.ll_menu:

                ll_menuList.setVisibility(View.VISIBLE);
                fl_menuLayout.setClickable(true);

                menuAdapter();

                break;

            case R.id.ll_viewCartLayoutBtn:

                Intent cartIntent = new Intent(RecyclerViewActivity.this, FC_CartActivity.class);
                startActivity(cartIntent);

                break;

            case R.id.img_search:

           /*     BottomDialougFragmentSearch searchDishBottomDialogFragment =
                        BottomDialougFragmentSearch.newInstance();
                searchDishBottomDialogFragment.show(getSupportFragmentManager(),
                        "search_dialog_fragment");*/


                Fragment SearchFragment = new FC_SearchHotelDetailsFragment();
                FragmentTransaction fragmentTransactionSearch = fragmentManagerHotelDetailsActiviy.beginTransaction();
                fragmentTransactionSearch.replace(R.id.fl_searchLayout, SearchFragment);
                fragmentTransactionSearch.addToBackStack(null);
                fragmentTransactionSearch.commit();

                break;
        }
    }

    @Override
    public void onDestroy() {

       // handler.removeCallbacks(handler);
        super.onDestroy();

    }

    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(parentLayout, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bar.dismiss();
                                }
                            });
                    bar.setActionTextColor(Color.RED);
                    TextView tv = bar.getView().findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.CYAN);
                    bar.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();
    }

}

