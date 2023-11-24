package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Personne {
    private int id;
    private String nom, prenom;

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void save() throws SQLException {
        // On vérifie si la personne est créée par java ou non
        if (id == -1) {
            this.saveNew();
        } else {
            this.update();
        }
    }

    private void saveNew() throws SQLException {
        String insert = "INSERT INTO Personne (nom, prenom) VALUES (?, ?);";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, this.nom);
        stmt.setString(2, this.prenom);
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
        String update = "update Personne set nom=?, prenom=? where id=?;";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(update);
        stmt.setString(1, this.nom);
        stmt.setString(2, this.prenom);
        stmt.setInt(3, this.id);
        stmt.executeUpdate();
    }

    public void delete() throws SQLException {
        // On ne peut supprimer la personne que si elle est présente ans la table
        if (this.id != -1) {
            PreparedStatement prep = DBConnection.getConnection().prepareStatement("DELETE FROM Personne WHERE id=?");
            prep.setInt(1, this.id);
            prep.execute();

            this.id = -1;
        }
    }

    public static void createTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String create = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";

        String insert = """
                INSERT INTO `Personne` (`id`, `nom`, `prenom`) VALUES
                (1, 'Spielberg', 'Steven'),
                (2, 'Scott', 'Ridley'),
                (3, 'Kubrick', 'Stanley'),
                (4, 'Fincher', 'David');
                """;

        Statement stmt = connect.createStatement();

        stmt.executeUpdate(create);
        stmt.executeUpdate(insert);
    }

    public static void deleteTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String drop2 = "DROP TABLE Personne";
        Statement stmt2 = connect.createStatement();
        stmt2.executeUpdate(drop2);
    }

    public static ArrayList<Personne> findAll() throws SQLException {
        ArrayList<Personne> l = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Personne;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");

            Personne p = new Personne(nom, prenom);
            p.setId(id);

            l.add(p);
        }
        return l;
    }

    public static Personne findById(int id) throws SQLException {
        String SQLPrep = "SELECT * FROM Personne WHERE id = ?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        if (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");

            Personne p = new Personne(nom, prenom);
            p.setId(id);
            return p;
        } else {
            return null;
        }
    }

    public static ArrayList<Personne> findByName(String nom) throws SQLException {
        ArrayList<Personne> l = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Personne WHERE nom = ?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setString(1, nom);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        while (rs.next()) {
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");

            Personne p = new Personne(nom, prenom);
            p.setId(id);

            l.add(p);
        }
        return l;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}