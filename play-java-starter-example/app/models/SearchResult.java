package models;

import java.util.List;

public class SearchResult {

    String searchQuery;
    List<Movie> movies;

    public SearchResult(String searchQuery, List<Movie> movies) {
        this.searchQuery = searchQuery;
        this.movies = movies;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
