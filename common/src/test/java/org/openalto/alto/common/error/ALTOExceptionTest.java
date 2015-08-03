package org.openalto.alto.common.error;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ALTOExceptionTest {

    @Test
    public void testALTOException() {
        try {
            throw new ALTOException(ErrorCode.E_SYNTAX);
        } catch (ALTOException e) {
            assertEquals(e.getErrorCode(), ErrorCode.E_SYNTAX);
        }

        try {
            throw new ALTOException("E_SYNTAX");
        } catch (ALTOException e) {
            assertEquals(e.getErrorCode(), ErrorCode.E_SYNTAX);
        }
    }
}
