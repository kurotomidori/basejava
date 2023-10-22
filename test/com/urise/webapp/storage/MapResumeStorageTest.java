package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;

import java.util.List;

public class MapResumeStorageTest extends AbstractStorageTest{
    public MapResumeStorageTest() {
        super(new MapResumeStorage());
    }

    @Override
    public void getAllSorted() throws Exception {
        List<Resume> testStorage = storage.getAllSorted();
        Assert.assertEquals(storage.size(), testStorage.size());
    }
}
