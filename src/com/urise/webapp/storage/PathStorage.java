package com.urise.webapp.storage;

import com.urise.webapp.serialization.Serializator;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private Path directory;
    private Serializator serializator;

    public void setSerialization(Serializator serializator) {
        this.serializator = serializator;
    }

    public PathStorage(String dir, Serializator serializator) {
        Objects.requireNonNull(serializator, "serialization must not be null");
        this.serializator = serializator;
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
            throw new StorageException("Couldn't create new file " + key, getFileName(key), e);
        }
        doUpdate(r, key);
    }

    @Override
    protected void doUpdate(Resume r, Path key) {
        try {
            serializator.doWrite(r, new BufferedOutputStream(Files.newOutputStream(key)));
        } catch (IOException e) {
            throw new StorageException("IO error" + key, getFileName(key), e);
        }
    }

    @Override
    protected Resume doGet(Path key) {
        try{
            return serializator.doRead(new BufferedInputStream(Files.newInputStream(key)));
        } catch (IOException e) {
            throw new StorageException("Can't read a file " + key, getFileName(key), e);
        }
    }

    @Override
    protected void doDelete(Path key) {
        try {
            Files.delete(key);
        } catch (IOException e) {
            throw new StorageException("File delete error", getFileName(key), e);
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
        for (Object file : getFilesList().toArray()) {
            list.add(doGet((Path) file));
        }
        return getFilesList().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }

    private Stream<Path> getFilesList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }
}
