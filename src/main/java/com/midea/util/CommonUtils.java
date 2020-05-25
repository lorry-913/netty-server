package com.midea.util;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {

	public static boolean isStrNotNull(String str) {
		return str != null && str.trim().length() != 0;
	}

	public static boolean isStrNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListNotNull(List l) {
		return l != null && l.size() > 0;
	}

	public static String getStr(Object o) {
		if (o == null)
			return "";
		else
			return o.toString().trim();
	}

	public static Short getShort(Object o) {
		if (o == null || o.toString().trim() == "")
			return Short.valueOf("0");
		else
			return Short.valueOf(o.toString().trim());
	}

	public static Long getLong(Object o) {
		if (o == null || o.toString().trim() == "")
			return Long.valueOf("0");
		else
			return Long.valueOf(o.toString().trim());
	}

	public static Float getFloat(Object o) {
		if (o == null || o.toString().trim() == "")
			return Float.valueOf("0");
		else
			return Float.valueOf(o.toString().trim());
	}

	public static Integer getInteger(Object o) {
		if (o == null || o.toString().trim() == "")
			return Integer.valueOf("0");
		else
			return Integer.valueOf(o.toString().trim());
	}

	public static int getCurrentPage(String iDisplayStart, String iDisplayLength) {
		if (CommonUtils.isStrNull(iDisplayStart) || CommonUtils.isStrNull(iDisplayLength))
			return 1;
		return Integer.valueOf(iDisplayStart) / Integer.valueOf(iDisplayLength) + 1;
	}

	/**
	 * 获取图片 mime 类型
	 *
	 * @param filePath
	 * @return
	 */
	public static String getImageContentType(String filePath) {
		if (filePath == null || filePath.lastIndexOf(".") <= 0)
			return null;
		String last = filePath.substring(filePath.lastIndexOf(".") + 1);
		return "image/" + last;
	}

	public static String appendStr(String... strings) {
		StringBuilder sb = new StringBuilder();
		if (strings != null && strings.length > 0) {
			for (String s : strings) {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	// 只有输入的为数值才会返回正确的输入值int
	// 其他任何类型比如字母等等均返回0
	public static int getInt(Object o) {
		try {
			if (o instanceof Integer) {
				return (int) (Integer) o;
			} else if (o instanceof String) {
				return (int) Integer.valueOf(o.toString());
			}

		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// public static boolean isNumeric(String str) {
	// for (int i = 0; i < str.length(); i++) {
	// if (!Character.isDigit(str.charAt(i))) {
	// return false;
	// }
	// }
	// return true;
	// }

	// 根据每页数据量和数据总量获取页数
	public static int getPageSize(int size, int count) {
		int pageSize = 0;
		if (count > 0) {
			int m = count % size;
			pageSize = count / size;
			if (m > 0) {
				pageSize = pageSize + 1;
			}
		}
		return pageSize;
	}

	// 获取客户端ip
	public static String getRemoteHost(HttpServletRequest request) {
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

	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		int r = v2.compareTo(BigDecimal.ZERO);
		if (r == 0) {
			return new BigDecimal("0.00");
		}
		return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static String disNname(String nname) {
		String result = "";
		if (nname.contains("_")) {
			int intc = nname.indexOf("_");
			if (intc != -1) {
				result = nname.substring(0, intc);
			}
		} else {
			result = nname;
		}
		return result;
	}

	/**
	 * 获取用户名
	 *
	 * @param uname
	 *            昵称
	 * @param email
	 *            邮箱
	 * @param mobile
	 *            手机号
	 * @param qqNickname
	 *            qq昵称
	 * @param wbNickname
	 *            wb昵称
	 * @param wxNickname
	 *            wx昵称
	 * @return
	 */
	public static String getUname(String uname, String email, String mobile, String qqNickname, String wbNickname,
			String wxNickname) {
		String result = "";
		// 用户名——>邮箱——>手机号——>QQ昵称——>微博昵称——>微信昵称
		String userName = "";
		if (isStrNotNull(mobile)) {
			userName = mobile.substring(0, mobile.length() - (mobile.substring(3)).length()) + "****"
					+ mobile.substring(7);
		}
		result = isStrNotNull(uname) ? uname
				: isStrNotNull(email) ? email
						: isStrNotNull(userName) ? userName
								: isStrNotNull(qqNickname) ? disNname(qqNickname)
										: isStrNotNull(qqNickname) ? disNname(qqNickname)
												: isStrNotNull(wbNickname) ? disNname(wbNickname)
														: isStrNotNull(wxNickname) ? disNname(wxNickname) : "";
		return result;
	}

	public static void main(String[] args) {
		String s = "false";
		System.out.println(isNumeric(s));
	}
}
