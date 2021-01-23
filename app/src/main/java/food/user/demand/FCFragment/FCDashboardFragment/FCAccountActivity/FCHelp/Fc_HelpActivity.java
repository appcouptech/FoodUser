package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCHelp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class Fc_HelpActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(Fc_HelpActivity.this,getResources().getConfiguration());
        setContentView(R.layout.fc_help);
        expListView =  findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            // Toast.makeText(getApplicationContext(),
            // "Group Clicked " + listDataHeader.get(groupPosition),
            // Toast.LENGTH_SHORT).show();
            return false;
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(groupPosition -> {
           /* Toast.makeText(getApplicationContext(),
                    listDataHeader.get(groupPosition) + " Expanded",
                    Toast.LENGTH_SHORT).show();*/
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(groupPosition -> {
            /*Toast.makeText(getApplicationContext(),
                    listDataHeader.get(groupPosition) + " Collapsed",
                    Toast.LENGTH_SHORT).show();*/

        });

        // Listview on child click listener
        expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // TODO Auto-generated method stub
           /* Toast.makeText(
                    getApplicationContext(),
                    listDataHeader.get(groupPosition)
                            + " : "
                            + listDataChild.get(
                            listDataHeader.get(groupPosition)).get(
                            childPosition), Toast.LENGTH_SHORT)
                    .show();*/
            return false;
        });
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Orders");
        listDataHeader.add("Delivery");
        listDataHeader.add("Returns");
        listDataHeader.add("Payments");
        listDataHeader.add("Product");
        listDataHeader.add("General");

        // Adding child data
        List<String> Orders = new ArrayList<>();
        Orders.add("Q1. Can I place order on call?\n" +
                "\n" +
                "A1. Sorry, we don’t accept orders on call. However you can call us on +91-7259085922 for any help related to placing order.");
        Orders.add("Q2. How do I know my order is confirmed?\n" +
                "\n" +
                "A2. We will send you a order confirmation email once we receive your order.");
        Orders.add("Q3. How do I edit my order after placing it?\n" +
                "\n" +
                "A3. Please call us on +91-9442330633 or write email to info@appcoup.com mentioning your Order #.");
        Orders.add("Q4. How do I cancel my order?\n" +
                "\n" +
                "A4. Please refer to our return & cancellation policy here.");


        List<String> Delivery = new ArrayList<>();
        Delivery.add("Q1. How will be my orders delivered?\n" +
                "\n" +
                "A1. Orders are delivered directly by the Appcoup Packaging Assist suppliers. Different items in an order could be fulfilled by different suppliers. We will share the contact details and amount payable for all deliveries via email and SMS.");
        Delivery.add("Q2. When is the order delivered?\n" +
                "\n" +
                "A2. Our suppliers deliver the products within 3-4 days of orders being placed. For products with your logo printed, lead time could be higher. Please check details at the product page.");
        Delivery.add("Q3. Is delivery free?\n" +
                "\n" +
                "A3. Yes. There are no delivery or handling charges.");
        Delivery.add("Q4. Why is my location is not serviceable?\n" +
                "\n" +
                "A4. We are currently live only in a few cities. We will be expanding soon.");
        Delivery.add("Q5. Can I have my order delivered to me by next day?\n" +
                "\n" +
                "A5. Please call Appcoup Partner Support on +91-9789753006 or write email to packaging@appcoup.com mentioning your Order # and we shall try our best to help you out");
        Delivery.add(" Q6. Will I get my entire order in single delivery?\n" +
                "\n" +
                "                A6. This depends on the items you have ordered. We have partnered with different suppliers for different products to help you get the best pricing.");
        Delivery.add("Q7. What if I have a problem with my delivery?\n" +
                "\n" +
                "A7. Please call Appcoup Partner Support on +91-9442330633 or write email to packaging@appcoup.com mentioning your Order # and they will help you out.");
        Delivery.add("Q8. Can I change the address once the order has been placed?\n" +
                "\n" +
                "A8. Yes, you can ask for a change in delivery address if our supplier hasn’t dispatched the placed order by calling Appcoup Partner Support on +91-9789753006 or writing to packaging@appcoup.com mentioning your Order #");



        List<String> Returns = new ArrayList<>();
        Returns.add("Q1. I received wrong products, what should I do?\n" +
                "\n" +
                "A1. Please see our return and cancellation policy here.");
        Returns.add("Q2. My goods are damaged, what should I do?\n" +
                "\n" +
                "A2. Please see our return and cancellation policy here.");

        List<String> Payments = new ArrayList<>();
        Payments.add("Q1. When I have to pay?\n" +
                "\n" +
                "A1. You will have to pay at the time of delivery as per the payment details we send to you via email.");
        Payments.add("Q2. What payment modes are accepted?\n" +
                "\n" +
                "A2. You can pay by cash or cheque at the time of delivery. We will soon be introducing more payment methods.");


        Payments.add("Q3. Can I buy the goods on credit?\n" +
                "\n" +
                "                A3. Sorry, we don’t allow a credit on goods currently.");
        Payments.add("Q4. Can I pay online?\n" +
                "\n" +
                "A4. You can do NEFT transfer. We will be sharing the details via email.");
        Payments.add("Q5. Will I get an invoice?\n" +
                "\n" +
                "A5. Yes. You will get your invoice from our supplier at the time of delivery.");


        List<String> Product = new ArrayList<>();
        Product.add("Q1. How have you selected these products?\n" +
                "\n" +
                "A1. We have show products which have passed our quality tests and are best suited for delivery. We will keep updating our catalog with better products. Stay tuned!");
        Product.add("Q2. You don’t have the product I need, what should I do?\n" +
                "\n" +
                "A2. You can write to us at packaging@appcoup.com and we will try our best to include your need in our catalog.");


        List<String> General = new ArrayList<>();
        General.add("Q1. Why is Appcoup doing this?\n" +
                "\n" +
                "A1. At Appcoup we strive to add value to your business. We understand the challenges you face on a daily basis in sourcing the best quality packaging material. Appcoup Packaging Assist is to take care of all your packaging needs.");
        General.add("Q2. How are you guys are different?\n" +
                "\n" +
                "A2. Best products - We only list the products which have passed our tests and are the best suitable for delivery. They will help in giving a better consumer experience, so you can get get repeated orders.\n" +
                "\n" +
                "Best prices - We have negotiated rates with our distributors exclusive for Appcoup Restaurant.\n" +
                "\n" +
                "Convenience - No need to haggle for prices. No need to follow-up on delivery. We track all orders to ensure you have a hassle free experience.");


        listDataChild.put(listDataHeader.get(0), Orders); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Delivery);
        listDataChild.put(listDataHeader.get(2), Returns);
        listDataChild.put(listDataHeader.get(3), Payments);
        listDataChild.put(listDataHeader.get(4), Product);
        listDataChild.put(listDataHeader.get(5), General);
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

         ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert infalInflater != null;
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild =  convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert infalInflater != null;
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader =  convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
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
}
