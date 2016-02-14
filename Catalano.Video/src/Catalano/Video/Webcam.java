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
 *   *	Neither the name of the Sleeping Dumpling LLC nor the names of its
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
package Catalano.Video;

import java.util.List;

import org.bridj.Pointer;

public class Webcam {

	public final static int DEFAULT_REQUESTED_FPS = 25;

	private OpenIMAJGrabber grabber;
	private int width;
	private int height;
	private int fps;

	public Webcam(int width, int height) throws WebcamException {
		this(width, height, DEFAULT_REQUESTED_FPS);
	}

	public Webcam(int width, int height, int fps) throws WebcamException {
		this.init(width, height, fps, null);
	}

	public Webcam(int width, int height, int fps, String deviceName) throws WebcamException {
		Device defaultDevice = null;
		List<Device> devices = Webcam.getVideoDevices();
		for (Device d : devices) {
			if (d.getIdentifierStr().contains(deviceName)) {
				defaultDevice = d;
				break;
			}
		}
		if (defaultDevice == null) {
			System.err.println("Warning: The device name given by the name \""
					+ deviceName + "\" was not found and has been ignored.");
			System.err.println("Valid devices are:");
			for (int x = 0; x < devices.size(); x++)
				System.err.println(x + " : "
						+ devices.get(x).getIdentifierStr());
		}

		this.init(width, height, fps, defaultDevice);
	}

	public Webcam(int width, int height, int fps, int deviceNumber) throws WebcamException {
		List<Device> devices = Webcam.getVideoDevices();

		Device defaultDevice = null;
		if (deviceNumber >= 0 && deviceNumber < devices.size()) {
			defaultDevice = devices.get(deviceNumber);
		} else {
			System.err.println("Warning: The deviceNumber \"" + deviceNumber
					+ "\" is out of range (0..<" + devices.size()
					+ ") and will be ignored.");
			System.err.println("Valid devices are:");
			for (int x = 0; x < devices.size(); x++)
				System.err.println(x + " : "
						+ devices.get(x).getIdentifierStr());
		}

		this.init(width, height, fps, defaultDevice);
	}

	public Webcam(int width, int height, int fps, Device device) throws WebcamException {
		this.init(width, height, fps, device);
	}

	private void init(int width, int height, int fps, Device defaultDevice) throws WebcamException {
		this.width = width;
		this.height = height;
		this.fps = fps;
		
		grabber = new OpenIMAJGrabber();

		if (defaultDevice == null) {
			if (!startSession(this.width, this.height, this.fps)) {
				throw new WebcamException("No video input device found!");
			}
		}
		else {
			if (!startSession(this.width, this.height, this.fps, defaultDevice)) {
				throw new WebcamException(
						"An error occured opening the capture device");
			}
		}
	}

	protected synchronized boolean startSession(final int requestedWidth, final int requestedHeight, double requestedFPS, Device device) {
		if (grabber.startSession(requestedWidth, requestedHeight, requestedFPS,
				Pointer.pointerTo(device))) {
			width = grabber.getWidth();
			height = grabber.getHeight();
			return true;
		}
		return false;
	}

	protected synchronized boolean startSession(int requestedWidth, int requestedHeight, double requestedFPS) {
		if (grabber.startSession(requestedWidth, requestedHeight, requestedFPS)) {
			width = grabber.getWidth();
			height = grabber.getHeight();
			return true;
		}
		return false;
	}

	public synchronized VideoFrame getNextFrame(VideoFrame frame) {

		grabber.nextFrame();
		
		final Pointer<Byte> data = grabber.getImage();
		if (data == null) {
			//no data, return null;
			frame = null;
		}
		else {
			final byte[] d = data.getBytes(width * height * 3);

			if (frame == null) {
				frame = new VideoFrame(this.width, this.height);
			}

			frame.setRawData(d);
		}
		
		return frame;
	}
	
	/**
	 * Stop the video capture system. Once stopped, it can only be started again
 by constructing a new instance of Webcam.
	 */
	public synchronized void stopSession() {
		grabber.stopSession();
	}

	/**
	 * Get a list of all compatible video devices attached to the machine.
	 * 
	 * @return a list of devices.
	 */
	public static List<Device> getVideoDevices() {
		OpenIMAJGrabber grabber = new OpenIMAJGrabber();
		DeviceList list = grabber.getVideoDevices().get();

		return list.asArrayList();
	}
}
