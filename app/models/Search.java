package models;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import play.api.*;
import play.data.validation.Constraints;
import play.mvc.PathBindable;
import play.libs.F.Option;
import javax.persistence.*;
//import play.db.ebean.Model;
import javax.persistence.OneToMany;
//import com.avaje.ebean.*;
import play.db.jpa.Transactional;
import play.db.jpa.JPA.*;
import play.db.jpa.*;

public class Search extends Vendor {

public Search() {}

public Search(String city, String speciality) {
super.city=city;
super.speciality=speciality;
super.picture=picture;
}
public static List<Vendor> filter_search(String city, String speciality) {
//return find.where().icontains("city",city).icontains("speciality",speciality).orderBy("Name asc").findList();
Query query2 = JPA.em().createNativeQuery("{call geodist_new(?,?,7)}",
                                   Vendor.class)           
                                   .setParameter(1, 0)
                                   .setParameter(2, 0);

List<Vendor> result2 = query2.getResultList();
return result2;
}
}