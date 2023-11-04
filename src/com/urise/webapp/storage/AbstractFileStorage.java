package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if(!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }
    @Override
    protected void doSave(Resume r, File key) {
        try {
            key.createNewFile();
            doWrite(r, key);
        } catch (IOException e) {
            throw new StorageException("IO error", key.getName(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, File key) {
        try {
            doWrite(r, key);
        } catch (IOException e) {
            throw new StorageException("IO error", key.getName(), e);
        }
    }

    @Override
    protected abstract Resume doGet(File key);

    @Override
    protected void doDelete(File key) {
        key.delete();
    }

    @Override
    protected File getKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File key) {
        return key.exists();
    }

    @Override
    protected List<Resume> listCopy() {
        List<Resume> list = new ArrayList<>();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File file : files) {
                list.add(doGet(file));
            }
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if(files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Override
    public int size() {
        return directory.listFiles().length;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;
}
