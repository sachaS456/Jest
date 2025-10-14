import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameSaver {
    private static final String SAVE_DIRECTORY = "jest_saves/";
    private static final String SAVE_EXTENSION = ".jest";

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

