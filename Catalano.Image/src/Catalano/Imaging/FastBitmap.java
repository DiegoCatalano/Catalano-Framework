// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Imaging;

import Catalano.Core.IntPoint;
import Catalano.Imaging.Filters.Grayscale;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Class to handle image.
 * @author Diego Catalano
 */
public class FastBitmap {
    
    private BufferedImage bufferedImage;
    private WritableRaster raster;
    private int[] pixels;
    private byte[] pixelsGRAY;

    /**
     * Color space.
     */
    public static enum ColorSpace {

        /**
         * Grayscale
         */
        Grayscale,
        /**
         * RGB
         */
        RGB,
        
        /**
         * ARGB
         */
        ARGB
    };

    /**
     * Initialize a new instance of the FastBitmap class.
     */
    public FastBitmap() {}
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param fastBitmap FastBitmap
     */
    public FastBitmap(FastBitmap fastBitmap){
        this.bufferedImage = fastBitmap.toBufferedImage();
        if (getType() == BufferedImage.TYPE_3BYTE_BGR) {
            toRGB();
        }
        refresh();
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param bufferedImage Buffered image.
     */
    public FastBitmap(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        if (getType() == BufferedImage.TYPE_3BYTE_BGR) {
            toRGB();
        }
        refresh();
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Image.
     */
    public FastBitmap(Image image) {
        bufferedImage = (BufferedImage)image;
        if (getType() == BufferedImage.TYPE_3BYTE_BGR) {
            toRGB();
        }
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param ico Ico.
     */
    public FastBitmap(ImageIcon ico){
        bufferedImage = (BufferedImage)ico.getImage();
        if (getType() == BufferedImage.TYPE_3BYTE_BGR) {
            toRGB();
        }
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param pathname The path that indicate a image.
     */
    public FastBitmap(String pathname){
        try {
            this.bufferedImage = ImageIO.read(new File(pathname));
            if (getType() == BufferedImage.TYPE_BYTE_GRAY) {
                refresh();
            }
            else if (getType() == BufferedImage.TYPE_INT_ARGB || getType() == BufferedImage.TYPE_4BYTE_ABGR){
                toARGB();
            }
            else{
                toRGB();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param width Width.
     * @param height Height.
     */
    public FastBitmap(int width, int height){
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param width Width.
     * @param height Height.
     * @param colorSpace Space color.
     */
    public FastBitmap(int width, int height, ColorSpace colorSpace){
        if (colorSpace == ColorSpace.RGB){
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        else if(colorSpace == ColorSpace.Grayscale){
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        else if(colorSpace == ColorSpace.RGB){
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Array.
     */
    public FastBitmap(int[][] image){
        bufferedImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_BYTE_GRAY);
        refresh();
        arrayToImage(image);
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Array.
     */
    public FastBitmap(int[][][] image){
        bufferedImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        refresh();
        arrayToImage(image);
    }
    
    /**
     * Refresh raster and get data buffer from raster.
     */
    private void refresh(){
        this.raster = getRaster();
        if (isGrayscale()) {
            pixelsGRAY = ((DataBufferByte)raster.getDataBuffer()).getData();
        }
        if (isRGB() || isARGB()) {
            pixels = ((DataBufferInt)raster.getDataBuffer()).getData();
        }
    }
    
    /**
     * Get Space color.
     * @return Space color.
     */
    public ColorSpace getColorSpace(){
        ColorSpace space = ColorSpace.RGB;
        if (getType() == BufferedImage.TYPE_BYTE_GRAY) {
            space = ColorSpace.Grayscale;
        }
        return space;
    }
    
    /**
     * Set image to FastBitmap.
     * @param bufferedImage BufferedImage.
     */
    public void setImage(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
        refresh();
    }
    
    /**
     * Set Image to Fast Bitmap.
     * @param fastBitmap FastBitmap.
     */
    public void setImage(FastBitmap fastBitmap){
        this.bufferedImage = fastBitmap.toBufferedImage();
        refresh();
    }
    
    /**
     * Convert FastBitmap to BufferedImage.
     * @return Buffered Image.
     */
    public BufferedImage toBufferedImage(){
        //return this.bufferedImage;
        BufferedImage b = new BufferedImage(getWidth(), getHeight(), getType());
        Graphics g = b.getGraphics();
        g.drawImage(this.bufferedImage, 0, 0, null);
        return b;
    }
    
    /**
     * Convert FastBitmap to Image.
     * @return Image.
     */
    public Image toImage(){
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }
    
    /**
     * Convert FastBitmap to Icon.
     * @return Icon.
     */
    public ImageIcon toIcon(){
        BufferedImage b = new BufferedImage(getWidth(), getHeight(), getType());
        Graphics g = b.getGraphics();
        g.drawImage(this.bufferedImage, 0, 0, null);
        ImageIcon ico = new ImageIcon(b);
        return ico;
    }
    
    /**
     * Convert any others space color to Grayscale.
     */
    public void toGrayscale(){
        new Grayscale().applyInPlace(this);
    }
    
    /**
     * Convert any others space colors to RGB.
     */
    public void toARGB(){
        BufferedImage b = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        g.drawImage(this.bufferedImage, 0, 0, null);
        this.bufferedImage = b;
        refresh();
        g.dispose();
    }
    
    /**
     * Convert any others space colors to RGB.
     */
    public void toRGB(){
        BufferedImage b = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = b.getGraphics();
        g.drawImage(this.bufferedImage, 0, 0, null);
        this.bufferedImage = b;
        refresh();
        g.dispose();
    }
    
    /**
     * Convert FastBitmap to Array.
     * @param image Array.
     */
    public void toArrayGray(int[][] image){
        
        int height = getHeight();
        int width = getWidth();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = getGray(i, j);
            }
        }
    }
    
    /**
     * Convert FastBitmap to Array.
     * @param image Image.
     */
    public void toArrayGray(float[][] image){
        
        int height = getHeight();
        int width = getWidth();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = (float)getGray(i, j);
            }
        }
    }
    
    /**
     * Convert FastBitmap to Array.
     * @param image 
     */
    public void toArrayGray(double[][] image){
        
        int height = getHeight();
        int width = getWidth();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = (double)getGray(i, j);
            }
        }
    }
    
    /**
     * Convert FastBitmap to Array.
     * @param image Array.
     */
    public void toArrayRGB(int[][][] image){
        
        int height = getHeight();
        int width = getWidth();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j][0] = getRed(i, j);
                image[i][j][1] = getGreen(i, j);
                image[i][j][2] = getBlue(i, j);
            }
        }
    }
    
    /**
     * Convert FastBitmap to Array.
     * @param image Array.
     */
    public void toArrayRGB(float[][][] image){
        
        int height = getHeight();
        int width = getWidth();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j][0] = getRed(i, j);
                image[i][j][1] = getGreen(i, j);
                image[i][j][2] = getBlue(i, j);
            }
        }
    }
    
    /**
     * Convert FastBitmap to Array.
     * @param image Array.
     */
    public void toArrayRGB(double[][][] image){
        
        int height = getHeight();
        int width = getWidth();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j][0] = getRed(i, j);
                image[i][j][1] = getGreen(i, j);
                image[i][j][2] = getBlue(i, j);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void arrayToImage(int image[][]){
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setGray(x, y, image[x][y]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void arrayToImage(float image[][]){
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setGray(x, y, (int)image[x][y]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void arrayToImage(double image[][]){
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setGray(x, y, (int)image[x][y]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void arrayToImage(int image[][][]){
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setRGB(x, y, image[x][y][0], image[x][y][1], image[x][y][2]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void arrayToImage(float image[][][]){
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setRGB(x, y, (int)image[x][y][0], (int)image[x][y][1], (int)image[x][y][2]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void arrayToImage(double image[][][]){
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setRGB(x, y, (int)image[x][y][0], (int)image[x][y][1], (int)image[x][y][2]);
            }
        }
    }
    
    /**
     * Clear all the image.
     */
    public void Clear(){
        if(isGrayscale()){
            int size = pixelsGRAY.length;
            for (int i = 0; i < size; i++) {
                pixelsGRAY[i] = 0;
            }
        }
        else{
            int size = pixelsGRAY.length;
            for (int i = 0; i < size; i++) {
                pixels[i] = 0;
            }
        }
    }
    
    /**
     * Allow use Java`s Graphics.
     * @return Graphics.
     */
    public Graphics getGraphics(){
        return this.bufferedImage.getGraphics();
    }
    
    /**
     * Create graphics.
     */
    public void createGraphics(){
        this.bufferedImage.createGraphics();
    }
    
    /**
     * Get Raster from BufferedImage.
     * @return Writable Raster.
     */
    private WritableRaster getRaster(){
        return this.bufferedImage.getRaster();
    }
    
    /**
     * Get Space color from buffered image.
     * @return Type.
     */
    private int getType(){
        return this.bufferedImage.getType();
    }
    
    /**
     * Verify Grayscale space color.
     * @return True if is Grayscale, otherwise false.
     */
    public boolean isGrayscale(){
        if (bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return true;
        }
        return false;
    }
    
    /**
     * Verify RGB space color.
     * @return True if is RGB, otherwise false.
     */
    public boolean isRGB(){
        if (bufferedImage.getType() == BufferedImage.TYPE_INT_RGB) {
            return true;
        }
        return false;
    }
    
    /**
     * Verify ARGB space color.
     * @return True if is ARGB, otherwise false.
     */
    public boolean isARGB(){
        if (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB){
            return true;
        }
        return false;
    }
    
    /**
     * Get width.
     * @return Width.
     */
    public int getWidth(){
        return bufferedImage.getWidth();
    }
    
    /**
     * Get height.
     * @return Height.
     */
    public int getHeight(){
        return bufferedImage.getHeight();
    }
    
    /**
     * Return RGB color.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return RGB.
     */
    public int[] getRGB(int x, int y){
        int[] rgb = new int[3];
        rgb[0] = pixels[x*getWidth()+y] >> 16 & 0xFF;
        rgb[1] = pixels[x*getWidth()+y] >> 8 & 0xFF;
        rgb[2] = pixels[x*getWidth()+y] & 0xFF;
        return rgb;
    }
    
    /**
     * Return RGB color.
     * @param point Point.
     * @return RGB.
     */
    public int[] getRGB(IntPoint point){
        return getRGB(point.x, point.y);
    }
    
    /**
     * Return ARGB color.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return ARGB.
     */
    public int[] getARGB(int x, int y){
        int[] argb = new int[4];
        argb[0] = pixels[x*getWidth()+y] >> 24 & 0xFF;
        argb[1] = pixels[x*getWidth()+y] >> 16 & 0xFF;
        argb[2] = pixels[x*getWidth()+y] >> 8  & 0xFF;
        argb[3] = pixels[x*getWidth()+y]       & 0xFF;
        return argb;
    }
    
    /**
     * Return ARGB color.
     * @param point Point.
     * @return ARGB.
     */
    public int[] getARGB(IntPoint point){
        return getARGB(point.x, point.y);
    }
    
    /**
     * Set RGB.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setRGB(int x, int y, int red, int green, int blue){
        int a = pixels[x*getWidth()+y] >> 24 & 0xFF;
        pixels[x*getWidth()+y] = a << 24 | red << 16 | green << 8 | blue;
    }
    
    /**
     * Set RGB.
     * @param point IntPoint.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setRGB(IntPoint point, int red, int green, int blue){
        setRGB(point.x,point.y,red,green,blue);
    }
    
    /**
     * Set RGB.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param rgb RGB color.
     */
    public void setRGB(int x, int y, int[] rgb){
         pixels[x*getWidth()+y] = rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }
    
    /**
     * Set RGB.
     * @param point IntPoint.
     * @param rgb RGB color.
     */
    public void setRGB(IntPoint point, int[] rgb){
         pixels[point.x*getWidth()+point.y] = rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }
    
    /**
     * Set ARGB.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param alpha Alpha channel's value.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setARGB(int x, int y, int alpha, int red, int green, int blue){
        pixels[x*getWidth()+y] = alpha << 24 | red << 16 | green << 8 | blue;
    }
    
    /**
     * Set ARGB.
     * @param point IntPoint.
     * @param alpha Alpha channel's value.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setARGB(IntPoint point, int alpha, int red, int green, int blue){
        setARGB(point.x,point.y,alpha,red,green,blue);
    }
    
    /**
     * Set ARGB.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param rgb ARGB color.
     */
    public void setARGB(int x, int y, int[] rgb){
         pixels[x*getWidth()+y] = rgb[0] << 24 | rgb[1] << 16 | rgb[2] << 8 | rgb[3];
    }
    
    /**
     * Set ARGB.
     * @param point IntPoint.
     * @param rgb ARGB color.
     */
    public void setARGB(IntPoint point, int[] rgb){
         pixels[point.x*getWidth()+point.y] = rgb[0] << 24 | rgb[1] << 16 | rgb[2] << 8 | rgb[3];
    }
    
    /**
     * Get Gray.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Gray channel's value.
     */
    public int getGray(int x, int y){
        return pixelsGRAY[x*getWidth()+y] < 0 ? pixelsGRAY[x*getWidth()+y] + 256 : pixelsGRAY[x*getWidth()+y];
    }
    
    /**
     * Get Gray.
     * @param point Point contains X and Y coordinates.
     * @return Gray channel's value.
     */
    public int getGray(IntPoint point){
        return pixelsGRAY[point.x*getWidth()+point.y] < 0 ? pixelsGRAY[point.x*getWidth()+point.y] + 256 : pixelsGRAY[point.x*getWidth()+point.y];
    }
    
    /**
     * Set Gray.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Gray channel's value.
     */
    public void setGray(int x, int y, int value){
        pixelsGRAY[x*getWidth()+y] = (byte)value;
    }
    
    /**
     * Set Gray.
     * @param point IntPoint.
     * @param value Gray channel's value.
     */
    public void setGray(IntPoint point, int value){
        pixelsGRAY[point.x*getWidth()+point.y] = (byte)value;
    }
    
    /**
     * Get Alpha.
     * @param x X Axis coordinate.
     * @param y Y Axis coordinate.
     * @return Alpha value.
     */
    public int getAlpha(int x, int y){
        return pixels[x*getWidth()+y] >> 24 & 0xFF;
    }
    
    /**
     * Set Alpha.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Alpha channel's value.
     */
    public void setAlpha(int x, int y, int value){
        int r,g,b;
        r = pixels[x*getWidth()+y] >> 16 & 0xFF;
        g = pixels[x*getWidth()+y] >> 8 & 0xFF;
        b = pixels[x*getWidth()+y] & 0xFF;
        pixels[x*getWidth()+y] = value << 24 | r << 16 | g << 8 | b;
    }
    
    /**
     * Get Red.
     * @param x X axis component.
     * @param y Y axis component.
     * @return Red channel's value.
     */
    public int getRed(int x, int y){
        return pixels[x*getWidth()+y] >> 16 & 0xFF;
    }
    
    /**
     * Get Red.
     * @param point IntPoint.
     * @return Red channel's value.
     */
    public int getRed(IntPoint point){
        return getRed(point.x,point.y);
    }
    
    /**
     * Set Red.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Red channel's value.
     */
    public void setRed(int x, int y, int value){
        int a,g,b;
        a = pixels[x*getWidth()+y] >> 24 & 0xFF;
        g = pixels[x*getWidth()+y] >> 8 & 0xFF;
        b = pixels[x*getWidth()+y] & 0xFF;
        pixels[x*getWidth()+y] = a << 24 | value << 16 | g << 8 | b;
    }
    
    /**
     * Set Red.
     * @param point IntPoint.
     * @param value Red channel's value.
     */
    public void setRed(IntPoint point, int value){
        setRed(point.x,point.y,value);
    }
    
    /**
     * Get Green.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Green channel's value.
     */
    public int getGreen(int x, int y){
        return pixels[x*getWidth()+y] >> 8 & 0xFF;
    }
    
    /**
     * Get Green.
     * @param point IntPoint.
     * @return Green channel's value.
     */
    public int getGreen(IntPoint point){
        return getGreen(point.x,point.y);
    }
    
    /**
     * Set Green.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Green channel's value.
     */
    public void setGreen(int x, int y, int value){
        int a,r,b;
        a = pixels[x*getWidth()+y] >> 24 & 0xFF;
        r = pixels[x*getWidth()+y] >> 16 & 0xFF;
        b = pixels[x*getWidth()+y] & 0xFF;
        pixels[x*getWidth()+y] = a << 24 | r << 16 | value << 8 | b;
    }
    
    /**
     * Set Green.
     * @param point IntPoint.
     * @param value Green channel's value.
     */
    public void setGreen(IntPoint point, int value){
        setGreen(point.x,point.y, value);
    }
    
    /**
     * Get Green.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Blue channel's value.
     */
    public int getBlue(int x, int y){
        return pixels[x*getWidth()+y] & 0xFF;
    }
    
    /**
     * Get Blue.
     * @param point IntPoint.
     * @return Blue channel's value.
     */
    public int getBlue(IntPoint point){
        return getBlue(point.x,point.y);
    }
    
    /**
     * Set Blue.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Blue channel's value.
     */
    public void setBlue(int x, int y, int value){
        int a,r,g;
        a = pixels[x*getWidth()+y] >> 24 & 0xFF;
        r = pixels[x*getWidth()+y] >> 16 & 0xFF;
        g = pixels[x*getWidth()+y] >> 8 & 0xFF;
        pixels[x*getWidth()+y] = a << 24 | r << 16 | g << 8 | value;
    }
    
    /**
     * Set Blue.
     * @param point IntPoint.
     * @param value Blue channel's value.
     */
    public void setBlue(IntPoint point, int value){
        setBlue(point.x,point.y,value);
    }
    
    /**
     * Save FastBitmap as Bitmap.
     * @param pathname Path name.
     */
    public void saveAsBMP(String pathname){
        try {
            ImageIO.write(this.bufferedImage, "bmp", new File(pathname));
        } catch (IOException ex) {
            //Logger.getLogger(FastBitmap.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Save FastBitmap as PNG.
     * @param pathname Path name.
     */
    public void saveAsPNG(String pathname){
        try {
            ImageIO.write(this.bufferedImage, "png", new File(pathname));
        } catch (IOException ex) {
            //Logger.getLogger(FastBitmap.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Save FastBitmap as GIF.
     * @param pathname Path name.
     */
    public void saveAsGIF(String pathname){
        try {
            ImageIO.write(this.bufferedImage, "gif", new File(pathname));
        } catch (IOException ex) {
            //Logger.getLogger(FastBitmap.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Save FastBitmap as JPG.
     * @param pathname Path name.
     */
    public void saveAsJPG(String pathname){
        try {
            ImageIO.write(this.bufferedImage, "jpg", new File(pathname));
        } catch (IOException ex) {
            //Logger.getLogger(FastBitmap.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}