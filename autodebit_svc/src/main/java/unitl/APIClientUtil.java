package unitl;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class APIClientUtil {

    public static final Charset C_UTF8 = StandardCharsets.UTF_8;

    public static String apiDateTimeClient() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return dateFormat.format(new Date());
    }

    public static String generateUUIDV4() {
        return UUID.randomUUID().toString();
    }

    private static String encodeSignature(String macKey, String payload) {
        try {
            byte[] byteMacKey = macKey.getBytes(C_UTF8);
            byte[] bytePayload = payload.getBytes(C_UTF8);
            Mac macSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(byteMacKey, macSHA256.getAlgorithm());
            macSHA256.init(secretKeySpec);
            byte[] signedPayload = macSHA256.doFinal(bytePayload);
            return Base64.encodeBase64String(signedPayload);

        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public static String buildSignatureString(String url, String method, String txnId, long created, String digest) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.delete(0,builder.length());
            builder.append("digest: ").append(digest).append("\n");
            builder.append("(request-target): ").append(method.toLowerCase() + " ").append(url).append("\n");
            builder.append("(created): ").append(created).append("\n");
            builder.append("x-client-transaction-id: ").append(txnId);
            return builder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    public static String generateSignature(long created,  String hMacKeyBilling,String strSignature){
        try {
           // log.info("HMacBilling Key {}", hMacKeyBilling);
            StringBuilder signBuilder = new StringBuilder();
            signBuilder.delete(0, signBuilder.length());
            signBuilder.append("keyId=\"key1\",")
                    .append("algorithm=\"hs2019\",")
                    .append("created=").append(created).append(",")
                    .append("expires=").append(created).append(",")
                    .append("headers=\"digest (request-target) (created) x-client-transaction-id\"").append(",")
                    .append("signature=\"").append(encodeSignature(hMacKeyBilling,strSignature)).append("\"");
            return signBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    public static Long created() {
        long current = new Date().getTime();
        return current;
    }

    public static String buildDigest(String body) {
        try {
            //log.info("Body To Hash {}", body);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteTextToHash = body.getBytes("UTF-8");
            byte[] byteHashing = digest.digest(byteTextToHash);
            String encode = "SHA-256" + "=" + Base64.encodeBase64String(byteHashing);
            return encode;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }
}
