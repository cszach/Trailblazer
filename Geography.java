import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;

/** A class that stores intersections and roads data that forms a geography. */
public class Geography {
  /**
   * The intersections in this geography, which is stored in a {@Map} that maps from an intersection
   * ID to its corresponding intersection object.
   */
  private Map<String, Intersection> intersections;
  /** The list of roads in this geography. */
  private ArrayList<Road> roads;

  /** Initializes an empty geography with no intersections and roads. */
  public Geography() {
    this.intersections = new HashMap<>();
    this.roads = new ArrayList<>();
  }

  /**
   * Returns the intersections data of this geography.
   *
   * @return the intersections data of this geography.
   */
  public Collection<Intersection> getIntersections() {
    return this.intersections.values();
  }

  /**
   * Returns the roads data of this geography.
   *
   * @return the roads data of this geography.
   */
  public Collection<Road> getRoads() {
    return this.roads;
  }

  /**
   * Returns an intersection in this geography.
   *
   * @param intersectionId the ID of the intersection.
   * @return the intersection that is identified by the specified ID, or {@code null} if such an
   *     intersection does not exist in this geography.
   */
  public Intersection getIntersection(String intersectionId) {
    return this.intersections.get(intersectionId);
  }

  /**
   * Returns a road in this geography.
   *
   * @param roadId the ID of the road.
   * @return the road that is identified by the specified ID, or {@code null} if such a road does
   *     not exist in this geography.
   */
  public Road getRoad(String roadId) {
    for (Road road : this.roads) {
      if (road.getId().equals(roadId)) {
        return road;
      }
    }

    return null;
  }

  /**
   * Imports data about a geography from a file.
   *
   * <p>The file format is hard-coded and assumed to be as follows:
   *
   * <ul>
   *   <li>The file consists of multiple entries separated by whitespace (but preferably a newline
   *       character);
   *   <li>An entry has cells that are delimited by whitespace;
   *   <li>If the first cell of an entry is {@code i}, the entry describes an intersection, and the
   *       next three cells describe the ID, the latitude, and the longtitude of the intersection,
   *       respectively. The ID is a string with no whitespace, and the latitude and longtitude are
   *       decimal values in degrees;
   *   <li>If the first cell of an entry is {@code r}, the entry describes a road, and the next
   *       three cells describe the ID of the road and the IDs of the two intersections that it
   *       connects, respectively. All are string with no whitespace;
   *   <li>No entry can start with neither {@code i} nor {@code r};
   *   <li>There should be no redundant cells; that is, each entry has exactly four cells.
   * </ul>
   *
   * @param path the path of the file to read from.
   * @throws FileNotFoundException when the specified file does not exist.
   * @throws NoSuchElementException when there is a trouble reading the file, which the program
   *     assumes to be due to an invalid file format.
   */
  public void importFromFile(String path) throws FileNotFoundException, NoSuchElementException {
    File file = new File(path);
    Scanner scanner = null;

    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException exception) {
      throw exception;
    }

    while (scanner.hasNext()) {
      String type = scanner.next();

      switch (type) {
        case "i":
          String id;
          double latitude;
          double longtitude;

          try {
            id = scanner.next();
            latitude = scanner.nextDouble();
            longtitude = scanner.nextDouble();
          } catch (NoSuchElementException exception) {
            throw new NoSuchElementException("Invalid or broken file format", exception.getCause());
          }

          this.intersections.put(id, new Intersection(id, latitude, longtitude));

          break;

        case "r":
          String intersection1Id;
          String intersection2Id;

          try {
            id = scanner.next();
            intersection1Id = scanner.next();
            intersection2Id = scanner.next();
          } catch (NoSuchElementException exception) {
            throw new NoSuchElementException("Invalid or broken file format", exception.getCause());
          }

          Intersection intersection1 = this.intersections.get(intersection1Id);
          Intersection intersection2 = this.intersections.get(intersection2Id);

          Road road = new Road(id, intersection1, intersection2);

          this.roads.add(road);

          intersection1.getNeighbors().put(intersection2, road);
          intersection2.getNeighbors().put(intersection1, road);

          break;

        default:
          throw new NoSuchElementException("Invalid or broken file format");
      }
    }

    scanner.close();
  }

  /**
   * Finds the shortest path between two given intersections using Dijkstra's algorithm.
   *
   * @param startIntersection the start intersection.
   * @param endIntersection the end intersection.
   * @return a (linked) list of roads that form the shortest path between the start intersection and
   *     the end, in the order that they should be taken starting from the start intersection.
   */
  public LinkedList<Road> findShortestPath(
      Intersection startIntersection, Intersection endIntersection) {
    PriorityQueue<Intersection> q =
        new PriorityQueue<>(
            this.intersections.size(),
            new Comparator<Intersection>() {
              @Override
              public int compare(Intersection intersection1, Intersection intersection2) {
                double diff = intersection1.getDistance() - intersection2.getDistance();

                if (diff > 0) {
                  return 1;
                } else if (diff < 0) {
                  return -1;
                } else {
                  return 0;
                }
              }
            });

    Collection<Intersection> intersections = this.intersections.values();
    intersections.remove(startIntersection);

    for (Intersection intersection : intersections) {
      intersection.setDistance(Double.MAX_VALUE);
      intersection.setPrev(null);
      // q.add(intersection);
    }

    startIntersection.setDistance(0);
    q.add(startIntersection);

    while (!q.isEmpty()) {
      Intersection min = q.poll();
      if (min.equals(endIntersection)) {
        break;
      }

      for (Map.Entry<Intersection, Road> neighbor : min.getNeighbors().entrySet()) {
        Intersection neighborIntersection = neighbor.getKey();
        Road neighborRoad = neighbor.getValue();

        double temp = min.getDistance() + neighborRoad.getDistance();

        if (temp < neighborIntersection.getDistance()) {
          // q.remove(neighborIntersection);
          neighborIntersection.setDistance(temp);
          neighborIntersection.setPrev(neighborRoad);

          q.add(neighborIntersection);
        }
      }
    }

    Intersection currentIntersection = endIntersection;
    LinkedList<Road> shortestPath = new LinkedList<>();

    while (currentIntersection.getPrev() != null) {
      shortestPath.addFirst(currentIntersection.getPrev());
      currentIntersection.getPrev().setIsShortestRoad(true);
      currentIntersection = currentIntersection.getPrev().getTheOtherEnd(currentIntersection);
    }

    return shortestPath;
  }
}
