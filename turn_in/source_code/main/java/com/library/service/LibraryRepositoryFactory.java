package com.library.service;

/**
 * Factory for creating repository implementations.
 */
public final class LibraryRepositoryFactory {

    private LibraryRepositoryFactory() {
    }

    public static LibraryRepository create(RepositoryType type) throws Exception {
        switch (type) {
            case MONGODB:
                return new MongoLibraryRepository();
            case ORACLE:
            default:
                return new OracleLibraryRepository();
        }
    }
}
