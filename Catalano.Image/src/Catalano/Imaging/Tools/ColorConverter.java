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
import Catalano.Math.Matrix;

/**
 * Convert between different color spaces supported.
 * 
 * RGB -> CIE-L*A*B* -> RGB
 * RGB -> CIE-L*c*h -> RGB
 * RGB -> CMYK -> RGB
 * RGB -> IHS -> RGB
 * RGB -> HLS -> RGB
 * RGB -> HunterLAB -> RGB
 * RGB -> HSV -> RGB
 * RGB -> RGChromaticity
 * RGB -> XYZ -> RGB
 * RGB -> YCbCr -> RGB
 * RGB -> YCC -> RGB
 * RGB -> YCoCg -> RGB
 * RGB -> YES -> RGB
 * RGB -> YIQ -> RGB
 * RGB -> YUV -> RGB
 * XYZ -> CIE-L*A*B* -> XYZ
 * XYZ -> HunterLAB -> XYZ
 * XYZ -> LMS -> XYZ
 * XYZ -> xyY -> XYZ
 * 
 * @author Diego Catalano
 */
public class ColorConverter {
    
    //HPE forward
    private static final double[][] hpe_f = new double[][]{
        {0.38971, 0.68898,-0.07868},
        {-0.22981, 1.18340, 0.04641},
        {0.00000, 0.00000, 1.00000}
    };
    
    //HPE backward
    private static final double[][] hpe_b = new double[][]{
        {1.91020, -1.11212, 0.20191},
        {0.37095, 0.62905, -0.00001},
        {0.00000, 0.00000, 1.00000}
    };
    
    //Bradford forward
    private static final double[][] bradford_f = new double[][]{
        {0.8951000,0.2664000,-0.1614000},
        {-0.7502000,1.7135000,0.0367000},
        {0.0389000,-0.0685000,1.0296000}
    };
    
    //Bradford backward
    private static final double[][] bradford_b = new double[][]{
        {0.9869929,-0.1470543,0.1599627},
        {0.4323053,0.5183603,0.0492912},
        {-0.0085287,0.0400428,0.9684867}
    };
    
    //VonKries forward
    private static final double[][] vonkries_f = new double[][]{
        {0.4002, 0.7076, -0.0808},
        {-0.2263, 1.1653, 0.0457},
        {0, 0, 0.9182}
    };
    
    //VonKries backward
    private static final double[][] vonkries_b = new double[][]{
        {1.86007, -1.12948, 0.21990},
        {0.36122, 0.63880, -0.00001},
        {0.00000, 0.00000, 1.08909}
    };
    
    //CAT97 forward
    private static final double[][] cat97_f = new double[][]{
        {0.8562, 0.3372, -0.1934},
        {-0.8360, 1.8327, 0.0033},
        {0.0357, -0.00469, 1.0112}
    };
    
    //CAT97 backward
    private static final double[][] cat97_b = new double[][]{
        {0.9838112, -0.1805292, 0.1887508},
        {0.4488317, 0.4632779, 0.0843307},
        {-0.0326513, 0.0085222, 0.9826514}
    };
    
    //CAT02 forward
    private static final double[][] cat02_f = new double[][]{
        {0.7328, 0.4296, -0.1624},
        {-0.7036, 1.6975, 0.0061},
        {0.0030, 0.0136, 0.9834}
    };
    
    //CAT02 backward
    private static final double[][] cat02_b = new double[][]{
        {1.0961238, -0.2788690, 0.1827452},
        {0.4543690, 0.4735332, 0.0720978},
        {-0.0096276, -0.0056980, 1.0153256}
    };
    
    /**
     * LMS Transformation matrix.
     */
    public static enum LMS{
        
        /**
         * Hunt-Pointer-Estevez.
         */
        HPE,
        
        /**
         * Bradford.
         */
        Bradford,
        
        /**
         * Von Kries.
         */
        VonKries,
        
        /**
         * CIECAM97s.
         */
        CAT97,
        
        /**
         * CIECAM02.
         */
        CAT02
    }
    
    public static enum YCbCrColorSpace {ITU_BT_601,ITU_BT_709_HDTV};
    
    //Used in CIE-LAB conversions
    private static double k = 903.2962962962963; //24389/27
    private static double e = 0.0088564516790356; //216/24389

    /**
     * Don't let anyone instantiate this class.
     */
    private ColorConverter() {}
    
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
     * @param rgb RGB values.
     * @return CMYK color space. Normalized.
     */
    public static double[] RGBtoCMYK(int[] rgb){
        return RGBtoCMYK(rgb[0], rgb[1], rgb[2]);
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
     * @param cmyk CMYK values.
     * @return RGB color space.
     */
    public static int[] CMYKtoRGB(double[] cmyk){
        return CMYKtoRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
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
     * @param rgb RGB values.
     * @return RGB color space.
     */
    public static double[] RGBtoIHS(int[] rgb){
        return RGBtoIHS(rgb[0], rgb[1], rgb[2]);
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
     * RGB -> YES.
     * @param color Color.
     * @return YES color space.
     */
    public static double[] RGBtoYES(Color color){
        return RGBtoYES(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> YES.
     * @param rgb RGB values.
     * @return YES color space.
     */
    public static double[] RGBtoYES(int[] rgb){
        return RGBtoYES(rgb[0], rgb[1], rgb[2]);
    }
    
    /**
     * RGB -> YES.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return YES color space.
     */
    public static double[] RGBtoYES(int red, int green, int blue){
        double[] yes = new double[3];
        
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        
        yes[0] = r * 0.253 + g * 0.684 + b * 0.063;
        yes[1] = r * 0.500 + g * -0.500;
        yes[2] = r * 0.250 + g * 0.250 + b * -0.5;
        
        return yes;
    }
    
    /**
     * YES -> RGB
     * @param yes YES color space.
     * @return RGB color space.
     */
    public static int[] YEStoRGB(double[] yes){
        return YEStoRGB(yes[0], yes[1], yes[2]);
    }
    
    /**
     * YES -> RGB
     * @param y Luminance component. [0..1]
     * @param e Chrominance factor. Difference of red and green channels. [-0.5..0.5]
     * @param s Chrominance factor. Difference of yellow and blue. [-0.5..0.5]
     * @return RGB color space.
     */
    public static int[] YEStoRGB(double y, double e, double s){
        
        int[] rgb = new int[3];
        
        rgb[0] = (int)((y + e * 1.431 + s * 0.126) * 255);
        rgb[1] = (int)((y + e * -0.569 + s * 0.126) * 255);
        rgb[2] = (int)((y + e * 0.431 + s * -1.874) * 255);
        
        return rgb;
        
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
        return RGBtoXYZ(new int[]{red, green, blue});
    }
    
    /**
     * RGB -> XYZ.
     * @param rgb sRGB color space.
     * @return XYZ color space.
     */
    public static double[] RGBtoXYZ(int[] rgb){
        double[] xyz = new double[3];
        
        double r = rgb[0] / 255D;
        double g = rgb[1] / 255D;
        double b = rgb[2] / 255D;
        
        //R
        if ( r > 0.04045)
            r = (double)Math.pow(( ( r + 0.055D ) / 1.055D ), 2.4D);
        else
            r /= 12.92D;
        
        //G
        if ( g > 0.04045)
            g = (double)Math.pow(( ( g + 0.055D ) / 1.055D ), 2.4D);
        else
            g /= 12.92D;
        
        //B
        if ( b > 0.04045)
            b = (double)Math.pow(( ( b + 0.055D ) / 1.055D ), 2.4D);
        else
            b /= 12.92D;
        
        //Used for scale
        r *= 100;
        g *= 100;
        b *= 100;
        
        double x = 0.412453D * r + 0.35758D * g + 0.180423D * b;
        double y = 0.212671D * r + 0.71516D * g + 0.072169D * b;
        double z = 0.019334D * r + 0.119193D * g + 0.950227D * b;
        
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
        return XYZtoRGB(new double[]{x,y,z});
    }
    
    /**
     * XYZ -> RGB
     * @param xyz XYZ color space.
     * @return sRGB color space.
     */
    public static int[] XYZtoRGB(double[] xyz){
        int[] rgb = new int[3];
        
        //Used for scale
        double x = xyz[0] / 100;
        double y = xyz[1] / 100;
        double z = xyz[2] / 100;
        
        double r = 3.240479D * x - 1.53715D * y - 0.498535D * z;
        double g = -0.969256D * x + 1.875991D * y + 0.041556D * z;
        double b = 0.055648D * x - 0.204043D * y + 1.057311D * z;
        
        if ( r > 0.0031308 )
            r = 1.055 * Math.pow(r, 1 / 2.4) - 0.055;
        else
            r = 12.92D * r;
        
        if ( g > 0.0031308 )
            g = 1.055 * Math.pow(g, 1 / 2.4) - 0.055;
        else
            g = 12.92D * g;
        
        if ( b > 0.0031308 )
            b = 1.055 * Math.pow(b, 1 / 2.4) - 0.055;
        else
            b = 12.92D * b;
        
        r = r < 0 ? 0 : r;
        g = g < 0 ? 0 : g;
        b = b < 0 ? 0 : b;
        
        r = r > 255 ? 255 : r;
        g = g > 255 ? 255 : g;
        b = b > 255 ? 255 : b;
        
        rgb[0] = (int)Math.round(r * 255);
        rgb[1] = (int)Math.round(g * 255);
        rgb[2] = (int)Math.round(b * 255);
        
        return rgb;
    }
    
    /**
     * XYZ to xyY color space.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return xyY color space.
     */
    public static double[] XYZtoXyY(double x, double y, double z){
        return XYZtoXyY(new double[]{x,y,z});
    }
    
    /**
     * XYZ to xyY color space.
     * @param xyz XYZ color space.
     * @return xyY color space.
     */
    public static double[] XYZtoXyY(double[] xyz){
        double[] xyy = new double[3];
        
        double sum = xyz[0] + xyz[1] + xyz[2];
        xyy[0] = xyz[0] / sum;
        xyy[1] = xyz[1] / sum;
        xyy[2] = xyz[1];
        
        return xyy;
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
     * XYZ -> LMS
     * CIECAM02 transformation matrix default.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return LMS color space.
     */
    public static double[] XYZtoLMS(double x, double y, double z){
        return XYZtoLMS(new double[]{x,y,z}, LMS.CAT02);
    }
    
    /**
     * XYZ -> LMS
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param matrix LMS transformation matrix.
     * @return LMS color space.
     */
    public static double[] XYZtoLMS(double x, double y, double z, LMS matrix){
        return XYZtoLMS(new double[] {x,y,z}, matrix);
    }
    
    /**
     * XYZ -> LMS
     * CIECAM02 transformation matrix default.
     * @param xyz XYZ Color space.
     * @return LMS color space.
     */
    public static double[] XYZtoLMS(double[] xyz){
        return XYZtoLMS(xyz, LMS.CAT02);
    }
    
    /**
     * XYZ -> LMS
     * @param xyz XYZ color space.
     * @param matrix LMS transformation matrix.
     * @return LMS color space.
     */
    public static double[] XYZtoLMS(double[] xyz, LMS matrix){
        
        switch(matrix){
            case HPE:
                return Matrix.Multiply(xyz, hpe_f);
            case Bradford:
                return Matrix.Multiply(xyz, bradford_f);
            case VonKries:
                return Matrix.Multiply(xyz, vonkries_f);
            case CAT97:
                return Matrix.Multiply(xyz, cat97_f);
            default:
                return Matrix.Multiply(xyz, cat02_f);
        }
        
    }
    
    /**
     * xyY to XYZ.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param Y Z coordinate.
     * @return XYZ color space.
     */
    public static double[] XYYtoXYZ(double x, double y, double Y){
        return XYYtoXYZ(new double[]{x,y,Y});
    }
    
    /**
     * xyY to XYZ.
     * @param xyY color space..
     * @return XYZ color space.
     */
    public static double[] XYYtoXYZ(double[] xyY){
        double x = (xyY[0] * xyY[2]) / xyY[1];
        double z = ((1D - xyY[0] - xyY[1]) * xyY[2]) / xyY[1];
        return new double[] {x, xyY[2], z};
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
     * RGB -> CIE L*C*h.
     * @param color Color.
     * @return CIE-L*c*h color space.
     */
    public static double[] RGBtoLCH(Color color){
        return RGBtoLCH(color.r, color.g, color.b);
    }
    
    /**
     * RGB -> CIE L*C*h.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return CIE L*C*h color space.
     */
    public static double[] RGBtoLCH(int red, int green, int blue){
        return RGBtoLCH(red, green, blue, Illuminant.CIE2.D65);
    }
    
    /**
     * RGB -> CIE L*C*h.
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @param tristimulus XYZ Tristimulus.
     * @return L*c*h color space.
     */
    public static double[] RGBtoLCH(int red, int green, int blue, double[] tristimulus){
        double[] lab = RGBtoLAB(red, green, blue, tristimulus);
        return LABtoLCH(lab[0], lab[1], lab[2]);
    }
    
    /**
     * RGB -> LMS color space.
     * CIECAM02 transformation matrix default.
     * 
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @return LMS color space.
     */
    public static double[] RGBtoLMS(int red, int green, int blue){
        return RGBtoLMS(new int[]{red, green, blue}, LMS.CAT02);
    }
    
    /**
     * RGB -> LMS color space.
     * 
     * @param red Red coefficient. Values in the range [0..255].
     * @param green Green coefficient. Values in the range [0..255].
     * @param blue Blue coefficient. Values in the range [0..255].
     * @param matrix LMS matrix.
     * @return LMS color space.
     */
    public static double[] RGBtoLMS(int red, int green, int blue, LMS matrix){
        return RGBtoLMS(new int[]{red, green, blue}, matrix);
    }
    
    /**
     * RGB -> LMS color space.
     * CIECAM02 transformation matrix default.
     * 
     * @param rgb sRGB color space.
     * @return LMS color space.
     */
    public static double[] RGBtoLMS(int[] rgb){
        return RGBtoLMS(rgb, LMS.CAT02);
    }
    
    /**
     * RGB -> LMS color space.
     * 
     * @param rgb sRGB color space.
     * @param matrix LMS matrix.
     * @return LMS color space.
     */
    public static double[] RGBtoLMS(int[] rgb, LMS matrix){
        double[] xyz = RGBtoXYZ(rgb);
        return XYZtoLMS(xyz, matrix);
    }
    
    /**
     * CIE-L*A*B* to RGB.
     * @param lab L*A*B* color space.
     * @return RGB color space.
     */
    public static int[] LABtoRGB(double[] lab){
        return LABtoRGB(lab[0], lab[1], lab[2], Illuminant.CIE2.D65);
    }
    
    /**
     * CIE-L*A*B* to RGB.
     * @param lab L*A*B* color space.
     * @param tristimulus XYZ Tristimulus.
     * @return RGB color space.
     */
    public static int[] LABtoRGB(double[] lab, double[] tristimulus){
        return LABtoRGB(lab[0], lab[1], lab[2], tristimulus);
    }
    
    /**
     * CIE-L*A*B* to RGB.
     * @param l L coefficient.
     * @param a a* coefficient.
     * @param b b* coefficient.
     * @return RGB color space.
     */
    public static int[] LABtoRGB(double l, double a, double b){
        return LABtoRGB(l, a, b, Illuminant.CIE2.D65);
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
     * CIE-LAB -> L*c*h
     * @param l L coefficient.
     * @param a A coefficient.
     * @param b B coefficient.
     * @return L*h*c color space.
     */
    public static double[] LABtoLCH(double l, double a, double b){
        
        double[] lch = new double[3];
        
        double h = Math.toDegrees(Math.atan2(b, a));
        if(h < 0) h += 360;
        
        lch[0] = l;
        lch[1] = Math.sqrt(a*a + b*b);
        lch[2] = h;
        
        return lch;
    }
    
    /**
     * LMS -> RGB.
     * @param l Long wavelength.
     * @param m Medium wavelength.
     * @param s Short wavelength.
     * @return sRGB color space.
     */
    public static int[] LMStoRGB(double l, double m, double s){
        return LMStoRGB(new double[] {l,m,s}, LMS.CAT02);
    }
    
    /**
     * LMS -> RGB.
     * @param l Long wavelength.
     * @param m Medium wavelength.
     * @param s Short wavelength.
     * @param matrix LMS transformation matrix.
     * @return sRGB color space.
     */
    public static int[] LMStoRGB(double l, double m, double s, LMS matrix){
        return LMStoRGB(new double[] {l,m,s}, matrix);
    }
    
    /**
     * LMS -> RGB.
     * @param lms LMS color space.
     * @return sRGB color space.
     */
    public static int[] LMStoRGB(double[] lms){
        return LMStoRGB(lms, LMS.CAT02);
    }
    
    /**
     * LMS -> RGB.
     * @param lms LMS color space.
     * @param matrix LMS transformation matrix.
     * @return sRGB color space.
     */
    public static int[] LMStoRGB(double[] lms, LMS matrix){
        double[] xyz = LMStoXYZ(lms, matrix);
        return XYZtoRGB(xyz);
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
        
        //Need divide tristimulus/100 if needs scale
        x /= tristimulus[0];
        y /= tristimulus[1];
        z /= tristimulus[2];
        
        if (x > 0.008856)
            x = (double)Math.pow(x,1/3D);
        else
            x = 7.787036 * x + 0.1379310344827586;
        
        if (y > 0.008856)
            y = (double)Math.pow(y,1/3D);
        else
            y = 7.787036 * y + 0.1379310344827586;
        
        if (z > 0.008856)
            z = (double)Math.pow(z,1/3D);
        else
            z = 7.787036 * z + 0.1379310344827586;
        
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
        
        double y = ( l + 16D ) / 116D;
        double x = (a / 500D) + y;
        double z = y - (b / 200D);
        
        //X
        if ( Math.pow(x,3) > e )
            x = (double)Math.pow(x,3);
        else
            x = (double)(116 * x - 16) / k;
        
        //Y
        if ( l > 8 )
            y = Math.pow(((l + 16) / 116D),3);
        else
            y = l / k;
        
        // Z
        if ( Math.pow(z,3) > e )
            z = (double)Math.pow(z,3);
        else
            z = (double)(116 * z - 16) / k;
        
        //Need divide tristimulus/100 if needs scale
        xyz[0] = x * tristimulus[0];
        xyz[1] = y * tristimulus[1];
        xyz[2] = z * tristimulus[2];
        
        return xyz;
    }
    
    /**
     * L*c*h -> CIE-L*A*B*
     * @param l L coefficient.
     * @param c *c coefficient.
     * @param h *h coefficient.
     * @return CIE-L*A*B* color space.
     */
    public static double[] LCHtoLAB(double l, double c, double h){
        
        double[] lab = new double[3];
        lab[0] = l;
        lab[1] = c * Math.cos(Math.toRadians(h));
        lab[2] = c * Math.sin(Math.toRadians(h));
        
        return lab;
    }
    
    /**
     * L*c*h -> RGB.
     * @param l L coefficient.
     * @param c *c coefficient.
     * @param h *h coefficient.
     * @return RGB color space.
     */
    public static int[] LCHtoRGB(double l, double c, double h){
        double[] lab = LCHtoLAB(l, c, h);
        return LABtoRGB(lab[0], lab[1], lab[2], Illuminant.CIE2.D65);
    }
    
    /**
     * LMS -> XYZ
     * @param l Long wavelengths.
     * @param m Medium wavelengths.
     * @param s Short wavelengths.
     * @param matrix Transformation matrix.
     * @return XYZ color space.
     */
    public static double[] LMStoXYZ(double l, double m, double s, LMS matrix){
        return LMStoXYZ(new double[] {l,m,s}, matrix);
    }
    
    /**
     * LMS -> XYZ
     * @param lms LMS color space.
     * @param matrix Transformation matrix.
     * @return XYZ color space.
     */
    public static double[] LMStoXYZ(double[] lms, LMS matrix){
        switch(matrix){
            case HPE:
                return Matrix.Multiply(lms, hpe_b);
            case Bradford:
                return Matrix.Multiply(lms, bradford_b);
            case VonKries:
                return Matrix.Multiply(lms, vonkries_b);
            case CAT97:
                return Matrix.Multiply(lms, cat97_b);
            default:
                return Matrix.Multiply(lms, cat02_b);
        }
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
