package unitl;

public class Constant {


    public static String SUCCESS_CODE = "00";
    public static String SUCCESS_CODE_EE = "EE";
    public static String SUCCESS_CODE_DONE = "00";
    public static String UN_SUCCESS_CODE = "05";
    public static String UN_NO_CODE = "01";
    public static String SUCCESS_MESSAGE = "SUCCESS";
    public static String UN_SUCCESS_MESSAGE = "UN_SUCCESS";
    public static String No_DATA = "Do Not Found";
    public static String UN_VALIDATE_MESSAGE = "CAN_NOT_VALIDATE";
    public static String UN_FAIL_MESSAGE = "CAN_NOT_PAYMENT";
    public static String API_SUCCESS_CODE = "00";
    public static String API_ERROR_CODE = "05";

    public static  String D_FAIL = "Fail";
    public static class Response{
        public static final String INTERNAL_ERROR="E500"; // Use for internal server error only
        public static final String USER_NOT_FOUND="E404"; // Use for when user Authentication not found
        public static final String AUTHORIZATION_FAILED = "E401"; //User for Un-Authorize Failed
        public static final String SUCCESS = "00"; // For success response
        public static final String SUCCESS_MESSAGE= "SUCCESS";
    }

    public static class BILL_RESPONSE{
        public static final String AMOUNT_NOT_VALID="AMT_INV"; //Amount Of Payment Invalid
        public static final String CANNOT_INQUIRY_BILL = "INF-ERR"; // Bill Get Info Error
        public static final String ACCOUNT_VALIDATE_ERR="ACC-ERR"; // Bill Account Not Found Or Not Active
        public static final String CCY_VALIDATE_ERR="CCY-ERR"; // Client Account CCY is match
        public static final String TXN_REFERENCE_FAILED = "REF-INV"; //Bill Transaction ERROR
        public static final String PAYMENT_FAILED ="TXN-ERR"; // Transaction Error
        public static final String INSUFFICIENT_FUND="TXN-IFT"; //INSUFFICIENT FUND
        public static final String REVERT_OK="TXN-REVERT";// TRANSACTION REVERT OK
        public static final String REVERT_FAILED = "TXN-REVERT-FAILED"; //TRANSACTION REVERT FAILED
        public static final String REQUEST_AMOUNT_NOT_ALLOW="AMOUNT_NOT_POSSIBLE"; //The request amount is less than balance cannot process to payment
        public static final String BILLER_NOT_ACTIVE = "INF-DIS"; //Bill Not Active

    }
    public static class RESPONSE_CODE{
        public static String ERROR="05";
        public static String SUCCESS="00";
    }
    public static class RESPONSE_MESSAGE{
        public static String SUCCESS="SUCCESS";
    }

        public static class CoreBankResMessage {
            //ຍອດເງິນບໍ່ພຽງພໍ
            public static String INSUFFICIENT_FUND = "OVERRIDE WITHDRAWL MAKES AC BAL LESS THAN MIN BAL";
            public static String ACCOUNT_INACTIVE = "Transaction code 175";
        }

        public static class TrustCode {

            public static class RoadTax {
                //public static String  ROADTAX_CONNECTION_ISSUE = "RT_101";

                public static String UNKNOWN_ERROR = "LT_999";
                public static String ROADTAX_CONNECTION_ISSUE = "RT_101";
                public static String ROADTAX_DONOT_FOUND ="RT_102";
                public static String ROADTAX_DO_VITYPE_FOUND ="RT_103";
                public static String TXN_TYPE_INVALID ="LT_999";
                public static String TXN_REVERT ="LT_9910";
                public static String TXN_PREVIUOS ="Y_991";
            }
            public static class easyTax {
                public static String EASYTAX_UNKNOWN_ERROR = "E_999";
                public static String EASYTAX_CONNECTION_ISSUE = "E_101";
                public static String EASYTAX_DONOT_FOUND ="E_102";

                public static String EASYTAX_TXN_TYPE_INVALID ="E_TXN_999";
                public static String EASYTAX_TXN_REVERT ="E_9910";
                public static String EASYTAX_TXN_PAD ="EP_103";
                public static String EASYTAX_NOTFOUND ="E_104";
                public static String EASYTAX_BAD_REQ ="E_105";
                public static String EASYTAX_BAD_UN ="E_401";
                public static String EASYTAX_BAD_FOB ="E_403";
                public static String HOLD_TXN_FAIL ="E_F101";
            }

            public static class LandTax {
                public static String READY_PAYMENT = "LT_991";
                public static String UNKNOWN_ERROR = "LT_999";
                public static String TXN_PAID = "LT_9911";
                public static String NOT_FOUND = "LT_404";
                public static String DATA_NOT_FOUND = "LT_103";
                public static String TOKEN_NOT_FOUND = "LT_101";
                public static String TOKEN_INVALID = "LT_102";
                public static String CREDIT_CURRENCY_NOT_ALLOW = "LT_107";
                public static String PAYMENT_AMOUNT_NOT_POSSIBLE = "LT_108";
                public static String LANDTAX_CONNECTION_ISSUE = "RT_101";
                public static String LANDTAX_PROCESS_PAY = "LT_110";
                public static String LANDTAX_TXN_FAIL = "LT_F101";
            }

            public static class CardZone {
                public static String TOKEN_NOT_FOUND = "LT_101";
                public static String TOKEN_INVALID = "LT_102";
            }

            public static class FinLink {
                public static String REFERENCE_NOT_FOUND = "LT_103";
                public static String UNKNOWN_ERROR = "LT_999";
                public static String MISMATCH_DO_CID = "LT_106";
                public static String LAND_TAX_YEAR_MISMATCH = "LT_109";
                public static String LAND_TAX_ALREADY_PAID = "LT_110";
            }

            public static class CoreBank {
                public static String INSUFFICIENT_FUND = "LT_104";
                public static String ACCOUNT_INACTIVE = "LT_105";
                public static String TXN_REVERT = "LT_105";
            }

            public static class InFarSoleTax {
                public static String UNKNOWN_ERROR = "LT_999";
                public static String INFARSOLE_CONNECTION_ISSUE = "RT_101";

            }
            public static class NSAW {
                public static String UNKNOWN_ERROR = "LT_999";
                public static String  NSAW_CONNECTION_ISSUE = "RT_101";
                public static String INVALID_SOI_FAIL="SP_001";
                public static String INVALID_SOI_DATA="NSA_101";
            }

            public static class CZ{
                public static String INVALID_ACCOUNT_OWNER="LT_111";
                public static String WALLET_PREPAID_NOT_ALLOW ="LT_112";

            }
        }

        public static class TrustLog {
            public static String REQUEST = "REQUEST";
            public static String RESPONSE = "RESPONSE";
            public static String ERROR = "ERROR";
        }

}
