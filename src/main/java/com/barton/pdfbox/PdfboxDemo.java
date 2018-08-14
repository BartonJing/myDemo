package com.barton.pdfbox;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    public static void main(String[] args) throws Exception{
        readPDf();
    }
}
