/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hackerrankjavaproblems.graphtheory.roadsandlibraries;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmarkhalberstadt
 */
public class RoadsAndLibraries_Main_V1 {
    
    
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
            File file = new File("src/hackerrankjavaproblems/graphtheory/roadsandlibraries/input10.txt");
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
        
        // map of city number to a set of cities it will be connected to once
        // the roads have been rebuilt
        HashMap<Integer, HashSet<Integer>> cityConnections = 
                new HashMap<>();
        
        // 
        HashSet<HashSet<Integer>> uniqueCityConnections = 
                new HashSet<>();
        
        // a set of cities which still need to be visit / connected to a
        // library
        HashSet<Integer> citiesToVisit = new HashSet<>();
        for (int i = 1; i < numCities; i++) citiesToVisit.add(i);
        
        int j = 0;
        
        for (Pair p : connections)
        {
            // remove both p.x and p.y from the set of cities which still need
            // to be visited
            citiesToVisit.remove(p.x);
            citiesToVisit.remove(p.y);
            
            // check to see if either p.x or p.y already have associations in
            // the map of city connections
            if (cityConnections.containsKey(p.x) && 
                    !cityConnections.containsKey(p.y))
            {
                // there are connections for p.x, but not p.y
                
                // get the set associated with p.x
                HashSet<Integer> set = cityConnections.get(p.x);
                
                // add p.y to the p.x set
                set.add(p.y);
                
                // remove the previous associations with p.x and p.y in the map
                cityConnections.remove(p.x);
                cityConnections.remove(p.y);
                
                // associate this set in the map with the p.y key
                cityConnections.put(p.x, set);
                cityConnections.put(p.y, set);
            }
            else if (!cityConnections.containsKey(p.x) && 
                    cityConnections.containsKey(p.y))
            {
                // there are connections for p.y but not p.x
                
                // get the set associated with p.x
                HashSet<Integer> xSet = cityConnections.get(p.y);
                
                uniqueCityConnections.remove(xSet);
                
                // add p.y to the p.x set
                xSet.add(p.x);
                
                // remove the previous associations with p.x and p.y in the map
                cityConnections.remove(p.x);
                cityConnections.remove(p.y);
                
                
                
                // associate this set in the map with the p.x and p.y key
                cityConnections.put(p.x, xSet);
                cityConnections.put(p.y, xSet);
                uniqueCityConnections.add(xSet);
            }
            else if (cityConnections.containsKey(p.x) && 
                    cityConnections.containsKey(p.y))
            {
                // there are associations for both p.x and p.y in the map
                
                // get the p.x set
                HashSet<Integer> xSet = cityConnections.get(p.x);
                // get the p.y set
                HashSet<Integer> ySet = cityConnections.get(p.y);
                
                // remove both p.x and p.y sets from the set of unique sets
                uniqueCityConnections.remove(xSet);
                uniqueCityConnections.remove(ySet);
                
                // add all members of the p.y set to the p.x set
                xSet.addAll(ySet);
                    
                // remove the previous associations with p.x and p.y in the map
                cityConnections.remove(p.x);
                cityConnections.remove(p.y);
                
                // associate this set in the map with the p.x and p.y key
                cityConnections.put(p.x, xSet);
                cityConnections.put(p.y, xSet);
                uniqueCityConnections.add(xSet);
            }
            else //(!cityConnections.containsKey(p.x) && 
                    // !cityConnections.containsKey(p.y))
            {
                // the map contains associations for neither p.x nor p.y
                
                // create a new HashSet
                HashSet<Integer> set = new HashSet<>();
                // add p.x and p.y to the set
                set.add(p.x);
                set.add(p.y);
                
                // associate this new set with both p.x and p.y in the map
                cityConnections.put(p.x, set);
                cityConnections.put(p.y, set);
                
                // add the set to the set of unique city connect sets
                uniqueCityConnections.add(set);
            }
        } // end for (Pair p : connections)
        
        // at this point, the cityConnections map has been filled with HashSets
        // representing all of the cities to which each city can be connected
        // to by rebuilding roads. Any unconnected city still left inthe
        // citiesToVisit set must have its own individual library built
        
        HashSet<Integer> citiesCounted = new HashSet<>();
        
        // variable to hold the return value
        long returnValue = 0;
        
        // iterate through the set of unique sets
        for (Map.Entry<Integer, HashSet<Integer>> entry : 
                cityConnections.entrySet())
        {
            if (!entry.getValue().isEmpty())
            {
                boolean countThisSet = true;
                
                // iterate through this set and see if it contains any cities
                // we have already counted - if so, do not count this set
                for (Integer city : entry.getValue())
                {
                    if (citiesCounted.contains(city))
                    {
                        // this set contains cities we have already counted
                        // --- do not count this set
                        countThisSet = false;
                        //break;
                    }
                    // add the current city to the set of cities we have
                    // counted
                    citiesCounted.add(city);
                }
                
                if (countThisSet)
                {
                    // we should count this set
                    
                    // build a library for this collection of cities connected
                    // by roads
                    returnValue += costLibrary;

                    // build the roads to connect these cities
                    // NOTE: we need to build (numCitiesInSet) - 1 roads
                    returnValue += (costRoad * (entry.getValue().size() - 1));
                }
                
                // remove all of the values from this set
                entry.getValue().clear();
            }
        }
        
        // if there are still cities in the citiesToVisit set, we need to
        // build libraries in those cities
        returnValue += (costLibrary * citiesToVisit.size());
        
        // return the return value
        return returnValue;
    }
}
