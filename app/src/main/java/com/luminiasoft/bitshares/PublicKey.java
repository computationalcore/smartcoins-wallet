package com.luminiasoft.bitshares;

import com.luminiasoft.bitshares.interfaces.ByteSerializable;
import org.bitcoinj.core.ECKey;

/**
 * Created by nelson on 11/30/16.
 */
public class PublicKey implements ByteSerializable {
    private ECKey publicKey;

    public PublicKey(ECKey key) {
//        if(!key.isPubKeyOnly()){
//            throw new IllegalStateException("Passing a private key to PublicKey constructor");
//        }
        this.publicKey = key;
    }

    public ECKey getKey(){
        return publicKey;
    }

    @Override
    public byte[] toBytes() {
        return publicKey.getPubKey();
    }

    public String getAddress(){
        return new Address(publicKey).toString();
    }
}
