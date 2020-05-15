package food.user.demand.FCUtils.BottomDailog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import food.user.demand.FCPojo.FCCartActivityObject.AddonObject;
import food.user.demand.FCPojo.FCCartActivityObject.IngredientObject;
import food.user.demand.FCPojo.FCCartActivityObject.PreparationObject;
import food.user.demand.FCPojo.FCCartActivityObject.PricingObject;
import food.user.demand.FCUtils.StatefulRecyclerView.StatefulRecyclerView;
import food.user.demand.FCViews.AC_BoldTextview;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

import static com.android.volley.VolleyLog.TAG;

public class BottomDialogFragmentAddonProducts extends BottomSheetDialogFragment {
    private NestedScrollView nsv_cart;
    private String nsv_scrollX;
    private String nsv_scrollY;
    private AddonProducts addonProducts;
    private LinearLayout ll_main;
    private AC_BoldTextview txt_ingredients,txt_preparation,txt_pricing,txt_addon;
    private StatefulRecyclerView rv_ingredients,rv_preparation,rv_pricing,rv_addon;
    private ArrayList<PricingObject> pricingObjects = new ArrayList<>();
    private ArrayList<AddonObject> addonObjects = new ArrayList<>();
    private ArrayList<IngredientObject> ingredientObjects = new ArrayList<>();
    private ArrayList<PreparationObject> preparationObjects = new ArrayList<>();
    private PricingAdapter pricingAdapters;
    private AddonAdapter addonAdapters;
    private IngredientAdapter ingredientAdapters;
    private PreparationAdapter preparationAdapters;
    private AC_Textview txt_submitAddon,txt_submitAddondummy,txt_submitAddondummy2;
    private Snackbar bar;
    private int addproducts = 0;

    public static BottomDialogFragmentAddonProducts newInstance() {
        return new BottomDialogFragmentAddonProducts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // get the views and attach the listener
        return inflater.inflate(R.layout.layout_addon_products, container,
                false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManagerHotelDetailsActiviy = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FindViewById(view);
        Bundle object = getArguments();
        if (getActivity() instanceof AddonProducts)
            addonProducts = (AddonProducts) getActivity();
        if (object != null) {
            FC_Common.hotelpricing = object.getString("hotelpricing");
            FC_Common.addonpricing = object.getString("addonpricing");
            FC_Common.restaurantid = object.getString("hotelid");
            FC_Common.productID = object.getString("productID");
            FC_Common.quantity = object.getString("quantity");
            Log.d("fdhgdfgfd","fghggdfgfd"+FC_Common.quantity);
            Utils.log(getActivity(), "productID : " + FC_Common.hotelpricing + " jvdjfd" +  FC_Common.quantity  + FC_Common.addonpricing);
        }
        FC_Common.quantity="1";
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
            nsv_cart.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                nsv_scrollX = String.valueOf(scrollX);
                nsv_scrollY = String.valueOf(scrollY);
                 Log.d(TAG, "scrollX :" + nsv_scrollX + " , " + "scrollY :" + nsv_scrollY);

              /*  if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                     Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                     Log.i(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                     Log.i(TAG, "BOTTOM SCROLL");
                }*/
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
                        //tv.setText(tv.getText() + "" + CustomAdapter.modelArrayList.get(i).getAnimal()+",");
                        //String sdmkldn = "" + txt_submitAddondummy.getText() + "" + ingredientObjects.get(i).getId() + "";
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
                        //String sdmkldn = "" + txt_submitAddondummy.getText() + "" + ingredientObjects.get(i).getId() + "";
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

               // FC_Common.ingrdientTotal = finalnumber3;
                //FC_Common.addonTotal = txt_submitAddondummy2.getText().toString();
                //Utils.toast(getContext(), txt_submitAddondummy.getText().toString());
                UpdateMenu(FC_Common.ingrdientTotal, FC_Common.PreparationID, FC_Common.pricingID, FC_Common.addonTotal,FC_Common.quantity);


//                ArrayList<IngredientObject> actorList = ((IngredientAdapter)rv_ingredients.getAdapter()).getSelectActorList();
//                Toast.makeText(getContext(),""+actorList.toArray(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void FindViewById(View view) {
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

    //AddonList Async Task Start//
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

//                                   addonObjects = new ArrayList<>();
//                                   AddonObject addonObject = new AddonObject();
//                                   addonObject.setD_images("");
//                                   addonObject.setD_images("");
//                                   addonObjects.add(addonObject);

                                }
                                else {
                                    Log.d("sdgfsdfsdfsd","failsdfsdfsdfsd");
                                }
                                if (!addoncategories.equals("0")) {

                                    Log.d("sdgfsdfsdfsd","546fhfg");
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
//                                    ingredientObjects = new ArrayList<>();
//                                    IngredientObject ingredientObject = new IngredientObject();
//                                    ingredientObject.setD_images("");
//                                    ingredientObject.setD_images("");
//                                    ingredientObjects.add(ingredientObject);

                                }
                                else {
                                    Log.d("sdgfsdfsdfsd","hjklhjkhj");
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
                                   /* preparationObjects = new ArrayList<>();
                                    PreparationObject preparationObject = new PreparationObject();
                                    preparationObject.setD_images("");
                                    preparationObject.setD_images("");
                                    preparationObjects.add(preparationObject);*/
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
                            Log.d("sdgfsdfsdfsd", "454" + e);
                           // txt_pricing.setVisibility(View.GONE);
//                                women = 1;
//                                Utils.log(getActivity(), "women : " + women);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("sdgfsdfsdfsd", "fdhfdh" + e);
                        //txt_pricing.setVisibility(View.GONE);

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
            public Map<String, String> getHeaders()  {
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
                holder.txt_dishcurrency.setText(pricingObjects.get(position).getCurrency());
                holder.txt_dishprice.setText(pricingObjects.get(position).getPrice());
                holder.rb_dishname.setChecked(position == mSelectedItem);
                holder.rb_dishname.setOnClickListener(v -> {
                    final int position1 = holder.getAdapterPosition();
                    mSelectedItem = position1;
                    notifyDataSetChanged();
                    FC_Common.pricingID = pricingObjects.get(position1).getId();
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
            AC_Textview txt_dishName,txt_dishcurrency,txt_dishprice;
            RadioButton rb_dishname;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_dishprice = itemView.findViewById(R.id.txt_dishprice);
                txt_dishcurrency = itemView.findViewById(R.id.txt_dishcurrency);
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






    private void UpdateMenu(String ingrdientTotal,String PreparationID,String pricingID,String addonTotal,String quantity) {

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
                            dismiss();
                            Log.d("addproducts", "dfgdfgfd" + addproducts);
                        }
                        else
                        {
                            addproducts = 2;
                            if (addonProducts != null)
                                addonProducts.addProducts(addproducts);
                            dismiss();
                            snackBar(FC_Common.message);
                            Utils.toast(getActivity(),FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        dismiss();
                       // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(getResources().getString(R.string.reach));
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
    @Override
    public void onResume()
    {
        super.onResume();
        if (nsv_scrollX != null && nsv_scrollY != null){


            int x = Integer.parseInt(nsv_scrollX);
            int y = Integer.parseInt(nsv_scrollY);

           // Log.d(TAG, " x :" + x + " , " + " y :" + y);

            nsv_cart.smoothScrollTo(x, y);
            // nsv_cart.scrollTo(x ,y);
        }
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
                }
            }
        };
        timerThread.start();
    }


    public interface AddonProducts {
        void addProducts(int products);
    }

}