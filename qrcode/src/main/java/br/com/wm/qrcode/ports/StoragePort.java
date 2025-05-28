package br.com.wm.qrcode.ports;

public interface StoragePort {
    String uploadFile(byte[] fileData, String fileName, String contentType);
}
