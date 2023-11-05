package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class AbstractStorageTest {
    protected static final String STORAGE_DIR = ".\\storage";
    protected Storage storage;

    private static final String UUID_1 = "uuid1";

    private static final String UUID_2 = "uuid2";

    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String FULL_NAME_1 = "fullName1";
    private static final String FULL_NAME_2 = "fullName2";
    private static final String FULL_NAME_3 = "fullName3";
    private static final String FULL_NAME_4 = "fullName4";
    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    static {
        RESUME_1 = ResumeTestData.makeTestResume(UUID_1, FULL_NAME_1);
        RESUME_2 = ResumeTestData.makeTestResume(UUID_2, FULL_NAME_2);
        RESUME_3 = ResumeTestData.makeTestResume(UUID_3, FULL_NAME_3);
        RESUME_4 = ResumeTestData.makeTestResume(UUID_4, FULL_NAME_4);
    }


    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(ResumeTestData.makeTestResume(UUID_1, FULL_NAME_1));
        storage.save(ResumeTestData.makeTestResume(UUID_2, FULL_NAME_2));
        storage.save(ResumeTestData.makeTestResume(UUID_3, FULL_NAME_3));
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        Assert.assertEquals(RESUME_4, storage.get(UUID_4));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_2);
        Assert.assertEquals(2,storage.size());
        storage.get(UUID_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(RESUME_1, storage.get(UUID_1));
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        storage.update(RESUME_2);
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));

    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(RESUME_4);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> testStorage = storage.getAllSorted();
        Assert.assertEquals(storage.size(), testStorage.size());
        Assert.assertEquals(RESUME_1, testStorage.get(0));
        Assert.assertEquals(RESUME_2, testStorage.get(1));
        Assert.assertEquals(RESUME_3, testStorage.get(2));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

}