/*
* Copyright (C) 2014 MediaTek Inc.
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.ecid;


import android.app.IECIDManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.KeyValueListParser;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeSet;
import com.android.server.SystemService;
import android.app.ECIDManager;
import android.app.ECIDManager.LgeSimInfo;
import com.android.server.ecid.utils.*;
import android.app.ECIDManager.EmailSettingsInfo;
import android.app.ECIDManager.EmailSettingsProtocol;
import android.app.ECIDManager.EmailServerProvider;
import android.app.ECIDManager.Bookmark;
import java.util.List;
import android.content.IntentFilter;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.uicc.IccCardProxy;
import android.os.Looper;
import android.provider.Telephony;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.server.ecid.utils.GeneralParserAttribute;
import com.android.server.ecid.utils.Utils;
import com.android.server.ecid.bookmarks.BrowserParserAttribute;
import com.android.server.ecid.cellbroadcast.CBParserAttribute;
import com.android.server.ecid.contacts.ContactsParserAttribute;
import com.android.server.ecid.email.EmailParserAttribute;
import com.android.server.ecid.telephony.TelephonyParserAttribute;
import android.provider.Settings;

/// M: end

public class ECIDManagerService extends SystemService implements GeneralParserAttribute {

    public static final String TAG = "ECIDManager";

    private final Context mContext;
    private final Handler mHandler;
    
    private static boolean mSlot1Absent = false;
    private static boolean mSlot2Absent = false;
    private static boolean hasLoadedOtherNotBasedSim = false;
    private static boolean isLoading = false;

    public static final int MSG_PASER_GPRI_NONE_RELATED_SIM = 0;
    public static final int MSG_PARSER_GPRI_WITH_SIM = 1;
    public static final int MSG_PASER_GPRI_RUNTIME = 2; //runtime use eg:bookmark,next boot use it

    private static int currentParserSlotId = PhoneConstants.SIM_ID_1 ;
    private LgeSimInfo currentSimIno = null;
    private SubscriptionInfo currentSub = null;
    private static String mCurrentMccMnc = null;
    private  LgeSimInfo simInfo = null;
    private static HashMap<String,String> mContactsMap = null;
    private static HashMap<String,String> mBrowserMap = null;
    private ArrayList<Bookmark> mBookmarkList = null;
    private static HashMap<String,String> mMessageMap = null;
    private static HashMap<String,String> mTeleServiceMap = null;
    private static HashMap<String,String> mCBConfigMap = null;
    private ArrayList<String> mCBChList = null;
    private ArrayList<String> mCBChNameList = null;
    private static HashMap<String,String> mTelephonyMap = null;
    private static HashMap<String,String> mLteReadyMap = null;
    private static HashMap<String,String> mSettingsProviderMap = null;
    private static ArrayList<EmailServerProvider> mEmailAccountList = null;
    private static EmailSettingsInfo mEmailSettingsInfo = null;
    private static EmailSettingsProtocol mEmailSettingsProtocol = null;
    private static HashMap<String,String> mAmrWBMap = null;

    private static String mMcc = null;
    private static String mMnc = null;
    private static String mGid = null;
    private static String mSpn = null;
    private static String mImsi = null;


    private static String mLoaded_flag = null;
    public ECIDManagerService(Context context) {
        super(context);
        mContext = context;
        Log.d(TAG, "ECIDManagerService");

        if(!hasLoaded()){
            IntentFilter intentFilter = new IntentFilter(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
            intentFilter.addAction(IccCardProxy.ACTION_INTERNAL_SIM_STATE_CHANGED);
            intentFilter.addAction(TelephonyIntents.ACTION_SUBINFO_RECORD_UPDATED);
            mContext.registerReceiver(sReceiver, intentFilter);
        }
        mHandler = new LgeParserUpdateHandler(context.getMainLooper());
        
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        publishBinderService(Context.ECID_PARSER_SERVICE, mService);
        String test = Settings.System.getString(mContext.getContentResolver(), ContactsParserAttribute.KEY_ATTR_DEFAULT_STORAGE);
        Log.d(TAG,"FANTA test =="+test);
        if(hasLoaded() && getSimProerty()){
            mHandler.obtainMessage(MSG_PASER_GPRI_RUNTIME).sendToTarget();
        }
    }

    private final BroadcastReceiver sReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action=" + action);
            if(hasLoaded() || isLoading ){
                return;
            }
            if(action.equals(IccCardProxy.ACTION_INTERNAL_SIM_STATE_CHANGED)){

            }else if(action.equals(TelephonyIntents.ACTION_SIM_STATE_CHANGED)){
                String simStatus = intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE);
                int slotId = intent.getIntExtra(PhoneConstants.SLOT_KEY, PhoneConstants.SIM_ID_1);
                Log.d(TAG,"simStatus=="+simStatus+" slotId=="+slotId);
                if(simStatus.equals(IccCardConstants.INTENT_VALUE_ICC_ABSENT)){
                    if(slotId == PhoneConstants.SIM_ID_1){
                        mSlot1Absent = true;
                    }else if(slotId == PhoneConstants.SIM_ID_2){
                        mSlot2Absent = true;
                    }
                }else if(simStatus.equals(IccCardConstants.INTENT_VALUE_ICC_LOADED)){
                    if(slotId == PhoneConstants.SIM_ID_1){
                        mSlot1Absent = false;
                    }else if(slotId == PhoneConstants.SIM_ID_2){
                        mSlot2Absent = false;
                    }
                }else{
                    Log.d(TAG,"this is not normal sim state(lock and so on)...ignore it");
                }
            }else if(action.equals(TelephonyIntents.ACTION_SUBINFO_RECORD_UPDATED)){
                if(!nullSimDevice() && !hasCorrectMccMnc()){
                    setCurrentSimInfo();
                }

            }
        }
    };

    private boolean nullSimDevice(){
        return mSlot1Absent && mSlot2Absent;
    }

    private static boolean hasLoaded(){
        return SystemProperties.get(ECID_LOADED_FLAG,"0").equals("1");
    }
    private void setCurrentSimInfo(){
        List<SubscriptionInfo> subs = SubscriptionManager.from(mContext)
                .getActiveSubscriptionInfoList();
        if(subs == null)
            return ;
        final TelephonyManager telephonyManager = (TelephonyManager)
                mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final SubscriptionManager subscriptionManager = SubscriptionManager.from(mContext);
        final int numSlots = telephonyManager.getSimCount();
        Phone phone = null;

        if(numSlots ==1){
            Log.d(TAG,"signal sim ");
        }else if(numSlots ==2){
            Log.d(TAG,"dual sim ");
        }

        if(subs.size() == 1){
            currentParserSlotId =subs.get(0).getSimSlotIndex();
            currentSub = subs.get(0);
        }else if(subs.size()>1){
            currentParserSlotId =PhoneConstants.SIM_ID_1;
            for(int i=0 ;i<subs.size();i++){
                if(subs.get(i).getSimSlotIndex()==currentParserSlotId){
                    currentSub = subs.get(i);
                    break;
                }
            }
        }
        int subId = currentSub.getSubscriptionId();
        mCurrentMccMnc = telephonyManager.getSimOperator(subId);
        Log.d(TAG,"mccmnc =="+mCurrentMccMnc);
        if(!hasCorrectMccMnc()){
                return;
        }
        mMcc = mCurrentMccMnc.substring(0, 3);
        mMnc = mCurrentMccMnc.substring(3);
        mSpn = telephonyManager.getSimOperatorNameForSubscription(subId);
        mImsi = telephonyManager.getSubscriberId(subId);
        mGid = telephonyManager.getGroupIdLevel1(subId);
        currentSimIno = new LgeSimInfo(mMcc, mMnc, mGid, mSpn, mImsi, currentParserSlotId);
        setSimProperty();
        Log.d(TAG,"currentSimIno =="+currentSimIno.toString());

        mHandler.obtainMessage(MSG_PARSER_GPRI_WITH_SIM).sendToTarget();
    }

    private void setSimProperty(){
        SystemProperties.set(ECID_FIRST_VALID_MCC,mMcc);
        SystemProperties.set(ECID_FIRST_VALID_MNC,mMnc);
        if(!Utils.isEmpty(mGid)){
            SystemProperties.set(ECID_FIRST_VALID_GID,mGid);
        }

        if(!Utils.isEmpty(mImsi)){
            SystemProperties.set(ECID_FIRST_VALID_IMSI,mImsi);
        }

        if(!Utils.isEmpty(mSpn)){
            SystemProperties.set(ECID_FIRST_VALID_SPN,mSpn);
        }
    }

    private boolean getSimProerty(){
        Log.d(TAG,"getSimProerty()");
        mMcc = SystemProperties.get(ECID_FIRST_VALID_MCC,"");
        mMnc = SystemProperties.get(ECID_FIRST_VALID_MNC,"");
        mGid = SystemProperties.get(ECID_FIRST_VALID_GID,"");
        mImsi = SystemProperties.get(ECID_FIRST_VALID_IMSI,"");
        mSpn = SystemProperties.get(ECID_FIRST_VALID_SPN,"");
        if(!Utils.isEmpty(mMcc)
                && !Utils.isEmpty(mMnc)){
            if(Utils.isEmpty(mGid)){
                mGid = null;
            }

            if(Utils.isEmpty(mImsi)){
                mImsi = null;
            }

            if(Utils.isEmpty(mSpn)){
                mSpn = null;
            }

            currentSimIno = new LgeSimInfo(mMcc, mMnc, mGid, mSpn, mImsi, currentParserSlotId);
            return true;
        }
        return false;
    }
    private final class LgeParserUpdateHandler extends Handler {
        public HashMap<String, String> mLteParserMap = new HashMap<String, String>();

        public LgeParserUpdateHandler(Looper looper) {
            super(looper, null, false);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "LgeParserUpdateHandler handleMessage" + msg.what);
            switch (msg.what) {
                case MSG_PASER_GPRI_NONE_RELATED_SIM:{
                    ParserUpdateThread updatorThread = new ParserUpdateThread(
                            new ParserUserObj(MSG_PASER_GPRI_NONE_RELATED_SIM, currentParserSlotId),
                            MSG_PASER_GPRI_NONE_RELATED_SIM);
                    updatorThread.start();
                    break;
                }
                case MSG_PARSER_GPRI_WITH_SIM:{
                    ParserUpdateThread updatorThread = new ParserUpdateThread(
                            new ParserUserObj(MSG_PARSER_GPRI_WITH_SIM, currentParserSlotId),
                            MSG_PARSER_GPRI_WITH_SIM);
                    updatorThread.start();
                    break;
                }
                case MSG_PASER_GPRI_RUNTIME:{
                    ParserUpdateThread updatorThread = new ParserUpdateThread(
                            new ParserUserObj(MSG_PASER_GPRI_RUNTIME, currentParserSlotId),
                            MSG_PASER_GPRI_RUNTIME);
                    updatorThread.start();
                    break;
                }
                default:
                    Log.d(TAG, "Unknown msg:" + msg.what);
            }
        }
    }
    
    private boolean hasCorrectMccMnc(){
     if(mCurrentMccMnc ==null || mCurrentMccMnc.isEmpty()){
      return false;
     }
     return true;
    }

    private class ParserUpdateThread extends Thread {

        private ParserUserObj mUserObj;
        private int mEventId;

        ParserUpdateThread(ParserUserObj userObj, int eventId) {
            mUserObj = userObj;
            mEventId = eventId;
        }

        @Override
        public void run() {
            switch (mEventId) {
                case MSG_PASER_GPRI_NONE_RELATED_SIM:
                    parserCommonGPRIWithOutSim();
                    break;
                case MSG_PARSER_GPRI_WITH_SIM:
                    parserSpecialGPRIWithSim();
                    break;
                case MSG_PASER_GPRI_RUNTIME:
                    parserRuntimeGPRI();
                    break;
                default:
                    break;
            }
        }
    }

    private static class ParserUserObj {
        public int reason;
        public int slotId;

        ParserUserObj(int reason, int slotId) {
            this.reason = reason;
            this.slotId = slotId;
        }
    }

    private void parserCommonGPRIWithOutSim(){

    }

    private void parserRuntimeGPRI(){
        //bookamark
        if(Settings.System.getInt(mContext.getContentResolver(), BrowserParserAttribute.HAS_LOADED_BROWSER_DEFAULT_SETTINGS,
                0) !=1){
            //parser....
            parserBrowser();
        }
        if(Settings.System.getInt(mContext.getContentResolver(), EmailParserAttribute.HAS_LOADED_EMAIL_DEFAULT_SETTINGS,
                0) !=1){
            //email
            parserEmail();
        }
        if(Settings.System.getInt(mContext.getContentResolver(), CBParserAttribute.HAS_LOADED_CB_DEFAULT_SETTINGS,
                0) !=1){
            //cb
            parserCBConfig();
        }

        //parser telephony
        parserTelephony();
    }

    private void parserSpecialGPRIWithSim(){
        //is loading true
        isLoading = true;
        simInfo = new LgeSimInfo("647","10");
        //parser contacts
        parserContacts();
        //parser browser
        parserBrowser();
        //parser MMS
        parserMessage();
        //parser telephonyconfig
        parserTelephonyConfig();
        // parser cell broadcast
        parserCBConfig();
        //parser teleohny
        parserTelephony();
        //ltemode
        parserLteReady();
        //settings provider
        parserSettings();
        //parser Email
        parserEmail();
        //amr-wb
        parserAmrWb();
        SystemProperties.set(ECID_LOADED_FLAG,"1");
        isLoading = false ;
    }
    private void parserTelephony(){
        mTelephonyMap = new HashMap<String,String>();
        Log.d(TAG,"Parser GPRI: telephony.xml");
        GeneralProfileParser lgeTelephonyParser = new LgeTelephonyParser(mContext);
        lgeTelephonyParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_TELEPHONY_PROFILE, mTelephonyMap, simInfo);
        Log.d(TAG,"mTelephonyMap=="+mTelephonyMap.toString());
    }
    private void parserCBConfig(){
        mCBConfigMap = new HashMap<String,String>();
        Log.d(TAG,"Parser apkoverlay-LGCbReceiver4:cb_config.xml");
        mCBChList = new ArrayList<String>();
        mCBChNameList = new ArrayList<String>();
        LgeCBConfigParser lgeCbConfigParser = new LgeCBConfigParser(mContext);
        lgeCbConfigParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_CB_CONFIG, mCBConfigMap, simInfo);
        mCBChList = lgeCbConfigParser.getChannelList();
        mCBChNameList = lgeCbConfigParser.getChannelNameList();
        Log.d(TAG,"mCBConfigMap=="+mCBConfigMap.toString());
        if(mCBChList != null){
            Log.d(TAG,"mCBChList=="+mCBChList.toString());
        }
        if(mCBChNameList != null){
            Log.d(TAG,"mCBChNameList=="+mCBChNameList.toString());
        }
    }
    private void parserTelephonyConfig(){
        mTeleServiceMap = new HashMap<String,String>();
        Log.d(TAG,"should Parser LGTelephonyServices_config.xml, but no it,i will parser telephony_config.xml");
        GeneralProfileParser LgeConfigParser = new LgeConfigParser(mContext);
        LgeConfigParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_TELEPHONY_CONFIG, mTeleServiceMap, simInfo);
        Log.d(TAG,"mTeleServiceMap=="+mTeleServiceMap.toString());
    }
    private void parserMessage(){
        mMessageMap = new HashMap<String,String>();
        //mms special:<siminfo operator="default" country="" mcc="" mnc="" />
        Log.d(TAG,"Parser apkoverlay-LGMessage4:mms_config.xml");
        GeneralProfileParser lgeMmsConfigParser = new LgeMmsConfigParser(mContext);
        lgeMmsConfigParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_MMS_CONFIG, mMessageMap, simInfo);
        Log.d(TAG,"mMessageMap=="+mMessageMap.toString());
    }

    private void parserBrowser(){
        mBrowserMap = new HashMap<String,String>();
        Log.d(TAG,"Parser apkoverlay-LGPartnerBookmarksProvider: browser_config.xml");
        mBookmarkList = new ArrayList<Bookmark>();
        LgeBrowserProfileParser lgeBrowserProfileParser =  new LgeBrowserProfileParser(mContext);
        lgeBrowserProfileParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_BROWSER_CONFIG, mBrowserMap, simInfo);
        mBookmarkList = lgeBrowserProfileParser.getBoomarkList();
        Log.d(TAG,"mBrowserMap=="+mBrowserMap.toString());
        Log.d(TAG,"mBookmarkList=="+mBookmarkList.size());
    }

    private void parserContacts(){
        mContactsMap = new HashMap<String,String>();
        Log.d(TAG,"Parser apkoverlay-Contacts3_JB:contacts_setting.xml");
        GeneralProfileParser lgeContactSettingParser =  new LgeContactSettingParser(mContext);
        lgeContactSettingParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_CONTACT_SETTINGS, mContactsMap, simInfo);
        Log.d(TAG,"mContactsMap=="+mContactsMap.toString());
    }

    private void parserLteReady(){
        mLteReadyMap = new HashMap<String,String>();
        Log.d(TAG,"Parser GPRI- lteready.xml");
        GeneralProfileParser lgeLTEConfigParser = new LgeLTEConfigParser(mContext);
        lgeLTEConfigParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_LTE_READY_CONFIG, mLteReadyMap, simInfo);
        Log.d(TAG,"mLteReadyMap=="+mLteReadyMap.toString());
    }
    private void parserSettings(){
        mSettingsProviderMap = new HashMap<String,String>();
        Log.d(TAG,"Parser apkoverlay-LGSettingsProvider:settings_provider_config.xml");
        GeneralProfileParser lgeSettingsProviderParser = new LgeSettingsProviderParser(mContext);
        lgeSettingsProviderParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_SETTINGS_PROVIDER_CONFIG, mSettingsProviderMap, simInfo);
        Log.d(TAG,"mSettingsProviderMap=="+mSettingsProviderMap.toString());
    }
    private void parserAmrWb(){
        Log.d(TAG,"Parser gpri:amrwb_gpri.xml");
        mAmrWBMap = new HashMap<String,String>();
        AmrwbSettingParser amrwbSettingParser =  new AmrwbSettingParser(mContext);
        amrwbSettingParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_AMRWB_CONFIG,mAmrWBMap,simInfo);
        Log.d(TAG,"mAmrWBMap=="+mAmrWBMap.toString());

        Intent ecidupdateIntent = new Intent(ACTION_ECID_UPDATE);
        mContext.sendBroadcast(ecidupdateIntent);
    }

    private void parserEmail(){
        Log.d(TAG,"Parser apkoverlay-LGEmail4:email_config.xml");
        HashMap<String,String> mConfig = new HashMap<String,String>();
        LgeEmailConfigParser lgeEmailConfigParser = new LgeEmailConfigParser(mContext);
        lgeEmailConfigParser.loadLgProfile(GeneralParserAttribute.FILE_PATH_EMAIL_CONFIG, mConfig, simInfo);
        mEmailAccountList = lgeEmailConfigParser.getEmailAccountList();
        mEmailSettingsProtocol = lgeEmailConfigParser.getEmailProtocol();
        mEmailSettingsInfo = lgeEmailConfigParser.getEmailSettings();

        if(mEmailAccountList !=null){
            Log.d(TAG,"result,mEmailAccountList=="+mEmailAccountList.toString());
        }
        if(mEmailSettingsProtocol != null){
            Log.d(TAG,"result,mEmailSettingsProtocol=="+mEmailSettingsProtocol.toString());
        }
        if(mEmailSettingsInfo !=null){
            Log.d(TAG,"result,mEmailSettingsInfo=="+mEmailSettingsInfo.toString());
        }
    }
    private boolean validMap(HashMap<String,String> map){
        if(map !=null && map.size()>0){
            return true;
        }
        return false;
    }
    private ArrayList<String> changeStringtoArray(String str){
        String[] list =  str.split(",");
        return (ArrayList<String>) Arrays.asList(list);

    }
    private final IBinder mService = new IECIDManager.Stub() {

        @Override
        public int getECIDPhoneId() {

            return 0;
        }

        @Override
        public boolean hasLteMode() {
            return false;
        }

        @Override
        public boolean hasLoadedParser() {
            Log.d(TAG, "hasLoadedParser");
            return hasLoaded();
        }

        @Override
        public boolean hasCBDefaultOn() {
            if(validMap(mCBConfigMap)){
                return mCBConfigMap.get(CBParserAttribute.KEY_ATTR_CB_DEFAULT_SWITCH);
            }
            Log.d(TAG, "hasCBDefaultOn");
            return false;
        }

        @Override
        public List<String> getCBChannelList() {
            Log.d(TAG, "getCBChannelList");
            if(mCBChList != null && mCBChList.size()>0){
                return mCBChList;
            }
            return null;
        }

        @Override
        public List<String> getCBChannelNameList() {
            Log.d(TAG, "getCBChannelNameList");
            if(mCBChNameList != null && mCBChNameList.size()>0){
                return mCBChNameList;
            }
            return null;
        }

        @Override
        public String getBrowserHomePager() {
            Log.d(TAG, "getBrowserHomePager");
            if(validMap(mBrowserMap)){
                return mBrowserMap.get(BrowserParserAttribute.KEY_ELEMENT_NAME_HOMEPAGE);
            }
            return null;
        }

        @Override
        public List<Bookmark> getBookmark() {
            Log.d(TAG, "getBookmark");
            if(mBookmarkList !=null && mBookmarkList.size()>0){
                return mBookmarkList;
            }
            return null;
        }

        @Override
        public boolean enableDataConsumptionWwarning() {
            Log.d(TAG, "enableDataConsumptionWwarning");
            return false;
        }

        @Override
        public String getDataWarningNotification() {
            Log.d(TAG, "getDataWarningNotification");
            return null;
        }

        @Override
        public boolean voiceMailEditable() {
            Log.d(TAG, "voiceMailEditable");
            return false;
        }

        @Override
        public boolean voiceMailRoamingEditable() {
            Log.d(TAG, "voiceMailRoamingEditable");
            return false;
        }

        @Override
        public boolean disableClirValue() {
            Log.d(TAG, "disableClirValue");
            return false;
        }

        @Override
        public EmailSettingsInfo getEmailSettingsInfo() {
            Log.d(TAG, "getEmailSettingsInfo");
            if(mEmailSettingsInfo !=null){
                return mEmailSettingsInfo;
            }
            return null;
        }

        @Override
        public EmailSettingsProtocol getEmailSettingsProtocl() {
            Log.d(TAG, "getEmailSettingsProtocl");
            if(mEmailSettingsProtocol !=null){
                return mEmailSettingsProtocol;
            }
            return null;
        }

        @Override
        public List<EmailServerProvider> getEmailServerProvider() {
            Log.d(TAG, "getEmailServerProvider");
            if(mEmailAccountList != null && mEmailAccountList.size()>0){
                return mEmailAccountList;
            }
            return null;
        }

        @Override
        public int getDefaultContactPath() {
            Log.d(TAG, "getDefaultContactPath");
            return -1;
        }

        @Override
        public LgeSimInfo getECIDSimInfo() {
            Log.d(TAG, "getECIDSimInfo");
            if(simInfo != null){
                return simInfo;
            }
            return null;
        }

        @Override
        public boolean enableAMRWBGSM() {
            Log.d(TAG, "enableAMRWBGSM");
            return false;
        }

        @Override
        public boolean enableAMRWBUMTS() {
            Log.d(TAG, "enableAMRWBUMTS");
            return false;
        }

        @Override
        public boolean getBooleanValue(String key,String module) {
            Log.d(TAG, "getBooleanValue");
            return false;
        }
        @Override
        public int getIntValue(String key,String module){
            Log.d(TAG, "getIntValue");
            if(module == null){
                return -1;
            }
            switch (module){
                case TelephonyParserAttribute.MODULE_NAME_TELEPHONY:{
                    if(validMap(mTelephonyMap)){
                        String value = mTelephonyMap.get(key);
                        if(!Utils.isEmpty(value)){
                            return Integer.valueOf(value);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            return -1;
        }
        @Override
        public String getStringValue(String key,String module){
            Log.d(TAG, "getStringValue");
            if(module == null){
                return null;
            }
            switch (module){
                case TelephonyParserAttribute.MODULE_NAME_TELEPHONY:{
                    if(validMap(mTelephonyMap)){
                        String value = mTelephonyMap.get(key);
                        if(!Utils.isEmpty(value)){
                            return value;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            return null;
        }
        
        @Override
        public List<String> getStringList(String key, String module) {
            if(module == null){
                return null;
            }
            switch (module){
                case TelephonyParserAttribute.MODULE_NAME_TELEPHONY:{
                    if(validMap(mTelephonyMap)){
                        String value = mTelephonyMap.get(key);
                        if(!Utils.isEmpty(value)){
                            return changeStringtoArray(value);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            return null;
        }
    };

}
