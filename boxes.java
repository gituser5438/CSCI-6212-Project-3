/*
This is a solution to
Box Stacking.  You are given a set of n types of rectangular 3-D boxes, where the i^th box has height h(i), width w(i) and depth d(i) (all real numbers). You want to create a stack of boxes which is as tall as possible, but you can only stack a box on top of another box if the dimensions of the 2-D base of the lower box are each strictly larger than those of the 2-D base of the higher box. Of course, you can rotate a box so that any side functions as its base. It is also allowable to use multiple instances of the same type of box.
*/

import java.io.*;
import java.util.*;

public class boxes{
	
		
	public static void main(String[] argv) {
		
		//This code runs the algorithm for any set of boxes specified as a 2D array
		//height, width, depth
		int[][] boxes = {{15,992,33}, {21,62,3}, {21,92,3}};
		printBoxes(boxes, "Original Boxes");
		findMaxHeightRotated(boxes, true);
		
		int[][] boxes2 = {{1,2,3}};
		printBoxes(boxes2, "Original Boxes");
		findMaxHeightRotated(boxes2, true);
		
		//The code below runs the algorithm for values of n and outputs the run time for time complexity analysis
		//int[] nvals = {10, 100, 500, 1000, 2000, 3100, 4100, 5200, 6200, 7200, 8300, 9300, 10400, 11400, 12500, 13500, 14500, 15600, 16600, 17700, 18700, 19700, 20800, 21800, 22900, 23900, 25000};
		int[] nvals = {5, 10, 15};
		long[] durationInNanos = new long[nvals.length];
		System.out.println("Experiment for n="+Arrays.toString(nvals));
		for (int i=0; i<nvals.length; i++) {
		
			boxes = createBoxes(nvals[i]);
			
			long startTime = System.nanoTime(); 
			findMaxHeightRotated(boxes, false);
        long endTime = System.nanoTime(); // Record the end time
        durationInNanos[i] = endTime - startTime;
		}
		
				for (int i=0; i<nvals.length; i++) {
			System.out.println("n="+nvals[i]+" "+"time="+durationInNanos[i]+" ns");
		}
		
	}

//This method creates a 2D array representing n sets of box dimensions for input n
	static int[][] createBoxes(int n) {
	int[][] randomboxes = new int[n][3];
	Random r = new Random();
	for (int i=0; i<n; i++) {
	randomboxes[i][0] = r.nextInt(100) + 1;
	randomboxes[i][1] = r.nextInt(100) + 1;
	randomboxes[i][2] = r.nextInt(100) + 1;
	}
	return randomboxes;
	}
	
	//Print 2D array of box dimensions
	static void printBoxes(int[][] boxes, String title) {
		System.out.println(title);
		for (int i=0; i<boxes.length; i++) {
			System.out.println(Arrays.toString(boxes[i]));
		}
	}
	
	//sorts 2D array
	public static void sortbyColumn(int a[][], int c){      
      Arrays.sort(a, (x, y) -> Integer.compare(x[c],y[c]));  
    }
	
	
	//rotate and rotate1 return an arraylist of 6 arrays which represent the rotated box dimensions
	public static int[] rotate1(int[] arr, int x, int y, int z) {
		int[] arr2 = {arr[x], arr[y], arr[z]};
		return arr2;
	}

	public static ArrayList<int[]> rotate(int[] arr) {
		ArrayList<int[]> a = new ArrayList<>();
		a.add(rotate1(arr, 0, 1, 2));
		a.add(rotate1(arr, 0, 2, 1));
		a.add(rotate1(arr, 1, 0, 2));
		a.add(rotate1(arr, 1, 2, 0));
		a.add(rotate1(arr, 2, 0, 1));
		a.add(rotate1(arr, 2, 1, 0));
		return a;
	}
	
	
	//This method rotates the boxes and then calls the main method findMaxHeight
	static int findMaxHeightRotated(int[][] boxes, boolean printSolution) {


//rotate boxes by calling rotate method
	ArrayList<int[]> rotatedBoxesList = new ArrayList<>();
	for (int i=0; i<boxes.length; i++) {
		ArrayList<int[]> rotatedBoxList = rotate(boxes[i]);
		for (int j=0; j<rotatedBoxList.size(); j++) {
			rotatedBoxesList.add(rotatedBoxList.get(j));
		}
	}
	
	//convert to 2D array
	int[][] rotatedBoxes = new int[rotatedBoxesList.size()][];
	for (int i=0; i<rotatedBoxes.length; i++) {
		rotatedBoxes[i] = rotatedBoxesList.get(i);
	}
	
	if (printSolution) {
	printBoxes(rotatedBoxes, "Rotated boxes");
	}
	
	// call findMaxHeight method using new expanded 2D array
		return findMaxHeight(rotatedBoxes, printSolution);
		
	}
	
	//This is the main method that uses dynamic programming
	static int findMaxHeight(int[][] boxes, boolean printSolution) {
		int[] a = new int[boxes.length];
		int[] a2 = new int[boxes.length];
		
		//sort by width
		sortbyColumn(boxes, 1);
		System.out.println();
		a[0] = boxes[0][0];
		
		int[][] b = new int[boxes.length][];
		int overall_max = 0;
		int overall_max_ind = -1;
		
		//Define a array with max heights using ith box as base
		for (int i=0; i<boxes.length; i++) {
			int height = boxes[i][0];
			int width = boxes[i][1];
			int depth = boxes[i][2];
			int maxheight = 0;
			int ind = -1;
			
			//find max of previous elements of a with smaller bases
			for (int j=0; j<i; j++) {
				int height2 = boxes[j][0];
				int width2 = boxes[j][1];
				int depth2 = boxes[j][2];
				if (width2<width && depth2<depth && height2>maxheight) {
					maxheight = height2;
					ind = j;
				}
			}
			
			//define a[i]
			a[i] = maxheight + height;
			a2[i]=ind;
			
			//update max if necessary
			if (a[i]>overall_max) {
				overall_max = a[i];
				overall_max_ind = i;
			}
			
			//store box indices for all included boxes to generate a[i]
			if (ind == -1) {
				int[] indices = new int[1];
				indices[0]=i;
				b[i] = indices;
			}
			else {
				int n = b[ind].length+1;
				int[] indices = new int[n];
				indices[0]=i;
				for (int k=1; k<n; k++) {
					indices[k] = b[ind][k-1];
				}
				b[i] = indices;
			}
		}
	if (printSolution) {
		
		//print a array with boxes
				System.out.println("Sorted box types");
		for (int i=0; i<boxes.length; i++) {
			if (a2[i]==-1) {
				System.out.println(i+") "+Arrays.toString(boxes[i])+ "    a[" + i + "] = height[" + i + "] = " + a[i]);
			}
			else {
			System.out.println(i+") "+Arrays.toString(boxes[i])+ "    a[" + i + "] = height[" + i + "] + a[" + a2[i] + "] = " + a[i]);
			}
		}
		
		//print solution
		System.out.println(" ");
		System.out.println("Solution: ");
		int[] s = b[overall_max_ind];
		int boxnum;
		for (int i=s.length-1; i>=0; i--) {
			boxnum=s[i];
			System.out.println("Box "+boxnum+": "+Arrays.toString(boxes[s[i]]));
		}
		System.out.println("Maximum height: " + overall_max);
		System.out.println();
	}
		return overall_max;
	}
	
}
