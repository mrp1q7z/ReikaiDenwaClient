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

import java.util.Random;

public class MyRandom {
	private static Random mRandom = new Random();

	/**
	 * 整数の乱数を発生させる
	 * 
	 * @param start
	 *            開始（この値を含む）
	 * @param end
	 *            終了（この値を含む）
	 * @return 乱数
	 */
	static public final int random(int start, int end) {
		return start + mRandom.nextInt(end - start + 1);
	}
}
