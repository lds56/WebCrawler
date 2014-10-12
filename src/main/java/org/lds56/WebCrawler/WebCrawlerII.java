package org.lds56.WebCrawler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class ImageUrl {
	ImageUrl(URL url, int size) {
		this.url = url;
		this.size = size;
	}
	URL url;
	int size;
}

/**
 * @author lds
 * 
 */

public class WebCrawlerII {
	public static String USE_BUILT_IN = "use_built_in";
	public static String USE_HTTP_CLIENT = "use_http_client";
	public static String USE_STRING_PARSER = "use_string_parser";
	public static String USE_SOUP_PARSER = "use_soup_parser";
	public static int IMAGE_AVERAGE_SIZE = -123456;
	public static int IMAGE_MAX_SIZE = 1000000;
	public final String FILE_DEFAULT_NAME = "WebContent";

	URL url;
	String content;
	Vector<URL> linkList = new Vector<URL> ();
	Vector<ImageUrl> imageList = new Vector<ImageUrl> ();
	int sizeAvg = 0;
	public WebCrawlerII(URL url, String contentMethod, String urlMethod) throws MalformedURLException {
		this.url = url;
		if (contentMethod == USE_BUILT_IN)  {
			getContentBuiltIn();
		} else {
			if (contentMethod == USE_HTTP_CLIENT) {}
			else throwMethodError("Invalid Method!");
			try {
				getContentHttpClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (urlMethod == USE_STRING_PARSER) {
			getLinkListByString();
			getImagesByString();
		}
		else {
			if (urlMethod == USE_SOUP_PARSER) {}
			getLinkListBySoup();
			getImagesBySoup();
		}
	}
	public WebCrawlerII(URL url) throws MalformedURLException {
		this(url, USE_HTTP_CLIENT, USE_SOUP_PARSER);
	}
	public WebCrawlerII(URL url, String contentMethod) throws MalformedURLException {
		this(url, contentMethod, USE_SOUP_PARSER);
	}
	public WebCrawlerII(String urlString) throws MalformedURLException {
		this(new URL(urlString));
	}
	public WebCrawlerII(String urlString, String contentMethod) throws MalformedURLException {
		this(new URL(urlString), contentMethod);
	}
	public WebCrawlerII(String urlString, String contentMethod, String urlMethod) throws MalformedURLException {
		this(new URL(urlString), contentMethod, urlMethod);
	}
	public void getContentBuiltIn() {
		byte[] mData = null;
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(5 * 1000);  
	        InputStream mInStream = conn.getInputStream();         
	        byte[]  buffer = new byte[1204];  
	        int len = 0;  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
			while ((len = mInStream.read(buffer)) != -1){  
	            outStream.write(buffer,0,len);  
	        }  
	        mInStream.close(); 
	    	mData = outStream.toByteArray();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        try {
			content = new String(mData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
	
	public void getContentHttpClient() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();   
		HttpResponse response = httpClient.execute(new HttpGet(url.toString()));
		HttpEntity entity = response.getEntity();
		content = EntityUtils.toString(entity, "UTF-8");
		//System.out.println(responseString);
		httpClient.close();
    }  

	public void getLinkListByString() throws MalformedURLException {      
        String regx = "href=\".*?\"";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            String aString = matcher.group();
            if (aString.contains("/") == false) continue;
			aString = aString.substring(6, aString.length()-1);
			if (aString.startsWith("http") == false) {
				aString = url.toString() + aString;
				if (aString.contains("//")) continue;
			}
			linkList.add(new URL(aString));
		}
	}
	
	public void getLinkListBySoup() throws MalformedURLException {
		Document doc = Jsoup.parse(content);
        Elements links = doc.select("a[href]");
        for (Element link : links) {
        	if (link.attr("abs:href")!="") //System.out.println(link.attr("abs:href"));
        		linkList.add(new URL(link.attr("abs:href")));
        }
	}
	
	public String getContent() {
		return content;
	}
	public Vector<URL> getLinkList() {
		return linkList;
	}
	public void getImagesBySoup() throws MalformedURLException {
		Document doc = Jsoup.parse(content);
        Elements images = doc.select("img[src]");
        for (Element image : images) {
        	if (image.attr("abs:src")!="") {
                URL aUrl = new URL(image.attr("abs:src"));
                int size = getImageSize(aUrl);
    			imageList.add(new ImageUrl(aUrl, size));
    			sizeAvg += size;
    		}
            if (imageList.isEmpty() == false) sizeAvg = sizeAvg / imageList.size();
        }
	}
	public int getImageSize(URL aUrl) {
		int size = -1;
		try {
			size = aUrl.openConnection().getContentLength();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	public void getImagesByString() throws MalformedURLException {
        String regx = "http://.*?.(jpg|png|gif|JPG|PNG|GIF)";   //href=\".*?\"
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            URL aUrl = new URL(matcher.group());
            int size = getImageSize(aUrl);
			imageList.add(new ImageUrl(aUrl, size));
			sizeAvg += size;
		}
        if (imageList.isEmpty() == false) sizeAvg = sizeAvg / imageList.size();
        //return imagesList;
	}
	public Vector<URL> getImageList(int minSize, int maxSize) {
		Vector<URL> newImageList = new Vector<URL> ();
		for (ImageUrl anImage : imageList) {
			if (anImage.size <= maxSize && anImage.size >= minSize) {
				newImageList.add(anImage.url);
			}
		}
		return newImageList;
	}
	public Vector<URL> getImageList(int minSize) {
		if (minSize == IMAGE_AVERAGE_SIZE) return getImageList(sizeAvg, IMAGE_MAX_SIZE);
		else return getImageList(minSize, IMAGE_MAX_SIZE);
	}
	public Vector<URL> getImageList() {
		return getImageList(0);
	}
	void throwMethodError(String errorString) {
		System.out.println(errorString);
	}
	public void exportImageInFile() {
		File theDir = new File("img");
		if(!theDir.exists()) theDir.mkdir();
		for (ImageUrl imageUrl : imageList) {
			try {
				String imgString = imageUrl.url.toString();
				String imgName = imgString.substring(imgString.lastIndexOf('/')+1, imgString.length());
		        CloseableHttpClient httpClient = HttpClients.createDefault();
		        HttpGet httpget = new HttpGet(imageUrl.url.toString());
		        HttpResponse response = httpClient.execute(httpget);
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        FileOutputStream fos = new FileOutputStream(new File("img/" + imgName));
		        int inByte;
		        while((inByte = is.read()) != -1) fos.write(inByte);
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void exportImageInHtml() {
		String imagesInHtml = new String();
		for (ImageUrl imageUrl : imageList) {
			imagesInHtml = imagesInHtml.concat("<img  src=\"" + imageUrl.url.toString() + "\"/>");
		}
		printTextInFile("img/images.html", imagesInHtml);
	}
	public void printTextInFile(String fileName, String fileContent) {
		String[] dirs = fileName.split("/");
		for (String dirName : dirs) {
			if (dirs[dirs.length-1].equals(dirName)) continue;
			File theDir = new File(dirName);
			if (!theDir.exists()) theDir.mkdir();
		}
		try {
			PrintWriter out = new PrintWriter(fileName);
			out.println(fileContent);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}