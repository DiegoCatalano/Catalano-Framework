// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright (c) 2015 Htet Aung Shine
// All rights reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy 
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished to do
// so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

package Catalano.Imaging.Parsers;

import Catalano.Imaging.FastBitmap;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

/**
 * GIF Animated Encoder.
 * @author Diego Catalano
 */
public class GifEncoder {
    
    private int milliseconds;
    private final String saveFile;
    
    private boolean firstFrame = true;
    private ImageTypeSpecifier spec;
    private ImageWriter wr;

    /**
     * Initializes a new instance of the GifEncoder class.
     * @param saveToFile Path where the file will be saved.
     */
    public GifEncoder(String saveToFile) {
        this(saveToFile, 100);
    }
    
    /**
     * Path where the file will be saved.
     * @param saveToFile Path where the file will be saved.
     * @param milliseconds Milliseconds between the frames.
     */
    public GifEncoder(String saveToFile, int milliseconds){
        this.saveFile = saveToFile;
        this.milliseconds = milliseconds / 10;
    }
    
    /**
     * Add frame in the sequence.
     * @param fastBitmap Image.
     */
    public void addFrame(FastBitmap fastBitmap){
        try {
            if(firstFrame){
                firstFrame = false;
                spec = new ImageTypeSpecifier(fastBitmap.toBufferedImage());
                
                wr = ImageIO.getImageWriters(spec, "GIF").next();
                wr.setOutput(ImageIO.createImageOutputStream(new File(saveFile)));
                
                IIOMetadata metadata = getMetadata(wr, fastBitmap.toBufferedImage().getType(), milliseconds);
                wr.prepareWriteSequence(metadata);
                wr.writeToSequence(new IIOImage(fastBitmap.toBufferedImage(), null, metadata), null);
            }
            wr.writeToSequence(new IIOImage(fastBitmap.toBufferedImage(), null, null), null);
        } catch (IOException ex) {
            Logger.getLogger(GifEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * End the sequence and save the animated gif.
     */
    public void finish(){
        try {
            firstFrame = true;
            wr.endWriteSequence();
            wr.dispose();
        } catch (IOException ex) {
            Logger.getLogger(GifEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private IIOMetadata getMetadata(ImageWriter writer, int type, int delay)
        throws IIOInvalidTreeException{
        
        // Type of the image
        ImageTypeSpecifier img_type = ImageTypeSpecifier.createFromBufferedImageType(type);
        
        // Get the metadata of type of image.
        IIOMetadata metadata = writer.getDefaultImageMetadata(img_type, null);
        String native_format = metadata.getNativeMetadataFormatName();
        IIOMetadataNode node_tree = (IIOMetadataNode)metadata.getAsTree(native_format);

        // Set the delay time you can see the format specification on this page
        // https://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/gif_metadata.html
        IIOMetadataNode graphics_node = getNode(node_tree, "GraphicControlExtension");
        graphics_node.setAttribute("delayTime", String.valueOf(delay));
        graphics_node.setAttribute("disposalMethod", "none");
        graphics_node.setAttribute("userInputFlag", "FALSE");

        metadata.setFromTree(native_format, node_tree);

        return metadata;
    }
    
    private IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)== 0) {
            return((IIOMetadataNode) rootNode.item(i));
            }
       }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
   }
}