package com.android.server.ecid;

import android.content.Context;
import android.util.Log;

import com.android.server.ecid.utils.GeneralParserAttribute;
import com.android.server.ecid.utils.SIMInfoConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import com.android.server.ecid.utils.*;
/**
 * Created by shiguibiao on 16-8-4.
 */

public class LgeConfigParser extends GeneralProfileParser {

    private static boolean mXmlMatchingData = false;
    public LgeConfigParser(Context context) {
        super(context);

    }


    public HashMap<String, String> loadLgProfile(String path, HashMap<String, String> map, LgeMccMncSimInfo siminfo) {

        try {
            File file = new File(path);
            in = new FileReader(file);

        } catch (FileNotFoundException e) {
            isFileExist = false;
            e.printStackTrace();
            return null;
        }

        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(in);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        ProfileData parsedData = getMatchedProfile(parser, siminfo, map);

        CAItem cp = (CAItem)parsedData;

        if (cp.getValueMap() != null)
        {
            changeGpriValueFromLGE(map, cp.getValueMap());
        }

        return cp.getValueMap();
    }

    void changeGpriValueFromLGE(HashMap<String, String> hashmap, HashMap<String, String> data)
    {
        Log.d(TAG,"LgeConfigParser---changeGpriValueFromLGE");
        HashMap<String, String> matchmap = new HashMap<String,String>();
        matchmap.put("Network@Data_Connection_Required", "enable_data_consumption_warning");
        matchmap.put("Network@Data_Connection_String", "sp_data_warning_noti_NORMAL");
        matchmap.put("Message@Voice_Mail_In_Home_Editable", "voice_mail_editable");
        matchmap.put("Settings@Connectivity_WiFi", "def_wifi_on");

        Iterator<String> key = matchmap.keySet().iterator();

        while(key.hasNext())
        {
            String tag = key.next();
            String matchString_Value = (String) matchmap.get(tag);
            String value = data.get(matchString_Value);
            if (value != null)
            {
                hashmap.put(tag, value);
                Log.d(TAG, "[getMatchedProfile] JUNAM Tag : "+tag +" value : "+value+" matching value : "+matchString_Value);
            }
        }
    }

    @Override
    protected ProfileData getMatchedProfile(XmlPullParser parser, LgeMccMncSimInfo simInfo, HashMap map) {
        CAItem item = getMatchedProfile(parser, simInfo, map, null);


        return item;
    }


    protected CAItem getMatchedProfile(XmlPullParser parser, LgeMccMncSimInfo simInfo, HashMap map, CAItem item) {

        Log.d(TAG,"LgeConfigParser---getMatchedProfile");
        int  match_priority  = -1;
        boolean match_siminfo = false;
        CAItem caItem = null;

        caItem = new CAItem();

        try {
            if (parser != null) {
                parser.next();
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String startTag = parser.getName();
                        if ( GeneralParserAttribute.ELEMENT_NAME_PROFILES.equals(startTag)) {
                            // Preceed configuration start TAG
                        } else if ( GeneralParserAttribute.ELEMENT_NAME_PROFILE.equals(startTag) ) {
                        } else if ( GeneralParserAttribute.ELEMENT_NAME_SIMINFO.equals(startTag)) {
                            match_priority = Utils.getMatchPriority(parser,simInfo,caItem);
                            if(match_priority> -1){
                                match_siminfo =  true;
                            }
                        } else {
                            // Check MNC/MCC/SPN values with device's and if current parsed informations are same
                            // with device, store settings items to shared user preferences. Until meeting END tag of CAItem
                            parser.next();
                            if ( startTag != null && parser.getText() != null  && match_siminfo) {
                                caItem.addSettingItem(startTag, parser.getText());
                                mXmlMatchingData = true;
                            }
                        }
                    } else if ( eventType == XmlPullParser.END_TAG ) {
                        if ( GeneralParserAttribute.ELEMENT_NAME_PROFILE.equals(parser.getName()) ) {
                            match_siminfo = false;
                        }
                    }

                    eventType = parser.next();
                }
            }
            //           //[WBT_S : 300321] 2012.01.13, seturn810.min@lge.com
            //           if (parser != null) {
            //               parser.close();
            //           }
            //[WBT_E : 300321] 2012.01.13, seturn810.min@lge.com
        } catch ( Exception e ) {
            // There is some problem in XML parsing process
            e.printStackTrace();
        }
        return caItem;
    }

}