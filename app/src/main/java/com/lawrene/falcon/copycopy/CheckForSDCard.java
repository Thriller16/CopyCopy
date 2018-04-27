package com.lawrene.falcon.copycopy;

import android.os.Environment;

/**
 * Created by lawrene on 4/27/18.
 */

class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
