package com.library.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Data synchronization manager to coordinate updates across all application tabs
 * Uses observer pattern to notify components when data changes occur
 */
public class DataSyncManager {
    private static DataSyncManager instance;
    private List<DataSyncListener> listeners;
    
    private DataSyncManager() {
        listeners = new ArrayList<>();
    }
    
    public static DataSyncManager getInstance() {
        if (instance == null) {
            instance = new DataSyncManager();
        }
        return instance;
    }
    
    /**
     * Register a listener to receive data change notifications
     */
    public void addListener(DataSyncListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Remove a listener from notifications
     */
    public void removeListener(DataSyncListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners that student data has changed
     */
    public void notifyStudentDataChanged() {
        for (DataSyncListener listener : listeners) {
            listener.onStudentDataChanged();
        }
    }
    
    /**
     * Notify all listeners that book data has changed
     */
    public void notifyBookDataChanged() {
        for (DataSyncListener listener : listeners) {
            listener.onBookDataChanged();
        }
    }
    
    /**
     * Notify all listeners that loan data has changed
     */
    public void notifyLoanDataChanged() {
        for (DataSyncListener listener : listeners) {
            listener.onLoanDataChanged();
        }
    }
    
    /**
     * Notify all listeners that fine data has changed
     */
    public void notifyFineDataChanged() {
        for (DataSyncListener listener : listeners) {
            listener.onFineDataChanged();
        }
    }
    
    /**
     * Notify all listeners to refresh all data
     */
    public void notifyAllDataChanged() {
        for (DataSyncListener listener : listeners) {
            listener.onAllDataChanged();
        }
    }
}