import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package hackerrankjavaproblems.graphtheory.journeytothemoon;

/**
 *
 * @author cmarkhalberstadt
 */
public class JourneyToTheMoon_WhatGotSubmittedAndAccepted {
    public static void main(String[] args) throws Exception{
        test_JourneyToTheMoon_readFromFile();
    }
    
    public static void test_JourneyToTheMoon_readFromFile(){
            //File file = new File("src/hackerrankjavaproblems/graphtheory/journeytothemoon/input_a_1.txt");
            Scanner in = new Scanner(System.in);
            int numAstronauts = in.nextInt();
            int numConnections = in.nextInt();
            LinkedList<Pair> connections = new LinkedList<>();
            for(int currentConnection = 0; 
                    currentConnection < numConnections; 
                    currentConnection++){
                
                // get the nerxt pair
                Pair p = new Pair(in.nextInt(), in.nextInt());
                // add the current pair to the list
                connections.add(p);
                
            }
            
            System.out.println(getNumCombinations(numAstronauts, connections));
            
            
    }
    
    public static long getNumCombinations(int numAstros, Collection<Pair> pairs)
    {
        // declare the initial sets
        ArrayList<Set<Integer>> setsByIndex = new ArrayList<>(numAstros);
        
        // set of unique sets
        Set<Set<Integer>> uniqueSets = new HashSet<>();
        
        // iterate through the sets and start off with each astro in their
        // own country
        for (int i = 0; i < numAstros; i++)
        {
            // add the set only containing (i) at the i'th position
            Set set = new HashSet<Integer>();
            set.add(i);
            setsByIndex.add(i, set);
            
            // add this set to the set of unique sets
            uniqueSets.add(set);
        }
        
        // iterate through the collection of pairs
        for (Pair p : pairs)
        {
            // associate the x pair with the y pair
            
            // check to see if there is not already an association between the
            // x pair and the y pair
            if (!setsByIndex.get(p.x).contains(p.y))
            {
                // the x set is not associated with the y set. we need to form
                // this association
                
                // get the xSet and ySet
                Set<Integer> xSet = setsByIndex.get(p.x);
                Set<Integer> ySet = setsByIndex.get(p.y);
                
                // remove both the x and y sets from the set of unique sets
                uniqueSets.remove(xSet);
                uniqueSets.remove(ySet);
                
                Set<Integer> setToAddInto = null;
                Set<Integer> setToAddFrom = null;
                
                // check to see which set is larger
                if (xSet.size() > ySet.size())
                {
                    // the x set is larger - we should add the y set to the
                    // x set
                    setToAddInto = xSet;
                    setToAddFrom = ySet;
                }
                else
                {
                    // the y set is larger - we should add the x set to the
                    // y set
                    setToAddInto = ySet;
                    setToAddFrom = xSet;
                }
                
                
                
                // add the y set to the x set
                setToAddInto.addAll(setToAddFrom);
                
                // add the new xSet back into the set of unique sets
                uniqueSets.add(setToAddInto);
                
                // add the new x set back into the arraylist of sets for both
                // x and y positions
                setsByIndex.set(p.x, setToAddInto);
                setsByIndex.set(p.y, setToAddInto);
                
                // iterate through all indeces in the setToAddFrom set and set
                // those indeces appropriately in the setsByIndex array to the
                // setToAddInto set
                for (Integer index : setToAddFrom)
                {
                    setsByIndex.set(index, setToAddInto);
                }
            }
        } // end of for (Pair p : pairs
        
        // array to hold the sizes of each set in the set of unique sets
        HashMap<Integer, Long> setSizesMappedToCounts = new HashMap<>();
        // set of unique set sizes
        HashSet<Integer> uniqueSetSizes = new HashSet<>();
        
        // iterate through all of the unique sets and record their sizes into 
        // the hash map and hash set data structures
        for (Set s : uniqueSets)
        {
            // 
            uniqueSetSizes.add(s.size());
            if (setSizesMappedToCounts.containsKey(s.size()))
            {
                setSizesMappedToCounts.put(s.size(),
                        setSizesMappedToCounts.get(s.size()) + 1l);
            }
            else
            {
                setSizesMappedToCounts.put(s.size(),1l);
            }
        }
        
        // return value
        long returnValue = 0;
        
        // iterate through the map of setSizesMappedToCounts
        for (Map.Entry<Integer, Long> entry : setSizesMappedToCounts.entrySet())
        {
            // if we have more than 1 occourence, compute the number of possibilities
            // for sets of this particular size
            if (entry.getValue() != 1)
            {
                // compute n-choose-2 for this value multiplied by the key for
                // this set squared
                long thisValue = (entry.getKey() * entry.getKey()) * 
                        (entry.getValue() * (entry.getValue() - 1) / 2);
                returnValue += thisValue;
            }
        }
        
        // get all combinations of 2 set sizes from the set of unique set sizes
        Collection<Collection<Integer>> combinations = 
                getAllCombinationsWithGivenNumberOfItems(uniqueSetSizes, 2);
        
        // iterate through all of the combinations of sets with heterogenious
        // counts
        for (Collection<Integer> c : combinations)
        {
            long toAdd = 1;
            for (Integer thisInt : c)
            {
                toAdd *= thisInt;
                toAdd *= setSizesMappedToCounts.get(thisInt);
            }
            returnValue += toAdd;
        }
        
        // return the computed value
        return returnValue;
    }
    
    public static <T> Collection<Collection<T>> getAllCombinationsWithGivenNumberOfItems(
            Collection<T> input,
            int numItems)
    {
        return getAllCombinationsWithGivenNumberOfItems(
            // convert the input collection to an array
            input.toArray((T[]) new Object[input.size()]),
            numItems);
    }
    
    public static <T> Collection<Collection<T>> getAllCombinationsWithGivenNumberOfItems(
            T[] input,
            int numItems)
    {
        // check to see if there are enough items in the input collection to
        // form combinations with the desired number of items
        if (numItems > input.length)
        {
            // there are not enough items in the input collection to be able to
            // return any combination. Return the empty collection
            return  new LinkedList<Collection<T>>();
        }
        
        // get all combinations with given number of 
        LinkedList<Collection<T>> returnValue = new LinkedList<>();
        
        // get all combinations recursively
        getAllCombinationsWithGivenNumberOfItemsRecursive(
                input,
                0,
                numItems,
                new ArrayList<T>(numItems),
                returnValue);
        
        // return the collection of combinations
        return returnValue;
    }
    
    private static <T> void getAllCombinationsWithGivenNumberOfItemsRecursive(
            T[] input,
            int currentIndex,
            int numRemainingItemsToChoose,
            Collection<T> currentCollection,
            Collection<Collection<T>> returnValue)
    {
        // check to see if we have to 
        if (numRemainingItemsToChoose == 0)
        {
            returnValue.add(currentCollection);
            return;
        }
        
        // check to see if we are able to form a valid collection with the
        // number of items we have remaining
        if ((input.length - currentIndex) < numRemainingItemsToChoose)
        {
            // we cannot form a valid combination
            return;
        }
        
        // get all combinations where current item is not included
        getAllCombinationsWithGivenNumberOfItemsRecursive(
                input,
                currentIndex + 1, // move to the next index
                numRemainingItemsToChoose, // still need same number of items
                new ArrayList<>(currentCollection),
                returnValue);
        
        // get all combinations where current item is included
        currentCollection.add(input[currentIndex]);
        getAllCombinationsWithGivenNumberOfItemsRecursive(
                input,
                currentIndex + 1, // move to next index
                numRemainingItemsToChoose - 1, // one less item needed
                new ArrayList<>(currentCollection),
                returnValue);
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
        
        public String toString()
        {
            return "(x:" + x + ", y:" + y +")";
        }
    }
}
