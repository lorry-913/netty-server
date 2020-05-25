package com.midea.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author guohusheng
 * @husheng.guo@midea.com
 */
public class IPUtil {

	/**
	 * 判断目标IP是否在指定范围内
	 *
	 * @param ipCheck
	 *            目标IP
	 * @param ipStart
	 *            指定I范围开始
	 * @param ipEnd
	 *            指定I范围结束
	 * @return
	 */
	public static boolean ipRangCheck(String ipCheck, String ipStart, String ipEnd) {
		boolean result = false;
		long ipStartL = getIpNum(ipStart);
		long ipEndL = getIpNum(ipEnd);
		long ipCheckL = getIpNum(ipCheck);
		if (isInner(Long.valueOf(ipCheckL), Long.valueOf(ipStartL), Long.valueOf(ipEndL))) {
			result = true;
		} else {
			result = false;
		}

		return result;
	}

	/* 获取IP数 */
	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);
		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}

	// 获取客户端ip
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
}
