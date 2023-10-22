package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;
    @Override
    protected void doSave(Resume r, Object key) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Переполнение хранилища", r.getUuid());
        } else {
            insertElement(r, (Integer) key);
            size++;
        }
    }
    @Override
   protected void doDelete(Object key) {
         fillDeletedElement((Integer) key);
         storage[size - 1] = null;
         size--;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    protected Resume doGet(Object key) {
        return storage[(Integer) key];
    }
    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }
    @Override
    protected void doUpdate(Resume r, Object key) {
        storage[(Integer) key] = r;

    }
    @Override
    protected List<Resume> listCopy() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    protected boolean isExist(Object key) {
        return (Integer) key >= 0;
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract void fillDeletedElement(int index);
}
