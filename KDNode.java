/**
 * Class for a node in the KDTree
 */
class KDNode
{
    int axis;
    double[] point;
    int id;
    boolean checked;
    boolean orientation;

    KDNode Parent;
    KDNode Left;
    KDNode Right;

    public KDNode(double[] pt, int axis0){
        point = new double[]{pt[0], pt[1],pt[2]};
        axis = axis0;
        Left = Right = Parent = null;
        checked = false;
        id = 0;
    }

    public KDNode FindParent(double[] pt){
        KDNode parent = null;
        KDNode next = this;
        int split;

        while (next != null){
            split = next.axis;
            parent = next;
            if (pt[split] > next.point[split])
                next = next.Right;
            else
                next = next.Left;
        }

        return parent;
    }

    
    public KDNode Insert(double[] pt){
        //point = new double[3];
        KDNode parent = FindParent(pt);
        if (equal(pt, parent.point) == true)
            return null;

        KDNode newNode = new KDNode(pt,parent.axis + 1 < 3 ? parent.axis + 1 : 0);

        newNode.Parent = parent;

        if (pt[parent.axis] > parent.point[parent.axis]){
            parent.Right = newNode;
            newNode.orientation = true; //
        } 
        else{
            parent.Left = newNode;
            newNode.orientation = false; //
        }

        return newNode;
    }

    /**
     * Check if two points have the same value
     * @param pt1
     * @param pt2
     * @return
     */
    boolean equal(double[] pt1, double[] pt2){
        for (int i = 0; i < 3; i++){
            if (pt1[i] != pt2[i])
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

}
