package myapp.jpa.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UE {

  @Id private String code;

  @Basic private int ects;
}
