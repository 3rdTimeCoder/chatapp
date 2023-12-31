package chatapp.communication.response;

import java.util.HashMap;

public class Response {

    private String result;
    private HashMap<String, Object> data;

     /**
     * Constructs a Response object with the specified result and data.
     *
     * @param result The result of the response.
     * @param data   The data associated with the response.
     */
 
     public Response(String result, HashMap<String, Object> data) {
        this.result = result;
        this.data = data;
     }

     /**
     * Returns the result associated with the response.
     *
     * @return The result associated with the response.
     */
     public String getResult() {
         return result;
     }
    
     /**
     * Returns the data associated with the response.
     *
     * @return The data associated with the response.
     */
    public HashMap<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tresult: '" + result + '\'' + ",\n" +
                "\tdata: " + data  + ",\n" +
                "}";
    }

    public static void main(String[] args) {
        Data data = new Data("testId", "This is a message.");
        Response res = new Response("OK", data.getData());
        System.out.println(res);
    }
}
