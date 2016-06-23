// Catalano Graph Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Muhammad Ahmed, 2011
// quteahmed at gmail.com
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
// Original license below:
// This software is provided 'as-is', without any express or
// implied warranty. In no event will the authors be held
// liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute
// it freely, subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
// you must not claim that you wrote the original software.
// If you use this software in a product, an acknowledgment
// in the product documentation would be appreciated but
// is not required.
//
// 2. Altered source versions must be plainly marked as such,
// and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any
// source distribution.
//

package Catalano.Graph.Pathfinding.AStar;

import java.util.ArrayList;

/**
 * Represents all nodes associates a map.
 * @author Diego Catalano
 */
public class NodeMap {
    
    private int width;
    private int height;
    public ANode[][] nodes;
    private AStar.Neighbor direction;
    private AStar.Heuristic heuristic;

    /**
     * Get width of Node Map.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height of Node Map.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Get ANode from associate in this Node map.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return ANode.
     */
    public ANode getNode(int x, int y){
        return nodes[x][y];
    }
    
    /**
     * Set ANode.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param node ANode.
     */
    public void setNode(int x, int y, ANode node){
        nodes[x][y] = node;
    }

    /**
     * Get direction.
     * @return Direction.
     */
    public AStar.Neighbor getDirection() {
        return direction;
    }

    /**
     * Set direction.
     * @param direction Direction.
     */
    public void setDirection(AStar.Neighbor direction) {
        this.direction = direction;
    }

    /**
     * Get heuristic.
     * @return Heuristic.
     */
    public AStar.Heuristic getHeuristic() {
        return heuristic;
    }

    /**
     * Set heuristic.
     * @param heuristic Heuristic.
     */
    public void setHeuristic(AStar.Heuristic heuristic) {
        this.heuristic = heuristic;
    }
    
    /**
     * Initializes a new instance of the NodeMap class.
     * @param width Width.
     * @param height Height.
     * @param initialCost Initial cost to all nodes.
     */
    public NodeMap(int width, int height, double initialCost){
        this.width = width;
        this.height = height;

        nodes = new ANode[height][width];
        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                nodes[x][y] = new ANode(x, y, initialCost, this);
            }
        }

        direction = AStar.Neighbor.Four;
        heuristic = AStar.Heuristic.Manhattan;
    }

    /**
     * Initializes a new instance of the NodeMap class.
     * @param nodeCostMap Cost map.
     */
    public NodeMap(double[][] nodeCostMap){
        this.width = nodeCostMap[0].length;
        this.height = nodeCostMap.length;

        nodes = new ANode[height][width];
        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                nodes[x][y] = new ANode(x, y, nodeCostMap[x][y], this);
            }
        }

        direction = AStar.Neighbor.Four;
        heuristic = AStar.Heuristic.Manhattan;
    }
    
    /**
     * Initializes a new instance of the NodeMap class.
     * @param width Width.
     * @param height Height.
     * @param initialCost Initial cost.
     * @param direction Direction.
     * @param heuristic Heuristic.
     */
    public NodeMap(int width, int height, double initialCost, AStar.Neighbor direction, AStar.Heuristic heuristic){
        this.width = width;
        this.height = height;

        nodes = new ANode[height][width];
        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                nodes[x][y] = new ANode(x, y, initialCost, this);
            }
        }

        this.direction = direction;
        this.heuristic = heuristic;
    }

    /**
     * Initializes a new instance of the NodeMap class.
     * @param costMap Cost map.
     * @param direction Direction.
     * @param heuristic Heuristic.
     */
    public NodeMap(double[][] costMap, AStar.Neighbor direction, AStar.Heuristic heuristic){
        this.width = costMap[0].length;
        this.height = costMap.length;

        nodes = new ANode[height][width];
        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                nodes[x][y] = new ANode(x, y, costMap[x][y], this);
            }
        }

        this.direction = direction;
        this.heuristic = heuristic;
    }

    /*
    Used for cloning.
    */
    private NodeMap(int width, int height, AStar.Neighbor direction, AStar.Heuristic heuristic)
    {
        this.width = width;
        this.height = height;

        nodes = new ANode[height][width];

        this.direction = direction;
        this.heuristic = heuristic;
    }
    
    /**
     * Get the neighbour nodes from the specified point.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Surrounding nodes.
     */
    public ANode[] getSurroundingNodes(int x, int y){
        ArrayList<ANode> tmpNodes = new ArrayList<ANode>(8);
        switch (direction){
            case Four:
                if (x - 1 >= 0)     tmpNodes.add(nodes[x - 1][y]);
                if (x + 1 < height) tmpNodes.add(nodes[x + 1][y]);
                if (y - 1 >= 0)     tmpNodes.add(nodes[x][y - 1]);
                if (y + 1 < width)  tmpNodes.add(nodes[x][y + 1]);
            break;

            case Eight:
                if (x - 1 >= 0 && y - 1 >= 0)        tmpNodes.add(nodes[x - 1][y - 1]);
                if (x - 1 >= 0)                      tmpNodes.add(nodes[x - 1][y]);
                if (x - 1 >= 0 && y + 1 < width)     tmpNodes.add(nodes[x - 1][y + 1]);
                
                if (y - 1 >= 0)                      tmpNodes.add(nodes[x][y - 1]);
                if (y + 1 < width)                   tmpNodes.add(nodes[x][y + 1]);
                
                if (x + 1 < height && y - 1 >= 0)    tmpNodes.add(nodes[x + 1][y - 1]);
                if (x + 1 < height)                  tmpNodes.add(nodes[x + 1][y]);
                if (x + 1 < height && y + 1 < width) tmpNodes.add(nodes[x + 1][y + 1]);
                break;
        }
        return tmpNodes.toArray(new ANode[tmpNodes.size()]);
    }
    
    /**
     * Compute the heuristic.
     * @param startX X axis coordinate.
     * @param startY Y axis coordinate.
     * @param endX X axis coordinate.
     * @param endY Y axis coordinate.
     * @return Value.
     */
    public double ComputeHeuristic(int startX, int startY, int endX, int endY){
        double H = -1;

        switch (heuristic){
            case Manhattan:
                H = Math.abs(startX - endX) + Math.abs(startY - endY);
                break;

            case Chebyshev:
                H = Math.max(Math.abs(startX - endX), Math.abs(startY - endY));
                break;

            case Euclidean:
                H = Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
                break;

            case SquaredEuclidean:
                H = Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2);
                break;

        }
        return (double)H;
    }

    @Override
    public NodeMap clone() {
        NodeMap copy = new NodeMap(width, height, direction, heuristic);
        for (int x = 0; x < height; x++)
        {
            for (int y = 0; y < width; y++)
            {
                copy.setNode(x, y, this.getNode(x, y).clone(copy));
            }
        }
        return copy;
    }
}