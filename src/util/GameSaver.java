package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.game.Game;
import model.game.GameState;

/**
 * Provides utility methods for saving, loading, and managing Jest game saves.
 * This class handles serialization and deserialization of game states to disk,
 * allowing players to save their progress and resume games later.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Save game states to files with custom or auto-generated names</li>
 *   <li>Load previously saved game states</li>
 *   <li>List all available save files</li>
 *   <li>Delete unwanted save files</li>
 * </ul>
 *
 * <p>All save files are stored in the "jest_saves/" directory with a ".jest" extension.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see GameState
 * @see Game
 */
public class GameSaver {
    /** The directory where all game saves are stored */
    private static final String SAVE_DIRECTORY = "jest_saves/";
    /** The file extension used for save files */
    private static final String SAVE_EXTENSION = ".jest";

    /**
     * Saves the current game state to a file.
     * If no save name is provided, an auto-generated timestamp name is used.
     * Creates the save directory if it doesn't exist.
     *
     * <p>Save file naming:</p>
     * <ul>
     *   <li>Custom name: uses the provided saveName parameter</li>
     *   <li>Auto-generated: "save_yyyy-MM-dd_HH-mm-ss" format</li>
     * </ul>
     *
     * @param game the Game instance to save
     * @param includeExpansion flag indicating whether the expansion pack is included
     * @param saveName the desired name for the save file (without extension),
     *                 or null/empty for auto-generated name
     * @return true if the save was successful, false if an error occurred
     */
    public static boolean saveGame(Game game, boolean includeExpansion, String saveName) {
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));

            if (saveName == null || saveName.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                saveName = "save_" + sdf.format(new Date());
            }

            String filePath = SAVE_DIRECTORY + saveName + SAVE_EXTENSION;

            GameState gameState = new GameState(game, includeExpansion);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(gameState);
            }

            System.out.println("💾 Game saved successfully: " + filePath);
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads a saved game state from a file.
     * Deserializes the game state from the specified save file.
     *
     * @param saveName the name of the save file to load (without extension)
     * @return the loaded GameState object, or null if loading failed
     */
    public static GameState loadGame(String saveName) {
        try {
            String filePath = SAVE_DIRECTORY + saveName + SAVE_EXTENSION;

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                GameState gameState = (GameState) ois.readObject();
                System.out.println("✅ Game loaded successfully: " + filePath);
                return gameState;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lists all available save files in the save directory.
     * Returns an array of save names without the file extension.
     *
     * @return an array of save file names (without extension),
     *         or an empty array if no saves exist or an error occurs
     */
    public static String[] listSaves() {
        try {
            File saveDir = new File(SAVE_DIRECTORY);
            if (!saveDir.exists()) {
                return new String[0];
            }

            File[] files = saveDir.listFiles((dir, name) -> name.endsWith(SAVE_EXTENSION));
            if (files == null || files.length == 0) {
                return new String[0];
            }

            String[] saveNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                saveNames[i] = fileName.substring(0, fileName.length() - SAVE_EXTENSION.length());
            }

            return saveNames;

        } catch (Exception e) {
            System.err.println("❌ Error listing saves: " + e.getMessage());
            return new String[0];
        }
    }

    /**
     * Deletes a saved game file.
     * Removes the specified save file from the save directory.
     *
     * @param saveName the name of the save file to delete (without extension)
     * @return true if the file was successfully deleted, false otherwise
     */
    public static boolean deleteSave(String saveName) {
        try {
            String filePath = SAVE_DIRECTORY + saveName + SAVE_EXTENSION;
            File file = new File(filePath);

            if (file.delete()) {
                System.out.println("🗑️  Save deleted: " + saveName);
                return true;
            } else {
                System.err.println("❌ Could not delete save: " + saveName);
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error deleting save: " + e.getMessage());
            return false;
        }
    }
}
