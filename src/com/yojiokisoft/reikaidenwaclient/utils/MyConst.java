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
package com.yojiokisoft.reikaidenwaclient.utils;

import com.yojiokisoft.reikaidenwaclient.App;

/**
 * 定数クラス.
 */
public class MyConst {
	/**
	 * 設定キー：開閉音
	 */
	public static final String PK_SOUND = "Sound";

	/**
	 * 設定キー：新しいメッセージの取得
	 */
	public static final String PK_REQ_MESSAGE = "ReqMessage";

	/**
	 * 設定キー：メッセージの取得件数
	 */
	public static final String PK_REQ_COUNT = "ReqCount";

	/**
	 * 設定キー：ローカルメッセージ件数
	 */
	public static final String PK_LOCAL_MESSAGE_COUNT = "LocalMessageCount";

	/**
	 * 設定キー：バージョン
	 */
	public static final String PK_VERSION = "Version";

	/**
	 * 設定キー：お問い合わせ
	 */
	public static final String PK_INQUIRY = "Inquiry";

	/**
	 * バグファイル名(キャッチした)
	 */
	public static final String BUG_CAUGHT_FILE = "bug_caught.txt";

	/**
	 * バグファイル名(キャッチされなかった)
	 */
	public static final String BUG_UNCAUGHT_FILE = "bug_uncaught.txt";

	/**
	 * キャッチしたバグファイルのフルパス
	 */
	public static String getCaughtBugFilePath() {
		return MyFile.pathCombine(App.getInstance().getAppDataPath(),
				BUG_CAUGHT_FILE);
	}

	/**
	 * キャッチされなかったバグファイルのフルパス
	 */
	public static String getUncaughtBugFilePath() {
		return MyFile.pathCombine(App.getInstance().getAppDataPath(),
				BUG_UNCAUGHT_FILE);
	}

	/**
	 * SQLiteのDB名
	 */
	public static final String DATABASE_FILE = "ReikaiDenwa.db";

	/**
	 * SQLiteのDB名のフルパス
	 */
	public static String getDatabasePath() {
		return App.getInstance().getDatabasePath(DATABASE_FILE)
				.getAbsolutePath();
	}
}
