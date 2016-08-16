package com.android.server.ecid;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import android.app.ECIDManager.LgeSimInfo;
import com.android.server.ecid.utils.ProfileData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import com.android.server.ecid.settings.SettingsParserAttribute;
import com.android.server.ecid.utils.*;
public class LgeSettingsProviderParser
		extends GeneralProfileParser
		implements SettingsParserAttribute{
	private static final String TAG = Utils.APP+LgeSettingsProviderParser.class.getSimpleName();
	private static HashMap<String, String> matchmap = new HashMap<String, String>();

	static {
		matchmap.put(KEY_ATTR_DEFAULT_LANGUAGE, ATTR_DEFAULT_LANGUAGE);
		matchmap.put(KEY_ATTR_DEFAULT_TIMEZONE, ATTR_DEFAULT_TIMEZONE);
		matchmap.put(KEY_ATTR_SOUND_PROFILE, ATTR_SOUND_PROFILE);
		matchmap.put(KEY_ATTR_TIME_FORMAT, ATTR_TIME_FORMAT);
		matchmap.put(KEY_ATTR_DATE_FORMAT, ATTR_DATE_FORMAT);
		matchmap.put(KEY_ATTR_AUTO_TIME, ATTR_AUTO_TIME);
		matchmap.put(KEY_ATTR_SCREEN_OFF_TIME, ATTR_SCREEN_OFF_TIME);
		matchmap.put(KEY_ATTR_DTMF_TONE_WHEN_DIAL, ATTR_DTMF_TONE_WHEN_DIAL);
		matchmap.put(KEY_ATTR_SOUND_EFFECTS, ATTR_SOUND_EFFECTS);
		matchmap.put(KEY_ATTR_MOBILE_DATA, ATTR_MOBILE_DATA);
		matchmap.put(KEY_ATTR_DATA_ROAMING, ATTR_DATA_ROAMING);
		matchmap.put(KEY_ATTR_LOCATION_ALLOWED, ATTR_LOCATION_ALLOWED);
		matchmap.put(KEY_ATTR_INSTALL_NON_MARKET_APPS, ATTR_INSTALL_NON_MARKET_APPS);
		matchmap.put(KEY_ATTR_NOTIFICATION_VIBRATE, ATTR_NOTIFICATION_VIBRATE);
		matchmap.put(KEY_ATTR_RINGTONE_VIBRATION, ATTR_RINGTONE_VIBRATION);
	}
	public LgeSettingsProviderParser(Context context) {
		super(context);

	}



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
				switch(tag){
					case KEY_ATTR_DEFAULT_LANGUAGE:{

						break;
					}
					case KEY_ATTR_DEFAULT_TIMEZONE:{

						break;
					}
					case KEY_ATTR_SOUND_PROFILE:{

						break;
					}
					case KEY_ATTR_TIME_FORMAT:{

						break;
					}
					case KEY_ATTR_DATE_FORMAT:{

						break;
					}
					case KEY_ATTR_AUTO_TIME:{

						break;
					}
					case KEY_ATTR_SCREEN_OFF_TIME:{

						break;
					}
					case KEY_ATTR_DTMF_TONE_WHEN_DIAL:{

						break;
					}
					case KEY_ATTR_SOUND_EFFECTS:{

						break;
					}
					case KEY_ATTR_MOBILE_DATA:{
						Settings.System.putInt(mContext.getContentResolver(), KEY_ATTR_MOBILE_DATA,
								Integer.valueOf(value));
						break;
					}
					case KEY_ATTR_DATA_ROAMING:{
						//default data roaming set
						break;
					}
					case KEY_ATTR_LOCATION_ALLOWED:{

						break;
					}
					case KEY_ATTR_INSTALL_NON_MARKET_APPS:{

						break;
					}
					case KEY_ATTR_NOTIFICATION_VIBRATE:{

						break;
					}case KEY_ATTR_RINGTONE_VIBRATION:{

						break;
					}
					default:
						break;
				}
            }
        }
    }

	@Override
	protected ProfileData readProfile(XmlPullParser parser) throws XmlPullParserException, IOException {


		NameValueProfile p = new NameValueProfile();
		while (ELEMENT_NAME_SIMINFO.equals(parser.getName())
				|| ELEMENT_NAME_FEATURESET.equals(parser.getName())) {
			nextElement(parser);
		}

		while (ELEMENT_NAME_SYSTEMPROPERTY.equals(parser.getName()) 
				|| ELEMENT_NAME_SETTINGSSYSTEM.equals(parser.getName())
				|| ELEMENT_NAME_SETTINGSSECURE.equals(parser.getName()) ) {

			String tag = parser.getName();
			if (true) {
				//Log.d(TAG, "[readProfile] TAG : " + tag);
			}
			
			int AttributeNum = parser.getAttributeCount();
			
			for (int i = 0; i < AttributeNum; i++) {
				String key = parser.getAttributeName(i);
				String value = parser.getAttributeValue(i);
				//Log.d(TAG,"[readProfile] Key : "+key+" value:"+value);
				if((!Utils.isEmpty(key))
						&& (!Utils.isEmpty(value))){
					p.setValue(key,value);
				}
			}
			nextElement(parser);

		}
		return (ProfileData)p;
	}
}

	



