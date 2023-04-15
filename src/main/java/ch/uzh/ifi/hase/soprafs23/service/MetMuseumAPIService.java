package ch.uzh.ifi.hase.soprafs23.service;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class MetMuseumAPIService {
    private Random rand = new SecureRandom();

    private String apiUrlImage = "https://collectionapi.metmuseum.org/public/collection/v1/objects/"; // +objectID


    /**
     * method which returns an Url to a jpg form of an image of the met Museum
     */
    public String getImageFromMetMuseum(){
        List<Integer> objectIDList = readFile();
        Integer objectID = objectIDList.get(this.rand.nextInt(objectIDList.size()));
        String imageUrl = getImageUrl(objectID);

        return imageUrl;
    }

    /**
     * method which reads text file which contains ObjectsIDs of the met Museum images
     */
    private List<Integer> readFile() {
        String fileName ="src/main/resources/metMuseumObjectIDs.txt";
        List<Integer> result = new ArrayList<>();
        try {
            // remove all empty lines in metMuseumObjectID file, else it won't work
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
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
