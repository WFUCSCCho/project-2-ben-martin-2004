/***************************************************************
 * @file: Proj2.java
 * @description: Project 2 driver (all-in-one). Runs multiple
 *               experiments automatically for different N values.
 *               For each N: reads N movies from CSV, creates
 *               sorted + randomized lists, builds 4 trees
 *               (BST/AVL x sorted/random), times insertion + search,
 *               prints results, appends CSV to output.txt.
 * @author: Ben Martin
 * @date: October 26, 2025
 ***************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Proj2
{
    public static void main( String[] args ) throws IOException
    {
        if( args.length != 1 )
        {
            System.err.println( "Usage: java Proj2 <input file>" );
            System.exit( 1 );
        }

        String inputFileName = args[ 0 ];

        int[] experiments = { 500, 1000, 3000, 6000, 9000, 12000 };

        for( int i = 0; i < experiments.length; i++ )
        {
            runExperiment( inputFileName, experiments[ i ] );
        }
    }

    private static void runExperiment( String inputFileName, int numLines ) throws IOException
    {
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        inputFileNameStream = new FileInputStream( inputFileName );
        inputFileNameScanner = new Scanner( inputFileNameStream );

        // ignore header
        if( inputFileNameScanner.hasNextLine( ) )
        {
            inputFileNameScanner.nextLine( );
        }

        ArrayList<HorrorMovie> original = new ArrayList<HorrorMovie>( );

        int linesRead = 0;
        while( inputFileNameScanner.hasNextLine( ) && linesRead < numLines )
        {
            String line = inputFileNameScanner.nextLine( );
            HorrorMovie movie = parseHorrorMovieLine( line );

            if( movie != null )
            {
                original.add( movie );
                linesRead++;
            }
        }

        inputFileNameScanner.close( );
        inputFileNameStream.close( );

        ArrayList<HorrorMovie> sorted = new ArrayList<HorrorMovie>( original );
        Collections.sort( sorted );

        ArrayList<HorrorMovie> randomized = new ArrayList<HorrorMovie>( original );
        Collections.shuffle( randomized, new Random( ) );

        BST<HorrorMovie> bstSorted = new BST<HorrorMovie>( );
        BST<HorrorMovie> bstRandom = new BST<HorrorMovie>( );
        AvlTree<HorrorMovie> avlSorted = new AvlTree<HorrorMovie>( );
        AvlTree<HorrorMovie> avlRandom = new AvlTree<HorrorMovie>( );

        long insBstSorted = timeInsertBST( bstSorted, sorted );
        long insAvlSorted = timeInsertAVL( avlSorted, sorted );
        long insBstRandom = timeInsertBST( bstRandom, randomized );
        long insAvlRandom = timeInsertAVL( avlRandom, randomized );

        long seaBstSorted = timeSearchBST( bstSorted, original );
        long seaAvlSorted = timeSearchAVL( avlSorted, original );
        long seaBstRandom = timeSearchBST( bstRandom, original );
        long seaAvlRandom = timeSearchAVL( avlRandom, original );

        printPretty( inputFileName, original.size( ),
                insBstSorted, insAvlSorted, insBstRandom, insAvlRandom,
                seaBstSorted, seaAvlSorted, seaBstRandom, seaAvlRandom );

        appendCsv( "output.txt", inputFileName, original.size( ),
                insBstSorted, insAvlSorted, insBstRandom, insAvlRandom,
                seaBstSorted, seaAvlSorted, seaBstRandom, seaAvlRandom );
    }

    /**
     * Dataset-specific CSV parsing.
     * Matches Parser.java exactly:
     *  - title  -> column index 2
     *  - rating -> column index 10
     */
    private static HorrorMovie parseHorrorMovieLine( String line )
    {
        if( line == null )
        {
            return null;
        }

        String trimmed = line.trim( );
        if( trimmed.length( ) == 0 )
        {
            return null;
        }

        String[] cols = trimmed.split( "," );

        if( cols.length <= 10 )
        {
            return null;
        }

        String title = cols[ 2 ].trim( );
        double rating = parseDoubleSafe( cols, 10 );

        if( title.length( ) == 0 )
        {
            return null;
        }

        return new HorrorMovie( title, rating );
    }

    private static double parseDoubleSafe( String[] cols, int index )
    {
        try
        {
            return Double.parseDouble( cols[ index ].trim( ) );
        }
        catch( Exception e )
        {
            return 0.0;
        }
    }

    private static long timeInsertBST( BST<HorrorMovie> tree, ArrayList<HorrorMovie> list )
    {
        long start = System.nanoTime( );

        for( int i = 0; i < list.size( ); i++ )
        {
            tree.insert( list.get( i ) );
        }

        return System.nanoTime( ) - start;
    }

    private static long timeInsertAVL( AvlTree<HorrorMovie> tree, ArrayList<HorrorMovie> list )
    {
        long start = System.nanoTime( );

        for( int i = 0; i < list.size( ); i++ )
        {
            tree.insert( list.get( i ) );
        }

        return System.nanoTime( ) - start;
    }

    private static long timeSearchBST( BST<HorrorMovie> tree, ArrayList<HorrorMovie> list )
    {
        long start = System.nanoTime( );

        for( int i = 0; i < list.size( ); i++ )
        {
            tree.search( list.get( i ) );
        }

        return System.nanoTime( ) - start;
    }

    private static long timeSearchAVL( AvlTree<HorrorMovie> tree, ArrayList<HorrorMovie> list )
    {
        long start = System.nanoTime( );

        for( int i = 0; i < list.size( ); i++ )
        {
            tree.contains( list.get( i ) );
        }

        return System.nanoTime( ) - start;
    }

    private static void printPretty(
            String filename,
            int n,
            long insBstSorted, long insAvlSorted, long insBstRandom, long insAvlRandom,
            long seaBstSorted, long seaAvlSorted, long seaBstRandom, long seaAvlRandom )
    {
        System.out.println( "==================================================" );
        System.out.println( "Dataset: " + filename );
        System.out.println( "N      : " + n );
        System.out.println( "Units  : seconds" );
        System.out.println( "--------------------------------------------------" );

        System.out.printf( "INSERT (sorted)  BST=%10.6f   AVL=%10.6f%n",
                nsToSec( insBstSorted ), nsToSec( insAvlSorted ) );
        System.out.printf( "INSERT (random)  BST=%10.6f   AVL=%10.6f%n",
                nsToSec( insBstRandom ), nsToSec( insAvlRandom ) );

        System.out.printf( "SEARCH (sorted)  BST=%10.6f   AVL=%10.6f%n",
                nsToSec( seaBstSorted ), nsToSec( seaAvlSorted ) );
        System.out.printf( "SEARCH (random)  BST=%10.6f   AVL=%10.6f%n",
                nsToSec( seaBstRandom ), nsToSec( seaAvlRandom ) );

        System.out.println( "==================================================" );
    }

    private static void appendCsv(
            String outFileName,
            String dataset,
            int n,
            long insBstSorted, long insAvlSorted, long insBstRandom, long insAvlRandom,
            long seaBstSorted, long seaAvlSorted, long seaBstRandom, long seaAvlRandom ) throws IOException
    {
        boolean writeHeader = false;

        File f = new File( outFileName );
        if( !f.exists( ) )
        {
            writeHeader = true;
        }

        FileOutputStream outStream = new FileOutputStream( outFileName, true );
        PrintWriter out = new PrintWriter( outStream );

        if( writeHeader )
        {
            out.println(
                    "dataset,N,ins_bst_sorted_ns,ins_avl_sorted_ns," +
                            "ins_bst_random_ns,ins_avl_random_ns," +
                            "sea_bst_sorted_ns,sea_avl_sorted_ns," +
                            "sea_bst_random_ns,sea_avl_random_ns"
            );
        }

        out.printf( "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                dataset,
                n,
                insBstSorted, insAvlSorted, insBstRandom, insAvlRandom,
                seaBstSorted, seaAvlSorted, seaBstRandom, seaAvlRandom );

        out.close( );
        outStream.close( );
    }

    private static double nsToSec( long ns )
    {
        return ns / 1000000000.0;
    }
}
