package food.user.demand.FCViews;
public class FC_URL
{

    //URL//
    //private static final String ROOT_URL = "http://192.168.21.248/api/user";
   // private static final String ROOT_URL = "https://foodcoup.appcoup.com/api/user";
    private static final String ROOT_URL = "https://foodcoupdemo3.appcoup.com/api/user";
    //Headers//
    private static final String ROOT_PHP = "";
    private static final String ROOT_ADDRESS = "/address";
    private static final String ROOT_GREATOFFER = "/banner";
    private static final String ROOT_ITEMOFFER = "/category";
    private static final String ROOT_HOTSELLER = "/hotseller";
    private static final String ROOT_TOPPICK = "/toppick";
    private static final String ROOT_CARDOFFER = "/offer";
    private static final String ROOT_ALLRESTAURANT = "/restaurant";
    private static final String ROOT_PRODUCT = "/product";
    private static final String ROOT_CART = "/cart";
    private static final String ROOT_CUISINE = "/cuisine";
    private static final String ROOT_SEARCH = "/search";
    private static final String ROOT_RECENT = "/recent";
    private static final String ROOT_FAVOURITE = "/favourite";
    private static final String ROOT_SEND = "/send";
    private static final String ROOT_ORDER = "/order";
    private static final String ROOT_PROFILE = "/profile";
    private static final String ROOT_PASTORDER = "/past";
    private static final String ROOT_BANNER = "/banner";
    private static final String ROOTTOKEN= "/token";
    private static final String ROOTCOUPON= "/coupon";
    private static final String ROOTWALLET= "/wallet";
    private static final String ROOTITEM= "/item";
    private static final String ROOTRATE= "/rate";
    private static final String ROOTCANCEL= "/cancel";
    private static final String ROOTPAYMENT= "/payment";

    //ROOT_URL //
    public static  String ROOT_URL_check = "https://foodcoupdemo3.appcoup.com/api/user";
    public static final String URL_TEST= "https://foodcoup.appcoup.com/api/driver/stripekey";
    public static final String URL_LOGIN= ROOT_URL+"/login"+ROOT_PHP;
    public static final String URL_LOGINOTP= ROOT_URL+"/otp"+ROOT_PHP;
    public static final String URL_REGISTER= ROOT_URL+"/register"+ROOT_PHP;
    public static final String URL_DETAILUSER= ROOT_URL+"/detail"+ROOT_PHP;
    public static final String URL_REORDER= ROOT_URL+"/reorder"+ROOT_PHP;
    public static final String URL_RATE= ROOT_URL+"/rate"+ROOT_PHP;
    public static final String URL_HISTORY= ROOT_URL+"/history"+ROOT_PHP;
    public static final String URL_LOGOUT= ROOT_URL+"/logout"+ROOT_PHP;
    public static final String ROOTSTRIPE= ROOT_URL+"/stripekey"+ROOT_PHP;


    //ROOT_URL + ROOT_ADDRESS//
    public static final String URL_ADDUSER= ROOT_URL+ROOT_ADDRESS+"/add"+ROOT_PHP;
    public static final String URL_LOCATIONLIST= ROOT_URL+ROOT_ADDRESS+"/list"+ROOT_PHP;
    public static final String URL_LOCATIONDEFAULT= ROOT_URL+ROOT_ADDRESS+"/default"+ROOT_PHP;
    public static final String URL_LOCATIONDELETE= ROOT_URL+ROOT_ADDRESS+"/delete"+ROOT_PHP;
    public static final String URL_LOCATIONUPDATE= ROOT_URL+ROOT_ADDRESS+"/update"+ROOT_PHP;

    //ROOT_URL + ROOT_GREATOFFER//
    public static final String URL_GREATOFFERLIST= ROOT_URL+ROOT_GREATOFFER+"/list"+ROOT_PHP;

    //ROOT_URL + ROOT_ITEMOFFER//
    public static final String URL_ITEMOFFERLIST= ROOT_URL+ROOT_ITEMOFFER+"/list"+ROOT_PHP;

    //ROOT_URL + ROOT_HOTSELLER//
    public static final String URL_HOTSELLERLIST= ROOT_URL+ROOT_HOTSELLER+"/list"+ROOT_PHP;


    //ROOT_URL + ROOT_TOPPICK//
    public static final String URL_TOPPICKLIST= ROOT_URL+ROOT_TOPPICK+"/list"+ROOT_PHP;
    public static final String URL_PRODUCTDETAIL= ROOT_URL+ROOT_TOPPICK+"/detail"+ROOT_PHP;
    public static final String URL_PRODUCT= ROOT_URL+ROOT_TOPPICK+"/product"+ROOT_PHP;

    //ROOT_URL + ROOT_TOPPICK//
    public static final String URL_ALLRESTAURANTLIST= ROOT_URL+ROOT_ALLRESTAURANT+"/list"+ROOT_PHP;
    public static final String URL_ALLRESTAURANTMAP= ROOT_URL+ROOT_ALLRESTAURANT+"/map"+ROOT_PHP;

    //ROOT_URL + ROOT_TOPPICK//
    public static final String URL_CARDOFFERLIST= ROOT_URL+ROOT_CARDOFFER+"/list"+ROOT_PHP;
    public static final String URL_PROMOCODE= ROOT_URL+ROOT_CARDOFFER+"/add"+ROOT_PHP;

    //ROOT_URL + ROOT_PRODUCT//
    public static final String URL_PRODUCTLIST= ROOT_URL+ROOT_PRODUCT+"/list"+ROOT_PHP;
    public static final String URL_PRODUCTADDONLIST= ROOT_URL+ROOT_PRODUCT+"/addon"+ROOT_PHP;
    public static final String URL_PRODUCTSHORT= ROOT_URL+ROOT_PRODUCT+"/short"+ROOT_PHP;

    //ROOT_URL + ROOT_CART//
    public static final String URL_PRODUCTADDONUPDATECART= ROOT_URL+ROOT_CART+"/add"+ROOT_PHP;
    public static final String URL_PRODUCTCARTLIST= ROOT_URL+ROOT_CART+"/list"+ROOT_PHP;
    public static final String URL_PRODUCTCARTDELETE= ROOT_URL+ROOT_CART+"/delete"+ROOT_PHP;
    public static final String URL_PRODUCTCARTCLEAR= ROOT_URL+ROOT_CART+"/clear"+ROOT_PHP;
    //ROOT_URL + ROOT_CUISINE//
    public static final String URL_CUISNELIST= ROOT_URL+ROOT_CUISINE+"/list"+ROOT_PHP;
    public static final String URL_CUISNELISTREST= ROOT_URL+ROOT_CUISINE+"/restaurant"+ROOT_PHP;

    //ROOT_URL + ROOT_CUISINE//
    public static final String URL_SEARCHLIST= ROOT_URL+ROOT_SEARCH+"/auto"+ROOT_PHP;

    //ROOT_URL + ROOT_PROFILE//
    public static final String URL_PROFILEUPDATE= ROOT_URL+ROOT_PROFILE+"/update"+ROOT_PHP;


    //ROOT_URL + ROOT_FAVOURITE//
    public static final String URL_FAVOURITELIST= ROOT_URL+ROOT_FAVOURITE+"/list"+ROOT_PHP;
    public static final String URL_FAVOURITEADD= ROOT_URL+ROOT_FAVOURITE+"/add"+ROOT_PHP;
    public static final String URL_FAVOURITEDELETE= ROOT_URL+ROOT_FAVOURITE+"/delete"+ROOT_PHP;

    //ROOT_URL + ROOT_SEND//
    public static final String URL_PAYMENT_GATEWAY= ROOT_URL+"/pay-gateway"+ROOT_PHP;
    public static final String URL_PAYMENT= ROOT_URL+ROOT_SEND+"/order"+ROOT_PHP;
    public static final String URL_PAYMENT_STATUS= ROOT_URL+ROOTPAYMENT+"/status"+ROOT_PHP;
    //ROOT_URL + ROOT_RECENT//
    public static final String URL_RECENTSEARCH= ROOT_URL+ROOT_RECENT+"/search"+ROOT_PHP;

    //ROOT_URL + ROOT_PASTORDER//
    public static final String URL_PASTORDER= ROOT_URL+ROOT_PASTORDER+"/order"+ROOT_PHP;


    //ROOT_URL + ROOT_ORDER//
    public static final String URL_ORDERLIST= ROOT_URL+ROOT_ORDER+"/list"+ROOT_PHP;
    public static final String URL_ORDERDETAIL= ROOT_URL+ROOT_ORDER+"/detail"+ROOT_PHP;

    //ROOT_URL + ROOT_BANNER//
    public static final String URL_BANNERRESTAURANT= ROOT_URL+ROOT_BANNER+"/restaurant"+ROOT_PHP;

    /*ROOT_URL + ROOTTOKEN*/
    public static final String URL_TOKENVALIDATE= ROOT_URL+ROOTTOKEN +"/validate"+ROOT_PHP;

    /*ROOT_URL + ROOTCOUPON*/
    public static final String URL_COUPONLIST= ROOT_URL+ROOTCOUPON +"/list"+ROOT_PHP;
    public static final String URL_COUPONUPDATE= ROOT_URL+ROOTCOUPON +"/update"+ROOT_PHP;

    /*ROOT_URL + ROOTWALLET*/
    public static final String URL_WALLETUPDATE= ROOT_URL+ROOTWALLET +"/update"+ROOT_PHP;
    public static final String URL_WALLETPAYMENT= ROOT_URL+ROOTWALLET +"/payment"+ROOT_PHP;
    public static final String URL_WALLETHISTORY= ROOT_URL+ROOTWALLET +"/history"+ROOT_PHP;
    public static final String URL_WALLETINFO= ROOT_URL+ROOTWALLET +"/info"+ROOT_PHP;

    /*ROOT_URL + ROOTITEM*/
    public static final String URL_ITEMDETAIL= ROOT_URL+ROOTITEM +"/detail"+ROOT_PHP;

    /*ROOT_URL + ROOTRATE*/
    public static final String URL_RATEDRIVER= ROOT_URL+ROOTRATE +"/driver"+ROOT_PHP;

    /*ROOT_URL + ROOTCANCEL*/
    public static final String URL_CANCEL= ROOT_URL+ROOTCANCEL +"/order"+ROOT_PHP;



}
