package com.wander.base.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 
     *  字符解析器
     */
    public interface CharSpeller {
        String spell(char c);
    }
	/**
	 * 如果string为null,返回""
	 */
	public static String getNotNullString(String string){
		if(null == string){
			return "";
		}
		return string;
	}
    private static CharSpeller speller = null;
    public static void registerSpeller(CharSpeller cs) {
        speller = cs;
    }    
    
	private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9",
		"a","b","c","d","e","f"};
	
	private final static Pattern DIGIT_PATTERN = Pattern.compile("(\\d+)");
	/**
	 * 将指定字节转换成16进制字符
	 * @param b 待转换字节
	 * @return 返回转换后的字符串
	 */
	public  static String byteToHexDigits(byte b){
		int n= b;
		if (n < 0)  n += 256;
		
		int d1 = n/16;
		int d2 = n%16;
		
		return hexDigits[d1] + hexDigits[d2];
	}
	/**
	 * 将指定字节数组转换成16进制字符串
	 * @param bytes 待转换的字节数组
	 * @return 返回转换后的字符串
	 */
	public static String bytesToHexes(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byteToHexDigits(bytes[i]));
		}
		return sb.toString();
	}
	
	/**
     * 比较两个字符串大小，考虑汉字拼音顺序, 忽略大小写
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 返回比较结果。0： s1 = s2， >0： s1 > s2, <0: s1 < s2
     */
	public static int compareToIgnoreCase(String s1, String s2) {
	    // 两者为空，相同
        if (s1 == null && s2 == null) {
            return 0;
        }
        // 某项为空，则以它为小
        if (s1 == null) {
            return -1;
        }
        
        if (s2 == null) {
            return 1;
        }
        
        if (s1.equals(s2)) {
            return 0;
        }
        
        String s3 = s1.toLowerCase();
        String s4 = s2.toLowerCase();
        
        return compareToUnicode(s3, s4);
        
        /*int ret = 0;
        Matcher matcher1 = DIGIT_PATTERN.matcher(s3);
        Matcher matcher2 = DIGIT_PATTERN.matcher(s4);
        
        if (matcher1.groupCount() == 0 || matcher2.groupCount() == 0) {
            ret = compareToGBK(s3, s4);
        } else {
            
            while (matcher1.find() && matcher2.find() && ret == 0) {                
                String sub1 = matcher1.group();
                int start1 = matcher1.start();              
                String sub2 = matcher2.group();
                int start2 = matcher2.start();
                String begin1 = s3.substring(0, start1);
                String begin2 = s4.substring(0, start2);
                ret = compareToGBK(begin1, begin2);
                if (start1 == start2 && ret == 0) { 
                    int i1 = 0 , i2 = 0;
                    boolean b1 = false , b2 = false;
                    try {
                        i1 = Integer.parseInt(sub1);                        
                    } catch (NumberFormatException e) {
                        b1 = true;
                    }
                    
                    try {                       
                        i2 = Integer.parseInt(sub2);                                
                    } catch (NumberFormatException e) {
                        b2 = true;
                    }
                    
                    if ( b1 && b2 ) {
                        ret = compareToBigInteger(sub1, sub2);
                    } else if (b1) {
                        ret = 1;
                    } else if (b2) {
                        ret = -1;
                    } else {
                        ret = i1 - i2;      
                    }                       
                } else {
                    return ret;
                }
            }
            
            if (ret == 0) {
                ret = compareToGBK(s3, s4);
            }
        }
        
        return ret;*/
	}
	
	/**
	 * 比较两个字符串大小，考虑汉字拼音顺序
	 * @param s1 字符串1
	 * @param s2 字符串2
	 * @return 返回比较结果。0： s1 = s2， >0： s1 > s2, <0: s1 < s2
	 */
	public static int compareTo(String s1, String s2) {
    	
    	// 两者为空，相同
    	if (s1 == null && s2 == null) {
    		return 0;
		}
    	// 某项为空，则以它为小
    	if (s1 == null) {
    		return -1;
    	}
    	
    	if (s2 == null) {
    		return 1;
    	}
    	
    	if (s1.equals(s2)) {
			return 0;
		}
    	
    	 if (s1.length() == 0) {
             return -1;
         }
         
         if (s2.length() == 0) {
             return 1;
         }
    	
    	return compareToUnicode(s1, s2);
    	/*int ret = 0;    	
    	
    	Matcher matcher1 = DIGIT_PATTERN.matcher(s1);
    	Matcher matcher2 = DIGIT_PATTERN.matcher(s2);
    	
    	if (matcher1.groupCount() == 0 || matcher2.groupCount() == 0) {
    		ret = compareToUnicode(s1, s2);
		} else {
			
			while (matcher1.find() && matcher2.find() && ret == 0) {				
				String sub1 = matcher1.group();
				int start1 = matcher1.start();				
				String sub2 = matcher2.group();
				int start2 = matcher2.start();
				String begin1 = s1.substring(0, start1);
				String begin2 = s2.substring(0, start2);
				ret = compareToUnicode(begin1, begin2);
				if (start1 == start2 && ret == 0) {	
					int i1 = 0 , i2 = 0;
					boolean b1 = false , b2 = false;
					try {
						i1 = Integer.parseInt(sub1);						
					} catch (NumberFormatException e) {
						b1 = true;
					}
					
					try {						
						i2 = Integer.parseInt(sub2);								
					} catch (NumberFormatException e) {
						b2 = true;
					}
					
					if ( b1 && b2 ) {
						ret = compareToBigInteger(sub1, sub2);
					} else if (b1) {
						ret = 1;
					} else if (b2) {
						ret = -1;
					} else {
						ret = i1 - i2;		
					}						
				} else {
					return ret;
				}
			}
			
			if (ret == 0) {
				ret = compareToUnicode(s1, s2);
			}
		}*/
    	
//    	return ret;
    }
	
	public static boolean isLetter(int ch) {
	    return (ch >= 'A' && ch <= 'Z') || (ch >='a' && ch <= 'z');
	}
	
	public static boolean isDigit(int ch) {
	    return ch >= '0' && ch <= '9';
	}
	
	static final int UPPER_LOWER_SPAN = 'A' - 'a';
	static final int LOWER_UPPER_SPAN = -UPPER_LOWER_SPAN;
	private static int compareToGBK(String s1, String s2) {
		int ret = 0;
		try {
			byte[] bytes1 = s1.getBytes("gbk");
			byte[] bytes2 = s2.getBytes("gbk");
			
			int len = Math.min(bytes1.length, bytes2.length);
			for (int i = 0; i < len; i++) {
			    
			    if (bytes1[i] >0 && bytes2[i] > 0) {
			        ret = Character.toLowerCase(bytes1[i]) - Character.toLowerCase(bytes2[i]);
                    if (ret == 0)
                        ret = bytes1[i] - bytes2[i];    
			    }
			    else {
			        int b1 = (bytes1[i] + 256) % 256;
	                int b2 = (bytes2[i] + 256) % 256;
	                ret = b1 - b2;	                
			    }
			    
			    if (ret != 0) {
                    break;
                }
				
			}
			if (ret == 0) {				
				ret = bytes1.length - bytes2.length;
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return ret ;
	}

	public static boolean isNumeric(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				if (i != 0 || str.charAt(i) != '-') {
					return false;
				}
			}
		}
		return true;
	}
	
	private static int compareToUnicode(String s1, String s2) {
	    
	    if (speller == null) {
	        return compareToGBK(s1, s2);
	    }
	    
        int ret = 0;
        int len = Math.min(s1.length(),s2.length());
        for (int i = 0; i < len; i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            
            ret = compareTo(c1, c2);
            if (ret != 0)
                break;
        }
        
        if (ret == 0) {
            ret = s1.length() - s2.length();
        }
        
        return ret;
    }
	
	private static int compareTo(char c1, char c2) {
        int ret = 0;
        // 字母比较，直接比较ASCII
        if (isLetter(c1) && isLetter(c2)) {
            ret = Character.toLowerCase(c1) - Character.toLowerCase(c2);
            if (ret == 0)
                ret = c1 - c2;
            
            return ret;
        }
        
        String s1  = null;
        String s2 = null;
        
        if (isLetter(c1)) {
            s2 = speller.spell(c2);
            char cc2 = s2.charAt(0);
            if (isLetter(cc2)) {
                ret = Character.toLowerCase(c1) - Character.toLowerCase(cc2);
                if (ret == 0) {
                    ret = 1;
                }
                
                return ret;
            }
            else 
                return -1;
        }
        
        else if (isLetter(c2)) {
            s1  = speller.spell(c1);
            char cc1 = s1.charAt(0);
            if (isLetter(cc1)) {
                ret = Character.toLowerCase(cc1) - Character.toLowerCase(c2);
                if (ret == 0) {
                    ret = -1;
                }
               
                return ret;
            } else {
                return 1;
            }
        } else {
            s1  = speller.spell(c1);
            s2 = speller.spell(c2);
        }
        
        int len = Math.min(s1.length(),s2.length());
        
        for (int i = 0; i < len; i++) {
            char cc1 = s1.charAt(i);
            char cc2 = s2.charAt(i);
            
            if (isLetter(cc1) && isLetter(cc2)) {
                ret = Character.toLowerCase(cc1) - Character.toLowerCase(cc2);
                if (ret == 0) {
                    ret = cc1 - cc2;
                }
            }
            
            else if (isLetter(cc1)) {
                ret = -1;
            }
            
            else if (isLetter(cc2)) {
                ret = 1;
            }
            
            else {
                ret = cc1 - cc2;
            }
            
            if (ret != 0) {
                break;
            } 
        }
        
        if (ret == 0) {
            ret = s1.length() - s2.length();
        }
        
        return ret;
	}
	
	private static int compareToBigInteger(String s1, String s2)
	{
		int ret = 0;
		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();
		
		int index1 = 0,index2 =0;
		while ( index1 < c1.length && c1[index1] == '0') index1++;
		while ( index2 < c2.length && c2[index2] == '0') index2++;
		
		if (c1.length - index1 != c2.length -index2) {
			ret = (c1.length - index1) - (c2.length - index2);
		} else {
			ret = c1[index1] -c2[index2];
		}
		
		return ret;
	}
	
	/**
	 * 将作为文件名的字符串的特殊字符"\*?:$/'",`^<>+"替换成"_"，以便文件顺利创建成功
	 * @param path 原待创建的文件名
	 * @return 返回处理后的文件名
	 */
	public static String filterForFile(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		String need = path.replaceAll("\\\\|\\*|\\?|\\:|\\$|\\/|'|\"|,|`|\\^|<|>|\\+", "_");
		return need;
	}

	//请求结果转换成字符串，可以指定编码
	public static String dataToString(byte[] data, String charset){

		if( null != data){
			StringCodec codec = new StringCodec();
			try{
				return codec.decode(data, charset);
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}catch(Throwable e){
			}
		}
		return null;
	}

	/**
	 * Converts the byte array of HTTP content characters to a string. If
	 * the specified charset is not supported, default system encoding
	 * is used.
	 *
	 * @param data the byte array to be encoded
	 * @param offset the index of the first byte to encode
	 * @param length the number of bytes to encode
	 * @param charset the desired character encoding
	 * @return The result of the conversion.
	 */
	public static String getString(
			final byte[] data,
			int offset,
			int length,
			String charset
	) {

		if (data == null) {
			throw new IllegalArgumentException("Parameter may not be null");
		}

		if (charset == null || charset.length() == 0) {
			throw new IllegalArgumentException("charset may not be null or empty");
		}

		try {
			return new String(data, offset, length, charset);
		} catch (UnsupportedEncodingException e) {
			return new String(data, offset, length);
		}
	}


	/**
	 * Converts the byte array of HTTP content characters to a string. If
	 * the specified charset is not supported, default system encoding
	 * is used.
	 *
	 * @param data the byte array to be encoded
	 * @param charset the desired character encoding
	 * @return The result of the conversion.
	 */
	public static String getString(final byte[] data, final String charset) {
		if (data == null) {
			throw new IllegalArgumentException("Parameter may not be null");
		}
		return getString(data, 0, data.length, charset);
	}
	/**
	 * 密码
	 */
	public static boolean iSPassword(String pwd){
		String regEx = "[a-zA-Z]{1,}[0-9]{1,}|[0-9]{1,}[a-zA-Z]{1,}" +
				"|[A-Za-z0-9]{1,}[~!@#$#$%^&*()_+;,.{}|<>￥.`£€~!@#$%^%^*()_+\":?>+-,?!#@]{1,}" +
				"|[~!@#$#$%^&*()_+;,.{}|<>￥.`£€~!@#$%^%^*()_+\":?>+-,?!#@]{1,}[A-Za-z0-9]{1,}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(pwd);
		return m.find();
	}
	//包含中文
	public static boolean isChinesechar(String pwd){
		String reg = "[(\\u4e00-\\u9fa5)]";
		Pattern pstr = Pattern.compile(reg);
		Matcher m1=pstr.matcher(pwd);
		return m1.find();
	}

	public static String getValueFromSetString(String setString) {
		String pair[] = setString.split(":");
		return (pair.length == 2 ? pair[1] : "");
	}

	public static String[] split(String str, char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	private static String[] splitWorker(String str, char separatorChar,
										boolean preserveAllTokens) {
		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	// 将List转成字符，每一行用'\n'结尾
	public static <T extends Object> String listToString(List<T> list) {
		if (list == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		for (T info : list) {
			if (info != null) {
				sb.append(info.toString());
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	// 将字符串按照行的方式，组成一个List
	public static List<String> stringToList(String data, List<String> list) {
		if (TextUtils.isEmpty(data) || list == null) {
			return null;
		}

		String[] lines = data.split("\\n");

		for (String line : lines) {
			list.add(line);
		}

		return list;
	}
}
