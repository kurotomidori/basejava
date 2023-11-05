package com.urise.webapp.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest{
    public ObjectStreamPathStorageTest() {
        super(new ObjectPathStreamStorage(STORAGE_DIR));
    }
}
