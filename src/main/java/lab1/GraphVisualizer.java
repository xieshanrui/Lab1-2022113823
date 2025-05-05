package lab1;

import java.io.*;
import java.util.*;

public class GraphVisualizer {
    private DirectedGraph graph;
    private static final String DOT_PATH = "dot"; // 确保系统已安装GraphViz并将dot添加到环境变量

    public GraphVisualizer(DirectedGraph graph) {
        this.graph = graph;
    }

    // 生成DOT语言描述的图结构
    private String generateDotString() {
        StringBuilder dot = new StringBuilder("digraph G {\n");
        dot.append("    node [shape=circle];\n");

        // 添加边
        for (String from : graph.getNodes()) {
            Map<String, Integer> edges = graph.getOutEdges(from);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String to = edge.getKey();
                int weight = edge.getValue();
                dot.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\"];\n", 
                    from, to, weight));
            }
        }

        dot.append("}");
        return dot.toString();
    }

    // 生成高亮路径的DOT描述
    private String generateDotStringWithPath(List<String> path) {
        StringBuilder dot = new StringBuilder("digraph G {\n");
        dot.append("    node [shape=circle];\n");

        // 添加所有边
        for (String from : graph.getNodes()) {
            Map<String, Integer> edges = graph.getOutEdges(from);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String to = edge.getKey();
                int weight = edge.getValue();
                boolean isInPath = false;

                // 检查边是否在路径中
                for (int i = 0; i < path.size() - 1; i++) {
                    if (path.get(i).equals(from) && path.get(i + 1).equals(to)) {
                        isInPath = true;
                        break;
                    }
                }

                // 为路径中的边添加特殊样式
                if (isInPath) {
                    dot.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\", color=red, penwidth=2.0];\n",
                        from, to, weight));
                } else {
                    dot.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\"];\n",
                        from, to, weight));
                }
            }
        }

        dot.append("}");
        return dot.toString();
    }

    // 将图保存为图片文件
    public void saveGraphImage(String outputPath) throws IOException, InterruptedException {
        // 生成DOT文件
        String dotContent = generateDotString();
        File tempDot = File.createTempFile("graph_", ".dot");
        try (PrintWriter writer = new PrintWriter(tempDot)) {
            writer.print(dotContent);
        }

        // 调用GraphViz生成图片
        ProcessBuilder pb = new ProcessBuilder(
            DOT_PATH, "-Tpng", tempDot.getAbsolutePath(), "-o", outputPath
        );
        Process process = pb.start();
        process.waitFor();

        // 清理临时文件
        tempDot.delete();
    }

    // 保存带有高亮路径的图片
    public void saveGraphImageWithPath(String outputPath, List<String> path) 
            throws IOException, InterruptedException {
        String dotContent = generateDotStringWithPath(path);
        File tempDot = File.createTempFile("graph_path_", ".dot");
        try (PrintWriter writer = new PrintWriter(tempDot)) {
            writer.print(dotContent);
        }

        ProcessBuilder pb = new ProcessBuilder(
            DOT_PATH, "-Tpng", tempDot.getAbsolutePath(), "-o", outputPath
        );
        Process process = pb.start();
        process.waitFor();

        tempDot.delete();
    }

    // 在命令行中展示图结构
    public void showInCLI() {
        System.out.println("Directed Graph Structure:");
        System.out.println("----------------------");

        for (String from : graph.getNodes()) {
            System.out.printf("From '%s':\n", from);
            Map<String, Integer> edges = graph.getOutEdges(from);
            if (edges.isEmpty()) {
                System.out.println("  No outgoing edges");
            } else {
                for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                    System.out.printf("  -> '%s' (weight: %d)\n", 
                        edge.getKey(), edge.getValue());
                }
            }
        }
        System.out.println("----------------------");
    }
}
<<<<<<< HEAD
//无意义的修改1
=======
// B2
>>>>>>> B2
