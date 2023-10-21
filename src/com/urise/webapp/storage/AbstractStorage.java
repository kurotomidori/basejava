package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected abstract void doSave(Resume r, Object key);
    protected abstract void doUpdate(Resume r, Object key);
    protected abstract Resume doGet(Object key);
    protected abstract void doDelete(Object key);
    protected abstract Object getKey(String uuid);
    protected abstract boolean isExist(Object key);

    @Override
    public void update(Resume r) {
        Object key = existInStorage(r.getUuid());
        doUpdate(r, key);
    }

    @Override
    public void save(Resume r) {
        Object key = notExistInStorage(r.getUuid());
        doSave(r, key);
    }

    @Override
    public Resume get(String uuid) {
        Object key = existInStorage(uuid);
        return doGet(key);
    }

    @Override
    public void delete(String uuid) {
        Object key = existInStorage(uuid);
        doDelete(key);
    }

    protected Object existInStorage(String uuid) {
        Object key = getKey(uuid);
        if(isExist(key)) {
            return key;
        } else {
            throw new NotExistStorageException(uuid);
        }

    }

    protected Object notExistInStorage(String uuid) {
        Object key = getKey(uuid);
        if(!isExist(key)) {
            return key;
        } else {
            throw new ExistStorageException(uuid);
        }
    }

}
