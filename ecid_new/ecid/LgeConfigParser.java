package com.android.server.ecid;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.server.ecid.telephony.TelephonyParserAttribute;
import com.android.server.ecid.utils.ProfileData;
import com.android.server.ecid.utils.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by shiguibiao on 16-8-4.
 */

public class LgeConfigParser extends GeneralProfileParser implements TelephonyParserAttribute{
    private static final String TAG = Utils.APP + LgeConfigParser.class.getSimpleName();
    private static boolean mXmlMatchingData = false;
    private static HashMap<String, String> matchmap = new HashMap<String, String>();

    static {
        matchmap.put(KEY_ATTR_ENABLE_DATA_WARNING, ATTR_ENABLE_DATA_WARNING);
        matchmap.put(KEY_ATTR_DATA_WARNING_NOTIFY, ATTR_DATA_WARNING_NOTIFY);
        matchmap.put(KEY_ATTR_VOICE_MAIL_EDITABLE, ATTR_VOICE_MAIL_EDITABLE);
        matchmap.put(KEY_ATTR_VOICE_MAIL_ROAMING_EDITABLE, ATTR_VOICE_MAIL_ROAMING_EDITABLE);
    }

    public LgeConfigParser(Context context) {
        super(context);

    }


    @Override
    protected ProfileData readProfile(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        NameValueProfile p = new NameValueProfile();

        while (ELEMENT_NAME_SIMINFO.equals(parser.getName()) ||
                ELEMENT_NAME_FEATURESET.equals(parser.getName())) {
            nextElement(parser);
        }
        //Log.d(TAG,"readProfile---valid Element name="+parser.getName());
        while (parser.getName() != null
                &&(!parser.getName().equals(ELEMENT_NAME_PROFILE))) {

            String tag = parser.getName();
            //Log.d(TAG, "[readProfile] TAG : " + tag);

            String key = parser.getName();
            if (key != null) {
                int type = parser.next();
                if (type == XmlPullParser.TEXT) {
                    String value = parser.getText();
                    p.setValue(key, value);
                    //Log.d(TAG, "[readProfile] KEY : " + key + ", VALUE : " + value);
                }
            }
            nextElement(parser);
        }

        return (ProfileData)p;
    }

    @Override
    protected void changeGpriValueFromLGE(HashMap hashmap, ProfileData data) {
        Log.d(TAG, "changeGpriValueFromLGE");
        Iterator<String> key = matchmap.keySet().iterator();
        while (key.hasNext()) {
            String tag = key.next();
            String matchString_Value = (String) matchmap.get(tag);
            String value = ((NameValueProfile)data).getValue(matchString_Value);
            if (value != null) {
                    Settings.System.putString(mContext.getContentResolver(), TAG,
                            value);
                    Log.d(TAG, "[changeGpriValueFromLGE] KEY : " + tag + ", VALUE : " + value);
                hashmap.put(tag, value);
            }
        }
    }

}
