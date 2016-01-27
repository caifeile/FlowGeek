package org.thanatos.base.model;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by thanatos on 15-9-25.
 * ####
 */
public class CacheManager {

    /**
     * 保存缓存
     * @param context context
     * @param object 缓存对象
     * @param fn 文件名
     */
    public static void saveObject(Context context, Serializable object, String fn){
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fn, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e1) {
                    //pass
                }
            }
            if (oos!=null){
                try {
                    oos.close();
                } catch (IOException e1) {
                    //pass
                }
            }
        }
    }

    /**
     * 读取缓存
     * @param context context
     * @param fn 文件名
     * @return
     */
    public static <T> T readObject(Context context, String fn){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = context.openFileInput(fn);
            ois = new ObjectInputStream(fis);
            return (T) ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            File file = context.getFileStreamPath(fn);
            if (file!=null){
                file.delete();
            }
            return null;
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e1) {
                    //pass
                }
            }
            if (ois!=null){
                try {
                    ois.close();
                } catch (IOException e1) {
                    //pass
                }
            }
        }
    }

    /**
     * 缓存是否存在
     * @param context context
     * @param fn 文件名
     * @return
     */
    public static boolean isExist4DataCache(Context context, String fn){
        if (fn == null)
            return false;
        return context.getFileStreamPath(fn).exists();
    }




}
