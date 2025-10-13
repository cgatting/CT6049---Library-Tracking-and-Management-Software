package com.library.gui;

import com.library.controller.LibraryController;
import com.library.service.LibraryContext;
import com.library.service.RepositoryType;
import com.library.util.DataSyncListener;
import com.library.util.DataSyncManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application window with tabbed interface.
 */
public class MainFrame extends JFrame implements DataSyncListener {

    private final LibraryController controller;
    private final RepositoryType repositoryType;

    private JTabbedPane tabbedPane;
    private BorrowReturnPanel borrowReturnPanel;
    private FinePaymentPanel finePaymentPanel;
    private ReportsPanel reportsPanel;
    private JLabel statusLabel;

    public MainFrame(LibraryController controller) {
        this.controller = controller;
        this.repositoryType = controller.getRepositoryType();

        DataSyncManager.getInstance().addListener(this);

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        testDatabaseConnection();
    }

    private void initializeComponents() {
        setTitle(String.format("Library Database System - %s",
            repositoryType == RepositoryType.ORACLE ? "Oracle Backend" : "MongoDB Backend"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());
        tabbedPane = new JTabbedPane();

        UserManagementPanel userManagementPanel = new UserManagementPanel(controller);
        BookManagementPanel bookManagementPanel = new BookManagementPanel(controller);
        borrowReturnPanel = new BorrowReturnPanel(controller);
        finePaymentPanel = new FinePaymentPanel(controller);
        reportsPanel = new ReportsPanel(controller);

        tabbedPane.addTab("User Management", userManagementPanel);
        tabbedPane.addTab("Book Management", bookManagementPanel);
        tabbedPane.addTab("Borrow/Return Books", borrowReturnPanel);
        tabbedPane.addTab("Pay Fines", finePaymentPanel);
        tabbedPane.addTab("Reports", reportsPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem refreshItem = new JMenuItem("Refresh All Data");
        refreshItem.setMnemonic(KeyEvent.VK_R);
        refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        refreshItem.addActionListener(e -> refreshAllData());
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitItem.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(exitItem);

        JMenu navigationMenu = new JMenu("Navigate");
        navigationMenu.setMnemonic(KeyEvent.VK_N);

        navigationMenu.add(createNavigationItem("User Management", KeyEvent.VK_1, 0));
        navigationMenu.add(createNavigationItem("Book Management", KeyEvent.VK_2, 1));
        navigationMenu.addSeparator();
        navigationMenu.add(createNavigationItem("Borrow/Return", KeyEvent.VK_3, 2));
        navigationMenu.add(createNavigationItem("Fine Payment", KeyEvent.VK_4, 3));
        navigationMenu.add(createNavigationItem("Reports", KeyEvent.VK_5, 4));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(navigationMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JMenuItem createNavigationItem(String label, int key, int tabIndex) {
        JMenuItem item = new JMenuItem(label);
        item.setMnemonic(key);
        item.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
        item.addActionListener(e -> tabbedPane.setSelectedIndex(tabIndex));
        return item;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataSyncManager.getInstance().removeListener(MainFrame.this);
                LibraryContext.shutdown();
                System.exit(0);
            }
        });
    }

    private void refreshAllData() {
        SwingUtilities.invokeLater(() -> {
            updateStatus("Refreshing all data...");
            DataSyncManager.getInstance().notifyAllDataChanged();
            updateStatus("Data refreshed successfully");
        });
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Library Management System\n" +
            "Version 1.0\n\n" +
            String.format("Built with Java Swing using %s backend",
                repositoryType == RepositoryType.ORACLE ? "Oracle" : "MongoDB"),
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            Timer timer = new Timer(3000, e -> statusLabel.setText("Ready"));
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void testDatabaseConnection() {
        SwingUtilities.invokeLater(() -> {
            try {
                controller.testConnection();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    String.format("Warning: Could not connect to %s backend.%nReason: %s",
                        repositoryType == RepositoryType.ORACLE ? "Oracle" : "MongoDB",
                        e.getMessage()),
                    "Database Connection Warning",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // -----------------------------------------------------------------
    // DataSyncListener implementation
    // -----------------------------------------------------------------

    public void onStudentDataChanged() {
        SwingUtilities.invokeLater(() -> updateStatus("Student data updated"));
    }

    public void onBookDataChanged() {
        SwingUtilities.invokeLater(() -> updateStatus("Book data updated"));
    }

    public void onLoanDataChanged() {
        SwingUtilities.invokeLater(() -> updateStatus("Loan data updated"));
    }

    public void onFineDataChanged() {
        SwingUtilities.invokeLater(() -> updateStatus("Fine data updated"));
    }

    public void onAllDataChanged() {
        SwingUtilities.invokeLater(() -> updateStatus("All data refreshed"));
    }
}