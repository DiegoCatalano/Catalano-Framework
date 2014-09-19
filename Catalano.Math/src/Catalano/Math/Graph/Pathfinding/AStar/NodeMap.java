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

import java.util.ArrayList;

/**
 * Represents all nodes associates a map.
 * @author Diego Catalano
 */
public class NodeMap {
    
    private int width;
    private int height;
    private int id;
    private String name;
    public ANode[][] nodes;
    private AStar.Neighbor direction;
    private AStar.Heuristic heuristic;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ANode getNode(int x, int y){
        return nodes[x][y];
    }
    
    public void setNode(int x, int y, ANode node){
        nodes[x][y] = node;
    }

    public AStar.Neighbor getDirection() {
        return direction;
    }

    public void setDirection(AStar.Neighbor direction) {
        this.direction = direction;
    }

    public AStar.Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(AStar.Heuristic heuristic) {
        this.heuristic = heuristic;
    }
    
    public NodeMap(int width, int height, double initialCost){
        this.width = width;
        this.height = height;

        nodes = new ANode[height][width];
        for (int x = 0; x < height; x++)
        {
            for (int y = 0; y < width; y++)
            {
                nodes[x][y] = new ANode(x, y, initialCost, this);
            }
        }

        direction = AStar.Neighbor.Four;
        heuristic = AStar.Heuristic.Manhattan;
    }


        public NodeMap(int width, int height, double[][] nodeCostMap)
        {
            this.width = width;
            this.height = height;

            nodes = new ANode[height][width];
            for (int x = 0; x < height; x++)
            {
                for (int y = 0; y < width; y++)
                {
                    nodes[x][y] = new ANode(x, y, nodeCostMap[x][y], this);
                }
            }

            direction = AStar.Neighbor.Four;
            heuristic = AStar.Heuristic.Manhattan;
        }
        
        public NodeMap(int width, int height, double initialCost, AStar.Neighbor direction, AStar.Heuristic heuristic)
        {
            this.width = width;
            this.height = height;

            nodes = new ANode[height][width];
            for (int x = 0; x < height; x++)
            {
                for (int y = 0; y < width; y++)
                {
                    nodes[x][y] = new ANode(x, y, initialCost, this);
                }
            }

            this.direction = direction;
            this.heuristic = heuristic;
        }


        public NodeMap(double[][] costMap, AStar.Neighbor direction, AStar.Heuristic heuristic)
        {
            this.width = costMap[0].length;
            this.height = costMap.length;

            nodes = new ANode[height][width];
            for (int x = 0; x < height; x++)
            {
                for (int y = 0; y < width; y++)
                {
                    nodes[x][y] = new ANode(x, y, costMap[x][y], this);
                }
            }

            this.direction = direction;
            this.heuristic = heuristic;
        }

        //Used for Cloning
        private NodeMap(int width, int height, AStar.Neighbor direction, AStar.Heuristic heuristic)
        {
            this.width = width;
            this.height = height;

            nodes = new ANode[height][width];

            this.direction = direction;
            this.heuristic = heuristic;
        }
        
        public ANode[] getSurroundingNodes(int x, int y){
            ArrayList<ANode> tmpNodes = new ArrayList<ANode>(8);
            switch (direction)
            {
                case Four:
                    if (x - 1 >= 0)
                    {
                        tmpNodes.add(nodes[x - 1][y]);
                    }

                    if (x + 1 < height)
                    {
                        tmpNodes.add(nodes[x + 1][y]);
                    }

                    if (y - 1 >= 0)
                    {
                        tmpNodes.add(nodes[x][y - 1]);
                    }

                    if (y + 1 < width)
                    {
                        tmpNodes.add(nodes[x][y + 1]);
                    }
                break;

                case Eight:
                    for (int tmpX = -1; tmpX <= 1; tmpX++)
                    {
                        for (int tmpY = -1; tmpY <= 1; tmpY++)
                        {
                            if (!(tmpY == 0 && tmpX == 0))
                            {
                                if (((x + tmpX) >= 0 && (x + tmpX) < height) && ((y + tmpY) >= 0 && (y + tmpY) < width))
                                {
                                    tmpNodes.add(nodes[x + tmpX][y + tmpY]);
                                }
                            }
                        }
                    }
                    break;
            }
            return tmpNodes.toArray(new ANode[tmpNodes.size()]);
        }
        
        public double CalculateHeuristic(int startX, int startY, int endX, int endY){
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