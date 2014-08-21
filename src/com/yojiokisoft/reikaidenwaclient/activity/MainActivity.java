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

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.yojiokisoft.reikaidenwaclient.App;
import com.yojiokisoft.reikaidenwaclient.DbUpdateService;
import com.yojiokisoft.reikaidenwaclient.R;
import com.yojiokisoft.reikaidenwaclient.dao.MessageDao;
import com.yojiokisoft.reikaidenwaclient.dao.SettingDao;
import com.yojiokisoft.reikaidenwaclient.entity.MessageEntity;
import com.yojiokisoft.reikaidenwaclient.utils.AdCatalogUtils;
import com.yojiokisoft.reikaidenwaclient.utils.MyLog;

/**
 * メインアクティビティ
 */
public class MainActivity extends ActionBarActivity implements AdListener {
	private Context mContext;
	private AdView adViewBanner;
	private RelativeLayout mExitContainer;
	private ImageView mPhoneImage;
	private boolean mOpenFlag;
	private SoundPool mSound;
	private int[] mSoundId;
	private TextView mMessageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		mOpenFlag = false;
		mPhoneImage = (ImageView) findViewById(R.id.phone_image);
		mPhoneImage.setOnClickListener(mPhoneImageClicked);

		mMessageText = (TextView) findViewById(R.id.message_text);
		mExitContainer = (RelativeLayout) findViewById(R.id.exit_container);

		mSoundId = new int[2];
		mSound = new SoundPool(mSoundId.length, AudioManager.STREAM_MUSIC, 0);
		mSoundId[0] = mSound.load(getApplicationContext(), R.raw.open, 0);
		mSoundId[1] = mSound.load(getApplicationContext(), R.raw.close, 0);

		Intent intent = new Intent(this, DbUpdateService.class);
		intent.putExtra("startSerialNo",
				MessageDao.getInstance().getCount() + 1);
		if (SettingDao.getInstance().getReqMessageBool()) {
			startService(intent);
		}

		AdRequest adRequest = AdCatalogUtils.createAdRequest();
		adViewBanner = (AdView) findViewById(R.id.adViewBanner);
		adViewBanner.setAdListener(this);
		adViewBanner.loadAd(adRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(App.getInstance().getAppContext(),
					SettingsActivity.class);
			startActivity(intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}

		mExitContainer.setVisibility(View.VISIBLE);

		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				finish();
			}
		}, 2000);

		return false;
	}

	@Override
	public void onDestroy() {
		if (adViewBanner != null) {
			adViewBanner.destroy();
		}
		mSound.release();
		super.onDestroy();
	}

	private View.OnClickListener mPhoneImageClicked = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int soundId;
			int resId;
			Animation anim;

			if (mOpenFlag) {
				soundId = mSoundId[1];
				resId = R.drawable.skin01_close;
				anim = AnimationUtils
						.loadAnimation(mContext, R.anim.scale_down);
			} else {
				soundId = mSoundId[0];
				resId = R.drawable.skin01_open;
				anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
				anim.setAnimationListener(mAnimScaleUp);
			}

			mOpenFlag = !mOpenFlag;
			mMessageText.setVisibility(View.INVISIBLE);
			if (SettingDao.getInstance().getSoundBool()) {
				mSound.play(soundId, 1.0F, 1.0F, 0, 0, 1.0F);
			}
			mPhoneImage.setImageResource(resId);
			mPhoneImage.startAnimation(anim);
		}
	};

	private AnimationListener mAnimScaleUp = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			MessageEntity message = MessageDao.getInstance().getRandomMessage();
			if (message == null) {
				MyLog.d("message is null !!!");
				return;
			}
			mMessageText.setVisibility(View.VISIBLE);
			mMessageText.setText(message.message);
		}
	};

	@Override
	public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
		Log.d("Banners_class", "I failed to receive an ad");
	}

	@Override
	public void onPresentScreen(Ad ad) {
		Log.d("Banners_class", "Presenting screen");
	}

	@Override
	public void onDismissScreen(Ad ad) {
		Log.d("Banners_class", "Dismissing screen");
	}

	@Override
	public void onLeaveApplication(Ad ad) {
		Log.d("Banners_class", "Leaving application");
	}

	@Override
	public void onReceiveAd(Ad ad) {
		Log.d("Banners_class", "onReceiveAd");
	}
}
