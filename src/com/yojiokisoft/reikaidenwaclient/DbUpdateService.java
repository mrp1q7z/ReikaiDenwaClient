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

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.yojiokisoft.reikaidenwa.entity.messageendpoint.Messageendpoint;
import com.yojiokisoft.reikaidenwa.entity.messageendpoint.model.CollectionResponseMessage;
import com.yojiokisoft.reikaidenwa.entity.messageendpoint.model.Message;
import com.yojiokisoft.reikaidenwaclient.dao.MessageDao;
import com.yojiokisoft.reikaidenwaclient.dao.SettingDao;
import com.yojiokisoft.reikaidenwaclient.entity.MessageEntity;
import com.yojiokisoft.reikaidenwaclient.utils.MyLog;
import com.yojiokisoft.reikaidenwaclient.utils.MyWait;

/**
 * DB更新サービス
 */
public class DbUpdateService extends Service {
	@Override
	public void onCreate() {
		// MyLog.d("onCreate");
		// Toast.makeText(this, "MyService#onCreate",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// MyLog.d("onStartCommand Received start id " + startId + ": " +
		// intent);
		// Toast.makeText(this, "MyService#onStartCommand", Toast.LENGTH_SHORT)
		// .show();

		int startSerialNo = intent.getIntExtra("startSerialNo", 0);
		new MessagesListAsyncTask(this, startSerialNo).execute();

		// 明示的にサービスの起動、停止が決められる場合の返り値
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// MyLog.d("onDestroy");
		// Toast.makeText(this, "MyService#onDestroy",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class MessagesListAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseMessage> {
		Context mContext;
		int mStartSerialNo;

		public MessagesListAsyncTask(Context context, int startSerialNo) {
			this.mContext = context;
			this.mStartSerialNo = startSerialNo;
		}

		protected CollectionResponseMessage doInBackground(Void... unused) {
			MyWait w = new MyWait();
			w.sleep(5000);

			CollectionResponseMessage messages = null;
			try {
				Messageendpoint.Builder builder = new Messageendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Messageendpoint service = builder.build();
				messages = service.listMessage2()
						.setStartSerialNo(mStartSerialNo)
						.setLimit(SettingDao.getInstance().getReqCountInt())
						.execute();
			} catch (Exception e) {
				MyLog.d("Could not retrieve Messages", e.getMessage(), e);
			}
			return messages;
		}

		protected void onPostExecute(CollectionResponseMessage messages) {
			if (messages == null) {
				return;
			}
			List<Message> list = messages.getItems();
			if (list == null) {
				// Toast.makeText(mContext, "新しいメッセージはありません",
				// Toast.LENGTH_SHORT)
				// .show();
				((DbUpdateService) mContext).stopSelf();
				return;
			}

			for (Message message : list) {
				MessageEntity m = new MessageEntity();
				m.id = message.getId();
				m.serialNo = message.getSerialNo();
				m.message = message.getMessage();
				m.author = message.getAuthor();

				MessageDao.getInstance().create(m);
			}
			Toast.makeText(mContext, "霊界電話: メッセージを" + list.size() + "件、追加しました",
					Toast.LENGTH_SHORT).show();
			((DbUpdateService) mContext).stopSelf();
		}
	}
}
