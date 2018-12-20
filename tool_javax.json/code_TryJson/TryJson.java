import java.io.*;
import javax.json.*;

public class TryJson {
   public static void main(String[] args) {
      try (
         InputStream is = new FileInputStream(new File( "facebook.json" ));
         JsonReader rdr = Json.createReader(is)
      ) {
         JsonObject obj = rdr.readObject();
         JsonArray results = obj.getJsonArray("data");
         for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            System.out.print(result.getJsonObject("from").getString("name"));
            System.out.print(": ");
            System.out.println(result.getString("message", ""));
            System.out.println("-----------");
         }
      } catch (IOException e) {
         System.out.println( e.getMessage());
      };
   }
}
