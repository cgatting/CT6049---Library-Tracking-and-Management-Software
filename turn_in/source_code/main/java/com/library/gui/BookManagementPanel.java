package com.library.gui;

import com.library.controller.LibraryController;
import com.library.model.Book;
import com.library.util.DataSyncListener;
import com.library.util.DataSyncManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Book management screen (presentation layer) that delegates logic to the
 * {@link LibraryController}.
 */
public class BookManagementPanel extends JPanel implements DataSyncListener {

    private final LibraryController controller;

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField bookIdField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField publisherField;
    private JTextField yearField;
    private JTextField categoryField;
    private JTextField locationField;
    private JSpinner totalCopiesSpinner;
    private JSpinner availableCopiesSpinner;

    private JTextField searchField;
    private JComboBox<String> searchCategoryCombo;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;
    private JButton searchButton;
    private JButton detailsButton;

    private int selectedBookId = -1;

    public BookManagementPanel(LibraryController controller) {
        this.controller = controller;
        DataSyncManager.getInstance().addListener(this);

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadBookData();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Title", "Author", "ISBN", "Publisher", "Year", "Category", "Total", "Available", "Location"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.setRowHeight(22);
        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(7).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(9).setPreferredWidth(100);
        sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        bookIdField = new JTextField(8);
        bookIdField.setEditable(false);
        titleField = new JTextField(25);
        authorField = new JTextField(20);
        isbnField = new JTextField(15);
        publisherField = new JTextField(20);
        yearField = new JTextField(6);
        categoryField = new JTextField(20);
        locationField = new JTextField(15);
        totalCopiesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        availableCopiesSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));

        searchField = new JTextField(20);
        searchCategoryCombo = new JComboBox<>(new String[]{"All", "Title", "Author", "ISBN", "Publisher", "Category"});

        addButton = new JButton("Add Book");
        updateButton = new JButton("Update Book");
        deleteButton = new JButton("Delete Book");
        clearButton = new JButton("Clear Form");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Search");
        detailsButton = new JButton("View Details");

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        detailsButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JScrollPane(bookTable), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);
        add(createSearchPanel(), BorderLayout.NORTH);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Search"));
        panel.add(new JLabel("Keyword:"));
        panel.add(searchField);
        panel.add(searchCategoryCombo);
        panel.add(searchButton);
        panel.add(refreshButton);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        panel.add(bookIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        panel.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Publisher:"), gbc);
        gbc.gridx = 1;
        panel.add(publisherField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Publication Year:"), gbc);
        gbc.gridx = 1;
        panel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1;
        panel.add(totalCopiesSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Available Copies:"), gbc);
        gbc.gridx = 1;
        panel.add(availableCopiesSpinner, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(detailsButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadBookData());
        searchButton.addActionListener(e -> applySearchFilter());
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        clearButton.addActionListener(e -> clearForm());
        detailsButton.addActionListener(e -> showSelectedBookDetails());

        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                populateFormFromSelection();
            }
        });
    }

    private void loadBookData() {
        try {
            List<Book> books = controller.getAllBooks();
            tableModel.setRowCount(0);
            for (Book book : books) {
                Object[] row = {
                    book.getBookId(),
                    nonNull(book.getTitle()),
                    nonNull(book.getAuthor()),
                    nonNull(book.getIsbn()),
                    nonNull(book.getPublisher()),
                    book.getPublicationYear() != null ? book.getPublicationYear() : "",
                    nonNull(book.getCategory()),
                    book.getTotalCopies(),
                    book.getAvailableCopies(),
                    nonNull(book.getLocation())
                };
                tableModel.addRow(row);
            }
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading books: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBook() {
        try {
            Book book = buildBookFromForm();
            controller.addBook(book);
            loadBookData();
            JOptionPane.showMessageDialog(this, "Book added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error adding book: " + e.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        if (selectedBookId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Book book = buildBookFromForm();
            book.setBookId(selectedBookId);
            controller.updateBook(book);
            loadBookData();
            JOptionPane.showMessageDialog(this, "Book updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating book: " + e.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        if (selectedBookId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this book?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.deleteBook(selectedBookId);
            loadBookData();
            JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting book: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Book buildBookFromForm() {
        if (titleField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (authorField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        int totalCopies = (Integer) totalCopiesSpinner.getValue();
        int availableCopies = (Integer) availableCopiesSpinner.getValue();
        if (availableCopies > totalCopies) {
            throw new IllegalArgumentException("Available copies cannot exceed total copies");
        }
        Book book = new Book();
        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setIsbn(trimToNull(isbnField.getText()));
        book.setPublisher(trimToNull(publisherField.getText()));
        book.setCategory(trimToNull(categoryField.getText()));
        book.setLocation(trimToNull(locationField.getText()));
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(availableCopies);
        String yearText = yearField.getText().trim();
        if (!yearText.isEmpty()) {
            try {
                book.setPublicationYear(Integer.parseInt(yearText));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Publication year must be numeric");
            }
        }
        return book;
    }

    private void clearForm() {
        selectedBookId = -1;
        bookIdField.setText("");
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        publisherField.setText("");
        yearField.setText("");
        categoryField.setText("");
        locationField.setText("");
        totalCopiesSpinner.setValue(1);
        availableCopiesSpinner.setValue(1);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        detailsButton.setEnabled(false);
    }

    private void populateFormFromSelection() {
        int viewRow = bookTable.getSelectedRow();
        if (viewRow == -1) {
            clearForm();
            return;
        }
        int modelRow = bookTable.convertRowIndexToModel(viewRow);
        selectedBookId = (Integer) tableModel.getValueAt(modelRow, 0);
        bookIdField.setText(String.valueOf(selectedBookId));
        titleField.setText((String) tableModel.getValueAt(modelRow, 1));
        authorField.setText((String) tableModel.getValueAt(modelRow, 2));
        isbnField.setText((String) tableModel.getValueAt(modelRow, 3));
        publisherField.setText((String) tableModel.getValueAt(modelRow, 4));
        yearField.setText(String.valueOf(tableModel.getValueAt(modelRow, 5)));
        categoryField.setText((String) tableModel.getValueAt(modelRow, 6));
        totalCopiesSpinner.setValue(tableModel.getValueAt(modelRow, 7));
        availableCopiesSpinner.setValue(tableModel.getValueAt(modelRow, 8));
        locationField.setText((String) tableModel.getValueAt(modelRow, 9));
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
        detailsButton.setEnabled(true);
    }

    private void applySearchFilter() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        int columnIndex = searchCategoryCombo.getSelectedIndex();
        if (columnIndex <= 0) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, columnIndex));
        }
    }

    private void showSelectedBookDetails() {
        if (selectedBookId == -1) {
            return;
        }
        try {
            for (Book book : controller.getAllBooks()) {
                if (book.getBookId() == selectedBookId) {
                    showBookDialog(book);
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error retrieving book details: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBookDialog(Book book) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Book Details", true);
        dialog.setLayout(new BorderLayout());
        JPanel details = new JPanel(new GridLayout(0, 2, 6, 6));
        details.add(new JLabel("Book ID:"));
        details.add(new JLabel(String.valueOf(book.getBookId())));
        details.add(new JLabel("Title:"));
        details.add(new JLabel(nonNull(book.getTitle())));
        details.add(new JLabel("Author:"));
        details.add(new JLabel(nonNull(book.getAuthor())));
        details.add(new JLabel("ISBN:"));
        details.add(new JLabel(nonNull(book.getIsbn())));
        details.add(new JLabel("Publisher:"));
        details.add(new JLabel(nonNull(book.getPublisher())));
        details.add(new JLabel("Year:"));
        details.add(new JLabel(book.getPublicationYear() != null ? book.getPublicationYear().toString() : ""));
        details.add(new JLabel("Category:"));
        details.add(new JLabel(nonNull(book.getCategory())));
        details.add(new JLabel("Location:"));
        details.add(new JLabel(nonNull(book.getLocation())));
        details.add(new JLabel("Total Copies:"));
        details.add(new JLabel(String.valueOf(book.getTotalCopies())));
        details.add(new JLabel("Available Copies:"));
        details.add(new JLabel(String.valueOf(book.getAvailableCopies())));
        details.add(new JLabel("Borrowed Copies:"));
        details.add(new JLabel(String.valueOf(book.getTotalCopies() - book.getAvailableCopies())));

        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(close);

        dialog.add(details, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String nonNull(String value) {
        return value == null ? "" : value;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    // -----------------------------------------------------------------
    // DataSyncListener implementation
    // -----------------------------------------------------------------

    public void onStudentDataChanged() {
        // Not used in this panel
    }

    public void onBookDataChanged() {
        SwingUtilities.invokeLater(this::loadBookData);
    }

    public void onLoanDataChanged() {
        SwingUtilities.invokeLater(this::loadBookData);
    }

    public void onFineDataChanged() {
        // Not used
    }

    public void onAllDataChanged() {
        SwingUtilities.invokeLater(this::loadBookData);
    }
}