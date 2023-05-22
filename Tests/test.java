import main.js;

public class UnogsApiTest {

    public static void main(String[] args) {
        String apiKey = "f0ff5c7516mshac0d2d4ab74f5e5p14af57jsn09d35d0a52c5";

        try {
            String tvShowInfo = fetchTvShowInfo(apiKey);
            System.out.println("TV Show Information:");
            System.out.println(tvShowInfo);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or show an appropriate error message
        }
    }
}
