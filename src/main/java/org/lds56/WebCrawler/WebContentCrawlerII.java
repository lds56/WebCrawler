package org.lds56.WebCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebContentCrawlerII extends WebCrawlerII{
	String text;
	String textInHtml;
	
	public WebContentCrawlerII(URL url, String contentMethod, String urlMethod) throws MalformedURLException {
		super(url, contentMethod, urlMethod);
	}
	public WebContentCrawlerII(URL url, String contentMethod) throws MalformedURLException {
		super(url, contentMethod);
	}
	public WebContentCrawlerII(URL url) throws MalformedURLException {
		super(url);
	}
	public WebContentCrawlerII(String urlString, String contentMethod, String urlMethod) throws MalformedURLException {
		super(urlString, contentMethod, urlMethod);
	}
	public WebContentCrawlerII(String urlString, String contentMethod) throws MalformedURLException {
		super(urlString, contentMethod);
	}
	public WebContentCrawlerII(String urlString) throws MalformedURLException {
		super(urlString);
	}
	private String clean(String rawContent) {
        String regx = "<p>.*?</p>";
        String cleanedContent = new String();
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(rawContent);
        while(matcher.find()) {
            String aString = matcher.group();
            cleanedContent = cleanedContent + aString + '\n';
		}
        return cleanedContent;
	}
	public void getContentBuiltIn() {
		super.getContentBuiltIn();  
        content = clean(content);
        filterText();
	}
	
	public void getContentHttpClient() throws IOException {
		super.getContentHttpClient();  
        content = clean(content);
        filterText();
	}
	public void filterText() {
		if (text == null) {
			text = new String();
			textInHtml = new String("<meta charset=\"utf-8\">\n");
			//content = "<p>aaaaa</p><a>aaaa</a>";			
			String[] textString = content.split("<.*?>");
			for (String aText : textString) {
				text = text.concat(aText);
			}
			textString = content.split("<(?!(p|/p)>).*?>");
			for (String aText : textString) {
				textInHtml = textInHtml.concat(aText);
			}
		}
	}
	public String getContentText() {
		return text;
	}
	public void printTextInHtml(String fileName) {
		super.printTextInFile("txt/" + fileName + ".html", textInHtml);
	}
	public void printTextInPlainText(String fileName) {
		super.printTextInFile("txt/" + fileName + ".txt", text);
	}
	public void printTextInHtml() {
		printTextInHtml(super.FILE_DEFAULT_NAME);
	}
	public void printTextInPlainText() {
		printTextInPlainText(super.FILE_DEFAULT_NAME);
	}
}
