package org.openalto.alto.common.error;

/*
 * ErrorCode: the error code for ALTO services
 *
 * See https://tools.ietf.org/html/rfc7285#page-25 for details.
 *
 * */
public class ErrorCode {
    public static final
    String E_SYNTAX_STR = "E_SYNTAX";

    public static final
    String E_MISSING_FIELD_STR = "E_MISSING_FIELD";

    public static final
    String E_INVALID_FIELD_TYPE_STR = "E_INVALID_FIELD_TYPE";

    public static final
    String E_INVALID_FIELD_VALUE_STR = "E_INVALID_FIELD_VALUE";

    public static final
    ErrorCode E_SYNTAX = new ErrorCode(E_SYNTAX_STR);

    public static final
    ErrorCode E_MISSING_FIELD = new ErrorCode(E_MISSING_FIELD_STR);

    public static final
    ErrorCode E_INVALID_FIELD_TYPE = new ErrorCode(E_INVALID_FIELD_TYPE_STR);

    public static final
    ErrorCode E_INVALID_FIELD_VALUE = new ErrorCode(E_INVALID_FIELD_VALUE_STR);


    private String m_errorCodeStr;

    public ErrorCode(String errorCodeStr) {
        m_errorCodeStr = errorCodeStr;
    }

    public ErrorCode(ErrorCode rhs) {
        m_errorCodeStr = rhs.m_errorCodeStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ErrorCode))
            return false;
        ErrorCode rhs = (ErrorCode)obj;
        return (this.toString().equals(rhs.toString()));
    }

    @Override
    public String toString() {
        return m_errorCodeStr;
    }
}
