[TOC]


##Class Overview

- ####**WebCrawlerII**

	- Class Summary
	
		- A class focuses on gaining and exporting content of the website given in a URL.
	
	- Nested Class Summary
	
		- ImageUrl

	- Constructor Summary
	

		Modifier  | Constructor and Description
		----------| ------|----------
			public | WebCrawlerII(URL url) <br>`Construct by a URL and require the content and the links of the website using default method.`</br> 
		public | WebCrawlerII(URL url, String contentMethod) <br>`Construct by a URL and require the content of website using given method.`</br> 
		public | WebCrawlerII(URL url, String contentMethod, String urlMethod) <br>`Construct by a URL and require the content and links of website using given method.`</br> 
		public | WebCrawlerII(String urlString) <br>`Construct by a string and require the content and the links of the website using default method.`</br> 
		public | WebCrawlerII(URL url, String contentMethod) <br>`Construct by a string and require the content of website using given method.`</br> 
		public | WebCrawlerII(URL url, String contentMethod, String urlMethod) <br>`Construct by a URL and require the content and links of website using given method.`</br> 

	- Element Summary
	
	
		Modifier  | Element and Description
		----------| --------|----------
		public static String | *USE_BUILT_IN* <br>`Use built-in library`</br> 
		public static String | *USE_HTTP_CLIENT* <br>`Use the third party library - HttpClient`</br> 
		public static String | *USE_STRING_PARSER* <br>`Parse in Regex.`</br> 
		public static String | *USE_SOUP_STRING* <br>`Parse in Jsoup`</br> 
		public static String | *FILE_DEFAULT_NAME* <br>`Define the default name of the file to be written.`</br> 
		public static int | *IMAGE_AVERAGE_SIZE* <br>`Denote the average size. `</br> 
		public static int | *IMAGE_MAX_SIZE* <br>`Define the maximum size.`</br> 
		private URL | url <br>`The url of the website.`</br> 
		private String | content <br>`The content(or source code) of the website.`</br> 
		private Vector&lt;URL&gt; | linkList <br>`The collection of links in the given website.`</br> 
		private Vector&lt;ImageUrl&gt; | imageList<br>`The collection of URLs of images in the given website.`</br> 
		int | sizeAvg <br>`The average size of all the images in the given website.`</br> 
	
	- Method Summary

		Modifier and Type | Method and Description
		----------|-------
		public void | getContentBuiltIn()  <br>`Require the content using HttpURLConnection.`</br> 
		public void | getContentHttpClient() <br>`Require the content using HttpClent.`</br> 
		public void | getLinkListByString()  <br>`Parse the links in regex.`</br> 
		public void | getLinkListBySoup() <br>`Parse the liinks in Jsoup.`</br> 
		public int | getImageSize(URL aUrl) <br>`Get the size of the image given in URL.`</br> 
		public void | getImagesByString() <br>`Parse the URLs of images in regex.`</br> 
		public void | getImagesBySoup() <br>`Parse the URLS of inages in Jsoup.`</br> 
		public String | getContent()  <br>`Return the content.`</br> 
		public Vector&lt;URL&gt; | getLinkList()  <br>`Return the collection of links.`</br> 
		public Vector&lt;URL&gt; | getImageList(int minSize, int maxSize)  <br>`Return the images whose size is between minSize and maxSize.`</br> 
		public Vector&lt;URL&gt; | getImageList(int minSize)  <br>`Return the images whose size is above minSize.`</br> 
		public Vector&lt;URL&gt; | getImageList() Return all the images. <br>`Return the images whose size is above minSize.`</br> 
		public void | exportImageInFile() <br>`Export images.`</br> 
		public void | exportImageInHtml() <br>`Export images in Html format.`</br> 
		public void | printTextInFile(String fileName, String fileContent) <br>`Write fileContent into a file named fileName.`</br> 

- ####**WebContentCrawlerII**

	- Class Summary
		- A class extends ***WebCrawlerII*** and focuses on gaining and exporting the content in the **main body** of website given in a URL.

	- Constructor Summary

		Modifier  | Constructor and Description
		--------- | -------------
		public | WebContentCrawlerII(URL url)
		public | WebContentCrawlerII(URL url, String contentMethod)
		public | WebContentCrawlerII(URL url, String contentMethod, String urlMethod)
		public | WebContentCrawlerII(String urlString)
		public | WebContentCrawlerII(String urlString, String contentMethod)
		public | WebContentCrawlerII(String urlString, String contentMethod, String urlMethod)

	- Element Summary


		Modifer and Type | Element and Description
		-----|-----
		String | text <br>`The text in the main body.`</br> 
		String | textInHtml <br>`The text organized in Html format in the main body`</br> 

	- Method Summary

		Modifer and Type | Element and Description
		----- | -----
		public void | getContentBuiltIn() <br>`Require the content in the main body of website using HttpURLConnection.`</br> 
		public void | getContentHttpClient() <br>`Require the content in the main body of website using HttpClient.`</br> 
		public void | filterText() <br>`Filter the content of main body from original content.`</br> 
		public String | getContentText() <br>`Return the text in the main body.`</br> 
		public void | printTextInHtml(String fileName) <br>`Export the text of main body in Html into a file named fileName.`</br> 
		public void | printTextInPlainText(String fileName) <br>`Export the text of main body into a file named fileName.`</br> 
public void | printTextInHtml()  <br>`Export the text of main body in Html into a file using a default name.`</br> 
public void | printTextInPlainText() <br>`Export the text of main body into a file using a default name.`</br> 

------


##Example

- Code

	```
	aUrl = new URL("http://www.ifanr.com/458506");
	WebContentCrawlerII crawler = new(WebContentCrawlerII(aUrl, WebCrawlerII.USE_HTTP_CLIENT, WebCrawlerII.USE_SOUP_PARSER);
	for (URL link : crawler.getLinkList()) {
			System.out.println(link.toString());
	}			System.out.println(crawler.getContentText());
	crawler.exportImageInHtml();
	crawler.exportImageInFile();
	crawler.printTextInHtml();
	crawler.printTextInPlainText(); 
	```

- Expected Result

	<pre>
	.
	+-- txt
	|   +-- WebContent.txt
	|   +-- WebContent.html	
	+-- img
	|   +-- 37.jpg
	|   +-- 210.jpg
	|   +-- D1.jpg
	|   +-- images.html
	</pre>


> Written with [StackEdit](https://stackedit.io/).