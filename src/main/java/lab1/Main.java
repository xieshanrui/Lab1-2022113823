package lab1;

import java.io.*;
import java.util.*;

public class Main {
    private static DirectedGraph graph;
    private static GraphAlgorithms algorithms;
    private static GraphVisualizer visualizer;
    private static Scanner scanner;

    public static void main(String[] args) {
        graph = new DirectedGraph();
        algorithms = new GraphAlgorithms(graph);
        visualizer = new GraphVisualizer(graph);
        scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            int choice = getChoice();

            try {
                switch (choice) {
                    case 1:
                        loadTextFile();
                        break;
                    case 2:
                        showGraph();
                        break;
                    case 3:
                        queryBridgeWords();
                        break;
                    case 4:
                        generateNewText();
                        break;
                    case 5:
                        findShortestPath();
                        break;
                    case 6:
                        calculatePageRank();
                        break;
                    case 7:
                        performRandomWalk();
                        break;
                    case 0:
                        System.out.println("感谢使用！再见！");
                        scanner.close();
                        return;
                    default:
                        System.out.println("无效的选择，请重试。");
                }
            } catch (Exception e) {
                System.out.println("操作出错：" + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void showMenu() {
        System.out.println("=== 文本图分析系统 ===");
        System.out.println("1. 加载文本文件");
        System.out.println("2. 显示有向图");
        System.out.println("3. 查询桥接词");
        System.out.println("4. 生成新文本");
        System.out.println("5. 查找最短路径");
        System.out.println("6. 计算PageRank值");
        System.out.println("7. 随机游走");
        System.out.println("0. 退出");
        System.out.print("请选择操作 (0-7): ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void loadTextFile() throws IOException {
        System.out.print("请输入文本文件路径：");
        String filePath = scanner.nextLine().trim();
        
        // 处理相对路径和绝对路径
        File file = new File(filePath);
        if (!file.isAbsolute()) {
            // 如果是相对路径，转换为绝对路径
            file = new File(System.getProperty("user.dir"), filePath);
        }
        
        // 检查文件是否存在
        if (!file.exists()) {
            throw new FileNotFoundException("找不到指定的文件：" + filePath);
        }
        
        // 检查文件是否可读
        if (!file.canRead()) {
            throw new IOException("无法读取文件：" + filePath);
        }
        
        try {
            graph.buildFromFile(file.getAbsolutePath());
            System.out.println("文件加载成功！");
        } catch (IOException e) {
            throw new IOException("文件读取错误：" + e.getMessage());
        }
    }

    private static void showGraph() throws IOException, InterruptedException {
        // 在命令行显示
        visualizer.showInCLI();

        // 保存为图片
        System.out.print("请输入图片保存路径（.png格式）：");
        String imagePath = scanner.nextLine();
        visualizer.saveGraphImage(imagePath);
        System.out.println("图片已保存到：" + imagePath);
    }

    private static void queryBridgeWords() {
        System.out.print("请输入第一个单词：");
        String word1 = scanner.nextLine();
        System.out.print("请输入第二个单词：");
        String word2 = scanner.nextLine();

        String result = graph.queryBridgeWords(word1, word2);
        System.out.println(result);
    }

    private static void generateNewText() {
        System.out.println("请输入文本：");
        String inputText = scanner.nextLine();

        String newText = graph.generateNewText(inputText);
        System.out.println("生成的新文本：");
        System.out.println(newText);
    }

    private static void findShortestPath() throws IOException, InterruptedException {
        System.out.print("请输入起始单词（或直接回车只输入一个单词）：");
        String word1 = scanner.nextLine();
        
        if (word1.isEmpty()) {
            System.out.println("请输入单词：");
            word1 = scanner.nextLine();
            // 计算到所有其他单词的最短路径
            for (String word2 : graph.getNodes()) {
                if (!word1.equals(word2)) {
                    String path = algorithms.calcShortestPath(word1, word2);
                    System.out.println("到 '" + word2 + "' 的最短路径：" + path);
                }
            }
        } else {
            System.out.print("请输入目标单词：");
            String word2 = scanner.nextLine();
            String path = algorithms.calcShortestPath(word1, word2);
            System.out.println("最短路径：" + path);

            // 保存带有路径高亮的图片
            System.out.print("请输入路径图片保存路径（.png格式）：");
            String imagePath = scanner.nextLine();
            List<String> pathList = Arrays.asList(path.split("->")); // 简单处理路径字符串
            visualizer.saveGraphImageWithPath(imagePath, pathList);
            System.out.println("带路径的图片已保存到：" + imagePath);
        }
    }

    private static void calculatePageRank() {
        System.out.print("请输入要计算PageRank的单词：");
        String word = scanner.nextLine();

        double pageRank = algorithms.calPageRank(word);
        System.out.printf("'%s' 的PageRank值：%.6f%n", word, pageRank);
    }

    private static void performRandomWalk() throws IOException {
        String path = algorithms.randomWalk();
        System.out.println("随机游走路径：");
        System.out.println(path);

        // 保存路径到文件
        System.out.print("请输入保存路径文件名：");
        String fileName = scanner.nextLine();
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(path);
        }
        System.out.println("路径已保存到文件：" + fileName);
    }
}
// 这是一个无意义的注释（测试git）
// 修改B1