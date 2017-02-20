package com.zlzkj.app.service;

import java.util.Map;

import com.zlzkj.app.model.Backup;

public interface BackupService {
	/**
	 * 添加备份
	 * @param backup
	 * @return
	 */
	public int add(Backup backup);
	/**
	 * 获取备份列表信息
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getBackupList(int page, int rows);
	/**
	 * 删除备份
	 * @param id
	 */
	public void del(int id);
}
