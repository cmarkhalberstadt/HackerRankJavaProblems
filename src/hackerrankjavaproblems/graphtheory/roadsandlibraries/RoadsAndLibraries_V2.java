/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hackerrankjavaproblems.graphtheory.roadsandlibraries;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmarkhalberstadt
 */
public class RoadsAndLibraries_V2 {
    
    
    public static void main(String[] args)
    {
        test_readFromFile();
    }
    
    public static void test1()
    {
        
        LinkedList<Pair> pairs = new LinkedList<Pair>();
        
        pairs.add(new Pair(1, 3));
        pairs.add(new Pair(3, 4));
        pairs.add(new Pair(2, 4));
        pairs.add(new Pair(1, 2));
        pairs.add(new Pair(2, 3));
        pairs.add(new Pair(5, 6));
        
        System.out.println(getLowestCost(6, 6, 5, 2, pairs));
    }
    
    public static void test2(){
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int currentTestCase = 0; currentTestCase < q; currentTestCase++){
            // get the number of cities
            int n = in.nextInt();
            // get the number of roads
            int m = in.nextInt();
            // get the cost to build a library
            long cLib = in.nextLong();
            // get the cost to build a road
            long cRoad = in.nextLong();
            
            // get the roads between cities which can be rebuilt
            LinkedList<Pair> roads = new LinkedList<>();
            for(int a1 = 0; a1 < m; a1++){
                Pair p = new Pair(in.nextInt(), in.nextInt());
                roads.add(p);
            }
            
            // get and print the lowest cost to provide library access
            // to all cities
            System.out.println(getLowestCost(
                n,      // num cities
                m,      // num roads
                cLib,   // cost to build a library
                cRoad,  // cost to build a road
                roads   // a collections of the roads connecting the cities
                ));
        }
    }
    
    public static void test_readFromFile(){
        try {
            File file = new File("src/hackerrankjavaproblems/graphtheory/roadsandlibraries/input2.txt");
            Scanner in = new Scanner(file);
            int q = in.nextInt();
            for(int currentTestCase = 0; currentTestCase < q; currentTestCase++){
                // get the number of cities
                int n = in.nextInt();
                // get the number of roads
                int m = in.nextInt();
                // get the cost to build a library
                long cLib = in.nextLong();
                // get the cost to build a road
                long cRoad = in.nextLong();
                
                // get the roads between cities which can be rebuilt
                LinkedList<Pair> roads = new LinkedList<>();
                for(int a1 = 0; a1 < m; a1++){
                    Pair p = new Pair(in.nextInt(), in.nextInt());
                    roads.add(p);
                }
                
                //System.out.println("input: " + n + " " + m + " " + 
                  //      cLib + " " + cRoad);
                
                // get and print the lowest cost to provide library access
                // to all cities
                System.out.println(getLowestCost(
                        n,      // num cities
                        m,      // num roads
                        cLib,   // cost to build a library
                        cRoad,  // cost to build a road
                        roads   // a collections of the roads connecting the cities
                ));
            }
            // close the file scanner
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RoadsAndLibraries_Main_V1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Integer Pair Class
     */
    public static class Pair
    {
        int x = 0,y = 0;
        
        public Pair(int x, int y)
        {
            this.x = x;this.y = y;
        }
    }
    
    
    
    /**
     * Returns the lowest cost for providing access to a library for each of
     * the cities
     * 
     * Runs in O(numCities + numRoads) time complexity and O(numCities) space
     * complexity
     * 
     * @param numCities
     * @param numRoads
     * @param costLibrary
     * @param costRoad
     * @param connections
     * @return - The minimum cost to provide access to a library to all cities
     */
    public static long getLowestCost(int numCities, int numRoads,
            long costLibrary, long costRoad,
            Collection<Pair> connections)
    {
        // check to see if the cost to build a road is greater than or equal
        // to the cost of building a library. If so, then we will just build
        // libraries in each city and not rebuild any roads. Return the
        // cost per library multiplied by the number of cities.
        if (costRoad >= costLibrary || numRoads == 0) 
            return (numCities * costLibrary);
        
        // we are going to build the minimum number of libraries and the
        // maximum number of roads
        
        // array of city number to a set of cities it will be connected to once
        // the roads have been rebuilt
        ArrayList<Set<Integer>> cityConnections = 
                new ArrayList<>(numCities);
        
        // fill up the array list of hash sets with sets of cities only
        // connected to themselves
        for (int i = 0; i < numCities; i++)
        {
            // create the new set to add
            Set setToAdd = new HashSet<Integer>();
            // add the current city to the set to add
            setToAdd.add(i+1);
            // add the current set to the array list of city connections
            cityConnections.add(i, setToAdd);
        }
        
        // iterate through all input pairs (denoting the roads)
        for (Pair p : connections)
        {
            // get the x set and y set
            Set xSet = cityConnections.get(p.x-1);
            Set ySet = cityConnections.get(p.y-1);
            
            // declare the set to add into and set to add from variables
            Set<Integer> setToAddInto = null;
            Set<Integer> setToAddFrom = null;
            int setToAddIntoIndex = -1;
            
            // check to see which set is larger - we want to always add into
            // the larger set
            if (xSet.size() > ySet.size())
            {
                setToAddIntoIndex = p.x-1;
                setToAddInto = xSet;
                setToAddFrom = ySet;
            }
            else
            {
                setToAddIntoIndex = p.y-1;
                setToAddInto = ySet;
                setToAddFrom = xSet;
            }
            
            // add the setToAddFrom into the setToAddInto
            setToAddInto.addAll(setToAddFrom);
            
            // add the setToAddInto back into the arraylist
            cityConnections.set(setToAddIntoIndex, setToAddInto);
            
            // iterate through the set to add from and set all indeces in the
            // city connections array to the setToAddInto
            for (Integer i : setToAddFrom)
            {
                cityConnections.set(i-1, setToAddInto);
            }
        } // end for (Pair p : connections)
        
        // at this point, the cityConnections map has been filled with HashSets
        // representing all of the cities to which each city can be connected
        // to by rebuilding roads. Any unconnected city still left inthe
        // citiesToVisit set must have its own individual library built
        
        
        // variable to hold the return value
        long returnValue = 0;
        
        // iterate through the array list of sets
        for (Set<Integer> thisSet : cityConnections)
        {
            if (!thisSet.isEmpty())
            {
                // for each of the sets in the unique city connections, we build
                // 1 library and (size() - 1) roads
                returnValue += costLibrary;

                returnValue += (costRoad * (thisSet.size() - 1));
                
                // clear the current set
                thisSet.clear();
            }
        }
        
        // return the return value
        return returnValue;
    }
}
