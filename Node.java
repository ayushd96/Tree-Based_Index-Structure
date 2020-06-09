class Node{
    public double[] point;
    public Node left;
    public Node right;
    public Node parent;
    public int splitBy;
    public boolean visited;
    //public double[][] bound;

    public Node(double[] pt, Node p, int s){
        point = new double[]{pt[0], pt[1],pt[2]};
      //  bound = new double[][]{{bnd[0][0],bnd[0][1]},{bnd[1][0],bnd[1][1]},{bnd[2][0],bnd[2][1]}};
        left=right=null;
        parent=p;
        splitBy=s;
        visited=false;

    }
    
    boolean equal(double[] pt){
        for (int i = 0; i < 3; i++){
            if (this.point[i] != pt[i])
                return false;
        }
        return true;
    }

        /**
     * check if node is in given range
     * @param from
     * @param to
     * @return
     */
    boolean inRange(double[] from, double[] to){
        boolean inRange = false;

        if((this.point[0] >= from[0] && this.point[0]<= to[0])
            && (this.point[1] >= from[1] && this.point[1]<= to[1])
                && (this.point[2] >= from[2] && this.point[2]<= to[2])){
                    inRange = true;
                }

        return inRange;
    }

    double distanceTo(Node node){
        double distTo = 0;
        for (int i = 0; i < 3; i++){
            distTo += (this.point[i] - node.point[i]) * (this.point[i] - node.point[i]);
        }
        return distTo;
    }

    double distanceTo(double[] coord){
        double distTo = 0;
        for (int i = 0; i < 3; i++){
            distTo += (this.point[i] - coord[i]) * (this.point[i] - coord[i]);
        }
        return distTo;
    }
/*
    boolean intersects(double[][] bnd){
        boolean intersct = false;
        if(bound[0][1] >= bnd[0][0]
            && bound[1][1] >= bnd[1][0]
                && bound[2][1] >= bnd[2][0]
                    && bnd[0][1] >= bound[0][0]
                        && bnd[1][1] >= bound[1][0]
                            && bnd[2][1] >= bound[2][0]){
                                intersct = true;
                            }
        return intersct;
    }
    */
}