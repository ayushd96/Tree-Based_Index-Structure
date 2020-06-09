import java.util.ArrayList;

class Tree {
    public Node root;
    public int size;

    double[] from;
    double[] to;

    Tree() {
        size = 0;
    }

    public void add(double[] coord) {
        if (coord == null) {
            System.out.println("Coordinate null");
            return;
        }

        root = add(root, coord, 0);
    }

    public Node add(Node current, double[] coord, int split) {
    	// add leaf.
        if (current == null) {
            size++;
            return new Node(coord, current, split);
        }

        // int splitBy = current.parent.splitBy;
        switch (split) {
        case 0:
            if (coord[0] < current.point[0]) {
                // bound[0][1] = current.point[0];
                current.left = add(current.left, coord, 1);
            } else if (coord[0] > current.point[0]) {
                // bound[0][0] = current.point[0];
                current.right = add(current.right, coord, 1);
            }
            break;
        case 1:
            if (coord[1] < current.point[1]) {
                //bound[1][1] = current.point[1];
                current.left = add(current.left, coord, 2);
            } else if (coord[1] > current.point[1]) {
                //bound[1][0] = current.point[1];
                current.right = add(current.right, coord, 2);
            }
            break;
        case 2:
            if (coord[2] < current.point[2]) {
                //bound[2][1] = current.point[2];
                current.left = add(current.left, coord, 0);
            } else if (coord[2] > current.point[2]) {
                //bound[2][0] = current.point[2];
                current.right = add(current.right, coord, 0);
            }
            break;
        }

        return current;
    }

    /**
     * Caculate distance between two nodes
     */
    double calcDistance(double[] from, double[] to) {
        double dist = 0;
        for (int i = 0; i < 3; i++) {
            dist += (from[i] - to[i]) * (from[i] - to[i]);
        }
        return dist;
    }

    /**
     * Print tree using preorder traversal
     */
    public void printTree() {
        printTree(root);
    }

    private void printTree(Node node) {

        if (node != null) {
            System.out.print(node.toString());
            printTree(node.left);
            printTree(node.right);
        }
    }

    public ArrayList<Node> searchBetween(double[] from, double[] to) {
        this.from = from;
        this.to = to;
        double[][] rangeBound = new double[][] { { from[0], to[0] }, { from[1], to[1] }, { from[2], to[2] } };
        double[][] nodeBound = new double[][] { { 0.00, 1000.00},{ 0.00, 1000.00},{ 0.00, 1000.00} };
        ArrayList<Node> arrNode = new ArrayList<Node>();
        searchBetween(root, rangeBound,nodeBound, arrNode);
        return arrNode;
    }

    private void searchBetween( Node node, double[][] rangeBound,double[][] nodeBound, ArrayList<Node> arrNode){

        if (node == null){
            return;
        }

        //No need to search in tree if not intersection
        if(!intersects(rangeBound, nodeBound)){
            return;
        }

        double[][] nodeBoundL = new double[][]{{nodeBound[0][0],nodeBound[0][1]},{nodeBound[1][0],nodeBound[1][1]},{nodeBound[2][0],nodeBound[2][1]}};
        double[][] nodeBoundR = new double[][]{{nodeBound[0][0],nodeBound[0][1]},{nodeBound[1][0],nodeBound[1][1]},{nodeBound[2][0],nodeBound[2][1]}};

        if(node.inRange(from, to)){
            //System.out.print("(" + node.point[0] + ", " + node.point[1] + ", "+ node.point[2] + ")  ");
            arrNode.add(node);
        }

        nodeBoundL[node.splitBy][1] = node.point[node.splitBy];
        searchBetween(node.left, nodeBoundL, rangeBound, arrNode);
        nodeBoundR[node.splitBy][0] = node.point[node.splitBy];
        searchBetween(node.right, nodeBoundR, rangeBound, arrNode);
    }

    public double[] findNearest(double[] coord){
        return findNearest(root, coord, root.point); 
    }

    /**
     * Recursively traverse tree. If the closest point discovered so far is closer than 
     * the distance between the query point and the cube corresponding to a node, 
     * there is no need to explore that node (or its subtrees). Only search a node if 
     * i may contain a point that is closer
     */

    private double[] findNearest(Node node, double[] coord, double[] nearest){

        if(node == null){
            return nearest;
        }

        if(node.equal(coord)){
            return coord;
        }

        double distance = calcDistance(nearest, coord);

        if(calcDistance(node.point, coord) < calcDistance(nearest, coord)){
            nearest = node.point;
        }

        if(coord[node.splitBy] < node.point[node.splitBy]){
            nearest = findNearest(node.left, coord, nearest);
            if(calcDistance(nearest, coord) >= distance){
                nearest = findNearest(node.right, coord, nearest);
            }
        }else{
            nearest = findNearest(node.right, coord, nearest);
            if(calcDistance(nearest, coord) >= distance){
                nearest = findNearest(node.left, coord, nearest);
            }
        }

        return nearest;
    }

    /**
     * Very if the bounding cube intersects
     */
    private boolean intersects(double[][] bnd1, double[][] bnd2){
        boolean intersct = false;
        if(bnd1[0][1] >= bnd2[0][0]
            && bnd1[1][1] >= bnd2[1][0]
                && bnd1[2][1] >= bnd2[2][0]
                    && bnd2[0][1] >= bnd1[0][0]
                        && bnd2[1][1] >= bnd1[1][0]
                            && bnd2[2][1] >= bnd1[2][0]){
                                intersct = true;
                            }
        return intersct;
    }

}

