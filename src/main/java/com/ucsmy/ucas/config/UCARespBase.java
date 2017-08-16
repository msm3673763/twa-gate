package com.ucsmy.ucas.config;

/**
 * @author ucs_fucong
 * @date 2017/08/03
 */
public class UCARespBase<T> {
    private Integer res;
    private String des;
    private T data;

    public Integer getRes() {
        return res;
    }

    public void setRes(Integer res) {
        this.res = res;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UCARespBase{" +
            "res=" + res +
            ", des='" + des + '\'' +
            ", data=" + data +
            '}';
    }

    public boolean isSuccess() {
        return res != null && res == 1;
    }
}
