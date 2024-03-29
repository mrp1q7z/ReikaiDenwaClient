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
package com.yojiokisoft.reikaidenwaclient;

import android.app.Application;
import android.content.Context;

import com.yojiokisoft.reikaidenwaclient.dao.DatabaseHelper;

/**
 * アプリケーションクラス
 */
public class App extends Application {
	// 唯一のアプリケーションインスタンス
	private static App sInstance;

	// アプリケーションコンテキスト
	private static Context sAppContext;

	/**
	 * コンストラクタ
	 */
	public App() {
		sInstance = this;
	}

	/**
	 * @return 唯一のアプリケーションインスタンス
	 */
	public static App getInstance() {
		if (sAppContext == null) {
			sAppContext = sInstance.getApplicationContext();
		}
		return sInstance;
	}

	/**
	 * @return アプリケーションコンテキスト
	 */
	public Context getAppContext() {
		return sAppContext;
	}

	/**
	 * @return アプリケーションデータの保存領域のパス
	 */
	public String getAppDataPath() {
		return sAppContext.getFilesDir().toString();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		DatabaseHelper.init();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		DatabaseHelper.destroy();
	}
}
