package com.wander.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

// by haiping
public class NetworkStateUtil extends BroadcastReceiver {
	static long l;
	private static final String TAG = "NetworkStateUtil";

	public static void init(final Context ctx) {
		if (attached) {
			return;
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		try {
			ctx.registerReceiver(instance, filter);
			attached = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		getNetworkInfo(ctx);
	}

	public static void release(final Context ctx) {
		if (attached) {
			try { // 说好了不到处try的，安卓你咋了
				ctx.unregisterReceiver(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
			attached=false;
		}
	}

	public static final int	TYPE_UNKNOWN	= 0, TYPE_2G = 1, TYPE_3G = 2, TYPE_4G = 3;

	public static int getNetWorkType() {
		return networkTypeID;
	}

	// "WIFI"、"2G"、"3G"、"4G"、"UNKNOWN"
	public static String getNetworkTypeName() {
		return networkTypeName;
	}

	public static final int	OPERATOR_UNKNOWN	= 0, OPERATOR_CMCC = 1, OPERATOR_CUCC = 2, OPERATOR_CT = 3;

	@Override
	public final void onReceive(final Context context, final Intent intent) {
		AsyncTask asyncTask = new AsyncTask() {
			@Override
			protected Object doInBackground(Object[] params) {
				getNetworkInfo(context);
				return null;
			}
		};
		asyncTask.execute();
	}

	public static void getNetworkInfo(final Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return;
		}

		NetworkInfo[] info = null;
		try {
			info = connectivity.getAllNetworkInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (info != null) {

			for (int i = 0; i < info.length; i++) {
				if (info[i].isConnected()) {
					if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
						networkTypeName = "WIFI";
					} else if (info[i].getType() == ConnectivityManager.TYPE_MOBILE) {
						int typeID = info[i].getSubtype();
						if (typeID < NETWORK_TYPES.length) {
							networkTypeID = NETWORK_TYPES[typeID][0];
							networkTypeName = TYPE_NAME[networkTypeID];
						} else {
							networkTypeID = TYPE_3G;
							networkTypeName = "3G";
						}
					} else {
						networkTypeName = "UNKNOWN";
					}
					break;
				}
			}
		}
	}

	private static final String[]	TYPE_NAME		= { "UNKNOWN", "2G", "3G", "4G" };

	private static final int[][]	NETWORK_TYPES	= {
													/* 0 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN },
													/* 1 */{ TYPE_2G, OPERATOR_CUCC },
													/* 2 */{ TYPE_2G, OPERATOR_CMCC },
													/* 3 */{ TYPE_3G, OPERATOR_CUCC },
													/* 4 */{ TYPE_2G, OPERATOR_CT },
													/* 5 */{ TYPE_3G, OPERATOR_CT },
													/* 6 */{ TYPE_3G, OPERATOR_CT },
													/* 7 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN },
													/* 8 */{ TYPE_3G, OPERATOR_CUCC },
													/* 9 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN },
													/* 10 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN },
													/* 11 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN },
													/* 12 */{ TYPE_3G, OPERATOR_CT },
													/* 13 */{ TYPE_4G/* 其实是TYPE_LTE */, OPERATOR_UNKNOWN },
													/* 14 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN },
													/* 15 */{ TYPE_UNKNOWN, OPERATOR_UNKNOWN }
													};

	private static NetworkStateUtil instance		= new NetworkStateUtil();
	private static boolean			attached;
	private static volatile int		networkTypeID;
	private static volatile String networkTypeName	= "UNKNOWN";
}
