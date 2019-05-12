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
public interface IGPGene {
    
    public enum GPGeneType{
        
        /**
         * Function gene - represents function to be executed.
         */
        Function,
        
        /**
         * Argument gene - represents argument of function.
         */
        Argument
    }
    
    GPGeneType GeneType();

    int ArgumentsCount();

    int MaxArgumentsCount();

    IGPGene Clone( );

    void Generate( );

    void Generate( GPGeneType type );

    IGPGene CreateNew( );

    IGPGene CreateNew( GPGeneType type );
    
    
}
