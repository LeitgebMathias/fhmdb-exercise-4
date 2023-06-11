package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;

import java.util.UUID;

public class MovieAPIRequestBuilder {

    public static final char DELIMITER = '&';
    public static final char QUERY_SIGN_CHAR = '?';
    public static final String QUERY_SIGN_STRING = "?";

    private StringBuilder movieAPIRequest;

    public MovieAPIRequestBuilder(String baseURL){
        movieAPIRequest = new StringBuilder(baseURL);
    }

    public MovieAPIRequestBuilder setQuery(String query) {
        if(query != null && !query.isEmpty()){
            if(movieAPIRequest.indexOf(QUERY_SIGN_STRING) == -1) {
                movieAPIRequest.append(QUERY_SIGN_STRING);
            }
            movieAPIRequest.append("query=").append(query).append(DELIMITER);
        }
        return this;
    }

    public MovieAPIRequestBuilder setGenre(Genre genre) {
        if(genre != null){
            if(movieAPIRequest.indexOf(QUERY_SIGN_STRING) == -1){
                movieAPIRequest.append(QUERY_SIGN_STRING);
            }
            movieAPIRequest.append("genre=").append(genre).append(DELIMITER);
        }
        return this;
    }

    public MovieAPIRequestBuilder setReleaseYear(String releaseYear) {
        if(releaseYear != null) {
            if(movieAPIRequest.indexOf(QUERY_SIGN_STRING) == -1){
                movieAPIRequest.append(QUERY_SIGN_STRING);
            }
            movieAPIRequest.append("releaseYear=").append(releaseYear).append(DELIMITER);
        }
        return this;
    }

    public MovieAPIRequestBuilder setRatingForm(String ratingForm) {
        if(ratingForm != null){
            if(movieAPIRequest.indexOf(QUERY_SIGN_STRING) == -1){
                movieAPIRequest.append(QUERY_SIGN_STRING);
            }
            movieAPIRequest.append("ratingFrom=").append(ratingForm).append(DELIMITER);
        }
        return this;
    }

    public MovieAPIRequestBuilder setID(UUID id) {
        if(id != null){
            movieAPIRequest.append("/").append(id);
        }
        return this;
    }

    public String build() {
        String finalMovieAPIRequest = movieAPIRequest.toString();
        int lastIndexOfRequest = finalMovieAPIRequest.length() - 1 ;

        if(finalMovieAPIRequest.charAt(lastIndexOfRequest) == QUERY_SIGN_CHAR ||
                finalMovieAPIRequest.charAt(lastIndexOfRequest) == DELIMITER){
            return finalMovieAPIRequest.substring(0, lastIndexOfRequest);
        }
        return finalMovieAPIRequest;
    }
}
