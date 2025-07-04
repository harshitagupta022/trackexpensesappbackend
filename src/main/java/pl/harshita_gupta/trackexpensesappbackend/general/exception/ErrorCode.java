package pl.harshita_gupta.trackexpensesappbackend.general.exception;

public enum ErrorCode {

    W001("W001", "WALLET_RETRIEVING_ERROR", 200),
    W003("W003", "WALLET_NOT_FOUND", 404),
    W004("W004", "WALLETS_LIST_LIKE_NAME_NOT_FOUND_EXC_MSG", 404),
    W005("W005", "USER_IS_NOT_WALLET_OWNER", 403),

    FT001("FT001", "FINANCIAL_TRANSACTION_NOT_FOUND", 404),
    FT002("FT002", "FINANCIAL_TRANSACTION_TYPE_DOES_NOT_MATCH_WITH_CATEGORY_TYPE", 400),

    FTC001("FTC001", "FINANCIAL_TRANSACTION_CATEGORY_NOT_FOUND", 404),

    TEA001("TEA001", "INTERNAL_SERVER_ERROR", 500),
    TEA002("TEA002", "ENDPOINT_DOES_NOT_EXISTS", 400),
    TEA003("TEA003", "VALIDATION_FAILED", 400),
    TEA004("TEA004", "THROWABLE_EXCEPTION", 500),

    U001("U001", "USER_ALREADY_EXISTS", 400),
    U002("U002", "INVALID_EMAIL_FORMAT", 400),
    U003("U003", "PASSWORD_DOES_NOT_MEET_REQUIREMENTS", 400),
    U004("U004", "PASSWORD_TOO_SHORT", 400),
    U005("U005", "USER_NOT_FOUND", 404),
    U006("U006", "WRONG_CREDENTIALS", 401),
    U007("U007", "PASSWORD_TOO_LONG", 400),

    S001("S001", "UNAUTHORIZED", 401),
    S002("S002", "FORBIDDEN", 403),
    S003("S003", "REFRESH_TOKEN_NOT_VALID", 403);

    private final String businessStatus;
    private final String businessMessage;
    private final Integer businessStatusCode;

    ErrorCode(String businessStatus, String businessMessage, Integer businessStatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessStatusCode = businessStatusCode;
    }

    public String getBusinessStatus() {
        return this.businessStatus;
    }

    public String getBusinessMessage() {
        return this.businessMessage;
    }

    public Integer getBusinessStatusCode() {
        return this.businessStatusCode;
    }

}
