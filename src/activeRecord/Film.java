package activeRecord;

import java.sql.*;
import java.util.ArrayList;

/**
 * Classe représentant un Film
 */
public class Film {
    private String titre;
    private int id, id_real;

    public Film(String titre, Personne real) {
        this.id = -1;
        this.titre = titre;
        this.id_real = real.getId();
    }

    private Film(String titre, int id, int id_real) {
        this.titre = titre;
        this.id = id;
        this.id_real = id_real;
    }

    public void save() throws SQLException, RealisateurAbsentException {
        // On vérifie si le réalisateur est absent
        if (id_real == -1) {
            throw new RealisateurAbsentException();
        }

        // On vérifie si le film est créée par java ou non
        if (id == -1) {
            this.saveNew();
        } else {
            this.update();
        }
    }

    private void saveNew() throws SQLException {
        String insert = "INSERT INTO Film (titre, id_rea) VALUES (?, ?);";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, this.titre);
        stmt.setInt(2, this.id_real);
        stmt.executeUpdate();

        // Màj de l'id de l'objet
        int autoInc = -1;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            autoInc = rs.getInt(1);
        }
        this.setId(autoInc);
    }

    private void update() throws SQLException {
        String update = "update Film set id_rea=?, titre=? where id=?;";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(update);
        stmt.setInt(1, this.id_real);
        stmt.setString(2, this.titre);
        stmt.setInt(3, this.id);
        stmt.executeUpdate();
    }

    public void delete() throws SQLException {
        // On ne peut supprimer le film que s'il est présent dans la table
        if (this.id != -1) {
            PreparedStatement prep = DBConnection.getConnection().prepareStatement("DELETE FROM Film WHERE id=?");
            prep.setInt(1, this.id);
            prep.execute();

            this.id = -1;
        }
    }

    public static void createTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String create = "CREATE TABLE Film ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "TITRE varchar(40) NOT NULL, " + "ID_REA varchar(40) DEFAULT NULL, " + "PRIMARY KEY (ID))";

        Statement stmt = connect.createStatement();

        stmt.executeUpdate(create);
    }

    public static void remplirTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String insert = """
                INSERT INTO `Film` (`id`, `titre`, `id_rea`) VALUES
                (1, 'Arche perdue', 1),
                (2, 'Alien', 2),
                (3, 'Temple Maudit', 1),
                (4, 'Blade Runner', 2),
                (5, 'Alien3', 4),
                (6, 'Fight Club', 4),
                (7, 'Orange Mecanique', 3);
                """;

        Statement stmt = connect.createStatement();

        stmt.executeUpdate(insert);
    }

    public static void deleteTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String drop = "DROP TABLE Film";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    public static Film findById(int id) throws SQLException {
        String SQLPrep = "SELECT * FROM Film WHERE id = ?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        if (rs.next()) {
            String titre = rs.getString("titre");
            int id_rea = rs.getInt("id_rea");

            return new Film(titre, id, id_rea);
        } else {
            return null;
        }
    }

    public static ArrayList<Film> findByRealisateur(Personne p) throws SQLException {
        ArrayList<Film> l = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Film WHERE id_rea = ?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, p.getId());
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            int id_rea = rs.getInt("id_rea");

            l.add(new Film(titre, id, id_rea));
        }
        return l;
    }

    public static ArrayList<Film> findAll() throws SQLException {
        ArrayList<Film> l = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Film;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            int id_rea = rs.getInt("id_rea");

            l.add(new Film(titre, id, id_rea));
        }
        return l;
    }

    public Personne getRealisateur() throws SQLException {
        return Personne.findById(this.id_real);
    }

    public String getTitre() {
        return titre;
    }

    public int getId() {
        return id;
    }

    public int getId_real() {
        return id_real;
    }

    private void setId(int id) {
        this.id = id;
    }

    public void setTitre(String t) {
        this.titre = t;
    }

    @Override
    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", id=" + id +
                ", id_real=" + id_real +
                '}';
    }
}