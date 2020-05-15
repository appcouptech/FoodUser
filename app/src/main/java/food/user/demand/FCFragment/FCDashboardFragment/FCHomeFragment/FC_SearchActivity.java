package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FC_HotelDetailsActivity;
import food.user.demand.FCPojo.FCExploreFragmentObject.RecentSearchObject;
import food.user.demand.FCPojo.FCExploreFragmentObject.SearchObject;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_SearchActivity extends AppCompatActivity {

    private Snackbar bar;
    private RecyclerView rv_searchRestaurant,rv_listRestaurant;
    private ArrayList<SearchObject> searchObjects;
    private ArrayList<RecentSearchObject> RecentsearchObjects;
    private SearchAdapter searchAdapters;
    private RecentSearchAdapter RecentsearchAdapters;
    private AC_Edittext edt_search;
    Context context;
    View parentLayout;
    AC_Textview txt_emptyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fc__explore);
        context=FC_SearchActivity.this;
        FindViewById();
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence txt, int i, int i1, int i2) {

                Utils.log(context,"txtlength"+txt.length());
                /*if (txt.length()>2) {
                    // SearchRestaurant
                    String searchvalue = edt_search.getText().toString().toLowerCase( Locale.getDefault());
                    SearchList(searchvalue);
                }*/
                String searchvalue = edt_search.getText().toString().toLowerCase( Locale.getDefault());
                SearchList(searchvalue);
                if (txt.length()==0){
                    RecentSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        SearchRecycler();
        RecentSearchRecycler();
        RecentSearch();


        searchAdapters = new SearchAdapter(searchObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_searchRestaurant.setLayoutManager(itemViewLLres);
        rv_searchRestaurant.setAdapter(searchAdapters);

        RecentsearchAdapters = new RecentSearchAdapter(RecentsearchObjects);
        LinearLayoutManager itemViewLLres1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_listRestaurant.setLayoutManager(itemViewLLres1);
        rv_listRestaurant.setAdapter(RecentsearchAdapters);
    }

    private void FindViewById() {
        parentLayout = findViewById(android.R.id.content);
        edt_search = findViewById(R.id.edt_search);
        txt_emptyview = findViewById(R.id.txt_emptyview);

        InputMethodManager imgr = (InputMethodManager) Objects.requireNonNull(context).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imgr != null) {
            imgr.showSoftInput(edt_search, InputMethodManager.SHOW_IMPLICIT);
        }

        rv_searchRestaurant = findViewById(R.id.rv_searchRestaurant);
        rv_listRestaurant = findViewById(R.id.rv_listRestaurant);
        searchObjects = new ArrayList<>();
        SearchObject object = new SearchObject();
        object.setD_images("");
        object.setD_images("");
        searchObjects.add(object);

        RecentsearchObjects = new ArrayList<>();
        RecentSearchObject Recentobject = new RecentSearchObject();
        Recentobject.setD_images("");
        Recentobject.setD_images("");
        RecentsearchObjects.add(Recentobject);

    }


    private void  SearchList(String searchvalue) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_SEARCHLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            searchObjects.clear();
                            // txt_emptyview.setVisibility(View.GONE);
                            rv_searchRestaurant.setVisibility(View.VISIBLE);
                            rv_listRestaurant.setVisibility(View.GONE);
                            for (int i = 0; i < dataArray.length(); i++) {
                                SearchObject playerModel = new SearchObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setRestaurant_id(product.getString("restaurant_id"));
                                    playerModel.setTitle(product.getString("title"));
                                    playerModel.setPhoto(product.getString("photo"));
                                    playerModel.setType(product.getString("type"));

                                    searchObjects.add(playerModel);
                                    if (searchObjects != null) {
                                        searchAdapters.visibleContentLayout();
                                    }
                                    else {
                                        // txt_emptyview.setVisibility(View.VISIBLE);
                                        rv_searchRestaurant.setVisibility(View.GONE);
                                        rv_listRestaurant.setVisibility(View.VISIBLE);
                                        Log.d("fghfdhfdg","dfghfdgfd");
                                        RecentSearch();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }}
                        } else {
                            rv_searchRestaurant.setVisibility(View.GONE);
                            rv_listRestaurant.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("term", searchvalue);
                Utils.log(context,"params: "+params);
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void  SearchRecycler() {
        searchAdapters = new SearchAdapter(searchObjects);
        rv_searchRestaurant.setAdapter(searchAdapters);
        rv_searchRestaurant.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }
    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        private final ArrayList<SearchObject> searchObjects;
        boolean visible;
        SearchAdapter( ArrayList<SearchObject> searchObjects) {
            this.searchObjects = searchObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_search__restaurant_items, parent, false);
            return new ViewHolder(listItem);
        }
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.vw_search.setVisibility(View.VISIBLE);
                holder.ll_loader.setVisibility(View.GONE);

                Picasso.get().load(searchObjects.get(position).getPhoto())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.img_dish);
                holder.txt_dishName.setText(searchObjects.get(position).getTitle());
                holder.txt_dishType.setText(searchObjects.get(position).getType());
                holder.ll_content.setOnClickListener(view -> {
                    FC_Common.restaurantid=searchObjects.get(position).getRestaurant_id();
                    FC_Common.recent_search="1";
                    Intent restaurant = new Intent(context, FC_HotelDetailsActivity.class);
                    restaurant.putExtra("hotelid",FC_Common.restaurantid);
                    restaurant.putExtra("recent_search",FC_Common.recent_search);
                    startActivity(restaurant);
                });
            } }
        @Override
        public int getItemCount() {
            return searchObjects.size();
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
            LinearLayout ll_loader,ll_content;
            View vw_search;
            ImageView img_dish;
            AC_Textview txt_dishName,txt_dishType;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                txt_dishType = itemView.findViewById(R.id.txt_dishType);
                img_dish = itemView.findViewById(R.id.img_dish);
                ll_loader = itemView.findViewById(R.id.ll_loader);
                ll_content = itemView.findViewById(R.id.ll_content);
                vw_search = itemView.findViewById(R.id.vw_search);
            }}}


    private void  RecentSearch() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_RECENTSEARCH,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            RecentsearchObjects.clear();
                            // txt_emptyview.setVisibility(View.GONE);
                            rv_searchRestaurant.setVisibility(View.GONE);
                            rv_listRestaurant.setVisibility(View.VISIBLE);
                            for (int i = 0; i < dataArray.length(); i++) {
                                RecentSearchObject playerModel = new RecentSearchObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));

                                    RecentsearchObjects.add(playerModel);
                                    if (RecentsearchObjects != null) {
                                        RecentsearchAdapters.visibleContentLayout();
                                    }
                                    else {
                                        //  txt_emptyview.setVisibility(View.VISIBLE);
                                        rv_listRestaurant.setVisibility(View.GONE);
                                        rv_searchRestaurant.setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }}
                        } else {
                            //txt_emptyview.setVisibility(View.VISIBLE);
                            rv_listRestaurant.setVisibility(View.GONE);
                            rv_searchRestaurant.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void  RecentSearchRecycler() {
        RecentsearchAdapters = new RecentSearchAdapter(RecentsearchObjects);
        rv_listRestaurant.setAdapter(RecentsearchAdapters);
        rv_listRestaurant.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }
    private class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
        private final ArrayList<RecentSearchObject> RecentSearchObjects;
        boolean visible;
        RecentSearchAdapter( ArrayList<RecentSearchObject> RecentSearchObjects) {
            this.RecentSearchObjects = RecentSearchObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_recentsearch_restaurant_items, parent, false);
            return new ViewHolder(listItem);
        }
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.vw_search.setVisibility(View.VISIBLE);
                holder.ll_loader.setVisibility(View.GONE);
                holder.txt_dishName.setText(RecentSearchObjects.get(position).getRestaurant_name());
                holder.ll_content.setOnClickListener(view -> {
                    FC_Common.restaurantid=RecentsearchObjects.get(position).getId();
                    FC_Common.recent_search="1";
                    Intent restaurant = new Intent(context, FC_HotelDetailsActivity.class);
                    restaurant.putExtra("hotelid",FC_Common.restaurantid);
                    restaurant.putExtra("recent_search",FC_Common.recent_search);
                    startActivity(restaurant);
                });
            } }
        @Override
        public int getItemCount() {
            return RecentSearchObjects.size();
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
            LinearLayout ll_loader,ll_content;
            View vw_search;
            AC_Textview txt_dishName;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                ll_loader = itemView.findViewById(R.id.ll_loader);
                ll_content = itemView.findViewById(R.id.ll_content);
                vw_search = itemView.findViewById(R.id.vw_search);
            }}}
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
