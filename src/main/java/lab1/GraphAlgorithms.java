package lab1;

import java.util.*;
import java.io.*;

public class GraphAlgorithms {
    private DirectedGraph graph;
    private static final double DAMPING_FACTOR = 0.85; // PageRank阻尼因子

    public GraphAlgorithms(DirectedGraph graph) {
        this.graph = graph;
    }

    // 计算最短路径（Dijkstra算法）
    public String calcShortestPath(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        if (!graph.getNodes().contains(word1) || !graph.getNodes().contains(word2)) {
            return "One or both words are not in the graph!";
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
            (a, b) -> distances.getOrDefault(a, Integer.MAX_VALUE) - 
                      distances.getOrDefault(b, Integer.MAX_VALUE));

        // 初始化距离
        for (String node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(word1, 0);
        queue.offer(word1);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(word2)) break;

            for (Map.Entry<String, Integer> edge : graph.getOutEdges(current).entrySet()) {
                String neighbor = edge.getKey();
                int newDist = distances.get(current) + edge.getValue();

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    queue.remove(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        if (!previous.containsKey(word2)) {
            return "No path exists between " + word1 + " and " + word2;
        }

        // 构建路径
        List<String> path = new ArrayList<>();
        String current = word2;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) result.append("->");
            result.append(path.get(i));
        }
        result.append("\nPath length: ").append(distances.get(word2));

        return result.toString();
    }

    // 计算PageRank值
    public double calPageRank(String word) {
        word = word.toLowerCase();
        if (!graph.getNodes().contains(word)) {
            return 0.0;
        }

        int N = graph.getNodes().size();
        Map<String, Double> pageRank = new HashMap<>();
        Map<String, Double> newPageRank = new HashMap<>();

        // 初始化PageRank值
        for (String node : graph.getNodes()) {
            pageRank.put(node, 1.0 / N);
        }

        // 迭代计算PageRank（执行20次迭代）
        for (int i = 0; i < 20; i++) {
            for (String node : graph.getNodes()) {
                double sum = 0.0;
                // 查找所有指向当前节点的节点
                for (String inNode : graph.getNodes()) {
                    if (graph.getWeight(inNode, node) > 0) {
                        int outDegree = graph.getOutEdges(inNode).size();
                        sum += pageRank.get(inNode) / outDegree;
                    } else if (graph.getOutEdges(inNode).isEmpty()) {
                        // 处理出度为0的节点，将其PR值均匀分配给所有节点
                        sum += pageRank.get(inNode) / N;
                    }
                }
                newPageRank.put(node, (1 - DAMPING_FACTOR) / N + DAMPING_FACTOR * sum);
            }
            pageRank = new HashMap<>(newPageRank);
        }

        return pageRank.get(word);
    }

    // 随机游走
    public String randomWalk() {
        List<String> nodes = new ArrayList<>(graph.getNodes());
        if (nodes.isEmpty()) {
            return "Graph is empty";
        }

        Random random = new Random();
        String currentNode = nodes.get(random.nextInt(nodes.size()));
        StringBuilder path = new StringBuilder(currentNode);
        Set<String> visitedEdges = new HashSet<>();

        while (true) {
            Map<String, Integer> outEdges = graph.getOutEdges(currentNode);
            if (outEdges.isEmpty()) {
                path.append("\nStopped at node with no out edges");
                break;
            }

            List<String> neighbors = new ArrayList<>(outEdges.keySet());
            String nextNode = neighbors.get(random.nextInt(neighbors.size()));
            String edge = currentNode + "->" + nextNode;

            if (visitedEdges.contains(edge)) {
                path.append("\nStopped at repeated edge: ").append(edge);
                break;
            }

            visitedEdges.add(edge);
            path.append("->" + nextNode);
            currentNode = nextNode;
        }

        return path.toString();
    }
}
// B2 modify 1
// B2 modify 2