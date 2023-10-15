package com.urise.webapp.exeption;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("ERROR: резюме " + uuid + " уже существует", uuid);
    }
}
