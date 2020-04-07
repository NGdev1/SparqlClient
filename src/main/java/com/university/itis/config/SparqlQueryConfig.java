package com.university.itis.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.itis.services.SparqlHttpClient;
import com.university.itis.utils.UriStorage;
import com.university.itis.utils.PrefixesStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SparqlQueryConfig {

    private ObjectMapper mapper = new ObjectMapper();

    public SparqlQueryConfig() {
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    SparqlHttpClient configureSparqlHttpClient() {
        String endpointUrl;
        try {
            InputStream resource = getClass()
                    .getClassLoader().getResourceAsStream("configprops.json");

            assert resource != null;
            JsonNode tree = mapper.readTree(resource);
            endpointUrl = tree.path("endpointUrl").asText();
        } catch (IOException e){
            System.out.println("Unable to read config: " + e.getMessage());
            return null;
        }

        return new SparqlHttpClient(endpointUrl);
    }

    PrefixesStorage configurePrefixes() {
        TypeReference<LinkedHashMap<String, String>> typeReference = new TypeReference<LinkedHashMap<String, String>>(){};
        Map<String, String> prefixes;
        try {
            InputStream resource = getClass()
                    .getClassLoader().getResourceAsStream("configprops.json");

            assert resource != null;
            JsonNode tree = mapper.readTree(resource);
            prefixes = mapper.readValue(tree.path("prefixes").toString(), typeReference);
        } catch (IOException e){
            System.out.println("Unable to read config: " + e.getMessage());
            return null;
        }

        return new PrefixesStorage(prefixes);
    }

    UriStorage configureUriStorage() {
        TypeReference<LinkedHashMap<String, String>> typeReferenceClasses = new TypeReference<LinkedHashMap<String, String>>(){};
        Map<String, String> classes;
        try {
            InputStream resource = getClass()
                    .getClassLoader().getResourceAsStream("classes.json");

            assert resource != null;
            classes = mapper.readValue(resource, typeReferenceClasses);
        } catch (IOException e){
            System.out.println("Unable to read config: " + e.getMessage());
            return null;
        }

        TypeReference<List<String>> typeReferenceBlackList = new TypeReference<List<String>>(){};
        List<String> blacklist;
        try {
            InputStream resource = getClass()
                    .getClassLoader().getResourceAsStream("blacklist.json");

            assert resource != null;
            blacklist = mapper.readValue(resource, typeReferenceBlackList);
        } catch (IOException e){
            System.out.println("Unable to read config: " + e.getMessage());
            return null;
        }

        return new UriStorage(classes, blacklist);
    }
}
