package ru.namibios.fishing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FillMatrix {
	
	private static final Logger logger = LoggerFactory.getLogger(FillMatrix.class);

	private static final int OVERFLAW = 10000;

	private static final int MIN_ROW_INDEX = 20;
	
	public static final int SYMBOL_ROW = 14;
	public static final int SYMBOL_COLUMN = 12;
	
	private static final int EMPTY = 0;
	private static final int VALUE = 1;
	
	private int[][] matrix;
	private Map<Integer, MatrixElement> elements;
	
	private int maxRow;
	private int maxColumn;
	
	private int counter;

	private int iteration;
	
	public FillMatrix(int[][] matrix, int row, int column) {
		this.matrix = matrix;
		this.elements = new HashMap<Integer, MatrixElement>();
		
		this.maxRow = row;
		this.maxColumn = column;
		
		this.counter = 2;
		this.iteration = 0;
	}
	
	private void fill(int row, int column){
		
		if(iteration > OVERFLAW) return;
		iteration++;
		
		//up
		if(row > 0 && matrix[row - 1][column] == VALUE){
			matrix[row - 1][column] = counter;
			fill(row - 1 , column);
		}
		
		//down
		if(row < maxRow - 1 && matrix[row + 1][column] == VALUE){
			matrix[row + 1][column] = counter;
			fill(row + 1 , column);
		}
		
		//left
		if(column > 0 && matrix[row][column - 1] == VALUE){
			matrix[row][column - 1]= counter;
			fill(row , column - 1);
		}
		
		//right
		if(column < maxColumn - 1  && matrix[row][column + 1] == VALUE){
			matrix[row][column + 1] = counter;
			fill(row , column + 1);
		}
	}
	
	public int getCounter(){
		return this.counter;
	}
	
	public int[][] getMatrix() {
		return matrix;
	}

	private void clear(int obj){
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxColumn; j++) {
				if(matrix[i][j] == obj) matrix[i][j] = EMPTY;
			}
		}
	}
	
	public void cleanOfBounds(int minCnt, int maxCnt){
		int objCnt= 2;
		
		while(objCnt < counter){
			int sum=0;
			int minIndexRow=maxRow;
			int minIndexColumn= maxColumn;
			int maxIndexRow = 0;
			int maxIndexColumn= 0;
			
			for (int i = 0; i < maxRow; i++) {
				for (int j = 0; j < maxColumn; j++) {
					if(matrix[i][j] == objCnt){
						
						if(minIndexRow > i)    minIndexRow= i;
						if(minIndexColumn > j) minIndexColumn= j; 
						if(maxIndexRow < i)    maxIndexRow= i; 
						if(maxIndexColumn < j) maxIndexColumn= j; 
						
						sum++;
					} 
				}
			}
			int maxRow = maxIndexRow - minIndexRow;
			int maxColumn = maxIndexColumn - minIndexColumn;
			
			boolean isIntervalOk = sum < minCnt || sum > maxCnt;
			boolean isDimensionOk = maxRow < 5 || maxColumn < 5 || maxRow > SYMBOL_ROW || maxColumn > SYMBOL_COLUMN;
			boolean isMinIndexOk = minIndexRow < MIN_ROW_INDEX;
			if(isIntervalOk || isDimensionOk || isMinIndexOk){
				clear(objCnt);
				logger.info("Clear object=" + objCnt + "| sum= " + sum + " | " + "isIntervalOk " + isIntervalOk + "| isDimensionOk " + isDimensionOk + "| isDimensionOk " + isDimensionOk);
			} else{
				MatrixElement element = new MatrixElement(minIndexRow, maxIndexRow, minIndexColumn, maxIndexColumn);
				elements.put(objCnt, element);
				logger.info("Object " + objCnt + " sum= " + sum + " | ");
				logger.info(element.toString());
			}
			objCnt++;
		}
		
	}
	
	private Map<Integer, MatrixElement> getMinMatrix(Map<Integer, MatrixElement> elements){
		
		Map<Integer, MatrixElement> rezultMap = new HashMap<Integer, MatrixElement>();
		
		int avg = 0;
		int count=0;
		int sumIndexRow=0;
		for (int key: elements.keySet()) {
			 MatrixElement element = elements.get(key);
			 sumIndexRow += element.getMaxRow();
			 count++;
		}
		
		List<Integer> sortIndex = new ArrayList<Integer>();
		
		for (int key: elements.keySet()) {
			 MatrixElement element = elements.get(key);
			 if(element.getMaxRow() > avg){
				sortIndex.add(element.getMinColumn());
			 }
		}
		
		Object[] sort = sortIndex.toArray();
		Arrays.sort(sort);
		
		for (int i = 0; i < sort.length; i++) {
			for (int key: elements.keySet()) {
				 MatrixElement element = elements.get(key);
				 if(element.getMinColumn() == (Integer)sort[i]){
					 rezultMap.put(i, element);
				 }
			}
		}
		
		return rezultMap;
	}
	
	public List<int[][]> toListMatrix(){
		List<int[][]> rezult = new ArrayList<int[][]>();
		elements = getMinMatrix(elements);
		for (int key : elements.keySet()) {
			MatrixElement element = elements.get(key);
			int[][] symbol = new int[SYMBOL_ROW][SYMBOL_COLUMN];
			int row=0;
			int column=0;
			for (int i = element.getMinRow(); i <= element.getMaxRow(); i++) {
				column=0;
				for (int j = element.getMinColumn(); j <= element.getMaxColumn(); j++) {
					symbol[row][column] = matrix[i][j] > 0 ? 1: 0;
					column++;
				}
				row++;
			}
			rezult.add(symbol);
		}

		return rezult;
	}
	
	public void markupMatrix(){
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxColumn; j++) {
				if(matrix[i][j] == VALUE){
					fill(i, j);
					counter++;
				}
			}
		}
	}
	
}