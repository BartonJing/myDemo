package com.barton.pdfbox;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static java.lang.System.out;

/**
 * create by barton on 2018-8-14
 */
public class PdfboxDemo {
    public static void readPDf() throws Exception{
        // 待解析PDF
        File pdfFile = new File("D:\\in.pdf");
        // 空白PDF
        File pdfFile_out = new File("D:\\out.pdf");

        PDDocument document = null;
        PDDocument document_out = null;
        try {
            document = PDDocument.load(pdfFile);
            document_out = PDDocument.load(pdfFile_out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int pages_size = document.getNumberOfPages();

        out.println("getAllPages===============" + pages_size);
        int j = 0;

        for (int i = 0; i < pages_size; i++) {
            // 文本内容
            PDFTextStripper stripper = new PDFTextStripper();
            // 设置按顺序输出
            stripper.setSortByPosition(true);
            stripper.setStartPage(i + 1);
            stripper.setEndPage(i + 1);
            String text = stripper.getText(document);
            out.println(text.trim());
            out.println("-=-=-=-=-=-=-=-=-=-=-=-=-");

            //图片内容
            PDPage page = document.getPage(i);
            PDPage page1 = document_out.getPage(0);
            PDResources resources = page.getResources();
            Iterable xobjects = resources.getXObjectNames();
            if (xobjects != null) {
                Iterator imageIter = xobjects.iterator();
                while (imageIter.hasNext()) {
                    COSName key = (COSName) imageIter.next();
                    if (resources.isImageXObject(key)) {
                        try {
                            PDImageXObject pdImage = (PDImageXObject) resources.getXObject(key);

                            // 方式一：将PDF文档中的图片 分别存到一个空白PDF中。
                            /*PDPageContentStream contentStream = new PDPageContentStream(document_out, page1, PDPageContentStream.AppendMode.APPEND,
                                    true);

                            float scale = 1f;
                            contentStream.drawImage(image, 20, 20, image.getWidth() * scale, image.getHeight() * scale);
                            contentStream.close();
                            document_out.save("D:\\" + j + ".pdf");

                            System.out.println(image.getSuffix() + "," + image.getHeight() + "," + image.getWidth());*/


                            // 方式二：将PDF文档中的图片 分别另存为图片。
                            File file = new File("d:\\"+j+".png");
                            FileOutputStream out = new FileOutputStream(file);
                            BufferedImage image = pdImage.getImage();
                            ImageIO.write(image, "png", out);
                            out.close();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        j++;
                    }
                }
            }
        }
        out.println(j);
    }
    public static java.util.List<String> getPdfImage(String pdf,String imageType) throws Exception{
        // 待解析PDF
        //File pdfFile = new File("/home/barton/Desktop/htmlToPdfPreview.pdf");
        File pdfFile = new File(pdf);
        PDDocument document = null;
        try {
            document = PDDocument.load(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int pages_size = document.getNumberOfPages();

        int j = 0;
        java.util.List<String> images = new java.util.ArrayList<String>();
        for (int i = 0; i < pages_size; i++) {
            //图片内容
            PDPage page = document.getPage(i);
            PDResources resources = page.getResources();
            Iterable xobjects = resources.getXObjectNames();
            if (xobjects != null) {
                Iterator imageIter = xobjects.iterator();
                while (imageIter.hasNext()) {
                    COSName key = (COSName) imageIter.next();
                    if (resources.isImageXObject(key)) {
                        ByteArrayOutputStream os = null;
                        try {
                            PDImageXObject pdImage = (PDImageXObject) resources.getXObject(key);

                            /*//将PDF文档中的图片 分别另存为图片。
                            File file = new File("/home/barton/Desktop/"+j+".png");
                            FileOutputStream out = new FileOutputStream(file);
                            BufferedImage image = pdImage.getImage();
                            ImageIO.write(image, "png", out);
                            out.close();*/

                            //返回图片base64字符串
                            BufferedImage image = pdImage.getImage();
                            os = new ByteArrayOutputStream();
                            ImageIO.write(image, imageType, os);
                            BASE64Encoder encoder = new BASE64Encoder();
                            String base64 = encoder.encode(os.toByteArray());
                            //此处判断生成的字符是否大于300 （有些pdf存在空的图片，会有小于300的字符串）
                            /*if(StringUtils.isNotEmpty(base64) && base64.length() > 300){
                                images.add(base64);
                            }*/

                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            if(os != null){
                                os.close();
                            }
                            os.close();
                        }
                        j++;
                    }
                }
            }
        }
        return images;
    }

    public static void main(String[] args) throws Exception{
        //getPdfImage();
        String ss="iVBORw0KGgoAAAANSUhEUgAAALEAAAC3CAYAAABdcL/YAAAAlElEQVR42u3BAQEAAACCIP+vbkhA\n" +
                "AQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAAAADAhQH64gABzovj5wAAAABJRU5ErkJggg==";
        out.println(ss.length());
    }
}
