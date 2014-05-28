package com.qyv_law;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CatchFood {

	public void catchAccount(int year, int fourmonth) {

	}

	public void CatchStart(String page) {

		// System.setProperty("http.proxySet", "true");
		// System.setProperty("http.proxyHost", "127.0.0.1");
		// System.setProperty("http.proxyPort", "8087"); // goagent端口

		try {
			HttpURLConnection urlConnection;
			String webBase = "http://www.boohee.com";
			String urlAddress = page; // 谷薯芋、杂豆、主食

			URL url = new URL(urlAddress);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(false);
			urlConnection.setRequestProperty("charset", "utf-8");
			// urlConnection.setRequestProperty("Accept-Encoding", "gzip");
			urlConnection.connect();
			DataOutputStream out = new DataOutputStream(urlConnection
					.getOutputStream());

			// InputStream urlStream = new GZIPInputStream(urlConnection
			// .getInputStream());
			//			
			InputStream urlStream = urlConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlStream));

			String line = "";
			String response = "";
			while ((line = reader.readLine()) != null) {
				response = response + line;
			}
			reader.close();
			urlStream.close();
			urlConnection.disconnect();

			List<String> strlist = null;
			List<String> strname = null;
			List<String> strPic = null;

			int end = 0;
			int start = 0;
			String tmpString = "";

			strlist = new ArrayList();
			strname = new ArrayList();
			strPic = new ArrayList();

			int whiletimes = 10;

			// while抓取一页的信息存起来
			while (response.contains("intr float-right") && whiletimes > 0) {

				whiletimes--;

				// href
				response = response.substring(response
						.indexOf("intr float-right"));
				response = response.substring(response.indexOf("href"));
				response = response.substring(response.indexOf("/"));
				end = response.indexOf("\"");
				start = 0;
				tmpString = "";
				tmpString = response.substring(start, end);
				tmpString = webBase + tmpString;
				strlist.add(tmpString);

				// name
				response = response.substring(response.indexOf("gray"));
				response = response.substring(response.indexOf(">"));
				end = response.indexOf("<");
				start = 1;
				tmpString = "";
				tmpString = response.substring(start, end);
				strname.add(tmpString);

			}// while

			// while抓取一页图像结束 开始访问不同网页 存不同图片
			// for
			for (int j = 0; j < strlist.size(); j++) {
				urlAddress = strlist.get(j); // 谷薯芋、杂豆、主食
				url = new URL(urlAddress);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				urlConnection.setRequestMethod("GET");
				urlConnection.setUseCaches(false);
				urlConnection.setInstanceFollowRedirects(false);
				urlConnection.setRequestProperty("charset", "utf-8");
				// urlConnection.setRequestProperty("Accept-Encoding", "gzip");
				urlConnection.connect();
				out = new DataOutputStream(urlConnection.getOutputStream());

				// InputStream urlStream = new GZIPInputStream(urlConnection
				// .getInputStream());
				//			
				urlStream = urlConnection.getInputStream();
				reader = new BufferedReader(new InputStreamReader(urlStream));

				line = "";
				response = "";
				while ((line = reader.readLine()) != null) {
					response = response + line;
				}
				reader.close();
				urlStream.close();
				urlConnection.disconnect();

				response = response.substring(response
						.indexOf("class=\"food-illus"));
				response = response.substring(response.indexOf("href="));
				response = response.substring(response.indexOf("'"));
				response = response.substring(1);
				end = response.indexOf("'");
				start = 0;
				tmpString = "";
				tmpString = response.substring(start, end);
				strPic.add(tmpString);

				BufferedInputStream in = null;
				FileOutputStream file = null;

				try {
					System.out.println("获取网络图片");
					System.out.println(strname.get(j));
					System.out.println(strlist.get(j));
					String filePath = "d:/image/";// 图片存储的位置

					url = new URL(tmpString);

					in = new BufferedInputStream(url.openStream());

					file = new FileOutputStream(new File(filePath
							+ strname.get(j)+".jpg"));
					int t;
					while ((t = in.read()) != -1) {
						file.write(t);
					}
					file.close();
					in.close();
					System.out.println("图片获取成功");

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				int m = 1;
				m = m + 1;

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		CatchFood cf = new CatchFood();
		for(int i=1;i<11;i++)
		{
			//group 2 此处修改后抓取不同种类
			String page="http://www.boohee.com/food/group/10?page=";
			page=page+String.valueOf(i);
			cf.CatchStart(page);
		}
		

	}

}
