// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright (c) 2006-2017 Wilhelm Burger, Mark J. Burge. All rights reserved.
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
// Original license from Wilhelm Burguer, Mark J. Burge.
//
//    Redistribution and use in source and binary forms, with or without
//    modification, are permitted provided that the following conditions are met: 
//
//    1. Redistributions of source code must retain the above copyright notice,  this
//       list of conditions and the following disclaimer. 
//    2. Redistributions in binary form must reproduce the  above  copyright  notice,
//       this list of conditions and the following disclaimer  in  the  documentation
//       and/or other materials provided with the distribution. 
//
//    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,  THE  IMPLIED
//    WARRANTIES  OF  MERCHANTABILITY  AND  FITNESS  FOR A  PARTICULAR  PURPOSE   ARE 
//    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
//    ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL  DAMAGES
//    (INCLUDING, BUT NOT LIMITED TO,  PROCUREMENT OF SUBSTITUTE GOODS  OR  SERVICES;
//    LOSS OF USE, DATA, OR PROFITS; OR  BUSINESS INTERRUPTION)  HOWEVER  CAUSED  AND
//    ON ANY THEORY OF LIABILITY, WHETHER IN  CONTRACT,  STRICT  LIABILITY,  OR  TORT
//    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  OF  THIS
//    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//    The views and conclusions contained in the software and documentation are those
//    of the authors and should not be interpreted as representing official policies, 
//    either expressed or implied, of the FreeBSD Project.

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;
import java.util.List;

/**
 * Find Contours.
 * 
 * Find the outer and inner contours.
 * Support: Matrix and Cartesian coordinate systems.
 * 
 * @author Diego Catalano
 */
public class FindContours { 
	
    private static final int[][] delta = {
        { 1,0}, { 1, 1}, {0, 1}, {-1, 1}, 
        {-1,0}, {-1,-1}, {0,-1}, { 1,-1}};

    private int[][] labelArray;
    private static final int VISITED = -1;
    private static final int BACKGROUND = 0;
    private static final int START_LABEL = 2;
    private int currentLabel;
    private int maxLabel;
	
    private List<Contour> outerContours;
    private List<Contour> innerContours;

    /**
     * Get all inner contours.
     * @return Inner contours.
     */
    public List<Contour> getAllInnerContours() {
        return innerContours;
    }

    /**
     * Get all outer contours.
     * @return Outer contours.
     */
    public List<Contour> getAllOuterContours() {
        return outerContours;
    }
	
    /**
     * Initialize a new instance of the FindContours class.
     */
    public FindContours () {}
    
    /**
     * Process the image.
     * @param fastBitmap Image to be processed.
     */
    public void Process(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()){
            labelArray = new int[fastBitmap.getWidth()+2][fastBitmap.getHeight()+2];	// initialized to zero
            outerContours = new ArrayList<Contour>();
            innerContours = new ArrayList<Contour>();

            applyLabeling(fastBitmap);
        }
        else{
            throw new IllegalArgumentException("Contour finding only works in grayscale images.");
        }
        
    }
        
    private int getNextLabel() {
        if (currentLabel < 1)
            currentLabel = START_LABEL;
        else
            currentLabel = currentLabel + 1;
        maxLabel = currentLabel;
        return currentLabel;
    }
        
    private void resetLabel() {
        currentLabel = -1;
        maxLabel = -1;
    }
	
    private void applyLabeling(FastBitmap fastBitmap) {

        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();

        resetLabel();
        
        boolean isMatrix = false;
        if(fastBitmap.getCoordinateSystem() == FastBitmap.CoordinateSystem.Matrix){
            isMatrix = true;
            fastBitmap.setCoordinateSystem(FastBitmap.CoordinateSystem.Cartesian);
        }
        
        // scan top to bottom, left to right
        for (int v = 0; v < height; v++) {
            int label = 0;	// reset label, scan through horiz. line:
            for (int u = 0; u < width; u++) {
                    if (fastBitmap.getGray(u, v) > 0) {	// unlabeled FOREGROUND pixel
                        if (label != 0) { // keep using the same label
                            setLabel(u, v, label);
                        }
                        else {	// label == zero
                            label = getLabel(u, v);
                            if (label == 0) {	// new (unlabeled) region is hit
                                label = getNextLabel(); // assign a new region label
                                Contour oc = traceContour(u, v, 0, label, fastBitmap, isMatrix);
                                outerContours.add(oc);
                                setLabel(u, v, label);
                            }
                        }
                    } 
                    else {	// BACKGROUND pixel
                        if (label != 0) { // exiting a region
                            if (getLabel(u, v) == BACKGROUND) { // unlabeled - new inner contour
                                Contour ic = traceContour(u-1, v, 1, label, fastBitmap, isMatrix);
                                innerContours.add(ic);
                            }
                        label = 0;
                    }
                }
            }
        }
        if(isMatrix)
            fastBitmap.setCoordinateSystem(FastBitmap.CoordinateSystem.Matrix);
    }
	
    // Trace one contour starting at (xS,yS) 
    // in direction dS with label label
    // trace one contour starting at (xS,yS) in direction dS	
    private Contour traceContour(int xS, int yS, int dS, int label, FastBitmap fastBitmap, boolean isMatrix) {
        
        List<IntPoint> contour = new ArrayList<IntPoint>();
        
        //Contour contr = new Contour(label);
        int xT, yT; // T = successor of starting point (xS,yS)
        int xP, yP; // P = previous contour point
        int xC, yC; // C = current contour point
        IntPoint pt = new IntPoint(xS, yS); 
        int dNext = findNextPoint(pt, dS, fastBitmap);
        if(isMatrix)
            contour.add(new IntPoint(pt.y,pt.x));
        else
            contour.add(pt);
        xP = xS; yP = yS;
        xC = xT = pt.x;
        yC = yT = pt.y;

        boolean done = (xS==xT && yS==yT);  // true if isolated pixel
        while (!done) {
                setLabel(xC, yC, label);
                pt = new IntPoint(xC, yC);
                int dSearch = (dNext + 6) % 8;
                dNext = findNextPoint(pt, dSearch, fastBitmap);
                xP = xC;  yP = yC;	
                xC = pt.x; yC = pt.y; 
                // are we back at the starting position?
                done = (xP==xS && yP==yS && xC==xT && yC==yT);
                if (!done) {
                    if(isMatrix)
                        contour.add(new IntPoint(pt.y, pt.x));
                    else
                        contour.add(pt);
                }
        }
        return new Contour(label, contour);
    }
	
    private int findNextPoint (IntPoint pt, int dir, FastBitmap fastBitmap) { 
        // Starts at Point pt in direction dir,
        // returns the resulting tracing direction
        // and modifies pt.
        for (int i = 0; i < 7; i++) {
            int x = pt.x + delta[dir][0];
            int y = pt.y + delta[dir][1];
            if (fastBitmap.getGray(x, y) == BACKGROUND) {
                setLabel(x, y, VISITED);	// mark surrounding background pixels
                dir = (dir + 1) % 8;
            } 
            else {	// found a non-background pixel (next pixel to follow)
                pt.x = x; 
                pt.y = y; 
                break;
            }
        }
        return dir;
    }

    // access methods to the label array (which is padded!)
    public int getLabel(int u, int v) {	// (u,v) are image coordinates
        return labelArray[u+1][v+1];	// label array is padded (offset = 1)
    }

    private void setLabel(int u, int v, int label) { // (u,v) are image coordinates
        labelArray[u+1][v+1] = label;
    }
    
}



