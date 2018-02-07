package test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

@Path("/services")
public class GeoJSonService{
    
   @Path("/own")
   @GET
   @Produces(MediaType.TEXT_PLAIN)   
   public static String searchPath(@QueryParam("originLat") double lLat, @QueryParam("originLon") double lLon, @QueryParam("destinationLat") double dLat, @QueryParam("destinationLon") double dLon) throws IOException {
     String response = "";        
        response = generateGeoJson(lLat, lLon,dLat,dLon);
        System.out.println(response);            
        return response;
    }
    
    public static String generateGeoJson(double lla, double llo, double dla, double dlo ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode obj = mapper.createObjectNode(); 
        FeatureCollection collection = new FeatureCollection();
        Feature feat = new Feature();
        LineString geom = new LineString();
        geom.add(new LngLatAlt(llo, lla));
        geom.add(new LngLatAlt(dlo, dla));        
        Map<String, Double> costMap = new HashMap();
        costMap.put("Distance", 100.);
         costMap.put("Travel_Time", 1000.);
        feat.setProperty("costs", costMap);
        feat.setProperty("instructions", new LinkedList());
        feat.setGeometry(geom);
        collection.add(feat);       
        try {
            obj.putPOJO("data", collection);                    
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
                System.err.println("Error processing Timed_Dijkstra response Json.");          
            }
        return null;
    }
    
}
