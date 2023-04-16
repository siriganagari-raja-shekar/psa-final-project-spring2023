//package org.info6205.tsp.util;
//
//import com.google.common.base.Function;
//import com.google.common.base.Functions;
//import edu.uci.ics.jung.algorithms.layout.KKLayout;
//import edu.uci.ics.jung.graph.DirectedSparseGraph;
//import edu.uci.ics.jung.graph.util.EdgeType;
//import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
//import edu.uci.ics.jung.visualization.Layer;
//import edu.uci.ics.jung.visualization.VisualizationViewer;
//import edu.uci.ics.jung.visualization.control.*;
//import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
//import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
//import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
//import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
//import edu.uci.ics.jung.visualization.renderers.Renderer;
//
//import javax.swing.*;
//import javax.xml.transform.Transformer;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.geom.Ellipse2D;
//
//public class JUNGMouseZoom {
//    /**
//     * the graph
//     */
//    DirectedSparseGraph<String, Number> graph;
//
//    /**
//     * the visual component and renderer for the graph
//     */
//    VisualizationViewer<String, Number> vv;
//
//    /**
//     * create an instance of a simple graph with controls to
//     * demo the zoom features.
//     *
//     */
//    public JUNGMouseZoom() {
//
//        // create a simple graph for the demo
//        graph = new DirectedSparseGraph<String, Number>();
//        String[] v = createVertices(10);
//        createEdges(v);
//
//        //Create visualization viwer and add the layout with the graph
//        vv = new VisualizationViewer<>(new KKLayout<>(graph));
//
//        vv.getRenderer().setVertexRenderer(new BasicVertexRenderer<>());
//        vv.getRenderContext().setEdgeDrawPaintTransformer(Functions.constant(Color.blue));
//        vv.getRenderContext().setArrowFillPaintTransformer(Functions.constant(Color.red));
//        vv.getRenderContext().setArrowDrawPaintTransformer(Functions.constant(Color.green));
//
//        // add my listeners for ToolTips
//        vv.setVertexToolTipTransformer(new ToStringLabeller());
//        vv.setEdgeToolTipTransformer(new Function<>() {
//            public String apply(Number edge) {
//                return "E" + graph.getEndpoints(edge).toString();
//            }
//        });
//
//        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//        vv.getRenderer().getVertexLabelRenderer().setPositioner(new BasicVertexLabelRenderer.InsidePositioner());
//        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);
//        vv.setForeground(Color.black);
//
//        // create a frame to hold the graph
//        final JFrame frame = new JFrame();
//        Container content = frame.getContentPane();
//
//        //Graph with zoom panel
//        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
//
//        //Add the zoom graph panel to the Jframe container
//        content.add(panel);
//
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Code for transform and picking
//        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String,Number>();
//        vv.setGraphMouse(graphMouse);
//        vv.addKeyListener(graphMouse.getModeKeyListener());
//
//        //vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");
//
//        //Add + and - for buttons for zooming
//        final ScalingControl scaler = new CrossoverScalingControl();
//        JButton plus = new JButton("+");
//        plus.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scaler.scale(vv, 1.1f, vv.getCenter());
//            }
//        });
//        JButton minus = new JButton("-");
//        minus.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scaler.scale(vv, 1/1.1f, vv.getCenter());
//            }
//        });
//
//        //Add JButton to reset the zoom and add event listener
//        JButton reset = new JButton("reset");
//        reset.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
//                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
//            }
//        });
//
//        //Add the controls to the JPanel
//        JPanel controls = new JPanel();
//        controls.add(plus);
//        controls.add(minus);
//        controls.add(reset);
//        content.add(controls, BorderLayout.SOUTH);
//
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//    /**
//     * create some vertices
//     * @param count how many to create
//     * @return the Vertices in an array
//     */
//    private String[] createVertices(int count) {
//        String[] v = new String[count];
//        for (int i = 0; i < count; i++) {
//            v[i] = "V"+i;
//            graph.addVertex(v[i]);
//        }
//        return v;
//    }
//
//    /**
//     * create edges for this demo graph
//     * @param v an array of Vertices to connect
//     */
//    void createEdges(String[] v) {
//        graph.addEdge(Double.valueOf(Math.random()), v[0], v[1], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[0], v[3], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[0], v[4], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[4], v[5], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[3], v[5], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[1], v[2], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[1], v[4], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[8], v[2], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[3], v[8], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[6], v[7], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[7], v[5], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[0], v[9], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[9], v[8], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[7], v[6], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[6], v[5], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[4], v[2], EdgeType.DIRECTED);
//        graph.addEdge(Double.valueOf(Math.random()), v[5], v[4], EdgeType.DIRECTED);
//    }
//
//    /**
//     * A nested class to demo the GraphMouseListener finding the
//     * right vertices after zoom/pan
//     */
//    static class TestGraphMouseListener<V> implements GraphMouseListener<V> {
//
//        public void graphClicked(V v, MouseEvent me) {
//            System.err.println("Vertex "+v+" was clicked at ("+me.getX()+","+me.getY()+")");
//        }
//        public void graphPressed(V v, MouseEvent me) {
//            System.err.println("Vertex "+v+" was pressed at ("+me.getX()+","+me.getY()+")");
//        }
//        public void graphReleased(V v, MouseEvent me) {
//            System.err.println("Vertex "+v+" was released at ("+me.getX()+","+me.getY()+")");
//        }
//    }
//
//    public static void main(String[] args)
//    {
//        new JUNGMouseZoom();
//    }
//}
