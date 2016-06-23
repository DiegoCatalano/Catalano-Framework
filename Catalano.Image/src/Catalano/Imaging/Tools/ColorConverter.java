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

package Catalano.Imaging.Tools;

import Catalano.Imaging.Color;

/**
 * Convert between different color spaces supported.
 * RGB -> IHS -> RGB
 * RGB -> CMYK -> RGB
 * RGB -> YIQ -> RGB
 * RGB -> YCbCr -> RGB
 * RGB -> YUV -> RGB
 * RGB -> RGChromaticity
 * RGB -> HSV -> RGB
 * RGB -> YCC -> RGB
 * RGB -> YCoCg -> RGB
 * RGB -> XYZ -> RGB
 * RGB -> HunterLAB -> RGB
 * RGB -> HLS -> RGB
 * RGB -> CIE-LAB -> RGB
 * XYZ -> HunterLAB -> XYZ
 * XYZ -> CIE-LAB -> XYZ
 * @author Diego Catalano
 */
public class ColorConverter {

    /**
     * Don't let anyone instantiate this class.
     */
    private ColorConverter() {}
    
    public static enum YCbCrColorSpace {ITU_BT_601,ITU_BT_709_HDTV};
    
    // XYZ (Tristimulus) Reference values of a perfect reflecting diffuser
    
    //2o Observer (CIE 1931)
    // X2, Y2, Z2
    public static double[] CIE2_A = {109.850f, 100f, 35.585f}; //Incandescent
    public static double[] CIE2_C = {98.074f, 100f, 118.232f};
    public static double[] CIE2_D50 = {96.422f, 100f, 82.521f};
    public static double[] CIE2_D55 = {95.682f, 100f, 92.149f};
    public static double[] CIE2_D65 = {95.047f, 100f, 108.883f}; //Daylight
    public static double[] CIE2_D75 = {94.972f, 100f, 122.638f};
    public static double[] CIE2_F2 = {99.187f, 100f, 67.395f}; //Fluorescent
    public static double[] CIE2_F7 = {95.044f, 100f, 108.755f};
    public static double[] CIE2_F11 = {100.966f, 100f, 64.370f};
    
    //10o Observer (CIE 1964)
    // X2, Y2, Z2
    public static double[] CIE10_A = {111.144f, 100f, 35.200f}; //Incandescent
    public static double[] CIE10_C = {97.285f, 100f, 116.145f};
    public static double[] CIE10_D50 = {96.720f, 100f, 81.427f};
    public static double[] CIE10_D55 = {95.799f, 100f, 90.926f};
    public static double[] CIE10_D65 = {94.811f, 100f, 107.304f}; //Daylight
    public static double[] CIE10_D75 = {94.416f, 100f, 120.641f};
    public static double[] CIE10_F2 = {103.280f, 100f, 69.026f}; //Fluorescent
    public static double[] CIE10_F7 = {95.792f, 100f, 107.687f};
    public static double[] CIE10_F11 = {103.866f, 100f, 65.627f};
    
    /**
     * RGB -> CMYK
     * @param color Color.
     * @return CMYK color space. Normalized.
     */
    public static double[] RGBtoCMYK(Color color){
        return RGBtoCMYK(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> CMYK
     * @param red Values in the range [0..255].
     * @param green Values in the range [0..255].
     * @param blue Values in the range [0..255].
     * @return CMYK color space. Normalized.
     */
    public static double[] RGBtoCMYK(int red, int green, int blue){
        double[] cmyk = new double[4];
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        double k = 1.0f - Math.max(r, Math.max(g, b));
        double c = (1f-r-k) / (1f-k);
        double m = (1f-g-k) / (1f-k);
        double y = (1f-b-k) / (1f-k);
        
        cmyk[0] = c;
        cmyk[1] = m;
        cmyk[2] = y;
        cmyk[3] = k;
        
        return cmyk;
    }
    
    /**
     * CMYK -> RGB
     * @param c Cyan.
     * @param m Magenta.
     * @param y Yellow.
     * @param k Black.
     * @return RGB color space.
     */
    public static int[] CMYKtoRGB(double c, double m, double y, double k){
        int[] rgb = new int[3];
        
        rgb[0] = (int)(255 * (1-c) * (1-k));
        rgb[1] = (int)(255 * (1-m) * (1-k));
        rgb[2] = (int)(255 * (1-y) * (1-k));
        
        return rgb;
    }
    
    /**
     * RGB -> IHS
     * @param color Color.
     * @return IHS color space. Normalized.
     */
    public static double[] RGBtoIHS(Color color){
        return RGBtoCMYK(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> IHS
     * @param red Values in the range [0..255].
     * @param green Values in the range [0..255].
     * @param blue Values in the range [0..255].
     * @return IHS color space. Normalized.
     */
    public static double[] RGBtoIHS(int red, int green, int blue){
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        double i = r+g+b;
        
        double h;
        if(b == Math.min(Math.min(r, g), b)){
            h = (g-b) / (i-3*b);
        }
        else if (r == Math.min(Math.min(r, g), b)){
            h = (b-r) / (i-3*r) + 1;
        }
        else{
            h = (r-g) / (i-3*g) + 2;
        }
        
        double s;
        if(h >= 0 && h <= 1){
            s = (i-3*b) / i;
        }
        else if(h >= 1 && h <= 2){
            s = (i-3*r) / i;
        }
        else{
            s = (i-3*g) / i;
        }
        
        return new double[] {i,h,s};
        
    }
    
    /**
     * IHS -> RGB
     * @param ihs IHS vector.
     * @return RGB color space.
     */
    public static double[] IHStoRGB(double[] ihs){
        
        if(ihs[1] >= 0 && ihs[1] <= 1){
            double r = ihs[0] * (1 + 2*ihs[2]-3*ihs[2]*ihs[1]) / 3;
            double g = ihs[0] * (1 - ihs[2]+3*ihs[2]*ihs[1]) / 3;
            double b = ihs[0] * (1 - ihs[2]) / 3;
            return new double[] {r*255,g*255,b*255};
        }
        else if(ihs[1] >= 1 && ihs[1] <= 2){
            double r = ihs[0] * (1 - ihs[2]) / 3;
            double g = ihs[0] * (1 + 2*ihs[2] - 3*ihs[2]*(ihs[1] - 1)) / 3;
            double b = ihs[0] * (1 - ihs[2] + 3*ihs[2]*(ihs[1] - 1)) / 3;
            return new double[] {r*255,g*255,b*255};
        }
        else{
            double r = ihs[0] * (1 - ihs[2] + 3*ihs[2]*(ihs[1] - 2)) / 3;
            double g = ihs[0] * (1 - ihs[2]) / 3;
            double b = ihs[0] * (1 + 2*ihs[2] - 3*ihs[2]*(ihs[1] - 2)) / 3;
            return new double[] {r*255,g*255,b*255};
        }
    }
    
    /**
     * RGB -> YUV.
     * @param color Color.
     * @return YUV color space.
     */
    public static double[] RGBtoYUV(Color color){
        return RGBtoYUV(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> YUV.
     * Y in the range [0..1].
     * U in the range [-0.5..0.5].
     * V in the range [-0.5..0.5].
     * @param red Values in the range [0..255].
     * @param green Values in the range [0..255].
     * @param blue Values in the range [0..255].
     * @return YUV color space.
     */
    public static double[] RGBtoYUV(int red, int green, int blue){
        
        double r = (double)red / 255;
        double g = (double)green / 255;
        double b = (double)blue / 255;
        
        double[] yuv = new double[3];
        double y,u,v;
        
        y = (double)(0.299 * r + 0.587 * g + 0.114 * b);
        u = (double)(-0.14713 * r - 0.28886 * g + 0.436 * b);
        v = (double)(0.615 * r - 0.51499 * g - 0.10001 * b);
        
        yuv[0] = y;
        yuv[1] = u;
        yuv[2] = v;
        
        return yuv;
    }
    
    /**
     * YUV -> RGB.
     * @param y Luma. In the range [0..1].
     * @param u Chrominance. In the range [-0.5..0.5].
     * @param v Chrominance. In the range [-0.5..0.5].
     * @return RGB color space.
     */
    public static int[] YUVtoRGB(double y, double u, double v){
        int[] rgb = new int[3];
        double r,g,b;
        
        r = (double)((y + 0.000 * u + 1.140 * v) * 255);
        g = (double)((y - 0.396 * u - 0.581 * v) * 255);
        b = (double)((y + 2.029 * u + 0.000 * v) * 255);
        
        rgb[0] = (int)r;
        rgb[1] = (int)g;
        rgb[2] = (int)b;
        
        return rgb;
    }
    
    /**
     * RGB -> YIQ.
     * @param color Color.
     * @return YIQ color space.
     */
    public static double[] RGBtoYIQ(Color color){
        return RGBtoYIQ(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> YIQ.
     * @param red Values in the range [0..255].
     * @param green Values in the range [0..255].
     * @param blue Values in the range [0..255].
     * @return YIQ color space.
     */
    public static double[] RGBtoYIQ(int red, int green, int blue){
        double[] yiq = new double[3];
        double y,i,q;
        
        double r = (double)red / 255;
        double g = (double)green / 255;
        double b = (double)blue / 255;
        
        y = (double)(0.299 * r + 0.587 * g + 0.114 * b);
        i = (double)(0.596 * r - 0.275 * g - 0.322 * b);
        q = (double)(0.212 * r - 0.523 * g + 0.311 * b);
        
        yiq[0] = y;
        yiq[1] = i;
        yiq[2] = q;
        
        return yiq;
    }
    
    /**
     * YIQ -> RGB.
     * @param y Luma. Values in the range [0..1].
     * @param i In-phase. Values in the range [-0.5..0.5].
     * @param q Quadrature. Values in the range [-0.5..0.5].
     * @return RGB color space.
     */
    public static int[] YIQtoRGB(double y, double i, double q){
        int[] rgb = new int[3];
        int r,g,b;
        
        r = (int)((y + 0.956 * i + 0.621 * q) * 255);
        g = (int)((y - 0.272 * i - 0.647 * q) * 255);
        b = (int)((y - 1.105 * i + 1.702 * q) * 255);
        
        r = Math.max(0,Math.min(255,r));
        g = Math.max(0,Math.min(255,g));
        b = Math.max(0,Math.min(255,b));
        
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        
        return rgb;
    }
    
    public static double[] RGBtoYCbCr(Color color, YCbCrColorSpace colorSpace){
        return RGBtoYCbCr(color.r, color.g, color.b, colorSpace);
    }
    
    public static double[] RGBtoYCbCr(int red, int green, int blue, YCbCrColorSpace colorSpace){
        
        double r = (double)red / 255;
        double g = (double)green / 255;
        double b = (double)blue / 255;
        
        double[] YCbCr = new double[3];
        double y,cb,cr;
        
        if (colorSpace == YCbCrColorSpace.ITU_BT_601) {
            y = (double)(0.299 * r + 0.587 * g + 0.114 * b);
            cb = (double)(-0.169 * r - 0.331 * g + 0.500 * b);
            cr = (double)(0.500 * r - 0.419 * g - 0.081 * b);
        }
        else{
            y = (double)(0.2215 * r + 0.7154 * g + 0.0721 * b);
            cb = (double)(-0.1145 * r - 0.3855 * g + 0.5000 * b);
            cr = (double)(0.5016 * r - 0.4556 * g - 0.0459 * b);
        }
        
        YCbCr[0] = (double)y;
        YCbCr[1] = (double)cb;
        YCbCr[2] = (double)cr;
        
        return YCbCr;
    }
    
    public static int[] YCbCrtoRGB(double y, double cb, double cr, YCbCrColorSpace colorSpace){
        int[] rgb = new int[3];
        double r,g,b;
        
        if (colorSpace == YCbCrColorSpace.ITU_BT_601) {
            r = (double)(y + 0.000 * cb + 1.403 * cr) * 255;
            g = (double)(y - 0.344 * cb - 0.714 * cr) * 255;
            b = (double)(y + 1.773 * cb + 0.000 * cr) * 255;
        }
        else{
            r = (double)(y + 0.000 * cb + 1.5701 * cr) * 255;
            g = (double)(y - 0.1870 * cb - 0.4664 * cr) * 255;
            b = (double)(y + 1.8556 * cb + 0.000 * cr) * 255;
        }
        
        rgb[0] = (int)r;
        rgb[1] = (int)g;
        rgb[2] = (int)b;
        
        return rgb;
    }
    
    /**
     * Rg-Chromaticity space is already known to remove ambiguities due to illumination or surface pose.
     * @param color Color.
     * @return Normalized RGChromaticity. Range[0..1].
     */
    public static double[] RGChromaticity(Color color){
        return RGChromaticity(color.r, color.g, color.b);
    }
    
    /**
     * Rg-Chromaticity space is already known to remove ambiguities due to illumination or surface pose.
     * @see Neural Information Processing - Chi Sing Leung. p. 668
     * @param red Red coefficient.
     * @param green Green coefficient.
     * @param blue Blue coefficient.
     * @return Normalized RGChromaticity. Range[0..1].
     */
    public static double[] RGChromaticity(int red, int green, int blue){
        double[] color = new double[5];
        
        double sum = red + green + blue;
        
        //red
        color[0] = red / sum;
        
        //green
        color[1] = green / sum;
        
        //blue
        color[2] = 1 - color[0] - color[1];
        
        double rS = color[0] - 0.333;
        double gS = color[1] - 0.333;
        
        //saturation
        color[3] = Math.sqrt(rS * rS + gS * gS);
        
        //hue
        color[4] = Math.atan(rS / gS);
        
        return color;
    }
    
    /**
     * RGB -> HSV.
     * Adds (hue + 360) % 360 for represent hue in the range [0..359].
     * @param color Color.
     * @return HSV color space.
     */
    public static double[] RGBtoHSV(Color color){
        return RGBtoHSV(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> HSV.
     * Adds (hue + 360) % 360 for represent hue in the range [0..359].
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return HSV color space.
     */
    public static double[] RGBtoHSV(int red, int green, int blue){
        double[] hsv = new double[3];
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double delta = max - min;
        
        // Hue
        if (max == min){
            hsv[0] = 0;
        }
        else if (max == r){
            hsv[0] = ((g - b) / delta) * 60f;
        }
        else if (max == g){
            hsv[0] = ((b - r) / delta + 2f) * 60f;
        }
        else if (max == b){
            hsv[0] = ((r - g) / delta + 4f) * 60f;
        }
        
        // Saturation
        if (delta == 0)
            hsv[1] = 0;
        else
            hsv[1] = delta / max;
        
        //Value
        hsv[2] = max;
        
        return hsv;
    }
    
    /**
     * HSV -> RGB.
     * @param hue Hue.
     * @param saturation Saturation. In the range[0..1].
     * @param value Value. In the range[0..1].
     * @return RGB color space. In the range[0..255].
     */
    public static int[] HSVtoRGB(double hue, double saturation, double value){
        int[] rgb = new int[3];
        
        double hi = (double)Math.floor(hue / 60.0) % 6;
        double f =  (double)((hue / 60.0) - Math.floor(hue / 60.0));
        double p = (double)(value * (1.0 - saturation));
        double q = (double)(value * (1.0 - (f * saturation)));
        double t = (double)(value * (1.0 - ((1.0 - f) * saturation)));
        
        if (hi == 0){
            rgb[0] = (int)(value * 255);
            rgb[1] = (int)(t * 255);
            rgb[2] = (int)(p * 255);
        }
        else if (hi == 1){
            rgb[0] = (int)(q * 255);
            rgb[1] = (int)(value * 255);
            rgb[2] = (int)(p * 255);
        }
        else if (hi == 2){
            rgb[0] = (int)(p * 255);
            rgb[1] = (int)(value * 255);
            rgb[2] = (int)(t * 255);
        }
        else if (hi == 3){
            rgb[0] = (int)(p * 255);
            rgb[1] = (int)(value * 255);
            rgb[2] = (int)(q * 255);
        }
        else if (hi == 4){
            rgb[0] = (int)(t * 255);
            rgb[1] = (int)(value * 255);
            rgb[2] = (int)(p * 255);
        }
        else if (hi == 5){
            rgb[0] = (int)(value * 255);
            rgb[1] = (int)(p * 255);
            rgb[2] = (int)(q * 255);
        }
        
        return rgb;
    }
    
    /**
     * RGB -> YCC.
     * @param color Color.
     * @return YCC color space. In the range [0..1].
     */
    public static double[] RGBtoYCC(Color color){
        return RGBtoYCC(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> YCC.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return YCC color space. In the range [0..1].
     */
    public static double[] RGBtoYCC(int red, int green, int blue){
        double[] ycc = new double[3];
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        double y = 0.213f * r + 0.419f * g + 0.081f * b;
        double c1 = -0.131f * r - 0.256f * g + 0.387f * b + 0.612f;
        double c2 = 0.373f * r - 0.312f * r - 0.061f * b + 0.537f;
        
        ycc[0] = y;
        ycc[1] = c1;
        ycc[2] = c2;
        
        return ycc;
    }
    
    /**
     * YCC -> RGB.
     * @param y Y coefficient.
     * @param c1 C coefficient.
     * @param c2 C coefficient.
     * @return RGB color space.
     */
    public static int[] YCCtoRGB(double y, double c1, double c2){
        int[] rgb = new int[3];
        
        double r = 0.981f * y + 1.315f * (c2 - 0.537f);
        double g = 0.981f * y - 0.311f * (c1 - 0.612f)- 0.669f * (c2 - 0.537f);
        double b = 0.981f * y + 1.601f * (c1 - 0.612f);
        
        rgb[0] = (int)(r * 255f);
        rgb[1] = (int)(g * 255f);
        rgb[2] = (int)(b * 255f);
        
        return rgb;
    }
    
    /**
     * RGB -> YCoCg.
     * @param color Color.
     * @return YCoCg color space.
     */
    public static double[] RGBtoYCoCg(Color color){
        return RGBtoYCoCg(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> YCoCg.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return YCoCg color space.
     */
    public static double[] RGBtoYCoCg(int red, int green, int blue){
        double[] yCoCg = new double[3];
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        double y = r / 4f + g / 2f + b / 4f;
        double co = r / 2f - b / 2f;
        double cg = -r / 4f + g / 2f - b / 4f;
        
        yCoCg[0] = y;
        yCoCg[1] = co;
        yCoCg[2] = cg;
        
        return yCoCg;
    }
    
    /**
     * YCoCg -> RGB.
     * @param y Pseudo luminance, or intensity.
     * @param co Orange chrominance.
     * @param cg Green chrominance.
     * @return RGB color space.
     */
    public static int[] YCoCgtoRGB(double y, double co, double cg){
        int[] rgb = new int[3];
        
        double r = y + co - cg;
        double g = y + cg;
        double b = y - co - cg;
        
        rgb[0] = (int)(r * 255f);
        rgb[1] = (int)(g * 255f);
        rgb[2] = (int)(b * 255f);
        
        return rgb;
    }
    
    /**
     * RGB -> XYZ.
     * @param color Color.
     * @return XYZ color space.
     */
    public static double[] RGBtoXYZ(Color color){
        return RGBtoXYZ(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> XYZ.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return XYZ color space.
     */
    public static double[] RGBtoXYZ(int red, int green, int blue){
        double[] xyz = new double[3];
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        //R
        if ( r > 0.04045)
            r = (double)Math.pow(( ( r + 0.055f ) / 1.055f ), 2.4f);
        else
            r /= 12.92f;
        
        //G
        if ( g > 0.04045)
            g = (double)Math.pow(( ( g + 0.055f ) / 1.055f ), 2.4f);
        else
            g /= 12.92f;
        
        //B
        if ( b > 0.04045)
            b = (double)Math.pow(( ( b + 0.055f ) / 1.055f ), 2.4f);
        else
            b /= 12.92f;
        
        r *= 100;
        g *= 100;
        b *= 100;
        
        double x = 0.412453f * r + 0.35758f * g + 0.180423f * b;
        double y = 0.212671f * r + 0.71516f * g + 0.072169f * b;
        double z = 0.019334f * r + 0.119193f * g + 0.950227f * b;
        
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        
        return xyz;
    }
    
    /**
     * XYZ -> RGB
     * @param x X coefficient.
     * @param y Y coefficient.
     * @param z Z coefficient.
     * @return RGB color space.
     */
    public static int[] XYZtoRGB(double x, double y, double z){
        int[] rgb = new int[3];
        
        x /= 100;
        y /= 100;
        z /= 100;
        
        double r = 3.240479f * x - 1.53715f * y - 0.498535f * z;
        double g = -0.969256f * x + 1.875991f * y + 0.041556f * z;
        double b = 0.055648f * x - 0.204043f * y + 1.057311f * z;
        
        if ( r > 0.0031308 )
            r = 1.055f * ( (double)Math.pow(r, 0.4166f) ) - 0.055f;
        else
            r = 12.92f * r;
        
        if ( g > 0.0031308 )
            g = 1.055f * ( (double)Math.pow(g, 0.4166f) ) - 0.055f;
        else
            g = 12.92f * g;
        
        if ( b > 0.0031308 )
            b = 1.055f * ( (double)Math.pow(b, 0.4166f) ) - 0.055f;
        else
            b = 12.92f * b;
        
        rgb[0] = (int)(r * 255);
        rgb[1] = (int)(g * 255);
        rgb[2] = (int)(b * 255);
        
        return rgb;
    }
    
    /**
     * XYZ -> HunterLAB
     * @param x X coefficient.
     * @param y Y coefficient.
     * @param z Z coefficient.
     * @return HunterLab coefficient.
     */
    public static double[] XYZtoHunterLAB(double x, double y, double z){
        double[] hunter = new double[3];
        
        
        double sqrt = (double)Math.sqrt(y);
        
        double l = 10 * sqrt;
        double a = 17.5f * (((1.02f * x) - y) / sqrt);
        double b = 7f * ((y - (0.847f * z)) / sqrt);
        
        hunter[0] = l;
        hunter[1] = a;
        hunter[2] = b;
        
        return hunter;
    }
    
    /**
     * HunterLAB -> XYZ
     * @param l L coefficient.
     * @param a A coefficient.
     * @param b B coefficient.
     * @return XYZ color space.
     */
    public static double[] HunterLABtoXYZ(double l, double a, double b){
        double[] xyz = new double[3];
        
        
        double tempY = l / 10f;
        double tempX = a / 17.5f * l / 10f;
        double tempZ = b / 7f * l / 10f;
        
        double y = tempY * tempY;
        double x = (tempX + y) / 1.02f;
        double z = -(tempZ - y) / 0.847f;
        
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        
        return xyz;
    }
    
    /**
     * RGB -> HunterLAB.
     * @param color Color.
     * @return HunterLAB color space.
     */
    public static double[] RGBtoHunterLAB(Color color){
        return RGBtoHunterLAB(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> HunterLAB.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return HunterLAB color space.
     */
    public static double[] RGBtoHunterLAB(int red, int green, int blue){
        double[] xyz = RGBtoXYZ(red, green, blue);
        return XYZtoHunterLAB(xyz[0], xyz[1], xyz[2]);
    }
    
    /**
     * HunterLAB -> RGB.
     * @param l L coefficient.
     * @param a A coefficient.
     * @param b B coefficient.
     * @return RGB color space.
     */
    public static int[] HunterLABtoRGB(double l, double a, double b){
        double[] xyz = HunterLABtoXYZ(l, a, b);
        return XYZtoRGB(xyz[0], xyz[1], xyz[2]);
    }
    
    /**
     * RGB -> HSL.
     * @param color Color.
     * @return HLS color space.
     */
    public static double[] RGBtoHSL(Color color){
        return RGBtoHSL(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> HSL.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return HLS color space.
     */
    public static double[] RGBtoHSL(int red, int green, int blue){
        double[] hsl = new double[3];
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        double max = Math.max(r,Math.max(g,b));
        double min = Math.min(r,Math.min(g,b));
        double delta = max - min;
        
        //HSK
        double h = 0;
        double s = 0;
        double l = (max + min) / 2;
        
        if ( delta == 0 ){
            // gray color
            h = 0;
            s = 0.0f;
        }
        else
        {
            // get saturation value
            s = ( l <= 0.5 ) ? ( delta / ( max + min ) ) : ( delta / ( 2f - max - min ) );

            // get hue value
            double hue;

            if ( r == max )
            {
                hue = ( ( g - b ) / 6f ) / delta;
            }
            else if ( g == max )
            {
                hue = ( 1.0f / 3f ) + ( ( b - r ) / 6f ) / delta; 
            }
            else
            {
                hue = ( 2.0f / 3f ) + ( ( r - g ) / 6f ) / delta;
            }

            // correct hue if needed
            if ( hue < 0 )
                hue += 1;
            if ( hue > 1 )
                hue -= 1;

            h = (int) ( hue * 360f );
        }
        
        hsl[0] = h;
        hsl[1] = s;
        hsl[2] = l;
        
        return hsl;
    }
    
    /**
     * HLS -> RGB.
     * @param hue Hue.
     * @param saturation Saturation.
     * @param luminance Luminance.
     * @return RGB color space.
     */
    public static int[] HSLtoRGB(double hue, double saturation, double luminance){
        int[] rgb = new int[3];
        double r = 0, g = 0, b = 0;
        
        if ( saturation == 0 )
        {
            // gray values
            r = g = b = (int) ( luminance * 255 );
        }
        else
        {
            double v1, v2;
            double h = (double) hue / 360;

            v2 = ( luminance < 0.5 ) ?
                ( luminance * ( 1 + saturation ) ) :
                ( ( luminance + saturation ) - ( luminance * saturation ) );
            v1 = 2 * luminance - v2;

            r = (int) ( 255 * Hue_2_RGB( v1, v2, h + ( 1.0f / 3 ) ) );
            g = (int) ( 255 * Hue_2_RGB( v1, v2, h ) );
            b = (int) ( 255 * Hue_2_RGB( v1, v2, h - ( 1.0f / 3 ) ) );
        }
        
        rgb[0] = (int)r;
        rgb[1] = (int)g;
        rgb[2] = (int)b;
        
        return rgb;
    }
    
    private static double Hue_2_RGB( double v1, double v2, double vH ){
        if ( vH < 0 )
            vH += 1;
        if ( vH > 1 )
            vH -= 1;
        if ( ( 6 * vH ) < 1 )
            return ( v1 + ( v2 - v1 ) * 6 * vH );
        if ( ( 2 * vH ) < 1 )
            return v2;
        if ( ( 3 * vH ) < 2 )
            return ( v1 + ( v2 - v1 ) * ( ( 2.0f / 3 ) - vH ) * 6 );
        return v1;
    }
    
    /**
     * RGB -> CIE-LAB.
     * @param color Color.
     * @param tristimulus XYZ Tristimulus.
     * @return CIE-LAB color space.
     */
    public static double[] RGBtoLAB(Color color, double[] tristimulus){
        return RGBtoLAB(color.r, color.g, color.b, tristimulus);
    }
    
    /**
     * RGB -> CIE-LAB.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @param tristimulus XYZ Tristimulus.
     * @return CIE-LAB color space.
     */
    public static double[] RGBtoLAB(int red, int green, int blue, double[] tristimulus){
        double[] xyz = RGBtoXYZ(red, green, blue);
        double[] lab = XYZtoLAB(xyz[0], xyz[1], xyz[2], tristimulus);
        
        return lab;
    }
    
    /**
     * CIE-LAB -> RGB.
     * @param l L coefficient.
     * @param a A coefficient.
     * @param b B coefficient.
     * @param tristimulus XYZ Tristimulus.
     * @return RGB color space.
     */
    public static int[] LABtoRGB(double l, double a, double b, double[] tristimulus){
        double[] xyz = LABtoXYZ(l, a, b, tristimulus);
        return XYZtoRGB(xyz[0], xyz[1], xyz[2]);
    }
    
    /**
     * XYZ -> CIE-LAB.
     * @param x X coefficient.
     * @param y Y coefficient.
     * @param z Z coefficient.
     * @param tristimulus XYZ Tristimulus.
     * @return CIE-LAB color space.
     */
    public static double[] XYZtoLAB(double x, double y, double z, double[] tristimulus){
        double[] lab = new double[3];
        
        x /= tristimulus[0];
        y /= tristimulus[1];
        z /= tristimulus[2];
        
        if (x > 0.008856)
            x = (double)Math.pow(x,0.33f);
        else
            x = (7.787f * x) + ( 0.1379310344827586f );
        
        if (y > 0.008856)
            y = (double)Math.pow(y,0.33f);
        else
            y = (7.787f * y) + ( 0.1379310344827586f );
        
        if (z > 0.008856)
            z = (double)Math.pow(z,0.33f);
        else
            z = (7.787f * z) + ( 0.1379310344827586f );
        
        lab[0] = ( 116 * y ) - 16;
        lab[1] = 500 * ( x - y );
        lab[2] = 200 * ( y - z );
        
        return lab;
    }
    
    /**
     * CIE-LAB -> XYZ.
     * @param l L coefficient.
     * @param a A coefficient.
     * @param b B coefficient.
     * @param tristimulus XYZ Tristimulus.
     * @return XYZ color space.
     */
    public static double[] LABtoXYZ(double l, double a, double b, double[] tristimulus){
        double[] xyz = new double[3];
        
        double y = ( l + 16f ) / 116f;
        double x = a / 500f + y;
        double z = y - b / 200f;
        
        //Y
        if ( Math.pow(y,3) > 0.008856 )
            y = (double)Math.pow(y,3);
        else
            y = (double)(( y - 16 / 116 ) / 7.787);
        
        //X
        if ( Math.pow(x,3) > 0.008856 )
            x = (double)Math.pow(x,3);
        else
            x = (double)(( x - 16 / 116 ) / 7.787);
        
        // Z
        if ( Math.pow(z,3) > 0.008856 )
            z = (double)Math.pow(z,3);
        else
            z = (double)(( z - 16 / 116 ) / 7.787);
        
        xyz[0] = x * tristimulus[0];
        xyz[1] = y * tristimulus[1];
        xyz[2] = z * tristimulus[2];
        
        return xyz;
    }
    
    /**
     * RGB -> C1C2C3.
     * @param color Color.
     * @return C1C2C3 color space.
     */
    public static double[] RGBtoC1C2C3(Color color){
        return RGBtoC1C2C3(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> C1C2C3.
     * @param r Red coefficient. Values in the range [0..255].
     * @param g Green coefficient. Values in the range [0..255].
     * @param b Blue coefficient. Values in the range [0..255].
     * @return C1C2C3 color space.
     */
    public static double[] RGBtoC1C2C3(int r, int g, int b){
        
        double[] c = new double[3];
        
        c[0] = (double)Math.atan(r / Math.max(g, b));
        c[1] = (double)Math.atan(g / Math.max(r, b));
        c[2] = (double)Math.atan(b / Math.max(r, g));
        
        return c;
        
    }
    
    /**
     * RGB -> O1O2.
     * @param color Color.
     * @return O1O2 color space.
     */
    public static double[] RGBtoO1O2(Color color){
        return RGBtoO1O2(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> O1O2.
     * @param r Red coefficient. Values in the range [0..255].
     * @param g Green coefficient. Values in the range [0..255].
     * @param b Blue coefficient. Values in the range [0..255].
     * @return O1O2 color space.
     */
    public static double[] RGBtoO1O2(int r, int g, int b){
        
        double[] o = new double[2];
        
        o[0] = (r - g) / 2f;
        o[1] = (r + g) / 4f - (b / 2f);
        
        return o;
        
    }
    
    /**
     * RGB -> Grayscale.
     * @param color Color.
     * @return Grayscale color space.
     */
    public static double RGBtoGrayscale(Color color){
        return RGBtoGrayscale(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> Grayscale.
     * @param r Red coefficient. Values in the range [0..255].
     * @param g Green coefficient. Values in the range [0..255].
     * @param b Blue coefficient. Values in the range [0..255].
     * @return Grayscale color space.
     */
    public static double RGBtoGrayscale(int r, int g, int b){
        return r*0.2125f + g*0.7154f + b*0.0721f;
    }
}
