import activeRecord.Film;
import activeRecord.Personne;
import activeRecord.RealisateurAbsentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilm {

    @BeforeEach
    public void before() throws SQLException {
        Personne.createTable();
        Personne.remplirTable();
        Film.createTable();
        Film.remplirTable();
    }

    @AfterEach
    public void after() throws SQLException {
        Personne.deleteTable();
        Film.deleteTable();
    }

    @Test
    public void testFindById() throws SQLException {
        // Si la base de données ne change pas
        Film f1 = Film.findById(3);
        String s = f1.toString();
        assertEquals("Film{titre='Temple Maudit', id=3, id_real=1}", s);

        // Ce film n'existe pas
        Film f2 = Film.findById(6515123);
        assertNull(f2);
    }

    @Test
    public void testFindByRealisateur() throws SQLException {
        // Si la base de données ne change pas
        String s = "";
        for (Film f : Film.findByRealisateur(Personne.findById(1))) {
            s += f;
        }
        assertEquals("Film{titre='Arche perdue', id=1, id_real=1}" +
                "Film{titre='Temple Maudit', id=3, id_real=1}", s);

        // Ce réalisateur n'existe pas, donc pas de films
        s = "";
        for (Film f : Film.findByRealisateur(new Personne("wololo", "tutu"))) {
            s += f;
        }
        assertEquals("", s);
    }

    @Test
    public void testSave() throws SQLException, RealisateurAbsentException {
        Film f1 = new Film("test", Personne.findById(1));
        Film f2 = Film.findById(2);

        f1.save();
        f2.save();

        String s = "";
        for (Film p : Film.findAll()) {
            s += p;
        }
        assertEquals("Film{titre='Arche perdue', id=1, id_real=1}" +
                "Film{titre='Alien', id=2, id_real=2}" +
                "Film{titre='Temple Maudit', id=3, id_real=1}" +
                "Film{titre='Blade Runner', id=4, id_real=2}" +
                "Film{titre='Alien3', id=5, id_real=4}" +
                "Film{titre='Fight Club', id=6, id_real=4}" +
                "Film{titre='Orange Mecanique', id=7, id_real=3}" +
                "Film{titre='test', id=8, id_real=1}", s);
        assertEquals("Film{titre='test', id=8, id_real=1}", f1.toString());

        // Maintenant si le réalisateur n'existe pas
        Film f3 = new Film("machin", new Personne("le nom", "l'autre nom"));

        assertThrows(RealisateurAbsentException.class, () -> {
            f3.save();
        });
    }

    @Test
    public void testDelete() throws SQLException {
        Film p2 = Film.findById(1);

        p2.delete();

        String s = "";
        for (Film p : Film.findAll()) {
            s += p;
        }
        assertEquals("Film{titre='Alien', id=2, id_real=2}" +
                "Film{titre='Temple Maudit', id=3, id_real=1}" +
                "Film{titre='Blade Runner', id=4, id_real=2}" +
                "Film{titre='Alien3', id=5, id_real=4}" +
                "Film{titre='Fight Club', id=6, id_real=4}" +
                "Film{titre='Orange Mecanique', id=7, id_real=3}", s);
        assertEquals("Film{titre='Arche perdue', id=-1, id_real=1}", p2.toString());
    }
}