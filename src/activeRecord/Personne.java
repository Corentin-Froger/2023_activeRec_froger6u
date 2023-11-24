package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Personne {
    private int id;
    private String nom, prenom;

    /**
     * Constructeur
     * @param nom nom de la personne
     * @param prenom prénom de la personne
     */
    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    /**
     * Renvoie l'id
     * @return l'id de la personne
     */
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    /**
     * Sauvegarde l'objet personne dans la base de données,
     * si l'objet existait déjà, on update, sinon, on l'insert
     * @throws SQLException
     */
    public void save() throws SQLException {
        // On vérifie si la personne est créée par java ou non
        if (id == -1) {
            this.saveNew();
        } else {
            this.update();
        }
    }

    /**
     * insert la personne dans la bdd
     * @throws SQLException
     */
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

    /**
     * màj la personne dans la bdd
     * @throws SQLException
     */
    private void update() throws SQLException {
        String update = "update Personne set nom=?, prenom=? where id=?;";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(update);
        stmt.setString(1, this.nom);
        stmt.setString(2, this.prenom);
        stmt.setInt(3, this.id);
        stmt.executeUpdate();
    }

    /**
     * supprimme la personne de la bdd uniquement
     * si elle en provient
     * @throws SQLException
     */
    public void delete() throws SQLException {
        // On ne peut supprimer la personne que si elle est présente ans la table
        if (this.id != -1) {
            PreparedStatement prep = DBConnection.getConnection().prepareStatement("DELETE FROM Personne WHERE id=?");
            prep.setInt(1, this.id);
            prep.execute();

            this.id = -1;
        }
    }

    /**
     * recréé la table personne
     * @throws SQLException
     */
    public static void createTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String create = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";

        /*String insert = """
                INSERT INTO `Personne` (`id`, `nom`, `prenom`) VALUES
                (1, 'Spielberg', 'Steven'),
                (2, 'Scott', 'Ridley'),
                (3, 'Kubrick', 'Stanley'),
                (4, 'Fincher', 'David');
                """;*/

        Statement stmt = connect.createStatement();

        stmt.executeUpdate(create);
        //stmt.executeUpdate(insert);
    }

    public static void remplirTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String insert = """
                INSERT INTO `Personne` (`id`, `nom`, `prenom`) VALUES
                (1, 'Spielberg', 'Steven'),
                (2, 'Scott', 'Ridley'),
                (3, 'Kubrick', 'Stanley'),
                (4, 'Fincher', 'David');
                """;

        Statement stmt = connect.createStatement();

        stmt.executeUpdate(insert);
    }

    /**
     * supprime la table personne
     * @throws SQLException
     */
    public static void deleteTable() throws SQLException {
        Connection connect = DBConnection.getConnection();

        String drop = "DROP TABLE Personne";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    /**
     * Renvoie une liste contenant toutes les personnes de la bdd
     * @return une liste
     * @throws SQLException
     */
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

    /**
     * Renvoie toutes les personnes en fonction de leur id
     * @param id l'id de la personne
     * @return
     * @throws SQLException
     */
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

    /**
     * Renvoie toutes les personnes portant un nom donné
     * @param nom le nom de la (ou les) personnes
     * @return
     * @throws SQLException
     */
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

    /**
     * modifie l'id
     * @param id l'id
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Modifie le nom
     * @param nom le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @param prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
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