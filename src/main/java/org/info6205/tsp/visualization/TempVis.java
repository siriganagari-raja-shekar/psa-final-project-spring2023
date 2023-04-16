package org.info6205.tsp.visualization;


import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.layout.CachingLayout;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.info6205.tsp.algorithm.MinimumSpanningTree;
import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashSet;

public class TempVis {

    public static void main(String[] args) throws Exception {
        Preprocess preprocess = new Preprocess();
        org.info6205.tsp.core.Graph tspGraph = preprocess.start("teamproject.csv");

        Graph<Vertex, Edge> jungGraph= new UndirectedSparseGraph();
        addVerticesToJungGraph(tspGraph, jungGraph);
        addEdgesToJungGraph(tspGraph, jungGraph);

        //Minimum weight spanning tree
        org.info6205.tsp.core.Graph mstGraph= new MinimumSpanningTree(tspGraph).getMinimumSpanningTree();
        Graph<Vertex, Edge> jungMSTGraph= new UndirectedSparseGraph();
        System.out.println(mstGraph.getAllEdges().size());
        addVerticesToJungGraph(mstGraph, jungMSTGraph);
        addEdgesToJungGraph(mstGraph, jungMSTGraph);

        // Create a JUNG visualization of your graph and render it
        Layout<Vertex, Edge> layout= new KKLayout<>(jungMSTGraph);
        layout.setSize(new Dimension(1900*2, 1000*2));
        //layout.setSize(new Dimension(1900, 1000));
        VisualizationViewer<Vertex, Edge> vv = new VisualizationViewer<>(layout);

        // Set the preferred size of the viewer
        vv.setPreferredSize(new Dimension(1900, 1000));

        // Set the vertex shape as a small circle and display the vertex label above it
        Function<Vertex, String> vertexStringer = new Function<Vertex, String>() {
            @Override
            public String apply(Vertex v) {
                return String.valueOf(v.getId());
            }
        };
        Function<Vertex, Shape> vertexShapeTransformer = new Function<Vertex, Shape>() {
            @Override
            public Shape apply(Vertex vertex) {
                return new Ellipse2D.Float(-5, -5, 10, 10);
            }
        };

        RenderContext<Vertex, Edge> renderContext = vv.getRenderContext();
        renderContext.setVertexLabelTransformer(vertexStringer);
        renderContext.setVertexShapeTransformer(vertexShapeTransformer);

        Renderer<Vertex, Edge> renderer = vv.getRenderer();
        renderer.getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.N);

        renderContext.setVertexFillPaintTransformer( v -> Color.black);
        //Color edges
        renderContext.setEdgeDrawPaintTransformer( v -> Color.black);

        renderContext.setEdgeStrokeTransformer( e -> new BasicStroke(2.0f));


        // Create a list of the edges in the MST in the order that they are added to the MST
        java.util.List<Edge> mstEdges = new ArrayList<>();
        for (Edge edge : mstGraph.getAllEdges()) {
            mstEdges.add(edge);
        }

        // Create a new JFrame to display the viewer
        JFrame frame = new JFrame("JUNG Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        //Code for transform and picking
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String,Number>();
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());

        frame.pack();
        frame.setLocationRelativeTo(null); // Centers the frame on the screen
        frame.setVisible(true);

        // Write a loop that iterates over the list of edges in the MST and highlights each edge in turn
        HashSet<Edge> animatedEdges= new HashSet<>();
        for (Edge edge : mstEdges) {
            // Highlight the current edge
            renderContext.setEdgeDrawPaintTransformer((Function<Edge, Paint>) v -> {
                return Color.gray;
            });
            renderContext.setEdgeStrokeTransformer((Function<Edge, Stroke>) e -> {
                return new BasicStroke(1.0f);
            });

            // Wait for a short period of time to create the simulation effect
            vv.repaint();
            //Thread.sleep(50);
        }

        // Reset the edge stroke transformer to the default
        //vv.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer<>(new BasicStroke()));
    }

    private static boolean checkIfEdgeExist(Edge e, HashSet<Edge> edges) {
        for(Edge edge: edges){
            if(edge.getWeight() == e.getWeight()) {
                return true;
            }
        }
        return false;
    }

    private static void addEdgesToJungGraph(org.info6205.tsp.core.Graph tspGraph, Graph jungGraph) {
        for(Edge e: tspGraph.getAllEdges()){
            jungGraph.addEdge(e, e.getSource(), e.getDestination());
        }
    }

    public static void addVerticesToJungGraph(org.info6205.tsp.core.Graph tspGraph, Graph jungGraph){
        for(Vertex v: tspGraph.getAllVertices()){
            jungGraph.addVertex(v);
        }
    }
}
