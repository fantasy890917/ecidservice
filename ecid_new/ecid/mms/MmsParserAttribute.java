package com.android.server.ecid.mms;

/**
 * Created by shiguibiao on 16-8-14.
 */

public interface MmsParserAttribute {
    /**
     * Message - Setting - SMS Settings - Delivery reports
     * [SMS] Delivery Report - Default: 0 (OFF)
     */
    public static final String ATTR_SMS_DELIVERY_REPORT = "sms_dr";

    /**
     * Message - Setting - SMS Settings - SMS Service Centre
     *  [SMS] Message Center - Editable - Default: 0 (OFF)
     */
    public static final String ATTR_SMS_CENTER_READONLY = "smsc_readonly";

    /**
     * [SMS] Message Center - Number - Default: (blank)
     */
    public static final String ATTR_SMS_CENTER_NUM = "smsc";

    /**
     * [SMS] Unicode characters - 7Bits Enable - Default: 0 (OFF)
     */
    public static final String ATTR_7BIT_ENCODING = "sms_char";

    /**
     * SMS] Timestamp - Default: 0 (OFF)
     */
    public static final String ATTR_TIME_STAMP = "sms_sent_time_mode";

    /**
     * [SMS] Validity Period - Default: 255 (Maximum)
     * Message - settings - Text Message(SMS) - SMS Validity Period
     */
    public static final String ATTR_SMS_VALIDITY = "sms_validity";

    /**
     * smsToMmsTextThreshold
     *  [SMS] Turn to MMS when SMS size is more than - Default: 5 (messages)
     */
    public static final String ATTR_SMS_TO_MMS_SIZE = "sms_concat";

    /**
     * [MMS] Delivery Report - Request Report - Default: 0 (OFF)
     * Message - Setting - MMS Settings - Delivery reports
     */
    public static final String ATTR_DELIVERY_REPORT_REQUEST = "mms_dr_r";

    /**
     * [MMS] Delivery Report - Allow Report - Default: 1 (ON)
     */
    public static final String ATTR_DELIVERY_REPORT_ALLOW = "mms_dr_a";

    /**
     * [[MMS] Read Reply - Request Reply - Default: 0 (OFF)
     */
    public static final String ATTR_READ_RREPLY_REQUEST = "mms_rr_r";

    /**
     * [MMS] Read Reply - Allow Reply - Default: 1 (ON)
     */
    public static final String ATTR_READ_RREPLY_ALLOW = "mms_rr_a";

    /**
     * [MMS] Auto Download/Retrieval Mode - Home - Default: 1 (ON)
     */
    public static final String ATTR_MMS_AUTO_RETRIEVE_HOME = "auto_retr";

    /**
     * [MMS] Auto Download/Retrieval Mode - Roaming - Default: 1 (ON)
     */
    public static final String ATTR_MMS_AUTO_RETRIEVE_ROAMING = "auto_retr_r";

    /**
     *  [MMS] Priority - Default: 1 (Normal)
     */
    public static final String ATTR_MMS_PRIORITY = "mms_priority";

    /**
     *  [MMS] Validity Period - Default: 6 (Maximum)
     */
    public static final String ATTR_MMS_VALIITY= "mms_validity";

    /**
     *  [MMS] Maximum Size - Default: 300 (Kb)
     */
    public static final String ATTR_MMS_SIZE= "mms_size";

    /**
     *  [MMS] Creation Mode - Options: 0 (Restricted), 1 (Warning), 2 (Free) - Default: 2
     */
    public static final String ATTR_MMS_CREATION= "mms_creation";

    /**
     *  [MMS] Slide Duration - Default: 5 (seconds)
     */
    public static final String ATTR_SLIDE_DURATION = "slide_dur";

    /**
     *  [MMS] Permited type - Advertisement - Default: 1 (ON)
     */
    public static final String ATTR_ADVER_PERMITED = "recv_adv";

    /**
     *  [INTERNET] Receive Push Message
     */
    public static final String ATTR_PUSH_ON = "push_on";

    /**
     *   [SMS] Delete old messages - Default: 1 (ON)
     */
    public static final String ATTR_DELETE_OLD= "del_old";

    /**
     *  SMS messages per thread limit(not on GPRI, but mandatory) - Default: 500
     */
    public static final String ATTR_SMS_LIMIT= "sms_limit";


    /**
     *  MMS messages per thread limit(not on GPRI, but mandatory) - Default: 50
     */
    public static final String ATTR_MMS_LIMIT= "mms_limit";

    /**
     *  Maximum MMS editor characters(not on GPRI, but mandatory) - Default: 2000
     */
    public static final String ATTR_MAX_EDITOR_NUM= "max_editor_num";

    /**
     *  Maximum slides (limit) on MMS creation(not on GPRI, but mandatory) - Default: 10
     */
    public static final String ATTR_MAX_SLIDE_NUM= "mms_slide_num";

    /**
     *  Maximum recipient limit(not on GPRI, but mandatory) - Default: 20
     */
    public static final String ATTR_MX_RECIPIENT_LIMIT= "mms_recipient_num";


    public static final String KEY_ATTR_SMS_DELIVERY_REPORT = "Message@SMS_Delivery_Report";
    public static final String KEY_ATTR_SMS_CENTER_READONLY = "Message@smsc_readonly";
    public static final String KEY_ATTR_SMS_CENTER_NUM = "Message@smsc";
    public static final String KEY_ATTR_7BIT_ENCODING = "Message@sms_char";
    public static final String KEY_ATTR_TIME_STAMP = "Message@sms_sent_time_mode";
    public static final String KEY_ATTR_SMS_VALIDITY = "Message@sms_validity";
    public static final String KEY_ATTR_SMS_TO_MMS_SIZE = "Message@sms_concat";
    public static final String KEY_ATTR_DELIVERY_REPORT_REQUEST = "Message@mms_dr_r";
    public static final String KEY_ATTR_DELIVERY_REPORT_ALLOW = "Message@mms_dr_a";
    public static final String KEY_ATTR_READ_RREPLY_REQUEST = "Message@mms_rr_r";
    public static final String KEY_ATTR_READ_RREPLY_ALLOW= "Message@mms_rr_a";
    public static final String KEY_ATTR_MMS_AUTO_RETRIEVE_HOME = "Message@auto_retr";
    public static final String KEY_ATTR_MMS_AUTO_RETRIEVE_ROAMING = "auto_retr_r";
    public static final String KEY_ATTR_MMS_PRIORITY = "Message@mms_priority";
    public static final String KEY_ATTR_MMS_VALIITY= "Message@mms_validity";
    public static final String KEY_ATTR_MMS_SIZE= "Message@mms_size";
    public static final String KEY_ATTR_MMS_CREATION= "Message@mms_creation";
    public static final String KEY_ATTR_SLIDE_DURATION = "Message@slide_dur";
    public static final String KEY_ATTR_ADVER_PERMITED = "Message@recv_adv";
    public static final String KEY_ATTR_PUSH_ON = "Message@push_on";
    public static final String KEY_ATTR_DELETE_OLD= "Message@del_old";
    public static final String KEY_ATTR_SMS_LIMIT= "Message@sms_limit";
    public static final String KEY_ATTR_MMS_LIMIT= "Message@mms_limit";
    public static final String KEY_ATTR_MAX_EDITOR_NUM= "Message@max_editor_num";
    public static final String KEY_ATTR_MAX_SLIDE_NUM= "Message@mms_slide_num";
    public static final String KEY_ATTR_MX_RECIPIENT_LIMIT= "Message@mms_recipient_num";

}
