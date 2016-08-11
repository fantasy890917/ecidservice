/*
 * ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.40
 * 
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * -----------------------------------------------------------------------------
 */

package com.android.server.ecid.email;

public enum EEMAIL_AUTHENTICATION_TYPE {
    eEMAIL_AUTHENTICATION_TYPE_NO_AUTH(0x00),
    eEMAIL_AUTHENTICATION_TYPE_SMTP_AUTH(0x01),
    eEMAIL_AUTHENTICATION_TYPE_APOP(0x02),
    eEMAIL_AUTHENTICATION_TYPE_EAS_PROVISIONING(0x04);

    public final int swigValue() {
        return swigValue;
    }

    public static EEMAIL_AUTHENTICATION_TYPE swigToEnum(int swigValue) {
        EEMAIL_AUTHENTICATION_TYPE[] swigValues = EEMAIL_AUTHENTICATION_TYPE.class.getEnumConstants();
        if (swigValues != null) { // WBT fix TD # 324752
            if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
                return swigValues[swigValue];
            for (EEMAIL_AUTHENTICATION_TYPE swigEnum : swigValues)
                if (swigEnum.swigValue == swigValue)
                    return swigEnum;
        }
        throw new IllegalArgumentException("No enum " + EEMAIL_AUTHENTICATION_TYPE.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private EEMAIL_AUTHENTICATION_TYPE() {
        this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private EEMAIL_AUTHENTICATION_TYPE(int swigValue) {
        this.swigValue = swigValue;
        SwigNext.next = swigValue + 1;
    }

    @SuppressWarnings("unused")
    private EEMAIL_AUTHENTICATION_TYPE(EEMAIL_AUTHENTICATION_TYPE swigEnum) {
        this.swigValue = swigEnum.swigValue;
        SwigNext.next = this.swigValue + 1;
    }

    private final int swigValue;

    private static class SwigNext {
        private static int next = 0;
    }
}
