class Winner {
    
    private int score;
    private String name;

    Winner() {
        this.name = "none";
        this.score = 0;
    }

    Winner(String name, int score) {
        this.name = name;
        this.score = score;
    }


    int getScore() {
        return score;
    }

    String getName() {
        return name;
    }
}
