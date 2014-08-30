/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Diego
 */
public class KMeansClusterCollection {
    
    private ArrayList<KMeansCluster> clusters;
    
    private double[] proportions;
    private double[][] centroids;
    private double[][][] covariances;
    
    public double[] getCentroids(int index){
        return centroids[index];
    }

    public double[][] getCentroids() {
        return centroids;
    }

    public void setCentroids(double[][] centroids) {
        if (centroids == centroids)
            return;

        if (centroids == null)
            throw new IllegalArgumentException("value");

        int k = this.clusters.size();

        if (centroids.length != k)
            throw new IllegalArgumentException("The number of centroids should be equal to K.");

        // Make a deep copy of the
        // input centroids vector.
        for (int i = 0; i < k; i++)
            centroids[i] = (double[])centroids[i].clone();

        // Reset derived information
        covariances = new double[k][][];
        proportions = new double[k];
    }
    
    public double[][] getCovariance(int index){
        return covariances[index];
    }
    
    public double[][][] getCovariance(){
        return covariances;
    }
    
    public void setCovariance(int index, double[][] covariance){
        this.covariances[index] = covariance;
    }
    
    public double getProportions(int index){
        return proportions[index];
    }

    public double[] getProportions() {
        return proportions;
    }
    
    public void setProportions(int index, double value){
        proportions[index] = value;
    }
    
    public int length(){
        return clusters.size();
    }

    public KMeansClusterCollection(int k) {
        this.proportions = new double[k];
        this.centroids = new double[k][];
        this.covariances = new double[k][][];
        clusters = new ArrayList<KMeansCluster>(k);
        for (int i = 0; i < k; i++)
            clusters.add(new KMeansCluster(this, i));
    }

    public int Nearest(double[] point){
        int min_cluster = 0;
        double min_distance = Catalano.Math.Distance.SquaredEuclidean(point, centroids[0]);

        for (int i = 1; i < centroids.length; i++)
        {
            double dist = Catalano.Math.Distance.SquaredEuclidean(point, centroids[i]);
            if (dist < min_distance)
            {
                min_distance = dist;
                min_cluster = i;
            }
        }

        return min_cluster;
    }
    
    public int[] Nearest(double[][] points){
        int[] labels = new int[points.length];
        for (int i = 0; i < points.length; i++)
            labels[i] = Nearest(points[i]);

        return labels;
    }
    
    public double Distortion(double[][] data){
        double error = 0.0;

        for (int i = 0; i < data.length; i++)
            error += Catalano.Math.Distance.SquaredEuclidean(data[i], centroids[Nearest(data[i])]);

        return error / (double)data.length;
    }
    
    public double Distortion(double[][] data, int[] labels){
        double error = 0.0;

        for (int i = 0; i < data.length; i++)
            error += Catalano.Math.Distance.SquaredEuclidean(data[i], centroids[labels[i]]);

        return error / (double)data.length;
    }
    
    public class KMeansCluster implements Serializable{
        KMeansClusterCollection owner;
        private int index;

        public int getIndex() {
            return index;
        }
        
        public double[] getMean(){
            return owner.getCentroids(index);
        }
        
        public double[][] getCovariance(){
            return owner.getCovariance(index);
        }
        
        public void setCovariance(int index, double[][] covariance){
            owner.setCovariance(index, covariance);
        }
        
        public double getProportion(){
            return owner.getProportions(index);
        }
        
        public void setProportion(double value){
            owner.setProportions(index, value);
        }
        
        public double Distortion(double[][] data){
            double[] centroid = getMean();

            double error = 0.0;
            for (int i = 0; i < data.length; i++)
                error += Catalano.Math.Distance.SquaredEuclidean(data[i], centroid);
            return error / (double)data.length;
        }
        
        public KMeansCluster(KMeansClusterCollection owner, int index){
            this.owner = owner;
            this.index = index;
        }
    }
    
}
