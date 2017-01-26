package com.wander.base.net;

import android.text.TextUtils;

import com.wander.base.context.AppContext;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;


public final class DNSLookup {

	public static DNSLookup getInstance() {
		return instance;
	}

	public static class IPInfo {
		public String ipAddr;
		public boolean useProxy;
		public String proxyIP;

		public IPInfo(final String ip, final boolean proxy, final String proxyIP) {
			ipAddr = ip;
			useProxy = proxy;
			this.proxyIP = proxyIP;
		}
	}

	//增加对K歌服务的DNS解析支持
	public IPInfo ipFromDomain(final String domain) {

		IPInfo ipInfo = getFromCache(domain);
		if (ipInfo != null) {
			return ipInfo;
		}

//		String serverIP = ConfMgr.getStringValue(ConfDef.SEC_APP, ConfDef.KEY_APP_KWUDPDNS_SERVER,
//				ConfDef.VAL_APP_KWUDPDNS_SERVER);
		String serverIP = "60.28.201.13";
		InetAddress serverAddr;
		try {
			serverAddr = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}

		DatagramSocket client;
		try {
			client = new DatagramSocket();
			client.setSoTimeout(10000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder dataBuilder = new StringBuilder("dnslookup\n");
		dataBuilder.append(domain).append("\n");
		dataBuilder.append(AppContext.INSTALL_SOURCE).append("\n");
		dataBuilder.append(AppContext.DEVICE_ID).append("\n");
		String data = dataBuilder.toString();
		
		int port = new Random().nextInt()%2 == 0 ? 443 : 80;
		DatagramPacket sendPacket = new DatagramPacket(data.getBytes(), data.length(), serverAddr, port);
		byte[] recvBuf = new byte[32];
		DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
		try {
			client.send(sendPacket);
			client.receive(recvPacket);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.disconnect();
			client.close();
		}

		String retData = new String(recvPacket.getData(), 0, recvPacket.getLength());
		if (TextUtils.isEmpty(retData)) {
			return null;
		}
		String ip = "";
		boolean useProxy = false;
		String proxyIP = "";
		if (!retData.contains("ignore")) {
			String[] lines = retData.split("\n");
			if (lines.length>0) {
				ip = lines[0];
			}
			if (lines.length>2) {
				useProxy = lines[1].contains("proxy");
				proxyIP = lines[2];
			}
		}
		
		IPInfo info = new IPInfo(ip, useProxy, proxyIP);
		save2Cache(domain, info);

		return info;
	}

	private synchronized IPInfo getFromCache(final String domain) {
		return ipCachemap.get(domain);
	}

	private synchronized void save2Cache(final String domain, final IPInfo ip) {
		ipCachemap.put(domain, ip);
	}

	private static DNSLookup instance = new DNSLookup();
	private HashMap<String, IPInfo> ipCachemap = new HashMap<String, IPInfo>();
}
