package org.info6205.tsp.visualization;

import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.Vertex;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;


public class TSPVisualization {
    private Graph tspGraph;
    private edu.uci.ics.jung.graph.Graph<Vertex, Edge> jungGraph;
    VisualizationViewer<Vertex, Edge> vv;
    JFrame frame;

    KKLayout<Vertex, Edge> layout;


    public TSPVisualization(final Graph tspGraph, final int width,  final int height){
        this.tspGraph = tspGraph;
        this.jungGraph = new UndirectedSparseGraph();
        addVerticesToJungGraph(tspGraph, jungGraph);
        addEdgesToJungGraph(tspGraph, jungGraph);
        this.initRenderer(width, height);
    }

    public void initRenderer(final int width,  final int height){
        this.setVisualizationViwer(width, height);
        this.frame = new JFrame("TSP Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        //Code for transform and picking
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String, Number>();
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setVisualizationViwer(final int WIDTH,  final int HEIGHT){
        this.layout= new KKLayout<>(jungGraph);
        this.layout.setMaxIterations(1000);
        this.layout.setSize(new Dimension(WIDTH, HEIGHT));
        this.layout.setInitializer(new RandomLocationTransformer<>(new Dimension(WIDTH, HEIGHT)));
        this.layout.initialize();
        this.vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void setVertexStyles(final int WIDTH, final int HEIGHT, final Color COLOR ){
        //get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();

        //Add labels to the vertices
        Function<Vertex, String> vertexStringer = v -> String.valueOf(v.getId());
        renderContext.setVertexLabelTransformer(vertexStringer);

        //Shape the vertices
        Function<Vertex, Shape> vertexShapeTransformer = vertex -> new Ellipse2D.Float(-5, -5, WIDTH, HEIGHT);
        renderContext.setVertexShapeTransformer(vertexShapeTransformer);

        //Set label position and the default vertex color
        Renderer<Vertex, Edge> renderer = vv.getRenderer();
        renderer.getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.N);
        renderContext.setVertexFillPaintTransformer(v -> COLOR);
    }

    public void setEdgeStyles(final float strokeWidth, final Color color){
        //get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();
        //set edge color and the stroke width
        renderContext.setEdgeDrawPaintTransformer(v -> color);
        renderContext.setEdgeStrokeTransformer(e -> new BasicStroke(strokeWidth));
    }

    private void addEdgesToJungGraph(Graph tspGraph, edu.uci.ics.jung.graph.Graph jungGraph) {
        for (Edge e : tspGraph.getAllEdges()) {
            jungGraph.addEdge(e, e.getSource(), e.getDestination());
        }
    }

    public void addVerticesToJungGraph(Graph tspGraph, edu.uci.ics.jung.graph.Graph jungGraph) {

        for (Vertex v : tspGraph.getAllVertices()) {
            jungGraph.addVertex(v);
        }
    }

    public boolean visualizeMST() {
        //get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();
        List<Edge> mstEdges = new ArrayList<>();
        for (Edge edge : tspGraph.getAllEdges()) {
            mstEdges.add(edge);
        }
        HashSet<Edge> animatedEdges = new HashSet<>();
        final int[] currentEdgeIndex = {0};

        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (currentEdgeIndex[0] < mstEdges.size()) {
                    Edge currentEdge = mstEdges.get(currentEdgeIndex[0]);

                    // Update the edge paint and stroke transformers for the current edge
                    renderContext.setEdgeDrawPaintTransformer(v -> {
                        if (checkIfEdgeExist(v, animatedEdges)) {
                            return new Color(128, 0, 0, 64);
                        } else {
                            if (currentEdge.getWeight() == v.getWeight()) {
                                animatedEdges.add(currentEdge);
                                return new Color(128, 0, 0, 64);
                            }
                        }
                        return null;
                    });
                    renderContext.setEdgeStrokeTransformer(e -> {
                        if (checkIfEdgeExist(e, animatedEdges)) {
                            return new BasicStroke(1.0f);
                        }
                        return null;
                    });

                    // Highlight the current edge
                    vv.repaint();

                    currentEdgeIndex[0]++;
                } else {
                    // Stop the timer once all edges have been animated
                    ((Timer) event.getSource()).stop();
                    System.out.println("Done painting edges");
                }
            }
        });
        timer.start();
        while(timer.isRunning()){
        }
        System.out.println("MST Timer stopped");
        return true;
    }


    private static boolean checkIfEdgeExist(Edge e, HashSet<Edge> edges) {
        for (Edge edge : edges) {
            if (edge.getWeight() == e.getWeight()) {
                return true;
            }
        }
        return false;
    }

    public void hightlightOddDegreeVertices(Set<Vertex> oddDegreeVertices, Color color, int width, int height) {
        Set<Vertex> visitedVertices = new HashSet<>();
        //get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();
        int delay = 1; // in milliseconds
        final int[] index = {0};

        // Create a Timer object to repaint the graph at regular intervals
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If all odd degree vertices have been visited, stop the timer
                if (visitedVertices.size() == oddDegreeVertices.size()) {
                    ((Timer) e.getSource()).stop();
                    return;
                }

                // Get the current vertex to highlight
                Vertex currentVertex = (Vertex) oddDegreeVertices.toArray()[index[0]];

                // Update the vertex fill paint and shape transformers for the current vertex
                renderContext.setVertexFillPaintTransformer(v -> {
                    if (checkIfVertexExists(visitedVertices, v)) {
                        return color;
                    } else {
                        if (v.equals(currentVertex)) {
                            visitedVertices.add(v);
                            return color;
                        }
                    }
                    return Color.black;
                });

                renderContext.setVertexShapeTransformer(v -> {
                    if (checkIfVertexExists(visitedVertices, v)) {
                        return new Ellipse2D.Float(-5, -5, width, height);
                    } else {
                        if (v.equals(currentVertex)) {
                            visitedVertices.add(v);
                            return new Ellipse2D.Float(-5, -5, width, height);
                        }
                    }
                    return new Ellipse2D.Float(-5, -5, 5, 5);
                });

                // Increment the index to highlight the next vertex in the next iteration
                index[0]++;

                // Repaint the graph with the updated render context
                vv.repaint();
            }
        });

        // Start the timer
        timer.start();

        while (timer.isRunning()){}
        System.out.println("Highlighting vertices done");
    }


    public boolean checkIfVertexExists(Collection<Vertex> collection, Vertex vertex){
        for(Vertex v: collection){
            if(vertex.getId() == v.getId())
                return true;
        }
        return false;
    }

    public edu.uci.ics.jung.graph.Graph<Vertex, Edge> highlightEdges(Graph graph, Color color, float strokeWidth) {
        Set<Edge> graphEdges = graph.getAllEdges();
        HashSet<Edge> visitedEdges = new HashSet<>();
        // get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();

        int delay = 1; // delay in milliseconds
        final int[] index = {0};

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (index[0] < graphEdges.size()) {
                    Edge edge = (Edge) graphEdges.toArray()[index[0]++];
                    // Update the edge paint and stroke transformers for the current edge
                    renderContext.setEdgeDrawPaintTransformer(e -> {
                        if (checkIfEdgeExist(e, visitedEdges)) {
                            return color;
                        } else {
                            if (edge.getWeight() == e.getWeight()) {
                                visitedEdges.add(edge);
                                return color;
                            }
                        }
                        return new Color(128, 0, 0, 128);
                    });
                    renderContext.setEdgeStrokeTransformer(e -> {
                        if (checkIfEdgeExist(e, visitedEdges)) {
                            return new BasicStroke(strokeWidth);
                        }
                        return new BasicStroke(1.0f);
                    });

                    // Highlight the current edge
                    vv.repaint();
                } else {
                    ((Timer) evt.getSource()).stop(); // stop the timer
                }
            }
        };

        Timer timer = new Timer(delay, taskPerformer);
        timer.start();

        while (timer.isRunning()){}
        System.out.println("Highlighting edges done");
        return jungGraph;
    }

    public void addNewEdgesToJungGraph(List<Edge> edges){
        try {
            for (Edge edge : edges) {
                Vertex sourceVertex = edge.getSource();
                Vertex destinationVertex = edge.getDestination();
                if(jungGraph.findEdge(sourceVertex, destinationVertex) == null || jungGraph.findEdge(destinationVertex, sourceVertex) == null){
                    System.out.println(sourceVertex.getId()+"-"+destinationVertex.getId());
                }
                jungGraph.addEdge(edge, sourceVertex, destinationVertex);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
