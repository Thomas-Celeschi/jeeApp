package myapp.jpa.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MasterUE extends UE {

  @Basic
  @Column(name = "master_name")
  private String masterName;

  public MasterUE(String code, int ects, String masterName) {
    super(code, ects);
    this.masterName = masterName;
  }
}
