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
public static List<String> cuisines;
public static int clause=0;
	
private static Form<Search> searchForm=Form.form(Search.class);
private static Form<Cuisine> cuisineForm=Form.form(Cuisine.class);
private static Form<Vendor> vendorForm=Form.form(Vendor.class);
	
    //public static  Page<Vendor> vendors;
	public static List<Vendor> vendors = new ArrayList<Vendor>();
	//public static List<object[]> vendors = new ArrayList<object[]>();
	
	@Transactional
    public static Result index() {
	//return redirect(routes.Order.list());
	return ok(views.html.geo.render());
    }
	
	
	@Transactional
	public static Result list() {
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	cuisines = Cuisine.find();
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	@Transactional
	public static Result search() {
	Form<Search> boundForm=searchForm.bindFromRequest();
	Search search=boundForm.get();
	searchForm=searchForm.fill(search);
	ddistrict=search.district;
	dspeciality=search.speciality;
	vendors=Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	@Transactional
	public static Result filter() {
	Form<Vendor> boundForm=vendorForm.bindFromRequest();
	Vendor vend=boundForm.get();
	vendorForm=vendorForm.fill(vend);
	ddistrict=vend.district;
	dspeciality=vend.speciality;
	vendors=Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	@Transactional
	public static Result filter_serves(String serves) {
	dserves=serves;
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	@Transactional
	public static Result filter_cost(int cost1,int cost2) {
	dcost1=cost1;
	dcost2=cost2;
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	@Transactional
	public static Result filter_cuisine(String cuisine) {
	dcuisine=cuisine;
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	@Transactional
	public static Result filter_res() {
	Form<Cuisine> boundForm=cuisineForm.bindFromRequest();
	if(boundForm.hasErrors()){
	flash("error", "Please correct errors above.");
	return badRequest(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	else {
	Cuisine cui=boundForm.get();
	flash("success", "filtered");
	cui.cuisines.removeAll(Collections.singleton(null));
	cuisineForm=cuisineForm.fill(cui);
    for(String t: cui.cuisines) {
	if(t.equals("")){}
	else {
	//clause += t + "";
	clause=cui.cuisines.size();
	}
	}
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}}
	
	@Transactional
	public static Result filter_mode(String mode) {
	dmode=mode;
	vendors = Vendor.find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	public static Result sort(String sort_option) {
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
	message=sort_option;
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	public static Result javascriptRoutes() {
    response().setContentType("text/javascript");
    return ok(
        Routes.javascriptRouter("jsRoutes",
            controllers.routes.javascript.Order.sort(),
			controllers.routes.javascript.Order.list()
        )
    );
}
    @Transactional
    public static Result geo(Float lat, Float lon) {
	cuisines = Cuisine.find();
	vendors = Vendor.geo_find(ddistrict,dspeciality,dcost1,dcost2,dmode,dserves,dcuisine,lat,lon);
	return ok(views.html.search.render(vendors,searchForm,cuisines,vendorForm,clause,cuisineForm));
	}
	
	public static Result showMap() {
	Form<Vendor> boundForm=vendorForm.bindFromRequest();
	Vendor location=boundForm.get();
	Float lati=location.latitude;
	Float longi=location.longitude;
	return ok(views.html.showmap.render(longi,lati));
}
}