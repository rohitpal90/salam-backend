package com.salam.dms.config.auth;


import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class ReadJsonFromFile {

    public JSONObject read() throws IOException, JSONException {
        ClassPathResource staticDataResource = new ClassPathResource("response.json");
        String staticDataString = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
       return  new JSONObject(staticDataString);
    }
}
