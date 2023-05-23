package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EnvisageConstants;
import ch.uzh.ifi.hase.soprafs23.entity.Keywords;
import ch.uzh.ifi.hase.soprafs23.exceptions.KeywordsLimitException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@Transactional
public class DalleAPIService {
    private final String apiKey = System.getenv("DALLE_API_KEY");
    private int numImages = 1;
    private String responseFormat = "url"; //url or b64_json
    private String apiUrl = "https://api.openai.com/v1/images/generations";

    public enum ImageSize {
        SMALL(256, "256x256"),
        MEDIUM(512, "512x512"),
        LARGE(1024, "1024x1024");

        public final int size;
        public final String strSize;

        ImageSize(int i, String s) {
            this.size = i;
            this.strSize = s;
        }
    }

    public JSONObject getImageFromDALLE(Keywords keywords) { //its not blob
        String prompt = keywords.getKeywords();
        if (prompt.length() > EnvisageConstants.MAX_KEYWORDS_LENGTH){
            throw new KeywordsLimitException(prompt.length());
        }
        StringEntity promptUTF8 = new StringEntity(keywords.getKeywords(), StandardCharsets.UTF_8);
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(apiUrl);

// Set headers
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("Authorization", "Bearer " + apiKey);

// Set request body
            // Define the JSON request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("prompt", promptUTF8);
            requestBody.put("n", numImages);
            requestBody.put("size", ImageSize.SMALL.strSize);
            requestBody.put("response_format", responseFormat);

// Create a StringEntity from the JSON request body
            StringEntity requestEntity = new StringEntity(requestBody.toString());

// Set the content type of the request body
            requestEntity.setContentType("application/json");
            httppost.setEntity(requestEntity);

// Send request and get response
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

// Parse response JSON to get image URL
            JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
            jsonResponse.put("DUMMY_VAR", System.getenv("DUMMY_ENV_VAR"));
            jsonResponse.put("request", requestBody);
// Download image and save locally
// Clean up resources
            EntityUtils.consume(entity);
            response.close();
            httpclient.close();
            return jsonResponse;

        }
        catch (IOException e) {
            return new JSONObject(e.toString());
        }
    }
}