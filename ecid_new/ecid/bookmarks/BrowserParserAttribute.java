package com.android.server.ecid.bookmarks;

/**
 * Created by shiguibiao on 16-8-13.
 */

public interface BrowserParserAttribute {
    // [Profile names]
    public static final String ATTR_URL = "url";
    public static final String ATTR_READONLY = "readOnly";

    public static final String ELEMENT_NAME_HOMEPAGE = "homepage";
    public static final String ELEMENT_NAME_BOOKMARK = "bookmark";
    public static final String ELEMENT_NAME_SINGLEBINARY = "singlebinary";
    public static final String ELEMENT_NAME_READONLY = "bookmark_read_only";

    public static final String KEY_ELEMENT_NAME_HOMEPAGE = "Browser@homepage";
    public static final String KEY_ELEMENT_NAME_READONLY = "Browser@bookmark_read_only";

    public static final String HAS_LOADED_BROWSER_DEFAULT_SETTINGS ="has_loaded_browser_default_settings";

}
