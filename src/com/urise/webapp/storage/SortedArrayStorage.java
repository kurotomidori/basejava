package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > -1) {
            storage[index] = r;
        } else {
            System.out.println("ERROR: нет резюме " + r.getUuid());
        }
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > -1) {
            System.out.println("ERROR: резюме " + r.getUuid() + " уже существует");
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR: нет свободного места в хранилище");
        } else {
            for (int i = size; i >= -index ; i--) {
                storage[i] = storage[i-1];
            }
            storage[-index-1] = r;
            size++;
        }

    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR: нет резюме " + uuid);
        } else {
            for (int i = index; i < size; i++) {
                storage[i] = storage[i+1];
            }
            storage[size - 1] = null;
            size--;
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }
}
