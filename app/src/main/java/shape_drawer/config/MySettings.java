package shape_drawer.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MySettings {
    private String savePath;

    public MySettings() {
        this.savePath = getJSONPath(); // Initialize savePath from JSON
    }

    private void createJson() {
        JsonObject settings = new JsonObject();
        settings.addProperty("savePath", "Save.jpeg"); // Set the default save path

        JsonObject root = new JsonObject();
        root.add("settings", settings); // Add the settings object to the root object
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Create a Gson instance with pretty printing
        try (FileWriter writer = new FileWriter("settings.json")) {
            gson.toJson(root, writer); // Write the JSON string to the file
            System.out.println("Settings saved to settings.json"); // Print a message indicating that the settings were
                                                                   // saved
        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions that occur during writing
        }
    }

    private void updateJson(String savePath) {
        JsonObject settings = new JsonObject();
        settings.addProperty("savePath", savePath); // Update the save path in the JSON object

        JsonObject root = new JsonObject();
        root.add("settings", settings); // Add the updated settings object to the root object
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Create a Gson instance with pretty printing
        try (FileWriter writer = new FileWriter("settings.json")) {
            gson.toJson(root, writer); // Write the updated JSON string to the file
            System.out.println("Settings updated in settings.json"); // Print a message indicating that the settings
                                                                     // were
                                                                     // updated
        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions that occur during writing
        }
    }

    private String getJSONPath() {
        try (FileReader reader = new FileReader("settings.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject(); // Parse the JSON file
            String savePath = jsonObject.getAsJsonObject("settings").get("savePath").getAsString();
            if (savePath == null || savePath.isEmpty()) {
                savePath = "Save.png"; // Set a default save path if not specified
            }
            System.out.println("Save path: " + savePath); // Print the save path
            return savePath;
        } catch (Exception e) {
            System.out.println("settings.json not found, creating a new one with default settings.");
            createJson(); // Create a new JSON file with default settings if it doesn't exist
            return "Save.jpeg"; // Return the default save path
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
        updateJson(savePath); // Update the JSON file whenever the save path is changed
    }

}