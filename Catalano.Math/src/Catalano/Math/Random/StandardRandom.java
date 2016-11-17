/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Random;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Diego
 */
public class StandardRandom implements IRandomNumberGenerator{
    
    private java.util.Random r;
    private long seed;

    public StandardRandom() {
        this(System.nanoTime());
    }

    public StandardRandom(long seed) {
        setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
        r = new java.util.Random(seed);
    }

    @Override
    public int nextBits(int numbits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int nextInt() {
        return r.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return r.nextInt(n);
    }

    @Override
    public long nextLong() {
        return r.nextLong();
    }

    @Override
    public double nextDouble() {
        return r.nextDouble();
    }

    @Override
    public void nextDoubles(double[] d) {
        int n = d.length;
        for (int i = 0; i < n; i++) {
            d[i] = nextDouble();
        }
    }
}