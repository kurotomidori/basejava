package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path directory;
    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;
    protected abstract Resume doRead(InputStream is) throws IOException;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if(!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }
    @Override
    protected void doSave(Resume r, Path key) {
        try {
            Files.createFile(key);
        } catch (IOException e) {
            throw new StorageException("Couldn't create new file " + key, key.toString(), e);
        }
        doUpdate(r, key);
    }

    @Override
    protected void doUpdate(Resume r, Path key) {
        try {
            doWrite(r, new BufferedOutputStream(Files.newOutputStream(key)));
        } catch (IOException e) {
            throw new StorageException("IO error", key.toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path key) {
        try{
            return doRead(new BufferedInputStream(Files.newInputStream(key)));
        } catch (IOException e) {
            throw new StorageException("Can't read a file " + key, key.toString(), e);
        }
    }

    @Override
    protected void doDelete(Path key) {
        try {
            Files.delete(key);
        } catch (IOException e) {
            throw new StorageException("File delete error", key.toString());
        }
    }

    @Override
    protected Path getKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path key) {
        return Files.exists(key);
    }

    @Override
    protected List<Resume> listCopy() {
        List<Resume> list = new ArrayList<>();
        try (Stream<Path> files = Files.list(directory)) {
            for (Object file : files.toArray()) {
                list.add(doGet((Path) file));
            }
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
        return list;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        int size;
        try {
            size = (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
        return size;
    }


}
