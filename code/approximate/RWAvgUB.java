package code.approximate;

import code.graph.RWGraph;

import java.util.HashMap;
import java.util.HashSet;

public class RWAvgUB extends RandWalk {
    public RWAvgUB(RWGraph G, HashSet<Integer> source, int scope, int topN) {
        super(G, source, scope, topN);
    }
    public RWAvgUB(){
        super();
    }
    @Override
    public void computePossibility() {
        this.edgeNum = 0;
        this.transitionMatrix.clear();
        if (this.node2index.size() < this.topN) {//top-n过大
            this.topN = this.node2index.size();
        }
        HashSet<Integer> nodes = new HashSet<>();
        nodes.addAll(this.node2index.keySet());
        double p;
        for (int i : nodes) {//遍历游走范围内的点
            double supSum = 0;
            HashMap<Integer, Double> tmp = new HashMap<>();//临时记录邻居j的sup
            this.transitionMatrix.put(i, new HashMap<>());
            for (int j : this.G.getGraph().get(i)) {
                if (nodes.contains(j)) {
                    double sup = this.G.getEdgeMaxTrussness().get(i).get(j);
                    sup *= this.G.getNodeAvgSup().get(j) / this.G.getNodeAvgSup().get(i);
                    tmp.put(j, sup);
                    supSum += sup;
                    this.edgeNum++;
                }
            }
            for (int j : tmp.keySet()) {
                //邻边的support都是0, 等概率转移
                if (supSum == 0) {
                    p = 1.0 / tmp.size();
                }
                else {
                    p = tmp.get(j) / supSum;
                }
                this.transitionMatrix.get(i).put(j, p);
            }
        }
        this.edgeNum /= 2;
    }
}
