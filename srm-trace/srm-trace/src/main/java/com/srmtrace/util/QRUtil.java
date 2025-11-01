package com.srmtrace.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class QRUtil {
    public static String createQRCode(String text, String outFileName) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bm = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);
            File out = new File("data/qrcodes");
            out.mkdirs();
            Path p = new File(out, outFileName).toPath();
            MatrixToImageWriter.writeToPath(bm, "PNG", p);
            return p.toString();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
