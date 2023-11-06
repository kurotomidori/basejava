package com.urise.webapp.storage;

import com.urise.webapp.serialization.ObjectStream;

import java.io.File;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {
    public ObjectStreamFileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new ObjectStream()));
    }
}
