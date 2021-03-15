package com.bestpearlstudio.diwaliphotoframe;

/**
 * Created by PARTH DESAI on 3/11/2017.
 */
public class diwali_FrameModel
{

    int thumbId, FrmId;

    public String id;
    public String message;


    public diwali_FrameModel(int thumbId, int frmId) {
        this.thumbId = thumbId;
        FrmId = frmId;
    }

    public int getThumbId() {
        return thumbId;
    }

    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }

    public int getFrmId() {
        return FrmId;
    }

    public void setFrmId(int frmId) {
        FrmId = frmId;
    }
    public diwali_FrameModel(String id, String message) {
        this.id = id;
        this.message = message;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
