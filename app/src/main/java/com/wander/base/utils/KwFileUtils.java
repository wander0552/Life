package com.wander.base.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KwFileUtils {
	private final static String TAG					= "FileUtils";

	/**
	 * 获取除扩展名以外的部分
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileNameWithoutSuffix(String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			return "";
		}
		int lastIndex = fileName.lastIndexOf(".");
		String fileNameWithoutSuffix = "";
		if (lastIndex != -1) {
			fileNameWithoutSuffix = fileName.substring(0, lastIndex);
		}
		return fileNameWithoutSuffix;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileExtension(String path) {
		if (TextUtils.isEmpty(path)) {
			return "";
		}
		int index = path.lastIndexOf(".");
		if (index == -1) {
			return "";
		}
		String extension = path.substring(index + 1, path.length());
		if (TextUtils.isEmpty(extension)) {
			return "";
		}
		return extension;
	}

	/**
	 * 获取文件目录
	 */
	public static String getFilePath(String fullPathName) {
		int lastIndex = fullPathName.lastIndexOf(File.separator);
		String path = "";
		if (lastIndex != -1) {
			path = fullPathName.substring(0, lastIndex);
		}
		return path;
	}

	/**
	 * 获取不带路径和后缀的文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameByPath(String path) {
		if (TextUtils.isEmpty(path)) {
			return "";
		}
		int separatorIndex = path.lastIndexOf(File.separator);
		if (separatorIndex > 0 && separatorIndex != path.length() - 1) {
			String fullName = path.substring(separatorIndex + 1, path.length());
			String name = getFileNameWithoutSuffix(fullName);
			return name;
		}
		return path;
	}
	
	/**
	 * 获取不带路径的文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFullFileNameByPath(String path) {
		if (TextUtils.isEmpty(path)) {
			return "";
		}
		
		int separatorIndex = path.lastIndexOf(File.separator);
		
		if (separatorIndex > 0 && separatorIndex != path.length() - 1) {
			String fullName = path.substring(separatorIndex + 1, path.length());
			return fullName;
		}
		
		return path;
	}

	// 文件改名
	public static boolean fileMove(String from, String to, boolean overwrite) {
		return fileMove(from, to, overwrite, false);
	}
	
	public static boolean fileMove(String from, String to, boolean overwrite, boolean copy) {
		File fromFile = new File(from);
		
		if (!fromFile.exists()) {
			return false;
		}
		
		File toFile = new File(to);
		if (toFile.exists()) {
			if (overwrite) {
				toFile.delete();
			} else {
				return false;
			}
		}
		
		boolean ret = false; 
		
		if (!copy) {
			ret = fromFile.renameTo(toFile);
		}
				
		if (!ret) {
			ret = fileCopy(fromFile, toFile);
			if (ret) {
				deleteFile(from);
			}
		}
		
		return ret;
	}

	// 过滤掉不可当文件名的字符
	static public String delInvalidFileNameStr(String title) {
		if (title != null && title.length() > 0) {
			String illegal = "[`\\\\~!@#\\$%\\^&\\*+=\\|\\{\\}:;\\,/\\.<>\\?·\\s\"]";
			Pattern pattern = Pattern.compile(illegal);
			Matcher matcher = pattern.matcher(title);
			return matcher.replaceAll("_").trim();
		}

		return title;
	}

	/**
	 * 创建空文件
	 * 
	 * @param path
	 *            待创建的文件路径
	 * @param size
	 *            空文件大小
	 * @return 创建是否成功
	 * @throws IOException
	 */
	public static boolean createEmptyFile(String path, long size) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(path, "rw");
			try {
				raf.setLength(size);
			} finally {
				raf.close();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 *            文件路径
	 * @return 是否存在
	 */
	public static boolean isExist(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		return file.exists();
	}

	// 拷贝文件
	public static boolean fileCopy(String from_file, String to_file) {
		return fileCopy(new File(from_file), new File(to_file));
	}

	/**
	 * 使用文件通道的方式复制文件
	 *
	 * @param s 源文件
	 * @param t 复制到的新文件
	 */

	public static void fileChannelCopy(File s, File t) {

		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;
		if (s.getAbsoluteFile().equals(t.getAbsoluteFile())) {
			return;
		}

		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();//得到对应的文件通道
			out = fo.getChannel();//得到对应的文件通道
			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (fi != null) {
					fi.close();
				}
				if (in != null) {
					in.close();
				}
				if (fo != null) {
					fo.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static boolean fileCopy(File from_file, File to_file) {
		if (!from_file.exists()) {
			return false;
		}

		if (to_file.exists()) {
			to_file.delete();
		}
		boolean success = true;
		FileInputStream from = null;
		FileOutputStream to = null;
		byte[] buffer;
		try {
			buffer = new byte[1024];
		} catch (OutOfMemoryError oom) {
			return false;
		}
		try {
			from = new FileInputStream(from_file);
			to = new FileOutputStream(to_file); // Create output stream
			int bytes_read;

			while ((bytes_read = from.read(buffer)) != -1) {
				// Read until EOF
				to.write(buffer, 0, bytes_read);
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			buffer = null;
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
				}
				from = null;
			}
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
				}
				to = null;
			}
		}

		if (!success) {
			to_file.delete();
		}

		return success;
	}

	/**
	 * 删除文件或者目录
	 * 
	 * @param path
	 *            指定路径的文件或目录
	 * @return 返回操作结果
	 */
	public static boolean deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return true;
		}
		File file = new File(path);
		if (!file.exists()) return true;

		if (file.isDirectory()) {
			String[] subPaths = file.list();
			if (subPaths != null){ //4.4系统无权限可能为空
				for (String p : subPaths) {
					if (!deleteFile(path + File.separator + p)) {
						return false;
					}
				}
			}else{
			}
		}

		return file.delete();
	}
	
	public static boolean deleteFilesExcept(String path, String fileName) {
		File[] files = getFiles(path);
		if(files != null) {
			for(File f : files) {
				if(!f.getAbsolutePath().endsWith(fileName)) {
					f.delete();
				}
			}
		}
		return true;
	}

	/**
	 * 创建目录，包括必要的父目录的创建，如果未创建
	 * 
	 * @param path
	 *            待创建的目录路径
	 * @return 返回操作结果
	 */
	public static boolean mkdir(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			return true;
		}

		return file.mkdirs();
	}
	
	public static long getRomTotalSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = null;
		try {
			stat = new StatFs(path.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return blockSize * totalBlocks;
	}
	  
	    /** 
	     * 获得机身可用内存 
	     *  
	     * @return 
	     */  
	public static long getRomAvailableSize() {  
		File path = Environment.getDataDirectory();
		StatFs stat = null;
		try {
			stat = new StatFs(path.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long availableSize = availableBlocks * blockSize;
		return availableSize - 5 * 1024 * 1024;// 预留5M的空间
	}  
	
	public static boolean isExternalStorageWriterable() {
		String state = null;
		
		try {
			state = Environment.getExternalStorageState();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	 
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    } 
	   return false;
	}
	    
	
	public static long getTotalExternalMemorySize() {
		String state = null;
		try {
			state = Environment.getExternalStorageState();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = null;
			try {
				stat = new StatFs(path.getPath());
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}

			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return blockSize*totalBlocks;
		}
		
		return 0;
	}
	
	/**
	 * 检查当前sdcard剩余空间大小
	 */
	public static long getAvailableExternalMemorySize() {
		String state = null;
		
		try {
			state = Environment.getExternalStorageState();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = null;
			try {
				stat = new StatFs(path.getPath());
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}

			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			long availableSize = availableBlocks * blockSize;
			return availableSize;
		}
		
		return 0;
	}

	/**
	 * 检查当前sdcard剩余空间大小是否够用，32M以上返回true
	 */
	public static boolean isExternalSpaceAvailable() {
		return getAvailableExternalMemorySize() > 32 * 1024 * 1024;
	}
	/**
	 * 检查当前sdcard剩余空间大小是否够用，100M以上返回true
	 * @param minSize 单位MB
	 */
	public static boolean isExternalSpaceAvailable(int minSize) {
		return getAvailableExternalMemorySize() > minSize * 1024 * 1024;
	}



	/**
	 * 获取当前目录的文件夹列表
	 */
	public static ArrayList<File> getDirs(String path) {
		ArrayList<File> fileList = new ArrayList<File>();
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						fileList.add(f);
					}
				}
			}
		}
		return fileList;
	}

	public static File[] getFiles(String path) {
		return getFiles(path, null);
	}

	/**
	 * 获取当前目录的文件列表
	 */
	public static File[] getFiles(String path, final String[] filters) {
		File file = new File(path);
		if (!file.isDirectory()) {
			return null;
		}

		FilenameFilter filter = null;
		if (filters != null && filters.length > 0) {
			filter = new FilenameFilter() {
				@Override
				public boolean accept(File directory, String filename) {
					if (!TextUtils.isEmpty(filename)) {
						String lowerCase = filename.toLowerCase();
						for (String type : filters) {
							if (lowerCase.endsWith(type)) {
								return true;
							}
						}
					}
					return false;
				}
			};
		}

		File[] fileList = file.listFiles(filter);
		return fileList;
	}

	/**
	 * 获取当前目录的文件列表，用正则匹配
	 */
	public static File[] getFilesByRegex(String path, final String regex,
										 final String exceptRegex) {
		if (TextUtils.isEmpty(path) || TextUtils.isEmpty(regex)) {
			return null;
		}
		File[] fileList = null;
		File file = new File(path);
		if (file.isDirectory()) {
			fileList = file.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File directory, String filename) {
					if (filename != null && !"".equals(filename)) {
						try {
							if (filename.matches(regex)) {
								if (exceptRegex == null || exceptRegex.length() == 0) {
									return true;
								} else {
									return !filename.matches(exceptRegex);
								}
							}
						} catch (Exception e) {
							return false;
						}
					}
					return false;
				}
			});
		}

		return fileList;
	}

	public static long getFileSize(final String path) {
		if (TextUtils.isEmpty(path)) {
			return 0;
		}
		File file = new File(path);
		if (!file.exists()) {
			return 0;
		}
		return file.length();
	}


	// 传统的＊和？匹配
	public static File[] getFilesClassic(final String dir, final String pattern) {
		if (TextUtils.isEmpty(dir) || TextUtils.isEmpty(pattern)) {
			return null;
		}
		StringBuilder builder = new StringBuilder("^");
		int state = 0;
		for (int i = 0; i < pattern.length(); i++) {
			char word = pattern.charAt(i);
			if (state == 0) {
				if (word == '?') {
					builder.append('.');
				} else if (word == '*') {
					builder.append(".*");
				} else {
					builder.append("\\Q");
					builder.append(word);
					state = 1;
				}
			} else {
				if (word == '?' || word == '*') {
					builder.append("\\E");
					state = 0;
					if (word == '?') {
						builder.append('.');
					} else {
						builder.append(".*");
					}
				} else {
					builder.append(word);
				}
			}
		}
		if (state == 1) {
			builder.append("\\E");
		}
		builder.append('$');
		ArrayList<File> list = null;
		try {
			Pattern p = Pattern.compile(builder.toString());
			list = filePattern(new File(dir), p);
		} catch (Exception e) {
			return null;
		}
		if (list == null || list.size() == 0) {
			return null;
		}
		File[] rtn = new File[list.size()];
		list.toArray(rtn);
		return rtn;
	}

	private static ArrayList<File> filePattern(File file, Pattern p) {
		if (file == null) {
			return null;
		} else if (file.isFile()) {
			Matcher fMatcher = p.matcher(file.getName());
			if (fMatcher.matches()) {
				ArrayList<File> list = new ArrayList<File>();
				list.add(file);
				return list;
			}
		}
		else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				ArrayList<File> list = new ArrayList<File>();
				for (File f : files) {
					if (p.matcher(f.getName()).matches()) {
						list.add(f);
					}
				}
				return list;
			}
		}
		return null;
	}
	
	// 李建衡：读文件。如果无法读出数据，就返回null
	public static String fileRead(String file) {
		if (TextUtils.isEmpty(file)) {
			return null;
		}
		
		byte[] buffer = fileRead(new File(file));
		
		if (buffer == null) {
			return null;
		}
		
		return new String(buffer);
	}
	
	public static String fileRead(String file, String charsetName) {
		if (TextUtils.isEmpty(file) || TextUtils.isEmpty(charsetName)) {
			return null;
		}
		
		byte[] buffer = fileRead(new File(file));
		
		if (buffer == null) {
			return null;
		}
		
		try {
			return new String(buffer, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] fileReadBytes(String file) {
		if (TextUtils.isEmpty(file)) {
			return null;
		}
		
		return fileRead(new File(file));
	}

	public static byte[] fileRead(File file) {
		byte[] buffer = null;
		
		if (!file.exists()) {
			return buffer;
		}
		
		FileInputStream fis;
				
		try {
			fis = new FileInputStream(file);
			try {
				int len = fis.available();
				buffer = new byte[len];
				fis.read(buffer);
			} finally {
				fis.close();
			}
		} catch (Throwable e) { // new byte有可能是OOM异常，要用Throwable跟IOException一起捕获
			e.printStackTrace();
			return null;
		}

		return buffer;
	}
	
	public static boolean fileWrite(String file, String data) {
		if (TextUtils.isEmpty(file) || data == null) {
			return false;
		}
		
		try {
			return fileWrite(new File(file), data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean fileWrite(String file, byte[] data) {
		if (TextUtils.isEmpty(file) || data == null) {
			return false;
		}
		
		try {
			return fileWrite(new File(file), data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	public static boolean fileWrite(File file, byte[] data) {
		FileOutputStream fos;
		
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			
			try {
				fos.write(data);
			} finally {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static boolean createFile(File file) {
		try {
			if (file != null) {
				if(file.exists()) {
					file.delete();
				}
				file.createNewFile();
				return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 对目录进行统一化处理。
	 * 这里很乱，
	 * 三星手机的/sdcard0/与emulated/0是同一目录
	 * 在华为上，/sdcard又是真实的，没有emulated
	 * @param oldPath
	 * @return
	 */
	public static String unifyMusicFilePath(String oldPath){
		String realPath = oldPath;
		if(oldPath.contains("/sdcard")){
			realPath = oldPath.replace("/sdcard", "/emulated/"); //此处把用户改为sdcard这种链接目录的路径，更换为真实目录emulated目录结构。
			Log.d("DirectoryScanner", "unifyMusicFilePath-->oldPath:"+oldPath+",newPath"+realPath);
			if(!isExist(realPath)){ //有些手机上，没有emulated存在，则保持原来路径
				realPath = oldPath;
			}
		}
		if(false==noLegacyFlag&&oldPath.contains("/legacy/")){
			realPath = oldPath.replace("/legacy/", "/0/"); //特别情况下，需要把legacy目录转回为/0目录。主用户目录，都是android支持多用户闹的
			if(!isExist(realPath)){ //有些手机可能没有对应目录，则保持原状态
				realPath = oldPath;
				noLegacyFlag=true;
			}
		}
		return realPath;
	}

	public static boolean fileWrite(String fileName, byte[] data, boolean append) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(fileName, append);
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}
	private static boolean noLegacyFlag=false;//用于提高上面unifyMusicFilePath的执行性能。这个函数调用比较多
}
