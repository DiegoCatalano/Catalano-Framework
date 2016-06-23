// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
//
// Copyright (c) 2011-2012 LTS2, EPFL
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification, 
// are permitted provided that the following conditions are met:
//
//    * Redistributions of source code must retain the above copyright notice, 
//      this list of conditions and the following disclaimer.
//    * Redistributions in binary form must reproduce the above copyright notice, 
//      this list of conditions and the following disclaimer in the documentation 
//      and/or other materials provided with the distribution.
//    * Neither the name of the CherryPy Team nor the names of its contributors 
//      may be used to endorse or promote products derived from this software 
//      without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//

package Catalano.Imaging.Corners.FREAK;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.IntegralImage;
import Catalano.Math.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Fast Retina Keypoint (FREAK) descriptor.
 * @author Diego Catalano
 */
public class FastRetinaKeypointDescriptor {
    
    private FastRetinaKeypointPattern pattern;
    
    private boolean isOrientationNormal;
    private boolean isScaleNormal;
    private boolean isExtended;
    private FastBitmap Image;
    public IntegralImage Integral;
    FastRetinaKeypoint temp;

    public boolean IsOrientationNormal() {
        return isOrientationNormal;
    }

    public void setOrientationNormal(boolean isOrientationNormal) {
        this.isOrientationNormal = isOrientationNormal;
    }

    public boolean IsScaleNormal() {
        return isScaleNormal;
    }

    public void setScaleNormal(boolean isScaleNormal) {
        this.isScaleNormal = isScaleNormal;
    }

    public boolean IsExtended() {
        return isExtended;
    }

    public void setExtended(boolean isExtended) {
        this.isExtended = isExtended;
    }
    
    FastRetinaKeypointDescriptor(FastBitmap fastBitmap, IntegralImage integral, FastRetinaKeypointPattern pattern){
        this.isExtended = false;
        this.isOrientationNormal = true;
        this.isScaleNormal = true;
        this.Image = fastBitmap;
        this.Integral = integral;

        this.pattern = pattern;
    }
    
        public void Compute(List<FastRetinaKeypoint> points)
        {
            final int CV_FREAK_SMALLEST_KP_SIZE = FastRetinaKeypointPattern.Size;
            final int CV_FREAK_NB_SCALES = FastRetinaKeypointPattern.Scales;
            final int CV_FREAK_NB_ORIENTATION = FastRetinaKeypointPattern.Orientations;

            int[] patternSizes = pattern.patternSizes;
            int[] pointsValues = pattern.pointsValues;
            FastRetinaKeypointPattern.OrientationPair[] orientationPairs = pattern.orientationPairs;
            FastRetinaKeypointPattern.DescriptionPair[] descriptionPairs = pattern.descriptionPairs;
            double step = pattern.step;


            // used to save pattern scale index corresponding to each keypoints
            ArrayList<Integer> scaleIndex = new ArrayList<Integer>(points.size());
            for (int i = 0; i < points.size(); i++)
                scaleIndex.add(0);


            // 1. Compute the scale index corresponding to the keypoint
            //  size and remove keypoints which are close to the border
            //
            if (isScaleNormal)
            {
                for (int k = points.size() - 1; k >= 0; k--)
                {
                    // Is k non-zero? If so, decrement it and continue.
                    double ratio = points.get(k).scale / CV_FREAK_SMALLEST_KP_SIZE;
                    scaleIndex.set(k, Math.max((int)(Math.log(ratio) * step + 0.5), 0));

                    if (scaleIndex.get(k) >= CV_FREAK_NB_SCALES)
                        scaleIndex.set(k, CV_FREAK_NB_SCALES - 1);

                    // Check if the description at this position and scale fits inside the image
                    if ((points.get(k).x <= patternSizes[scaleIndex.get(k)]) ||
                         points.get(k).y <= patternSizes[scaleIndex.get(k)] ||
                         points.get(k).x >= Image.getHeight()- patternSizes[scaleIndex.get(k)] ||
                         points.get(k).y >= Image.getWidth()- patternSizes[scaleIndex.get(k)])
                    {
                        points.remove(k);  // No, it doesn't. Remove the point.
                        scaleIndex.remove(k);
                    }
                }
            }

            else // if (!IsScaleNormal)
            {
                int scale = Math.max((int)(Constants.Log3 * step + 0.5), 0);

                for (int k = points.size() - 1; k >= 0; k--)
                {
                    // equivalent to the formule when the scale is normalized with
                    // a constant size of keypoints[k].size = 3 * SMALLEST_KP_SIZE

                    scaleIndex.set(k, scale);
                    if (scaleIndex.get(k) >= CV_FREAK_NB_SCALES)
                        scaleIndex.set(k, CV_FREAK_NB_SCALES - 1);

                    if ((points.get(k).x <= patternSizes[scaleIndex.get(k)]) ||
                         points.get(k).y <= patternSizes[scaleIndex.get(k)] ||
                         points.get(k).x >= Image.getHeight()- patternSizes[scaleIndex.get(k)] ||
                         points.get(k).y >= Image.getWidth()- patternSizes[scaleIndex.get(k)])
                    {
                        points.remove(k);
                        scaleIndex.remove(k);
                    }
                }
            }


            // 2. Allocate descriptor memory, estimate
            //    orientations, and extract descriptors
            //

            // For each interest (key/corners) point
            for (int k = 0; k < points.size(); k++)
            {
                int thetaIndex = 0;

                // Estimate orientation
                if (!isOrientationNormal)
                {
                    // Orientation is not normalized, assign 0.
                    temp = points.get(k);
                    temp.setOrientation(0);
                    thetaIndex = 0;
                    points.set(k, temp);
                }

                else // if (IsOrientationNormal)
                {
                    // Get intensity values in the unrotated patch
                    for (int i = 0; i < pointsValues.length; i++)
                        pointsValues[i] = mean(points.get(k).x, points.get(k).y, scaleIndex.get(k), 0, i);

                    int a = 0, b = 0;
                    for (int m = 0; m < orientationPairs.length; m++)
                    {
                        FastRetinaKeypointPattern.OrientationPair p = orientationPairs[m];
                        int delta = (pointsValues[p.i] - pointsValues[p.j]);
                        a += delta * (p.weight_dx) / 2048;
                        b += delta * (p.weight_dy) / 2048;
                    }

                    temp = points.get(k);
                    temp.setOrientation(Math.atan2(b, a) * (180.0 / Math.PI));
                    points.set(k, temp);
                    thetaIndex = (int)(CV_FREAK_NB_ORIENTATION * points.get(k).getOrientation() * (1 / 360.0) + 0.5);

                    if (thetaIndex < 0) // bound in interval
                        thetaIndex += CV_FREAK_NB_ORIENTATION;
                    if (thetaIndex >= CV_FREAK_NB_ORIENTATION)
                        thetaIndex -= CV_FREAK_NB_ORIENTATION;
                }

                // Extract descriptor at the computed orientation
                for (int i = 0; i < pointsValues.length; i++)
                    pointsValues[i] = mean(points.get(k).x, points.get(k).y, scaleIndex.get(k), thetaIndex, i);


                // Extract either the standard descriptors of 512-bits (64 bytes)
                //   or the extended descriptors of 1024-bits (128 bytes) length.
                //
                if (!isExtended)
                {
                    temp = points.get(k);
                    temp.setDescriptor(new byte[64]);
                    for (int m = 0; m < descriptionPairs.length; m++)
                    {
                        FastRetinaKeypointPattern.DescriptionPair p = descriptionPairs[m];
                        byte[] descriptor = temp.getDescriptor();

                        if (pointsValues[p.i] > pointsValues[p.j])
                            descriptor[m / 8] |= (byte)(1 << m % 8);
                        else descriptor[m / 8] &= (byte)~(1 << m % 8);

                    }
                }

                else // if (Extended)
                {
                    temp = points.get(k);
                    temp.setDescriptor(new byte[128]);
                    for (int i = 1, m = 0; i < pointsValues.length; i++)
                    {
                        for (int j = 0; j < i; j++, m++)
                        {
                            byte[] descriptor = temp.getDescriptor();

                            if (pointsValues[i] > pointsValues[j])
                                descriptor[m / 8] |= (byte)(1 << m % 8);
                            else descriptor[m / 8] &= (byte)~(1 << m % 8);
                        }
                    }
                }
            }
        }


        private int mean(double kx, double ky, int scale, int orientation, int pointIndex)
        {
            final int CV_FREAK_NB_ORIENTATION = FastRetinaKeypointPattern.Orientations;
            final int CV_FREAK_NB_POINTS = FastRetinaKeypointPattern.Points;

            // get point position in image
            FastRetinaKeypointPattern.PatternPoint freak = pattern.lookupTable[
                scale * CV_FREAK_NB_ORIENTATION * CV_FREAK_NB_POINTS
                + orientation * CV_FREAK_NB_POINTS + pointIndex];

            double xf = freak.x + ky;
            double yf = freak.y + kx;
            int x = (int)(xf);
            int y = (int)(yf);
            int ret_val;

            // get the sigma:
            float radius = freak.sigma;

            // calculate output:
            if (radius < 0.5)
            {
                // interpolation multipliers:
                int r_x = (int)((xf - x) * 1024);
                int r_y = (int)((yf - y) * 1024);
                int r_x_1 = (1024 - r_x);
                int r_y_1 = (1024 - r_y);
                //byte* ptr = (byte*)Image.ImageData.ToPointer() + x + y * imagecols;

                // linear interpolation:
                ret_val = (r_x_1 * r_y_1 * Image.getGray(y, x));
                //ptr++;
                ret_val += (r_x * r_y_1 * Image.getGray(y, x + 1));
                //ptr += imagecols;
                ret_val += (r_x * r_y * Image.getGray(y + 1, x + 1));
                //ptr--;
                ret_val += (r_x_1 * r_y * Image.getGray(y + 1, x));
                return ((ret_val + 512) / 1024);
            }


            // calculate borders
            int x_left = (int)(xf - radius + 0.5);
            int y_top = (int)(yf - radius + 0.5);
            int x_right = (int)(xf + radius + 1.5);  //integral image is 1px wider
            int y_bottom = (int)(yf + radius + 1.5); //integral image is 1px higher

            ret_val = (int)Integral.getInternalData(y_bottom, x_right); //bottom right corner
            ret_val -= (int)Integral.getInternalData(y_bottom, x_left);
            ret_val += (int)Integral.getInternalData(y_top, x_left);
            ret_val -= (int)Integral.getInternalData(y_top, x_right);
            ret_val = ret_val / ((x_right - x_left) * (y_bottom - y_top));
            return ret_val;
        }
}