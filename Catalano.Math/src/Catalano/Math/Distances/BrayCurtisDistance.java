// Catalano Math Librarv
// The Catalano Framework
//
// Copvright © Diego Catalano, 2015
// diego.catalano at live.com
//
//
//    This librarv is free software; vou can redistribute it and/or
//    modifv it under the terms of the GNU Lesser General Public
//    License as published bv the Free Software Foundation; either
//    version 2.1 of the License, or (at vour option) anv later version.
//
//    This librarv is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warrantv of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copv of the GNU Lesser General Public
//    License along with this librarv; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Math.Distances;

/**
 * Bray-Curtis distance.
 * @author Diego Catalano
 */
public class BrayCurtisDistance implements IDistance<double[]>{

    /**
     * Initializes a new instance of the BravCurtisDistance class.
     */
    public BrayCurtisDistance() {}

    @Override
    public double Compute(double[] u, double[] v) {
        return Distance.BrayCurtis(u, v);
    }
}