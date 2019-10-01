package com.jieniuwuliu.jieniu.listener;

public interface OnDownloadListener {
    void onDownloadConnect(int fileSize);
    void onDownloadUpdate(int position);
    void onDownloadComplete(String url);
    void onDownloadError(Exception e);
}
