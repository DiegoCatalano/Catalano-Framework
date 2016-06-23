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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import java.util.ArrayList;
import java.util.List;

/**
 * Release several filters in the sequence.
 * 
 * <br> Example:
 * <pre>
 * {@code 
 * FiltersSequence fs = new FiltersSequence();
 * fs.add(new Grayscale());
 * fs.add(new Blur());
 * fs.add(new Threshold(150));
 * 
 * fs.applyInPlace(fastBitmap);
 * }</pre>
 * 
 * @author Diego Catalano
 */
public class FiltersSequence implements IApplyInPlace{
    
    private List<IApplyInPlace> lst = new ArrayList<IApplyInPlace>();

    /**
     * Initialize a new instance of the FiltersSequence class.
     */
    public FiltersSequence() {}
    
    public FiltersSequence(List<IApplyInPlace> sequence){
        this.lst = sequence;
    }
    
    /**
     * Add filter to be processed.
     * @param filter Filter.
     */
    public void add(IApplyInPlace filter){
        this.lst.add(filter);
    }
    
    /**
     * Eliminate all filters.
     */
    public void clear(){
        this.lst.clear();
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        for (IApplyInPlace f : lst) {
            f.applyInPlace(fastBitmap);
        }
    }
}