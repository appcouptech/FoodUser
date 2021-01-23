package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants.Testing_item;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants.Testing_main;
import food.user.demand.FCPojo.FCHomeFragmentObject.TestObject;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;

import food.user.demand.FCViews.Utils;
import food.user.demand.R;


public class Testingdashboard extends AppCompatActivity {
    private static final String HI="http://192.168.21.129/api/user/search/auto";
    private List<ActorsList>actorsLists;
    private ActorsAdapter actorsAdapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(Testingdashboard.this,getResources().getConfiguration());
        setContentView(R.layout.testingdashboard);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        actorsLists=new ArrayList<>();
        actorsAdapter=new ActorsAdapter(actorsLists,this);
        getActorsData();
    }

    private void getActorsData() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, HI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("sdgfsfdgsfd","dfgdfg"+jsonObject);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject object=jsonArray.getJSONObject(i);
                        ActorsList actorsList=new ActorsList(object.getString("title"),
                                object.getString("photo"));
                        actorsLists.add(actorsList);
                        recyclerView.setAdapter(actorsAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("sdgfsfdgsfd","dfgdfg"+e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", "11.03885560");
                params.put("longitude", "77.03589200");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type_dup + " " + FC_Common.access_token_dup);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview,menu);
        final MenuItem menuItem=menu.findItem(R.id.search);
        searchView=(SearchView)menuItem.getActionView();
        ((EditText)searchView.findViewById(R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<ActorsList>filterModList=filter(actorsLists,newText);
                actorsAdapter.setfilter(filterModList);
                return true;
            }
        });
        return true;

    }
    private List<ActorsList>filter(List<ActorsList>hi,String query){
        query=query.toLowerCase();
        final List<ActorsList>filterModeList=new ArrayList<>();
        for (ActorsList modal:hi){
            final String text=modal.getName().toLowerCase();
            if (text.startsWith(query)){
                filterModeList.add(modal);
            }
        }
        return filterModeList;
    }


    public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ViewHolder> {
        private List<ActorsList>actorsLists;
        private Context context;

    public ActorsAdapter(List<ActorsList> actorsLists, Context context) {
            this.actorsLists = actorsLists;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.autocomplete,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ActorsList actorsList=actorsLists.get(position);
            holder.aName.setText(actorsList.getName());
            //Picasso.get().load(actorsLists.get(position).getImage_url()).into(holder.aImg);
            Picasso.get().load(actorsLists.get(position).getImage_url())
                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.aImg);
        }

        @Override
        public int getItemCount() {
            return actorsLists.size();
        }
        public void setfilter(List<ActorsList>actorsList){
            actorsLists=new ArrayList<>();
            actorsLists.addAll(actorsList);
            notifyDataSetChanged();

        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView aImg;
            private TextView aName;
            public ViewHolder(View itemView) {
                super(itemView);
                aImg=(ImageView)itemView.findViewById(R.id.image_view);
                aName=(TextView)itemView.findViewById(R.id.actor_name);
            }
        }
    }

}
