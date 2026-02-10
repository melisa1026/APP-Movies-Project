package services;

import com.google.gson.reflect.TypeToken;
import models.Movie;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.SearchResult;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import com.typesafe.config.Config;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class TMDbService {

    public final String apiKey;
    private final WSClient ws;
    private final String baseUrl;
    private String jsonQueryList = "";

    // cache to save search queries
    private final Map<String, SearchResult> cache = new ConcurrentHashMap<>();

    ArrayList<SearchResult> allSearches;

    // contructor
    @Inject
    public TMDbService(Config config, WSClient ws) {
        this.apiKey = config.getString("tmdb.api.key").trim();
        this.ws = ws;
        this.baseUrl = "https://api.themoviedb.org/3";

        allSearches = new ArrayList<SearchResult>();
    }

    public CompletionStage<SearchResult> search(String query, String categoryQuery) {

        // update the query list (to save to the session)
        updateQueryList(query);

        // if the query is already in the cache, use it
        if (cache.containsKey(query)) {
            System.out.println("Cache hit: " + cache.get(query));
            return CompletableFuture.completedFuture(cache.get(query));
        }

        // otherwise, make an API call to search
        return apiSearch(query, categoryQuery);


    }


    CompletionStage<SearchResult> apiSearch(String query, String categoryQuery) {

        System.out.println("Making API call...");

        String url = baseUrl + "/search/" + categoryQuery + "?query=" + query;
        WSRequest request = ws.url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Accept", "application/json");

        return request.get().thenApply(response -> {
            if (response.getStatus() != 200) {
                System.err.println("Failed to fetch data from TBDb API: " + response.getStatusText());
                return new SearchResult("Failed to fetch results", Collections.emptyList());
            }
            else {
                // save the list of videos
                List<Movie> movies = parseSearchResponse(response.asJson());
                SearchResult results = new SearchResult(query, movies);

                // add the results to the cache
                cache.put(query, results);

                return results;
            }});
    }

    void updateQueryList(String query) {

        List<String> queryList = new Gson().fromJson(jsonQueryList, new TypeToken<List<String>>() {}.getType());

        if (queryList == null)
            queryList = new ArrayList<>();

        // put the query at the front of the list
        if (queryList.contains(query))
            queryList.remove(query);
        queryList.add(0, query);

        // limit the list to 10
        final List<String> finalQueryList = queryList.stream().limit(10).collect(Collectors.toList());

        // save it as a json string (to save to the session)
        jsonQueryList = new Gson().toJson(finalQueryList);

    }

    public List<Movie> parseSearchResponse(JsonNode json) {
        ArrayNode items = (ArrayNode) json.get("results");
        return StreamSupport.stream(items.spliterator(), false)
                .map(item -> {
                    String title = item.at("/title").asText();
                    String original_language = item.at("/original_language").asText();
                    String release_date = item.at("/release_date").asText();;
                    String genre_id_strings = item.at("/genre_ids").asText();
                    double vote_average = item.at("/vote_average").asDouble();
                    int id = item.at("/id").asInt();

                    int[] genre_ids = new int[1];

                    return new Movie(title, original_language, release_date, genre_ids, vote_average);
                })
                .collect(Collectors.toList());
    }

    public ArrayList<SearchResult> getAllSearches(SearchResult newQuery) {

        // the query is already in the list, remove it, and put it back to the top
        for(SearchResult query : allSearches) {
            if(query.getSearchQuery().equals(newQuery.getSearchQuery())) {
                allSearches.remove(query);
                break;
            }
        }

        allSearches.add(0, newQuery);

        return allSearches;
    }

    public String getJsonQueryList() { return jsonQueryList; }

    SearchResult checkCache(String query) {
        return cache.get(query);
    }

    void addToCache(String query, SearchResult searchResult) {
        cache.put(query, searchResult);
    }

    boolean checkIfCacheContainsQuery(String query) {
        return cache.containsKey(query);
    }
}
