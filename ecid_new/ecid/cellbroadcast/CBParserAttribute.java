package com.android.server.ecid.cellbroadcast;

/**
 * Created by shiguibiao on 16-8-4.
 */

public interface CBParserAttribute {

    public static final String ATTR_CB_DEFAULT_SWITCH = "cb_on";
    public static final String ATTR_CB_DEFAULT_CH_LIST= "cb_ch_list";
    public static final String ATTR_CB_DEFAULT_CH_NAME = "cb_ch_list_name";
    public static final String ATTR_CB_DEFAULT_EU_ALERT = "cb_eu_alert_on";

    public static final String KEY_ATTR_CB_DEFAULT_SWITCH = "Message@Cell_Broadcast_Receive";

    public static final String HAS_LOADED_CB_DEFAULT_SETTINGS ="has_loaded_cb_default_settings";
}
