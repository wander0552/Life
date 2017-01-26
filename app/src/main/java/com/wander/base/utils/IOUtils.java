package com.wander.base.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

public class IOUtils {
	
	public static final int MAX_BUFFER_BYTES = 2048;
	
	public static int readInt(InputStream in) throws IOException {
		
		byte[] buffer = new byte[4];	
			
		if(in.markSupported())
			in.mark(4);
		
		int count = in.read(buffer, 0, 4);
		// 没有读取到数据，或者数据不充分
		if(count <= 0 ) {
			buffer = null;
			return Integer.MIN_VALUE;
		} 
		else if(count < 4){
			in.reset();
			buffer = null;
			return Integer.MIN_VALUE;
		}
		else {
			
			int result = (((buffer[3] << 24)& 0xff000000) | 
					((buffer[2]<<16) & 0xff0000) | 
					((buffer[1]<<8)& 0xff00) | 
					(buffer[0] & 0xff));
			
			buffer = null;
			return result;
		}

	}
	
	public static int readInt(byte[] bytes, int start) {
		byte[] buffer = new byte[4];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = bytes[start + i];
		}
		
		int result = (((buffer[3] << 24)& 0xff000000) | 
				((buffer[2]<<16) & 0xff0000) | 
				((buffer[1]<<8)& 0xff00) | 
				(buffer[0] & 0xff));
		
		buffer = null;
		return result;
	}
	
	public static short readShort(InputStream in) throws IOException {
		int len = 2;
		byte[] buffer = new byte[len];	
			
		if(in.markSupported())
			in.mark(4);
		
		int count = in.read(buffer, 0, len);
		// 没有读取到数据，或者数据不充分
		if(count <= 0 ) {
			buffer = null;
			return Short.MIN_VALUE;
		} 
		else if(count < len){
			in.reset();
			buffer = null;
			return Short.MIN_VALUE;
		}
		else {
			
			short result = (short) (((buffer[1]<<8)& 0xff00) | (buffer[0] & 0xff));
			
			buffer = null;
			return result;
		}
	}
	
	public static short readShort(byte[] bytes, int start) {
		byte[] buffer = new byte[2];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = bytes[start + i];
		}
		
		short result = (short) (((buffer[1]<<8)& 0xff00) | (buffer[0] & 0xff));
		
		buffer = null;
		return result;
	}

	public static String readLine(InputStream in) {
        StringBuffer stringbuffer = new StringBuffer();
        int i = -1;
        do {
            try {
                if ((i = in.read()) == -1) {
                    break;
                }
                char c = (char) i;
                if (c == '\n') {
                	stringbuffer.append("\r\n");
                    break;
                }
                if (c != '\r') {
                    stringbuffer.append(c);
                }
                continue;
            }
            catch (Exception exception) {
            }
            break;
        } while (true);
        if (stringbuffer.length() == 0) {
            if (i == -1) {
                return null;
            } else {
                return "";
            }
        } else {
        	
            return stringbuffer.toString();
        }
    }

	public static double readDouble(InputStream in) throws IOException {
		
		byte[] buffer = new byte[8];	
			
		if(in.markSupported())
			in.mark(8);
		
		int count = in.read(buffer, 0, 8);
		// 没有读取到数据，或者数据不充分
		if(count <= 0 ) {
			buffer = null;
			return (Double) null;
		} 
		else if(count < 8){
			in.reset();
			buffer = null;
			return (Double) null;
		}			
		else {
             int i = 0;
             long   accum  = 0; 
             for   (   int   shiftBy   =   0;   shiftBy   <   64;   shiftBy   +=   8   )   { 
         		accum |=((long)(buffer[i]&0xff))<<shiftBy; 
         		i++; 
         		} 
			buffer = null;
			return   Double.longBitsToDouble(accum);
		}

	}

	
	public static byte[] readBytes(InputStream in, int len) throws IOException {
		if( len <= 0 ) {
			return null;
		}
		
		int pos = 0, recvBytes = 0;
		byte[] buffer = new byte[len];
		while(pos < len && (recvBytes = in.read(buffer, pos, len - pos)) > 0) {
			pos += recvBytes;
		}
		
		return buffer;
	}

	public static CharSequence readString(InputStream in, int len) throws IOException {
		int leftBytes = len, recvBytes = 0;
		int bufLen = Math.min(leftBytes, MAX_BUFFER_BYTES);
		
		byte[] buffer = new byte[bufLen];		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAX_BUFFER_BYTES);
		while(leftBytes > 0 && (recvBytes = in.read(buffer, 0, bufLen)) != -1 ) {
			outputStream.write(buffer, 0, recvBytes);
			leftBytes -= recvBytes;			
		}
		
		buffer = null;
		String result = outputStream.toString();
		outputStream.close();
		return result;
	}
	
	public static CharSequence readString(InputStream in, int len, String characterSet) throws IOException {
		int leftBytes = len, recvBytes = 0;
		int bufLen = Math.min(leftBytes, MAX_BUFFER_BYTES);
		
		byte[] buffer = new byte[bufLen];		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAX_BUFFER_BYTES);
		while(leftBytes > 0 && (recvBytes = in.read(buffer, 0, bufLen)) != -1 ) {
			outputStream.write(buffer, 0, recvBytes);
			leftBytes -= recvBytes;			
		}
		
		buffer = null;
		String result = outputStream.toString(characterSet);
		outputStream.close();
		return result;
	}
	
	public static void writeShort(OutputStream out, int s) throws IOException {
		byte[] buffer = new byte[] {(byte)((s >> 8)& 0xff), (byte)(s&0xff)};
		
		out.write(buffer);
		buffer = null;
		
	}
	
	public static void writeInt(OutputStream out, int s) throws IOException {
		byte[] buffer = new byte[] {
				(byte)((s >> 24)& 0xff), 
				(byte)((s >> 16)& 0xff),
				(byte)((s >> 8)& 0xff), 
				(byte)(s&0xff)};
		
		out.write(buffer);
		buffer = null;
		
	}
	
	public static void writeString(OutputStream out, String s) throws IOException {
		out.write(s.getBytes());
	}
	
	public static void writeString(OutputStream out, String s, String characterSet) throws IOException {
		out.write(s.getBytes(characterSet));
	}
	
	public static void writeString(OutputStream out, String s, int fixedLen)throws IOException {
		byte[] bytes = s.getBytes("utf-8");
		if(fixedLen <= bytes.length){
			out.write(bytes, 0, fixedLen);
		} else {
			out.write(bytes);
			byte[] fillBytes = new byte[fixedLen - bytes.length];
			Arrays.fill(fillBytes, (byte)0);
			out.write(fillBytes);
		}
	}
	
	public static void writeString(OutputStream out, String s, String characterSet, int fixedLen)throws IOException {
		byte[] bytes = s.getBytes(characterSet);
		if(fixedLen <= bytes.length){
			out.write(bytes, 0, fixedLen);
		} else {
			out.write(bytes);
			byte[] fillBytes = new byte[fixedLen - bytes.length];
			Arrays.fill(fillBytes, (byte)0);
			out.write(fillBytes);
		}
	}
	
	public static ReadableByteChannel getChannel(InputStream inputStream) {
	    return (inputStream != null) ? Channels.newChannel(inputStream) : null;
	}

	   
    public static WritableByteChannel getChannel(OutputStream outputStream) {
        return (outputStream != null) ? Channels.newChannel(outputStream)
                : null;
    }
    
    public static long exhaust(InputStream input) throws IOException {
        long result = 0L;

        if (input != null) {
            byte[] buf = new byte[MAX_BUFFER_BYTES/2];
            try {
	            int read = input.read(buf);
	            result = (read == -1) ? -1 : 0;
	
	            while ( read != -1 ) {
	                result += read;
	                read = input.read(buf);
	            }
            }catch (IOException e) {
            	throw e;
			}finally {
				buf = null;
			}
        }

        return result;
    }
    
    public static String readLeft(InputStream in) throws IOException {
    	
    	String result = null;
    	if (in != null) {
        	int  recvBytes = 0;
        	boolean cancel = false;
    		byte[] buffer = new byte[MAX_BUFFER_BYTES/2];	    		
    		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAX_BUFFER_BYTES);
			while((recvBytes = in.read(buffer, 0, MAX_BUFFER_BYTES/2)) > 0) {
				outputStream.write(buffer, 0, recvBytes);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					cancel = true;
					break;
				}
			} 
			
    		buffer = null;	   
    		if (!cancel) {
    			result = outputStream.toString();
    		}
    		outputStream.close();
    		in.close();
        }
        return result;
    }
    
    public static String readLeft(InputStream in, String characterSet) throws IOException {
    	String result = null;
    	if (in != null) {
        	int  recvBytes = 0;
        	boolean cancel = false;
    		byte[] buffer = new byte[MAX_BUFFER_BYTES/2];	    		
    		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAX_BUFFER_BYTES);
			while((recvBytes = in.read(buffer, 0, MAX_BUFFER_BYTES/2)) > 0) {
				outputStream.write(buffer, 0, recvBytes);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					cancel = true;
					break;
				}
			} 
			
    		buffer = null;	
    		if (!cancel) {
    			result = outputStream.toString(characterSet);
    		}
    		outputStream.close();
    		in.close();
        }
        return result;
    }
    
    public static byte[] readLeftBytes(InputStream in) throws IOException {
    	byte[] result = null;
    	if (in != null) {
        	int  recvBytes = 0;
        	boolean cancel = false;
    		byte[] buffer = new byte[MAX_BUFFER_BYTES];	    		
    		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAX_BUFFER_BYTES);
			while((recvBytes = in.read(buffer, 0, MAX_BUFFER_BYTES)) > 0) {
				outputStream.write(buffer, 0, recvBytes);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					cancel = true;
					break;
				}
			} 
			
    		buffer = null;	
    		if (!cancel) {
    			result = outputStream.toByteArray();
    		}
    		outputStream.close();
    		in.close();
        }
    	
    	return result;
    }
    
    public static byte[] createZeroBytes(int length) {
    	if(length <= 0)
    		throw new IllegalArgumentException("length must be gt 0");
    	
    	byte[] bytes = new byte[length];
    	Arrays.fill(bytes,(byte)0);
    	return bytes;
    }    

    /**
	 * 从指定字节数组中查找某子字节数组的第一次出现的位置
	 * @param datas 指定数组
	 * @param start 起始查询位置
	 * @param t 待查询数组
	 * @return 如果没找到，返回-1，否则返回索引
	 */
	public static int indexOf(byte[] datas, int start, byte[] t) {
		
		if (datas == null || t == null) {
			throw new NullPointerException("source or target array is null!");
		}
		
		int index = -1;
		int len = datas.length;
		int tlen = t.length;
		
		if (start >= len || len-start < tlen ) {
			return -1;
		}

		while ( start <= len - tlen) {
			int i = 0;
			for ( ; i < tlen; i++) {
				if( datas[start+i] != t[i] ){					
					break;
				}					
			}
			
			if (i == tlen) {
				index = start;
				break;
			}
			
			start ++;
		}
		
		return index;
	}
	
	/**
	 * 从一个字节数组中解析一个整数
	 * @param buf 字节数组
	 * @param bigEndian 是否大字节序解析 
	 * @return 相应的整数
	 */
	public static int parseInteger(byte[] buf, boolean bigEndian) {
		return (int)parseNumber(buf, 4, bigEndian);
	}
	/**
	 * 从一个字节数组中解析一个短整数
	 * @param buf 字节数组
	 * @param bigEndian 是否大字节序解析 
	 * @return 相应的短整数
	 */
	public static int parseShort(byte[] buf, boolean bigEndian) {
		return (int)parseNumber(buf, 2, bigEndian);
	}
	
	/**
	 * 从一个字节数组中解析一个长整数
	 * @param buf 字节数组
	 * @param len 整数组成的字节数
	 * @param bigEndian 是否大字节序解析 
	 * @return 相应的长整数
	 */
	public static long parseNumber(byte[] buf, int len, boolean bigEndian) {
		if (buf == null || buf.length == 0) {
			throw new IllegalArgumentException("byte array is null or empty!");
		}
		int mlen = Math.min(len, buf.length);
		long r = 0;
		if (bigEndian)
			for (int i = 0; i < mlen; i++) {
				r <<= 8;
				r |= (buf[i] & 0x000000ff);
			}
		else
			for (int i = mlen-1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x000000ff);
			}
		return r;
	}
	
	public static byte[] convertToBytes(short[] buffer, int start, int length) {
		byte[] bytes = new byte[length * 2];
		ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(buffer, start, length);
		return bytes;
	}
	
	public static short[] convertToShortArray(byte[] bytes, int start, int length) {
		short[] shorts = new short[length/2];
		ShortBuffer buffer = ByteBuffer.wrap(bytes, start, length).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		buffer.get(shorts);
		buffer.clear();
		return shorts;
	}
	
	public static void convertToShortArray(byte[] bytes, int bytesstart, int byteslength,
			short[]shorts, int shortsstart, int shortslenght) {
		ShortBuffer buffer = ByteBuffer.wrap(bytes, bytesstart, byteslength).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		buffer.get(shorts, shortsstart, shortslenght);
		buffer.clear();
	}
}
