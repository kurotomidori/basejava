package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;

import java.util.List;

public class MapUuidStorageTest extends AbstractStorageTest {
    public MapUuidStorageTest() {
        super(new MapUuidStorage());
    }

    @Override
    public void getAllSorted() throws Exception {
        List<Resume> testStorage = storage.getAllSorted();
        Assert.assertEquals(storage.size(), testStorage.size());
    }
}
