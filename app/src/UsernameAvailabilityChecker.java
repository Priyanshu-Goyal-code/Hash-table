import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsernameAvailabilityChecker {

    // Stores registered usernames
    private ConcurrentHashMap<String, Integer> usernameToUserId;

    // Stores how many times a username was attempted
    private ConcurrentHashMap<String, Integer> attemptFrequency;

    public UsernameAvailabilityChecker() {
        usernameToUserId = new ConcurrentHashMap<>();
        attemptFrequency = new ConcurrentHashMap<>();
    }

    // Check if username is available
    public boolean checkAvailability(String username) {

        // Increase attempt count
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        // Check existence
        return !usernameToUserId.containsKey(username);
    }

    // Register new user
    public boolean registerUser(String username, int userId) {

        if (usernameToUserId.containsKey(username)) {
            return false;
        }

        usernameToUserId.put(username, userId);
        return true;
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameToUserId.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Replace underscore with dot
        String modified = username.replace("_", ".");

        if (!usernameToUserId.containsKey(modified)) {
            suggestions.add(modified);
        }

        // Add prefix suggestion
        String prefix = "the_" + username;
        if (!usernameToUserId.containsKey(prefix)) {
            suggestions.add(prefix);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }

    // Print all registered users
    public void printUsers() {
        System.out.println("Registered Users:");
        for (Map.Entry<String, Integer> entry : usernameToUserId.entrySet()) {
            System.out.println(entry.getKey() + " -> UserID: " + entry.getValue());
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        UsernameAvailabilityChecker system = new UsernameAvailabilityChecker();

        // Register some users
        system.registerUser("john_doe", 1);
        system.registerUser("admin", 2);
        system.registerUser("alex99", 3);

        // Check availability
        System.out.println("Check john_doe: " +
                system.checkAvailability("john_doe"));

        System.out.println("Check jane_smith: " +
                system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("\nSuggestions for john_doe:");
        List<String> suggestions = system.suggestAlternatives("john_doe");

        for (String s : suggestions) {
            System.out.println(s);
        }

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        system.checkAvailability("john");
        system.checkAvailability("john");

        // Most attempted
        System.out.println("\nMost Attempted Username:");
        System.out.println(system.getMostAttempted());

        // Print all users
        System.out.println();
        system.printUsers();
    }
}