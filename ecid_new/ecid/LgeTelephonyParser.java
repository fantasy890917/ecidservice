package com.android.server.ecid;

import android.content.Context;
import android.util.Log;

import com.android.server.ecid.telephony.TelephonyParserAttribute;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import com.android.server.ecid.utils.*;
public class LgeTelephonyParser extends GeneralProfileParser implements TelephonyParserAttribute {

	private static final String TAG = Utils.APP+LgeTelephonyParser.class.getSimpleName();
	private static HashMap<String, String> matchmap = new HashMap<String,String>();
	public LgeTelephonyParser(Context context) {
		super(context);
		
	}
	static{
		matchmap.put(KEY_ATTR_NAME_NO_SIM_ECC_LIST, ATTR_NAME_NO_SIM_ECC_LIST);
		matchmap.put(KEY_ATTR_NAME_ECC_LIST, ATTR_NAME_ECC_LIST);
		matchmap.put(KEY_ATTR_NAME_ECC_IDLE_LIST, ATTR_NAME_ECC_IDLE_LIST);
		matchmap.put(KEY_ATTR_NAME_ECC_SIM_LOCK_LIST, ATTR_NAME_ECC_SIM_LOCK_LIST);
		matchmap.put(KEY_ATTR_NAME_VMS, ATTR_NAME_VMS);
		matchmap.put(KEY_ATTR_NAME_RVMS, ATTR_NAME_RVMS);
		matchmap.put(KYE_ATTR_NAME_SEND_MY_NUMBER, ATTR_NAME_SEND_MY_NUMBER);
		matchmap.put(KEY_ATTR_NAME_SHORT_CODE_CALL, ATTR_NAME_SHORT_CODE_CALL);
		matchmap.put(KEY_ATTR_NAME_TBCW, ATTR_NAME_TBCW);
		matchmap.put(KEY_ATTR_NAME_FULL_TBCW, ATTR_NAME_FULL_TBCW);
		matchmap.put(KEY_ATTR_NAME_A_SRVCC, ATTR_NAME_SRVCC);
		matchmap.put(KEY_ATTR_NAME_E_SRVCC, ATTR_NAME_E_SRVCC);
		matchmap.put(KEY_ATTR_NAME_MID_CALL_SRVCC, ATTR_NAME_MID_CALL_SRVCC);
	}

	@Override
	protected ProfileData readProfile(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		NameValueProfile p = new NameValueProfile();
		//Log.d(TAG,"[readProfile] name="+parser.getName());
		while (ELEMENT_NAME_SIMINFO.equals(parser.getName()) ||
				ELEMENT_NAME_FEATURESET.equals(parser.getName())) {
			nextElement(parser);
		}

		while (parser.getName() != null
				&&(!parser.getName().equals(ELEMENT_NAME_PROFILE))) {

			String tag = parser.getName();
			//Log.d(TAG, "[readProfile] TAG : " + tag);
			String key = parser.getAttributeValue(null, ATTR_NAME);
			//Log.d(TAG, "[readProfile] key : " + key);
			if (key != null) {
				int type = parser.next();
				//Log.d(TAG, "[readProfile] type : " + type);
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
    protected void changeGpriValueFromLGE(HashMap hashmap, ProfileData data)
    {
		Log.d(TAG,"changeGpriValueFromLGE");
        Iterator<String> key = matchmap.keySet().iterator(); 
        
        while(key.hasNext()) 
        {
            String tag = key.next();
            String matchString_Value = (String) matchmap.get(tag);
            String value = ((NameValueProfile)data).getValue(matchString_Value);
            if (value != null) 
            {
                hashmap.put(tag, value);
            }
        }
    }
}
