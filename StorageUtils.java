package imgconvertt.compressimg.convrtformat.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StorageUtils {
    private Context kContext;

    public StorageUtils(Context context) {
        this.kContext = context;

    }

    private File getStorageDir(String directoryName) {
        File file = new File(Environment.getExternalStorageDirectory(), directoryName);

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File storeToDirectory(String directoryName, String fileName) {

        File photo = new File(getStorageDir30(directoryName), fileName);
        return photo;

    }
    private File getStorageDir30(String directoryName) {
        File file=null;
        try {

            int position=0;
            for(position=0;position<kContext.getExternalMediaDirs().length;position++)
            {
                if(kContext.getExternalMediaDirs()[position].getAbsolutePath().startsWith("/storage/emulated/0/Android/media/"))
                {
                    break;
                }

            }
            if(position!=kContext.getExternalMediaDirs().length)
            {
                file=new File(kContext.getExternalMediaDirs()[position],directoryName);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        }
        catch (Exception e)
        {
            return file;
        }
    }


    private File getPackageStorageDir(String folderName) {
        try {
            File file = new File(kContext.getFilesDir() + "/", folderName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public File storeToPackageDirectory(String directoryName, String fileName) {
        try {
            File photo = new File(getPackageStorageDir(directoryName), fileName);
            return photo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean fileDelete(String fileName) {
        File file = new File(fileName);

        return file.delete();
    }
    public void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[] {canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }
    public File getCreateStorageDirectoryPath(String directoryName) {
        return getStorageDir30(directoryName);
    }

    public File getCreatePackageStorageDirectoryPath(String directoryName) {
        return getPackageStorageDir(directoryName);
    }

    public void copyAssetsFileToPackageFile(String assetPath, String directoryName, String fileName) {
        try {
            InputStream ims = this.kContext.getAssets().open(assetPath);
            FileOutputStream fileOutputStream = new FileOutputStream(storeToPackageDirectory(directoryName, fileName));
            copyFile(ims, fileOutputStream);
            ims.close();
            ims = null;
            fileOutputStream.flush();
            fileOutputStream.close();
            fileOutputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
//            LogUtils.loge(TAG, "getBitmapFromView Bitmap is null");
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public void saveBitmap(Bitmap bitmap, File file) throws Exception {
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
    }

    public void copyFile(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {

        }
    }

    public Bitmap viewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
