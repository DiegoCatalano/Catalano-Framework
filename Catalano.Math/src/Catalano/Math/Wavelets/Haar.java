// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.Math.Wavelets;

/**
 * Haar Wavelet Transform.
 * @author Diego Catalano
 */
public class Haar implements IWavelet{
    
    private static final double w0 = 0.5;
    private static final double w1 = -0.5;
    private static final double s0 = 0.5;
    private static final double s1 = 0.5;
    private int levels = 2;

    /**
     * Constructs a new Haar Wavelet Transform.
     * @param levels Levels.
     */
    public Haar(int levels) {
        this.levels = levels;
    }
    
    @Override
    public void Forward(double[] data) {
        double[] temp = new double[data.length];
        int h = data.length >> 1;
        
        for (int i = 0; i < h; i++){
            int k = (i << 1);
            temp[i] = data[k] * s0 + data[k + 1] * s1;
            temp[i + h] = data[k] * w0 + data[k + 1] * w1;
        }

        for (int i = 0; i < data.length; i++)
            data[i] = temp[i];
    }

    @Override
    public void Forward(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        
        double[] row = new double[cols];
        double[] col = new double[rows];
        
            for (int k = 0; k < levels; k++)
            {
                for (int i = 0; i < rows; i++)
                {
                    for (int j = 0; j < row.length; j++)
                        row[j] = data[i][j];

                    Forward(row);

                    for (int j = 0; j < row.length; j++)
                        data[i][j] = row[j];
                }

                for (int j = 0; j < cols; j++)
                {
                    for (int i = 0; i < col.length; i++)
                        col[i] = data[i][j];

                    Forward(col);

                    for (int i = 0; i < col.length; i++)
                        data[i][j] = col[i];
                }
            }
    }

    @Override
    public void Backward(double[] data) {
        double[] temp = new double[data.length];
        int h = data.length >> 1;
        
        for (int i = 0; i < h; i++){
            int k = (i << 1);
            temp[k] = (data[i] * s0 + data[i + h] * w0) / w0;
            temp[k + 1] = (data[i] * s1 + data[i + h] * w1) / s0;
        }

        for (int i = 0; i < data.length; i++)
            data[i] = temp[i];
    }

    @Override
    public void Backward(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        
        double[] row = new double[cols];
        double[] col = new double[rows];
        
        for (int l = 0; l < levels; l++){
            for (int j = 0; j < cols; j++){
                for (int i = 0; i < row.length; i++)
                    col[i] = data[i][j];

                Backward(col);

                for (int i = 0; i < col.length; i++)
                    data[i][j] = col[i];
            }

            for (int i = 0; i < rows; i++){
                for (int j = 0; j < row.length; j++)
                    row[j] = data[i][j];

                Backward(row);

                for (int j = 0; j < row.length; j++)
                    data[i][j] = row[j];
            }
        }
    }
}
