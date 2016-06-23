// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.Imaging.Corners;

import Catalano.Imaging.*;
import java.util.List;

/**
 * Corners detector's interface.
 * <para>The interface specifies set of methods, which should be implemented by different
 * corners detection algorithms.</para>
 * @author Diego Catalano
 */
public interface ICornersFeatureDetector {
    /**
     * Process image.
     * @param fastBitmap Image to be processed
     * @return List of feature points.
     */
    List<FeaturePoint> ProcessImage(FastBitmap fastBitmap);
}