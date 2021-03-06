package com.pengjun.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import android.util.Base64;

public class StringUtils {

	public static final String NULL_STRING = "";

	public static String getObjectFieldValue(Object object) {
		return ReflectionToStringBuilder.toString(object,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public static byte[] decodeBase64(final byte[] bytes) {
		return Base64.decode(bytes, Base64.DEFAULT);
	}

	public static String encodeBase64(final byte[] bytes) {
		return new String(Base64.encode(bytes, Base64.DEFAULT));
	}

	public static String createMd5(String str) {

		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();

	}

	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String replace(String source, String oldStr, String newStr) {
		return replace(source, oldStr, newStr, true);
	}

	public static String replace(String source, String oldStr, String newStr,
			boolean matchCase) {
		if (source == null) {
			return null;
		}
		if (source.toLowerCase().indexOf(oldStr.toLowerCase()) == -1) {
			return source;
		}
		int findStartPos = 0;
		int a = 0;
		StringBuffer bbuf = new StringBuffer(source);
		while (a > -1) {
			int b = 0;
			String strA, strB;
			if (matchCase) {
				strA = source;
				strB = oldStr;
			} else {
				strA = source.toLowerCase();
				strB = oldStr.toLowerCase();
			}
			a = strA.indexOf(strB, findStartPos);
			if (a > -1) {
				b = oldStr.length();
				findStartPos = a + b;
				bbuf = bbuf.replace(a, a + b, newStr);
				// �µĲ��ҿ�ʼ��λ���滻����ַ�Ľ�β
				findStartPos = findStartPos + newStr.length() - b;
			}
		}
		return bbuf.toString();
	}

	/**
	 * ����ַ��β�Ŀո�.
	 * 
	 * @param input
	 *            String ������ַ�
	 * @return ת�����
	 */
	public static String trimTailSpaces(String input) {
		if (isEmpty(input)) {
			return "";
		}

		String trimedString = input.trim();
		if (trimedString.length() == input.length()) {
			return input;
		}
		return input.substring(0,
				input.indexOf(trimedString) + trimedString.length());
	}

	/**
	 * Change the null string value to "", if not null, then return it self, use
	 * this to avoid display a null string to "null".
	 * 
	 * @param input
	 *            the string to clear
	 * @return the result
	 */
	public static String clearNull(String input) {
		return isEmpty(input) ? "" : input;
	}

	/**
	 * ��ȡ�̶������ַ�
	 * 
	 * @param input
	 *            String
	 * @param maxLength
	 *            int
	 * @return String processed result
	 */
	public static String limitStringLength(String input, int maxLength) {
		if (isEmpty(input))
			return "";

		if (input.length() <= maxLength) {
			return input;
		} else {
			return input.substring(0, maxLength - 3) + "...";
		}

	}

	/**
	 * �ж��ַ��Ƿ�ȫ�������ַ�.
	 * 
	 * @param input
	 *            ������ַ�
	 * @return �жϽ��, true Ϊȫ����, false Ϊ���з������ַ�
	 */
	public static boolean isNumeric(String input) {
		if (isEmpty(input)) {
			return false;
		}

		for (int i = 0; i < input.length(); i++) {
			char charAt = input.charAt(i);

			if (!Character.isDigit(charAt)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ת���ɱ?��ȡ����ݵ�����(�� ISO8859 ת���� gb2312).
	 * 
	 * @param input
	 *            ������ַ�
	 * @return ת�����, ����д�����, �򷵻�ԭ����ֵ
	 */
	public static String ISO2GBK(String input) {
		try {
			byte[] bytes = input.getBytes("ISO8859-1");
			return new String(bytes, "GBK");
		} catch (Exception ex) {
		}
		return input;
	}

	/**
	 * ת���ɱ?��ȡ����ݵ����뵽 ISO(�� GBK ת����ISO8859-1).
	 * 
	 * @param input
	 *            ������ַ�
	 * @return ת�����, ����д�����, �򷵻�ԭ����ֵ
	 */
	public static String GBK2ISO(String input) {
		return changeEncoding(input, "GBK", "ISO8859-1");
	}

	/**
	 * ת���ַ������.
	 * 
	 * @param input
	 *            ������ַ�
	 * @param sourceEncoding
	 *            Դ�ַ����
	 * @param targetEncoding
	 *            Ŀ���ַ����
	 * @return ת�����, ����д�����, �򷵻�ԭ����ֵ
	 */
	public static String changeEncoding(String input, String sourceEncoding,
			String targetEncoding) {
		if (input == null || input.equals("")) {
			return input;
		}

		try {
			byte[] bytes = input.getBytes(sourceEncoding);
			return new String(bytes, targetEncoding);
		} catch (Exception ex) {
		}
		return input;
	}

	public static String replaceSql(String input) {
		return replace(input, "'", "''");
	}

	public static String encode(String value, String type) {
		if (isEmpty(value)) {
			return "";
		}

		try {
			value = java.net.URLEncoder.encode(value, type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return value;
	}

	public static String decode(String value, String type) {
		if (isEmpty(value)) {
			return "";
		}

		try {
			return java.net.URLDecoder.decode(value, type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return value;
	}

	public static boolean isEmpty(String input) {
		return (input == null || input.length() == 0);
	}

	public static int getBytesLength(String input) {
		if (input == null) {
			return 0;
		}

		int bytesLength = input.getBytes().length;
		return bytesLength;
	}

	public static String isEmpty(String input, String errorMsg) {
		if (isEmpty(input)) {
			return errorMsg;
		}
		return "";
	}

	public static boolean isPrime(int x) {
		if (x <= 7) {
			if (x == 2 || x == 3 || x == 5 || x == 7)
				return true;
		}
		int c = 7;
		if (x % 2 == 0)
			return false;
		if (x % 3 == 0)
			return false;
		if (x % 5 == 0)
			return false;
		int end = (int) Math.sqrt(x);
		while (c <= end) {
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 6;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 6;
		}
		return true;
	}

	public static String toLength(String str, int length) {
		if (str == null) {
			return null;
		}
		if (length <= 0) {
			return "";
		}
		try {
			if (str.getBytes("GBK").length <= length) {
				return str;
			}
		} catch (Exception ex) {
		}
		StringBuffer buff = new StringBuffer();

		int index = 0;
		char c;
		length -= 3;
		while (length > 0) {
			c = str.charAt(index);
			if (c < 128) {
				length--;
			} else {
				length--;
				length--;
			}
			buff.append(c);
			index++;
		}
		buff.append("...");
		return buff.toString();
	}

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isEmail(String str) {
		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	public static String filter(String value) {
		if (value == null || value.length() == 0)
			return value;
		StringBuffer result = null;
		String filtered = null;
		for (int i = 0; i < value.length(); i++) {
			filtered = null;
			switch (value.charAt(i)) {
			case 60: // '&lt;'
				filtered = "&lt;";
				break;

			case 62: // '&gt;'
				filtered = "&gt;";
				break;

			case 38: // '&amp;'
				filtered = "&amp;";
				break;

			case 34: // '"'
				filtered = "\"";
				break;

			case 39: // '\''
				filtered = "'";
				break;
			}
			if (result == null) {
				if (filtered != null) {
					result = new StringBuffer(value.length() + 50);
					if (i > 0)
						result.append(value.substring(0, i));
					result.append(filtered);
				}
			} else if (filtered == null)
				result.append(value.charAt(i));
			else
				result.append(filtered);
		}

		return result != null ? result.toString() : value;
	}

	public static HashMap<String, String> parseQuery(String url) {
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			return null;
		}
		final String query = uri.getQuery();
		if (query == null) {
			return null;
		}
		StringTokenizer tokens = new StringTokenizer(query, "&", false);
		HashMap<String, String> params = new HashMap<String, String>(
				tokens.countTokens());
		while (tokens.hasMoreTokens()) {
			final String kv = tokens.nextToken();
			final int delimiterIndex = kv.indexOf('=');
			if (delimiterIndex > 0 && delimiterIndex < kv.length() - 1) {
				final String key = kv.substring(0, delimiterIndex);
				final String value = kv.substring(delimiterIndex + 1);
				params.put(key, value);
			}
		}

		return params;
	}
}
