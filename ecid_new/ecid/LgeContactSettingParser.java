
package com.android.server.ecid;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.server.ecid.contacts.ContactsParserAttribute;
import com.android.server.ecid.utils.ProfileData;
import com.android.server.ecid.utils.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class LgeContactSettingParser extends GeneralProfileParser
        implements ContactsParserAttribute {
    private static final String TAG = Utils.APP + LgeContactSettingParser.class.getSimpleName();
    private static HashMap<String, String> matchmap = new HashMap<String, String>();

    static {
        matchmap.put(KEY_ATTR_DEFAULT_STORAGE, ATTR_DEFAULT_STORAGE);
        matchmap.put(KEY_ATTR_DISPLAY_NUMBER, ATTR_DISPLAY_NUMBER);
        matchmap.put(KEY_ATTR_DISPLY_SDN, ATTR_DISPLY_SDN);
    }


    public LgeContactSettingParser(Context context) {
        super(context);
    }

    protected ProfileData readProfile(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        NameValueProfile p = new NameValueProfile();

        while (ELEMENT_NAME_SIMINFO.equals(parser.getName()) ||
                ELEMENT_NAME_FEATURESET.equals(parser.getName())) {
            nextElement(parser);
        }

        while (ELEMENT_NAME_ITEM.equals(parser.getName())) {

            String tag = parser.getName();
            String key = parser.getAttributeValue(null, ATTR_NAME);
            if (VDBG) {
                Log.d(TAG, "[readProfile] key : " + key);
            }
            if (key != null) {
                int type = parser.next();
                if (VDBG) {
                    Log.d(TAG, "[readProfile] type : " + type);
                }
                if (type == XmlPullParser.TEXT) {
                    String value = parser.getText();
                    p.setValue(key, value);
                    if (VDBG) {
                        Log.d(TAG, "[readProfile] KEY : " + key + ", VALUE : " + value);
                    }
                }
            }
            nextElement(parser);
        }

        return (ProfileData) p;
    }

    protected void changeGpriValueFromLGE(HashMap hashmap, ProfileData data) {
        Log.d(TAG, "changeGpriValueFromLGE");
        Iterator<String> key = matchmap.keySet().iterator();
        while (key.hasNext()) {
            String tag = key.next();
            String matchString_Value = (String) matchmap.get(tag);
            String value = ((NameValueProfile) data).getValue(matchString_Value);
            if (!Utils.isEmpty(value)) {
                if(tag.equals(KEY_ATTR_DEFAULT_STORAGE)){
                    Settings.System.putString(mContext.getContentResolver(), KEY_ATTR_DEFAULT_STORAGE,
                            value);
                    Log.d(TAG, "[changeGpriValueFromLGE] KEY : " + tag + ", VALUE : " + value);
                }
                //Log.d(TAG, "[changeGpriValueFromLGE] KEY : " + tag + ", VALUE : " + value);
                hashmap.put(tag, value);
            }


        }
    }

}
