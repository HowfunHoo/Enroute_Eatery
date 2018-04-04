package com.enroute.enroute.interfaces;

import org.json.JSONObject;

/**
 * This interface callbacks the JSON Object Result of searching cuisine IDs
 */
public interface CuisineCallbacks {
    void onCuisineCallbacks(JSONObject JSONObjectResult);
}
