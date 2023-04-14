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
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
        this.layout= new KKLayout(jungGraph);
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

    public boolean visualizeMST() throws InterruptedException {
        //get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();
        List<Edge> mstEdges = new ArrayList<>();
        for (Edge edge : tspGraph.getAllEdges()) {
            mstEdges.add(edge);
        }
        HashSet<Edge> animatedEdges = new HashSet<>();
        for (Edge edge : mstEdges) {
            // Update the edge paint and stroke transformers for the current edge
            renderContext.setEdgeDrawPaintTransformer(v -> {
                if (checkIfEdgeExist(v, animatedEdges)) {
                    return new Color(128, 0, 0, 64);
                } else {
                    if (edge.getWeight() == v.getWeight()) {
                        animatedEdges.add(edge);
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

            // Wait for a short period of time to create the simulation effect
            Thread.sleep(20);
            //System.out.println("Painting edges");

        }

        // Reset the edge paint and stroke transformers to the default
        renderContext.setEdgeDrawPaintTransformer(v -> new Color(128, 0, 0, 64));
        renderContext.setEdgeStrokeTransformer(e -> new BasicStroke(1.0f));
        System.out.println("Done painting edges");

        while(!layout.done()){
            System.out.println("Organizing layout");
        }
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

    public void hightlightOddDegreeVertices(Set<Vertex> oddDegreeVertices, Color color, int width, int height) throws InterruptedException {
        Set<Vertex> visitedVertices= new HashSet<>();
        //get render context from the visualization viewer
        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();
        for(Vertex currentVertex: oddDegreeVertices){
            renderContext.setVertexFillPaintTransformer(v -> {
                if(checkIfVertexExists(visitedVertices, v)){
                    return color;
                } else{
                    if(checkIfVertexExists(oddDegreeVertices, v)){
                        visitedVertices.add(v);
                        return color;
                    }
                }
                return Color.black;
            });

            renderContext.setVertexShapeTransformer(v -> {
                if(checkIfVertexExists(visitedVertices, v)){
                    return new Ellipse2D.Float(-5, -5, width, height);
                } else{
                    if(checkIfVertexExists(oddDegreeVertices, v)){
                        visitedVertices.add(v);
                        return new Ellipse2D.Float(-5, -5, width, height);
                    }
                }
                return new Ellipse2D.Float(-5, -5, 5, 5);
            });
            vv.repaint();
            Thread.sleep(10);
        }
    }

    public boolean checkIfVertexExists(Collection<Vertex> collection, Vertex vertex){
        for(Vertex v: collection){
            if(vertex.getId() == v.getId())
                return true;
        }
        return false;
    }
}
