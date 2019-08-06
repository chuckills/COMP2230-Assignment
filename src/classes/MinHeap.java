package classes;

import java.util.ArrayList;

/** MinHeap.java
 *
 * Author: Gregory Choice
 * Student#: c9311718
 * Date created: 10/10/2018
 * Date modified: 26/10/2018
 *
 * MinHeap class is a data structure based on an indirect binary minheap
 *
 * NOTE: "into" array is redundant in this implementation as it is simply
 * an array of the indexes of the outOf array which can be easily referenced
 */
public class MinHeap
{
    private ArrayList<Station> keyList;
    private ArrayList<Integer> outOf;

    /** MinHeap Constructor **/
    public MinHeap()
    {
        keyList = new ArrayList<>();
        outOf = new ArrayList<>();
        keyList.add(null);
        outOf.add(null);
    }

    /** isEmpty()
     *
     * Method that indicates if the structure has no elements
     *
     * @return boolean, Returns true if there are no elements, false otherwise
     */
    public boolean isEmpty()
    {
        return keyList.size() == 1 && outOf.size() == 1;
    }

    /** size()
     *
     * Method that indicates how many elements are in the structure
     *
     * PRE-CONDITION - Both the keyList and outOf ArrayLists must be modified in tandem
     *
     * @return int, Returns the number of elements in the structure
     */
    private int size()
    {
        return keyList.size() - 1;
    }

    /** add()
     *
     * Method to add new elements to the structure while maintaining its integrity
     *
     * @param newStation - Station, the station object to add
     */
    public void add(Station newStation)
    {
        keyList.add(newStation);
        outOf.add(size(), size());

        // Check the heap integrity from the bottom up
        siftUp(size());
    }

    /** siftUp()
     *
     * Method to recover the integrity of the structure after adding an element
     *
     * @param  newIndex - int, the index in the position array of the new element's actual index
     */
    private void siftUp(int newIndex)
    {
        int child = newIndex;
        int parent = newIndex / 2;

        if(parent > 0)
        {
            Station childStation = keyList.get(outOf.get(child));
            Station parentStation = keyList.get(outOf.get(parent));

            // Iterate through the heap until integrity is satisfied
            while (parent > 0 && (childStation).compareTo(parentStation) < 0)
            {
                // Swap array index numbers
                swap(parent, child);

                // Move up a level in the tree
                child = parent;
                parent = parent / 2;

                if(parent > 0)
                {
                    childStation = keyList.get(outOf.get(child));
                    parentStation = keyList.get(outOf.get(parent));
                }
            }
        }
    }

    /** poll()
     *
     * Method to get the minimum element from the structure while maintaining its integrity
     *
     * @return Station, Returns the station with the smallest time cost
     */
    public Station poll()
    {
        int rootIndex = outOf.get(1);
        Station first = keyList.get(rootIndex);

        // If more than one element in the heap
        if(size() > 1)
        {
            // Move the last element to the top
            Station temp = keyList.get(outOf.get(size()));
            keyList.set(rootIndex, temp);
            int keyIndex = outOf.get(size());
            outOf.remove(new Integer(size()));
            keyList.remove(keyIndex);
        }
        else
        {
            // Remove the final element
            outOf.remove(1);
            keyList.remove(1);
        }

        if(size() > 1)
            // Check the heap integrity from the top down
            siftDown(1);
        return first;
    }

    /** siftDown()
     *
     * Method to maintain the structure's integrity after removing the smallest element
     *
     * @param parent - int, the index in the position array of the actual parent element's index
     */
    private void siftDown(int parent)
    {
        int min;
        int left = parent * 2, right = parent * 2 + 1;

        Station parentStation = keyList.get(outOf.get(parent));
        Station leftStation, rightStation;

        // Check if the left child index is in the bounds
        if(right <= size())
        {
            leftStation = keyList.get(outOf.get(left));
            rightStation = keyList.get(outOf.get(right));

            if(leftStation.compareTo(rightStation) > 0)
            {
                min = right;
            }
            else
            {
                min = left;
            }

            if(parentStation.compareTo(keyList.get(outOf.get(min))) > 0)
            {
                swap(parent, min);
                siftDown(min);
            }
        }
        else if(left <= size())
        {
            leftStation = keyList.get(outOf.get(left));
            if(parentStation.compareTo(leftStation) < 0)
            {
                swap(parent, left);
                siftDown(left);
            }
        }
    }

    /** swap()
     *
     * Method to swap the actual indexes of a parent and child element in the array position array
     *
     * @param parent - int, the array position index of the parent element
     * @param child - int, the array position index of the child element
     */
    private void swap(int parent, int child)
    {
        int arrayTemp = outOf.get(parent);
        outOf.set(parent, outOf.get(child));
        outOf.set(child, arrayTemp);

    }
}
