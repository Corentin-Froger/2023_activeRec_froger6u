package activeRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Personne {
    private int id;
    private String nom, prenom;

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
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

    private void setId(int id) {
        this.id = id;
    }
}