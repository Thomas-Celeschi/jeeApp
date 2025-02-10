package myapp.jpa.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LicenceUE extends UE {

  @Basic private String description;

  public LicenceUE(String code, int ects, String description) {
    super(code, ects);
    this.description = description;
  }
}
