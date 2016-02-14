// Catalano Video Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

/**
 * Copyright (c) 2013, The Sleeping Dumpling LLC and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package Catalano.Video.Swing;

import Catalano.Video.*;
import Catalano.Imaging.FastBitmap;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.concurrent.locks.LockSupport;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Diego
 */
public class WebcamPanel extends JPanel{
    
    private int width;
    private int height;
    private int fps;
    
    private Webcam videoInput;
    private BufferedImage displayImage;
    private double videoFps;
    private final static long ONE_SECOND_IN_NANOS = 1000000000L;

    public WebcamPanel(int width, int height, int fps) {
        this.width = width;
        this.height = height;
        this.fps = fps;
    }
    
    public void Start(){
        startRetrieverThread(width, height, fps);
    }
    
    public void Stop(){
        videoInput.stopSession();
    }
    
    public double CurrentFPS(){
        return videoFps;
    }
    
    public FastBitmap GrabImage(){
        if(displayImage == null)
            return null;
        return new FastBitmap(displayImage);
    }
    
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (displayImage != null) {
			Graphics2D g2 = (Graphics2D) g.create();
                        g2.drawImage(displayImage, 0, 0, null);
			g2.dispose();
			//String fpsString = this.fpsFormat.format(this.videoFps) + " fps";
			//int textWidth = (int) g2.getFontMetrics().getStringBounds(fpsString, g2).getWidth();
			//g2.setColor(Color.BLACK);
			//g2.drawString(fpsString, getWidth() - textWidth - 5, getHeight() - 5);
			//g2.setColor(Color.ORANGE);
			//g2.drawString(fpsString, getWidth() - textWidth - 6, getHeight() - 6);
			//g2.dispose();
		}
	}
    
	private void startRetrieverThread(final int width, final int height, final int fps) {
            Thread videoFrameRetrieverThread = new Thread() {
                public void run() {
                        retrieveAndDisplay(width, height, fps);
                }
            };
            videoFrameRetrieverThread.start();
	}
	
	private void retrieveAndDisplay(int width, int height, int fps) {
            try {
                videoInput = new Webcam(width, height);
                final long interval = ONE_SECOND_IN_NANOS / fps;
                long lastReportTime = -1;
                long imgCnt = 0;

                while (true) {
                    long start = System.nanoTime();
                    try {
                        VideoFrame vf = videoInput.getNextFrame(null);
                        if (vf != null) {
                            this.displayImage = getRenderingBufferedImage(vf);
                            imgCnt++;

                            SwingUtilities.invokeAndWait(new Runnable() {
                                    public void run() {
                                            paintImmediately(0, 0, getWidth(), getHeight());
                                    }
                            });

                            long now = System.nanoTime();
                            if (lastReportTime != -1 &&  now - lastReportTime >= ONE_SECOND_IN_NANOS) {
                                    videoFps = ((double)imgCnt*ONE_SECOND_IN_NANOS)/(now - lastReportTime);
                                    imgCnt = 0;
                                    lastReportTime = now;
                            }
                            else if (lastReportTime == -1) {
                                    lastReportTime = now;
                            }
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    finally {
                        long end = System.nanoTime();
                        long waitTime = interval - (end - start);
                        if (waitTime > 0) {
                            LockSupport.parkNanos(waitTime);
                        }
                    }
                }
            }
            catch (WebcamException e1) {
                    e1.printStackTrace();
            }
	}
        
	private BufferedImage getRenderingBufferedImage(VideoFrame videoFrame) {
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage img = gc.createCompatibleImage(videoFrame.getWidth(), videoFrame.getHeight(), Transparency.OPAQUE);
            if (img.getType() == BufferedImage.TYPE_INT_ARGB 
                || img.getType() == BufferedImage.TYPE_INT_ARGB_PRE
                || img.getType() == BufferedImage.TYPE_INT_RGB) {
                WritableRaster raster = img.getRaster();
                DataBufferInt dataBuffer = (DataBufferInt) raster.getDataBuffer();

                byte[] data = videoFrame.getRawData();
                addAlphaChannel(data, data.length, dataBuffer.getData());
                return img; //convert the data ourselves, the performance is much better
            }
            else {
                return videoFrame.getBufferedImage(); //much slower when drawing it on the screen.
            }
	}	
	
	private void addAlphaChannel(byte[] rgbBytes, int bytesLen, int[] argbInts) {
            for (int i = 0, j = 0; i < bytesLen; i += 3, j++) {
                argbInts[j] = ((byte) 0xff) << 24 			|		// Alpha
                                (rgbBytes[i] << 16) & (0xff0000) 	|		// Red
                                (rgbBytes[i + 1] << 8) & (0xff00) 	|		// Green
                                (rgbBytes[i + 2]) & (0xff); 				// Blue
            }
	}
}