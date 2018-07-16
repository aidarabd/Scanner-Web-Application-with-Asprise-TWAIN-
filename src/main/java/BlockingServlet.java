import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BlockingServlet extends HttpServlet {

    String scannerType;









    public String scan() throws IOException {


        Properties prop = new Properties();
        InputStream input = null;
        OutputStream output = null;
        try {

            input = new FileInputStream("config.properties");
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            String bool = prop.getProperty("scannerType");
            if (bool == null) {
                    scannerType="select";
            }
            else {
                scannerType=bool;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }





        Map<String, Object> map = new HashMap<>();
        map.put("status", "error");
        try {
            System.out.println("worked");

            Imaging imaging = new Imaging("myApp", 0);
            //String defaultSrc = imaging.scanSelectDefaultSource(null);

            String defaultSourceName = imaging.scanGetDefaultSourceName();
            Result result = imaging.scan(Request.fromJson(
                    "{"
                            + "\"output_settings\" : [ {"
                            + "  \"type\" : \"return-base64\","
                            + "  \"format\" : \"jpeg\","
                            + "\"jpeg_quality\": \"60\""
                            //+ "  \"save_path\" : \"E:/Internship/WebScanner/src/main/resources/static/images/test.jpg\""
                            + "} ]"



                            + "}"), scannerType,
                    false, false);
            map.put("status", "ok");
            String newResult = (result.getOutputItems().toString());
            String resultCut = newResult.substring(73, newResult.length() -3);
            map.put("image", "data:image/jpeg;base64,"+resultCut);

            if(scannerType.equals("select")){
                output=new FileOutputStream("config.properties");
                prop.setProperty("scannerType", defaultSourceName);
                prop.store(output, null);
                output.close();
            }
            return ("data:image/jpeg;base64,"+resultCut);

        }
        catch (Exception e){ System.out.println("is used" + e.getMessage());
            map.put("errorCode", "something went wrong");
            return "something went wrong";
        }
    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(scan());

    }
}