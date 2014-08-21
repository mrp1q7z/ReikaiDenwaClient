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

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.reikaidenwaclient.entity.MessageEntity;
import com.yojiokisoft.reikaidenwaclient.utils.MyRandom;

/**
 * メッセージのDAO
 */
public class MessageDao {
	private static MessageDao mInstance = null;
	private static Dao<MessageEntity, Integer> mMessageDao = null;

	/**
	 * インスタンスの取得.
	 *
	 * @return DatabaseHelper
	 */
	public static MessageDao getInstance() {
		if (mInstance == null) {
			mInstance = new MessageDao();
			try {
				mMessageDao = DatabaseHelper.getInstance().getDao(
						MessageEntity.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mInstance;
	}

	public MessageEntity queryForSerialNo(int serialNo) {
		try {
			List<MessageEntity> list = mMessageDao.queryForEq(
					MessageEntity.SERIAL_NO, serialNo);
			if (list == null || list.size() != 1) {
				throw new SQLException("Message is a lot : serialNo="
						+ serialNo);
			}
			return list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getCount() {
		try {
			return (int) mMessageDao.countOf();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public MessageEntity getRandomMessage() {
		int cnt = getCount();
		int no = MyRandom.random(1, cnt);
		return queryForSerialNo(no);
	}

	public int create(MessageEntity message) {
		int ret = -1;
		try {
			ret = mMessageDao.create(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
