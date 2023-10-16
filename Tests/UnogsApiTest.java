import RecommendationGenerator.java;
import java.io.IOException;

public class UnogsApiTest {

    // TODO: Finish writing test class for RecFlix
    public static void main(String[] args) {
        String apiKey = "<API Key>";

        try {
            String tvShowInfo = fetchNetflixWatchHistory(apiKey);
            System.out.println("TV Show Information:");
            System.out.println(tvShowInfo);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or show an appropriate error message
        }
    }
}
