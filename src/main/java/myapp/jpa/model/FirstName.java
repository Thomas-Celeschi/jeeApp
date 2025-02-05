package myapp.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirstName {

    private long id;
    private String firstName;

}