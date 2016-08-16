package com.android.server.ecid.contacts;

/**
 * Created by shiguibiao on 16-8-13.
 */

public interface ContactsParserAttribute {

    public static final String ATTR_DEFAULT_STORAGE = "NEW_CONTACT_DEFAULT";
    public static final String ATTR_DISPLAY_NUMBER = "display_number_on_phonebook";
    public static final String ATTR_DISPLY_SDN = "display_SDN_in_call_register";
    /**
     * add new contacts default storage in phone memory
     */
    public static final int IN_PHONE_MEMORY = 0;

    /**
     * add new contacts default storage in sim memory
     */
    public static final int IN_SIM_MEMORY = 1;

    /**
     * add new contacts default storage in sim memory
     */
    public static final String ATTR_VALUE_SIM_STORAGE = "In SIM memory";

    /**
     * add new contacts default storage in phone memory
     */
    public static final String ATTR_VALUE_PHONE_STORAGE = "In Phone Memory";


    public static final String KEY_ATTR_DEFAULT_STORAGE = "Contacts@Default_Storage_Location";
    public static final String KEY_ATTR_DISPLAY_NUMBER = "Contacts@display_number_on_phonebook";
    public static final String KEY_ATTR_DISPLY_SDN = "Contacts@display_SDN_in_call_register";
}
