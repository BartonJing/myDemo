package com.barton.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import static java.lang.System.out;

/**
 * pdf 转图片
 */
public class PdfImage {
    public static void main(String [] args){
        // 待解析PDF
        File file = new File("/home/barton/Desktop/11.pdf");

        //将pdf装图片 并且自定义图片得格式大小

        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for(int i=0;i<pageCount;i++){
                BufferedImage image = renderer.renderImageWithDPI(i, 96); // Windows native DPI
                //BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                String aa = "/home/barton/Desktop/aa.png";
                ImageIO.write(image, "PNG", new File(aa));
                System.out.println(isSimpleColorImg(aa));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否为纯色
     * @param imgPath 图片源
     * @return
     * @throws IOException
     */
    public static boolean isSimpleColorImg(String imgPath) throws IOException {

        BufferedImage src = ImageIO.read(new File(imgPath));
        int height = src.getHeight();
        int width = src.getWidth();
        int[] rgb = new int[4];
        int o = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = src.getRGB(i, j);
                rgb[1] = (pixel & 0xff0000) >> 16;
                rgb[2] = (pixel & 0xff00) >> 8;
                rgb[3] = (pixel & 0xff);
                //如果像素点不相等的数量超过50个 就判断为彩色图片
                if (rgb[1] != rgb[2] && rgb[2] != rgb[3] && rgb[3] != rgb[1]) {
                    o += 1;
                    if (o >= 50) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int type=source.getType();
        BufferedImage target=null;
        double sx=(double)targetW/source.getWidth();
        double sy=(double)targetH/source.getHeight();
        if(sx>sy){
            sx=sy;
            targetW=(int)(sx*source.getWidth());
        }else{
            sy=sx;
            targetH=(int)(sy*source.getHeight());
        }
        if(type==BufferedImage.TYPE_CUSTOM){
            ColorModel cm=source.getColorModel();
            WritableRaster raster=cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied=cm.isAlphaPremultiplied();
            target=new BufferedImage(cm,raster,alphaPremultiplied,null);
        }else{
            target=new BufferedImage(targetW, targetH,type);
        }
        Graphics2D g=target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }
}
