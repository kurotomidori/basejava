package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;

public class MapStorageTest extends AbstractStorageTest {
    public MapStorageTest () {
        super(new MapStorage());
    }

    @Override
    public void getAll() throws Exception {
        Resume[] testStorage = storage.getAll();
        Assert.assertEquals(storage.size(), testStorage.length);
    }
}
