package myapp.jpa.model;

import java.util.Date;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(name = "Person")
@Table(name = "TPerson",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "first_name", "birth_day"
                })
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name="findPersonsByFirstName", query="SELECT p FROM Person p WHERE p.firstName LIKE :pattern")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic(optional = false, fetch = FetchType.EAGER)
    @Column(name = "first_name", length = 200)
    private String firstName;

    @Basic(optional = true, fetch = FetchType.EAGER)
    @Column(name = "second_name", length = 100, nullable = true, unique = true)
    private String secondName;

    @Basic()
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_day")
    private Date birthDay;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "street_2")),
            @AttributeOverride(name = "city", column = @Column(name = "city_2")),
            @AttributeOverride(name = "country", column = @Column(name = "country_2"))
    })
    private Address address2;


    @Version
    private long version = 0;

    @Transient
    public static long updateCounter = 0;

    public Person(String firstName, Date birthDay) {
        this(0, firstName, null, birthDay, new Address(), new Address(), 0);
    }

    public Person(String firstName, String secondName, Date birthDay) {
        this(0, firstName, secondName,birthDay, new Address(), new Address(), 0);
    }

    public Person(String firstName, String secondName, Date birthDay, Address address1) {
        this(0, firstName, secondName,birthDay, address1, new Address(), 0);
    }

    public Person(String firstName, String secondName, Date birthDay, Address address1, Address address2) {
        this(0, firstName, secondName,birthDay, address1, address2, 0);
    }


    @PreUpdate
    public void beforeUpdate() {
        System.err.println("PreUpdate of " + this);
    }

    @PostUpdate
    public void afterUpdate() {
        System.err.println("PostUpdate of " + this);
        updateCounter++;
    }

}