package com.srinivart.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Download {

    private final String USER_AGENT = "Mozilla/5.0";

    private Document document;

    public void downloadMedia(String url, String path){

           Validate.validateURL(url);

           try{
               document = Jsoup.connect(url).userAgent(USER_AGENT).get();
               String mediaType = document.select("meta[name=medium]").first()
                       .attr("content");
               switch (mediaType) {
                   case "image":
                       downloadImage(url, path);
                       break;
                   case "video":
                       downloadVideo(url, path);
                       break;
                   default:
                       System.out.println("Unable to download media file.");
                       break;
               }

           } catch (IOException e) {
               e.printStackTrace();
           }
    }

    public void downloadImage(String url, String path){
        String imageUrl = "";

        Validate.validateURL(url);
        try {
            document = Jsoup.connect(url).userAgent(USER_AGENT).get();
            imageUrl = document.select("meta[property=og:image]").first()
                    .attr("content");

        } catch (IOException e) {
            e.printStackTrace();
        }

        download(imageUrl, path);
    }

    public void downloadVideo(String url, String path){
        String videoUrl = "";

        Validate.validateURL(url);
        try {
            document = Jsoup.connect(url).userAgent(USER_AGENT).get();
            videoUrl = document.select("meta[property=og:video]").first()
                    .attr("content");

        } catch (IOException e){
            e.printStackTrace();
        }

        download(videoUrl, path);
    }

    public String getDownloadUrl(String url){
        String downloadUrl = "";

        Validate.validateURL(url);
        try{
            document = Jsoup.connect(url).userAgent(USER_AGENT).get();
            String mediaType = document.select("meta[name=medium]").first()
                    .attr("content");

            switch (mediaType) {
                case "image":
                    downloadUrl = document.select("meta[property=og:image]").first()
                            .attr("content");
                    break;
                case "video":
                    downloadUrl = document.select("meta[property=og:video]").first()
                            .attr("content");
                    break;
                default:
                    downloadUrl = "No media file found.";
                    break;
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        return downloadUrl;
    }

    private void download(String url, String path){
        String[] tempName = url.split("/");
        String filename = tempName[tempName.length-1].split("[?]")[0];

        try(InputStream inputStream = URI.create(url).toURL().openStream()){
            HttpURLConnection conn = (HttpURLConnection)URI.create(url).toURL().openConnection();

            Path targetPath = new File(path + File.separator + filename).toPath();
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            int BYTES_PER_KB = 1024;
            double fileSize = ((double)conn.getContentLength() / BYTES_PER_KB);

            System.out.println("Media file downloaded successfully.");
            System.out.println(String.format("Media Location: %s", targetPath));
            System.out.println(String.format("Media Name: %s", filename));
            System.out.println(String.format("Media Size: %.2f kb", fileSize));
            System.out.println(String.format("Media Type: %s", Validate.mediaType(filename)));

        } catch (IOException e){
            e.printStackTrace();
        }
    }




}
