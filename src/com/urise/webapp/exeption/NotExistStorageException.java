package com.urise.webapp.exeption;

public class NotExistStorageException extends StorageException{
    public NotExistStorageException(String uuid) {
        super("ERROR: нет резюме " + uuid, uuid);
    }
}
