package myapp.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SpringNameDao {

    @Autowired
    JdbcTemplate jt;

    // Nouvelle version

    @PostConstruct
    public void initSchema() throws SQLException {
        var query = "create table if not exists NAME (" //
                + " id integer not null, " //
                + " name varchar(50) not null, " //
                + " primary key (id) )";
        jt.execute(query);
    }

    public void addName(Name name) {
        var query = "insert into NAME values (?,?)";
        jt.update(query, name.getId(), name.getName());
    }

    public void deleteName(int id) {
        var query = "Delete From NAME where (id = ?)";
        jt.update(query, id);
    }

    public Name findName(int id) {
        var query = "Select * From NAME where (id = ?)";
        return jt.queryForObject(query, new RowMapper<Name>() {
            @Override
            public Name mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Name.nameMapper(rs);
            }
        }, id);

    }

    public Collection<Name> findNames() {
        var query = "Select * From NAME order by name";
        return jt.query(query, new RowMapper<Name>() {
            @Override
            public Name mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Name.nameMapper(rs);
            }
        });

    }

    public void updateName(int id, String name) {
        var query = "Update NAME Set (name = ?) where (id = ?)";
        jt.update(query, id, name);
    }

    public void longWork() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
    }

    public void addNameTwoTimes(int id, String name) {
        var query = "insert into NAME values (?,?)";
        jt.update(query, id, name);
        jt.update(query, id, name);
    }

    public int countNames(String pattern) {
        String query = "SELECT COUNT(*) FROM NAME WHERE name LIKE ?";
        try {
            return jt.queryForObject(query, new Object[] { "%" + pattern + "%" }, Integer.class);
        } catch (NullPointerException ignored) {

        }
        return 0;
    }

}
