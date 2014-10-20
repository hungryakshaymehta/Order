package controllers;
import com.google.common.io.Files;
import play.*;
import play.mvc.*;
import views.html.*;
import models.*;
import java.util.*;
import java.io.File.*;
import java.io.*;
import java.io.IOException;
import play.data.validation.Constraints.*;
import play.data.Form;
import static play.data.Form.form;
import play.mvc.Http.*;
import  play.mvc.Http.MultipartFormData;
//import play.db.ebean.Model;
import javax.persistence.*;
import javax.persistence.OneToMany;
//import com.avaje.ebean.*;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import play.db.jpa.Transactional;
import play.db.jpa.JPA.*;
import play.db.jpa.*;
//import utils.GeolocationHelper;
//import com.edulify.modules.geolocation.Geolocation;
//import com.edulify.modules.geolocation.InvalidAddressException;

public class Order extends Controller {

public static String ddistrict="%";
public static String dspeciality="%";
public static String dcuisine="%";
public static int dcost1=0;
public static int dcost2=10000;
public static String dtype="%";
public static String dmode="%";
public static String dserves="%";
public static String message="distance";
public static Float dlatitude=99999.9f;
public static Float latitude=99999.9f;
public static Float dlongitude=99999.9f;
public static String dcategory="%";
public static String clause="";
public static int dindex=1;
public static String sorts="rating";
public static List<String> cuisines;
private static Form<Search> searchForm=Form.form(Search.class);
private static Form<Cuisine> cuisineForm=Form.form(Cuisine.class);
private static Form<Vendor> vendorForm=Form.form(Vendor.class);
public static Arrays cuisns[] = new Arrays[20];	
    //public static  Page<Vendor> vendors;
	public static List<Vendor> vendors = new ArrayList<Vendor>();
	
	@Transactional
    public static Result index() {
	//return redirect(routes.Order.list());
	return ok(views.html.geo.render());
    }
	
	@Transactional
	public static Result page(int index) {
	dindex=index;
	/*if(dlatitude.equals(latitude)){
	vendors = Vendor.find_page(sorts,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory,dindex);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	//sort(sorts);*/
	//cuisines = Cuisine.find();
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	@Transactional
	public static Result list() {
	vendors = Vendor.find_page(sorts,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory,dindex);
	cuisines = Cuisine.find();
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	@Transactional
	public static Result search() {
	dcuisine="%";
	dcost1=0;
	dcost2=10000;
	dserves="%";
	dcategory="%";
	dtype="%";
	dmode="%";
	dindex=1;
	Form<Search> boundForm=searchForm.bindFromRequest();
	Search search=boundForm.get();
	searchForm=searchForm.fill(search);
	//ddistrict=search.district;
	dlatitude=search.latitude;
	dlongitude=search.longitude;
	dspeciality=search.speciality;
	if(dlatitude == null){
	return badRequest(views.html.search.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	else {
	System.out.println("latitude:" + dlatitude + "longitude:" + dlongitude);
	cuisines = Cuisine.find();
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	/*if(ddistrict!=null) {
	try {
        Geolocation geolocation = GeolocationHelper.getGeolocation(addr);
        if (geolocation == null) {
          return badRequest(views.html.search.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
        }
		dlatitude=geolocation.getLatitude();
		dlongitude=geolocation.getLongitude();
        /*return ok(geoData.render(addr,
                                 geolocation.getIp(),
                                 geolocation.getCountryCode(),
                                 geolocation.getCountryName(),
                                 geolocation.getRegionCode(),
                                 geolocation.getRegionName(),
                                 geolocation.getCity(),
                                 geolocation.getLatitude(),
                                 geolocation.getLongitude()));
      } catch (InvalidAddressException ex) {
        return ok(index.render("Invalid ip"));
      }
	  vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	  }*/
	//vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	}

	@Transactional
	public static Result filter() {
	Form<Vendor> boundForm=vendorForm.bindFromRequest();
	Vendor vend=boundForm.get();
	vendorForm=vendorForm.fill(vend);
	ddistrict=vend.district;
	dspeciality=vend.speciality;
	vendors=Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	@Transactional
	public static Result filter_serves(String serves) {
	dserves=serves;
	dindex=0;
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	sort(sorts);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	@Transactional
	public static Result filter_location(String location) {
	ddistrict=location;
	dlatitude=latitude;
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	
	@Transactional
	public static Result filter_item(String item) {
	dspeciality=item;
	dindex=0;
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	sort(sorts);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	
	@Transactional
	public static Result filter_cost(int cost1,int cost2) {
	dcost1=cost1;
	dcost2=cost2;
	dindex=0;
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	sort(sorts);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	@Transactional
	public static Result filter_cuisine(String cuisin) {
	/*String[] cuisArray = cuisin.split(",");
	clause="";
	for(int i=0;i<cuisArray.length;i++){
	if(i==0)
	clause += cuisArray[i];
	else
	clause += " or name like " + cuisArray[i];
	}*/
	dcuisine=cuisin;
	dindex=0;
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find_page(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory,dindex);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	sort(sorts);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
		
	
	
	@Transactional
	public static Result filter_category(String cat) {
	dcategory=cat;
	dindex=0;
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	sort(sorts);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	
	@Transactional
	public static Result list_sel() {
	dindex=0;
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	cuisines = Cuisine.find();
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	
	@Transactional
	public static Result filter_mode(String mode) {
	dindex=0;
	dmode=mode;
	if(dlatitude.equals(latitude)){
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dcategory);
	}
	else {
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	}
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	public static Result sort(String sort_option) {
	sorts=sort_option;
	dindex=1;
	if(sort_option.equals("cost_desc")) {
	Collections.sort(vendors, new Vendor.costComparator());
	}
	else if(sort_option.equals("rating")) {
	Collections.sort(vendors, new Vendor.ratingComparator());
	}
	else if(sort_option.equals("distance")){
	Collections.sort(vendors, new Vendor.distComparator());
	}
	else {
	Collections.sort(vendors);
	}
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	public static Result javascriptRoutes() {
    response().setContentType("text/javascript");
    return ok(
        Routes.javascriptRouter("jsRoutes",
            controllers.routes.javascript.Order.sort(),
			controllers.routes.javascript.Order.list(),
			controllers.routes.javascript.Order.filter_cuisine(),
			controllers.routes.javascript.Order.filter_cost(),
			controllers.routes.javascript.Order.filter_mode(),
			controllers.routes.javascript.Order.filter_serves(),
			controllers.routes.javascript.Order.filter_category(),
			controllers.routes.javascript.Order.filter_location(),
			controllers.routes.javascript.Order.filter_item(),
			controllers.routes.javascript.Order.geo(),
			controllers.routes.javascript.Order.page(),
			controllers.routes.javascript.Order.geo_def()
        )
    );
}

    @Transactional
    public static Result geo(Float lat, Float lon) {
	cuisines = Cuisine.find();
	ddistrict="%";
	dlatitude=lat;
	dlongitude=lon;
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	return ok(views.html.list_sel.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	@Transactional
    public static Result geo_def(Float lat, Float lon) {
	cuisines = Cuisine.find();
	ddistrict="%";
	dlatitude=lat;
	dlongitude=lon;
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,dlatitude,dlongitude,dcategory,dindex);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,dindex,cuisineForm));
	}
	
	public static Result showMap() {
	Form<Vendor> boundForm=vendorForm.bindFromRequest();
	Vendor location=boundForm.get();
	Float lati=location.latitude;
	Float longi=location.longitude;
	return ok(views.html.showmap.render(longi,lati));
}
}