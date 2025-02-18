package myapp.jpa.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity(name = "Person")
@Table(
    name = "TPerson",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"first_name", "birth_day"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
  @NamedQuery(
      name = "findPersonsByFirstName",
      query = "SELECT p FROM Person p WHERE p.firstName LIKE :pattern"),
  @NamedQuery(
      name = "findPersonsByCarModel",
      query = "SELECT p FROM Person p JOIN p.cars c WHERE c.model LIKE :model")
})
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

    @OneToMany(//
            fetch = FetchType.LAZY, //
            mappedBy = "owner", //
            cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE } //
    )
    @OrderBy("immatriculation ASC")
    private Set<Car> cars;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    // @JoinTable est optionnelle (afin de préciser les tables)
    @JoinTable(
            name = "Person_Movie",
            joinColumns = { @JoinColumn(name = "id_person") },
            inverseJoinColumns = { @JoinColumn(name = "id_movie") }
    )
    @ToString.Exclude
    private Set<Movie> movies;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cv_id")
    private CV cv;

    @Version
    private long version = 0;

    @Transient
    public static long updateCounter = 0;

    public Person(String firstName, Date birthDay) {
        this(0, firstName, null, birthDay, null, null, null, null, null, 0);
    }

    public Person(String firstName, String secondName, Date birthDay) {
        this(0, firstName, secondName,birthDay, null, null, null, null, null, 0);
    }

    public Person(String firstName, String secondName, Date birthDay, Address address1) {
        this(0, firstName, secondName,birthDay, address1, null, null, null, null, 0);
    }

    public Person(String firstName, String secondName, Date birthDay, Address address1, Address address2) {
        this(0, firstName, secondName,birthDay, address1, address2, null, null, null, 0);
    }

    public void addCar(Car c) {
        if (cars == null) {
            cars = new HashSet<>();
        }
        cars.add(c);
        c.setOwner(this);
    }

    public void addMovie(Movie movie) {
        if (movies == null) {
            movies = new HashSet<>();
        }
        movies.add(movie);
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