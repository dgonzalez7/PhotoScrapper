package com.thosegonzos.photoscrapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class PhotoScrapper 
{
	public static void main(String[] args) 
	{
		String targetUrl = "http://elizabethgreve.com/blog/becca-matt-wedding-at-the-abbington-in-glen-ellyn/";
		String title = "";
		String outfile = "MattBeccaHTML.txt";
		
		System.out.println("Hello Scrapper!");
		
        // JSoup Example 2 - Reading HTML page from URL
        Document doc = null;

        try 
        {
            doc = Jsoup.connect(targetUrl).get();
            title = doc.title();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        System.out.println("Jsoup Can read HTML page from URL, title : " + title);
        // System.out.println("\n" + doc.html());
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outfile)))
        {
        	writer.write(doc.html());
        } 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        
//        Element content = doc.getElementById("content");
//        Elements paras = content.getElementsByAttribute("img");
//        for (Element p : paras)
//        {
//        	String imgLink = p.attr("src");
//        	System.out.println(imgLink);
//        }
        
        Elements media = doc.select("[data-lazyload-src]");
        System.out.println("Media: " + media.size());
        for (Element src : media)
        {

    		System.out.println(">" + src.tagName() + " , " + src.attr("abs:data-lazyload-src"));
    		
        }
	}
}
