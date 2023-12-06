package com.newmysmileQR.APIManager;


import com.newmysmileQR.APIModel.RootModel;

public interface ResponseCallback {

    void onResponse(RootModel response);
    void onFailure(Throwable t);
}
