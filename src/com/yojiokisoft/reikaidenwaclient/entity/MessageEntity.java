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
package com.yojiokisoft.reikaidenwaclient.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * メッセージのエンティティ
 */
@DatabaseTable(tableName = "Message")
public class MessageEntity {
	/**
	 * ID
	 */
	public final static String ID = "id";
	@DatabaseField(columnName = ID, canBeNull = false)
	public Long id;

	/**
	 * 連番
	 */
	public final static String SERIAL_NO = "serial_no";
	@DatabaseField(columnName = SERIAL_NO, canBeNull = false)
	public Integer serialNo;

	/**
	 * メッセージ
	 */
	public final static String MESSAGE = "message";
	@DatabaseField(columnName = MESSAGE, canBeNull = false)
	public String message;

	/**
	 * 作者
	 */
	public final static String AUTHOR = "author";
	@DatabaseField(columnName = AUTHOR)
	public String author;
}
