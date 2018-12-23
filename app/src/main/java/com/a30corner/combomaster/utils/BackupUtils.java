package com.a30corner.combomaster.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.os.Environment;

import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.provider.vo.MonsterDO2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BackupUtils {

    public interface IResultCallback {
        public void onComplete(String output);
        
        public void onFail(Exception e);
    }
    
    public static void restore(Context context, String filename, IResultCallback callback) {
        String restoreSession = UUID.randomUUID().toString();
        File restorePath = new File(context.getCacheDir(), restoreSession);
        File path = new File(StorageUtil.getCustomDirectory(context)); 
        File zipFile = new File(path, filename);
        if ( !zipFile.exists() ) {
        	path = new File(Environment.getExternalStorageDirectory(), "ComboMaster");
        	zipFile = new File(path, filename);
        }
        
        ZipUtils.unpackZip(context, zipFile, restorePath.getAbsolutePath());

        File box = new File(restorePath, "box");
        File[] boxList = box.listFiles();
        if ( boxList != null ) {
            Gson gson = new Gson();
            Type type = new TypeToken<MonsterDO>() {}.getType();
            byte[] buffer = new byte[512];
            boolean tryOldVersion = false;
            
            for(File item : boxList) {
                FileInputStream fis = null;
                try {
                    StringBuilder sb = new StringBuilder();
                    fis = new FileInputStream(item);
                    int read;
                    while((read=fis.read(buffer, 0, 512)) != -1) {
                        sb.append(new String(buffer, 0, read));
                    }
                    MonsterDO mdo = gson.fromJson(sb.toString(), type);
                    LocalDBHelper.addMonster(context, mdo);
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                    tryOldVersion = true;
                    break;
                } finally {
                    EasyUtil.close(fis);
                }
                item.delete();
            }
            
            if ( tryOldVersion ) {
            	Type type2 = new TypeToken<MonsterDO2>() {}.getType();
                for(File item : boxList) {
                    FileInputStream fis = null;
                    try {
                        StringBuilder sb = new StringBuilder();
                        fis = new FileInputStream(item);
                        int read;
                        while((read=fis.read(buffer, 0, 512)) != -1) {
                            sb.append(new String(buffer, 0, read));
                        }
                        MonsterDO2 mdo = gson.fromJson(sb.toString(), type2);
                        LocalDBHelper.addMonster(context, mdo);
                    } catch (Exception e) {
                        LogUtil.e(e.toString());
                    } finally {
                        EasyUtil.close(fis);
                    }
                    item.delete();
                }
            }
        }
        box.delete();
        
        File images = new File(restorePath, "images");
        File[] imageList = images.listFiles();
        if ( imageList != null ) {
            for(File image : imageList) {
                File newPath = new File(context.getFilesDir(), image.getName());
                if ( !newPath.exists() ) {
                    image.renameTo(newPath);
                }
            }
        }
        images.delete();
        
        File shared = new File(restorePath, "shared_prefs");
        File[] sharedList = shared.listFiles();
        if ( sharedList != null ) {
            for(File f : sharedList) {
                File sharedPath = new File(context.getFilesDir().getParentFile(), "shared_prefs");
                File newName = new File(sharedPath, f.getName());
                if ( newName.exists() ) {
                    newName.delete();
                }
                f.renameTo(newName);
            }
        }
        shared.delete();
        
        restorePath.delete();
        callback.onComplete(filename);
    }
    
    public static boolean isFileExists(String path, String filename) {
        String filenameWithZip = filename;
        if ( !filenameWithZip.endsWith(".zip") ) {
            filenameWithZip = filename + ".zip";
        }
        if ( filenameWithZip.contains(" ") ) {
            filenameWithZip = filenameWithZip.replaceAll(" ", "_");
        }
        return new File(path, filenameWithZip).exists();
    }
    
    public static void backup(Context context, String filename, IResultCallback callback) {
        backup(context, filename, callback, false);
    }
    
    public static void backup(Context context, String filename, IResultCallback callback, boolean override) {
        String filenameWithZip = filename;
        if ( !filenameWithZip.endsWith(".zip") ) {
            filenameWithZip = filename + ".zip";
        }
        if ( filenameWithZip.contains(" ") ) {
            filenameWithZip = filenameWithZip.replaceAll(" ", "_");
        }
        
        File path = new File(StorageUtil.getCustomDirectory(context));
        if ( !path.exists() ) {
            path.mkdirs();
        }
        File output = new File(path, filenameWithZip);
        if ( output.exists() ) {
            if ( override ) {
                output.delete();
            } else {
                // change file name...
                int dot = filenameWithZip.lastIndexOf(".");
                String newname = filenameWithZip.substring(0, dot);
                int i;
                for(i=1; i<=20; ++i) { // only check 20 times
                    File f = new File(path, newname + "-" + i + ".zip");
                    if ( !f.exists() ) {
                        output = f;
                        break;
                    }
                }
                if ( i == 20 ) {
                    if ( callback != null ) {
                        callback.onFail(new IOException("file exists"));
                    }
                    return ;
                }
            }
        }
        
        ZipOutputStream os = null;
        byte[] readBuffer = new byte[512];
        try {
            File fileDir = context.getFilesDir();
            os = new ZipOutputStream(new FileOutputStream(output));
            List<MonsterDO> list = LocalDBHelper.getMonsterDO(context);
            Type type = new TypeToken<MonsterDO>() {}.getType();
            Gson gson = new Gson();
            for(MonsterDO dom : list) {
                if ( dom.no == -1 ) {
                    continue;
                }
                String data = gson.toJson(dom, type);
                
                // put monster box data
                ZipEntry entry = new ZipEntry("box/" + dom.index);
                try {
                    os.putNextEntry(entry);
                    
                    byte[] buffer = data.getBytes();
                    os.write(buffer, 0, buffer.length);
                } catch (IOException e) {
                }
                
                // put images
                String imageFile = String.format("%di.png", dom.no);
                File file = new File(fileDir, imageFile);
                if ( file.exists() ) {
                    FileInputStream fis = null;
                    ZipEntry image = new ZipEntry("images/" + imageFile);
                    try {
                        os.putNextEntry(image);
                        
                        fis = new FileInputStream(file);
                        int read;
                        while((read = fis.read(readBuffer, 0, 512)) != -1) {
                            os.write(readBuffer, 0, read);
                        }
                        
                    } catch(IOException e) {
                    } finally {
                        EasyUtil.close(fis);
                    }
                }
                // put team info
                File datadata = context.getFilesDir().getParentFile();
                File shared = new File(datadata, "shared_prefs");
                File[] listXml = shared.listFiles(new FileFilter() {
                    
                    @Override
                    public boolean accept(File pathname) {
                        String name = pathname.getName();
                        return "combomaster.xml".equalsIgnoreCase(name) ||
                                "team.xml".equalsIgnoreCase(name);
                    }
                });
                if ( listXml != null ) {
                    for(File f : listXml) {
                        FileInputStream fis = null;
                        try {
                            ZipEntry sp = new ZipEntry("shared_prefs/" + f.getName());
                            os.putNextEntry(sp);
                            
                            fis = new FileInputStream(f);
                            int read;
                            while((read = fis.read(readBuffer, 0, 512)) != -1) {
                                os.write(readBuffer, 0, read);
                            }
                        } catch (IOException e) {
                        } finally {
                            EasyUtil.close(fis);
                        }
                    }
                }
            }
            
            
            if ( callback != null ) {
                callback.onComplete(output.getName());
            }
        } catch (FileNotFoundException e) {
            LogUtil.e("backup", e.getMessage(), e);
            if ( callback != null ) {
                callback.onFail(e);
            }
        } finally{
            EasyUtil.close(os);
        }
    }
}
