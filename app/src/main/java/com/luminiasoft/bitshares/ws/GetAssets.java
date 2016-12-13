package com.luminiasoft.bitshares.ws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luminiasoft.bitshares.Asset;
import com.luminiasoft.bitshares.RPC;
import com.luminiasoft.bitshares.interfaces.WitnessResponseListener;
import com.luminiasoft.bitshares.models.ApiCall;
import com.luminiasoft.bitshares.models.BaseResponse;
import com.luminiasoft.bitshares.models.WitnessResponse;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hvarona on 12/12/16.
 */
public class GetAssets extends WebSocketAdapter {

    private ArrayList<String> assetName;
    private WitnessResponseListener mListener;

    public GetAssets(ArrayList<String> assetName, WitnessResponseListener listener) {
        this.assetName = assetName;
        this.mListener = listener;
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        ArrayList<Serializable> accountParams = new ArrayList<>();
        accountParams.add(assetName);
        ApiCall getAssetCall = new ApiCall(0, RPC.CALL_GET_ASSET, accountParams, "2.0", 1);
        websocket.sendText(getAssetCall.toJsonString());
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        try {
            String response = frame.getPayloadText();
            Gson gson = new Gson();

            Type getAssetResponse = new TypeToken<WitnessResponse<ArrayList<Asset>>>() {
            }.getType();
            WitnessResponse<ArrayList<Asset>> witnessResponse = gson.fromJson(response, getAssetResponse);

            if (witnessResponse.error != null) {
                this.mListener.onError(witnessResponse.error);
            } else {
                this.mListener.onSuccess(witnessResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        websocket.disconnect();
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        mListener.onError(new BaseResponse.Error(cause.getMessage()));
        websocket.disconnect();
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        mListener.onError(new BaseResponse.Error(cause.getMessage()));
        websocket.disconnect();
    }
}