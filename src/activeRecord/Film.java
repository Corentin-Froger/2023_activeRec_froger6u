package activeRecord;

public class Film {
    private String titre;
    private int id, id_real;

    public Film(String titre, Personne real) {
        this.titre = titre;
        this.id_real = real.getId();
    }
}