package imgconvertt.compressimg.convrtformat.utils;


import android.content.Context;
import android.os.Environment;

import java.io.File;


public class MediaFolderStorageUtils
{

    Context context;

    public MediaFolderStorageUtils(Context context)
    {
        this.context = context;
    }

    /**
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     android:maxSdkVersion="29"/>
     */

    /**
     *
     * @param directoryName Name of directory,
     * @param fileName  Name of file,
     * @param mediaType (Environment.DIRECTORY_PICTURES,
     *      *                  Environment.DIRECTORY_MOVIES,
     *      *                  Environment.DIRECTORY_DOWNLOADS,
     *      *                  Environment.DIRECTORY_DOCUMENTS,
     *      *                  Environment.DIRECTORY_MUSIC)
     * @return
     */
    public File storeToDirectory(String directoryName,String fileName,String mediaType) {
        return new File(getStorageDir(directoryName,mediaType), fileName);
    }
    /**
     * @param directoryName Name of directory,
     * @param mediaType (Environment.DIRECTORY_PICTURES,
     *                  Environment.DIRECTORY_MOVIES,
     *                  Environment.DIRECTORY_DOWNLOADS,
     *                  Environment.DIRECTORY_DOCUMENTS,
     *                  Environment.DIRECTORY_MUSIC)
     * @return
     */
    public File getStorageDir(String directoryName,String mediaType) {
        File file = new File(Environment.getExternalStoragePublicDirectory(mediaType), directoryName);

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
