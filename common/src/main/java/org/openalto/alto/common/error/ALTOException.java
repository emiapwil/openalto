package org.openalto.alto.common.error;

/**
 * ALTOException: the base class for all ALTO related exceptions.
 * */
public class ALTOException extends Exception {

    private ErrorCode m_errorCode;

    public ALTOException(ErrorCode code) {
        m_errorCode = code;
    }

    public ALTOException(String errorCode) {
        m_errorCode = new ErrorCode(errorCode);
    }

    public ErrorCode getErrorCode() {
        return m_errorCode;
    }
}
