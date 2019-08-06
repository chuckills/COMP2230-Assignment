package classes;

/** Edge.java
 *
 * Author: Gregory Choice
 * Student#: c9311718
 * Date created: 10/10/2018
 * Date modified: 26/10/2018
 *
 * Edge class represents the graph edge between two Station objects in a rail network simulation
 *
 */
public class Edge
{
    private Station eDestination;
    private int eDuration;

    /** Edge Constructor
     *
     * @param dest - Station, the destination Station of the Edge
     * @param duration - int, the time cost of the Edge
     */
    public Edge(Station dest, int duration)
    {
        eDestination = dest;
        eDuration = duration;
    }

    /** getDestination()
     *
     * @return - Station, Destination station
     */
    public Station getDestination()
    {
        return eDestination;
    }

    /** getDuration()
     *
     * @return - int, Time cost of the edge
     */
    public int getDuration()
    {
        return eDuration;
    }

    @Override
    public String toString()
    {
        return eDestination.getName() + " " + eDuration + "\n";
    }
}
