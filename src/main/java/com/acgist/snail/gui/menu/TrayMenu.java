package com.acgist.snail.gui.menu;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.event.MouseInputAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.snail.gui.AbstractMenu;
import com.acgist.snail.gui.about.AboutWindow;
import com.acgist.snail.gui.main.MainWindow;
import com.acgist.snail.system.config.DownloadConfig;
import com.acgist.snail.system.config.SystemConfig;
import com.acgist.snail.utils.BrowseUtils;
import com.acgist.snail.utils.PlatformUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * 菜单 - 托盘
 */
public class TrayMenu extends AbstractMenu {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);

	private static final int MENU_WINDOW_HEIGHT = 150; // 窗口高度
	
	private Stage trayStage;
	private TrayIcon trayIcon;
	
	private static TrayMenu INSTANCE;
	
	private TrayMenu() {
		init();
		buildMenu();
		enableTray();
	}

	static {
		synchronized (TaskMenu.class) {
			if (INSTANCE == null) {
				LOGGER.info("初始化托盘菜单");
				INSTANCE = new TrayMenu();
				Platform.setImplicitExit(false); // 必须设置此项，否者窗口关闭后将不能通过托盘显示
			}
		}
	}
	
	public static final TrayMenu getInstance() {
		return INSTANCE;
	}

	private MenuItem showMenu;
	private MenuItem hideMenu;
	private MenuItem exitMenu;
	private MenuItem aboutMenu;
	private MenuItem sourceMenu;
	private MenuItem supportMenu;

	/**
	 * 创建菜单
	 */
	protected void buildMenu() {
		this.showMenu = new MenuItem("显示", new ImageView("/image/16/show.png"));
		this.hideMenu = new MenuItem("隐藏", new ImageView("/image/16/hide.png"));
		this.sourceMenu = new MenuItem("官网与源码", new ImageView("/image/16/source.png"));
		this.supportMenu = new MenuItem("问题与建议", new ImageView("/image/16/support.png"));
		this.aboutMenu = new MenuItem("关于", new ImageView("/image/16/about.png"));
		this.exitMenu = new MenuItem("退出", new ImageView("/image/16/exit.png"));
		
		showMenu.setOnAction(showAction);
		hideMenu.setOnAction(hideAction);
		exitMenu.setOnAction(exitAction);
		aboutMenu.setOnAction(aboutAction);
		sourceMenu.setOnAction(sourceAction);
		supportMenu.setOnAction(supportAction);
		
		this.addEventFilter(WindowEvent.WINDOW_HIDDEN, windowHiddenAction);
		
		addMenu(showMenu);
		addMenu(hideMenu);
		addMenu(sourceMenu);
		addMenu(supportMenu);
		addMenu(aboutMenu);
		this.addSeparator();
		addMenu(exitMenu);
	}
	
	/**
	 * 系统托盘
	 */
	private void enableTray() {
		MouseListener mouseListener = new MouseInputAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent event) {
				if (event.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					if (MainWindow.getInstance().isShowing()) {
						Platform.runLater(() -> {
							MainWindow.getInstance().hide();
						});
					} else {
						Platform.runLater(() -> {
							MainWindow.getInstance().show();
						});
					}
				} else if(event.getButton() == java.awt.event.MouseEvent.BUTTON3) {
					Platform.runLater(() -> {
						int x = event.getXOnScreen();
						int y = event.getYOnScreen() - MENU_WINDOW_HEIGHT;
						TrayMenu.INSTANCE.show(createTrayStage(), x, y);
					});
				}
			}
		};
		try {
			BufferedImage image = ImageIO.read(MainWindow.class.getResourceAsStream("/image/16/logo.png"));
			trayIcon = new TrayIcon(image, SystemConfig.getName());
			trayIcon.addMouseListener(mouseListener);
			SystemTray.getSystemTray().add(trayIcon);
		} catch (Exception e) {
			LOGGER.error("添加托盘异常", e);
		}
	}
	
	/**
	 * 提示信息
	 */
	public void notice(String title, String content) {
		notice(title, content, MessageType.INFO);
	}

	/**
	 * 提示信息
	 */
	public void notice(String title, String content, MessageType type) {
		if(DownloadConfig.getNotice()) {
			trayIcon.displayMessage(title, content, type);
		}
	}
	
	
	/**
	 * 关闭托盘
	 */
	public static final void exit() {
		TrayIcon trayIcon = TrayMenu.getInstance().trayIcon;
		SystemTray.getSystemTray().remove(trayIcon);
	}
	
	/**
	 * 托盘菜单
	 */
	private Stage createTrayStage() {
		FlowPane trayPane = new FlowPane();
		trayPane.setBackground(Background.EMPTY);
		Scene trayScene = new Scene(trayPane);
		trayScene.setFill(Color.TRANSPARENT);
		Stage trayStage = new Stage();
		trayStage.initStyle(StageStyle.UTILITY);
		trayStage.setOpacity(0);
		trayStage.setMaxWidth(0);
		trayStage.setMaxHeight(0);
		trayStage.setAlwaysOnTop(true);
		trayStage.setScene(trayScene);
		trayStage.show();
		this.trayStage = trayStage;
		return trayStage;
	}
	
	private EventHandler<ActionEvent> showAction = (event) -> {
		Platform.runLater(() -> {
			MainWindow.getInstance().show();
		});
	};
	
	private EventHandler<ActionEvent> hideAction = (event) -> {
		Platform.runLater(() -> {
			MainWindow.getInstance().hide();
		});
	};
	
	private EventHandler<ActionEvent> exitAction = (event) -> {
		PlatformUtils.exit();
	};
	
	private EventHandler<ActionEvent> aboutAction = (event) -> {
		AboutWindow.getInstance().show();
	};
	
	private EventHandler<ActionEvent> sourceAction = (event) -> {
		BrowseUtils.open(SystemConfig.getSource());
	};
	
	private EventHandler<ActionEvent> supportAction = (event) -> {
		BrowseUtils.open(SystemConfig.getSupport());
	};
	
	// 窗口隐藏时移除托盘显示的Stage
	private EventHandler<WindowEvent> windowHiddenAction = (event) -> {
		Platform.runLater(() -> {
			if(trayStage != null) {
				trayStage.close();
			}
		});
	};
	
}