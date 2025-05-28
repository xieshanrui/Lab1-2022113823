import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import cn.edu.hit.DirectedGraph;

public class DirectedGraphTest {
    private DirectedGraph graph;

    @Before
    public void setUp() {
        graph = new DirectedGraph();
        // 构建测试图
        // hello -> world -> there
        // hello -> hit
        // world -> hello
        graph.addEdge("hello", "world");
        graph.addEdge("hello", "hit");
        graph.addEdge("world", "there");
        graph.addEdge("world", "hello");
    }

    @Test
    public void testQueryBridgeWords_BothWordsExistNoBridge() {
        String result = graph.queryBridgeWords("hello", "world");
        assertEquals("No bridge words from hello to world!", result);
    }

    @Test
    public void testQueryBridgeWords_Word1NotExist() {
        String result = graph.queryBridgeWords("nonexist", "world");
        assertEquals("No nonexist or world in the graph!", result);
    }

    @Test
    public void testQueryBridgeWords_Word2NotExist() {
        String result = graph.queryBridgeWords("hello", "nonexist");
        assertEquals("No hello or nonexist in the graph!", result);
    }

    @Test
    public void testQueryBridgeWords_BridgeExists() {
        // hello -> world -> there
        String result = graph.queryBridgeWords("hello", "there");
        assertEquals("The bridge words from hello to there are: world.", result);
    }

    @Test
    public void testQueryBridgeWords_SameWord() {
        String result = graph.queryBridgeWords("hello", "hello");
        assertEquals("No bridge words from hello to hello!", result);
    }

    @Test
    public void testQueryBridgeWords_MultipleBridges() {
        // 添加另一个桥接路径：hello -> test -> there
        graph.addEdge("hello", "test");
        graph.addEdge("test", "there");

        String result = graph.queryBridgeWords("hello", "there");
        // 结果可能有两种顺序，所以需要灵活判断
        assertTrue(result.matches("The bridge words from hello to there are: (world and test|test and world)\\."));
    }

    @Test
    public void testQueryBridgeWords_EmptyInput() {
        String result = graph.queryBridgeWords("", "");
        assertEquals("No  or  in the graph!", result);
    }

    @Test
    public void testQueryBridgeWords_CaseInsensitive() {
        String result1 = graph.queryBridgeWords("HELLO", "WORLD");
        String result2 = graph.queryBridgeWords("hello", "world");
        assertEquals(result2, result1);
    }
}
