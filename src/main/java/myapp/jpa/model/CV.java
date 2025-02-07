package myapp.jpa.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CV {

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    private String title;

    public CV(String title) {
        this.title = title;
    }
}
