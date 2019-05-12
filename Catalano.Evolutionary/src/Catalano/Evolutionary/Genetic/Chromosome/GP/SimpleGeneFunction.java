/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Evolutionary.Genetic.Chromosome.GP;

/**
 *
 * @author Diego
 */
public class SimpleGeneFunction implements IGPGene{
    
    protected enum Functions
    {
        /// <summary>
        /// Addition operator.
        /// </summary>
        Add,
        /// <summary>
        /// Suntraction operator.
        /// </summary>
        Subtract,
        /// <summary>
        /// Multiplication operator.
        /// </summary>
        Multiply,
        /// <summary>
        /// Division operator.
        /// </summary>
        Divide,
    }
    
    /// <summary>
    /// Number of different functions supported by the class.
    /// </summary>
    protected final int FunctionsCount = 4;

    // gene type
    private GPGeneType type;
    // total amount of variables in the task which is supposed to be solved
    private int variablesCount;
    //
    private int val;

    @Override
    public GPGeneType GeneType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int ArgumentsCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int MaxArgumentsCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IGPGene Clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Generate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Generate(GPGeneType type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IGPGene CreateNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IGPGene CreateNew(GPGeneType type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
