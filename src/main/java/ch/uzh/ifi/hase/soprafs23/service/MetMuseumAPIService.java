package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.ObjectIDPaths;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MetMuseumAPIService {
    private Random rand = new SecureRandom();

    private String apiUrlImage = "https://collectionapi.metmuseum.org/public/collection/v1/objects/"; // +objectID


    /**
     * method which returns an Url to a jpg form of an image of the met Museum
     */
    public String getImageFromMetMuseum(String category){
        String filePath = getFilePath(category);
        List<Integer> objectIDList = readFile(filePath);
        Integer objectID = objectIDList.get(this.rand.nextInt(objectIDList.size()));

        return getImageUrl(objectID);
    }

    private String getFilePath(String category){
        if (category.equals("landscape")){
            return ObjectIDPaths.LANDSCAPE_CATEGORY;
        }
        else if (category.equals("portrait")){
            return ObjectIDPaths.PORTRAIT_CATEGORY;
        }
        else if (category.equals("postcard")){
            return ObjectIDPaths.POSTCARD_CATEGORY;
        }
        else if(category.equals("still life")){
            return  ObjectIDPaths.STILLLIFE_CATEGORY;
        }
        else if(category.equals("abstract art")){
            return ObjectIDPaths.ABSTRACTART_CATEGORY;
        }
        else {
            return ObjectIDPaths.RANDOM_CATEGORY;
        }
    }

    /**
     * method which reads text file which contains ObjectsIDs of the met Museum images
     */
    private List<Integer> readFile(String filePath) {
        List<Integer> result = new ArrayList<>();
        try {
            // remove all empty lines in metMuseumObjectID file, else it won't work
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String curLine;
            while ((curLine = bufferedReader.readLine()) != null){
                result.add(Integer.valueOf(curLine));
            }
            bufferedReader.close();
            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method which returns the URL to jpg image
     * @param objectID
     * @return
     */
    private String getImageUrl(Integer objectID){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(apiUrlImage + objectID);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity);
            JSONObject result = new JSONObject(entityString);
            String urlString = result.getString("primaryImage");

            // close instances of CloseableHttp
            response.close();
            httpclient.close();
            return urlString;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
