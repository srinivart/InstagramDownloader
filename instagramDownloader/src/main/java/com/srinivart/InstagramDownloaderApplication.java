package com.srinivart;

import com.srinivart.download.Download;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstagramDownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramDownloaderApplication.class, args);



		Download d = new Download();
		String url="https://www.instagram.com/p/CQH0r3Tgl41";
		String path= "//Users/srinivaspanaganti/Desktop/";

		d.downloadImage(url,path);


	}

}
