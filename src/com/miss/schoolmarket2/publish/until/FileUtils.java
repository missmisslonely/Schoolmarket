package com.miss.schoolmarket2.publish.until;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/formats/";
	public static String SDPATH1 = Environment.getExternalStorageDirectory()
			+ "/myimages/";

	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String savePic(String picPath, String goodsId) {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/SchoolMarket2/";// 获取手机本地存//储路径，这个位置可以根据需要自己改。
		Bitmap bm = BitmapFactory.decodeFile(picPath);

		File file = new File(path + goodsId + ".png");// 创建文件
		if (!file.exists()) {
			try {// 如果文件不存在就创建文件，写入图片
				file.createNewFile();
				FileOutputStream fOut = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.PNG, 60, fOut);
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return path+goodsId+".png";
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(path); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
