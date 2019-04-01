package com.acgist.snail.net.upnp;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.snail.net.http.HTTPClient;
import com.acgist.snail.utils.XMLUtils;

/**
 * UPNP操作
 */
public class UpnpService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpnpService.class);
	
	/**
	 * 控制类型
	 */
	private static final String SERVICE_TYPE = "urn:schemas-upnp-org:service:WANIPConnection:1";
	
	/**
	 * 协议
	 */
	public enum Protocol {
		TCP,
		UDP;
	}
	
	private String location; // 描述文件地址
	private String controlURL; // 控制URL
	private String serviceType; // 服务类型

	private static final UpnpService INSTANCE = new UpnpService();
	
	private UpnpService() {
	}

	public static final UpnpService getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 加载信息
	 */
	public void load(String location) {
		this.location = location;
		var client = HTTPClient.newClient();
		var request = HTTPClient.newRequest(this.location)
			.GET()
			.build();
		var response = HTTPClient.request(client, request, BodyHandlers.ofString());
		final String body = response.body();
		final XMLUtils xml = XMLUtils.load(body);
		final List<String> serviceTypes = xml.elementValues("serviceType");
		final List<String> controlURLs = xml.elementValues("controlURL");
		if(controlURLs == null) {
			LOGGER.warn("加载UPNP信息失败");
			return;
		}
		for (int index = 0; index < serviceTypes.size(); index++) {
			String serviceType = serviceTypes.get(index);
			if(SERVICE_TYPE.equals(serviceType)) {
				this.serviceType = serviceType;
				this.controlURL = controlURLs.get(index);
				this.initControlURL();
				LOGGER.info("服务类型：{}", this.serviceType);
				LOGGER.info("控制地址：{}", this.controlURL);
				break;
			}
		}
	}

	/**
	 * 获取外网IP：GetExternalIPAddress
	 * 请求头：SOAPAction:"urn:schemas-upnp-org:service:WANIPConnection:1#GetExternalIPAddress"
	 */
	public String getExternalIPAddress() {
		UpnpRequest upnpRequest = UpnpRequest.newRequest(serviceType);
		String xml = upnpRequest.buildGetExternalIPAddress();
		var client = HTTPClient.newClient();
		var request = HTTPClient.newRequest(controlURL)
			.header("SOAPAction", "\"" + serviceType + "#GetExternalIPAddress\"")
			.POST(BodyPublishers.ofString(xml))
			.build();
		var response = HTTPClient.request(client, request, BodyHandlers.ofString());
		String body = response.body();
		return UpnpResponse.parseGetExternalIPAddress(body);
	}

	/**
	 * 获取端口映射情况：GetSpecificPortMappingEntry
	 * 请求头：SOAPAction:"urn:schemas-upnp-org:service:WANIPConnection:1#GetSpecificPortMappingEntry"
	 * 如果没有映射：返回500错误代码
	 */
	public boolean getSpecificPortMappingEntry(int port, Protocol protocol) {
		UpnpRequest upnpRequest = UpnpRequest.newRequest(serviceType);
		String xml = upnpRequest.buildGetSpecificPortMappingEntry(port, protocol);
		var client = HTTPClient.newClient();
		var request = HTTPClient.newRequest(controlURL)
			.header("SOAPAction", "\"" + serviceType + "#GetSpecificPortMappingEntry\"")
			.POST(BodyPublishers.ofString(xml))
			.build();
		var response = HTTPClient.request(client, request, BodyHandlers.ofString());
		return response.statusCode() == 500; // 没被使用
	}
	
	/**
	 * 添加端口映射：AddPortMapping
	 * 请求头：SOAPAction:"urn:schemas-upnp-org:service:WANIPConnection:1#AddPortMapping"
	 */
	public boolean addPortMapping(int port, String address, Protocol protocol) {
		UpnpRequest upnpRequest = UpnpRequest.newRequest(serviceType);
		String xml = upnpRequest.buildAddPortMapping(port, address, protocol);
		var client = HTTPClient.newClient();
		var request = HTTPClient.newRequest(controlURL)
			.header("SOAPAction", "\"" + serviceType + "#AddPortMapping\"")
			.POST(BodyPublishers.ofString(xml))
			.build();
		var response = HTTPClient.request(client, request, BodyHandlers.ofString());
		return response.statusCode() == 200;
	}
	
	/**
	 * 删除端口映射：DeletePortMapping
	 * 请求头：SOAPAction:"urn:schemas-upnp-org:service:WANIPConnection:1#DeletePortMapping"
	 */
	public boolean deletePortMapping(int port, Protocol protocol) {
		UpnpRequest upnpRequest = UpnpRequest.newRequest(serviceType);
		String xml = upnpRequest.buildDeletePortMapping(port, protocol);
		var client = HTTPClient.newClient();
		var request = HTTPClient.newRequest(controlURL)
			.header("SOAPAction", "\"" + serviceType + "#DeletePortMapping\"")
			.POST(BodyPublishers.ofString(xml))
			.build();
		var response = HTTPClient.request(client, request, BodyHandlers.ofString());
		return response.statusCode() == 200;
	}
	
	/**
	 * 初始化
	 */
	private void initControlURL() {
		URL url = null;
		try {
			url = new URL(this.location);
		} catch (MalformedURLException e) {
			LOGGER.error("获取URL失败", e);
		}
		StringBuilder builder = new StringBuilder();
		builder.append(url.getProtocol())
			.append("://")
			.append(url.getAuthority())
			.append(this.controlURL);
		this.controlURL = builder.toString();
	}
	
}
