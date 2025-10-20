package com.library.service;

/**
 * Holds the currently active repository so that UI components can access the
 * selected backend (Oracle or MongoDB).
 */
public final class LibraryContext {

    private static volatile LibraryRepository repository;
    private static volatile RepositoryType repositoryType = RepositoryType.ORACLE;

    private LibraryContext() {
    }

    public static synchronized void initialize(RepositoryType type) throws Exception {
        if (repository != null) {
            repository.close();
        }
        repository = LibraryRepositoryFactory.create(type);
        repositoryType = type;
        repository.testConnection();
    }

    public static LibraryRepository getRepository() {
        if (repository == null) {
            throw new IllegalStateException("Repository has not been initialised");
        }
        return repository;
    }

    public static RepositoryType getRepositoryType() {
        return repositoryType;
    }

    public static synchronized void shutdown() {
        if (repository != null) {
            repository.close();
            repository = null;
        }
    }
}
