package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage<String>{
    Map<String, Resume> storage = new HashMap<>();
    @Override
    protected void doSave(Resume r, String key) {
        storage.put(key, r);
    }

    @Override
    protected void doUpdate(Resume r, String key) {
        storage.put(key, r);
    }

    @Override
    protected Resume doGet(String key) {
        return storage.get(key);
    }

    @Override
    protected void doDelete(String key) {
        storage.remove(key);
    }

    @Override
    protected String getKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String key) {
        return storage.containsKey(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> listCopy() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
