package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCActivity.FCCartActivity.FC_CartAddressActivity;
import food.user.demand.FCActivity.FCProfile.FC_locationPickerActivty;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FC_OffersActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity.FC_OrderPickedUpActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants.Fc_CuisineSeeAllRestaurants;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants.Fc_HomeSeeAllRestaurants;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCTopPickRestaurants.Fc_TopPickRestaurant;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentGreatOffersActivity.FCGreatOfferForSetOFHotelsActivity.FC_SetOfHotelsOfferActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotSellersActivity.FC_HotSellersHotelActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FC_HotelDetailsActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentViewOnMap.FC_ViewOnMapActivity;
import food.user.demand.FCPojo.FCHomeFragmentObject.AllRestaurantObject;
import food.user.demand.FCPojo.FCHomeFragmentObject.CardOffersObject;
import food.user.demand.FCPojo.FCHomeFragmentObject.GreatOfferObject;
import food.user.demand.FCPojo.FCHomeFragmentObject.HotSellersObject;
import food.user.demand.FCPojo.FCHomeFragmentObject.ItemViewObject;
import food.user.demand.FCPojo.FCHomeFragmentObject.OrderObject;
import food.user.demand.FCPojo.FCHomeFragmentObject.TopPickForYouObject;
import food.user.demand.FCUtils.BottomDailog.BottomDialogForFilter;
import food.user.demand.FCUtils.Loader.LoaderCircluarImageView;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;
public class FC_HomeFragment extends Fragment implements View.OnClickListener ,BottomDialogForFilter.CheckType {

    private LinearLayout ll_seeall;
    private LinearLayout ll_noSeeall;
    private LinearLayout ll_filter;
    private LinearLayout ll_LocationAndOffer;
    private LinearLayout ll_searchLayout;
    private LinearLayout ll_cardOffers;
    private LinearLayout ll_allRestaurant;
    private LinearLayout ll_seeAllRestaurant;
    private LoaderTextView lt_filter_loaderLayout, lt_selectLocation, lt_searchLayout, lt_seeAll, lt_topPic,lt_greatOffer,lt_itemOffer,
            lt_cardOffers, lt_allRestaurant;
    private ImageView lc_sellAllImg;
    private Snackbar bar;
    private GreatOfferAdapter greatOfferAdapter;
    private RecyclerView rv_greatOffer;

    private ItemViewAdapter itemViewAdapter;
    private RecyclerView rv_itemView;

    private HotSellersAdapter hotSellersAdapter;
    private RecyclerView rv_hotSellers,rv_search;
    private AC_Textview txt_selectlocation;
    private AC_Textview txt_emptyview;
    private TopPicksAdapter topPicksAdapter;
    private RecyclerView rv_topPicks;

    private CardOffersAdapter cardOffersAdapter;
    private OrderAdapter orderAdapters;
    private RecyclerView rv_cardOffers;

    private AllRestaurantAdapter allRestaurantAdapter;
    private RecyclerView rv_allRestaurants;

    private ArrayList<GreatOfferObject> greatOfferObjects;
    private ArrayList<ItemViewObject> itemViewObjects;
    private ArrayList<HotSellersObject> hotSellersObjects;
    private ArrayList<TopPickForYouObject> topPickForYouObjects;
    private ArrayList<CardOffersObject> cardOffersObjects;
    private ArrayList<AllRestaurantObject> allRestaurantObjects;
    private ArrayList<OrderObject> orderObjects = new ArrayList<>();
    private FrameLayout lin_frame;
    private Handler handler;
    private int greatoffercounter = 0;
    private int itemcounter = 0;
    private int hotsellercounter = 0;
    private int toppickcounter = 0;
    private int allresataurantcounter = 0;
    private int cardoffercounter = 0;
    private BottomSheetDialog orderdialog;
    private Context context;
    public FC_HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Utils.adjustFontScale(getActivity(),getResources().getConfiguration());
        return inflater.inflate(R.layout.fragment_fc__home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        context=getActivity();
        Log.d("fghfghfdghfg","Home");
        Bundle object = getArguments();
        if (object != null) {
            FC_Common.typeVeg = object.getString("typeVeg");
            FC_Common.filter_price_min_check = object.getString("filter_price_min");
            FC_Common.filter_price_max_check = object.getString("filter_price_max");
            FC_Common.typePopularity = object.getString("typePopularity");
            FC_Common.cuisinecheck = object.getString("cuisinecheck");
            Log.d("dfgfdgsfdg","home"+FC_Common.typeVeg);
            Log.d("dfgfdgsfdg","home"+FC_Common.filter_price_min_check);
            Log.d("dfgfdgsfdg","home"+FC_Common.filter_price_max_check);
            Log.d("dfgfdgsfdg","home"+FC_Common.typePopularity);
            Log.d("dfgfdgsfdg","home"+FC_Common.cuisinecheck);
        }
        Log.d("xgsfdbxcvbxc","check"+FC_Common.filter_price_min_check);
        Log.d("xgsfdbxcvbxc","check1"+FC_Common.filter_price_max_check);
        if (FC_Common.filter_price_min_check.equalsIgnoreCase("")){
            FC_Common.filter_price_min_check=FC_Common.filter_price_min;
        }
        if (FC_Common.filter_price_max_check.equalsIgnoreCase(""))
        {
            FC_Common.filter_price_max_check=FC_Common.filter_price_max;
        }
        //CheckPermission();

       /* DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);

        @SuppressLint("DefaultLocale")
        String check =String.format("%.1f", screenInches);

        Log.d("dgsdgsdfs","dfgsdfgsd: "+screenInches+"dgdsg: "+check);*/
        FindViewById(view);
        //RecyclerView Adapter//
        GreatofferRecycler();
        ItemofferRecycler();
        HotsellerRecycler();
        TopPicksRecycler();
        cardoffersRecycler();
        AllRestaurantRecycler();
        //RecyclerView List//
        GreatofferList();
        ItemViewList();
        HotsellerList();
        TopPicksList();
        CardoffersList();
        AllRestaurantList();


        //Greatoffer Adapter Set//
        greatOfferAdapter = new GreatOfferAdapter( greatOfferObjects);
        LinearLayoutManager greatofferLL = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_greatOffer.setLayoutManager(greatofferLL);
        rv_greatOffer.setAdapter(greatOfferAdapter);
        //itemViewAdapter  Set//
        itemViewAdapter = new ItemViewAdapter(itemViewObjects);
        LinearLayoutManager itemViewLL = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_itemView.setLayoutManager(itemViewLL);
        rv_itemView.setAdapter(itemViewAdapter);
//hotSellersAdapter  Set//
        hotSellersAdapter = new HotSellersAdapter(hotSellersObjects);
        RecyclerView.LayoutManager rvLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        rv_hotSellers.setLayoutManager(rvLayoutManager);
        rv_hotSellers.setAdapter(hotSellersAdapter);
        //topPicksAdapter  Set//
        topPicksAdapter = new TopPicksAdapter( topPickForYouObjects);
        LinearLayoutManager itemViewLLl = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_topPicks.setLayoutManager(itemViewLLl);
        rv_topPicks.setAdapter(topPicksAdapter);
//cardOffersAdapter  Set//
        cardOffersAdapter = new CardOffersAdapter(cardOffersObjects);
        LinearLayoutManager itemViewLLcard = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_cardOffers.setLayoutManager(itemViewLLcard);
        rv_cardOffers.setAdapter(cardOffersAdapter);
//allRestaurantAdapter  Set//
        allRestaurantAdapter = new AllRestaurantAdapter(allRestaurantObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_allRestaurants.setLayoutManager(itemViewLLres);
        rv_allRestaurants.setAdapter(allRestaurantAdapter);

        //Data file initialization//
        FC_User user = FC_SharedPrefManager.getInstance(getActivity()).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.mobile = String.valueOf(user.getmobile());
        FC_Common.email_verified_at = String.valueOf(user.getemail_verified_at());
        FC_Common.dial_code = String.valueOf(user.getdial_code());
        FC_Common.location_name = String.valueOf(user.getlocation_name());
        FC_Common.location_type = String.valueOf(user.getlocation_type());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.status = String.valueOf(user.getstatus());
        FC_Common.is_guest = String.valueOf(user.getis_guest());
        FC_Common.picture = String.valueOf(user.getpicture());
        FC_Common.device_token = String.valueOf(user.getdevice_token());
        FC_Common.device_id = String.valueOf(user.getdevice_id());
        FC_Common.device_type = String.valueOf(user.getdevice_type());
        FC_Common.login_by = String.valueOf(user.getlogin_by());
        FC_Common.social_unique_id = String.valueOf(user.getsocial_unique_id());
        FC_Common.cust_id = String.valueOf(user.getcust_id());
        FC_Common.wallet_balance = String.valueOf(user.getwallet_balance());
        FC_Common.rating = String.valueOf(user.getrating());
        FC_Common.userotp = String.valueOf(user.getotp());
        FC_Common.created_at = String.valueOf(user.getcreated_at());
        FC_Common.updated_at = String.valueOf(user.getupdated_at());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.gender = String.valueOf(user.getgender());
        FC_Common.filter_price_min = String.valueOf(user.getfilter_price_min());
        FC_Common.filter_price_max = String.valueOf(user.getfilter_price_max());
        txt_selectlocation.setText(FC_Common.location_type);
    }

    @SuppressLint("SetTextI18n")
    private void FindViewById(View view) {
        /*location Selection*/
        ll_LocationAndOffer = view.findViewById(R.id.ll_LocationAndOffer);
        LinearLayout ll_selectLocation = view.findViewById(R.id.ll_selectLocation);
        lt_selectLocation = view.findViewById(R.id.lt_selectLocation);
        ImageView img_order = view.findViewById(R.id.img_order);
        txt_selectlocation = view.findViewById(R.id.txt_selectlocation);
        LinearLayout ll_offer = view.findViewById(R.id.ll_offer);
        lin_frame = view.findViewById(R.id.lin_frame);
        //parentLayout = view.findViewById(android.R.id.content);
        /*  searchLayout*/
        ll_searchLayout = view.findViewById(R.id.ll_searchLayout);
        lt_searchLayout = view.findViewById(R.id.lt_searchLayout);
        /*filter layout*/
        ll_filter = view.findViewById(R.id.ll_filter);
        lt_filter_loaderLayout = view.findViewById(R.id.lt_filter_loaderLayout);
        LinearLayout ll_filterSortBy = view.findViewById(R.id.ll_filterSortBy);
        LinearLayout ll_vegOnly = view.findViewById(R.id.ll_vegOnly);
        LinearLayout  ll_priceRange = view.findViewById(R.id.ll_priceRange);
        LinearLayout ll_rating = view.findViewById(R.id.ll_rating);
        LinearLayout  ll_cuisines = view.findViewById(R.id.ll_cuisines);
        ImageView img_plusVegOnly = view.findViewById(R.id.img_plusVegOnly);
        ImageView img_plusPriceRange = view.findViewById(R.id.img_plusPriceRange);
        ImageView img_plusRating = view.findViewById(R.id.img_plusRating);
        ImageView img_plusCuisines = view.findViewById(R.id.img_plusCuisines);
        // here all icons visiblity are gone here make sure while onclick implements
        img_plusVegOnly.setVisibility(View.GONE);
        img_plusPriceRange.setVisibility(View.GONE);
        img_plusRating.setVisibility(View.GONE);
        img_plusCuisines.setVisibility(View.GONE);
        /*HotSeller*/
        LinearLayout ll_seeAllHotSellers = view.findViewById(R.id.ll_seeAllHotSellers);
        lt_seeAll = view.findViewById(R.id.lt_seeAll);
        ll_seeall = view.findViewById(R.id.ll_seeall);
        ll_noSeeall = view.findViewById(R.id.ll_noSeeall);
        lc_sellAllImg = view.findViewById(R.id.lc_sellAllImg);
        /*topPick*/
        lt_topPic = view.findViewById(R.id.lt_topPic);
        lt_greatOffer = view.findViewById(R.id.lt_greatOffer);
        lt_itemOffer = view.findViewById(R.id.lt_itemOffer);
        /*CardOffers*/
        ll_cardOffers = view.findViewById(R.id.ll_cardOffers);
        lt_cardOffers = view.findViewById(R.id.lt_cardOffers);
        /*  allRestaurant*/
        LinearLayout ll_viewOnMap = view.findViewById(R.id.ll_viewOnMap);
        ll_viewOnMap.setOnClickListener(this);
        lt_allRestaurant = view.findViewById(R.id.lt_allRestaurant);
        ll_allRestaurant = view.findViewById(R.id.ll_allRestaurant);
        ll_seeAllRestaurant = view.findViewById(R.id.ll_seeAllRestaurant);
        /*RecycleView*/
        rv_greatOffer = view.findViewById(R.id.rv_greatOffer);
        rv_itemView = view.findViewById(R.id.rv_itemView);
        rv_hotSellers = view.findViewById(R.id.rv_hotSellers);
        rv_topPicks = view.findViewById(R.id.rv_topPicks);
        rv_cardOffers = view.findViewById(R.id.rv_cardOffers);
        rv_allRestaurants = view.findViewById(R.id.rv_allRestaurants);
        /*setOnClickListener*/
        ll_offer.setOnClickListener(this);
        ll_filterSortBy.setOnClickListener(this);
        ll_vegOnly.setOnClickListener(this);
        ll_priceRange.setOnClickListener(this);
        ll_rating.setOnClickListener(this);
        ll_cuisines.setOnClickListener(this);
        ll_selectLocation.setOnClickListener(this);
        ll_seeAllRestaurant.setOnClickListener(this);
        ll_seeAllHotSellers.setOnClickListener(this);
        img_order.setOnClickListener(this);
        ll_searchLayout.setOnClickListener(this);
        /*GreatOfferObject*/
        greatOfferObjects = new ArrayList<>();
        GreatOfferObject greatOffer = new GreatOfferObject();
        greatOffer.setD_images("");
        greatOffer.setD_info("");
        greatOfferObjects.add(greatOffer);
        greatOffer.setD_images("");
        greatOffer.setD_info("");
        greatOfferObjects.add(greatOffer);
        /* ItemViewObjects*/
        itemViewObjects = new ArrayList<>();
        ItemViewObject viewObject = new ItemViewObject();
        viewObject.setD_images("");
        viewObject.setD_info("");
        itemViewObjects.add(viewObject);
        viewObject.setD_images("");
        viewObject.setD_info("");
        itemViewObjects.add(viewObject);
        viewObject.setD_images("");
        viewObject.setD_info("");
        itemViewObjects.add(viewObject);
        viewObject.setD_images("");
        viewObject.setD_info("");
        itemViewObjects.add(viewObject);
        viewObject.setD_images("");
        viewObject.setD_info("");
        itemViewObjects.add(viewObject);
        /*HotSellersObjects*/
        hotSellersObjects = new ArrayList<>();
        HotSellersObject sellersObject = new HotSellersObject();
        sellersObject.setD_images("");
        sellersObject.setD_images("");
        hotSellersObjects.add(sellersObject);
        sellersObject.setD_images("");
        sellersObject.setD_images("");
        hotSellersObjects.add(sellersObject);
        sellersObject.setD_images("");
        sellersObject.setD_images("");
        hotSellersObjects.add(sellersObject);
        /*  TopPickForYouObject*/
        topPickForYouObjects = new ArrayList<>();
        TopPickForYouObject pickForYouObject = new TopPickForYouObject();
        pickForYouObject.setD_images("");
        pickForYouObject.setD_images("");
        topPickForYouObjects.add(pickForYouObject);
        pickForYouObject.setD_images("");
        pickForYouObject.setD_images("");
        topPickForYouObjects.add(pickForYouObject);
        pickForYouObject.setD_images("");
        pickForYouObject.setD_images("");
        topPickForYouObjects.add(pickForYouObject);
        /*CardOffers*/
        cardOffersObjects = new ArrayList<>();
        CardOffersObject offersObject = new CardOffersObject();
        offersObject.setD_images("");
        offersObject.setD_images("");
        cardOffersObjects.add(offersObject);
        offersObject.setD_images("");
        offersObject.setD_images("");
        cardOffersObjects.add(offersObject);
        /*AllRestaurant*/
        allRestaurantObjects = new ArrayList<>();
        AllRestaurantObject object = new AllRestaurantObject();
        object.setD_images("");
        object.setD_images("");
        allRestaurantObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        allRestaurantObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        allRestaurantObjects.add(object);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("typeveg", Context.MODE_PRIVATE);
        String vegCheck = sharedPreferences.getString("typeVeg","");
        Log.d("fgdfgdfgdf","sdfsdfsd"+vegCheck);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_offer:
                Intent offer = new Intent(getActivity(), FC_OffersActivity.class);
                startActivity(offer);
                break;
            case R.id.ll_filterSortBy:
                int bottomSheetValue = 1;
                Bundle filterbundle = new Bundle();
                filterbundle.putString("typeVeg", FC_Common.typeVeg);
                filterbundle.putString("filter_price_min",FC_Common.filter_price_min_check);
                filterbundle.putString("filter_price_max",FC_Common.filter_price_max_check);
                filterbundle.putString("typePopularity",FC_Common.typePopularity);
                filterbundle.putString("cuisinecheck",FC_Common.cuisinecheck);
                //bundle.putString("hotelpricing",FC_Common.hotelpricing);

                BottomDialogForFilter filterBottomDialogFragment = BottomDialogForFilter.newInstance();
                filterBottomDialogFragment.onclick(getActivity(), bottomSheetValue);
                filterBottomDialogFragment.setArguments(filterbundle);
                filterBottomDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "dialog_fragment");

                break;
            case R.id.ll_vegOnly:
                bottomSheetValue = 2 ;
                Bundle vegbundle = new Bundle();
                vegbundle.putString("typeVeg", FC_Common.typeVeg);
                vegbundle.putString("filter_price_min",FC_Common.filter_price_min_check);
                vegbundle.putString("filter_price_max",FC_Common.filter_price_max_check);
                vegbundle.putString("typePopularity",FC_Common.typePopularity);
                vegbundle.putString("cuisinecheck",FC_Common.cuisinecheck);

                Log.d("dfgfdgsfdg","pass"+FC_Common.typeVeg);
                BottomDialogForFilter filtervegBottomDialogFragment = BottomDialogForFilter.newInstance();
                filtervegBottomDialogFragment.onclick(getActivity(), bottomSheetValue);
                filtervegBottomDialogFragment.setArguments(vegbundle);
                filtervegBottomDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        "dialog_fragment");
                break;
            case R.id.ll_priceRange:
                bottomSheetValue = 3 ;
                Bundle rangebundle = new Bundle();
                rangebundle.putString("typeVeg", FC_Common.typeVeg);
                rangebundle.putString("filter_price_min",FC_Common.filter_price_min_check);
                rangebundle.putString("filter_price_max",FC_Common.filter_price_max_check);
                rangebundle.putString("typePopularity",FC_Common.typePopularity);
                rangebundle.putString("cuisinecheck",FC_Common.cuisinecheck);
                BottomDialogForFilter filterPriceBottomDialogFragment =
                        BottomDialogForFilter.newInstance();
                filterPriceBottomDialogFragment.onclick(getActivity(), bottomSheetValue);
                filterPriceBottomDialogFragment.setArguments(rangebundle);
                filterPriceBottomDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        "dialog_fragment");
                break;
            case R.id.ll_rating:
                bottomSheetValue = 4 ;
                Bundle ratingbundle = new Bundle();
                ratingbundle.putString("typeVeg", FC_Common.typeVeg);
                ratingbundle.putString("filter_price_min",FC_Common.filter_price_min_check);
                ratingbundle.putString("filter_price_max",FC_Common.filter_price_max_check);
                ratingbundle.putString("typePopularity",FC_Common.typePopularity);
                ratingbundle.putString("cuisinecheck",FC_Common.cuisinecheck);
                BottomDialogForFilter filterRatingBottomDialogFragment =
                        BottomDialogForFilter.newInstance();
                filterRatingBottomDialogFragment.onclick(getActivity(), bottomSheetValue);
                filterRatingBottomDialogFragment.setArguments(ratingbundle);
                filterRatingBottomDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        "dialog_fragment");
                break;
            case R.id.ll_cuisines:
                bottomSheetValue = 5 ;
                Bundle cuisinebundle = new Bundle();
                cuisinebundle.putString("typeVeg", FC_Common.typeVeg);
                cuisinebundle.putString("filter_price_min",FC_Common.filter_price_min_check);
                cuisinebundle.putString("filter_price_max",FC_Common.filter_price_max_check);
                cuisinebundle.putString("typePopularity",FC_Common.typePopularity);
                cuisinebundle.putString("cuisinecheck",FC_Common.cuisinecheck);
                BottomDialogForFilter filterCuisinesBottomDialogFragment =
                        BottomDialogForFilter.newInstance();
                filterCuisinesBottomDialogFragment.onclick(getActivity(), bottomSheetValue);
                filterCuisinesBottomDialogFragment.setArguments(cuisinebundle);
                filterCuisinesBottomDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        "dialog_fragment");
                break;
            case R.id.ll_selectLocation:
                Intent intent = new Intent(getActivity(), FC_locationPickerActivty.class);
                intent.putExtra("username", FC_Common.name);
                intent.putExtra("usermobile", FC_Common.mobile);
                intent.putExtra("useremail", FC_Common.email);
                intent.putExtra("usergender", FC_Common.gender);
                intent.putExtra("deviceid", FC_Common.device_id);
                intent.putExtra("token_type", FC_Common.token_type);
                intent.putExtra("access_token", FC_Common.access_token);
                intent.putExtra("latitude", FC_Common.latitude);
                intent.putExtra("longitude", FC_Common.longitude);
                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                intent.putExtra("change_address", "dash");
                startActivity(intent);
                break;
            case R.id.ll_viewOnMap:
                Intent viewOnMap = new Intent(getActivity(), FC_ViewOnMapActivity.class);
                startActivity(viewOnMap);
                break;
            case R.id.ll_seeAllRestaurant:
                Intent ll_seeAllRestaurant = new Intent(getActivity(), Fc_HomeSeeAllRestaurants.class);
                startActivity(ll_seeAllRestaurant);
                break;
            case R.id.ll_seeAllHotSellers:
                Intent seeAllHotSellers =new Intent(getActivity(), FC_HotSellersHotelActivity.class);
                startActivity(seeAllHotSellers);
                break;
            case R.id.ll_searchLayout:
                Intent searchdish =new Intent(getActivity(), FC_SearchActivity.class);
                startActivity(searchdish);
                break;
            case R.id.img_order:
                @SuppressLint("InflateParams")
                View view1 = getLayoutInflater().inflate(R.layout.layout_address_bottom_sheet, null);
                FindViewByIdBottomDialog(view1);
                OrderRecycler();
                Orderlist();
                orderdialog = new BottomSheetDialog(context);
                orderdialog.setContentView(view1);
                orderdialog.show();
                break;
        }
    }
    //GreatOffer Async Task Start//
    private void GreatofferList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_GREATOFFERLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("dfgfdgfd","dfgdfgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            //LocationObject.clear();
                            JSONArray dataArray = obj.getJSONArray("data");
                            greatOfferObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                GreatOfferObject playerModel = new GreatOfferObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setTitle(product.getString("title"));
                                    playerModel.setUrl(product.getString("url"));
                                    playerModel.setPosition(product.getString("position"));
                                    playerModel.setStatus(product.getString("status"));
                                    greatOfferObjects.add(playerModel);
                                    if (greatOfferObjects != null) {

                                        lt_greatOffer.setText(R.string.great_offer);
                                        greatOfferAdapter.visibleContentLayout();

                                    }
                                    else {

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("dfgfdgsfdg","esd"+ex);

                                }
                            }
                        } else {
                            lt_greatOffer.setText(R.string.noitem);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));

                    }
                }, error -> {
            //displaying the error in toast if occurrs
            snackBar(getResources().getString(R.string.reach));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void GreatofferRecycler() {
        greatOfferAdapter = new GreatOfferAdapter(greatOfferObjects);
        rv_greatOffer.setAdapter(greatOfferAdapter);
        rv_greatOffer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
    private class GreatOfferAdapter extends RecyclerView.Adapter<GreatOfferAdapter.ViewHolder> {
        boolean visible;
        private final ArrayList<GreatOfferObject> greatOfferObjects;
        GreatOfferAdapter( ArrayList<GreatOfferObject> greatOfferObjects) {
            this.greatOfferObjects = greatOfferObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_great_offer, parent, false);
            return new ViewHolder(listItem);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                lt_selectLocation.setVisibility(View.GONE);
                ll_LocationAndOffer.setVisibility(View.VISIBLE);
                lt_searchLayout.setVisibility(View.GONE);
                ll_searchLayout.setVisibility(View.VISIBLE);
                lt_filter_loaderLayout.setVisibility(View.GONE);
                ll_filter.setVisibility(View.VISIBLE);
                //Picasso.get().load(greatOfferObjects.get(position).getUrl()).into(holder.ll_banner);
                Picasso.get().load(greatOfferObjects.get(position).getUrl())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ll_banner);
                holder.itemView.setOnClickListener(view -> {
                    DisplayMetrics dm = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int width=dm.widthPixels;
                    int height=dm.heightPixels;
                    double wi=(double)width/(double)dm.xdpi;
                    double hi=(double)height/(double)dm.ydpi;
                    double x = Math.pow(wi,2);
                    double y = Math.pow(hi,2);
                    double screenInches = Math.sqrt(x+y);
                    if (screenInches>=5){
                        FC_Common.bannerid=greatOfferObjects.get(position).getId();
                        FC_Common.bannerdevicesize="5";
                        Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                        setOfHotels.putExtra("bannerid",FC_Common.bannerid);
                        setOfHotels.putExtra("sizes",FC_Common.bannerdevicesize);
                        startActivity(setOfHotels);
                    }
                    else if (screenInches>=6){
                        FC_Common.bannerid=greatOfferObjects.get(position).getId();
                        FC_Common.bannerdevicesize="6";
                        Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                        setOfHotels.putExtra("bannerid",FC_Common.bannerid);
                        setOfHotels.putExtra("sizes",FC_Common.bannerdevicesize);
                        startActivity(setOfHotels);
                    }
                    else if (screenInches>=7){
                        FC_Common.bannerid=greatOfferObjects.get(position).getId();
                        FC_Common.bannerdevicesize="7";
                        Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                        setOfHotels.putExtra("bannerid",FC_Common.bannerid);
                        setOfHotels.putExtra("sizes",FC_Common.bannerdevicesize);
                        startActivity(setOfHotels);
                    }
                    else if (screenInches>=8){
                        FC_Common.bannerid=greatOfferObjects.get(position).getId();
                        FC_Common.bannerdevicesize="8";
                        Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                        setOfHotels.putExtra("bannerid",FC_Common.bannerid);
                        setOfHotels.putExtra("sizes",FC_Common.bannerdevicesize);
                        startActivity(setOfHotels);
                    }
                    else if (screenInches>=9){
                        FC_Common.bannerid=greatOfferObjects.get(position).getId();
                        FC_Common.bannerdevicesize="8";
                        Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                        setOfHotels.putExtra("bannerid",FC_Common.bannerid);
                        setOfHotels.putExtra("sizes",FC_Common.bannerdevicesize);
                        startActivity(setOfHotels);
                    }
                    else if (screenInches>=10){
                        FC_Common.bannerid=greatOfferObjects.get(position).getId();
                        FC_Common.bannerdevicesize="8";
                        Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                        setOfHotels.putExtra("bannerid",FC_Common.bannerid);
                        setOfHotels.putExtra("sizes",FC_Common.bannerdevicesize);
                        startActivity(setOfHotels);
                    }

                });
            }
        }
        @Override
        public int getItemCount() { return greatOfferObjects.size(); }
        @Override
        public int getItemViewType(int position) { return super.getItemViewType(position); }
        void visibleContentLayout() { visible = true;notifyDataSetChanged(); }
        @Override
        public long getItemId(int position) { return super.getItemId(position); }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView ll_banner;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_banner = itemView.findViewById(R.id.ll_banner);
            }}}
    //GreatOffer Async Task End//
    //ItemView Async Task Start//
    private void ItemViewList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ITEMOFFERLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            itemViewObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                ItemViewObject playerModel = new ItemViewObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    //playerModel.setPartner_id(product.getString("partner_id"));
                                    playerModel.setCategory_name(product.getString("category_name"));
                                    playerModel.setImage(product.getString("image"));
                                    //playerModel.setStatus(product.getString("status"));
                                    itemViewObjects.add(playerModel);
                                    if (itemViewObjects != null) {
                                        lt_itemOffer.setText(getResources().getString(R.string.top_category));
                                        itemViewAdapter.visibleContentLayout();
                                    }
                                    else {

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();

                                } }
                        } else {
                            lt_itemOffer.setText(R.string.noitem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));

                    }
                }, error -> {

            snackBar(getResources().getString(R.string.reach));

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("dish_id", FC_Common.typeVeg);
                params.put("price_from", FC_Common.filter_price_min_check);
                params.put("price_to", FC_Common.filter_price_max_check);
                params.put("rating", FC_Common.typePopularity);
                params.put("cuisines", FC_Common.cuisinecheck);
                Log.d("fhdfgdfgfdg","dfgdfgdfg"+params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    private void ItemofferRecycler() {
        itemViewAdapter = new ItemViewAdapter(itemViewObjects);
        rv_itemView.setAdapter(itemViewAdapter);
        rv_itemView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
    private class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {
        private final ArrayList<ItemViewObject> itemViewObjects;
        boolean visible;
        ItemViewAdapter(ArrayList<ItemViewObject> itemViewObjects) {
            this.itemViewObjects = itemViewObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_item_view, parent, false);
            return new ViewHolder(listItem);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.lc_item.setVisibility(View.GONE);
                holder.lc_circleItem.setVisibility(View.VISIBLE);
                holder.txt_itemName.setText(itemViewObjects.get(position).getCategory_name());
                //Picasso.get().load(itemViewObjects.get(position).getImage()).into(holder.lc_circleItem);

                Picasso.get().load(itemViewObjects.get(position).getImage())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lc_circleItem);


                // Random rand = new Random();
                //The random generator creates values between [0,256) for use as RGB values used below to create a random color
                //We call the RelativeLayout object and we change the color.  The first parameter in argb() is the alpha.
                //  holder.ll_item.setBackgroundColor(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256) ));


               /* Picasso.get().load(itemViewObjects.get(position).getImage())
                        .placeholder(getRandomDrawbleColor())
                        .into(holder.lc_item);
                Picasso.get().load(itemViewObjects.get(position).getImage()).into(holder.lc_item, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (itemViewObjects.get(position).getPosterPalette()!=null){
                            setUpInfoBackgroundColor(holder.ll_item , itemViewObjects.get(position).getPosterPalette());

                        }
                        else {
                            Bitmap bitmap = ((BitmapDrawable) holder.lc_item.getDrawable()).getBitmap();

                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    itemViewObjects.get(position).setPosterPalette(palette);

                                    setUpInfoBackgroundColor(holder.ll_item, palette);

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });*/
               holder.itemView.setOnClickListener(view -> {
                   FC_Common.CuisineItemId=itemViewObjects.get(position).getId();
                   Intent ItemViewIntent = new Intent(getActivity(), Fc_CuisineSeeAllRestaurants.class);
                   ItemViewIntent.putExtra("cuisines", FC_Common.CuisineItemId);
                   startActivity(ItemViewIntent);
               });
            }}
        @Override
        public int getItemCount() {
            return itemViewObjects.size();
        }
        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderCircluarImageView lc_item;
            CircleImageView lc_circleItem;
            LinearLayout ll_item;
            AC_Textview txt_itemName;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                lc_item = itemView.findViewById(R.id.lc_item);
                ll_item = itemView.findViewById(R.id.ll_item);
                lc_circleItem = itemView.findViewById(R.id.lc_circleItem);
                txt_itemName = itemView.findViewById(R.id.txt_itemName);
            }
        }
    }
    //ItemView Async Task End//
    //HotSeller Async Task Start//
    private void HotsellerList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_HOTSELLERLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            hotSellersObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                HotSellersObject playerModel = new HotSellersObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRating(product.getString("rating"));
                                    playerModel.setDelivery_estimation(product.getString("delivery_estimation"));
                                    playerModel.setPerson_limit(product.getString("person_limit"));
                                    playerModel.setCost_limit(product.getString("cost_limit"));
                                    playerModel.setRestaurant_latitude(product.getString("restaurant_latitude"));
                                    playerModel.setRestaurant_longitude(product.getString("restaurant_longitude"));
                                    playerModel.setDistance(product.getString("distance"));
                                    playerModel.setCuisines(product.getString("cuisines"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setRestaurant_status(product.getString("restaurant_status"));
                                    hotSellersObjects.add(playerModel);
                                    if (hotSellersObjects != null) {
                                        lt_seeAll.setText(R.string.see_all);
                                        lc_sellAllImg.setVisibility(View.VISIBLE);
                                        ll_noSeeall.setVisibility(View.GONE);
                                        hotSellersAdapter.visibleContentLayout();
                                    }
                                    else {

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();

                                }}
                        } else {
                            ll_seeall.setVisibility(View.GONE);
                            ll_noSeeall.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("" + getResources().getString(R.string.reach));

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("dish_id", FC_Common.typeVeg);
                params.put("price_from", FC_Common.filter_price_min_check);
                params.put("price_to", FC_Common.filter_price_max_check);
                params.put("rating", FC_Common.typePopularity);
                params.put("cuisines", FC_Common.cuisinecheck);
                Utils.log(getContext(),"params: "+params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        } }
    private void HotsellerRecycler() {
        hotSellersAdapter = new HotSellersAdapter(hotSellersObjects);
        rv_hotSellers.setAdapter(hotSellersAdapter);
        rv_hotSellers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }
    private class HotSellersAdapter extends RecyclerView.Adapter<HotSellersAdapter.ViewHolder> {
        private final ArrayList<HotSellersObject> hotSellersObjects;
        boolean visible;
        HotSellersAdapter( ArrayList<HotSellersObject> hotSellersObjects) {
            this.hotSellersObjects = hotSellersObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_hot_seller_item, parent, false);
            return new ViewHolder(listItem);
        }
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                FC_Common.hotsellerrestaurant_status = hotSellersObjects.get(position).getRestaurant_status();
                // Picasso.get().load(hotSellersObjects.get(position).getRestaurant_logo()).into(holder.lm_imageDish);
                Picasso.get().load(hotSellersObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_restaurantName.setText(hotSellersObjects.get(position).getRestaurant_name());
               // holder.lt_cuisines.setText(hotSellersObjects.get(position).getCuisines());
                holder.lt_cuisines.setText(hotSellersObjects.get(position).getRestaurant_status());
                holder.lt_deliveryEstimation.setText(hotSellersObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(hotSellersObjects.get(position).getCurrency());
                holder.lt_costlimit.setText(hotSellersObjects.get(position).getCost_limit() + " For " + hotSellersObjects.get(position).getPerson_limit());

                holder.txt_ratings.setText(hotSellersObjects.get(position).getRating());
                Double doublestax = Double.parseDouble(holder.txt_ratings.getText().toString());
                holder.txt_ratings.setText(String.format("%.1f", doublestax));
                Double double_test=Double.parseDouble(holder.txt_ratings.getText().toString());
                Double double_one=1.0;
                Double double_two=2.0;
                Double double_three=3.0;
                Double double_four=4.0;
                Double double_five=5.0;

                if (double_test<=double_one){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate1));
                }
                else if (double_test<=double_two){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate2));
                    Log.d("fgujfghfg","2132132dfgdfg"+doublestax);
                }
                else if (double_test<=double_three){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate3));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_four){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate4));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_five){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate5));
                }
                else{
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_normal));
                }


                if (FC_Common.hotsellerrestaurant_status.equalsIgnoreCase("OPEN")) {
                    holder.txt_timeOut.setVisibility(View.GONE);
                } else if (FC_Common.hotsellerrestaurant_status.equalsIgnoreCase("CLOSED")) {
                    holder.txt_timeOut.setVisibility(View.VISIBLE);
                }
                holder.ll_main.setOnClickListener(view -> {
                    FC_Common.restaurantid=hotSellersObjects.get(position).getId();
                    Intent restaurant = new Intent(getContext(), FC_HotelDetailsActivity.class);
                    restaurant.putExtra("hotelid",FC_Common.restaurantid);
                    restaurant.putExtra("recent_search","0");
                    startActivity(restaurant);
                });
            } }
        @Override
        public int getItemCount() {
            return hotSellersObjects.size();
        }
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageDish;
            LinearLayout ll_content, ll_main,ll_rating;
            LoaderTextView lt_restaurantName, lt_cuisines, lt_deliveryEstimation, lt_costlimit, lt_currency;
            AC_Textview txt_ratings,txt_timeOut;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_rating = itemView.findViewById(R.id.ll_rating);
                ll_content = itemView.findViewById(R.id.ll_content);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                txt_timeOut = itemView.findViewById(R.id.txt_timeOut);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_cuisines = itemView.findViewById(R.id.lt_cuisines);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                ll_main = itemView.findViewById(R.id.ll_main);
            }}}
    //HotSeller Async Task End//
    //TopPicks Async Task Start//
    private void TopPicksList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_TOPPICKLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            Log.d("sdfsdfsdf","sdfsdf"+dataArray);
                            topPickForYouObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                TopPickForYouObject playerModel = new TopPickForYouObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRating(product.getString("rating"));
                                    playerModel.setDelivery_estimation(product.getString("delivery_estimation"));
                                    playerModel.setPerson_limit(product.getString("person_limit"));
                                    playerModel.setCost_limit(product.getString("cost_limit"));
                                    playerModel.setRestaurant_latitude(product.getString("restaurant_latitude"));
                                    playerModel.setRestaurant_longitude(product.getString("restaurant_longitude"));
                                    playerModel.setDistance(product.getString("distance"));
                                    playerModel.setCuisines(product.getString("cuisines"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setRestaurant_status(product.getString("restaurant_status"));
                                    playerModel.setDiscount(product.getString("discount"));
                                    playerModel.setDiscount_type(product.getString("discount_type"));
                                    topPickForYouObjects.add(playerModel);
                                    if (topPickForYouObjects != null) {
                                        lt_topPic.setText(R.string.top_pic);
                                        topPicksAdapter.visibleContentLayout();
                                    }
                                    else {

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();

                                } }
                        }
                        else {
                            lt_topPic.setText(R.string.noitem);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }, error -> {
            snackBar("Hotsellernew" + getResources().getString(R.string.reach));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("dish_id", FC_Common.typeVeg);
                params.put("price_from", FC_Common.filter_price_min_check);
                params.put("price_to", FC_Common.filter_price_max_check);
                params.put("rating", FC_Common.typePopularity);
                params.put("cuisines", FC_Common.cuisinecheck);
                Utils.log(getContext(),"params: "+params);
                Utils.log(getContext(),"FC_Common: "+FC_Common.filter_price_min_check);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void TopPicksRecycler() {
        topPicksAdapter = new TopPicksAdapter(topPickForYouObjects);
        rv_topPicks.setAdapter(topPicksAdapter);
        rv_topPicks.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
    private class TopPicksAdapter extends RecyclerView.Adapter<TopPicksAdapter.ViewHolder> {
        private final ArrayList<TopPickForYouObject> topPickForYouObjects;
        boolean visible;
        TopPicksAdapter(ArrayList<TopPickForYouObject> topPickForYouObjects) {
            this.topPickForYouObjects = topPickForYouObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_top_picks_item, parent, false);
            return new ViewHolder(listItem);
        }
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_mainLayout.setVisibility(View.VISIBLE);
                if (position % 2 == 1) {
                    holder.ll_mainLayout.setBackgroundColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.dash_toppick1));
                } else {
                    holder.ll_mainLayout.setBackgroundColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.dash_toppick2));
                }
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.lc_item.setVisibility(View.GONE);
                holder.img_item.setVisibility(View.VISIBLE);
                holder.lt_offer.setVisibility(View.VISIBLE);
                holder.img_item.setImageDrawable(getActivity().getDrawable(R.drawable.test_img_250));
                holder.ll_content.setVisibility(View.VISIBLE);
                FC_Common.toppickrestaurant_status = topPickForYouObjects.get(position).getRestaurant_status();
                FC_Common.toppickrestaurant_discount = topPickForYouObjects.get(position).getDiscount();
                FC_Common.toppickrestaurant_discounttype = topPickForYouObjects.get(position).getDiscount_type();
                // Picasso.get().load(topPickForYouObjects.get(position).getRestaurant_logo()).into(holder.img_item);
                Picasso.get().load(topPickForYouObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.img_item);
                holder.txt_restaurantName.setText(topPickForYouObjects.get(position).getRestaurant_name());
                holder.lt_deliveryEstimation.setText(topPickForYouObjects.get(position).getDelivery_estimation());
                holder.lt_offer.setText(topPickForYouObjects.get(position).getCost_limit() + " For " + hotSellersObjects.get(position).getPerson_limit());

                holder.txt_ratings.setText(topPickForYouObjects.get(position).getRating());
                Double doublestax = Double.parseDouble(holder.txt_ratings.getText().toString());
                holder.txt_ratings.setText(String.format("%.1f", doublestax));
                Double double_test=Double.parseDouble(holder.txt_ratings.getText().toString());
                Double double_one=1.0;
                Double double_two=2.0;
                Double double_three=3.0;
                Double double_four=4.0;
                Double double_five=5.0;

                if (double_test<=double_one){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate1));
                }
                else if (double_test<=double_two){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate2));
                    Log.d("fgujfghfg","2132132dfgdfg"+doublestax);
                }
                else if (double_test<=double_three){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate3));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_four){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate4));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_five){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate5));
                }
                else{
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_normal));
                }


                if (FC_Common.toppickrestaurant_status.equalsIgnoreCase("OPEN")) {
                    holder.img_timeOut.setVisibility(View.GONE);
                } else if (FC_Common.toppickrestaurant_status.equalsIgnoreCase("CLOSED")) {
                    holder.img_timeOut.setVisibility(View.VISIBLE);
                }
                if (FC_Common.toppickrestaurant_discounttype.equalsIgnoreCase("percentage")) {
                    holder.lt_offer.setText(topPickForYouObjects.get(position).getDiscount() + " % " + "OFF");
                } else if (FC_Common.toppickrestaurant_discounttype.equalsIgnoreCase("flat")) {
                    holder.lt_offer.setText("Flat " + topPickForYouObjects.get(position).getDiscount() + " % " + "OFF");
                }
                if (FC_Common.toppickrestaurant_discount.equalsIgnoreCase("")) {
                    holder.lt_offer.setText("NO OFFER");
                } else if (FC_Common.toppickrestaurant_discounttype.equalsIgnoreCase("")) {
                    holder.lt_offer.setText("NO OFFER");
                }
                holder.itemView.setOnClickListener(view -> {

                    FC_Common.toppickid=topPickForYouObjects.get(position).getId();
                    Intent toppick = new Intent(getActivity(), Fc_TopPickRestaurant.class);
                    toppick.putExtra("toppickid",FC_Common.toppickid);
                    startActivity(toppick);

                    /*Intent sharedintent = new Intent(getActivity(), FC_ItemActivity.class);
                    Pair[] pairs;
                    pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(holder.img_item, "transaction_itemImg");
                    ActivityOptions option = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        option = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                    }
                    startActivity(sharedintent, Objects.requireNonNull(option).toBundle());*/
                }); } }
        @Override
        public int getItemCount() { return topPickForYouObjects.size(); }
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
            LinearLayout ll_mainLayout, ll_content, ll_main,ll_rating;
            LoaderCircluarImageView lc_item;
            CircleImageView img_item,img_timeOut;
            LoaderTextView lt_offer, lt_deliveryEstimation;
            AC_Textview txt_restaurantName, txt_ratings;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_rating = itemView.findViewById(R.id.ll_rating);
                img_timeOut = itemView.findViewById(R.id.img_timeOut);
                ll_mainLayout = itemView.findViewById(R.id.ll_mainLayout);
                ll_content = itemView.findViewById(R.id.ll_content);
                lc_item = itemView.findViewById(R.id.lc_item);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                img_item = itemView.findViewById(R.id.img_item);
                lt_offer = itemView.findViewById(R.id.lt_offer);
                txt_restaurantName = itemView.findViewById(R.id.txt_restaurantName);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                ll_main = itemView.findViewById(R.id.ll_main);
            }}}
    //TopPicks Async Task End//
    //CardOffer Async Task Start//
    private void CardoffersList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_CARDOFFERLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            cardOffersObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                CardOffersObject playerModel = new CardOffersObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setOffer_code(product.getString("offer_code"));
                                    playerModel.setDiscount(product.getString("discount"));
                                    playerModel.setDiscount_type(product.getString("discount_type"));
                                    playerModel.setDescription(product.getString("description"));
                                    playerModel.setEnable_limit(product.getString("enable_limit"));
                                    playerModel.setOffer_upto(product.getString("offer_upto"));
                                    playerModel.setOffer_upto(product.getString("offer_upto"));
                                    playerModel.setUse_count(product.getString("use_count"));
                                    playerModel.setCards(product.getString("cards"));
                                    cardOffersObjects.add(playerModel);
                                    if (cardOffersObjects != null) {
                                        lt_cardOffers.setVisibility(View.GONE);
                                        ll_cardOffers.setVisibility(View.VISIBLE);
                                        cardOffersAdapter.visibleContentLayout();
                                    } else {
                                      //Utils.toast(context,getResources().getString(R.string.reach));
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    snackBar(getResources().getString(R.string.reach));
                                } }
                        } else {
                            snackBar(getResources().getString(R.string.reach));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("" + getResources().getString(R.string.reach));

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        } }
    private void cardoffersRecycler() {
        cardOffersAdapter = new CardOffersAdapter(cardOffersObjects);
        rv_cardOffers.setAdapter(cardOffersAdapter);
        rv_cardOffers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
    public class CardOffersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        boolean visible;
        private List<CardOffersObject> cardOffersObjects;
        CardOffersAdapter(List<CardOffersObject> cardOffersObjects) {
            this.cardOffersObjects = cardOffersObjects;
        }
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardoffer_item_header, parent, false);
                return new HeaderViewHolder(layoutView);
            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardoffer_item, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
            if (visible) {
                lt_cardOffers.setVisibility(View.GONE);
                ll_cardOffers.setVisibility(View.VISIBLE);

                if (holder instanceof HeaderViewHolder) {
                    ((HeaderViewHolder) holder).img_cardOffersHeader.setImageDrawable(getResources().getDrawable(R.drawable.offers1));
                } else if (holder instanceof ItemViewHolder) {
                    CardOffersObject mObject = cardOffersObjects.get(position - 1);
                    ((ItemViewHolder) holder).txt_offer.setText(mObject.getDiscount_type()
                            + " " + mObject.getDiscount() + " % " + "offer");
                    ((ItemViewHolder) holder).txt_cards.setText(mObject.getCards());
                    ((ItemViewHolder) holder).vv_offer.setText(mObject.getOffer_code());
                    ((ItemViewHolder) holder).itemView.setOnClickListener(view -> {

                        Intent paymentOffer = new Intent(getActivity(),FC_OffersActivity.class);
                        paymentOffer.putExtra("id","2");
                        startActivity(paymentOffer);
                    });
                }
            } else {
                lt_cardOffers.setVisibility(View.VISIBLE);
                ll_cardOffers.setVisibility(View.GONE);
            }
        }
        @Override
        public int getItemCount() {
            return cardOffersObjects.size() + 1;
        }
        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }
        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }
        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }
    //CardOffer Async Task End//
    //AllRestaurant Async Task Start//
    private void AllRestaurantList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ALLRESTAURANTLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            allRestaurantObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                AllRestaurantObject playerModel = new AllRestaurantObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRating(product.getString("rating"));
                                    playerModel.setDelivery_estimation(product.getString("delivery_estimation"));
                                    playerModel.setPerson_limit(product.getString("person_limit"));
                                    playerModel.setCost_limit(product.getString("cost_limit"));
                                    playerModel.setRestaurant_latitude(product.getString("restaurant_latitude"));
                                    playerModel.setRestaurant_longitude(product.getString("restaurant_longitude"));
                                    playerModel.setDistance(product.getString("distance"));
                                    playerModel.setCuisines(product.getString("cuisines"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setRestaurant_status(product.getString("restaurant_status"));
                                    allRestaurantObjects.add(playerModel);
                                    if (allRestaurantObjects != null) {
                                        ll_seeAllRestaurant.setVisibility(View.VISIBLE);
                                        lt_allRestaurant.setVisibility(View.GONE);
                                        ll_allRestaurant.setVisibility(View.VISIBLE);
                                        allRestaurantAdapter.visibleContentLayout();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();

                                }}
                        } else {
                            lt_allRestaurant.setVisibility(View.VISIBLE);
                            lt_allRestaurant.setText(R.string.noitem);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("dish_id", FC_Common.typeVeg);
                params.put("price_from", FC_Common.filter_price_min_check);
                params.put("price_to", FC_Common.filter_price_max_check);
                params.put("rating", FC_Common.typePopularity);
                params.put("cuisines", FC_Common.cuisinecheck);
                Utils.log(getContext(),"params: "+params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                Log.d("dfgfdgdfgdf","dfgdfgdf"+params);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void AllRestaurantRecycler() {
        allRestaurantAdapter = new AllRestaurantAdapter(allRestaurantObjects);
        rv_allRestaurants.setAdapter(allRestaurantAdapter);
        rv_allRestaurants.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }
    private class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.ViewHolder> {
        private final ArrayList<AllRestaurantObject> allRestaurantObjects;
        boolean visible;
        AllRestaurantAdapter( ArrayList<AllRestaurantObject> allRestaurantObjects) {
            this.allRestaurantObjects = allRestaurantObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_all_restaurant, parent, false);
            return new ViewHolder(listItem);
        }
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                FC_Common.Allrestaurant_status = allRestaurantObjects.get(position).getRestaurant_status();
                //  Picasso.get().load(allRestaurantObjects.get(position).getRestaurant_logo()).into(holder.lm_imageDish);
                Picasso.get().load(allRestaurantObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_restaurantName.setText(allRestaurantObjects.get(position).getRestaurant_name());
                holder.txt_cuisines.setText(allRestaurantObjects.get(position).getCuisines());
                holder.lt_deliveryEstimation.setText(allRestaurantObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(allRestaurantObjects.get(position).getCurrency());
                holder.lt_costlimit.setText(allRestaurantObjects.get(position).getCost_limit() + " For " + allRestaurantObjects.get(position).getPerson_limit());

                holder.txt_ratings.setText(allRestaurantObjects.get(position).getRating());
                Double doublestax = Double.parseDouble(holder.txt_ratings.getText().toString());
                holder.txt_ratings.setText(String.format("%.1f", doublestax));

                Double double_test=Double.parseDouble(holder.txt_ratings.getText().toString());
                Double double_one=1.0;
                Double double_two=2.0;
                Double double_three=3.0;
                Double double_four=4.0;
                Double double_five=5.0;

                if (double_test<=double_one){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate1));
                }
                else if (double_test<=double_two){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate2));
                    Log.d("fgujfghfg","2132132dfgdfg"+doublestax);
                }
                else if (double_test<=double_three){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate3));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_four){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate4));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_five){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate5));
                }
                else{
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_normal));
                }


                if (FC_Common.Allrestaurant_status.equalsIgnoreCase("OPEN")) {
                    holder.txt_timeOut.setVisibility(View.GONE);
                } else if (FC_Common.Allrestaurant_status.equalsIgnoreCase("CLOSED")) {
                    holder.txt_timeOut.setVisibility(View.VISIBLE);
                }
                holder.ll_main.setOnClickListener(view -> {
                    FC_Common.restaurantid=allRestaurantObjects.get(position).getId();
                    Intent restaurant = new Intent(getContext(), FC_HotelDetailsActivity.class);
                    restaurant.putExtra("hotelid",FC_Common.restaurantid);
                    restaurant.putExtra("recent_search","0");
                    startActivity(restaurant);
                });
            } }
        @Override
        public int getItemCount() {
            return allRestaurantObjects.size();
        }
        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
        public void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageDish;
            LoaderTextView lt_restaurantName, lt_deliveryEstimation, lt_currency, lt_costlimit;
            AC_Textview txt_cuisines, txt_ratings,txt_timeOut;
            LinearLayout ll_content, ll_main,ll_rating;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_rating = itemView.findViewById(R.id.ll_rating);
                txt_timeOut = itemView.findViewById(R.id.txt_timeOut);
                ll_main = itemView.findViewById(R.id.ll_main);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                txt_cuisines = itemView.findViewById(R.id.txt_cuisines);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                ll_content = itemView.findViewById(R.id.ll_content);
            }}}
    //AllRestaurant Async Task End//
    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(lin_frame, value, Snackbar.LENGTH_LONG)
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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void checkTypes(int checktypes) {
        //FC_Common.typeVeg=typeVeg;
        if (checktypes ==1)
        {
            Log.d("dfgbfdgfdgd","FC_Common.typeVeg"+FC_Common.typeVeg);
            Log.d("dfgbfdgfdgd","dfgdfg"+ checktypes);
            //  init();
        }
        Log.d("dfgbfdgfdgd","checkTypes"+ checktypes);

    }

    private void FindViewByIdBottomDialog(View view) {

        rv_search=view.findViewById(R.id.rv_search);
        txt_emptyview=view.findViewById(R.id.txt_emptyview);
        AC_Textview txt_orderHeader = view.findViewById(R.id.txt_orderHeader);
        LinearLayout ll_addNewAddress = view.findViewById(R.id.ll_addNewAddress);
        txt_orderHeader.setText(R.string.your_orders);
        ll_addNewAddress.setVisibility(View.GONE);

        OrderObject object = new OrderObject();
        object.setD_images("");
        object.setD_info("");
        orderObjects.add(object);

    }

    private void Orderlist() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_ORDERLIST,
                response -> {
                    Utils.log(context, "Location:" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            //LocationObject.clear();
                            txt_emptyview.setVisibility(View.GONE);
                            rv_search.setVisibility(View.VISIBLE);
                            JSONArray dataArray = obj.getJSONArray("data");
                            orderObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                OrderObject playerModel = new OrderObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setStatus(product.getString("status"));
                                    // playerModel.setUser_id(product.getString("user_id"));
                                    Log.d("dfhdfgfdg","dfgdfgfd"+product.getString("restaurant_name"));
                                    orderObjects.add(playerModel);

                                    if (orderObjects != null) {
                                        orderAdapters.visibleContentLayout();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("cvncvbcvbvc", "dfgfdgfd" + ex);
                                }
                            }

                        } else {
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_search.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //snackBar(String.valueOf(e));
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("cvncvbcvbvc", "dfgfdgdf" + e);
                        txt_emptyview.setVisibility(View.VISIBLE);
                        rv_search.setVisibility(View.GONE);
                    }
                }, error -> {
            //displaying the error in toast if occurrs
            String error_value = String.valueOf(error);
            snackBar(error_value);
            txt_emptyview.setVisibility(View.VISIBLE);
            rv_search.setVisibility(View.GONE);
            Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
            Log.d("cvncvbcvbvc", "d324dffgfdgdf" + error);
        }) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                // Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
        requestQueue.addRequestFinishedListener(request -> {
            // Utils.stopProgressBar();

        });

    }

    private void OrderRecycler() {


        orderAdapters = new OrderAdapter( orderObjects);
        rv_search.setAdapter(orderAdapters);
        rv_search.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

        boolean visible;
        private final ArrayList<OrderObject> orderObjects;
        OrderAdapter( ArrayList<OrderObject> orderObjects) {
            this.orderObjects = orderObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_order_list, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            if (visible) {
                Log.d("dfgfdgdfg","dfgdfg"+orderObjects.get(position).getRestaurant_name());
                holder.lt_orderRestaurant.setText(orderObjects.get(position).getRestaurant_name());
                holder.lt_orderStatus.setText(orderObjects.get(position).getStatus());
                if (orderObjects.get(position).getStatus().equalsIgnoreCase("ORDERED")) {
                    holder.lt_orderStatus.setTextColor(getResources().getColor(R.color.ordered));
                } else if (orderObjects.get(position).getStatus().equalsIgnoreCase("ACCEPTED")) {
                    holder.lt_orderStatus.setTextColor(getResources().getColor(R.color.approved));
                } else if (orderObjects.get(position).getStatus().equalsIgnoreCase("PICKEDUP")) {
                    holder.lt_orderStatus.setTextColor(getResources().getColor(R.color.pending));
                }else if (orderObjects.get(position).getStatus().equalsIgnoreCase("COMPLETED")) {
                    holder.lt_orderStatus.setTextColor(getResources().getColor(R.color.delivered));
                }else if (orderObjects.get(position).getStatus().equalsIgnoreCase("CANCELLED")) {
                    holder.lt_orderStatus.setTextColor(getResources().getColor(R.color.rejected));
                }

                holder.ll_content.setOnClickListener(v -> {
                    OrderObject item = orderObjects.get(position);
                    FC_Common.order_id = item.getId();

                    orderdialog.dismiss();
                    Intent intent = new Intent(context, FC_OrderPickedUpActivity.class);
                    intent.putExtra("order_id",FC_Common.order_id);
                    startActivity(intent);

                    Utils.log(context, "dfsdfdsfs" + FC_Common.order_id);

                });
            }
        }

        @Override
        public int getItemCount() {
            return orderObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        void visibleContentLayout() {
            visible = true;

            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderTextView lt_orderRestaurant, lt_orderStatus;
            LinearLayout ll_content;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                lt_orderRestaurant = itemView.findViewById(R.id.lt_orderRestaurant);
                lt_orderStatus = itemView.findViewById(R.id.lt_orderStatus);
                ll_content = itemView.findViewById(R.id.ll_content);
            }
        }
    }
}