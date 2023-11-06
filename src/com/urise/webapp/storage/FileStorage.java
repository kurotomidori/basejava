package com.urise.webapp.storage;

import com.urise.webapp.serialization.Serializator;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private File directory;
    private Serializator serializator;

    public void setSerialization(Serializator serializator) {
        this.serializator = serializator;
    }

    public FileStorage(File directory, Serializator serializator) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(serializator, "serialization must not be null");
        this.serializator = serializator;
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
        } catch (IOException e) {
            throw new StorageException("Couldn't create new file " + key.getAbsolutePath(), key.getName(), e);
        }
        doUpdate(r, key);
    }

    @Override
    protected void doUpdate(Resume r, File key) {
        try {
            serializator.doWrite(r, new BufferedOutputStream(new FileOutputStream(key)));
        } catch (IOException e) {
            throw new StorageException("IO error", key.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File key) {
        try{
            return serializator.doRead(new BufferedInputStream(new FileInputStream(key)));
        } catch (IOException e) {
            throw new StorageException("Can't read a file " + key.getAbsolutePath(), key.getName(), e);
        }
    }

    @Override
    protected void doDelete(File key) {
        if(!key.delete()) {
            throw new StorageException("File delete error", key.getName());
        }
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
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
                list.add(doGet(file));
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if(files != null) {
            for (File file : files) {
                doDelete(file);
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if(list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }


}
