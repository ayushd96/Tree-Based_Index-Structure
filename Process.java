import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Process {

    private File workingDirectory = new File(System.getProperty("user.dir"));
    public File inputFile = new File( "C:\\Users\\ayush\\eclipse-workspace\\LAB2\\src\\LA2.txt");
    public File outputFile = new File( "C:\\Users\\ayush\\eclipse-workspace\\LAB2\\out\\out.txt");
    Process(){
        
    }

    /**
     * Find median as candidate for root
     * @return
     */
    public String findMedian(){
        String median=null;
        String inLine;
        int counter=0;
        ArrayList<String> arrSorted = new ArrayList<String>();
        try{
            BufferedReader bReader = new BufferedReader(new FileReader(inputFile));  
            while((inLine=bReader.readLine()) != null){
                
                if(counter < 10000){
                    //if(pointInMedian(inLine)){
                        arrSorted.add(inLine);
                    //}                    
                }else{
                    Collections.sort(arrSorted, new comparePoints());
                    break;
                }
                counter++;
            }
            bReader.close();
        }catch(FileNotFoundException e){
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
        median = arrSorted.get(arrSorted.size()/2);
        return median;
    }


    /**
     * Refine values to find the root
     * @param str
     * @return
     */
    public boolean pointInMedian(String str){
        boolean inRange = false;
        String[] aStr = str.substring(1,  str.length()-1).split(",");
        if ((Double.parseDouble(aStr[0]) < 550 && Double.parseDouble(aStr[0]) > 450)
                && (Double.parseDouble(aStr[1]) < 550 && Double.parseDouble(aStr[1]) > 450)
                    && (Double.parseDouble(aStr[2]) < 550 && Double.parseDouble(aStr[2]) > 450)){
            inRange = true;
        }

        return inRange;
    }

    public void clearWrkingDirectory(){
        File outDir = new File( "C:\\Users\\ayush\\eclipse-workspace\\LAB2\\out");
        for(File outFile: outDir.listFiles()){
            outFile.delete();
        }
    }

    static class comparePoints implements Comparator<String>{
        public int compare(String a, String b){
            String[] strA;
            String[] strB;
            strA = a.substring(1,  a.length()-1).split(",");
            strB = b.substring(1,  b.length()-1).split(",");
            int xComp, yComp, zComp;
            xComp = Double.compare(Double.parseDouble(strA[0]), Double.parseDouble(strB[0]));
            yComp = Double.compare(Double.parseDouble(strA[1]), Double.parseDouble(strB[1]));
            zComp = Double.compare(Double.parseDouble(strA[2]), Double.parseDouble(strB[2]));
            //return xComp;
           if(xComp != 0){
                return xComp;
            }else{
                if(yComp != 0){
                    return yComp;
                }
                else{
                    return zComp;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String inLine="";
        String strMedian;
        boolean printFile = false;
        //int iSize=10;
        //String[] arrSorted = new String[1000];
        ArrayList<String> arrSorted = new ArrayList<String>();
        String[] strArr = new String[3];
        //x,y,x
        double[] pointArr = new double[3];
        int counter=0;
        BufferedReader bReader=null;
        BufferedWriter bWriter=null;

        Process process = new Process();
        process.clearWrkingDirectory();
        long startTime = System.currentTimeMillis();
        strMedian = process.findMedian();
         System.out.print("Root = " + process.findMedian());

        //KDTree indexTree = new KDTree(iSize);
        Tree indexTree = new Tree();
        //Adding root
        strArr = strMedian.substring(1,  strMedian.length()-1).split(",");
        for(int i=0; i<strArr.length;i++){
            pointArr[i] = Double.parseDouble(strArr[i]);
        }
        indexTree.add(pointArr);

        try{
            bReader = new BufferedReader(new FileReader(process.inputFile));
            //bWriter = new BufferedWriter(new FileWriter(process.outputFile));

            while((inLine=bReader.readLine()) != null){
                if(!strMedian.equals(inLine)){
                    strArr = inLine.substring(1,  inLine.length()-1).split(",");
                    //Convert to double
                    for(int i=0; i<strArr.length;i++){
                        pointArr[i] = Double.parseDouble(strArr[i]);
                    }
                    indexTree.add(pointArr); 
                } 

                //System.out.println(pointArr[1]);
                //counter++;
                //if(counter<iSize-1){
                //    break;
                //}   

            }
            //indexTree.preorder();
        }catch(FileNotFoundException e){
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nTotal time to build index: " +  (endTime-startTime) + "ms");
        System.out.println("Estimated total size of index:" + ((indexTree.size)*(8*3))/1048576 + " MB");
        //System.out.println(" ");
        //indexTree.printTree();
        //System.out.println(" ");

        startTime = System.currentTimeMillis();
        double[] from = {64.39683, 7.367402, 408.0358};
        double[] to = {455.5008, 60.9156, 580.5400};
        ArrayList<Node>  aNode = indexTree.searchBetween(from, to);
        endTime = System.currentTimeMillis();
        System.out.println("Total nodes found: " +  aNode.size());
        System.out.println("Total time for range search: " +  (endTime-startTime) + "ms");
        if(printFile == true) {
        	bWriter = new BufferedWriter(new FileWriter(process.outputFile));
        	for(int i=0; i < aNode.size();i++) {
        		bWriter.write(aNode.get(i).toString()+ "\r");
        	}
        	bWriter.close();
        }
        //double[] from = {64.39683, 7.367402, 408.0358};

        /*** Nearest Neighbor search ***/
        
        double[] pt = {455.5008, 60.9156, 580.5400};
        startTime = System.nanoTime();
        //KDNode n = indexTree.find_nearest(to);
        double[] n = indexTree.findNearest(pt);
        endTime = System.nanoTime();
        System.out.println("Nearest: " + n[0] + ", " + n[1] + ", "+ n[2]);
        System.out.println("Total time for nearest neighbour search: " +  (endTime-startTime) + "ns");

        //indexTree.searchBetween(from, to);
        /*
        double test1[] = {816.5008, 717.9156, 452.0825};
        indexTree.search(test1);
        System.out.println(" ");
        double test2[] = {816.5008, 717.9156, 452.0825};
        indexTree.find_nearest(test2);
        */
        bReader.close();
        //bWriter.close();
    }
}