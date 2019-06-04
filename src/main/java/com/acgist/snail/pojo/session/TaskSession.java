package com.acgist.snail.pojo.session;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.acgist.snail.downloader.IDownloader;
import com.acgist.snail.downloader.ftp.FtpDownloader;
import com.acgist.snail.downloader.http.HttpDownloader;
import com.acgist.snail.downloader.torrent.TorrentDownloader;
import com.acgist.snail.gui.main.TaskDisplay;
import com.acgist.snail.pojo.entity.TaskEntity;
import com.acgist.snail.pojo.entity.TaskEntity.Status;
import com.acgist.snail.pojo.entity.TaskEntity.Type;
import com.acgist.snail.pojo.wrapper.TorrentFileSelectWrapper;
import com.acgist.snail.repository.impl.TaskRepository;
import com.acgist.snail.system.exception.DownloadException;
import com.acgist.snail.system.manager.DownloaderManager;
import com.acgist.snail.system.statistics.SystemStatistics;
import com.acgist.snail.utils.DateUtils;
import com.acgist.snail.utils.FileUtils;
import com.acgist.snail.utils.StringUtils;

/**
 * Task Session
 * 
 * @author acgist
 * @since 1.0.0
 */
public class TaskSession {

	private ThreadLocal<SimpleDateFormat> formater = new ThreadLocal<>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		};
	};
	
	private TaskEntity entity; // 任务
	private IDownloader downloader; // 下载器
	private StatisticsSession statistics; // 统计
	
	private TaskSession(TaskEntity entity) throws DownloadException {
		if(entity == null) {
			throw new DownloadException("创建下载任务失败");
		}
		this.entity = entity;
		this.statistics = new StatisticsSession(true, SystemStatistics.getInstance().getSystemStatistics());
	}
	
	// 功能 //
	public static final TaskSession newInstance(TaskEntity entity) throws DownloadException {
		return new TaskSession(entity);
	}
	
	public TaskEntity entity() {
		return entity;
	}
	
	public IDownloader downloader() {
		return this.downloader;
	}
	
	public void downloader(IDownloader downloader) {
		this.downloader = downloader;
	}
	
	/**
	 * 获取下载目录
	 */
	public File downloadFolder() {
		File file = new File(entity.getFile());
		if(entity.getType() == Type.torrent) {
			return file;
		} else {
			return file.getParentFile();
		}
	}
	
	/**
	 * 获取已选择的下载文件
	 */
	public List<String> downloadTorrentFiles() {
		if(entity.getType() != Type.torrent) {
			return List.of();
		}
		String description = entity.getDescription();
		if(StringUtils.isEmpty(description)) {
			return List.of();
		} else {
			final TorrentFileSelectWrapper wrapper = TorrentFileSelectWrapper.newDecoder(description);
			return wrapper.list();
		}
	}

	/**
	 * 更新状态，刷新下载
	 */
	public void updateStatus(Status status) {
		if(complete()) {
			return;
		}
		TaskRepository repository = new TaskRepository();
		if(status == Status.complete) {
			this.entity.setEndDate(new Date()); // 设置完成时间
		}
		this.entity.setStatus(status);
		repository.update(this.entity);
		DownloaderManager.getInstance().refresh(); // 刷新下载
		TaskDisplay.getInstance().refreshTaskData(); // 刷新状态
	}
	
	public StatisticsSession statistics() {
		return this.statistics;
	}
	
	/**
	 * 已下载大小
	 */
	public long downloadSize() {
		return this.statistics.downloadSize();
	}
	
	/**
	 * 设置已下载大小
	 */
	public void downloadSize(long size) {
		this.statistics.downloadSize(size);
	}

	/**
	 * 等待状态
	 */
	public boolean await() {
		return entity.getStatus() == Status.await;
	}
	
	/**
	 * 下载状态
	 */
	public boolean download() {
		return entity.getStatus() == Status.download;
	}
	
	/**
	 * 完成状态
	 */
	public boolean complete() {
		return entity.getStatus() == Status.complete;
	}
	
	/**
	 * 任务执行状态：等待中或者下载中
	 */
	public boolean coming() {
		return await() || download();
	}
	
	/**
	 * 获取下载任务
	 */
	public IDownloader newDownloader() throws DownloadException {
		if(this.downloader != null) {
			return this.downloader;
		}
		var type = this.entity().getType();
		switch (type) {
			case ftp:
				return FtpDownloader.newInstance(this);
			case http:
				return HttpDownloader.newInstance(this);
			case torrent:
				return TorrentDownloader.newInstance(this);
			default:
				throw new DownloadException("不支持的下载类型：" + type);
		}
	}
	
	// Table数据绑定 //
	
	/**
	 * 任务名称
	 */
	public String getNameValue() {
		return entity.getName();
	}

	/**
	 * 任务状态
	 */
	public String getStatusValue() {
		if(download()) {
			return FileUtils.formatSize(statistics.downloadSecond()) + "/S";
		} else {
			return entity.getStatus().getValue();
		}
	}
	
	/**
	 * 任务进度
	 */
	public String getProgressValue() {
		if(complete()) {
			return FileUtils.formatSize(entity.getSize());
		} else {
			return FileUtils.formatSize(statistics.downloadSize()) + "/" + FileUtils.formatSize(entity.getSize());
		}
	}

	/**
	 * 创建时间
	 */
	public String getCreateDateValue() {
		if(entity.getCreateDate() == null) {
			return "-";
		}
		return formater.get().format(entity.getCreateDate());
	}
	
	/**
	 * 完成时间
	 */
	public String getEndDateValue() {
		if(entity.getEndDate() == null) {
			if(download()) {
				final long downloadSecond = statistics.downloadSecond();
				if(downloadSecond == 0L) {
					return "-";
				} else {
					long second = (entity.getSize() - statistics.downloadSize()) / downloadSecond;
					return DateUtils.secondToString(second);
				}
			} else {
				return "-";
			}
		}
		return formater.get().format(entity.getEndDate());
	}
	
}
