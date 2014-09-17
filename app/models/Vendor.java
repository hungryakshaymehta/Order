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
import javax.persistence.*;
//import com.avaje.ebean.*;
import java.util.Comparator;
import play.db.jpa.Transactional;
import play.db.jpa.JPA.*;
import play.db.jpa.*;
import javax.persistence.EntityManager.*;
import javax.persistence.ManyToMany;
import org.hibernate.transform.AliasToEntityMapResultTransformer.*;
import org.hibernate.transform.RootEntityResultTransformer.*; 
import org.hibernate.transform.ResultTransformer.*;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

@Entity
public class Vendor implements Comparable<Vendor> {
@Id
public Long vendor_id;
public String vname;
public String category;
public String district;
public String city;
public Long contact;
public String speciality;
public String cuisine;
public int cost;
public String serves;
public String mode;
public Float rating;
public byte[] picture;
public Float latitude;
public Float longitude;
@ManyToMany
public List<Cuisine> cuis = new ArrayList<Cuisine>();

public Vendor() {}

public Vendor(String vname, String category, String district, String city, Long contact, String speciality, int cost, String serves, String mode, Float rating, Float distance) {
this.vname=vname;
this.category=category;
this.district=district;
this.city=city;
this.contact=contact;
this.speciality=speciality;
//this.cuisine=cuisine;
this.cost=cost;
this.serves=serves;
this.mode=mode;
this.rating=rating;
}

public String ToString() {
return String.format("%s - %s", vname);
}

//public static Finder<Long, Vendor>  find = new Finder (Long.class, Vendor.class);

public static List<Vendor> find(String district, String speciality, int cost1, int cost2, String mode, String serves, String cuisine) {
/*Query query = JPA.em().createNativeQuery("select * from vendor where district like :district and speciality like :speciality and cost between :cost1 and :cost2 and mode like :mode and serves like :serves and cuisine like :cuisine order by rating desc",Vendor.class)
                                   .setParameter("district","%" + district + "%")
								   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("cuisine","%" + cuisine + "%");*/
/*Query query = JPA.em().createNativeQuery("select * from vendor v join vendor_cuisine vc on v.vendor_id=vc.vendor_vendor_id join cuisine on vc.cuisine_id=cuisine.id where cuisine.name like :cuisine",Vendor.class)*/
Query query = JPA.em().createNativeQuery("select * from vendor where district like :district and speciality like :speciality and cost between :cost1 and :cost2 and mode like :mode and serves like :serves and vendor_id in (select vendor_vendor_id from vendor_cuisine where cuisine_id in (select id from cuisine where name like :cuisine))",Vendor.class)
.setParameter("district","%" + district + "%")
								   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
.setParameter("cuisine","%" + cuisine + "%");
//.setResultTransformer(Transformers.aliasToBean(Vendor.class));
//query.setResultTransformer ( new AliasToBeanResultTransformer(Vendor.class));*/
List<Vendor> result = query.getResultList();
return result;
}

/*public static Page<Vendor> filter_cuisine(String cuisine) {
//public static List<Vendor> filter_cuisine(String cuisine) {
return find.where().like("cuisine","%cuisine%").orderBy("Name asc").findPagingList(5).setFetchAhead(false).getPage(0);
}
*/
	
public static List<Vendor> geo_find(String district, String speciality, int cost1, int cost2, String mode, String serves, String cuisine, Float lat, Float lon) {
Query geo_query = JPA.em().createNativeQuery("{call geodist_new(?,?,?,?,?,?,?,?,?,7)}",Vendor.class)           
                                   .setParameter(1, lon)
                                   .setParameter(2, lat)
								   .setParameter(3, cuisine)
								   .setParameter(4, district)
								   .setParameter(5, speciality)
								   .setParameter(6, mode)
								   .setParameter(7, serves)
								   .setParameter(8, cost1)
								   .setParameter(9, cost2);
List<Vendor> result1 = geo_query.getResultList();
return result1;
}

public static class costComparator implements Comparator<Vendor> {
@Override 
public int compare(Vendor v1, Vendor v2) {
        return v1.cost < v2.cost ? 1 : (v1.cost > v2.cost ? -1 : 0);
      }
    }

public static class distComparator implements Comparator<Vendor> {
@Override 
public int compare(Vendor v1, Vendor v2) {
        return v1.cost < v2.cost ? 1 : (v1.cost > v2.cost ? -1 : 0);
      }
    }
	
public static class ratingComparator implements Comparator<Vendor> {
@Override 
public int compare(Vendor v1, Vendor v2) {
        return v1.rating < v2.rating ? 1 : (v1.rating > v2.rating ? -1 : 0);
      }
    }

@Override
    public int compareTo(Vendor o) {
        return this.cost > o.cost ? 1 : (this.cost < o.cost ? -1 : 0);
    }
}