package com.urise.webapp.model;

import java.io.Serializable;

public abstract class Section<T> implements Serializable {
    public abstract T getContent();

}
