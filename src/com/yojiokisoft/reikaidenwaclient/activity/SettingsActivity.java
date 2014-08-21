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
package com.yojiokisoft.reikaidenwaclient.activity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.yojiokisoft.reikaidenwaclient.R;
import com.yojiokisoft.reikaidenwaclient.dao.SettingDao;
import com.yojiokisoft.reikaidenwaclient.utils.MyConst;
import com.yojiokisoft.reikaidenwaclient.utils.MyMail;
import com.yojiokisoft.reikaidenwaclient.utils.MyResource;

/**
 * 設定アクティビティ
 */
public class SettingsActivity extends PreferenceActivity {
	private final static String BR = System.getProperty("line.separator");
	private SettingDao mSettingDao;

	/**
	 * アクティビティの初期化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSettingDao = SettingDao.getInstance();
		addPreferencesFromResource(R.xml.settings);

		setSummary();

		// お問い合わせの設定値をクリア
		// 既に選択中のものを選択してもイベントが発生しないから
		clearPreference(MyConst.PK_INQUIRY);

		// 設定値が変更された時のイベントリスナーを登録
		CheckBoxPreference prefSound = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(MyConst.PK_SOUND);
		prefSound.setOnPreferenceChangeListener(mSoundChanged);

		CheckBoxPreference prefReqMessage = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(MyConst.PK_REQ_MESSAGE);
		prefReqMessage.setOnPreferenceChangeListener(mReqMessageChanged);

		ListPreference prefReqCount = (ListPreference) getPreferenceScreen()
				.findPreference(MyConst.PK_REQ_COUNT);
		prefReqCount.setOnPreferenceChangeListener(mReqCountChanged);

		ListPreference prefInquiry = (ListPreference) getPreferenceScreen()
				.findPreference(MyConst.PK_INQUIRY);
		prefInquiry.setOnPreferenceChangeListener(mInquiryChanged);
	}

	/**
	 * 設定値のクリア.
	 *
	 * @param key
	 *            キー
	 */
	private void clearPreference(String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 文字列をBRまででカットする.
	 *
	 * @param str
	 *            対象の文字列
	 * @return BRまでの文字列
	 */
	private String indexOfBr(String str) {
		int find = str.indexOf(BR);
		if (find == -1) {
			return str;
		}
		return str.substring(0, find);
	}

	/**
	 * サマリーに現在の設定値をセットする.
	 */
	private void setSummary() {
		String[] prefKeys = { MyConst.PK_SOUND, MyConst.PK_REQ_MESSAGE,
				MyConst.PK_REQ_COUNT, MyConst.PK_LOCAL_MESSAGE_COUNT,
				MyConst.PK_VERSION, MyConst.PK_INQUIRY };
		Preference pref;
		String summary;
		String nowVal;
		for (int i = 0; i < prefKeys.length; i++) {
			pref = getPreferenceScreen().findPreference(prefKeys[i]);
			summary = indexOfBr(pref.getSummary().toString());
			nowVal = getNowValue(prefKeys[i]);
			if (nowVal != null) {
				summary += BR + getString(R.string.now_setting) + nowVal;
			}
			pref.setSummary(getSummarySpannableString(summary));
		}
	}

	/**
	 * 現在値の取得.
	 *
	 * @param key
	 *            キー
	 * @return 現在値
	 */
	private String getNowValue(String key) {
		if (MyConst.PK_SOUND.equals(key)) {
			return mSettingDao.getSound();
		} else if (MyConst.PK_REQ_MESSAGE.equals(key)) {
			return mSettingDao.getReqMessage();
		} else if (MyConst.PK_REQ_COUNT.equals(key)) {
			return mSettingDao.getReqCount();
		} else if (MyConst.PK_LOCAL_MESSAGE_COUNT.equals(key)) {
			return mSettingDao.getLocalMessageCount();
		} else if (MyConst.PK_VERSION.equals(key)) {
			PackageInfo packageInfo = MyResource.getPackageInfo();
			return "Version " + packageInfo.versionName;
		}
		return null;
	}

	/**
	 * 文字列をサマリー用のマークアップ可能な文字列に変換
	 *
	 * @param summary
	 *            文字列
	 * @return サマリー用の文字列
	 */
	private SpannableString getSummarySpannableString(String summary) {
		SpannableString span;
		span = new SpannableString(summary);
		span.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.theme_settingColor)), 0, span.length(), 0);
		return span;
	}

	/**
	 * 開閉時の音が変更された.
	 */
	private final OnPreferenceChangeListener mSoundChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			CheckBoxPreference pref = (CheckBoxPreference) preference;
			String summary = indexOfBr(pref.getSummary().toString());
			summary += BR + getString(R.string.now_setting)
					+ mSettingDao.booleanVal2Key((Boolean) newValue);
			pref.setSummary(getSummarySpannableString(summary));
			return true;
		}
	};

	/**
	 * 新しいメッセージの取得が変更された.
	 */
	private final OnPreferenceChangeListener mReqMessageChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			CheckBoxPreference pref = (CheckBoxPreference) preference;
			String summary = indexOfBr(pref.getSummary().toString());
			summary += BR + getString(R.string.now_setting)
					+ mSettingDao.booleanVal2Key((Boolean) newValue);
			pref.setSummary(getSummarySpannableString(summary));
			return true;
		}
	};

	/**
	 * メッセージの取得件数が変更された.
	 */
	private final OnPreferenceChangeListener mReqCountChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			ListPreference pref = (ListPreference) preference;
			String summary = indexOfBr(pref.getSummary().toString());
			summary += BR + getString(R.string.now_setting)
					+ newValue.toString();
			pref.setSummary(getSummarySpannableString(summary));
			return true;
		}
	};

	/**
	 * 問い合わせが変更された.
	 */
	private final OnPreferenceChangeListener mInquiryChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String inquiry = (String) newValue;
			if (!"".equals(inquiry)) {
				String inquiryKey = mSettingDao.inquiryVal2Key(inquiry);
				String subject = "[" + inquiryKey + "]"
						+ getString(R.string.app_name);
				// メール送信
				MyMail.Builder.newInstance(getApplicationContext())
						.setTo(getString(R.string.developer_email))
						.setSubject(subject).send();
			}

			return false; // データの変更はしない
		}
	};
}
