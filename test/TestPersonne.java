import activeRecord.Personne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestPersonne {

    @BeforeEach
    public void before() throws SQLException {
        Personne.createTable();
    }

    @AfterEach
    public void after() throws SQLException {
        Personne.deleteTable();
    }

    @Test
    public void testFindAll() throws SQLException {
        // Si la base de données ne change pas
        String s = "";
        for (Personne p : Personne.findAll()) {
            s += p;
        }
        assertEquals("Personne{id=1, nom='Spielberg', prenom='Steven'}" +
                "Personne{id=2, nom='Scott', prenom='Ridley'}" +
                "Personne{id=3, nom='Kubrick', prenom='Stanley'}" +
                "Personne{id=4, nom='Fincher', prenom='David'}", s);
    }

    @Test
    public void testFindById() throws SQLException {
        // Si la base de données ne change pas
        Personne p1 = Personne.findById(1);
        String s = p1.toString();
        assertEquals("Personne{id=1, nom='Spielberg', prenom='Steven'}", s);

        // Cette personne n'existe pas
        Personne p2 = Personne.findById(6515123);
        assertNull(p2);
    }

    @Test
    public void testFindByName() throws SQLException {
        // Si la base de données ne change pas
        String s = "";
        for (Personne p : Personne.findByName("Scott")) {
            s += p;
        }
        assertEquals("Personne{id=2, nom='Scott', prenom='Ridley'}", s);
    }

    @Test
    public void testSave() throws SQLException {
        Personne p1 = new Personne("test", "test");
        Personne p2 = Personne.findById(2);

        p1.save();
        p2.save();

        String s = "";
        for (Personne p : Personne.findAll()) {
            s += p;
        }
        assertEquals("Personne{id=1, nom='Spielberg', prenom='Steven'}" +
                "Personne{id=2, nom='Scott', prenom='Ridley'}" +
                "Personne{id=3, nom='Kubrick', prenom='Stanley'}" +
                "Personne{id=4, nom='Fincher', prenom='David'}" +
                "Personne{id=5, nom='test', prenom='test'}", s);
        assertEquals("Personne{id=5, nom='test', prenom='test'}", p1.toString());
    }

    @Test
    public void testDelete() throws SQLException {
        Personne p2 = Personne.findById(1);

        p2.delete();

        String s = "";
        for (Personne p : Personne.findAll()) {
            s += p;
        }
        assertEquals("Personne{id=2, nom='Scott', prenom='Ridley'}" +
                "Personne{id=3, nom='Kubrick', prenom='Stanley'}" +
                "Personne{id=4, nom='Fincher', prenom='David'}", s);
        assertEquals("Personne{id=-1, nom='Spielberg', prenom='Steven'}", p2.toString());
    }
}