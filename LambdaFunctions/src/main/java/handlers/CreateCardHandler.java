package handlers;

import accessDB.CardsDAO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import models.Card;
import models.GatewayResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
public class CreateCardHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "POST,DELETE,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin",  "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);

        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        CardsDAO dao = new CardsDAO();
        Card card;
        int status;
        try {
            //final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            // Implement body here
            JSONObject event = (JSONObject) parser.parse(reader);
            card = new Gson().fromJson(event.get("body").toString(), Card.class);

            JSONObject responseBody = new JSONObject();

            int card_id = dao.addCard(card);
            card.setCardID(card_id);
            status = 200;

        } catch (ParseException pe) {
            card = null;
            status = 500;
        } catch (Exception e) {
           card = null;
           status = 501;
        }
        //PrintWriter pw = new PrintWriter(outputStream);
        responseJson.put("body", new Gson().toJson(card));
        responseJson.put("statusCode", status);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

        writer.write(responseJson.toJSONString()); //responseJson.toString());
        writer.close();
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }


}
