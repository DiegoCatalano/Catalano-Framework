/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning;

import Catalano.Math.Matrix;
import Catalano.Statistics.Distributions.GeneralDiscreteDistribution;
import java.util.Arrays;
/**
 *
 * @author Diego
 */
public class KMeans {
    
    private KMeansClusterCollection clusters;

    public KMeansClusterCollection getClusters() {
        return clusters;
    }
    
    public int getK(){
        return clusters.length();
    }
    
    public int getDimension(){
        return clusters.getCentroids(0).length;
    }

    public KMeans(int k) {
        if (k <= 0) throw new IllegalArgumentException("k");
        this.clusters = new KMeansClusterCollection(k);
    }
    
    public void Randomize(double[][] points, boolean useSeeding){
        if (points == null) throw new IllegalArgumentException("points");

        double[][] centroids = clusters.getCentroids();

        if (useSeeding)
        {
            // Initialize using K-Means++
            // http://en.wikipedia.org/wiki/K-means%2B%2B

            // 1. Choose one center uniformly at random from among the data points.
            centroids[0] = (double[])points[Catalano.Math.Tools.Random().nextInt(points.length)].clone();

            for (int c = 1; c < centroids.length; c++)
            {
                // 2. For each data point x, compute D(x), the distance between
                //    x and the nearest center that has already been chosen.

                double sum = 0;
                double[] D = new double[points.length];
                for (int i = 0; i < D.length; i++)
                {
                    double[] x = points[i];

                    double min = Catalano.Math.Distance.SquaredEuclidean(x, centroids[0]);
                    for (int j = 1; j < c; j++)
                    {
                        double d = Catalano.Math.Distance.SquaredEuclidean(x, centroids[j]);
                        if (d < min) min = d;
                    }

                    D[i] = min;
                    sum += min;
                }

                for (int i = 0; i < D.length; i++)
                    D[i] /= sum;

                // 3. Choose one new data point at random as a new center, using a weighted
                //    probability distribution where a point x is chosen with probability 
                //    proportional to D(x)^2.
                centroids[c] = (double[])points[GeneralDiscreteDistribution.Random(D)].clone();
            }
        }
        else
        {
            // pick K unique random indexes in the range 0..n-1
            int[] idx = Catalano.Statistics.Tools.RandomSample(points.length, this.clusters.length());

            // assign centroids from data set
            
            double[][] temp = Matrix.Submatrix(points, idx);
            temp = Matrix.MemberwiseClone(temp);
            
            centroids = temp;
        }

        this.clusters.setCentroids(centroids);
    }
    
    public int[] Compute(double[][] points, double threshold)
    {
        return Compute(points, threshold, true);
    }
    
    public int[] Compute(double[][] data, double threshold, boolean computeInformation){
        
        threshold = 1e-5;
        computeInformation = true;
        // Initial argument checking
        if (data == null)
            throw new IllegalArgumentException("data");
        if (data.length < getK())
            throw new IllegalArgumentException("Not enough points. There should be more points than the number K of clusters.");
        if (threshold < 0)
            throw new IllegalArgumentException("Threshold should be a positive number.");

        // TODO: Implement a faster version using the triangle
        // inequality to reduce the number of distance calculations
        //
        //  - http://www-cse.ucsd.edu/~elkan/kmeansicml03.pdf
        //  - http://mloss.org/software/view/48/
        //

        int k = getK();
        int rows = data.length;
        int cols = data[0].length;


        // Perform a random initialization of the clusters
        // if the algorithm has not been initialized before.
        if (this.clusters.getCentroids(0) == null){
            Randomize(data, false);
        }


        // Initial variables
        int[] count = new int[k];
        int[] labels = new int[rows];
        double[][] centroids = clusters.getCentroids();
        double[][] newCentroids = new double[k][];
        for (int i = 0; i < newCentroids.length; i++)
            newCentroids[i] = new double[cols];

        double[][][] covariances = clusters.getCovariance();
        double[] proportions = clusters.getProportions();


        boolean shouldStop = false;

        while (!shouldStop) // Main loop
        {
            
            // Reset the centroids and the member counters
            for (int i = 0; i < newCentroids.length; i++)
                Arrays.fill(newCentroids[i], 0, newCentroids[i].length, 0);
            Arrays.fill(count, 0, count.length, 0);

            // First we will accumulate the data points
            // into their nearest clusters, storing this
            // information into the newClusters variable.

            // For each point in the data set,
            for (int i = 0; i < data.length; i++)
            {
                // Get the point
                double[] point = data[i];

                // Get the nearest cluster centroid
                int c = labels[i] = clusters.Nearest(point);

                // Increase the cluster's sample counter
                count[c]++;

                // Accumulate in the corresponding centroid
                for (int j = 0; j < point.length; j++)
                    newCentroids[c][j] += point[j];
            }

            // Next we will compute each cluster's new centroid
            //  by dividing the accumulated sums by the number of
            //  samples in each cluster, thus averaging its members.
            for (int i = 0; i < newCentroids.length; i++)
            {
                double clusterCount = count[i];

                if (clusterCount != 0)
                {
                    for (int j = 0; j < newCentroids[i].length; j++)
                        newCentroids[i][j] /= clusterCount;
                }
            }


            // The algorithm stops when there is no further change in the
            //  centroids (relative difference is less than the threshold).
            shouldStop = converged(centroids, newCentroids, threshold);

            // go to next generation
            for (int i = 0; i < centroids.length; i++)
                for (int j = 0; j < centroids[i].length; j++)
                    centroids[i][j] = newCentroids[i][j];
        }


        if (computeInformation)
        {
            // Compute cluster information (optional)
            for (int i = 0; i < centroids.length; i++)
            {
                // Extract the data for the current cluster
                double[][] sub = data.Submatrix(labels.Find(x => x == i));

                if (sub.length > 0)
                {
                    // Compute the current cluster variance
                    covariances[i] = Catalano.Statistics.Tools.Covariance(sub, centroids[i]);
                }
                else
                {
                    // The cluster doesn't have any samples
                    covariances[i] = new double[cols][cols];
                }

                // Compute the proportion of samples in the cluster
                proportions[i] = (double)sub.length / data.length;
            }
        }

        clusters.setCentroids(centroids);

        // Return the classification result
        return labels;
    }
    
    private static boolean converged(double[][] centroids, double[][] newCentroids, double threshold){
        for (int i = 0; i < centroids.length; i++){
            double[] centroid = centroids[i];
            double[] newCentroid = newCentroids[i];

            for (int j = 0; j < centroid.length; j++){
                if ((Math.abs((centroid[j] - newCentroid[j]) / centroid[j])) >= threshold)
                    return false;
            }
        }
        return true;
    }
    
}
