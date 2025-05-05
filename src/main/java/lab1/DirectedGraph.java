package lab1;

import java.util.*;
import java.io.*;

public class DirectedGraph {
    // 图的邻接表表示
    private Map<String, Map<String, Integer>> graph;
    private Random random;

    public DirectedGraph() {
        graph = new HashMap<>();
        random = new Random();
    }

    // 添加边或更新权重
    public void addEdge(String from, String to) {
        from = from.toLowerCase();
        to = to.toLowerCase();
        
        graph.putIfAbsent(from, new HashMap<>());
        graph.putIfAbsent(to, new HashMap<>());
        
        Map<String, Integer> edges = graph.get(from);
        edges.put(to, edges.getOrDefault(to, 0) + 1);
    }

    // 从文本文件构建图
    public void buildFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String previousWord = null;
            
            while ((line = reader.readLine()) != null) {
                // 将所有非字母字符替换为空格
                line = line.replaceAll("[^a-zA-Z\\s]", " ");
                String[] words = line.trim().split("\\s+");
                
                for (String word : words) {
                    if (word.isEmpty()) continue;
                    
                    if (previousWord != null) {
                        addEdge(previousWord, word);
                    }
                    previousWord = word;
                }
            }
        }
    }

    // 查询桥接词
    public String queryBridgeWords(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }

        List<String> bridges = new ArrayList<>();
        Map<String, Integer> neighbors1 = graph.get(word1);

        for (String bridge : neighbors1.keySet()) {
            if (graph.containsKey(bridge) && graph.get(bridge).containsKey(word2)) {
                bridges.add(bridge);
            }
        }

        if (bridges.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        }

        StringBuilder result = new StringBuilder("The bridge words from " + word1 + " to " + word2 + " are: ");
        for (int i = 0; i < bridges.size(); i++) {
            if (i > 0) {
                result.append(i == bridges.size() - 1 ? " and " : ", ");
            }
            result.append(bridges.get(i));
        }
        result.append(".");
        return result.toString();
    }

    // 生成新文本
    public String generateNewText(String inputText) {
        String[] words = inputText.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            result.append(words[i]);

            if (i < words.length - 1) {
                String word1 = words[i].toLowerCase();
                String word2 = words[i + 1].toLowerCase();
                List<String> bridges = new ArrayList<>();

                if (graph.containsKey(word1) && graph.containsKey(word2)) {
                    for (String bridge : graph.get(word1).keySet()) {
                        if (graph.containsKey(bridge) && graph.get(bridge).containsKey(word2)) {
                            bridges.add(bridge);
                        }
                    }
                }

                if (!bridges.isEmpty()) {
                    String bridge = bridges.get(random.nextInt(bridges.size()));
                    result.append(" ").append(bridge);
                }
            }
            result.append(" ");
        }

        return result.toString().trim();
    }

    // 获取图的节点集合
    public Set<String> getNodes() {
        return new HashSet<>(graph.keySet());
    }

    // 获取边的权重
    public int getWeight(String from, String to) {
        if (graph.containsKey(from) && graph.get(from).containsKey(to)) {
            return graph.get(from).get(to);
        }
        return 0;
    }

    // 获取节点的出边
    public Map<String, Integer> getOutEdges(String node) {
        return graph.getOrDefault(node, new HashMap<>());
    }
}
