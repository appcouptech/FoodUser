package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FCSearchFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCViews.Utils;
import food.user.demand.Testing_New.Call;
import food.user.demand.Testing_New.Description;
import food.user.demand.Testing_New.Header;
import food.user.demand.Testing_New.Recommended;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import food.user.demand.FCActivity.FCCartActivity.FC_CartActivity;
import food.user.demand.FCUtils.BottomDailog.BottomDialogFragmentAddonProducts;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.R;

public class FC_SearchHotelDetailsActivity extends AppCompatActivity implements View.OnClickListener, BottomDialogFragmentAddonProducts.AddonProducts {
    Snackbar bar;
    int addcheck = 0;
    private LoaderTextView lt_totalPrice;
    private LinearLayout ll_dishDetails,ll_header;
    private LinearLayout ll_viewCart;
    private AC_Edittext edt_dishName;
    private RecyclerView rv_itemDetails;
    private List<Object> menulist;
    BottomSheetDialog paymentdialog;
    private AC_Textview txt_totalQty,txt_emptyview;
    private AC_Textview txt_ok;
    private AC_Textview txt_cancel;
    private MultiViewTypeAdapter multiViewTypeAdapter;
    private Context context;
    InputMethodManager inputMgr;
    View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_SearchHotelDetailsActivity.this,getResources().getConfiguration());
        setContentView(R.layout.fragment_fc__search_hotel_details);
        context=FC_SearchHotelDetailsActivity.this;
        FindViewbyId();
        ll_dishDetails.setBackgroundColor(Objects.requireNonNull(context).getResources().getColor(R.color.white));
        Intent intent = getIntent();
        Bundle object = intent.getExtras();
        if (object != null) {
            FC_Common.restaurant_name = object.getString("restaurant_name");
            FC_Common.delivery_estimation = object.getString("delivery_estimation");
        }
        init();
        edt_dishName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                FC_Common.searchlist = edt_dishName.getText().toString().toLowerCase( Locale.getDefault());
                AllRestaurantList(FC_Common.searchlist);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void FindViewbyId() {

        try {
            inputMgr = (InputMethodManager) Objects.requireNonNull(context).getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }
        parentLayout = findViewById(android.R.id.content);
        lt_totalPrice = findViewById(R.id.lt_totalPrice);
        txt_emptyview = findViewById(R.id.txt_emptyview);
        AC_Textview txt_restaurantName = findViewById(R.id.txt_restaurantName);
        AC_Textview txt_deliveryEstimation = findViewById(R.id.txt_deliveryEstimation);
        txt_totalQty = findViewById(R.id.txt_totalQty);
        ImageView img_back = findViewById(R.id.img_backBtn);
        edt_dishName = findViewById(R.id.edt_dishName);
        ll_dishDetails = findViewById(R.id.ll_dishDetails);
        ll_header = findViewById(R.id.ll_header);
        rv_itemDetails = findViewById(R.id.rv_itemDetails);
        txt_restaurantName.setText("Search In - "+ FC_Common.restaurant_name);
        txt_deliveryEstimation.setText(FC_Common.delivery_estimation);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rv_itemDetails.setLayoutManager(linearLayoutManager);
        // Limiting the size
        rv_itemDetails.setHasFixedSize(true);

        ll_viewCart = findViewById(R.id.ll_viewCart);
        LinearLayout ll_viewCartLayoutBtn = findViewById(R.id.ll_viewCartLayoutBtn);

        img_back.setOnClickListener(this);
        ll_viewCartLayoutBtn.setOnClickListener(this);


        ll_dishDetails.setOnTouchListener((v, event) -> {
            inputMgr.hideSoftInputFromWindow(ll_dishDetails.getWindowToken(), 0);
            return false;
        });
        ll_dishDetails.setOnTouchListener((v, event) -> {
            inputMgr.hideSoftInputFromWindow(rv_itemDetails.getWindowToken(), 0);
            return false;
        });
        ll_header.setOnTouchListener((v, event) -> {
            inputMgr.hideSoftInputFromWindow(ll_header.getWindowToken(), 0);
            return false;
        });
    }

    @Override
    public void onResume() {
        Log.d("fghdfhdfgdf","fdhgdfgdfgfd");
        AllRestaurantList(FC_Common.searchlist);
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_backBtn:

                onBackPressed();

                break;

            case R.id.ll_viewCartLayoutBtn:

                Intent cartIntent = new Intent(context, FC_CartActivity.class);
                startActivity(cartIntent);

                break;


        }
    }

    private void init(){

        Log.d("sdfgsdvxzvxzcv","dfgdfgfd"+FC_Common.restaurantid);
        menulist =new ArrayList<>();
        // Initiating Adapter
        multiViewTypeAdapter =new MultiViewTypeAdapter(context);
        rv_itemDetails.setAdapter(multiViewTypeAdapter);
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

        AllRestaurantList(FC_Common.searchlist);


    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void AllRestaurantList(String term) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTLIST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Log.d("fghfghfgh","fghfg"+jsonObject);
                        FC_Common.success = jsonObject.getString("success");

                        if(FC_Common.success.equalsIgnoreCase("1"))
                        {

                            txt_emptyview.setVisibility(View.GONE);
                            rv_itemDetails.setVisibility(View.VISIBLE);
                            FC_Common.total_quantity = jsonObject.getString("total_quantity");
                            FC_Common.total_price = jsonObject.getString("total_price");
                            FC_Common.minimum_order = jsonObject.getInt("minimum_order");
                            FC_Common.maximum_order = jsonObject.getInt("maximum_order");
                            txt_totalQty.setText(FC_Common.total_quantity + " Items");
                            lt_totalPrice.setText(FC_Common.total_price);
                            JSONArray jsonArray = jsonObject.getJSONArray("category");
                            //JSONArray dataArray = jsonObject.getJSONArray("recommended");
                            Log.d("dfhdfhgdfhg","sdgsdfgDfhgfdgdf"+jsonArray);
                            menulist.clear();

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
                                }
                            }
                        }
                        else {
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_itemDetails.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        txt_emptyview.setVisibility(View.VISIBLE);
                        rv_itemDetails.setVisibility(View.GONE);
                        Log.d("asfasfasfsaf","34543cvbvcb"+e);
                        //snackBar("Please Try Again");

                    }

                }, error -> {
            //displaying the error in toast if occurrs
            String error_value = String.valueOf(error);
            txt_emptyview.setVisibility(View.VISIBLE);
            rv_itemDetails.setVisibility(View.GONE);
            //snackBar("Please Try Again");

            Log.d("asfasfasfsaf","fgr546t4fgbddfgfdg"+error_value);


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", FC_Common.restaurantid);
                params.put("term", term);
                params.put("vn_type", "2");
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:12" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        // request queue
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CALL=1,TYPE_DESCRIPTION=2,TYPE_Header=3,TYPE_RECOMMENDED=4;
        private List<Object> menufeed;
        // Context is not used here but may be required to
        // perform complex operations or call methods from outside
        private Context context;

        // Constructor
        MultiViewTypeAdapter(Context context){
            this.context=context;

        }

        void menuFeed(List<Object> menufeed){
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
                    ((MultiViewTypeAdapter.LoaderView)holder).showCallDetails();
                    Utils.log(context,"sdgdsfgdgfd"+"xgsdgvfdgfgd");
                    break;
                case TYPE_DESCRIPTION:
                    Description Description=(Description)menufeed.get(position);
                    ((MultiViewTypeAdapter.DescriptionView)holder).showDescription(Description);
                    break;
                case TYPE_Header:
                    Header header=(Header)menufeed.get(position);
                    ((MultiViewTypeAdapter.HeaderView)holder).showHeader(header);
                    Utils.log(context,"sdgdsfgdgfd"+"cvbfdxgsdgvfdgfgd");
                    rv_itemDetails.post(() -> {
                        float y = rv_itemDetails.getY();
                        Log.d("ScrollTo ","ScrollTo :" + y ) ;
                        Utils.log(context,"sdgdsfgdgfd"+"5435xgsdgvfdgfgd");
                        // nsv_hotelsDetails.smoothScrollTo(0, (int) y);
                    });
                    break;
            }
        }

        @Override
        public int getItemCount(){return menufeed.size();}

        // Invoked by layout manager to create new views
        @org.jetbrains.annotations.NotNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull @NotNull ViewGroup parent, int viewType) {
            // Attach layout for single cell
            int layout ;
            RecyclerView.ViewHolder viewHolder;
            // Identify viewType returned by getItemViewType(...)
            // and return ViewHolder Accordingly
            switch (viewType){
                case TYPE_CALL:
                    layout= R.layout.layout_recommended_items;
                    View callsView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new MultiViewTypeAdapter.LoaderView(callsView);
                    break;
                case TYPE_DESCRIPTION:
                    layout=R.layout.layout_description_items;
                    View smsView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new MultiViewTypeAdapter.DescriptionView(smsView);
                    break;
                case TYPE_Header:
                    layout=R.layout.layout_sub_title_items;
                    View header = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder=new MultiViewTypeAdapter.HeaderView(header);
                    break;
                default:
                    viewHolder=null;
                    break;
            }
            assert viewHolder != null;
            return viewHolder;
        }

        // First ViewHolder of object type Call
        // Reference to the views for each call items to display desired information
        class LoaderView extends RecyclerView.ViewHolder {
            LinearLayout ll_loader;
            LoaderTextView lt_loaderView;
            ProgressBar pb_cart ;
            LoaderView(View itemView) {
                super(itemView);
                // Initiate view
                lt_loaderView=itemView.findViewById(R.id.lt_loaderView);
                ll_loader=itemView.findViewById(R.id.ll_loader);
                pb_cart=itemView.findViewById(R.id.pb_cart);
            }

            void showCallDetails(){
                // Attach values for each item
                lt_loaderView.setVisibility(View.VISIBLE);
                ll_loader.setVisibility(View.VISIBLE);

            }

        }

        // Second ViewHolder of object type Description
        // Reference to the views for each call items to display desired information
        class DescriptionView extends RecyclerView.ViewHolder {
            LinearLayout ll_nextAvailable,ll_content ,ll_addBtn,ll_loader,ll_addAndMinus;
            ImageView img_VegNonVegType ,img_plus,img_minus;
            LoaderTextView lt_loaderView,lt_nextAvailable;
            AC_Textview txt_restaurantName,txt_currency,txt_price,txt_preparation,txt_quantity;
            ProgressBar pb_cart;

            DescriptionView(View itemView) {
                super(itemView);
                // Initiate view
                img_VegNonVegType =  itemView.findViewById(R.id.img_VegNonVegType);
                img_plus =  itemView.findViewById(R.id.img_plus);
                img_minus =  itemView.findViewById(R.id.img_minus);
                txt_restaurantName =  itemView.findViewById(R.id.txt_restaurantName);
                txt_currency =  itemView.findViewById(R.id.txt_currency);
                txt_price =  itemView.findViewById(R.id.txt_price);
                txt_preparation =  itemView.findViewById(R.id.txt_preparation);
                ll_content =  itemView.findViewById(R.id.ll_content);
                lt_loaderView =  itemView.findViewById(R.id.lt_loaderView);
                ll_loader =  itemView.findViewById(R.id.ll_loader);
                pb_cart =  itemView.findViewById(R.id.pb_cart);
                ll_addBtn = itemView.findViewById(R.id.ll_addBtn);
                ll_addAndMinus = itemView.findViewById(R.id.ll_addAndMinus);
                ll_nextAvailable = itemView.findViewById(R.id.ll_nextAvailable);
                lt_nextAvailable = itemView.findViewById(R.id.lt_nextAvailable);
                txt_quantity = itemView.findViewById(R.id.txt_quantity);


            }
            void showDescription(Description Description){
                // Attach values for each item
                txt_restaurantName.setText(Description.getProduct_name());
                // txt_currency.setText(Description.getPrice());
                txt_price.setText(Description.getPrice());
                txt_preparation.setText(Description.getProduct_description());
                txt_quantity.setText(Description.getquantity());
                FC_Common.availabilty_Description=(Description.getAvailability());
                ll_content.setVisibility(View.VISIBLE);
                lt_loaderView.setVisibility(View.GONE);
                ll_loader.setVisibility(View.GONE);

                if (FC_Common.total_quantity.equalsIgnoreCase("0"))
                {
                    ll_viewCart.setVisibility(View.GONE);
                }
                else {
                    ll_viewCart.setVisibility(View.VISIBLE);
                    Animation animSlideUp1 = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp1);

                }
                //ll_viewCart.setVisibility(View.VISIBLE);
                pb_cart.setVisibility(View.GONE);

                if (FC_Common.availabilty_Description.equalsIgnoreCase("0"))
                {
                    ll_nextAvailable.setVisibility(View.VISIBLE);
                    ll_addAndMinus.setVisibility(View.GONE);
                    ll_addBtn.setVisibility(View.GONE);
                    //lt_nextAvailable.setText(R.string.nextavailable+""+recommened.getNext_avail_time1());
                    lt_nextAvailable.setText(Description.getnext_available());
                }
                else {
                    ll_nextAvailable.setVisibility(View.GONE);
                    ll_addBtn.setVisibility(View.VISIBLE);
                    if (Description.getquantity().equalsIgnoreCase("0"))
                    {
                        ll_addBtn.setVisibility(View.VISIBLE);
                        ll_addAndMinus.setVisibility(View.GONE);
                    }
                    else {
                        ll_addBtn.setVisibility(View.GONE);
                        ll_addAndMinus.setVisibility(View.VISIBLE);
                    }
                }
                if (Description.getDish_id().equalsIgnoreCase("1")) {
                    img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                }
                else {
                    img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }
                ll_addBtn.setOnClickListener(view -> {
                    if (FC_Common.restaurant_status.equalsIgnoreCase("OPEN")) {
                        FC_Common.hotelpricing=Description.getPrice_status();
                        FC_Common.addonpricing=Description.getAddon_status();
                        FC_Common.productID=Description.getid();
                        FC_Common.quantity=Description.getquantity();
                        FC_Common.price = Integer.parseInt(Description.getPrice());
                        FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                        Log.d("dfhdfghdfgd","dfgdfgdfg"+FC_Common.productID);
                        Log.d("dfhdfghdfgd","dfgdfgdfg"+FC_Common.productID);
                        if (FC_Common.hotelpricing.equalsIgnoreCase("1")||
                                FC_Common.addonpricing.equalsIgnoreCase("1")){
                            Utils.log(context,"sdfsdfsdfs"+" hotelpricing : "+FC_Common.hotelpricing);
                            Utils.log(context,"sdfsdfsdfs"+" addonpricing : "+FC_Common.addonpricing);

                            Bundle bundle = new Bundle();
                            bundle.putString("hotelpricing",FC_Common.hotelpricing);
                            bundle.putString("addonpricing", FC_Common.addonpricing);
                            bundle.putString("hotelid", FC_Common.restaurantid);
                            bundle.putString("productID", FC_Common.productID);
                            bundle.putString("quantity", FC_Common.quantity);

                            BottomDialogFragmentAddonProducts addPhotoBottomDialogFragment = BottomDialogFragmentAddonProducts.newInstance();
                            addPhotoBottomDialogFragment.setArguments(bundle);
                            addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");

                        }
                        else
                        {
                            pb_cart.setVisibility(View.VISIBLE);
                            //FC_Common.quantity="1";
                            int count = Integer.parseInt(Description.getquantity());

                            count = count + 1;
                            FC_Common.hotelpricing=Description.getPrice_status();
                            FC_Common.addonpricing=Description.getAddon_status();
                            FC_Common.productID=Description.getid();
                            FC_Common.quantity=String.valueOf(count);
                            /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                            Log.d("fhdfgdfg","max");
                            if(FC_Common.maximum_order>FC_Common.priceTotal)
                            {
                                Log.d("fhdfgdfg","fdgdgdmin");
                                snackBar("item to be adding please wait");
                                UpdateMenu();
                            }
                            else {
                                snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                            }
                            // }
                            Utils.log(context,"sdfsdfsdfs"+" hotelpricing : "+"fail");
                            Utils.log(context,"sdfsdfsdfs"+" addonpricing : "+"fail");
                        }




                    }
                    else {
                        ll_addBtn.setVisibility(View.GONE);
                        snackBar(FC_Common.restaurant_name+" Currently UnAvailable");
                    }

                });

                img_plus.setOnClickListener(view -> {

                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(Description.getquantity());

                    count = count + 1;
                    FC_Common.hotelpricing=Description.getPrice_status();
                    FC_Common.addonpricing=Description.getAddon_status();
                    FC_Common.productID=Description.getid();
                    FC_Common.quantity=String.valueOf(count);
                    FC_Common.price = Integer.parseInt(Description.getPrice());
                    FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                    Utils.log(context,"sdfsdfsdfsdfs"+"count : "+count);
                    Utils.log(context,"sdfsdfsdfsdfs"+"quantity : "+FC_Common.quantity);
                   /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                    Log.d("fhdfgdfg","max");
                    if(FC_Common.maximum_order>FC_Common.priceTotal)
                    {
                        Log.d("fhdfgdfg","fdgdgdmin");
                        UpdateMenu();
                    }
                    else {
                        snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                    }
                    // }

                });
                img_minus.setOnClickListener(v -> {
                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(Description.getquantity());
                    count = count - 1;
                    FC_Common.hotelpricing=Description.getPrice_status();
                    FC_Common.addonpricing=Description.getAddon_status();
                    FC_Common.productID=Description.getid();
                    FC_Common.quantity=String.valueOf(count);
                    UpdateMenu();
                    Utils.log(context,"sdfsdfsdfg"+"count : "+count);
                });
            }
        }

        class HeaderView extends RecyclerView.ViewHolder{

            AC_Textview txt_header;
            HeaderView(View itemView) {
                super(itemView);
                txt_header =  itemView.findViewById(R.id.txt_header);

            }
            void showHeader(Header header){
                // Attach values for each item
                //String categoryname   =header.getCategoryname();
                txt_header.setText(header.getCategoryname());

            }
        }
    }
    private void UpdateMenu() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTADDONUPDATECART,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {

                            AllRestaurantList(FC_Common.searchlist);
                        }
                        else
                        {
                            snackBar(FC_Common.message);
                            @SuppressLint("InflateParams")
                            View view1 = getLayoutInflater().inflate(R.layout.bottom_payment_gateway, null);
                            FindViewByIdBottomDialog(view1);
                            paymentdialog = new BottomSheetDialog(context);
                            paymentdialog.setContentView(view1);
                            paymentdialog.show();
                            txt_cancel.setOnClickListener(v -> paymentdialog.dismiss());
                            txt_ok.setOnClickListener(v -> ClearCart());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(context, FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", FC_Common.productID);
                params.put("partner_id", FC_Common.restaurantid);
                params.put("quantity", FC_Common.quantity);

                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
    @Override
    public void addProducts(int products) {
        addcheck = products;
        if (addcheck == 1) {
            //init();
            AllRestaurantList(FC_Common.searchlist);
        }
        else if (addcheck == 2){
            @SuppressLint("InflateParams")
            View view1 = getLayoutInflater().inflate(R.layout.bottom_payment_gateway, null);
            FindViewByIdBottomDialog(view1);
            paymentdialog = new BottomSheetDialog(context);
            paymentdialog.setContentView(view1);
            paymentdialog.show();
            txt_cancel.setOnClickListener(v -> paymentdialog.dismiss());
            txt_ok.setOnClickListener(v -> ClearCart());
        }
        Log.d("zxvbzxvzxcxzc", "dfgdfg" );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void FindViewByIdBottomDialog(View view) {

        txt_ok = view.findViewById(R.id.txt_ok);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        AC_Textview txt_header = view.findViewById(R.id.txt_header);
        LinearLayout ll_payment = view.findViewById(R.id.ll_payment);
        LinearLayout ll_clearcart = view.findViewById(R.id.ll_clearcart);
        txt_header.setText(R.string.f_clearcart);
        ll_payment.setVisibility(View.GONE);
        ll_clearcart.setVisibility(View.VISIBLE);

    }
    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(parentLayout, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", v -> bar.dismiss());
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

    private void ClearCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_PRODUCTCARTCLEAR,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            paymentdialog.dismiss();
                            AllRestaurantList(FC_Common.searchlist);
                        }
                        else {
                            paymentdialog.dismiss();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
