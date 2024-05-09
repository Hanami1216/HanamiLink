package com.hanamilink.ui.ble;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BleManagerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BleManagerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ble fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
