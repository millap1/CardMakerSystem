package handlers;

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GetImagesHandlerTest {
	private static final String RESULT = "200";
	
	@Test
	public void testGetImagesHandler() throws IOException, ParseException {
		String SAMPLE_INPUT_STRING = "{\"body\":{}}";
		
		GetImageListInS3Handler handler = new GetImageListInS3Handler();
        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, null);       
        JSONParser parser = new JSONParser();
        JSONObject OutputNode = (JSONObject) parser.parse(output.toString());
        
        Assert.assertEquals(RESULT, OutputNode.get("statusCode").toString());
    }

}
