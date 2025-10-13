package com.library;

import com.formdev.flatlaf.FlatLightLaf;
import com.library.controller.LibraryController;
import com.library.gui.MainFrame;
import com.library.service.LibraryContext;
import com.library.service.RepositoryType;

import javax.swing.*;

/**
 * Main application class for the Library Database System.
 */
public class LibraryApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize Look and Feel: " + e.getMessage());
        }

        RepositoryType preferredBackend = determineBackendPreference(args);

        SwingUtilities.invokeLater(() -> {
            RepositoryType selectedType = preferredBackend != null ? preferredBackend : promptForRepository();
            if (selectedType == null) {
                System.exit(0);
                return;
            }

            boolean initialised = false;
            while (!initialised) {
                try {
                    LibraryContext.initialize(selectedType);
                    initialised = true;
                } catch (Exception ex) {
                    int retry = JOptionPane.showConfirmDialog(
                        null,
                        String.format("Failed to connect to the %s backend.%nReason: %s%n%nWould you like to choose a different backend?",
                            backendLabel(selectedType), ex.getMessage()),
                        "Connection Failed",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE);

                    if (retry == JOptionPane.YES_OPTION) {
                        selectedType = promptForRepository();
                        if (selectedType == null) {
                            System.exit(0);
                            return;
                        }
                    } else {
                        System.exit(0);
                        return;
                    }
                }
            }

            try {
                LibraryController controller = new LibraryController(LibraryContext.getRepository());
                MainFrame mainFrame = new MainFrame(controller);
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to start application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static RepositoryType determineBackendPreference(String[] args) {
        if (args != null) {
            for (String arg : args) {
                if (arg != null && arg.toLowerCase().startsWith("--backend=")) {
                    String value = arg.substring("--backend=".length()).trim().toLowerCase();
                    RepositoryType parsed = parseBackend(value);
                    if (parsed != null) {
                        return parsed;
                    }
                }
            }
        }
        String property = System.getProperty("library.backend");
        if (property != null) {
            RepositoryType parsed = parseBackend(property.trim().toLowerCase());
            if (parsed != null) {
                return parsed;
            }
        }
        return null;
    }

    private static RepositoryType parseBackend(String value) {
        if (value == null) {
            return null;
        }
        if (value.startsWith("oracle")) {
            return RepositoryType.ORACLE;
        }
        if (value.startsWith("mongo")) {
            return RepositoryType.MONGODB;
        }
        return null;
    }
    private static RepositoryType promptForRepository() {

        String message = "Select the data backend to use. The MongoDB option fulfils the NoSQL portion of the CT6049 brief.";
        String[] options = {"Oracle (Relational)", "MongoDB (NoSQL)"};
        int choice = JOptionPane.showOptionDialog(
            null,
            message,
            "Choose Backend",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            return null;
        }
        return choice == 0 ? RepositoryType.ORACLE : RepositoryType.MONGODB;
    }

    private static String backendLabel(RepositoryType type) {
        return type == RepositoryType.ORACLE ? "Oracle (Relational)" : "MongoDB (NoSQL)";
    }
}







