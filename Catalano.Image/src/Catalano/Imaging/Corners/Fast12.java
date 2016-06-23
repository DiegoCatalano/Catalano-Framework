// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Alexandros Michael, 2011
//
// Copyright (c) 2006, 2008 Edward Rosten
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
//
// *Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//
// *Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// *Neither the name of the University of Cambridge nor the names of
// its contributors may be used to endorse or promote products derived
// from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features from Accelerated Segment Test (FAST) corners detector.
 * 
 * <para> In the FAST corner detection algorithm, a pixel is defined as a corner
 * if (in a circle surrounding the pixel), N or more contiguous pixels are
 * all significantly brighter then or all significantly darker than the center
 * pixel. The ordering of questions used to classify a pixel is learned using
 * the ID3 algorithm.</para>
 * 
 * <para>This detector has been shown to exibit a high degree of repeatability.</para>
 * 
 * @author Diego Catalano
 */
public class Fast12 implements ICornersFeatureDetector{
    
    private int threshold = 20;
    private boolean suppress = true;

    /**
     * Get Threshold.
     * 
     * A number denoting how much brighter or darker the pixels surrounding the pixel in question
     * should be in order to be considered a corner.
     * 
     * @return Threshold.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set Threshold.
     * 
     * A number denoting how much brighter or darker the pixels surrounding the pixel in question
     * should be in order to be considered a corner.
     * 
     * @param threshold Threshold.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Check if needs apply a non-maximum suppression algorithm on the results, to allow only maximal corners.
     * @return If true, allow only maximal corners, otherwise false.
     */
    public boolean isSuppressed() {
        return suppress;
    }

    /**
     * Set suppression if needs apply a non-maximum suppression algorithm on the results, to allow only maximal corners.
     * @param suppress If true, allow only maximal corners, otherwise false.
     */
    public void setSuppression(boolean suppress) {
        this.suppress = suppress;
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     */
    public Fast12() {}
    
    /**
     * Initializes a new instance of the FastCornersDetector class.
     * @param threshold Threshold.
     */
    public Fast12(int threshold){
        this.threshold = threshold;
    }
    
    /**
     * Initializes a new instance of the FastCornersDetector class.
     * @param threshold Threshold.
     * @param suppress Supress.
     */
    public Fast12(int threshold, boolean suppress){
        this.threshold = threshold;
        this.suppress = suppress;
    }
    
    @Override
    public List<FeaturePoint> ProcessImage(FastBitmap fastBitmap){
        
        FastBitmap gray;
        if (fastBitmap.isGrayscale()){
            gray = fastBitmap;
        }
        else{
            gray = new FastBitmap(fastBitmap);
            gray.toGrayscale();
        }
        
        if (isSuppressed()){
            List<FeaturePoint> lst = detect(gray, threshold);
            lst = nonMaxSuppression(fastBitmap.getWidth(), fastBitmap.getHeight(), lst);
            return lst;
        }
        
        return detect(gray, threshold);
    }
    
    private List<FeaturePoint> detect(FastBitmap fb, int threshold){
        
        ArrayList<FeaturePoint> corners = new ArrayList<FeaturePoint>();
        
        int width = fb.getWidth();
        int height = fb.getHeight();
        int count = 0;
        
        
        for (int i = 4; i < height - 4; ++i) {
                for (int j = 4; j < width - 4; ++j) {
                        int cb = fb.getGray(i, j) + threshold;
                        int c_b = fb.getGray(i, j) - threshold;

                        if (fb.getGray(i+3, j+0) > cb)
                         if (fb.getGray(i+3, j+1) > cb)
                          if (fb.getGray(i+2, j+2) > cb)
                           if (fb.getGray(i+1, j+3) > cb)
                            if (fb.getGray(i+0, j+3) > cb)
                             if (fb.getGray(i+-1, j+3) > cb)
                              if (fb.getGray(i+-2, j+2) > cb)
                               if (fb.getGray(i+-3, j+1) > cb)
                                if (fb.getGray(i+-3, j+0) > cb)
                                 if (fb.getGray(i+-3, j+-1) > cb)
                                  if (fb.getGray(i+-2, j+-2) > cb)
                                   if (fb.getGray(i+-1, j+-3) > cb)
                                    {;}
                                   else
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                  else
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                 else
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                else
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                               else
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                              else
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                             else
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                            else if (fb.getGray(i+0, j+3) < c_b) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else if (fb.getGray(i+-3, j+0) < c_b) 
                              if (fb.getGray(i+-1, j+3) < c_b)
                               if (fb.getGray(i+-2, j+2) < c_b)
                                if (fb.getGray(i+-3, j+1) < c_b)
                                 if (fb.getGray(i+-3, j+-1) < c_b)
                                  if (fb.getGray(i+-2, j+-2) < c_b)
                                   if (fb.getGray(i+-1, j+-3) < c_b)
                                    if (fb.getGray(i+0, j+-3) < c_b)
                                     if (fb.getGray(i+1, j+-3) < c_b)
                                      if (fb.getGray(i+2, j+-2) < c_b)
                                       if (fb.getGray(i+3, j+-1) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                           else if (fb.getGray(i+1, j+3) < c_b) 
                            if (fb.getGray(i+3, j+-1) > cb) 
                             if (fb.getGray(i+-3, j+1) > cb) 
                              if (fb.getGray(i+-3, j+0) > cb) 
                               if (fb.getGray(i+-3, j+-1) > cb) 
                                if (fb.getGray(i+-2, j+-2) > cb) 
                                 if (fb.getGray(i+-1, j+-3) > cb) 
                                  if (fb.getGray(i+0, j+-3) > cb) 
                                   if (fb.getGray(i+1, j+-3) > cb) 
                                    if (fb.getGray(i+2, j+-2) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else if (fb.getGray(i+-3, j+1) < c_b) 
                              if (fb.getGray(i+0, j+3) < c_b)
                               if (fb.getGray(i+-1, j+3) < c_b)
                                if (fb.getGray(i+-2, j+2) < c_b)
                                 if (fb.getGray(i+-3, j+0) < c_b)
                                  if (fb.getGray(i+-3, j+-1) < c_b)
                                   if (fb.getGray(i+-2, j+-2) < c_b)
                                    if (fb.getGray(i+-1, j+-3) < c_b)
                                     if (fb.getGray(i+0, j+-3) < c_b)
                                      if (fb.getGray(i+1, j+-3) < c_b)
                                       if (fb.getGray(i+2, j+-2) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             if (fb.getGray(i+0, j+3) < c_b)
                              if (fb.getGray(i+-1, j+3) < c_b)
                               if (fb.getGray(i+-2, j+2) < c_b)
                                if (fb.getGray(i+-3, j+1) < c_b)
                                 if (fb.getGray(i+-3, j+0) < c_b)
                                  if (fb.getGray(i+-3, j+-1) < c_b)
                                   if (fb.getGray(i+-2, j+-2) < c_b)
                                    if (fb.getGray(i+-1, j+-3) < c_b)
                                     if (fb.getGray(i+0, j+-3) < c_b)
                                      if (fb.getGray(i+1, j+-3) < c_b)
                                       if (fb.getGray(i+2, j+-2) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                           else
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else if (fb.getGray(i+-3, j+1) < c_b) 
                             if (fb.getGray(i+0, j+3) < c_b)
                              if (fb.getGray(i+-1, j+3) < c_b)
                               if (fb.getGray(i+-2, j+2) < c_b)
                                if (fb.getGray(i+-3, j+0) < c_b)
                                 if (fb.getGray(i+-3, j+-1) < c_b)
                                  if (fb.getGray(i+-2, j+-2) < c_b)
                                   if (fb.getGray(i+-1, j+-3) < c_b)
                                    if (fb.getGray(i+0, j+-3) < c_b)
                                     if (fb.getGray(i+1, j+-3) < c_b)
                                      if (fb.getGray(i+2, j+-2) < c_b)
                                       if (fb.getGray(i+3, j+-1) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                          else if (fb.getGray(i+2, j+2) < c_b) 
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      if (fb.getGray(i+0, j+3) > cb) 
                                       if (fb.getGray(i+-1, j+3) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else if (fb.getGray(i+-2, j+2) < c_b) 
                            if (fb.getGray(i+0, j+3) < c_b)
                             if (fb.getGray(i+-1, j+3) < c_b)
                              if (fb.getGray(i+-3, j+1) < c_b)
                               if (fb.getGray(i+-3, j+0) < c_b)
                                if (fb.getGray(i+-3, j+-1) < c_b)
                                 if (fb.getGray(i+-2, j+-2) < c_b)
                                  if (fb.getGray(i+-1, j+-3) < c_b)
                                   if (fb.getGray(i+0, j+-3) < c_b)
                                    if (fb.getGray(i+1, j+-3) < c_b)
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      {;}
                                     else
                                      if (fb.getGray(i+2, j+-2) < c_b)
                                       if (fb.getGray(i+3, j+-1) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      if (fb.getGray(i+0, j+3) > cb) 
                                       if (fb.getGray(i+-1, j+3) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else if (fb.getGray(i+-2, j+2) < c_b) 
                            if (fb.getGray(i+0, j+3) < c_b)
                             if (fb.getGray(i+-1, j+3) < c_b)
                              if (fb.getGray(i+-3, j+1) < c_b)
                               if (fb.getGray(i+-3, j+0) < c_b)
                                if (fb.getGray(i+-3, j+-1) < c_b)
                                 if (fb.getGray(i+-2, j+-2) < c_b)
                                  if (fb.getGray(i+-1, j+-3) < c_b)
                                   if (fb.getGray(i+0, j+-3) < c_b)
                                    if (fb.getGray(i+1, j+-3) < c_b)
                                     if (fb.getGray(i+2, j+-2) < c_b)
                                      if (fb.getGray(i+1, j+3) < c_b)
                                       {;}
                                      else
                                       if (fb.getGray(i+3, j+-1) < c_b)
                                        {;}
                                       else
                                        continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                         else if (fb.getGray(i+3, j+1) < c_b) 
                          if (fb.getGray(i+-1, j+3) > cb) 
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      if (fb.getGray(i+0, j+3) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+2, j+2) > cb) 
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      if (fb.getGray(i+0, j+3) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else if (fb.getGray(i+-1, j+3) < c_b) 
                           if (fb.getGray(i+0, j+3) < c_b)
                            if (fb.getGray(i+-2, j+2) < c_b)
                             if (fb.getGray(i+-3, j+1) < c_b)
                              if (fb.getGray(i+-3, j+0) < c_b)
                               if (fb.getGray(i+-3, j+-1) < c_b)
                                if (fb.getGray(i+-2, j+-2) < c_b)
                                 if (fb.getGray(i+-1, j+-3) < c_b)
                                  if (fb.getGray(i+0, j+-3) < c_b)
                                   if (fb.getGray(i+1, j+3) < c_b)
                                    if (fb.getGray(i+2, j+2) < c_b)
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+-3) < c_b)
                                      if (fb.getGray(i+2, j+-2) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+1, j+-3) < c_b)
                                     if (fb.getGray(i+2, j+-2) < c_b)
                                      if (fb.getGray(i+3, j+-1) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           continue;
                         else
                          if (fb.getGray(i+-1, j+3) > cb) 
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+-3) > cb) 
                                   if (fb.getGray(i+2, j+-2) > cb) 
                                    if (fb.getGray(i+3, j+-1) > cb) 
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      if (fb.getGray(i+0, j+3) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+2, j+2) > cb) 
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      if (fb.getGray(i+0, j+3) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else if (fb.getGray(i+-1, j+3) < c_b) 
                           if (fb.getGray(i+0, j+3) < c_b)
                            if (fb.getGray(i+-2, j+2) < c_b)
                             if (fb.getGray(i+-3, j+1) < c_b)
                              if (fb.getGray(i+-3, j+0) < c_b)
                               if (fb.getGray(i+-3, j+-1) < c_b)
                                if (fb.getGray(i+-2, j+-2) < c_b)
                                 if (fb.getGray(i+-1, j+-3) < c_b)
                                  if (fb.getGray(i+0, j+-3) < c_b)
                                   if (fb.getGray(i+1, j+-3) < c_b)
                                    if (fb.getGray(i+1, j+3) < c_b)
                                     if (fb.getGray(i+2, j+2) < c_b)
                                      {;}
                                     else
                                      if (fb.getGray(i+2, j+-2) < c_b)
                                       {;}
                                      else
                                       continue;
                                    else
                                     if (fb.getGray(i+2, j+-2) < c_b)
                                      if (fb.getGray(i+3, j+-1) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           continue;
                        else if (fb.getGray(i+3, j+0) < c_b) 
                         if (fb.getGray(i+3, j+1) > cb) 
                          if (fb.getGray(i+-1, j+3) > cb) 
                           if (fb.getGray(i+0, j+3) > cb) 
                            if (fb.getGray(i+-2, j+2) > cb) 
                             if (fb.getGray(i+-3, j+1) > cb) 
                              if (fb.getGray(i+-3, j+0) > cb) 
                               if (fb.getGray(i+-3, j+-1) > cb) 
                                if (fb.getGray(i+-2, j+-2) > cb) 
                                 if (fb.getGray(i+-1, j+-3) > cb) 
                                  if (fb.getGray(i+0, j+-3) > cb) 
                                   if (fb.getGray(i+1, j+3) > cb) 
                                    if (fb.getGray(i+2, j+2) > cb) 
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+-3) > cb) 
                                      if (fb.getGray(i+2, j+-2) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+1, j+-3) > cb) 
                                     if (fb.getGray(i+2, j+-2) > cb) 
                                      if (fb.getGray(i+3, j+-1) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else if (fb.getGray(i+-1, j+3) < c_b) 
                           if (fb.getGray(i+-2, j+2) < c_b)
                            if (fb.getGray(i+-3, j+1) < c_b)
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      if (fb.getGray(i+0, j+3) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+2, j+2) < c_b)
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      if (fb.getGray(i+0, j+3) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           continue;
                         else if (fb.getGray(i+3, j+1) < c_b) 
                          if (fb.getGray(i+2, j+2) > cb) 
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+0, j+3) > cb) 
                             if (fb.getGray(i+-1, j+3) > cb) 
                              if (fb.getGray(i+-3, j+1) > cb) 
                               if (fb.getGray(i+-3, j+0) > cb) 
                                if (fb.getGray(i+-3, j+-1) > cb) 
                                 if (fb.getGray(i+-2, j+-2) > cb) 
                                  if (fb.getGray(i+-1, j+-3) > cb) 
                                   if (fb.getGray(i+0, j+-3) > cb) 
                                    if (fb.getGray(i+1, j+-3) > cb) 
                                     if (fb.getGray(i+1, j+3) > cb) 
                                      {;}
                                     else
                                      if (fb.getGray(i+2, j+-2) > cb) 
                                       if (fb.getGray(i+3, j+-1) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else if (fb.getGray(i+-2, j+2) < c_b) 
                            if (fb.getGray(i+-3, j+1) < c_b)
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      if (fb.getGray(i+0, j+3) < c_b)
                                       if (fb.getGray(i+-1, j+3) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else if (fb.getGray(i+2, j+2) < c_b) 
                           if (fb.getGray(i+1, j+3) > cb) 
                            if (fb.getGray(i+3, j+-1) < c_b)
                             if (fb.getGray(i+-3, j+1) > cb) 
                              if (fb.getGray(i+0, j+3) > cb) 
                               if (fb.getGray(i+-1, j+3) > cb) 
                                if (fb.getGray(i+-2, j+2) > cb) 
                                 if (fb.getGray(i+-3, j+0) > cb) 
                                  if (fb.getGray(i+-3, j+-1) > cb) 
                                   if (fb.getGray(i+-2, j+-2) > cb) 
                                    if (fb.getGray(i+-1, j+-3) > cb) 
                                     if (fb.getGray(i+0, j+-3) > cb) 
                                      if (fb.getGray(i+1, j+-3) > cb) 
                                       if (fb.getGray(i+2, j+-2) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else if (fb.getGray(i+-3, j+1) < c_b) 
                              if (fb.getGray(i+-3, j+0) < c_b)
                               if (fb.getGray(i+-3, j+-1) < c_b)
                                if (fb.getGray(i+-2, j+-2) < c_b)
                                 if (fb.getGray(i+-1, j+-3) < c_b)
                                  if (fb.getGray(i+0, j+-3) < c_b)
                                   if (fb.getGray(i+1, j+-3) < c_b)
                                    if (fb.getGray(i+2, j+-2) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             if (fb.getGray(i+0, j+3) > cb) 
                              if (fb.getGray(i+-1, j+3) > cb) 
                               if (fb.getGray(i+-2, j+2) > cb) 
                                if (fb.getGray(i+-3, j+1) > cb) 
                                 if (fb.getGray(i+-3, j+0) > cb) 
                                  if (fb.getGray(i+-3, j+-1) > cb) 
                                   if (fb.getGray(i+-2, j+-2) > cb) 
                                    if (fb.getGray(i+-1, j+-3) > cb) 
                                     if (fb.getGray(i+0, j+-3) > cb) 
                                      if (fb.getGray(i+1, j+-3) > cb) 
                                       if (fb.getGray(i+2, j+-2) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                           else if (fb.getGray(i+1, j+3) < c_b) 
                            if (fb.getGray(i+0, j+3) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-1, j+3) > cb) 
                               if (fb.getGray(i+-2, j+2) > cb) 
                                if (fb.getGray(i+-3, j+1) > cb) 
                                 if (fb.getGray(i+-3, j+-1) > cb) 
                                  if (fb.getGray(i+-2, j+-2) > cb) 
                                   if (fb.getGray(i+-1, j+-3) > cb) 
                                    if (fb.getGray(i+0, j+-3) > cb) 
                                     if (fb.getGray(i+1, j+-3) > cb) 
                                      if (fb.getGray(i+2, j+-2) > cb) 
                                       if (fb.getGray(i+3, j+-1) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else if (fb.getGray(i+-3, j+0) < c_b) 
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else if (fb.getGray(i+0, j+3) < c_b) 
                             if (fb.getGray(i+-1, j+3) < c_b)
                              if (fb.getGray(i+-2, j+2) < c_b)
                               if (fb.getGray(i+-3, j+1) < c_b)
                                if (fb.getGray(i+-3, j+0) < c_b)
                                 if (fb.getGray(i+-3, j+-1) < c_b)
                                  if (fb.getGray(i+-2, j+-2) < c_b)
                                   if (fb.getGray(i+-1, j+-3) < c_b)
                                    {;}
                                   else
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                  else
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                 else
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                else
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                               else
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                              else
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                             else
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                            else
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                           else
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+0, j+3) > cb) 
                              if (fb.getGray(i+-1, j+3) > cb) 
                               if (fb.getGray(i+-2, j+2) > cb) 
                                if (fb.getGray(i+-3, j+0) > cb) 
                                 if (fb.getGray(i+-3, j+-1) > cb) 
                                  if (fb.getGray(i+-2, j+-2) > cb) 
                                   if (fb.getGray(i+-1, j+-3) > cb) 
                                    if (fb.getGray(i+0, j+-3) > cb) 
                                     if (fb.getGray(i+1, j+-3) > cb) 
                                      if (fb.getGray(i+2, j+-2) > cb) 
                                       if (fb.getGray(i+3, j+-1) > cb) 
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else if (fb.getGray(i+-3, j+1) < c_b) 
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                          else
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+0, j+3) > cb) 
                             if (fb.getGray(i+-1, j+3) > cb) 
                              if (fb.getGray(i+-3, j+1) > cb) 
                               if (fb.getGray(i+-3, j+0) > cb) 
                                if (fb.getGray(i+-3, j+-1) > cb) 
                                 if (fb.getGray(i+-2, j+-2) > cb) 
                                  if (fb.getGray(i+-1, j+-3) > cb) 
                                   if (fb.getGray(i+0, j+-3) > cb) 
                                    if (fb.getGray(i+1, j+-3) > cb) 
                                     if (fb.getGray(i+2, j+-2) > cb) 
                                      if (fb.getGray(i+1, j+3) > cb) 
                                       {;}
                                      else
                                       if (fb.getGray(i+3, j+-1) > cb) 
                                        {;}
                                       else
                                        continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else if (fb.getGray(i+-2, j+2) < c_b) 
                            if (fb.getGray(i+-3, j+1) < c_b)
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      if (fb.getGray(i+0, j+3) < c_b)
                                       if (fb.getGray(i+-1, j+3) < c_b)
                                        {;}
                                       else
                                        continue;
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                         else
                          if (fb.getGray(i+-1, j+3) > cb) 
                           if (fb.getGray(i+0, j+3) > cb) 
                            if (fb.getGray(i+-2, j+2) > cb) 
                             if (fb.getGray(i+-3, j+1) > cb) 
                              if (fb.getGray(i+-3, j+0) > cb) 
                               if (fb.getGray(i+-3, j+-1) > cb) 
                                if (fb.getGray(i+-2, j+-2) > cb) 
                                 if (fb.getGray(i+-1, j+-3) > cb) 
                                  if (fb.getGray(i+0, j+-3) > cb) 
                                   if (fb.getGray(i+1, j+-3) > cb) 
                                    if (fb.getGray(i+1, j+3) > cb) 
                                     if (fb.getGray(i+2, j+2) > cb) 
                                      {;}
                                     else
                                      if (fb.getGray(i+2, j+-2) > cb) 
                                       {;}
                                      else
                                       continue;
                                    else
                                     if (fb.getGray(i+2, j+-2) > cb) 
                                      if (fb.getGray(i+3, j+-1) > cb) 
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else if (fb.getGray(i+-1, j+3) < c_b) 
                           if (fb.getGray(i+-2, j+2) < c_b)
                            if (fb.getGray(i+-3, j+1) < c_b)
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+-3) < c_b)
                                   if (fb.getGray(i+2, j+-2) < c_b)
                                    if (fb.getGray(i+3, j+-1) < c_b)
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      if (fb.getGray(i+0, j+3) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+2, j+2) < c_b)
                                     if (fb.getGray(i+1, j+3) < c_b)
                                      if (fb.getGray(i+0, j+3) < c_b)
                                       {;}
                                      else
                                       continue;
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           continue;
                        else
                         if (fb.getGray(i+0, j+3) > cb) 
                          if (fb.getGray(i+-1, j+3) > cb) 
                           if (fb.getGray(i+-2, j+2) > cb) 
                            if (fb.getGray(i+-3, j+1) > cb) 
                             if (fb.getGray(i+-3, j+0) > cb) 
                              if (fb.getGray(i+-3, j+-1) > cb) 
                               if (fb.getGray(i+-2, j+-2) > cb) 
                                if (fb.getGray(i+-1, j+-3) > cb) 
                                 if (fb.getGray(i+0, j+-3) > cb) 
                                  if (fb.getGray(i+1, j+3) > cb) 
                                   if (fb.getGray(i+2, j+2) > cb) 
                                    if (fb.getGray(i+3, j+1) > cb) 
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+-3) > cb) 
                                      {;}
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+1, j+-3) > cb) 
                                     if (fb.getGray(i+2, j+-2) > cb) 
                                      {;}
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   if (fb.getGray(i+1, j+-3) > cb) 
                                    if (fb.getGray(i+2, j+-2) > cb) 
                                     if (fb.getGray(i+3, j+-1) > cb) 
                                      {;}
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           continue;
                         else if (fb.getGray(i+0, j+3) < c_b) 
                          if (fb.getGray(i+-1, j+3) < c_b)
                           if (fb.getGray(i+-2, j+2) < c_b)
                            if (fb.getGray(i+-3, j+1) < c_b)
                             if (fb.getGray(i+-3, j+0) < c_b)
                              if (fb.getGray(i+-3, j+-1) < c_b)
                               if (fb.getGray(i+-2, j+-2) < c_b)
                                if (fb.getGray(i+-1, j+-3) < c_b)
                                 if (fb.getGray(i+0, j+-3) < c_b)
                                  if (fb.getGray(i+1, j+3) < c_b)
                                   if (fb.getGray(i+2, j+2) < c_b)
                                    if (fb.getGray(i+3, j+1) < c_b)
                                     {;}
                                    else
                                     if (fb.getGray(i+1, j+-3) < c_b)
                                      {;}
                                     else
                                      continue;
                                   else
                                    if (fb.getGray(i+1, j+-3) < c_b)
                                     if (fb.getGray(i+2, j+-2) < c_b)
                                      {;}
                                     else
                                      continue;
                                    else
                                     continue;
                                  else
                                   if (fb.getGray(i+1, j+-3) < c_b)
                                    if (fb.getGray(i+2, j+-2) < c_b)
                                     if (fb.getGray(i+3, j+-1) < c_b)
                                      {;}
                                     else
                                      continue;
                                    else
                                     continue;
                                   else
                                    continue;
                                 else
                                  continue;
                                else
                                 continue;
                               else
                                continue;
                              else
                               continue;
                             else
                              continue;
                            else
                             continue;
                           else
                            continue;
                          else
                           continue;
                         else
                          continue;
                        corners.add(new FeaturePoint(i, j));
                        count++;
                }
        }
        for (int i = 0; i < count; ++i) {
                int x = corners.get(i).x;
                int y = corners.get(i).y;
                
                corners.get(i).score = (cornerScore(fb, x, y));
        }
        Collections.sort(corners, Collections.reverseOrder());
        return corners;
    }
    
    private int cornerScore(FastBitmap fastBitmap, int posx, int posy){
            int bmin = 0;
            int bmax = 255;
            int b = (bmax + bmin)/2;

            while (true){
                    if (isCorner(fastBitmap, posx, posy, b)) {
                            bmin = b;
                    } else {
                            bmax = b;
                    }

                    if (bmin == bmax - 1 || bmin == bmax) {
                            return bmin;
                    }

                    b = (bmin + bmax) / 2;
            }
    }
    
    private boolean isCorner(FastBitmap fb, int i, int j, int threshold){	
            
        int cb = fb.getGray(i, j) + threshold;
        int c_b = fb.getGray(i, j) - threshold;
            
            if (fb.getGray(i+3, j+0) > cb) 
             if (fb.getGray(i+3, j+1) > cb) 
              if (fb.getGray(i+2, j+2) > cb) 
               if (fb.getGray(i+1, j+3) > cb) 
                if (fb.getGray(i+0, j+3) > cb) 
                 if (fb.getGray(i+-1, j+3) > cb) 
                  if (fb.getGray(i+-2, j+2) > cb) 
                   if (fb.getGray(i+-3, j+1) > cb) 
                    if (fb.getGray(i+-3, j+0) > cb) 
                     if (fb.getGray(i+-3, j+-1) > cb) 
                      if (fb.getGray(i+-2, j+-2) > cb) 
                       if (fb.getGray(i+-1, j+-3) > cb) 
                        return true;
                       else
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                      else
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                     else
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                    else
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                   else
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                  else
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                 else
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                else if (fb.getGray(i+0, j+3) < c_b) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else if (fb.getGray(i+-3, j+0) < c_b) 
                  if (fb.getGray(i+-1, j+3) < c_b)
                   if (fb.getGray(i+-2, j+2) < c_b)
                    if (fb.getGray(i+-3, j+1) < c_b)
                     if (fb.getGray(i+-3, j+-1) < c_b)
                      if (fb.getGray(i+-2, j+-2) < c_b)
                       if (fb.getGray(i+-1, j+-3) < c_b)
                        if (fb.getGray(i+0, j+-3) < c_b)
                         if (fb.getGray(i+1, j+-3) < c_b)
                          if (fb.getGray(i+2, j+-2) < c_b)
                           if (fb.getGray(i+3, j+-1) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
               else if (fb.getGray(i+1, j+3) < c_b) 
                if (fb.getGray(i+3, j+-1) > cb) 
                 if (fb.getGray(i+-3, j+1) > cb) 
                  if (fb.getGray(i+-3, j+0) > cb) 
                   if (fb.getGray(i+-3, j+-1) > cb) 
                    if (fb.getGray(i+-2, j+-2) > cb) 
                     if (fb.getGray(i+-1, j+-3) > cb) 
                      if (fb.getGray(i+0, j+-3) > cb) 
                       if (fb.getGray(i+1, j+-3) > cb) 
                        if (fb.getGray(i+2, j+-2) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else if (fb.getGray(i+-3, j+1) < c_b) 
                  if (fb.getGray(i+0, j+3) < c_b)
                   if (fb.getGray(i+-1, j+3) < c_b)
                    if (fb.getGray(i+-2, j+2) < c_b)
                     if (fb.getGray(i+-3, j+0) < c_b)
                      if (fb.getGray(i+-3, j+-1) < c_b)
                       if (fb.getGray(i+-2, j+-2) < c_b)
                        if (fb.getGray(i+-1, j+-3) < c_b)
                         if (fb.getGray(i+0, j+-3) < c_b)
                          if (fb.getGray(i+1, j+-3) < c_b)
                           if (fb.getGray(i+2, j+-2) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 if (fb.getGray(i+0, j+3) < c_b)
                  if (fb.getGray(i+-1, j+3) < c_b)
                   if (fb.getGray(i+-2, j+2) < c_b)
                    if (fb.getGray(i+-3, j+1) < c_b)
                     if (fb.getGray(i+-3, j+0) < c_b)
                      if (fb.getGray(i+-3, j+-1) < c_b)
                       if (fb.getGray(i+-2, j+-2) < c_b)
                        if (fb.getGray(i+-1, j+-3) < c_b)
                         if (fb.getGray(i+0, j+-3) < c_b)
                          if (fb.getGray(i+1, j+-3) < c_b)
                           if (fb.getGray(i+2, j+-2) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
               else
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else if (fb.getGray(i+-3, j+1) < c_b) 
                 if (fb.getGray(i+0, j+3) < c_b)
                  if (fb.getGray(i+-1, j+3) < c_b)
                   if (fb.getGray(i+-2, j+2) < c_b)
                    if (fb.getGray(i+-3, j+0) < c_b)
                     if (fb.getGray(i+-3, j+-1) < c_b)
                      if (fb.getGray(i+-2, j+-2) < c_b)
                       if (fb.getGray(i+-1, j+-3) < c_b)
                        if (fb.getGray(i+0, j+-3) < c_b)
                         if (fb.getGray(i+1, j+-3) < c_b)
                          if (fb.getGray(i+2, j+-2) < c_b)
                           if (fb.getGray(i+3, j+-1) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
              else if (fb.getGray(i+2, j+2) < c_b) 
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) > cb) 
                          if (fb.getGray(i+0, j+3) > cb) 
                           if (fb.getGray(i+-1, j+3) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else if (fb.getGray(i+-2, j+2) < c_b) 
                if (fb.getGray(i+0, j+3) < c_b)
                 if (fb.getGray(i+-1, j+3) < c_b)
                  if (fb.getGray(i+-3, j+1) < c_b)
                   if (fb.getGray(i+-3, j+0) < c_b)
                    if (fb.getGray(i+-3, j+-1) < c_b)
                     if (fb.getGray(i+-2, j+-2) < c_b)
                      if (fb.getGray(i+-1, j+-3) < c_b)
                       if (fb.getGray(i+0, j+-3) < c_b)
                        if (fb.getGray(i+1, j+-3) < c_b)
                         if (fb.getGray(i+1, j+3) < c_b)
                          return true;
                         else
                          if (fb.getGray(i+2, j+-2) < c_b)
                           if (fb.getGray(i+3, j+-1) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) > cb) 
                          if (fb.getGray(i+0, j+3) > cb) 
                           if (fb.getGray(i+-1, j+3) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else if (fb.getGray(i+-2, j+2) < c_b) 
                if (fb.getGray(i+0, j+3) < c_b)
                 if (fb.getGray(i+-1, j+3) < c_b)
                  if (fb.getGray(i+-3, j+1) < c_b)
                   if (fb.getGray(i+-3, j+0) < c_b)
                    if (fb.getGray(i+-3, j+-1) < c_b)
                     if (fb.getGray(i+-2, j+-2) < c_b)
                      if (fb.getGray(i+-1, j+-3) < c_b)
                       if (fb.getGray(i+0, j+-3) < c_b)
                        if (fb.getGray(i+1, j+-3) < c_b)
                         if (fb.getGray(i+2, j+-2) < c_b)
                          if (fb.getGray(i+1, j+3) < c_b)
                           return true;
                          else
                           if (fb.getGray(i+3, j+-1) < c_b)
                            return true;
                           else
                            return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
             else if (fb.getGray(i+3, j+1) < c_b) 
              if (fb.getGray(i+-1, j+3) > cb) 
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) > cb) 
                          if (fb.getGray(i+0, j+3) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        if (fb.getGray(i+2, j+2) > cb) 
                         if (fb.getGray(i+1, j+3) > cb) 
                          if (fb.getGray(i+0, j+3) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else if (fb.getGray(i+-1, j+3) < c_b) 
               if (fb.getGray(i+0, j+3) < c_b)
                if (fb.getGray(i+-2, j+2) < c_b)
                 if (fb.getGray(i+-3, j+1) < c_b)
                  if (fb.getGray(i+-3, j+0) < c_b)
                   if (fb.getGray(i+-3, j+-1) < c_b)
                    if (fb.getGray(i+-2, j+-2) < c_b)
                     if (fb.getGray(i+-1, j+-3) < c_b)
                      if (fb.getGray(i+0, j+-3) < c_b)
                       if (fb.getGray(i+1, j+3) < c_b)
                        if (fb.getGray(i+2, j+2) < c_b)
                         return true;
                        else
                         if (fb.getGray(i+1, j+-3) < c_b)
                          if (fb.getGray(i+2, j+-2) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        if (fb.getGray(i+1, j+-3) < c_b)
                         if (fb.getGray(i+2, j+-2) < c_b)
                          if (fb.getGray(i+3, j+-1) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               return false;
             else
              if (fb.getGray(i+-1, j+3) > cb) 
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+-3) > cb) 
                       if (fb.getGray(i+2, j+-2) > cb) 
                        if (fb.getGray(i+3, j+-1) > cb) 
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) > cb) 
                          if (fb.getGray(i+0, j+3) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        if (fb.getGray(i+2, j+2) > cb) 
                         if (fb.getGray(i+1, j+3) > cb) 
                          if (fb.getGray(i+0, j+3) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else if (fb.getGray(i+-1, j+3) < c_b) 
               if (fb.getGray(i+0, j+3) < c_b)
                if (fb.getGray(i+-2, j+2) < c_b)
                 if (fb.getGray(i+-3, j+1) < c_b)
                  if (fb.getGray(i+-3, j+0) < c_b)
                   if (fb.getGray(i+-3, j+-1) < c_b)
                    if (fb.getGray(i+-2, j+-2) < c_b)
                     if (fb.getGray(i+-1, j+-3) < c_b)
                      if (fb.getGray(i+0, j+-3) < c_b)
                       if (fb.getGray(i+1, j+-3) < c_b)
                        if (fb.getGray(i+1, j+3) < c_b)
                         if (fb.getGray(i+2, j+2) < c_b)
                          return true;
                         else
                          if (fb.getGray(i+2, j+-2) < c_b)
                           return true;
                          else
                           return false;
                        else
                         if (fb.getGray(i+2, j+-2) < c_b)
                          if (fb.getGray(i+3, j+-1) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               return false;
            else if (fb.getGray(i+3, j+0) < c_b) 
             if (fb.getGray(i+3, j+1) > cb) 
              if (fb.getGray(i+-1, j+3) > cb) 
               if (fb.getGray(i+0, j+3) > cb) 
                if (fb.getGray(i+-2, j+2) > cb) 
                 if (fb.getGray(i+-3, j+1) > cb) 
                  if (fb.getGray(i+-3, j+0) > cb) 
                   if (fb.getGray(i+-3, j+-1) > cb) 
                    if (fb.getGray(i+-2, j+-2) > cb) 
                     if (fb.getGray(i+-1, j+-3) > cb) 
                      if (fb.getGray(i+0, j+-3) > cb) 
                       if (fb.getGray(i+1, j+3) > cb) 
                        if (fb.getGray(i+2, j+2) > cb) 
                         return true;
                        else
                         if (fb.getGray(i+1, j+-3) > cb) 
                          if (fb.getGray(i+2, j+-2) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        if (fb.getGray(i+1, j+-3) > cb) 
                         if (fb.getGray(i+2, j+-2) > cb) 
                          if (fb.getGray(i+3, j+-1) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else if (fb.getGray(i+-1, j+3) < c_b) 
               if (fb.getGray(i+-2, j+2) < c_b)
                if (fb.getGray(i+-3, j+1) < c_b)
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) < c_b)
                          if (fb.getGray(i+0, j+3) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        if (fb.getGray(i+2, j+2) < c_b)
                         if (fb.getGray(i+1, j+3) < c_b)
                          if (fb.getGray(i+0, j+3) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               return false;
             else if (fb.getGray(i+3, j+1) < c_b) 
              if (fb.getGray(i+2, j+2) > cb) 
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+0, j+3) > cb) 
                 if (fb.getGray(i+-1, j+3) > cb) 
                  if (fb.getGray(i+-3, j+1) > cb) 
                   if (fb.getGray(i+-3, j+0) > cb) 
                    if (fb.getGray(i+-3, j+-1) > cb) 
                     if (fb.getGray(i+-2, j+-2) > cb) 
                      if (fb.getGray(i+-1, j+-3) > cb) 
                       if (fb.getGray(i+0, j+-3) > cb) 
                        if (fb.getGray(i+1, j+-3) > cb) 
                         if (fb.getGray(i+1, j+3) > cb) 
                          return true;
                         else
                          if (fb.getGray(i+2, j+-2) > cb) 
                           if (fb.getGray(i+3, j+-1) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else if (fb.getGray(i+-2, j+2) < c_b) 
                if (fb.getGray(i+-3, j+1) < c_b)
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) < c_b)
                          if (fb.getGray(i+0, j+3) < c_b)
                           if (fb.getGray(i+-1, j+3) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else if (fb.getGray(i+2, j+2) < c_b) 
               if (fb.getGray(i+1, j+3) > cb) 
                if (fb.getGray(i+3, j+-1) < c_b)
                 if (fb.getGray(i+-3, j+1) > cb) 
                  if (fb.getGray(i+0, j+3) > cb) 
                   if (fb.getGray(i+-1, j+3) > cb) 
                    if (fb.getGray(i+-2, j+2) > cb) 
                     if (fb.getGray(i+-3, j+0) > cb) 
                      if (fb.getGray(i+-3, j+-1) > cb) 
                       if (fb.getGray(i+-2, j+-2) > cb) 
                        if (fb.getGray(i+-1, j+-3) > cb) 
                         if (fb.getGray(i+0, j+-3) > cb) 
                          if (fb.getGray(i+1, j+-3) > cb) 
                           if (fb.getGray(i+2, j+-2) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else if (fb.getGray(i+-3, j+1) < c_b) 
                  if (fb.getGray(i+-3, j+0) < c_b)
                   if (fb.getGray(i+-3, j+-1) < c_b)
                    if (fb.getGray(i+-2, j+-2) < c_b)
                     if (fb.getGray(i+-1, j+-3) < c_b)
                      if (fb.getGray(i+0, j+-3) < c_b)
                       if (fb.getGray(i+1, j+-3) < c_b)
                        if (fb.getGray(i+2, j+-2) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 if (fb.getGray(i+0, j+3) > cb) 
                  if (fb.getGray(i+-1, j+3) > cb) 
                   if (fb.getGray(i+-2, j+2) > cb) 
                    if (fb.getGray(i+-3, j+1) > cb) 
                     if (fb.getGray(i+-3, j+0) > cb) 
                      if (fb.getGray(i+-3, j+-1) > cb) 
                       if (fb.getGray(i+-2, j+-2) > cb) 
                        if (fb.getGray(i+-1, j+-3) > cb) 
                         if (fb.getGray(i+0, j+-3) > cb) 
                          if (fb.getGray(i+1, j+-3) > cb) 
                           if (fb.getGray(i+2, j+-2) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
               else if (fb.getGray(i+1, j+3) < c_b) 
                if (fb.getGray(i+0, j+3) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-1, j+3) > cb) 
                   if (fb.getGray(i+-2, j+2) > cb) 
                    if (fb.getGray(i+-3, j+1) > cb) 
                     if (fb.getGray(i+-3, j+-1) > cb) 
                      if (fb.getGray(i+-2, j+-2) > cb) 
                       if (fb.getGray(i+-1, j+-3) > cb) 
                        if (fb.getGray(i+0, j+-3) > cb) 
                         if (fb.getGray(i+1, j+-3) > cb) 
                          if (fb.getGray(i+2, j+-2) > cb) 
                           if (fb.getGray(i+3, j+-1) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else if (fb.getGray(i+-3, j+0) < c_b) 
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else if (fb.getGray(i+0, j+3) < c_b) 
                 if (fb.getGray(i+-1, j+3) < c_b)
                  if (fb.getGray(i+-2, j+2) < c_b)
                   if (fb.getGray(i+-3, j+1) < c_b)
                    if (fb.getGray(i+-3, j+0) < c_b)
                     if (fb.getGray(i+-3, j+-1) < c_b)
                      if (fb.getGray(i+-2, j+-2) < c_b)
                       if (fb.getGray(i+-1, j+-3) < c_b)
                        return true;
                       else
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                      else
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                     else
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                    else
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                   else
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                  else
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                 else
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                else
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
               else
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+0, j+3) > cb) 
                  if (fb.getGray(i+-1, j+3) > cb) 
                   if (fb.getGray(i+-2, j+2) > cb) 
                    if (fb.getGray(i+-3, j+0) > cb) 
                     if (fb.getGray(i+-3, j+-1) > cb) 
                      if (fb.getGray(i+-2, j+-2) > cb) 
                       if (fb.getGray(i+-1, j+-3) > cb) 
                        if (fb.getGray(i+0, j+-3) > cb) 
                         if (fb.getGray(i+1, j+-3) > cb) 
                          if (fb.getGray(i+2, j+-2) > cb) 
                           if (fb.getGray(i+3, j+-1) > cb) 
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else if (fb.getGray(i+-3, j+1) < c_b) 
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
              else
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+0, j+3) > cb) 
                 if (fb.getGray(i+-1, j+3) > cb) 
                  if (fb.getGray(i+-3, j+1) > cb) 
                   if (fb.getGray(i+-3, j+0) > cb) 
                    if (fb.getGray(i+-3, j+-1) > cb) 
                     if (fb.getGray(i+-2, j+-2) > cb) 
                      if (fb.getGray(i+-1, j+-3) > cb) 
                       if (fb.getGray(i+0, j+-3) > cb) 
                        if (fb.getGray(i+1, j+-3) > cb) 
                         if (fb.getGray(i+2, j+-2) > cb) 
                          if (fb.getGray(i+1, j+3) > cb) 
                           return true;
                          else
                           if (fb.getGray(i+3, j+-1) > cb) 
                            return true;
                           else
                            return false;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else if (fb.getGray(i+-2, j+2) < c_b) 
                if (fb.getGray(i+-3, j+1) < c_b)
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) < c_b)
                          if (fb.getGray(i+0, j+3) < c_b)
                           if (fb.getGray(i+-1, j+3) < c_b)
                            return true;
                           else
                            return false;
                          else
                           return false;
                         else
                          return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
             else
              if (fb.getGray(i+-1, j+3) > cb) 
               if (fb.getGray(i+0, j+3) > cb) 
                if (fb.getGray(i+-2, j+2) > cb) 
                 if (fb.getGray(i+-3, j+1) > cb) 
                  if (fb.getGray(i+-3, j+0) > cb) 
                   if (fb.getGray(i+-3, j+-1) > cb) 
                    if (fb.getGray(i+-2, j+-2) > cb) 
                     if (fb.getGray(i+-1, j+-3) > cb) 
                      if (fb.getGray(i+0, j+-3) > cb) 
                       if (fb.getGray(i+1, j+-3) > cb) 
                        if (fb.getGray(i+1, j+3) > cb) 
                         if (fb.getGray(i+2, j+2) > cb) 
                          return true;
                         else
                          if (fb.getGray(i+2, j+-2) > cb) 
                           return true;
                          else
                           return false;
                        else
                         if (fb.getGray(i+2, j+-2) > cb) 
                          if (fb.getGray(i+3, j+-1) > cb) 
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else if (fb.getGray(i+-1, j+3) < c_b) 
               if (fb.getGray(i+-2, j+2) < c_b)
                if (fb.getGray(i+-3, j+1) < c_b)
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+-3) < c_b)
                       if (fb.getGray(i+2, j+-2) < c_b)
                        if (fb.getGray(i+3, j+-1) < c_b)
                         return true;
                        else
                         if (fb.getGray(i+1, j+3) < c_b)
                          if (fb.getGray(i+0, j+3) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                       else
                        if (fb.getGray(i+2, j+2) < c_b)
                         if (fb.getGray(i+1, j+3) < c_b)
                          if (fb.getGray(i+0, j+3) < c_b)
                           return true;
                          else
                           return false;
                         else
                          return false;
                        else
                         return false;
                      else
                       return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               return false;
            else
             if (fb.getGray(i+0, j+3) > cb) 
              if (fb.getGray(i+-1, j+3) > cb) 
               if (fb.getGray(i+-2, j+2) > cb) 
                if (fb.getGray(i+-3, j+1) > cb) 
                 if (fb.getGray(i+-3, j+0) > cb) 
                  if (fb.getGray(i+-3, j+-1) > cb) 
                   if (fb.getGray(i+-2, j+-2) > cb) 
                    if (fb.getGray(i+-1, j+-3) > cb) 
                     if (fb.getGray(i+0, j+-3) > cb) 
                      if (fb.getGray(i+1, j+3) > cb) 
                       if (fb.getGray(i+2, j+2) > cb) 
                        if (fb.getGray(i+3, j+1) > cb) 
                         return true;
                        else
                         if (fb.getGray(i+1, j+-3) > cb) 
                          return true;
                         else
                          return false;
                       else
                        if (fb.getGray(i+1, j+-3) > cb) 
                         if (fb.getGray(i+2, j+-2) > cb) 
                          return true;
                         else
                          return false;
                        else
                         return false;
                      else
                       if (fb.getGray(i+1, j+-3) > cb) 
                        if (fb.getGray(i+2, j+-2) > cb) 
                         if (fb.getGray(i+3, j+-1) > cb) 
                          return true;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               return false;
             else if (fb.getGray(i+0, j+3) < c_b) 
              if (fb.getGray(i+-1, j+3) < c_b)
               if (fb.getGray(i+-2, j+2) < c_b)
                if (fb.getGray(i+-3, j+1) < c_b)
                 if (fb.getGray(i+-3, j+0) < c_b)
                  if (fb.getGray(i+-3, j+-1) < c_b)
                   if (fb.getGray(i+-2, j+-2) < c_b)
                    if (fb.getGray(i+-1, j+-3) < c_b)
                     if (fb.getGray(i+0, j+-3) < c_b)
                      if (fb.getGray(i+1, j+3) < c_b)
                       if (fb.getGray(i+2, j+2) < c_b)
                        if (fb.getGray(i+3, j+1) < c_b)
                         return true;
                        else
                         if (fb.getGray(i+1, j+-3) < c_b)
                          return true;
                         else
                          return false;
                       else
                        if (fb.getGray(i+1, j+-3) < c_b)
                         if (fb.getGray(i+2, j+-2) < c_b)
                          return true;
                         else
                          return false;
                        else
                         return false;
                      else
                       if (fb.getGray(i+1, j+-3) < c_b)
                        if (fb.getGray(i+2, j+-2) < c_b)
                         if (fb.getGray(i+3, j+-1) < c_b)
                          return true;
                         else
                          return false;
                        else
                         return false;
                       else
                        return false;
                     else
                      return false;
                    else
                     return false;
                   else
                    return false;
                  else
                   return false;
                 else
                  return false;
                else
                 return false;
               else
                return false;
              else
               return false;
             else
              return false;
    }
    
    private static List<FeaturePoint> nonMaxSuppression(int width, int height, List<FeaturePoint> features){
            int[][] pixels = new int[height][width];
            ArrayList<FeaturePoint> nonMaxFeatures = new ArrayList<FeaturePoint>();
            
            for (int i = 0; i < features.size(); ++i) {
                    FeaturePoint fp = features.get(i);
                    pixels[fp.x][fp.y] = fp.score;
            }
            
            for (int i = 0; i < features.size(); ++i) {
                    FeaturePoint fp = features.get(i);
                    int x = fp.x;
                    int y = fp.y;
                    int score = fp.score;
                    if (score >= pixels[x-1][y+1] && score >= pixels[x-1][y] &&
                        score >= pixels[x-1][y-1] && score >= pixels[x][y+1] && 
                        score >= pixels[x][y-1] && score >= pixels[x+1][y+1] && 
                        score >= pixels[x+1][y] && score >= pixels[x+1][y-1]) {
                            nonMaxFeatures.add(fp);
                    }
            }
            return nonMaxFeatures;
    }
}