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
import java.util.ArrayList;
import java.util.Arrays;

@Entity
@Table(name = "vendor")
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
//public Float distance;
@ManyToMany(cascade = {CascadeType.ALL})
@JoinTable(name="vendor_cuisine", 
                joinColumns={@JoinColumn(name="vendor_vendor_id")}, 
                inverseJoinColumns={@JoinColumn(name="cuisine_id")})
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

public static List<Vendor> find_page(String district, String speciality, int cost1, int cost2, String mode, String serves, String cuisine, String category, int index) {
Query query;

if(cuisine.equals("%")){
query=JPA.em().createQuery("select distinct v from Vendor v join fetch v.cuis c where c.name like :cuisine and (lower(v.speciality) like :speciality or lower(v.vname) like :speciality) and cost between :cost1 and :cost2 and lower(v.mode) like :mode and lower(v.serves) like :serves and lower(v.category) like :category order by rating desc")
                                    //.setParameter("district",district)
								   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("category","%" + category + "%")
								   .setParameter("cuisine","%" + cuisine + "%");
								   //.setMaxResults(5)
								   //.setFirstResult((index)*5);							   
}
else {
List<String> myList = new ArrayList<String>(Arrays.asList(cuisine.split(",")));
query=JPA.em().createQuery("select distinct v from Vendor v join fetch v.cuis c where c.name in :cuisine and (lower(v.speciality) like :speciality or lower(v.vname) like :speciality) and cost between :cost1 and :cost2 and lower(v.mode) like :mode and lower(v.serves) like :serves and lower(v.category) like :category order by rating desc")
								   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("category","%" + category + "%")
								   .setParameter("cuisine",myList)
								   .setMaxResults(5)
								   .setFirstResult((index)*5);
}
List<Vendor> result = query.getResultList();
return result;
}
//public static Finder<Long, Vendor>  find = new Finder (Long.class, Vendor.class);

public static List<Vendor> find(String district, String speciality, int cost1, int cost2, String mode, String serves, String cuisine, String category) {
Query query;
int index=0;
index++;
if(cuisine.equals("%")){
query=JPA.em().createQuery("select distinct v from Vendor v join fetch v.cuis c where c.name like :cuisine and (lower(v.speciality) like :speciality or lower(v.vname) like :speciality) and cost between :cost1 and :cost2 and lower(v.mode) like :mode and lower(v.serves) like :serves and lower(v.category) like :category")
								   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("category","%" + category + "%")
								   .setParameter("cuisine","%" + cuisine + "%")
								   .setMaxResults(5)
								   .setFirstResult((index)*5);							   
}
else {
List<String> myList = new ArrayList<String>(Arrays.asList(cuisine.split(",")));
query=JPA.em().createQuery("select distinct v from Vendor v join fetch v.cuis c where c.name in :cuisine and (lower(v.speciality) like :speciality or lower(v.vname) like :speciality) and cost between :cost1 and :cost2 and lower(v.mode) like :mode and lower(v.serves) like :serves and lower(v.category) like :category")
								   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("category","%" + category + "%")
								   .setParameter("cuisine",myList)
								   .setMaxResults(5)
								   .setFirstResult((index)*5);
}
List<Vendor> result = query.getResultList();
return result;
}

/*public static Page<Vendor> filter_cuisine(String cuisine) {
PageRequest page1=new PageRequest(0,10,Direction.DESC,"rating");
//return find.where().like("cuisine","%cuisine%").orderBy("Name asc").findPagingList(5).setFetchAhead(false).getPage(0);
}*/
	
public static List<Vendor> geo_find(String district, String speciality, int cost1, int cost2, String mode, String serves, String cuisine, Float lat, Float lon, String category, int index) {
Query geo_query;
if(cuisine.equals("%")){
geo_query = JPA.em().createNativeQuery("select dest.*,3956 * 2 * ASIN(SQRT( POWER(SIN((:lat - dest.latitude)* pi()/180 / 2), 2) + COS(:lat * pi()/180) *COS(dest.latitude * pi()/180) *POWER(SIN((:lon - dest.longitude) * pi()/180 / 2), 2) )) as distance FROM vendor dest where speciality like :speciality and cost between :cost1 and :cost2 and mode like :mode and serves like :serves and category like :category and vendor_id in (select vendor_vendor_id from vendor_cuisine where cuisine_id in (select id from cuisine where name like :cuisine)) having distance < 5 ORDER BY distance",Vendor.class);
                          geo_query.setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("category","%" + category + "%")
								   .setParameter("cuisine","%" + cuisine + "%")
								   .setParameter("lat",lat)
								   .setParameter("lon",lon);
								   //.setMaxResults(5)
								   //.setFirstResult((index)*5);
}
else {
List<String> myList = new ArrayList<String>(Arrays.asList(cuisine.split(",")));
geo_query = JPA.em().createNativeQuery("select dest.*,3956 * 2 * ASIN(SQRT( POWER(SIN((:lat - dest.latitude)* pi()/180 / 2), 2) + COS(:lat * pi()/180) *COS(dest.latitude * pi()/180) *POWER(SIN((:lon - dest.longitude) * pi()/180 / 2), 2) )) as distance FROM vendor dest where speciality like :speciality and cost between :cost1 and :cost2 and mode like :mode and serves like :serves and category like :category and vendor_id in (select vendor_vendor_id from vendor_cuisine where cuisine_id in (select id from cuisine where name in (:cuisine))) having distance < 5 ORDER BY distance",Vendor.class)
                                   .setParameter("speciality","%" + speciality + "%")
								   .setParameter("cost1",cost1)
								   .setParameter("cost2",cost2)
								   .setParameter("mode","%" + mode + "%")
								   .setParameter("serves","%" + serves + "%")
								   .setParameter("category","%" + category + "%")
								   .setParameter("cuisine",myList)
								   .setParameter("lat",lat)
								   .setParameter("lon",lon);
								   //.setMaxResults(5)
								   //.setFirstResult((index)*5);
}								   
List<Vendor> result1 = geo_query.getResultList();
return result1;
}

public static List<Vendor> search(String district, String speciality) {
Query search_query=JPA.em().createQuery("select distinct v from Vendor v where lower(v.district) like :district and (lower(v.speciality) like :speciality or lower(v.vname) like :speciality) order by v.vname")
                                   .setParameter("district","%" + district + "%")
								   .setParameter("speciality","%" + speciality + "%");
List<Vendor> result2 = search_query.getResultList();
return result2;								   
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