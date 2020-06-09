class KDTree{

    KDNode Root;
    int TimeStart, TimeFinish;
    int CounterFreq;

    double d_min;
    KDNode nearest_neighbour;

    int KD_id;
    int nList;


    KDNode CheckedNodes[];
    int checked_nodes;
    KDNode List[];

    
    double pt_min[], pt_max[];
    boolean max_boundary[], min_boundary[];
    int n_boundary;

    double[] from;
    double[] to;

    /**
     * Constructor
     * @param i
     */
    public KDTree(int i){
        Root = null;
        KD_id = 1;

        nList = 0;
        List = new KDNode[i];

        CheckedNodes = new KDNode[i];
        max_boundary = new boolean[3];
        min_boundary = new boolean[3];
        pt_min = new double[3];
        pt_max = new double[3];
    }

    /**
     * Add a new node
     */
    public boolean add(double[] x){
        if (nList >= 2000000 - 1)
            return false; // can't add more points
            
        if (Root == null)        {
            Root = new KDNode(x, 0);
            Root.id = KD_id++;
            List[nList++] = Root;
        } 
        else{
            KDNode pNode;
            if ((pNode = Root.Insert(x)) != null){
                pNode.id = KD_id++;
                List[nList++] = pNode;
            }
        }

        return true;
    }

    /**
     * Caculate distance between two nodes
     */
    double calcDistance(double[] pt1, double[] pt2){
        double d = 0;
        for (int i = 0; i < 3; i++)
            d += (pt1[i] - pt2[i]) * (pt1[i] - pt2[i]);
        return d;
    }

    /**
     * Given a node, find the nearest neighbour
     * @param x
     * @return
     */
    public KDNode find_nearest(double[] x){

        if (Root == null)
            return null;
    
        checked_nodes = 0;
        KDNode parent = Root.FindParent(x);
        nearest_neighbour = parent;
        d_min = calcDistance(x, parent.point);
        
        if (parent.equal(x, parent.point) == true)
            return nearest_neighbour;
        search_parent(parent, x);
        uncheck();

        return nearest_neighbour;
    }

    

    public void check_subtree(KDNode node, double[] x){

        if ((node == null) || node.checked)
            return;

        CheckedNodes[checked_nodes++] = node;
        node.checked = true;

        set_bounding_cube(node, x);
        int dim = node.axis;

        double d = node.point[dim] - x[dim];
    
        if (d * d > d_min){
            if (node.point[dim] > x[dim])
                check_subtree(node.Left, x);
            else
                check_subtree(node.Right, x);
        } 
        else{
            check_subtree(node.Left, x);
            check_subtree(node.Right, x);
        }
    }

    public void set_bounding_cube(KDNode node, double[] x){
        if (node == null){
            return;
        }
            
        int d = 0;
        double dx;

        for (int k = 0; k < 3; k++){
            dx = node.point[k] - x[k];
            if (dx > 0){
                dx *= dx;
                if (!max_boundary[k]){

                    if (dx > pt_max[k])
                        pt_max[k] = dx;

                    if (pt_max[k] > d_min){

                        max_boundary[k] = true;
                        n_boundary++;
                    }
                }
            } 
            else{
                dx *= dx;
                if (!min_boundary[k]){
                    if (dx > pt_min[k])
                        pt_min[k] = dx;
                    if (pt_min[k] > d_min){
                        min_boundary[k] = true;
                        n_boundary++;
                    }
                }
            }

            d += dx;

            if (d > d_min)
                return;
        }

        if (d < d_min){

            d_min = d;
            nearest_neighbour = node;
        }
    }

    public KDNode search_parent(KDNode parent, double[] pt){
        for (int i = 0; i < 3; i++){
            pt_min[i] = pt_max[i] = 0;
            max_boundary[i] = min_boundary[i] = false; //
        }
        n_boundary = 0;

        KDNode search_root = parent;
        while (parent != null && (n_boundary != 3 * 3)){
            check_subtree(parent, pt);
            search_root = parent;
            parent = parent.Parent;
        }
        return search_root;
    }

    public void uncheck(){
        for (int n = 0; n < checked_nodes; n++)
            CheckedNodes[n].checked = false;
    }

    /**
     * Print tree using preorder traversal
     */
    public void printTree(){
        printTree(Root);
    }

    private void printTree(KDNode node){

        if (node != null){
            System.out.print("(" + node.point[0] + ", " + node.point[1] + ", "+ node.point[2] + ")  ");

            printTree(node.Left);
            printTree(node.Right);
        }
    }

    /**
     * Search for a node
     */
    public void search(double[] pt){
        search(Root, pt);
    }

    private void search(KDNode root, double[] pt){

        if (root != null){
            search(root.Left, pt);
            if (pt[0] == root.point[0] && pt[1] == root.point[1] && pt[2] == root.point[2])
                System.out.print("True (" + root.point[0] + ", " + root.point[1] + ", "+ root.point[2] + ")  ");

            search(root.Right, pt);
        }
    }
    
    public void searchBetween(double[] from, double[] to){
        this.from = from;
        this.to = to;
        searchBetween(Root);
    }

    private void searchBetween(KDNode node){
        if (node != null){
            if(node.inRange(from, to)){
                System.out.print("(" + node.point[0] + ", " + node.point[1] + ", "+ node.point[2] + ")  ");
            }

            searchBetween(node.Left);
            searchBetween(node.Right);
        }
    }

}