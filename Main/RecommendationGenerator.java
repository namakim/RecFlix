import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.io.*;
import java.util.*;

import javax.sql.rowset.spi.SyncProvider;


public class RecommendationGenerator {
    // Function to fetch Netflix watch history
    // Make HTTP request to Netflix API and retrieve watch history data
    // Parse the response and return the watch history
    public static Map<String, String[]> fetchNetflixWatchHistory(String apiKey, String apiString) throws IOException, InterruptedException {
        // TODO: Implement code to fetch Netflix watch history
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiString))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "unogs-unogs-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Extracting Movie Info....");
        String responseResult = response.body().split("results")[1];
        return extractMovieInfo(responseResult);
    }
    // public static Map<String, String[]> TESTfetchNetflixWatchHistory(String apiKey) throws IOException, InterruptedException {
    //     // TODO: Implement code to fetch Netflix watch history
    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create("https://unogs-unogs-v1.p.rapidapi.com/search/titles?start_year=2022&order_by=rating&limit=5&end_year=2022&type=movie"))
    //             .header("X-RapidAPI-Key", apiKey)
    //             .header("X-RapidAPI-Host", "unogs-unogs-v1.p.rapidapi.com")
    //             .method("GET", HttpRequest.BodyPublishers.noBody())
    //             .build();

    //     HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    //     System.out.println("Extracting Movie Info....");
    //     String responseResult = response.body().split("results")[1];
    //     return extractMovieInfo(responseResult);
    // }

    public static Map<String, String[]> extractMovieInfo(String body) {
        System.out.println("Continuing with extraction...");
        Map<String, String[]> movieMap = new HashMap<>();
        String[] entries = body.split("\"title\":\"");
        for (String entry: entries) {
            if (!entry.startsWith("\":[{")) {
                String[] parts = entry.split("\"");
                String title = parts[0];
                String imdbRating = null;
                String titleType = null;
                String year = null;
                String synopsis = null;

                for (int i = 1; i < parts.length; i++) {
                    if (parts[i].equals("rating")) {
                        imdbRating = parts[i + 2];
                    } else if (parts[i].equals("title_type")) {
                        titleType = parts[i + 2];
                    } else if (parts[i].equals("year")) {
                        year = parts[i + 2];
                        break;
                    } else if (parts[i].equals("synopsis")) {
                        synopsis = parts[i + 2];
                    }
                 }

                 if (title != null && imdbRating != null && titleType != null && year != null && synopsis != null) {
                    movieMap.put(title, new String[]{imdbRating, titleType, year, synopsis});
                 } else {
                    System.out.println(title + imdbRating + titleType + year + synopsis);
                    System.out.println("Extraction Failed!");
                 }
            }
        }
        return movieMap;
    }

    // Helper Function to extract movie title
    public static String extractTitle(String body) {
        int startIndex = body.indexOf("\"title\":\"") + "\"title\":\"".length();
        int endIndex = body.indexOf("\"", startIndex);
        if (startIndex >= 0 && endIndex >= 0) {
            return body.substring(startIndex, endIndex);
        }
        return null;
    }
  
    // Function to fetch Amazon watch history
    public static String fetchAmazonWatchHistory() {
        // TODO: Implement code to fetch Amazon watch history
        // Make HTTP request to Amazon API and retrieve watch history data
        // Parse the response and return the watch history
        return null;
    }
  
    // Function to recommend unwatched TV shows and films
    public static void recommendShowsAndFilms(String netflixHistory, String amazonHistory) {
        // TODO: Implement code to recommend unwatched shows and films
        // Analyze the watch history data
        // Filter and sort recommendations based on certain criteria
        // Display the recommendations to the user
    }

    // Fucntion to prompt for User inputs for their Serach
    public static String userInputs(String titleType, String year, String person, String genre) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Recflix! This program will provide you recommendations of Netflix tv shows and movies based on your search criteria.");
        
        // Prompt user for a title type
        System.out.println("To start things off, are you interested in a tv show, movie, or both?");
        titleType = scanner.nextLine();

        // Prompt user to select which criteria to search for
        System.out.println("Great! Would you like Netflix recommandtions based on a person, genre, or year?");
        String misc = scanner.nextLine().toLowerCase();
        while(true) {
            if (misc.equals("year") || misc.equals("person") || misc.equals("title")) {
                break;
            } else {
                System.out.println("I'm sorry, I didn't catch that. Please re-enter your search criteria: person, genre, or year?");
                misc = scanner.nextLine().toLowerCase();
            }
        }
        switch (misc) {
            case "year":
                System.out.println("What year would like recommations for?");
                year = scanner.nextLine();
                return apiStringBuilder(misc, year, titleType);
            case "person":
                System.out.println("Which actor or actress would you like recommendations for?");
                person = scanner.nextLine();
                return apiStringBuilder(misc, person, titleType);
            case "genre":
                System.out.println("Which movie genre would like similar recommendations for?");
                genre = scanner.nextLine();
                return apiStringBuilder(misc, genre, titleType);
            default:
                return null;
        }
    }

    public static String apiStringBuilder(String key, String value, String titleType) {
        String apiStr = "";
        switch (key) {
            case "year":
                apiStr = "https://unogs-unogs-v1.p.rapidapi.com/search/titles?country_list=78&start_year=" + value + "&order_by=rating&limit=5&end_year=" + value + "&type=" + titleType;
                return apiStr;
            case "person":
                value = value.replace(" ", "%20");
                apiStr = "https://unogs-unogs-v1.p.rapidapi.com/search/titles?person=" + value + "&order_by=rating&limit=5&type=" + titleType;
                return apiStr;
            case "genre":
                return "Done!";
        }

        return apiStr;
    }

    public static String getGenreList(String genre) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://unogs-unogs-v1.p.rapidapi.com/static/genres"))
		.header("X-RapidAPI-Key", "f0ff5c7516mshac0d2d4ab74f5e5p14af57jsn09d35d0a52c5")
		.header("X-RapidAPI-Host", "unogs-unogs-v1.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return null;
    }
    public static void main(String[] args) {

        // Replace "<YOUR_API_KEY>" with your actual uNoGS API key
        String apiKey = "<API Key>";
        String GENRE = "";
        String TITLE_TYPE = "";
        String YEAR = "";
        String PERSON = "";

        String apiString = userInputs(TITLE_TYPE, YEAR, PERSON, GENRE);
      
        try {
            // Fetch TV show  or Movie information from the uNoGS API
            Map<String, String[]> movieMap = fetchNetflixWatchHistory(apiKey, apiString);
            // Map<String, String[]> movieMap = TESTfetchNetflixWatchHistory(apiKey);
            List<Map.Entry<String, String[]>> movieList = new ArrayList<>(movieMap.entrySet());
            
            // Sort the movie list based on IMDB rating in descending order
            Collections.sort(movieList, new Comparator<Map.Entry<String, String[]>>() {
                @Override
                public int compare(Map.Entry<String, String[]> entry1, Map.Entry<String, String[]> entry2) {
                    double rating1 = Double.parseDouble(entry1.getValue()[0]);
                    double rating2 = Double.parseDouble(entry2.getValue()[0]);
                    return Double.compare(rating2, rating1);
                }
            });
            
            System.out.println();
            System.out.println("_____RESULTS_____");
            System.out.println();
            
            // Process the TV show information as needed
            for (Map.Entry<String, String[]> entry: movieList) {
                String title = entry.getKey();
                String[] movieInfo = entry.getValue();
                String imdbRating = movieInfo[0];
                String titleType = movieInfo[1];
                String year = movieInfo[2];
                String synopsis = movieInfo[3];
                System.out.println("Title: " + title);
                System.out.println("IMDB Rating: " + imdbRating);
                System.out.println("Type: " + titleType);
                System.out.println("Year: " + year);
                System.out.println("Synopsis: " + synopsis);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
