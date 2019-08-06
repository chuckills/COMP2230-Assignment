package classes;

import java.util.ArrayList;

/** Station.java
 *
 * Author: Gregory Choice
 * Student#: c9311718
 * Date created: 10/10/2018
 * Date modified: 26/10/2018
 *
 * Station class represents the graph vertices in a simulated rail network
 *
 */
public class Station implements Comparable<Station>
{
    private String sName;
    private String sLine;
    private ArrayList<Edge> sEdges;
    private double sTimeCost;
    private Station previous;

    /** Station Constructor
     *
     * @param name - String, Station name
     * @param line - String, Station line
     */
    public Station(String name, String line)
    {
        sName = name;
        sLine = line;
        sEdges = new ArrayList<>();
        sTimeCost = Double.POSITIVE_INFINITY;
        previous = null;
    }

    /** getTimeCost()
     *
     * @return - double, Time cost
     */
    public double getTimeCost()
    {
        return sTimeCost;
    }

    /** setTimeCost()
     *
     * @param cost - double, Time cost from the source to this station
     */
    public void setTimeCost(double cost)
    {
        sTimeCost = cost;
    }

    /** getPrevious()
     *
     * @return - Station, Previous station
     */
    public Station getPrevious()
    {
        return previous;
    }

    /** setPrevious()
     *
     * @param prev - Station, Previous station
     */
    public void setPrevious(Station prev)
    {
        previous = prev;
    }

    /** getEdges()
     *
     * @return - ArrayList<Edge>, Edge list
     */
    public ArrayList<Edge> getEdges()
    {
        return sEdges;
    }

    /** addEdge()
     *
     * @param edge - Edge object to add to the edge list
     */
    public void addEdge(Edge edge)
    {
        sEdges.add(edge);
    }

    /** getLine()
     *
     * @return - String, Line name
     */
    public String getLine()
    {
        return sLine;
    }

    /** getName()
     *
     * @return - String, Station name
     */
    public String getName()
    {
        return sName;
    }

    /** compareTo()
     *
     * @param otherStation - Station, The Station object to compare to this Station
     * @return - int, returns 1 if sTimeCost is higher, 0 if sTimeCost is the same, -1 if sTimeCost is smaller
     */
    @Override
    public int compareTo(Station otherStation)
    {
        return Double.compare(sTimeCost, otherStation.sTimeCost);
    }

    /** equals()
     *
     * @param otherStation - Station, The Station object to compare to this Station
     * @return - boolean, returns true if otherStation is a Station and the name and line are the same or
     *           otherStation is a String and the name is the same, false otherwise
     */
    @Override
    public boolean equals(Object otherStation)
    {
        if(otherStation instanceof Station)
        {
            return ((Station)otherStation).getName().equals(sName) && ((Station)otherStation).getLine().equals(sLine);
        }
        else if(otherStation instanceof String)
        {
            return otherStation.equals(sName);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return sName + " " + sLine + " " + sTimeCost + "\n";
    }
}
