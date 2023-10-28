package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume>{
    Map<String, Resume> storage = new HashMap<>();
    @Override
    protected void doSave(Resume r, Resume key) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, Resume key) {
        storage.put((key).getUuid(), r);
    }

    @Override
    protected Resume doGet(Resume key) {
        return storage.get((key).getUuid());
    }

    @Override
    protected void doDelete(Resume key) {
        storage.remove((key).getUuid());
    }

    @Override
    protected Resume getKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Resume key) {
        return storage.containsValue(key);
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
