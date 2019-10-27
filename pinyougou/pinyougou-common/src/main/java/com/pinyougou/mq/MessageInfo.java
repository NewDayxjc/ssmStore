package com.pinyougou.mq;

import java.io.Serializable;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/1 12:00
 */
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 6968936926769239855L;

    public static  final  int METHOD_ADD=1;
    public static final int METHOD_UPDATE=2;
    public static  final int METHOD_DELETE=3;

    //操作方式  1|增加 2|修改 3|删除
    private int method;
    //要发送的内容
    private Object context;

    public MessageInfo() {
    }

    public MessageInfo(Object context) {
        this.context = context;
    }

    public MessageInfo(int method, Object context) {
        this.method = method;
        this.context = context;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }



    @Override
    public String toString() {
        return "MessageInfo{" +
                "method=" + method +
                ", context=" + context +
                '}';
    }
}
