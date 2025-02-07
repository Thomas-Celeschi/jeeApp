package myapp.jpa.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "immatriculation")
public class Car {

    // properties
    @Id
    private String immatriculation;

    @Basic(optional = false)
    private String model;

    @ManyToOne(optional = true)
    @JoinColumn(name = "owner_id") // optionnelle
    @ToString.Exclude // afin d'éviter les boucles
    private Person owner;

    public Car(String immatriculation, String model) {
        this(immatriculation, model, null);
    }

}