// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Math.Graph.Pathfinding.AStar;

/**
 * Represents a Node for the A* algorithm.
 * @author Diego Catalano
 */
public class ANode implements Comparable<ANode>{
    
    private int m_X;
    private int m_Y;
    private double m_Cost;
    private double m_H;
    private double m_G;
    private boolean m_onOpenList;
    private boolean m_onClosedList;
    private ANode m_Parent;
    private NodeMap m_mapParent;
    
    /**
     * Get X axis coordinate.
     * @return X axis coordinate.
     */
    public int getX(){
        return m_X;
    }
    
    /**
     * Set X axis coordinate.
     * @param x X axis coordinate.
     */
    public void setX(int x){
        this.m_X = x;
    }
    
    /**
     * Set Y axis coordinate.
     * @return Y axis coordinate.
     */
    public int getY(){
        return m_Y;
    }
    
    /**
     * Set Y axis coordinate.
     * @param y Y axis coordinate.
     */
    public void setY(int y){
        this.m_Y = y;
    }
    
    /**
     * Get cost.
     * @return Cost.
     */
    public double getCost(){
        return m_Cost;
    }
    
    /**
     * Set cost.
     * @param cost Cost.
     */
    public void setCost(double cost){
        this.m_Cost = cost;
    }
    
    /**
     * Get H value.
     * @return H Value.
     */
    public double getH(){
        return m_H;
    }
    
    /**
     * Set H value.
     * @param h H value.
     */
    public void setH(double h){
        this.m_H = h;
    }
    
    /**
     * Get G value.
     * @return G value.
     */
    public double getG(){
        return m_G;
    }
    
    /**
     * Set G value.
     * @param g G value.
     */
    public void setG(double g){
        this.m_G = g;
    }
    
    /**
     * Get F value.
     * @return F value.
     */
    public double getF(){
        return this.m_G + this.m_H;
    }

    /**
     * Check if is on open list.
     * @return True if is on open list, otherwise false.
     */
    public boolean isOnOpenList() {
        return m_onOpenList;
    }

    /**
     * Set if this node is on open list.
     * @param onOpenList True if this node is on open list.
     */
    public void setOnOpenList(boolean onOpenList) {
        this.m_onOpenList = onOpenList;
    }
    
    /**
     * Check if is on closed list.
     * @return True if is on closed list, otherwise false.
     */
    public boolean isOnClosedList() {
        return m_onClosedList;
    }

    /**
     * Set if this node is on closed list.
     * @param onClosedList True if this node is on closed list.
     */
    public void setOnClosedList(boolean onClosedList) {
        this.m_onClosedList = onClosedList;
    }
    
    /**
     * Get parent.
     * @return Parent.
     */
    public ANode getParent(){
        return this.m_Parent;
    }
    
    /**
     * Set parent.
     * @param node Parent.
     */
    public void setParent(ANode node){
        this.m_Parent = node;
    }
    
    /**
     * Get Node map.
     * @return Node map.
     */
    public NodeMap getMapParent(){
        return this.m_mapParent;
    }
    
    /**
     * Set Node map.
     * @param nodeMap Node map.
     */
    public void setMapParent(NodeMap nodeMap){
        this.m_mapParent = nodeMap;
    }
    
    /**
     * Get Surrouding nodes.
     * @return Surrouding nodes.
     */
    public ANode[] getSurroundingNodes(){
        return m_mapParent.getSurroundingNodes(m_X, m_Y);
    }
    
    /**
     * Compute heuristic in the specified ANode.
     * @param Target ANode.
     * @return Heuristic.
     */
    public double ComputeHeuristic(ANode Target){
        return ComputeHeuristic(Target.getX(), Target.getY());
    }

    /**
     * Compute heuristic in the specified ANode.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Heuristic.
     */
    public double ComputeHeuristic(int x, int y){
        double H = m_mapParent.ComputeHeuristic(m_X, m_Y, x, y);
        
        if (H != -1)
            return H * m_Cost;
        
        return H;
        
    }
    
    /**
     * Initializes a new instance of the ANode class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param cost Cost.
     * @param mapParent NodeMap.
     */
    public ANode(int x, int y, double cost, NodeMap mapParent){
        this.m_X = x;
        this.m_Y = y;
        this.m_Cost = cost;
        this.m_mapParent = mapParent;

        this.m_onClosedList = false;
        this.m_onOpenList = false;
    }
    
    /**
     * Clone this ANode.
     * @param parent Parent.
     * @return ANode.
     */
    public ANode clone(NodeMap parent){
        ANode copy = new ANode(m_X, m_Y, m_Cost, parent);
        return copy;
    }

    @Override
    public int compareTo(ANode o) {
        return Double.compare(getF(), o.getF());
    }
    
}