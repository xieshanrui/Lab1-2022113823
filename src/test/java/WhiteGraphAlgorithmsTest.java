import cn.edu.hit.DirectedGraph;
import cn.edu.hit.GraphAlgorithms;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Random;
import java.lang.reflect.Field;

public class WhiteGraphAlgorithmsTest {
    private DirectedGraph graph;
    private GraphAlgorithms algo;

    @Before
    public void setUp() {
        graph = new DirectedGraph();
        algo = new GraphAlgorithms(graph);
    }

    @Test // TC1 - 空图测试
    public void testEmptyGraph() {
        String result = algo.randomWalk();
        System.out.println(result);
        assertEquals("Graph is empty", result);
    }

    @Test // TC2 - 无出边停止测试
    public void testSimpleWalk() {
        graph.addEdge("a", "b");

        String result = algo.randomWalk();
        System.out.println(result);

        // 两种合法情况：
        assertTrue(result.equals("a->b\nStopped at node with no out edges") ||
                result.equals("b\nStopped at node with no out edges"));
    }


    @Test // TC3 - 重复边测试
    public void testRepeatedEdge() {
        graph.addEdge("a", "b");
        graph.addEdge("b", "a");

        // 创建Mock随机对象
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt()))
                .thenReturn(0)  // 第一次选a
                .thenReturn(0)  // 选b
                .thenReturn(0); // 选a

        injectRandom(algo, mockRandom);

        String result = algo.randomWalk();
        System.out.println(result);
        assertEquals("a->b->a\nStopped at repeated edge: a->b", result);
    }

    @Test // TC4 - 游走循环测试
    public void testNoOutEdges() {
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");

        // 固定选择顺序：a->b->c
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt()))
                .thenReturn(0)  // 选a
                .thenReturn(0)  // 选b
                .thenReturn(0); // 选c

        injectRandom(algo, mockRandom);

        String result = algo.randomWalk();
        System.out.println(result);
        assertEquals("a->b->c\nStopped at node with no out edges", result);
    }

    private void injectRandom(GraphAlgorithms algo, Random random) {
        try {
            // 获取私有random字段
            Field field = GraphAlgorithms.class.getDeclaredField("random");
            field.setAccessible(true);
            field.set(algo, random);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject random", e);
        }
    }
}
