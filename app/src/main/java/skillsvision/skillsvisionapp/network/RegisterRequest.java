package skillsvision.skillsvisionapp.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://192.168.8.107:8080/skillsvision/register_user.php/";
    private Map<String, String> params;

    public RegisterRequest(String emailadress, String password, Date birthdate, String country, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("emailadress", emailadress);
        params.put("password", password);
        params.put("birthdate", birthdate + "");
        params.put("country", country);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
