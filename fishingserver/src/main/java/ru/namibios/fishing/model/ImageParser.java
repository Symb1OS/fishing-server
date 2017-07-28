package ru.namibios.fishing.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageParser {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageParser.class);
	
	private static final int ROW = 58;
	private static final int COLUMN = 372;

	private static final double CHARS_MIN_KOEF = 0.88;
	
	private int[][] imageMatrix;
	private ArrayList<int[][]> keyWordListList;
	
	public ImageParser(int[][] imageMatrix){
		this.imageMatrix = imageMatrix;
		this.keyWordListList = new ArrayList<int[][]>();
	}
	
	public void getCodes(){
		FillMatrix fillMatrix = new FillMatrix(imageMatrix, ROW, COLUMN);
		fillMatrix.markupMatrix();
		fillMatrix.cleanOfBounds(40, 100);
		imageMatrix = fillMatrix.getMatrix();
		
		printMatrix(imageMatrix, ROW, COLUMN);
		
		List<int[][]> list = fillMatrix.toListMatrix();
		
		keyWordListList = new ArrayList<int[][]>(list);
		
		for (int[][] is : list) {
			printTemplate(is, FillMatrix.SYMBOL_ROW, FillMatrix.SYMBOL_COLUMN);
		}
		
		}
	
	public int[][] getImageMatrix(){
		return imageMatrix;
	}
			
	private int equalsMatrix(int[][] numberMatrix) {
		int rezultIndex = -1;
		
		double koef = 0;
		double templateKoef = 0;
		double calcKoef = 0;
		double maxCalcKoef = 0;

		int index = 0;
		int size =  Chars.values().length;
		while(index < size){
			
			List<int[][]> templateNumber = Chars.values()[index].getTemplates(); 
			for (int[][] template : templateNumber) {
				
				if(calcKoef > CHARS_MIN_KOEF) break;
				if(template.length != numberMatrix.length ) continue;
					
				templateKoef=0; koef = 0;
				for (int i = 0; i < ROW; i++) {
					for (int j = 0; j < COLUMN; j++) {
						boolean isValue;
						try{ isValue = (template[i][j] == 1); }catch(ArrayIndexOutOfBoundsException e){break;}
						if(isValue) templateKoef++;
						
						boolean valuesEqual = numberMatrix[i][j] == template[i][j] && template[i][j] != 0; 
						if(valuesEqual) koef +=1;
					}
				}
				
				calcKoef = koef / templateKoef;
				boolean isNewKoef = calcKoef > maxCalcKoef; 
				if( isNewKoef ){
					rezultIndex = index;
					maxCalcKoef = calcKoef;
				}
				
				boolean isUndefined = maxCalcKoef < CHARS_MIN_KOEF;
				if( isUndefined ) rezultIndex = -1;
				
				logger.info("index= " + index + " |templateKoef= " + templateKoef + " | koef= " + koef + " | " + calcKoef );
				logger.info("========================================");
				
			}
			index++;
		}
		
		return rezultIndex == 10 ? -1 : rezultIndex;
	}
	
	public String getkeyFromTemlate() {
		StringBuilder rezult = new StringBuilder();
		
		
		for (int[][] numberMatrix : keyWordListList) {
			int rezultIndex = equalsMatrix(numberMatrix);
			if(rezultIndex != -1){
				rezult.append(Chars.values()[rezultIndex]);
			} else {
				rezult.append("-1");
			}
		}	
		return rezult.toString().replace("-1", "");
	}
	
	private void printMatrix(int[][] tmp, int row, int column){
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				System.out.print(tmp[i][j]!= 0 ? 1 : " ");
			}
			System.out.println();
		}
	}
	
	private void printTemplate(int[][] tmp, int row, int column){
		System.out.println("new int[][]{");
		for (int i = 0; i < row; i++) {
			System.out.print("{");
			for (int j = 0; j < column; j++) {
				System.out.print((tmp[i][j] == 0 ? "0" : "1") + ", ");
			}
			System.out.print("},");
			System.out.println();
		}
		System.out.println("}");
		System.out.println();
	}
}