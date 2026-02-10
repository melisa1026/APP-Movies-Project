package models;

public class Movie {

    public String title, original_language, release_date;
    public int[] genre_ids;
    public int id;
    public double vote_average;

    public Movie(String title, String original_language, String release_date, int[] genre_ids, double vote_average) {
        this.title = title;
        this.original_language = original_language;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.vote_average = vote_average;
        this.id = id;
    }




    // Getters and setters:
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
}
