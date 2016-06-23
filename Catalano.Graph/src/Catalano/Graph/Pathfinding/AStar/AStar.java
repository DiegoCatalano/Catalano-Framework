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

import Catalano.Core.IntPoint;
import Catalano.Core.Structs.BinaryHeap;
import Catalano.Graph.Pathfinding.ISearch;
import java.util.ArrayList;

/**
 * AStar pathfinding.
 * @author Diego Catalano
 */
public class AStar implements ISearch{
    
    public static enum Neighbor {Four, Eight};
    public static enum Heuristic {Manhattan, Chebyshev, Euclidean, SquaredEuclidean};
    
    private int width;
    private int height;
    private Neighbor neighbor;
    private Heuristic heuristic;
    private NodeMap nodeMap;

    /**
     * Get width of the map.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height of the map.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get neighbor size of the algorithm.
     * @return Neighbor.
     */
    public Neighbor getNeighbor() {
        return neighbor;
    }

    /**
     * Set neighbor size of the algorithm.
     * @param neighbor Neighbor.
     */
    public void setNeighbor(Neighbor neighbor) {
        this.neighbor = neighbor;
    }

    /**
     * Get heuristic.
     * @return Heuristic.
     */
    public Heuristic getHeuristic() {
        return heuristic;
    }

    /**
     * Set heuristic.
     * @param heuristic Heuristic.
     */
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
    
    /**
     * Add block in the path.
     * Is the same with the cust of the node equals zero.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void addBlock(int x, int y){
        nodeMap.nodes[x][y].setCost(0);
    }
    
    /**
     * Remove block in the path.
     * Is the same with the cust of the node equals one.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void removeBlock(int x, int y){
        nodeMap.nodes[x][y].setCost(1);
    }
    
    /**
     * Verify if the actual node is blocked.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return True if is blocked, otherwise false.
     */
    public boolean isBlocked(int x, int y){
        return nodeMap.nodes[x][y].getCost() == 0;
    }
    
    /**
     * Get cost from a point.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Cost.
     */
    public double getCost(int x, int y){
        return this.nodeMap.nodes[x][y].getCost();
    }
    
    /**
     * Set cost.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param cost Cost.
     */
    public void setCost(int x, int y, double cost){
        this.nodeMap.nodes[x][y].setCost(cost);
    }

    /**
     * Get node map.
     * @return Node map.
     */
    public NodeMap getNodeMap() {
        return nodeMap;
    }
    
    /**
     * Get total cost.
     * @param path List of the points.
     * @return Total cost.
     */
    public double getTotalCost(ArrayList<IntPoint> path){
        double sum = 0;
        for (IntPoint p : path) {
            sum += nodeMap.getNode(p.x, p.y).getCost();
        }
        return sum;
    }
    
    /**
     * Initializes a new instance of the AStar class.
     * @param width Width of the Matrix.
     * @param height Height of the Matrix.
     */
    public AStar(int width, int height){
        this(width, height, 1, Neighbor.Eight, Heuristic.Euclidean);
    }
    
    /**
     * Initializes a new instance of the AStar class.
     * @param width Width of the Matrix.
     * @param height Height of the Matrix.
     * @param initialCost Initial cost for all nodes.
     */
    public AStar(int width, int height, double initialCost){
        this(width, height, initialCost, Neighbor.Eight, Heuristic.Euclidean);
    }
    
    /**
     * Initializes a new instance of the AStar class.
     * @param costMap Cost map.
     */
    public AStar(double[][] costMap){
        this(costMap, Neighbor.Eight, Heuristic.Euclidean);
    }
    
    /**
     * Initializes a new instance of the AStar class.
     * @param costMap Cost map.
     * @param neighbor Neighbor.
     * @param heuristic Heuristic.
     */
    public AStar(double[][] costMap, Neighbor neighbor, Heuristic heuristic){
        this.width = costMap[0].length;
        this.height = costMap.length;
        this.neighbor = neighbor;
        this.heuristic = heuristic;
        nodeMap = new NodeMap(costMap, neighbor, heuristic);
    }

    /**
     * Initializes a new instance of the AStar class.
     * @param width Width of the matrix.
     * @param height Height of the matrix.
     * @param initialCost Initial cost for all the nodes.
     * @param neighbor Neighbor.
     * @param heuristic Heuristic.
     */
    public AStar(int width, int height, double initialCost, Neighbor neighbor, Heuristic heuristic) {
        this.width = width;
        this.height = height;
        this.neighbor = neighbor;
        this.heuristic = heuristic;
        nodeMap = new NodeMap(width, height, initialCost, neighbor, heuristic);
    }
    
    private boolean AStar(NodeMap Map, int StartX, int StartY, int EndX, int EndY) {
        return AStar(Map, Map.nodes[StartX][StartY], Map.nodes[EndX][EndY]);
    }

    private boolean AStar(NodeMap Map, ANode Start, ANode End) {
        try{
            if (Start != null && End != null){
                if (Start.getMapParent() == Map && End.getMapParent() == Map) {
                    boolean PathFound = true;
                    BinaryHeap<ANode> OpenList = new BinaryHeap<ANode>();
                    BinaryHeap<ANode> ClosedList = new BinaryHeap<ANode>();
                    OpenList.add(Start);
                    Start.setOnOpenList(true);
                    while (!End.isOnClosedList()) {
                        if (OpenList.size() > 0) {
                            ANode Current = OpenList.remove();

                            ClosedList.add(Current);
                            Current.setOnClosedList(true);
                            ANode[] Surrounding = Current.getSurroundingNodes();
                            for (ANode Next : Surrounding) {
                                if (Next.getCost() > 0 && !Next.isOnClosedList()) {
                                    if (!Next.isOnOpenList()){
                                        Next.setParent(Current);
                                        Next.setG(Current.getG() + Next.getCost());
                                        Next.setH(Next.ComputeHeuristic(End));
                                        OpenList.add(Next);
                                        Next.setOnOpenList(true);
                                    }
                                    else {
                                        if (Current.getG() > (Next.getG() + Current.getCost())) {
                                            Current.setParent(Next);
                                            Current.setG(Next.getG() + Current.getCost());
                                            OpenList.remove(Current);
                                            OpenList.add(Current);
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            PathFound = false;
                            break;
                        }
                    }
                    return PathFound;
                }
                else
                {
                    throw new IllegalArgumentException("Start or End Node does not belong to the NodeMap passed into the function.");
                }
            }
            else
            {
                throw new IllegalArgumentException("Start or End Node were not initialised, ensure they are initialised and belong to a NodeMap.");
            }
        }
        catch (IllegalArgumentException Ex)
        {
            throw new IllegalArgumentException(Ex.getMessage());
        }
    }

    @Override
    public ArrayList<IntPoint> FindPath(IntPoint start, IntPoint end) {
        return FindPath(start.x, start.y, end.x, end.y);
    }

    @Override
    public ArrayList<IntPoint> FindPath(int startX, int startY, int endX, int endY) {
        
        ArrayList<IntPoint> path = new ArrayList<IntPoint>();
        
        NodeMap copy = nodeMap.clone();
        boolean result = AStar(copy, startX, startY, endX, endY);
        
        if(result){
            
            ANode cur = copy.getNode(endX, endY);
            ANode end = copy.getNode(startX, startY);
            
            try{
                while (true) {
                    if(cur.getParent() != end){
                        path.add(new IntPoint(cur.getParent().getX(), cur.getParent().getY()));
                        cur = cur.getParent();
                    }
                    else{
                        break;
                    }
                }
            }
            finally{
                return path;
            }
        }
        return path;
    }
}