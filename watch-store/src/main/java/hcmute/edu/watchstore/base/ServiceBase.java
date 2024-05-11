package hcmute.edu.watchstore.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.constants.ResponseCode;

public class ServiceBase {
    
    public ResponseEntity<?> success(Object object) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", object);
        response.put("code", ResponseCode.OK.getCode());
        response.put("message", ResponseCode.OK.getMessage());
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> error(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }
}
