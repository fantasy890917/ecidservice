package com.android.server.ecid;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.app.ECIDManager.LgeSimInfo;
import com.android.server.ecid.utils.ProfileData;
import com.android.server.ecid.utils.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.android.server.ecid.mms.MmsParserAttribute;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


public class LgeMmsConfigParser extends GeneralProfileParser
        implements MmsParserAttribute {

    private static final String TAG = Utils.APP+LgeMmsConfigParser.class.getSimpleName();
    private static HashMap<String, String> matchmap = new HashMap<String, String>();

    static {
        matchmap.put(KEY_ATTR_SMS_DELIVERY_REPORT, ATTR_SMS_DELIVERY_REPORT);
        matchmap.put(KEY_ATTR_SMS_CENTER_READONLY, ATTR_SMS_CENTER_READONLY);
        matchmap.put(KEY_ATTR_SMS_CENTER_NUM, ATTR_SMS_CENTER_NUM);
        matchmap.put(KEY_ATTR_7BIT_ENCODING, ATTR_7BIT_ENCODING);
        matchmap.put(KEY_ATTR_TIME_STAMP, ATTR_TIME_STAMP);
        matchmap.put(KEY_ATTR_SMS_VALIDITY,ATTR_SMS_VALIDITY );
        matchmap.put(KEY_ATTR_SMS_TO_MMS_SIZE, ATTR_SMS_TO_MMS_SIZE);
        matchmap.put(KEY_ATTR_DELIVERY_REPORT_REQUEST, ATTR_DELIVERY_REPORT_REQUEST);
        matchmap.put(KEY_ATTR_DELIVERY_REPORT_ALLOW, ATTR_DELIVERY_REPORT_ALLOW);
        matchmap.put(KEY_ATTR_READ_RREPLY_REQUEST, ATTR_READ_RREPLY_REQUEST);
        matchmap.put(KEY_ATTR_READ_RREPLY_ALLOW, ATTR_READ_RREPLY_ALLOW);
        matchmap.put(KEY_ATTR_MMS_AUTO_RETRIEVE_HOME, ATTR_MMS_AUTO_RETRIEVE_HOME );
        matchmap.put(KEY_ATTR_MMS_AUTO_RETRIEVE_ROAMING, ATTR_MMS_AUTO_RETRIEVE_ROAMING );
        matchmap.put(KEY_ATTR_MMS_PRIORITY, ATTR_MMS_PRIORITY);
        matchmap.put(KEY_ATTR_MMS_VALIITY, ATTR_MMS_VALIITY);
        matchmap.put(KEY_ATTR_MMS_SIZE, ATTR_MMS_SIZE);
        matchmap.put(KEY_ATTR_MMS_CREATION, ATTR_MMS_CREATION);
        matchmap.put(KEY_ATTR_SLIDE_DURATION, ATTR_SLIDE_DURATION);
        matchmap.put(KEY_ATTR_ADVER_PERMITED, ATTR_ADVER_PERMITED);
        matchmap.put(KEY_ATTR_PUSH_ON, ATTR_PUSH_ON);
        matchmap.put(KEY_ATTR_DELETE_OLD, ATTR_DELETE_OLD );
        matchmap.put(KEY_ATTR_SMS_LIMIT, ATTR_SMS_LIMIT);
        matchmap.put(KEY_ATTR_MMS_LIMIT, ATTR_MMS_LIMIT);
        matchmap.put(KEY_ATTR_MAX_EDITOR_NUM, ATTR_MAX_EDITOR_NUM);
        matchmap.put(KEY_ATTR_MAX_SLIDE_NUM, ATTR_MAX_SLIDE_NUM);
        matchmap.put(KEY_ATTR_MX_RECIPIENT_LIMIT, ATTR_MX_RECIPIENT_LIMIT);
    }
    public LgeMmsConfigParser(Context context) {
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
        Log.d(TAG,"readProfile---valid Element name="+parser.getName());
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
        Log.d(TAG,"changeGpriValueFromLGE");
        Iterator<String> key = matchmap.keySet().iterator();

        while (key.hasNext()) {
            String tag = key.next();
            String matchString_Value = (String) matchmap.get(tag);
            String value = ((NameValueProfile)data).getValue(matchString_Value);
            if (value != null) {
                //Log.d(TAG,"match:"+matchString_Value+" value:"+value);
                Settings.System.putString(mContext.getContentResolver(), tag,
                        value);
                hashmap.put(tag, value);
            }
        }
    }

}


