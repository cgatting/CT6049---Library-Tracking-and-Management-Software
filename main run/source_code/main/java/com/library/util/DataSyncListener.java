package com.library.util;

/**
 * Interface for components that need to be notified of data changes
 * Implements observer pattern for data synchronization across tabs
 */
public interface DataSyncListener {
    
    /**
     * Called when student data has been modified
     */
    default void onStudentDataChanged() {
        // Default implementation - override if needed
    }
    
    /**
     * Called when book data has been modified
     */
    default void onBookDataChanged() {
        // Default implementation - override if needed
    }
    
    /**
     * Called when loan data has been modified
     */
    default void onLoanDataChanged() {
        // Default implementation - override if needed
    }
    
    /**
     * Called when fine data has been modified
     */
    default void onFineDataChanged() {
        // Default implementation - override if needed
    }
    
    /**
     * Called when all data should be refreshed
     */
    default void onAllDataChanged() {
        // Default implementation - refresh everything
        onStudentDataChanged();
        onBookDataChanged();
        onLoanDataChanged();
        onFineDataChanged();
    }
}