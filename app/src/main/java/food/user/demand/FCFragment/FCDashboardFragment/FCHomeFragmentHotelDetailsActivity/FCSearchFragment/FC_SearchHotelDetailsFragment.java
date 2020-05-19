package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FCSearchFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
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
import food.user.demand.FCPojo.FCCartActivityObject.AddonObject;
import food.user.demand.FCPojo.FCCartActivityObject.IngredientObject;
import food.user.demand.FCPojo.FCCartActivityObject.PreparationObject;
import food.user.demand.FCPojo.FCCartActivityObject.PricingObject;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCUtils.StatefulRecyclerView.StatefulRecyclerView;
import food.user.demand.FCViews.AC_BoldTextview;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;
import food.user.demand.Testing_New.Call;
import food.user.demand.Testing_New.Description;
import food.user.demand.Testing_New.Header;
import food.user.demand.Testing_New.Recommended;

public class FC_SearchHotelDetailsFragment extends Fragment implements View.OnClickListener {
Snackbar bar;
    int addcheck = 0;
    String check="false";
    private NestedScrollView nsv_cart;
    private String nsv_scrollX;
    private String nsv_scrollY;
    private AddonProducts addonProducts;
    private LinearLayout ll_main;
    private LoaderTextView lt_totalPrice;
    private AC_BoldTextview txt_ingredients,txt_preparation,txt_pricing,txt_addon;
    private StatefulRecyclerView rv_ingredients,rv_preparation,rv_pricing,rv_addon;
    private LinearLayout ll_dishDetails,ll_header;
    private LinearLayout ll_viewCart,ll_clearcart,ll_payment;
    private AC_Edittext edt_dishName;
   // private ItemDetailsAdapter itemDetailsAdapter;
    private RecyclerView rv_itemDetails;
    private List<Object> menulist;
    private ArrayList<PricingObject> pricingObjects = new ArrayList<>();
    private ArrayList<AddonObject> addonObjects = new ArrayList<>();
    private ArrayList<IngredientObject> ingredientObjects = new ArrayList<>();
    private ArrayList<PreparationObject> preparationObjects = new ArrayList<>();
    private PricingAdapter pricingAdapters;
    private AddonAdapter addonAdapters;
    BottomSheetDialog paymentdialog;
    private IngredientAdapter ingredientAdapters;
    private PreparationAdapter preparationAdapters;
    private AC_Textview txt_totalQty,txt_emptyview;
    private AC_Textview txt_submitAddon;
    private AC_Textview txt_submitAddondummy;
    private AC_Textview txt_submitAddondummy2,txt_ok,txt_cancel,txt_header;
    private BottomSheetDialog dialog;
    private int addproducts = 0;
    private MultiViewTypeAdapter multiViewTypeAdapter;
    private Context context;
    InputMethodManager inputMgr;
    public FC_SearchHotelDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fc__search_hotel_details, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();
        FindViewbyId(view);
        if (getActivity() instanceof AddonProducts)
            addonProducts = (AddonProducts) getActivity();
        ll_dishDetails.setBackgroundColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.white));

        Bundle object = getArguments();
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
       /* SearchRecycler();
        SearchList("res");*/

    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void FindViewbyId(View view) {

        try {
            inputMgr = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }

        lt_totalPrice = view.findViewById(R.id.lt_totalPrice);
        txt_emptyview = view.findViewById(R.id.txt_emptyview);
        AC_Textview txt_restaurantName = view.findViewById(R.id.txt_restaurantName);
        AC_Textview txt_deliveryEstimation = view.findViewById(R.id.txt_deliveryEstimation);
        txt_totalQty = view.findViewById(R.id.txt_totalQty);
        ll_main = view.findViewById(R.id.ll_main);
        ImageView img_back = view.findViewById(R.id.img_backBtn);
        edt_dishName = view.findViewById(R.id.edt_dishName);
        ll_dishDetails = view.findViewById(R.id.ll_dishDetails);
        ll_header = view.findViewById(R.id.ll_header);
        rv_itemDetails = view.findViewById(R.id.rv_itemDetails);
        txt_restaurantName.setText("Search In - "+FC_Common.restaurant_name);
        txt_deliveryEstimation.setText(FC_Common.delivery_estimation);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_itemDetails.setLayoutManager(linearLayoutManager);
        // Limiting the size
        rv_itemDetails.setHasFixedSize(true);

        ll_viewCart = view.findViewById(R.id.ll_viewCart);
        LinearLayout ll_viewCartLayoutBtn = view.findViewById(R.id.ll_viewCartLayoutBtn);

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

                Objects.requireNonNull(getActivity()).onBackPressed();

                break;

            case R.id.ll_viewCartLayoutBtn:

                Intent cartIntent = new Intent(getActivity(), FC_CartActivity.class);
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
        boolean visible;
        String check_value="false";
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
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
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


                            @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.layout_addon_products, null);
                            FindViewByIdBottomDialog(view1);
                            dialog = new BottomSheetDialog(context);
                            dialog.setContentView(view1);
                            dialog.show();

                            /*BottomDialogFragmentAddonProducts addPhotoBottomDialogFragment = BottomDialogFragmentAddonProducts.newInstance();
                            addPhotoBottomDialogFragment.setArguments(bundle);
                            addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(), "add_photo_dialog_fragment");*/

                            PricingRecycler();
                            AddonRecycler();
                            IngredientRecycler();
                            PreparationRecycler();
                            AddonList();
                            pricingAdapters = new PricingAdapter(pricingObjects);
                            LinearLayoutManager itemViewLLl = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rv_pricing.setLayoutManager(itemViewLLl);
                            rv_pricing.setAdapter(pricingAdapters);

                            addonAdapters = new AddonAdapter(addonObjects);
                            LinearLayoutManager itemViewLLl1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rv_addon.setLayoutManager(itemViewLLl1);
                            rv_addon.setAdapter(addonAdapters);

                            // ingredientObjects = getModel(false);
                            ingredientAdapters = new IngredientAdapter(ingredientObjects);
                            LinearLayoutManager itemViewLLl2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rv_ingredients.setLayoutManager(itemViewLLl2);
                            rv_ingredients.setAdapter(ingredientAdapters);

                            preparationAdapters = new PreparationAdapter(preparationObjects);
                            LinearLayoutManager itemViewLLl3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rv_preparation.setLayoutManager(itemViewLLl3);
                            rv_preparation.setAdapter(preparationAdapters);

                            if (nsv_cart != null) {
                                nsv_cart.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                    @Override
                                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                                        nsv_scrollX = String.valueOf(scrollX);
                                        nsv_scrollY = String.valueOf(scrollY);
                                        // Log.d(TAG, "scrollX :" + nsv_scrollX + " , " + "scrollY :" + nsv_scrollY);

                                        if (scrollY > oldScrollY) {
                                            //Log.i(TAG, "Scroll DOWN");
                                        }
                                        if (scrollY < oldScrollY) {
                                            // Log.i(TAG, "Scroll UP");
                                        }

                                        if (scrollY == 0) {
                                            //  Log.i(TAG, "TOP SCROLL");
                                        }

                                        if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                                            // Log.i(TAG, "BOTTOM SCROLL");
                                        }
                                    }
                                });
                            }
                            txt_submitAddon.setOnClickListener(v -> {
                                //ingredientObjects.clear();

                                if (FC_Common.pricingID.equalsIgnoreCase("")) {
                                    snackBar("Please Select pricing Value");
                                    Utils.toast(getContext(),"Please Select pricing Value");
                                } else {
                                    txt_submitAddondummy.setText("");
                                    for (int i = 0; i < ingredientObjects.size(); i++) {

                                        if (ingredientObjects.get(i).getSelected()) {
                                            String number = ""+txt_submitAddondummy.getText()+"(,"+ingredientObjects.get(i).getId()+")";
                                            String newNumber = number.replace("(,", "(");
                                            Log.d("sfsdsdfsdfs", "number" + number);
                                            txt_submitAddondummy.setText(newNumber);
                                            String text = txt_submitAddondummy.getText().toString().trim();
                                            String finalnumber = text.replace(")(", ",");
                                            txt_submitAddondummy.setText(finalnumber);
                                            String finalnumber1 = finalnumber.replace("(", "");
                                            txt_submitAddondummy.setText(finalnumber1);
                                            String finalnumber2 = finalnumber1.replace(",)", "");
                                            txt_submitAddondummy.setText(finalnumber2);
                                            FC_Common.ingrdientTotal = finalnumber2.replace(")", "");
                                            txt_submitAddondummy.setText(finalnumber2);
                                            FC_Common.quantity="1";

                                            Log.d("sdgsvdfsdfsdf", "finalnumberdfs" + finalnumber);
                                            Log.d("sdgsvdfsdfsdf", "finalnumberzxc" + finalnumber1);
                                            Log.d("sdgsvdfsdfsdf", "finalnumberfsd" + finalnumber2);
                                            Log.d("sdgsvdfsdfsdf", "finalnumberxcx" + FC_Common.ingrdientTotal);
                                        }
                                    }
                                    txt_submitAddondummy2.setText("");
                                    for (int i = 0; i < addonObjects.size(); i++) {

                                        if (addonObjects.get(i).getSelected()) {
                                            String number = ""+txt_submitAddondummy2.getText()+"(,"+addonObjects.get(i).getId()+")";
                                            String newNumber = number.replace("(,", "(");
                                            Log.d("sfsdsdfsdfs", "number" + number);
                                            txt_submitAddondummy2.setText(newNumber);
                                            String text = txt_submitAddondummy2.getText().toString().trim();
                                            String finalnumber = text.replace(")(", ",");
                                            txt_submitAddondummy2.setText(finalnumber);
                                            String finalnumber1 = finalnumber.replace("(", "");
                                            txt_submitAddondummy2.setText(finalnumber1);
                                            String finalnumber2 = finalnumber1.replace(",)", "");
                                            txt_submitAddondummy2.setText(finalnumber2);
                                            FC_Common.addonTotal = finalnumber2.replace(")", "");
                                            txt_submitAddondummy2.setText(finalnumber2);
                                            FC_Common.quantity="1";

                                            Log.d("sdgsvdfsdfsdf", "finalnumberdfs" + finalnumber);
                                            Log.d("sdgsvdfsdfsdf", "finalnumberzxc" + finalnumber1);
                                            Log.d("sdgsvdfsdfsdf", "finalnumberfsd" + finalnumber2);
                                            Log.d("sdgsvdfsdfsdf", "finalnumberxcx" + FC_Common.addonTotal);
                                        }


                                    }

                                    FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                                    Log.d("csdgvhgcv","priceTotal"+FC_Common.priceTotal);
                                    Log.d("csdgvhgcv","price"+FC_Common.price);
                                    Log.d("csdgvhgcv","total_price"+FC_Common.total_price);
                                    if(FC_Common.maximum_order>FC_Common.priceTotal)
                                    {
                                        UpdateMenuAddon(FC_Common.ingrdientTotal, FC_Common.PreparationID, FC_Common.pricingID, FC_Common.addonTotal,FC_Common.quantity);

                                    }
                                    else {
                                        dialog.dismiss();
                                        Utils.toast(getContext(),getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                                        //snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                                    }



//                ArrayList<IngredientObject> actorList = ((IngredientAdapter)rv_ingredients.getAdapter()).getSelectActorList();
//                Toast.makeText(getContext(),""+actorList.toArray(),Toast.LENGTH_LONG).show();
                                }
                            });

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
                            FindViewByIdBottomDialog_bottom(view1);
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
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

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

  /*  private class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.ViewHolder> {
        private final FragmentActivity activity;

        public ItemDetailsAdapter(FragmentActivity activity) {
            this.activity = activity;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_hotel_items, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.ll_addBtn.setOnClickListener(view -> {

                holder.ll_addBtn.setVisibility(View.GONE);
                holder.ll_addAndMinus.setVisibility(View.VISIBLE);
                holder.img_plus.setVisibility(View.VISIBLE);
            });

            holder.ll_addAndMinus.setOnClickListener(view -> {

                ll_viewCart.setVisibility(View.VISIBLE);

                if (ll_viewCart.getVisibility() != View.VISIBLE) {
                    ll_viewCart.setVisibility(View.VISIBLE);

                    Animation animSlideUp1 = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_content, ll_addBtn, ll_addAndMinus;
            ImageView img_plus;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_content = itemView.findViewById(R.id.ll_content);

                ll_addBtn = itemView.findViewById(R.id.ll_addBtn);
                ll_addAndMinus = itemView.findViewById(R.id.ll_addAndMinus);
                img_plus = itemView.findViewById(R.id.img_plus);


            }
        }
    }*/
    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(ll_main, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", v -> bar.dismiss());
                    bar.setActionTextColor(Color.RED);
                    TextView tv = bar.getView().findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.CYAN);
                    bar.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } }};
        timerThread.start();
    }

    private void FindViewByIdBottomDialog(View view) {
        nsv_cart =  view.findViewById(R.id.nsv_cart);
        txt_submitAddon =  view.findViewById(R.id.txt_submitAddon);
        txt_submitAddondummy =  view.findViewById(R.id.txt_submitAddondummy);
        txt_submitAddondummy2 =  view.findViewById(R.id.txt_submitAddondummy2);
        txt_ingredients =  view.findViewById(R.id.txt_ingredients);
        rv_ingredients =  view.findViewById(R.id.rv_ingredients);
        txt_preparation =  view.findViewById(R.id.txt_preparation);
        rv_preparation =  view.findViewById(R.id.rv_preparation);
        txt_pricing =  view.findViewById(R.id.txt_pricing);
        rv_pricing =  view.findViewById(R.id.rv_pricing);
        txt_addon =  view.findViewById(R.id.txt_addon);
        rv_addon =  view.findViewById(R.id.rv_addon);
        ll_main =  view.findViewById(R.id.ll_main);

        pricingObjects = new ArrayList<>();
        PricingObject pricing = new PricingObject();
        pricing.setD_images("");
        pricing.setD_images("");
        pricingObjects.add(pricing);

        /*addonObjects = new ArrayList<>();
        AddonObject addonObject = new AddonObject();
        addonObject.setD_images("");
        addonObject.setD_images("");
        addonObjects.add(addonObject);


        ingredientObjects = new ArrayList<>();
        IngredientObject ingredientObject = new IngredientObject();
        ingredientObject.setD_images("");
        ingredientObject.setD_images("");
        ingredientObjects.add(ingredientObject);

        preparationObjects = new ArrayList<>();
        PreparationObject preparationObject = new PreparationObject();
        preparationObject.setD_images("");
        preparationObject.setD_images("");
        preparationObjects.add(preparationObject);*/

    }

    private void AddonList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTADDONLIST,
                response -> {
                    Log.d("gfdgvbcxbxcvxcvxc", ">>" + response);
                    Log.d("URL_PRODUCTADDONLIST", ">>" + FC_URL.URL_PRODUCTADDONLIST);
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);

                        try {
                            if (obj.optString("success").equals("1")) {
                                txt_ingredients.setVisibility(View.GONE);
                                txt_addon.setVisibility(View.GONE);
                                txt_preparation.setVisibility(View.GONE);
                                txt_pricing.setVisibility(View.GONE);
                                JSONArray pricing = obj.getJSONArray("pricing");
                                JSONArray addoncategories = obj.getJSONArray("addoncategories");
                                JSONArray ingredients = obj.getJSONArray("ingredients");
                                JSONArray preparations = obj.getJSONArray("preparations");
                                if (!pricing.equals("0")) {
                                    Log.d("sdgfsdfsdfsd","sdfsdfsdfsd");
                                    pricingObjects.clear();
                                    for (int i = 0; i < pricing.length(); i++) {

                                        PricingObject PricingModel = new PricingObject();
                                        JSONObject pricingitem = pricing.getJSONObject(i);
                                        PricingModel.setId(pricingitem.optString("id"));
                                        PricingModel.setProduct_id(pricingitem.optString("product_id"));
                                        PricingModel.setFoodsize_id(pricingitem.optString("foodsize_id"));
                                        PricingModel.setFoodsize_name(pricingitem.optString("foodsize_name"));
                                        PricingModel.setPrice(pricingitem.optString("price"));
                                        PricingModel.setCurrency(pricingitem.optString("currency"));
                                        pricingObjects.add(PricingModel);
                                        if (pricingObjects != null) {
                                            Log.d("sdgfsdfsdfsd","sdfsdfsdfsd");
                                            txt_pricing.setVisibility(View.VISIBLE);
                                            pricingAdapters.visibleContentLayout();
                                        }
                                    }

                                }
                                else {
                                    Log.d("sdgfsdfsdfsd","failsdfsdfsdfsd");
                                }
                                if (!addoncategories.equals("0")) {
                                    addonObjects.clear();
                                    for (int j = 0; j < addoncategories.length(); j++) {

                                        AddonObject AddonModel = new AddonObject();
                                        JSONObject addonObjectsitem = addoncategories.getJSONObject(j);
                                        AddonModel.setId(addonObjectsitem.optString("id"));
                                        AddonModel.setAddon_category_id(addonObjectsitem.optString("addon_category_id"));
                                        AddonModel.setAddon_name(addonObjectsitem.optString("addon_name"));
                                        AddonModel.setPrice(addonObjectsitem.optString("price"));
                                        AddonModel.setSequence(addonObjectsitem.optString("sequence"));
                                        AddonModel.setStatus(addonObjectsitem.optString("status"));
                                        AddonModel.setCurrency(addonObjectsitem.optString("currency"));
                                        addonObjects.add(AddonModel);
                                        if (addonObjects != null) {
                                            Log.d("sdgfsdfsdfsd","1213sdfsdfsdfsd");
                                            txt_addon.setVisibility(View.VISIBLE);
                                            addonAdapters.visibleContentLayout();
                                        }
                                    }

                                }
                                if (!ingredients.equals("0")) {
                                    ingredientObjects.clear();
                                    for (int k = 0; k < ingredients.length(); k++) {

                                        IngredientObject IngredientModel = new IngredientObject();
                                        JSONObject ingredientsitem = ingredients.getJSONObject(k);
                                        IngredientModel.setId(ingredientsitem.optString("id"));
                                        IngredientModel.setIngredient_name(ingredientsitem.optString("ingredient_name"));
                                        IngredientModel.setStatus(ingredientsitem.optString("status"));
                                        ingredientObjects.add(IngredientModel);
                                        if (ingredientObjects != null) {
                                            Log.d("sdgfsdfsdfsd","cxg45r3sdfsdfsdfsd");
                                            txt_ingredients.setVisibility(View.VISIBLE);
                                            ingredientAdapters.visibleContentLayout();
                                        }
                                    }
                                }
                                if (!preparations.equals("0")) {

                                    preparationObjects.clear();
                                    for (int m = 0; m < preparations.length(); m++) {

                                        PreparationObject PreparationModel = new PreparationObject();
                                        JSONObject preparationsitem = preparations.getJSONObject(m);
                                        PreparationModel.setId(preparationsitem.optString("id"));
                                        PreparationModel.setPreparation_type(preparationsitem.optString("preparation_type"));
                                        PreparationModel.setStatus(preparationsitem.optString("status"));
                                        preparationObjects.add(PreparationModel);
                                        if (preparationObjects != null) {
                                            Log.d("sdgfsdfsdfsd","sdg23qewe2sdfsdfsdfsd");
                                            txt_preparation.setVisibility(View.VISIBLE);
                                            preparationAdapters.visibleContentLayout();
                                        }
                                    }
                                }
                            }
                            else {
                                Log.d("sdgfsdfsdfsd","fail");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("sdgfsdfsdfsd", "" + e);
                            txt_pricing.setVisibility(View.GONE);
//                                women = 1;
//                                Utils.log(getActivity(), "women : " + women);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("sdgfsdfsdfsd", "fdhfdh" + e);
                        txt_pricing.setVisibility(View.GONE);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    Log.d("sdgfsdfsdfsd", "hfdhdf" + error);

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // params.put("id", FC_Common.hotelid);
                params.put("id", FC_Common.productID );
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
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
    //AddonList Async Task End//

    //Pricing Adapter Task Start//
    private void PricingRecycler() {

        pricingAdapters = new PricingAdapter(pricingObjects);
        rv_pricing.setAdapter(pricingAdapters);
        rv_pricing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.ViewHolder> {
        private final ArrayList<PricingObject> pricingObjects;
        boolean visible;
        private int mSelectedItem = -1;
        PricingAdapter( ArrayList<PricingObject> pricingObjects) {
            this.pricingObjects = pricingObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_addon, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.ll_loaderingredients.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.ll_checkBox.setVisibility(View.GONE);
                holder.ll_radioButton.setVisibility(View.VISIBLE);
                FC_Common.pricingName = pricingObjects.get(position).getFoodsize_name();
                /*final int position1 = holder.getAdapterPosition();
                mSelectedItem = position1;
                notifyDataSetChanged();
                FC_Common.pricingID = pricingObjects.get(position).getId();*/
                holder.rb_dishname.setText(FC_Common.pricingName);
                holder.txt_radiocurrency.setText(pricingObjects.get(position).getCurrency());
                holder.txt_radioprice.setText(pricingObjects.get(position).getPrice());
                holder.rb_dishname.setChecked(position == mSelectedItem);
                holder.rb_dishname.setOnClickListener(v -> {
                    final int position1 = holder.getAdapterPosition();
                    mSelectedItem = position1;
                    notifyDataSetChanged();
                    FC_Common.pricingID = pricingObjects.get(position1).getId();
                    FC_Common.price=Integer.parseInt(pricingObjects.get(position1).getPrice());
                    Log.d("Dfgdfgdfgfd","pricingID : "+FC_Common.pricingID);
                });
            }
        }

        @Override
        public int getItemCount() {
            return pricingObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
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

            LinearLayout ll_loaderingredients,ll_content,ll_radioButton,ll_checkBox;
            // RadioGroup radioGroup;
            CheckBox chk_dishName;
            AC_Textview txt_dishName,txt_radiocurrency,txt_radioprice;
            RadioButton rb_dishname;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_radioprice = itemView.findViewById(R.id.txt_radioprice);
                txt_radiocurrency = itemView.findViewById(R.id.txt_radiocurrency);
                rb_dishname = itemView.findViewById(R.id.rb_dishname);
                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                chk_dishName = itemView.findViewById(R.id.chk_dishName);
                // radioGroup = itemView.findViewById(R.id.radioGroup);
                ll_loaderingredients = itemView.findViewById(R.id.ll_loaderingredients);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_radioButton = itemView.findViewById(R.id.ll_radioButton);
                ll_checkBox = itemView.findViewById(R.id.ll_checkBox);
            }
        }
    }
    //Pricing Adapter Task End//

    //Addon Adapter Task Start//
    private void AddonRecycler() {

        addonAdapters = new AddonAdapter(addonObjects);
        rv_addon.setAdapter(addonAdapters);
        rv_addon.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.ViewHolder> {
        private final ArrayList<AddonObject> addonObjects;
        boolean visible;

        AddonAdapter( ArrayList<AddonObject> addonObjects) {
            this.addonObjects = addonObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_addon, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.ll_loaderingredients.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.ll_checkBox.setVisibility(View.VISIBLE);
                holder.ll_radioButton.setVisibility(View.GONE);

                FC_Common.addonName = addonObjects.get(position).getAddon_name();
                holder.txt_dishName.setText(FC_Common.ingredientName);


                //holder.chk_dishName.setText("Checkbox "+position);
                holder.txt_dishName.setText(FC_Common.addonName);
                holder.chk_dishName.setChecked(addonObjects.get(position).getSelected());
                holder.txt_dishcurrency.setText(addonObjects.get(position).getCurrency());
                holder.txt_dishprice.setText(addonObjects.get(position).getPrice());

                holder.chk_dishName.setTag( position);
                holder.chk_dishName.setOnClickListener(v -> {

                    Integer pos = (Integer)  holder.chk_dishName.getTag();
                    //Toast.makeText(getContext(), "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();
                    if(addonObjects.get(pos).getSelected()){
                        addonObjects.get(pos).setSelected(false);
                    }else {
                        addonObjects.get(pos).setSelected(true);
                    }

                });
            }
        }

        @Override
        public int getItemCount() {
            return addonObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
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

            LinearLayout ll_loaderingredients,ll_content,ll_radioButton,ll_checkBox;
            // RadioGroup radioGroup;
            CheckBox chk_dishName;
            AC_Textview txt_dishName,txt_dishprice,txt_dishcurrency;
            RadioButton rb_dishname;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_dishprice = itemView.findViewById(R.id.txt_dishprice);
                txt_dishcurrency = itemView.findViewById(R.id.txt_dishcurrency);
                rb_dishname = itemView.findViewById(R.id.rb_dishname);
                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                chk_dishName = itemView.findViewById(R.id.chk_dishName);
                //  radioGroup = itemView.findViewById(R.id.radioGroup);
                ll_loaderingredients = itemView.findViewById(R.id.ll_loaderingredients);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_radioButton = itemView.findViewById(R.id.ll_radioButton);
                ll_checkBox = itemView.findViewById(R.id.ll_checkBox);


            }
        }
    }
    //Addon Adapter Task End//

    //Preparation Adapter Task Start//
    private void PreparationRecycler() {

        preparationAdapters = new PreparationAdapter(preparationObjects);
        rv_preparation.setAdapter(preparationAdapters);
        rv_preparation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private class PreparationAdapter extends RecyclerView.Adapter<PreparationAdapter.ViewHolder> {
        private final ArrayList<PreparationObject> preparationObjects;
        boolean visible;
        private int mSelectedItem = -1;
        PreparationAdapter( ArrayList<PreparationObject> preparationObjects) {
            this.preparationObjects = preparationObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_addon, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {

                holder.ll_loaderingredients.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.ll_checkBox.setVisibility(View.GONE);
                holder.ll_radioButton.setVisibility(View.VISIBLE);
                Log.d("DFgsfdgfdsgsfd","dsgsdfsdf"+preparationObjects.get(position).getId());
                FC_Common.PreparationType = preparationObjects.get(position).getPreparation_type();
                holder.rb_dishname.setText(FC_Common.PreparationType);
                holder.rb_dishname.setChecked(position == mSelectedItem);
                //final RadioButton radioButton = findViewById(R.id.radioButton);

//                FC_Common.PreparationID = preparationObjects.get(position).getId();
//                Log.d("Dfgdfgdfgfd","fghfdgfd"+FC_Common.PreparationID);


                holder.rb_dishname.setOnClickListener(v -> {
                    final int position1 = holder.getAdapterPosition();
                    mSelectedItem = position1;
                    notifyDataSetChanged();
                    FC_Common.PreparationID = preparationObjects.get(position1).getId();
                    Log.d("Dfgdfgdfgfd","PreparationID: "+FC_Common.PreparationID);
                });

            }
        }

        @Override
        public int getItemCount() {
            return preparationObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
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
            LinearLayout ll_loaderingredients,ll_content,ll_radioButton,ll_checkBox;
            // RadioGroup radioGroup;
            CheckBox chk_dishName;
            AC_Textview txt_dishName;

            RadioButton rb_dishname;
            CompoundButton.OnCheckedChangeListener listener;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                rb_dishname = itemView.findViewById(R.id.rb_dishname);
                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                chk_dishName = itemView.findViewById(R.id.chk_dishName);
                //radioGroup = itemView.findViewById(R.id.radioGroup);
                ll_loaderingredients = itemView.findViewById(R.id.ll_loaderingredients);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_radioButton = itemView.findViewById(R.id.ll_radioButton);
                ll_checkBox = itemView.findViewById(R.id.ll_checkBox);
            }
        }
    }
    //Preparation Adapter Task End//

    //Ingredient Adapter Task Start//
    private void IngredientRecycler() {

        //ingredientObjects = getModel(false);
        ingredientAdapters = new IngredientAdapter(ingredientObjects);
        rv_ingredients.setAdapter(ingredientAdapters);
        rv_ingredients.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
        private final ArrayList<IngredientObject> ingredientObjects;
        boolean visible;
        IngredientAdapter( ArrayList<IngredientObject> ingredientObjects) {
            this.ingredientObjects = ingredientObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_addon, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.ll_loaderingredients.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.ll_checkBox.setVisibility(View.VISIBLE);
                holder.ll_radioButton.setVisibility(View.GONE);
                FC_Common.ingredientName = ingredientObjects.get(position).getIngredient_name();
                holder.txt_dishName.setText(FC_Common.ingredientName);


                //holder.chk_dishName.setText("Checkbox "+position);
                holder.txt_dishName.setText(FC_Common.ingredientName);
                holder.chk_dishName.setChecked(ingredientObjects.get(position).getSelected());

                holder.chk_dishName.setTag( position);
                holder.chk_dishName.setOnClickListener(v -> {

                    Integer pos = (Integer)  holder.chk_dishName.getTag();
                    //Toast.makeText(getContext(), "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();
                    if(ingredientObjects.get(pos).getSelected()){
                        ingredientObjects.get(pos).setSelected(false);
                    }else {
                        ingredientObjects.get(pos).setSelected(true);
                    }

                });
            }
        }

        @Override
        public int getItemCount() {
            return ingredientObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return ingredientObjects.get(position);
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

            LinearLayout ll_loaderingredients,ll_content,ll_radioButton,ll_checkBox;
            //  RadioGroup radioGroup;
            CheckBox chk_dishName;
            AC_Textview txt_dishName;
            RadioButton rb_dishname;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                rb_dishname = itemView.findViewById(R.id.rb_dishname);
                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                chk_dishName = itemView.findViewById(R.id.chk_dishName);
                //  radioGroup = itemView.findViewById(R.id.radioGroup);
                ll_loaderingredients = itemView.findViewById(R.id.ll_loaderingredients);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_radioButton = itemView.findViewById(R.id.ll_radioButton);
                ll_checkBox = itemView.findViewById(R.id.ll_checkBox);
            }
        }
    }
    //Ingredient Adapter Task End//

    private void UpdateMenuAddon(String ingrdientTotal,String PreparationID,String pricingID,String addonTotal,String quantity) {

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

                            FC_Common.ingrdientTotal="";
                            FC_Common.PreparationID="";
                            FC_Common.addonTotal="";
                            FC_Common.pricingID="";
                            addproducts = 1;
                            if (addonProducts != null)
                                addonProducts.addProducts(addproducts);
                            dialog.dismiss();
                            Log.d("addproducts", "dfgdfgfd" + addproducts);
                        AllRestaurantList(FC_Common.searchlist);
                        }
                        else
                        {
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        dialog.dismiss();
                        //AllRestaurantList(FC_Common.searchlist);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

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
                params.put("quantity", quantity);
                params.put("ingredient", ingrdientTotal);
                params.put("preparation", PreparationID);
                params.put("product_price_id", pricingID);
                params.put("addon", addonTotal);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
        requestQueue.add(stringRequest);

    }
    public interface AddonProducts {
        void addProducts(int products);
    }

    private void FindViewByIdBottomDialog_bottom(View view) {

        txt_ok = view.findViewById(R.id.txt_ok);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_header = view.findViewById(R.id.txt_header);
        ll_payment = view.findViewById(R.id.ll_payment);
        ll_clearcart = view.findViewById(R.id.ll_clearcart);
        txt_header.setText(R.string.f_clearcart);
        ll_payment.setVisibility(View.GONE);
        ll_clearcart.setVisibility(View.VISIBLE);

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
