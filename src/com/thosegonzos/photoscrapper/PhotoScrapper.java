package com.thosegonzos.photoscrapper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class PhotoScrapper 
{
	public static void main(String[] args) 
	{
		String targetUrl = "http://elizabethgreve.com/blog/becca-matt-wedding-at-the-abbington-in-glen-ellyn/";
		String outfile = "MattBeccaHTML.txt";
		String outFolder = "images";
		
        Document doc = null;
        
		System.out.println("Hello Scrapper!");

		// Get the webpage document (HTML)
        try 
        {
            doc = Jsoup.connect(targetUrl).get();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        // Save webpage document (HTML) into a file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outfile)))
        {
        	writer.write(doc.html());
        } 
        catch (IOException e) 
        {
			e.printStackTrace();
		}

        // Parse and grab all image URLs
        int i = 0;
        Elements media = doc.select("[data-lazyload-src]");
        System.out.println("Media: " + media.size());
		String[] jpgs = new String[media.size()];
        for (Element src : media)
        {
    		jpgs[i] = src.attr("abs:data-lazyload-src");
    		i++;
        }
        
        // Arrays.stream(jpgs).forEach(System.out::println);

        // Loop through all images
		System.out.println("\n\n");
        for (String jpg : jpgs)
        {
			// Get photo URL
        	int c1 = jpg.indexOf('(');
			int c2 = jpg.indexOf(')');
			String newJpg = null;
			if (c1 != -1)
			{
				newJpg = removeChar(jpg, c1, c2);
			}
			else
			{
				newJpg = jpg;
			}
			System.out.println(newJpg);
			
			// Get filename used to save photo
			String jpgFile = getFilename(jpg);
        	c1 = jpgFile.indexOf('(');
			c2 = jpgFile.indexOf(')');
			String newJpgFile = null;
			if (c1 != -1)
			{
				newJpgFile = removeChar(jpgFile, c1, c2);
			}
			else
			{
				newJpgFile = jpgFile;
			}
			System.out.println("> "+ newJpgFile);
			
			// Get photo at URL...
	        Response resultImageResponse = null;
			try 
			{
				resultImageResponse = Jsoup.connect(newJpg)
				        .ignoreContentType(true).execute();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
	        
			// ...and save it at filename
	        FileOutputStream out = null;
			try 
			{
				out = (new FileOutputStream(new java.io.File(outFolder + "/" + newJpgFile)));
				out.write(resultImageResponse.bodyAsBytes());
				out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
        }
        
	}
	
	// Removes a substring from inside a String
	public static String removeChar(String str, int c1, int c2) 
	{
		String front = str.substring(0, c1);
		String back = str.substring(c2+1, str.length());
		return front + back;
	}
	
	// Extracts the filename from a URL
	public static String getFilename(String str) 
	{
		int last = str.lastIndexOf('/');
		String back = str.substring(last+1, str.length());
		return back;
	}
}









