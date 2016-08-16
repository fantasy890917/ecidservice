package com.android.server.ecid.telephony;

/**
 * Created by shiguibiao on 16-8-14.
 */

public interface TelephonyParserAttribute {
    /**
     * Lteready.xml attr
     */
    public static final String ATTR_NAME_VALUE = "value";
    public static final String ATTR_NAME_LTE_MODE = "ltemode";

    /**
     * lteready.xml element
     */
    public static final String ELEMENT_NAME_LTE = "lte";

    /**
     * map key for letready
     */
    public static final String KEY_LTEREADY_VALUE = "Lteready@value";
    public static final String KEY_LTEREADY_MODE = "Lteready@ltemode";

    /**
     * telephony.xml attr
     * no sim/has sim always set as ecc number
     */
    public static final String ATTR_NAME_NO_SIM_ECC_LIST = "no_sim_ecclist";

    /**
     * telephony.xml attr
     * no sim/has sim always set as ecc number, same with no_sim_list
     */
    public static final String ATTR_NAME_ECC_LIST = "ECC_list";

    /*

    /**
     * telephony.xml attr
     * has sim set as ecc number
     */
    public static final String ATTR_NAME_ECC_IDLE_LIST = "ECC_IdleMode";

    /**
     * telephony.xml attr
     * SIM pin/puk lock set ecc
     */
    public static final String ATTR_NAME_ECC_SIM_LOCK_LIST = "sim_lock_ecclist";

    /**
     * telephony.xml attr
     * Voice Mail Service
     */
    public static final String ATTR_NAME_VMS = "VMS";

    /**
     * telephony.xml attr
     * Roaming Voice Mail Service
     */
    public static final String ATTR_NAME_RVMS = "RVMS";

    /**
     * telephony.xml attr
     * SCA caller ID
     0：set by network
     1: off
     2 : on
     */
    public static final String ATTR_NAME_SEND_MY_NUMBER = "SendMyNumberInformation";

    /**
     * telephony.xml attr
     * use cc call.not ussd
     */
    public static final String ATTR_NAME_SHORT_CODE_CALL = "ShortCodeCall";

    /**
     * telephony.xml attr
     * pending by LG
     */
    public static final String ATTR_NAME_TBCW= "TBCW";

    /**
     * telephony.xml attr
     * pending by LG
     */
    public static final String ATTR_NAME_FULL_TBCW = "FULL_TBCW";

    /**
     * telephony.xml attr
     * SRVCC_Support (mtk default support)
     */
    public static final String ATTR_NAME_SRVCC= "SRVCC_Support";

    /**
     * telephony.xml attr
     *aSRVCC  (mtk default support)
     */
    public static final String ATTR_NAME_A_SRVCC = "aSRVCC";

    /**
     * telephony.xml attr
     *  eSRVCC (mtk default support)
     */
    public static final String ATTR_NAME_E_SRVCC = "eSRVCC";

    /**
     * telephony.xml attr
     * MidCall_SRVCC (mtk default support)
     */
    public static final String ATTR_NAME_MID_CALL_SRVCC = "MidCall_SRVCC";


    public static final String KEY_ATTR_NAME_NO_SIM_ECC_LIST = "telephony@no_sim_ecclist";
    public static final String KEY_ATTR_NAME_ECC_LIST = "telephony@ECC_list";
    public static final String KEY_ATTR_NAME_ECC_IDLE_LIST = "telephony@ECC_IdleMode";
    public static final String KEY_ATTR_NAME_ECC_SIM_LOCK_LIST = "telephony@sim_lock_ecclist";
    public static final String KEY_ATTR_NAME_VMS = "telephony@VMS";
    public static final String KEY_ATTR_NAME_RVMS = "telephony@RVMS";
    public static final String KYE_ATTR_NAME_SEND_MY_NUMBER = "telephony@SendMyNumberInformation";
    public static final String KEY_ATTR_NAME_SHORT_CODE_CALL = "telephony@ShortCodeCall";
    public static final String KEY_ATTR_NAME_TBCW= "telephony@TBCW";
    public static final String KEY_ATTR_NAME_FULL_TBCW = "telephony@FULL_TBCW";
    public static final String KEY_ATTR_NAME_SRVCC= "telephony@SRVCC_Support";
    public static final String KEY_ATTR_NAME_A_SRVCC = "telephony@aSRVCC";
    public static final String KEY_ATTR_NAME_E_SRVCC = "telephony@eSRVCC";
    public static final String KEY_ATTR_NAME_MID_CALL_SRVCC = "telephony@MidCall_SRVCC";


    public static final String ATTR_ENABLE_DATA_WARNING = "enable_data_consumption_warning";
    public static final String ATTR_DATA_WARNING_NOTIFY = "sp_data_warning_noti_NORMAL";
    public static final String ATTR_VOICE_MAIL_EDITABLE = "voice_mail_editable";
    public static final String ATTR_VOICE_MAIL_ROAMING_EDITABLE = "voice_mail_in_roaming_editable";

    public static final String KEY_ATTR_ENABLE_DATA_WARNING = "elephony@enable_data_consumption_warning";
    public static final String KEY_ATTR_DATA_WARNING_NOTIFY = "elephony@sp_data_warning_noti_NORMAL";
    public static final String KEY_ATTR_VOICE_MAIL_EDITABLE = "elephony@voice_mail_editable";
    public static final String KEY_ATTR_VOICE_MAIL_ROAMING_EDITABLE = "elephony@voice_mail_in_roaming_editable";

    public static String MODULE_NAME_TELEPHONY = "telephony";

}
