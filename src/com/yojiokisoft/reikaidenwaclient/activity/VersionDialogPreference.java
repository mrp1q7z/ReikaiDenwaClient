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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yojiokisoft.reikaidenwaclient.App;
import com.yojiokisoft.reikaidenwaclient.R;
import com.yojiokisoft.reikaidenwaclient.utils.MyResource;

/**
 * 設定画面のバージョンダイアログ
 */
public class VersionDialogPreference extends DialogPreference {
	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 * @param attrs
	 */
	public VersionDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * カスタムコンテンツビューの作成.
	 */
	@Override
	protected View onCreateDialogView() {
		PackageInfo packageInfo = MyResource.getPackageInfo();
		String version = App.getInstance().getString(R.string.app_name)
				+ " Version " + packageInfo.versionName;

		TextView textView1 = new TextView(this.getContext());
		textView1.setText(version);
		textView1.setPadding(10, 10, 10, 10);
		textView1.setTextColor(this.getContext().getResources()
				.getColor(R.color.theme_dialogTextColor));

		TextView textView2 = new TextView(this.getContext());
		textView2.setText("このアプリは、かつて地上で生活をしたことのある実在の人物からのメッセージを伝える電話です。");
		textView2.setPadding(10, 10, 10, 10);
		textView2.setTextColor(this.getContext().getResources()
				.getColor(R.color.theme_dialogTextColor));

		LinearLayout parent = new LinearLayout(this.getContext());
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.addView(textView1);
		parent.addView(textView2);

		return parent;
	}
}
