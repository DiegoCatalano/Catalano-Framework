// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

package Catalano.MachineLearning;

import java.text.ParseException;

/**
 * Numeric attribute. Numeric attributes can be real or integer numbers.
 *
 * @author Haifeng Li
 */
public class NumericAttribute extends Attribute {

    /**
     * Initializes a new instance of the NumericAttribute class.
     * @param name Name of the attribute.
     */
    public NumericAttribute(String name) {
        super(Type.NUMERIC, name);
    }

    /**
     * Initializes a new instance of the NumericAttribute class.
     * @param name Name of the attribute.
     * @param weight Weight of the attribute.
     */
    public NumericAttribute(String name, double weight) {
        super(Type.NUMERIC, name, weight);
    }

    /**
     * Initializes a new instance of the NumericAttribute class.
     * @param name Name of the attribute.
     * @param description Description of the attribute.
     */
    public NumericAttribute(String name, String description) {
        super(Type.NUMERIC, name, description);
    }

    /**
     * Initializes a new instance of the NumericAttribute class.
     * @param name Name of the attribute.
     * @param description Description of the attribute.
     * @param weight Weight of the attribute.
     */
    public NumericAttribute(String name, String description, double weight) {
        super(Type.NUMERIC, name, description, weight);
    }

    @Override
    public String toString(double x) {
        return Double.toString(x);
    }

    @Override
    public double valueOf(String s) throws ParseException {
        return Double.valueOf(s);
    }
}