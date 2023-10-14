package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    private int size = 0;

    private int search(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = search(r.getUuid());
        if (index > -1) {
            storage[index] = r;
        } else {
            System.out.println("ERROR: нет резюме c uuid: " + r.getUuid());
        }
    }

    public void save(Resume r) {
        if (size == 10000) {
            System.out.println("ERROR: нет свободного места в хранилище");
            return;
        } else if (search(r.getUuid()) > -1) {
            System.out.println("ERROR: резюме с uuid: " + r.getUuid() + " уже сохранено");
            return;
        }
        storage[size] = r;
        size++;
    }

    public Resume get(String uuid) {
        int index = search(uuid);
        if (index > -1) {
            return storage[index];
        }
        System.out.println("ERROR: нет резюме c uuid: " + uuid);
        return null;
    }

    public void delete(String uuid) {
        int index = search(uuid);
        if (index > -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("ERROR: нет резюме c uuid: " + uuid);
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

}
