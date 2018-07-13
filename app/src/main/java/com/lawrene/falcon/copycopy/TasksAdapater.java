package com.lawrene.falcon.copycopy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by lawrene on 6/13/18.
 */

public class TasksAdapater extends ArrayAdapter<MyTasks> {

    public TasksAdapater(@NonNull Context context, @NonNull List<MyTasks> objects) {
        super(context, 0, objects);
    }


}
