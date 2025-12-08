package com.codeit_team01.sb07_hrbank_team01.history.utils;

import java.util.Base64;

public class CursorUtils {
    //id를 커서로 인코딩
    public static String encodeCursor(Long id){
        if(id==null){
            return null;
        }
        String cursorJson = String.format("{\"id\":%d}", id);
        return Base64.getEncoder().encodeToString(cursorJson.getBytes());
    }
    //커서를 id로 디코딩
    public static Long decodeCursor(String cursor){
        if(cursor==null || cursor.isBlank()){
            return null;
        }
        try{
            String decoded = new String(Base64.getDecoder().decode(cursor));
            String idStr = decoded.replaceAll("[^0-9]", "");
            return Long.parseLong(idStr);
        } catch(Exception e){
            return null;
        }
    }
    private CursorUtils(){
        throw new AssertionError("Cannot instantiate utility class");
    }
}
