package myapp.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class NameDao {

    @Autowired
    DataSource dataSource;

    private Connection newConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @PostConstruct
    public void initSchema() throws SQLException {
        var query = "create table if not exists NAME (" //
                + " id integer not null, " //
                + " name varchar(50) not null, " //
                + " primary key (id) )";
        try (var conn = newConnection()) {
            conn.createStatement().execute(query);
        }
    }

    public void addName(int id, String name) throws SQLException {
        var query = "insert into NAME values (?,?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setInt(1, id);
            st.setString(2, name);
            st.execute();
        }
    }

    public void deleteName(int id) throws SQLException {
        var query = "Delete From NAME where (id = ?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setInt(1, id);
            st.execute();
        }
    }

    public String findName(int id) throws SQLException {
        var query = "Select * From NAME where (id = ?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setInt(1, id);
            var rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    public Collection<String> findNames() throws SQLException {
        var query = "Select * From NAME order by name";
        var result = new LinkedList<String>();
        try (var conn = newConnection()) {
            var st = conn.createStatement();
            var rs = st.executeQuery(query);
            while (rs.next()) {
                result.add(rs.getString("name"));
            }
        }
        return result;
    }

    public void updateName(int id, String name) throws SQLException {
        var query = "Update NAME Set (name = ?) where (id = ?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setString(1, name);
            st.setInt(2, id);
            st.execute();
        }
    }

    public void longWork() {
        try (var c = newConnection()) {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        } catch (SQLException e1) {
        }
    }

}
