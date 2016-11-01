// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
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
    private CoordinateSystem cSystem = CoordinateSystem.Matrix;
    private int strideX, strideY;
    private int size;
    
    /**
     * Coodinate system.
     */
    public static enum CoordinateSystem {
        /**
         * Represents X and Y.
         * <p>Example:
         * <pre>
         * {@code 
         * for(int y = 0; y < height; y++){
         *    for(int x = 0; x < width; x++){
         *       int g = fastBitmap.getGray(x,y);
         *       ...
         *    }
         * }
         * }
         * </pre>
         */
        Cartesian,
        
        /**
         * Represents I and J.
         * <p>Example:
         * <pre>
         * {@code 
         * for(int i = 0; i < height; i++){
         *    for(int j = 0; j < width; j++){
         *       int g = fastBitmap.getGray(i,j);
         *       ...
         *    }
         * }
         * }
         * </pre>
         */
        Matrix
    };

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
        setCoordinateSystem(fastBitmap.getCoordinateSystem());
        refresh();
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param bufferedImage Buffered image.
     */
    public FastBitmap(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        prepare();
        refresh();
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Image.
     */
    public FastBitmap(Image image) {
        bufferedImage = (BufferedImage)image;
        prepare();
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param ico Ico.
     */
    public FastBitmap(ImageIcon ico){
        bufferedImage = (BufferedImage)ico.getImage();
        prepare();
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param pathname The path that indicate a image.
     */
    public FastBitmap(String pathname){
        try {
            this.bufferedImage = ImageIO.read(new File(pathname));
            prepare();
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
        this.setCoordinateSystem(CoordinateSystem.Matrix);
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
        else if(colorSpace == ColorSpace.ARGB){
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        
        this.setCoordinateSystem(CoordinateSystem.Matrix);
        refresh();
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Array.
     */
    public FastBitmap(int[][] image){
        bufferedImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_BYTE_GRAY);
        this.setCoordinateSystem(CoordinateSystem.Matrix);
        refresh();
        matrixToImage(image);
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Array.
     */
    public FastBitmap(int[][][] image){
        if (image[0][0].length == 3)
            bufferedImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        else{
            bufferedImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_ARGB);
        }
        this.setCoordinateSystem(CoordinateSystem.Matrix);
        refresh();
        matrixToImage(image);
    }
    
    /**
     * Prepare the Fast Bitmap;
     */
    private void prepare(){
        if (getType() == BufferedImage.TYPE_BYTE_GRAY) {
            refresh();
        }
        else if (getType() == BufferedImage.TYPE_INT_ARGB || getType() == BufferedImage.TYPE_4BYTE_ABGR){
            toARGB();
        }
        else{
            toRGB();
        }
        setCoordinateSystem(CoordinateSystem.Matrix);
    }
    
    /**
     * Refresh raster and get data buffer from raster.
     */
    private void refresh(){
        this.raster = getRaster();
        if (isGrayscale()) {
            pixelsGRAY = ((DataBufferByte)raster.getDataBuffer()).getData();
            this.size = pixelsGRAY.length;
        }
        if (isRGB() || isARGB()) {
            pixels = ((DataBufferInt)raster.getDataBuffer()).getData();
            this.size = pixels.length;
        }
    }
    
    /**
     * Get Space color.
     * @return Space color.
     */
    public ColorSpace getColorSpace(){
        if (getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return ColorSpace.Grayscale;
        }
        else if (getType() == BufferedImage.TYPE_INT_ARGB) {
        	return ColorSpace.ARGB;
        }
        return ColorSpace.RGB;
    }
    
    /**
     * Retrieve the raw gray data from the Fast bitmap.
     * @return The data with the pixels values.
     */
    public byte[] getGrayData(){
        return this.pixelsGRAY;
    }
    
    /**
     * Set the raw gray data in the Fast Bitmap.
     * @param data Data.
     */
    public void setGrayData(byte[] data){
        this.pixelsGRAY = data;
    }
    
    /**
     * Retrieve the raw rgb or argb data from the Fast bitmap.
     * @return The data with pixels values.
     */
    public int[] getRGBData(){
        return this.pixels;
    }

    /**
     * Get the size of the image in pixels.
     * @return Number of pixels.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Set the raw rgb or argb data in the Fast Bitmap.
     * @param data 
     */
    public void setRGBData(int[] data){
        this.pixels = data;
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
        setCoordinateSystem(fastBitmap.getCoordinateSystem());
        refresh();
    }
    
    /**
     * Get the actually coordinate system.
     * @return Coordinate system.
     */
    public CoordinateSystem getCoordinateSystem(){
        return cSystem;
    }
    
    /**
     * Set coordinate system.
     * @param coSystem Coordinate system.
     */
    public void setCoordinateSystem(CoordinateSystem coSystem){
        this.cSystem = coSystem;
        if (coSystem == CoordinateSystem.Matrix){
            this.strideX = getWidth();
            this.strideY = 1;
        }
        else{
            this.strideX = 1;
            this.strideY = getWidth();
        }
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
     * Convert the image to array of double representation.
     * @return Array of the image.
     */
    public double[] toArrayGrayAsDouble(){
        double[] array = new double[getHeight()*getWidth()];
        for (int i = 0; i < array.length; i++) {
            array[i] = getGray(i);
        }
        return array;
    }
    
    /**
     * Convert the image to array of double representation.
     * @return Array of the image.
     */
    public int[] toArrayGrayAsInt(){
        int[] array = new int[getHeight()*getWidth()];
        for (int i = 0; i < array.length; i++) {
            array[i] = getGray(i);
        }
        return array;
    }
    
    /**
     * Convert the image to array of double representation.
     * @return Array of the image.
     */
    public float[] toArrayGrayAsFloat(){
        float[] array = new float[getHeight()*getWidth()];
        for (int i = 0; i < array.length; i++) {
            array[i] = getGray(i);
        }
        return array;
    }
    
    /**
     * Convert the image to matrix of integer representation.
     * @return Matrix of the image.
     */
    public int[][] toMatrixGrayAsInt(){
        int height = getHeight();
        int width = getWidth();
        
        int[][] image = new int[height][width];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = getGray(idx++);
            }
        }
        
        return image;
    }
    
    /**
     * Convert the image to matrix of double representation.
     * @return Matrix of the image.
     */
    public double[][] toMatrixGrayAsDouble(){
        int height = getHeight();
        int width = getWidth();
        
        double[][] image = new double[height][width];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = getGray(idx++);
            }
        }
        
        return image;
    }
    
    /**
     * Convert the image to matrix of float representation.
     * @return Matrix of the image.
     */
    public float[][] toMatrixGrayAsFloat(){
        int height = getHeight();
        int width = getWidth();
        
        float[][] image = new float[height][width];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = getGray(idx++);
            }
        }
        
        return image;
    }
    
    /**
     * Convert the image to matrix of integer representation.
     * @return Matrix of the image.
     */
    public int[][][] toMatrixRGBAsInt(){
        int height = getHeight();
        int width = getWidth();
        
        int[][][] image = new int[height][width][3];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j][0] = getRed(idx);
                image[i][j][1] = getGreen(idx);
                image[i][j][2] = getBlue(idx);
                idx++;
            }
        }
        
        return image;
    }
    
    /**
     * Convert the image to matrix of integer representation.
     * @return Matrix of the image.
     */
    public double[][][] toMatrixRGBAsDouble(){
        int height = getHeight();
        int width = getWidth();
        
        double[][][] image = new double[height][width][3];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j][0] = getRed(idx);
                image[i][j][1] = getGreen(idx);
                image[i][j][2] = getBlue(idx);
                idx++;
            }
        }
        
        return image;
    }
    
    /**
     * Convert the image to matrix of integer representation.
     * @return Matrix of the image.
     */
    public float[][][] toMatrixRGBAsFloat(){
        int height = getHeight();
        int width = getWidth();
        
        float[][][] image = new float[height][width][3];
        int idx = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j][0] = getRed(idx);
                image[i][j][1] = getGreen(idx);
                image[i][j][2] = getBlue(idx);
                idx++;
            }
        }
        
        return image;
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void matrixToImage(int image[][]){
        int idx = 0;
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setGray(idx++, image[x][y]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void matrixToImage(float image[][]){
        int idx = 0;
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setGray(idx++, (int)image[x][y]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void matrixToImage(double image[][]){
        int idx = 0;
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                setGray(idx++, (int)image[x][y]);
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void matrixToImage(int image[][][]){
        int idx = 0;
        if (image[0][0].length == 3)
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    setRGB(idx++, image[x][y][0], image[x][y][1], image[x][y][2]);
                }
            }
        else{
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    setARGB(idx++, image[x][y][0], image[x][y][1], image[x][y][2], image[x][y][3]);
                }
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void matrixToImage(float image[][][]){
        int idx = 0;
        if (image[0][0].length == 3)
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    setRGB(idx++, (int)image[x][y][0], (int)image[x][y][1], (int)image[x][y][2]);
                }
            }
        else{
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    setARGB(idx++, (int)image[x][y][0], (int)image[x][y][1], (int)image[x][y][2], (int)image[x][y][3]);
                }
            }
        }
    }
    
    /**
     * Convert Array to FastBitmap.
     * @param image Array.
     */
    public void matrixToImage(double image[][][]){
        int idx = 0;
        if (image[0][0].length == 3)
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    setRGB(idx++, (int)image[x][y][0], (int)image[x][y][1], (int)image[x][y][2]);
                }
            }
        else{
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    setARGB(idx++, (int)image[x][y][0], (int)image[x][y][1], (int)image[x][y][2], (int)image[x][y][3]);
                }
            }
        }
    }
    
    /**
     * Clear all the image.
     * Set all pixels with value 0.
     */
    public void Clear(){
        if(isGrayscale()){
            int size = pixelsGRAY.length;
            for (int i = 0; i < size; i++) {
                pixelsGRAY[i] = 0;
            }
        }
        else{
            int size = pixels.length;
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
     * @param offset Offset.
     * @return RGB.
     */
    public int[] getRGB(int offset){
        int[] rgb = new int[3];
        rgb[0] = pixels[offset] >> 16 & 0xFF;
        rgb[1] = pixels[offset] >> 8 & 0xFF;
        rgb[2] = pixels[offset] & 0xFF;
        return rgb;
    }
    
    /**
     * Return RGB color.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return RGB.
     */
    public int[] getRGB(int x, int y){
        int[] rgb = new int[3];
        rgb[0] = pixels[x*strideX+y*strideY] >> 16 & 0xFF;
        rgb[1] = pixels[x*strideX+y*strideY] >> 8 & 0xFF;
        rgb[2] = pixels[x*strideX+y*strideY] & 0xFF;
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
        argb[0] = pixels[x*strideX+y*strideY] >> 24 & 0xFF;
        argb[1] = pixels[x*strideX+y*strideY] >> 16 & 0xFF;
        argb[2] = pixels[x*strideX+y*strideY] >> 8  & 0xFF;
        argb[3] = pixels[x*strideX+y*strideY]       & 0xFF;
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
     * @param color RGB color.
     */
    public void setRGB(int x, int y, Color color){
        setRGB(x, y, color.r, color.g, color.b);
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
        int a = pixels[x*strideX+y*strideY] >> 24 & 0xFF;
        pixels[x*strideX+y*strideY] = a << 24 | red << 16 | green << 8 | blue;
    }
    
    /**
     * Set RGB.
     * @param point IntPoint.
     * @param color RGB color.
     */
    public void setRGB(IntPoint point, Color color){
        setRGB(point.x, point.y, color.r, color.g, color.b);
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
         pixels[x*strideX+y*strideY] = rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }
    
    /**
     * Set RGB values.
     * @param offset Offset.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setRGB(int offset, int red, int green, int blue){
        int a = pixels[offset] >> 24 & 0xFF;
        pixels[offset] = a << 24 | red << 16| green << 8 | blue;
    }
    
    /**
     * Set RGB values.
     * @param offset Offset.
     * @param rgb RGB array.
     */
    public void setRGB(int offset, int[] rgb){
        int a = pixels[offset] >> 24 & 0xFF;
        pixels[offset] = a << 24 | rgb[0] << 16| rgb[1] << 8 | rgb[2];
    }
    
    /**
     * Set RGB values.
     * @param offset Offset.
     * @param color Color.
     */
    public void setRGB(int offset, Color color){
        int a = pixels[offset] >> 24 & 0xFF;
        pixels[offset] = a << 24 | color.r << 16| color.g << 8 | color.b;
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
        pixels[x*strideX+y*strideY] = alpha << 24 | red << 16 | green << 8 | blue;
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
         pixels[x*strideX+y*strideY] = rgb[0] << 24 | rgb[1] << 16 | rgb[2] << 8 | rgb[3];
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
     * Set ARGB values.
     * @param offset Offset.
     * @param alpha Alpha channel's value.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setARGB(int offset, int alpha, int red, int green, int blue){
        pixels[offset] = alpha << 24| red << 16| green << 8 | blue;
    }
    
    /**
     * Set ARGB values.
     * @param offset Offset.
     * @param argb ARGB array.
     */
    public void setARGB(int offset, int[] argb){
        pixels[offset] = argb[0] << 24| argb[1] << 16| argb[2] << 8 | argb[3];
    }
    
    /**
     * Get Gray.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Gray channel's value.
     */
    public int getGray(int x, int y){
        return pixelsGRAY[x*strideX+y*strideY] & 0xFF;
    }
    
    /**
     * Get Gray.
     * @param point Point contains X and Y coordinates.
     * @return Gray channel's value.
     */
    public int getGray(IntPoint point){
        return pixelsGRAY[point.x*getWidth()+point.y] & 0xFF;
    }
    
    /**
     * Get gray channel's value.
     * @param offset Offset.
     */
    public int getGray(int offset){
        return pixelsGRAY[offset] & 0xFF;
    }
    
    /**
     * Set gray channel's value.
     * @param offset Offset.
     * @param value Gray channel's value.
     */
    public void setGray(int offset, int value){
        pixelsGRAY[offset] = (byte)value;
    }
    
    /**
     * Set Gray.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Gray channel's value.
     */
    public void setGray(int x, int y, int value){
        pixelsGRAY[x*strideX+y*strideY] = (byte)value;
    }
    
    /**
     * Set Gray.
     * @param point IntPoint.
     * @param value Gray channel's value.
     */
    public void setGray(IntPoint point, int value){
        pixelsGRAY[point.x*strideX+point.y*strideY] = (byte)value;
    }
    
    /**
     * Get Alpha.
     * @param x X Axis coordinate.
     * @param y Y Axis coordinate.
     * @return Alpha value.
     */
    public int getAlpha(int x, int y){
        return pixels[x*strideX+y*strideY] >> 24 & 0xFF;
    }
    
    /**
     * Get alpha channel's value.
     * @param offset Offset.
     */
    public int getAlpha(int offset){
        return pixels[offset] >> 24 & 0xFF;
    }
    
    /**
     * Set alpha channel's value.
     * @param offset Offset.
     * @param value Alpha channel's value.
     */
    public void setAlpha(int offset, int value){
        int r,g,b;
        r = pixels[offset] >> 16 & 0xFF;
        g = pixels[offset] >> 8 & 0xFF;
        b = pixels[offset] & 0xFF;
        pixels[offset] = value << 24 | r << 16 | g << 8 | b;
    }
    
    /**
     * Set Alpha.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Alpha channel's value.
     */
    public void setAlpha(int x, int y, int value){
        int r,g,b;
        r = pixels[x*strideX+y*strideY] >> 16 & 0xFF;
        g = pixels[x*strideX+y*strideY] >> 8 & 0xFF;
        b = pixels[x*strideX+y*strideY] & 0xFF;
        pixels[x*strideX+y*strideY] = value << 24 | r << 16 | g << 8 | b;
    }
    
    /**
     * Get Red.
     * @param x X axis component.
     * @param y Y axis component.
     * @return Red channel's value.
     */
    public int getRed(int x, int y){
        return pixels[x*strideX+y*strideY] >> 16 & 0xFF;
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
     * Get red channel's value.
     * @param offset Offset.
     */
    public int getRed(int offset){
        return pixels[offset] >> 16 & 0xFF;
    }
    
    /**
     * Set red channel's value.
     * @param offset Offset.
     * @param value Red channel's value.
     */
    public void setRed(int offset, int value){
        int a,g,b;
        a = pixels[offset] >> 24 & 0xFF;
        g = pixels[offset] >> 8 & 0xFF;
        b = pixels[offset] & 0xFF;
        pixels[offset] = a << 24 | value << 16 | g << 8 | b;
    }
    
    /**
     * Set Red.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Red channel's value.
     */
    public void setRed(int x, int y, int value){
        int a,g,b;
        a = pixels[x*strideX+y*strideY] >> 24 & 0xFF;
        g = pixels[x*strideX+y*strideY] >> 8 & 0xFF;
        b = pixels[x*strideX+y*strideY] & 0xFF;
        pixels[x*strideX+y*strideY] = a << 24 | value << 16 | g << 8 | b;
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
        return pixels[x*strideX+y*strideY] >> 8 & 0xFF;
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
     * Get green channel's value.
     * @param offset Offset.
     */
    public int getGreen(int offset){
        return pixels[offset] >> 8 & 0xFF;
    }
    
    /**
     * Set green channel's value.
     * @param offset Offset.
     * @param value Green channel's value.
     */
    public void setGreen(int offset, int value){
        int a,r,b;
        a = pixels[offset] >> 24 & 0xFF;
        r = pixels[offset] >> 16 & 0xFF;
        b = pixels[offset] & 0xFF;
        pixels[offset] = a << 24 | r << 16 | value << 8 | b;
    }
    
    /**
     * Set Green.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Green channel's value.
     */
    public void setGreen(int x, int y, int value){
        int a,r,b;
        a = pixels[x*strideX+y*strideY] >> 24 & 0xFF;
        r = pixels[x*strideX+y*strideY] >> 16 & 0xFF;
        b = pixels[x*strideX+y*strideY] & 0xFF;
        pixels[x*strideX+y*strideY] = a << 24 | r << 16 | value << 8 | b;
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
        return pixels[x*strideX+y*strideY] & 0xFF;
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
     * Get blue channel's value.
     * @param offset Offset.
     */
    public int getBlue(int offset){
        return pixels[offset] & 0xFF;
    }
    
    /**
     * Set blue channel's value.
     * @param offset Offset.
     * @param value Blue channel's value.
     */
    public void setBlue(int offset, int value){
        int a,r,g;
        a = pixels[offset] >> 24 & 0xFF;
        r = pixels[offset] >> 16 & 0xFF;
        g = pixels[offset] >> 8 & 0xFF;
        pixels[offset] = a << 24 | r << 16 | g << 8 | value;
    }
    
    /**
     * Set Blue.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Blue channel's value.
     */
    public void setBlue(int x, int y, int value){
        int a,r,g;
        a = pixels[x*strideX+y*strideY] >> 24 & 0xFF;
        r = pixels[x*strideX+y*strideY] >> 16 & 0xFF;
        g = pixels[x*strideX+y*strideY] >> 8 & 0xFF;
        pixels[x*strideX+y*strideY] = a << 24 | r << 16 | g << 8 | value;
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
     * Clamp values.
     * @param value Value.
     * @param min Minimum value.
     * @param max Maximum value.
     * @return Clamped values.
     */
    public int clampValues(int value, int min, int max){
        if(value < min)
            return min;
        else if(value > max)
            return max;
        return value;
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
     * @param quality Quality.
     */
    public void saveAsJPG(String pathname, float quality){
        try {
            JPEGImageWriteParam params = new JPEGImageWriteParam(null);
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionQuality(quality);
            
            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(new FileImageOutputStream(new File(pathname)));
            writer.write(null, new IIOImage(bufferedImage, null, null), params);
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