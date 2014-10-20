package models;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import play.api.*;
import play.data.validation.Constraints;
import java.util.*;
import play.mvc.PathBindable;
import play.libs.F.Option;
import javax.persistence.*;
//import play.db.ebean.Model;
import javax.persistence.*;
//import play.db.ebean.Model;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import play.db.jpa.Transactional;
import play.db.jpa.JPA.*;
import play.db.jpa.*;
import javax.persistence.EntityManager.*;
import java.util.ArrayList;
import java.util.Arrays;

@Entity
@Table(name = "cuisine")
public class Cuisine {
@Id
public Long id;
public String name;
@ManyToMany(mappedBy="cuis")
public List<Vendor> vendors;
public static List<String> cuisines;
private static List<Cuisine> cuis = new ArrayList<Cuisine>();
public static Arrays cuisns[] = new Arrays[20];
public static String cuisin;

//public static Finder<Long, Cuisine>  find = new Finder (Long.class, Cuisine.class);

public Cuisine() {
}

public Cuisine(Long id, String name, Collection<Vendor> vendors) {
this.id=id;
this.name=name;
this.vendors=new LinkedList<Vendor>(vendors);
for(Vendor vendor:vendors) {
vendor.cuis.add(this);
}
}

public static List<String> find() {
Query query = JPA.em().createNativeQuery("select name from cuisine order by 1");
cuisines = query.getResultList();
return cuisines;
}
}