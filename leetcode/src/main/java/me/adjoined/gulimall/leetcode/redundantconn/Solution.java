package me.adjoined.gulimall.leetcode.redundantconn;

import java.util.*;

enum NodeStatus {
    NOT_STARTED,
    STARTED,
    FINISHED
}

class Node {
    int id;
    NodeStatus status = NodeStatus.NOT_STARTED;
    List<Node> children = new ArrayList<>();

    Node(int id) {
        this.id = id;
    }

    void addChild(Node node) {
        this.children.add(node);
    }
}
public class Solution {
    public int[] findRedundantDirectedConnection(int[][] edges) {
        Map<Integer, Node> nodeMap = new HashMap<>();
        Set<Integer> rootCandidates = new HashSet<>();
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            if (!nodeMap.containsKey(u)) {
                nodeMap.put(u, new Node(u));
                rootCandidates.add(u);
            }
            if (!nodeMap.containsKey(v)) nodeMap.put(v, new Node(v));
            rootCandidates.remove(v);
            nodeMap.get(u).addChild(nodeMap.get(v));
        }
        if (!rootCandidates.isEmpty()) {
            int r = (Integer) rootCandidates.toArray()[0];
            Node node = nodeMap.get(r);
            List<Integer> path = new ArrayList<>();
            path.add(node.id);
            return DFS(node, path, edges, true);
        }

        for (Node node : nodeMap.values()) {
            if (node.status == NodeStatus.NOT_STARTED) {
                List<Integer> path = new ArrayList<>();
                path.add(node.id);
                int[] redundant = DFS(node, path, edges, false);
                if (redundant != null) return redundant;
            }
        }
        return null;
    }

    private int[] DFS(Node node, List<Integer> path, int[][] edges, boolean hasRoot) {
        node.status = NodeStatus.STARTED;

        for (Node child : node.children) {
            path.add(child.id);
            if (child.status == NodeStatus.FINISHED) {
                if (!hasRoot) {
                    path.remove(path.size()-1);
                    continue;
                }
                // cross edge
                // delete either edge into child
                for (int i = edges.length - 1; i >=0; i--) {
                    if (edges[i][1] == child.id) {
                        return new int[]{edges[i][0], edges[i][1]};
                    }
                }

            } else if (child.status ==  NodeStatus.STARTED) {
                // back edge
                if (hasRoot) return new int[]{node.id, child.id};

                int largest = Integer.MIN_VALUE;
                int l = 0;
                for (int i = 0; i < path.size() - 1; i++) {
                    for (int j = edges.length - 1; j >= 0; j--) {
                        if (edges[j][0] == path.get(i) && edges[j][1] == path.get(i+1)) {
                            if (j > largest) {
                                largest = j;
                                l = i;
                            }
                            break;
                        }
                    }
                }
                return new int[]{path.get(l), path.get(l+1)};
            } else {
                int[] redundant = DFS(child, path, edges, hasRoot);
                if (redundant != null) return redundant;
                path.remove(path.size()-1);
            }
        }
        node.status = NodeStatus.FINISHED;
        return null;
    }

    public static void main(String[] args) {
        int[][] edges = {
//                {1, 2}, {1, 3}, {2, 3}
//                {1, 2}, {2, 3}, {3, 4}, {4, 1}, {1, 5}
//                {3, 1}, {1, 4}, {3, 5}, {1, 2}, {1, 5}
//                {6,3},{8,4},{9,6},{3,2},{5,10},{10,7},{2,1},{7,6},{4,5},{1,8}
                {2, 3}, {3, 1}, {3, 4}, {4, 2}
        };

        Solution sol = new Solution();
        int[] res = sol.findRedundantDirectedConnection(edges);
        System.out.println(res[0] + "->" + res[1]);

    }
}
