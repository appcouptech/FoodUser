package food.user.demand.FCFragment.FCDashboardFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import food.user.demand.FCActivity.FCCartActivity.FC_CartActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCHelp.Fc_HelpActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCManageAddressActivity.FC_ManageAddressActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCOfferActivity.FC_PendingOrderActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FC_FavoritesActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FC_OffersActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FC_OrderHistoryActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FC_ReferralsActivity;
import food.user.demand.FCPojo.FCAccountFragmentObject.PastOrderObject;
import food.user.demand.FCPojo.FCHotelDetailsActivityObject.ItemViewOrderObject;
import food.user.demand.FCUtils.Loader.LoaderCircluarImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_BoldTextview;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

import static android.app.Activity.RESULT_OK;
import static food.user.demand.FCUtils.FilePath.getDataColumn;

/**
 * A simple {@link Fragment} subclass.
 * created by kamal 25-1-2020
 */
public class FC_AccountFragment extends Fragment implements View.OnClickListener {
    private BottomSheetDialog profiledialog,ratingdialog;
    private DialogPlus itemdialog;
    private Context context;
    private Uri imageUri;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int STORAGE_CODE = 1000;
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 1234;
    private static int CAMERA_REQUEST = 0;
    private static int GALLERY_PICTURE = 1;
    private Snackbar bar;
    private int logout = 0;
    private Bitmap bitmap;
    private CircleImageView img_profile,img_male,img_female;
    private PastOrderAdapter pastOrderAdapter;
    private ItemOrderAdapter itemOrderAdapters;
    private RecyclerView rv_pastOrders;
    private ListView lst_itemview;
    private ArrayList<PastOrderObject> pastOrderObjects;
   // private ArrayList<ItemViewOrderObject> itemViewOrderObjects ;
   private List<ItemViewOrderObject> Listvalues = new ArrayList<>();
    private ItemViewOrderObject ListDatas;
    private AC_Textview txt_emptyview;
    private AC_Textview txt_myAccount;
    private AC_Textview txt_saveBtn;
    private AC_Textview edt_mobile;
    private AC_Textview txt_submit;
    private AC_Edittext edt_name;
    private AC_Edittext edt_email;
    private AC_Edittext edt_commentres,edt_commentdriver;
    private RatingBar ratingBar_restaurant,ratingBar_driver;
    private LoaderTextView lt_cuisine,lt_restaurantname,txt_mobileEmail,txt_userName,lt_drivername;
    private LinearLayout ll_main;
    private LinearLayout ll_myAccountExpandable;
    private LinearLayout ll_myAccount;
    private ImageView img_myAccountRight;
    private ImageView img_myAccountTop,img_profile_new;
    private Handler handler;
    private LoaderCircluarImageView lc_imgdriver,lc_imgRestaurant;
    private int counter = 0;

    private AlertDialog alertDialog;
    private LogoutAdmin logoutAdminListener;

    public FC_AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fc__account, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();
        FindViewById(view);


        FC_User user = FC_SharedPrefManager.getInstance(getActivity()).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.mobile = String.valueOf(user.getmobile());
        FC_Common.gender = String.valueOf(user.getgender());
        Log.d("fghfdgfd","cgfdg"+FC_Common.gender);



        pastOrderAdapter = new PastOrderAdapter( pastOrderObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_pastOrders.setLayoutManager(itemViewLLres);
        rv_pastOrders.setAdapter(pastOrderAdapter);



        txt_userName.setText(FC_Common.name);
        txt_mobileEmail.setText(FC_Common.mobile+" - "+FC_Common.email);
        PastOrderList();

        if (getActivity() instanceof LogoutAdmin)
            logoutAdminListener = (LogoutAdmin) getActivity();

    }

    public interface LogoutAdmin {
        void onLogOut(int logout);
    }

    private void FindViewById(View view) {

        AC_Textview txt_help = view.findViewById(R.id.txt_help);
        AC_Textview txt_logout = view.findViewById(R.id.txt_logout);
        ll_main = view.findViewById(R.id.ll_main);
        txt_emptyview = view.findViewById(R.id.txt_emptyview);
        img_profile_new = view.findViewById(R.id.img_profile_new);
        ImageView img_profileEdit = view.findViewById(R.id.img_profileEdit);
        LinearLayout ll_favorites = view.findViewById(R.id.ll_favorites);
        LinearLayout ll_referrals = view.findViewById(R.id.ll_referrals);
        LinearLayout ll_offers = view.findViewById(R.id.ll_offers);
        ll_myAccount = view.findViewById(R.id.ll_myAccount);
        LinearLayout ll_payment = view.findViewById(R.id.ll_payment);
        LinearLayout ll_manageAddress = view.findViewById(R.id.ll_manageAddress);
        LinearLayout ll_orderHistory = view.findViewById(R.id.ll_orderHistory);
        LinearLayout ll_pendingOrders = view.findViewById(R.id.ll_pendingOrders);
        txt_myAccount = view.findViewById(R.id.txt_myAccount);
        txt_userName = view.findViewById(R.id.txt_userName);
        txt_mobileEmail = view.findViewById(R.id.txt_mobileEmail);
        rv_pastOrders = view.findViewById(R.id.rv_pastOrders);
        img_myAccountRight = view.findViewById(R.id.img_myAccountRight);
        img_myAccountTop = view.findViewById(R.id.img_myAccountTop);
        ImageView img_orderHistory = view.findViewById(R.id.img_orderHistory);
        ImageView img_favorites = view.findViewById(R.id.img_favorites);
        ImageView img_manageAddress = view.findViewById(R.id.img_manageAddress);
        ImageView img_payment = view.findViewById(R.id.img_payment);
        ImageView img_referrals = view.findViewById(R.id.img_referrals);
        ImageView img_offers = view.findViewById(R.id.img_offers);
        ll_myAccountExpandable = view.findViewById(R.id.ll_myAccountExpandable);

        ll_pendingOrders.setOnClickListener(this);
        ll_orderHistory.setOnClickListener(this);
        ll_manageAddress.setOnClickListener(this);
        ll_favorites.setOnClickListener(this);
        ll_referrals.setOnClickListener(this);
        ll_offers.setOnClickListener(this);
        ll_myAccount.setOnClickListener(this);
        ll_payment.setOnClickListener(this);
        txt_myAccount.setOnClickListener(this);
        img_myAccountRight.setOnClickListener(this);
        img_orderHistory.setOnClickListener(this);
        img_favorites.setOnClickListener(this);
        img_manageAddress.setOnClickListener(this);
        img_offers.setOnClickListener(this);
        img_referrals.setOnClickListener(this);
        img_payment.setOnClickListener(this);
        img_profileEdit.setOnClickListener(this);
        txt_help.setOnClickListener(this);
        txt_logout.setOnClickListener(this);

        pastOrderObjects = new ArrayList<>();
        PastOrderObject fragmentObject = new PastOrderObject();
        fragmentObject.setD_images("");
        fragmentObject.setD_info("");
        pastOrderObjects.add(fragmentObject);



        if (FC_Common.gender.equalsIgnoreCase("Female"))
        {

            Picasso.get().load(R.drawable.female)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(img_profile_new);
        }
        else {

            Picasso.get().load(R.drawable.male)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(img_profile_new);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.txt_myAccount:

                txt_myAccount.setOnClickListener(view13 -> {

                    if (ll_myAccountExpandable.getVisibility() == View.VISIBLE) {

                        Log.d("dgdfgdfg","dfgdfgfd");
                        img_myAccountRight.setVisibility(View.VISIBLE);
                        img_myAccountTop.setVisibility(View.GONE);
                        ll_myAccountExpandable.setVisibility(View.GONE);
                        // Its visible
                    } else {
                        // Either gone or invisible
                        Log.d("dgdfgdfg","123213dfgdfgfd");
                        img_myAccountRight.setVisibility(View.GONE);
                        img_myAccountTop.setVisibility(View.VISIBLE);
                        ll_myAccountExpandable.setVisibility(View.VISIBLE);

                        img_myAccountTop.setOnClickListener(view131 -> {

                            ll_myAccountExpandable.setVisibility(View.GONE);
                            img_myAccountRight.setVisibility(View.VISIBLE);
                            img_myAccountTop.setVisibility(View.GONE);

                        });
                    }                    });

                break;

            case R.id.ll_myAccount:

                ll_myAccount.setOnClickListener(view14 -> {

                    if (ll_myAccountExpandable.getVisibility() == View.VISIBLE) {

                        Log.d("dgdfgdfg","dfgdfgfd");
                        img_myAccountRight.setVisibility(View.VISIBLE);
                        img_myAccountTop.setVisibility(View.GONE);
                        ll_myAccountExpandable.setVisibility(View.GONE);
                        // Its visible
                    } else {
                        // Either gone or invisible
                        Log.d("dgdfgdfg","123213dfgdfgfd");
                        img_myAccountRight.setVisibility(View.GONE);
                        img_myAccountTop.setVisibility(View.VISIBLE);
                        ll_myAccountExpandable.setVisibility(View.VISIBLE);

                        img_myAccountTop.setOnClickListener(view141 -> {

                            ll_myAccountExpandable.setVisibility(View.GONE);
                            img_myAccountRight.setVisibility(View.VISIBLE);
                            img_myAccountTop.setVisibility(View.GONE);

                        });
                    }                    });

                break;
            case R.id.img_myAccountRight:

                img_myAccountRight.setOnClickListener(view12 -> {

                    img_myAccountRight.setVisibility(View.GONE);
                    img_myAccountTop.setVisibility(View.VISIBLE);
                    ll_myAccountExpandable.setVisibility(View.VISIBLE);

                    img_myAccountTop.setOnClickListener(view121 -> {

                        ll_myAccountExpandable.setVisibility(View.GONE);
                        img_myAccountRight.setVisibility(View.VISIBLE);
                        img_myAccountTop.setVisibility(View.GONE);

                    });
                });

                break;


            case R.id.ll_orderHistory:

                Intent ll_orderHistory = new Intent(getActivity(), FC_OrderHistoryActivity.class);
                startActivity(ll_orderHistory);
                break;
            case R.id.ll_pendingOrders:

                Intent ll_pendingOrders = new Intent(getActivity(), FC_PendingOrderActivity.class);
                startActivity(ll_pendingOrders);
                break;



            case R.id.img_orderHistory:

                Intent orderHistory = new Intent(getActivity(), FC_OrderHistoryActivity.class);
                startActivity(orderHistory);
                break;

            case R.id.ll_manageAddress:

                Intent ll_manageAddress =  new Intent(getActivity(), FC_ManageAddressActivity.class);
                startActivity(ll_manageAddress);
                break;

            case R.id.img_manageAddress:

                Intent manageAddress =  new Intent(getActivity(), FC_ManageAddressActivity.class);
                startActivity(manageAddress);
                break;


            case R.id.ll_payment:

                /*Intent ll_payment = new Intent(getActivity(), FC_FavoritesActivity.class);
                startActivity(ll_payment);*/

                break;

            case R.id.img_payment:

               /* Intent img_payment = new Intent(getActivity(), FC_FavoritesActivity.class);
                startActivity(img_payment);*/
                break;
            case R.id.txt_help:

                Intent help = new Intent(getActivity(), Fc_HelpActivity.class);
                startActivity(help);
                break;
            case R.id.txt_logout:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                LayoutInflater inflater = getActivity().getLayoutInflater();
                @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_exit, null);
                AC_Textview txt_message =  dialogView.findViewById(R.id.txt_alertMessage);
                AC_BoldTextview txt_no = dialogView.findViewById(R.id.txt_no);
                AC_BoldTextview txt_yes = dialogView.findViewById(R.id.txt_yes);
                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                txt_message.setText(getActivity().getResources().getString(R.string.conform_logout));

                txt_no.setOnClickListener(v -> alertDialog.dismiss());

                txt_yes.setOnClickListener(v -> {

                    Logout();

                });

                alertDialog.show();

                break;



            case R.id.img_profileEdit:

                @SuppressLint("InflateParams")
                View view1 = getLayoutInflater().inflate(R.layout.fc_intro_profile, null);
                FindViewByIdBottomDialog(view1);
                if (edt_name.getText().toString().trim().equalsIgnoreCase("")&&
                        edt_email.getText().toString().trim().equalsIgnoreCase("")) {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                }
                else {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                }
                edt_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (edt_name.getText().toString().trim().equalsIgnoreCase("")||
                                edt_email.getText().toString().trim().equalsIgnoreCase("")) {
                            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                        }
                        else if (!edt_email.getText().toString().trim().contains("@") || !edt_email.getText().toString().trim().contains(".com")) {
                            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                            //snackBar(value);
                        }
                        else {
                            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                        }

                    }
                });
                edt_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (edt_name.getText().toString().trim().equalsIgnoreCase("")||
                                edt_email.getText().toString().trim().equalsIgnoreCase("")) {
                            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                        }

                        else if (!edt_email.getText().toString().trim().contains("@") || !edt_email.getText().toString().trim().contains(".com")) {
                            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                            //snackBar(value);
                        }
                        else {
                            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                        }

                    }
                });
                UserDetails();
                profiledialog = new BottomSheetDialog(context);
                profiledialog.setContentView(view1);
                profiledialog.show();

                break;

            case R.id.txt_saveBtn:

                Log.d("sdfsdfsdf","sdfsdf"+FC_Common.usergender);
                FC_Common.updateedtemail=edt_email.getText().toString();

                if (edt_name.getText().toString().trim().equalsIgnoreCase("")) {
                    String value="Name cannot be empty";
                    snackBar(value);
                }
                else  if (edt_mobile.getText().toString().trim().equalsIgnoreCase("")) {
                    String value="Mobile Number cannot be empty";
                    snackBar(value);
                }
                else if (!FC_Common.updateedtemail.contains("@") || !FC_Common.updateedtemail.contains(".com")) {
                    String value="Invalid Email";
                    snackBar(value);
                    //snackBar(value);
                }
                else  if (FC_Common.usergender.equalsIgnoreCase("")) {
                    String value="Choose Gender";
                    snackBar(value);
                }
                else {
                    Log.d("sdfsdfsdf","gdfgdfgdfgdfgfd");
                    FC_Common.username=edt_name.getText().toString().trim();
                    FC_Common.usermobile=edt_mobile.getText().toString().trim();
                    FC_Common.useremail=edt_email.getText().toString().trim();

                    DeleteMenu();
                }
                break;
            case R.id.img_profile:
                startDialog();
                break;
            case R.id.img_male:
                Log.d("dfhfddfgdf","sdgfsdfsd");
                //GetUserDetails();
                img_male.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                img_female.setBackgroundColor(getResources().getColor(R.color.white));
                FC_Common.usergender="Male";
                break;
            case R.id.img_female:
                img_female.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                img_male.setBackgroundColor(getResources().getColor(R.color.white));
                FC_Common.usergender="Female";
                break;

            case R.id.ll_favorites:

                Intent ll_favorites = new Intent(getActivity(), FC_FavoritesActivity.class);
                startActivity(ll_favorites);

                break;

            case R.id.img_favorites:

                Intent favorites = new Intent(getActivity(), FC_FavoritesActivity.class);
                startActivity(favorites);

                break;


            case R.id.ll_referrals:

                Intent ll_referrals = new Intent(getActivity(), FC_ReferralsActivity.class);
                startActivity(ll_referrals);

                break;
            case R.id.img_referrals:

                Intent img_referrals = new Intent(getActivity(), FC_ReferralsActivity.class);
                startActivity(img_referrals);

                break;

            case R.id.ll_offers :

                Intent ll_offers = new Intent(getActivity(), FC_OffersActivity.class);
                startActivity(ll_offers);

                break;

            case R.id.img_offers :

                Intent offers = new Intent(getActivity(), FC_OffersActivity.class);
                startActivity(offers);
                break;
        }
    }
    //Pastorders Async Task Start//
    private void PastOrderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_PASTORDER,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("dfgdfgfdgdf","fdgdfgv"+FC_URL.URL_PASTORDER);
                        if (obj.optString("success").equals("1")) {
                            txt_emptyview.setVisibility(View.GONE);
                            rv_pastOrders.setVisibility(View.VISIBLE);
                            JSONArray dataArray = obj.getJSONArray("data");
                            pastOrderObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                PastOrderObject playerModel = new PastOrderObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setItem(product.getString("item"));
                                    playerModel.setTotal(product.getString("total"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setCuisine_id(product.getString("cuisine_id"));
                                    playerModel.setCreated_at(product.getString("created_at"));

                                    pastOrderObjects.add(playerModel);
                                    if (pastOrderObjects != null) {

                                        pastOrderAdapter.visibleContentLayout();
                                    }
                                    else {
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        rv_pastOrders.setVisibility(View.GONE);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    snackBar(getResources().getString(R.string.reach));
                                   /* final int counter_pastorder = counter++;
                                    if (counter_pastorder < 5) {
                                        PastOrderList();
                                    }*/
                                }}
                        } else {

                           /* final int counter_pastorder = counter++;
                            if (counter_pastorder < 5) {
                                PastOrderList();
                            }*/
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_pastOrders.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                      /*  final int counter_pastorder = counter++;
                        if (counter_pastorder < 5) {
                            PastOrderList();
                        }*/
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar(getResources().getString(R.string.reach));
            /*final int counter_pastorder = counter++;
            if (counter_pastorder < 5) {
                PastOrderList();
            }*/
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                Log.d("sdfsdfsdfsdf","sdfsdfsd"+params);
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
    private class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.ViewHolder> {
        private final ArrayList<PastOrderObject> pastOrderObjects;
        boolean visible;

        PastOrderAdapter( ArrayList<PastOrderObject> pastOrderObjects) {
            this.pastOrderObjects = pastOrderObjects;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_past_order_items, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.txt_reOrder.setVisibility(View.VISIBLE);
                holder.txt_rating.setVisibility(View.VISIBLE);
                holder.img_detailItem.setVisibility(View.VISIBLE);
                Log.d("gfdgdfg","dfgdfgdfg"+pastOrderObjects.get(position).getRestaurant_name());
                Picasso.get().load(pastOrderObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lc_img);
                holder.lt_restaurantName.setText(pastOrderObjects.get(position).getRestaurant_name());
                holder.lt_cuisine.setText(pastOrderObjects.get(position).getCuisine_id());
                holder.lt_itemName.setText(pastOrderObjects.get(position).getItem());
                holder.lt_currency.setText(pastOrderObjects.get(position).getCurrency());
                holder.lt_itemTotal.setText(pastOrderObjects.get(position).getTotal());
                holder.lt_datetime.setText(pastOrderObjects.get(position).getCreated_at());


                holder.img_detailItem.setOnClickListener(v -> {
                    FC_Common.order_id=pastOrderObjects.get(position).getId();
                    ItemDialog();
                });

                holder.txt_reOrder.setOnClickListener(v -> {
                    FC_Common.order_id=pastOrderObjects.get(position).getId();
                    UpdateCart();
                });
                holder.txt_rating.setOnClickListener(v -> {
                    @SuppressLint("InflateParams")
                    View view1 = getLayoutInflater().inflate(R.layout.layout_rating, null);
                    FC_Common.order_id=pastOrderObjects.get(position).getId();
                    FindViewByIdBottomDialogRating(view1);
                    detailsview();
                    ratingdialog = new BottomSheetDialog(context);
                    ratingdialog.setContentView(view1);
                    ratingdialog.show();
                });

                FC_Common.date_time = pastOrderObjects.get(position).getCreated_at();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    Date oneWayTripDate = input.parse(FC_Common.date_time);
                    assert oneWayTripDate != null;
                    String date = output.format(oneWayTripDate);
                    holder.lt_datetime.setText(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public int getItemCount() {
            return pastOrderObjects.size();
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
            LoaderCircluarImageView lc_img;
            AC_Textview txt_reOrder,txt_rating;
            ImageView img_detailItem;
            LoaderTextView lt_restaurantName,lt_cuisine,lt_itemName,lt_currency,lt_itemTotal,lt_datetime;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_detailItem = itemView.findViewById(R.id.img_detailItem);
                lc_img = itemView.findViewById(R.id.lc_img);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                lt_itemName = itemView.findViewById(R.id.lt_itemName);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_itemTotal = itemView.findViewById(R.id.lt_itemTotal);
                lt_datetime = itemView.findViewById(R.id.lt_datetime);
                txt_reOrder = itemView.findViewById(R.id.txt_reOrder);
                txt_rating = itemView.findViewById(R.id.txt_rating);

            }
        }
    }
    //Pastorders Async Task End//

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ItemDialog() {
        itemdialog = DialogPlus.newDialog(Objects.requireNonNull(getActivity()))
                .setContentHolder(new ViewHolder(R.layout.dialogitemview))
                .setCancelable(true)
                .setGravity( Gravity.CENTER)
                .setOnItemClickListener((dialogPlus, item, view, position) -> {
                }).setExpanded(false).create();
        itemdialog.show();
        ImageView imgcancel = (ImageView) itemdialog.findViewById(R.id.imgcancel);

        assert imgcancel != null;
        imgcancel.setOnClickListener(v -> itemdialog.dismiss());
        lst_itemview = (ListView) itemdialog.findViewById(R.id.lst_itemview);

        ItemDialogAsync();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ItemDialogAsync() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest( Request.Method.POST, FC_URL.URL_ITEMDETAIL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray arr = jsonObject.getJSONArray("data");
                        Log.d("sdgsdfsd","dfsd"+FC_URL.URL_ITEMDETAIL);
                        Listvalues.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject product = arr.getJSONObject(i);
                            ListDatas = new ItemViewOrderObject();
                            ListDatas.setCuisine_name(product.getString("product_name"));
                            ListDatas.setQty(product.getString("qty"));
                            Listvalues.add(ListDatas);
                        }
                        if (Listvalues != null && lst_itemview != null) {

                            Utils.stopProgressBar();
                            itemOrderAdapters = new ItemOrderAdapter(getContext(), Listvalues);
                            lst_itemview.setAdapter(itemOrderAdapters);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("dgsdfsd","dfsdf"+e);
                        Log.d("dgsdfsd","dfsdf"+FC_URL.URL_ITEMDETAIL);
                    } }, error -> Utils.toast(getActivity(),"" + R.string.reach)){

                @Override
                protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
                @Override
                public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:12" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        // request queue
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Utils.log(context,"dgsdgsdfgsd"+ex);
        }
    }
    public static class ItemOrderAdapter extends ArrayAdapter<ItemViewOrderObject> {
        private List<ItemViewOrderObject> _list;
        @SuppressLint("StaticFieldLeak")
        LinearLayout lin_click_event;
        private ArrayList<ItemViewOrderObject> arraylist;
        ItemOrderAdapter(Context context, List<ItemViewOrderObject> lst) {
            super(context, 0, lst);
            _list = lst;
            this.arraylist = new ArrayList<>();
            this.arraylist.addAll(_list);
        }
        @NonNull
        @Override
        public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("ViewHolder") final View view = inflater.inflate( R.layout.layout_itemview, parent, false);
            try {
                ItemViewOrderObject vidItem = _list.get(position);
               LoaderTextView lt_itemName =view.findViewById(R.id.lt_itemName);
               LoaderTextView lt_qty =view.findViewById(R.id.lt_qty);

                lt_itemName.setText(vidItem.getCuisine_name());
                lt_qty.setText(vidItem.getQty());
               } catch (Exception pre) {
                pre.printStackTrace();
            }
            return view;
        }
        }

    /*private static class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ViewHolder> {
        private final ArrayList<ItemViewOrderObject> itemViewOrderObjects;
        boolean visible;

        ItemOrderAdapter( ArrayList<ItemViewOrderObject> itemViewOrderObjects) {
            this.itemViewOrderObjects = itemViewOrderObjects;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_itemview, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.lt_itemName.setText(itemViewOrderObjects.get(position).getCuisine_name());
                holder.lt_qty.setText(itemViewOrderObjects.get(position).getQty());

            }

        }

        @Override
        public int getItemCount() {
            return itemViewOrderObjects.size();
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

            LoaderTextView lt_itemName,lt_qty;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                lt_itemName = itemView.findViewById(R.id.lt_itemName);
                lt_qty = itemView.findViewById(R.id.lt_qty);


            }
        }
    }*/

    private void FindViewByIdBottomDialog(View view) {
        img_profile=view.findViewById(R.id.img_profile);
        AC_Textview txt_referalHead=view.findViewById(R.id.txt_referalHead);
        AC_Edittext edt_referral=view.findViewById(R.id.edt_referral);
        txt_referalHead.setVisibility(View.GONE);
        edt_referral.setVisibility(View.GONE);
        img_male=view.findViewById(R.id.img_male);
        img_female=view.findViewById(R.id.img_female);
        edt_name=view.findViewById(R.id.edt_name);
        edt_mobile=view.findViewById(R.id.edt_mobile);
        edt_email=view.findViewById(R.id.edt_email);
        txt_saveBtn=view.findViewById(R.id.txt_saveBtn);

        txt_saveBtn.setOnClickListener(this);
        img_female.setOnClickListener(this);
        img_male.setOnClickListener(this);
        img_profile.setOnClickListener(this);

    }
    private void FindViewByIdBottomDialogRating(View view) {

        lc_imgRestaurant=view.findViewById(R.id.lc_imgRestaurant);
        lc_imgdriver=view.findViewById(R.id.lc_imgdriver);
        lt_restaurantname=view.findViewById(R.id.lt_restaurantname);
        lt_drivername=view.findViewById(R.id.lt_drivername);
        lt_cuisine=view.findViewById(R.id.lt_cuisine);
        ratingBar_restaurant=view.findViewById(R.id.ratingBar_restaurant);
        ratingBar_driver=view.findViewById(R.id.ratingBar_driver);
        txt_submit=view.findViewById(R.id.txt_submit);
        edt_commentres=view.findViewById(R.id.edt_commentres);
        edt_commentdriver = view.findViewById(R.id.edt_commentdriver);
        txt_submit.setOnClickListener(v -> {

            Log.d("hvcnbvcnbvc","cgcc"+FC_Common.restaurant_rating);
            Log.d("hvcnbvcnbvc","cgcc"+FC_Common.driver_rating);

            if (FC_Common.restaurant_rating.equalsIgnoreCase(""))
            {
                Log.d("hvcnbvcnbvc","dg4wcgcc"+FC_Common.restaurant_rating);
                snackBar(getResources().getString(R.string.rate_res));
            }
            if (FC_Common.driver_rating.equalsIgnoreCase(""))
            {
                Log.d("hvcnbvcnbvc","xcghdfcgcc"+FC_Common.driver_rating);
                snackBar(getResources().getString(R.string.rate_driver));
            }
            if (FC_Common.restaurant_rating.equalsIgnoreCase(".0")||
                    FC_Common.driver_rating.equalsIgnoreCase(".0")||
                    FC_Common.restaurant_rating.equalsIgnoreCase("0.0")||
                    FC_Common.driver_rating.equalsIgnoreCase("0.0")){
                   Log.d("fghfghfghfg","fail");
                snackBar(getResources().getString(R.string.rating_res_driver));
            }
            else {
                txt_submit.setEnabled(false);
                FC_Common.restaurant_comments=edt_commentres.getText().toString().trim();
                FC_Common.driver_comments=edt_commentdriver.getText().toString().trim();
                FC_Common.restaurant_rating=String.valueOf(ratingBar_restaurant.getRating());
                FC_Common.driver_rating=String.valueOf(ratingBar_driver.getRating());
                Log.d("fghfghfghfg","fghfghfgh"+FC_Common.restaurant_rating+FC_Common.driver_rating);

               /* FC_Common.restaurant_rating_int=Integer.parseInt(FC_Common.restaurant_rating);
                FC_Common.driver_rating_int=Integer.parseInt(FC_Common.driver_rating);*/
                Updaterating();
            }
        });
    }
    private void UserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("ServerResponse", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.usergender = jsonResponse1.getString("gender");
                        String picture = jsonResponse1.getString("picture");
                        edt_name.setText(jsonResponse1.getString("name"));
                        edt_mobile.setText(jsonResponse1.getString("mobile"));
                        edt_email.setText(jsonResponse1.getString("email"));
                        Picasso.get().load(picture)
                                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(img_profile);
                        if (FC_Common.usergender.equalsIgnoreCase("Female"))
                        {
                            img_female.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                            img_male.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        else {
                            img_male.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                            img_female.setBackgroundColor(getResources().getColor(R.color.white));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                        snackBar(getResources().getString(R.string.reach));
                    }
                }, volleyError -> {

            snackBar(getResources().getString(R.string.reach));
                }) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    private void detailsview() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ORDERDETAIL,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            FC_Common.orderrestaurant_name = obj.getString("restaurant_name");
                            FC_Common.ordercuisine_id= obj.getString("cuisine_id");
                            FC_Common.ordername= obj.getString("name");
                            FC_Common.orderdriver= obj.getString("avatar");
                            FC_Common.orderrestaurant= obj.getString("restaurant_logo");
                            FC_Common.orderid= obj.getString("id");
                            Picasso.get().load(FC_Common.orderrestaurant)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(lc_imgRestaurant);
                            Picasso.get().load(FC_Common.orderdriver)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(lc_imgdriver);
                            lt_restaurantname.setText(FC_Common.orderrestaurant_name);
                            lt_cuisine.setText(FC_Common.ordercuisine_id);
                            lt_drivername.setText(FC_Common.ordername);
                            txt_submit.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(getResources().getString(R.string.reach));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                Log.d("order_id: ", "" + params);
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
    private void Updaterating() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_RATE,
                response -> {
                    Log.d("cvnvncvb", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            txt_submit.setEnabled(true);
                            Utils.toast(getContext(),FC_Common.message);
                            //snackBar(FC_Common.message);
                            ratingdialog.dismiss();
                        } else {
                            txt_submit.setEnabled(true);
                            Utils.toast(getContext(),FC_Common.message);
                            //snackBar(FC_Common.message);
                            ratingdialog.dismiss();
                        }

                    } catch (JSONException e) {
                        txt_submit.setEnabled(true);
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                    }
                },
                error -> {
                    txt_submit.setEnabled(true);

                    snackBar(getResources().getString(R.string.reach));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                params.put("driver_rating", FC_Common.driver_rating);
                params.put("driver_comment", FC_Common.driver_comments);
                params.put("restaurant_rating", FC_Common.restaurant_rating);
                params.put("restaurant_comment", FC_Common.restaurant_comments);
                //params.put("rating", String.valueOf(FC_Common.restaurant_rating_int));
                //  params.put("order_id", "+");

                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void UpdateCart() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_REORDER,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            FC_Common.cartView="1";
                            Intent intent =new Intent(context, FC_CartActivity.class);
                            startActivity(intent);
                            Objects.requireNonNull(getActivity()).finish();
                        } else {
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                    }
                },
                error -> {

                    snackBar(getResources().getString(R.string.reach));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
               params.put("order_id", FC_Common.order_id);
              //  params.put("order_id", "+");

                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
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
    private void startDialog() {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                Objects.requireNonNull(context));
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery", (arg0, arg1) -> {
            Intent pictureActionIntent;
            pictureActionIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(
                    pictureActionIntent,
                    GALLERY_PICTURE);
        });

        myAlertDialog.setNegativeButton("Camera",
                (arg0, arg1) -> {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = Objects.requireNonNull(getActivity()).getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent,
                            CAMERA_REQUEST);

                });
        myAlertDialog.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri selectedImageUri;
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            Bitmap thumbnail = null ;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(
                        Objects.requireNonNull(getActivity()).getContentResolver(), imageUri);

            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                thumbnail = rotateImageIfRequired(Objects.requireNonNull(getActivity()),thumbnail,imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(thumbnail).compress(Bitmap.CompressFormat.JPEG, 20, bytes);
            String path = MediaStore.Images.Media.insertImage(Objects.requireNonNull(getActivity()).getContentResolver(), thumbnail, "Title", null);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            selectedImageUri = Uri.parse(path);

                img_profile.setBackground(null);
                FC_Common.img_profile = getPath(selectedImageUri);
                img_profile.setImageBitmap(thumbnail);
        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE && null != data) {

            Uri selectedImage = data.getData();
            String mCurrentPhotoPath = getRealPathFromURI(Objects.requireNonNull(getActivity()), selectedImage);

            selectedImageUri = data.getData();

                img_profile.setBackground(null);
                img_profile.setImageURI(selectedImageUri);
                FC_Common.img_profile = mCurrentPhotoPath;



        }
    }
    private String getPath(Uri uri) {

        String filePath = null;
        String uriString = uri.toString();

        if(uriString.startsWith("content://media")){
            filePath = getDataColumn(Objects.requireNonNull(getActivity()), uri, null, null);
        } else if (uriString.startsWith("file")){
            filePath = uri.getPath();
        } else if (uriString.startsWith("content://com")){
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            Uri contentUri;
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String selection = "_id=?";
            String[] selectionArgs = new String[] {split[1]};
            filePath = getDataColumn(Objects.requireNonNull(getActivity()), contentUri, selection, selectionArgs);
        }

        return filePath;
    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) {
            assert input != null;
            ei = new ExifInterface(input);
        }
        else
            ei = new ExifInterface(Objects.requireNonNull(selectedImage.getPath()));

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    @SuppressLint("SetTextI18n")
    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("ServerResponse", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.id = jsonResponse1.getString("id");
                        FC_Common.name = jsonResponse1.getString("name");
                        FC_Common.email = jsonResponse1.getString("email");
                        FC_Common.mobile = jsonResponse1.getString("mobile");
                        FC_Common.email_verified_at = jsonResponse1.getString("email_verified_at");
                        FC_Common.dial_code = jsonResponse1.getString("dial_code");
                        FC_Common.location_name = jsonResponse1.getString("location_name");
                        FC_Common.location_type = jsonResponse1.getString("location_type");
                        FC_Common.latitude = jsonResponse1.getString("latitude");
                        FC_Common.longitude = jsonResponse1.getString("longitude");
                        FC_Common.status = jsonResponse1.getString("status");
                        FC_Common.is_guest = jsonResponse1.getString("is_guest");
                        FC_Common.picture = jsonResponse1.getString("picture");
                        FC_Common.device_token = jsonResponse1.getString("device_token");
                        FC_Common.device_id = jsonResponse1.getString("device_id");
                        FC_Common.device_type = jsonResponse1.getString("device_type");
                        FC_Common.login_by = jsonResponse1.getString("login_by");
                        FC_Common.social_unique_id = jsonResponse1.getString("social_unique_id");
                        FC_Common.cust_id = jsonResponse1.getString("cust_id");
                        FC_Common.wallet_balance = jsonResponse1.getString("wallet_balance");
                        FC_Common.rating = jsonResponse1.getString("rating");
                        FC_Common.userotp = jsonResponse1.getString("otp");
                        FC_Common.created_at = jsonResponse1.getString("created_at");
                        FC_Common.updated_at = jsonResponse1.getString("updated_at");
                        FC_Common.token_type = jsonResponse1.getString("token_type");
                        FC_Common.access_token = jsonResponse1.getString("access_token");
                        FC_Common.gender = jsonResponse1.getString("gender");
                        FC_Common.filter_price_min = jsonResponse1.getString("filter_price_min");
                        FC_Common.filter_price_max = jsonResponse1.getString("filter_price_max");
                        FC_Common.is_default = jsonResponse1.getString("is_default");
                        Log.d("fdhdfhfd","dfgfd"+FC_Common.id);

                        if (FC_Common.status.equalsIgnoreCase("1"))
                        {

                            FC_User user = new FC_User(jsonResponse1.getString("id"),
                                    jsonResponse1.getString("name"),
                                    jsonResponse1.getString("email"),
                                    jsonResponse1.getString("mobile"),
                                    jsonResponse1.getString("email_verified_at"),
                                    jsonResponse1.getString("dial_code"),
                                    jsonResponse1.getString("location_name"),
                                    jsonResponse1.getString("location_type"),
                                    jsonResponse1.getString("latitude"),
                                    jsonResponse1.getString("longitude"),
                                    jsonResponse1.getString("status"),
                                    jsonResponse1.getString("is_guest"),
                                    jsonResponse1.getString("picture"),
                                    jsonResponse1.getString("device_token"),
                                    jsonResponse1.getString("device_id"),
                                    jsonResponse1.getString("device_type"),
                                    jsonResponse1.getString("login_by"),
                                    jsonResponse1.getString("social_unique_id"),
                                    jsonResponse1.getString("cust_id"),
                                    jsonResponse1.getString("wallet_balance"),
                                    jsonResponse1.getString("rating"),
                                    jsonResponse1.getString("otp"),
                                    jsonResponse1.getString("created_at"),
                                    jsonResponse1.getString("updated_at"),
                                    jsonResponse1.getString("token_type"),
                                    jsonResponse1.getString("access_token"),
                                    jsonResponse1.getString("gender"),
                                    jsonResponse1.getString("filter_price_min"),
                                    jsonResponse1.getString("filter_price_max"),
                                    jsonResponse1.getString("is_default")
                            );

                            FC_SharedPrefManager.getInstance(context).userLogin(user);
                            SharedPreferences sharedPreferences = context.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("FirstLogin", true);
                            editor.apply();
                            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                            editor1.putString(Utils.id, FC_Common.id);
                            editor1.putString(Utils.name, FC_Common.name);
                            editor1.putString(Utils.email, FC_Common.email);
                            editor1.putString(Utils.mobile, FC_Common.mobile);
                            editor1.putString(Utils.email_verified_at, FC_Common.email_verified_at);
                            editor1.putString(Utils.dial_code, FC_Common.dial_code);
                            editor1.putString(Utils.location_name, FC_Common.location_name);
                            editor1.putString(Utils.location_type, FC_Common.location_type);
                            editor1.putString(Utils.latitude, FC_Common.latitude);
                            editor1.putString(Utils.longitude, FC_Common.longitude);
                            editor1.putString(Utils.status, FC_Common.status);
                            editor1.putString(Utils.is_guest, FC_Common.is_guest);
                            editor1.putString(Utils.picture, FC_Common.picture);
                            editor1.putString(Utils.device_token, FC_Common.device_token);
                            editor1.putString(Utils.device_id, FC_Common.device_id);
                            editor1.putString(Utils.device_type, FC_Common.device_type);
                            editor1.putString(Utils.login_by, FC_Common.login_by);
                            editor1.putString(Utils.social_unique_id, FC_Common.social_unique_id);
                            editor1.putString(Utils.cust_id, FC_Common.cust_id);
                            editor1.putString(Utils.wallet_balance, FC_Common.wallet_balance);
                            editor1.putString(Utils.rating, FC_Common.rating);
                            editor1.putString(Utils.userotp, FC_Common.userotp);
                            editor1.putString(Utils.created_at, FC_Common.created_at);
                            editor1.putString(Utils.updated_at, FC_Common.updated_at);
                            editor1.putString(Utils.token_type, FC_Common.token_type);
                            editor1.putString(Utils.access_token, FC_Common.access_token);
                            editor1.putString(Utils.gender, FC_Common.gender);
                            editor1.putString(Utils.filter_price_min, FC_Common.filter_price_min);
                            editor1.putString(Utils.filter_price_max, FC_Common.filter_price_max);
                            editor1.putString(Utils.is_default, FC_Common.is_default);
                            editor1.apply();
                            profiledialog.dismiss();
                            txt_userName.setText(FC_Common.name);
                            txt_mobileEmail.setText(FC_Common.mobile+" - "+FC_Common.email);
                            if (FC_Common.gender.equalsIgnoreCase("Female"))
                            {

                                Picasso.get().load(R.drawable.female)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(img_profile_new);
                            }
                            else {

                                Picasso.get().load(R.drawable.male)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(img_profile_new);
                            }

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                        snackBar(getResources().getString(R.string.reach));
                    }
                }, volleyError -> {
            snackBar(getResources().getString(R.string.reach));
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }


    private void DeleteMenu() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PROFILEUPDATE,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            //AllCartList();
                            Utils.stopProgressBar();
                            GetUserDetails();
                        }
                        else {
                            Utils.stopProgressBar();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    Utils.stopProgressBar();
                    snackBar(getResources().getString(R.string.reach));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", FC_Common.username);
                params.put("email", FC_Common.useremail);
                params.put("gender", FC_Common.usergender);
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

    private String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String [] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted from popup, call savepdf method
                new savefunc(getActivity()).execute();
            } else {
                //permission was denied from popup, show error message
                Toast.makeText(getActivity(), "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);//If the user has denied the permission previously your code will come to this block
//Here you can explain why you need this permission
//Explain here why you need this permission
//And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @SuppressLint("StaticFieldLeak")
    private class savefunc extends AsyncTask<Void, Void, Void> {
        public Context context;

        savefunc(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                String uploadId = UUID.randomUUID().toString();

               String path = FC_Common.img_profile;


                    new MultipartUploadRequest(Objects.requireNonNull(getActivity()), uploadId, FC_URL.URL_PROFILEUPDATE)
                            .addFileToUpload(path, "picture") //Adding file
                            .addParameter("name", FC_Common.username) //Adding text parameter to the request
                            .addParameter("email", FC_Common.useremail) //Adding text parameter to the request
                            .addParameter("gender", FC_Common.usergender) //Adding text parameter to the request

                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(4)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {
                                    Utils.toast(getActivity(), "Uploading please Wait");
                                    txt_saveBtn.setEnabled(false);
                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                    Utils.toast(getActivity(), "Upload Error");
                                    txt_saveBtn.setEnabled(true);
                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    profiledialog.dismiss();
                                    GetUserDetails();
                                }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {
                                    Utils.toast(getActivity(), "Upload Cancel");
                                    txt_saveBtn.setEnabled(true);
                                }
                            })
                            .startUpload(); //Starting the upload
            } catch (Exception exc) {
            }
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void result) {
        }
    }


    private void Logout() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_LOGOUT,
                ServerResponse -> {

                    Log.d("ServerResponse", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.success = jsonResponse1.getString("success");
                        FC_Common.message = jsonResponse1.getString("message");
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {
                            logout = 1;
                            if (logoutAdminListener != null)
                                logoutAdminListener.onLogOut(logout);
                            Utils.stopProgressBar();
                            alertDialog.dismiss();
                        }else
                        {
                            Utils.stopProgressBar();
                            alertDialog.dismiss();
                            snackBar(FC_Common.message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        snackBar(getResources().getString(R.string.reach));
                        alertDialog.dismiss();
                    }
                }, volleyError -> {
            Utils.stopProgressBar();
            snackBar(getResources().getString(R.string.reach));
            alertDialog.dismiss();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }



}
