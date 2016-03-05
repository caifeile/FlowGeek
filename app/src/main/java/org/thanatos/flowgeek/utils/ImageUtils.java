package org.thanatos.flowgeek.utils;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import rx.functions.Action1;

/**
 * Created by thanatos on 3/4/16.
 */
public class ImageUtils {


    /**
     * 通过uri获取文件的绝对路径
     * @param uri
     * @return
     */
    public static void getImageAbsolutePath(Activity context, Uri uri, Action1<String> action) {
        context.getLoaderManager().initLoader(110, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(context, uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data == null) return;
                if (data.getCount() > 0 && data.moveToFirst()) {
                    String path = data.getString(0);
                    action.call(path);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }


}
