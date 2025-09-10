public class PlayerSave {
    private String name;
    private int score;

    public PlayerSave(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return name +" | "+score;
    }
}
