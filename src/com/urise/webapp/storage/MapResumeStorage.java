package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage{
    Map<String, Resume> storage = new HashMap<>();
    @Override
    protected void doSave(Resume r, Object key) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, Object key) {
        storage.put(((Resume) key).getUuid(), r);
    }

    @Override
    protected Resume doGet(Object key) {
        return storage.get(((Resume) key).getUuid());
    }

    @Override
    protected void doDelete(Object key) {
        storage.remove(((Resume) key).getUuid());
    }

    @Override
    protected Object getKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object key) {
        return storage.containsValue((Resume) key);
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
