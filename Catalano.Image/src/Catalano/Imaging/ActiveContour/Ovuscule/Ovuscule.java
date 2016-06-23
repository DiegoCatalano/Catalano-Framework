// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Philippe Thevenaz, 2011
// philippe.thevenaz at epfl.ch
// http://www.ricarddelgado.com/papers/ovuscule2011.pdf
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

package Catalano.Imaging.ActiveContour.Ovuscule;

import Catalano.Core.DoublePoint;
import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Math.Constants;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * References: http://www.ricarddelgado.com/papers/ovuscule2011.pdf
 * @author Diego Catalano
 */
public class Ovuscule implements IOvusculeSnake2D{
    
    private FastBitmap fastBitmap = null;
    private double area, RamanujanPerimeter;
    private double a11, a12, a22, a33, a3;
    private double c1, c2;
    private double p1, p2, pq1, pq2, pq;
    private double q1, q2, qr1, qr2, qr;
    private double r1, r2, rp1, rp2, rp;
    private double s1, s2;
    private double u1, u2;
    private double v1, v2;
    private double y1, y2;
    private OvusculeSnake2DNode[] node = new OvusculeSnake2DNode[3];
    private int height;
    private int width;
    
    private static final double AREA_FACTOR = 1.2091995761561452337293855;
    private static final double HALF_SQRT2 = Constants.Sqrt2 / 2.0;
    private static final double REGULARIZATION_WEIGHT = 100.0;
    private static final double SQRT_TINY = sqrt((double)Float.intBitsToFloat((int)0x33FFFFFF));
    private static final double SQRT2 = Constants.Sqrt2;
    private static final double SQRT3 = Constants.Sqrt3;
    private static final double THIRD_SQRT2 = Constants.Sqrt2 / 3.0;
    
    public Ovuscule(FastBitmap fastBitmap, DoublePoint p, DoublePoint q, DoublePoint r){
        this(fastBitmap, p.x, p.y, q.x, q.y, r.x, r.y);
    }
    
    public Ovuscule(FastBitmap fastBitmap, IntPoint p, IntPoint q, IntPoint r){
        this(fastBitmap, (double)p.x, (double)p.y, (double)q.x, (double)q.y, (double)r.x, (double)r.y);
    }
    
    public Ovuscule (FastBitmap fastBitmap, double p1, double p2, double q1, double q2, double r1, double r2) {
        this.fastBitmap = fastBitmap;
        height = fastBitmap.getHeight();
        width = fastBitmap.getWidth();
        node[0] = new OvusculeSnake2DNode(p2, p1);
        node[1] = new OvusculeSnake2DNode(q2, q1);
        node[2] = new OvusculeSnake2DNode(r2, r1);
        setNodes(node);
    } /* end Ovuscule */
    
/*....................................................................
	IOvusculeSnake2D methods
....................................................................*/
/*------------------------------------------------------------------*/
    @Override
    public double energy () {
    double weightedArea = contrast();
    double regularization = regularization();
    return(weightedArea + regularization);
} /* end energy */

/*------------------------------------------------------------------*/
    @Override
    public DoublePoint[] getEnergyGradient () {
    DoublePoint[] gc = contrastGradient(node[0], node[1]);
    DoublePoint[] gr = regularizationGradient(node[0], node[1]);
    return(plus(gc, gr));
} /* end getEnergyGradient */

/*------------------------------------------------------------------*/
    @Override
    public OvusculeSnake2DNode[] getNodes () {
    return(node);
} /* end getNodes */

/*------------------------------------------------------------------*/
    @Override
    public OvusculeSnake2DScale[] getScales () {
    int K = (int)ceil(1.0 + RamanujanPerimeter / 2.0);
    if (K > (2 * (width + height))) {
            return(null);
    }
    int[] s0x1 = new int[K];
    int[] s0x2 = new int[K];
    int[] s1x1 = new int[K];
    int[] s1x2 = new int[K];
    for (int k = 0; (k < K); k++) {
            double theta = 2.0 * PI * (double)k / (double)K;
            s0x1[k] = (int)round(y1 + c1 * cos(theta) + s1 * sin(theta));
            s0x2[k] = (int)round(y2 + c2 * cos(theta) + s2 * sin(theta));
            s1x1[k] = (int)round(y1
                    + HALF_SQRT2 * (c1 * cos(theta) + s1 * sin(theta)));
            s1x2[k] = (int)round(y2
                    + HALF_SQRT2 * (c2 * cos(theta) + s2 * sin(theta)));
    }
    OvusculeSnake2DScale[] skin = new OvusculeSnake2DScale[2];
    skin[0] = new OvusculeSnake2DScale(null, null, true, false);
    skin[1] = new OvusculeSnake2DScale(null, null, true, false);
    skin[0].npoints = K;
    skin[0].xpoints = s0x1;
    skin[0].ypoints = s0x2;
    skin[1].npoints = K;
    skin[1].xpoints = s1x1;
    skin[1].ypoints = s1x2;
    return(skin);
} /* end getScales */

/*------------------------------------------------------------------*/
    @Override
    public void setNodes (OvusculeSnake2DNode[] node) {
    p1 = node[0].x;
    p2 = node[0].y;
    q1 = node[1].x;
    q2 = node[1].y;
    r1 = node[2].x;
    r2 = node[2].y;
    this.node[0].x = p1;
    this.node[0].y = p2;
    this.node[1].x = q1;
    this.node[1].y = q2;
    this.node[2].x = r1;
    this.node[2].y = r2;
    pq1 = p1 - q1;
    pq2 = p2 - q2;
    qr1 = q1 - r1;
    qr2 = q2 - r2;
    rp1 = r1 - p1;
    rp2 = r2 - p2;
    pq = p1 * q2 - p2 * q1;
    qr = q1 * r2 - q2 * r1;
    rp = r1 * p2 - r2 * p1;
    y1 = (p1 + q1 + r1) / 3.0;
    y2 = (p2 + q2 + r2) / 3.0;
    a11 = 3.0 * (p2 * pq2 + q2 * qr2 + r2 * rp2);
    a12 = 3.0 * (p1 * (q2 - 2.0 * p2) + p2 * q1
            + q1 * (r2 - 2.0 * q2) + q2 * r1 + r1 * (p2 - 2.0 * r2) + r2 * p1);
    a22 = 3.0 * (p1 * pq1 + q1 * qr1 + r1 * rp1);
    a33 = pq + qr + rp;
    a3 = abs(a33);
    c1 = (pq1 - rp1) / 3.0;
    c2 = (pq2 - rp2) / 3.0;
    s1 = qr1 / SQRT3;
    s2 = qr2 / SQRT3;
    double halfWidth = sqrt(abs(c1 * c1 + s1 * s1));
    double halfHeight = sqrt(abs(c2 * c2 + s2 * s2));
    u1 = y1 - halfWidth;
    u2 = y2 - halfHeight;
    v1 = y1 + halfWidth;
    v2 = y2 + halfHeight;
    double a = (a11 + a22) / 3.0;
    double b = sqrt(abs(a * a - 3.0 * a33 * a33));
    double semiMinor = THIRD_SQRT2 * sqrt(abs(a - b));
    double semiMajor = THIRD_SQRT2 * sqrt(abs(a + b));
    area = AREA_FACTOR * a3;
    double excentricity = (semiMajor - semiMinor) / (semiMajor + semiMinor);
    excentricity *= excentricity;
    RamanujanPerimeter = PI * (semiMajor + semiMinor) * (1.0
            + 3.0 * excentricity / (10.0 + sqrt(abs(4.0 - 3.0 * excentricity))));
} /* end setNodes */

/*....................................................................
	private methods
....................................................................*/
/*------------------------------------------------------------------*/
private double contrast () {
    if (area < 1.0) {
            return(1.0 / SQRT_TINY);
    }
    double c = 0.0;
    int xmin = max((int)floor(u1), 0);
    int xmax = min((int)ceil(v1), width - 1);
    int ymin = max((int)floor(u2), 0);
    int ymax = min((int)ceil(v2), height - 1);
    if ((u1 < xmin) || (xmax < v1) || (u2 < ymin) || (ymax < v2)){
            return(1.0 / SQRT_TINY);
    }
    if ((xmax <= xmin) || (ymax <= ymin)) {
            return(1.0 / SQRT_TINY);
    }
    for (int y = ymin; (y <= ymax); y++) {
            double dy = y2 - (double)y;
            double dy2 = dy * dy;
            for (int x = xmin; (x <= xmax); x++) {
                    double dx = y1 - (double)x;
                    double dx2 = dx * dx;
                    double d = sqrt(dx2 + dy2);
                    double z = a11 * dx2 + a12 * dx * dy + a22 * dy2;
                    if (z < SQRT_TINY) {
                            c -= fastBitmap.getGray(y, x);
                            continue;
                    }
                    z = a3 / sqrt(z);
                    double d0 = (1.0 - z / SQRT2) * d;
                    if (d0 < -HALF_SQRT2) {
                            c -= fastBitmap.getGray(y, x);
                            continue;
                    }
                    if (d0 < HALF_SQRT2) {
                            c += SQRT2 * d0 * fastBitmap.getGray(y, x);
                            continue;
                    }
                    d0 = (1.0 - z) * d;
                    if (d0 < -1.0) {
                            c += fastBitmap.getGray(y, x);
                            continue;
                    }
                    if (d0 < 1.0) {
                            c += (1.0 - d0) * fastBitmap.getGray(y, x) / 2.0;
                            continue;
                    }
            }
    }
    return(c / area);
} /* end contrast */

/*------------------------------------------------------------------*/
private DoublePoint[] contrastGradient (OvusculeSnake2DNode p, OvusculeSnake2DNode q) {
    if (area < 1.0) {
            return(null);
    }
    int xmin = max((int)floor(u1), 0);
    int xmax = min((int)ceil(v1), width - 1);
    int ymin = max((int)floor(u2), 0);
    int ymax = min((int)ceil(v2), height - 1);
    if ((u1 < xmin) || (xmax < v1) || (u2 < ymin) || (ymax < v2)) {
            return(null);
    }
    if ((xmax <= xmin) || (ymax <= ymin)) {
            return(null);
    }
    DoublePoint[] gradient = new DoublePoint[3];
    gradient[0] = new DoublePoint(0.0, 0.0);
    gradient[1] = new DoublePoint(0.0, 0.0);
    gradient[2] = new DoublePoint(0.0, 0.0);
    double[] z12 = {pq1 - qr1, pq2 - qr2, qr1 - rp1, qr2 - rp2, rp1 - pq1, rp2 - pq2};
    for (int y = ymin; (y <= ymax); y++) {
            double dy = y2 - (double)y;
            double dy2 = dy * dy;
            for (int x = xmin; (x <= xmax); x++) {
                    double dx = y1 - (double)x;
                    double dx2 = dx * dx;
                    double d = sqrt(dx2 + dy2);
                    if (d < SQRT_TINY) {
                            continue;
                    }
                    double f = fastBitmap.getGray(y, x);
                    double z = a11 * dx2 + a12 * dx * dy + a22 * dy2;
                    if (z < SQRT_TINY) {
                            gradient[0].x += f * qr2;
                            gradient[0].y -= f * qr1;
                            gradient[1].x += f * rp2;
                            gradient[1].y -= f * rp1;
                            gradient[2].x += f * pq2;
                            gradient[2].y -= f * pq1;
                            continue;
                    }
                    z = a3 / sqrt(z);
                    double d0 = (1.0 - z / SQRT2) * d;
                    if (d0 < -HALF_SQRT2) {
                            gradient[0].x += f * qr2;
                            gradient[0].y -= f * qr1;
                            gradient[1].x += f * rp2;
                            gradient[1].y -= f * rp1;
                            gradient[2].x += f * pq2;
                            gradient[2].y -= f * pq1;
                            continue;
                    }
                    if (d0 < HALF_SQRT2) {
                            double g = SQRT2 * d;
                            double g0 = z * z * z * d / (6.0 * a33);
                            double g1 = a33 * (SQRT2 - z) / (3.0 * g0 * d);
                            double gx = (2.0 * a11 + g1) * dx + a12 * dy;
                            double gy = (2.0 * a22 + g1) * dy + a12 * dx;
                            double h0 = 9.0 * (z12[5] * dx - z12[4] * dy);
                            double h1 = 9.0 * (z12[1] * dx - z12[0] * dy);
                            double h2 = 9.0 * (z12[3] * dx - z12[2] * dy);
                            gradient[0].x -= f * (g * qr2 - g0 * (dy * h0 + gx));
                            gradient[0].y += f * (g * qr1 - g0 * (dx * h0 - gy));
                            gradient[1].x -= f * (g * rp2 - g0 * (dy * h1 + gx));
                            gradient[1].y += f * (g * rp1 - g0 * (dx * h1 - gy));
                            gradient[2].x -= f * (g * pq2 - g0 * (dy * h2 + gx));
                            gradient[2].y += f * (g * pq1 - g0 * (dx * h2 - gy));
                            continue;
                    }
                    d0 = (1.0 - z) * d;
                    if (d0 < -1.0) {
                            gradient[0].x -= f * qr2;
                            gradient[0].y += f * qr1;
                            gradient[1].x -= f * rp2;
                            gradient[1].y += f * rp1;
                            gradient[2].x -= f * pq2;
                            gradient[2].y += f * pq1;
                            continue;
                    }
                    if (d0 < 1.0) {
                            double g = (d - 1.0) / 2.0;
                            double g0 = z * z * z * d / (12.0 * a33);
                            double g1 = a33 * (1.0 - z) / (6.0 * g0 * d);
                            double gx = (2.0 * a11 + g1) * dx + a12 * dy;
                            double gy = (2.0 * a22 + g1) * dy + a12 * dx;
                            double h0 = 9.0 * (z12[5] * dx - z12[4] * dy);
                            double h1 = 9.0 * (z12[1] * dx - z12[0] * dy);
                            double h2 = 9.0 * (z12[3] * dx - z12[2] * dy);
                            gradient[0].x += f * (g * qr2 - g0 * (dy * h0 + gx));
                            gradient[0].y -= f * (g * qr1 - g0 * (dx * h0 - gy));
                            gradient[1].x += f * (g * rp2 - g0 * (dy * h1 + gx));
                            gradient[1].y -= f * (g * rp1 - g0 * (dx * h1 - gy));
                            gradient[2].x += f * (g * pq2 - g0 * (dy * h2 + gx));
                            gradient[2].y -= f * (g * pq1 - g0 * (dx * h2 - gy));
                            continue;
                    }
            }
    }
    double A = a33 * area;
    gradient[0].x /= A;
    gradient[0].y /= A;
    gradient[1].x /= A;
    gradient[1].y /= A;
    gradient[2].x /= A;
    gradient[2].y /= A;
    return(gradient);
} /* end contrastGradient */

/*------------------------------------------------------------------*/
private DoublePoint[] plus (DoublePoint[] gc, DoublePoint[] gr) {
    if ((null == gc) || (null == gr)) {
            return(null);
    }
    int K = gc.length;
    if (K != gr.length) {
            return(null);
    }
    DoublePoint[] g = new DoublePoint[K];
    for (int k = 0; (k < K); k++) {
            g[k] = new DoublePoint(gc[k].x + gr[k].x, gc[k].y + gr[k].y);
    }
    return(g);
} /* end plus */

/*------------------------------------------------------------------*/
private double regularization () {
    double regularization = min(min(pq2 * pq2, qr2 * qr2), rp2 * rp2);
    return(REGULARIZATION_WEIGHT * regularization / area);
} /* end regularization */

/*------------------------------------------------------------------*/
private DoublePoint[] regularizationGradient (OvusculeSnake2DNode p, OvusculeSnake2DNode q) {
    DoublePoint[] gradient = new DoublePoint[3];
    gradient[0] = new DoublePoint(0.0, 0.0);
    gradient[1] = new DoublePoint(0.0, 0.0);
    gradient[2] = new DoublePoint(0.0, 0.0);
    double ppqq2 = pq2 * pq2;
    double qqrr2 = qr2 * qr2;
    double rrpp2 = rp2 * rp2;
    double A = 0.0;
    if ((ppqq2 <= qqrr2) && (ppqq2 <= rrpp2)) {
            gradient[0].x = -pq2 * qr2;
            gradient[0].y = 2.0 * a33 + pq2 * qr1;
            gradient[1].x = -rp2 * pq2;
            gradient[1].y = -2.0 * a33 + rp1 * pq2;
            gradient[2].x = -ppqq2;
            gradient[2].y = pq1 * pq2;
            A = REGULARIZATION_WEIGHT * pq2 / (a33 * area);
    }
    else if ((qqrr2 <= rrpp2) && (qqrr2 <= ppqq2)) {
            gradient[0].x = -qqrr2;
            gradient[0].y = qr1 * qr2;
            gradient[1].x = -qr2 * rp2;
            gradient[1].y = 2.0 * a33 + qr2 * rp1;
            gradient[2].x = -pq2 * qr2;
            gradient[2].y = -2.0 * a33 + pq1 * qr2;
            A = REGULARIZATION_WEIGHT * qr2 / (a33 * area);
    }
    else if ((rrpp2 <= ppqq2) && (rrpp2 <= qqrr2)) {
            gradient[0].x = -qr2 * rp2;
            gradient[0].y = -2.0 * a33 + qr1 * rp2;
            gradient[1].x = -rrpp2;
            gradient[1].y = rp1 * rp2;
            gradient[2].x = -rp2 * pq2;
            gradient[2].y = 2.0 * a33 + rp2 * pq1;
            A = REGULARIZATION_WEIGHT * rp2 / (a33 * area);
    }
    gradient[0].x *= A;
    gradient[0].y *= A;
    gradient[1].x *= A;
    gradient[1].y *= A;
    gradient[2].x *= A;
    gradient[2].y *= A;
    return(gradient);
    } /* end regularizationGradient */
}