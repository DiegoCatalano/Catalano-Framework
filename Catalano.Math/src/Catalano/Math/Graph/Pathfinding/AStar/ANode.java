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
    
    public int getX(){
        return m_X;
    }
    
    public void setX(int x){
        this.m_X = x;
    }
    
    public int getY(){
        return m_Y;
    }
    
    public void setY(int y){
        this.m_Y = y;
    }
    
    public double getCost(){
        return m_Cost;
    }
    
    public void setCost(double cost){
        this.m_Cost = cost;
    }
    
    public double getH(){
        return m_H;
    }
    
    public void setH(double h){
        this.m_H = h;
    }
    
    public double getG(){
        return m_G;
    }
    
    public void setG(double g){
        this.m_G = g;
    }
    
    public double getF(){
        return this.m_G + this.m_H;
    }

    public boolean isOnOpenList() {
        return m_onOpenList;
    }

    public void setOnOpenList(boolean onOpenList) {
        this.m_onOpenList = onOpenList;
    }
    
    public boolean isOnClosedList() {
        return m_onClosedList;
    }

    public void setOnClosedList(boolean m_onClosedList) {
        this.m_onClosedList = m_onClosedList;
    }
    
    public ANode getParent(){
        return this.m_Parent;
    }
    
    public void setParent(ANode node){
        this.m_Parent = node;
    }
    
    public NodeMap getMapParent(){
        return this.m_mapParent;
    }
    
    public void setMapParent(NodeMap nodeMap){
        this.m_mapParent = nodeMap;
    }
    
    public ANode[] getSurroundingNodes()
    {
        return m_mapParent.getSurroundingNodes(m_X, m_Y);
    }
    
    public double CalcHeuristic(ANode Target)
    {
        return CalcHeuristic(Target.getX(), Target.getY());
    }

    public double CalcHeuristic(int X, int Y)
    {
        double H = m_mapParent.CalculateHeuristic(m_X, m_Y, X, Y);
        if (H != -1)
        {
            return H * m_Cost;
        }
        
        return H;
        
    }
    
    public ANode(int x, int y, double cost, NodeMap mapParent){
        this.m_X = x;
        this.m_Y = y;
        this.m_Cost = cost;
        this.m_mapParent = mapParent;

        this.m_onClosedList = false;
        this.m_onOpenList = false;
    }
    
    public ANode clone(NodeMap parent){
        ANode copy = new ANode(m_X, m_Y, m_Cost, parent);
        return copy;
    }

    @Override
    public int compareTo(ANode o) {
        return Double.compare(getF(), o.getF());
    }
    
}
