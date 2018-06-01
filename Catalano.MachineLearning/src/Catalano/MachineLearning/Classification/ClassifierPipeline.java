// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.MachineLearning.Classification;

import Catalano.MachineLearning.Codebook;
import Catalano.MachineLearning.Dataset.DecisionVariable;
import Catalano.MachineLearning.FeatureScaling.IFeatureScaling;
import Catalano.MachineLearning.FeatureScaling.Normalization;
import java.io.Serializable;

/**
 * Classifier Pipeline.
 * 
 * 
 * @author Diego Catalano
 */
public class ClassifierPipeline implements Serializable{
    
    private final IClassifier classifier;
    private final IFeatureScaling featureScale;
    private DecisionVariable[] variables;
    
    private Codebook codebook;

    public IClassifier getClassifier() {
        return classifier;
    }

    public IFeatureScaling getFeatureScale() {
        return featureScale;
    }
    
    /**
     * Classifier pipeline.
     * @param classifier Classifier.
     */
    public ClassifierPipeline(IClassifier classifier){
        this(classifier, new Normalization());
    }

    /**
     * Classifier pipeline.
     * @param classifier Classifier.
     * @param scale Feature scale.
     */
    public ClassifierPipeline(IClassifier classifier, IFeatureScaling scale) {
        this(classifier, scale, null);
    }
    
    /**
     * Classifier pipeline.
     * @param classifier Classifier.
     * @param scale Feature scale.
     * @param variables Decision variables.
     */
    public ClassifierPipeline(IClassifier classifier, IFeatureScaling scale, DecisionVariable[] variables) {
        this(classifier, scale, variables, null);
    }
    
    /**
     * Classifier pipeline.
     * @param classifier Classifier.
     * @param scale Feature scale.
     * @param variables Decision variables.
     * @param codebook Codebook.
     */
    public ClassifierPipeline(IClassifier classifier, IFeatureScaling scale, DecisionVariable[] variables, Codebook codebook) {
        this.classifier = classifier;
        this.featureScale = scale;
        this.variables = variables;
        this.codebook = codebook;
    }
    
    /**
     * Predict the sample.
     * @param sample Sample.
     * @return The predicted value.
     */
    public int Predict(double[] sample){
        
        double[] v;
        if(variables == null){
            if(featureScale != null){
                v = featureScale.Compute(sample);
                return classifier.Predict(v);
            }
            else{
                return classifier.Predict(sample);
            }
        }
        else{
            if(featureScale != null){
                v = featureScale.Compute(variables, sample);
                return classifier.Predict(v);
            }
            else{
                return classifier.Predict(sample);
            }
        }
        
    }
    
    public String Translate(int code){
        return codebook.Translate(code);
    }
    
}