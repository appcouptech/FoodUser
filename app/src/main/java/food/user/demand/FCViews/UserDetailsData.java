package food.user.demand.FCViews;

public class UserDetailsData {

    private String name;
    private String id;
    private String companyname ;
    private String companyaliasname;
    private String neworderid ;
    private String userid ;
    private String categoryid ;
    private String subcategoryid;
    private String qty;
    private String weightfrom;
    private String weightto;
    private String units;
    private String productimage;
    private String expecteddate;
    private String comments;
    private String status;
    private String orderdate;
    private String categoryname;
    private String productname;
    private String subcategoryname;
    private String adminname;
    private String productimage1;
    private String productimage2;
    private String productiondate;

    public UserDetailsData() {
    }

    public UserDetailsData(String name, String companyname, String companyaliasname, String neworderid,
                           String userid, String categoryid, String subcategoryid, String qty, String weightfrom, String weightto,
                           String units, String productimage, String expecteddate, String comments, String status,
                           String orderdate, String categoryname, String productname, String subcategoryname, String adminname) {
        this.name = name;
        this.companyname = companyname;
        this.companyaliasname = companyaliasname;
        this.neworderid = neworderid;
        this.userid = userid;
        this.categoryid = categoryid;
        this.subcategoryid = subcategoryid;
        this.subcategoryid = subcategoryid;
        this.qty = qty;
        this.weightfrom = weightfrom;
        this.weightto = weightto;
        this.units = units;
        this.productimage = productimage;
        this.expecteddate = expecteddate;
        this.comments = comments;
        this.status = status;
        this.orderdate = orderdate;
        this.categoryname = categoryname;
        this.productname = productname;
        this.subcategoryname = subcategoryname;
        this.adminname = adminname;
    }
    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getNeworderid() {
        return neworderid;
    }

    public void setNeworderid(String neworderid) {
        this.neworderid = neworderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getSubcategoryid() {
        return subcategoryid;
    }

    public void setSubcategoryid(String subcategoryid) {
        this.subcategoryid = subcategoryid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getWeightfrom() {
        return weightfrom;
    }

    public void setWeightfrom(String weightfrom) {
        this.weightfrom = weightfrom;
    }

    public String getWeightto() {
        return weightto;
    }

    public void setWeightto(String weightto) {
        this.weightto = weightto;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getExpecteddate() {
        return expecteddate;
    }

    public void setExpecteddate(String expecteddate) {
        this.expecteddate = expecteddate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }


    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getCompanyaliasname() {
        return companyaliasname;
    }

    public void setCompanyaliasname(String companyaliasname) {
        this.companyaliasname = companyaliasname;
    }

    public String getProductimage1() {
        return productimage1;
    }

    public void setProductimage1(String productimage1) {
        this.productimage1 = productimage1;
    }

    public String getProductimage2() {
        return productimage2;
    }

    public void setProductimage2(String productimage2) {
        this.productimage2 = productimage2;
    }

    public String getProductiondate() {
        return productiondate;
    }

    public void setProductiondate(String productiondate) {
        this.productiondate = productiondate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}