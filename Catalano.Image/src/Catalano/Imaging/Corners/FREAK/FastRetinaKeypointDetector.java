// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.Imaging.Corners.FREAK;

import Catalano.Core.IntPoint;
import Catalano.Imaging.Corners.FeaturePoint;
import Catalano.Imaging.Corners.ICornersDetector;
import Catalano.Imaging.Corners.ICornersFeatureDetector;
import Catalano.Imaging.Corners.SusanCornersDetector;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.IntegralImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Fast Retina Keypoint (FREAK) detector.
 * @author Diego Catalano
 */
public class FastRetinaKeypointDetector {
    
    /**
     * FREAK Feature descriptor types.
     */
    public enum FastRetinaKeypointDescriptorType
    {
        /// <summary>
        ///   Do not compute descriptors.
        /// </summary>
        ///
        None,

        /// <summary>
        ///   Compute standard 512-bit descriptors.
        /// </summary>
        ///
        Standard,

        /// <summary>
        ///   Compute extended 1024-bit descriptors.
        /// </summary>
        ///
        Extended,
    }
    
    private FastRetinaKeypointDescriptorType featureType = FastRetinaKeypointDescriptorType.Standard;
    private float scale = 22.0f;
    private int octaves = 4;

    private IntegralImage integral;

    private FastBitmap grayImage;

    private FastRetinaKeypointPattern pattern;

    private FastRetinaKeypointDescriptor descriptor;

    public ICornersDetector Detector;
    
    public ICornersFeatureDetector FDetector;
        
    public FastRetinaKeypointDescriptor GetDescriptor() {
        if (descriptor == null || pattern == null){
            if (pattern == null)
                pattern = new FastRetinaKeypointPattern(octaves, scale);

            descriptor = new FastRetinaKeypointDescriptor(grayImage, integral, pattern);
            descriptor.setExtended(featureType == FastRetinaKeypointDescriptorType.Extended);
        }

        return descriptor;
    }
    
    public FastRetinaKeypointDetector(ICornersDetector cornerDetector){
        this.Detector = cornerDetector;
    }
    
    public FastRetinaKeypointDetector(ICornersFeatureDetector cornerFeatureDetector){
        this.FDetector = cornerFeatureDetector;
    }

    public FastRetinaKeypointDetector() {
        this.Detector = new SusanCornersDetector();
    }
        
    public List<FastRetinaKeypoint> ProcessImage(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            grayImage = new FastBitmap(fastBitmap);
        }
        else{
            grayImage = new FastBitmap(fastBitmap);
            grayImage.toGrayscale();
        }
        
        // 1. Extract corners points from the image.
        List<FastRetinaKeypoint> features = new ArrayList<FastRetinaKeypoint>();
        if(Detector != null){
            List<IntPoint> corners = Detector.ProcessImage(grayImage);

            for (int i = 0; i < corners.size(); i++)
                features.add(new FastRetinaKeypoint(corners.get(i).x, corners.get(i).y));
        }
        else{
            List<FeaturePoint> corners = FDetector.ProcessImage(grayImage);

            for (int i = 0; i < corners.size(); i++)
                features.add(new FastRetinaKeypoint(corners.get(i).x, corners.get(i).y));
        }

        // 2. Compute the integral for the given image
        integral = IntegralImage.FromFastBitmap(grayImage);

        // 3. Compute feature descriptors if required
        descriptor = null;
        if (featureType != FastRetinaKeypointDescriptorType.None){
            descriptor = GetDescriptor();
            descriptor.Compute(features);
        }
        return features;
    }
}