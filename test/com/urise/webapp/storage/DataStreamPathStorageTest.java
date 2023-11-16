package com.urise.webapp.storage;

import com.urise.webapp.serialization.DataStreamSerializer;
import com.urise.webapp.serialization.XmlStreamSerializer;

public class DataStreamPathStorageTest extends AbstractStorageTest{
    public DataStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new DataStreamSerializer()));

    }
}
