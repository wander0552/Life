package com.wander.base.net;

import android.text.TextUtils;

import com.wander.base.log.WLog;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;



public final class TCPProxy {
	private static final String TAG = "TCPProxy";

	private static final int SERVER_PORT = 80;

	public static TCPProxy getInstance() {
		return instance;
	}

	public static class KWTCPProxyResult {
		public boolean success;
		public byte[] data;

		public KWTCPProxyResult(final boolean success, final byte[] data) {
			this.success = success;
			this.data = data;
		}
	}

	// data为空发GET请求，不为空发POST请求
	public KWTCPProxyResult syncHTTP(final String url, final byte[] data) {
//		KwDebug.mustNotMainThread();

		String host = null;
		try {
			host = new URL(url).getHost();
		} catch (MalformedURLException e) {
			return null;
		}

		DNSLookup.IPInfo ipInfo = DNSLookup.getInstance().ipFromDomain(host);
		if (ipInfo==null || !ipInfo.useProxy || TextUtils.isEmpty(ipInfo.proxyIP)) {
			return null;
		}

		WLog.i(TAG, "syncHTTP" + url);

		Socket socket = null;
		try {
			socket = new Socket(ipInfo.proxyIP, SERVER_PORT);
			socket.setSoTimeout(10000);
		} catch (Exception e) {
			return null;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (Exception e) {
			try {
				if (in != null)
					in.close();
				socket.close();
			} catch (Exception e1) {
			}
			return null;
		}
		try {
			try {
				byte[] delim = "\n".getBytes();
				out.write("pro".getBytes());
				if (data != null) {
					out.write("P".getBytes());
				} else {
					out.write("G".getBytes());
				}
				out.write(delim);
				out.write("install".getBytes());
				out.write(delim);
				out.write("deviceId".getBytes());
				out.write(delim);
				out.write(url.getBytes());
				out.write(delim);
				if (data != null) {
					out.write(String.valueOf(data.length).getBytes());
					out.write(delim);
					out.write(data);
					out.write(delim);
				}
				out.flush();
			} catch (Exception e) {
				WLog.e(TAG, e);
				return null;
			}
			byte[] buf = new byte[1];
			StringBuilder builder = new StringBuilder();
			try {
				while (in.read(buf) == 1 && buf[0] != '\n') {
					builder.append(String.valueOf((char) buf[0]));
				}
			} catch (Exception e) {
				WLog.d("TCPProxy", builder.toString());
				WLog.e("TCPProxy", e);
				return null;
			}
			String firstLine = builder.toString().trim();
			WLog.i(TAG, "firstLine" + firstLine.length() + ":" + firstLine);
			if (!firstLine.contains("ok")) {
				WLog.d(TAG, "firstLine no 'ok'");
				return null;
			}
			if (firstLine.length() == 2) {
				WLog.i(TAG, "success no body");
				return new KWTCPProxyResult(true, null);
			}
			int len = 0;
			try {
				len = Integer.valueOf(firstLine.substring(2));
			} catch (Exception e) {
				WLog.e(TAG, e);
				return null;
			}
			if (len == 0) {
				WLog.i(TAG, "success no body");
				return new KWTCPProxyResult(true, null);
			}
			byte[] buffer = new byte[len];
			int total = 0;
			try {
				while (total < len) {
					int num = in.read(buffer,total,len - total);
					total += num;
				}
			} catch (Exception e) {
				WLog.e(TAG, e);
				return null;
			}
			WLog.i(TAG, "success with body");
			return new KWTCPProxyResult(true, buffer);
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e1) {
			}
			try {
				socket.close();
			} catch (Exception e1) {
			}
		}
	}

	private static TCPProxy instance = new TCPProxy();
}
