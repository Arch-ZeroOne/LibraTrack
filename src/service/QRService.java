/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

/**
 *
 * @author Windyl
 */
public class QRService {
    
    
    //Still need to review this
    public Path generateQR(String text,String filepath) throws WriterException,IOException{
        //Sets the width and height of the barcode
        int width = 300;
        int height = 300;
        
        
      
        
        //Sets the type of qr code text 
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        //Creates the QR pattern
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        
        //Where to save the picture to
        Path path = FileSystems.getDefault().getPath(filepath);
        
        System.out.println("Generated Path");
        
        System.out.println(path);
        
        //Writes qr code into an image
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        
        return path;
        
    }
    
}
