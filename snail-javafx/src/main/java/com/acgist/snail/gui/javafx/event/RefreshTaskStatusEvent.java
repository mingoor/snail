package com.acgist.snail.gui.javafx.event;

import com.acgist.snail.gui.event.GuiEvent;
import com.acgist.snail.gui.event.adapter.RefreshTaskStatusEventAdapter;
import com.acgist.snail.gui.javafx.main.TaskDisplay;

/**
 * <p>GUI刷新任务状态事件</p>
 * 
 * @author acgist
 * @since 1.1.0
 */
public final class RefreshTaskStatusEvent extends RefreshTaskStatusEventAdapter {

	private static final RefreshTaskStatusEvent INSTANCE = new RefreshTaskStatusEvent();
	
	public static final GuiEvent getInstance() {
		return INSTANCE;
	}
	
	private RefreshTaskStatusEvent() {
	}
	
	@Override
	protected void executeNative(Object ... args) {
		TaskDisplay.getInstance().refreshTaskStatus();
	}

}
