/*
 * Copyright (C) 2014 4jiokiSoft
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.yojiokisoft.reikaidenwaclient.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yojiokisoft.reikaidenwaclient.App;
import com.yojiokisoft.reikaidenwaclient.R;
import com.yojiokisoft.reikaidenwaclient.utils.MyConst;

/**
 * 設定情報のDAO
 */
public class SettingDao {
	private static SettingDao mInstance = null;
	private static SharedPreferences mSharedPref = null;
	private static Context mContext;
	private static String[] mInquiryKey;
	private static String[] mInquiryVal;

	/**
	 * コンストラクタは公開しない インスタンスを取得する場合は、getInstanceを使用する.
	 */
	private SettingDao() {
	}

	/**
	 * インスタンスの取得.
	 *
	 * @return SettingDao
	 */
	public static SettingDao getInstance() {
		if (mInstance == null) {
			mInstance = new SettingDao();
			mContext = App.getInstance().getAppContext();
			mSharedPref = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			mInquiryKey = mContext.getResources().getStringArray(
					R.array.inquiry_key);
			mInquiryVal = mContext.getResources().getStringArray(
					R.array.inquiry_val);
		}
		return mInstance;
	}

	/**
	 * 開閉音を値からキーに変換 例：true -> オン
	 *
	 * @param isChecked
	 *            ブール値
	 * @return 文字列
	 */
	public String booleanVal2Key(boolean isChecked) {
		String key;

		if (isChecked) {
			key = mContext.getString(R.string.sound_on);
		} else {
			key = mContext.getString(R.string.sound_off);
		}
		return key;
	}

	/**
	 * @return 開閉音のON/OFF状態
	 */
	public boolean getSoundBool() {
		return mSharedPref.getBoolean(MyConst.PK_SOUND, true);
	}

	/**
	 * @return 開閉音のON/OFF状態を文字列で
	 */
	public String getSound() {
		return booleanVal2Key(getSoundBool());
	}

	/**
	 * @return 新しいメッセージの取得のON/OFF状態
	 */
	public boolean getReqMessageBool() {
		return mSharedPref.getBoolean(MyConst.PK_REQ_MESSAGE, true);
	}

	/**
	 * @return 新しいメッセージの取得のON/OFF状態を文字列で
	 */
	public String getReqMessage() {
		return booleanVal2Key(getReqMessageBool());
	}

	/**
	 * @return メッセージの取得件数
	 */
	public int getReqCountInt() {
		return Integer.parseInt(getReqCount());
	}

	/**
	 * @return メッセージの取得件数
	 */
	public String getReqCount() {
		return mSharedPref.getString(MyConst.PK_REQ_COUNT, "10");
	}

	/**
	 * @return 端末のメッセージ件数
	 */
	public String getLocalMessageCount() {
		int value = MessageDao.getInstance().getCount();
		return String.valueOf(value);
	}

	/**
	 * お問い合わせを値からキーに変換 例：questions -> ご質問
	 *
	 * @param val
	 *            値
	 * @return キー値
	 */
	public String inquiryVal2Key(String val) {
		String key = null;

		for (int i = 0; i < mInquiryVal.length; i++) {
			if (mInquiryVal[i].equals(val)) {
				key = mInquiryKey[i];
				break;
			}
		}
		return key;
	}
}
