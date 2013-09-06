package org.nupter.nupter.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-9-5
 * Time: 下午6:16
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {
    private String SDCardRoot;

    public FileUtils() {
        SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    public File createFileInSDCard(String dir, String fileName)
            throws IOException {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dir) {
        File dirFile = new File(SDCardRoot + dir + File.separator);
        dirFile.mkdirs();
        return dirFile;
    }

    public boolean isFileExist(String path, String fileName) {
        File file = new File(path + fileName);
        return file.exists();
    }
    public ArrayList<String> readFileName(String path){
        ArrayList<String> arrayList=new ArrayList<String>();
        File file = new File(SDCardRoot + path + File.separator);
        if(file.isDirectory()){
            File [] fileArray = file.listFiles();
            if(null != fileArray && 0 != fileArray.length){
                for (int i=0;i<fileArray.length;i++){
                    if(fileArray[i].toString().endsWith(".png")){
                         arrayList.add(fileArray[i].toString());
                    }
                }
                return arrayList;
            }
        }
        return arrayList;
    }
    public Boolean write2SDFromBitmap(String path, String fileName, Bitmap bitmap) {
        if (bitmap != null) {
            createSDDir(path);
            if (!isFileExist(path, fileName)) {
                try {
                    File file = createFileInSDCard(path, fileName);
                    FileOutputStream out = new FileOutputStream(file);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                        out.flush();
                        out.close();
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}

