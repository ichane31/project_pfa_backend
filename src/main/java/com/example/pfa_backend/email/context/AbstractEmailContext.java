package com.example.pfa_backend.email.context;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractEmailContext {

    private String from;
    private String to;
    private String subject;
    private String email;
    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;


    public AbstractEmailContext() {
        this.context = new HashMap<>();
    }

    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
    }

    public Object put(String key, Object value) {
        return key ==null ? null : this.context.put(key.intern(),value);
    }

    public abstract <T> void init(T context, String password);
}