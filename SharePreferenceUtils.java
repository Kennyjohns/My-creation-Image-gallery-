package imgconvertt.compressimg.convrtformat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {

    private SharedPreferences mPreferences;
    private Context context;

    public SharePreferenceUtils(Context mContext) {
        context=mContext;
        mPreferences = mContext.getSharedPreferences("ImageToPdf", Context.MODE_PRIVATE);
    }

    private String isExists() {
        return context.getFilesDir().toString();
    }

    public Boolean contains(String Key) {
        return mPreferences.contains(Key);
    }

    public String getString(String Key, String DefaultValue) {
        String value;
        value = mPreferences.getString(Key, DefaultValue);
        return value;
    }

    public void putInt(String Key, int value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(Key, value);
        apply(editor);
    }

    public int getInt(String Key, int DefaultValue) {
        int value;
        value = mPreferences.getInt(Key, DefaultValue);
        return value;
    }

    public void putString(String Key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Key, value);
        apply(editor);
    }

    public Long getLong(String Key, Long DefaultValue) {
        Long value;
        value = mPreferences.getLong(Key, DefaultValue);
        return value;
    }

    public void putLong(String Key, Long value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(Key, value);
        apply(editor);
    }

    public boolean getBoolean(String Key, Boolean DefaultValue) {
        boolean value;
        value = mPreferences.getBoolean(Key, DefaultValue);
        return value;
    }

    public void putBoolean(String Key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(Key, value);
        apply(editor);
    }

    private void apply(SharedPreferences.Editor editor) {
        editor.commit();
        editor.apply();
    }

}
