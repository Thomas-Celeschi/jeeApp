package myapp.jdbc;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hsqldb.jdbc.JDBCResultSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;

    public static Name nameMapper(ResultSet rs) throws SQLException {
        var n = new Name();
        n.setId(rs.getInt("id"));
        n.setName(rs.getString("name"));
        return n;
    }

}
