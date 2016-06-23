// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Philippe Thevenaz, 2011
// philippe.thevenaz at epfl.ch
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
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.FastGraphics;
import Catalano.Imaging.Shapes.IntPolygon;
import Catalano.Math.Constants;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 *
 * @author Diego Catalano
 */
public class OvusculeSnake2DKeeper {
    
    private Double energy = null;
    private IOvusculeSnake2D snake = null;
    private boolean optimizing = false;

    private static final double COMPLEMENTARY_GOLDEN_RATIO = Constants.ComplementaryGoldenRatio;//1.5 - sqrt(1.25);
    private static final double GOLDEN_RATIO = Constants.GoldenRatio; //0.5 + sqrt(1.25);
    private static final double MAXIMAL_PARABOLIC_EXCURSION = 100.0;
    private static final double SQRT_TINY = sqrt((double)Float.intBitsToFloat((int)0x33FFFFFF));
    private static final double TINY = (double)Float.intBitsToFloat((int)0x33FFFFFF);

    /**
     * Optimize snake using ovuscule approach.
     * @param snake Snake.
     */
    public void Optimize(IOvusculeSnake2D snake){
        this.snake = snake;
        energy = null;
        optimizing = true;

        OvusculeSnake2DNode[] youngSnake = snake.getNodes();
        int K = youngSnake.length;
        OvusculeSnake2DNode[] X = new OvusculeSnake2DNode[K];
        for (int k = 0; (k < K); k++) {
                X[k] = new OvusculeSnake2DNode(youngSnake[k].x, youngSnake[k].y,
                        youngSnake[k].frozen, youngSnake[k].hidden);
        }
        Optimize(X);
    }

    /**
     * Optimize nodes.
     * @param X Nodes.
     */
    private void Optimize (OvusculeSnake2DNode[] X) {
        int K = X.length;
        DoublePoint[] V = new DoublePoint[K];
        for (int k = 0; (k < K); k++) {
                V[k] = new DoublePoint(0.0, 0.0);
        }
        DoublePoint[] G0 = g(X);
        if (G0 == null) {
                return;
        }
        double totalDisplacement;
        do {
                double g0 = 0.0;
                for (int k = 0; (k < K); k++) {
                        V[k].x = -G0[k].x;
                        V[k].y = -G0[k].y;
                        g0 += G0[k].x * G0[k].x + G0[k].y * G0[k].y;
                }
                if (g0 <= SQRT_TINY) {
                        snake.setNodes(X);
                        break;
                }
                totalDisplacement = 0.0;
                for (int n = 0, N = 2 * K; (n <= N); n++) {
                        double dx = LineMinimization(X, V);
                        if (dx < 0.0) {
                                snake.setNodes(X);
                                return;
                        }
                        totalDisplacement += dx;
                        DoublePoint[] G1 = g(X);
                        if (G1 == null) {
                                snake.setNodes(X);
                                return;
                        }
                        double g1 = 0.0;
                        double b = 0.0;
                        for (int k = 0; (k < K); k++) {
                                b += G1[k].x * (G1[k].x - G0[k].x)
                                        + G1[k].y * (G1[k].y - G0[k].y);
                                g1 += G1[k].x * G1[k].x + G1[k].y * G1[k].y;
                        }
                        if (g1 <= SQRT_TINY) {
                                snake.setNodes(X);
                                return;
                        }
                        else {
                                b /= g0;
                                double v = 0.0;
                                for (int k = 0; (k < K); k++) {
                                        V[k].x = b * V[k].x - G1[k].x;
                                        V[k].y = b * V[k].y - G1[k].y;
                                        v += V[k].x * V[k].x + V[k].y * V[k].y;
                                }
                                if (v <= SQRT_TINY) {
                                        snake.setNodes(X);
                                        return;
                                }
                                g0 = g1;
                                G0 = G1;
                        }
                }
        } while (SQRT_TINY < totalDisplacement);
        snake.setNodes(X);
    }
    
    private Double f (OvusculeSnake2DNode[] X, double u, DoublePoint[] V) {
        int K = X.length;
        OvusculeSnake2DNode[] Y = new OvusculeSnake2DNode[K];
        for (int k = 0; (k < K); k++) {
                Y[k] = new OvusculeSnake2DNode(X[k].x, X[k].y);
                Y[k].x += u * V[k].x;
                Y[k].y += u * V[k].y;
        }
        snake.setNodes(Y);
        if (!optimizing) {
                return(null);
        }
        return(new Double(snake.energy()));
    }

    private DoublePoint[] g (OvusculeSnake2DNode[] X) {
        int K = X.length;
        snake.setNodes(X);
        if (!optimizing) {
                return(null);
        }
        DoublePoint[] G = snake.getEnergyGradient();
        if (null != G) {
                DoublePoint[] G0 = new DoublePoint[K];
                for (int k = 0; (k < K); k++) {
                        if (X[k].frozen) {
                                G0[k] = new DoublePoint(0.0, 0.0);
                        }
                        else {
                                G0[k] = new DoublePoint(G[k].x, G[k].y);
                        }
                }
                G = G0;
        }
        else {
                OvusculeSnake2DNode[] Y = new OvusculeSnake2DNode[K];
                G = new DoublePoint[K];
                for (int k = 0; (k < K); k++) {
                        Y[k] = new OvusculeSnake2DNode(X[k].x, X[k].y);
                        G[k] = new DoublePoint(0.0, 0.0);
                }
                for (int k = 0; (k < K); k++) {
                        if (!X[k].frozen) {
                                Y[k].x = X[k].x - SQRT_TINY;
                                snake.setNodes(Y);
                                if (!optimizing) {
                                        return(null);
                                }
                                double f0 = snake.energy();
                                Y[k].x = X[k].x + SQRT_TINY;
                                snake.setNodes(Y);
                                if (!optimizing) {
                                        return(null);
                                }
                                double f1 = snake.energy();
                                G[k].x = 0.5 * (f1 - f0) / SQRT_TINY;
                                Y[k].x = X[k].x;
                                Y[k].y = X[k].y - SQRT_TINY;
                                snake.setNodes(Y);
                                if (!optimizing) {
                                        return(null);
                                }
                                f0 = snake.energy();
                                Y[k].y = X[k].y + SQRT_TINY;
                                snake.setNodes(Y);
                                if (!optimizing) {
                                        return(null);
                                }
                                f1 = snake.energy();
                                G[k].y = 0.5 * (f1 - f0) / SQRT_TINY;
                                Y[k].y = X[k].y;
                        }
                }
                snake.setNodes(X);
        }
        return(G);
    }

    private double LineMinimization (OvusculeSnake2DNode[] X, DoublePoint[] V) {
        int K = X.length;
        double a = 0.0;
        Double Fa = f(X, a, V);
        if (Fa == null) {
                return(-1.0);
        }
        if (energy == null) {
                energy = Fa;
        }
        else {
                energy = (energy.compareTo(Fa) < 0) ? (energy) : (Fa);
        }
        double fa = Fa.doubleValue();
        if (!optimizing) {
                return(-1.0);
        }
        OvusculeSnake2DScale[] Pa = snake.getScales();
        double b = SQRT_TINY;
        Double Fb = f(X, b, V);
        if (Fb == null) {
                return(-1.0);
        }
        energy = (energy.compareTo(Fb) < 0) ? (energy) : (Fb);
        double fb = Fb.doubleValue();
        if (!optimizing) {
                if (fb < fa) {
                        for (int k = 0; (k < K); k++) {
                                X[k].x += b * V[k].x;
                                X[k].y += b * V[k].y;
                        }
                }
                return(-1.0);
        }
        OvusculeSnake2DScale[] Pb = snake.getScales();
        if (fa < fb) {
                double z = a;
                a = b;
                b = z;
                double f = fa;
                fa = fb;
                fb = f;
        }
        double c = b + GOLDEN_RATIO * (b - a);
        Double Fc = f(X, c, V);
        if (Fc == null) {
                for (int k = 0; (k < K); k++) {
                        X[k].x += b * V[k].x;
                        X[k].y += b * V[k].y;
                }
                return(-1.0);
        }
        energy = (energy.compareTo(Fc) < 0) ? (energy) : (Fc);
        double fc = Fc.doubleValue();
        if (fc < fb) {
                if (!optimizing) {
                        for (int k = 0; (k < K); k++) {
                                X[k].x += c * V[k].x;
                                X[k].y += c * V[k].y;
                        }
                        return(-1.0);
                }
        }
        double u = c;
        double fu = fc;
        while (fc <= fb) {
                double r = (b - a) * (fb - fc);
                double q = (b - c) * (fb - fa);
                u = 0.5 * (b - (b - c) * q + (b - a) * r);
                u = (TINY < abs(q - r))
                        ? (u / (q - r)) : ((r < q) ? (u / TINY) : (-u / TINY));
                double ulim = b + MAXIMAL_PARABOLIC_EXCURSION * (c - b);
                if (0.0 < ((b - u) * (u - c))) {
                        Double Fu = f(X, u, V);
                        if (Fu == null) {
                                for (int k = 0; (k < K); k++) {
                                        X[k].x += c * V[k].x;
                                        X[k].y += c * V[k].y;
                                }
                                return(-1.0);
                        }
                        energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
                        fu = Fu.doubleValue();
                        if (fu < fc) {
                                if (!optimizing) {
                                        for (int k = 0; (k < K); k++) {
                                                X[k].x += u * V[k].x;
                                                X[k].y += u * V[k].y;
                                        }
                                        return(-1.0);
                                }
                                a = b;
                                fa = fb;
                                b = u;
                                fb = fu;
                                break;
                        }
                        else {
                                if (fb < fu) {
                                        c = u;
                                        fc = fu;
                                        break;
                                }
                        }
                        u = c + GOLDEN_RATIO * (c - b);
                        Fu = f(X, u, V);
                        if (Fu == null) {
                                for (int k = 0; (k < K); k++) {
                                        X[k].x += c * V[k].x;
                                        X[k].y += c * V[k].y;
                                }
                                return(-1.0);
                        }
                        energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
                        fu = Fu.doubleValue();
                        if (fu < fc) {
                                if (!optimizing) {
                                        for (int k = 0; (k < K); k++) {
                                                X[k].x += u * V[k].x;
                                                X[k].y += u * V[k].y;
                                        }
                                        return(-1.0);
                                }
                        }
                }
                else {
                        if (0.0 < ((c - u) * (u - ulim))) {
                                Double Fu = f(X, u, V);
                                if (Fu == null) {
                                        for (int k = 0; (k < K); k++) {
                                                X[k].x += c * V[k].x;
                                                X[k].y += c * V[k].y;
                                        }
                                        return(-1.0);
                                }
                                energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
                                fu = Fu.doubleValue();
                                if (fu < fc) {
                                        if (!optimizing) {
                                                for (int k = 0; (k < K); k++) {
                                                        X[k].x += u * V[k].x;
                                                        X[k].y += u * V[k].y;
                                                }
                                                return(-1.0);
                                        }
                                        b = c;
                                        c = u;
                                        u = c + GOLDEN_RATIO * (c - b);
                                        fb = fc;
                                        fc = fu;
                                        Fu = f(X, u, V);
                                        if (Fu == null) {
                                                for (int k = 0; (k < K); k++) {
                                                        X[k].x += c * V[k].x;
                                                        X[k].y += c * V[k].y;
                                                }
                                                return(-1.0);
                                        }
                                        energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
                                        fu = Fu.doubleValue();
                                        if (fu < fc) {
                                                if (!optimizing) {
                                                        for (int k = 0; (k < K); k++) {
                                                                X[k].x += u * V[k].x;
                                                                X[k].y += u * V[k].y;
                                                        }
                                                        return(-1.0);
                                                }
                                        }
                                }
                        }
                        else {
                                if (0.0 <= ((u - ulim) * (ulim - c))) {
                                        u = ulim;
                                        Double Fu = f(X, u, V);
                                        if (Fu == null) {
                                                for (int k = 0; (k < K); k++) {
                                                        X[k].x += c * V[k].x;
                                                        X[k].y += c * V[k].y;
                                                }
                                                return(-1.0);
                                        }
                                        energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
                                        fu = Fu.doubleValue();
                                }
                                else {
                                        u = c + GOLDEN_RATIO * (c - b);
                                        Double Fu = f(X, u, V);
                                        if (Fu == null) {
                                                for (int k = 0; (k < K); k++) {
                                                        X[k].x += c * V[k].x;
                                                        X[k].y += c * V[k].y;
                                                }
                                                return(-1.0);
                                        }
                                        energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
                                        fu = Fu.doubleValue();
                                }
                                if (fu < fc) {
                                        if (!optimizing) {
                                                for (int k = 0; (k < K); k++) {
                                                        X[k].x += u * V[k].x;
                                                        X[k].y += u * V[k].y;
                                                }
                                                return(-1.0);
                                        }
                                }
                        }
                }
                a = b;
                b = c;
                c = u;
                fa = fb;
                fb = fc;
                fc = fu;
        }
        double d = 0.0;
        double e = 0.0;
        double x = b;
        double v = b;
        double w = b;
        double fx = fb;
        double fv = fb;
        double fw = fb;
        if (c < a) {
                b = a;
                a = c;
                fb = fa;
                fa = fc;
        }
        else {
                b = c;
                fb = fc;
        }
        while (true) {
            double xm = 0.5 * (a + b);
            double tol1 = SQRT_TINY * abs(x) + TINY;
            double tol2 = 2.0 * tol1;
            if (abs(x - xm) <= (tol2 - 0.5 * (b - a))) {
                    double dx = 0.0;
                    for (int k = 0; (k < K); k++) {
                            X[k].x += x * V[k].x;
                            X[k].y += x * V[k].y;
                            dx += V[k].x * V[k].x + V[k].y * V[k].y;
                    }
                    return(abs(x) * sqrt(dx));
            }
            if (tol1 < abs(e)) {
                    double r = (x - w) * (fx - fv);
                    double q = (x - v) * (fx - fw);
                    double p = (x - v) * q - (x - w) * r;
                    q = 2.0 * (q - r);
                    if (0.0 < q) {
                            p = -p;
                    }
                    q = abs(q);
                    double etemp = e;
                    e = d;
                    if ((abs(0.5 * q * etemp) <= abs(p))
                            || (p <= (q * (a - x))) || ((q * (b - x)) <= p)) {
                            e = (xm <= x) ? (a - x) : (b - x);
                            d = COMPLEMENTARY_GOLDEN_RATIO * e;
                    }
                    else {
                            d = p / q;
                            u = x + d;
                            if (((u - a) < tol2) || ((b - u) < tol2)) {
                                    d = (x <= xm) ? (tol1) : (-tol1);
                            }
                    }
            }
            else {
                    e = (xm <= x) ? (a - x) : (b - x);
                    d = COMPLEMENTARY_GOLDEN_RATIO * e;
            }
            u = (tol1 <= abs(d))
                    ? (x + d) : (x + ((0.0 <= d) ? (tol1) : (-tol1)));
            Double Fu = f(X, u, V);
            if (Fu == null) {
                    for (int k = 0; (k < K); k++) {
                            X[k].x += x * V[k].x;
                            X[k].y += x * V[k].y;
                    }
                    return(-1.0);
            }
            energy = (energy.compareTo(Fu) < 0) ? (energy) : (Fu);
            fu = Fu.doubleValue();
            if (fu <= fx) {
                    if (!optimizing) {
                            for (int k = 0; (k < K); k++) {
                                    X[k].x += u * V[k].x;
                                    X[k].y += u * V[k].y;
                            }
                            return(-1.0);
                    }
                    if (x <= u) {
                            a = x;
                    }
                    else {
                            b = x;
                    }
                    v = w;
                    fv = fw;
                    w = x;
                    fw = fx;
                    x = u;
                    fx = fu;
            }
            else {
                if (u < x) {
                        a = u;
                }
                else {
                        b = u;
                }
                if ((fu <= fw) || (w == x)) {
                        v = w;
                        fv = fw;
                        w = u;
                        fw = fu;
                }
                else {
                    if ((fu <= fv) || (v == x) || (v == w)) {
                            v = u;
                            fv = fu;
                    }
                }
            }
        }
    }
    
    private void init(IOvusculeSnake2D snake){
        this.snake = snake;
        energy = null;
        optimizing = true;

        OvusculeSnake2DNode[] youngSnake = snake.getNodes();
        int K = youngSnake.length;
        OvusculeSnake2DNode[] X = new OvusculeSnake2DNode[K];
        for (int k = 0; (k < K); k++) {
                X[k] = new OvusculeSnake2DNode(youngSnake[k].x, youngSnake[k].y,
                        youngSnake[k].frozen, youngSnake[k].hidden);
        }
    }
    
    /**
     * Draw Ovuscule.
     * @param fastBitmap Image in RGB.
     * @param snake Ovuscule snake.
     * @param r Red channel.
     * @param g Green channel.
     * @param b Blue channel.
     */
    public void DrawOvuscule(FastBitmap fastBitmap, IOvusculeSnake2D snake, int r, int g, int b){
        
        init(snake);
        
        OvusculeSnake2DScale[] skin = snake.getScales();
        
        FastGraphics fg = new FastGraphics(fastBitmap);
        fg.setColor(255, 0, 0);
        
        for (int k = 0, K = skin.length; (k < K); k++) {
            final int[] xpoints = skin[k].xpoints;
            final int[] ypoints = skin[k].ypoints;
            final IntPolygon poly = new IntPolygon();
            final int N = skin[k].npoints;
            for (int n = 0; (n < N); n++) {
                poly.addPoint(ypoints[n], xpoints[n]);
            }
            
            fg.DrawPolygon(poly);
            
        }
    }
}