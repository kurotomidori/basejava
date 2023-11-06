package com.urise.webapp.storage;

import com.urise.webapp.serialization.ObjectStream;

public class ObjectStreamPathStorageTest extends AbstractStorageTest{
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStream()));

    }
}
