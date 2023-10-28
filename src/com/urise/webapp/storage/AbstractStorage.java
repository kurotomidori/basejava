package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK>implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    protected abstract void doSave(Resume r, SK key);
    protected abstract void doUpdate(Resume r, SK key);
    protected abstract Resume doGet(SK key);
    protected abstract void doDelete(SK key);
    protected abstract SK getKey(String uuid);
    protected abstract boolean isExist(SK key);
    protected abstract List<Resume> listCopy();

    protected static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r);
        SK key = existInStorage(r.getUuid());
        doUpdate(r, key);
    }

    @Override
    public void save(Resume r) {
        LOG.info("Save " + r);
        SK key = notExistInStorage(r.getUuid());
        doSave(r, key);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK key = existInStorage(uuid);
        return doGet(key);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK key = existInStorage(uuid);
        doDelete(key);
    }

    protected SK existInStorage(String uuid) {
        SK key = getKey(uuid);
        if(isExist(key)) {
            return key;
        } else {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }

    }

    protected SK notExistInStorage(String uuid) {
        SK key = getKey(uuid);
        if(!isExist(key)) {
            return key;
        } else {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
    }
    @Override
    public List<Resume> getAllSorted() {
        List<Resume> toSort = listCopy();
        toSort.sort(RESUME_COMPARATOR);
        return toSort;
    }
}
