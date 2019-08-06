import classes.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

/** assign1.java
 *
 * COMP2230 Algorithms Assignment
 *
 * Author: Gregory Choice
 * Student#: c9311718
 * Date created: 10/10/2018
 * Date modified: 26/10/2018
 *
 * assign1 displays the shortest time cost path between two Stations (specified at the command line)
 * on a simulated rail network
 *
 * Based on Dijkstra's Algorithm - Using a priority queue
 * from https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Using_a_priority_queue
 * and modified to use a custom indirect minheap.
 *
 * XML parsing code based on
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
 *
 */
public class assign1
{
    /** addStations()
     *
     * Creates an ArrayList of stations with Edge adjacencies
     *
     * @param filename - String, filename from command line arguments
     * @return - ArrayList<Station>, Network of Stations
     */
    private ArrayList<Station> addStations(String filename)
    {
        ArrayList<Station> network = null;

        try
        {
            // Create input file object
            File inputFile = new File(filename);

            // Build DOM document object from XML input
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

            // Generate list of nodes from DOM document
            NodeList stations = document.getElementsByTagName("Station");

            // Create network as a Java object
            network = new ArrayList<>();

            // Populate Station objects from DOM document and add to the network
            for(int i = 0; i < stations.getLength(); i++)
            {
                Node sNode = stations.item(i);

                if(sNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element station = (Element)sNode;

                    String sName = station.getElementsByTagName("Name").item(0).getTextContent();
                    String sLine = station.getElementsByTagName("Line").item(0).getTextContent();

                    // Add station to the network
                    network.add(new Station(sName, sLine));
                }
            }

            // Create adjacency lists for each Station object
            for(int i = 0; i < network.size(); i++)
            {
                Node sNode = stations.item(i);
                Station currentStation = network.get(i);
                if(sNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element station = (Element)sNode;

                    NodeList edgeList = station.getElementsByTagName("StationEdge");

                    // Populate Edge objects from DOM document and add to station adjacency list
                    for(int j = 0; j < edgeList.getLength(); j++)
                    {
                        Node eNode = edgeList.item(j);

                        if(eNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element edge = (Element)eNode;

                            String eName = edge.getElementsByTagName("Name").item(0).getTextContent();
                            String eLine = edge.getElementsByTagName("Line").item(0).getTextContent();
                            int eDuration = Integer.parseInt(edge.getElementsByTagName("Duration").item(0).getTextContent());

                            // Get reference to next Station object
                            Station nextStation = network.get(network.indexOf(new Station(eName, eLine)));

                            // Add edge to the adjacency list of the current station
                            currentStation.addEdge(new Edge(nextStation, eDuration));
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        return network;
    }

    /** getPaths()
     *
     * Calculates minimum time costs from the source station to all other stations
     *
     * @param source - String, Name of the starting station
     * @param network - ArrayList<Station>, the list of stations available
     */
    private void getPaths(String source, String destination, ArrayList<Station> network)
    {
        // Select all source station lines and add to a list
        ArrayList<Station> sourceLines = new ArrayList<>();
        for(Station station : network)
        {
            if(source.equals(station.getName()))
                sourceLines.add(station);
        }

        // Calculate shortest paths from each source station line to all stations
        for(Station sourceStation : sourceLines)
        {
            // Instantiate min-heap
            MinHeap nextStations = new MinHeap();

            sourceStation.setTimeCost(0);
            nextStations.add(sourceStation);

            while(!nextStations.isEmpty())
            {
                // Get station on the top of the heap
                Station currentStation = nextStations.poll();

                // Check if the cost to the next station is smaller than the previously visited cost
                for(Edge edge : currentStation.getEdges())
                {
                    Station nextStation = edge.getDestination();
                    double minCost = currentStation.getTimeCost() + edge.getDuration();

                    // Update the next station if the cost is smaller and it is not the source station
                    if(minCost < nextStation.getTimeCost() && !nextStation.getName().equals(source))
                    {
                        // Update station
                        nextStation.setPrevious(currentStation);
                        nextStation.setTimeCost(minCost);

                        // Add to heap
                        nextStations.add(nextStation);
                    }
                    if(nextStation.getName().equals(destination))
                        break;
                }
            }
        }
    }

    /** getShortest()
     *
     * Chooses the shortest time path from the source station to the destination
     *
     * @param destination - String, the name of the destination station
     * @param network - ArrayList<Station>, the list of stations available
     * @return ArrayList<Station>, the list of stations on the shortest path
     */
    private ArrayList<Station> getShortest(String destination, ArrayList<Station> network)
    {
        // Instantiate min-heap
        MinHeap destinationLines = new MinHeap();

        // Select all lines for destination station, shortest path will be at the head of the queue
        for(Station station : network)
        {
            if(destination.equals((station.getName())))
                destinationLines.add(station);
        }

        // Create list of stations on the shortest path
        ArrayList<Station> shortest = new ArrayList<>();

        // Backtrack the destination with the smallest time cost to the source station.
        for(Station station = destinationLines.poll(); station != null; station = station.getPrevious())
        {
            shortest.add(0, station);
        }

        return shortest;
    }

    /** run()
     *
     * Top level program statements
     *
     * @param args - Command line arguments passed from the main method
     */
    private void run(String[] args)
    {
        // Convert source file to Java objects
        ArrayList<Station> network = addStations(args[0]);

        // Calculate time cost from source to all stations
        getPaths(args[1], args[2], network);

        // Pick shortest path to destination
        ArrayList<Station> shortest = getShortest(args[2], network);

        String line = null;

        // Display path to standard output
        for(Station station : shortest)
        {
            // Display the source station
            if(station.getPrevious() == null)
            {
                line = station.getLine();
                System.out.print("From " + station.getName() + ", take line " + line +" to ");
            }

            // Line change occurred, display details of next segment
            if(!station.getLine().equals(line))
            {
                line = station.getLine();
                System.out.print(station.getPrevious().getName());
                System.out.print(";\nthen change to line " + line + ", and continue to ");
            }

            // Display the destination station
            if(station.getName().equals(args[2]))
            {
                System.out.println(station.getName() + ".");
                System.out.println("The total trip will take approximately " + (int)station.getTimeCost() + " minutes.");
            }
        }
    }

    /** main()
     *
     * @param args - String[], Command line arguments 0(Input file name), 1(Source station), 2(Destination station), 3(criterion - n/a)
     */
    public static void main(String[] args)
    {
        assign1 dijkstra = new assign1();
        dijkstra.run(args);
    }
}
