import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import cn.edu.hit.DirectedGraph;

public class BlackDirectedGraphTest {
    private DirectedGraph graph;

    @Before
    public void setUp() {
        graph = new DirectedGraph();
        // 基础图结构
        graph.addEdge("hello", "world");
        graph.addEdge("world", "there");
        graph.addEdge("hello", "hit");
        graph.addEdge("world", "hello");

        // 为桥接词测试添加的边
        graph.addEdge("hello", "bridge1");
        graph.addEdge("bridge1", "target");
        graph.addEdge("hello", "bridge2");
        graph.addEdge("bridge2", "target");

        graph.addEdge("start", "b1");
        graph.addEdge("b1", "end");
        graph.addEdge("start", "b2");
        graph.addEdge("b2", "end");
        graph.addEdge("start", "b3");
        graph.addEdge("b3", "end");
    }

    // ========== 有效等价类测试 ==========

    @Test // TC1 - 单个桥接词
    public void testSingleBridgeWord() {
        String result = graph.queryBridgeWords("hello", "there");
        System.out.println(result);
        assertEquals("The bridge words from hello to there are: world.", result);
    }

    @Test // TC2 - 两个桥接词
    public void testTwoBridgeWords() {
        String result = graph.queryBridgeWords("hello", "target");
        System.out.println(result);
        assertTrue(result.matches("The bridge words from hello to target are: (bridge1 and bridge2|bridge2 and bridge1)\\."));
    }

    @Test // TC3 - 多个(3+)桥接词
    public void testMultipleBridgeWords() {
        String result = graph.queryBridgeWords("start", "end");
        System.out.println(result);
        assertTrue(result.matches("The bridge words from start to end are: (b1, b2 and b3|b1, b3 and b2|b2, b1 and b3|b2, b3 and b1|b3, b1 and b2|b3, b2 and b1)\\."));
    }

    @Test // TC4 - 大小写不敏感测试
    public void testCaseInsensitive() {
        String lowerResult = graph.queryBridgeWords("hello", "there");
        String upperResult = graph.queryBridgeWords("HELLO", "THERE");
        System.out.println(upperResult);
        assertEquals("The bridge words from hello to there are: world.", upperResult);
        assertEquals(lowerResult, upperResult);
    }

    @Test // TC5 - 自环有桥接词
    public void testSameWordWithBridge() {
        String result = graph.queryBridgeWords("hello", "hello");
        System.out.println(result);
        assertEquals("The bridge words from hello to hello are: world.", result);
    }

    // ========== 无效等价类测试 ==========

    @Test // TC6 - 无桥接词
    public void testNoBridgeWords() {
        String result = graph.queryBridgeWords("hello", "world");
        System.out.println(result);
        assertEquals("No bridge words from hello to world!", result);
    }

    @Test // TC7 - 自环无桥接词
    public void testSameWordNoBridge() {
        String result = graph.queryBridgeWords("hit", "hit");
        System.out.println(result);
        assertEquals("No bridge words from hit to hit!", result);
    }

    @Test // TC8 - word1不存在
    public void testWord1NotExist() {
        String result = graph.queryBridgeWords("nonexist", "world");
        System.out.println(result);
        assertEquals("No nonexist or world in the graph!", result);
    }

    @Test // TC9 - word2不存在
    public void testWord2NotExist() {
        String result = graph.queryBridgeWords("hello", "nonexist");
        System.out.println(result);
        assertEquals("No hello or nonexist in the graph!", result);
    }

    @Test // TC10 - 两个词都不存在
    public void testBothWordsNotExist() {
        String result = graph.queryBridgeWords("nonexist1", "nonexist2");
        System.out.println(result);
        assertEquals("No nonexist1 or nonexist2 in the graph!", result);
    }

    @Test // TC11 - 空输入
    public void testEmptyInput() {
        String result = graph.queryBridgeWords("", "");
        System.out.println(result);
        assertEquals("No  or  in the graph!", result);
    }

    @Test(expected = NullPointerException.class) // TC12 - null输入
    public void testNullInput() {
        graph.queryBridgeWords(null, "world");
    }
}