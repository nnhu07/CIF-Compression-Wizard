import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Compressor {

	private ImageRegions ir;

	public Compressor(ImageRegions ir)
	{
		this.ir = ir;
	}

	public void segment(int K)
	{
		if(K<2)
		{
			throw new IllegalArgumentException();
		}

		// Create PriorityQueue and add all adjacent pixels' similaritites to it
		PriorityQueue<Similarity> pQueue = new PriorityQueue<>();
		for (int i = 0; i < ir.getSize(); i++)
			for (int j : ir.getAdjacent(i))
				pQueue.add(new Similarity(ir.get(i), ir.get(j)));

		// Loop until we have the number of regions left that we want
		while (ir.getSize() > K) {

			// Get Similarity with the smallest distance and determine pixels and roots
			Similarity toUnion = pQueue.remove();
			int p1 = ir.getID(toUnion.r1.getRoot());
			int p2 = ir.getID(toUnion.r1.getRoot());
			int root1 = ir.find(p1);
			int root2 = ir.find(p2);

			// Ignore pairs of pixels from the same set
			if (root1 == root2) continue;

			// If pixels are not roots, union only if color difference is 0 - otherwise add roots back into PQ
			if (p1 != root1 || p2 != root2) {
				if (toUnion.distance > 0) {
					pQueue.add(new Similarity(ir.get(root1), ir.get(root2)));
				} else {
					ir.union(root1, root2);
				}

				// If pixels are both roots, union
			} else if (new Similarity(ir.get(root1), ir.get(root2)).compareTo(toUnion) == 0){
				int R = ir.union(root1, root2);

				// Add neighboring sets back into PQ only if distance is greater than 0 and doing so is necessary
				if (toUnion.distance > 0) {
					for (int n : ir.getAdjacent(R)) {
						pQueue.add(new Similarity(ir.get(R), ir.get(n)));
					}
				}

			}

		}

		//Hint: the algorithm is not fast and you are processing many pixels
		//      (e.g., 10,000 pixel for a small 100 by 100 image)
		//      output a "." every 100 unions so you get some progress updates.
	}

	//Task 5: Output results (10%)
	//Recolor all pixels with the average color and save output image
//	public void outputResults(int K)
//	{
//		//collect all sets
//		int region_counter=1;
//		ArrayList<Pair<Integer>> sorted_regions = new ArrayList<Pair<Integer>>();
//
//		int width = this.image.getWidth();
//		int height = this.image.getHeight();
//		for(int h=0; h<height; h++){
//			for(int w=0; w<width; w++){
//				int id=getID(new Pixel(w,h));
//				int setid=ir.find(id);
//				if(id!=setid) continue;
//				sorted_regions.add(new Pair<Integer>(ir.get(setid).size(),setid));
//			}//end for w
//		}//end for h
//
//		//sort the regions
//		Collections.sort(sorted_regions, new Comparator<Pair<Integer>>(){
//			@Override
//			public int compare(Pair<Integer> a, Pair<Integer> b) {
//				if(a.p!=b.p) return b.p-a.p;
//				else return b.q-a.q;
//			}
//		});
//
//		//recolor and output region info
//
//		int i = 1; // Keep region counter for display purposes
//		for (Pair<Integer> region : sorted_regions) {
//
//			// Get set and compute color
//			Set<Pixel> set = ir.get(region.q);
//			Color c = computeAverageColor(set);
//
//			// Set all pixels from the current set to its color value
//			for (Pixel p : set) {
//				image.setRGB(p.p, p.q, c.getRGB());
//			}
//
//			// Print info about current region and increment counter
//			System.out.printf("region %d size= %d color=%s\n", i, region.p, c.toString());
//			i++;
//
//		}
//
//		//Hint: Use image.setRGB(x,y,c.getRGB()) to change the color of a pixel (x,y) to the given color "c"
//
//		//save output image
//		String out_filename = img_filename+"_seg_"+K+".png";
//		try
//		{
//			File ouptut = new File(out_filename);
//			ImageIO.write(this.image, "png", ouptut);
//			System.err.println("- Saved result to "+out_filename);
//		}
//		catch (Exception e) {
//			System.err.println("! Error: Failed to save image to "+out_filename);
//		}
//	}

	//this class represents the similarity between the colors of two adjacent pixels or regions
	private class Similarity implements Comparable<Similarity> {

		//a pair of ajacent pixels or regions (represented by the "root" pixels)
		Region r1;
		Region r2;

		//distance between the color of two pixels or two regions,
		//smaller distance indicates higher similarity
		int distance;

		Similarity(Region r1, Region r2) {
			this.r1 = r1;
			this.r2 = r2;
			this.distance = calcDistance();
		}

		private int getDifference(Color c1, Color c2)
		{
			int r = (c1.getRed()-c2.getRed());
			int g = (c1.getGreen()-c2.getGreen());
			int b = (c1.getBlue()-c2.getBlue());

			return r*r+g*g+b*b;
		}

		private int calcDistance() {

			Color avgColor = Region.avgColor(r1, r2);
			int d1 = getDifference(r1.getColor(), avgColor);
			int d2 = getDifference(r2.getColor(), avgColor);

			return d1 * r1.getSize() + d2 * r2.getSize();

		}

		public int compareTo( Similarity other ) {
			//remove ambiguity~ update: 11/28/2017
			int diff=this.distance - other.distance;
			if(diff!=0) return diff;
			diff=this.r1.getRoot().compareTo(other.r1.getRoot());
			if(diff!=0) return diff;
			return this.r2.getRoot().compareTo(other.r2.getRoot());
		}

	}

}
