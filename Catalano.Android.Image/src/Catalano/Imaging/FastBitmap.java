package Catalano.Imaging;

import Catalano.Core.IntPoint;
import Catalano.Imaging.Filters.Grayscale;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class FastBitmap {
    Bitmap b;
    int[] pixels;
    private int stride;
    private boolean isGrayscale = false;
	
    /**
        * Color space.
        */
    public static enum ColorSpace {

        /**
            * Grayscale.
            */
        Grayscale,
        /**
            * RGB.
            */
        RGB
    };
    
    /**
     * Initialize a new instance of the FastBitmap class.
     */
    public FastBitmap() {}

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param fastBitmap FastBitmap type.
     */
    public FastBitmap(FastBitmap fastBitmap){
        this.b = fastBitmap.toBitmap();
        refresh();

        if (fastBitmap.isRGB())
                isGrayscale = false;
        else
                isGrayscale = true;
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param bitmap Bitmap type.
     */
    public FastBitmap(Bitmap bitmap){
        this.b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        refresh();
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param width Width.
     * @param height Height.
     */
    public FastBitmap(int width, int height){
        b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        refresh();
    }

    /**
     * Initialize a new instance of the FastBitmap class.
     * @param width Width.
     * @param height Height.
     * @param colorSpace Color space.
     */
    public FastBitmap(int width, int height, ColorSpace colorSpace){
        b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        refresh();
        if (colorSpace == ColorSpace.Grayscale) isGrayscale = true;
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Array.
     */
    public FastBitmap(int[][] image){
        this.b = Bitmap.createBitmap(image[0].length, image.length, Config.ARGB_8888);
        isGrayscale = true;
        refresh();
        arrayToImage(image);
    }
    
    /**
     * Initialize a new instance of the FastBitmap class.
     * @param image Array.
     */
    public FastBitmap(int[][][] image){
    	this.b = Bitmap.createBitmap(image[0][0].length, image[0].length, Config.ARGB_8888);
    	isGrayscale = false;
        refresh();
        arrayToImage(image);
    }
    
    /**
     * Read pixels from a bitmap.
     */
    private void refresh(){
    	this.stride = getWidth();
    	pixels = new int[b.getWidth() * b.getHeight()];
    	b.getPixels(pixels, 0, getWidth(), 0, 0, b.getWidth(), b.getHeight());
    }

    /**
     * Get Height.
     * @return height.
     */
    public int getHeight(){
        return b.getHeight();
    }

    /**
     * Get Width.
     * @return Width.
     */
    public int getWidth(){
        return b.getWidth();
    }

    /**
     * Get Color space.
     * @return ColorSpace.
     */
    public ColorSpace getColorSpace(){
        if (isGrayscale)
                return ColorSpace.Grayscale;
        else
                return ColorSpace.RGB;
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
     * Set RGB.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setRGB(int x, int y, int red, int green, int blue){
        pixels[x * stride + y] = 255 << 24 | red << 16 | green << 8 | blue;
    }
    
    /**
     * Set RGB.
     * @param point X and Y coordinates.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setRGB(IntPoint point, int red, int green, int blue){
    	pixels[point.x * stride + point.y] = 255 << 24 | red << 16 | green << 8 | blue;
    }
    
    /**
     * Set RGB.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param rgb RGB color.
     */
    public void setRGB(int x, int y, int[] rgb){
         pixels[x*getWidth()+y] = 255 << 24 | rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }
    
    /**
     * Set RGB.
     * @param point IntPoint.
     * @param rgb RGB color.
     */
    public void setRGB(IntPoint point, int[] rgb){
         pixels[point.x*getWidth()+point.y] = 255 << 24 | rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }

    /**
     * Get Gray.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Gray channel's value.
     */
    public int getGray(int x, int y){
        return pixels[x * stride + y] >> 16 & 0xFF;
    }
    
    /**
     * Get Gray.
     * @param point X and Y coordinates.
     * @return Gray channel's value.
     */
    public int getGray(IntPoint point){
    	return pixels[point.x * stride + point.y] >> 16 & 0xFF;
    }

    /**
     * Set Gray.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Gray channel's value.
     */
    public void setGray(int x, int y, int value){
        pixels[x * stride + y] =  255 << 24 | value << 16 | value << 8 | value;
    }
    
    /**
     * Set Gray.
     * @param point X and Y coordinates.
     * @param value Gray channel's value.
     */
    public void setGray(IntPoint point, int value){
    	pixels[point.x * stride + point.y] =  255 << 24 | value << 16 | value << 8 | value;
    }

    /**
     * Get Red.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Red channel's value.
     */
    public int getRed(int x, int y){
        return pixels[x * stride + y] >> 16 & 0xFF;
    }
    
    /**
     * Get Red.
     * @param point X and Y coordinates.
     * @return Red channel's value.
     */
    public int getRed(IntPoint point){
    	return pixels[point.x * stride + point.y] >> 16 & 0xFF;
    }

    /**
     * Set Red.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Red channel's value.
     */
    public void setRed(int x, int y, int value){
        int g = pixels[x * stride + y] >> 8 & 0xFF;
        int b = pixels[x * stride + y] & 0xFF;
        pixels[x * stride + y] =  255 << 24 | value << 16 | g << 8 | b;
    }
    
    /**
     * Set Red.
     * @param point X and Y coordinates.
     * @param value Red channel's value.
     */
    public void setRed(IntPoint point, int value){
        int g = pixels[point.x * stride + point.y] >> 8 & 0xFF;
        int b = pixels[point.x * stride + point.y] & 0xFF;
        pixels[point.x * stride + point.y] =  255 << 24 | value << 16 | g << 8 | b;
    }

    /**
     * Get Green.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Green channel's value.
     */
    public int getGreen(int x, int y){
        return pixels[x * stride + y] >> 8 & 0xFF;
    }
    
    /**
     * Get Green.
     * @param point X and Y coordinates.
     * @return Green channel's value.
     */
    public int getGreen(IntPoint point){
    	return pixels[point.x * stride + point.y] >> 8 & 0xFF;
    }

    /**
     * Set Green.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Green channel's value.
     */
    public void setGreen(int x, int y, int value){
        int r = pixels[x * stride + y] >> 16 & 0xFF;
        int b = pixels[x * stride + y] & 0xFF;
        pixels[x * stride + y] = 255 << 24 | r << 16 | value << 8 | b;
    }
    
    /**
     * Set Green.
     * @param point X and Y coordinates.
     * @param value Green channel's value.
     */
    public void setGreen(IntPoint point, int value){
        int r = pixels[point.x * stride + point.y] >> 16 & 0xFF;
        int b = pixels[point.x * stride + point.y] & 0xFF;
        pixels[point.x * stride + point.y] = 255 << 24 | r << 16 | value << 8 | b;
    }

    /**
     * Get Blue.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Blue channel's value.
     */
    public int getBlue(int x, int y){
        return pixels[x * stride + y] & 0xFF;
    }
    
    /**
     * Get Blue.
     * @param point X and Y coordinates.
     * @return Blue channel's value.
     */
    public int getBlue(IntPoint point){
    	return pixels[point.x * stride + point.y] & 0xFF;
    }

    /**
     * Set Blue.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param value Blue channel's value.
     */
    public void setBlue(int x, int y, int value){
        int r = pixels[x * stride + y] >> 16 & 0xFF;
        int g = pixels[x * stride + y] >> 8 & 0xFF;
        pixels[x * stride + y] = 255 << 24 | r << 16 | g << 8 | value;
    }
    
    /**
     * Set Blue.
     * @param point X and Y coordinates.
     * @param value Blue channel's value.
     */
    public void setBlue(IntPoint point, int value){
        int r = pixels[point.x * stride + point.y] >> 16 & 0xFF;
        int g = pixels[point.x * stride + point.y] >> 8 & 0xFF;
        pixels[point.x * stride + point.y] = 255 << 24 | r << 16 | g << 8 | value;
    }

    /**
     * Set image to FastBitmap.
     * @param bitmap Bitmap.
     */
    public void setImage(Bitmap bitmap){
        this.b = bitmap;
        this.stride = b.getWidth();
        pixels = new int[b.getHeight() * b.getWidth()];
        b.getPixels(pixels, 0, getWidth(), 0, 0, b.getWidth(), b.getHeight());
    }

    /**
     * Set image to FastBitmap.
     * @param fastBitmap FastBitmap.
     */
    public void setImage(FastBitmap fastBitmap){
        this.b = fastBitmap.toBitmap();
        this.stride = b.getWidth();
        pixels = new int[b.getHeight() * b.getWidth()];
        b.getPixels(pixels, 0, getWidth(), 0, 0, b.getWidth(), b.getHeight());
        if (fastBitmap.isRGB())
                this.isGrayscale = false;
        else
            isGrayscale = true;
    }

    /**
     * Verify RGB space color.
     * @return True if is RGB, otherwise false.
     */
    public boolean isRGB(){
        return !isGrayscale;
    }

    /**
     * Verify Grayscale space color.
     * @return True if is Grayscale, otherwise false.
     */
    public boolean isGrayscale(){
        return isGrayscale;
    }

    /**
     * Convert any others space colors to RGB.
     */
    public void toRGB(){
        this.isGrayscale = false;
    }

     /** Convert image to grayscale.
     * This method will convert to Luminosity method.
     */
    public void toGrayscale(){
        new Grayscale().applyInPlace(this);
    }

    /**
     * Convert FastBitmap to Bitmap.
     * @return Bitmap.
     */
    public Bitmap toBitmap(){
        if (isRGB())
                b.setPixels(pixels, 0, stride, 0, 0, b.getWidth(), b.getHeight());
        else{
                for (int i = 0; i < getHeight(); i++) {
                        for (int j = 0; j < getWidth(); j++) {
                                int g = pixels[i * stride + j] >> 16 & 0xFF;
                                pixels[i * stride + j] = 255 << 24 | g << 16 | g << 8 | g;
                        }
                }
                b.setPixels(pixels, 0, stride, 0, 0, b.getWidth(), b.getHeight());
        }
        return b;
    }
    
    /**
     * Recycle.
     * Free the native object associated with this bitmap, and clear the reference to the pixel data.
     * This will not free the pixel data synchronously; it simply allows it to be garbage collected if
     * there are no other references.
     */
    public void recycle(){
    	this.b.recycle();
    }
}