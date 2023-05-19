package imgconvertt.compressimg.convrtformat.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import imgconvertt.compressimg.convrtformat.model.MyPdfModel;

public class Utils {

    public static final int MAXSELECTIONCOUNT = 25;
    public static ArrayList<MyPdfModel> arraylistImageStaticForPDF = new ArrayList<>();

    public static void sortCreation(List<MyPdfModel> data) {
        ArrayList<MyPdfModel> temp = new ArrayList<>();
        Collections.sort(data, new Comparator<MyPdfModel>() {
            @Override
            public int compare(MyPdfModel s, MyPdfModel t1) {
                return Long.compare(new File(s.getPdfPath()).lastModified(), new File(t1.getPdfPath()).lastModified());
            }
        });
        Collections.reverse(data);
    }

    public static void sortFolderName(ArrayList<String> data) {
        ArrayList<String> temp = new ArrayList<>();
        Collections.sort(data, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return new File(s).getName().toUpperCase().compareTo(new File(t1).getName().toUpperCase());
            }
        });

    }

    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}
