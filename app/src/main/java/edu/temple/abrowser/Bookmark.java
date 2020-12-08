package edu.temple.abrowser;

import java.net.URL;

/**
 * Bookmark model
 */
public class Bookmark {

    public String name;
    public String url;

    // -----------------------------
    // - Constructors
    // -----------------------------

    public Bookmark()
    {
        name = "";
        url  = "";
    }

    public Bookmark(String name, String url)
    {
        this.name = name;
        this.url  = url;
    }

    // -----------------------------
    // - Accessor methods
    // -----------------------------

    public void   setName(String name)
    {
        this.name = name;
    }

    public void   setUrl(String url)
    {
        this.url = url;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUrl()
    {
        return this.url;
    }

}
