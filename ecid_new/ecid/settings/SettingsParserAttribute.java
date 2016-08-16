package com.android.server.ecid.settings;

/**
 * Created by shiguibiao on 16-8-14.
 */

public interface SettingsParserAttribute {

    //Setting provider
    public static final String ELEMENT_NAME_SYSTEMPROPERTY = "SystemProperty";
    public static final String ELEMENT_NAME_SETTINGSSYSTEM = "Settings.System";
    public static final String ELEMENT_NAME_SETTINGSSECURE = "Settings.Secure";

    public static final String ATTR_DEFAULT_LANGUAGE = "default_language";
    public static final String ATTR_DEFAULT_TIMEZONE = "default_timezone";
    public static final String ATTR_SOUND_PROFILE = "sound_profile";
    public static final String ATTR_TIME_FORMAT = "time_12_24";
    public static final String ATTR_DATE_FORMAT = "def_date_format";
    public static final String ATTR_AUTO_TIME = "auto_time";
    public static final String ATTR_SCREEN_OFF_TIME = "screen_off_timeout";
    public static final String ATTR_DTMF_TONE_WHEN_DIAL = "dtmf_tone_when_dialing";
    public static final String ATTR_SOUND_EFFECTS = "sound_effects_enabled";
    public static final String ATTR_MOBILE_DATA = "mobile_data";
    public static final String ATTR_DATA_ROAMING = "data_roaming";
    public static final String ATTR_LOCATION_ALLOWED = "location_providers_allowed";
    public static final String ATTR_INSTALL_NON_MARKET_APPS = "install_non_market_apps";
    public static final String ATTR_NOTIFICATION_VIBRATE = "notification_vibrate";
    public static final String ATTR_RINGTONE_VIBRATION = "ringtone_vibration";

    public static final String KEY_ATTR_DEFAULT_LANGUAGE = "Settings@Phone_Default_Language";
    public static final String KEY_ATTR_DEFAULT_TIMEZONE = "Settings@Phone_Time_Zone";
    public static final String KEY_ATTR_SOUND_PROFILE = "Settings@Profile_Sound_Vibrate";
    public static final String KEY_ATTR_TIME_FORMAT = "Settings@Phone_Time_Format";
    public static final String KEY_ATTR_DATE_FORMAT = "Settings@Phone_Date_Format";
    public static final String KEY_ATTR_AUTO_TIME = "Settings@Phone_Auto_update_date_time";
    public static final String KEY_ATTR_SCREEN_OFF_TIME = "Settings@Display_Backlight";
    public static final String KEY_ATTR_DTMF_TONE_WHEN_DIAL = "ettings@Profile_Sound_Keypad_tone";
    public static final String KEY_ATTR_SOUND_EFFECTS = "Settings@Profile_Sound_Touch_Tone";
    public static final String KEY_ATTR_MOBILE_DATA = "Settings@Connectivity_Data_connection";
    public static final String KEY_ATTR_DATA_ROAMING = "Settings@Connectivity_Data_connection_in_roaming";
    public static final String KEY_ATTR_LOCATION_ALLOWED = "Settings@Phone_Default_GPS_activation";
    public static final String KEY_ATTR_INSTALL_NON_MARKET_APPS = "Settings@install_non_market_apps";
    public static final String KEY_ATTR_NOTIFICATION_VIBRATE = "Settings@Profile_Sound_Notification_vibrate";
    public static final String KEY_ATTR_RINGTONE_VIBRATION = "Settings@Profile_Sound_Ringtone_with_Vibration";

}
