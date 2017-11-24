package com.mrll.javelin.tikaparser.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import net.sourceforge.tess4j.util.ImageIOHelper;

public class PdfBoxUtils {
	
	
	/*public static void main(String[] args) {
		
		try {
			//new PdfBoxUtils().convertToTiff(new File("C:\\Users\\rishabh.jain1\\Downloads\\LargeExcelFiles\\Pdf _files\\01 - The C Language_done.pdf"));
			File tiffFile = null ;
			tiffFile = new File("C:\\Users\\rishabh.jain1\\Downloads\\LargeExcelFiles\\JAV-16707\\Images"+File.separator+"multipage.tif");
			
			File[] files = {new File("C:\\Users\\rishabh.jain1\\Downloads\\LargeExcelFiles\\JAV-16707\\1359846393.png"),
					new File("C:\\Users\\rishabh.jain1\\Downloads\\LargeExcelFiles\\JAV-16707\\image01.png"),
					new File("C:\\Users\\rishabh.jain1\\Downloads\\LargeExcelFiles\\JAV-16707\\Miller-series-webtype.png")};
			
			ImageIOHelper.mergeTiff(files, tiffFile);
			new PdfBoxUtils().gifToJpg();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	
	public void gifToJpg() throws IOException{
		 BufferedImage image=ImageIO.read(new File("E:/giff2.gif"));
	     File file=new File("E:/giff2_1.tiff");
	     file.createNewFile();
	     OutputStream os = new FileOutputStream(file);
	     //ImageIOUtil.writeImage(image, "E:/t7.tiff", 300);
	     ImageIOUtil.writeImage(image, "tiff", os, 300, 1.0F);
	     //ImageIO.write(image,"tiff", file);
		
	}
	
	public void getPngFromPdf(File file) throws IOException{
		
		//PDDocument document = PDDocument.load(new File("C:\\Users\\rishabh.jain1\\Downloads\\LargeExcelFiles\\Pdf _files\\Pdf_with_images_done.pdf"));
	    DocumentProcesser processer = new DocumentProcesser();
		PDDocument document = PDDocument.load(file);
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		
		for (int page = 0; page < document.getNumberOfPages(); ++page)
		{ 
			BufferedImage image = pdfRenderer.renderImage(page, 4, ImageType.RGB);
			
			//images[page] = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
			System.out.println("PDFBOXUTILS Processing"+file.getName()+" Page "+page);
			
			ImageIOUtil.writeImage(image, file.getParent()+File.separator+"result" + "-" + (page+1) + ".tif", 300);
			processer.getTextAndPdfFromImage(new File(file.getParent()+File.separator+"result" + "-" + (page+1) + ".tif"));
            System.gc();
            image.flush();
		}
		merge_pdfFiles(file.getParent(),document.getNumberOfPages());
		//deleteFiles(".pdf" ,file.getParent(),document.getNumberOfPages());
		mergeFiles(file.getParent(),document.getNumberOfPages());
		deleteFiles(".txt" ,file.getParent(),document.getNumberOfPages());
		file.delete();
		document.close();
	}
	
	public void merge_pdfFiles(String path , int page_count) throws IOException{
		
		PDFMergerUtility ut = new PDFMergerUtility();
		MemoryUsageSetting setting = MemoryUsageSetting.setupMainMemoryOnly();
		
		for (int page = 0; page < page_count; ++page)
		{ 
		  try {
			ut.addSource(new File(path+File.separator+"result" + "-" + (page+1) + ".pdf"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to merge files");
		}   
		}
		ut.setDestinationFileName(path+File.separator+"result_PDF.pdf");
		ut.mergeDocuments(setting);
	}
	
	public void deleteFiles(String type ,String path , int page_count){
		
		for (int page = 0; page < page_count; ++page)
		{ 
			new File(path+File.separator+"result" + "-" + (page+1) + type).delete();
		}
	}
	
	
	/*public File convertToTiff(File file) throws IOException{
		String path = file.getParent();
		File tiffFile = null ;
		tiffFile = new File(path+File.separator+"multipage.tif");
		ImageIOHelper.mergeTiff(getPngFromPdf(file), tiffFile);
		System.out.println("PDFBOXUTILS Processing"+file.getName()+" convert to tiff successfully");
		file.delete();
		return tiffFile;
		
	}*/
	
	
	public static void mergeFiles(String path, int pageCount) throws IOException {
		 
		FileWriter fstream = null;
		BufferedWriter out = null;	
		File mergedFile = new File(path+File.separator+"result_TXT.txt");
		mergedFile.createNewFile();
		try {
			fstream = new FileWriter(mergedFile, true);
			 out = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
 
		for (int page = 0; page < pageCount; ++page) {
			File f = new File(path+File.separator+"result" + "-" + (page+1) + ".txt");
			System.out.println("merging: " + f.getName());
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
				String aLine;
				while ((aLine = in.readLine()) != null) {
					out.write(aLine);
					out.newLine();
				}
 
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
 
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}

}
