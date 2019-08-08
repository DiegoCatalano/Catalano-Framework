// Catalano Graph Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
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
package Catalano.Graph.Pathfinding;

import Catalano.Core.IntPoint;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Wavefront Planning.
 * 
 * Rules: 0 means block.
 *      : 1 means path.
 * 
 * @author Diego Catalano
 */
public class WavefrontPlanning {
    
    private int maxValue = 0;

    public int getMaxValue() {
        return maxValue;
    }

    public WavefrontPlanning() {}
    
    public int[][] Compute(int[][] map, IntPoint start){
        return Compute(map, start.x, start.y);
    }
    
    public int[][] Compute(int[][] map, int startX, int startY){
        
        maxValue = 0;
        int[][] r = new int[map.length][map[0].length];
        
        if(map[startX][startY] == 0) return map;
        
        Queue<IntPoint> points = new LinkedList<>();
        points.add(new IntPoint(startX, startY));
        
        int index = 1;
        while(points.size() > 0){
            IntPoint p = points.poll();
            
            //Check neighbourdhood
            //Top
            if(r[p.x - 1][p.y - 1] == 1){
                points.add(new IntPoint(p.x - 1, p.y - 1));
                map[p.x - 1][p.y - 1] = index;
            }
            
            if(r[p.x - 1][p.y] == 1){
                points.add(new IntPoint(p.x - 1, p.y));
                map[p.x - 1][p.y] = index;
            }
            
            if(r[p.x - 1][p.y + 1] == 1){
                points.add(new IntPoint(p.x - 1, p.y + 1));
                map[p.x - 1][p.y + 1] = index;
            }
            
            //Mid
            if(r[p.x][p.y - 1] == 1){
                points.add(new IntPoint(p.x, p.y - 1));
                map[p.x][p.y - 1] = index;
            }
            
            if(r[p.x][p.y + 1] == 1){
                points.add(new IntPoint(p.x, p.y + 1));
                map[p.x][p.y + 1] = index;
            }
            
            //Bottom
            if(r[p.x + 1][p.y - 1] == 1){
                points.add(new IntPoint(p.x + 1, p.y - 1));
                map[p.x + 1][p.y - 1] = index;
            }
            
            if(r[p.x + 1][p.y] == 1){
                points.add(new IntPoint(p.x + 1, p.y));
                map[p.x + 1][p.y] = index;
            }
            
            if(r[p.x + 1][p.y + 1] == 1){
                points.add(new IntPoint(p.x + 1, p.y + 1));
                map[p.x + 1][p.y + 1] = index;
            }
            
            index++;
            maxValue = index;
        }
        
        return r;
        
    }
    
}
