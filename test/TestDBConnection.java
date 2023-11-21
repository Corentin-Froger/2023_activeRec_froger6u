import activeRecord.DBConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestDBConnection {

    @Test
    public void testGetInstance() throws SQLException {
        Connection c1 = DBConnection.getConnection();
        Connection c2 = DBConnection.getConnection();
        Connection c3 = DBConnection.getConnection();

        // Toutes les connections doivent être à la meme adresse mémoire donc
        assert c1 == c2;
        assert c2 == c3;
        assert c1 == c3;

        // c1 et c2 ne doivent pas être connectés à la même bdd
        DBConnection.setNomDB("test");

        c2 = DBConnection.getConnection();

        assert c1 != c2;
    }
}