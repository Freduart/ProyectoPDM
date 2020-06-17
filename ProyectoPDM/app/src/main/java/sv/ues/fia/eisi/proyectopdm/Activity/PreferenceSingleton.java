package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceSingleton {
    private static PreferenceSingleton mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private PreferenceSingleton(){ }

    public static PreferenceSingleton getInstance(){
        if (mInstance == null) mInstance = new PreferenceSingleton();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void writePreference(String key, String value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.apply();
    }

    public String readPreference(String key){
        return mMyPreferences.getString(key,"");
    }
}
