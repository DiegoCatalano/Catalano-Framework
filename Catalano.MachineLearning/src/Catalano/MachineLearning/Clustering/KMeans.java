// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright 2015 Diego Catalano
// diego.catalano at live.com
//
// Copyright 2015 Haifeng Li
// haifeng.hli at gmail.com
//
// Based on Smile (Statistical Machine Intelligence & Learning Engine)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package Catalano.MachineLearning.Clustering;

import Catalano.Core.Concurrent.MulticoreExecutor;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Distances.Distance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * K-Means learn aims to partition n observations into k clusters in which
 * each observation belongs to the cluster with the nearest mean.
 * Although finding an exact solution to the k-means problem for arbitrary
 * input is NP-hard, the standard approach to finding an approximate solution
 * (often called Lloyd's algorithm or the k-means algorithm) is used widely
 * and frequently finds reasonable solutions quickly.
 * <p>
 * However, the k-means algorithm has at least two major theoretic shortcomings:
 * <ul>
 * <li> First, it has been shown that the worst case running time of the
 * algorithm is super-polynomial in the input size.
 * <li> Second, the approximation found can be arbitrarily bad with respect
 * to the objective function compared to the optimal learn.
 * </ul>
 * In this implementation, we use k-means++ which addresses the second of these
 * obstacles by specifying a procedure to initialize the cluster centers before
 * proceeding with the standard k-means optimization iterations. With the
 * k-means++ initialization, the algorithm is guaranteed to find a solution
 * that is O(log k) competitive to the optimal k-means solution.
 * <p>
 * We also use k-d trees to speed up each k-means step as described in the filter
 * algorithm by Kanungo, et al.
 * <p>
 * K-means is a hard clustering method, i.e. each sample is assigned to
 * a specific cluster. In contrast, soft clustering, e.g. the
 * Expectation-Maximization algorithm for Gaussian mixtures, assign samples
 * to different clusters with different probabilities.
 *
 * <h2>References</h2>
 * <ol>
 * <li> Tapas Kanungo, David M. Mount, Nathan S. Netanyahu, Christine D. Piatko, Ruth Silverman, and Angela Y. Wu. An Efficient k-Means Clustering Algorithm: Analysis and Implementation. IEEE TRANS. PAMI, 2002.</li>
 * <li> D. Arthur and S. Vassilvitskii. "K-means++: the advantages of careful seeding". ACM-SIAM symposium on Discrete algorithms, 1027-1035, 2007.</li>
 * <li> Anna D. Peterson, Arka P. Ghosh and Ranjan Maitra. A systematic evaluation of different methods for initializing the K-means clustering algorithm. 2010.</li>
 * </ol>
 * 
 * @see BBDTree
 * 
 * @author Haifeng Li
 */
public class KMeans extends PartitionClustering<double[]> implements ICentroidClustering {
    
    /**
     * The total distortion.
     */
    double distortion;
    /**
     * The centroids of each cluster.
     */
    double[][] centroids;
    
    private int maxIteration;
    
    private int maxRuns = 0;

    /**
     * Returns the distortion.
     */
    public double distortion() {
        return distortion;
    }

    /**
     * Returns the centroids.
     */
    @Override
    public double[][] getCentroids() {
        return centroids;
    }

    public int getClusters() {
        return k;
    }

    public void setCluster(int k) {
        this.k = k;
    }

    /**
     * Initializes a new instance of the KMeans class.
     */
    public KMeans() {
        this(3);
    }
    
    /**
     * Initializes a new instance of the KMeans class.
     * @param k Number of the clusters.
     */
    public KMeans(int k){
        this(k, 100);
    }
    
    /**
     * Initializes a new instance of the KMeans class.
     * @param k Number of the clusters.
     * @param maxIteration Maximum iteration.
     */
    public KMeans(int k, int maxIteration){
        this.k = k;
        this.maxIteration = maxIteration;
    }
    
    /**
     * Initializes a new instance of the KMeans class.
     * @param k Number of the clusters.
     * @param maxIteration Maximum iteration.
     * @param maxRuns Perform KMeans n times.
     */
    public KMeans(int k, int maxIteration, int maxRuns){
        this.k = k;
        this.maxIteration = maxIteration;
        this.maxRuns = maxRuns;
    }

    @Override
    public void Compute(DatasetClassification dataset) {
        Compute(dataset.getInput());
    }

    @Override
    public void Compute(double[][] input) {
        if(maxRuns > 0)
            Perform(input, maxRuns);
        else
            Perform(input);
    }
    
    /**
     * Cluster a new instance.
     * @param x a new instance.
     * @return the cluster label, which is the index of nearest centroid.
     */
    @Override
    public int Predict(double[] x) {
        double minDist = Double.MAX_VALUE;
        int bestCluster = 0;

        for (int i = 0; i < k; i++) {
            double dist = Distance.SquaredEuclidean(x, centroids[i]);
            if (dist < minDist) {
                minDist = dist;
                bestCluster = i;
            }
        }

        return bestCluster;
    }
    
    private void Perform(double[][] input){
        
        BBDTree bbd = new BBDTree(input);

        int n = input.length;
        int d = input[0].length;

        distortion = Double.MAX_VALUE;
        y = seed(input, k, DistanceMethod.EUCLIDEAN);
        size = new int[k];
        centroids = new double[k][d];

        for (int i = 0; i < n; i++) {
            size[y[i]]++;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                centroids[y[i]][j] += input[i][j];
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < d; j++) {
                centroids[i][j] /= size[i];
            }
        }

        double[][] sums = new double[k][d];
        for (int iter = 1; iter <= maxIteration; iter++) {
            double dist = bbd.clustering(centroids, sums, size, y);
            for (int i = 0; i < k; i++) {
                if (size[i] > 0) {
                    for (int j = 0; j < d; j++) {
                        centroids[i][j] = sums[i][j] / size[i];
                    }
                }
            }

            if (distortion <= dist) {
                break;
            } else {
                distortion = dist;
            }
        }
    }
    
    /**
     * Clustering data into k clusters. Run the algorithm for given times
     * and return the best one with smallest distortion.
     * @param data the input data of which each row is a sample.
     * @param k the number of clusters.
     * @param maxIteration the maximum number of iterations for each running.
     * @param runs the number of runs of K-Means algorithm.
     */
    private void Perform(double[][] input, int runs) {

        BBDTree bbd = new BBDTree(input);

        List<KMeansThread> tasks = new ArrayList<KMeansThread>();
        for (int i = 0; i < runs; i++) {
            tasks.add(new KMeansThread(bbd, input, k, maxIteration));
        }

        KMeans best = new KMeans();
        best.distortion = Double.MAX_VALUE;

        try {
            List<KMeans> clusters = MulticoreExecutor.run(tasks);
            for (KMeans kmeans : clusters) {
                if (kmeans.distortion < best.distortion) {
                    best = kmeans;
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);

            for (int i = 0; i < runs; i++) {
                KMeans kmeans = lloyd(input, k, maxIteration);
                if (kmeans.distortion < best.distortion) {
                    best = kmeans;
                }
            }            
        }

        this.distortion = best.distortion;
        this.centroids = best.centroids;
        this.y = best.y;
        this.size = best.size;
    }

    /**
     * Adapter for running BBD-Tree based K-Means algorithm in thread pool.
     */
    static class KMeansThread implements Callable<KMeans> {

        final BBDTree bbd;
        final double[][] data;
        final int k;
        final int maxIteration;

        KMeansThread(BBDTree bbd, double[][] data, int k, int maxIteration) {
            this.bbd = bbd;
            this.data = data;
            this.k = k;
            this.maxIteration = maxIteration;
        }

        @Override
        public KMeans call() {
            KMeans km = new KMeans(k, maxIteration);
            km.Compute(data);
            return km;//new KMeans(data, k, maxIteration);
        }
    }

    /**
     * The implementation of Lloyd algorithm as a benchmark. The data may
     * contain missing values (i.e. Double.NaN). The algorithm runs up to
     * 100 iterations.
     * @param input the input data of which each row is a sample.
     * @param k the number of clusters.
     */
    public static KMeans lloyd(double[][] input, int k) {
        return lloyd(input, k, 100);
    }

    /**
     * The implementation of Lloyd algorithm as a benchmark. The data may
     * contain missing values (i.e. Double.NaN).
     * @param data the input data of which each row is a sample.
     * @param k the number of clusters.
     * @param maxIteration the maximum number of iterations for each running.
     */
    public static KMeans lloyd(double[][] input, int k, int maxIteration) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIteration <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIteration);
        }

        int n = input.length;
        int d = input[0].length;
        int[][] nd = new int[k][d]; // The number of non-missing values per cluster per variable.

        double distortion = Double.MAX_VALUE;
        int[] size = new int[k];
        double[][] centroids = new double[k][d];
        int[] y = seed(input, k, DistanceMethod.EUCLIDEAN_MISSING_VALUES);

        int np = MulticoreExecutor.getThreadPoolSize();
        List<LloydThread> tasks = null;
        if (n >= 1000 && np >= 2) {
            tasks = new ArrayList<LloydThread>(np + 1);
            int step = n / np;
            if (step < 100) {
                step = 100;
            }

            int start = 0;
            int end = step;
            for (int i = 0; i < np-1; i++) {
                tasks.add(new LloydThread(input, centroids, y, start, end));
                start += step;
                end += step;
            }
            tasks.add(new LloydThread(input, centroids, y, start, n));
        }

        for (int iter = 0; iter < maxIteration; iter++) {
            Arrays.fill(size, 0);
            for (int i = 0; i < k; i++) {
                Arrays.fill(centroids[i], 0);
                Arrays.fill(nd[i], 0);
            }

            for (int i = 0; i < n; i++) {
                int m = y[i];
                size[m]++;
                for (int j = 0; j < d; j++) {
                    if (!Double.isNaN(input[i][j])) {
                        centroids[m][j] += input[i][j];
                        nd[m][j]++;
                    }
                }
            }

            for (int i = 0; i < k; i++) {
                for (int j = 0; j < d; j++) {
                    centroids[i][j] /= nd[i][j];
                }
            }

            double wcss = Double.NaN;
            if (tasks != null) {
                try {
                    wcss = 0.0;
                    for (double ss : MulticoreExecutor.run(tasks)) {
                        wcss += ss;
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                    wcss = Double.NaN;
                }
            }

            if (Double.isNaN(wcss)) {
                wcss = 0.0;
                for (int i = 0; i < n; i++) {
                    double nearest = Double.MAX_VALUE;
                    for (int j = 0; j < k; j++) {
                        double dist = squaredDistance(input[i], centroids[j]);
                        if (nearest > dist) {
                            y[i] = j;
                            nearest = dist;
                        }
                    }
                    wcss += nearest;
                }              
            }
            
            if (distortion <= wcss) {
                break;
            } else {
                distortion = wcss;
            }
        }

        // In case of early stop, we should recalculate centroids and clusterSize.
        Arrays.fill(size, 0);
        for (int i = 0; i < k; i++) {
            Arrays.fill(centroids[i], 0);
            Arrays.fill(nd[i], 0);
        }

        for (int i = 0; i < n; i++) {
            int m = y[i];
            size[m]++;
            for (int j = 0; j < d; j++) {
                if (!Double.isNaN(input[i][j])) {
                    centroids[m][j] += input[i][j];
                    nd[m][j]++;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < d; j++) {
                centroids[i][j] /= nd[i][j];
            }
        }

        KMeans kmeans = new KMeans();
        kmeans.k = k;
        kmeans.distortion = distortion;
        kmeans.size = size;
        kmeans.centroids = centroids;
        kmeans.y = y;

        return kmeans;
    }

    /**
     * The implementation of Lloyd algorithm as a benchmark. Run the algorithm
     * multiple times and return the best one in terms of smallest distortion.
     * The data may contain missing values (i.e. Double.NaN).
     * @param data the input data of which each row is a sample.
     * @param k the number of clusters.
     * @param maxIteration the maximum number of iterations for each running.
     * @param runs the number of runs of K-Means algorithm.
     */
    public static KMeans lloyd(double[][] input, int k, int maxIteration, int runs) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIteration <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIteration);
        }

        if (runs <= 0) {
            throw new IllegalArgumentException("Invalid number of runs: " + runs);
        }

        KMeans best = lloyd(input, k, maxIteration);

        for (int i = 1; i < runs; i++) {
            KMeans kmeans = lloyd(input, k, maxIteration);
            if (kmeans.distortion < best.distortion) {
                best = kmeans;
            }
        }

        return best;
    }

    /**
     * Adapter for running Lloyd algorithm in thread pool.
     */
    static class LloydThread implements Callable<Double> {

        /**
         * The start index of data portion for this task.
         */
        final int start;
        /**
         * The end index of data portion for this task.
         */
        final int end;
        final double[][] input;
        final int k;
        final double[][] centroids;
        int[] y;

        LloydThread(double[][] input, double[][] centroids, int[] y, int start, int end) {
            this.input = input;
            this.k = centroids.length;
            this.y = y;
            this.centroids = centroids;
            this.start = start;
            this.end = end;
        }

        @Override
        public Double call() {
            double wcss = 0.0;
            for (int i = start; i < end; i++) {
                double nearest = Double.MAX_VALUE;
                for (int j = 0; j < k; j++) {
                    double dist = squaredDistance(input[i], centroids[j]);
                    if (nearest > dist) {
                        y[i] = j;
                        nearest = dist;
                    }
                }
                wcss += nearest;
            }
            
            return wcss;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("K-Means distortion: %.5f\n", distortion));
        sb.append(String.format("Clusters of %d data points of dimension %d:\n", y.length, centroids[0].length));
        for (int i = 0; i < k; i++) {
            int r = (int) Math.round(1000.0 * size[i] / y.length);
            sb.append(String.format("%3d\t%5d (%2d.%1d%%)\n", i, size[i], r / 10, r % 10));
        }
        
        return sb.toString();
    }
}