package io.github.cszach.Trailblazer;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import io.github.cszach.Trailblazer.geo.Intersection;
import io.github.cszach.Trailblazer.geo.Road;
import io.github.cszach.Trailblazer.geo.Geography;
import io.github.cszach.Trailblazer.gui.MapPanel;
import io.github.cszach.Trailblazer.gui.AppWindow;
import io.github.cszach.Trailblazer.projection.WebMercatorProjection;

/** The main application */
public class App {
  /**
   * Reads command line arguments and starts the program accordingly.
   *
   * @param args an array of command line arguments. Accepted options are
   *        <ul>
   *        <li>{@code <MAP>}: the path of the map data file, which must be the first argument. The
   *        other options can be specified in any order;
   *        <li>{@code --show}: display the map in GUI mode;
   *        <li>{@code --directions <INTERSECTION1> <INTERSECTION2>}: print the intersections that
   *        form the shortest path between {@code <INTERSECTION1>} and {@code <INTERSECTION2>},
   *        which is indicated on the GUI if {@code --show} is present;
   *        <li>{@code --debug}: turn on debugging mode, which draws a bounding box around the map
   *        on the GUI panel.
   *        </ul>
   */
  public static void main(String[] args) {
    Geography geo = new Geography();

    try {
      geo.importFromFile(args[0]);
    } catch (ArrayIndexOutOfBoundsException exception) {
      System.err.println("Invalid command line arguments");
      System.exit(ExitCodes.INVALID_COMMAND_LINE_ARGUMENTS);
    } catch (FileNotFoundException exception) {
      exception.printStackTrace();
      System.exit(ExitCodes.FILE_NOT_FOUND);
    } catch (NoSuchElementException exception) {
      exception.printStackTrace();
      System.exit(ExitCodes.INVALID_FILE_FORMAT);
    }

    // Options' values
    boolean show = false;
    String startIntersectionId = null;
    String endIntersectionId = null;
    boolean debugging = false;

    // Parse command line arguments

    for (int i = 1; i < args.length; i++) {
      switch (args[i]) {
        case "--show":
          show = true;

          break;

        case "--directions":
          try {
            startIntersectionId = args[++i];
            endIntersectionId = args[++i];
          } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.println("Invalid command line arguments");
            System.exit(ExitCodes.INVALID_COMMAND_LINE_ARGUMENTS);
          }

          break;

        case "--debug":
          debugging = true;

          break;
      }
    }

    // If --directions is specified, find the shortest path between the given
    // intersections and print the intersections of the path in order.

    if (startIntersectionId != null) {
      Intersection startIntersection = geo.getIntersection(startIntersectionId);
      Intersection endIntersection = geo.getIntersection(endIntersectionId);

      LinkedList<Road> shortestPath = geo.findShortestPath(startIntersection, endIntersection);

      Intersection currentIntersection = startIntersection;
      System.out.println(currentIntersection.getId());

      if (shortestPath.size() == 0) {
        System.out.println("No path connects " + startIntersectionId + " and " + endIntersectionId);
      } else {
        System.out.println("Going from " + startIntersectionId + " to " + endIntersectionId);

        double totalMiles = 0.0;

        for (Road road : shortestPath) {
          Intersection nextIntersection = road.getTheOtherEnd(currentIntersection);
          System.out.println(nextIntersection.getId());
          totalMiles += road.getDistance();
          currentIntersection = nextIntersection;
        }

        System.out.println("Total miles travelled: " + totalMiles);
      }
    }

    // Display GUI if --show is present

    if (show) {
      AppWindow window = new AppWindow("Street Mapping", 1280, 800);
      MapPanel map =
          new MapPanel(geo, new WebMercatorProjection(window.getWidth(), window.getHeight(), 0));

      window.add(map);
      window.display();

      map.setDebugging(debugging);
      map.project();
      map.resetView();
    }
  }
}
